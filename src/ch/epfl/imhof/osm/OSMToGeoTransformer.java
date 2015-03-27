package ch.epfl.imhof.osm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.Graph;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.OpenPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.projection.CH1903Projection;

/**
 * représente un convertisseur de données OSM en carte
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OSMToGeoTransformer {

    private final Projection projection;
    private final Map.Builder builder = new Map.Builder();

    private final static Set<String> AREA_VALUE = new HashSet<>(Arrays.asList("yes", "1", "true"));
    private final static Set<String> AREA_ATTRIBUTS = new HashSet<>(Arrays.asList(
        "aeroway", "amenity", "building", "harbour", "historic",
        "landuse", "leisure", "man_made", "military", "natural",
        "office", "place", "power", "public_transport", "shop",
        "sport", "tourism", "water", "waterway", "wetland"
    ));
    private final static Set<String> FILTRE_POLYLINE = new HashSet<>(Arrays.asList(
        "bridge", "highway", "layer", "man_made", "railway",
        "tunnel", "waterway"
    ));
    private final static Set<String> FILTRE_POLYGON = new HashSet<>(Arrays.asList(
        "building", "landuse", "layer", "leisure", "natural",
        "waterway"
    ));

    /**
     * construit un convertisseur OSM en géométrie qui utilise la projection donnée
     * @param projection
     */
    public OSMToGeoTransformer(Projection projection){
        this.projection = projection;
    }

    /**
     * convertit une « carte » OSM en une carte géométrique projetée
     * @param map
     * @return
     */
    public Map transform(OSMMap map) {
        PolyLine.Builder polyLineBuilder = new PolyLine.Builder();

        for (OSMWay w : map.ways()) {
            for (OSMNode n : w.nodes()) {
                polyLineBuilder.addPoint(projection.project(n.position()));
            }

            String areaValue = w.attributeValue("area");
            boolean isArea = AREA_VALUE.contains(areaValue);

            if (isArea || !w.attributes().keepOnlyKeys(AREA_ATTRIBUTS).isEmpty()) {
                Attributes att = w.attributes().keepOnlyKeys(FILTRE_POLYGON);

                if (att.size() > 0){
                    builder.addPolygon(new Attributed<Polygon>(new Polygon(polyLineBuilder.buildClosed()), att));
                }

            } else {
                Attributes att = w.attributes().keepOnlyKeys(FILTRE_POLYLINE);
                if (att.size() > 0) {
                    builder.addPolyLine(new Attributed<PolyLine>(polyLineBuilder.buildOpen(), att));
                }
            }
        }

        for (OSMRelation r : map.relations()) {
            boolean isMultipolygon = r.attributeValue("type") == null ? false : r.attributeValue("type").equals("multipolygon");
            if (isMultipolygon || !r.attributes().keepOnlyKeys(AREA_ATTRIBUTS).isEmpty()) {
                Attributes att = r.attributes().keepOnlyKeys(FILTRE_POLYGON);
                if (att.size() > 0) {
                    builder.addPolygon(new Attributed<Polygon>(new Polygon(polyLineBuilder.buildClosed()), att));
                }
            }
        }

        return builder.build();
    }

    /**
     * calcule et retourne l'ensemble des anneaux de la relation donnée ayant le rôle spécifié
     * @param relation Relation donnée
     * @param role Role spécifié de la relation donnée
     * @return l'ensemble des anneaux de la relation donnée ayant le rôle spécifié
     *         ou une liste vide si le calcul des anneaux échoue
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role){
        Set<OSMNode> nodesNotVisited;
        Set<OSMNode> nodesVisited = new HashSet<>();
        Graph.Builder<OSMNode> g = new Graph.Builder<OSMNode>();

        for(OSMRelation.Member m : relation.members()){
            if((m.type() == OSMRelation.Member.Type.WAY) && (m.role().equals(role))){
                OSMWay w = (OSMWay) m.member();
                for(int i = 0; i < w.nodes().size(); ++i){
                    g.addNode(w.nodes().get(i-1));
                    g.addNode(w.nodes().get(i));
                    g.addEdge(w.nodes().get(i-1), w.nodes().get(i));
                }
            }
        }

        Graph<OSMNode> graph = g.build();
        for(OSMNode n : graph.nodes()){
            if(graph.neighborsOf(n).size() != 2){
                graph = new Graph<OSMNode>(new HashMap<OSMNode, Set<OSMNode>>());
            }
        }

        nodesNotVisited = new HashSet<>(graph.nodes());

        List<ClosedPolyLine> rings = new ArrayList<>();

        do{
            for(OSMNode n : nodesNotVisited){
                PolyLine.Builder polyLineBuilder = new PolyLine.Builder();
                if(nodesNotVisited.contains(n)){
                    polyLineBuilder.addPoint(projection.project(n.position()));

                    do{
                        for(OSMNode m : graph.neighborsOf(n)){
                            if(nodesNotVisited.contains(m)){
                                polyLineBuilder.addPoint(projection.project(m.position()));
                                nodesNotVisited.remove(m);
                                nodesVisited.add(m);
                            }
                        }
                    }while(!graph.neighborsOf(n).isEmpty());
                }

                nodesNotVisited.remove(n);
                nodesVisited.add(n);

                rings.add(polyLineBuilder.buildClosed());
            }
        }while(!nodesNotVisited.isEmpty());

        return rings;
    }

    /**
     * calcule et retourne la liste des polygones attribués de la relation donnée, en leur attachant les attributs donnés
     * @param relation
     * @param attributes
     * @return la liste des polygones attribués de la relation donnée
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){
        List<ClosedPolyLine> inners = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outers = ringsForRole(relation, "outer");

        java.util.Map<ClosedPolyLine, List<ClosedPolyLine>> prePolygon = new HashMap<>();
        for(ClosedPolyLine o : outers){
            prePolygon.put(o, new ArrayList<ClosedPolyLine>());
        }

        Collections.sort(outers, (o1, o2) -> ((Double) o1.area()).compareTo(o2.area()));

        for(ClosedPolyLine i : inners){
            ClosedPolyLine shell = null;
            for(ClosedPolyLine o : outers){
                if(o.containsPoint(i.firstPoint())){
                    shell = o;
                    break;
                }
            }

            if(shell != null){
                prePolygon.get(shell).add(i);
            }
        }

        List<Attributed<Polygon>> attributedPolygon = new ArrayList<>();
        Attributes att = attributes.keepOnlyKeys(FILTRE_POLYGON);
        if(att.size()>0){
            for(ClosedPolyLine o : outers){
                attributedPolygon.add(new Attributed<Polygon>(new Polygon(o, prePolygon.get(o)), att));
            }
        }

        return attributedPolygon;
    }


}
