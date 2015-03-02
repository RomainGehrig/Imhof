package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Représente une projection 
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public interface Projection {
    /**
     * Projette sur le plan le point reçu en argument
     * @param point Le point en coordonnées cartésiennes
     * @return Le point en coordonnées cartésiennes
     */
    public Point project(PointGeo point);

    /**
     * 'Dé-projette' le point du plan reçu en argument
     * @param point Le point en coordonnées cartésiennes
     * @return Le point en coordonnées sphériques
     */
    public PointGeo inverse(Point point);
}
