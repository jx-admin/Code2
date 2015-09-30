/*
  BitBoardAnalyzerImpl - A class to analyze the game position on a
                         BitBoard type chessbaord.

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

package de.java_chess.javaChess.engine;

import de.java_chess.javaChess.*;
import de.java_chess.javaChess.bitboard.*;
import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.position.*;


/**
 * The class implements the functionality to analyze
 * a game position, stored as a bitboard.
 */
public class BitBoardAnalyzerImpl implements BitBoardAnalyzer {

    // Static variables

    // The position value of pawns on all the squares.
    static short [] _pawnPositionalValue = {  0,  0,  0,  0,  0,  0,  0,  0,
					      0,  0,  0,  0,  0,  0,  0,  0,
					      2,  3,  4,  5,  5,  4,  3,  2,
					      3,  4,  5,  6,  6,  5,  4,  3,
					      4,  5,  6,  7,  7,  6,  5,  4,
					      5,  6,  7,  8,  8,  7,  6,  5,
					      6,  7,  8,  9,  9,  8,  8,  7,
					      7,  8,  9, 10, 10,  9,  8,  7 };

    // The positional value of knights on the various squares.
    static short [] _knightPositionalValue = {  0,  0,  0,  0,  0,  0,  0,  0,
						0,  0,  0,  0,  0,  0,  0,  0,
						0,  0,  1,  5,  5,  1,  0,  0,
						1,  3,  5,  6,  6,  5,  3,  1,
						1,  3,  5,  6,  6,  5,  3,  1,
						0,  0,  1,  6,  6,  1,  0,  0,
						0,  0,  0,  0,  0,  0,  0,  0,
						0,  0,  0,  0,  0,  0,  0 , 0 };

    // The positional values of bishops on the various squares.
    static short [] _bishopPositionalValue = {  0,  0,  0,  0,  0,  0,  0,  0,
						0,  4,  0,  0,  0,  0,  4,  0,
						0,  3,  5,  3,  3,  5,  3,  0,
						0,  0,  0,  6,  6,  0,  0,  0,
						0,  0,  0,  6,  6,  0,  0,  0,
						0,  3,  5,  3,  3,  5,  3,  0,
						0,  4,  0,  0,  0,  0,  4,  0,
						0,  0,  0,  0,  0,  0,  0,  0 };


    // Instance variables

    /**
     * The currently analyzed board.
     */
    private BitBoard _board;

    /**
     * The flag to indicate, if white moves next.
     */
    private boolean _whiteMoves;

    /**
     * A ply generator instance to simulate moves.
     */
    PlyGenerator _plyGenerator;

    /**
     * The position of the king.
     */
    private long _kingPosition;

    /**
     * The line of the king position.
     */
    private int _kingLine;

    /**
     * The row of the king position.
     */
    private int _kingRow;

    /**
     * The empty squares of the current board as a bitmask.
     */
    private long _emptySquares;

    /**
     * The square with pieces on them as a bitmask.
     */
    private long _nonEmptySquares;
    

    // Constructors

    /**
     * Create a new bitboard analyzer.
     *
     * @param plyGenerator A PlyGenerator instance to simulate moves.
     */
    public BitBoardAnalyzerImpl( PlyGenerator plyGenerator) {
	setPlyGenerator( plyGenerator);
    }


    // Methods

    /**
     * Get the currently analyzed board.
     *
     * @return The currently analyzed board.
     */
    public final BitBoard getBoard() {
	return _board;
    }

    /**
     * Set a new board to be analyzed.
     *
     * @param board The new board.
     */
    public final void setBoard( BitBoard board) {
	_board = board;
    }

    /**
     * Check, if white moves next.
     *
     * @return true, if white moves next, false otherwise.
     */
    private final boolean whiteHasMoveRight() {
	return _whiteMoves;
    }

    /**
     * Set the flag, if white is about to move.
     *
     * @param white Flag to indicate, if white has the
     *              next move.
     */
    public final void setMoveRight( boolean white) {
	_whiteMoves = white;
    }

