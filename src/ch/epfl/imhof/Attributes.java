package ch.epfl.imhof;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Attributes {
    private Map<String, String> attributes;
    
    public Attributes(Map<String, String> attributes){
        this.attributes = Collections.unmodifiableMap(new HashMap<String, String>(attributes));
    }
    
    public boolean isEmpty(){
        return attributes.isEmpty();
    }
    
    public boolean contains(String key){
        return attributes.containsKey(key);
    }
    
    public String get(String key){
        return attributes.get(key);
    }
    
    public String get(String key, String defaultvalue){
        if(attributes.get(key) == null){
            return defaultvalue;
        } else{
            return attributes.get(key);
        }
    }
    
    public int get(String key, int defaultvalue){
        try{
            return Integer.parseInt(attributes.get(key));
        }
        catch(NumberFormatException e){
            return defaultvalue;
        }
    }
    
    public Attributes keepOnlyKeys(Set<String> keysToKeep){
        Map<String, String> tmp = new HashMap<String, String>();
        for(String key : keysToKeep){
            tmp.put(key, get(key));
        }
        return new Attributes(tmp);
    }
    
    public final static class Builder{
        private Map<String, String> attributes;
        
        public void put(String key, String value){
            attributes.put(key, value);
        }
        
        public Attributes build(){
            return (new Attributes(attributes));
        }
    }
    
}
