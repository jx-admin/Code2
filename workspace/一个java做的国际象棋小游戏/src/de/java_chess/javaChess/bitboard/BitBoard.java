/*
  BitBoard - A interface to implement a chess board data structure as
             layered bitmaps.

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

package de.java_chess.javaChess.bitboard;

import de.java_chess.javaChess.board.*;


/**
 * This interface defines the methods to be implemented by any 
 * bitboard implementation.
 */
public interface BitBoard extends Board {
    
    // Static constants.

    public final static long _LINE_A = 0x0101010101010101L;
    public final static long _LINE_B = 0x0202020202020202L;
    public final static long _LINE_C = 0x0404040404040404L;
    public final static long _LINE_D = 0x0808080808080808L;
    public final static long _LINE_E = 0x1010101010101010L;
    public final static long _LINE_F = 0x2020202020202020L;
    public final static long _LINE_G = 0x4040404040404040L;
    public final static long _LINE_H = 0x8080808080808080L;
    public final static long _NOT_LINE_A = ~_LINE_A;
    public final static long _NOT_LINE_B = ~_LINE_B;
    public final static long _NOT_LINE_C = ~_LINE_C;
    public final static long _NOT_LINE_D = ~_LINE_D;
    public final static long _NOT_LINE_E = ~_LINE_E;
    public final static long _NOT_LINE_F = ~_LINE_F;
    public final static long _NOT_LINE_G = ~_LINE_G;
    public final static long _NOT_LINE_H = ~_LINE_H;
    public final static long _ROW_1 = 0x00000000000000FFL;
    public final static long _ROW_2 = 0x000000000000FF00L;
    public final static long _ROW_3 = 0x0000000000FF0000L;
    public final static long _ROW_4 = 0x00000000FF000000L;
    public final static long _ROW_5 = 0x000000FF00000000L;
    public final static long _ROW_6 = 0x0000FF0000000000L;
    public final static long _ROW_7 = 0x00FF000000000000L;
    public final static long _ROW_8 = 0xFF00000000000000L;
    public final static long _NOT_ROW_1 = ~_ROW_1;
    public final static long _NOT_ROW_2 = ~_ROW_2;
    public final static long _NOT_ROW_3 = ~_ROW_3;
    public final static long _NOT_ROW_4 = ~_ROW_4;
    public final static long _NOT_ROW_5 = ~_ROW_5;
    public final static long _NOT_ROW_6 = ~_ROW_6;
    public final static long _NOT_ROW_7 = ~_ROW_7;
    public final static long _NOT_ROW_8 = ~_ROW_8;


    // Methods

    /**
     * Get the position of some pieces as a long (64 bit wide) bitmask.
     *
     * @param pieceTypeColor The color and type of the pieces.
     *
     * @return A bitmask with the positions of these pieces.
     */
    long getPositionOfPieces( int pieceTypeColor);

    /**
     * Get the position of all empty squares.
     *
     * @return The position of all empty squares.
     */
    long getEmptySquares();

    /**
     * Get all white or black pieces.
     *
     * @param white true, if the white pieces are requested, 
     *              false for the black pieces.
     */
    long getAllPiecesForColor( boolean white);

    /**
     * Get the board as a byte stream.
     *
     * @return The board as a array of bytes.
     */
    byte [] getBytes();
}