    /**
     * Get the ply generator.
     *
     * @return The PlyGenerator.
     */
    private final PlyGenerator getPlyGenerator() {
	return _plyGenerator;
    }

    /**
     * Set a new PlyGenerator instance.
     *
     * @param plyGenerator The new PlyGenerator instance.
     */
    private final void setPlyGenerator( PlyGenerator plyGenerator) {
	_plyGenerator = plyGenerator;
    }

    /**
     * Analyze the current board.
     */
    public final short analyze() {

	// A check thread has a value, too.
	short checkValue = 0;

	// Start with the tests, if one of the players is in check.
	// It's important to start the test with the color that moves next!
	if( whiteHasMoveRight()) {
	    if( isInCheck( true)) {                 // If the king of the moving player is in check,
		checkValue += BitBoardAnalyzer.BLACK_WIN;  // the player seems to win.
	    }
	    if( isInCheck( false)) {                  // If the opponent's king is in check,
		checkValue += BitBoardAnalyzer.WHITE_WIN;    // the opponent seems to win.
	    }
	} else {
	    if( isInCheck( false)) {                  // If the opponent's king is in check,
		checkValue += BitBoardAnalyzer.WHITE_WIN;    // the opponent seems to win.
	    }
	    if( isInCheck( true)) {                 // If the king of the moving player is in check,
		checkValue += BitBoardAnalyzer.BLACK_WIN;  // the player seems to win.
	    }
	}
	
	// Now compute the position and material value of all pieces.

	short materialValue = 0;     // Count the figures and their material value.
	short positionalValue = 0;   // Score the position value.

	// Check the entire board.

	// I reuse the same PositionImpl object to avoid the overhead of
	// object instancing for each square.
	Position pos = new PositionImpl(0);

	for( int i = 0; i < 64; i++) {
	    pos.setSquareIndex( i);
	    Piece p = getBoard().getPiece( pos);
	    if( p != null) {
		short mValue = 0;
		short pValue = 0;

		// Add the value of the piece
		switch( p.getType()) {
		case Piece.PAWN: 
		    mValue = 10; 
		    pValue = _pawnPositionalValue[ p.isWhite() ? i : ( ( 7 - (i >> 3)) << 3) + ( i & 7)];
		    break;
		case Piece.KNIGHT: 
		    pValue = _knightPositionalValue[i];
		    mValue = 30;
		    break;
		case Piece.BISHOP: 
		    pValue = _bishopPositionalValue[i];
		    mValue = 30; 
		    break;
		case Piece.ROOK: mValue = 45; break;
		case Piece.QUEEN: mValue = 80; break;
		}
                if( p.isWhite()) {
		    materialValue += mValue;
		    positionalValue += pValue;
		} else {
		    materialValue -= mValue;
		    positionalValue -= pValue;
		}
	    }
	}

	// Return a weighted score
	return (short)( (short)2 * positionalValue + (short)7 * materialValue + checkValue);
    }

    /**
     * Test, if the given player is in check.
     *
     * @param white Flag, if the white king is to test.
     *
     * @return true, if the king is in check, false otherwise.
     */
    public final boolean isInCheck( boolean white) {

	// Get the position of the king.
	_kingPosition = getBoard().getPositionOfPieces( white ? Piece.KING << 1 | 1 : Piece.KING << 1 );
	int kingSquare = BitUtils.getHighestBit( _kingPosition);
	_kingRow = kingSquare >> 3;  // Compute the line and row of the king for later use.
	_kingLine = kingSquare & 7;

	// Get and cache the empty squares of the current board.
	_emptySquares = getBoard().getEmptySquares(); 

	// Compute the squares, that are not empty.
	_nonEmptySquares = ~_emptySquares;
	
	return  isInKnightCheck( white) 
	    || isInBishopCheck( white) 
	    || isInRookCheck( white) 
	    || isInQueenCheck( white) 
	    || isInPawnCheck( white);
    }

    /**
     * Test if a king is in check on a given board.
     *
     * @param board The board to test.
     * @param white true, if the white king is checked, false otherwise.
     */
    public final boolean isInCheck( BitBoard board, boolean white) {
	setBoard( board);
	return isInCheck( white);
    }

