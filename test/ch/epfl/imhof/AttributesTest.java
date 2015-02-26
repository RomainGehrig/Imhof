package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class AttributesTest {
    
    Attributes a;
    
    @Before
    public void Init() {
        Attributes.Builder b = new Attributes.Builder();
        b.put("natural", "water");
        b.put("name", "Lac Léman");
        b.put("ele", "372");
        a = b.build();
    }
    
    @Test
    public void isEmptyTest(){
        Attributes.Builder a = new Attributes.Builder();
        assertTrue(a.build().isEmpty());
    }
    
    @Test
    public void containtsTest(){
        assertTrue(a.contains("ele"));
    }
    
    @Test
    public void getTest(){
        assertEquals("water", a.get("natural"));
        assertEquals("Lac Léman", a.get("name", "Lac"));
        assertEquals("Lac", a.get("nameTest", "Lac"));
        assertEquals(372, a.get("ele", 0));
    }
    
    @Test
    public void keepOnlyKeys(){
        Set<String> s = new HashSet<>();
        s.add("ele");
        a.keepOnlyKeys(s);
        assertFalse(s.contains("natural"));
    }
    
    
}
