package ch.epfl.imhof.geometry;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Représente un polygone à trous
 * @author Yura Tak (247528)
 *
 */
public final class Polygon {
    private final ClosedPolyLine shell;
    private final List<ClosedPolyLine> holes;

    /**
     * Construit un polygone avec l'enveloppe et les trous donnés
     * @param shell L'enveloppe du polygone
     * @param holes Liste des trous
     */
    public Polygon(ClosedPolyLine shell, List<ClosedPolyLine> holes){
        this.shell = shell;
        this.holes = Collections.unmodifiableList(new ArrayList<ClosedPolyLine>(holes));
    }

    /**
     * Construit un polygone avec l'enveloppe donnée, sans trous
     * @param shell L'enveloppe du polygone
     */
    public Polygon(ClosedPolyLine shell){
        this(shell, Collections.emptyList());
    }

    /**
     * Getter sur l'enveloppe
     * @return shell
     */
    public PolyLine shell(){
        return shell;
    }

    /**
     * Getter sur la liste de trou
     * @return holes
     */
    public List<ClosedPolyLine> holes(){
        return holes;
    }
}
