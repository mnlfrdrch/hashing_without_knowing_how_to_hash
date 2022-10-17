package localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.Sender;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.SenderBuilder;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;

/**
 * In order to perform an oblivious transfer, there needs to be a specific sender for each receiver
 * The senders methods need to be called multiple times during the oblivious transfer
 * The SenderCache.class temporarily generates and stores senders in a way, that they are accessible by their specific wire address
 * If sender is not instantiated yet, the SenderCache automatically instantiates and caches them
 */
public class SenderCache{

    private Map<String, Sender> wireAddressToSender;

    private static SenderCache singleton;

    private SenderCache(){
        wireAddressToSender =new HashMap<>();
    }

    /**
     * Instantiates a SenderCache or gives access to the only instance of SenderCache
     * SenderCache implements the singleton pattern, because there should be only one SenderCache within the runtime of this program
     * @return reference to the only instance of SenderCache
     */
    public static SenderCache getInstance(){
        if(singleton==null){
            singleton=new SenderCache();
        }
        return singleton;
    }

    /**
     * Gives access to the Sender object corresponding to given wire address
     * If already in cache, then fitting Sender object is returned
     * If not in cache yet, then new Sender object is instantiated, cached and returned
     * Important to coordinate the individual senders of various oblivious transfers
     * @param wireAddress to corresponding sender
     * @return correct instance of sender
     */
    public Sender requestSender(WireAddressDto wireAddress){
        Sender sender=null;
        if(wireAddressToSender.containsKey(wireAddress.toString())){
            sender= wireAddressToSender.get(wireAddress.toString());
        }else {
            sender=cacheNewSender(wireAddress);
        }
        return sender;
    }

    /**
     * Deletes cached sender
     * After all sender methods were called, sender is no longer needed
     * Sender can be then be deleted to free memory
     * @param wireAddress to corresponding sender
     */
    public void removeSender(WireAddressDto wireAddress){
        wireAddressToSender.remove(wireAddress.toString());
    }

    private Sender cacheNewSender(WireAddressDto wireAddress){
        Sender sender=null;
        WireCache wireCache=WireCache.getInstance();
        Wire wire=wireCache.requestWire(wireAddress);
        if(wire!=null) {
            sender = SenderBuilder.instantiateSender(wire);
            wireAddressToSender.put(wireAddress.toString(), sender);
        }
        return sender;
    }
}
