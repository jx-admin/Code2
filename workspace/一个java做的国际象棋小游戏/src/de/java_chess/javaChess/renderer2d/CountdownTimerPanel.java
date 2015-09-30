/*
  CountdownTimerPanel - A component to display a countdown timer.

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
import java.awt.event.*;
import javax.swing.*;


/**
 * This class implements the functionality to display a timer
 */
public class CountdownTimerPanel extends JPanel implements Runnable {

    // Static variables

    /**
     * The width of the display in pixels.
     */
    static final int _displayWidth = 70;

    /**
     * The height if of the display in pixels.
     */
    static final int _displayHeight = 16;


    // Instance variables

    /**
     * The timer thread.
     */
    Thread _timerThread;

    /**
     * A label to display the time.
     */
    JLabel _display;

    /**
     * The time left in milliseconds.
     */
    long _time;

    /**
     * The start time of the thread.
     */
    long _startTime;


    // Constructors

    /**
     * Create a new countdown timer panel.
     */
    public CountdownTimerPanel() {
	_startTime = -1L;
	_display = new JLabel("0:00:00", JLabel.RIGHT);
	_display.setPreferredSize( new Dimension( _displayWidth, _displayHeight));
        _display.setHorizontalTextPosition(SwingConstants.CENTER);
	add( _display);
    }


    // Methods

    /**
     * Set the length of the countdown in seconds.
     *
     * param time The length of the countdown.
     */
    public void setCountdown( int time) {
	_time = 1000L * (long)time;
	display( _time);
    }

    /**
     * Start the timer.
     */
    public void start() {
	if( _startTime == -1L) {
	    _startTime = System.currentTimeMillis();
	}
	if( _timerThread == null) {
            _timerThread = new Thread( this);
            _timerThread.start();
        }
    }

    /**
     * Stop the timer.
     */
    public void stop() {
	_timerThread = null;
    }

    /**
     * The actual thread method.
     */
    public void run() {

	// While there's still time left and noone stopped the thread.
	long remainingTime;
	try {
	    do {
		Thread.sleep( 100);  // Wait 1/10 of a second.
		remainingTime = _time - ( System.currentTimeMillis() - _startTime);
		display( remainingTime);  // decrease the time by a second and display it.
	    } while( ( remainingTime >= 0) && (Thread.currentThread() == _timerThread));
	} catch( InterruptedException ignored) {}
    }

    /**
     * Display the current time.
     *
     * @param time The current time in milliseconds.
     */
    void display( long time) {
	int timeSec = (int)( time / 1000L);  // Display accurate to  1 s.
	StringBuffer timeString = new StringBuffer();
	int hours = timeSec / 3600;
	timeString.append( "" + hours);
	int minutes = ( timeSec / 60) % 60;
	timeString.append( ( ( minutes < 10) ?  ":0" : ":") + minutes);
	int seconds = timeSec % 60;
	timeString.append( ( ( seconds < 10) ? ":0" : ":") + seconds);
	_display.setText( timeString.toString());
	_display.paintImmediately( 0, 0, _displayWidth, _displayHeight);
    }

    /**
     * Get the remaining time in seconds.
     *
     * @return The remaining time in seconds.
     */
    public int getRemainingTime() {
	return (int)( _time / 1000L);
    }

    public void alignText()
    {
      this._display.setHorizontalTextPosition(SwingConstants.CENTER);
      this._display.setHorizontalAlignment(SwingConstants.CENTER);
    }
}
