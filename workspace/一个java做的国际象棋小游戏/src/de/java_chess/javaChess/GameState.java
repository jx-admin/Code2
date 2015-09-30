/*
  GameState - A interface to provide various game states.

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

package  de.java_chess.javaChess;


/**
 * This interface provides the states on a chess game.
 */
public interface GameState {

    // Static variables

    /**
     * A stopped or not yet started game.
     */
    final byte STOPPED = 0;

    /**
     * A ongoing game.
     */
    final byte ONGOING = 1;

    /**
     * A draw.
     */
    final byte DRAW = 2;

    /**
     * A check.
     */
    final byte CHECK = 3;

    /**
     * A checkmate.
     */
    final byte CHECKMATE = 4;


    // Methods
}
