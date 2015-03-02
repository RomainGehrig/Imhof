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
        b.put("testnull", null);
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
        assertEquals(null, a.get("testnull"));
    }
    
    @Test
    public void keepOnlyKeys(){
        Set<String> s = new HashSet<>();
        s.add("ele");
        Attributes a2 = a.keepOnlyKeys(s);
        assertFalse(a2.contains("natural"));
    }
    
    @Test
    public void withUnknown_keepOnlyKeys(){
        Set<String> s = new HashSet<>();
        s.add("Ceci n'est pas une clé");
        Attributes a2 = a.keepOnlyKeys(s);
        assertFalse(a2.contains("Ceci n'est pas une clé"));
        assertTrue(a2.isEmpty());
    }
    
}
