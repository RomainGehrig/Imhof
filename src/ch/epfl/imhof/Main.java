package ch.epfl.imhof;

import org.xml.sax.SAXException;
import java.util.List;
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
import ch.epfl.imhof.dem.Mesh3D;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.projection.CH1903Projection;

// Affichage / 3D
import javafx.scene.paint.PhongMaterial;
import javafx.collections.*;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.CullFace;
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

    // Constantes
    private static final Painter PAINTER = SwissPainter.painter();
    private static final Projection PROJECTION = new CH1903Projection();
    private static final Vector3 LIGHT_SOURCE = new Vector3(-1,1,1);

    // Coordonnées/angles utilisés pour la manipulation du mesh
    // dans la fenêtre de l'application
    private double baseX, baseY, baseZ;
    private double anchorX, anchorY, anchorZ;
    private double anchorAngle;

    /**
     * Point d'entrée principal du programme
     * @param args Les arguments passés au programme
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Point d'entrée de l'Application (JavaFX)
     * @param stage Vue principale, fournie lors du lancement de l'Application par JavaFX
     */
    @Override
    public void start(Stage stage) {
        List<String> argsLst = getParameters().getRaw();
        boolean render3d = argsLst.get(0).equals("-render3d") || argsLst.get(1).equals("-render3d");
        boolean skipMapCreation = argsLst.get(0).equals("-skipMapCreation") || argsLst.get(1).equals("-skipMapCreation");
        // On shift les arguments s'il y a les paramètres pour le rendu 3d / skip de la création de la map
        String[] args = argsLst.subList((render3d ? 1 : 0) + (skipMapCreation ? 1 : 0), argsLst.size()).toArray(new String[0]);

        if (render3d)
            System.out.println("3d rendering enabled");
        else
            System.out.println("3d rendering disabled");

        OSMMap osmMap = null;
        HGTDigitalElevationModel dem = null;
        try {
            if (!skipMapCreation) {
                System.out.println("Reading OSM file");
                osmMap = OSMMapReader.readOSMFile(args[0],true);
            }
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

        if ((osmMap == null || dem == null) && !skipMapCreation) {
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
        File raw_output = new File("raw_" + args[7]);

        double pixelsPerMeter = dpi * 39.370079; // valeur donnée par le programme `units`
        int height = (int) Math.round(pixelsPerMeter * 1/25000d
                                      * (geoTR.latitude() - geoBL.latitude())
                                      * Earth.RADIUS);
        int width = (int) Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * height);

        if (!skipMapCreation) {
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
                if (render3d)
                    ImageIO.write(canvas.image(), "png", raw_output);
            } catch (IOException e) {}
        }

        // Création du mesh et affichage de la fenêtre de l'application
        if (render3d) {

            System.out.println("Getting the texture");
            Image texture = new Image("file:" + raw_output.getPath());

            System.out.println("Creating mesh");
            Mesh3D mesh = new Mesh3D(100.0, dem);

            double ratio = width/(double)height;
            int windowWidth = (int) Math.round(Math.min(1200, width));
            int windowHeight = (int) Math.round(windowWidth/ratio);

            mesh.construct(geoBL, geoTR, windowWidth, windowHeight);

            MeshView meshView = new MeshView(mesh.mesh());
            Group root = new Group(meshView);
            Scene scene = new Scene(root, windowWidth, windowHeight, true);

            // Event au clic d'un bouton
            scene.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent event) {
                        anchorX = event.getSceneX();
                        anchorY = event.getSceneY();
                        baseX = meshView.getTranslateX();
                        baseY = meshView.getTranslateY();
                        anchorAngle = meshView.getRotate();
                    }
                });

            // Event durant le cliqué-glissé de la souris
            scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            meshView.setTranslateX(baseX + anchorX - event.getSceneX());
                            meshView.setTranslateY(baseY + anchorY - event.getSceneY());
                        } else {
                            meshView.setRotate(anchorAngle + anchorY - event.getSceneY());
                        }
                    }
                });

            // Application de la texture
            PhongMaterial material = new PhongMaterial();
            material.setDiffuseMap(texture);

            // Mise en place du mesh dans la scène
            meshView.setMaterial(material);
            meshView.setCullFace(CullFace.NONE);
            meshView.setRotationAxis(Rotate.X_AXIS);
            meshView.setRotate(-45);

            scene.setCamera(new PerspectiveCamera(false));
            stage.setScene(scene);

            System.out.println("All done!");
            stage.setResizable(false);
            stage.show();
        }
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
