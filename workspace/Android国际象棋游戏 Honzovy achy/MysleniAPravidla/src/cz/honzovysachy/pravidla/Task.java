package cz.honzovysachy.pravidla;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import cz.honzovysachy.mysleni.HodnotaPozice;
import cz.honzovysachy.resouces.S;

public class Task extends SavedTask {

	public int hodPos;
	
	private static final long serialVersionUID = 3579393683410168823L;

	// End game constants
	public static final int NO_END = 0;

	public static final int WHITE_WINS_MAT = 1;

	public static final int BLACK_WINS_MAT = 11;

	public static final int DRAW_WHITE_IN_STALEMATE = 21;
	public static final int DRAW_BLACK_IN_STALEMATE = 22;
	public static final int DRAW_MATERIAL = 23;
	public static final int DRAW_50_MOVES = 24;
	public static final int DRAW_3_REPETITION = 25;

	// Move constants
	public static final int MBRoch = (7 << 13);
	public static final int VBRoch = ((7 << 13) | (1 << 11));
	public static final int MCRoch = (15 << 12);
	public static final int VCRoch = (31 << 11);

	public ZasobnikTahu mZasobnikTahu;

	int mIndexVZasobnikuTahu;

	public Vector mZasobnik;

	public int mIndexVZasobniku;

	public Hash mHashF;

	public boolean mExitThinking;
	public long mTimeStart;
	public boolean mNullMove;

	//public Pozice mBoardComputing;

	/**  @return deep copy of the task. */
	public SavedTask getSavedTask() {
		return new SavedTask(mEnd, (Vector)mGame.clone(), mIndexInGame, (Pozice)mBoard.clone(), (Pozice)mBoardComputing.clone());
	}
	
	public Task(SavedTask task) {
		super(NO_END, new Vector(), -1, new Pozice(), new Pozice());
		if (task != null) {
			mEnd = task.mEnd;
			mGame = task.mGame;
			mIndexInGame = task.mIndexInGame;
			mBoard = task.mBoard;
			mBoardComputing = task.mBoardComputing;
			// Update fix! There were crashes when an older app was not uninstalled, just reinstalled 
			// (mBoardComputing == null)
			if (mBoardComputing == null) {
				mBoardComputing = new Pozice(mBoard);
			}
		}
		mIndexVZasobniku = -1;
		mZasobnik = new Vector();
		mZasobnikTahu = new ZasobnikTahu();
		mHashF = new Hash();
		mNullMove = true;
	}

	public String getEndOfGameString(int end) {
		if (end == 0)
			return "";
		String s = new String();
		if (end < 10)
			s = S.g("WHITE_WINS");
		else if (end < 20)
			s = S.g("BLACK_WINS");
		else
			s = S.g("DRAW");
		switch (end) {
		case WHITE_WINS_MAT:
			s += S.g("COMMA_BLACK_CHECKMATED");
			break;
		case BLACK_WINS_MAT:
			s += S.g("COMMA_WHITE_CHECKMATED");
			break;
		case DRAW_WHITE_IN_STALEMATE:
		case DRAW_BLACK_IN_STALEMATE:
			s += S.g("COMMA_STALEMATE");
			break;
		case DRAW_MATERIAL:
			s += S.g("COMMA_MATERIAL");
			break;
		case DRAW_50_MOVES:
			s += S.g("COMMA_50_MOVES_RULE");
			break;
		case DRAW_3_REPETITION:
			s += S.g("COMMA_3_TIMES_REPETITION");
			break;
		}
		return s;
	}

	boolean importantMove() {
		int t = ((ZasobnikStruct) (mGame.elementAt(mIndexInGame + 1))).mTah;
		if ((t & (1 << 15)) != 0) {
			/* nenormalni tah */
			if ((t & (1 << 14)) != 0)
			/* rosada nebo promena pesce */
			{
				if ((t & (1 << 13)) != 0)
					return false;
				else
					return true;
				/* rosada else promena pesce */
			} else
				/* brani mimochodem */
				return true;
		} else
		/* nenormalni tah */
		{
			if (mBoardComputing.sch[t & 127] != 0 || mBoardComputing.sch[t >> 7] == 1
					|| mBoardComputing.sch[t >> 7] == -1)
				return true;
			else
				return false;
		}
	}

	int draw50or3() {

		int i, dupl;
		Pozice pos;

		dupl = 0;
		pos = new Pozice(mBoardComputing);
		for (i = 0; i < 100; i++) {
			if (mIndexInGame < 0)
				break;
			tahniZpet(0, true, null);
			if (importantMove()) {
				i++;
				break;
			}
			if (pos.equals(mBoardComputing))
				if (++dupl == 2) {
					i++;
					break;
				}
			if (i == 99)
				dupl = 49;
		}
		for (; i > 0; i--)
			tahni(0, true, false, null);
		if (dupl < 2)
			dupl = 0;
		else
			dupl++;
		return dupl;
	}

	public int getEndOfGame() {
		int i, bbs, bcs, cbs, ccs, cj, bj;

		Vector v = nalezTahyVector();

		if (v.isEmpty()) {
			if (mBoardComputing.sach())
				return (mBoardComputing.bily ? BLACK_WINS_MAT : WHITE_WINS_MAT);
			return (mBoardComputing.bily ? DRAW_WHITE_IN_STALEMATE
					: DRAW_BLACK_IN_STALEMATE);
		}

		switch (draw50or3()) {
		case 50:
			return DRAW_50_MOVES;
		case 3:
			return DRAW_3_REPETITION;
		}

		bbs = bcs = cbs = ccs = cj = bj = 0;
		for (i = Pozice.a1; i <= Pozice.h8; i++) {
			switch (mBoardComputing.sch[i]) {
			case 1:
				return NO_END;
			case 4:
				return NO_END;
			case 5:
				return NO_END;
			case -1:
				return NO_END;
			case -4:
				return NO_END;
			case -5:
				return NO_END;
			case 2:
				bj++;
				break;
			case -2:
				cj++;
				break;
			case 3:
				if ((((i / 10) + (i % 10)) & 1) != 0)
					bbs++;
				else
					bcs++;
				break;
			case -3:
				if ((((i / 10) + (i % 10)) & 1) != 0)
					cbs++;
				else
					ccs++;
				break;
			}
		}
		if ((i = bbs + bcs + cbs + ccs + cj + bj) <= 1 || i == bbs + cbs
				|| i == bcs + ccs)
			return DRAW_MATERIAL;
		/*
		 * Je-li zadna nebo jen jedna lehka figura nebo jsou na sachovnici jen
		 * strelci jedne barvy, nebude mat
		 */
		return NO_END;
	}

	public void tahniZpet(int tah, boolean globalne, ZobrazPole zobrazPole) {

		int odkud, kam;
		ZasobnikStruct z;
		if (globalne) {
			z = (ZasobnikStruct) mGame.elementAt(mIndexInGame--);
			tah = z.mTah;
			mEnd = 0;
		} else {
			z = (ZasobnikStruct) mZasobnik.elementAt(mIndexVZasobniku--);
		}
		mBoardComputing.mimoch = z.mMimoch;
		mBoardComputing.roch = z.mRoch;
		mBoardComputing.bily = !mBoardComputing.bily;

		if ((tah >> 15) == 0) /* Normalni tah */
		{
			kam = tah & 127;
			odkud = tah >> 7;
			mBoardComputing.sch[odkud] = mBoardComputing.sch[kam];
			mBoardComputing.sch[kam] = z.mBrani;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(odkud);
				zobrazPole.zobrazPole(kam);
			}
			return;
		}

