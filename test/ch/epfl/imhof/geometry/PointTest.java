package ch.epfl.imhof.geometry;

import static org.junit.Assert.assertEquals;
import java.util.function.Function;
import java.util.Random;

import org.junit.Test;


public class PointTest {
    private static final double DELTA = 0.000001;

    @Test
    public void xGetterReturnsX() {
        for (double x = -100; x <= 100; x += 12.32)
            assertEquals(x, new Point(x, 0).x(), DELTA);
    }

    @Test
    public void yGetterReturnsY() {
        for (double y = -100; y <= 100; y += 12.32)
            assertEquals(y, new Point(0, y).y(), DELTA);
    }

    @Test
    public void alignedCoordinateChangeIdentity() {
        Random rand = new Random();

        for (int i=0; i<10; ++i) {
            for (int j=0; j<10; ++j) {
                float x1 = rand.nextFloat()*10;
                float y1 = rand.nextFloat()*10;

                float x2 = rand.nextFloat()*10;
                float y2 = rand.nextFloat()*10;

                if (x1 == x2 || y1 == y2)
                    continue;

                Point p1 = new Point(x1,y1);
                Point p2 = new Point(x2,y2);
                Function<Point, Point> id = Point.alignedCoordinateChange(p1,p2,p1,p2);

                Point p3 = new Point(rand.nextFloat()*i, rand.nextFloat()*j);
                Point p4 = id.apply(p3);

                assertEquals(p3.x(), p4.x(), 0);
            }
        }
    }

    @Test
    public void alignedCoordinateChangeTranslation() {
        Point a = new Point(0,0);
        Point b = new Point(1,1);
        Point c = new Point(1,1);
        Point d = new Point(2,2);

        Function<Point,Point> f = Point.alignedCoordinateChange(a,b,c,d);
        Point a_ = f.apply(a);
        Point b_ = f.apply(b);

        assertEquals(a_.x(), c.x(), 0);
        assertEquals(a_.y(), c.y(), 0);

        assertEquals(b_.x(), d.x(), 0);
        assertEquals(b_.y(), d.y(), 0);

    }
}
