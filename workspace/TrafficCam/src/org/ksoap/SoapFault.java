package org.ksoap;

import java.util.Vector;
import java.io.*;
import org.kxml.*;
import org.kxml.io.*;
import org.kxml.parser.*;

public class SoapFault extends IOException implements XmlIO {

    public String faultcode;
    public String faultstring;
    public String faultactor;
    public Vector detail;

    public void parse (AbstractXmlParser parser) throws IOException {
	parser.read (Xml.START_TAG, Soap.ENV, "Fault");

	while (true) {
	    parser.skip ();
	    ParseEvent ev = parser.peek ();
	    switch (ev.getType ()) {

	    case Xml.START_TAG: 
		{
		    String name = ev.getName ();

		    if (name.equals ("detail")) {
			detail = new Vector ();
			parser.readTree (detail);
		    }
		    else {
			parser.read ();
			String value = parser.readText ();
			parser.read ();
			
			
			if (name.equals ("faultcode"))
			    faultcode = value;
			else if (name.equals ("faultstring"))
			    faultstring = value;
			else if (name.equals ("faultactor"))
			    faultactor = value;
		    }
		}
		break;

	    case Xml.END_TAG: 
		parser.read ();
		return;
	    default:
		parser.read ();  // ignore text
	    }
	}
    }

    
    public void write (AbstractXmlWriter xw) throws IOException {
	xw.startTag (Soap.ENV, "Fault");
	xw.startTag ("faultcode");
	xw.write (""+ faultcode);
	xw.endTag ();
	xw.startTag ("faultstring");
	xw.write (""+ faultstring);
	xw.endTag ();
	
	xw.startTag ("detail");

	if (detail != null) 
	    for (int i = 0; i < detail.size (); i++) {
		xw.startTag ("item");
		xw.write (""+detail.elementAt (i));
		xw.endTag ();
	    }
	    
	xw.endTag ();
	xw.endTag ();
    }
    

    public String toString () {
	return "SoapFault - faultcode: '"+faultcode
	    +"' faultstring: '"+faultstring 
	    +"' faultactor: '"+faultactor
	    +"' detail: "+detail;
	
    }

}
