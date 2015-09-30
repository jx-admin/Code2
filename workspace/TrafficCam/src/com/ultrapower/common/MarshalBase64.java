package com.ultrapower.common;
/**
 * @author VictorZheng
 *
 */
import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/** Base64 (de)serializer */

public class MarshalBase64 implements Marshal {

    static byte[] BA_WORKAROUND = new byte[0];
    public static Class BYTE_ARRAY_CLASS = BA_WORKAROUND.getClass();

    public Object readInstance(
        XmlPullParser parser,
        String namespace,
        String name,
        PropertyInfo expected)
        throws IOException, XmlPullParserException {

        //	parser.read (); // start tag
        Object result = Base64.decode(parser.nextText());
        //	parser.parser.read (); // end tag
        return result;
    }

    public void writeInstance(XmlSerializer writer, Object obj)
        throws IOException {

        writer.text(Base64.encode((byte[]) obj));
    }

    public void register(SoapSerializationEnvelope cm) {
        cm.addMapping(
            cm.xsd,
            "base64Binary",
            MarshalBase64.BYTE_ARRAY_CLASS,
            this);

        cm.addMapping(
            SoapEnvelope.ENC,
            "base64",
            MarshalBase64.BYTE_ARRAY_CLASS,
            this);

		cm.addMapping(
				"http://schemas.xmlsoap.org/soap/encoding/",
				"base64Binary",
	            MarshalBase64.BYTE_ARRAY_CLASS,
	            this);
		
		cm.addMapping(
				"http://schemas.xmlsoap.org/soap/encoding/",
	            "base64",
	            MarshalBase64.BYTE_ARRAY_CLASS,
	            this);
    }
}
