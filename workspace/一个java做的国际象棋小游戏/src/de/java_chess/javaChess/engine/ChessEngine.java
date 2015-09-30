/*
  ChessEngine - A interface to implement a engine to play chess.

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

import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.game.*;
import de.java_chess.javaChess.ply.*;
import javax.swing.*;


/**
 * This interface defines the functionality of a engine
 * to play the game of chess.
 */
public interface ChessEngine {

    /**
     * Get the current game.
     *
     * @return The current game.
     */
    Game getGame();

    /**
     * Set the current game.
     *
     * @param The current game.
     */
    void setGame( Game game);

    /**
     * Get the current board.
     *
     * @return The current board.
     */
    Board getBoard();
    
    /**
     * Set the board.
     *
     * @param board The new board.
     */
    void setBoard( Board board);

    /**
     * Get the color of this engine.
     *
     * @param white true, if the engine operates with the white pieces.
     */
    boolean isWhite();

    /**
     * Set the color of the engine.
     *
     * @param white flag to indicate if the engine operates on the white pieces.
     */
    void setWhite( boolean white);

    /**
     * Compute the best ply for the current position.
     *
     * @return The best known ply for the current position.
     */
    Ply computeBestPly();

    /**
     * Check if a ply made by the user is valid.
     *
     * @param ply The user ply.
     *
     * @return true, if the ply is valid. false otherwise.
     */
    boolean validateUserPly( Ply ply);

    /**
     * Request a menu from the chess engine, where the user
     * can change it's settings.
     *
     * @return A menu for the engine settings.
     */
    JMenu getMenu();

    /**
     * Get the current game state for a given color.
     *
     * @param white True, if the state of the white player is requested.
     *
     * @return The current game state.
     */
    byte getCurrentGameState( boolean white);
}
