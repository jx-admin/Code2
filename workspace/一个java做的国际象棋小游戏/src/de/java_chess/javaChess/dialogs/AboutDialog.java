/*
  AboutDialog - A about box for JavaChess with some info on the project.

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

package de.java_chess.javaChess.dialogs;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * This class implements the about box.
 */
public class AboutDialog extends JDialog implements ActionListener {

    // Instance variables

    /**
     * The only instance of this class (implements the singleton pattern).
     */
    private static AboutDialog _instance = null;

    /**
     * A button to hide the dialog.
     */
    private JButton _okButton;
    

    // Constructors

    /**
     * Create a new about dialog.
     *
     * @param parent The parent frame.
     * @param modal Flag, if the dialog is modal.
     */
    private AboutDialog( JFrame parent, boolean modal) {
	super( parent, modal);

	setTitle( "About JavaChess");  // Set the title of the dialog.

	// Set a new layout with bigger gaps.
	getContentPane().setLayout( new BorderLayout( 16, 16));

	JTextArea aboutText = new JTextArea();
	aboutText.setEditable( false);
	aboutText.setText( getAboutText());

	getContentPane().add( new JScrollPane( aboutText));

	// Add a button to hide the dialog.
	_okButton = new JButton("Ok");
	_okButton.addActionListener( this);
	getContentPane().add( _okButton, BorderLayout.SOUTH);

	pack();

	// Get the screen size and place the dialog in the middle of the screen.
	Dimension size = getSize();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation( screenSize.width / 2 - size.width / 2, screenSize.height / 2 - size.height / 2);     
    }

    // Methods

    /**
     * Get the only instance of this class.
     *
     * @return The only instance of this class.
     */
    public final static AboutDialog getInstance() {
	if( _instance == null) {
	    _instance = new AboutDialog( null, true);
	}
	return _instance;
    }

    /**
     * A method to handle action events coming from the dialog.
     *
     * @param event The event that triggered this action.
     */
    public void actionPerformed( ActionEvent event) {
	setVisible( false);
    }

    /**
     * Get the about text. Might be put in a separate file later, so
     * I encapsulated this in a function.
     *
     * @return The about text.
     */
    private final String getAboutText() {
	return "Welcome to the JavaChess project!\n\n"
	    + "You can get the latest version at http://www.java-chess.de\n"
	    + "Please contact us at info@java-chess.de , if you want to contribute,\n"
	    + "have question or suggestions.\n\n"
	    + "We hope you enjoy to use our little toy...\n\n"
	    + "Harald Faber <harald.faber@java-chess.de>\n"
	    + "Andreas Rückert <andreas.rueckert@java-chess.de>";
    }
}
