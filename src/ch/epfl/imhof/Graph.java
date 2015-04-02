package ch.epfl.imhof;

import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * représente un graphe non orienté
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 * @param <N> représente le type des nœuds du graphe
 */
public final class Graph <N> {

    private Map<N, Set<N>> neighbors;

    /**
     * construit un graphe non orienté avec la table d'adjacence donnée
     * @param neighbors La table d'adjacence donnée
     */
    public Graph(Map<N, Set<N>> neighbors) {
        this.neighbors = new HashMap<N, Set<N>>();
        for (Entry<N, Set<N>> neighbor: neighbors.entrySet()) {
            this.neighbors.put(neighbor.getKey(), Collections.unmodifiableSet(new HashSet<N>(neighbor.getValue())));
        }
        this.neighbors = Collections.unmodifiableMap(new HashMap<>(this.neighbors));
    }

    /**
     * retourne l'ensemble des nœuds du graphe
     * @return l'ensemble des noeuds du graphe
     */
    public Set<N> nodes(){
        return neighbors.keySet();
    }

    /**
     * retourne l'ensemble des nœuds voisins du nœud donné
     * @param node Le noeud donnée
     * @return l'ensemble des noeuds voisins du noeud donné
     * @thors IllegalArgumentException si le nœud donné ne fait pas partie du graphe
     */
    public Set<N> neighborsOf(N node){
        if(!neighbors.containsKey(node)){
            throw new IllegalArgumentException("le noeud donné ne fait pas partie du graphe");
        }
        return neighbors.get(node);
    }


    /**
     * sert de bâtisseur à la classe Graph
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     * @param <N> représente le type des noeuds du graphe
     */
    public static final class Builder <N>{
        private Map<N, Set<N>> neighbors = new HashMap<>();

        /**
         * ajoute le nœud donné au graphe en cours de construction, s'il n'en faisait pas déjà partie
         * @param n Le noeud donné à ajouter
         */
        public void addNode(N n){
            if (!neighbors.containsKey(n))
                neighbors.put(n, new HashSet<>());
        }

        /**
         * ajoute une arête entre les deux nœuds donnés au graphe en cours de construction
         * @param n1 A ajouter à l'ensemble des voisins du second noeud
         * @param n2 A ajouter à l'ensemble des voisins du premier noeud
         * @throws IllegalArgumentException si l'un des nœuds n'appartient pas au graphe en cours de construction
         */
        public void addEdge(N n1, N n2){
            if(!neighbors.containsKey(n1) || !neighbors.containsKey(n2)){
                throw new IllegalArgumentException("L'un des noeuds n'apparient pas au graphe en cours de construction");
            }
            neighbors.get(n1).add(n2);
            neighbors.get(n2).add(n1);
        }

        /**
         * construit le graphe composé des nœuds et arêtes ajoutés jusqu'à présent au bâtisseur
         * @return le graphe composé des noeuds et arêtes ajoutés
         */
        public Graph<N> build(){
            return new Graph<N>(neighbors);
        }
    }


}
