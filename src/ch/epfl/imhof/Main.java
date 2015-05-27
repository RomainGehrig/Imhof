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

// TESTS
import ch.epfl.imhof.dem.Mesh3D;
import javafx.scene.paint.PhongMaterial;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.shape.MeshView;
import javafx.scene.*;
import javafx.scene.shape.CullFace;
import javafx.collections.*;
import javafx.scene.transform.Rotate;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.event.EventHandler;


/**
 * Point d'entrée principal du programme
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public class Main extends Application {

    // TEST
    double anchorX, anchorY;
    double baseX, baseY;

    // Constantes
    private static final Painter PAINTER = SwissPainter.painter();
    private static final Projection PROJECTION = new CH1903Projection();
    private static final Vector3 LIGHT_SOURCE = new Vector3(-1,1,1);

    /**
     * Point d'entrée principal du programme
     * @param args Les arguments passés au programme
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        OSMMap osmMap = null;
        HGTDigitalElevationModel dem = null;
        try {
            System.out.println("Reading OSM file");
            //    osmMap = OSMMapReader.readOSMFile(args[0],true);
            dem = new HGTDigitalElevationModel(new File(args[1]));
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        //     catch (SAXException e) {
        //         System.out.println(e.getMessage());
        //         System.exit(1);
        //     }

        // if (osmMap == null || dem == null) {
        //     System.out.println("Something went wrong when reading files. Exiting");
        //     System.exit(1);
        // }

        PointGeo geoBL = new PointGeo(Math.toRadians(Double.parseDouble(args[2])),
                                      Math.toRadians(Double.parseDouble(args[3])));
        PointGeo geoTR = new PointGeo(Math.toRadians(Double.parseDouble(args[4])),
                                      Math.toRadians(Double.parseDouble(args[5])));
        Point bl = PROJECTION.project(geoBL);
        Point tr = PROJECTION.project(geoTR);

        int dpi = Integer.parseInt(args[6]);
        File output = new File(args[7]);
        File raw_output = new File("raw_" + args[7]);

        double pixelsPerMeter = dpi * 39.370079; // valeur donnée par le programme `units`
        int height = (int) Math.round(pixelsPerMeter * 1/25000d
                                      * (geoTR.latitude() - geoBL.latitude())
                                      * Earth.RADIUS);
        int width = (int) Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height);

        System.out.println("Width: " + width + " Height: " + height);

        /*
        System.out.println("Transforming OSMMap");
        Map map = new OSMToGeoTransformer(PROJECTION).transform(osmMap);
        Java2DCanvas canvas = new Java2DCanvas(bl, tr, width, height, dpi, Color.WHITE);

        System.out.println("Creating relief");
        double gaussianBlurRadius = pixelsPerMeter * 1.7 * 1/1000d;
        ReliefShader reliefShader = new ReliefShader(PROJECTION, dem, LIGHT_SOURCE);
        BufferedImage reliefImg = reliefShader.shadedRelief(bl, tr, width, height, gaussianBlurRadius);

        System.out.println("Drawing in the canvas");
        PAINTER.drawMap(map, canvas);

        // Output
        try {
            ImageIO.write(mixImages(reliefImg, canvas.image()), "png", output);
            ImageIO.write(canvas.image(), "png", raw_output);
        } catch (IOException e) {}

        */

        System.out.println("Getting the texture");
        Image texture = new Image("file:" + raw_output.getPath());

        System.out.println("Creating mesh");
        Mesh3D mesh = new Mesh3D(geoBL, geoTR, 100.0, dem);

        MeshView meshView = new MeshView(mesh.mesh());

        double ratio = width/height;
        int windowWidth = (int) Math.round(Math.min(1600, width));
        int windowHeight = (int) Math.round(windowWidth/ratio);

        mesh.construct(windowWidth, windowHeight);

        final Group root = new Group(meshView);
        final Scene scene = new Scene(root, windowWidth, windowHeight, true);

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                baseX = meshView.getTranslateX();
                baseY = meshView.getTranslateY();
            }
        });

        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                meshView.setTranslateX(baseX + anchorX - event.getSceneX());
                meshView.setTranslateY(baseY + anchorY - event.getSceneY());
            }
        });

        // Application de la texture
        PhongMaterial material = new PhongMaterial();//javafx.scene.paint.Color.DARKGREEN);
        material.setDiffuseMap(texture);

        ObservableFloatArray points = mesh.mesh().getPoints();

        meshView.setMaterial(material);
        meshView.setCullFace(CullFace.NONE);
        //        meshView.setTranslateX(points.get(0));
        //        meshView.setTranslateY(points.get(1));
        //        meshView.setTranslateZ(points.get(2));
        meshView.setRotationAxis(Rotate.X_AXIS);
        meshView.setRotate(-60);
        System.out.println("All done!");

        scene.setCamera(new PerspectiveCamera(false));
        stage.setScene(scene);
        stage.show();
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
