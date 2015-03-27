package ch.epfl.imhof;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * représente une carte projetée, composée d'entités géométriques attribuées
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Map {
    private final List<Attributed<PolyLine>> polyLines;
    private final List<Attributed<Polygon>> polygons;

    /**
     * construit une carte à partir des listes de polylignes et polygones attribués donnés
     * @param polyLines Listes de polylignes attribués donnés
     * @param polygons Listes de polygones attribués donnés
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons){
        this.polyLines = Collections.unmodifiableList(new ArrayList<>(polyLines));
        this.polygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * retourne la liste des polylignes attribuées de la carte
     * @return la liste des polylignes attribuées de la carte
     */
    public List<Attributed<PolyLine>> polyLines(){
        return polyLines;
    }

    /**
     * retourne la liste des polygones attribués de la carte
     * @return al liste des polygones attribués de la carte
     */
    public List<Attributed<Polygon>> polygons(){
        return polygons;
    }

    /**
     * sert de bâtisseur à la classe Map
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder{
        private final List<Attributed<PolyLine>> polyLines = new ArrayList<>();
        private final List<Attributed<Polygon>> polygons = new ArrayList<>();

        /**
         * ajoute une polyligne attribuée à la carte en cours de construction
         * @param newPolyLine Polyligne attribuée
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine){
            polyLines.add(newPolyLine);
        }

        /**
         * ajoute un polygone attribué à la carte en cours de construction
         * @param newPolygon Polygone attribué
         */
        public void addPolygon(Attributed<Polygon> newPolygon){
            polygons.add(newPolygon);
        }

        /**
         * construit une carte avec les polylignes et polygones ajoutés jusqu'à présent au bâtisseur
         * @return Une carte avec les polylignes et polygones ajoutés jusqu'à présent au bâtisseur
         */
        public Map build(){
            return new Map(polyLines, polygons);
        }
    }

}
