package org.kxml.io;

import java.io.*;
import java.util.*;

import org.kxml.*;


/** An abstract XmlWriter including namespace handling. */


public abstract class AbstractXmlWriter extends Writer {

    protected State current =
	new State (null,
		   PrefixMap.DEFAULT, //null, null,
		   null);



    /** writes an attribute. Only allowed immediately after
     * startTag or attribute.  */

    public abstract void attribute (String name,
				    String value) throws IOException;



    /** writes an attribute with the given namespace. Only allowed
     * immediately after startTag or another attribute
     * call. */

    public void attribute (String namespace,
			   String name,
			   String value) throws IOException {

	if (namespace == null || "".equals (namespace))
	    attribute (name, value);
	else {
	    String prefix = current.prefixMap.getPrefix (namespace);
	    if (prefix == null || prefix.equals ("")) {
		int cnt = 0;
		do {
		    prefix = "p"+(cnt++);
		}
		while (current.prefixMap.getNamespace (prefix) != null);
		current.prefixMap = new PrefixMap
		    (current.prefixMap, prefix, namespace);

		attribute ("xmlns:"+prefix, namespace);
	    }
	    attribute (prefix + ":" + name, value);
	}
    }

    public PrefixMap getPrefixMap () {
	return current.prefixMap;
    }


    /** writes a start tag with the given name, using the given prefix
     * map.  This method cares about the namespace prefixes and calls
     * startTag (PrefixMap prefixMap, String tag) for concrete
     * writing. */

    public void startTag (PrefixMap prefixMap,
			  String namespace,
			  String name) throws IOException {

	// check if namespace is default.

	if (prefixMap == null) prefixMap = current.prefixMap;
	if (namespace == null) namespace = "";

	String prefix = prefixMap.getPrefix (namespace);

	if (prefix == null) {
	    prefixMap = new PrefixMap (prefixMap, "", namespace);
	    prefix = "";
	}

	String tag = prefix.length () == 0
	    ? name
	    : prefix + ':' + name;

	PrefixMap oldMap = current.prefixMap;

	startTag (prefixMap, tag);

	// if namespace has changed, write out changes...

	if (prefixMap != oldMap) {
	    PrefixMap current = prefixMap;
        boolean nsOutput=false;
	    do {
		String p2 = current.getPrefix ();
		String ns = current.getNamespace ();
		if (prefixMap.getNamespace (p2).equals (ns)
		    && !ns.equals (oldMap.getNamespace (p2))
                && !nsOutput) {
		    attribute (p2.equals ("") ? "xmlns" : ("xmlns:"+p2), ns);
            nsOutput=true;
        }
		current = current.getPrevious ();
	    }
	    while (current != null && current != oldMap);
	}
    }


    /** writes a start tag with the given namespace and name */

    public void startTag (String namespace,
			  String name) throws IOException {
	startTag (null, namespace, name);
    }


    /** convenience method for startTag (Xml.NO_NAMESPACE, name) */

    public void startTag (String name) throws IOException {
	startTag (null, Xml.NO_NAMESPACE, name);
    }


    /** abstract method that must be overwritten by
     * a method actually writing the resolved start tag
     * without namespace checking. This implementation
     * just puts the state on the stack.<br /><br />
     *
     * Attention: The actual implementation include the
     * following line in order to
     * put the current State on the stack!<br /><br />
     *
     * current = new State (current, prefixMap, tag);
    */


    protected abstract void startTag (PrefixMap prefixMap,
				      String tag) throws IOException;


    /** Abstract method for writing an end tag.  <b>Attention:</b>
     * Concrete implementations must pop the previous stack from the
     * stack:<br /><br />
     *
     * current = current.prev; */

    public abstract void endTag () throws IOException;

    /** writes Xml.DOCTYPE, Xml.PROCESSING_INSTRUCTION or Xml.COMMENT */

    public abstract void writeLegacy (int type,
				      String text) throws IOException;
}
