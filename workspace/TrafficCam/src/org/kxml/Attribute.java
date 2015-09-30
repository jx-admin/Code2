package org.kxml;


/** Attribute class, used by both kDom and the pullparser.  The
    instances of this class are immutable. This restriction allows
    manipulation aware element implementations without needing to care
    about hidden changes in attributes. */


public class Attribute  {

    String namespace;
    String name;
    String value;
    

    /** Creates a new Attribute instance with the given name and
        value. The namespace is set to "". */

    public Attribute (String name, String value) {
	this.namespace = "";
	this.name = name;
	this.value = value;
    }


    /** creates a new Attribute with the given namespace, name and
        value */

    public Attribute (String namespace, String name, String value) {
	this.namespace = namespace == null ? "" : namespace;
	this.name = name;
	this.value = value;
    }
    

    /** returns the string value of the attribute */

    public String getValue () {
	return value;
    }


    /** returns the name of the attribute */
    
    public String getName () {
	return name;
    }


    /** returns the namespace of the attribute */

    public String getNamespace () {
	return namespace;
    }


    public String toString () {
	return (!namespace.equals ("") 
		? ("{"+namespace+"}"+name)
		: name) + "=\""+value+"\"";
    }
}

