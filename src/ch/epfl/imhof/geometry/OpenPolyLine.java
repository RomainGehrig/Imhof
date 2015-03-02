
package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Représente une polyligne ouverte
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OpenPolyLine extends PolyLine {

    /**
     * Construit une polyligne ouverte de sommets donnés
     * @param points Liste de sommets
     */
    public OpenPolyLine(List<Point> points) {
        super(points);
    }

    @Override
    public boolean isClosed() {
        return false;
    }
}
