/*
  JavaChess - The main class for the Java chess game.

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

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import javax.swing.border.BevelBorder;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;

//////////////////////////////////////////////////////////////////////////////

/**
 * Class for the display of engine output
 */
public class EnginePanel extends JPanel {

    /**
     * The layout
     */
    private GridBagLayout gridBagEngine = new GridBagLayout();

/**
 * The JScrollPane contains the JTextArea
 */
	private JScrollPane jScrollPane;

    /**
     * The notation will be displayed in a JTextArea
     */
    private JTextArea jtEngine = new JTextArea();

 /**
  * The Headline "Engine output" does not need to be contained in the TextArea
 */
	private JLabel jlHeadline;

    /**
     * Standardconstructor
     */
    public EnginePanel() {
	super();

	try {
	    jbInit();
	} catch(Exception e) {
	    e.printStackTrace();
	}
  }

    /**
     * Construction a la JBuilder to be able to use the JBuilder designer
     *
     * @throws Exception
     */
    private void jbInit() throws Exception {
	this.setLayout(gridBagEngine);
	this.jtEngine.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.white,
								Color.white, new Color(148, 145, 140),
								new Color(103, 101, 98)));
	this.jtEngine.setEditable( false );
	this.jtEngine.setLineWrap( true );
	this.jtEngine.setWrapStyleWord( true );
		this.jScrollPane = new JScrollPane( this.jtEngine );

		this.jlHeadline = new JLabel( "Engine and debug output:" );

    this.add(this.jScrollPane,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    this.add(this.jlHeadline,  new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
//						   ,GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    }

    /**
     * Adds the given text to the existing
     *
     * @param newText The text to add
     */
	public void modifyText(String newText)
	{
		String sCurrentText = this.jtEngine.getText();
		if ( sCurrentText.equals( "" ) == false )
		{
		  this.jtEngine.setText( sCurrentText + "\n" + newText );
      Point point = new Point( 0, (int)(this.jtEngine.getSize().getHeight()) );
      this.jScrollPane.getViewport().setViewPosition( point );
		}
		else{
			this.jtEngine.setText( newText );
		}
//		this.jtEngine.setText( sCurrentText + "\n" + newText );
    }

    /**
     * Get the preferred size of the board.
     *
     * @return The preferred size of the board.
     */
//    public final Dimension getPreferredSize() {
//	return getMinimumSize();
//    }

    /**
     * Get the maximum size of the board.
     *
     * @return The maximum size of the board.
     */
//    public final Dimension getMaximumSize() {
//	return getMinimumSize();
//    }

    /**
     * Get the minimum size of the board.
     *
     * @return The minimum size of the board.
     */
//    public final Dimension getMinimumSize() {
//	return new Dimension( 100, 100);
//    }
}
