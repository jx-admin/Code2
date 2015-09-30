/*
  AnalyzedPly - A interface for analyzed plies and their scores.
                
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


/**
 * Utility class for plies and their scores.
 */
public interface AnalyzedPly {


    // Static class variables

    /**
     * The maximum score.
     */
    short MAX_SCORE = Short.MAX_VALUE;

    /**
     * The minimum score.
     */
    short MIN_SCORE = Short.MIN_VALUE;

    
    // Methods

    /**
     * Get the analyzed ply.
     *
     * @return The analyzed ply.
     */
    Ply getPly();
    
    /**
     * Set a new ply.
     *
     * @param ply The new ply.
     */
    void setPly( Ply ply);

    /**
     * Get the scrore of this ply.
     *
     * @return The score of this ply.
     */
    short getScore();

    /**
     * Set a new scrore for this ply.
     *
     * @param score The new score for this ply.
     */
    void setScore( short score);

    /**
     * Clone this ply.
     *
     * @return A clone of this ply.
     */
    Object clone();
}
