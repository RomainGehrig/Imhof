
package ch.epfl.imhof.geometry;

import java.util.List;

import ch.epfl.imhof.

/**
 * représente une polyligne fermée
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class ClosedPolyLine extends PolyLine {
    /**
     * construit une polyligne fermée ayant les sommets données
     * @param points
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    public boolean isClosed() {
        return true;
    }
    
    /**
     * retourne l'aire de la polyligne (toujours positive)
     * @return l'aire
     */
    public double area(){
        double area = 0;
        for(int i = 0; i < points.size(); ++i){
            area += getPoint(i).x()*(getPoint(i+1).y() - getPoint(i-1).y());
        }
        
        if(area<0){
            area = -area;
        }
        
        return (area/2);
    }

    /**
     * permet d'obtenir un sommet d'indice généralisé
     * @param indice
     * @return sommet se trouvant à cet endroit
     */
    private Point getPoint(int n) {
        return points.get(Math.floorMod(n, points.size()));
    }

    /**
     * retourne vrai si le point p se trouve à gauche d'une ligne définie par deux points p1, p2
     * @param p
     * @param p1
     * @param p2
     * @return
     */
    private boolean isLeft(Point point, Point p1, Point p2) {
        double x = point.x();
        double y = point.y();

        double x1 = p1.x();
        double y1 = p1.y();

        double x2 = p2.x();
        double y2 = p2.y();
        return (x1-x)*(y2-y) > (x2-x)*(y1-y);
    }

    /**
     * retourne vrai si et seulement si le point donné est à l'intérieur de la polyligne
     * @param p
     * @return
     */
    public boolean containsPoint(Point p){
        int indice = 0;
        for(int i = 0; i<points.size(); ++i){
            Point p1 = getPoint(i);
            Point p2 = getPoint(i+1);
            
            if(p1.y() <= p.y()){
                if(p2.y() > p.y() && isLeft(p, p1, p2)){
                    ++indice;
                }  
            } else{
                if(p2.y() <= p.y() && isLeft(p, p2, p1)){
                    --indice;
                }   
            }
        }
        
        return (indice != 0);
    }

}
