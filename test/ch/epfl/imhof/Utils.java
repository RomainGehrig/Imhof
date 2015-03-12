package ch.epfl.imhof;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.imhof.osm.OSMNode;
import ch.epfl.imhof.osm.OSMWay;

public abstract class Utils {

    public static void assertEqualsNode(OSMNode n1, OSMNode n2) {
        assertEquals(n1.id(), n2.id());
        assertEquals(n1.position(), n2.position());
        assertEquals(n1.attributes().size(), n2.attributes().size());
    }
    
    public static void assertEqualsWay(OSMWay w1, OSMWay w2){
        for(int i = 0; i<w1.nodes().size(); ++i){
            assertEqualsNode(w1.nodes().get(i), w2.nodes().get(i));
        }
    }
}
