package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public final class OSMMapReader {
    private OSMMapReader() {}

    public static OSMMap readOSMFile(String fileName, boolean unGZip) {

        
        return null;
    }

    private static class OSMHandler extends DefaultHandler {
        @Override
        public void startElement(String uri,
                                 String lName,
                                 String qName,
                                 Attributes atts)
        throws SAXException {
            
        }

        @Override
        public void endElement(String uri,
                               String lName,
                               String qName) {
            
        }
    
    }
}
