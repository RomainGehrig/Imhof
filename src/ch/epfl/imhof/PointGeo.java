package ch.epfl.imhof;

/**
 * Représente un point à la surface de la Terre, dont la position est exprimée en coordonnées sphériques dans le système WGS 84
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class PointGeo {
    private final double longitude;
    private final double latitude;

    /**
     * Construit un point avec la longitude et la latitude données (en radians)
     * @param longitude la longitude du point, en radians
     * @param latitude  la latitude du point, en radians
     * @throws IllegalArgumentException
     *          si la longitude est invalide, c-a-d hors de l'intervalle[-π; π]
     * @throws IllegalArgumentException
     *          si la latitude est invalide, c-a-d hors de l'intervalle [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude){
        if(longitude < -Math.PI || longitude > Math.PI){
            throw new IllegalArgumentException("la longitude n'est pas dans l'intervalle");
        }
        if(latitude < -Math.PI/2 || latitude > Math.PI/2){
            throw new IllegalArgumentException("la latitude n'est pas dans l'intervalle");
        }

        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * Getter sur la longitude
     * @return la longitude
     */
    public double longitude(){
        return longitude;
    }

    /**
     * Getter sur la latitude
     * @return la latitude
     */
    public double latitude(){
        return latitude;
    }
}
