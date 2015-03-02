package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * représente un ensemble d'attributs et la valeur qui leur est associée
 * @author Yura Tak(247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Attributes {
    private Map<String, String> attributes;
    
    /**
     * construit un ensemble d'attributs avec les paires clef/valeur présentes dans la table associative donnée
     * @param attributes
     */
    public Attributes(Map<String, String> attributes){
        this.attributes = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
    }
    
    /**
     * retourne vrai si et seulement si l'ensemble d'attributs est vide
     * @return
     */
    public boolean isEmpty(){
        return attributes.isEmpty();
    }
    
    /**
     * retourne vrai si l'ensemble d'attributs contient la clef donnée
     * @param key
     * @return
     */
    public boolean contains(String key){
        return attributes.containsKey(key);
    }
    
    /**
     * retourne la valeur associée à la clef donnée, ou null si la clef n'existe pas
     * @param key
     * @return
     */
    public String get(String key){
        return attributes.get(key);
    }
    
    /**
     * retourne la valeur associée à la clef donnée, ou la valeur par défaut donnée si aucune valeur ne lui est associée
     * @param key
     * @param defaultvalue
     * @return
     */
    public String get(String key, String defaultvalue){
        if(contains(key)){
            return attributes.get(key);
        } else{
            return defaultvalue;
        }
    }
    
    /**
     * retourne l'entier associé à la clef donnée, ou la valeur par défaut donnée 
     * si aucune valeur ne lui est associée, ou si cette valeur n'est pas un entier valide
     * @param key
     * @param defaultvalue
     * @return
     */
    public int get(String key, int defaultvalue){
        try{
            return Integer.parseInt(get(key)); // Throw si nbr invalide ou null
        }
        catch(NumberFormatException e){
            return defaultvalue;
        }
    }
    
    /**
     * retourne une version filtrée des attributs ne contenant que ceux dont le nom figure dans l'ensemble passé
     * @param keysToKeep
     * @return
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep){
        Map<String, String> tmp = new HashMap<String, String>();
        keysToKeep.retainAll(attributes.keySet()); // Filtre pour garder les clés qui font partie de notre attributes

        for(String key: keysToKeep){
            tmp.put(key, get(key));
        }
        return new Attributes(tmp);
    }
    
    /**
     * sert de bâtisseur à la classe Attributes
     * @author Yura Tak(247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder{
        private Map<String, String> attributes = new HashMap<>();
        
        /**
         * ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs en cours de construction
         * Si un attribut de même nom avait déjà été ajouté précédemment à l'ensemble, sa valeur est remplacée par celle donnée
         * @param key
         * @param value
         */
        public void put(String key, String value){
            attributes.put(key, value);
        }
        
        /**
         * construit un ensemble d'attributs contenant les associations clef/valeur ajoutées jusqu'à présent
         * @return
         */
        public Attributes build(){
            return (new Attributes(attributes));
        }
    }
    
}
