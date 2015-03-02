package ch.epfl.imhof;

import java.util.Collections;

/**
 * Représente une entité de type T dotée d'attributs
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Attributed<T> {
    private final T value;
    private final Attributes attributes;
    
    /**
     * Construit une valeur attribuée dont la valeur et les attributs sont ceux donnés
     * @param value Valeur en question
     * @param attributes attributs de la valeur
     */
    public Attributed(T value, Attributes attributes){
        this.value = value;
        this.attributes = attributes;
    }
    
    /**
     * Getter sur la valeur
     * @return valeur
     */
    public T value(){
        return value;
    }
    
    /**
     * Getter sur les attributes
     * @return l'objet attributes
     */
    public Attributes attributes(){
        return attributes;
    }
    
    /**
     * Retourne vrai si et seulement si les attributs incluent celui dont le nom est passé en argument
     * @param attributeName Le nom de l'attribut dont on veut vérifier l'existence
     * @return true si l'attribut existe, false sinon
     */
    public boolean hasAttribute(String attributeName){
        return attributes.contains(attributeName);
    }
    
    /**
     * Retourne la valeur associée à l'attribut donné, ou null si celui-ci n'existe pas
     * @param attributeName Le nom de l'attribut dont on veut la valeur
     * @return La valeur associée à l'attribut donné (null si inexistant)
     */
    public String attributeValue(String attributeName){
        return attributes.get(attributeName);
    }
    
    /**
     * Retourne la valeur associée à l'attribut donné, ou la valeur par défaut donnée si celui-ci n'existe pas
     * @param attributeName Le nom de l'attribut dont on veut la valeur
     * @param defaultValue La valeur a retourner si l'attribut n'existe pas
     * @return La valeur associée à l'attribut donné (defaultValue si inexistant)
     */
    public String attributeValue(String attributeName, String defaultValue){
        return attributes.get(attributeName, defaultValue);
    }
    
    /**
     * Retourne la valeur entière associée à l'attribut donné, ou la valeur par défaut 
     * si celui-ci n'existe pas ou si la valeur qui lui est associée n'est pas un entier valide
     * @param attributeName Le nom de l'attribut dont on veut la valeur
     * @param defaultValue La valeur a retourner si l'attribut n'existe pas
     * @return La valeur en int associée à l'attribut donné (defaultValue si la conversion échoue
     * ou si la valeur n'existe pas)
     */
    public int attributeValue(String attributeName, int defaultValue){
        return attributes.get(attributeName, defaultValue);
    }
    
}
