package ch.epfl.imhof.osm;

import static org.junit.Assert.*;
import static ch.epfl.imhof.Utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

public class OSMWayTest {
    
    private List<OSMNode> nodes;
    private PointGeo p;
    private Attributes a;
    private OSMWay w;
    
    @Before
    public void create(){
        p = new PointGeo(0, 0);
        Map<String, String> h = new HashMap<String, String>();
        h.put("h", "h");
        a = new Attributes(h);
        nodes = new ArrayList<>();
        nodes.add(new OSMNode(0, p, a));
    }

    
    @Test(expected = IllegalArgumentException.class)
    public void OSMWayConstructorErrorTest() {
        w = new OSMWay(0, nodes, a);
    }
    
    @Test
    public void nodesCountTest(){
        nodes.add(new OSMNode(1, p, a));
        w = new OSMWay(0, nodes, a);
        assertEquals(w.nodesCount(), 2);
    }
    
    @Test
    public void nonRepeatingNodes(){
        nodes.add(new OSMNode(1, p, a));
        nodes.add(new OSMNode(0, p, a));
        assertEqualsNode(nodes.get(0), nodes.get(nodes.size()-1));
    }
    
    @Test
    public void firstNodeTest(){
        assertEqualsNode(new OSMNode(0, p, a), nodes.get(0));
    }
    
    @Test
    public void lastNodeTest(){
        OSMNode n = new OSMNode(10, p, a);
        nodes.add(n);
        assertEqualsNode(n, nodes.get(nodes.size()-1));
    }
    
    @Test
    public void isClosedTest(){
        nodes.add(new OSMNode(0, p, a));
        assertEqualsNode(nodes.get(0), nodes.get(nodes.size()-1));
    }
    
    @Test(expected = IllegalStateException.class)
    public void buildTest(){
        OSMWay.Builder builder = new OSMWay.Builder(10);
        builder.setIncomplete();
        builder.build();
    }
    
    @Test
    public void isIncompleteTest(){
        OSMWay.Builder builder = new OSMWay.Builder(10);
        builder.addNode(new OSMNode(0, p, a));
        assertTrue(builder.isIncomplete());
    }

}
