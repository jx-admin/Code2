/*
  EnPassantPlyImpl - A class to implement a en passant attack.

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

import de.java_chess.javaChess.position.*;


/**
 * This class is for pawn plies, that attack a pawn
 * en passant.
 */
public class EnPassantPlyImpl extends PlyImpl implements EnPassantPly {

    // Instance variables

    /**
     * The position of the attacked pawn.
     */
    Position _attackedPosition;

    
    // Constructors

    /**
     * Create a new en passant ply instance.
     *
     * @param source The source square of the attacking pawn.
     * @param destination The destination square of the attacking pawn.
     * @param attackedPosition The position of the attacked pawn.
     */
    public EnPassantPlyImpl( Position source, Position destination, Position attackedPosition) {
	super( source, destination);
	setAttackedPosition( attackedPosition);
    }


    // Methods

    /**
     * Get the position of the attacked pawn.
     *
     * @return Get the position of the attacked pawn.
     */
    public final Position getAttackedPosition() {
	return _attackedPosition;
    }

    /**
     * Set the position of the attacked pawn.
     *
     * @param position The position of the attacked pawn.
     */
    public final void setAttackedPosition( Position attackedPosition) {
	_attackedPosition = attackedPosition;
    }
}     
