package ch.epfl.imhof;

import org.xml.sax.SAXException;
import java.util.function.Predicate;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.projection.CH1903Projection;

/**
 * Point d'entrée principal du programme
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public class Main {

    // Constantes
    private static final Painter PAINTER = SwissPainter.painter();
    private static final Projection PROJECTION = new CH1903Projection();
    private static final Vector3 LIGHT_SOURCE = new Vector3(-1,1,1);

    /**
     * Point d'entrée principal du programme
     * @param args Les arguments passés au programme
     */
    public static void main(String[] args) {
        OSMMap osmMap = null;
        HGTDigitalElevationModel dem = null;
        try {
            osmMap = OSMMapReader.readOSMFile(args[0],true);
            dem = new HGTDigitalElevationModel(new File(args[1]));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch (SAXException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        if (osmMap == null || dem == null) {
            System.out.println("Something went wrong when reading files. Exiting");
            System.exit(1);
        }

        PointGeo geoBL = new PointGeo(Math.toRadians(Double.parseDouble(args[2])),
                                      Math.toRadians(Double.parseDouble(args[3])));
        PointGeo geoTR = new PointGeo(Math.toRadians(Double.parseDouble(args[4])),
                                      Math.toRadians(Double.parseDouble(args[5])));
        Point bl = PROJECTION.project(geoBL);
        Point tr = PROJECTION.project(geoTR);

        int dpi = Integer.parseInt(args[6]);
        File output = new File(args[7]);

        double pixelsPerMeter = dpi * 39.370079; // valeur donnée par le programme `units`
        int height = (int) Math.round(pixelsPerMeter * 1/25000d
                                      * (geoTR.latitude() - geoBL.latitude())
                                      * Earth.RADIUS);
        int width = (int) Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height);

        Map map = new OSMToGeoTransformer(PROJECTION).transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi, Color.WHITE);

        double gaussianBlurRadius = pixelsPerMeter * 1.7 * 1/1000d;
        ReliefShader reliefShader = new ReliefShader(PROJECTION, dem, LIGHT_SOURCE);
        BufferedImage reliefImg = reliefShader.shadedRelief(bl, tr, width, height, gaussianBlurRadius);

        PAINTER.drawMap(map, canvas);

        // Output
        try {
            ImageIO.write(mixImages(reliefImg, canvas.image()), "png", output);
        } catch (IOException e) {}
    }

    private static BufferedImage mixImages(BufferedImage a, BufferedImage b) {
        int width = a.getWidth();
        int height = a.getHeight();

        if (width != b.getWidth() || height != b.getHeight())
            throw new IllegalArgumentException("Images are not the same size");

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                Color c1 = Color.rgb(a.getRGB(x,y));
                Color c2 = Color.rgb(b.getRGB(x,y));
                result.setRGB(x,y, c1.mixWith(c2).toAWTColor().getRGB());
            }
        }
        return result;
    }
}
