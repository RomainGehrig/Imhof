
package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Représente une polyligne
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public abstract class PolyLine {
    protected final List<Point> points;

    /**
     * Retourne vrai si et seulement si la polyligne est fermée
     * @return boolean 
     */
    public abstract boolean isClosed();

    /**
     * Construit une polyligne avec les sommets donnés.
     * @param points Liste des sommets
     * @throws IllegalArgumentException
     *          si la liste des sommets est nulle
     *          si la liste des sommets est vide
     */
    public PolyLine(List<Point> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Array should not be null nor empty");
        }
        this.points = Collections.unmodifiableList(new ArrayList<>(points));
    }

    /**
     * Retourne le premier sommet de la polyligne
     * @return le premier point
     */
    public Point firstPoint() {
        return points.get(0);
    }

   /**
    * Getter pour la liste des sommets
    * @return liste des sommets de la polyligne
    */
    public List<Point> points() {
        return points;
    }
    
    /**
     * Sert de bâtisseur aux deux sous-classes de PolyLine 
     * et permet de construire une polyligne en plusieurs étapes
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder{
        List<Point> p = new ArrayList<Point>();
        
        /**
         * Ajoute le point donné à la fin de la liste des sommets de la polyligne en cours de construction
         * @param newPoint Sommet à ajouter
         */
        public void addPoint(Point newPoint){
            p.add(newPoint);
        }
        
        /**
         * Construit et retourne une polyligne ouverte avec les points ajoutés jusqu'à présent
         * @return polyligne ouverte
         */
        public OpenPolyLine buildOpen(){
            return new OpenPolyLine(p);
        }
        
        /**
         * Construit et retourne une polyligne fermée avec les points ajoutés jusqu'à présent
         * @return polyligne fermée
         */
        public ClosedPolyLine buildClosed(){
            return new ClosedPolyLine(p);
        }
    }
}
