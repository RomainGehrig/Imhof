package ch.epfl.imhof.osm;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

/**
 * représente une carte OpenStreetMap, c'est-à-dire un ensemble de chemins et de relations
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OSMMap {

    private final List<OSMWay> ways;
    private final List<OSMRelation> relations;

    /**
     * construit une carte OSM avec les chemins et les relations donnés
     * @param ways Les chemins donnés
     * @param relations Les relations données
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations){
        this.ways = Collections.unmodifiableList(new ArrayList(ways));
        this.relations = Collections.unmodifiableList(new ArrayList(relations));
    }

    /**
     * retourne la liste des chemins de la carte
     * @return la liste des chemins de la carte
     */
    public List<OSMWay> ways(){
        return ways;
    }

    /**
     * retourne la liste des relations de la carte
     * @return la liste des relations de la carte
     */
    public List<OSMRelation> relations(){
        return relations;
    }

    /**
     * sert de bâtisseur à la classe OSMMap
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder {
        private final Map<Long, OSMNode> nodes;
        private final Map<Long, OSMWay> ways;
        private final Map<Long, OSMRelation> relations;

        public Builder() {
            nodes = new HashMap<>(); // LinkedHashMap? Si l'ordre d'insertion compte
            ways = new HashMap<>();
            relations = new HashMap<>();
        }

        public void addNode(OSMNode n) {
            nodes.put(n.id(), n);
        }
        /**
         * retourne le nœud dont l'identifiant unique est égal à celui donné
         * @param id L'identifiant unique donné
         * @return le noeud dont l'identifiant unique est égal à ceui donné
         *         ou null si ce nœud n'a pas été ajouté précédemment au bâtisseur
         */
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

        /**
         * construit une carte OSM avec les chemins et les relations ajoutés jusqu'à présent
         * @return une carte OSM
         */
        public OSMMap build(){
            return new OSMMap(ways.values(), relations.values());
        }
    }
}
