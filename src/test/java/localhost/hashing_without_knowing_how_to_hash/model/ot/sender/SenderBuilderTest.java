package test.java.localhost.hashing_without_knowing_how_to_hash.model.ot.sender;

import mockdata.WireMockData;
import org.junit.jupiter.api.Test;
import yao.gate.Wire;

import static org.junit.jupiter.api.Assertions.*;

class SenderBuilderTest {

    @Test
    public void testInstantiateSenderShouldCreateSenderInstance(){
        //given
        Wire mockedWire= WireMockData.mockWireA();

        //when
        Sender sender=SenderBuilder.instantiateSender(mockedWire);

        //then
        assertNotNull(sender);
    }


}