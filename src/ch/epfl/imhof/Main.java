package ch.epfl.imhof;

import java.util.function.Predicate;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.*;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.*;


// TESTS
import static ch.epfl.imhof.painting.Color.gray;
import static ch.epfl.imhof.painting.Color.rgb;
import static ch.epfl.imhof.painting.Filters.tagged;
import static ch.epfl.imhof.painting.Painter.line;
import static ch.epfl.imhof.painting.Painter.outline;
import static ch.epfl.imhof.painting.Painter.polygon;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.painting.RoadPainterGenerator;
import ch.epfl.imhof.painting.RoadPainterGenerator.RoadSpec;


public class Main {
    // TESTS
    private Color black = Color.BLACK;
    private Color darkGray = gray(0.2);
    private Color darkGreen = rgb(0.75, 0.85, 0.7);
    private Color darkRed = rgb(0.7, 0.15, 0.15);
    private Color darkBlue = rgb(0.45, 0.7, 0.8);
    private Color lightGreen = rgb(0.85, 0.9, 0.85);
    private Color lightGray = gray(0.9);
    private Color orange = rgb(1, 0.75, 0.2);
    private Color lightYellow = rgb(1, 1, 0.5);
    private Color lightRed = rgb(0.95, 0.7, 0.6);
    private Color lightBlue = rgb(0.8, 0.9, 0.95);
    private Color white = Color.WHITE;

    public static void main(String[] args) {
        // Le peintre et ses filtres
        Predicate<Attributed<?>> isLake =
            Filters.tagged("natural", "water");
        Painter lakesPainter =
            Painter.polygon(Color.BLUE).when(isLake);

        Predicate<Attributed<?>> isBuilding =
            Filters.tagged("building");
        Predicate<Attributed<?>> isHighway =
            Filters.tagged("natural");
        Painter buildingsPainter =
            Painter.polygon(Color.BLACK).when(isHighway);

        Painter painter = SwissPainter.painter();

        Projection p = new CH1903Projection();
        OSMMap m = null;
        Point bl = null;
        Point tr = null;
        Java2DCanvas canvas = null;
        String filename = null;

        try {
            switch (1) {
            case 0:
                m = OSMMapReader.readOSMFile("data/lausanne.osm.gz",true); // Lue depuis lausanne.osm.gz
                bl = new Point(532510, 150590);
                tr = new Point(539570, 155260);
                canvas = new Java2DCanvas(bl, tr, 1600, 1080, 150, Color.WHITE);
                filename = "loz";
                break;
            case 1:
                m = OSMMapReader.readOSMFile("data/interlaken.osm.gz",true);
                bl = new Point(628590, 168210);
                tr = new Point(635660, 172870);
                canvas = new Java2DCanvas(bl, tr, 800, 530, 72, Color.WHITE);
                filename = "inter";
                break;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Map map = (new OSMToGeoTransformer(p)).transform(m);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);
        try {
            ImageIO.write(canvas.image(), "png", new File(filename + ".png"));
        } catch (IOException e) {}
    }
}
