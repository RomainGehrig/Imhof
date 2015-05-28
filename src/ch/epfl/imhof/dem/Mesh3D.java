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
    private final double METERS_TO_RADIANS = 1d/Earth.RADIUS; // correspondance mètres -> radians

    /**
     * Création d'un mesh 3d
     * @param cathetusLength La longueur des cathètes de chaque triangle (en mètres)
     * @param dem Le modèle digital d'élevation dont on tirera les données
     */
    public Mesh3D(double cathetusLength, DigitalElevationModel dem) {
        mesh = new TriangleMesh();
        this.dem = dem;
        this.cathetusLength = cathetusLength;
    }

    /**
     * Construit le mesh avec la taille demandée, entre les points donnés
     * @param bl Le point bas-gauche
     * @param tr Le point haut-droit
     * @param width La largeur
     * @param height la hauteur
     */
    public void construct(PointGeo bl, PointGeo tr, int width, int height) {

        // Conversion mètres -> coordonnées du mesh, utilisé pour convertir la hauteur
        double METERS_TO_COORDINATE = width/(Earth.RADIUS * (tr.longitude() - bl.longitude()));

        // delta radians entre chaque PointGeo
        double delta = cathetusLength * METERS_TO_RADIANS;

        int nbrHorizontalPoints = (int) Math.floor((tr.longitude() - bl.longitude())/delta) + 1;
        int nbrVerticalPoints = (int) Math.floor((tr.latitude() - bl.latitude())/delta) + 1;

        Function<Double, Double> toX = (x) -> width*(x - bl.longitude()) / (tr.longitude() - bl.longitude());
        Function<Double, Double> toY = (y) -> height*(1 - (y - bl.latitude()) / (tr.latitude() - bl.latitude()));

        // position dans le tableau; variable utilisée pour chaque boucle
        int pos = 0;

        // Il y a nbrHorizontalPoints * nbrVerticalPoints points, de chacun 3 coordonnées (x,y,z)
        float[] points = new float[3 * nbrHorizontalPoints * nbrVerticalPoints];

        float minHeight = (float) dem.getHeightAt(bl);

        double topLeftX = bl.longitude();
        double topLeftY = tr.latitude();
        for (int y = 0; y < nbrVerticalPoints; ++y) {
            for (int x = 0; x < nbrHorizontalPoints; ++x) {
                double xpos = topLeftX + x*delta;
                double ypos = topLeftY - y*delta;

                float z = (float) dem.getHeightAt(new PointGeo(xpos,ypos));
                minHeight = Math.min(minHeight, z);

                points[pos + 0] = toX.apply((double) xpos).floatValue(); // composante x
                points[pos + 1] = toY.apply((double) ypos).floatValue(); // composante y
                points[pos + 2] = z; // composante z

                pos += 3;
            }
        }

        // Normalisation des hauteurs
        for (int x = 2; x < points.length; x += 3) {
            // Note: On inverse (avec -1) les hauteurs pour la 3d
            points[x] = -1 * (points[x] - minHeight) * (float) METERS_TO_COORDINATE;
        }

        mesh.getPoints().setAll(points);

        // Points des textures
        float[] textures = new float[2 * nbrHorizontalPoints * nbrVerticalPoints];

        pos = 0;
        for (int y = 0; y < nbrVerticalPoints; ++y) {
            for (int x = 0; x < nbrHorizontalPoints; ++x) {
                textures[pos + 0] = (float) x / (float) (nbrHorizontalPoints - 1); // x entre 0 et 1
                textures[pos + 1] = (float) y / (float) (nbrVerticalPoints - 1);   // y entre 0 et 1
                pos += 2;
            }
        }

        mesh.getTexCoords().setAll(textures);

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
        //   Chaque triangle se déclare avec 6 points: 3 pour les points
        // du triangle et 3 pour les points de la texture
        //      ___
        //   |\ \ | 2
        // 1 |_\ \|
        //
        pos = 0;
        int pointIndex = 0;
        for (int vs = 0; vs < nbrVerticalPoints - 1; ++vs) {
            for (int hs = 0; hs < nbrHorizontalPoints - 1; ++hs) {
                pointIndex = pos + vs;

                // Triangle bas-gauche (1)
                triangles[12*pos + 0] = pointIndex;                           // point n°1 du triangle
                triangles[12*pos + 1] = pointIndex;                           // point n°1 de la texture
                triangles[12*pos + 2] = pointIndex + nbrHorizontalPoints;     // point n°2 du triangle
                triangles[12*pos + 3] = pointIndex + nbrHorizontalPoints;     // point n°2 de la texture
                triangles[12*pos + 4] = pointIndex + nbrHorizontalPoints + 1; // point n°3 du triangle
                triangles[12*pos + 5] = pointIndex + nbrHorizontalPoints + 1; // point n°3 de la texture

                // Triangle haut-droite (2)
                triangles[12*pos + 6] = pointIndex;
                triangles[12*pos + 7] = pointIndex;
                triangles[12*pos + 8] = pointIndex + nbrHorizontalPoints + 1;
                triangles[12*pos + 9] = pointIndex + nbrHorizontalPoints + 1;
                triangles[12*pos + 10] = pointIndex + 1;
                triangles[12*pos + 11] = pointIndex + 1;

                ++pos;
            }
        }

        mesh.getFaces().setAll(triangles);
    }

    /**
     * Getter sur le mesh
     * Note: le mesh n'est pas immuable
     */
    public TriangleMesh mesh() {
        return mesh;
    }
}
