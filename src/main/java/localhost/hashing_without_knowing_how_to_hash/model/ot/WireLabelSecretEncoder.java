package localhost.hashing_without_knowing_how_to_hash.model.ot;

import java.math.BigInteger;

/**
 * Concrete SecretEncoder for oblivious transfer, which encodes wire labels of type byte[] into their integer representation
 */
public class WireLabelSecretEncoder extends AbstractObliviousTransferSecretEncoder<byte[]>{

    public WireLabelSecretEncoder(byte[] wireLabelSecretToEncode){
        super(wireLabelSecretToEncode);
    }

    @Override
    public BigInteger encode(){
        if(getSecretToEncode()==null){
            return new BigInteger("-1");
        }else {
            return new BigInteger(getSecretToEncode());
        }
    }
}
