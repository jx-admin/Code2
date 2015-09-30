/*
  AboutAction - A action to display a about box.

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

import de.java_chess.javaChess.dialogs.*;
import java.awt.event.*;
 

/**
 * This class implements the action to display a about box.
 */
public class AboutAction extends JavaChessAction {

    // Instance variables

    
    // Constructors

    /**
     * Create a new action instance.
     */
    public AboutAction() {
	super( "About");
    }


    // Methods

    /**
     * The actual action.
     *
     * @param event The event, that triggered the action.
     */
    public void actionPerformed( ActionEvent event) {

	// Create a display a about box.
	AboutDialog.getInstance().show();
    }
}


