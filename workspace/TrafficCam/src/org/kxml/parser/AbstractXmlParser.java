/* kXML
 *
 * The contents of this file are subject to the Enhydra Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License
 * on the Enhydra web site ( http://www.enhydra.org/ ).
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
 * the License for the specific terms governing rights and limitations
 * under the License.
 *
 * The Initial Developer of kXML is Stefan Haustein. Copyright (C)
 * 2000, 2001 Stefan Haustein, D-46045 Oberhausen (Rhld.),
 * Germany. All Rights Reserved.
 *
 * Contributor(s): Paul Palaszewski, Wilhelm Fitzpatrick, 
 *                 Eric Foster-Johnson, Daniel Feygin,  Scott Daub
 *
 * */

package org.kxml.parser;

import java.io.IOException;
import java.util.*;

import org.kxml.*;
import org.kxml.io.*;

/** An abstract base class for the XML and WBXML parsers. Of course,
    you can implement your own subclass with additional features,
    e.g. a validating parser.  */


public abstract class AbstractXmlParser {

    
    protected boolean processNamespaces = true;


    /** Ignores a tree */

    public void ignoreTree () throws IOException {
	readTree (null);
    }

    /** Reads a complete element tree to the given event
	Vector. The next event must be a start tag. */


    public void readTree (Vector buf) throws IOException { 


	StartTag start = (StartTag) read ();
	//if (buf != null) buf.addElement (start); [caused duplication, fixed by SD]

	while (true) {
	    ParseEvent event = peek ();
	    if (buf != null) buf.addElement (event);

	    switch (event.getType ()) {

	    case Xml.START_TAG:
		readTree (buf);
		break;

	    case Xml.END_TAG:
	    case Xml.END_DOCUMENT:
		read ();
		return;

	    default: 
		read ();
	    }
	}	
    }


    /** Returns the current line number; -1 if unknown. Convenience
        method for peek ().getLineNumber (). */

    public int getLineNumber () throws IOException {
	return peek ().getLineNumber ();
    }
	

    /** reads the next event available from the parser. If the end of
       the parsed stream has been reached, an event of type
       END_DOCUMENT is returned.  */

    public abstract ParseEvent read () throws IOException;




    /** Reads an event of the given type. If the type is START_TAG or
	END_TAG, namespace and name are tested, otherwise
	ignored. Throws a ParseException if the actual event does not
	match the given parameters. */

    public ParseEvent read (int type, String namespace, 
			    String name) throws IOException {

	if (peek (type, namespace, name)) 
	    return read ();
	else throw new ParseException 
	    ("unexpected: "+peek (), null,  
	     peek().getLineNumber (), -1);
    }


    public boolean peek (int type, String namespace, 
			 String name) throws IOException {
	 
	ParseEvent pe = peek ();
	
	return pe.getType () == type 
	    && (namespace == null || namespace.equals (pe.getNamespace ()))
	    && (name == null || name.equals (pe.getName ()));
    }


    /** Convenience Method for skip (Xml.COMMENT | Xml.DOCTYPE 
	| Xml.PROCESSING_INSTRUCTION | Xml.WHITESPACE) */

    public void skip () throws IOException { 
	while (true) {
	    int type = peek ().type;
	    if (type != Xml.COMMENT 
		&& type != Xml.DOCTYPE 
		&& type != Xml.PROCESSING_INSTRUCTION 
		&& type != Xml.WHITESPACE) break;
	    read ();
	}
    } 


    /** reads the next event available from the parser
	without consuming it */

    public abstract ParseEvent peek () throws IOException;



    /** tells the parser if it shall resolve namespace prefixes to
	namespaces. Default is true */

    public void setProcessNamespaces (boolean processNamespaces) {
	this.processNamespaces = processNamespaces;
    }



    
    /** Convenience method for reading the content of text-only 
        elements. The method reads text until an end tag is
        reached. Processing instructions and comments are
	skipped. The end tag is NOT consumed.  The concatenated text
        String is returned. If the method reaches a start tag, an
        Exception is thrown. */
    

    public String readText () throws IOException {
	
	StringBuffer buf = new StringBuffer ();

	while (true) {

	    ParseEvent event = peek ();

	    switch (event.getType ()) {

	    case Xml.START_TAG:
	    case Xml.END_DOCUMENT:
	    case Xml.DOCTYPE:
		throw new RuntimeException 
		    ("Illegal event: "+event);

	    case Xml.WHITESPACE:
	    case Xml.TEXT:
		read ();
		buf.append (event.getText ());
		break;

	    case Xml.END_TAG:
		return buf.toString ();

	    default:
		read ();
	    }
	}	
    }
}

