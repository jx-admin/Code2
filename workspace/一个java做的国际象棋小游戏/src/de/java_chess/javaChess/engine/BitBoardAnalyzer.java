/*
  BitBoardAnalyzer - A interface for classes, that analyze the position
                     on BitBoards.

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


/**
 * This interface has to be implemented by any class, that
 * that analyses a BitBoard type chess game position.
 */
public interface BitBoardAnalyzer {

    // Static variables.

    /**
     * A board, where the white king is in chess.
     */
    short BLACK_WIN = -500;

    /**
     * A board, where the black king is in chess.
     */
    short WHITE_WIN = -BLACK_WIN;

    
    // Methods

    /**
     * Get the currently analyzed board.
     *
     * @return The currently analyzed board.
     */
    BitBoard getBoard();

    /**
     * Set a new board to be analyzed.
     *
     * @param board The new board.
     */
    void setBoard( BitBoard board);

    /**
     * Set the flag, if white is about to move.
     *
     * @param white Flag to indicate, if white has the
     *              next move.
     */
    void setMoveRight( boolean white);

    /**
     * Test if a king is in check.
     *
     * @param white true, if the white king is checked, false otherwise.
     */
    boolean isInCheck( boolean white);

    /**
     * Test if a king is in check on a given board.
     *
     * @param board The board to test.
     * @param white true, if the white king is checked, false otherwise.
     */
    boolean isInCheck( BitBoard board, boolean white);
    
    /**
     * Analyze the current board.
     */
    short analyze();

    /**
     * Analyzed a new board.
     *
     * @param board The new board to analyze.
     * @param white Flag to indicate, if white has the next move.
     */
    short analyze( BitBoard board, boolean white);
}
