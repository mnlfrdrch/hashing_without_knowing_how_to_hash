package test.java.localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.Sender;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTree;
import mockdata.WireMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SenderCacheTest {

    private SenderCache senderCache;
    private WireCache mockedWireCache;


    @BeforeEach
    public void setUp(){
        senderCache=SenderCache.getInstance();
        WireMockData.putMockWiresInWireCache();
        mockedWireCache=WireCache.getInstance();
    }

    @AfterEach
    public void tearDown(){
        ReflectionTestUtils.setField(senderCache, "wireAddressToSender", new HashMap());
        ReflectionTestUtils.setField(mockedWireCache, "wireCacheMap", new AccessControlTree<Wire>());
        senderCache=null;
        mockedWireCache=null;
    }

    @Test
    public void testCallingGetInstanceTwiceShouldReturnSameInstance(){
        //given

        //when
        SenderCache firstInstance=SenderCache.getInstance();
        SenderCache secondInstance=SenderCache.getInstance();

        //then
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testValidWireAddressShouldReturnWireInstance(){
        //given
        WireAddressDto wireAddress=WireMockData.getAddressOfA();

        //when
        Sender sender=senderCache.requestSender(wireAddress);

        //then
        assertNotNull(sender);
    }

    @Test
    public void testInvalidCircuitsContainerHashOfWireAddressShouldReturnNull(){
        //given
        WireAddressDto invalidWireAddress=new WireAddressDto(WireMockData.CIRCUITS_CONTAINER_HASH-1,WireMockData.CIRCUIT_HASH,WireMockData.WIRE_ID_A);

        //when
        Sender senderExpectedNull=senderCache.requestSender(invalidWireAddress);

        //then
        assertNull(senderExpectedNull);
    }

    @Test
    public void testInvalidCircuitHashOfWireAddressShouldReturnNull(){
        //given
        WireAddressDto invalidWireAddress=new WireAddressDto(WireMockData.CIRCUITS_CONTAINER_HASH,WireMockData.CIRCUIT_HASH-1,WireMockData.WIRE_ID_A);

        //when
        Sender senderExpectedNull=senderCache.requestSender(invalidWireAddress);

        //then
        assertNull(senderExpectedNull);
    }

    @Test
    public void testInvalidWireIdOfWireAddressShouldReturnNull(){
        //given
        WireAddressDto invalidWireAddress=new WireAddressDto(WireMockData.CIRCUITS_CONTAINER_HASH,WireMockData.CIRCUIT_HASH,WireMockData.WIRE_ID_NON_EXISTING);

        //when
        Sender senderExpectedNull=senderCache.requestSender(invalidWireAddress);

        //then
        assertNull(senderExpectedNull);
    }

    @Test
    public void testMultipleCallsOfSameWireAddressShouldReturnSameInstance(){
        //given
        WireAddressDto wireAddressA=WireMockData.getAddressOfA();

        //when
        Sender senderFirstCall=senderCache.requestSender(wireAddressA);
        Sender senderSecondCall=senderCache.requestSender(wireAddressA);

        //then
        assertSame(senderFirstCall,senderSecondCall);
    }

    @Test
    public void testCallsOfDifferentWireAddressesShouldReturnDifferentInstances(){
        //given
        WireAddressDto wireAddressA=WireMockData.getAddressOfA();
        WireAddressDto wireAddressB=WireMockData.getAddressOfB();

        //when
        Sender senderA=senderCache.requestSender(wireAddressA);
        Sender senderB=senderCache.requestSender(wireAddressB);

        //then
        assertNotSame(senderA,senderB);
    }

    @Test
    public void testRemoveSenderShouldDeleteSenderFromCache(){
        //given
        WireAddressDto wireAddress=WireMockData.getAddressOfA();
        Sender senderShouldExistFirst=senderCache.requestSender(wireAddress);

        //when
        senderCache.removeSender(wireAddress);
        Sender senderShouldNotExistAnymore=senderCache.requestSender(wireAddress);

        //then
        assertNotNull(senderShouldExistFirst);
        assertNull(senderShouldNotExistAnymore);
    }
}