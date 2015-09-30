/*
  Ply - A interface to implement one ply of a chess game.

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

package de.java_chess.javaChess.ply;

import de.java_chess.javaChess.position.*;


/**
 * This interface has to be implemented by any ply.
 */
public interface Ply {

    // Static variables


    // Methods
    
    /**
     * Get the source of the ply.
     */
    Position getSource();

    /**
     * Set the source of the ply.
     */
    void setSource( Position source);

    /**
     * Get the destination of the piece.
     */
    Position getDestination();

    /**
     * Set the destination of the piece.
     */
    void setDestination( Position destination);

    /**
     * Test, if this ply is equal to another ply.
     *
     * @param ply The other ply.
     *
     * @return true, if the 2 plies are equal.
     */
    boolean equals( Ply ply);
}
