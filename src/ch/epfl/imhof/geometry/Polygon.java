package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * représente un polygone à trous
 * @author Yura Tak (247528)
 *
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;
    
    /**
     * construit un polygone avec l'enveloppe et les trous donnés
     * @param shell
     * @param holes
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes){
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }
    
    /**
     * construit un polygone avec l'enveloppe donnée, sans trous
     * @param shell
     */
    public Polygon(ClosedPolyLine shell){
        this(shell, Collections.emptyList());   
    }
    
    /**
     * retourne l'enveloppe
     * @return
     */
    public PolyLine shell(){
        return shell;
    }
    
    /**
     * retouren la liste des trous
     * @return
     */
    public List<ClosedPolyLine> holes(){
        return holes;
    }
}
