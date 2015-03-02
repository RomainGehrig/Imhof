package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * représente une relation OSM
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class OSMRelation extends OSMEntity {
    
    private List<Member> members;

    public OSMRelation(long id, Attributes attributes) {
        super(id, attributes);
    }
    
    /**
     * construit une relation étant donnés son identifiant unique, ses membres et ses attributs
     * @param id
     * @param members
     * @param attributes
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes){
        super(id, attributes);
        this.members = members;
    }
    
    /**
     * retourne la liste des membres de la relation
     * @return la lsite des membres de la relation
     */
    public List<Member> members(){
        return members;
    }
    
    
    /**
     * représente un membre d'une relation OSM
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public static final class Member{
        
        private Type type;
        private String role;
        private OSMEntity member;
        
        /**
         * construit un membre ayant le type, le rôle et la valeur donnés
         * @param type Le type donné
         * @param role Le role donné
         * @param member L'OSMEntity donnée
         */
        public Member(Type type, String role, OSMEntity member){
            this.type = type;
            this.role = role;
            this.member = member;
        }
        
        /**
         * retourne le type du membre
         * @return le type du membre
         */
        public Type type(){
            return type;
        }
        
        /**
         * retourne le rôle du membre
         * @return le rôle du membre
         */
        public String role(){
            return role;
        }
        
        /**
         * retourne le membre lui-même
         * @return le membre lui-même
         */
        public OSMEntity member(){
            return member;
        }
        
        /**
         * énumère les trois types de membres qu'une relation peut comporter
         * NODE pour les nœuds, WAY pour les chemins, et RELATION pour les relations
         * @author Yura Tak (247528)
         * @author Romain Gehrig (223316)
         *
         */
        public static enum Type{
            NODE, WAY, RELATION
        }
    }
    
    
    
    /**
     * sert de bâtisseur à la classe OSMRelation et permet de construire une relation en plusieurs étapes
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public static final class Builder extends OSMEntity.Builder{
        
        private OSMRelation relation;
        private List<Member> m;
        private Attributes attributes;
        
        /**
         * construit un bâtisseur pour une relation ayant l'identifiant donné
         * @param id L'identifiant unique
         */
        public Builder(long id){
            super(id);
            m = new ArrayList<>();
        }
        
        /**
         * ajoute un nouveau membre de type et de rôle donnés à la relation
         * @param type
         * @param role
         * @param newMember
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember){
            Member newmember = new Member(type, role, newMember);
            m.add(newmember);
        }
        
        /**
         * construit et retourne la relation ayant l'identifiant passé au constructeur 
         * ainsi que les membres et les attributs ajoutés jusqu'à présent au bâtisseur
         * @return la relation ayant l'identifiant passé au constructeur ainsi que les membres et les attributs ajoutés jusqu'à présent au bâtisseur
         * @throws IllegalStateException si la relation en cours de construction est incomplète
         */
        public OSMRelation build(){
            if(isIncomplete()){
                throw new IllegalStateException();
            }
            relation = new OSMRelation(id, m, attributes);
            return relation;
        }
        
        
    }
}
