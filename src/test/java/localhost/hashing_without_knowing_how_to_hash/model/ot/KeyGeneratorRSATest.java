package test.java.localhost.hashing_without_knowing_how_to_hash.model.ot;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.PrivateKeyRSADto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.PublicKeyRSADto;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorRSATest {

    @Test
    public void testInstantiationOfKeyGeneratorRsaDoesNotThrowException(){
        assertDoesNotThrow(()->new KeyGeneratorRSA());
    }

    @Test
    public void testPublicKeyAndPrivateKeyShouldMatch(){
        //given
        KeyGeneratorRSA keyGenerator=null;
        try {
            keyGenerator = new KeyGeneratorRSA();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
        PublicKeyRSADto publicKey=keyGenerator.getPublicKey();
        PrivateKeyRSADto privateKey=keyGenerator.getPrivateKey();
        BigInteger plainText=new BigInteger("12345");

        //when
        BigInteger cipherText=plainText.modPow(publicKey.getE(), publicKey.getN());
        BigInteger decryptedCipherText=cipherText.modPow(privateKey.getD(), privateKey.getN());

        //then
        assertEquals(plainText, decryptedCipherText);
    }

}