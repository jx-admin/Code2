/*
  PiecesLayer - A component to hold the pieces of a chess game.

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
import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import java.awt.*;
import javax.swing.*;


/**
 * This class implements the functionality to hold the pieces of a
 * chessboard.
 */
class PiecesLayer extends JPanel {

    // Instance variables

    private Board _board;

    private ChessSet _set;

    /**
     * Flag to indicate of the moves are animated.
     */
    private boolean _animatedMoves = false;

    /**
     * The layer to animate the moved pieces.
     */
    private AnimationLayer _animationLayer;

    
    /**
     * The labels for all the squares.
     */
    private PositionRenderer [] _square = new PositionRenderer[64];

    PiecesLayer( Board board) {
	super( new GridLayout(8,8));
	_set = new ChessSet(this);
	
	_animationLayer = new AnimationLayer( this);

        _board = board;
        int _boardSize = 8 * ChessBoardRenderer2D.getSquareSize();
	setPreferredSize( new Dimension( _boardSize, _boardSize));
	setBounds( 0, 0 , _boardSize, _boardSize);
	setOpaque(false);
	repaintBoard();
    }

    final void repaintBoard() {
	// removeAll();

	// Create a renderer for each chessboard square

	Position pos = new PositionImpl( 0);  // Avoid to create new position instances for each square

	for( int s = 0; s < 64; s++) {
	    pos.setSquareIndex( s);
	    Piece p = _board.getPiece( pos);
	    _square[ s] = ( p != null ? new PositionRenderer( new PieceRenderer( p.getColor(), p.getType(), _set, this)) : new PositionRenderer());
	}

	// The order of the component adding is different then the order of the chessboard squares.
	// _square[0] = square a1 of the chessboard, but the components are added from the left
	// upper square (= a8)
        for( int s = 63; s >= 0; s--) {
	    add( _square[ ( s & ~7) + ( 7 - ( s & 7))]);
	}
	repaint();
    }

    /**
     * Render a ply.
     *
     * @param ply The ply to render.
     */
    public final void doPly( Ply ply) {
	if( _animatedMoves) {
	    getAnimationLayer().animatePly( ply);
	    getAnimationLayer().start();
	    repaint();
	} else {
	    // Check, if it was a castling
	    if( ply instanceof CastlingPly) {
		int source = ply.getSource().getSquareIndex();
		if( ( (CastlingPly)ply).isLeftCastling()) {
		    _square[ source - 2].getPieceFrom( _square[ source]);
		    _square[ source - 1].getPieceFrom( _square[ source - 4]);  // Move the rook to the right
		} else {
		    _square[ source + 2].getPieceFrom( _square[ source]);
		    _square[ source + 1].getPieceFrom( _square[ source + 3]);  // Move the rook to the left
		}
	    } else {
		// If a pawn has just reached the last row
		if(ply instanceof TransformationPly) {

		    // Copy the piece from source square to destination square.
		    _square[ ply.getDestination().getSquareIndex()].getPieceFrom( _square[ ply.getSource().getSquareIndex()]);

		    // Now change the rendering to the new piece type.
		    _square[ ply.getDestination().getSquareIndex()].setIcon( new ImageIcon( new PieceRenderer( ply.getDestination().getSquareIndex() < 8 
												? Piece.BLACK : Piece.WHITE, ( (TransformationPly)ply).getTypeAfterTransformation(), _set, this)));
		} else {
		    // Copy the piece from source square to destination square.
		    _square[ ply.getDestination().getSquareIndex()].getPieceFrom( _square[ ply.getSource().getSquareIndex()]);

		    // If it's a en passant ply, remove the attacked pawn.
		    if( ply instanceof EnPassantPly) {
			_square[ ( (EnPassantPly)ply).getAttackedPosition().getSquareIndex()] = new PositionRenderer();
		    }
		}
	    }
	    repaint();
	}
    }

    /**
     * Get the current animation layer.
     *
     * @return The current animation layer.
     */
    final AnimationLayer getAnimationLayer() {
	return _animationLayer;
    }

    /**
     * Get the position renderer for a given square.
     *
     * @param squareIndex The index of the square.
     *
     * @return The position renderer for the given square.
     */
    final PositionRenderer getPositionRenderer( int squareIndex) {
	return _square[ squareIndex];
    }
}
