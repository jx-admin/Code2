/*
  PlyNotationImpl - A class to notate a ply.

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

package de.java_chess.javaChess.notation;

import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.ply.*;


/**
 * This class holds all the methods to render a ply
 * as a string notation.
 */
public class PlyNotationImpl implements PlyNotation {

    // Instance variables.

    /**
     * The ply.
     */
    private Ply _ply;

    /**
     * The moved piece.
     */
    private Piece _piece;

    /**
     * Flag to indicate, if another piece is captureed.
     */
    private boolean _capture;

    /**
     * Flag to indicate, if this ply sets the opponent in check.
     */
    private boolean _check;

    /**
     * Flag to indicate, if the opponent is checkmate after this ply.
     */
    private boolean _checkMate;

    /**
     * A cache for the string representation.
     */
    private String _stringRepresentation;


    // Constructors

    /**
     * Create a new ply notation instance.
     *
     * @param ply The ply to render.
     * @param capture Flag to indicate if it's a capture.
     * @param piece The moved piece.
     */
    public PlyNotationImpl( Ply ply, boolean capture, Piece piece) {
	setPly( ply);
	setCapture( capture);
	setPiece( piece);

	// Render the string representation only once and not during
	// the toString() call.
	computeStringRepresentation();
    }

    // Methods

    /**
     * Render this notation as a string.
     *
     * @return A string representation of this ply.
     */
    public String toString() {
	return _stringRepresentation;
    }

    /**
     * Render this ply as a string.
     */
    private final void computeStringRepresentation() {
	StringBuffer notation = new StringBuffer();

	if( ! ( getPly() instanceof CastlingPly)) {

	    // Start with the type of the piece, if it is not a pawn.
	    if( getPiece().getType() != Piece.PAWN) {
		notation.append( getPieceTypeNotation( getPiece().getType()));
	    }

	    // Get this ply as a string.
	    String plyString = getPly().toString();

	    // If it's a capture, indicate it with an 'x'
	    notation.append( isCapture() ? plyString.replace( '-', 'x') : plyString);

	    // If it's a pawn transforming to a new piece type, append the new type.
	    if( getPly() instanceof TransformationPly) {
		notation.append( getPieceTypeNotation( ( (TransformationPly)getPly()).getTypeAfterTransformation()));
	    }
	} else {
	    notation.append( getPly().toString());
	}

	// Add the check(-mate) signs.
	if( isCheckMate()) {
	    notation.append( '#');
	} else {
	    if( isCheck()) {
		notation.append( '+');
	    }
	}	

	// Convert the buffer to a string and store it.
	_stringRepresentation =  notation.toString();
    }

    /**
     * Get the piece of this ply.
     *
     * @return The piece, that is moved with this ply.
     */
    public final Piece getPiece() {
	return _piece;
    }

    /**
     * Set the piece, that is moved with this ply.
     *
     * @param ply The moved ply.
     */
    public final void setPiece( Piece piece) {
	_piece = piece;
    }

    /**
     * Get the ply of this notation.
     *
     * @return The ply for this notation.
     */
    public final Ply getPly() {
	return _ply;
    }

    /**
     * Set the ply for this notation.
     *
     * @param ply The ply for this notation.
     */
    public final void setPly( Ply ply) {
	_ply = ply;
    }

    /**
     * Check if the piece captures another piece with this ply.
     *
     * @return true, if another piece is captureed with this ply.
     */
    public final boolean isCapture() {
	return _capture;
    }

    /**
     * Set the flag to indicate if another piece is captureed with this ply.
     *
     * @param capture Flag to indicate, if this ply captures another piece.
     */
    public final void setCapture( boolean capture) {
	_capture = capture;
    }

        /**
     * Get the flag for a check.
     *
     * @return true, if the opponent is in check.
     */
    public final boolean isCheck() {
	return _check;
    }

    /**
     * Set the flag for a check.
     *
     * @param check, true if the oppenent is in check.
     */
    public final void setCheck( boolean check) {
	_check = check;

	// If we are not in check, we are also no checkmate.
	if( check == false) {
	    setCheckMate( false);
	}

	// Recompute the string representation to make sure, that
	// it is still correct.
	computeStringRepresentation();
    }

    /**
     * Get the flag for a checkmate.
     *
     * @return true, if the opponent is checkmate.
     */
    public final boolean isCheckMate() {
	return _checkMate;
    }

    /**
     * Set the flag for a checkmate.
     *
     * @param check, true if the oppenent is checkmate.
     */
    public final void setCheckMate( boolean checkMate) {
	_checkMate = checkMate;

	// If the opponent is checkmate, he is also in check.
	if( checkMate == true) {
	    setCheck( true); 
	}
    }

    /**
     * Get the notation for a piece type.
     *
     * @return The notation for a piece type.
     */
    public final String getPieceTypeNotation( byte pieceType) {

	String notation = "";  // The result;

	// Convert the piece type to a string.
	switch( pieceType) {
	    case Piece.PAWN:   notation = "P"; break;
	    case Piece.KNIGHT: notation = "N"; break; 
	    case Piece.BISHOP: notation = "B"; break;
	    case Piece.ROOK:   notation = "R"; break;
   	    case Piece.QUEEN:  notation = "Q"; break;
	    case Piece.KING:   notation = "K"; break;
	}
	return notation;
    }
}   
