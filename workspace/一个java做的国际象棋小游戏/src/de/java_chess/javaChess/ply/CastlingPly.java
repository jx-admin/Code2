/*
  CastlingPly - A interface to implement a castling ply.

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


/**
 * This interface defines the functionality for a castling ply.
 */
public interface CastlingPly extends Ply {

    // Static variables

    
    // Methods

    /**
     * Check, if this castling goes to the left side of the board.
     *
     * @return true, if the castling goes to the left side, false otherwise.
     */
    boolean isLeftCastling();

    /**
     * Set the flag, if the castling goes left.
     *
     * @param goesLeft Flag to indicate if the castling goes left.
     */
    void setLeftCastling( boolean goesLeft);
}
