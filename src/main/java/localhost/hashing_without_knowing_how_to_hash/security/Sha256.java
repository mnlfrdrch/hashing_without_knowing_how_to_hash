package localhost.hashing_without_knowing_how_to_hash.security;

import localhost.hashing_without_knowing_how_to_hash.config.ModelMetaData;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HashFunctionSecretKeyDto;
import localhost.hashing_without_knowing_how_to_hash.model.hash_function_share.HashingAlgorithm;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Implementation of a keyed SHA256 hashing algorithm (HMAC)
 */
public class Sha256 implements HashingAlgorithm {

    public static final int KEYSIZE=256;
    private HmacUtils hmacUtils;
    private byte[] key;

    public Sha256() {
        KeyGenerator keyGenerator;
        try{
            keyGenerator=KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(KEYSIZE);
            SecretKey secretKeyInterface=keyGenerator.generateKey();
            key=secretKeyInterface.getEncoded();
            hmacUtils=new HmacUtils(HmacAlgorithms.HMAC_SHA_256,key);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            key=new byte[Sha256.KEYSIZE];
        }
    }

    public Sha256(HashFunctionSecretKeyDto secretKey){
        byte[] secretKeyHex=null;
        try{
            secretKeyHex=Hex.decodeHex(secretKey.getKey());
        }
        catch (Exception e){
            e.printStackTrace();
            secretKeyHex=new byte[Sha256.KEYSIZE];
        }
        key=secretKeyHex;
        hmacUtils=new HmacUtils(HmacAlgorithms.HMAC_SHA_256,secretKeyHex);
    }

    @Override
    public String h(String x){
        String y;
        if(hmacUtils!=null)
        {
            y=hmacUtils.hmacHex(x);
        }
        else {
            y="";
        }
        return y;
    }

    //@Override
    private String convertKeyToHexSequence(){
        String hexKey=null;
        try {
            hexKey = Hex.encodeHexString(key);
        }
        catch (Exception e) {
            e.printStackTrace();
            hexKey="";
        }
        return hexKey;
    }

    @Override
    public HashFunctionSecretKeyDto getSecretKey(){
        HashFunctionSecretKeyDto key=null;
        try{
            key=new HashFunctionSecretKeyDto(convertKeyToHexSequence(), ModelMetaData.ALGORITHM_TYPE);
        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        return key;
    }

    @Override
    public int getHashValueLength(){
        return 64; // 256/4
    }

    @Override
    public int getSecretKeyLength(){
        return KEYSIZE/4; // 256Bit key length and 1 hex char for 4 bits
    }

    @Override
    public String toString(){
        return convertKeyToHexSequence();
    }

}
