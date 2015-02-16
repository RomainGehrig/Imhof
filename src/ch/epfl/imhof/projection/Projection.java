package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

public interface Projection {
    public Point project(PointGeo point);
    public PointGeo inverse(Point point);
}
