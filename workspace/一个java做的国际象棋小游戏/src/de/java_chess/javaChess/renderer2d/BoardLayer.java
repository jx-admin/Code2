/*
  BaordLayer - A pane to display a chess board.

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

package de.java_chess.javaChess.renderer2d;

import java.awt.*;
import javax.swing.JPanel;


/**
 * This class renders a chessboard as a JPanel.
 */
class BoardLayer extends JPanel {

    // Instance variables

    /**
     * The size of a square.
     */
    private int _squareSize;

    /**
     * The size of the entire board.
     */
    private int _boardSize;


    // Constructors

    BoardLayer() {
        _squareSize = ChessBoardRenderer2D.getSquareSize();
        _boardSize = 8 * _squareSize;
	setPreferredSize( new Dimension( _boardSize, _boardSize));
	setBounds(0, 0 , _boardSize, _boardSize);
    }


    // Methods

    public void paintComponent(Graphics g) {
//	g.setColor( new Color( 190, 190, 140) );
//	g.setColor( new Color( 229, 229, 225) );
	g.setColor( new Color( 247, 247, 245) );
	g.fillRect(0, 0, _boardSize, _boardSize);
//	g.setColor( new Color( 122, 80, 44));
//	g.setColor( new Color( 250, 105, 86));
//	g.setColor( new Color( 235, 142, 9));
	g.setColor( new Color( 249, 134, 89));
	for(int i = 0; i < 8; i++)
	    for(int j = 0; j < 8; j++)
		if((i % 2 != 0) && (j % 2 == 0))
		    g.fillRect(i * _squareSize, j * _squareSize, _squareSize, _squareSize);
		else if((i % 2 == 0) && (j % 2 != 0))
		    g.fillRect(i * _squareSize, j * _squareSize, _squareSize, _squareSize);
    }

    /**
     * Get the square size.
     *
     * @return The square size.
     */
    final int getSquareSize() {
	return _squareSize;
    }

    /**
     * Get the board size.
     *
     * @return The board size.
     */
    final int getBoardSize() {
	return _boardSize;
    }

    /**
     * Get the preferred size of the board.
     *
     * @return The preferred size of the board.
     */
    public final Dimension getPreferredSize() {
	return getMinimumSize();
    }

    /**
     * Get the maximum size of the board.
     *
     * @return The maximum size of the board.
     */
    public final Dimension getMaximumSize() {
	return getMinimumSize();
    }

    /**
     * Get the minimum size of the board.
     *
     * @return The minimum size of the board.
     */
    public final Dimension getMinimumSize() {
	return new Dimension( getBoardSize(), getBoardSize());
    }
}
