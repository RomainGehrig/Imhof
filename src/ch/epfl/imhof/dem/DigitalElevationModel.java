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

    /**
     * Retrourne le vecteur normal au terrain au point indiqué
     * @param pt Le point géo où l'on veut le vecteur normal
     * @return le vecteur normal (et normalisé) au terrain
     */
    Vector3 normalAt(PointGeo x);
    /**
     * Retrourne la hauteur (par rapport au niveau de la mer),
     * au point spécifié
     * @param pt Le point géo où l'on veut la hauteur
     * @return hauteur en mètres
     */
    double getHeightAt(PointGeo x);
}
