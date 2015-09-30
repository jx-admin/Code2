/*
  TransformationPlyImpl - A class for pawn plies that reach the last
                          row and transform into a new piece type.

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
 * This class is for pawn plies, that reach the last row
 * and transform into a new piece type.
 */
public class TransformationPlyImpl extends PlyImpl implements TransformationPly {


    // Instance variables.

    /**
     * The new piece type after the transformation.
     */
    byte _newPieceType;

    
    // Constructors

    /**
     * Create a new transformation ply instance.
     *
     * @param source The source square.
     * @param destination The destination square.
     * @param pieceType The piece type after the transformation.
     */
    public TransformationPlyImpl( Position source, Position destination, byte pieceType) {
	super( source, destination);
	setTypeAfterTransformation( pieceType);
    }


    // Methods

    /**
     * Get the new type of the piece after the transformation.
     *
     * @return The new piece after the transformation.
     */
    public final byte getTypeAfterTransformation() {
	return _newPieceType;
    }

    /**
     * Set the new piece type after the transformation.
     *
     * @param pieceType The new piece type after the transformation.
     */
    public final void setTypeAfterTransformation( byte pieceType) {
	_newPieceType = pieceType;
    }
}
