
package ch.epfl.imhof.osm;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Collections;

public final class OSMMap {
    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        this.ways = Collections.unmodifiableList(new ArrayList<>(ways));
        this.relations = Collections.unmodifiableList(new ArrayList<>(relations));
    }
    
    public List<OSMWay> ways() {
        return ways;
    }
    public List<OSMRelation> relations() {
        return relations;
    }

    public final static class Builder {
        private final Map<Long, OSMNode> nodes;
        private final Map<Long, OSMWay> ways;
        private final Map<Long, OSMRelation> relations;
        
        public Builder() {
            nodes = new LinkedHashMap<>();
            ways = new LinkedHashMap<>();
            relations = new LinkedHashMap<>();
        }
        public void addNode(OSMNode newNode) {
            nodes.put(newNode.id(), newNode);
        }
        public OSMNode nodeForId(long id) {
            return nodes.get(id);
        }
        public void addWay(OSMWay newWay) {
            ways.put(newWay.id(), newWay);
        }
        public OSMWay wayForId(long id) {
            return ways.get(id);
        }
        public void addRelation(OSMRelation newRelation) {
            relations.put(newRelation.id(), newRelation);
        }
        public OSMRelation relationForId(long id) {
            return relations.get(id);
        }
        public OSMMap build() {
            return new OSMMap(ways.values(), relations.values());
        }
    }
}
