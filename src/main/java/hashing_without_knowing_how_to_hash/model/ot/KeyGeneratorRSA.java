package main.java.hashing_without_knowing_how_to_hash.model.ot;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.PrivateKeyRSADto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.PublicKeyRSADto;
import yao.Utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * For the RSA variant of oblivious transfer there is a RSA key pair required
 * This class generates public key and private key for oblivious transfer comfortably
 */
public class KeyGeneratorRSA {

    private KeyFactory factory;
    private KeyPair keyPair;
    private final String ALGORITHM="RSA";

    public KeyGeneratorRSA() throws Exception{
        init();
    }
    private void init() throws Exception{
        keyPair=Utils.genRSAkeypair();
        factory = KeyFactory.getInstance(ALGORITHM);
    }

    public PublicKeyRSADto getPublicKey(){
        java.security.PublicKey publicKey=keyPair.getPublic();
        PublicKeyRSADto publicKeyRSADtoInstance =null;
        try {
            RSAPublicKeySpec publicKeyParams = factory.getKeySpec(publicKey, RSAPublicKeySpec.class);
            publicKeyRSADtoInstance =new PublicKeyRSADto(publicKeyParams.getModulus(),publicKeyParams.getPublicExponent());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return publicKeyRSADtoInstance;
    }

    public PrivateKeyRSADto getPrivateKey(){
        java.security.PrivateKey privateKey=keyPair.getPrivate();
        PrivateKeyRSADto privateKeyRSADtoInstance =null;
        try {
            RSAPrivateKeySpec publicKeyParams = factory.getKeySpec(privateKey, RSAPrivateKeySpec.class);
            privateKeyRSADtoInstance =new PrivateKeyRSADto(publicKeyParams.getModulus(), publicKeyParams.getPrivateExponent());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return privateKeyRSADtoInstance;
    }
}
