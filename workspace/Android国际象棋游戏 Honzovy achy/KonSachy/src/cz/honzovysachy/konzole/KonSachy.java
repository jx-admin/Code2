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

package cz.honzovysachy.konzole;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import cz.honzovysachy.mysleni.Minimax;
import cz.honzovysachy.pravidla.Pozice;
import cz.honzovysachy.pravidla.Task;
import cz.honzovysachy.pravidla.ZasobnikStruct;

public class KonSachy {
	private static void hlavniDosCyklus() {
		Task task = new Task(null);
		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
		hlavni:
		while (true) {
			String s = "";
			try {
				s = br.readLine();
			} catch (IOException e) {
				System.out.println("Koncim");
				break;
			}
			//if (s.length() > 2)	s = s.substring(0, 2);
			if (s.equals("?")) {
				System.out.println(
						"? - tahle napoveda\n" +
						"ta - tahni\n" +
						"tz - tahni zpet\n" +
						"tg - test generatoru\n" +
						"tb - test brani\n" +
						"sa - tiskni sachovnici\n" +
						"bb - alfa beta brani\n" +
						"ko - konec\n");
				continue;
			}
			if (s.equals("ta")) {
				Vector t = task.nalezTahyVector();
				if (t.size() == 0) {
					System.out.println("Neni tah");
					continue;
				}
				int tah = Minimax.minimax(task, 5000, null);
				System.out.println(task.tah2Str(t, tah, true));
				task.tahni(tah, true, true, null);
				tiskniSachovnici(task.mBoardComputing);
				continue;
			}
			if (s.equals("tz")) {
				if (task.mIndexInGame >= 0)
				task.tahniZpet(((ZasobnikStruct)(task.mGame.elementAt(task.mIndexInGame))).mTah, true, null);
					tiskniSachovnici(task.mBoardComputing);
				continue;
			}
			if (s.equals("tg")) {
				Vector t = task.nalezTahyVector();
				for (int i = 0; i < t.size(); i++) {
					System.out.print(task.tah2Str(t, ((Integer)(t.elementAt(i))).intValue(), true) + " ");
				}
				System.out.println();
				continue;
			}
			if (s.equals("tb")) {
				task.nalezPseudolegalniBraniZasobnik();
				int odkud = task.getOdkud();
				int kam = task.getKam();
				for (int i = odkud; i < kam; i++) {
					System.out.print(task.tah2Str(new Vector(), task.mZasobnikTahu.tahy[i], true) + " (" + task.mZasobnikTahu.hodnoty[i] + ") ");
				}
				task.mZasobnikTahu.pos--;
				System.out.println();
				continue;
			}
			if (s.equals("bb")) {
				System.out.println(Minimax.alfabetaBrani(task, 4, -Minimax.MAT, Minimax.MAT));
				continue;
			}
			if (s.equals("sa")) {
				tiskniSachovnici(task.mBoardComputing);
				continue;
			}
			if (s.equals("ko")) {
				System.out.println("Koncim");
				break;
			}
			Vector t = task.nalezTahyVector();
			for (int i = 0; i < t.size(); i++) {
				int tah = ((Integer)(t.elementAt(i))).intValue();
				if (s.equals(task.tah2Str(t, tah, true))) {
					task.tahni(tah, true, true, null);
					tiskniSachovnici(task.mBoardComputing);
					continue hlavni;
				}
				
			}
			System.out.println("Neznamy prikaz nebo neplatny tah");
		}
	}
	public static void tiskniSachovnici(Pozice p) {
		System.out.print(p.toString());
	}
	public static void main(String[] args) {
		System.out.println("Honzuv sachovy program, '?' je napoveda");
		hlavniDosCyklus();
	}

}
