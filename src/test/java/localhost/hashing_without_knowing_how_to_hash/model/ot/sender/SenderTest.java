package test.java.localhost.hashing_without_knowing_how_to_hash.model.ot.sender;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class SenderTest {

    Sender sender;

    @BeforeEach
    public void setUp(){
        PlaintextSecretPairDto plaintextSecretPairDto=new PlaintextSecretPairDto(new BigInteger("123"), new BigInteger("456"));
        try {
            sender = new Sender(plaintextSecretPairDto);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetRandomMessagePair(){
        //when
        RandomMessagePairDto randomMessages=sender.getRandomMessagePair();

        //then
        assertNotNull(randomMessages);
    }

    @Test
    public void testGetPublicKeyRsa(){
        //when
        PublicKeyRSADto publicKey=sender.getPublicKeyRsa();

        //then
        assertNotNull(publicKey);
    }

    @Test
    public void testGetEncryptedSecretPairShouldProduceOutput(){
        //given
        VValueDto mockedVValue= Mockito.mock(VValueDto.class);
        Mockito.when(mockedVValue.getV()).thenReturn(new BigInteger("12345"));
        sender.setVValue(mockedVValue);

        //when
        EncryptedSecretPairDto encryptedSecretPair=sender.getEncryptedSecretPair();

        //then
        assertNotNull(encryptedSecretPair);
    }

}