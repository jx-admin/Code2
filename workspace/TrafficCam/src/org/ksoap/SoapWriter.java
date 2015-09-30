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
 * Contributor(s): Mark Sanguinetti, Sean McDaniel
 *
 * */

package org.ksoap;

import java.io.*;
import java.util.*;

import org.kxml.*;
import org.kxml.io.*;
import org.kobjects.serialization.*;

/** A writer that is able to write objects wrt. the SOAP section five
    encoding rules. */

public class SoapWriter {

    public AbstractXmlWriter writer;
    ClassMap classMap;
    Vector multiRef = new Vector ();
    Vector types = new Vector ();

    /** The SoapWriter is initialized with an AbstractXmlWriter and a
        class map.  */


    public SoapWriter (AbstractXmlWriter writer,
		       ClassMap classMap) {

	this.writer = writer;
	this.classMap = classMap;
    }



    /** Serializes the given object */

    public void write (Object obj) throws IOException {

	multiRef.addElement (obj);
	types.addElement (ElementType.OBJECT_TYPE);

	for (int i = 0; i < multiRef.size (); i++) {

	    obj = multiRef.elementAt (i);

	    Object [] qName = classMap.getInfo (null, obj);

	    writer.startTag (classMap.prefixMap,
			     (String) qName [0], (String) qName [1]);
	    writer.attribute
		("id",
		 qName [2] == null ? ("o"+i) : (String) qName [2]);

	    if (i == 0) writer.attribute (classMap.enc, "root", "1");

	    if (qName [3] != null)
		((Marshal) qName [3]).writeInstance (this, obj);
	    else if (obj instanceof KvmSerializable)
		writeObjectBody ((KvmSerializable) obj);
	    else if (obj instanceof Vector)
		writeVectorBody
		    ((Vector) obj,
		     ((ElementType) types.elementAt (i)).elementType);
	    else
		throw new RuntimeException ("Cannot serialize: "+ obj);

	    writer.endTag ();
	}
    }


    /** Writes the body of an KvmSerializable object. This
	method is public for access from Marshal subclasses. */


    public void writeObjectBody (KvmSerializable obj) throws IOException {

	PropertyInfo info = new PropertyInfo ();
	int cnt = obj.getPropertyCount ();

	for (int i = 0; i < cnt; i++) {

	    obj.getPropertyInfo (i, info);

            //	    Object value = obj.getProperty (i);

	    //	    if (value != null) {
		writer.startTag (info.name);
		writeProperty (obj.getProperty (i), info);
		writer.endTag ();
		//}
	}
    }


    protected  void writeProperty (Object obj,
				   ElementType type) throws IOException {

	if (obj == null) {
	    writer.attribute (classMap.xsi, classMap.version >= Soap.VER12 
			      ? "nil" : "null", "true");
	    return;
	}

	Object [] qName = classMap.getInfo (null, obj);

	if (type.multiRef || qName [2] != null) {
	    int i = multiRef.indexOf (obj);
	    if (i == -1) {
		i = multiRef.size ();
		multiRef.addElement (obj);
		types.addElement (type);
	    }

	    writer.attribute
		("href", qName [2] == null ? ("#o"+i) : "#" + qName [2]);
	}
	else {

	    if (!classMap.implicitTypes || obj.getClass () != type.type) {

		String prefix = writer.getPrefixMap ().getPrefix
		    ((String) qName [0]);

		if (prefix == null) throw new RuntimeException
		    ("Prefix for namespace "+qName [0] + " undefined!");

		writer.attribute
		    (classMap.xsi, "type", prefix + ":" + qName [1]);
	    }

	    if (qName [3] != null)
		((Marshal) qName [3]).writeInstance (this, obj);
	    else if (obj instanceof KvmSerializable)
		writeObjectBody ((KvmSerializable) obj);
	    else if (obj instanceof Vector)
		writeVectorBody ((Vector) obj, type.elementType);
	    else
		throw new RuntimeException ("Cannot serialize: "+ obj);
	}
    }


    protected void writeVectorBody (Vector vector,
				    ElementType elementType) throws IOException {

	if (elementType == null)
	    elementType = ElementType.OBJECT_TYPE;

	int cnt = vector.size ();

	Object [] arrType = classMap.getInfo (elementType.type, null);


	writer.attribute
	    (classMap.enc, "arrayType",
	     writer.getPrefixMap ().getPrefix ((String) arrType [0])
	     + ":" + arrType [1] + "[" + cnt + "]");


        boolean skipped = false;
	for (int i = 0; i < cnt; i++) {
            if (vector.elementAt (i) == null)
                skipped = true;
            else {
                writer.startTag ("item");
                if (skipped) {
                    writer.attribute (classMap.enc, "position", "["+i+"]");
                    skipped = false;
                }

   	        writeProperty (vector.elementAt (i), elementType);
                writer.endTag ();
            }
	}
    }
}

