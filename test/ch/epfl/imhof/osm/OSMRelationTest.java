package ch.epfl.imhof.osm;

import static org.junit.Assert.*;
import static ch.epfl.imhof.Utils.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.osm.OSMRelation.Member.Type;

public class OSMRelationTest {
    
    private OSMRelation.Builder m = new OSMRelation.Builder(0);
    private List<Member> l = new ArrayList<>();
    private Attributes a;
    private OSMRelation r = new OSMRelation(0, l, a);
    private Type type = OSMRelation.Member.Type.WAY;
    private String role = "in";
    private OSMWay newMember = newWay();
    
    @Test
    public void addMemberTest(){
        m.addMember(type, role, newMember);
        
    }

}
