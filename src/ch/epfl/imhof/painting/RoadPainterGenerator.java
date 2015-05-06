package ch.epfl.imhof.painting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.painting.LineStyle.LineCap;
import ch.epfl.imhof.painting.LineStyle.LineJoin;


/**
 * représente un générateur de peintre de réseau routier
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public abstract class RoadPainterGenerator {
    
    //TODO : ROMAIIIINNN : il faut retourner un painter à partir de mon tableau à deux dimensions avec le Stream T^T
    /**
     * 
     * @param roadSpecs Un nombre variable de spécifications de routes passé en arguments
     * @return le peintre pour le réseau routier correspondant
     */
    public static Painter painterForRoads(RoadSpec... roadSpecs){
        
        List<Painter> a = new ArrayList<>();
        List<Painter> b = new ArrayList<>();
        List<Painter> c = new ArrayList<>();
        List<Painter> d = new ArrayList<>();
        List<Painter> e = new ArrayList<>();
        
        for(RoadSpec roadSpec : roadSpecs){
            a.add(roadSpec.BridgeInSpec());
            b.add(roadSpec.BridgeOutSpec());
            c.add(roadSpec.RoadInSpec());
            d.add(roadSpec.RoadOutSpec());
            e.add(roadSpec.TunnelSpec());
            roadSpec.BridgeInSpec().above(roadSpec.BridgeOutSpec()).above(roadSpec.RoadInSpec()).above(roadSpec.RoadOutSpec()).above(roadSpec.TunnelSpec());
        }
        
        List<List<Painter>> l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        l.add(d);
        l.add(e);
        
        return null;
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
        
        public Painter BridgeInSpec(){
            return Painter.line(wi, ci, LineCap.Round, LineJoin.Round, null).when(filter.and(Filters.tagged("bridge")));
        }
        
        public Painter BridgeOutSpec(){
            return Painter.line(wi+2*wc, cc, LineCap.Butt, LineJoin.Round, null).when(filter.and(Filters.tagged("bridge")));
        }
        
        public Painter RoadInSpec(){
            return Painter.line(wi, ci, LineCap.Round, LineJoin.Round, null).when(filter.and(Filters.notTagged("bridge").and(Filters.notTagged("tunnel"))));
        }
        
        public Painter RoadOutSpec(){
            return Painter.line(wi+2*wc, cc, LineCap.Round, LineJoin.Round, null).when(filter.and(Filters.notTagged("bridge").and(Filters.notTagged("tunnel"))));
        }
        
        public Painter TunnelSpec(){
            return Painter.line(wi/2, cc, LineCap.Butt, LineJoin.Round, new float[]{2*wi, 2*wi}).when(filter.and(Filters.tagged("tunnel")));
        }
    }
}

