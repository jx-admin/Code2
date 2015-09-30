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

import cz.honzovysachy.pravidla.Pozice;
import cz.honzovysachy.pravidla.Task;
import cz.honzovysachy.pravidla.ZasobnikStruct;

public class HodnotaPozice {
	
	public static final int[] mStdCenyFigur = {0, 50, 150, 150, 250, 450, Minimax.BLIZKO_MATU};
	
	static int StdCenaBPesce[] =
	
	{  100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   /*     a    b    c    d    e    f    g    h*/
	   100,   0,   0,   0,   0,   0,   0,   0,   0, 100, /* 1 */
	   100,   0,   0,  -1, - 8, - 8,   0,   0,   0, 100, /* 2*/
	   100,   1,   1,   1,   4,   4,   0,   1,   1, 100, /* 3*/
	   100,   1,   1,   6,   8,   8,   6,   1,   1, 100, /* 4*/
	   100,   4,   4,   6,  10,  10,   3,   4,   4, 100, /* 5*/
	   100,  10,  10,  10,  15,  15,  10,  10,  10, 100, /* 6*/
	   100,  20,  20,  20,  20,  20,  20,  20,  20, 100, /* 7*/
	   100,   0,   0,   0,   0,   0,   0,   0,   0 }; /* 8*/

	static int StdCenaBKone[] =
	{  100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   /*     a    b    c    d    e    f    g    h*/
	   100, -10,  -4,  -3,  -2,  -2,  -3,  -4, -10, 100, /* 1 */
	   100,  -5,  -5,  -3,   0,   0,  -3,  -5,  -5, 100, /* 2*/
	   100,  -3,   0,   3,   5,   5,   3,   0,  -3, 100, /* 3*/
	   100,  -2,   5,   5,   7,   7,   5,   5,  -2, 100, /* 4*/
	   100,   0,   5,   6,   8,   8,   6,   5,   0, 100, /* 5*/
	   100,   5,   6,   7,   8,   8,   7,   6,   5, 100, /* 6*/
	   100,   0,   5,   6,   8,   8,   6,   5,   0, 100, /* 7*/
	   100, -10,  -5,  -2,   0,   0,  -2,  -5, -10 }; /* 8*/
	static int StdCenaBKrale[] =
	{  100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   /*     a    b    c    d    e    f    g    h*/
	   100,  10,  15,  13,  -5,   0,  -5,  17,  12, 100, /* 1 */
	   100,  -5,  -5, -10, -15, -15, -10,  -5,  -5, 100, /* 2*/
	   100, -15, -15, -18, -20, -20, -18, -15, -15, 100, /* 3*/
	   100, -30, -30, -30, -30, -30, -30, -30, -30, 100, /* 4*/
	   100, -30, -30, -30, -30, -30, -30, -30, -30, 100, /* 5*/
	   100, -30, -30, -30, -30, -30, -30, -30, -30, 100, /* 6*/
	   100, -30, -30, -30, -30, -30, -30, -30, -30, 100, /* 7*/
	   100, -30, -30, -30, -30, -30, -30, -30, -30}; /* 8*/
	static int StdCenaKraleKoncovka[] =
	{  100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   /*     a    b    c    d    e    f    g    h*/
	   100, -20, -15, -12, -10, -10, -12, -15, -20, 100, /* 1 */
	   100, -15, -12,  -7,   0,   0,  -7, -12, -15, 100, /* 2*/
	   100, -12,  -7,   1,   5,   5,   1,  -7, -12, 100, /* 3*/
	   100, -10,   0,   5,  10,  10,   5,   0, -10, 100, /* 4*/
	   100, -10,   0,   5,  10,  10,   5,   0, -10, 100, /* 5*/
	   100, -12,  -7,   1,   5,   5,   1,  -7, -12, 100, /* 6*/
	   100, -15, -12,  -7,   0,   0,  -7, -12, -15, 100, /* 7*/
	   100, -20, -15, -12, -10, -10, -12, -15, -20}; /* 8*/
	static int StdCenaBStrelce[] =
	{  100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   /*     a    b    c    d    e    f    g    h*/
	   100,  -5,  -4,  -3,  -2,  -2,  -3,  -4,  -5, 100, /* 1 */
	   100,  -5,   6,  -2,   5,   5,  -2,   6,  -5, 100, /* 2*/
	   100,   0,   0,   1,   5,   5,   1,   0,   0, 100, /* 3*/
	   100,   0,   2,   5,   5,   5,   5,   5,   0, 100, /* 4*/
	   100,   0,   5,   6,   8,   8,   6,   5,   0, 100, /* 5*/
	   100,   0,   0,   0,   0,   0,   0,   0,   0, 100, /* 6*/
	   100,   0,   0,   0,   0,   0,   0,   0,   0, 100, /* 7*/
	   100,  -5,   0,   0,   0,   0,   0,   0,  -5 }; /* 8*/
	
