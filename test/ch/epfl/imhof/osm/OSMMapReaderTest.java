package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.xml.sax.SAXException;
import java.io.IOException;

public class OSMMapReaderTest {

    @Test
    public void bcTest() throws SAXException,IOException {
        String fileName = getClass().getClassLoader().getResource("bc.osm").getFile();
        OSMMap map = OSMMapReader.readOSMFile(fileName, false);
    }
}
