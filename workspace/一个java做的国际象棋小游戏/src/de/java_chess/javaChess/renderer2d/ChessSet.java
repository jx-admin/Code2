/*
  ChessSet - A class to provide a image of a set of chess pieces.

  Copyright (C) 2002,2003 Andreas Rueckert <mail@andreas-rueckert.de>

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package de.java_chess.javaChess.renderer2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.*;


/**
 * This class loads and provides a images with all the chess pieces.
 */
class ChessSet extends BufferedImage {
    ChessSet(Component c) {
	super( 240, 80, BufferedImage.TYPE_INT_ARGB);
	Image im = null;
	try {
	    // URL url = getClass().getResource("ChessPieces_neu.gif");
	    // im = Toolkit.getDefaultToolkit().getImage( url );
	    im = Toolkit.getDefaultToolkit().getImage( new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/ChessPieces02.gif"));
	} catch( MalformedURLException ignored) {}
	MediaTracker mT = new MediaTracker(c);
	mT.addImage(im, 0);
	try{
	    mT.waitForID(0);
	} catch(InterruptedException ie){}
	Graphics g = getGraphics();
	g.drawImage(im, 0, 0, c);
    }
}
