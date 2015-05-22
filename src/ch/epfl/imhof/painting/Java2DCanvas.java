package ch.epfl.imhof.painting;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 *
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public class Java2DCanvas implements Canvas {

    private final Function<Point, Point> transformation;
    private final BufferedImage image;
    private final Graphics2D ctx;

    /**
     * Construit un canvas 2d sur lequel on peut dessiner
     * @param BL Point bottom-left
     * @param TR Point top-right
     * @param width Largeur
     * @param height Hauteur
     * @param dpi Le nombre de dpi du canvas
     * @param bgColor Couleur de fond
     */
    public Java2DCanvas(Point BL, Point TR, int width, int height, int dpi, Color bgColor){

        double pica = dpi/72.0;

        this.transformation = Point.alignedCoordinateChange(BL, TR,
                                                            new Point(0, height/pica),
                                                            new Point(width/pica,0));
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        this.ctx = image.createGraphics();
        // Active l'anticrénelage.
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        // Remplit le fond
        ctx.setColor(bgColor.toAWTColor());
        ctx.fillRect(0, 0, width, height);
        // Change le repère pour que chaque côté soit de longueur unitaire…
        ctx.scale(pica, pica);
    }

    /**
     * Dessine une polyline avec le style proposé
     * @param polyline La polyline à dessiner
     * @param style Le style du dessin
     */
    @Override
    public void drawPolyLine(PolyLine polyline, LineStyle style) {
        ctx.setColor(style.color().toAWTColor());
        ctx.setStroke(new BasicStroke(style.width(), style.cap().getAWT(), style.join().getAWT(), 10.0f, style.dashingPattern(), 0.0f));

        Path2D path = getPath2D(polyline);
        ctx.draw(path);
    }

    /**
     * Dessine un polygone rempli avec la couleur proposée
     * @param polygon Le polygone à dessiner
     * @param color La couleur à utiliser
     */
    @Override
    public void drawPolygon(Polygon polygon, Color color) {
        ctx.setColor(color.toAWTColor());

        Path2D path = getPath2D(polygon.shell());

        Area area = new Area(path);

        for(ClosedPolyLine hole : polygon.holes()){
            area.subtract(new Area(getPath2D(hole)));
        }

        ctx.fill(area);

    }

    private Path2D getPath2D(PolyLine polyline){
        Path2D path = new Path2D.Double();
        Point p = transformation.apply(polyline.firstPoint());
        path.moveTo(p.x(), p.y());

        polyline.points().stream()
            .map(transformation::apply)
            .forEach(point -> path.lineTo(point.x(), point.y()));

        if (polyline.isClosed()){
            path.closePath();
        }

        return path;
    }

    /**
     * @return L'image du canvas
     */
    public BufferedImage image(){
        return image;
    }

}
