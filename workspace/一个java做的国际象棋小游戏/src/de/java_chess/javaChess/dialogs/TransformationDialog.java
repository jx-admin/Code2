/*
  TransformationDialog - A dialog to determine what type of piece the users 
                         wants to get, when a pawn reached the last row.

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

import de.java_chess.javaChess.piece.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;     

/**
 * This class implements the about box.
 */
public class TransformationDialog extends JDialog implements ActionListener {

    // Instance variables

    /**
     * The only instance of this class (implements the singleton pattern).
     */
    private static TransformationDialog _instance = null;

    /**
     * The radio button for a queen.
     */
    private JRadioButton _queenButton;

    /**
     * The radio button for a rook.
     */
    private JRadioButton _rookButton;

    /**
     * The radio button for a knight.
     */
    private JRadioButton _knightButton;

    /**
     * The radio button for a bishop.
     */
    private JRadioButton _bishopButton;

    /**
     * A button to hide the dialog.
     */
    private JButton _okButton;

    /**
     * The currently selected type.
     */
    private byte _pieceType;


    // Constructors

    /**
     * Create a new transformation dialog.
     *
     * @param parent The parent frame.
     * @param modal Flag, if the dialog is modal.
     */
    private TransformationDialog( JFrame parent, boolean modal) {
        super( parent, modal);

	_pieceType = Piece.QUEEN;  // Set the piece type to the default value.

        setTitle( "Chose a piece type");  // Set the title of the dialog.

        // Set a new layout with bigger gaps.
        getContentPane().setLayout( new BorderLayout( 16, 16));

	// Add a panel with the options.
	JPanel choserPanel = new JPanel();
	choserPanel.setLayout( new GridLayout( 4, 1));

	// Create a group for the piece types to handle the selection
	// of the radio buttons.
	ButtonGroup buttonGroup = new ButtonGroup();

	// Add the buttons for piece types

	_queenButton = new JRadioButton( "Queen");
	_queenButton.setSelected(true);    // Queen is the default choice.
	buttonGroup.add( _queenButton);
	_queenButton.addActionListener( this);
	choserPanel.add( _queenButton);

	_rookButton = new JRadioButton( "Rook");
	buttonGroup.add( _rookButton);
	_rookButton.addActionListener( this);
	choserPanel.add( _rookButton);

	_knightButton = new JRadioButton( "Knight");
	buttonGroup.add( _knightButton);
	_knightButton.addActionListener( this);
	choserPanel.add( _knightButton);

	_bishopButton = new JRadioButton( "Bishop");
	buttonGroup.add( _bishopButton);
	_bishopButton.addActionListener( this);
	choserPanel.add( _bishopButton);

        getContentPane().add( choserPanel, BorderLayout.CENTER);

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
    public final static TransformationDialog getInstance() {
        if( _instance == null) {
            _instance = new TransformationDialog( null, true);
        }
        return _instance;
    }

    /**
     * A method to handle action events coming from the dialog.
     *
     * @param event The event that triggered this action.
     */
    public void actionPerformed( ActionEvent event) {

	// Get the source of the event.
	Object eventSource = event.getSource();

	// If the user closed the dialog.
	if( eventSource == _okButton) {
	    setVisible( false);
	    return;
	}

	// Check for the radio buttons.
	if( eventSource == _queenButton) {
	    _pieceType = Piece.QUEEN;
	} else if( eventSource == _rookButton) {
	    _pieceType = Piece.ROOK;
	} else if( eventSource == _knightButton) {
	    _pieceType = Piece.KNIGHT;
	} else if( eventSource == _bishopButton) {
	    _pieceType = Piece.BISHOP;
	}
    }

    /**
     * Get the currently selected piece type.
     *
     * @return The currently selected piece type.
     */
    public byte getPieceType() {
	return _pieceType;
    }
}  
