package org.kxml.kdom;

import java.util.*;
import java.io.*;


import org.kxml.*;
import org.kxml.io.*;
import org.kxml.parser.*;


/** The document consists of some legacy events and a single root
    element. This class basically adds some consistency checks to
    Node. */

public class Document extends Node {

    protected int rootIndex = -1;
 
    /** returns "#document" */

    public String getName () {
	return "#document";
    }


    /** Adds a child at the given index position. Throws
	an exception when a second root element is added */ 

    public void addChild (int index, int type, Object child) {
	if (type == Xml.ELEMENT) {
	    if (rootIndex != -1) 
		throw new RuntimeException 
		    ("Only one document root element allowed"); 

	    rootIndex = index;
	}
	else if (rootIndex >= index) rootIndex++;

	super.addChild (index, type, child);
    }


    /** reads the document and checks if the last event
	is END_DOCUMENT. If not, an exception is thrown.
	The end event is consumed. For parsing partial
        XML structures, consider using Node.parse (). */
	

    public void parse (AbstractXmlParser parser) throws IOException {
	super.parse (parser);
	if (parser.read ().getType () != Xml.END_DOCUMENT)
	    throw new RuntimeException ("Document end expected!");

    }


 
    public void removeChild (int index) {
	if (index == rootIndex) rootIndex = -1;
	else if (index < rootIndex) rootIndex--;

	super.removeChild (index);
    }

    
    /** returns the root element of this document. */

    public Element getRootElement () {
	if (rootIndex == -1) 
	    throw new RuntimeException ("Document has no root element!");

	return (Element) getChild (rootIndex);
    }
}
