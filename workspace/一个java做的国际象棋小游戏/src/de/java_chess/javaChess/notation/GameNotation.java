/*
  GameNotation - This interace defines the functionality to notate a 
                 entire game.

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

package de.java_chess.javaChess.notation;


/**
 * This interface defines the functionality to notate a entire game.
 */
public interface GameNotation {


    // Methods

    /**
     * Get the notation for a given move and piece color.
     *
     * @param moveIndex The index of the move.
     * @param boolean white Flag to indicate if the color is white.
     *
     * @return The notation for a given move and color.
     */
    String getMove( int moveIndex, boolean white);

    /**
     * Add a new ply with it's notation.
     *
     * @param plyNotation The notation of the new ply.
     */
    public void addPly( PlyNotation plyNotation);

    /**
     * Get the entire game as a string.
     *
     * @return The entire game as a string.
     */
    String toString();

    /**
     * Get some info on a player.
     *
     * @return Some info on a player.
     */
    String getPlayerInfo( boolean white);

    /**
     * Set the info on a player.
     *
     * @param playerInfo The player info.
     * @param white Flag to indicate, if it's the player 
     *              with the white pieces.
     */
    void setPlayerInfo( String playerInfo, boolean white);
}
