package cz.honzovysachy.pravidla;

public class Hash {

		int[][] mF; //[13][99];
	/* [od cerneho krale pres prazdne pole az po bileho krale][pole]*/
		public int mWhite;
		int[] mCastle; //[16];
		int[] mEp; //[69];
		
		public int iRandom() {
			return (int) (((Math.random() - 0.5) * 2) * 0x7FFFFFFF);
		}
		public Hash() {
			mF = new int[13][];
			for (int i = 0; i < mF.length; i++) {
				mF[i] = new int[99];
				for (int j = 0; j < mF[i].length; j++)
					mF[i][j] = iRandom();
			}
			mWhite =  iRandom();
			mCastle = new int[16];
			for (int i = 0; i < mCastle.length; i++)
				mCastle[i] = iRandom();
			mEp = new int[69];
			for (int i = 0; i < mEp.length; i++)
				mEp[i] = iRandom();
		}
		public int hash(Pozice p) {
			int i,j;
			int ret;

			ret = 0;
			for (i = 0; i <= 7; i++)
				for(j = 0; j <= 7; j++)
					ret ^= mF[p.sch[Pozice.a1 + j + i * 10] + 6][Pozice.a1 + j + i * 10];
			ret ^= mEp[p.mimoch];
			ret ^= mCastle[p.roch];
			if (p.bily) ret ^= mWhite;
			return ret;
		}
	}

