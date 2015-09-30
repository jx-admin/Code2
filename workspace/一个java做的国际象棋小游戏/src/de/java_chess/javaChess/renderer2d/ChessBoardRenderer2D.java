/*
  ChessBoardRenderer2D - A class to render a 2D representation of
                         a chessboard.

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

import de.java_chess.javaChess.*;
import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.renderer.ChessBoardRenderer;
import java.awt.*;
import javax.swing.*;


/**
 * This class renders a chessboard as a 2D graphics.
 */
public class ChessBoardRenderer2D extends JPanel implements ChessBoardRenderer {

    // Static variables

    /**
     * The size of each square.
     */
    private static final int _squareSize = 50;

    // Instance variables

    /**
     * The current board.
     */
    private Board _board;

    /**
     * The control layer, where the user does his moves.
     */
    ControlLayer _controlLayer;

    /**
     * The pieces layer, that holds all the pieces of the current board.
     */
    PiecesLayer _piecesLayer;


    // Constructors

    /**
     * Create a new renderer instance.
     *
     * @param controller The game controller.
     * @param board The current board.
     */
    public ChessBoardRenderer2D( GameController controller, Board board) {
	setBoard( board);  // Store the board in a class variable.

	// Use a boarder layout for the entire component, since it holds
	// more that the chessboard itself.
	setLayout( new BorderLayout());

	// Add the row numbers to the board.
	JPanel rowNumbers = new JPanel();
	rowNumbers.setLayout( new GridLayout( 8, 1));
	rowNumbers.setPreferredSize( new Dimension( _squareSize / 2, 8 * _squareSize));
	for( int i = 8; i > 0; i--) {
	    rowNumbers.add( new JLabel( "" + i, JLabel.CENTER));
	}
	add( rowNumbers, BorderLayout.WEST);

	// Add the board itself.
	JLayeredPane boardPane = new JLayeredPane();
	boardPane.setPreferredSize( new Dimension( 8 * _squareSize, 8 * _squareSize));
	boardPane.setOpaque( false);

	// The squares of the board are drawn on the board layer.
	boardPane.add( new BoardLayer(), JLayeredPane.DEFAULT_LAYER);

	// The next layer holds the pieces.
	boardPane.add( _piecesLayer = new PiecesLayer( getBoard()), JLayeredPane.PALETTE_LAYER);

	// The next layer holds the control markers, when the user moves a piece.
	boardPane.add( _controlLayer = new ControlLayer( controller, _piecesLayer), JLayeredPane.MODAL_LAYER);

	// The next layer shows the animated pieces.
	boardPane.add( _piecesLayer.getAnimationLayer(), JLayeredPane.DRAG_LAYER);

	add( boardPane);

	// Add the line names to the board
	JPanel lineNames = new JPanel();
	lineNames.setLayout( new FlowLayout( FlowLayout.CENTER, 0, 0));
	lineNames.setPreferredSize( new Dimension( 8 * _squareSize + _squareSize / 2, _squareSize / 2));
	JLabel placeHolder = new JLabel();
	placeHolder.setPreferredSize( new Dimension( _squareSize / 2, _squareSize / 2));
	lineNames.add( placeHolder);
	byte [] name = new byte[1];
	for( int i = 0; i < 8; i++) {
	    name[0] = (byte)( 'a' + i);
	    JLabel nameLabel = new JLabel( new String( name), JLabel.CENTER);
	    nameLabel.setPreferredSize( new Dimension( _squareSize, _squareSize / 2));
	    lineNames.add( nameLabel);
	}
	add( lineNames, BorderLayout.SOUTH);
    }

    /**
     * Repaint the board after a game position change.
     */
    public final void repaintBoard() {
	_piecesLayer.repaintBoard();
	repaint();
    }

    /**
     * Render a ply (the move of a piece).
     *
     * @param ply The ply to render.
     */
    public final void doPly( Ply ply) {
	_piecesLayer.doPly( ply);
	repaint();
    }

    /**
     * Get the current board.
     *
     * @return The current board.
     */
    public final Board getBoard() {
	return _board;
    }

    /**
     * Set a new board.
     *
     * @param board The new board.
     */
    public final void setBoard(Board board) {
	_board = board;
    }

    /**
     * Get the size of a square.
     *
     * @return The size of a square as a int (since height and width are the same).
     */
    public static final int getSquareSize() {
	return _squareSize;
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
	return new Dimension( 8 * getSquareSize() + getSquareSize() / 2, 8 * getSquareSize() + getSquareSize() / 2);
    }
}
