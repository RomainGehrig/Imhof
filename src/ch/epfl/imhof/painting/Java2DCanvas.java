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

public class Java2DCanvas implements Canvas{

    private final Function<Point, Point> transformation;
    private final BufferedImage image;
    private final Graphics2D ctx;

    public Java2DCanvas(Point BL, Point TR, int l, int h, int dpi, Color c){

        double pica = dpi/72.0;

        this.transformation = Point.alignedCoordinateChange(BL, TR, new Point(0, h/pica), new Point(l/pica,0));
        this.image = new BufferedImage(l, h, BufferedImage.TYPE_INT_RGB);

        this.ctx = image.createGraphics();
        // Active l'anticrénelage.
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        // Remplit le fond en rouge.
        ctx.setColor(c.toAWTColor());
        ctx.fillRect(0, 0, l, h);
        // Change le repère pour que chaque côté soit de longueur unitaire…
        ctx.scale(pica, pica);

    }

    @Override
    public void drawPolyLine(PolyLine polyline, LineStyle style) {
        ctx.setColor(style.color().toAWTColor());
        ctx.setStroke(new BasicStroke(style.width(), style.cap().getAWT(), style.join().getAWT(), 10.0f, style.dashingPattern(), 0.0f));

        Path2D path = getPath2D(polyline);
        ctx.draw(path);
    }

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
        for(Point point : polyline.points()){
            Point to = transformation.apply(point);
            if(to != p){
                path.lineTo(to.x(), to.y());
            }
        }

        if(polyline.isClosed()){
            path.closePath();
        }

        return path;
    }

    public BufferedImage image(){
        return image;
    }

}
