/*
  PlyHashtable - A interface to define the functionality to control
                 a hashtable with chess plies.

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
 * This interface defines the functionality to control a
 * hashtable with chess plies.
 */
public interface PlyHashtable {

    // Methods

    /**
     * Get the maximum number of entries in the hashtable.
     *
     * @return The maximum number of entries in the hashtable.
     */
    int getMaximumSize();

    /**
     * Set the maximum number of entries in the hashtable.
     *
     * @param maximumEntries The new maximum number of entries.
     */
    void setMaximumSize( int maximumEntries);

    /**
     * Get the current number of entries.
     *
     * @return The current number of entries.
     */
    int getSize();

    /**
     * Try to push a new entry into the hashtable.
     *
     * @param entry The new entry, that the hashtable might store.
     */
    void pushEntry( PlyHashtableEntry ply);

    /**
     * Get the stored ply for a given board and piece color.
     *
     * @param board The board before the move.
     * @param white Flag to indicate, if we want a move with the white pieces.
     */
    Ply getPly( Board board, boolean white);

    /**
     * Try to remove the oldest entry.
     */
    void removeOldestEntry();
}
