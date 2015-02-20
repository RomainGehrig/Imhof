
package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public abstract class PolyLine {
    public abstract boolean isClosed();
    protected final List<Point> points;

    public PolyLine(List<Point> points) {
        if (points == null) {
            throw new IllegalArgumentException("Array should not be null");
        }
        this.points = Collections.unmodifiableList(new ArrayList<>(points));
    }

    public Point firstPoint() {
        return this.points.get(0);
    }
    
    public List<Point> points() {
        return this.points;
    }

}
  
