package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Représente un ensemble d'attributs et la valeur qui leur est associée
 * @author Yura Tak(247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Attributes {
    private final Map<String, String> attributes;

    /**
     * Construit un ensemble d'attributs avec les paires clef/valeur présentes dans la table associative donnée
     * @param attributes Map des clefs/valeurs
     */
    public Attributes(Map<String, String> attributes){
        this.attributes = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
    }

    /**
     * Retourne la taille de la map des attributs
     * @return la taille
     */
    public int size(){
        return attributes.size();
    }

    /**
     * Retourne vrai si et seulement si l'ensemble d'attributs est vide
     * @return Vrai si vide, faux sinon
     */
    public boolean isEmpty(){
        return attributes.isEmpty();
    }

    /**
     * Retourne vrai si l'ensemble d'attributs contient la clef donnée
     * @param key Clef à vérifier
     * @return Vrai si présent, faux sinon
     */
    public boolean contains(String key){
        return attributes.containsKey(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée, ou null si la clef n'existe pas
     * @param key Clef dont on veut la valeur associée
     * @return La valeur associée
     */
    public String get(String key){
        return attributes.get(key);
    }

    /**
     * Retourne la valeur associée à la clef donnée, ou la valeur par défaut donnée si aucune valeur ne lui est associée
     * @param key Nom de l'attribut
     * @param defaultvalue Valeur par défaut au cas où l'attribut n'existe pas
     * @return La valeur associée ou la valeur par défaut s'il n'y a pas de valeur associée
     */
    public String get(String key, String defaultvalue){
        if(contains(key)){
            return attributes.get(key);
        } else{
            return defaultvalue;
        }
    }

    /**
     * Retourne l'entier associé à la clef donnée, ou la valeur par défaut donnée
     * si aucune valeur ne lui est associée, ou si cette valeur n'est pas un entier valide
     * @param key Nom de l'attribut à convertir
     * @param defaultvalue Valeur par défaut au cas où l'attribut n'est pas convertible
     * @return La valeur convertie en entier, ou la valeur par défaut s'il y a eu un problème
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
     * Retourne une version filtrée des attributs ne contenant que ceux dont le nom figure dans l'ensemble passé
     * @param keysToKeep Set des clefs à garder
     * @return Un nouvel objet Attributes ne contenant que les clefs mentionnées
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
     * Sert de bâtisseur à la classe Attributes
     * @author Yura Tak(247528)
     * @author Romain Gehrig (223316)
     *
     */
    public final static class Builder{
        private Map<String, String> attributes = new HashMap<>();

        /**
         * Ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs en cours de construction
         * Si un attribut de même nom avait déjà été ajouté précédemment à l'ensemble, sa valeur est remplacée par celle donnée
         * @param key Clef
         * @param value Valeur
         */
        public void put(String key, String value){
            attributes.put(key, value);
        }

        /**
         * Construit un ensemble d'attributs contenant les associations clef/valeur ajoutées jusqu'à présent
         * @return L'objet Attributes construit
         */
        public Attributes build(){
            return (new Attributes(attributes));
        }
    }

}
