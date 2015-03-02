
package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * Représente la projection équirectangulaire
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class EquirectangularProjection implements Projection{
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }
    
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }
    
}
