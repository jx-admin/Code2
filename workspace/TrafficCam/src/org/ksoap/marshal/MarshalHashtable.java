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
 * Contributor(s): Sean McDaniel
 *
 * */
package org.ksoap.marshal;

import java.io.*;
import java.util.*;

import org.kobjects.serialization.ElementType;
import org.ksoap.*;
import org.kxml.Xml;
import org.kxml.io.XmlWriter;
import org.kxml.parser.*;

/**
 * serializes instances of hashtable to and from xml.
 * this implementation is based on the xml schema from
 * apache-soap, namely the type 'map' in the namespace
 * 'http://xml.apache.org/xml-soap'.  other soap
 * implementations including apache (obviously ) and
 * glue are also interoperable with the schema.
 */
public class MarshalHashtable implements Marshal {

    /** use then during registration */
    public static final String NAMESPACE = "http://xml.apache.org/xml-soap";

    /** use then during registration */
    public static final String NAME = "Map";

    /** CLDC does not support .class, so this helper is needed. */
    public static final Class HASHTABLE_CLASS = new Hashtable ().getClass ();
    
    public Object readInstance ( SoapParser parser,
                                 String namespace,
                                 String name,
                                 ElementType expected ) throws IOException {

        Hashtable instance = new Hashtable ();
        AbstractXmlParser xmlParser = parser.parser;

        // advance <apache-xml:map>
        xmlParser.read ();
        xmlParser.skip ();
        while (xmlParser.peek ().getType () != Xml.END_TAG) {
            SoapObject item = new ItemSoapObject (instance);

            // advance <item>
            xmlParser.read ();
            xmlParser.skip ();

            Object key =
              parser.read (item, 0, null, null, ElementType.OBJECT_TYPE);
            parser.parser.skip ();
            if (key != null)
                item.setProperty (0, key);

            Object value =
              parser.read (item, 1, null, null, ElementType.OBJECT_TYPE);
            parser.parser.skip ();
            if (value != null)
                item.setProperty (1, value);

            // advance </item>
            xmlParser.read ();
            xmlParser.skip ();
        }

        // advance </apache-xml:map>
        parser.parser.read ();
        return instance;
    }

    public void writeInstance (SoapWriter writer,
                               Object instance) throws IOException {

        Hashtable h = (Hashtable) instance;
        SoapObject item = new SoapObject (null, null);
        item.addProperty ("key", ElementType.OBJECT_TYPE, null);
        item.addProperty ("value", ElementType.OBJECT_TYPE, null);
        for (Enumeration keys = h.keys (); keys.hasMoreElements ();) {
            writer.writer.startTag ("item");
            Object key = keys.nextElement ();
            item.setProperty (0, key);
            item.setProperty (1, h.get( key ));
            writer.writeObjectBody (item);
            writer.writer.endTag ();
        }
    }

    //////////////////////////////////////////////
    class ItemSoapObject extends SoapObject {
        Hashtable h;
        int resolvedIndex = -1;

        ItemSoapObject (Hashtable h) {
          super (null, null);
          this.h = h;
          addProperty ("key", ElementType.OBJECT_TYPE, null);
          addProperty ("value", ElementType.OBJECT_TYPE, null);
        }

        // 0 & 1 only valid
        public void setProperty (int index, Object value) {
          if (resolvedIndex == -1) {
              super.setProperty (index, value);
              resolvedIndex = index;
          }
          else {
              // already have a key or value
              Object resolved =
                resolvedIndex == 0
                ? getProperty (0)
                : getProperty (1);

              if (index == 0)
                  h.put (value, resolved);
              else
                  h.put (resolved, value);
          }
        }
    }

    public void register (ClassMap cm) {

	cm.addMapping (MarshalHashtable.NAMESPACE,
		       MarshalHashtable.NAME,
		       HASHTABLE_CLASS, this);

	/*
      Hashtable h = new Hashtable ();
      Integer multiref = new Integer (12);
      h.put ("one", multiref);
      h.put ("two", multiref);
      SoapEnvelope envelope = new SoapEnvelope ();
      envelope.setClassMap (classMap);
      envelope.setBody (h);

      XmlWriter writer =
        new XmlWriter (
          new OutputStreamWriter (
            new FileOutputStream (args[0])
          )
        );
      envelope.write (writer);
      writer.flush ();

      envelope = new SoapEnvelope ();
      envelope.setClassMap (classMap);
      XmlParser parser =
        new XmlParser (new FileReader (args[0]));
      envelope.parse (parser);
      Hashtable body = (Hashtable) envelope.getBody ();
      System.out.println (body);

	*/
    }
}
