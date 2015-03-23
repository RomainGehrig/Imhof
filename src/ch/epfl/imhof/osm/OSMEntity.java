package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * sert de classe mère à toutes les classes représentant les entités OSM
 * @author Yura Tak (247528)
 * @author Romain Gehrig (223316)
 *
 */
public abstract class OSMEntity {
    protected long id;
    private Attributes attributes;

    /**
     * construit une entité OSM dotée de l'identifiant unique et des attributs donnés
     * @param id L'identifiant unique
     * @param attributes Attributs donnés
     * @throws NullPointerException si attributes est vide
     */
    public OSMEntity(long id, Attributes attributes){
        if (attributes == null)
            throw new NullPointerException("Attributes cannot be null.");

        this.id = id;
        this.attributes = attributes;
    }

    /**
     * retourne l'identifiant unique de l'entité
     * @return l'identifiant unique
     */
    public long id(){
        return id;
    }

    public Attributes attributes(){
        return attributes;
    }

    /**
     * retourne vrai si et seulement si l'entité possède l'attribut passé en argument.
     * @param key La clef passée en argument
     * @return vrai ssi l'entité possède le key
     */
    public boolean hasAttribute(String key){
        return attributes.contains(key);
    }

    /**
     * retourne la valeur de l'attribut donné
     * @param key La clef passée en argument
     * @return la valeur de l'attribut donné ou null si celui-ci n'existe pas
     */
    public String attributeValue(String key){
        return attributes.get(key);
    }

    /**
     * sert de classe mère à toutes les classes de bâtisseurs d'entités OSM
     * @author Yura Tak (247528)
     * @author Romain Gehrig (223316)
     *
     */
    public static class Builder{

        private long id;
        protected Attributes.Builder attributes = new Attributes.Builder();
        private boolean isIncomplete = false;

        /**
         * construit un bâtisseur pour une entité OSM identifiée par l'entier donné
         * @param id L'identifiant unique
         */
        public Builder(long id){
            this.id = id;
        }

        public long id() {
            return id;
        }

        /**
         * ajoute l'association (clef, valeur) donnée à l'ensemble d'attributs de l'entité en cours de construction.
         * Si un attribut de même nom avait déjà été ajouté précédemment, sa valeur est remplacée par celle donnée
         * @param key La clef
         * @param value La valeur
         */
        public void setAttribute(String key, String value){
            attributes.put(key, value);
        }

        /**
         * déclare que l'entité en cours de construction est incomplète
         */
        public void setIncomplete(){
            isIncomplete = true;
        }

        /**
         * retourne vrai si et seulement si l'entité en cours de construction est incomplète,
         * c-à-d si la méthode setIncomplete a été appelée au moins une fois sur ce bâtisseur depuis sa création.
         * @return
         */
        public boolean isIncomplete(){
            return isIncomplete;
        }
    }
}
