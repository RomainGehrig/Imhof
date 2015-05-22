package ch.epfl.imhof.painting;

import com.sun.prism.BasicStroke;

/**
 * regroupe tous les paramètres de style utiles au dessin d'une ligne
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public class LineStyle {

    public enum LineCap {
        Butt(BasicStroke.CAP_BUTT), Round(BasicStroke.CAP_ROUND), Square(BasicStroke.CAP_SQUARE);
        private int AWTLineCap;
        private LineCap(int AWTLineCap){
            this.AWTLineCap = AWTLineCap;
        }
        public int getAWT(){
            return AWTLineCap;
        }
    };

    public enum LineJoin {
        Round(BasicStroke.JOIN_ROUND), Bevel(BasicStroke.JOIN_BEVEL), Miter(BasicStroke.JOIN_MITER);
        private int AWTLineJoin;
        private LineJoin(int AWTLineJoin){
            this.AWTLineJoin = AWTLineJoin;
        }
        public int getAWT(){
            return AWTLineJoin;
        }
    }

    private final float width;
    private final Color color;
    private final LineCap cap;
    private final LineJoin join;
    private final float[] dashingPattern;

    /**
     * construit le style en prenant en arguments tous les paramètres de style
     * @param width La largeur du trait
     * @param color La couleur du trait
     * @param cap La terminaison des lignes
     * @param join La jointure des segments
     * @param dashingPattern L'alternance des sections opaques et transparentes
     * @throw IllegalArgumentException si la largeur du trait est négative
     *                              ou si l'un des éléments de la séquence d'alternance des segments est négatif ou nul
     */
    public LineStyle(float width, Color color, LineCap cap, LineJoin join, float[] dashingPattern){
        if (width < 0) {
            throw new IllegalArgumentException();
        }

        if (dashingPattern != null) {
            this.dashingPattern = new float[dashingPattern.length];
            for (int i = 0; i < dashingPattern.length; ++i) {
                if (dashingPattern[i] <= 0) {
                    throw new IllegalArgumentException();
                }
                this.dashingPattern[i] = dashingPattern[i];
            }
        } else {
            this.dashingPattern = null;
        }

        this.width = width;
        this.color = color;
        this.cap = cap;
        this.join = join;
    }


    /**
     * construit le style en prenant en arguments que la largeur et la couleur du trait
     * @param width La largeur du trait
     * @param color La couleur du trait
     */
    public LineStyle(float width, Color color) {
        this(width, color, LineCap.Butt, LineJoin.Miter, null);
    }

    /**
     * retourne la largeur du trait
     * @return la largeur du trait
     */
    public float width(){
        return width;
    }

    /**
     * retourne la couleur du trait
     * @return la couleur du trait
     */
    public Color color(){
        return color;
    }

    /**
     * retourne la terminaison des lignes
     * @return la terminaison des lignes
     */
    public LineCap cap(){
        return cap;
    }

    /**
     * retourne la jointure des lignes
     * @return la jointure des lignes
     */
    public LineJoin join(){
        return join;
    }

    /**
     * retourne l'alternance des sections opaques et transparentes
     * @return l'alternance des sections opaques et transparentes
     */
    public float[] dashingPattern(){
        return dashingPattern;
    }

    /**
     * construit un style identique, sauf pour le largeur passé en argument
     * @param w Le largeur du trait
     * @return un style identique mais avec un nouveau largeur
     */
    public LineStyle withWidth(float w){
        return new LineStyle(w, color, cap, join, dashingPattern);
    }

    /**
     * construit un style identique, sauf pour la couleur passée en argument
     * @param c La couleur du trait
     * @return un style identique mais avec une nouvelle couleur
     */
    public LineStyle withColor(Color c){
        return new LineStyle(width, c, cap, join, dashingPattern);
    }

    /**
     * construit un style identique, sauf pour la terminaison des lignes passée en argument
     * @param c La terminaison des lignes
     * @return un style identique mais avec une nouvelle terminaison des lignes
     */
    public LineStyle withCap(LineCap c){
        return new LineStyle(width, color, c, join, dashingPattern);
    }

    /**
     * construit un style identique, sauf pour la jointure des lignes passée en argument
     * @param j La jointure des lignes
     * @return un style identique mais avec une nouvelle jointure des lignes
     */
    public LineStyle withJoin(LineJoin j){
        return new LineStyle(width, color, cap, j, dashingPattern);
    }

    /**
     * construit un style identique, sauf pour l'alternance des sections opaques et transparentes passée en argument
     * @param f L'alternance des sections opaques et transparentes
     * @return un style identique mais avec une nouvelle alternance des sections opaques et transparentes
     */
    public LineStyle withDashingPattern(float[] f){
        return new LineStyle(width, color, cap, join, f);
    }
}
