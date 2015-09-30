/*
  Piece - A interface to implement a data structure for a chess piece.

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

package de.java_chess.javaChess.piece;


/**
 * This interface has to be implemented by any chess piece.
 */
public interface Piece {

    // Static variables

    // The colors of pieces as constants.
    static byte BLACK = 0;
    static byte WHITE = 1;

    // The various types of pieces as constants.
    static byte PAWN   = 1;
    static byte BISHOP = 2;
    static byte KNIGHT = 3;
    static byte ROOK   = 4;
    static byte QUEEN  = 5;
    static byte KING   = 6;


    // Methods

    /**
     * Get the type of this piece.
     *
     * @return The type of the piece.
     */
    public byte getType();

    /**
     * Set the type of this piece.
     *
     * @param type The new type of this piece.
     */
    public void setType( byte type);

    /**
     * Get the color of this piece.
     *
     * @return The color of this piece.
     */
    public byte getColor();

    /**
     * Set the color of this piece.
     *
     * @param The new color of this piece.
     */
    public void setColor( byte color);

    /**
     * Check, if this piece is white.
     *
     * @return true, if this piece is white, false if black.
     */
    public boolean isWhite();

    /**
     * Get type and color as 1 byte.
     *
     * @return Type and color and 1 byte.
     */
    public byte getTypeAndColor();
}
