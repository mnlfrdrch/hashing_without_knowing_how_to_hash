package main.java.hashing_without_knowing_how_to_hash.model.ot.sender;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import localhost.hashing_without_knowing_how_to_hash.model.ot.KeyGeneratorRSA;

/**
 * Implements math for a 1-out-of-2 RSA oblivious transfer for the sender role
 */
public class Sender {

    private PlaintextSecretPairDto plaintextSecretPairDto;
    private KeyGeneratorRSA keyGeneratorRSA;
    private RandomMessagePairDto randomMessagePairDto;
    private VValueDto vValueDto;

    private boolean isGetPublicKeyRSAValidate;
    private boolean isGetRandomMessagePairValidate;
    private boolean isGetEncryptedSecretPairValidate;


    public Sender(PlaintextSecretPairDto plaintextSecretPairDto) throws Exception{
        this.plaintextSecretPairDto = plaintextSecretPairDto;
        keyGeneratorRSA=new KeyGeneratorRSA();
        randomMessagePairDto =new RandomMessagePairDto(1024);
        vValueDto =null;

        isGetPublicKeyRSAValidate=true;
        isGetRandomMessagePairValidate=true;
        isGetEncryptedSecretPairValidate=true;
    }

    /**
     * Transfers the RSA public key to the receiver
     * @return wrapper of a RSA public key or *null*
     */
    public PublicKeyRSADto getPublicKeyRsa(){
        if(isGetPublicKeyRSAValidate) {
            isGetPublicKeyRSAValidate=false;
            return keyGeneratorRSA.getPublicKey();
        }else {
            return null;
        }
    }

    /**
     * Transfers the two random messages to the sender
     * @return wrapper of the two random messages or *null*
     */
    public RandomMessagePairDto getRandomMessagePair(){
        if(isGetRandomMessagePairValidate){
            isGetRandomMessagePairValidate=false;
            return randomMessagePairDto;
        }
        else {
            return null;
        }
    }

    /**
     * Sets the vValue computed and sent by the receiver
     * @param vValueDto the transferred vValue wrapper
     */
    public void setVValue(VValueDto vValueDto){
        this.vValueDto = vValueDto;
    }

    /**
     * Transfers both the encrypted secrets to the receiver
     * Secrets are encrypted using vValue
     * @return wrapper of the two encrypted secrets
     */
    public EncryptedSecretPairDto getEncryptedSecretPair(){
        if(isGetEncryptedSecretPairValidate) {
            isGetEncryptedSecretPairValidate=false;
            KValuePairDto kValuePairDto = new KValuePairDto(keyGeneratorRSA.getPrivateKey(), randomMessagePairDto, vValueDto);
            EncryptedSecretPairDto encryptedSecretPairDto = new EncryptedSecretPairDto(plaintextSecretPairDto, kValuePairDto);
            return encryptedSecretPairDto;
        }else {
            return null;
        }
    }
}
