/*
  AnimationLayer - A pane to animate moved chess pieces.

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
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import java.awt.*;
import javax.swing.*;


/**
 * This class animates moved chess pieces in a separate thread.
 */
class AnimationLayer extends JPanel implements Runnable {

    // Static variables


    // Instance variables

    /**
     * The pieces layer.
     */
    private PiecesLayer _piecesLayer;

    /**
     * The current ply to animate.
     */
    private Ply _ply = null;

    /**
     * The renderer for the piece, that we are animate.
     */
    // private PieceRenderer _piece;

    /**
     * The current point of the animated piece.
     */
    private Point _currentPoint;

    /**
     * The animation thread.
     */
    private Thread _animationThread;


    // Constructors

    /**
     * Create a new AnimationLayer instance.
     *
     * @param piecesLayer The layer with the pieces.
     */
    AnimationLayer( PiecesLayer piecesLayer) {
	setPiecesLayer( piecesLayer);
    }


    // Methods

    /**
     * Animate the move of a piece.
     */
    void animatePly( Ply ply) {
	_ply = ply;
    }

    /**
     * The method to start the thread.
     */
    public void start() {
	if( _animationThread == null) {
	    _animationThread = new Thread( this);
	    _animationThread.start();
	}
    }

    /**
     * The run method to start the animation thread.
     */
    public void run() {
	ImageIcon icon = (ImageIcon)getPiecesLayer().getPositionRenderer( _ply.getSource().getSquareIndex()).getIcon();
	JLabel movingPiece = new JLabel( icon);
	// _piece = (PieceRenderer)icon.getImage();
	getPiecesLayer().getPositionRenderer( _ply.getSource().getSquareIndex()).setIcon( null);
	getPiecesLayer().repaint();
	int xOffset = ( ChessBoardRenderer2D.getSquareSize() - icon.getIconWidth()) / 2;
	int yOffset = ( ChessBoardRenderer2D.getSquareSize() - icon.getIconHeight()) / 2;
	_currentPoint = new Point( ( _ply.getSource().getSquareIndex() & 7) * ChessBoardRenderer2D.getSquareSize() + xOffset
				   , ( 7 - ( _ply.getSource().getSquareIndex() >> 3)) * ChessBoardRenderer2D.getSquareSize() + yOffset);
	Point endPoint = new Point( ( _ply.getDestination().getSquareIndex() & 7) * ChessBoardRenderer2D.getSquareSize() + xOffset
				    , ( 7 - ( _ply.getDestination().getSquareIndex() >> 3)) * ChessBoardRenderer2D.getSquareSize() + yOffset);
	int xMoveOffset = _currentPoint.x < endPoint.x ? 1 : _currentPoint.x > endPoint.x ? -1 : 0;
	int yMoveOffset = _currentPoint.y < endPoint.y ? 1 : _currentPoint.y > endPoint.y ? -1 : 0;

	movingPiece.setLocation( _currentPoint);

	while( ! _currentPoint.equals( endPoint)) {
	    if( _currentPoint.x == endPoint.x) {
		xMoveOffset = 0;
	    }
	    if( _currentPoint.y == endPoint.y) {
		yMoveOffset = 0;
	    }
	    _currentPoint.x += xMoveOffset;
	    _currentPoint.y += yMoveOffset;
	    // invalidate();
	    // repaint();
	    // paint( getGraphics());
	    //getGraphics().drawImage( _piece, _currentPoint.x, _currentPoint.y, this);
	    movingPiece.setLocation( _currentPoint);
	    paintImmediately( _currentPoint.x - 1, _currentPoint.y - 1, ChessBoardRenderer2D.getSquareSize(), ChessBoardRenderer2D.getSquareSize());
	    try {
		Thread.sleep( 5);
	    } catch( InterruptedException ignored) {}
	    // getGraphics().clearRect( _currentPoint.x - 1, _currentPoint.y - 1, ChessBoardRenderer2D.getSquareSize() + 1, ChessBoardRenderer2D.getSquareSize() + 1);
	}
	movingPiece.setVisible( false);
	getPiecesLayer().getPositionRenderer( _ply.getDestination().getSquareIndex()).setIcon( icon);
	_ply = null;
	_animationThread = null;
    }

    /**
     * Paint the animated piece.
     *
     * @param g The graphics context.
     */
    /*  public void paint( Graphics g) {
	System.out.println( "Repaint piece at " + _currentPoint.x + "," + _currentPoint.y);
	// g.drawImage( _piece, _currentPoint.x, _currentPoint.y, this);
	} */

    /**
     * Get the current pieces layer.
     *
     * @return The current pieces layer.
     */
    PiecesLayer getPiecesLayer() {
	return _piecesLayer;
    }

    /**
     * Set a new pieces layer.
     *
     * @param piecesLayer The new pieces layer.
     */
    void setPiecesLayer( PiecesLayer piecesLayer) {
	_piecesLayer = piecesLayer;
    }
}
