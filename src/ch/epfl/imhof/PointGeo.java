
package ch.epfl.imhof;

public final class PointGeo {

    private final double longitude;
    private final double latitude;
    
    public PointGeo(double longitude, double latitude) {
        if (longitude < -Math.PI || longitude > Math.PI)
            throw new IllegalArgumentException("Longitude is not in [-PI, PI] range!");
        if (latitude < -Math.PI/2 || latitude > Math.PI/2)
            throw new IllegalArgumentException("Latitude is not in [-PI/2, PI/2] range!");
        
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double longitude() {
        return this.longitude;
    }

    public double latitude() {
        return this.latitude;
    }

}
