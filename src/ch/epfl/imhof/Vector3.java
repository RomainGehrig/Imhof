package ch.epfl.imhof;

/**
 * représentant un vecteur tridimensionnel
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Vector3 {

    private final double x, y, z;

    /**
     * crée un vecteur étant données ses trois composantes
     * @param x L'abscisse
     * @param y L'ordonnée
     * @param z Le côte
     */
    public Vector3(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
        System.out.println("x:" + x + " y: " + y + " z: " + z);
    }

    /**
     * permet d'obtenir la norme du vecteur
     * @return la norme du vecteur
     */
    public double norm() {
        return Math.sqrt(Math.pow(x,2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /**
     * permet d'obtenir la version normalisée du vecteur (c-à-d un vecteur parallèle à celui-ci, de même direction mais de longueur unitaire)
     * @return la version normalisée du vecteur
     */
    public Vector3 normalized() {
        double norm = norm();
        return new Vector3(x/norm, y/norm, z/norm);
    }

    /**
     * retourne le produit scalaire entre le récepteur et un second vecteur passé en argument
     * @param Le second vecteur passé en argument
     * @return
     */
    public double scalarProduct(Vector3 vect) {
        return (vect.x*this.x) + (vect.y*this.y) + (this.z*vect.z);
    }
}
