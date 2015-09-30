/*
  GameController - A class to control the game of chess.

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

package de.java_chess.javaChess;

import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.dialogs.*;
import de.java_chess.javaChess.engine.*;
import de.java_chess.javaChess.game.*;
import de.java_chess.javaChess.notation.*;
import de.java_chess.javaChess.piece.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import de.java_chess.javaChess.renderer.ChessBoardRenderer;
import de.java_chess.javaChess.timer.*;
import javax.swing.*;


/**
 * This class controls the game.
 */
public class GameController {

    // Instance variables

    /**
     * The current game.
     */
    private Game _game;

    /**
     * The game notation.
     */
    private GameNotation _gameNotation;

    /**
     * A flag to indicate, if white has the next move.
     */
    private boolean _moveRight = true;

    /**
     * A flag to indicate, if the computer play with white pieces.
     */
    private boolean _computerIsWhite = false;

    /**
     * The chess engine.
     */
    private ChessEngine _engine;

    /**
     * The current board.
     */
    private Board _board;

    /**
     * The renderer.
     */
    ChessBoardRenderer _renderer;

    /**
     * The timer for the game.
     */
    GameTimer _gameTimer; 

    /**
     * The current game state.
     */
    byte _gameState;


    // Constructors

    /**
     * Create a new controller instance.
     *
     * @param game The current game.
     * @param gameNotation The notation of the game.
     * @param engine The current engine.
     * @param board The current board.
     * @param timer The game timer.
     */
    public GameController( Game game, GameNotation gameNotation, ChessEngine engine, Board board, GameTimer timer) {
	setGame( game);
	setGameNotation( gameNotation);
	setEngine( engine);
	setBoard( board);
	setGameTimer( timer);
    }

    
    // Methods

    /**
     * Get the current game.
     *
     * @return The current game.
     */
    public final Game getGame() {
	return _game;
    }

    /**
     * Set the current game.
     *
     * @param The current game.
     */
    public final void setGame( Game game) {
	_game = game;
    }

    /**
     * Get the current game notation.
     *
     * @return The current game notation.
     */
    public final GameNotation getGameNotation() {
	return _gameNotation;
    }

    /**
     * Set the current game notation.
     *
     * @param gameNotation The new game notation.
     */
    public final void setGameNotation( GameNotation gameNotation) {
	_gameNotation = gameNotation;
    }

    /**
     * Get the current chess engine.
     *
     * @return The current chess engine.
     */
    final ChessEngine getEngine() {
	return _engine;
    }

    /**
     * Set a new chess engine.
     *
     * @param engine The new engine.
     */
    final void setEngine( ChessEngine engine) {
	_engine = engine;
    }

    /**
     * Get the current board.
     *
     * @return The current board.
     */
    final Board getBoard() {
	return _board;
    }

    /**
     * Set a new board.
     *
     * @param board The new board.
     */
    final void setBoard( Board board) {
	_board = board;
    }

    /**
     * Get the renderer.
     *
     * @return The current renderer.
     */
    final ChessBoardRenderer getRenderer() {
	return _renderer;
    }

    /**
     * Set a new renderer.
     *
     * @param renderer The new renderer.
     */
    final void setRenderer( ChessBoardRenderer renderer) {
	_renderer = renderer;
    }

    /**
     * Get the current game timer.
     *
     * @return The current game timer.
     */
    final GameTimer getGameTimer() {
	return _gameTimer;
    }

    /**
     * Set a new game timer.
     *
     * @param timer The new game timer.
     */
    final void setGameTimer( GameTimer timer) {
	_gameTimer = timer;
    }

    /**
     * Reset the game controller.
     */
    public final void reset() {
	_gameState = GameState.STOPPED;
    }

    /**
     * Let the computer make a move.
     */
    public final boolean computerPly() {
	Ply nextPly = getEngine().computeBestPly();
	if( nextPly == null) {
	    System.out.println( "No computer move returned");
	    return false;
	} else {
	    doPly( nextPly);
	    return true;
	}
    }

    /**
     * The user moved a piece.
     *
     * @param ply The ply of the user.
     */
    public final void userPly( Ply ply) {

	// plyInterpretation
	ply = convertUserPly( ply);

	// Check if the user has the right to move a piece
	// and made a valid move.
	if( _moveRight != _computerIsWhite) {
	    if( ( _gameState != GameState.CHECKMATE) && ( _gameState != GameState.DRAW)) {
		if( getEngine().validateUserPly( ply)) {
		    doPly( ply);    // Ok => move the piece.
		} else {  // The user had to right to move or made an invalid move.
		    signalUserInputError( "invalid move");
		}
	    } else {
		signalUserInputError( "game is already over");
	    }
	} else {
	    signalUserInputError( "user is not about to move");
	}
    }
    
