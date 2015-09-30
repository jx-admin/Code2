/*
  PositionRenderer - A class to render chessboard squares.

  Copyright (C) 2003 The Java-Chess team <info@java-chess.de>

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
import javax.swing.*;


/**
 * This class renders a position on the chessboard,
 * no matter if it is empty or a piece on the square.
 */
class PositionRenderer extends JLabel {

    // Instance variables.

    
    // Constructors

    /**
     * Create a new position rendering for a empty position.
     */
    PositionRenderer() {
	super();  // Create a empty JLabel instance.

	// Even if we do not have to render an image yet, it might
	// get added later, when a piece moves on this square.
	setHorizontalAlignment( CENTER);
	setVerticalAlignment( CENTER);
    }

    /**
     * Create a new position rendering with a given image of a piece.
     *
     * image The image for the rendering.
     */
    PositionRenderer( Image image) {
	super( new ImageIcon( image));
    }


    // Methods

    /**
     * Move a piece from another position to this one.
     *
     * positionRenderer The position renderer for the source.
     */
    void getPieceFrom( PositionRenderer positionRenderer) {
	setIcon( positionRenderer.getIcon());
	positionRenderer.setIcon( null);
    }

    /**
     * Set a new icon and align it properly.
     *
     * @param icon The new icon.
     */
    public void setIcon( ImageIcon icon) {
	super.setIcon( icon);
	if( icon != null) {
	    invalidate();
	} else {
	    revalidate();
	}
    }
}
