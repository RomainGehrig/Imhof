package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import org.xml.sax.SAXException;
import java.io.IOException;

public class OSMMapReaderTest {

    private final boolean RUN_BIG_TESTS = true;

    public void printMap(OSMMap m) {
        System.out.println(m.ways().size() + " ways.");
        System.out.println(m.relations().size() + " relations.");
    }

    @Test
    public void bcTest() throws SAXException,IOException {
        // String fileName = getClass().getClassLoader().getResource("bc.osm").getFile();
        // OSMMap map = OSMMapReader.readOSMFile(fileName, false);

        testBigData("bc",false);
    }

    @Test
    public void learningcenterTest() throws SAXException,IOException {
        // String fileName = getClass().getClassLoader().getResource("lc.osm").getFile();
        // OSMMap map = OSMMapReader.readOSMFile(fileName, false);
        testBigData("lc",false);
    }

    @Test
    public void lausanneTest() throws SAXException,IOException {
        testBigData("lausanne", true);
    }

    @Test
    public void interlakenTest() throws SAXException,IOException {
        testBigData("interlaken", true);
    }

    @Test
    public void berneTest() throws SAXException,IOException {
        testBigData("berne", true);
    }

    public void testBigData(String cityname, boolean gz) throws SAXException,IOException {
        if (!RUN_BIG_TESTS) return;

        long start = System.currentTimeMillis();
        String fileName = getClass().getClassLoader().getResource(cityname + ".osm" + (gz ? ".gz" : "")).getFile();
        OSMMap map = OSMMapReader.readOSMFile(fileName, gz);
        long end = System.currentTimeMillis();
        System.out.println(cityname + " took " + ((end - start)/1000d) + "sec to complete.");
        printMap(map);
    }
}
