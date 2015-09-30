/*
  PlyHashtableEntry - A interface to define the functionality of
                      a entry in the ply hashtable.

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

import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.ply.*;


/**
 * This interface defines the functionality of a entry in
 * the ply hashtable.
 */
public interface PlyHashtableEntry {

    // Methods

    /**
     * Get the color, that is supposed to apply this ply.
     *
     * @return true, if white applies this ply.
     */
    boolean isWhiteMove();

    /**
     * Get the game position before the ply is applied.
     *
     * @return The board before the ply is applied.
     */
    Board getBoard();

    /**
     * Get the ply.
     *
     * @return The ply.
     */
    Ply getPly();

    /**
     * Request a key for this entry.
     *
     * @return The key for this entry.
     */
    long hashKey();
}
