/*
 This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package cz.honzovysachy.mysleni;

import cz.honzovysachy.pravidla.Task;
import cz.honzovysachy.pravidla.ZasobnikStruct;

public class Minimax {
	public static final int MAT = 30000;
	public static final int BLIZKO_MATU = 20000;
	private static int dalOdMatu(int i) {
		if (i < BLIZKO_MATU && i > -BLIZKO_MATU) return -i;
		if (i > 0) return -(i - 1);
		return -(i + 1);
	}
	private static int blizKMatu(int i) {
		if (i < BLIZKO_MATU && i > -BLIZKO_MATU) return -i;
		if (i > 0) return -(1 + i);
		return -(i - 1);
	}
	
	
	public static int alfabetaBrani(Task task, int hloubka, int alfa, int beta) {
		int h = HodnotaPozice.hodnotaPozice(task, alfa, beta);
		if (hloubka == 0) return h;
		if (h > MAT || h < -MAT) return h;
		boolean sach = task.mBoardComputing.sach();
		if (h > alfa && !sach) {
			alfa = h;
			if (h >= beta) {
				return beta; 
			}
		}
		
		if (sach) 
			task.nalezPseudolegalniTahyZasobnik();
		else
			task.nalezPseudolegalniBraniZasobnik();
		int odkud = task.getOdkud();
		int kam = task.getKam();
		
		if (kam - odkud == 0) {
			task.mZasobnikTahu.pos--;
			if (task.mBoardComputing.sach()) return -MAT;
			return alfa;
		}
		hloubka--;
		
		for (int i = odkud; i < kam; i++) {
			int t = task.mZasobnikTahu.tahy[i];

			task.tahni(t, false, false, null);
			h = dalOdMatu(alfabetaBrani(task, hloubka, blizKMatu(beta), blizKMatu(alfa)));
			task.tahniZpet(t, false, null);

			if (h > alfa) {
				alfa = h;
				if (h >= beta) {
					task.mZasobnikTahu.pos--;
					return beta; 
				}
			}
		}
		task.mZasobnikTahu.pos--;
		return alfa;
	}

	protected static boolean nullMove(Task task, int prah, int hloubka) {
		boolean b;
		byte m;
		ZasobnikStruct z = (ZasobnikStruct)task.mZasobnik.elementAt(task.mIndexVZasobniku);

		switch (hloubka) {
		    case 1: hloubka = 0; break;
		    case 2: hloubka = 1; break;
		    default: hloubka -= 2;
		}

	//task.VNT++;
		z.mHashF ^= task.mHashF.mWhite;
		task.mBoardComputing.bily = !task.mBoardComputing.bily;
		m = task.mBoardComputing.mimoch;
		task.mBoardComputing.mimoch = 0;

		b =  (
			    (hloubka > 0 ? -alfabeta(task, hloubka, -prah, 1 - prah)    : 
			  -alfabetaBrani(task, 4, -prah, 1 - prah)
			    )
			    
			    >= prah);
			       
		task.mBoardComputing.mimoch = m;
		task.mBoardComputing.bily = !task.mBoardComputing.bily;

		//task.VNT--;
		z.mHashF ^= task.mHashF.mWhite;
  
		return b;
	}
	
	public static int alfabeta(Task task, int hloubka, int alfa, int beta) {
		if (System.currentTimeMillis() > task.mTimeStart) {
			task.mExitThinking = true;
			return alfa;
		}
		if (hloubka == 0) return alfabetaBrani(task, 4, alfa, beta);
		int h = HodnotaPozice.hodnotaPozice(task, alfa, beta);
		if (h > MAT || h < -MAT) return h;
		ZasobnikStruct z = (ZasobnikStruct) task.mZasobnik.elementAt(task.mIndexVZasobniku);
		z.mSach = task.mBoardComputing.ohrozeno(task.mBoardComputing.bily ? z.mBk : z.mCk, !task.mBoardComputing.bily);
		task.nalezPseudolegalniTahyZasobnik();
		int odkud = task.getOdkud();
		int kam = task.getKam();
		
		 if (task.mNullMove && !z.mSach && beta < MAT - 100) {
		      if (nullMove(task, beta, hloubka)) {
		        task.mZasobnikTahu.pos--;
		        return beta;
		      } 
		 }
		
		hloubka--;
		boolean legalMove = false;
		for (int i = odkud; i < kam; i++) {
			int t = task.mZasobnikTahu.tahy[i];
			//int hf = p.hashF.hash(p);
			task.tahni(t, false, false, null);
			if (task.mBoardComputing.sach(!task.mBoardComputing.bily)) {
				task.tahniZpet(t, false, null);
				continue;
			}
			legalMove = true;
			h = dalOdMatu(alfabeta(task, hloubka, blizKMatu(beta), blizKMatu(alfa)));
			task.tahniZpet(t, false, null);
			//int hg = p.hashF.hash(p);
			//if (hg != hf) {
			//	System.out.println(p.toString() + "\n" + p.tah2Str(t));
			//	throw new RuntimeException("Jauvajs");
			//}
			if (task.mExitThinking) {
				task.mZasobnikTahu.pos--;
				return alfa;
			}
			if (h > alfa) {
				alfa = h;
				if (h >= beta) {
					task.mZasobnikTahu.pos--;
					return beta; 
				}
			}
		}
		task.mZasobnikTahu.pos--;
		if (!legalMove) return z.mSach ? -MAT : 0;
		return alfa;
	}
	
	protected static void clear(Task task) {
		task.mIndexVZasobniku--;
		task.mZasobnikTahu.pos--;
	}
	
	
	
	public static int minimax(Task task, long casMs, ThinkingOutput output) {
		task.hodPos = 0;
		task.mTimeStart = System.currentTimeMillis() + casMs;
		task.mExitThinking = false;
		task.push();
		task.nalezTahyZasobnik();
		
		int odkud = task.getOdkud();
		int kam = task.getKam();
		if (kam - odkud == 0) {
			clear(task);
			return 0;
		}
		switch (kam - odkud) {
		case 0:
			clear(task);
			if (task.mBoardComputing.sach()) return -MAT;
			return 0;
		case 1:
			clear(task);
			return task.mZasobnikTahu.tahy[odkud];
		}
		for (int hloubka = 0; hloubka < 10; hloubka++) {
			if (output != null) output.depth(hloubka + 1);
			int max = -MAT + 1;
			for (int i = odkud; i < kam; i++) {
				int t = task.mZasobnikTahu.tahy[i];
				int hf = task.mHashF.hash(task.mBoardComputing);
				task.tahni(t, false, false, null);
				int h = dalOdMatu(alfabeta(task, hloubka, blizKMatu(MAT), blizKMatu(max)));
				task.tahniZpet(t, false, null);
				if (!task.mExitThinking && (i == 0 || h > max)) {
					max = h;
					if (i != 0)
					{
						for (int j = i; j > odkud; j--)
							task.mZasobnikTahu.tahy[j] = task.mZasobnikTahu.tahy[j - 1];
						task.mZasobnikTahu.tahy[odkud] = t;
					}
					if (output != null) {
						output.bestMove(task.tah2Str(t, task.mBoardComputing), max);
					}
				}

				int hg = task.mHashF.hash(task.mBoardComputing);
				if (hg != hf) {
					throw new RuntimeException("Error, board has changed!!!");
				}
				if (task.mExitThinking) break;
			}
			if (task.mExitThinking) break;
			//System.out.println(hloubka + " " + Task.tah2StrNoBoard(task.mZasobnikTahu.tahy[odkud]));
		}
		clear(task);
		System.out.println(task.hodPos);
		return task.mZasobnikTahu.tahy[odkud];
	}
}
