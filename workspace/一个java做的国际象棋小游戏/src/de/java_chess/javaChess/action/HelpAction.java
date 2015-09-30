/*
  HelpAction - A action to display help on the application.

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

import java.awt.event.*;


/**
 * This class implements the action to start the help system.
 */
public class HelpAction extends JavaChessAction {

    // Instance variables

    
    // Constructors

    /**
     * Create a new action instance.
     */
    public HelpAction() {
	super( "Help");
    }


    // Methods

    /**
     * The action action.
     *
     * @param event The event, that triggered the action.
     */
    public void actionPerformed( ActionEvent event) {

	// Just a dummy so far...
    }
}
