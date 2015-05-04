package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * représente un chemin OSM
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OSMWay extends OSMEntity{

    private final List<OSMNode> nodes;

    /**
     * construit un chemin étant donnés son identifiant unique, ses nœuds et ses attributs
     * @param id L'identifiant unique
     * @param nodes Les noeuds
     * @param attributes Les attributs
     * @throws IllegalArgumentException si la liste de nœuds possède moins de deux éléments
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) {
        super(id, attributes);

        if (nodes.size() < 2)
            throw new IllegalArgumentException("Il faut au moins 2 noeuds pour contruire une OSMWay.");

        this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
    }

    /**
     * retourne le nombre de nœuds du chemin
     * @return le nombre de noeuds du chemin
     */
    public int nodesCount() {
        return nodes.size();
    }

    /**
     * retourne la liste des nœuds du chemin
     * @return la liste des noeuds du chemin
     */
    public List<OSMNode> nodes() {
        return nodes;
    }

    /**
     * retourne la liste des nœuds du chemin sans le dernier si celui-ci est identique au premier
     * @return la liste des noeuds du chemin sans le dernier si celui-ci est identique au premier
     */
    public List<OSMNode> nonRepeatingNodes() {
        // Note: nodes est garanti de posséder au moins 2 noeuds
        return new ArrayList<>(nodes.subList(0, nodes.size() + (isClosed() ? -1 : 0)));
    }

    /**
     * retourne le premier nœud du chemin
     * @return le premeir noeud du chemin
     */
    public OSMNode firstNode(){
        return nodes.get(0);
    }

    /**
     * retourne le dernier noeud du chemin
     * @return le dernier noeud du chemin
     */
    public OSMNode lastNode(){
        return nodes.get(nodesCount()-1);
    }

    /**
     * retourne vrai si et seulement si le chemin est fermé
     * @return vrai ssi le chemin est fermé, c-à-d que son premier nœud est identique à son dernier nœud
     */
    public boolean isClosed(){
        return firstNode().equals(lastNode());
    }

    /**
     * sert de bâtisseur à la classe OSMWay et permet de construire un chemin en plusieurs étapes
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder extends OSMEntity.Builder {

        private final List<OSMNode> nodes = new ArrayList<>();

        /**
         * construit un bâtisseur pour un chemin ayant l'identifiant donné
         * @param id L'identifiant unique donné
         */
        public Builder(long id) {
            super(id);
        }

        /**
         * ajoute un nœud à (la fin) des nœuds du chemin en cours de construction
         * @param newNode Un nouveau noeud à ajouter à al fin des noeuds
         */
        public void addNode(OSMNode newNode){
            nodes.add(newNode);
        }

        /**
         * construit et retourne le chemin ayant les nœuds et les attributs ajoutés jusqu'à présent
         * @return le chemin ayant les noeuds et les attributs ajoutés jusqu'à présent
         * @throws IllegalStateException si le chemin en cours de construction est incomplet
         */
        public OSMWay build() {
            if(isIncomplete())
                throw new IllegalStateException("OSMWay incomplète: ne peut pas build");

            return new OSMWay(super.id(), nodes, attributes.build());
        }

        /**
         * @inheritDoc
         * redéfinit la méthode isIncomplete héritée de sa super-classe
         * afin qu'un chemin en cours de construction mais possèdant moins de deux nœuds soit également considéré comme incomplet,
         * même si la méthode setIncomplete n'a pas été appelée
         */
        public boolean isIncomplete() {
            return super.isIncomplete() || (nodes.size() < 2);
        }
    }
}
