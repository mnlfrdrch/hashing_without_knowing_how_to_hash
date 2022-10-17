package main.java.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTree;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTreeFinalLayer;
import yao.gate.Wire;

import java.util.ArrayList;
import java.util.List;

/**
 * When Yao's garbled circuit is built, the sender of oblivious transfer needs to cache the used wires,
 * so the receiver can request the wire labels to each circuit in order to evaluate the garbled circuit
 * The WireCache therefore caches wires, which can be accessed by its wire address
 * However to prevent a potentially malicious receiver from gathering more than one wire label per circuit input bit,
 * and evaluate more than just one value per ciruit, access needs to be restriced.
 * WireCache therefore only allows one request per existing wires
 */
public class WireCache {

    private AccessControlTree<Wire> wireCacheMap;

    private static WireCache singleton;

    private WireCache(){
        wireCacheMap=new AccessControlTree<>();
    }

    /**
     * There should only be one wire cache to control the access of wires
     * All wires should be cached by one object, and they should only be accessible once
     * Therefore WireCache implements the singleton pattern
     * @return reference to only instance of WireCache
     */
    public static WireCache getInstance(){
        if(WireCache.singleton==null){
            WireCache.singleton=new WireCache();
        }
        return WireCache.singleton;
    }

    /**
     * Allows to access addressed wire exactly once
     * Prohibits access if wire not in cache or accessed more than just once
     * @param wireAddress requested by receiver
     * @return specified wire instance if in cache and *null*, if accessed twiche or not in cache
     */
    public Wire requestWire(WireAddressDto wireAddress){
        List<String> idList=convertCircuitAddressToAddressListForWireControlTree(wireAddress.getCircuitsContainerHash(),wireAddress.getCircuitHash());
        idList.add(wireAddress.getWireId());
        return wireCacheMap.get(idList);
    }

    /**
     * Allows caching all bunchOfWires of a transferred circuit specified by its circuits container hash and its circuit hash
     * Wires need to be collected in final layer of AccessControlTree already
     * They can then be inserted easily into the underlying AccessControlTree, which restricts access
     * @param circuitsContainerHash of transmitted circuit as part of the address the wires should be accessible
     * @param circuitHash of transmitted circuit as part of the address the wires should be accessible
     * @param bunchOfWires organised within an AccessControlTreeFinalLayer
     */
    public void cacheWires(int circuitsContainerHash, int circuitHash, AccessControlTreeFinalLayer<Wire> bunchOfWires){
        List<String> idList=convertCircuitAddressToAddressListForWireControlTree(circuitsContainerHash, circuitHash);
        wireCacheMap.set(idList, bunchOfWires);
    }

    private List<String> convertCircuitAddressToAddressListForWireControlTree(int circuitsContainerHash, int circuitHash){
        List<String> idList=new ArrayList<>();
        idList.add(String.valueOf(circuitsContainerHash));
        idList.add(String.valueOf(circuitHash));
        return idList;
    }

}
