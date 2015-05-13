package ch.epfl.imhof;

import java.util.function.Predicate;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.*;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.projection.*;

public class Main {

    public static void main(String[] args) {
        // Le peintre et ses filtres
        Predicate<Attributed<?>> isLake =
            Filters.tagged("natural", "water");
        Painter lakesPainter =
            Painter.polygon(Color.BLUE).when(isLake);

        Predicate<Attributed<?>> isBuilding =
            Filters.tagged("building");
        Painter buildingsPainter =
            Painter.polygon(Color.BLACK).when(isBuilding);

        Painter painter = buildingsPainter.above(lakesPainter);

        Projection p = new CH1903Projection();
        Map map = null;
        try {
            map = (new OSMToGeoTransformer(p)).transform(OSMMapReader.readOSMFile("data/lausanne.osm.gz",true)); // Lue depuis lausanne.osm.gz
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // La toile
        Point bl = new Point(532510, 150590);
        Point tr = new Point(539570, 155260);
        Java2DCanvas canvas =
            new Java2DCanvas(bl, tr, 800, 530, 72, Color.WHITE);

        // Dessin de la carte et stockage dans un fichier
        painter.drawMap(map, canvas);
        try {
            ImageIO.write(canvas.image(), "png", new File("loz.png"));
        } catch (IOException e) {}
    }
}
