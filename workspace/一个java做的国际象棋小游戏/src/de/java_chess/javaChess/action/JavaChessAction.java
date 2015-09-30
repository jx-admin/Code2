/*
  JavaChessAction - The base class for actions within this project.

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

import javax.swing.*;


/**
 * This class is the foundation for most actions within this application.
 */
public abstract class JavaChessAction extends AbstractAction {

    // Instance variables


    // Constructors

    /**
     * Create a new Java-Chess action.
     *
     * @param name The name of the action.
     */
    public JavaChessAction( String name) {
	super( name);
	putValue( SHORT_DESCRIPTION, name);
    }

    // Methods

    /**
     * Get the short description (the name) of this action.
     *
     * @return The short description of this action.
     */
    public String getShortDescription() {
	return (String)getValue( SHORT_DESCRIPTION);
    }

    /**
     * Get the name of this action (which is currently the short description).
     *
     * @return The name of this action.
     */
    public final String getName() {
	return getShortDescription();
    }
}
