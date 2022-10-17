package main.java.hashing_without_knowing_how_to_hash.security.access_control_tree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Data structure that stores values to a given key, but elements are just accessible once
 * @param <T>
 */
public class AccessControlTree<T> {

    Map<String, AccessControlTreeFinalLayer<T>> map;

    public AccessControlTree(){
        map=new HashMap<>();
    }

    public void set(List<String> id, AccessControlTreeFinalLayer<T> finalLayer){
        String pathId=concatStringsInList(id);
        map.put(pathId, finalLayer);
    }

    public T get(List<String> id){
        T requestedElement=null;
        String elementId=getLastElementFromList(id);
        List<String> allElementsButLastOne=getAllElementsButLastOne(id);
        String pathId=concatStringsInList(allElementsButLastOne);
        if(map.containsKey(pathId)){
            AccessControlTreeFinalLayer<T> finalLayer=map.get(pathId);
            requestedElement=finalLayer.accessElement(elementId);
            if(finalLayer.isInvalid()){
                map.remove(pathId);
            }
        }
        return requestedElement;
    }

    public String concatStringsInList(List<String> stringList){
        String concatenatedString="";
        for(String s:stringList){
            concatenatedString=concatenatedString+s;
        }
        return concatenatedString;
    }

    private String getLastElementFromList(List<String> stringList){
        if(stringList==null || stringList.size()<1){
            return "";
        }
        else {
            return stringList.get(stringList.size() - 1);
        }
    }

    private List<String> getAllElementsButLastOne(List<String> stringList){
        List<String> copyOfStringList=new LinkedList<>(stringList);
        if(copyOfStringList.size()<1){
            return copyOfStringList;
        }
        else {
            copyOfStringList.remove(copyOfStringList.size()-1);
        }
        return copyOfStringList;
    }

}
