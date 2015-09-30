package org.kxml;

import java.io.IOException;
import org.kxml.io.AbstractXmlWriter;
import org.kxml.parser.AbstractXmlParser;

public interface XmlIO {

    public void parse (AbstractXmlParser parser) throws IOException;
    public void write (AbstractXmlWriter writer) throws IOException;
}
