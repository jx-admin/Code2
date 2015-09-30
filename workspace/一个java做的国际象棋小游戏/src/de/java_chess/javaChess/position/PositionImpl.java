/*
  PositionImpl - A class to implement the functionality of the position 
                 of one piece of a chess game.

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

package de.java_chess.javaChess.position;


/**
 * This class implements the functionality to store the position
 * of a chess piece.
 */
public class PositionImpl implements Position {

    // Instance variables

    // The index of the chessboard square.
    private int _squareIndex;


    // Constructors

    public PositionImpl( int squareIndex) {
	setSquareIndex( squareIndex);
    }


    // Methods

    /**
     * Get the square index of this position (0-63).
     *
     * @return The square index of this position.
     */
    public final int getSquareIndex() {
	return _squareIndex;
    }

    /**
     * Set the square index of this position.
     *
     * @param squareIndex The new square index of this position.
     */
    public final void setSquareIndex( int squareIndex) {
	_squareIndex = squareIndex;
    }

    /**
     * Get the row index of this position (0-7).
     *
     * @return The row index of this position.
     */
    public final int getRowIndex() {
	return _squareIndex >> 3;
    }

    /**
     * Get the line index of this postion (0-7).
     *
     * @return The line index of this position.
     */
    public final int getLineIndex() {
	return _squareIndex & 7;
    }

    /**
     * Convert this position to a square name (like 'a4').
     *
     * @return The suare name of this position.
     */
    public final String toSquareName() {
	byte [] byteRepresentation = new byte[2];
	byteRepresentation[0] = (byte)((int)'a' + getLineIndex());
	byteRepresentation[1] = (byte)((int)'1' + getRowIndex());

	return new String( byteRepresentation);
    }

    /**
     * Test if 2 positions are equal.
     *
     * @param Another position.
     *
     * @return true, if the positions are equal, false otherwise.
     */
    public final boolean equals( Position pos) {
	return pos.getSquareIndex() == getSquareIndex();
    }
}
