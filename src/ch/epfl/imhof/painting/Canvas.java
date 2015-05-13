package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

public interface Canvas {
    /**
     * permet de dessiner sur la toile une polyligne donnée avec une style de ligne donné
     * @param polyline Polyligne donnée
     * @param style style de ligne donné
     */
    public abstract void drawPolyLine(PolyLine polyline, LineStyle style);

    /**
     * permet de dessiner sur la toile un polygone donné avec une couleur donnée
     * @param polygon Polygone donné
     * @param color Couleur donnée
     */
    public abstract void drawPolygon(Polygon polygon, Color color);
}
