
package ch.epfl.imhof.projection;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.PointGeo;

/**
 * représente la projection CH1903
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class CH1903Projection implements Projection {

    /**
     * projette un point geographique en un point cartésien en utilisant
     * la projection CH1903
     * @params point, point géographique
     * @return un point cartésien
     */
    public Point project(PointGeo point) {
        double degreeLong = Math.toDegrees(point.longitude());
        double degreeLat = Math.toDegrees(point.latitude());

        double lambda = 1e-4 * (degreeLong * 3600 -  26782.5);  // == longitude
        double psi    = 1e-4 * (degreeLat  * 3600 - 169028.66); // == latitude

        double x = 600072.37
                 + 211455.93 * lambda
                 -  10938.51 * lambda * psi
                 -      0.36 * lambda * Math.pow(psi, 2)
                 -     44.54 * Math.pow(lambda, 3);

        double y = 200147.07
                 + 308807.95 * psi
                 +   3745.25 * Math.pow(lambda, 2)
                 +     76.63 * Math.pow(psi, 2)
                 -    194.56 * Math.pow(lambda, 2) * psi
                 +    119.79 * Math.pow(psi, 3);

        return new Point(x, y);
    }

    /**
     * projette un point geographique en un point géographique en utilisant
     * la projection inverse de CH1903
     * @param un point cartésien
     * @return point, point géographique
     */
    public PointGeo inverse(Point point) {
        double x = (point.x()-6e5) / 1e6;
        double y = (point.y()-2e5) / 1e6;

        double lambda0 = 2.6779094
                       + 4.728982 * x
                       + 0.791484 * x * y
                       + 0.1306   * x * Math.pow(y, 2)
                       - 0.0436   * Math.pow(x, 3);
        
        double psi0 = 16.9023892
                    +  3.238272 * y
                    -  0.270978 * Math.pow(x, 2)
                    -  0.002528 * Math.pow(y, 2)
                    -  0.0447   * Math.pow(x, 2) * y
                    -  0.0140   * Math.pow(y, 3);
        
        double lambda = Math.toRadians(lambda0 * 100 / 36); // longitude (converted to radians)
        double psi    = Math.toRadians(psi0 * 100 / 36);    // latitude  (converted to radians)
        
        return new PointGeo(lambda, psi);
    }
}
