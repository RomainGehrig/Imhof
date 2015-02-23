
package ch.epfl.imhof.geometry;

/**
 * représente un point dans le plan, en coordonnées cartésiennes
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 */
public final class Point {
    private final double x;
    private final double y;
    
    /**
     * construit un point avec les coordonnées données
     * @param x coordonnée x
     * @param y coordonnée y
     */
    public Point(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    /**
     * 
     * @return coordonnée x
     */
    public double x(){
        return x;
    }
    
    /**
     * 
     * @return coordonnée y
     */
    public double y(){
        return y;
    }
    
}
