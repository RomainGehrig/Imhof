package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * Représente une polyligne fermée
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class ClosedPolyLine extends PolyLine {
    /**
     * Construit une polyligne fermée ayant les sommets données
     * @param points Liste des sommets
     */
    public ClosedPolyLine(List<Point> points) {
        super(points);
    }

    public boolean isClosed() {
        return true;
    }

    /**
     * Retourne l'aire de la polyligne (toujours positive)
     * @return l'aire
     */
    public double area(){
        double area = 0;
        for(int i = 0; i < points().size(); ++i){
            area += getPoint(i).x()*(getPoint(i+1).y() - getPoint(i-1).y());
        }

        if(area<0){
            area = -area;
        }

        return (area/2);
    }

    /**
     * Permet d'obtenir un sommet d'indice généralisé
     * @param n L'indice (positif ou négatif)
     * @return sommet se trouvant à cet endroit
     */
    private Point getPoint(int n) {
        return points().get(Math.floorMod(n, points().size()));
    }

    /**
     * Retourne vrai si le point p se trouve à gauche d'une ligne définie par deux points p1, p2
     * @param point Point à vérifier
     * @param p1    1er pt de la ligne
     * @param p2    2e pt de la ligne
     * @return Vrai si le pt se trouve à gauche, faux sinon
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
     * Retourne vrai si et seulement si le point donné est à l'intérieur de la polyligne
     * @param p Le point à vérifier
     * @return Vrai si le pt se trouve à l'intérieur, faux sinon
     */
    public boolean containsPoint(Point p){
        int indice = 0;
        for(int i = 0; i<points().size(); ++i){
            Point p1 = getPoint(i);
            Point p2 = getPoint(i+1);

            if(p1.y() <= p.y()){
                if(p2.y() > p.y() && isLeft(p, p1, p2)){
                    ++indice;
                }
            } else {
                if(p2.y() <= p.y() && isLeft(p, p2, p1)){
                    --indice;
                }
            }
        }
        return (indice != 0);
    }

}
