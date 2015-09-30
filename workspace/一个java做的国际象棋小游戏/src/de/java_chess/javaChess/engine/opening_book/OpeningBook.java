/*
  OpeningBook - Interface to define the functionality of a opening book.

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

package de.java_chess.javaChess.engine.opening_book;

import de.java_chess.javaChess.ply.*;
import java.io.File;


/**
 * This interface defines the functionality of a opening book.
 */
public interface OpeningBook {

    // Methods

    /**
     * Advance the game by one user ply.
     *
     * @param ply The ply from the user.
     */
    void doUserPly( Ply ply);

    /**
     * Get the next ply from the opening book, if there is
     * one available, or null if not.
     *
     * @return The next ply from the opening book, or null 
     *         if there's no ply available.
     */
    Ply getOpeningBookPly();

    /**
     * Reset the opening book to the initial piece position.
     */
    void reset();

    /**
     * Add a opening in PGN format to the opening book and return
     * the error status.
     *
     * @param pgnFile The File to add to the opening book.
     *
     * @throws IOException if there was a problem with reading the file.
     * @throws RecognitionException if there was a problem with the PGN parsing.
     */
    void addPGNopening( File pgnFile);
}


