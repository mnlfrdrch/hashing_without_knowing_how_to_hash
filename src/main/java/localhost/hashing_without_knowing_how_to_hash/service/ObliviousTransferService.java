package localhost.hashing_without_knowing_how_to_hash.service;

import localhost.hashing_without_knowing_how_to_hash.cache.SenderCache;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.Sender;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

/**
 * Oblivious transfer of keys (wire labels) to securely evaluate the shared encrypted hash function circuit for specific input values
 */
@Service
@NoArgsConstructor
public class ObliviousTransferService {

    public static final PublicKeyRSADto DEFAULT_PUBLIC_KEY=new PublicKeyRSADto(new BigInteger("3"), new BigInteger("1"));
    public static final RandomMessagePairDto DEFAULT_RANDOM_MESSAGES_PAIR=new RandomMessagePairDto(1);
    public static final EncryptedSecretPairDto DEFAULT_ENCRYPTED_SECRET_PAIR=new EncryptedSecretPairDto(new BigInteger("0"), new BigInteger("0"));

    /**
     * Sends public key from sender to receiver
     * Sender is addressed by the following three parameters
     * @param circuitContainerHash
     * @param circuitHash
     * @param wireId
     * @return a wrapper of the public key
     */
    public PublicKeyRSADto getPublicKey(int circuitContainerHash, int circuitHash, String wireId){
        SenderCache senderCache=SenderCache.getInstance();
        Sender sender=senderCache.requestSender(new WireAddressDto(circuitContainerHash, circuitHash, wireId));
        if(sender!=null) {
            return sender.getPublicKeyRsa();
        }
        else {
            return DEFAULT_PUBLIC_KEY;
        }
    }

    /**
     * Sends the two random messages from sender to receiver
     * Sender is addressed by the following three parameters
     * @param circuitContainerHash
     * @param circuitHash
     * @param wireId
     * @return both random messages wrapped in a dto
     */
    public RandomMessagePairDto getRandomMessagePair(int circuitContainerHash, int circuitHash, String wireId){
        SenderCache senderCache=SenderCache.getInstance();
        Sender sender=senderCache.requestSender(new WireAddressDto(circuitContainerHash, circuitHash, wireId));
        if(sender!=null) {
            return sender.getRandomMessagePair();
        }
        else {
            return DEFAULT_RANDOM_MESSAGES_PAIR;
        }
    }

    /**
     * The receiver sends its computed value vValue to the receiver
     * The sender then ses the vValue
     * @param vValueContainerDto containing vValueDto and the address of the sender
     */
    public void setVValue(VValueContainerDto vValueContainerDto){
        WireAddressDto wireAddress=vValueContainerDto.getWireAddress();
        VValueDto vValue = vValueContainerDto.getVvalue();

        SenderCache senderCache=SenderCache.getInstance();
        Sender sender=senderCache.requestSender(wireAddress);
        sender.setVValue(vValue);
    }

    /**
     * Sends the two encrypted secrets from sender to receiver
     * Sender is addressed by the following three parameters
     * @param circuitContainerHash
     * @param circuitHash
     * @param wireId
     * @return both encrypted secrets are wrapped in a dto
     */
    public EncryptedSecretPairDto getEncryptedSecretPair(int circuitContainerHash, int circuitHash, String wireId){
        SenderCache senderCache=SenderCache.getInstance();
        Sender sender=senderCache.requestSender(new WireAddressDto(circuitContainerHash, circuitHash, wireId));
        senderCache.removeSender(new WireAddressDto(circuitContainerHash,circuitHash,wireId));
        if(sender!=null) {
            return sender.getEncryptedSecretPair();
        }
        else {
            return DEFAULT_ENCRYPTED_SECRET_PAIR;
        }
    }

}
