/*
  TransformationPly - A interface to implement a pawn ply to the last
                      row, so it transforms into another piece type.

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
 * This interface is for pawn plies, that reach the last row
 * and transform into a new piece type.
 */
public interface TransformationPly extends Ply {

    // Methods

    /**
     * Get the new type of the piece after the transformation.
     *
     * @return The new piece after the transformation.
     */
    byte getTypeAfterTransformation();

    /**
     * Set the new piece type after the transformation.
     *
     * @param pieceType The new piece type after the transformation.
     */
    void setTypeAfterTransformation( byte pieceType);
}
