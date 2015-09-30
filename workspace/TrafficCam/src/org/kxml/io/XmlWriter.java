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
 *                 Eric Foster-Johnson, Hans-Harald Schulz
 *
 * */


package org.kxml.io;

import java.io.*;
import java.util.*;

import org.kxml.*;


/** a concrete XML Writer */

public class XmlWriter extends AbstractXmlWriter {

    protected Writer writer;
    boolean pending = false;
    int indentLevel = 0;
    int noIndent = Integer.MAX_VALUE;


    static char [] indent
	= {'\r', '\n', ' ', ' ', ' ', ' ', ' ', ' ',
	   ' ', ' ', ' ', ' ', ' ', ' ',  ' ', ' '};


    /** creates a new xmlWritet based on the given writer */

    public XmlWriter (Writer writer) {
	this.writer = writer;
    }


    protected void checkPending () throws IOException {
	if (pending) {
	    writer.write ('>');
	    pending = false;
	}
    }


    /** closes the XmlWriter by closing the underlying writer */

    public void close () throws IOException {
	flush ();
	writer.close ();
    }


    /** flushes the XmlWriter. Attention: If a closing
	angle braket is pending, it will be appended before
	flushing the underlying writer. Thus, after flush
	attributes cannot be added any longer */

    public void flush () throws IOException {
	checkPending ();
	writer.flush ();
    }


    /** writes a single character using the xml escaping rules */

    public void write (char c) throws IOException {
	checkPending ();
	if (noIndent > indentLevel) noIndent = indentLevel;

	switch (c) {
	case '<': writer.write ("&lt;"); break;
	case '>': writer.write ("&gt;"); break;
	case '&': writer.write ("&amp;"); break;
	default: writer.write (c);
	}
    }




    /** writes an character array using the XML escaping rules */

    public void write (char [] buf, int start, int len) throws IOException {
	checkPending ();
	if (noIndent > indentLevel) noIndent = indentLevel;

	int end = start + len;

	do {
	    int i = start;

	    while (i < end && "<>&".indexOf (buf [i]) == -1) i++;

	    writer.write (buf, start, i - start);

	    if (i == end) break;

	    write (buf [i]);

	    start = i+1;
	}
	while (start < end);
    }

    /** writes an character array using the XML escaping rules */

    public void writeCData (String buf) throws IOException {
        checkPending ();
        if (noIndent > indentLevel) noIndent = indentLevel;

        writer.write("<![CDATA[");
        writer.write(buf);
        writer.write("]]>");

    }

    public void writeIndent () throws IOException {

	int l = indentLevel + 2;

	if (l < 2) l = 2;
	else if (l > indent.length) l = indent.length;

	checkPending ();
	//	writer.write ("<!-- i -->");
	writer.write (indent, 0, l);
    }


    /** writes a degenerated tag with the given name and attributes */

    public void attribute (String name,
			   String value) throws IOException {

	if (!pending) throw new RuntimeException
	    ("can write attr only immediately after a startTag");

	writer.write (' ');
	writer.write (name);
	writer.write ("=\"");
	writer.write (Xml.encode (value, Xml.ENCODE_QUOT));
	writer.write ('"');

	if (name.equals ("xml:space") && value.equals ("preserve"))
	    noIndent = indentLevel;
    }




    /** writes a start tag with the given name */

    protected void startTag (PrefixMap prefixMap,
			     String tag) throws IOException {

	current = new State (current, prefixMap, tag);

	checkPending ();

	if (indentLevel < noIndent)
	    writeIndent ();

	indentLevel++;

	writer.write ('<');
	writer.write (tag);

	pending = true;
    }


    /** writes an end tag. */



    public void endTag () throws IOException {
	indentLevel--;

	if (pending) {
	    writer.write (" />");
	    pending = false;
	}
	else {
	    if (indentLevel + 1 < noIndent)
		writeIndent ();

	    writer.write ("</");
	    writer.write (current.tag);
	    writer.write (">");
	}

	if (indentLevel + 1 == noIndent)
	    noIndent = Integer.MAX_VALUE;

	current = current.prev;
	if (current == null)
	    throw new RuntimeException ("too many closing tags!");
    }


    /** ATTENTION: Application needs to take care about not writing
        illegal character sequences (like comment end in comments) */

    public void writeLegacy (int type, String content) throws IOException {
	checkPending ();
	switch (type) {
	case Xml.COMMENT:
	    writer.write ("<!--");
	    writer.write (content);
	    writer.write ("-->");

	    break;
	case Xml.DOCTYPE:
	    writer.write ("<!DOCTYPE ");
	    writer.write (content);
	    writer.write (">");
	    break;

	case Xml.PROCESSING_INSTRUCTION:
	    writer.write ("<?");
	    writer.write (content);
	    writer.write ("?>");
	    break;
	}
    }


    /** writes a string without escaping. Use with care! Not
	available in wbxml writer */

    public void writeRaw (String s) throws IOException {
	checkPending ();
	writer.write (s);
    }
}
