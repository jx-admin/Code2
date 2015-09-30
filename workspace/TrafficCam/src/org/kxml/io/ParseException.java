package org.kxml.io;

import java.io.*;

//   FIXME: would adding a file name make sense ?? 

/** a possibly chained exception, indicatin a line and column number. */

public class ParseException extends IOException {

    protected int lineNumber = -1;
    protected int columnNumber = -1;
    protected Exception chained; 


    /** Builds a new ParseException with the given message text, 
	chained Exception, lineNumber, columNumber. Set message text
	or chained exception to null and lineNumber and
	columNumber to -1 if not applicable. */


    public ParseException (String msg, Exception chained, 
			   int lineNumber, int columnNumber) {
    
	super ((msg == null ? "ParseException" : msg) 
	       + " @"+lineNumber+":" +columnNumber);

	this.chained = chained;
 	this.lineNumber = lineNumber;
	this.columnNumber = columnNumber;
    }

    
    /* Removed since super is not available in MIDP :( 

       prints the own
       stack trace followed by the stack trace of the original
       exception to the given PrintStream

    public void printStackTrace (PrintStream p) {
	super.printStackTrace (p);
	if (chained != null) 
	    chained.printStackTrace (p);
    }


    /** prints the own stack trace followed by the stack trace of the
	original exception to the given PrintWriter 

    public void printStackTrace (PrintWriter p) {
	super.printStackTrace (p);
	if (chained != null)
	    chained.printStackTrace (p);
	    } */

    /** prints the own stack trace followed by the stack trace of the
	original exception. */

    public void printStackTrace () {
	super.printStackTrace ();
	if (chained != null) 
	    chained.printStackTrace ();
    }


    public int getLineNumber () {
	return lineNumber;
    }

}

