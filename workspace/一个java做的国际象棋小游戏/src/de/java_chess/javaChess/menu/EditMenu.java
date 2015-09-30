/*
  EditMenu - A class to create the Edit menu.

  Copyright (C) 2002,2003 Harald Faber <info@java-chess.de>

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

package de.java_chess.javaChess.menu;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import de.java_chess.javaChess.renderer2d.NotationPanel;


/**
 * This class constructs the Edit menu.
 */
public class EditMenu implements ActionListener
{

/**
 * Holds the NotationPanel, e.g. for editing the players names.
 */
	private NotationPanel notationPanel;

/**
 * The menu item to edit the white player name
 */
	JMenuItem jmiWhitePlayerName = new JMenuItem();

/**
 * The menu item to edit the black player name
 */
	JMenuItem jmiBlackPlayerName = new JMenuItem();

/**
 * Standardconstructor
 */
  public EditMenu()
  {
		this.notationPanel = null;
  }

/**
 * Interface method. Currently displays input dialogs to edit white and black player
 * names
 */
  public void actionPerformed(ActionEvent e)
  {
		if ( notationPanel != null )
		{
			boolean bWhiteOrBlack = true;
			JDialog inputDialog = new JDialog();
			inputDialog.setModal( true );
			inputDialog.getContentPane().setLayout( new GridLayout( 1, 2 ));
			JTextField jtNewName = new JTextField();

			if( e.getSource().equals( jmiWhitePlayerName ))
			{
				inputDialog.setTitle( "Edit white player's name" );
				inputDialog.getContentPane().add( new JLabel( "New white player name: " ));
				jtNewName.setText( notationPanel.getWhitePlayerName() );
				bWhiteOrBlack = true;
			}
			else if( e.getSource().equals( jmiBlackPlayerName ))
			{
				inputDialog.setTitle( "Edit black player's name" );
				inputDialog.getContentPane().add( new JLabel( "New black player name: " ));
				jtNewName.setText( notationPanel.getBlackPlayerName() );
				bWhiteOrBlack = false;
			}

			inputDialog.getContentPane().add( jtNewName );
			inputDialog.setSize( 300, 75 );
			Dimension   screenSize  = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension   dialogSize  = inputDialog.getSize();

			inputDialog.setLocation((screenSize.width - dialogSize.width) / 2,
																		(screenSize.height - dialogSize.height) / 2);

			inputDialog.show();
			if ( bWhiteOrBlack == true )
			{
				notationPanel.setWhitePlayerName( jtNewName.getText() );
			}
			else{
				notationPanel.setBlackPlayerName( jtNewName.getText() );
			}
		}
  }

/**
 * Return a menu from the chess engine, where the user
 * can change the settings.
 *
 * @return A menu for the engine settings.
 */
	public final JMenu getMenu()
	{
		// Create a new menu.
		JMenu jmEditMenu = new JMenu( "Edit");

		// Add menu items
		jmiBlackPlayerName.setText( "Black player name" );
		jmiWhitePlayerName.setText( "White player name" );

		jmiBlackPlayerName.addActionListener( this );
		jmiWhitePlayerName.addActionListener( this );

		jmEditMenu.add( jmiWhitePlayerName );
		jmEditMenu.add( jmiBlackPlayerName );

		return ( jmEditMenu );
	}

/**
 * Sets the NotationPanel to be able to perform some actions there
 * @param panel The NotationPanel object
 */
	public final void setNotationPanel(NotationPanel panel)
	{
		this.notationPanel = panel;
	}

}