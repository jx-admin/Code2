/*
  GameImpl - A class to store a complete chess game and provide
             access to all the game stages.

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

package de.java_chess.javaChess.game;

import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.position.*;
import java.util.*;


/**
 * This class stores a complete chess game and provides access
 * to all the game stages.
 */
public class GameImpl implements Game {

    // Inner classes

    /**
     * This class holds the game status after a ply.
     */
    class GameStatus {

	// Instance variables

	/**
	 * The ply that lead to this status.
	 */
	Ply _ply;

	/**
	 * This bitmap indicates the pieces, that
	 * were moved from their initial position.
	 */
	long _movedPieces;


	// Constructors
	
	/**
	 * Create a game status from a ply and a moved pieces bitmask.
	 *
	 * @param ply The ply for this game status.
	 * @param bitmask The moved pieces _before_ this ply.
	 */
	GameStatus( Ply ply, long bitmask) {

	    // Store the ply in an instance variable.
	    setPly( ply);

	    // Store the moved pieces of the previous game status.
	    setMovedPieces( bitmask);

	    // And store the moved piece of the current ply.
	    setMoved( ply.getSource());
	}

	/**
	 * Create a first game status.
	 *
	 * @param ply The first ply of this game.
	 */
	GameStatus( Ply ply) {
	    
	    // Construct a new instance from this ply and all
	    // pieces on their initial positions.
	    this( ply, 0L);
	}

	/**
	 * Create a new game status from a preceeding status and a ply.
	 *
	 * @param status The previous game status.
	 */
	GameStatus( Ply ply, GameStatus previousStatus) {

	    // Create a new instance from this ply and the
	    // previous game status.
	    this( ply, previousStatus.getMovedPieces());
	}

	// Methods
	
	/**
	 * Get the ply, that lead to this game status.
	 *
	 * @return The ply, that lead to this game status.
	 */
	final Ply getPly() {
	    return _ply;
	}

	/**
	 * Set the ply, that lead to this game status.
	 *
	 * @param ply The ply, that lead to this game status.
	 */
	final void setPly( Ply ply) {
	    _ply = ply;
	}
	
	/**
	 * Mark a piece moved from it's initial position.
	 *
	 * @param position The position of the moved piece.
	 */
	private final void setMoved( Position position) {
	    _movedPieces |= ( 1L << position.getSquareIndex());
	}
	
	/**
	 * Set the bitmask for the moved pieces.
	 *
	 * @param bitmask The new bitmask for the moved pieces.
	 */
	private final void setMovedPieces( long bitmask) {
	    _movedPieces = bitmask;
	}
	
	/**
	 * Get the bitmask for the moved pieces.
	 *
	 * @return The bitmask for the moved pieces.
	 */
	private final long getMovedPieces() {
	    return _movedPieces;
	}

	/**
	 * Check if a piece on a given initial position
	 * has been moved.
	 */
	public final boolean hasBeenMoved( Position position) {
	    return ( ( _movedPieces & ( 1L << position.getSquareIndex())) != 0L);
	}
    }


    // Instance variables

    /**
     * This stack holds the plies with the associated game status.
     * The reason for using a stack are the search operations for a new
     * ply. You have to do and undo a lot of plies.
     */
    private Stack _gameStatus;


    // Constructors

    /**
     * Create a new game instance.
     */
    public GameImpl() {
	_gameStatus = new Stack();
    }


    // Methods

    /**
     * Get the last game status.
     *
     * @return The last game status.
     */
    private final GameStatus getLastGameStatus() {
	return _gameStatus.empty() ? null : (GameStatus)_gameStatus.peek();
    }

    /**
     * Reset the game.
     */
    public final void reset() {
	// Simply remove all the status instances from the game.
	_gameStatus.clear();
    }

    /**
     * Do a new ply in this game.
     *
     * @param ply The next ply.
     */
    public final void doPly( Ply ply) {

	// Add a new game status, that derives from the previous status.
	_gameStatus.push( getLastGameStatus() == null? new GameStatus( ply) : new GameStatus( ply, getLastGameStatus()));
    }

    /**
     * Undo the last ply.
     */
    public final void undoLastPly() {
	
	// Remove the last ply
	_gameStatus.pop();
    }

    /**
     * Get the total number of plies.
     *
     * @return The total number of plies.
     */
    public final int getNumberOfPlies() {
	return _gameStatus.size();
    }

    /**
     * Get the last ply.
     *
     * @return The last ply.
     */
    public final Ply getLastPly() {
	GameStatus lastStatus = getLastGameStatus();
	return lastStatus == null ? null : lastStatus.getPly();
    }

    /**
     * Check, if a piece on a given positon was moved from it's
     * initial position.
     *
     * @param position The position to check.
     */
    public final boolean hasBeenMoved( Position position) {
	GameStatus lastStatus = getLastGameStatus();
	return lastStatus == null ? false : lastStatus.hasBeenMoved( position);
    }

    /**
     * Convert the game to a string.
     *
     * @return A string representation of the game.
     */
    public final String toString() {

	// A buffer for the string representation
	StringBuffer resultBuffer = new StringBuffer();

	// Iterate over the stack content
	for( Iterator iter = _gameStatus.iterator(); iter.hasNext(); ) {
	    resultBuffer.append( ((GameStatus)iter.next()).getPly().toString());
	    resultBuffer.append( "\n");
	}

	return resultBuffer.toString();
    }
}

