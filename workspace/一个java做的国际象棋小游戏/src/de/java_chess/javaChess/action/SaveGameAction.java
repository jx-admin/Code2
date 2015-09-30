/*
  SaveGameAction - A action to save a game.

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

import de.java_chess.javaChess.notation.*;
import de.java_chess.javaChess.pgn.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


/**
 * This class implements the action to save a game.
 */
public class SaveGameAction extends JavaChessAction {

    // Instance variables

    /**
     * A game notation object.
     */
    GameNotation _gameNotation;


    // Constructors

    /**
     * Create a new action instance.
     *
     * @param gameNotation The game notation.
     */
    public SaveGameAction(  GameNotation gameNotation) {
	super( "Save Game");
	_gameNotation = gameNotation;
    }

    /**
     * Create a new action instance.
     *
     * @param name The name of the action.
     * @param gameNotation The notation of the game.
     */
    SaveGameAction( String name, GameNotation gameNotation) {
	super( "Save Game as...");
	_gameNotation = gameNotation;
    }


    // Methods

    /**
     * The actual action.
     *
     * @param event The event, that caused this action.
     */
    public void actionPerformed( ActionEvent event) {
    }

    /**
     * Try to save the game into the given file.
     *
     * @param file The file to write into.
     */
    public void trySave( File file) {
	try {
	    PGNOutputStream PGNOutput = new PGNOutputStream( file);

	    // Write the notation of the game.
	    PGNOutput.write( _gameNotation);
	} catch( IOException exception) {
	    JOptionPane.showMessageDialog( null, "Error while writing file: " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE); 
	}
    }
}
