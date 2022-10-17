package localhost.hashing_without_knowing_how_to_hash.model.ot;

import org.junit.jupiter.api.Test;
import yao.gate.Wire;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class WireLabelSecretEncoderTest {

    @Test
    public void testEncoderShouldHandleNull(){
        //given
        byte[] notInstantialisedWireLabel=null;
        WireLabelSecretEncoder encoder=new WireLabelSecretEncoder(notInstantialisedWireLabel);

        //when
        BigInteger encodedNotInstantialisedWireLabel=encoder.encode();

        //then
        assertNotNull(encodedNotInstantialisedWireLabel);

    }

    @Test
    public void testEncodeOfEmptyByteArrayShouldReturnZero(){
        //given
        byte[] emptyWireLabel=new byte[Wire.AES_KEYLENGTH];
        WireLabelSecretEncoder encoder=new WireLabelSecretEncoder(emptyWireLabel);
        BigInteger expectedEncodedValue=new BigInteger("0");

        //when
        BigInteger actualEncodedValue=encoder.encode();

        //then
        assertEquals(expectedEncodedValue, actualEncodedValue);
    }

}