
package ch.epfl.imhof.geometry;

import java.util.function.Function;

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
    
    /**
     * transforme un point exprimé dans le système de départ en un point exprimé dans le système d'arrivée
     * @param a Point a dans le repère de départ
     * @param b Point b dans le repère de départ
     * @param c Point c dans le repère d'arrivée
     * @param d Point d dans le repère d'arrivée
     * @return un nouveau point dans le repère d'arrivée à partir d'un point auquel on l'applique
     */
    public static Function<Point, Point> alignedCoordinateChange(Point a, Point b, Point c, Point d){
        if(a.x == b.x || a.y == b.y){
            throw new IllegalArgumentException();
        }
        double alphaX = (c.x - d.x)/(a.x-b.x) ;
        double betaX  = c.x - (a.x*(c.x-d.x)/(a.x-b.x));
        double alphaY = (c.y - d.y)/(a.y-b.y) ;;
        double betaY = c.y - (a.y*(c.y-d.y)/(a.y-b.y));;
        return p -> new Point(alphaX*p.x + betaX, alphaY*p.y + betaY);
    }
    
}
