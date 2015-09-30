/*
  ChessEngineImpl - A class to implement a engine to play chess.

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

package de.java_chess.javaChess.engine;

import de.java_chess.javaChess.*;
import de.java_chess.javaChess.bitboard.*;
import de.java_chess.javaChess.board.*;
import de.java_chess.javaChess.engine.hashtable.*;
import de.java_chess.javaChess.game.*;
import de.java_chess.javaChess.ply.*;
import de.java_chess.javaChess.renderer2d.EnginePanel;
import java.awt.event.*;
import javax.swing.*;


/**
 * This class implements the functionality to play the
 * actual game of chess
 */
public class ChessEngineImpl implements ChessEngine, Runnable, ActionListener {


    // Instance variables

    /**
     * The current game.
     */
    private Game _game;

    /**
     * The board to operate on.
     */
    private Board _board;

    /**
     * A analyzer for the boards.
     */
    private BitBoardAnalyzer _analyzer;

    /**
     * The maximum search depth.
     */
    private int _maxSearchTime = 5000;

    /**
     * Flag to indicate if the engine operates on the white pieces.
     */
    boolean _white;

    /**
     * The generator for the plies.
     */
    PlyGenerator _plyGenerator;

    /**
     * A hashtable for computed plies.
     */
    PlyHashtable _hashtable;

    /**
     * The currently used search depth.
     */
    int _searchDepth;

    /**
     * A counter for the analyzed boards.
     */
    long _analyzedBoards;

    /**
     * A thread to search for the best move.
     */
    Thread _searchThread;

    /**
     * Flag to stop the search.
     */
    boolean _stopSearch;

    /**
     * The best computed ply so far.
     */
    AnalyzedPly _bestPly = null;

    /**
     * The menu items for the various search times.
     */
    private JMenuItem [] _searchTimeMenuItem;

    /**
     * The predefined search times (in seconds).
     */
    private int [] _searchTime = { 3, 5, 10, 15, 30, 45, 60};

    /**
     * The menu items for the various hashtable sizes.
     */
    private JMenuItem [] _hashtableSizeMenuItem;

    /**
     * The predefined hashtable sizes.
     */
    private int [] _hashtableSize = { 5000, 10000, 20000, 50000, 100000 };

    /**
     * The menu items for the various search times.
     */
    private EnginePanel enginePanel = null;


    // Constructors