    /**
     * Check if a king is in check by a knight.
     *
     * @param white Flag to indicate if we test the white king.
     *
     * @return true, if the king is in check. false otherwise.
     */
    private final boolean isInKnightCheck( boolean white) {

	// If we check for the white king, get the black knights
	long knightPositions = getBoard().getPositionOfPieces( white ? Piece.KNIGHT << 1 : ( Piece.KNIGHT << 1) | 1);

	// Compute a bitmask, that has 1 bits on all possible knight destinations
	while( knightPositions != 0L) {

	    // Get the position of the first knight.
	    int highestBit = BitUtils.getHighestBit( knightPositions);

	    // Get all the moves for this knight and return immediately, if
	    // the king is in check from this knight.
	    if( ( getPlyGenerator().getKnightPlies( highestBit) & _kingPosition) != 0L) {
		return true;
	    }

	    // Remove this knight from the list.
	    knightPositions &= ~(1L << highestBit);
	}
	return false;
    }

    /**
     * Check if a king is in check by a bishop.
     *
     * @param white Flag to indicate if we test the white king.
     *
     * @return true, if the king is in check. false otherwise.
     */
    private final boolean isInBishopCheck( boolean white) {

	// Get all the bishop positions of the opposite color.
	long bishopPositions = getBoard().getPositionOfPieces( white ? Piece.BISHOP << 1 : ( Piece.BISHOP << 1) | 1);

	while( bishopPositions != 0L) {
	    int highestBit = BitUtils.getHighestBit( bishopPositions);

	    if( isInBishopPositionCheck( highestBit)) {
		return true;
	    }

	    bishopPositions &= ~(1L << highestBit);
	}
	return false;
    }

    /**
     * Check if the king is in check by a bishop.
     *
     * @param square The square, where the bishop is located.
     *
     * @return true, if the king is in check. false otherwise.
     */
    private final boolean isInBishopPositionCheck( int square) {
	
	// Compute row and line of the rook.
	int bishopRow = square >> 3;
	int bishopLine = square & 7;

	if( Math.abs( bishopRow - _kingRow) == Math.abs( bishopLine - _kingLine)) {
	    long bishopMask = 1L << square;
	    if( bishopLine < _kingLine) {  // The bishop is left from the king
		if( bishopRow < _kingRow) {  // The bishop is below the king
		    // Move the bishop to the upper right.
		    while( ( ( bishopMask = ( bishopMask << 9)) & _nonEmptySquares) == 0L);
		} else {
		    // Move the bishop to the lower right.
		    while( ( ( bishopMask = ( ( bishopMask >> 7) & 0x01FFFFFFFFFFFFFFL)) & _nonEmptySquares) == 0L);
		}
	    } else {  // The bishop is right from the king.
		if( bishopRow < _kingRow) {  // The bishop is below the king
		    // Move the bishop to the upper left.
		    while( ( ( bishopMask = ( bishopMask << 7)) & _nonEmptySquares) == 0L);
		} else {
		    // Move the bishop to the lower left
		    while( ( ( bishopMask = ( ( bishopMask >> 9) & 0x007FFFFFFFFFFFFFL)) & _nonEmptySquares) == 0L);
		}
	    }
	    return ( ( bishopMask & _kingPosition) != 0L);
	}
	return false;
    }
    
    /**
     * Check if a king is in check by a rook.
     *
     * @param white Flag to indicate if we test the white king.
     *
     * @return true, if the king is in check. false otherwise.
     */
    private final boolean isInRookCheck( boolean white) {

	// Get all the rook positions of the opposite color.
	long rookPositions = getBoard().getPositionOfPieces( white ? Piece.ROOK << 1 : ( Piece.ROOK << 1) | 1);

	while( rookPositions != 0L) {
	    int highestBit = BitUtils.getHighestBit( rookPositions);

	    if( isInRookPositionCheck( highestBit)) {
		return true;
	    }

	    rookPositions &= ~(1L << highestBit);
	}
	return false;
    }

