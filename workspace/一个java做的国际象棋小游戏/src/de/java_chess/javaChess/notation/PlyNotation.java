/*
  PlyNotation - This interace defines the functionality to notate a ply.

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
 * This interface defines the functionality to notate a ply.
 */
public interface PlyNotation {

    
    // Methods

    /**
     * Render this ply as a string.
     *
     * @return The notated ply as a string.
     */
    String toString();

    /**
     * Get the piece of this ply.
     *
     * @return The piece, that is moved with this ply.
     */
    Piece getPiece();

    /**
     * Set the piece, that is moved with this ply.
     *
     * @param ply The moved ply.
     */
    void setPiece( Piece piece);

    /**
     * Get the ply of this notation.
     *
     * @return The ply for this notation.
     */
    Ply getPly();

    /**
     * Set the ply for this notation.
     *
     * @param ply The ply for this notation.
     */
    void setPly( Ply ply);

    /**
     * Check if the piece captures another piece with this ply.
     *
     * @return true, if another piece is captureed with this ply.
     */
    boolean isCapture();

    /**
     * Set the flag to indicate if another piece is captured with this ply.
     *
     * @param capture Flag to indicate, if this ply captures another piece.
     */
    void setCapture( boolean capture);

    /**
     * Get the flag for a check.
     *
     * @return true, if the opponent is in check.
     */
    boolean isCheck();

    /**
     * Set the flag for a check.
     *
     * @param check, true if the oppenent is in check.
     */
    void setCheck( boolean check);

    /**
     * Get the flag for a checkmate.
     *
     * @return true, if the opponent is checkmate.
     */
    boolean isCheckMate();

    /**
     * Set the flag for a checkmate.
     *
     * @param check, true if the oppenent is checkmate.
     */
    void setCheckMate( boolean checkMate);

    /**
     * Get the notation for a piece type.
     *
     * @param pieceType The piece type.
     *
     * @return The notation for a piece type.
     */
    String getPieceTypeNotation( byte pieceType);
}
