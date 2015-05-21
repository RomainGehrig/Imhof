package ch.epfl.imhof.painting;

/**
 * représente une couleur, décrite par ses trois composantes rouge, verte et bleue.
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public class Color {

    /**
     * La couleur « rouge » (pur)
     */
    public static final Color RED = new Color(1,0,0);
    /**
     * La couleur « vert » (pur)
     */
    public static final Color GREEN = new Color(0,1,0);
    /**
     * La couleur « bleu » (pur)
     */
    public static final Color BLUE = new Color(0,0,1);
    /**
     * La couleur « noir »
     */
    public static final Color BLACK = new Color(0,0,0);
    /**
     * La couleur « blanc »
     */
    public static final Color WHITE = new Color(1,1,1);

    private final double r;
    private final double g;
    private final double b;

    /**
     * construit une couleur avec les composantes rouges, vertes et bleues données, qui doivent être dans l'intervalle [0;1]
     * @param r La composante rouge
     * @param g La composante verte
     * @param b La composante bleue
     * @throws IllegalArgumentException si l'une des composantes est hors de l'intervalle [0;1]
     */
    private Color(double r, double g, double b){
        if(r<0 || r>1){
            throw new IllegalArgumentException();
        } else {
            this.r = r;
        }

        if(g<0 || g>1){
            throw new IllegalArgumentException();
        } else {
            this.g = g;
        }

        if(b<0 || b>1){
            throw new IllegalArgumentException();
        } else {
            this.b = b;
        }
    }

    /**
     * construit la couleur grise dont les trois composantes sont égales à la valeur passée en argument qui doit être dans l'intervalle [0;1]
     * @param d La composante unique
     * @return la couleur grise
     */
    public static Color gray(double d){
        return new Color(d, d, d);
    }

    /**
     * construit une couleur avec les composantes rouges, vertes et bleues données, qui doivent être dans l'intervalle [0;1]
     * @param r La composante rouge
     * @param g La composante verte
     * @param b La composante bleue
     * @return la couleur correspondante
     */
    public static Color rgb(double r, double g, double b){
        return new Color(r, g, b);
    }

    /**
     * construit une couleur en « déballant » les trois composantes RGB stockées chacune sur 8 bits.
     * La composante r est supposée occuper les bits 23 à 16, la composante g les bits 15 à 8 et la composante b les bits 7 à 0
     * @param rgb La couleur encodée, au format RGB
     * @return la couleur correspondante
     */
    public static Color rgb(int rgb){
        double r = ((rgb >> 16) & 0xFF) / 255d;
        double g = ((rgb >>  8) & 0xFF) / 255d;
        double b = ((rgb >>  0) & 0xFF) / 255d;

        return new Color(r, g, b);
    }

    /**
     * mélange la couleur réceptrice avec la couleur passée en argument
     * @param that Couleur avec laquelle la couleur réceptrice va être mélangée
     * @return une couleur mélangée
     */
    public Color mixWith(Color that){
        return new Color(r*that.r, g*that.g, b*that.b);
    }

    /**
     * convertit la couleur en une couleur AWT
     * @return La couleur AWT correspondant à la couleur réceptrice
     */
    public java.awt.Color toAWTColor(){
        return new java.awt.Color((float)r, (float)g, (float)b);
    }
}
