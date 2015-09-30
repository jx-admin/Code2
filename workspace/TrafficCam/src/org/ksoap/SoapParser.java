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
 * Contributor(s): John D. Beatty, F. Hunter, Renaud Tognelli, Sean McDaniel
 *
 * */

package org.ksoap;

import java.util.*;
import java.io.*;

import org.kxml.*;
import org.kxml.io.*;
import org.kxml.parser.*;
import org.kobjects.serialization.*;


/** A SOAP parser. Limitations:
    <ul>
    <li>Partial arrays are not yet supported</li>
    <li>Multi-Dimensional Arrays are not supported</li>
    <li>The hrefs must be local</li>
    </ul>

      */

public class SoapParser {

    ClassMap classMap;
    Hashtable idMap = new Hashtable ();
    public AbstractXmlParser parser;


    class FwdRef {
	FwdRef next;
	Object obj;
	int index;
    }


    public SoapParser (AbstractXmlParser parser,
		       ClassMap classMap) {

	this.parser = parser;
	this.classMap = classMap;
    }




    /** Extracts namespace and name and calls readBody for actual reading */

    public Object read () throws IOException {

	Object root = null;

	//System.out.println ("start parsing....");


	while (true) {
	    parser.skip ();
	    int type = parser.peek ().getType ();

	    if (type == Xml.END_TAG || type == Xml.END_DOCUMENT)
		break;
	    else if (type != Xml.START_TAG)
		throw new IOException ("Unexpected event: "+parser.peek ());

	    StartTag start = (StartTag) parser.peek ();

	    //	String name = namespaceMap.getPackage (start.getNamespace ())
	    //	+ "." + start.getName ();

	    Object o = read
		(null, -1,
		 start.getNamespace (), start.getName (),
		 ElementType.OBJECT_TYPE);

	    Attribute attr = start.getAttribute (classMap.enc, "root");
	    if ((attr != null && attr.getValue ().equals ("1"))
		|| root == null)
		root = o;
	}

	//System.out.println ("leaving root read");

	return root;
    }




    /** Builds an object from the XML stream. This method
        is public for usage in conjuction with Marshal subclasses. */

    public Object read (Object owner, int index,
			String namespace, String name,
			ElementType expected)

	throws IOException {

	//	System.out.println ("in read");


	StartTag start = (StartTag) parser.peek ();

	// determine wire element type

	String href = start.getValueDefault ("href", null);
	Object obj;

	if (href != null) {
	    if (owner == null)
		throw new RuntimeException ("href at root level?!?");

	    href = href.substring (1);
	    obj = idMap.get (href);

	    if (obj == null || obj instanceof FwdRef) {

		FwdRef f = new FwdRef ();
		f.next = (FwdRef) obj;
		f.obj = owner;
		f.index = index;
		idMap.put (href, f);
		obj = null;
	    }

	    parser.read (); // start tag
	    parser.skip ();
	    parser.read (Xml.END_TAG, null, null);
	}
	else {
	    Attribute attr = start.getAttribute
		(classMap.xsi, "nil");

            if (attr == null) attr = start.getAttribute
		(classMap.xsi, "null");

	    if (attr != null && Soap.stringToBoolean (attr.getValue ())) {
		obj = null;
		parser.read (); // start tag
		parser.skip ();
		parser.read (Xml.END_TAG, null, null);
	    }
	    else {

		attr = start.getAttribute
		    (classMap.xsi, "type");

		if (attr != null) {
		    String type = attr.getValue ();
		    int cut = type.indexOf (':');

		    name = type.substring (cut + 1);
		    String prefix = cut == -1 ? "" : type.substring (0, cut);
		    namespace = start.getPrefixMap ().getNamespace (prefix);
		}
		else if (name == null && namespace == null) {
		    if (start.getAttribute (classMap.enc, "arrayType") != null) {
			namespace = classMap.enc;
			name = "Array";
		    }
		    else {
			Object [] names = classMap.getInfo
			    (expected.type, null);

			namespace = (String) names [0];
			name = (String) names [1];

			//	System.out.println ("getInfo for "+expected.type+": {"+namespace+"}"+name);
		    }
		}

		obj = classMap.readInstance
		    (this, namespace, name, expected);

		if (obj == null)
		    obj = readUnknown (namespace, name);
	    }

	    // finally, care about the id....
	    String id = start.getValueDefault ("id", null);

	    if (id != null) {
		Object hlp = idMap.get (id);
		if (hlp instanceof FwdRef) {
		    FwdRef f = (FwdRef) hlp;
		    do {
			if (f.obj instanceof KvmSerializable)
			    ((KvmSerializable) f.obj)
				.setProperty (f.index, obj);
			else
			    ((Vector) f.obj)
				.setElementAt (obj, f.index);

			f = f.next;
		    }
		    while (f != null);
		}
		else if (hlp != null)
		    throw new RuntimeException ("double ID");

		idMap.put (id, obj);
	    }
	}

	//	System.out.println ("leaving read");

	return obj;
    }



