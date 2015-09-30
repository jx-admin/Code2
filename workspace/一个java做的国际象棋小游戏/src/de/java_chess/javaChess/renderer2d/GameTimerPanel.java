/*
  GameTimerPanel - A component to display a game timer.

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

import de.java_chess.javaChess.timer.*;
import java.awt.*;
import javax.swing.*;


/**
 * A timer for a chess game.
 */
public class GameTimerPanel extends JPanel implements GameTimer
{
    // Instance variables

    /**
     * A timer for the player with the white pieces.
     */
    CountdownTimerPanel _white;

    /**
     * A timer for the player with the black pieces.black
     */
    CountdownTimerPanel _black;

    /**
     * A flag to indicate, if the white timer is current active.
     */
    boolean _whiteActive;

		private JLabel jlWhite = new JLabel();
		private JLabel jlBlack = new JLabel();

    /**
     * A flag to indicate if the timer is running.
     */
    boolean _running;


    // Constructors

    /**
     * Create a new timer instance for a game.
     */
    public GameTimerPanel() {
	super();

	try {
	    jbInit();
	} catch(Exception e)
			{
				e.printStackTrace();
			}
		}

/**
 * Construction a la JBuilder to be able to use JBuilder designer
 *
 * @throws Exception
 */
    private void jbInit() throws Exception {
	    _white = new CountdownTimerPanel();
	    _black = new CountdownTimerPanel();

	    _white.setBackground( Color.white );
      _white.alignText();
	    _black.setBackground( Color.white );
      _black.alignText();

	    _white.setBorder(BorderFactory.createLineBorder(Color.black));
	    _black.setBorder(BorderFactory.createLineBorder(Color.black));

	    this.jlWhite.setText( "White: " );
	    this.jlWhite.setForeground( Color.white );
	    jlWhite.setHorizontalAlignment(SwingConstants.CENTER);

	    this.jlBlack.setText( "Black: " );
	    this.jlBlack.setForeground( Color.black );
	    jlBlack.setHorizontalAlignment(SwingConstants.CENTER);

	    // Layout the entire timer.
	    setLayout( new GridLayout( 1, 4));

	    // Add the timers with a label for each of them
	    this.add( jlWhite, null );
	    add( _white);
	    this.add( jlBlack, null );
	    add( _black);
    }

    /**
     * Create a given game timer with a countdown time.
     *
     * @param maxTime The remaining time.
     */
    public GameTimerPanel( int maxTime) {
	this();
	setCountdown( maxTime);
    }


    // Methods

    /**
     * Set the remaining time for each timer.
     *
     * @param time The remaining time for each time.
     */
    public void setCountdown( int time) {
	_white.setCountdown( time);
	_black.setCountdown( time);
    }

    /**
     * Start the game
     */
    public void start() {
	_black.start();
	_whiteActive = false;
  setBlackClockRunning();
	setRunning( true);
    }

    /**
     * Toggle the timer for one player to the other.
     */
    public void toggle() {
	if( _whiteActive) {
            setBlackClockRunning();
//            setWhiteClockRunning();
	    _white.stop();
	    _black.start();
	} else {
            setWhiteClockRunning();
//            setBlackClockRunning();
	    _black.stop();
	    _white.start();
	}
	_whiteActive = !_whiteActive;
    }

    /**
     * Stop the entire timer.
     */
    public void stop() {
	_white.stop();
	_black.stop();
	setRunning( false);
    }

    /**
     * Get the active state of this timer.
     *
     * @return The active state of this timer.
     */
    public boolean isRunning() {
	return _running;
    }

    /**
     * Set the new running state of the timer.
     *
     * @param active The new state of the timer panel.
     */
    public void setRunning( boolean active) {
	_running = active;
    }

    public void setWhiteClockRunning()
    {
      _white.setBackground(Color.white);
      _black.setBackground(Color.lightGray);
    }

    public void setBlackClockRunning()
    {
      _white.setBackground(Color.lightGray);
      _black.setBackground(Color.white);
    }

}
