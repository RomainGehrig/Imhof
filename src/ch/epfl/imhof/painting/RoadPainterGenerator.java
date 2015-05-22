package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;

/**
 * Générateur de peintres de réseau routier
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public abstract class RoadPainterGenerator {


    // Constructeur privé
    private RoadPainterGenerator() {}

    /**
     * Construit un painter à partir de spécifications pour les routes
     * @param roadSpecs Un nombre variable de spécifications de routes passés en arguments
     * @return le peintre pour le réseau routier correspondant
     */
    public static Painter painterForRoads(RoadSpec... roadSpecs){

        List<List<Painter>> painters = new ArrayList<>();

        for (int i=0; i<5; ++i) {
            painters.add(new ArrayList<>());
        }

        for(RoadSpec roadSpec : roadSpecs){
            painters.get(0).add(roadSpec.BridgeInSpec());
            painters.get(1).add(roadSpec.BridgeOutSpec());
            painters.get(2).add(roadSpec.RoadInSpec());
            painters.get(3).add(roadSpec.RoadOutSpec());
            painters.get(4).add(roadSpec.TunnelSpec());
        }

        Painter paint = painters.stream()
            .map(subPainters -> subPainters.stream().reduce(Painter::above).get())
            .reduce(Painter::above).get();

        return paint;
    }

    /**
     * décrit le dessin d'un type de route donné
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class RoadSpec{

        private final Predicate<Attributed<?>> filter;
        private final float wi;
        private final Color ci;
        private final float wc;
        private final Color cc;

        /**
         * construit une spécification de route
         * @param filter Filtre permettant de sélectionner ce type de route
         * @param wi Un paramètre de style
         * @param ci Un paramètre de style, une couleur
         * @param wc Un paramètre de style
         * @param cc Un paramètre de style, une couleur
         */
        public RoadSpec(Predicate<Attributed<?>> filter, float wi, Color ci, float wc, Color cc) {
            this.filter = filter;
            this.wi = wi;
            this.ci = ci;
            this.wc = wc;
            this.cc = cc;
        }

        /**
         * @return Un painter pour l'intérieur des ponts
         */
        public Painter BridgeInSpec(){
            return Painter.line(wi, ci, LineCap.Round, LineJoin.Round, null).when(filter.and(Filters.tagged("bridge")));
        }

        /**
         * @return Un painter pour l'extérieur des ponts
         */
        public Painter BridgeOutSpec(){
            return Painter.line(wi+2*wc, cc, LineCap.Butt, LineJoin.Round, null).when(filter.and(Filters.tagged("bridge")));
        }

        /**
         * @return Un painter pour l'intérieur des routes
         */
        public Painter RoadInSpec(){
            return Painter.line(wi, ci, LineCap.Round, LineJoin.Round, null).when(filter.and(Filters.notTagged("bridge").and(Filters.notTagged("tunnel"))));
        }

        /**
         * @return Un painter pour l'extérieur des routes
         */
        public Painter RoadOutSpec(){
            return Painter.line(wi+2*wc, cc, LineCap.Round, LineJoin.Round, null).when(filter.and(Filters.notTagged("bridge").and(Filters.notTagged("tunnel"))));
        }

        /**
         * @return Un painter pour les tunnels
         */
        public Painter TunnelSpec(){
            return Painter.line(wi/2, cc, LineCap.Butt, LineJoin.Round, new float[]{2*wi, 2*wi}).when(filter.and(Filters.tagged("tunnel")));
        }
    }
}