		/*
		 * Nenormalni tah
		 * 
		 * Mala bila rosada
		 */
		if (tah == MBRoch) {
			mBoardComputing.sch[Pozice.e1] = 6;
			mBoardComputing.sch[Pozice.g1] = 0;
			mBoardComputing.sch[Pozice.h1] = 4;
			mBoardComputing.sch[Pozice.f1] = 0;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.e1);
				zobrazPole.zobrazPole(Pozice.f1);
				zobrazPole.zobrazPole(Pozice.g1);
				zobrazPole.zobrazPole(Pozice.h1);
			}
			return;
		}

		/* Velka bila rosada */
		if (tah == VBRoch) {
			mBoardComputing.sch[Pozice.e1] = 6;
			mBoardComputing.sch[Pozice.c1] = 0;
			mBoardComputing.sch[Pozice.a1] = 4;
			mBoardComputing.sch[Pozice.d1] = 0;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.e1);
				zobrazPole.zobrazPole(Pozice.d1);
				zobrazPole.zobrazPole(Pozice.c1);
				zobrazPole.zobrazPole(Pozice.a1);
			}
			return;
		}

		/* Mala cerna rosada */
		if (tah == MCRoch) {
			mBoardComputing.sch[Pozice.e8] = -6;
			mBoardComputing.sch[Pozice.g8] = 0;
			mBoardComputing.sch[Pozice.h8] = -4;
			mBoardComputing.sch[Pozice.f8] = 0;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.e8);
				zobrazPole.zobrazPole(Pozice.f8);
				zobrazPole.zobrazPole(Pozice.g8);
				zobrazPole.zobrazPole(Pozice.h8);
			}
			return;
		}
		/* Velka cerna rosada */
		if (tah == VCRoch) {
			mBoardComputing.sch[Pozice.e8] = -6;
			mBoardComputing.sch[Pozice.c8] = 0;
			mBoardComputing.sch[Pozice.a8] = -4;
			mBoardComputing.sch[Pozice.d8] = 0;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.e8);
				zobrazPole.zobrazPole(Pozice.d8);
				zobrazPole.zobrazPole(Pozice.c8);
				zobrazPole.zobrazPole(Pozice.a8);
			}
			return;
		}

		/* Promena bileho pesce */
		if ((tah >> 12) == 12) {
			odkud = Pozice.a7 + ((tah >> 7) & 7);
			kam = Pozice.a8 + ((tah >> 4) & 7);
			mBoardComputing.sch[odkud] = 1;
			mBoardComputing.sch[kam] = z.mBrani;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			// TODO zobrazpole?
			return;
		}

		/* Promena cerneho pesce */
		if ((tah >> 12) == 13) {
			odkud = Pozice.a2 + ((tah >> 7) & 7);
			kam = Pozice.a1 + ((tah >> 4) & 7);
			mBoardComputing.sch[odkud] = -1;
			mBoardComputing.sch[kam] = z.mBrani;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			// TODO zobrazpole?
			return;
		}
		/* Brani mimochodem (nic jineho to uz byt nemuze) */
		tah &= 0x3fff; /* odstraneni prvnich dvou bitu, aby se lepe siftovalo */
		kam = (tah & 127);
		odkud = (tah >> 7);
		mBoardComputing.sch[kam] = 0;
		if (odkud < kam) {
			mBoardComputing.sch[kam - 10] = -1; /* to hraje bily */
			mBoardComputing.sch[odkud] = 1;
		} else {
			mBoardComputing.sch[kam + 10] = 1; /* cerny */
			mBoardComputing.sch[odkud] = -1;
		}
		if (globalne) mBoard = (Pozice)mBoardComputing.clone();
		// TODO zobrazpole?
	}

	public void tahni(int tah, boolean globalne, boolean ukousniKonec,
			ZobrazPole zobrazPole) {

		int odkud, kam;
		byte co;

		if (globalne && !ukousniKonec) {
			tah = ((ZasobnikStruct) mGame.elementAt(mIndexInGame + 1)).mTah;
		}
		
		ZasobnikStruct z = push(globalne, ukousniKonec, tah);
		z.mBrani = 0;
		
		mBoardComputing.mimoch = 0; /* Vetsina tahu neni pescem o 2, pokud ano, osetri se */
		mBoardComputing.bily = !mBoardComputing.bily;

		if ((tah >> 15) == 0) /* Normalni tah */{
			kam = tah & 127;
			odkud = tah >> 7;
			if (mBoardComputing.sch[odkud] == 6) z.mBk = (byte) kam;
			if (mBoardComputing.sch[odkud] == -6) z.mCk = (byte) kam;
			if (/* bud cerny tahne pescem o 2 */
			odkud - kam == 20 && mBoardComputing.sch[odkud] == -1
			/* a bily pesec ciha */
			&& (mBoardComputing.sch[kam + 1] == 1 || mBoardComputing.sch[kam - 1] == 1)
			/* nebo bily tahne pescem o 2 */
			|| odkud - kam == -20 && mBoardComputing.sch[odkud] == 1
			/* a cerny pesec ciha */
			&& (mBoardComputing.sch[kam + 1] == -1 || mBoardComputing.sch[kam - 1] == -1))

				mBoardComputing.mimoch = (byte) kam;
			/*
			 * Niceni rosad Pozn.: nejde dat vsude 'else', protoze napr. Va1xa8
			 * nici obe velke rosady
			 */
			if (odkud == Pozice.e1)
				mBoardComputing.roch &= 12;
			else /* 1100b */
			if (odkud == Pozice.e8)
				mBoardComputing.roch &= 3;
			else /* 0011b */{
				if (kam == Pozice.a1 || odkud == Pozice.a1)
					mBoardComputing.roch &= 13;/* 1101b */
				if (kam == Pozice.h1 || odkud == Pozice.h1)
					mBoardComputing.roch &= 14;/* 1110b */
				if (kam == Pozice.a8 || odkud == Pozice.a8)
					mBoardComputing.roch &= 7; /* 0111b */
				if (kam == Pozice.h8 || odkud == Pozice.h8)
					mBoardComputing.roch &= 11;/* 1011b */
			}
			/* Ulozim si sebranou figuru */
			z.mBrani = mBoardComputing.sch[kam];
			if (mBoardComputing.sch[kam] > 0)
				z.mBm -= HodnotaPozice.mStdCenyFigur[mBoardComputing.sch[kam]];
			else
				z.mCm -= HodnotaPozice.mStdCenyFigur[-mBoardComputing.sch[kam]];
			z.mKam = (byte)kam;
			
			/* zakladni rutina normalniho tahu: */
			mBoardComputing.sch[kam] = mBoardComputing.sch[odkud];
			mBoardComputing.sch[odkud] = 0;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(odkud);
				zobrazPole.zobrazPole(kam);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();
			return;
		}
		/*
		 * Nenormalni tah Mala bila rosada
		 */
		if (tah == MBRoch) {
			mBoardComputing.sch[Pozice.e1] = 0;
			mBoardComputing.sch[Pozice.g1] = 6;
			mBoardComputing.sch[Pozice.h1] = 0;
			mBoardComputing.sch[Pozice.f1] = 4;
			mBoardComputing.roch &= 12;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.e1);
				zobrazPole.zobrazPole(Pozice.f1);
				zobrazPole.zobrazPole(Pozice.g1);
				zobrazPole.zobrazPole(Pozice.h1);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();
			z.mKam = Pozice.g1;
			z.mBk = Pozice.g1;
			return;
		}
		/* Velka bila rosada */
		if (tah == VBRoch) {
			mBoardComputing.sch[Pozice.e1] = 0;
			mBoardComputing.sch[Pozice.c1] = 6;
			mBoardComputing.sch[Pozice.a1] = 0;
			mBoardComputing.sch[Pozice.d1] = 4;
			mBoardComputing.roch &= 12;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.a1);
				zobrazPole.zobrazPole(Pozice.c1);
				zobrazPole.zobrazPole(Pozice.d1);
				zobrazPole.zobrazPole(Pozice.e1);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();
			z.mKam = Pozice.c1;
			z.mBk = Pozice.c1;
			return;
		}
		/* Mala cerna rosada */
		if (tah == MCRoch) {
			mBoardComputing.sch[Pozice.e8] = 0;
			mBoardComputing.sch[Pozice.g8] = -6;
			mBoardComputing.sch[Pozice.h8] = 0;
			mBoardComputing.sch[Pozice.f8] = -4;
			mBoardComputing.roch &= 3;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.e8);
				zobrazPole.zobrazPole(Pozice.f8);
				zobrazPole.zobrazPole(Pozice.g8);
				zobrazPole.zobrazPole(Pozice.h8);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();
			z.mKam = Pozice.g8;
			z.mCk = Pozice.g8;
			return;
		}
		/* Velka cerna rosada */
		if (tah == VCRoch) {
			mBoardComputing.sch[Pozice.e8] = 0;
			mBoardComputing.sch[Pozice.c8] = -6;
			mBoardComputing.sch[Pozice.a8] = 0;
			mBoardComputing.sch[Pozice.d8] = -4;
			mBoardComputing.roch &= 3;
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(Pozice.a8);
				zobrazPole.zobrazPole(Pozice.c8);
				zobrazPole.zobrazPole(Pozice.d8);
				zobrazPole.zobrazPole(Pozice.e8);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();
			z.mKam = Pozice.c8;
			z.mCk = Pozice.g8;
			return;
		}
		/* Promena bileho pesce */
		if ((tah >> 12) == 12) {
			odkud = (Pozice.a7 + ((tah >> 7) & 7));
			kam = (Pozice.a8 + ((tah >> 4) & 7));
			co = (byte) (2 + ((tah >> 10) & 3));
			/* Ulozim si, co jsem sebral */
			z.mBrani = mBoardComputing.sch[kam];
			z.mBm += HodnotaPozice.mStdCenyFigur[co] - HodnotaPozice.mStdCenyFigur[1];
			z.mCm -= HodnotaPozice.mStdCenyFigur[-mBoardComputing.sch[kam]];
			mBoardComputing.sch[odkud] = 0;
			mBoardComputing.sch[kam] = co;
			if (kam == Pozice.a8) /*
								 * meni pesce na a8, mohl sezrat vez =>
								 * rosady...
								 */
				mBoardComputing.roch &= 7; /* 0111b */
			if (kam == Pozice.h8) /*
								 * meni pesce na h8, mohl sezrat vez =>
								 * rosady...
								 */
				mBoardComputing.roch &= 11; /* 1011b */
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(odkud);
				zobrazPole.zobrazPole(kam);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();
			return;
		}
		/* Promena cerneho pesce */
		if ((tah >> 12) == 13) {
			odkud = (byte) (Pozice.a2 + ((tah >> 7) & 7));
			kam = (byte) (Pozice.a1 + ((tah >> 4) & 7));
			co = (byte) (-(2 + ((tah >> 10) & 3)));

			/* Ulozim si, co jsem sebral */
			z.mBrani = mBoardComputing.sch[kam];
			z.mCm += HodnotaPozice.mStdCenyFigur[-co] - HodnotaPozice.mStdCenyFigur[1];
			z.mBm -= HodnotaPozice.mStdCenyFigur[mBoardComputing.sch[kam]];
			
			mBoardComputing.sch[odkud] = 0;
			mBoardComputing.sch[kam] = co;
			if (kam == Pozice.a1) /*
								 * meni pesce na a1, mohl sezrat vez =>
								 * rosady...
								 */
				mBoardComputing.roch &= 13; /* 1101b */
			if (kam == Pozice.h1) /*
								 * meni pesce na h1, mohl sezrat vez =>
								 * rosady...
								 */
				mBoardComputing.roch &= 14; /* 1110b */
			if (globalne) mBoard = (Pozice)mBoardComputing.clone();
			if (zobrazPole != null) {
				zobrazPole.zobrazPole(odkud);
				zobrazPole.zobrazPole(kam);
			}
			if (globalne && ukousniKonec)
				mEnd = getEndOfGame();

			return;
		}
		/* Brani mimochodem (nic jineho to uz byt nemuze) */
		tah &= 0x3fff; /* odstraneni prvnich dvou bitu, aby se lepe siftovalo */
		kam = (tah & 127);
		odkud = (tah >> 7);
		if (odkud < kam) {
			mBoardComputing.sch[kam - 10] = 0;
			z.mBrani = -1;
			z.mCm -= HodnotaPozice.mStdCenyFigur[1];
		}
		/* to hral bily */
		else {
			mBoardComputing.sch[kam + 10] = 0;
			z.mBrani = 1;
			z.mBm -= HodnotaPozice.mStdCenyFigur[1];
		}
		/* cerny */
		mBoardComputing.sch[kam] = mBoardComputing.sch[odkud];
		mBoardComputing.sch[odkud] = 0;
		if (globalne) mBoard = (Pozice)mBoardComputing.clone();
		if (zobrazPole != null) {
			zobrazPole.zobrazPole(odkud);
			zobrazPole.zobrazPole(kam);
			zobrazPole.zobrazPole(kam + 10);
			zobrazPole.zobrazPole(kam - 10);
		}
		if (globalne && ukousniKonec)
			mEnd = getEndOfGame();
		return;
	}

	public void push() {
		mIndexVZasobniku = 0;
		if (mZasobnik.size() > 0)
			((ZasobnikStruct) mZasobnik.elementAt(mIndexVZasobniku)).set(this);
		else
			mZasobnik.add(new ZasobnikStruct(this));
	}

	protected ZasobnikStruct push(boolean globalne, boolean ukousniKonec, int tah) {
		ZasobnikStruct r;
		
		if (globalne) {
			mIndexInGame++;
			if (!ukousniKonec)
				return (ZasobnikStruct) mGame.elementAt(mIndexInGame);
			if (mIndexInGame >= mGame.size()) {
				mGame.add(r = new ZasobnikStruct(mBoardComputing.roch, mBoardComputing.mimoch, tah));
			} else {
				r = (ZasobnikStruct) mGame.elementAt(mIndexInGame);
				r.set(mBoardComputing.roch, mBoardComputing.mimoch, tah);
				if (ukousniKonec && mGame.size() > mIndexInGame + 1) {
					mGame.setSize(mIndexInGame + 1);
				}
			}
		} else {
			ZasobnikStruct old = mIndexVZasobniku >= 0 ? (ZasobnikStruct) mZasobnik.elementAt(mIndexVZasobniku) : null;
			mIndexVZasobniku++;
			if (mIndexVZasobniku >= mZasobnik.size()) {
				mZasobnik.add(r = new ZasobnikStruct(mBoardComputing.roch, mBoardComputing.mimoch, tah));
			} else {
				r = (ZasobnikStruct) mZasobnik.elementAt(mIndexVZasobniku);
				r.set(mBoardComputing.roch, mBoardComputing.mimoch, tah);
			}
			if (old != null) {
				r.mBm = old.mBm;
				r.mCm = old.mCm;
				r.mHashF = old.mHashF;
				r.mBk = old.mBk;
				r.mCk = old.mCk;
			}
		}
		return r;
	}

	protected Vector nalezXxxTahyVector(boolean all) {
		if (all) {
			nalezPseudolegalniTahyZasobnik();
		} else {
			nalezTahyZasobnik();
		}
		Vector t = new Vector();
		int moveFrom = getOdkud();
		int moveTo = getKam();
		for (int i = moveFrom; i < moveTo; i++)
			t.add(new Integer(mZasobnikTahu.tahy[i]));
		mZasobnikTahu.pos--;
		return t;
	}

	public Vector nalezTahyVector() {
		return nalezXxxTahyVector(false);
	}

	public Vector nalezPseudolegalniTahyVector() {
		return nalezXxxTahyVector(true);
	}

	public int getOdkud() {
		return mZasobnikTahu.pos == 1 ? 0
				: mZasobnikTahu.hranice[mZasobnikTahu.pos - 2];
	}

	public int getKam() {
		return mZasobnikTahu.hranice[mZasobnikTahu.pos - 1];
	}

	private void zaradTah(int i, int j) {
		mZasobnikTahu.tahy[mIndexVZasobnikuTahu] = (i << 7) | j;
		mZasobnikTahu.hodnoty[mIndexVZasobnikuTahu] = HodnotaPozice.mStdCenyFigur[abs(mBoardComputing.sch[j])];
		mIndexVZasobnikuTahu++;
	}

	private void zaradMimochodem(int i, int j) {
		mZasobnikTahu.tahy[mIndexVZasobnikuTahu] = (1 << 15) | ((i) << 7) | (j);
		mIndexVZasobnikuTahu++;
	}

	private void zaradBilouPromenu(int p1, int p2, int fig) {
		mZasobnikTahu.tahy[mIndexVZasobnikuTahu] = ((3 << 14) | (fig << 10)
				| ((p1 - Pozice.a7) << 7) | ((p2 - Pozice.a8) << 4));
		if (mZasobnikTahu.tahy[mIndexVZasobnikuTahu] < 0) {
			System.out.println(toString());
		}
		mIndexVZasobnikuTahu++;
	}

	private void zaradCernouPromenu(int p1, int p2, int fig) {
		mZasobnikTahu.tahy[mIndexVZasobnikuTahu] = ((13 << 12) | (fig << 10)
				| ((p1 - Pozice.a2) << 7) | ((p2 - Pozice.a1) << 4));
		mIndexVZasobnikuTahu++;
	}

	private void dlouhaBilaFigura(int o1, int o2, int p) {
		for (int j = o1; j <= o2; j++) {
			for (int q = p + Pozice.mOfsety[j]; mBoardComputing.sch[q] <= 0; q += Pozice.mOfsety[j]) {
				zaradTah(p, q);
				if (mBoardComputing.sch[q] < 0) {
					break;
				}
			}
		}
	}

	private void dlouhaCernaFigura(int o1, int o2, int p) {
		for (int j = o1; j <= o2; j++) {
			for (int q = p + Pozice.mOfsety[j]; mBoardComputing.sch[q] >= 0
					&& mBoardComputing.sch[q] < 7; q += Pozice.mOfsety[j]) {
				zaradTah(p, q);
				if (mBoardComputing.sch[q] > 0) {
					break;
				}
			}
		}
	}

	private void dlouhaBilaFiguraBrani(int o1, int o2, int p) {
		for (int j = o1; j <= o2; j++) {
			for (int q = p + Pozice.mOfsety[j]; mBoardComputing.sch[q] <= 0; q += Pozice.mOfsety[j]) {

				if (mBoardComputing.sch[q] < 0) {
					zaradTah(p, q);
					break;
				}
			}
		}
	}

	private void dlouhaCernaFiguraBrani(int o1, int o2, int p) {
		for (int j = o1; j <= o2; j++) {
			for (int q = p + Pozice.mOfsety[j]; mBoardComputing.sch[q] >= 0
					&& mBoardComputing.sch[q] < 7; q += Pozice.mOfsety[j]) {

				if (mBoardComputing.sch[q] > 0) {
					zaradTah(p, q);
					break;
				}
			}
		}
	}

	private void zaradRosadu(int jakou) {
		mZasobnikTahu.tahy[mIndexVZasobnikuTahu] = jakou;
		mIndexVZasobnikuTahu++;
	}

	public void nalezPseudolegalniTahyZasobnik() {
		int j, i; /* promenne pro for cykly */
		if (mZasobnikTahu.pos == 0)
			mIndexVZasobnikuTahu = 0;
		else
			mIndexVZasobnikuTahu = mZasobnikTahu.hranice[mZasobnikTahu.pos - 1];

		if (mBoardComputing.bily) {
			for (i = Pozice.a1; i <= Pozice.h8; i++) {
				if (mBoardComputing.sch[i] < 1 || mBoardComputing.sch[i] > 6)
					continue;
				switch (mBoardComputing.sch[i]) {
				case 1: /* pesec */
					if (i < Pozice.a7) /* tedy nehrozi promena */{
						if (mBoardComputing.sch[i + 11] < 0)
							zaradTah(i, i + 11);
						if (mBoardComputing.sch[i + 9] < 0)
							zaradTah(i, i + 9);
						if (mBoardComputing.sch[i + 10] == 0) {
							zaradTah(i, i + 10);
							if (i <= Pozice.h2 && mBoardComputing.sch[i + 20] == 0)
								zaradTah(i, i + 20);
						} /* pescem o 2 */
						if (mBoardComputing.mimoch == i + 1)
							zaradMimochodem(i, i + 11);
						else if (mBoardComputing.mimoch == i - 1)
							zaradMimochodem(i, i + 9);
					} else /* i>=a7 => promeny pesce */
					{
						if (mBoardComputing.sch[i + 10] == 0)
							for (j = 3; j >= 0; j--)
								zaradBilouPromenu(i, i + 10, j);
						if (mBoardComputing.sch[i + 11] < 0)
							for (j = 3; j >= 0; j--)
								zaradBilouPromenu(i, i + 11, j);
						if (mBoardComputing.sch[i + 9] < 0)
							for (j = 3; j >= 0; j--)
								zaradBilouPromenu(i, i + 9, j);
					}
					break;
				case 2: /* kun */
					for (j = 8; j <= 15; j++)
						if ((mBoardComputing.sch[i + Pozice.mOfsety[j]]) <= 0)
							zaradTah(i, i + Pozice.mOfsety[j]);
					break;
				case 3: /* strelec */
					dlouhaBilaFigura(4, 7, i);
					break;
				case 4: /* vez */
					dlouhaBilaFigura(0, 3, i);
					break;
				case 5: /* dama */
					dlouhaBilaFigura(0, 7, i);
					break;
				case 6: /* kral */
					for (j = 0; j <= 7; j++)
						if ((mBoardComputing.sch[i + Pozice.mOfsety[j]]) <= 0)
							zaradTah(i, i + Pozice.mOfsety[j]);
					if (i == Pozice.e1 && ((mBoardComputing.roch & 1) != 0)
							&& (mBoardComputing.sch[Pozice.f1] == 0)
							&& (mBoardComputing.sch[Pozice.g1] == 0)
							&& (mBoardComputing.sch[Pozice.h1] == 4)
							&& !mBoardComputing.ohrozeno(Pozice.e1, false)
							&& !mBoardComputing.ohrozeno(Pozice.f1, false)
							&& !mBoardComputing.ohrozeno(Pozice.g1, false)) {
						zaradRosadu(MBRoch);
					}
					if (i == Pozice.e1 && ((mBoardComputing.roch & 2) != 0)
							&& (mBoardComputing.sch[Pozice.d1] == 0)
							&& (mBoardComputing.sch[Pozice.c1] == 0)
							&& (mBoardComputing.sch[Pozice.b1] == 0)
							&& (mBoardComputing.sch[Pozice.a1] == 4)
							&& !mBoardComputing.ohrozeno(Pozice.e1, false)
							&& !mBoardComputing.ohrozeno(Pozice.d1, false)
							&& !mBoardComputing.ohrozeno(Pozice.c1, false)) {
						zaradRosadu(VBRoch);
					}
					break; /* od krale */
				}/* od switch */
			} /* od for cyklu */
		} /* od hraje bily */
		else {
			for (i = Pozice.a1; i <= Pozice.h8; i++) {
				if (mBoardComputing.sch[i] >= 0)
					continue;
				switch (mBoardComputing.sch[i]) {
				case -1: /* pesec */
					if (i > Pozice.h2) /* tedy nehrozi promena */{
						if (mBoardComputing.sch[i - 11] > 0 && mBoardComputing.sch[i - 11] < 7)
							zaradTah(i, i - 11);
						if (mBoardComputing.sch[i - 9] > 0 && mBoardComputing.sch[i - 9] < 7)
							zaradTah(i, i - 9);
						if (mBoardComputing.sch[i - 10] == 0) /*
													 * policko pred pescem je
													 * volne
													 */{
							zaradTah(i, i - 10);
							if (i >= Pozice.a7 && mBoardComputing.sch[i - 20] == 0)
								zaradTah(i, i - 20);
						} /* pescem o 2 */
						if (mBoardComputing.mimoch == i + 1)
							zaradMimochodem(i, i - 9);
						else if (mBoardComputing.mimoch == i - 1)
							zaradMimochodem(i, i - 11);
					} else /* i<=h2 => promeny pesce */{
						if (mBoardComputing.sch[i - 10] == 0)
							for (j = 3; j >= 0; j--)
								zaradCernouPromenu(i, i - 10, j);
						if (mBoardComputing.sch[i - 11] > 0 && mBoardComputing.sch[i - 11] < 7)
							for (j = 3; j >= 0; j--)
								zaradCernouPromenu(i, i - 11, j);
						if (mBoardComputing.sch[i - 9] > 0 && mBoardComputing.sch[i - 9] < 7)
							for (j = 3; j >= 0; j--)
								zaradCernouPromenu(i, i - 9, j);
					}
					break;
				case -2: /* kun */
					for (j = 8; j <= 15; j++)
						if (mBoardComputing.sch[i + Pozice.mOfsety[j]] >= 0
								&& mBoardComputing.sch[i + Pozice.mOfsety[j]] < 7)
							zaradTah(i, i + Pozice.mOfsety[j]);
					break;
				case -3: /* strelec */
					dlouhaCernaFigura(4, 7, i);
					break;
				case -4: /* vez */
					dlouhaCernaFigura(0, 3, i);
					break;
				case -5: /* dama */
					dlouhaCernaFigura(0, 7, i);
					break;
				case -6: /* kral */
					for (j = 0; j <= 7; j++)
						if (mBoardComputing.sch[i + Pozice.mOfsety[j]] >= 0
								&& mBoardComputing.sch[i + Pozice.mOfsety[j]] < 7)
							zaradTah(i, i + Pozice.mOfsety[j]);
					if (i == Pozice.e8 && (mBoardComputing.roch & 4) != 0
							&& mBoardComputing.sch[Pozice.f8] == 0
							&& mBoardComputing.sch[Pozice.g8] == 0
							&& (mBoardComputing.sch[Pozice.h8] == -4)
							&& !mBoardComputing.ohrozeno(Pozice.g8, true)
							&& !mBoardComputing.ohrozeno(Pozice.e8, true)
							&& !mBoardComputing.ohrozeno(Pozice.f8, true)) {
						zaradRosadu(MCRoch);
					}
					if (i == Pozice.e8 && (mBoardComputing.roch & 8) != 0
							&& mBoardComputing.sch[Pozice.d8] == 0
							&& mBoardComputing.sch[Pozice.c8] == 0
							&& mBoardComputing.sch[Pozice.b8] == 0
							&& (mBoardComputing.sch[Pozice.a8] == -4)
							&& !mBoardComputing.ohrozeno(Pozice.c8, true)
							&& !mBoardComputing.ohrozeno(Pozice.e8, true)
							&& !mBoardComputing.ohrozeno(Pozice.d8, true)) {
						zaradRosadu(VCRoch);
					}
					break;
				}/* od switch */
			} /* od for cyklu */
		} /* od hraje cerny */
		mZasobnikTahu.hranice[mZasobnikTahu.pos] = mIndexVZasobnikuTahu;
		mZasobnikTahu.pos++;

		if (false) {
			int maxPos;
			int max;
			int tmp;
			for (i = (mZasobnikTahu.pos == 1 ? 0
					: mZasobnikTahu.hranice[mZasobnikTahu.pos - 2]); i < mIndexVZasobnikuTahu - 1; i++) {
				maxPos = i;
				max = mZasobnikTahu.hodnoty[i];
				for (j = i + 1; j < mIndexVZasobnikuTahu; j++) {
					if (mZasobnikTahu.hodnoty[j] > max) {
						maxPos = j;
						max = mZasobnikTahu.hodnoty[j];
					}
				}
				if (maxPos != i) {
					mZasobnikTahu.hodnoty[maxPos] = mZasobnikTahu.hodnoty[i];
					mZasobnikTahu.hodnoty[i] = max;
					tmp = mZasobnikTahu.tahy[maxPos];
					mZasobnikTahu.tahy[maxPos] = mZasobnikTahu.tahy[i];
					mZasobnikTahu.tahy[i] = tmp;
				}
			}
		}
	}

	public void nalezTahyZasobnik() {
		nalezPseudolegalniTahyZasobnik();

		int odkud = getOdkud();
		int kam = getKam();

		boolean jeSach = false;
		for (int i = odkud; i < kam; i++) {
			int tah = mZasobnikTahu.tahy[i];
			tahni(tah, false, false, null);
			if (mBoardComputing.sach(!mBoardComputing.bily)) {
				mZasobnikTahu.tahy[i] = 0;
				jeSach = true;
			}
			tahniZpet(tah, false, null);
		}

		if (jeSach) {
			int indexOdkud = odkud;
			int indexKam = odkud;
			hlavni: while (indexOdkud < kam) {
				while (mZasobnikTahu.tahy[indexOdkud] == 0) {
					indexOdkud++;
					if (indexOdkud == kam) {
						break hlavni;
					}
				}
				mZasobnikTahu.tahy[indexKam++] = mZasobnikTahu.tahy[indexOdkud++];
			}
			mZasobnikTahu.hranice[mZasobnikTahu.pos - 1] = indexKam;
		}

	}

	public void nalezPseudolegalniBraniZasobnik() {
		int j, i; /* promenne pro for cykly */
		if (mZasobnikTahu.pos == 0)
			mIndexVZasobnikuTahu = 0;
		else
			mIndexVZasobnikuTahu = mZasobnikTahu.hranice[mZasobnikTahu.pos - 1];

		if (mBoardComputing.bily) {
			for (i = Pozice.a1; i <= Pozice.h8; i++) {
				if (mBoardComputing.sch[i] < 1 || mBoardComputing.sch[i] > 6)
					continue;
				switch (mBoardComputing.sch[i]) {
				case 1: /* pesec */
					if (i < Pozice.a7) /* tedy nehrozi promena */{
						if (mBoardComputing.sch[i + 11] < 0)
							zaradTah(i, i + 11);
						if (mBoardComputing.sch[i + 9] < 0)
							zaradTah(i, i + 9);
						if (mBoardComputing.mimoch == i + 1)
							zaradMimochodem(i, i + 11);
						else if (mBoardComputing.mimoch == i - 1)
							zaradMimochodem(i, i + 9);
					} else /* i>=a7 => promeny pesce */
					{
						if (mBoardComputing.sch[i + 10] == 0)
							for (j = 3; j >= 0; j--)
								zaradBilouPromenu(i, i + 10, j);
						if (mBoardComputing.sch[i + 11] < 0)
							for (j = 3; j >= 0; j--)
								zaradBilouPromenu(i, i + 11, j);
						if (mBoardComputing.sch[i + 9] < 0)
							for (j = 3; j >= 0; j--)
								zaradBilouPromenu(i, i + 9, j);
					}
					break;
				case 2: /* kun */
					for (j = 8; j <= 15; j++)
						if ((mBoardComputing.sch[i + Pozice.mOfsety[j]]) < 0)
							zaradTah(i, i + Pozice.mOfsety[j]);
					break;
				case 3: /* strelec */
					dlouhaBilaFiguraBrani(4, 7, i);
					break;
				case 4: /* vez */
					dlouhaBilaFiguraBrani(0, 3, i);
					break;
				case 5: /* dama */
					dlouhaBilaFiguraBrani(0, 7, i);
					break;
				case 6: /* kral */
					for (j = 0; j <= 7; j++)
						if ((mBoardComputing.sch[i + Pozice.mOfsety[j]]) < 0)
							zaradTah(i, i + Pozice.mOfsety[j]);

					break; /* od krale */
				}/* od switch */
			} /* od for cyklu */
		} /* od hraje bily */
		else {
			for (i = Pozice.a1; i <= Pozice.h8; i++) {
				if (mBoardComputing.sch[i] >= 0)
					continue;
				switch (mBoardComputing.sch[i]) {
				case -1: /* pesec */
					if (i > Pozice.h2) /* tedy nehrozi promena */{
						if (mBoardComputing.sch[i - 11] > 0 && mBoardComputing.sch[i - 11] < 7)
							zaradTah(i, i - 11);
						if (mBoardComputing.sch[i - 9] > 0 && mBoardComputing.sch[i - 9] < 7)
							zaradTah(i, i - 9);

						if (mBoardComputing.mimoch == i + 1)
							zaradMimochodem(i, i - 9);
						else if (mBoardComputing.mimoch == i - 1)
							zaradMimochodem(i, i - 11);
					} else /* i<=h2 => promeny pesce */{
						if (mBoardComputing.sch[i - 10] == 0)
							for (j = 3; j >= 0; j--)
								zaradCernouPromenu(i, i - 10, j);
						if (mBoardComputing.sch[i - 11] > 0 && mBoardComputing.sch[i - 11] < 7)
							for (j = 3; j >= 0; j--)
								zaradCernouPromenu(i, i - 11, j);
						if (mBoardComputing.sch[i - 9] > 0 && mBoardComputing.sch[i - 9] < 7)
							for (j = 3; j >= 0; j--)
								zaradCernouPromenu(i, i - 9, j);
					}
					break;
				case -2: /* kun */
					for (j = 8; j <= 15; j++)
						if (mBoardComputing.sch[i + Pozice.mOfsety[j]] > 0
								&& mBoardComputing.sch[i + Pozice.mOfsety[j]] < 7)
							zaradTah(i, i + Pozice.mOfsety[j]);
					break;
				case -3: /* strelec */
					dlouhaCernaFiguraBrani(4, 7, i);
					break;
				case -4: /* vez */
					dlouhaCernaFiguraBrani(0, 3, i);
					break;
				case -5: /* dama */
					dlouhaCernaFiguraBrani(0, 7, i);
					break;
				case -6: /* kral */
					for (j = 0; j <= 7; j++)
						if (mBoardComputing.sch[i + Pozice.mOfsety[j]] > 0
								&& mBoardComputing.sch[i + Pozice.mOfsety[j]] < 7)
							zaradTah(i, i + Pozice.mOfsety[j]);

					break;
				}/* od switch */
			} /* od for cyklu */
		} /* od hraje cerny */
		mZasobnikTahu.hranice[mZasobnikTahu.pos] = mIndexVZasobnikuTahu;
		mZasobnikTahu.pos++;

		int maxPos;
		int max;
		int tmp;
		for (i = (mZasobnikTahu.pos == 1 ? 0
				: mZasobnikTahu.hranice[mZasobnikTahu.pos - 2]); i < mIndexVZasobnikuTahu - 1; i++) {
			maxPos = i;
			max = mZasobnikTahu.hodnoty[i];
			for (j = i + 1; j < mIndexVZasobnikuTahu; j++) {
				if (mZasobnikTahu.hodnoty[j] > max) {
					maxPos = j;
					max = mZasobnikTahu.hodnoty[j];
				}
			}
			if (maxPos != i) {
				mZasobnikTahu.hodnoty[maxPos] = mZasobnikTahu.hodnoty[i];
				mZasobnikTahu.hodnoty[i] = max;
				tmp = mZasobnikTahu.tahy[maxPos];
				mZasobnikTahu.tahy[maxPos] = mZasobnikTahu.tahy[i];
				mZasobnikTahu.tahy[i] = tmp;
			}
		}
	}

	public static int abs(int i) {
		if (i < 0)
			return -i;
		return i;
	}

	private static final int J_Nic = 0;
	private static final int J_Radka = 1;
	private static final int J_Sloupec = 2;

	/* Je tah urcen jednoznacne ? (urcen je J_xxx) a tahy v uloze jsou nalezene */
	private boolean jednoZnacny(Vector tahy, int tah, int urcen) {
		int odkud, kam;

		if (tah >> 14 != 0)
			return true;/* Nenormalni tah je vzdy jednoznacny */
		kam = tah & 127;
		odkud = tah >> 7;

		Iterator i = tahy.iterator();

		while (i.hasNext()) {
			int t = ((Integer) (i.next())).intValue();
			if (((t >> 14) == 0) && /* normalni tah */
			((t & 127) == kam) && /* na stejne policko */
			(odkud != (t >> 7)) && /* z jineho policka */
			((mBoardComputing.sch[odkud]) == (mBoardComputing.sch[t >> 7])) /* stejnou figurkou */
			)
				switch (urcen) {
				case J_Nic:
					return false;
				case J_Radka:
					if (odkud / 10 == (t >> 7) / 10)
						return false;
					break;
				case J_Sloupec:
					if (odkud % 10 == (t >> 7) % 10)
						return false;
					break;
				}
		}
		return true;
	}

	/**
	 * Converts move into string, does not depend on the board.
	 * 
	 * @param tah
	 *            move
	 * @return string like f1c3
	 */
	public static String tah2StrNoBoard(int tah) {
		int odkud, kam;
		StringBuffer s = new StringBuffer();

		if ((tah >> 14) == 0) /* Normalni tah */{
			kam = tah & 127;
			odkud = tah >> 7;
			s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
			s.append((char) ((odkud - Pozice.a1) / 10 + '1'));
			s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
			s.append((char) ((kam - Pozice.a1) / 10 + '1'));
			return new String(s);
		}
		/*
		 * Nenormalni tah Mala rosada
		 */
		if (tah == MBRoch || tah == MCRoch)
			return "O-O";
		/* Velka rosada */
		if (tah == VBRoch || tah == VCRoch)
			return "O-O-O";

		/* Promena bileho pesce */
		if ((tah >> 12) == 12 || (tah >> 12) == 13) {
			if ((tah >> 12) == 12) {
				odkud = Pozice.a7 + ((tah >> 7) & 7);
				kam = Pozice.a8 + ((tah >> 4) & 7);
			} else {
				odkud = Pozice.a2 + ((tah >> 7) & 7);
				kam = Pozice.a1 + ((tah >> 4) & 7);
			}
			s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
			s.append((char) ((kam - Pozice.a1) / 10 + '1'));
			switch ((tah >> 10) & 3) {
			case 0:
				s.append('N');
				break;
			case 1:
				s.append('B');
				break;
			case 2:
				s.append('R');
				break;
			case 3:
				s.append('Q');
				break;
			}
			return new String(s);
		}
		/* Brani mimochodem (nic jineho to uz byt nemuze) */
		tah &= 0x3fff; /* odstraneni prvnich dvou bitu, aby se lepe siftovalo */
		kam = tah & 127;
		odkud = tah >> 7;
		s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
		/* s[i++]=(odkud-a1)/10 + '1'; */
		s.append('x');
		s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
		s.append((char) ((kam - Pozice.a1) / 10 + '1'));
		return new String(s);

	}

	public String tah2Str(int tah, Pozice p) {
		int odkud, kam;
		StringBuffer s = new StringBuffer();

		if ((tah >> 14) == 0) /* Normalni tah */{
			kam = tah & 127;
			odkud = tah >> 7;
			// i = 0;
			switch (abs(p.sch[odkud])) {
			case 1:
				if (p.sch[kam] != 0)
					s.append((char) ('a' + (odkud - Pozice.a1) % 10));
				break;
			case 2:
				s.append('N');
				break;
			case 3:
				s.append('B');
				break;
			case 4:
				s.append('R');
				break;
			case 5:
				s.append('Q');
				break;
			case 6:
				s.append('K');
				break;
			}
			if (abs(p.sch[odkud]) != 1) {
				s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
				s.append((char) ((odkud - Pozice.a1) / 10 + '1'));
			}
			if (p.sch[kam] != 0)
				s.append('x');
			s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
			s.append((char) ((kam - Pozice.a1) / 10 + '1'));

			return new String(s);
		}
		/*
		 * Nenormalni tah Mala rosada
		 */
		if (tah == MBRoch || tah == MCRoch)
			return "O-O";
		/* Velka rosada */
		if (tah == VBRoch || tah == VCRoch)
			return "O-O-O";

		/* Promena bileho pesce */
		if ((tah >> 12) == 12 || (tah >> 12) == 13) {
			if ((tah >> 12) == 12) {
				odkud = Pozice.a7 + ((tah >> 7) & 7);
				kam = Pozice.a8 + ((tah >> 4) & 7);
			} else {
				odkud = Pozice.a2 + ((tah >> 7) & 7);
				kam = Pozice.a1 + ((tah >> 4) & 7);
			}
			s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
			if (p.sch[kam] != 0) {
				s.append('x');
				s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
			}
			s.append((char) ((kam - Pozice.a1) / 10 + '1'));
			switch ((tah >> 10) & 3) {
			case 0:
				s.append('N');
				break;
			case 1:
				s.append('B');
				break;
			case 2:
				s.append('R');
				break;
			case 3:
				s.append('Q');
				break;
			}
			return new String(s);
		}
		/* Brani mimochodem (nic jineho to uz byt nemuze) */
		tah &= 0x3fff; /* odstraneni prvnich dvou bitu, aby se lepe siftovalo */
		kam = tah & 127;
		odkud = tah >> 7;
		s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
		/* s[i++]=(odkud-a1)/10 + '1'; */
		s.append('x');
		s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
		s.append((char) ((kam - Pozice.a1) / 10 + '1'));
		return new String(s);
	}

	public String tah2Str(Vector tahy, int tah, boolean cz) {
		int odkud, kam;
		StringBuffer s = new StringBuffer();

		if ((tah >> 14) == 0) /* Normalni tah */{
			kam = tah & 127;
			odkud = tah >> 7;
			// i = 0;
			switch (abs(mBoardComputing.sch[odkud])) {
			case 1:
				if (mBoardComputing.sch[kam] != 0)
					s.append((char) ('a' + (odkud - Pozice.a1) % 10));
				break;
			case 2:
				s.append(cz ? 'J' : 'N');
				break;
			case 3:
				s.append(cz ? 'S' : 'B');
				break;
			case 4:
				s.append(cz ? 'V' : 'R');
				break;
			case 5:
				s.append(cz? 'D' : 'Q');
				break;
			case 6:
				s.append('K');
				break;
			}
			if (abs(mBoardComputing.sch[odkud]) != 1) {
				if (!jednoZnacny(tahy, tah, J_Nic)) /* Zkusim Da1 */
				{ /* Tak Dha1 */
					if (jednoZnacny(tahy, tah, J_Sloupec))
						s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
					else /* Tak D1a1 */
					if (jednoZnacny(tahy, tah, J_Radka))
						s.append((char) ((odkud - Pozice.a1) / 10 + '1'));
					else /*
						 * Tak teda Dh1a1 (nutne pokud jsou 3 damy na h1, h8 a
						 * a8)
						 */
					{
						s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
						s.append((char) ((odkud - Pozice.a1) / 10 + '1'));
					}
				}
			}
			if (mBoardComputing.sch[kam] != 0)
				s.append('x');
			s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
			s.append((char) ((kam - Pozice.a1) / 10 + '1'));

			return new String(s);
		}
		/*
		 * Nenormalni tah Mala rosada
		 */
		if (tah == MBRoch || tah == MCRoch)
			return "O-O";
		/* Velka rosada */
		if (tah == VBRoch || tah == VCRoch)
			return "O-O-O";

		/* Promena bileho pesce */
		if ((tah >> 12) == 12 || (tah >> 12) == 13) {
			if ((tah >> 12) == 12) {
				odkud = Pozice.a7 + ((tah >> 7) & 7);
				kam = Pozice.a8 + ((tah >> 4) & 7);
			} else {
				odkud = Pozice.a2 + ((tah >> 7) & 7);
				kam = Pozice.a1 + ((tah >> 4) & 7);
			}
			s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
			if (mBoardComputing.sch[kam] != 0) {
				s.append('x');
				s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
			}
			s.append((char) ((kam - Pozice.a1) / 10 + '1'));
			switch ((tah >> 10) & 3) {
			case 0:
				s.append(cz ? 'J' : 'N');
				break;
			case 1:
				s.append(cz ? 'S' : 'B');
				break;
			case 2:
				s.append(cz ? 'V' : 'R');
				break;
			case 3:
				s.append(cz ? 'D' : 'Q');
				break;
			}
			return new String(s);
		}
		/* Brani mimochodem (nic jineho to uz byt nemuze) */
		tah &= 0x3fff; /* odstraneni prvnich dvou bitu, aby se lepe siftovalo */
		kam = tah & 127;
		odkud = tah >> 7;
		s.append((char) ((odkud - Pozice.a1) % 10 + 'a'));
		/* s[i++]=(odkud-a1)/10 + '1'; */
		s.append('x');
		s.append((char) ((kam - Pozice.a1) % 10 + 'a'));
		s.append((char) ((kam - Pozice.a1) / 10 + '1'));
		return new String(s);
	}

	/**
	 * Je mezi nalezenymi tahi nejaky vedouci z odkud ?
	 * 
	 * @param odkud
	 * @return
	 */
	public boolean JeTam1(Vector tahy, int odkud) {
		// int p, k;

		Iterator ti = tahy.iterator();
		while (ti.hasNext()) {
			int t = ((Integer) ti.next()).intValue();
			if ((t >> 15) == 0) /* Normalni tah */
				if (odkud == t >> 7)
					return true;
				else
					continue;
			if ((t == MBRoch || t == VBRoch))
				if (odkud == Pozice.e1)
					return true;
				else
					continue;
			if ((t == MCRoch || t == VCRoch))
				if (odkud == Pozice.e8)
					return true;
				else
					continue;
			/* Promena bileho pesce */
			if ((t >> 12) == 12)
				if (odkud == Pozice.a7 + ((t >> 7) & 7))
					return true;
				else
					continue;
			/* Promena cerneho pesce */
			if ((t >> 12) == 13)
				if (odkud == Pozice.a2 + ((t >> 7) & 7))
					return true;
				else
					continue;
			/* Brani mimochodem (nic jineho to uz byt nemuze) */
			if ((t & 0x3fff) >> 7 == odkud)
				return true;
			else
				continue;
		}
		return false;
	}

	public boolean JeTam2(Vector tahy, int odkud, int kam) {
		Iterator ti = tahy.iterator();
		while (ti.hasNext()) {
			int t = ((Integer) ti.next()).intValue();
			// Normalni tah
			if ((t >> 15) == 0)
				if (kam == (t & 127) && odkud == (t >> 7))
					return true;
				else
					continue;
			/* Ro��dy */
			if (t == MBRoch)
				if (odkud == Pozice.e1 && kam == Pozice.g1)
					return true;
				else
					continue;
			if (t == VBRoch)
				if (odkud == Pozice.e1 && kam == Pozice.c1)
					return true;
				else
					continue;
			if (t == MCRoch)
				if (odkud == Pozice.e8 && kam == Pozice.g8)
					return true;
				else
					continue;
			if (t == VCRoch)
				if (odkud == Pozice.e8 && kam == Pozice.c8)
					return true;
				else
					continue;
			/* Promena bileho pesce */
			if ((t >> 12) == 12)
				if (odkud == Pozice.a7 + ((t >> 7) & 7)
						&& kam == Pozice.a8 + ((t >> 4) & 7))
					return true;
				else
					continue;
			/* Promena cerneho pesce */
			if ((t >> 12) == 13)
				if (odkud == Pozice.a2 + ((t >> 7) & 7)
						&& kam == Pozice.a1 + ((t >> 4) & 7))
					return true;
				else
					continue;
			/* Brani mimochodem (nic jineho to uz byt nemuze) */
			t &= 0x3fff; /* odstraneni prvnich dvou bitu, aby se lepe siftovalo */
			if (kam == (t & 127) && odkud == (t >> 7))
				return true;
			else
				continue;
		} /* konec while cyklu */
		return false;
	}

	/**
	 * Makes a move from fields
	 * 
	 * @param tahy
	 * @param fromField
	 * @param toField
	 * @return 0 if cancelled or error, otherwise move
	 */
	public int makeMove(Vector tahy, int fromField, int toField,
			PawnPromotionGUI gui) {
		Iterator ti = tahy.iterator();
		while (ti.hasNext()) {
			int t = ((Integer) ti.next()).intValue();
			/* Normalni tah */
			if ((t >> 15) == 0)
				if (toField == (t & 127) && fromField == (t >> 7))
					return t;
				else
					continue;
			/* Rosady */
			if (t == MBRoch)
				if (fromField == Pozice.e1 && toField == Pozice.g1)
					return t;
				else
					continue;
			if (t == VBRoch)
				if (fromField == Pozice.e1 && toField == Pozice.c1)
					return t;
				else
					continue;
			if (t == MCRoch)
				if (fromField == Pozice.e8 && toField == Pozice.g8)
					return t;
				else
					continue;
			if (t == VCRoch)
				if (fromField == Pozice.e8 && toField == Pozice.c8)
					return t;
				else
					continue;
			/* Promena bileho pesce */
			if ((t >> 12) == 12)
				if (fromField == Pozice.a7 + ((t >> 7) & 7)
						&& toField == Pozice.a8 + ((t >> 4) & 7)) {
					int prom = gui.promotion();
					if (prom == 0)
						return 0;
					prom -= 2;
					return (t & (0xFFFF ^ (3 << 10))) | (prom << 10);
				} else
					continue;
			/* Promena cerneho pesce */
			if ((t >> 12) == 13)
				if (fromField == Pozice.a2 + ((t >> 7) & 7)
						&& toField == Pozice.a1 + ((t >> 4) & 7)) {
					int prom = gui.promotion();
					if (prom == 0)
						return 0;
					prom -= 2;
					return (t & (0xFFFF ^ (3 << 10))) | (prom << 10);
				} else
					continue;
			/* Brani mimochodem (nic jineho to uz byt nemuze) */
			if (toField == (t & 0x3fff & 127)
					&& fromField == ((t & 0x3fff) >> 7))
				return t;
			else
				continue;
		} /* konec while cyklu */
		return 0;
	}
	
	
	public static void putc(char c, FileOutputStream f) throws IOException {
		f.write((int)c);
	}
	
	public static void fputs(String s, FileOutputStream f)  throws IOException {
		for (int i = 0; i < s.length(); i++)
			putc(s.charAt(i), f);
	}
	
	public static void puts(String s, FileOutputStream f)  throws IOException {
		fputs(s, f);
		putc('\n', f);
	}
	
	private static String removeQuotes(String s) {
		return s.replace('"', ' ');
	}
	
	public void savePNG(FileOutputStream f, boolean close, PGNHeaderData header) throws IOException {
		String result = PGNHeaderData.RESULTS[0];
		if (header.mResult > 0 && header.mResult < PGNHeaderData.RESULTS.length)
			result = PGNHeaderData.RESULTS[header.mResult];
		puts(
				"[Event \"" + removeQuotes(header.mEvent) + "\"]\n" +
				"[Site \"" + removeQuotes(header.mSite) + "\"]\n" +
				"[Date \"" + header.mYear + "." + header.mMonth + "." + header.mDay + "\"]\n" +
				"[Round \"" + header.mRound + "\"]\n" +
				"[White \"" + removeQuotes(header.mWhite) + "\"]\n" +
				"[Black \"" + removeQuotes(header.mBlack) + "\"]\n" +
				"[Result \"" + result + "\"]\n" +
				"[WhiteElo \"" + header.mWhiteElo + "\"]\n" +
				"[BlackElo \"" + header.mBlackElo + "\"]\n" +
				"[PlyCount \"" + mGame.size() +"\"]\n"
				, f);
		int index = mIndexInGame;
		while (mIndexInGame >= 0) {
			tahniZpet(0, true, null);
		}
		while (mIndexInGame < index) {
			if ((mIndexInGame & 1) == 1) {
				fputs(Integer.toString((mIndexInGame + 3) / 2, 10) + ".", f);
				putc(' ', f);
			}
			fputs(tah2Str(nalezPseudolegalniTahyVector(), ((ZasobnikStruct) mGame.elementAt(mIndexInGame + 1)).mTah, false), f);
			if ((mIndexInGame & 7) == 6) putc('\n', f); else putc(' ', f);
			
			tahni(0, true, false, null);
		}
		fputs(" " + result + "\n", f);
		if (close) f.close();
	}
}
