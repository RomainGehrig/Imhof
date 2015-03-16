package ch.epfl.imhof.osm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    
    private static OSMMap.Builder m;
    
    /**
     * constructeur par défaut privé et vide qui rend la classe non instanciable
     */
    private OSMMapReader(){}
    
    /**
     * lit la carte OSM contenue dans le fichier de nom donné,
     * en le décompressant avec gzip si et seulement si le second argument est vrai
     * @param fileName Contient la carte OSM
     * @param unGZip
     * @return l'OSMMap
     * @throws SAXException en cas d'erreur dans le format du fichier XML contenant la carte
     * @throws IOException en cas d'autre erreur d'entrée/sortie, p.ex. si le fichier n'existe pas
     */
    public static OSMMap readOSMFile(String fileName, boolean unGZip) throws IOException, SAXException {
        m = new OSMMap.Builder();
        try (InputStream i = new FileInputStream(OSMMapReader.class.getResource(fileName).getFile())) {
            InputStream ii;
            if(!unGZip){
                ii = i;
            } else{
                ii = new GZIPInputStream(i);
            }
            XMLReader r = XMLReaderFactory.createXMLReader();
            r.setContentHandler(new OSMMapHandler());
            r.parse(new InputSource(ii));
        }
        return m.build();
    }
    
    public static void main(String[] args) throws IOException, SAXException{
        OSMMapReader.readOSMFile("/bc.osm", false);
    }
    
    public final static class OSMMapHandler extends DefaultHandler{
        
        ArrayList<OSMEntity.Builder> a = new ArrayList<>();
        
        @Override
        public void startElement(String uri,
                                 String lName,
                                 String qName,
                                 Attributes atts)
          throws SAXException {
          switch(lName){
          case "node" :
              long id = Long.parseLong(atts.getValue("id"));
              PointGeo p = new PointGeo(Math.toRadians(Double.parseDouble(atts.getValue("lon"))), Math.toRadians(Double.parseDouble(atts.getValue("lat"))));
              OSMNode.Builder n = new OSMNode.Builder(id, p);
              a.add(n);
              break;
          
          case "way" :
              
              break;
          
          case "nd" :
              
              break;
              
          case "tag" :
              
              break;
              
          case "relation" :
              
              break;
          
          case "member" :
              
              break;
              
          default :
              
              break;
              
          }
        }

        @Override
        public void endElement(String uri,
                               String lName,
                               String qName) {
            
            OSMEntity.Builder b = a.get(a.size()-1);
            
            switch(lName){
            case "node" :
                m.addNode(((OSMNode.Builder)b).build());
                break;
            
            case "way" :
                
                break;
                
            case "relation" :
                
                break;
            
            default :
                
                break;
                
            }
        }
    }
    
    
}
