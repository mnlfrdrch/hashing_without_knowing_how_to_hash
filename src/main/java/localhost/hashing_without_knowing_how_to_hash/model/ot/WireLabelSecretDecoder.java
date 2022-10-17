package localhost.hashing_without_knowing_how_to_hash.model.ot;

import yao.gate.Wire;

import java.math.BigInteger;

/**
 * Actual SecretDecoder for oblivious transfer, which decodes an integer secret back into a wire label (byte[])
 */
public class WireLabelSecretDecoder extends AbstractObliviousTransferSecretDecoder<byte[]>
{

    public WireLabelSecretDecoder(BigInteger wireLabelSecretToDecode){
        super(wireLabelSecretToDecode);
    }

    @Override
    public byte[] decode(){
        BigInteger secret=getSecretToDecode();
        if(secret==null){
            return new byte[Wire.AES_KEYLENGTH];
        }
        return getSecretToDecode().toByteArray();
    }
}
