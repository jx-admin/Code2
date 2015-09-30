/*
  PieceImpl - A class to implement the functionality of a chess piece.

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
 * This class implements the functionality for a piece.
 */
public class PieceImpl implements Piece {


    // Instance variables

    /**
     * The color and type of a piece.
     */
    private byte _typeColor;

    
    // Constructors

    /**
     * Create a new piece instance.
     *
     * @param typeColor The color and type of piece, with color in bit 0 
     *                  and the type color in bit 1-3.
     */
    public PieceImpl( byte typeColor) {
	_typeColor = typeColor;
    }


    /**
     * Create a new Piece instance from type and color.
     *
     * @param type The type of the piece.
     * @param color The color of the piece.
     */
    public PieceImpl( byte type, byte color) {
	this( (byte)((type << 1) + color));
    }


    // Methods

    /**
     * Get the type of this piece.
     *
     * @return The type of this piece.
     */
    public final byte getType() {
	return (byte)(_typeColor >> 1);
    }

    /**
     * Set the type of this piece.
     *
     * @param type The type of this piece as defined as 
     *             constants in the Piece interface.
     */
    public final void setType( byte type) {
	_typeColor &= (byte)1;
	_typeColor |= (type << 1);
    }

    /**
     * Get the color of this piece.
     *
     * @return The color of this piece.
     */
    public final byte getColor() {
	return (byte)(_typeColor & (byte)1);
    }

    /**
     * Set the color of this piece.
     *
     * @param The new color of this piece.
     */
    public final void setColor( byte color) {
	_typeColor &= (byte)14;
	_typeColor |= color;
    }

    /**
     * Check, if this piece is white.
     *
     * @return true, if this piece is white, false if black.
     */
    public final boolean isWhite() {
	return (_typeColor & 1) != 0;
    }

    /**
     * Get type and color as 1 byte.
     *
     * @return Type and color and 1 byte.
     */
    public final byte getTypeAndColor() {
	return _typeColor;
    }
}
