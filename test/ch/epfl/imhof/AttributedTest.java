package ch.epfl.imhof;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.geometry.Polygon;

public class AttributedTest {
    
    Attributed<Polygon> ap;
    
    @Before
    public void test() {
        Attributes.Builder b = new Attributes.Builder();
        b.put("natural", "water");
        b.put("name", "Lac Léman");
        b.put("ele", "372");
        Attributes a = b.build();

        Polygon p = new Polygon(null);
        ap = new Attributed<>(p, a);
    }
    
    @Test
    public void hasAttributeTest(){
        assertTrue(ap.hasAttribute("ele"));
    }
    
    @Test
    public void attributeValue(){
        assertEquals("water", ap.attributeValue("natural"));
        assertEquals("Lac Léman", ap.attributeValue("name", "Lac"));
        assertEquals("Lac", ap.attributeValue("Na", "Lac"));
        assertEquals(372, ap.attributeValue("ele", 0));
        assertEquals(300, ap.attributeValue("a", 300));
    }

}
