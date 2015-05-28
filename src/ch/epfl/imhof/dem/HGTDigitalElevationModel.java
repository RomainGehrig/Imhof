package ch.epfl.imhof.dem;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.geometry.Point;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

/**
 * Implémentation du modèle numérique du terrain avec la norme HGT
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {

    private final ShortBuffer buffer;
    private final FileInputStream file;
    private final PointGeo basePoint;
    private final int lineLength;
    private final double delta; // en radians

    /**
     * Construit un modèle de terrain digital à partir d'un fichier
     * @param file Le fichier HGT à lire
     * @throws IOException Lancée lors de l'ouverture du fichier
     * @throws IllegalArgumentException Si le fichier est invalide d'une façon ou d'une autre
     */
    public HGTDigitalElevationModel(File file) throws IOException {
        double latitude = 0.0;
        double longitude = 0.0;

        String filename = file.getName();

        // Vérification de la taille d'un nom de fichier HGT
        if (filename.length() != 11)
            throw new IllegalArgumentException("Cannot parse HGT filename: invalid filename");

        // Première lettre (latitude du coin sud-ouest)
        switch (filename.charAt(0)) {
        case 'N':
            latitude =  1.0;
            break;
        case 'S':
            latitude = -1.0;
            break;
        default:
            throw new IllegalArgumentException("Cannot parse HGT filename: invalid latitude");
        }

        // Lettres 2-3 (valeur absolue de la latitude)
        try {
            latitude *= Integer.parseInt(filename.substring(1,3));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse HGT filename: invalid latitude");
        }

        // Quatrième lettre (longitude du coin sud-ouest)
        switch (filename.charAt(3)) {
        case 'E':
            longitude =  1.0;
            break;
        case 'W':
            longitude = -1.0;
            break;
        default:
            throw new IllegalArgumentException("Cannot parse HGT filename: invalid longitude");
        }

        // Lettres 5-7 (valeur absolue de la longitude)
        try {
            longitude *= Integer.parseInt(filename.substring(4, 7));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Cannot parse HGT filename: invalid longitude");
        }

        // Extension
        if (!filename.substring(7).equals(".hgt"))
            throw new IllegalArgumentException("Cannot parse HGT filename: invalid extension");

        longitude = Math.toRadians(longitude);
        latitude = Math.toRadians(latitude);

        basePoint = new PointGeo(longitude, latitude);

        double length = Math.sqrt(file.length()/2.0);
        if (length != Math.floor(length)) {
            throw new IllegalArgumentException("HGT file does not have a useable format. (Is not square)");
        }
        lineLength = ((int)length) - 1; // On ne compte pas le dernier point
        delta = 1.0/lineLength;

        this.file = new FileInputStream(file);
        buffer = this.file.getChannel()
            .map(MapMode.READ_ONLY, 0, file.length())
            .asShortBuffer();
    }

    public double getHeightAt(PointGeo pt) {
        double longDiff = Math.toDegrees(pt.longitude() - basePoint.longitude());
        double latDiff = Math.toDegrees(pt.latitude() - basePoint.latitude());

        if (latDiff > 1 || latDiff < 0 || longDiff > 1 || longDiff < 0) {
            throw new IllegalArgumentException("PointGeo is not in the correct range.");
        }

        int i = bottomLeftX(Math.toDegrees(pt.longitude()));
        int j = bottomLeftY(Math.toDegrees(pt.latitude()));

        return heightAt(i,j);
    }

    public Vector3 normalAt(PointGeo pt) {
        double longDiff = Math.toDegrees(pt.longitude() - basePoint.longitude());
        double latDiff = Math.toDegrees(pt.latitude() - basePoint.latitude());

        if (latDiff > 1 || latDiff < 0 || longDiff > 1 || longDiff < 0) {
            throw new IllegalArgumentException("PointGeo is not in the correct range.");
        }

        /*
          Schéma des vecteurs:
                   c
       (i,j-1) o<------o (i+1,j-1)
               ^       |
             b |       |
               |       | d
               |       v
         (i,j) o------>o (i+1,j)
                   a

          Changements par rapport à la formule du cours: la coordonnée j est
          inversée, donc j+1 devient j-1: cela simplifie les calculs suivants
        */

        int i = bottomLeftX(Math.toDegrees(pt.longitude()));
        int j = bottomLeftY(Math.toDegrees(pt.latitude()));

        // Distance en mètres entre les points
        double s = Math.toRadians(delta) * Earth.RADIUS;

        // Changement de j+1 en j-1 ici:
        double x = 1/2d * s * (heightAt(i,j) - heightAt(i+1,j) + heightAt(i,j-1) - heightAt(i+1,j-1));
        // Changement de j+1 en j-1 ici:
        double y = 1/2d * s * (heightAt(i,j) + heightAt(i+1,j) - heightAt(i,j-1) - heightAt(i+1,j-1));
        double z = s * s;

        return new Vector3(x,y,z).normalized();
    }

    public void close() throws IOException {
        file.close();
    }

    private short heightAt(int i, int j) {
        return buffer.get(i + j*(lineLength + 1));
    }

    private int bottomLeftX(double longitude) {
        int x = (int) Math.floor((longitude - Math.toDegrees(basePoint.longitude())) * lineLength);
        return Math.min(x, lineLength-1);
    }

    private int bottomLeftY(double latitude) {
        int y = (int) Math.floor((latitude - Math.toDegrees(basePoint.latitude())) * lineLength);
        return (lineLength - Math.min(y, lineLength - 1));
    }
}
