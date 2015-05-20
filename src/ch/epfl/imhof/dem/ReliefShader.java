
package ch.epfl.imhof.dem;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.Kernel;
import java.awt.image.ConvolveOp;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.dem.DigitalElevationModel;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.projection.Projection;

/**
 * Permet de dessiner un relief ombré coloré
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 */

public final class ReliefShader {

    private final Projection projection;
    private final DigitalElevationModel dem;
    private final Vector3 lightSource;

    /**
     * Constructeur
     * @param projection Projection qui sera utilisée
     * @param dem Le modèle numérique du terrain
     * @param lightSource Le vecteur pointant dans la direction de la source lumineuse
     */
    public ReliefShader(Projection projection, DigitalElevationModel dem, Vector3 lightSource) {
        if (projection == null || dem == null || lightSource == null)
            throw new IllegalArgumentException("Arguments shouldn't be null");

        this.projection = projection;
        this.dem = dem;
        this.lightSource = lightSource;
    }

    /**
     * Construit l'image du relief ombré coloré
     * @param botLeftPoint Point en bas à gauche
     * @param topRightPoint Point en haut à droite
     * @param width Largeur de l'image
     * @param height Longueur de l'image
     * @param radius Rayon du flou gaussien (désactivé si égal à 0)
     * @return Une BufferedImage contenant le relief ombré coloré
     */
    public BufferedImage shadedRelief(Point botLeftPoint, Point topRightPoint, int width, int height, double radius) {
        if (botLeftPoint == null || topRightPoint == null)
            throw new IllegalArgumentException("Points should not be null.");

        if (width <= 0 || height <= 0 || radius < 0)
            throw new IllegalArgumentException("Invalid parameter (<= 0)");


        int pad = ((int) Math.ceil(radius));

        BufferedImage relief = rawShadedRelief(width + pad*2,
                                               height + pad*2,
                                               Point.alignedCoordinateChange(new Point(pad,pad),
                                                                             new Point(width+pad,
                                                                                       height+pad),
                                                                             botLeftPoint,
                                                                             topRightPoint));
        if (radius > 0) {
            Kernel kernel = createKernel((float) radius);
            relief = applyGaussianBlur(kernel, relief).getSubimage(pad,pad,width,height);
        }
        return relief;
    }

    private BufferedImage rawShadedRelief(int width, int height, Function<Point, Point> fromImageToPlane) {
        // Sera utile pour calculer le cosinus
        Vector3 normLight = lightSource.normalized();
        BufferedImage relief = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = relief.createGraphics();

        for (int y=0; y<height; ++y) {
            for (int x=0; x<width; ++x) {
                // Note: la coordonnée y doit être inversée
                Point planePoint = fromImageToPlane.apply(new Point(x,height-y));
                Vector3 normal = dem.normalAt(projection.inverse(planePoint));

                // les deux vecteurs sont normalisés, donc le produit scalaire
                // donne immédiatement la valeur du cosinus
                double cos = normLight.scalarProduct(normal);

                Color color = Color.rgb(1/2d*(cos + 1), 1/2d*(cos + 1), 1/2d*(0.7*cos + 1));
                graphics.setColor(color.toAWTColor());
                Rectangle pixel = new Rectangle(x,y,1,1);
                graphics.draw(pixel);
            }
        }

        return relief;
    }

    private Kernel createKernel(float radius) {
        int n = (int)(2 * Math.ceil(radius)) + 1;
        float sigma = radius/3;
        float[] data = new float[n];

        float denominator = 2*sigma*sigma;
        float sum = 0;

        for (int i=0; i<n; ++i) {
            float x = i - n/2;
            float value = (float) Math.exp(-x*x/denominator);
            data[i] = value;
            sum += value;
        }

        // Normalisation
        for (int i=0; i<n; ++i)
            data[i] /= sum;

        return new Kernel(n, 1, data);
    }

    private BufferedImage applyGaussianBlur(Kernel kernel, BufferedImage image) {
        // 1ère passe
        ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        image = convolve.filter(image, null);

        // 2ème passe
        int newWidth = kernel.getHeight();
        int newHeight = kernel.getWidth();
        kernel = new Kernel(newWidth, newHeight, kernel.getKernelData(null));
        convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return convolve.filter(image, null);
    }
}
