
package ch.epfl.imhof.geometry;

/**
 * Représente un point dans le plan, en coordonnées cartésiennes
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 */
public final class Point {
    private final double x;
    private final double y;
    
    /**
     * Construit un point avec les coordonnées données
     * @param x coordonnée x
     * @param y coordonnée y
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * Getter sur la coordonnée x
     * @return Coordonnée x
     */
    public double x(){
        return x;
    }
    
    /**
     * Getter sur la coordonnée y
     * @return coordonnée y
     */
    public double y(){
        return y;
    }
    
}
