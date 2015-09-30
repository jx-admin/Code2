/*
  PlayerDialog - The class for editing the players names.

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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * <p>Description: Dialog for editing the players names</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Organisation: The Java-Chess team</p>
 * @author: Faber
 * @version 1.0
 */

public class PlayerDialog extends JDialog implements ActionListener
{

  JTextField jtNewName;
  JLabel jLabel = new JLabel();
  boolean bWhiteOrBlack;
  JButton jbOK = new JButton( "OK" );

/**
 * Standardconstructor
 */
  public PlayerDialog()
  {
    super();
  }

  /**
   * @param iWhiteBlack 1 = edit white players name
   * @param currentString The string which already is displayed above the notation panel
   */
    public PlayerDialog(int iWhiteBlack, String currentString)
    {
      this.setModal(true);
      this.getContentPane().setLayout(new GridBagLayout());
      jtNewName = new JTextField( currentString );
      jtNewName.setSelectionStart( 0 );
      jtNewName.setSelectionEnd( jtNewName.getText().length() );

      if ( iWhiteBlack == 1 )
      {
        this.setTitle("Edit white player's name");
        jLabel.setText("New white player name: ");
        this.getContentPane().add(jLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        bWhiteOrBlack = true;
      }
      else if ( iWhiteBlack == 2 )
      {
        jLabel.setText( "New black player name: ");
        this.setTitle("Edit black player's name");
        this.getContentPane().add(jLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        bWhiteOrBlack = false;
      }

      this.getContentPane().add(jtNewName, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
          GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0));

      jbOK.addActionListener(this);
      this.getContentPane().add(jbOK, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0,
          GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

      this.setSize(300, 75);

    }

/**
 * Interface method; handles the mouse click on the OK-button
 *
 * @param e The action event
 */
  public void actionPerformed(ActionEvent e)
  {
    if ( e.getSource().equals( jbOK ))
    {
      this.hide();
    }
  }

/**
 * Returns the name from within the JTextField
 *
 * @return The new name
 */
  public String getNewName()
  {
    return ( this.jtNewName.getText() );
  }
}