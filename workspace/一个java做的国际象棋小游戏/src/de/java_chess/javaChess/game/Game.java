/*
  Game - A interface to define the functionality to store a complete
         game.

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

package de.java_chess.javaChess.game;

import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;


/**
 * This interface defines the functionality to store a complete game,
 * including access to all the stages of a game.
 */
public interface Game {

    // Methods

    /**
     * Reset the game.
     */
    void reset();

    /**
     * Add a new ply to the game.
     *
     * @ply The new ply.
     */
    void doPly( Ply ply);

    /**
     * Take the last ply back.
     */
    void undoLastPly();

    /**
     * Get the number of plies in this game.
     *
     * @return The number of plies in this game.
     */
    int getNumberOfPlies();

    /**
     * Get the last ply.
     *
     * @return The last ply.
     */
    Ply getLastPly();

    /**
     * Check, if a piece on a given positon was moved from it's
     * initial position.
     *
     * @param position The position to check.
     */
    boolean hasBeenMoved( Position position);
}