    /**
     * Create a new engine instance with a given board.
     *
     * @param board The new board.
     * @param white Flag, to indicate if the engine operates on the white pieces.
     */
    public ChessEngineImpl( Game game, Board board, boolean white) {
	setGame( game);
	setBoard( board);
	setWhite( white);
	_hashtable = new PlyHashtableImpl( 10000);
	_plyGenerator = new PlyGenerator( getGame(), _hashtable);
	_analyzer = new BitBoardAnalyzerImpl( _plyGenerator);
	_plyGenerator.setAnalyzer( _analyzer);
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
     * Get the current board.
     *
     * @return The current board.
     */
    public Board getBoard() {
	return _board;
    }

    /**
     * Set the board.
     *
     * @param board The new board.
     */
    public void setBoard( Board board) {
	_board = board;
    }

    /**
     * Get the current hashtable for this ply generator.
     *
     * @return The current hashtable for this ply generator.
     */
    public final PlyHashtable getHashtable() {
	return _hashtable;
    }

    /**
     * Set a new hashtable for this ply generator.
     *
     * @param hashtable The new hashtable for this ply generator.
     */
    public final void setHashtable( PlyHashtable hashtable) {
	_hashtable = hashtable;
    }

    /**
     * Get the maximum search time.
     *
     * @return The maximum search time.
     */
    public final int getMaximumSearchTime() {
	return _maxSearchTime;
    }

    /**
     * Set the maximum search time.
     *
     * @param depth The new search time.
     */
    public final void setMaximumSearchTime( int time) {
	_maxSearchTime = time;
    }

    /**
     * Get the color of this engine.
     *
     * @param white true, if the engine operates with the white pieces.
     */
    public boolean isWhite() {
	return _white;
    }

    /**
     * Set the color of the engine.
     *
     * @param white flag to indicate if the engine operates on the white pieces.
     */
    public void setWhite( boolean white) {
	_white = white;
    }

    /**
     * Start a new thread to search for a ply.
     */
    public void start() {
	if( _searchThread == null) {
	    _stopSearch = false;
	    _searchThread = new Thread( this);
	    _searchThread.start();
	}
    }


    /**
     * Compute the best ply for the current position.
     *
     * @return The best known ply for the current position.
     */
    public Ply computeBestPly() {

	_bestPly = null;  // Remove ply from last computation.
	long startTime = System.currentTimeMillis();

	start();
	try {
	    Thread.sleep( getMaximumSearchTime());
	    _stopSearch = true;
	    _searchThread.join();      // Wait for the search thread to end the search at this search depth.
	    _searchThread = null;      // Remove the thread, so it can be recreated for the next move.
	} catch( InterruptedException ignored) {}

	long usedTime = System.currentTimeMillis() - startTime;
	if( _bestPly != null) {
	    String sOut1 = "Best ply: " + _bestPly.getPly().toString() + " with score " + _bestPly.getScore() + " and search depth " + _searchDepth;
	    String sOut2 = "Analyzed boards: " + _analyzedBoards + " in " + usedTime + " ms";
	    
	    if ( this.enginePanel != null ) {
		this.enginePanel.modifyText( sOut1 );
		this.enginePanel.modifyText( sOut2 );
	    }
	    return _bestPly.getPly();
	}
	return null;
    }

    /**
     * The main method of the search thread.
     */
    public void run() {
	_analyzedBoards = 0;
	_searchDepth = 0;

	// The following search is rather inefficent at the moment, since we should try to get a principal variant
	// from a search, so we can presort the plies for the next search.
	// Thread currentThread = Thread.currentThread();
	String sOutput;
	do {
	    _searchDepth++;
	    AnalyzedPly searchDepthResult = startMinimaxAlphaBeta( isWhite());
	    if( searchDepthResult != null) {
		_bestPly = searchDepthResult;
		sOutput = "Best ply for search depth " + _searchDepth + " is " + _bestPly.getPly().toString() + " with score " + _bestPly.getScore();
		if ( this.enginePanel != null ) {
		    this.enginePanel.modifyText( sOutput );
		}
	    }
	    // If search depth 1 was completed and no valid ply was found,
	    // it seems that the computer is checkmate and the search can be aborted.
	} while( ! _stopSearch && ( _bestPly != null));
    }

    /**
     * Start a complete Minimax-Alpha-Beta search. This is the search level 1, where we have to store the
     * analyzed ply, so it gets a special method.
     *
     * @param white Flag to indicate, if white is about to move.
     */
    private final AnalyzedPly startMinimaxAlphaBeta( boolean white) {
	short curAlpha = AnalyzedPly.MIN_SCORE;
	short curBeta = AnalyzedPly.MAX_SCORE;
	int bestPlyIndex = -1;

	Ply [] plies = _plyGenerator.getPliesForColor( (BitBoard)getBoard(), white);
	if( white) {
	    for( int i = 0; i < plies.length; i++) {
		if( _stopSearch && ( _searchDepth > 1)) {  // If the search time is over and at least depth 1 was completed
		    return null;                           // abort the search at search depth 1.
		}                                          // (Deeper levels are still searched).
		getGame().doPly( plies[i]);
		short val = minimaxAlphaBeta( plies[i], getBoard().getBoardAfterPly( plies[i]), false, 1, curAlpha, curBeta);
		if( val > curAlpha) {
		    curAlpha = val;
		    bestPlyIndex = i;
		}
		if( curAlpha >= curBeta) {
		    getGame().undoLastPly();  // Take the last ply back, before the loop is aborted.
		    break;
		}
		getGame().undoLastPly();
	    }

	    // Since this is the best ply so far, we store it in the hashtable. This makes sense,
	    // since the minimax algorithm is started several times, before a move is selected.
	    // So this move is not necessarily applied immediately!
	    getHashtable().pushEntry( new PlyHashtableEntryImpl( getBoard(), plies[bestPlyIndex]));

	    return ( bestPlyIndex != -1) ? new AnalyzedPlyImpl( plies[bestPlyIndex], curAlpha) : null;
	} else {
	    for( int i = 0; i < plies.length; i++) {
		if( _stopSearch && ( _searchDepth > 1)) {  // If the search time is over and at least depth 1 was completed
		    return null;                           // abort the search at search depth 1.
		}                                          // (Deeper levels are still searched).
		getGame().doPly( plies[i]);
		short val = minimaxAlphaBeta( plies[i], getBoard().getBoardAfterPly( plies[i]), true, 1, curAlpha, curBeta);
		if( val < curBeta) {
		    curBeta = val;
		    bestPlyIndex = i;
		}
		if( curBeta <= curAlpha) {
		    getGame().undoLastPly();  // Take the last ply back, before the loop is aborted.
		    break;
		}
		getGame().undoLastPly();
	    }

	    // Since this is the best ply so far, we store it in the hashtable. This makes sense,
	    // since the minimax algorithm is started several times, before a move is selected.
	    // So this move is not necessarily applied immediately!
	    getHashtable().pushEntry( new PlyHashtableEntryImpl( getBoard(), plies[bestPlyIndex]));

	    return ( bestPlyIndex != -1) ? new AnalyzedPlyImpl( plies[bestPlyIndex], curBeta) : null;
	}
    }

    /**
     * Perform a alpha-beta minimax search on the board.
     *
     * @param lastPly The ply, that created this board.
     * @param board The board to analyze.
     * @param white true, if white has the next move.
     * @param byte searchLevel The level to search for.
     * @param alpha The current maximum.
     * @param beta The current minimum.
     */
    private final short minimaxAlphaBeta( Ply lastPly, Board board, boolean white, int searchLevel, short alpha, short beta) {
	if( searchLevel >= _searchDepth) {
	    _analyzedBoards++;
	    return analyzeBoard( board);
	} else {
	    short curAlpha = alpha;
	    short curBeta = beta;
	    int bestPlyIndex = -1;

	    Ply [] plies = _plyGenerator.getPliesForColor( (BitBoard)board, white);
	    if( white) {
		for( int i = 0; i < plies.length; i++) {
		    getGame().doPly( plies[i]);
		    short val = minimaxAlphaBeta( plies[i], board.getBoardAfterPly( plies[i]), false, searchLevel + 1, curAlpha, curBeta);
		    if( val > curAlpha) {
			curAlpha = val;
			bestPlyIndex = i;  // Store the index of this ply, so we can access it later.
		    }
		    if( curAlpha >= curBeta) {
			getGame().undoLastPly();  // Take the last ply back, before the loop is aborted.
			break;
		    }
		    getGame().undoLastPly();
		}

		if( bestPlyIndex != -1) {
		    // Since this is the best ply for this search level, we store it in the hashtable
		    getHashtable().pushEntry( new PlyHashtableEntryImpl( board, plies[ bestPlyIndex]));
		}

		return curAlpha;
	    } else {
		for( int i = 0; i < plies.length; i++) {
		    getGame().doPly( plies[i]);
		    short val = minimaxAlphaBeta( plies[i], board.getBoardAfterPly( plies[i]), true, searchLevel + 1, curAlpha, curBeta);
		    if( val < curBeta) {
			curBeta = val;
			bestPlyIndex = i;  // Store the index of this ply, so we can access it later.
		    }
		    if( curBeta <= curAlpha) {
			getGame().undoLastPly();  // Take the last ply back, before the loop is aborted.
			break;
		    }
		    getGame().undoLastPly();
		}

		if( bestPlyIndex != -1) {
		    // Since this is the best ply for this search level, we store it in the hashtable
		    getHashtable().pushEntry( new PlyHashtableEntryImpl( board, plies[ bestPlyIndex]));
		}

		return curBeta;
	    }
	}
    }

    /**
     * Compute a score for a game position.
     *
     * @return A score for the current game position.
     */
    public final short analyzeBoard( Board board) {
	return _analyzer.analyze( (BitBoard)board, isWhite());
    }

    /**
     * Check if a ply made by the user is valid.
     *
     * @param ply The user ply.
     *
     * @return true, if the ply is valid. false otherwise.
     */
    public final boolean validateUserPly( Ply ply) {

	// The following code is really inefficient, since
	// the ply computation should be done during the permanent brain
	// computations.

	// Compute all the plies a user can perform (also really not
	// very effient).
	Ply [] plies = _plyGenerator.getPliesForColor( (BitBoard)getBoard(), ! isWhite());

	for( int p = 0; p < plies.length; p++) {  // For each ply
	    if( plies[p].equals( ply)) {          // if the user ply equals this computed
		return true;                      // ply, it seems to be valid.
	    }
	}

	// The computer could not compute the ply, the user has made,
	// so we assume, that it is not valid.
	System.out.println( "Invalid move " + ply.toString());
	System.out.println( "Piecetype on source square " + ( getBoard().getPiece( ply.getSource()) == null ? "null" : "" + getBoard().getPiece( ply.getSource()).getType()));
	System.out.println( "Valid moves are:");
	for( int p = 0; p < plies.length; p++) {  // For each ply
	    System.out.print( plies[p].toString() + " ");
	}
	System.out.println();
	return false;
    }

    /**
     * Return a menu from the chess engine, where the user
     * can change the settings.
     *
     * @return A menu for the engine settings.
     */
    public final JMenu getMenu() {

	// Create a new menu.
	JMenu engineMenu = new JMenu( "Engine");

	// Add a menu for the maximum search time
	JMenu searchTimeMenu = new JMenu( "Search time");

	// Add various options for the search time
	// (maybe a user defined search time should be added, too).
	_searchTimeMenuItem = new JMenuItem[ _searchTime.length];
	for( int st = 0; st < _searchTime.length; st++) {
	    _searchTimeMenuItem[st] = new JMenuItem( "" + _searchTime[st] + " seconds");
	    _searchTimeMenuItem[st].addActionListener( this);

	    // Add the current search time menu item to it's menu.
	    searchTimeMenu.add( _searchTimeMenuItem[st]);
	}

	// Add the search time menu to the main engine menu.
	engineMenu.add( searchTimeMenu);

	// Add a menu for the hashtable size.
	JMenu hashtableSizeMenu = new JMenu( "Hashtable size");

	// Add various options for the hashtable size.
	_hashtableSizeMenuItem = new JMenuItem[ _hashtableSize.length];
	for( int hts = 0; hts < _hashtableSize.length; hts++) {
	    _hashtableSizeMenuItem[hts] = new JMenuItem( "" + _hashtableSize[hts] + " entries");
	    _hashtableSizeMenuItem[hts].addActionListener( this);

	    // Add the current search time menu item to it's menu.
	    hashtableSizeMenu.add( _hashtableSizeMenuItem[hts]);
	}

	// Add the search time menu to the main engine menu.
	engineMenu.add( hashtableSizeMenu);

	// Return the engine menu.
	return engineMenu;
    }

    /**
     * Perform a action (could be a menu related action).
     *
     * @param actionEvent The event.
     */
    public final void actionPerformed(ActionEvent actionEvent) {

	// Check if the user has requested a new search time
	for( int st = 0; st < _searchTime.length; st++) {
	    if( actionEvent.getSource().equals( _searchTimeMenuItem[ st])) {
		setMaximumSearchTime( _searchTime[ st] * 1000);
		break;
	    }
	}

	// Check, if the user has requested a different hashtable size.
	for( int hts = 0; hts < _hashtableSize.length; hts++) {
	    if( actionEvent.getSource().equals( _hashtableSizeMenuItem[ hts])) {
		getHashtable().setMaximumSize( _hashtableSize[ hts]);
		break;
	    }
	}
    }

    /**
     * Sets the EnginePanel to be able to output in the panel and not only with
     * System.out.println(...)
     *
     * @param panel The EnginePanel to set
     */
    public void setEnginePanel(EnginePanel panel) {
	this.enginePanel = panel;
    }

    /**
     * Get the current game state.
     *
     * @param white True, if the state of the white player is requested.
     *
     * @return The current game state.
     */
    public final byte getCurrentGameState( boolean white) {
	
	// Test if the given player is in check.
	boolean inCheck = _analyzer.isInCheck( (BitBoard)getBoard(), white);

	// Test if the player has valid plies available.
	// The following computation of the available plies is not really efficient,
	// since they are done anyway (either to compute the next computer ply or to
	// check if a user ply is valid). So maybe the plygenerator or the engine should
	// cache the computed plies.
	boolean validPliesAvailable = ( _plyGenerator.getPliesForColor( (BitBoard)getBoard(), white).length > 0);

	if( inCheck) {
	    return validPliesAvailable ? GameState.CHECK : GameState.CHECKMATE;
	} else {
	    return validPliesAvailable ? GameState.ONGOING : GameState.DRAW;
	}
    }
}

