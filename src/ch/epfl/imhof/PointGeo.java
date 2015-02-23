package ch.epfl.imhof;

/**
 * représente un point à la surface de la Terre, dont la position est exprimée en coordonnées sphériques dans le système WGS 84
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class PointGeo {
    final double longitude;
    final double latitude;
    
    
    /**
     * construit un point avec la longitude et la latitude données (en radians)
     * @param longitude
     *          la longitude du point, en radians
     * @param latitude
     *          la latitude du point, en radians
     * @throw IllegalArgumentException
     *          si la longitude est invalide, c-a-d hors de l'intervalle[-π; π]
     * @throw IllegalArgumentException
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
     * 
     * @return la longitude
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * 
     * @return la latitude
     */
    public double latitude(){
        return latitude;
    }
}
