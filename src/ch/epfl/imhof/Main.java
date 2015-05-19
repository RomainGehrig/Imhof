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
import ch.epfl.imhof.dem.HGTDigitalElevationModel;

public class Main {
    public static void main(String[] args) {
        Painter painter = SwissPainter.painter();
        Projection p = new CH1903Projection();
        OSMMap m = null;
        Point bl = null;
        Point tr = null;
        Java2DCanvas canvas = null;
        String filename = null;
        HGTDigitalElevationModel dem = null;

        try {
            dem = new HGTDigitalElevationModel(new File("data/imhof-dems/N46E007.hgt"));
        } catch (IOException e) {}

        try {
            switch (0) {
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
