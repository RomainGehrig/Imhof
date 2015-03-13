package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

public class OSMNodeTest {

    private Attributes attr;
    private final PointGeo pt = new PointGeo(1,1);

    @Before
    public void createAttributes() {
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put("Bla", "bla");
        this.attr = new Attributes(attrs);
    }

    @Test (expected = NullPointerException.class)
    public void testNullAttributes_Constructor() {
        Attributes null_attr = null;
        OSMNode n = new OSMNode(100, this.pt, null_attr);
    }

    @Test (expected = NullPointerException.class)
    public void testNullPointGeo_Constructor() {
        PointGeo null_pt = null;
        OSMNode n = new OSMNode(100, null_pt, this.attr);
    }

    @Test (expected = NullPointerException.class)
    public void testNullPointGeo_Builder() {
        new OSMNode.Builder(10, null);
    }

    @Test (expected = IllegalStateException.class)
    public void testIncompleteBuilder() {
        OSMNode.Builder b = new OSMNode.Builder(10, this.pt);
        b.setIncomplete();
        b.build();
    }

    @Test
    public void testBuildWithNoAttributes() {
        OSMNode.Builder b = new OSMNode.Builder(10, this.pt);
        b.build();
    }
    
    @Test(expected = NullPointerException.class)
    public void constructorOSMNodeTest() {
        new OSMNode (0, pt, null);
    }
    
    @Test
    public void  BuilderTest(){
        OSMNode.Builder b = new OSMNode.Builder(384723942929L, pt);
    }
    
    @Test
    public void BuilderNegatifTest(){
        OSMNode.Builder bNeg = new OSMNode.Builder(-1234, pt);
    }
    
}

