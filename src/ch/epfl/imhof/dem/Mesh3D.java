package ch.epfl.imhof.dem;

import javafx.scene.shape.TriangleMesh;
import java.util.function.Function;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.DigitalElevationModel;

/**
 * Représentation du mesh 3D basé sur les hauteurs
 * données par un modèle digital d'élevation
 * @author Romain Gehrig (223316)
 *
 */
public final class Mesh3D {

    private final TriangleMesh mesh;
    private final DigitalElevationModel dem;
    private final double cathetusLength;
    private final PointGeo bl;
    private final PointGeo tr;
    private final double METERS_TO_RADIANS = 1d/Earth.RADIUS; // correspondance mètres -> radians

    /**
     * Création d'un mesh 3d
     * @param bl Le point bas-gauche
     * @param tr Le point haut-droit
     * @param cathetusLength La longueur des cathètes de chaque triangle (en mètre)
     * @param dem Le modèle digital d'élevation dont on tirera les données
     */
    public Mesh3D(PointGeo bl, PointGeo tr, double cathetusLength, DigitalElevationModel dem) {
        mesh = new TriangleMesh();
        this.dem = dem;
        this.cathetusLength = cathetusLength;
        this.bl = bl;
        this.tr = tr;
    }

    public void construct() {

        // delta radians entre chaque PointGeo
        double delta = cathetusLength * METERS_TO_RADIANS;

        int nbrHorizontalPoints = (int) Math.floor((tr.longitude() - bl.longitude())/delta) + 1;
        int nbrVerticalPoints = (int) Math.floor((tr.latitude() - bl.latitude())/delta) + 1;

        Function<Double, Double> toXSize = (x) -> (x - bl.longitude()) / (tr.longitude() - bl.longitude())*500;
        Function<Double, Double> toYSize = (y) -> 500 - (y - bl.latitude()) / (tr.latitude() - bl.latitude())*500;

        // position dans le tableau; variable utilisée pour chaque boucle
        int pos = 0;

        // Il y a nbrHorizontalPoints * nbrVerticalPoints points, de chacun 3 coordonnées (x,y,z)
        float[] points = new float[3 * nbrHorizontalPoints * nbrVerticalPoints];

        float minHeight = (float) dem.interpolatedHeightAt(bl);
        float maxHeight = minHeight;

        double topLeftX = bl.longitude();
        double topLeftY = tr.latitude();
        for (int y = 0; y < nbrVerticalPoints; ++y) {
            for (int x = 0; x < nbrHorizontalPoints; ++x) {
                float height = (float) dem.interpolatedHeightAt(new PointGeo(topLeftX + x*delta,
                                                                             topLeftY - y*delta));
                minHeight = Math.min(minHeight, height);
                maxHeight = Math.max(maxHeight, height);

                points[pos + 0] = x / (float) nbrHorizontalPoints * 1000; //toXSize.apply(x).floatValue(); // composante x
                points[pos + 1] = y / (float) nbrVerticalPoints * 1000; //toYSize.apply(y).floatValue(); // composante y
                points[pos + 2] = height; // composante z

                pos += 3;
            }
        }

        float diffHeight = maxHeight - minHeight;
        if (diffHeight != 0) {
            for (int x = 2; x < points.length; x += 3) {
                points[x] = Math.abs((points[x] - minHeight) / diffHeight) * -50;
            }
        }

        mesh.getPoints().setAll(points);

        float[] textures = new float[2 * nbrHorizontalPoints * nbrVerticalPoints];

        System.out.println("Mesh: construction des textures");
        pos = 0;
        for (int y = 0; y < nbrVerticalPoints; ++y) {
            for (int x = 0; x < nbrHorizontalPoints; ++x) {
                textures[pos + 0] = (float) x / (float) (nbrHorizontalPoints - 1);
                textures[pos + 1] = (float) y / (float) (nbrVerticalPoints - 1);
                pos += 2;
            }
        }

        mesh.getTexCoords().setAll(textures);

        System.out.println("Mesh: construction des triangles");
        // Il y a 2 triangles de 3+3 points pour chaque carré de 4 points
        // il y a (nbrHorizontalPoints - 1) * (nbrVerticalPoints - 1) carrés
        int[] triangles = new int[2 * 6 * (nbrHorizontalPoints - 1) * (nbrVerticalPoints - 1)];

        // Construction des triangles:
        //
        //   Les points doivent être indiqués dans le sens antihoraire pour
        // construire des triangles dont la face visible est dirigée vers
        // le haut.
        //   On construit les triangles deux par deux en utilisant les 4
        // points d'un carré:
        //      ___
        //   |\ \ | 2
        // 1 |_\ \|
        //
        pos = 0;
        int diff = 0; // il y a une différence à chaque 
        int pointIndex = 0;
        for (int vs = 0; vs < nbrVerticalPoints - 1; ++vs) {
            for (int hs = 0; hs < nbrHorizontalPoints - 1; ++hs) {
                pointIndex = pos + diff;

                // Triangle bas-gauche (1)
                triangles[12*pos + 0] = pointIndex;
                triangles[12*pos + 1] = pointIndex;
                triangles[12*pos + 2] = pointIndex + nbrHorizontalPoints;
                triangles[12*pos + 3] = pointIndex + nbrHorizontalPoints;
                triangles[12*pos + 4] = pointIndex + nbrHorizontalPoints + 1;
                triangles[12*pos + 5] = pointIndex + nbrHorizontalPoints + 1;

                // Triangle haut-droite (2)
                triangles[12*pos + 6] = pointIndex;
                triangles[12*pos + 7] = pointIndex;
                triangles[12*pos + 8] = pointIndex + nbrHorizontalPoints + 1;
                triangles[12*pos + 9] = pointIndex + nbrHorizontalPoints + 1;
                triangles[12*pos + 10] = pointIndex + 1;
                triangles[12*pos + 11] = pointIndex + 1;

                ++pos;
            }
            ++diff;
        }

        mesh.getFaces().setAll(triangles);

    }

    public TriangleMesh mesh() {
        return mesh;
    }
}
