/*
  NavigationPanel - The class for the game navigation.

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.*;
//////////////////////////////////////////////////////////////////////////////

/**
 * Class for the display of engine output
 */
public class NavigationPanel extends JPanel implements ActionListener
{

		/**
		 * The button for one ply (=half move) back
		 */
		JButton jbBack = new JButton();

		/**
		 * The button for jump to the beginning of the game
		 */
		JButton jbStart = new JButton();

		/**
		 * The button for one ply (=half move) back
		 */
		JButton jbForward = new JButton();

		/**
		 * The button for jump to the end of the game
		 */
		JButton jbEnd = new JButton();

    /**
     * The layout
     */
    private GridBagLayout gridBagNavigation = new GridBagLayout();

    /**
     * Standardconstructor
     */
    public NavigationPanel()
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
     * Construction a la JBuilder to be able to use the JBuilder designer
     *
     * @throws Exception
     */
    private void jbInit() throws Exception
		{
			this.setLayout(gridBagNavigation);
			jbBack.setBackground(Color.white);
    jbBack.setToolTipText("one ply back");
			jbStart.setBackground(Color.white);
    jbStart.setToolTipText("to start position");
			jbForward.setBackground(Color.white);
    jbForward.setToolTipText("one ply forward");
			jbEnd.setBackground(Color.white);
    jbEnd.setToolTipText("to end position");

				URL urlBack = getClass().getResource("images/back03.gif");
				Image imBack = Toolkit.getDefaultToolkit().getImage( urlBack );
        jbBack.setIcon( new ImageIcon( imBack ));

				URL urlStart = getClass().getResource("images/start03.gif");
				Image imStart = Toolkit.getDefaultToolkit().getImage( urlStart );
        jbStart.setIcon( new ImageIcon( imStart ));

				URL urlForward = getClass().getResource("images/forward03.gif");
				Image imForward = Toolkit.getDefaultToolkit().getImage( urlForward );
        jbForward.setIcon( new ImageIcon( imForward ));

				URL urlEnd = getClass().getResource("images/end03.gif");
				Image imEnd = Toolkit.getDefaultToolkit().getImage( urlEnd );
        jbEnd.setIcon( new ImageIcon( imEnd ));

//			jbBack.setIcon( new ImageIcon( Toolkit.getDefaultToolkit().getImage( new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/back02.gif"))));
//			jbStart.setIcon( new ImageIcon( Toolkit.getDefaultToolkit().getImage( new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/start02.gif"))));
//			jbForward.setIcon( new ImageIcon( Toolkit.getDefaultToolkit().getImage( new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/forward02.gif"))));
//			jbEnd.setIcon( new ImageIcon( Toolkit.getDefaultToolkit().getImage( new URL( "jar:file:javaChess.jar!/de/java_chess/javaChess/renderer2d/images/end02.gif"))));

			jbBack.addActionListener( this );
			jbStart.addActionListener( this );
			jbForward.addActionListener( this );
			jbEnd.addActionListener( this );

			this.add(jbStart,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(jbBack,        new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(jbForward,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
			this.add(jbEnd,     new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0
							,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

    }

/**
 * Moves one half move back in the game history
 */
		private void moveOnePlyBack()
		{
			/** @todo still to implement */
//			System.out.println( "Einen Halbzug zurück" );
		}

/**
 * Moves to the beginning of the game
 */
		private void moveToStart()
		{
			/** @todo still to implement */
//			System.out.println( "Zum Partieanfang" );
		}

/**
 * Moves one half move forward in the game
 */
		private void moveOnePlyForward()
		{
			/** @todo still to implement */
			System.out.println( "Einen Halbzug vor" );
		}

/**
 * Moves to the end position of the game
 */
		private void moveToEnd()
		{
			/** @todo still to implement */
			System.out.println( "Zum Partieende" );
		}

/**
 * React on mouseclick
 */
		public void actionPerformed( ActionEvent ae )
		{
			if ( ae.getSource().equals( jbStart ))
				 moveToStart();
			if ( ae.getSource().equals( jbBack ))
				 moveOnePlyBack();
			if ( ae.getSource().equals( jbForward ))
				 moveOnePlyForward();
			if ( ae.getSource().equals( jbEnd ))
				 moveToEnd();
		}
}
