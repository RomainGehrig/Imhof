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
        lineLength = (int)length;
        delta = 1.0/(lineLength - 1); // On ne compte pas le dernier point

        this.file = new FileInputStream(file);
        buffer = this.file.getChannel()
            .map(MapMode.READ_ONLY, 0, file.length())
            .asShortBuffer();
    }

    public void close() throws IOException {
        file.close();
    }

    public Vector3 normalAt(PointGeo pt) {
        double longDiff = pt.longitude() - basePoint.longitude();
        double latDiff = pt.latitude() - basePoint.latitude();

        if (latDiff > 1 || latDiff < 0 || longDiff > 1 || longDiff < 0)
            throw new IllegalArgumentException("PointGeo is not in the correct range");

        /*
          Schéma des points:

               <-- δ -->
               a ----- b ^
               |       | |
               |       | δ
               |       | |
               c ----- d v

          Vecteurs utiles: AB, AC, DB, DC
         */

        /*
          Schéma des vecteurs:
                   c
       (i,j+1) o<------o (i+1,j+1)
               ^       |
             b |       |
               |       | d
               |       v
         (i,j) o------>o (i+1,j)
                   a

         */

        int i = lowerLeftX(pt.longitude());
        int j = lowerLeftY(pt.latitude());

        double s = delta * Earth.RADIUS; // Distance en mètres entre les points

        double x = 1/2d * s * (heightAt(i,j) - heightAt(i+1,j) + heightAt(i,j+1) - heightAt(i+1,j+1));
        double y = 1/2d * s * (heightAt(i,j) + heightAt(i+1,j) - heightAt(i,j+1) - heightAt(i+1,j+1));
        double z = s * s;

        // double firstLong = basePoint.longitude() + firstX*delta;
        // double firstLat = basePoint.latitude() + firstY*delta;
        // Point3 a = new Point3(firstLong      , firstLat      , (double) getHeightAt(firstX  , firstY  ));
        // Point3 b = new Point3(firstLong+delta, firstLat      , (double) getHeightAt(firstX+1, firstY  ));
        // Point3 c = new Point3(firstLong      , firstLat+delta, (double) getHeightAt(firstX  , firstY+1));
        // Point3 d = new Point3(firstLong+delta, firstLat+delta, (double) getHeightAt(firstX+1, firstY+1));

        // Vector3 ab = a.toVector(b);
        // Vector3 ac = a.toVector(c);
        // Vector3 db = d.toVector(b);
        // Vector3 dc = d.toVector(c);

        return new Vector3(x,y,z);
    }

    private short heightAt(int x, int y) {
        return buffer.get(x + y*lineLength);
    }

    private int lowerLeftX(double longitude) {
        int x = (int)Math.floor((longitude - basePoint.longitude())*lineLength);
        return Math.min(lineLength-1, x);
    }

    private int lowerLeftY(double latitude) {
        int y = (int)Math.floor((1 - (latitude - basePoint.latitude()))*lineLength);
        return Math.max(1, y);
    }

    private final class Point3 {
        private final double x;
        private final double y;
        private final double z;

        public Point3(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3 toVector(Point3 to) {
            return new Vector3(to.x - x, to.y - y, to.z - z);
        }
    }
}
