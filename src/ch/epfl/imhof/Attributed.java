package ch.epfl.imhof;

import java.util.Collections;

/**
 * représente une entité de type T dotée d'attributs
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;
    
    /**
     * construit une valeur attribuée dont la valeur et les attributs sont ceux donnés
     * @param value
     * @param attributes
     */
    public Attributed(T value, Attributes attributes){
        this.value = value;
        this.attributes = attributes;
    }
    
    /**
     * retourne la valeur à laquelle les attributs sont attachés
     * @return
     */
    public T value(){
        return value;
    }
    
    /**
     * retourne les attributs attachés à la valeur
     * @return
     */
    public Attributes attributes(){
        return attributes;
    }
    
    /**
     * retourne vrai si et seulement si les attributs incluent celui dont le nom est passé en argument
     * @param AttributeName
     * @return
     */
    public boolean hasAttribute(String AttributeName){
        return attributes.contains(AttributeName);
    }
    
    /**
     * retourne la valeur associée à l'attribut donné, ou null si celui-ci n'existe pas
     * @param attributeName
     * @return
     */
    public String attributeValue(String attributeName){
        return attributes.get(attributeName);
    }
    
    /**
     * retourne la valeur associée à l'attribut donné, ou la valeur par défaut donnée si celui-ci n'existe pas
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public String attributeValue(String attributeName, String defaultValue){
        return attributes.get(attributeName, defaultValue);
    }
    
    /**
     * retourne la valeur entière associée à l'attribut donné, ou la valeur par défaut 
     * si celui-ci n'existe pas ou si la valeur qui lui est associée n'est pas un entier valide
     * @param attributeName
     * @param defaultValue
     * @return
     */
    public int attributeValue(String attributeName, int defaultValue){
        return attributes.get(attributeName, defaultValue);
    }
    
}
