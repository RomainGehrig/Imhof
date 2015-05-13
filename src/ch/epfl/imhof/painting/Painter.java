package ch.epfl.imhof.painting;

import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * représente un peintre
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public interface Painter {

    /**
     * dessine une carte sur une toile
     * @param map La carte passée en argument
     * @param canvas La toile passée en argument
     */
    public abstract void drawMap(Map map, Canvas canvas);

    /**
     * retourne un peintre dessinant l'intérieur de tous les polygones de la carte qu'il reçoit avec la couleur passée en argument
     * @param color La couleur passée en argument
     * @return un peintre dessinant l'intérieur de tous les polygones de la carte
     */
    public static Painter polygon(Color color) {
        return (map, canvas) -> {
            for(Attributed<Polygon> p : map.polygons()){
                canvas.drawPolygon(p.value(), color);
            }
        };
    }

    /**
     * retourne un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec le style de ligne passé en argument
     * @param linestyle Le style de ligne passé en argument
     * @return un peintre dessinant toutes les lignes de la carte
     */
    public static Painter line(LineStyle linestyle) {
        return (map, canvas) -> {
            for(Attributed<PolyLine> l : map.polyLines() ){
                canvas.drawPolyLine(l.value(), linestyle);
            }
        };
    }

    /**
     * retourne un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec les cinq paramètres de dessin d'une ligne passés en argument
     * @param width La largeur passeé en argument
     * @param color La couleur passée en argument
     * @param cap La terminaison des lignes passée en argument
     * @param join La jointure des segments passée en argument
     * @param dashingPattern L'alternance des sections opaques et transparentes passée en argument
     * @return un peintre dessinant toutes les lignes de la carte
     */
    public static Painter line(float width, Color color, LineCap cap, LineJoin join, float[] dashingPattern) {
        return line(new LineStyle(width, color, cap, join, dashingPattern));
    }

    /**
     * retourne un peintre dessinant toutes les lignes de la carte qu'on lui fournit avec les deux paramètres de dessin d'une ligne passés en argument
     * @param width La largeur passée en argument
     * @param color La couleur passée en argument
     * @return un peintre dessinant toutes les lignes de la carte
     */
    public static Painter line(float width, Color color) {
        return line(new LineStyle(width, color));
    }

    /**
     * retourne un peintre dessinant toutes les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit avec le style de ligne passé en argument
     * @param linestyle Le style de ligne passé en argument
     * @return un peintre dessinant toutes les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     */
    public static Painter outline(LineStyle linestyle) {
        return (map, canvas) -> {
            for(Attributed<Polygon> p : map.polygons()){
                canvas.drawPolyLine(p.value().shell(), linestyle);
                for(ClosedPolyLine h  : p.value().holes()){
                    canvas.drawPolyLine(h, linestyle);
                }
            }

        };
    }

    /**
     * retourne un peintre dessinant toutes les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit avec les cinq paramètres de dessin d'une ligne passés en argument
     * @param width La largeur passeé en argument
     * @param color La couleur passée en argument
     * @param cap La terminaison des lignes passée en argument
     * @param join La jointure des segments passée en argument
     * @param dashingPattern L'alternance des sections opaques et transparentes passée en argument
     * @return un peintre dessinant toutes les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     */
    public static Painter outine(float width, Color color, LineCap cap, LineJoin join, float[] dashingPattern) {
        return outline(new LineStyle(width, color, cap, join, dashingPattern));
    }

    /**
     * retourne un peintre dessinant toutes les pourtours de l'enveloppe et des trous de tous les polygones de la carte qu'on lui fournit avec les deux paramètres de dessin d'une ligne passés en argument
     * @param width La largeur passée en argument
     * @param color La couleur passée en argument
     * @returnun peintre dessinant toutes les pourtours de l'enveloppe et des trous de tous les polygones de la carte
     */
    public static Painter outline(float width, Color color) {
        return outline(new LineStyle(width, color));
    }

    /**
     * retourne un peintre se comportant comme celui auquel on l'applique, si ce n'est qu'il ne considère que les éléments de la carte satisfaisant le prédicat passé en argument
     * @param predicate Le prédicat passé en argument
     * @return un peintre se comportant comme celui auquel on l'applique
     */
    public default Painter when(Predicate<Attributed<?>> predicate) {
        return (map, canvas) -> {
            Map.Builder mapBuilder = new Map.Builder();
            map.polyLines().stream().filter(predicate).forEach(mapBuilder :: addPolyLine);
            map.polygons().stream().filter(predicate).forEach(mapBuilder :: addPolygon);
            drawMap(mapBuilder.build(), canvas);
        };
    }

    /**
     * retourne un peintre dessinant d'abord la carte produite par ce second peintre passé en argument puis, par dessus, la carte produite par le premier peintre
     * @param painter Le second peintre passé en argument
     * @return un peintre dessinant d'abord la carte produite par ce second peintre puis, par dessus, la carte produite par le premier peintre
     */
    public default Painter above(Painter painter) {
        return (map, canvas) -> {
            painter.drawMap(map, canvas);
            this.drawMap(map, canvas);
        };
    }

    /**
     * retourne un peintre utilisant l'attribut layer attaché aux entités de la carte pour la dessiner par couches, c-à-d en dessinant d'abord tous les entités de la couche –5, puis celle de la couche –4, et ainsi de suite jusqu'à la couche +5.
     * @return un peintre utilisant l'attribut layer attaché aux entités de la carte pour la dessiner par couches
     */
    public default Painter layered(){
        return (map, canvas) -> {
            Painter main = when(Filters.onLayer(-5));
            Painter current = main;
            for(int i = -4 ; i <= 5; ++i){
                current = when(Filters.onLayer(i));
                main = main.above(current);
            }
        };
    }

}
