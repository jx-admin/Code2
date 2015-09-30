/*
  PGNFileFilter - A file filter for PGN files.

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

package de.java_chess.javaChess.pgn;

import javax.swing.filechooser.FileFilter;
import java.io.File;


/**
 * A file filter for PGN files.
 */
public class PGNFileFilter extends FileFilter {

    // Instance variables


    // Constructors


    // Methods

    /**
     * Check if the given file is a PGN file.
     *
     * @param file The file to check.
     *
     * @return true, if the file is a PGN file, false otherwise.
     */
    public boolean accept( File file) {
	if (file == null) {
	    return false;
	}
	String filename = file.getName();
	int i = filename.lastIndexOf('.');
	if (i > 0 &&  i < filename.length() - 1) {
	    return "pgn".equalsIgnoreCase( filename.substring( i + 1).toLowerCase());
	}
	return false;      
    }

    /**
     * Get a description of this file filter.
     *
     * @return A description of this file filter.
     */
    public String getDescription() {
	return "PGN - Portable Game Notation format";
    }
}

