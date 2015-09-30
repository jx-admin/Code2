/*
  MinimaxTest2 - The second test for the minimax implementation.

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

package de.java_chess.javaChess.engine.test;

import de.java_chess.javaChess.bitboard.*;
import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.engine.*;
import de.java_chess.javaChess.game.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import junit.framework.*;


/**
 * A test, to make sure, that the minimax algorithm
 * does not give pieces away while there's no need.
 * e4 e5 f3 Qg5 d3 Qxg2, which is a useless move, since
 * the queen is captured by the bishop f1.
 */
class MinimaxTest2 extends TestCase {

    // Instance variables

    /**
     * The current game.
     */
    Game _game;

    /**
     * The chess board.
     */
    BitBoard _board;


    /**
     * The chess engine.
     */
    ChessEngine _engine;

    
    // Constructors

    /**
     * Create a new instance of this test.
     */
    public MinimaxTest2() {
	super( "A test of the minimax implementation, if piece captures are not recognized");
    }


    // Methods 

    /**
     * Run the actual test(s).
     */
    public void runTest() {
	minimaxTest1();
    }

    /**
     * Prepare the test(s).
     */
    protected void setUp() {

	// Create a new game.
	_game = new GameImpl();

	// Create a new board.
	_board = new BitBoardImpl();

	// Create a engine instance.
        _engine = new ChessEngineImpl( _game, _board, false);

	// Set the pieces on their initial positions.
	_board.initialPosition();

	// Move the white pawn from e2 - e4
	doPly( new PlyImpl( new PositionImpl( 12), new PositionImpl( 12 + 16)));

	// Move the black pawn to e5
	doPly( new PlyImpl( new PositionImpl( 52), new PositionImpl( 52 - 16)));

	// Move the white pawn to f3
	doPly( new PlyImpl( new PositionImpl( 13), new PositionImpl( 13 + 8)));

	// Move the black queen to g5
	doPly( new PlyImpl( new PositionImpl( 59), new PositionImpl( 38)));

	// Move the white pawn to d3
	doPly( new PlyImpl( new PositionImpl( 11), new PositionImpl( 19)));

	// Black is about to move
	_engine.setWhite( false);
    }

    /**
     * Run the actual test.
     */
    private void minimaxTest1() {

	// Compute the best ply for this game position.
	Ply bestPly = _engine.computeBestPly();

	// Check if Qxg2 was delivered as the best ply.
	assertTrue( "Engine computed ply Qxg2"
		    , ( ( bestPly.getSource().getSquareIndex() != 38) 
			|| ( bestPly.getDestination().getSquareIndex() != ( 14))));
    }

    /**
     * Perform a ply.
     *
     * @param ply The ply to perform.
     */
    private void doPly( Ply ply) {
	_game.doPly( ply);
	_board.doPly( ply);
    }
}
