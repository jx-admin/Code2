/* kSOAP
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
 * The Initial Developer of kSOAP is Stefan Haustein. Copyright (C)
 * 2000, 2001 Stefan Haustein, D-46045 Oberhausen (Rhld.),
 * Germany. All Rights Reserved.
 *
 * Contributor(s): John D. Beatty, F. Hunter, Renaud Tognelli
 *
 * */
 

package org.ksoap;

import java.io.*;

import org.kxml.*;
import org.kxml.io.*;
import org.kxml.parser.*;
import org.kobjects.serialization.*;


/** The SOAP envelope. */


public class SoapEnvelope {
	
    Object body;
    ClassMap classMap;
    String encodingStyle; 
    
    /** deprecated */

    public SoapEnvelope () {
	this (new ClassMap ());
    }


    public SoapEnvelope (ClassMap classMap) {
	this.classMap = classMap;
    }


    /** Returns the body object of the envelope.  */
       

    public Object getBody () {
	return body;
    } 


    /** Returns the first property of the body object */

    public Object getResult () {
		System.out.println("Enter SoapEnvelope::getResult");
		
	KvmSerializable ks = (KvmSerializable) body;
	return ks.getPropertyCount () == 0 ? null : ks.getProperty (0);
    }


    /** Parses the SOAP envelope from the given parser */
    

    public void parse (AbstractXmlParser parser) throws IOException {

		System.out.println("Enter SoapEnvelope::parse");
	parseHead (parser);
	parseBody (parser);
	parseTail (parser);
    }


    public void parseHead (AbstractXmlParser parser) throws IOException {
	parser.skip ();
	StartTag tag = (StartTag) parser.read (Xml.START_TAG, classMap.env, 
					       "Envelope");
	Attribute attr = tag.getAttribute (classMap.env, "encodingStyle");
	if (attr != null) encodingStyle = attr.getValue ();

	parser.skip ();

	if (parser.peek (Xml.START_TAG, classMap.env, "Header")) {
	    // consume start header
	    
	    parser.read ();
	    parser.skip ();
	    
	    // look at all header entries
	    while (parser.peek().getType( ) != Xml.END_TAG) {
		attr = parser.peek ().getAttribute 
		    (classMap.env, "mustUnderstand");
		
		if (attr != null && attr.getValue ().equals ("1"))
		    throw new RuntimeException ("mU not supported");
		
		parser.ignoreTree( );
		parser.skip( );
	    }

	    parser.read (Xml.END_TAG, classMap.env, "Header");
	}
	
	parser.skip ();
	tag = (StartTag) parser.read (Xml.START_TAG, classMap.env, "Body");
	attr = tag.getAttribute (classMap.env, "encodingStyle");
	if (attr != null) encodingStyle = attr.getValue ();
    }


    public void parseBody (AbstractXmlParser parser) throws IOException {
		System.out.println("Enter SoapEnvelope::parseBody");
	parser.skip ();
	// insert fault generation code here

	if (parser.peek (Xml.START_TAG, classMap.env, "Fault")) {
		
		System.out.println("parseBody>>Fault");
		
	    SoapFault fault = new SoapFault ();
	    fault.parse (parser);
	    body = fault;
		
		System.out.println("SoapFault>>faultactor=" +
				fault.faultactor + "\n<>faultcode="
				+ fault.faultcode + "\n<>faultstring="
				+ fault.faultstring);
	}
	else if (body != null && body instanceof XmlIO) 
	    ((XmlIO) body).parse (parser);
	else
	    body = new SoapParser 
		(parser, classMap).read ();
    }



    public void parseTail (AbstractXmlParser parser) throws IOException {

	parser.skip ();
	parser.read (Xml.END_TAG, classMap.env, "Body");

	parser.skip ();
	parser.read (Xml.END_TAG, classMap.env, "Envelope");
    }
    
    
    /** Sets the encoding style. */
    
    public void setEncodingStyle (String encodingStyle) {
	this.encodingStyle = encodingStyle;
    }

    
    /** Writes the envelope and body to the given XML writer. */ 

    public void write (AbstractXmlWriter writer) throws IOException {
	writeHead (writer);
	writeBody (writer);
	writeTail (writer);
    }


    /** Writes the head including the encoding style attribute and the 
	body start tag */


    public void writeHead (AbstractXmlWriter writer) throws IOException {

	writer.startTag (classMap.prefixMap, classMap.env, "Envelope");
	//	writer.attribute (Soap.ENV, "encodingStyle", encodingStyle); 
	writer.startTag (classMap.env, "Body");
    }



    /** Overwrite this method for customized writing of
	the soap message body. */


    public void writeBody (AbstractXmlWriter writer) throws IOException {
	
	if (body instanceof XmlIO) {
	    if (encodingStyle != null)	 
		writer.attribute 
		    (classMap.env, "encodingStyle", encodingStyle);

	    ((XmlIO) body).write (writer);
	}
	else {
	    writer.attribute
		(classMap.env, "encodingStyle",
		 encodingStyle == null ? classMap.enc : encodingStyle);
	    
	    new SoapWriter (writer, classMap).write (body);
	}
    }


    public void writeTail (AbstractXmlWriter writer) throws IOException {
	writer.endTag ();
	writer.endTag ();
    }	



    public void setBody (Object body) {
	this.body = body;
    }


    public void setClassMap (ClassMap classMap) {
	this.classMap = classMap;
    }
    
}