    /**
     * Try to interpretate a user ply, if it's a castling or so.
     *
     * @param ply The user ply.
     *
     * @return The converted ply.
     */
    private final Ply convertUserPly( Ply ply) {

	Position source = ply.getSource();
	Position destination = ply.getDestination();
	Piece piece = getBoard().getPiece( source);

	if( piece != null) {
	    // Check, if the user wanted a castling.
	    if( piece.getType() == Piece.KING) {
		int sourceLine = source.getSquareIndex() & 7;
		int destinationLine = destination.getSquareIndex() & 7;
		
		// If the castling goes to the left.
		if( ( sourceLine - 2) == destinationLine) {
		    return new CastlingPlyImpl( source, true);
		}

		// If the castling goes to the right.
		if( ( sourceLine + 2) == destinationLine) {
		    return new CastlingPlyImpl( source, false);
		}
	    } else {  // Check if this is a transformation ply
		if( piece.getType() == Piece.PAWN) {
		    int destinationRow = destination.getSquareIndex() >> 3;

		    // If the pawn reached the last row
		    if( destinationRow == 7) {
			TransformationDialog.getInstance().show();
			byte pieceType = TransformationDialog.getInstance().getPieceType();

			// Create and return a new transformation ply.
			return new TransformationPlyImpl( source, destination, pieceType);
		    }
		}
	    }
	}
	
	return ply;  // Return the original ply
    }

    /**
     * Perform a ply.
     *
     * @param ply The ply to do.
     */
    private final void doPly( Ply ply) {

	// Store the ply notation to set the check flags a bit later, since they are computed in the gameOver()
	// call in the move right toggle.
	PlyNotation plyNotation = new PlyNotationImpl(  ply
							, ! (ply instanceof CastlingPly) && ( getBoard().getPiece( ply.getDestination()) != null)
							, getBoard().getPiece( ply.getSource()));
	getGame().doPly( ply);
	getBoard().doPly( ply);    
	getRenderer().doPly( ply);

	// Now try to get some info on the current game state.
	boolean gameOver = gameOver( ! _moveRight);

	// Set check info for this ply.
	if( _gameState == GameState.CHECKMATE) {
	    plyNotation.setCheckMate( true);
	} else {
	    if( _gameState == GameState.CHECK) {
		plyNotation.setCheck( true);
	    }
	}
	
	// Add the ply to the notation.
	getGameNotation().addPly( plyNotation);

	if( gameOver) {

	    getGameTimer().stop();  // The game has ended.

	    if( _gameState == GameState.DRAW) {
		signalGameOver( "Draw! Do you want to play again?");
	    } else {
		signalGameOver( _moveRight == _computerIsWhite ? "Checkmate! I win! :-)" : "Checkmate! I lose... :-(" );
	    }
	} else {
	    toggleMoveRight();
	}
    }

    /**
     * Turn the right to move from one player to the other.
     */
    public void toggleMoveRight() {
	_moveRight = ! _moveRight;

	if( getGameTimer().isRunning()) {
	    getGameTimer().toggle();
	} else {
	    getGameTimer().start();

	    _gameState = GameState.ONGOING;  // Change the game state to running.
	}
	
	// Check if the computer has the next move.
	if( _moveRight == _computerIsWhite) {
	    if( ! computerPly()) {
		System.out.println( "Computer cannot move!");
	    }
	}
    }

    /**
     * Check if the game is over.
     *
     * @param white True, if white is about to move.
     *
     * @return true, if the game is over.
     */
    private final boolean gameOver( boolean white) {
	_gameState = getEngine().getCurrentGameState( white);

	return ( _gameState == GameState.CHECKMATE) || ( _gameState == GameState.DRAW);
    }

    /**
     * Signal a input error to the user.
     *
     * @param errorMessage More information on the error.
     */
    public final void signalUserInputError( String errorMessage) {
	System.out.print( (char)7);
	JOptionPane.showMessageDialog( null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Signal the end if the game.
     *
     * @param gameOverMessage Some info how and why the game has ended.
     */
    public final void signalGameOver( String gameOverMessage) {
	System.out.print( (char)7);
	JOptionPane.showMessageDialog( null, gameOverMessage, "Game over", JOptionPane.INFORMATION_MESSAGE);
    }
}

