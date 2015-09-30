package org.kxml.parser;


import org.kxml.*;

/** A class for events indicating the 
 *  end of an element 
 */


public class Tag extends ParseEvent {

    StartTag parent;
    String namespace;
    String name;

    public Tag (int type, StartTag parent, String namespace, String name) {
	super (type, null);
	this.parent = parent;
	this.namespace = namespace == null ? Xml.NO_NAMESPACE : namespace;
	this.name = name;
    }



    /** returns the (local) name of the element */

    public String getName () {
	return name;
    }

    /** returns the namespace */ 
    
    public String getNamespace () {
	return namespace;
    }
  
    /** Returns the (corresponding) start tag or the start tag of the parent element, depending on the event type. */ 

    public StartTag getParent () {
	return parent;
    }

  

    public String toString () {
	return "EndTag </"+name + ">";
    }
}








