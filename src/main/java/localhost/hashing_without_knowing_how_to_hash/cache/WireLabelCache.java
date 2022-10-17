package localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.receiver.WireLabelReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * For evaluation of the garbled circuit the receiver might need to access the same wire label multiple times.
 * However, the receiver of the oblivious transfer allows only on wire label for each individual wire to be transferred
 * Also receiving the same wire labels over and over again using oblivious transfer would lead to terrible performance
 * Therefore the receiver needs to cache once transferred wire label
 * The WireLabelCache allows the selected wire label to be called by its wire address
 */
public class WireLabelCache {

    private static WireLabelCache singleton;
    private Map<String, byte[]> wireAddressToWireLabel;
    private WireLabelCache(){
        wireAddressToWireLabel =new HashMap<>();
    }

    /**
     * There should be exactly one WireLabelCache per party
     * For this reason WireLabelCache implements the singleton pattern, which allows for one instance only
     * @return reference to the only WireLabelCache instance
     */
    public static WireLabelCache getInstance(){
        if(singleton==null){
            singleton=new WireLabelCache();
        }
        return singleton;
    }

    /**
     * Implements the access to wire label specified by host and its wire address
     * 'existsWireLabel()' should be called before to check, whether requested wire label is actually cached
     * If addressed wire label is not cached yet, oblivious transfer will be performed for specified host, wire and with default selection flag b=false
     * @param hostDto which host the circuit had been received from
     * @param wireAddress of wire label
     * @return previously received wire label
     */
    public byte[] requestWireLabel(HostDto hostDto, WireAddressDto wireAddress){
        if(existsWireLabel(wireAddress)){
            return wireAddressToWireLabel.get(wireAddress.toString());
        }
        else {
            return cacheNewWireLabel(hostDto, wireAddress, false);
        }
    }

    /**
     * Performs oblivious transfer for specified parameters
     * Received wire label is then cached and accessible by use of 'requestWireLabel()'
     * @param hostDto which is sender of wire label in oblivious transfer
     * @param wireAddress of requested wire label
     * @param b assignment of wire
     * @return wire label for selection received in oblivious transfer
     */
    public byte[] cacheNewWireLabel(HostDto hostDto, WireAddressDto wireAddress, boolean b){
        WireLabelReceiver receiver = new WireLabelReceiver(hostDto, wireAddress, b);
        byte[] receivedWireLabel = receiver.receive();
        wireAddressToWireLabel.put(wireAddress.toString(), receivedWireLabel);
        return receivedWireLabel;
    }

    /**
     * Checks whether addressed wire label is already in cache and can therefore be called by 'requestWireLabel()'
     * or if it needs to be received and stored in cache first by calling 'cacheNewWireLabel()'
     * @param wireAddress
     * @return *true* if it exists in cache, else *false*
     */
    public boolean existsWireLabel(WireAddressDto wireAddress){
        return wireAddressToWireLabel.containsKey(wireAddress.toString());
    }

}