    protected void readSerializable (KvmSerializable obj)
	throws IOException {

	parser.read (); // read start tag

	int testIndex = -1; // inc at beg. of loop for perf. reasons
	int sourceIndex = 0;
	int cnt = obj.getPropertyCount ();
	PropertyInfo info = new PropertyInfo ();

	while (true) {
	    parser.skip ();
	    if (parser.peek ().getType () == Xml.END_TAG) break;

	    StartTag start = (StartTag) parser.peek ();
	    String name = start.getName ();
	    //	    System.out.println ("tagname:"+name);

	    int countdown = cnt;

	    while (true) {
		if (countdown-- == 0)
		    throw new RuntimeException ("Unknwon Property: "+name);

		if (++testIndex >= cnt) testIndex = 0;

		obj.getPropertyInfo (testIndex, info);
		if (info.name == null 
		    ? testIndex == sourceIndex
		    : info.name.equals (name)) break;

	    }

	    obj.setProperty 
		(testIndex, read (obj, testIndex, null, null, info));

	    sourceIndex = 0;
	}

	parser.read (Xml.END_TAG, null, null);
    }


    public Object readUnknown (String namespace,
			       String name) throws IOException {

	parser.read (); // start tag
	parser.skip ();

	Object result;

	if (parser.peek ().getType () == Xml.TEXT)
	     result = new SoapPrimitive
		 (namespace, name, parser.readText ());
	else {
	    SoapObject so = new SoapObject (namespace, name);

	    while (parser.peek ().getType () != Xml.END_TAG) {
		so.addProperty (parser.peek ().getName (),
				read (so, so.getPropertyCount (),
				      null, null,
				      ElementType.OBJECT_TYPE));
		parser.skip ();
	    }
	    result = so;
	}

	parser.read (Xml.END_TAG, null, null);

	return result;
    }

    private int getIndex (Attribute attr, int start, int dflt) {
	if (attr == null) return dflt;
	String value = attr.getValue ();
	return value.length() - start < 3 
	    ? dflt
	    : Integer.parseInt (value.substring (start+1, value.length()-1));
    }
    

    public void readVector (Vector v,
			    ElementType elementType) throws IOException {

	StartTag start = (StartTag) parser.read ();

	String namespace = null;
	String name = null;
        int size = v.size ();
        boolean dynamic = true;

	Attribute attr = start.getAttribute (classMap.enc, "arrayType");
	if (attr != null) {
	    String type = attr.getValue ();

	    int cut0 = type.indexOf (':');
	    int cut1 = type.indexOf ("[", cut0);
	    name = type.substring (cut0 + 1, cut1);
	    String prefix = cut0 == -1 ? "" : type.substring (0, cut0);
	    namespace = start.getPrefixMap ().getNamespace (prefix);

	    size = getIndex(attr, cut1, -1);
	    if (size != -1) { 
		v.setSize (size);
		dynamic = false;
	    }
	}

	if (elementType == null)
	    elementType = ElementType.OBJECT_TYPE;

	parser.skip ();

        int position = getIndex 
	    (start.getAttribute (classMap.enc, "offset"), 0, 0);

	while (parser.peek ().getType () != Xml.END_TAG) {
            // handle position

	    position = getIndex (parser.peek ().getAttribute 
				 (classMap.enc, "position"), 0, position);

            if (dynamic && position >= size) {
		size = position + 1;
		v.setSize (size);
            }

            // implicit handling of position exceeding specified size
	    v.setElementAt 
		(read (v, position, namespace, name, elementType),
		 position);
	    
            position++;
	    parser.skip ();
	}

	parser.read (Xml.END_TAG, start.getNamespace (), start.getName ());
    }
}





