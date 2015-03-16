package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    public final static class Builder{
        private List<OSMNode> nodes;
        private List<OSMWay> ways;
        private List<OSMRelation> relations;
        
        /**
         * ajoute le nœud donné au bâtisseur
         * @param newNode
         */
        public void addNode(OSMNode newNode){
            nodes.add(newNode);
        }
        
        /**
         * retourne le nœud dont l'identifiant unique est égal à celui donné
         * @param id L'identifiant unique donné
         * @return le noeud dont l'identifiant unique est égal à ceui donné
         *         ou null si ce nœud n'a pas été ajouté précédemment au bâtisseur
         */
        public OSMNode nodeForId(long id){
            for(OSMNode n : nodes){
                if(n.id() == id){
                    return n;
                }
            }
            return null;
        }
        
        /**
         * ajoute le chemin donné à la carte en cours de construction
         * @param newWay
         */
        public void addWay(OSMWay newWay){
            ways.add(newWay);
        }
        
        /**
         * retourne le chemin dont l'identifiant unique est égal à celui donné
         * @param id L'identifiant unique donné
         * @return le chemin dont l'identifiant unique est égal à celui donné, 
         *         ou null si ce chemin n'a pas été ajouté précédemment au bâtisseur
         */
        public OSMWay wayForId(long id){
            for(OSMWay w : ways){
                if(w.id() == id){
                    return w;
                }
            }
            return null;
        }
        
        /**
         * ajoute la relation donnée à la carte en cours de construction
         * @param newRelation La relation donnée
         */
        public void addRelation(OSMRelation newRelation){
            relations.add(newRelation);
        }
        
        /**
         * retourne la relation dont l'identifiant unique est égal à celui donné
         * @param id L'identifiant unique donné
         * @return la relation dont l'identifiant unique est égal à celui donné, 
         *         ou null si cette relation n'a pas été ajoutée précédemment au bâtisseur
         */
        public OSMRelation relationForId(long id){
            for(OSMRelation r : relations){
                if(r.id() == id){
                    return r;
                }
            }
            return null;
        }
        
        /**
         * construit une carte OSM avec les chemins et les relations ajoutés jusqu'à présent
         * @return une carte OSM
         */
        public OSMMap build(){
            return new OSMMap(ways, relations);
        }
        
    }
}
