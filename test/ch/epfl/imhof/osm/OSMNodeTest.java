package ch.epfl.imhof.osm;

import static org.junit.Assert.*;

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
