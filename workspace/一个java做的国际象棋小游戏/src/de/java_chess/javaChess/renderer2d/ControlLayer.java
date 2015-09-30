/*
  ControlLayer - A component to render user interactions with the board.

  Copyright (C) 2002,2003 Andreas Rueckert <mail@andreas-rueckert.de>

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

import de.java_chess.javaChess.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


/**
 * This class handles the control layer, where the user enters his moves.
 */
class ControlLayer extends JPanel {

    // Instance variables.

    /**
     * The game controller.
     */
    GameController _controller;

    /**
     * The pieces layer, that holds the chess pieces.
     */
    PiecesLayer _piecesLayer;

    /**
     * The size of one square as a instance variable for caching purposes.
     */
    int _squareSize;

    /**
     * The size of the entire board as a instance var for caching purposes.
     */
    int _boardSize;

    /**
     * The source square for the next ply.
     */
    int _sourceSquare = -1;


    // Constructors

    ControlLayer( GameController controller, PiecesLayer pl) {
	setController( controller);
        _squareSize = ChessBoardRenderer2D.getSquareSize();
        _boardSize = 8 * _squareSize;
	setLayout(null);
	setOpaque(false);
	setPreferredSize( new Dimension( _boardSize, _boardSize));
	setBounds( 0, 0, _boardSize, _boardSize);
	_piecesLayer = pl;
	addMouseListener(new MouseAdapter() {

		public void mouseClicked( MouseEvent e) {
		    if( _sourceSquare == -1) {
			_sourceSquare = 8 * ( 7 - e.getY() / _squareSize) + ( e.getX() / _squareSize);
			repaint();
		    } else {
			final int destSquare = 8 * ( 7 - e.getY() / _squareSize) + ( e.getX() / _squareSize);

			new Thread( new Runnable() {
				public void run(){
				    getController().userPly( new PlyImpl( new PositionImpl( _sourceSquare), new PositionImpl( destSquare)));
				    _sourceSquare = -1;
				    repaint();
				}
			    }).start();
			//  getController().userPly( new PlyImpl( new PositionImpl( _sourceSquare), new PositionImpl( destSquare)));
			//  _sourceSquare = -1;
			//  repaint();
		    }
		}
	    });
    }


    // Methods

    /**
     * Overridden  paintComponent method to draw the marker for
     * the source square.
     *
     * @param g The graphics context.
     */
    public void paintComponent( Graphics g) {
	if( _sourceSquare != -1) {          // If a source square is selected,
	    markSquare( _sourceSquare, g);  // mark it.
	}
    }

    /**
     * Draw a marker at the given square.
     *
     * @param square The square to mark.
     * @param g The graphics context.
     */
    private final void markSquare( int square, Graphics g) {
	int xpos = ( square & 7) * _squareSize;         // Compute the location of the
	int ypos = ( 7 - (square >> 3)) * _squareSize;  // square
	int rectSize = _squareSize - 1;                 // and it's size.
	g.setColor( Color.blue);                        // Simply draw a blue frame there.
	for( int i = 0; i < 3; i++) {
	    g.drawRect( xpos++, ypos++, rectSize, rectSize);
	    rectSize -= 2;
	}
    }

    /**
     * Get the game controller.
     *
     * @return The game controller.
     */
    private final GameController getController() {
	return _controller;
    }

    /**
     * Set a new game controller.
     *
     * @param The new game controller.
     */
    private final void setController( GameController controller) {
	_controller = controller;
    }
}
