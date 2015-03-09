package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * représente un nœud OSM
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OSMNode extends OSMEntity{
    
    private PointGeo position;
    
    /**
     * construit un nœud OSM avec l'identifiant, la position et les attributs donnés
     * @param id L'identifiant unique
     * @param position La position à la surface de la Terre
     * @param attributes Attributs données
     */
    public OSMNode(long id, PointGeo position, Attributes attributes){
        super(id, attributes);
        if(position != null){
            this.position = position;
        } else {
            throw new NullPointerException("position est nulle");
        }
        
    }
    
    /**
     * retourne la position du nœud
     * @return la position du noeud
     */
    public PointGeo position(){
        return position;
    }
    
    /**
     * sert de bâtisseur à la classe OSMNode et permet de construire un nœud en plusieurs étapes
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder extends OSMEntity.Builder {
        
        private long id;
        private PointGeo position;
        private Attributes.Builder attributes = new Attributes.Builder();
        private OSMNode node;
        
        /**
         * construit un bâtisseur pour un nœud ayant l'identifiant et la position donnés
         * @param id L'identifiant unique
         * @param position La position donnée
         */
        public Builder(long id, PointGeo position){
            super(id);
            if(position != null){
                this.position = position;
            } else{
                throw new NullPointerException("position est null");
            }
        }
        
        /**
         * construit un nœud OSM avec l'identifiant et la position passés au constructeur, et les éventuels attributs ajoutés jusqu'ici au bâtisseur. 
         * @throws IllegalStateException si le nœud en cours de construction est incomplet 
         * (c-à-d si la méthode setIncomplete a été appelée sur ce bâtisseur depuis sa création)
         * @return 
         */
        public OSMNode build(){
            if(isIncomplete()){
                throw new IllegalStateException("le noeud en cours de construction est incomplet");
            } else{
                node = new OSMNode(id, position, attributes.build());
                return node;
            }
        }
        
    }
    
    
    
}
