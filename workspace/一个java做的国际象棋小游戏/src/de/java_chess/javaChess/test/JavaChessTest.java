/*
  JavaChessTest - The main class for the Java chess tests.

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

package de.java_chess.javaChess.test;

import de.java_chess.javaChess.engine.test.*;
import junit.framework.*;


/**
 * This is the main test class for JavaChess.
 */
public class JavaChessTest {

    // Instance variables

    
    // Constructors

    
    // Methods

    /**
     * Create the main test suite.
     *
     * @return The main test suite.
     */
    public static Test suite() {

	// Create a new test suite.
	TestSuite suite = new TestSuite();

	// Add the test suite for the engine.
	suite.addTest( ChessEngineTest.suite());

	return suite;
    }
}


