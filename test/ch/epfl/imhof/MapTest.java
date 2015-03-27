
package ch.epfl.imhof;

import org.junit.Test;

public class MapTest {

    @Test
    public void builderTest() {
        Map.Builder b = new Map.Builder();
        b.addPolygon(null);
        b.addPolyLine(null);
        b.build();
    }

    @Test
    public void builderTest2() {
        Map.Builder b = new Map.Builder();
        b.build();
    }

    @Test
    public void builderTest3() {
        Map.Builder b = new Map.Builder();
    }
}
