package main.java.hashing_without_knowing_how_to_hash.model.circuit;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.GatesEncodingContainerDto;
import lombok.Getter;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds encoding of input intermediate gates
 * Gates are encoded as numerical string
 * Therefore provides aliases to gate instances, such ac 5, 132 or 56
 * This makes smaller transport format of a garbled circuit possible
 */
public class GateEncoding {

    /**
     * Gates are stored in byte array form,
     * so it can be transferred as part of the garbled circuit transport format
     */
    @Getter
    private Map<byte[][], String> gateToId;

        public GateEncoding(){
            gateToId =new HashMap();
        }

        /**
         * Store gate instance g and get unique numerical id back
         * @param gate is Gate to set
         * @return id of this specific gate
         */
        public String setGate(Gate gate){
            byte[][] encodedGate=gate.getLut();
            //numerical id is "position" of this gate in map

            String id=null;
            if(isExisting(gate)){
                id= gateToId.get(encodedGate);
            }
            else {
                id = String.valueOf(gateToId.size() + 1);
                gateToId.put(encodedGate, id);
            }

            return id;
        }

    /**
     * Get previously stored instance of gate to gate id
     * @param id the id of the gate instance
     * @return corresponding Gate instance to gate id
     */
    public Gate getGate(String id){
            BiMap reverse=copyToBiMap().inverse();
            byte[][] encodedGate=null;
            Object obj=reverse.get(id);
            if(obj instanceof byte[][] && isGateEncoding(id)){
                encodedGate=(byte[][])obj;
            }
            else{
                encodedGate=new byte[4][Wire.AES_KEYLENGTH];
            }

            Gate gate=new Gate(encodedGate);
            return gate;
        }

        private boolean isGateEncoding(String id){
            boolean isGateEncoding=true;
            try{
                int num=Integer.parseInt(id);
                if(num<0){
                    isGateEncoding=false;
                }
                if(id.charAt(0)=='0'){
                    isGateEncoding=false;
                }
            }
            catch (NumberFormatException e){
                isGateEncoding=false;
            }
            catch (Exception e){
                e.printStackTrace();
            }
            return isGateEncoding;
        }


        private boolean isExisting(Gate gate){
            byte[][] encryptedGate=gate.getLut();
            return gateToId.containsKey(encryptedGate);
        }

        private BiMap<byte[][], String> copyToBiMap(){
            BiMap<byte[][], String> bimap=HashBiMap.create();
            bimap.putAll(gateToId);
            return bimap;
        }

    /**
     * Wraps the collected data into a GatesEncodingContainer instance
     * @return instance of GatesEncodingContainer
     */
    public GatesEncodingContainerDto getGatesEncodingContainer(){
            BiMap<byte[][], String> invertibleMap=copyToBiMap();
            Map<String, byte[][]> invertedMap=new HashMap<>(invertibleMap.inverse());
            return new GatesEncodingContainerDto(invertedMap);
    }

}
