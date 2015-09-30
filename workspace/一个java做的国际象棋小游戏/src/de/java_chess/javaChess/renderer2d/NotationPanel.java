/*
  NotationPanel - A panel to display the game notation.

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
//////////////////////////////////////////////////////////////////////////////
package de.java_chess.javaChess.renderer2d;

import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import de.java_chess.javaChess.dialogs.PlayerDialog;
//////////////////////////////////////////////////////////////////////////////

/**
 * Class for the display of the game notation
 */
public class NotationPanel extends JPanel implements ActionListener
{

/**
 * The layout
 */
  private GridBagLayout gridBagText = new GridBagLayout();

 /**
  * The JScrollPane contains the JTextArea
 */
	private JScrollPane jScrollPane;

/**
 * The notation will be displayed in a JTextArea
 */
  private JTextArea jtNotation = new JTextArea();

/**
 * The Headline "Notation and game details" does not need to be contained in the TextArea
 */
	private JLabel jlHeadline;

/**
 * The panel for the players names
 */
  private JPanel jpPlayers = new JPanel();

/**
 * Label for the white player
 */
  private JLabel jlWhite = new JLabel();

/**
 * The name for the white player
 */
  private JButton jbWhite = new JButton();

/**
 * Label for the black player
 */
  private JLabel jlBlack = new JLabel();

/**
 * The name for the black player
 */
  private JButton jbBlack = new JButton();

/**
 * The layout for this panel
 */
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

/**
 * Standardconstructor
 */
  public NotationPanel()
  {
		super();

		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
  }

/**
 * Construction a la JBuilder to be able to use JBuilder designer
 *
 * @throws Exception
 */
	private void jbInit() throws Exception
	{
	  this.setLayout(gridBagText);
		this.jtNotation.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white,
									  Color.white, new Color(148, 145, 140),
									  new Color(103, 101, 98)));
		this.jtNotation.setEditable( false );
		this.jtNotation.setLineWrap( true );
		this.jtNotation.setWrapStyleWord( true );

		this.jScrollPane = new JScrollPane( this.jtNotation );

		this.jlHeadline = new JLabel( "Notation and game details:" );

    jlHeadline.setFont(new java.awt.Font("Dialog", 1, 12));
    jpPlayers.setLayout(gridBagLayout1);

    jlWhite.setText("White:");
    jbWhite.setText("white player name");
    jbWhite.setBackground( Color.white );
    jbWhite.setForeground( Color.black );
    jbWhite.setFocusPainted( false);
    jbWhite.addActionListener( this );

    jlBlack.setText("Black:");
    jbBlack.setText("black player name");
    jbBlack.setBackground( Color.black );
    jbBlack.setForeground( Color.white );
    jbBlack.setFocusPainted( false);
    jbBlack.addActionListener( this );

    this.add(jScrollPane,      new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jlHeadline,      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    this.add(jpPlayers,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));

    jpPlayers.add(jlWhite,  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 4));
    jpPlayers.add(jbWhite,    new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
    jpPlayers.add(jlBlack,   new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
    jpPlayers.add(jbBlack,   new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

	}

    /**
     * Adds the given text to the existing
     *
     * @param newText The text to add
     */
	public void modifyText(String newText)
	{
		String sCurrentText = this.jtNotation.getText();
		if ( sCurrentText.equals( "" ) == false )
		{
		  this.jtNotation.setText( sCurrentText + "\n" + newText );
      Point point = new Point( 0, (int)(this.jtNotation.getSize().getHeight()) );
      this.jScrollPane.getViewport().setViewPosition( point );
		}
		else{
			this.jtNotation.setText( newText );
		}
	}

    /**
     * Set a new notation.
     *
     * @param newNotation The new notation.
     */
     public void setText(String newNotation) {
	 this.jtNotation.setText( newNotation);
     }

    /**
     * Returns the white player's name
     *
     * @return The String for the white player
     */
		 public String getWhitePlayerName()
		 {
			return ( this.jbWhite.getText() );
		 }

    /**
     * Sets the white player's name
     *
     * @param newName The new string for the white player
     */
		 public void setWhitePlayerName(String newName)
		 {
			this.jbWhite.setText( newName );
		 }

    /**
     * Returns the black player's name
     *
     * @return The String for the black player
     */
		 public String getBlackPlayerName()
		 {
			return ( this.jbBlack.getText() );
		 }

    /**
     * Sets the black player's name
     *
     * @param newName The new string for the black player
     */
		 public void setBlackPlayerName(String newName)
		 {
			this.jbBlack.setText( newName );
		 }

    public void actionPerformed(ActionEvent e)
    {
      boolean bWhiteOrBlack = true;
      PlayerDialog playerDialog = null;

      if (e.getSource().equals(jbWhite))
      {
        playerDialog = new PlayerDialog(1, getWhitePlayerName());
        bWhiteOrBlack = true;
      }
      else if (e.getSource().equals(jbBlack))
      {
          playerDialog = new PlayerDialog(2, getBlackPlayerName());
          bWhiteOrBlack = false;
      }

      if ( playerDialog != null )
      {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialogSize = playerDialog.getSize();

        playerDialog.setLocation( (screenSize.width - dialogSize.width) / 2,
                                 (screenSize.height - dialogSize.height) / 2);

        playerDialog.show();
        if (bWhiteOrBlack == true) {
          jbWhite.setText(playerDialog.getNewName());
        }
        else {
          jbBlack.setText(playerDialog.getNewName());
        }
      }
    }
}