	static int OpacnePole[] =

	{  100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   100, 100, 100, 100, 100, 100, 100, 100, 100, 100,
	   /*     a    b    c    d    e    f    g    h*/
	   100,  91,  92,  93,  94,  95,  96,  97,  98, 100, /* 1 */
	   100,  81,  82,  83,  84,  85,  86,  87,  88, 100, /* 2*/
	   100,  71,  72,  73,  74,  75,  76,  77,  78, 100, /* 3*/
	   100,  61,  62,  63,  64,  65,  66,  67,  68, 100, /* 4*/
	   100,  51,  52,  53,  54,  55,  56,  57,  58, 100, /* 5*/
	   100,  41,  42,  43,  44,  45,  46,  47,  48, 100, /* 6*/
	   100,  31,  32,  33,  34,  35,  36,  37,  38, 100, /* 7*/
	   100,  21,  22,  23,  24,  25,  26,  27,  28 }; /* 8*/
	
	public static short bm(Pozice p) {
		short sum = 0;
		for (int i = Pozice.a1; i <= Pozice.h8; i++) {
			int f = p.sch[i];
			if (f > 0 && f < 7)	sum += mStdCenyFigur[f];
		}
		return sum;
	}
	
	public static short cm(Pozice p) {
		short sum = 0;
		for (int i = Pozice.a1; i <= Pozice.h8; i++) {
			int f = -p.sch[i];
			if (f > 0 && f < 7)	sum += mStdCenyFigur[f];
		}
		return sum;
	}


	public static int hodnotaPozice(Task t, int alfa, int beta) {
		t.hodPos++;
		ZasobnikStruct z = (ZasobnikStruct) t.mZasobnik.elementAt(t.mIndexVZasobniku);
		Pozice p = t.mBoardComputing;
		int bm = z.mBm;
		int cm = z.mCm;
		
		if (bm < mStdCenyFigur[6]) return (-Minimax.MAT - 1) * (p.bily ? 1 : -1);
		if (cm < mStdCenyFigur[6]) return (Minimax.MAT + 1) * (p.bily ? 1 : -1);
		
		int suma = 0;
		suma = bm - cm;
		if (!p.bily) suma = -suma;
		if (alfa > Minimax.BLIZKO_MATU || beta < -Minimax.BLIZKO_MATU)	return suma;
		// TODO pres posledni pozici
		if (suma < alfa - 100 || suma > beta + 100) return suma;		
		
		int bp = 0;
		int cp = 0;
		
		boolean kralDoCentra = bm - mStdCenyFigur[6] < 550 || cm - mStdCenyFigur[6] < 550;
		
		for (int i = Pozice.a1; i <= Pozice.h8; i++) {
			switch (p.sch[i]) {
			case 0: break;
			case 100: break;
			case 1: bp += StdCenaBPesce[i]; break;
			case -1: cp += StdCenaBPesce[OpacnePole[i]]; break;
			case 2: bp += StdCenaBKone[i]; break;
			case -2: cp += StdCenaBKone[OpacnePole[i]]; break;
			case 3: bp += StdCenaBStrelce[i]; break;
			case -3: cp += StdCenaBStrelce[OpacnePole[i]]; break;
			case 6: bp += (kralDoCentra ? StdCenaKraleKoncovka : StdCenaBKrale)[i]; break;
			case -6: cp += (kralDoCentra ? StdCenaKraleKoncovka : StdCenaBKrale)[OpacnePole[i]]; break;
			}
		}

		suma = bm + bp - cm - cp;
		if (!p.bily) suma = -suma;
		
		return suma;
	}
}
