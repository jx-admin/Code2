/*
  AnalyzedPlyImpl - A utility class to store and pass a ply along with it's 
                    evaluated score.

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

package de.java_chess.javaChess.ply;

import de.java_chess.javaChess.*;


/**
 * Utility class for plies and their scores.
 */
public class AnalyzedPlyImpl implements AnalyzedPly {


    // Instance variables

    /**
     * The analyzed ply.
     */
    private Ply _ply;

    /**
     * The result of the analysis.
     */
    private short _score;


    // Constructors

    /**
     * Create a new AnalyzedPly instance.
     */
    public AnalyzedPlyImpl( Ply ply, short score) {
	setPly( ply);
	setScore( score);
    }

    
    // Methods

    /**
     * Get the analyzed ply.
     *
     * @return The analyzed ply.
     */
    public final Ply getPly() {
	return _ply;
    }
    
    /**
     * Set a new ply.
     *
     * @param ply The new ply.
     */
    public final void setPly( Ply ply) {
	_ply = ply;
    }

    /**
     * Get the scrore of this ply.
     *
     * @return The score of this ply.
     */
    public final short getScore() {
	return _score;
    }

    /**
     * Set a new scrore for this ply.
     *
     * @param score The new score for this ply.
     */
    public final void setScore( short score) {
	_score = score;
    }

    /**
     * Clone this ply.
     *
     * @return A clone of this ply.
     */
    public Object clone() {
	return new AnalyzedPlyImpl( getPly(), getScore());
    }
}
