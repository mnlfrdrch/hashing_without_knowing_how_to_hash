package main.java.hashing_without_knowing_how_to_hash.security.access_control_tree;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides wires on sender side of oblivious transfer
 * Ts can be accessed by id
 * Access control prevents multiple accesses of the same wire and stops potential malicious activities by the receiver
 */
public class AccessControlTreeFinalLayer<T> {

    private Map<String, T> idAndCorrespondingElement;
    private Map<T, Boolean> elementAndCorrespondingAccessStatus;
    @Getter
    private int numElementsAlreadyAccessed;

    public AccessControlTreeFinalLayer(Map<String, T> idAndElementMap){
        idAndCorrespondingElement=idAndElementMap;
        initialiseAccessStatusMap();
        numElementsAlreadyAccessed =0;
    }

    private void initialiseAccessStatusMap(){
        if(idAndCorrespondingElement==null){
            idAndCorrespondingElement=new HashMap<>();
        }
        elementAndCorrespondingAccessStatus=new HashMap<>();
        for(T t:idAndCorrespondingElement.values()){
            elementAndCorrespondingAccessStatus.put(t,false);
        }
    }

    public boolean isInvalid(){
        return numElementsAlreadyAccessed ==elementAndCorrespondingAccessStatus.size();
    }

    public T accessElement(String id){
        T elementToReturn=null;
        T requestedElement=idAndCorrespondingElement.get(id);
        if(requestedElement!=null) {
            boolean isAlreadyUsed = elementAndCorrespondingAccessStatus.get(requestedElement);
            if (!isAlreadyUsed) {
                markElementAsAccessed(requestedElement);
                elementToReturn = requestedElement;
            } else {
                printIllegalAccessWithIdToStacktrace(id);
            }
        }
        return elementToReturn;
    }

    private void printIllegalAccessWithIdToStacktrace(String id){
        try{
            throw new Exception("Illegal access to already used wire with id: " + id);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void markElementAsAccessed(T element){
        numElementsAlreadyAccessed++;
        elementAndCorrespondingAccessStatus.replace(element,false,true);
    }

}
