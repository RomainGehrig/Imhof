package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

/**
 * détermine si une entité attribuée donnée doit être gardée ou non
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public final class Filters {
    
    /**
     * construit les filtres
     */
    private Filters(){
        
    }
    
    /**
     * vérifie si on applique à une entité attribuée portant un certain nom
     * @param attribut L'attribut passé en argument
     * @return vrai si la valeur attribuée à laquelle on l'applique possède un attribut portant ce nom
     */
    public static Predicate<Attributed<?>> tagged(String attribut){
        return x->x.hasAttribute(attribut);
    }
    
    /**
     * vérifie si on applique à une entité attribuée portant un certain nom et si la valeur associée à cet attribut fait partie de celles données
     * @param attribut  L'attribut passé en argument
     * @param valeur La valeur passé en argument
     * @return vrai si la valeur attribuée à laquelle on l'applique possède un attribut portant le nom donné 
     * et si la valeur associée à cet attribut fait partie de celles données
     */
    public static Predicate<Attributed<?>> tagged(String attribut, String... valeur){
        List<String> l = Arrays.asList(valeur);
        return x->x.hasAttribute(attribut) && l.contains(x.attributeValue(attribut));
    }
    
    /**
     * vérifie si on applique à une entité attribuée appartenant à une certaine couche
     * @param i Layer passé en argument
     * @return vrai lorsqu'on l'applique à une entité attribuée appartenant cette couche
     */
    public static Predicate<Attributed<?>> onLayer(int i){
        return x->x.attributeValue("layer", 0) == i;
    }
    
    
    
}
