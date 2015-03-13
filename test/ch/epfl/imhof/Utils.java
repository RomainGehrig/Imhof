package ch.epfl.imhof;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import ch.epfl.imhof.osm.OSMEntity;
import ch.epfl.imhof.osm.OSMNode;
import ch.epfl.imhof.osm.OSMRelation;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;
import ch.epfl.imhof.osm.OSMWay;

public abstract class Utils {
    
    public static PointGeo newPointGeo(){
        Math.random();
        double longitude = -Math.PI + (Math.random()*(2*Math.PI));
        double latitude = -Math.PI/2 + (Math.random()*(Math.PI));
        PointGeo p = new PointGeo(longitude, latitude);
        return p;
    }
    
    public static void assertPointGeo(PointGeo p1, PointGeo p2){
        assertEquals(p1.latitude(), p2.latitude(), 0.00000001);
        assertEquals(p1.longitude(), p2.longitude(), 0.00000001);
    }
    
    public static long newId(){
        long a = (long)(Math.random()*1000);
        return a;
    }
    
    public static Attributes newAttributes(){
        Map<String, String> map = new HashMap<>();
        map.put("k", "v");
        map.put("k1", "v1");
        map.put("k2", "v2");
        return new Attributes(map);
        }
    
    public static assertAttributes(){
        
    }
    
    public static OSMNode newNode(){
        return new OSMNode(0, newPointGeo(), newAttributes());
    }
    
    public static void assertEqualsNode(OSMNode n1, OSMNode n2) {
        assertEquals(n1.id(), n2.id());
        assertEquals(n1.position(), n2.position());
        assertEquals(n1.attributes().size(), n2.attributes().size());
    } 
    
    public static List<OSMNode> newNodeList(){
        List<OSMNode> l = new ArrayList<>();
        for(int i = 0; i<10; ++i){
            l.add(newNode());
        }
        return l;
    }
    
    public static OSMWay newWay(){
        return new OSMWay(0, newNodeList(), newAttributes());
    }
    
    public static void assertEqualsWay(OSMWay w1, OSMWay w2){
        for(int i = 0; i<w1.nodes().size(); ++i){
            assertEqualsNode(w1.nodes().get(i), w2.nodes().get(i));
        }
    }
    
    public static Member newMember(){
        Type type = null;
        String role = "";
        
        int i = (int)(Math.random()*3);
        switch(i){
        case 1 : 
            type = OSMRelation.Member.Type.NODE;
            break;
        case 2 :
            type = OSMRelation.Member.Type.RELATION;
            break;
        case 3 :
            type = OSMRelation.Member.Type.WAY;
            break;
        }
        
        int j = (int)(Math.random()*2);
        switch(j){
        case 1 :
            role = "in";
            break;
        case 2 :
            role = "out";
            break;
        }
        
        if(type == OSMRelation.Member.Type.NODE){
            return new Member(type, role, newNode());
        } else if(type == OSMRelation.Member.Type.RELATION){
            return new Member(type, role, newRelation());
        } else {
            return new Member(type, role, newWay());
        }
    }
    
    public static List<Member> newMemberList(){
        ArrayList<Member> a = new ArrayList<Member>();
        for(int i = 0; i<10; ++i){
            a.add(newMember());
        }
        return a;
    }
    
    public static OSMRelation newRelation(){
        return new OSMRelation(newId(), newMemberList(), newAttributes());
    }
    
    public static void assertRelation(OSMRelation r1, OSMRelation r2){
        assertEquals(r1.id(), r2.id());
        assertEquals(r1.members(), r2.members());
        assertEquals(r1.attributes(), r2.attributes());
    }   
}
