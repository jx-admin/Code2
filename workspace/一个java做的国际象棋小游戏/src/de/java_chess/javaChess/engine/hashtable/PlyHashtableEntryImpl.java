/*
  PlyHashtableEntryImpl - Implements a hashtable entry for plies.

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

package de.java_chess.javaChess.engine.hashtable;

import de.java_chess.javaChess.bitboard.*;
import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import java.util.zip.CRC32;


/**
 * The class implements the functionality to store
 * chess plies in a hashtable.
 */
public class PlyHashtableEntryImpl implements PlyHashtableEntry {

    // Instance variables

    /**
     * The board before this ply.
     */
    Board _board;

    /**
     * The ply to store.
     */
    Ply _ply;

    
    // Constructors

    /**
     * Create a new hashtable entry.
     *
     * @param board The board before the ply.
     * @param ply The ply to store.
     */
    public PlyHashtableEntryImpl( Board board, Ply ply) {
	setBoard( board);
	setPly( ply);
    }


    // Methods

    /**
     * Get the board of this entry.
     *
     * @return The board of this entry.
     */
    public final Board getBoard() {
	return _board;
    }

    /**
     * Set a new board for this entry.
     *
     * @param board The new board.
     */
    public final void setBoard( Board board) {
	_board = board;
    }

    /**
     * Get the ply.
     *
     * @return The ply.
     */
    public final Ply getPly() {
	return _ply;
    }

    /**
     * Set the ply.
     *
     * @param ply The ply to set.
     */
    public final void setPly( Ply ply) {
	_ply = ply;
    }

    /**
     * Check, if it's a move with white pieces.
     *
     * @return true, if it's a move with white pieces.
     */
    public final boolean isWhiteMove() {
	return getBoard().getPiece( getPly().getSource()).getColor() == Piece.WHITE;
    }
    
    /**
     * Get the hashcode for this ply.
     *
     * @return A hashcode for this ply.
     */
    public final long hashKey() {
	return hashKey( getBoard(), isWhiteMove());
    }

    /**
     * Get a hashcode for a given board and piece color.
     *
     * @param board The current board.
     * @param white true, if it's a ply with the white pieces.
     *
     * @return A hashcode for the given board and color.
     */
    public static long hashKey( Board board, boolean white) {
	
	// I use a CRC32 code as the hash code.
	CRC32 crc32 = new CRC32();

	if( board instanceof BitBoard) {

	    // Serialize the board as bytes, if it's a bitboard.
	    crc32.update( ( (BitBoard)board).getBytes());
	} else {

	    // Reuse the same position object for each square.
	    Position curPosition = new PositionImpl( 0);

	    for( int square = 0; square < 64; square++) {
		curPosition.setSquareIndex( square);
		Piece p = board.getPiece( curPosition);
		
		crc32.update( p != null ? p.getType() : 0);
	    }
	}
	
	// Change the code, depending on the color of the moved
	// piece, so we store the best move for white _and_ black
	// for the _same_ board in the hashtable.
	crc32.update( white ? 127 : 63);
	    
	// Return the lower 32 bits of the current crc32 value.
	return crc32.getValue();
    }
}
