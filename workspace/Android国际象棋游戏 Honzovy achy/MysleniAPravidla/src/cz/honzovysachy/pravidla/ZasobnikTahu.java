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

package cz.honzovysachy.pravidla;


public class ZasobnikTahu {
	public static final int MAX_HLOUBKA_PROPOCTU = 32;
	public static final int HLOUBKA_ZASOBNIKU = 23 * 128;
	public ZasobnikTahu() {
		pos = 0;
	}

	   /** Tahy */
	public  int[] tahy = new int[HLOUBKA_ZASOBNIKU];
	public   int[] hodnoty = new int[HLOUBKA_ZASOBNIKU];
	  
	  /** Pocatky skupin tahu hranice[0] by mela byt vzdy 0 */
	public   int[] hranice = new int[MAX_HLOUBKA_PROPOCTU]; 
	  /** Prvni volna skupina (je-li 0, je zasobnik prazdny),
	      ukazuje take hloubku zanoreni aktualni pozice ve stromu propoctu*/
	public   int pos;
	  /** Pole, na nemz stoji pesec tahnuvsi v predchozim tahu o 2 pokud zaroven
	      na vedlejsim poli je souperuv pesec,
	      nebo 0 pokud se minule netahlo pescem o 2 a nebo se o 2 tahlo, ale souperuv
	      pesec neciha. */
	  byte[] mimoch = new byte[MAX_HLOUBKA_PROPOCTU];
	  /** Stavy-moznosti rochovat */
	  int[] roch = new int[MAX_HLOUBKA_PROPOCTU];
	  /** Sebrane figury tj. jaka figura byla sebrana tahem z teto pozice
	    (nebylo-li tazeno, nezajimave)*/
	  byte[] brani = new byte[MAX_HLOUBKA_PROPOCTU];
	  /** hodnota materialu bileho, hodnota materialu cerneho */  
	  int[] bm = new int[MAX_HLOUBKA_PROPOCTU];
	  int[] cm = new int[MAX_HLOUBKA_PROPOCTU];
	  /** hodnoty pozice (nezahrnuje material) z hlediska bileho */  
	  int[] hodpos = new int[MAX_HLOUBKA_PROPOCTU];
	  /** hodnoty hash funkce HashF */
	  int[] hF = new int[MAX_HLOUBKA_PROPOCTU];
	  /** hodnoty hash funkce HashF */
	  int[] hG = new int[MAX_HLOUBKA_PROPOCTU];
	  /** hodnoty hash funkce HashF */
	  //u32 hPechF[MaxHloubkaPropoctu];
	  /** hodnoty hash funkce HashF */
	  //u32 hPechG[MaxHloubkaPropoctu];
	  /** Cilova policka tahu - kvuli prohlubovani */
	  int[] kam = new int[MAX_HLOUBKA_PROPOCTU];
	  /** Je hrac na tahu v sachu ? */
	  boolean[] sach = new boolean[MAX_HLOUBKA_PROPOCTU];
	  /** ke kolika nevratnym zmenam (tah pescem nebo brani) doslo
	  na ceste z korene. Jeden tah - beznem brani pescem se
	  pocita jako dve zmeny, to ale nevadi.*/
	  int[] zmen = new int[MAX_HLOUBKA_PROPOCTU];
	  /** Ke kolika rozsirenim doslo na ceste z korene. 
	  Neuvazuje se rozsireni pri jedinem pripustnem
	  tahu.*/
	  int[] rozsir = new int[MAX_HLOUBKA_PROPOCTU];
	  /** K jakym druhum rozsireni doslo .*/
	  int[] druhRozsir = new int[MAX_HLOUBKA_PROPOCTU];
}
