package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.xml.sax.SAXException;
import java.io.IOException;

public class OSMMapReaderTest {

    private final boolean RUN_BIG_TESTS = true;

    @Test
    public void bcTest() throws SAXException,IOException {
        String fileName = getClass().getClassLoader().getResource("bc.osm").getFile();
        System.out.println(fileName);
        OSMMap map = OSMMapReader.readOSMFile(fileName, false);
    }

    @Test
    public void learningcenterTest() throws SAXException,IOException {
        String fileName = getClass().getClassLoader().getResource("lc.osm").getFile();
        System.out.println(fileName);
        OSMMap map = OSMMapReader.readOSMFile(fileName, false);
    }

    @Test
    public void lausanneTest() throws SAXException,IOException {
        testBigData("lausanne");
    }
    @Test
    public void interlakenTest() throws SAXException,IOException {
        testBigData("interlaken");
    }
    @Test
    public void berneTest() throws SAXException,IOException {
        testBigData("berne");
    }

    public void testBigData(String cityname) throws SAXException,IOException {
        if (!RUN_BIG_TESTS) return;

        long start = System.currentTimeMillis();
        String fileName = getClass().getClassLoader().getResource(cityname + ".osm.gz").getFile();
        OSMMap map = OSMMapReader.readOSMFile(fileName,true);
        long end = System.currentTimeMillis();
        System.out.println(cityname + " took " + ((end - start)/1000d) + "sec to complete.");
    }
}
