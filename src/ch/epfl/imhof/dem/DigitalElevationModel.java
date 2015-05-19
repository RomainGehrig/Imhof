package ch.epfl.imhof.dem;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.PointGeo;

/**
 * Interface d'un modèle numérique du terrain
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public interface DigitalElevationModel extends AutoCloseable {
    Vector3 normalAt(PointGeo x);
}
