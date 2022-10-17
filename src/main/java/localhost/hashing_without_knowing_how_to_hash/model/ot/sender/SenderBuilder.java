package localhost.hashing_without_knowing_how_to_hash.model.ot.sender;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.PlaintextSecretPairDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.AbstractObliviousTransferSecretEncoder;
import localhost.hashing_without_knowing_how_to_hash.model.ot.WireLabelSecretEncoder;
import yao.gate.Wire;

import java.math.BigInteger;

/**
 * Provides instantiation for the senders to given input wires
 */
public class SenderBuilder {

    public static Sender instantiateSender(Wire wire){
        AbstractObliviousTransferSecretEncoder<byte[]> falseLabelEncoder=new WireLabelSecretEncoder(wire.getValue0());
        AbstractObliviousTransferSecretEncoder<byte[]> trueLabelEncoder=new WireLabelSecretEncoder(wire.getValue1());
        BigInteger encodedFalseLabel=falseLabelEncoder.encode();
        BigInteger encodedTrueLabel=trueLabelEncoder.encode();

        PlaintextSecretPairDto secretPair=new PlaintextSecretPairDto(encodedFalseLabel, encodedTrueLabel);
        Sender sender=null;
        try{
            sender=new Sender(secretPair);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return sender;
    }
}
