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
=======
import org.junit.Test;

import ch.epfl.imhof.PointGeo;

public class OSMNodeTest {
    
    private PointGeo p = new PointGeo(0, 0);
    private OSMNode n;

    @Test(expected = NullPointerException.class)
    public void constructorOSMNodeTest() {
        n = new OSMNode (0, p, null);
    }
    
    @Test
    public void  BuilderTest(){
        OSMNode.Builder b = new OSMNode.Builder(384723942929L, p);
    }
    
    @Test
    public void BuilderNegatifTest(){
        OSMNode.Builder bNeg = new OSMNode.Builder(-1234, p);
    }
    
    @Test(expected = NullPointerException.class)
    public void BuilderNullTest(){
        OSMNode.Builder bNull = new OSMNode.Builder(0, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void buildIncompletTest(){
        OSMNode.Builder b0 = new OSMNode.Builder(0, p);
        b0.setIncomplete();
        b0.build();
    }
    
    @Test
    public void buildCompletTest(){
        OSMNode.Builder b1 = new OSMNode.Builder(0, p);
        b1.build();
    }

}
