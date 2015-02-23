package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;

/**
 * représente une projection 
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public interface Projection {
    /**
     * projette sur le plan le point reçu en argument
     * @param point
     * @return le point en coordonnées cartésiennes
     */
    public Point project(PointGeo point);

    /**
     * 'dé-projette' le point du plan reçu en argument
     * @param point
     * @return le point en coordonnées sphériques
     */
    public PointGeo inverse(Point point);
}