    /**
     * Check, if the king is in check of a rook position.
     *
     * @param square The square, where the rook is located.
     *
     * @return true, if the king is in check of this rook. 
     *         False otherwise.
     */
    private final boolean isInRookPositionCheck( int square) {

	// Compute row and line of the rook.
	int rookRow = square >> 3;
	int rookLine = square & 7;
	long rookMask = 1L << square;  // Compute a bitmask for the rook.

	if( rookLine == _kingLine) {

	    if( rookRow > _kingRow) {
		while( ( ( rookMask = ( ( rookMask >> 8) & 0x00FFFFFFFFFFFFFFL)) & _nonEmptySquares) == 0L);
	    } else {
		while( ( ( rookMask <<= 8) & _nonEmptySquares) == 0L);
	    }
	    if( ( rookMask & _kingPosition) != 0L) {
		return true;
	    }
	} else {
	    if( rookRow == _kingRow) {
		if( rookLine > _kingLine) {
		    while( ( ( rookMask = ( ( rookMask >> 1) & 0x7FFFFFFFFFFFFFFFL)) & _nonEmptySquares) == 0L);
		} else {
		    while( ( ( rookMask <<= 1) & _nonEmptySquares) == 0L);
		}
		if( ( rookMask & _kingPosition) != 0L) {
		    return true;
		}  
	    }
	}
	return false;  // No check for this rook.
    }

    /**
     * Check if a king is in check by a queen.
     *
     * @param white Flag to indicate if we test the white king.
     *
     * @return true, if the king is in check. false otherwise.
     */
    private final boolean isInQueenCheck( boolean white) {

	// Get all the queen positions of the opposite color.
	long queenPositions = getBoard().getPositionOfPieces( white ? Piece.QUEEN << 1 : ( Piece.QUEEN << 1) | 1);

	// Since we can have more than 1 queen after a pawn reached the 8th row, check for all the bits
	while( queenPositions != 0L) {
	    int highestBit = BitUtils.getHighestBit( queenPositions);

	    if( isInQueenPositionCheck( highestBit)) {
		return true;
	    }

	    queenPositions &= ~(1L << highestBit);
	}
	return false;
    }

    /**
     * Check, if the king is in check of a queen.
     *
     * @param square The square, where the queen is located.
     *
     * @return true, if the king is in check. false otherwise.
     */
    private final boolean isInQueenPositionCheck( int square) {
	return isInRookPositionCheck( square) || isInBishopPositionCheck( square);
    }

    /**
     * Check if a king is in check by a pawn.
     *
     * @param white Flag to indicate if we test the white king.
     *
     * @return true, if the king is in check, false otherwise.
     */
    private final boolean isInPawnCheck( boolean white) {

	// The bitmask for the moved pawns.
	long movedPawns;

	// If we check for the white king, get the black pawns(!)
	if( white) {
	    // Get the positions of all black pawns
	    long pawnPositions = getBoard().getPositionOfPieces( Piece.PAWN << 1);

	    // Move all the pawns
	    movedPawns = ( ( ( ( pawnPositions & BitBoard._NOT_LINE_A) >> 9) & 0x007FFFFFFFFFFFFFL)
			   | ( ( ( pawnPositions & BitBoard._NOT_LINE_H) >> 7) & 0x01FFFFFFFFFFFFFFL));
	} else {
	    // Get the positions of all white pawns
	    long pawnPositions = getBoard().getPositionOfPieces( ( Piece.PAWN << 1) | 1);
	    
	    // Move all the pawns
	    movedPawns = ( ( ( pawnPositions & BitBoard._NOT_LINE_H) << 9)
			   | ( ( pawnPositions & BitBoard._NOT_LINE_A) << 7));
	}

	// If the moved pawns are on the king position, the king is in check.
	return ( ( movedPawns & _kingPosition) != 0L);
    }

    /**
     * Analyzed a new board.
     *
     * @param board The new board to analyze.
     * @param white Flag to indicate, if white has the next move.
     */
    public final short analyze( BitBoard board, boolean white) {
	setBoard( board);
	setMoveRight( white);
	return analyze();
    }
}
