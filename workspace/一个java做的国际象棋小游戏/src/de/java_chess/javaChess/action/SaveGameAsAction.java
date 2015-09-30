/*
  SaveGameAsAction - A action to save a game.

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

package de.java_chess.javaChess.action;

import de.java_chess.javaChess.pgn.*;
import de.java_chess.javaChess.notation.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


/**
 * This class implements the action to save a game.
 */
public class SaveGameAsAction extends SaveGameAction {

    // Instance variables

    /**
     * A file filter for PGN files.
     */
    private static PGNFileFilter _pgnFileFilter;


    // Constructors

    /**
     * Create a new action instance.
     *
     * @param gameNotation The notation of the game.
     */
    public SaveGameAsAction( GameNotation gameNotation) {
	super( "Save Game as...", gameNotation);
	_gameNotation = gameNotation;
    }

    
    // Methods

    /**
     * The actual action.
     *
     * event The event, that caused this action.
     */
    public void actionPerformed( ActionEvent event) {

	// Create a new filechooser
	JFileChooser chooser = new JFileChooser();
	
	chooser.setDialogTitle( "Saving game as pgn file");	
        chooser.setFileFilter( getPGNFileFilter());

	int retval = chooser.showSaveDialog( null);
        if (retval == JFileChooser.APPROVE_OPTION) {
	    trySave( chooser.getSelectedFile());
        } 
    }

    /**
     * Get a PGN file filter.
     * 
     * @return A PGN file filter.
     */
    private PGNFileFilter getPGNFileFilter() {

	// To avoid the instanciation of too many object at app startup,
	// less important object are created as needed.
	if( null == _pgnFileFilter) {
	    _pgnFileFilter = new PGNFileFilter();
	}

	return _pgnFileFilter;
    }
}
