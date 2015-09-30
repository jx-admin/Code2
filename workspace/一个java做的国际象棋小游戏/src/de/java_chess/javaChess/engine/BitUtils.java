/*
  BitUtils - A class with utility methods to manipulate bitmasks.

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

package de.java_chess.javaChess.engine;


/**
 * This class provides utility methods to manipulate bitmasks.
 */
public class BitUtils {

    /**
     * The highest set bit for all byte values.
     */
    private static int [] _highestBit = { -1,0,1,1,2,2,2,2,3,3,3,3,3,3,3,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,
					  5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,
					  6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
					  6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,6,
					  7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
					  7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
					  7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,
					  7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7 };

    /**
     * Find the index of highest set bit in a long bitmask.
     *
     * @param bitmask The bitmask to operate on.
     *
     * @return The index of the highest set bit or 0, if no bits are set.
     */
    public static final int getHighestBit( long bitmask) {
	int highestBit = 0;

	// The following 3 statements split the 64 bit word down to
	// a byte.

	int dwordPart = (int)(bitmask >> 32);

	if( dwordPart != 0) {
	    highestBit += 32;
	} else {
	    dwordPart = (int)bitmask;
	}
	
	int wordPart = (dwordPart >> 16) & 0xFFFF;
	
	if( wordPart != 0) {
	    highestBit += 16;
	} else {
	    wordPart = dwordPart & 0xFFFF;
	}
	
	int bytePart = (wordPart >> 8) & 0xFF;
	
	if( bytePart != 0) {
	    highestBit += 8;
	} else {
	    bytePart = wordPart & 0xFF;
	}

	// Use the byte as a index for a precomputed array
	// of bit indexes.
	return highestBit + _highestBit[ bytePart];
    }
}
