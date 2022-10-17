package localhost.hashing_without_knowing_how_to_hash.model.ot;

import mockdata.WireMockData;
import yao.gate.Wire;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class WireLabelSecretDecoderTest {


    @Test
    public void testDecoderRevertsEncodedWireLabel(){
        //given
        byte[] expectedWireLabel0= WireMockData.mockWireA().getValue0();

        WireLabelSecretEncoder encoder0=new WireLabelSecretEncoder(expectedWireLabel0);
        BigInteger encodedWireLabel0=encoder0.encode();

        //when
        WireLabelSecretDecoder decoder0=new WireLabelSecretDecoder(encodedWireLabel0);

        byte[] actualWireLabel0=decoder0.decode();

        //then
        assertArrayEquals(expectedWireLabel0, actualWireLabel0);
    }

    @Test
    public void testDecoderShouldHandleNull(){
        //given
        BigInteger notInstantialised=null;

        //when
        WireLabelSecretDecoder decoder=new WireLabelSecretDecoder(notInstantialised);
        byte[] wireLabel=decoder.decode();

        //then
        assertNotNull(wireLabel);
    }

}