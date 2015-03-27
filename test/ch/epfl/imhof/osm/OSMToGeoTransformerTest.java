
package ch.epfl.imhof.osm;

import org.xml.sax.SAXException;
import java.io.IOException;

import org.junit.Test;

import ch.epfl.imhof.projection.EquirectangularProjection;
import ch.epfl.imhof.Map;

public class OSMToGeoTransformerTest {

    private final boolean RUN_BIG_TESTS = true;
    private final OSMToGeoTransformer trans = new OSMToGeoTransformer(new EquirectangularProjection());

    @Test
    public void bcTest() throws SAXException,IOException {
        testGeoTransformer("bc",false);
    }

    @Test
    public void learningcenterTest() throws SAXException,IOException {
        testGeoTransformer("lc",false);
    }

    @Test
    public void lausanneTest() throws SAXException,IOException {
        if (!RUN_BIG_TESTS) return;
        testGeoTransformer("lausanne", true);
    }

    @Test
    public void interlakenTest() throws SAXException,IOException {
        if (!RUN_BIG_TESTS) return;
        testGeoTransformer("interlaken", true);
    }

    @Test
    public void berneTest() throws SAXException,IOException {
        if (!RUN_BIG_TESTS) return;
        testGeoTransformer("berne", true);
    }

    public static void printMap(Map m) {
        System.out.println(m.polygons().size() + " polygons.");
        System.out.println(m.polyLines().size() + " polyLines.");
    }

    public OSMMap testGeoTransformer(String cityname, boolean gz) throws SAXException,IOException {
        long start = System.currentTimeMillis();
        String fileName = getClass().getClassLoader().getResource(cityname + ".osm" + (gz ? ".gz" : "")).getFile();
        OSMMap map = OSMMapReader.readOSMFile(fileName, gz);

        Map newMap = trans.transform(map);

        long end = System.currentTimeMillis();
        System.out.println(cityname + " took " + ((end - start)/1000d) + "sec to read & transform.");
        OSMMapReaderTest.printMap(map);
        printMap(newMap);

        return map;
    }


}
