/*
  Board - A interface to implement a chess board data structure.

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

package de.java_chess.javaChess.board;

import de.java_chess.javaChess.*;
import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;


/**
 * This interface has to be implemented by any board 
 * implementation.
 */
public interface Board extends Cloneable {
    
    /**
     * Get the piece of a given position.
     *
     * @param position The position of the piece.
     *
     * @return The piece on that position, or null if the 
     * square is empty.
     */
    Piece getPiece( Position position);

    /**
     * Remove all the pieces from the board.
     */
    void emptyBoard();

    /**
     * Set the pieces to their initial positions.
     */
    void initialPosition();

    /**
     * Set a piece on a given position.
     *
     * @param piece The piece to set.
     * @param position The position to set the piece on.
     */
    void setPiece( Piece piece, Position position);

    /**
     * Move a piece from one square to another.
     *
     * @param ply The move to perform.
     */
    void doPly( Ply ply);

    /**
     * Return a new board, that results from a given ply.
     *
     * @param ply The ply to perform.
     *
     * @return A new board with the game position after the ply.
     */
    Board getBoardAfterPly( Ply ply);
}
