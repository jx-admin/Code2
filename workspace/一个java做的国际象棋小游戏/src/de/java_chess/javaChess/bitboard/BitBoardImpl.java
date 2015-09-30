/*
  BitBoardImpl - A class, that implements a chess board data structure 
                 as layered bitmaps.

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

import de.java_chess.javaChess.*;
import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;


/**
 * This class stores a chess board as a couple of
 * overlayed longs (64-Bit wide).
 */
public class BitBoardImpl implements BitBoard {


    // Instance variables

    /**
     * Store the board in 4 64 bit layers.
     * Layer 0 holds the color, layer 1-3 hold the piece code or 0, 
     * if the square is empty.
     */
    long [] _boardLayer = new long[4];


    // Constructors

    /**
     * Create a new instance of a chess board with pieces on their
     * initial positions.
     */
    public BitBoardImpl() {
	initialPosition();
    }


    // Methods

    /**
     * Create a clone of this board.
     *
     * @return A clone of this board.
     */
    protected final Object clone() {
	BitBoardImpl clone = new BitBoardImpl();
	for( int i = 0; i < 4; i++) {
	    clone._boardLayer[i] = _boardLayer[i];
	}
	return clone;
    }

    /**
     * Remove all the pieces from the board.
     */
    public final void emptyBoard() {
	for( int i = 0; i < 4; i++) {
	    _boardLayer[i] = 0L;
	}
    }

    /**
     * Set all the pieces to their initial positions.
     */
    public final void initialPosition() {
	for( int i = 0; i < 8; i++) {
	    setPiece( new PieceImpl( Piece.PAWN, Piece.WHITE), new PositionImpl( 8 + i));
	    setPiece( new PieceImpl( Piece.PAWN, Piece.BLACK), new PositionImpl( 48 + i));
	}
	setPiece( new PieceImpl( Piece.ROOK, Piece.WHITE), new PositionImpl( 0));
	setPiece( new PieceImpl( Piece.KNIGHT, Piece.WHITE), new PositionImpl( 1));
	setPiece( new PieceImpl( Piece. BISHOP, Piece.WHITE), new PositionImpl( 2));
	setPiece( new PieceImpl( Piece.QUEEN, Piece.WHITE), new PositionImpl( 3));
	setPiece( new PieceImpl( Piece.KING, Piece.WHITE), new PositionImpl( 4));
	setPiece( new PieceImpl( Piece. BISHOP, Piece.WHITE), new PositionImpl( 5));
	setPiece( new PieceImpl( Piece.KNIGHT, Piece.WHITE), new PositionImpl( 6));
	setPiece( new PieceImpl( Piece.ROOK, Piece.WHITE), new PositionImpl( 7));
	setPiece( new PieceImpl( Piece.ROOK, Piece.BLACK), new PositionImpl( 56));
	setPiece( new PieceImpl( Piece.KNIGHT, Piece.BLACK), new PositionImpl( 57));
	setPiece( new PieceImpl( Piece. BISHOP, Piece.BLACK), new PositionImpl( 58));
	setPiece( new PieceImpl( Piece.QUEEN, Piece.BLACK), new PositionImpl( 59));
	setPiece( new PieceImpl( Piece.KING, Piece.BLACK), new PositionImpl( 60));
	setPiece( new PieceImpl( Piece. BISHOP, Piece.BLACK), new PositionImpl( 61));
	setPiece( new PieceImpl( Piece.KNIGHT, Piece.BLACK), new PositionImpl( 62));
	setPiece( new PieceImpl( Piece.ROOK, Piece.BLACK), new PositionImpl( 63));
    }

    /**
     * Get the piece of a given position, or null of the square is empty.
     *
     * @param position The position of the piece.
     * 
     * @return The piece of the square or null, of the square is empty.
     */
    public final Piece getPiece( Position position) {
	int bitpos = position.getSquareIndex();

	int pieceType = (((int)(_boardLayer[1] >> bitpos) & 1)
			 | (((int)(_boardLayer[2] >> bitpos) & 1) << 1)
			 | (((int)(_boardLayer[3] >> bitpos) & 1) << 2));

	return (pieceType == 0) ? null : new PieceImpl( (byte)(( pieceType << 1) | (( _boardLayer[0] >> bitpos) & 1)));
    }

