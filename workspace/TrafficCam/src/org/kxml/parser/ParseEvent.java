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
 *                 Eric Foster-Johnson
 *
 * */

package org.kxml.parser;

import java.util.*;
import java.io.IOException;

import org.kxml.*;


/** Abstract superclass for all pull parser events.  In order to avoid
    some typecasts, this class already provides most of the content
    access methods filled in the specialized subclasses.  */
 

public class ParseEvent {

    static final String WRONG_TYPE = 
	"Method not supported for the given event type!";


    int lineNumber = -1;
    int type;
    String text;

    public ParseEvent (int type, String text) {
	this.type = type;
	this.text = text;
    }
 

    /** returns the line number of the event */

    public int getLineNumber () {
	return lineNumber;
    }

    /** returns the event type integer constant assigned to this
        event.  Possible event types are Xml.START_TAG, Xml.END_TAG,
        Xml.TEXT, Xml.WHITESPACE, Xml.PROCESSING_INSTRUCTION, Xml.COMMENT,
        Xml.DOCTYPE, and Xml.END_DOCUMENT. Please refer to the Xml class
        description for more details. */
	
    public int getType () {
	return type;
    }

    
    /** sets the line number of the event. Used by the parser only. */

    public void setLineNumber (int lineNumber) {
	this.lineNumber = lineNumber;
    }


   /** In the event type is START_TAG, this method returns the attribute 
       at the given index position. For all other event
       types, or if the index is out of range, an exception is thrown. */


    public Attribute getAttribute (int index) {
	return (Attribute) getAttributes().elementAt (index);
    }
    
    /** returns the local attribute with the given name.  convenience
	method for getAttribute (Xml.NO_NAMESPACE, name); */

    public Attribute getAttribute (String name) {
	return getAttribute (Xml.NO_NAMESPACE, name);
    }


    /** returns the local attribute with the given qualified name.
        Please use null as placeholder for any namespace or
        Xml.NO_NAMESPACE for no namespace. */

    public Attribute getAttribute (String namespace, String name) {

	Vector attributes = getAttributes ();
	int len = getAttributeCount ();

	for (int i = 0; i < len; i++) {
	    Attribute attr = (Attribute) attributes.elementAt (i);
	    
	    if (attr.getName ().equals (name) 
		&& (namespace == null || namespace.equals (attr.getNamespace ())))
		
		return attr;  
	}

	return null;
    }


    /** If the event type is START_TAG, the number of attributes is
	returned. For all other event types, an exception is thrown. */

    public int getAttributeCount () {
	Vector a = getAttributes ();
	return a == null ? 0 : a.size ();
    }


    /** If the event type is START_TAG, the attribute Vector (null if
	no attributes) is returned. For all other event types, an
	exception is thrown. */

    public Vector getAttributes () {
	throw new RuntimeException (WRONG_TYPE);
    }

    /** returns the (local) name of the element started if
        instance of StartTag, null otherwise. */

    public String getName () {
	return null;
    }


    /** returns namespace if instance of StartTag, null
        otherwise. */ 

    public String getNamespace () {
	return null;
    }


    /** Returns the value of the attribute with the given name.
	Throws an exception if not instanceof StartTag or if not
	existing. In order to get a null value for not existing
	attributes, please call getValueDefault (attrName, null)
	instead. */

    public String getValue (String attrName) {
	Attribute attr = getAttribute (attrName);
	if (attr == null) throw new RuntimeException 
	    ("Attribute "+ attrName + " in " + this + " expected!");
	return attr.getValue ();
    }


    /** Returns the given attribute value, or the given default value
	if the attribute is not existing. */

    public String getValueDefault (String attrName, String deflt) {
	Attribute attr = getAttribute (attrName);
	return attr == null ? deflt : attr.getValue ();
    }

 
    /** If the event type is TEXT, PROCESSING_INSTRUCTION,
	or DOCTYPE, the corresponding string is returned. For
	all othe event types, null is returned. */

    public String getText () {
	return text;
    }



    public String toString () {
	return "ParseEvent type="+type+ " text='"+text+"'";
    }




}

