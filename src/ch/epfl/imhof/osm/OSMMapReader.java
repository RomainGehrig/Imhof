package ch.epfl.imhof.osm;

import java.util.Deque;
import java.util.ArrayDeque;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.zip.GZIPInputStream;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import ch.epfl.imhof.PointGeo;

/**
 * permet de construire une carte OpenStreetMap à partir de données stockées dans un fichier au format OSM
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OSMMapReader {

    /**
     * constructeur par défaut privé et vide qui rend la classe non instanciable
     */
    private OSMMapReader() {}

    /**
     * lit la carte OSM contenue dans le fichier de nom donné,
     * en le décompressant avec gzip si et seulement si le second argument est vrai
     * @param fileName Contient la carte OSM
     * @param unGZip
     * @return l'OSMMap
     * @throws SAXException en cas d'erreur dans le format du fichier XML contenant la carte
     * @throws IOException en cas d'autre erreur d'entrée/sortie, p.ex. si le fichier n'existe pas
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws SAXException, IOException {
        InputStream file = new FileInputStream(fileName);
        if (unGZip) {
            file = new GZIPInputStream(file);
        }

        XMLReader r = XMLReaderFactory.createXMLReader();
        OSMMap.Builder mapBuilder = new OSMMap.Builder();

        r.setContentHandler(new OSMHandler(mapBuilder));
        r.parse(new InputSource(file));
        return mapBuilder.build();
    }

    private static class OSMHandler extends DefaultHandler {

        Deque<OSMEntity.Builder> elements = new ArrayDeque<>();
        OSMMap.Builder mapBuilder;

        public OSMHandler(OSMMap.Builder builder) {
            mapBuilder = builder;
        }

        @Override
        public void startElement(String uri,
                                 String lName,
                                 String qName,
                                 Attributes atts)
        throws SAXException {
            long id;
            switch(lName) {
                case "node":
                    id = Long.parseLong(atts.getValue("id"));
                    double lon = Math.toRadians(Double.parseDouble(atts.getValue("lon")));
                    double lat = Math.toRadians(Double.parseDouble(atts.getValue("lat")));
                    PointGeo p = new PointGeo(lon, lat);
                    elements.push(new OSMNode.Builder(id, p));
                    break;
                case "way":
                    id = Long.parseLong(atts.getValue("id"));
                    elements.push(new OSMWay.Builder(id));
                    break;
                case "nd": // In OSMWay
                    try {
                        OSMNode n = mapBuilder.nodeForId(Long.parseLong(atts.getValue("ref")));
                        if (n == null)
                            elements.peek().setIncomplete();
                        else
                            ((OSMWay.Builder) elements.peek()).addNode(n);
                    } catch (ClassCastException e) {
                        throw new SAXException("Invalid use of nd");
                    }
                    break;
                case "tag": // OSMEntity
                    elements.peek().setAttribute(atts.getValue("k"), atts.getValue("v"));
                    break;
                case "relation": //
                    id = Long.parseLong(atts.getValue("id"));
                    elements.push(new OSMRelation.Builder(id));
                    break;
                case "member": // in a relation
                    OSMRelation.Member.Type type;
                    switch (atts.getValue("type")) {
                        case "way":
                            type = OSMRelation.Member.Type.WAY;
                            break;
                        case "node":
                            type = OSMRelation.Member.Type.NODE;
                            break;
                        case "relation":
                            type = OSMRelation.Member.Type.RELATION;
                            break;
                        default:
                            type = null; // will be handled later
                            break;
                    }

                    OSMEntity member = null;
                    Long refID = Long.parseLong(atts.getValue("ref"));
                    if (type != null) {
                        switch (type) {
                        case WAY:
                            member = mapBuilder.wayForId(refID);
                            break;
                        case NODE:
                            member = mapBuilder.nodeForId(refID);
                            break;
                        case RELATION:
                            member = mapBuilder.relationForId(refID);
                            break;
                        }
                        if (member == null)
                            elements.peek().setIncomplete();
                    }
                    String role = atts.getValue("role");

                    try {
                        ((OSMRelation.Builder) elements.peek()).addMember(type,role,member);
                    } catch (ClassCastException e) {
                        throw new SAXException("Invalid use of member");
                    }
                    break;
                default: return;
            }
        }

        @Override
        public void endElement(String uri,
                               String lName,
                               String qName) {
            try {
                switch(lName) {
                case "node":
                    mapBuilder.addNode(((OSMNode.Builder) elements.pop()).build());
                    break;
                case "way":
                    mapBuilder.addWay(((OSMWay.Builder) elements.pop()).build());
                    break;
                case "relation":
                    mapBuilder.addRelation(((OSMRelation.Builder) elements.pop()).build());
                    break;
                }
            } catch (IllegalStateException e) {
                // skip the element if it cannot build
            } catch (ClassCastException e) {}
        }
    }
}
