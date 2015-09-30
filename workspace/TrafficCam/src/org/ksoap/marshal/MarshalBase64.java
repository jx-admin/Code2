package org.ksoap.marshal;

import java.io.*;
import org.ksoap.*;
import org.kobjects.base64.*;
import org.kobjects.serialization.*;

/** Base64 (de)serializer */


public class MarshalBase64 implements Marshal {

    static byte [] BA_WORKAROUND = new byte [0];
    public static Class BYTE_ARRAY_CLASS = BA_WORKAROUND.getClass ();   
    

    public Object readInstance (SoapParser parser,
				String namespace, String name,
				ElementType expected) throws IOException {

	parser.parser.read (); // start tag
	Object result = Base64.decode 
	    (parser.parser.readText ());
	parser.parser.read (); // end tag
	return result;
    }
	

    public void writeInstance (SoapWriter writer, 
			       Object obj) throws IOException {

	writer.writer.write (Base64.encode ((byte[]) obj));
    }

    public void register (ClassMap cm) {
	cm.addMapping 
	    (cm.xsd, "base64Binary", 
	     MarshalBase64.BYTE_ARRAY_CLASS, this);   

	cm.addMapping 
	    (Soap.ENC, "base64", 
	     MarshalBase64.BYTE_ARRAY_CLASS, this);

    }
}
