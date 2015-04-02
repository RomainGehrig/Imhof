package ch.epfl.imhof.osm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.LinkedList;
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
    private final Map.Builder builder;

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
        this.builder = new Map.Builder();
    }

    /**
     * convertit une « carte » OSM en une carte géométrique projetée
     * @param map
     * @return
     */
    public Map transform(OSMMap map) {
        PolyLine.Builder polyLineBuilder;

        // Conversion des OSMWay
        for (OSMWay way: map.ways()) {
            polyLineBuilder = new PolyLine.Builder();

            for (OSMNode node: way.nodes()) {
                polyLineBuilder.addPoint(projection.project(node.position()));
            }

            boolean isClosed = AREA_VALUE.contains(way.attributeValue("area")) || !way.attributes().keepOnlyKeys(AREA_ATTRIBUTS).isEmpty();

            if (isClosed) {
                Attributes attr = way.attributes().keepOnlyKeys(FILTRE_POLYGON);
                if (!attr.isEmpty()) // Il faut qu'il y ait des attributs pour pouvoir ajouter un polygone
                    builder.addPolygon(new Attributed<>(new Polygon(polyLineBuilder.buildClosed()),attr));
            } else {
                Attributes attr = way.attributes().keepOnlyKeys(FILTRE_POLYLINE);
                if (!attr.isEmpty()) // Il faut qu'il y ait des attributs pour pouvoir ajouter une polyline
                    builder.addPolyLine(new Attributed<>(polyLineBuilder.buildOpen(),attr));
            }
        }

        // Conversion des OSMRelation
        for (OSMRelation rel: map.relations()) {
            for (Attributed<Polygon> poly: assemblePolygon(rel, rel.attributes()))
                builder.addPolygon(poly);
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
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<OSMNode>();

        // On extrait tous les noeuds des ways
        for (OSMRelation.Member member: relation.members()) {

            if (!member.role().equals(role) || member.type() != OSMRelation.Member.Type.WAY) // On skip si le membre n'est pas du bon rôle ou n'est pas une WAY
                continue;

            OSMWay way = null;
            try {
                way = (OSMWay) member.member();
            } catch (ClassCastException e) {
                // On skip le membre s'il est malformé
                // (le type indique que c'est une WAY et on n'arrive pas à le cast en OSMWay => malformé)
                continue;
            }

            Iterator<OSMNode> nodes1 = way.nodes().iterator();
            Iterator<OSMNode> nodes2 = way.nodes().iterator();
            if (!nodes2.hasNext())
                continue;

            graphBuilder.addNode(nodes2.next());
            while (nodes2.hasNext()) {
                OSMNode n1 = nodes1.next();
                OSMNode n2 = nodes2.next();
                graphBuilder.addNode(n2);
                graphBuilder.addEdge(n1,n2);
            }
        }

        Graph graph = graphBuilder.build();

        Set<OSMNode> visited = new HashSet<>();
        Deque<OSMNode> toVisit = new LinkedList<>();
        toVisit.addAll(graph.nodes());

        List<ClosedPolyLine> rings = new ArrayList<>();
        while (!toVisit.isEmpty()) {
            PolyLine.Builder polyLineBuilder = new PolyLine.Builder();
            Deque<OSMNode> visiting = new LinkedList<>();

            OSMNode startNode = toVisit.removeFirst();
            if (graph.neighborsOf(startNode).size() != 2)
                return null;

            visiting.addAll(graph.neighborsOf(startNode));
            visiting.removeFirst(); // On supprime un des noeuds voisins (pour garder qu'une direction)

            OSMNode currentNode = null;
            OSMNode previousNode = startNode;

            // Recherche de la chaîne
            while (!visiting.isEmpty()) {
                currentNode = visiting.removeFirst();
                visited.add(currentNode);

                if (currentNode.equals(startNode)) {
                    polyLineBuilder.addPoint(projection.project(currentNode.position()));
                    rings.add(polyLineBuilder.buildClosed());
                    break;
                }

                Set<OSMNode> neighbors = graph.neighborsOf(currentNode);
                if (neighbors.size() != 2) {
                    return null;
                }

                for (OSMNode neighbor: neighbors) {
                    if (!visited.contains(neighbor) && !neighbor.equals(previousNode)) {
                        visiting.add(neighbor);
                    }
                }
                polyLineBuilder.addPoint(projection.project(currentNode.position()));
                previousNode = currentNode;
            }
        }

        return rings;
    }

    /* Aucune utilité finalement :(
    private Set<OSMNode> connectedNeighbors(Graph graph, OSMNode startNode) {
        Set<OSMNode> visited = new HashSet<>();
        Deque<OSMNode> toVisit = new LinkedList<>();

        toVisit.addAll(graph.neighborsOf(startNode));
        OSMNode currentNode = null;
        while (!toVisit.isEmpty()) {
            currentNode = toVisit.removeFirst();
            visited.add(currentNode);

            Set<OSMNode> neighbors = graph.neighborsOf(currentNode);
            for (OSMNode neighbor: neighbors)
                if (!visited.contains(neighbor))
                    toVisit.add(neighbor);

        }
        return visited;
    }
    */

    /**
     * calcule et retourne la liste des polygones attribués de la relation donnée, en leur attachant les attributs donnés
     * @param relation
     * @param attributes
     * @return la liste des polygones attribués de la relation donnée
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes){
        Attributes attr = attributes.keepOnlyKeys(FILTRE_POLYGON);

        List<Attributed<Polygon>> attributedPolygons = new ArrayList<>();
        // On ne fait rien s'il n'y a pas d'attributs
        if (attr.isEmpty())
            return attributedPolygons;

        List<ClosedPolyLine> inners = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outers = ringsForRole(relation, "outer");

        java.util.Map<ClosedPolyLine, List<ClosedPolyLine>> plainPolygones = new HashMap<>();
        for (ClosedPolyLine out: outers)
            plainPolygones.put(out, new ArrayList<>());

        // Tri des polylines extérieures
        Collections.sort(outers, (o1, o2) -> ((Double) o1.area()).compareTo(o2.area()));

        for (ClosedPolyLine in: inners) {
            ClosedPolyLine correspondingOuter = null;

            for (ClosedPolyLine out: outers) {
                boolean isInside = false;
                for (Point p: in.points()) {
                    if (out.containsPoint(p)) {
                        isInside = true;
                        break;
                    }
                }

                if (isInside) {
                    correspondingOuter = out;
                    break;
                }
            }

            if (correspondingOuter != null)
                plainPolygones.get(correspondingOuter).add(in);

        }

        for (java.util.Map.Entry<ClosedPolyLine, List<ClosedPolyLine>> polyEntry: plainPolygones.entrySet()) {
            Polygon polygon = new Polygon(polyEntry.getKey(), polyEntry.getValue());

            attributedPolygons.add(new Attributed<Polygon>(polygon, attr));
        }

        return attributedPolygons;
    }


}