    /**
     * Set a piece on a given square.
     *
     * @param piece The piece to set, or null to empty the square.
     * @param position The position of the square.
     */
    public final void setPiece( Piece piece, Position position) {
	byte pieceCodeColor = ((piece == null) ? 0 : piece.getTypeAndColor());
	long bitmask = 1L <<  position.getSquareIndex();
	long bitFilter = ~bitmask;
	
	for( int i = 0; i < 4; i++) {
	    if( ( pieceCodeColor & 1) != 0) {
		_boardLayer[i] |= bitmask;  // Set this bit to 1.
	    } else {
		_boardLayer[i] &= bitFilter;  // Set this bit to 0.
	    }
	    pieceCodeColor >>= 1;
	}
    }

    /**
     * Move a piece from one square to another.
     *
     * @param ply The ply to perform.
     */
    public final void doPly( Ply ply) {
	Piece movedPiece = getPiece( ply.getSource());

	// Check, if it was a castling
	if( ply instanceof CastlingPly) {
	    int offset = movedPiece.isWhite() ? 0 : 56;
	    if( ( (CastlingPly)ply).isLeftCastling()) {
		setPiece( movedPiece, new PositionImpl( 2 + offset));
		// Move the rook to the right
		doPly( new PlyImpl( new PositionImpl( 0 + offset), new PositionImpl( 3 + offset)));
	    } else {
		setPiece( movedPiece, new PositionImpl( 6 + offset));
		// Move the rook to the left
		doPly( new PlyImpl( new PositionImpl( 7 + offset), new PositionImpl( 5 + offset)));
	    }
	} else {
	    // If a pawn has just reached the last row
	    if(ply instanceof TransformationPly) {
		// Set a piece of the new type on the destination square.
		setPiece( new PieceImpl( ( (TransformationPly)ply).getTypeAfterTransformation(), movedPiece.getColor()), ply.getDestination());
	    } else {
		// Copy the piece from source square to destination square.
		setPiece( movedPiece, ply.getDestination());

		// If it's a en passant ply, remove the attacked pawn.
		if( ply instanceof EnPassantPly) {
		    setPiece( null, ( (EnPassantPly)ply).getAttackedPosition());
		}
	    }
	}

	// Empty the source square.
	setPiece( null, ply.getSource());
    }

    /**
     * Return a new board, that results from a given ply.
     *
     * @param ply The ply to perform.
     *
     * @return A new board with the game position after the ply.
     */
    public final Board getBoardAfterPly( Ply ply) {
	BitBoard newBoard = (BitBoard)clone();
	newBoard.doPly( ply);
	return newBoard;
    }

    /**
     * Get the positions of some pieces as a long (64 bit wide) bitmask.
     *
     * @param pieceTypeColor The color and type of the pieces.
     */
    public final long getPositionOfPieces( int pieceTypeColor) {
	return (((pieceTypeColor & 1) != 0 ? _boardLayer[0] : ~_boardLayer[0])
		& ((pieceTypeColor & 2) != 0 ? _boardLayer[1] : ~_boardLayer[1])
		& ((pieceTypeColor & 4) != 0 ? _boardLayer[2] : ~_boardLayer[2])
		& ((pieceTypeColor & 8) != 0 ? _boardLayer[3] : ~_boardLayer[3]));
    }

    /**
     * Get a bitmask with all the free squares.
     *
     * @return A bitmask with all the empty squares marked by a 1 bit.
     */
    public final long getEmptySquares() {
	return ~( _boardLayer[1] | _boardLayer[2] | _boardLayer[3]);
    }

    /**
     * Get all white or black pieces.
     *
     * @param white true, if the white pieces are requested, 
     *              false for the black pieces.
     */
    public final long getAllPiecesForColor( boolean white) {
	return ( ( _boardLayer[1] | _boardLayer[2] | _boardLayer[3]) 
		 & ( white ? _boardLayer[0] : ~_boardLayer[0]));
    }

    /**
     * Get the board as a byte stream.
     *
     * @return The board as a array of bytes.
     */
    public final byte [] getBytes() {

        // A buffer for the bytes.
        byte [] buffer = new byte[ 32];
	int bufferIndex = 0;

	for( int layer = 0; layer < 4; layer++) {

	    // Get the current layer.
	    long currentLayer = _boardLayer[ layer];

	    // Now shift the layer in the buffer.
	    for( int bytePos = 0; bytePos < 8; bytePos++) {
	        buffer[ bufferIndex++] = (byte)( (int)currentLayer & 0xFF);
		currentLayer >>= 8;
	    }
	}

	return buffer;  // Return the buffer with the bytes.
    }
}
