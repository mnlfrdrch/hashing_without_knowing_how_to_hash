package localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTree;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTreeFinalLayer;
import mockdata.WireMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import static org.junit.jupiter.api.Assertions.*;

class WireCacheTest {

    private WireCache wireCache;

    @BeforeEach
    public void setUp(){
        wireCache=WireCache.getInstance();
    }

    @AfterEach
    public void tearDown(){
        ReflectionTestUtils.setField(wireCache, "wireCacheMap", new AccessControlTree<Wire>());
        wireCache=null;
    }

    @Test
    public void testCallingGetInstanceTwiceShouldReturnSameInstance(){
        //given

        //when
        WireCache firstInstance=WireCache.getInstance();
        WireCache secondInstance=WireCache.getInstance();

        //then
        assertEquals(firstInstance, secondInstance);
    }

    @Test
    public void testRequestWiresWithoutWiresCachedShouldReturnNull(){
        //given
        WireAddressDto wireAddressToNonExistingWire= WireMockData.getAddressOfA();

        //when
        Wire wireShouldBeNull=wireCache.requestWire(wireAddressToNonExistingWire);

        //then
        assertNull(wireShouldBeNull);
    }

    @Test
    public void testCacheWiresShouldMakeWiresRequestable(){
        //given
        AccessControlTreeFinalLayer<Wire> finalLayerOfMockedData=new AccessControlTreeFinalLayer<>(WireMockData.getWireEncodingOfMockedWires());

        //when
        wireCache.cacheWires(WireMockData.CIRCUITS_CONTAINER_HASH, WireMockData.CIRCUIT_HASH, finalLayerOfMockedData);
        Wire shouldNotBeNull=wireCache.requestWire(WireMockData.getAddressOfA());

        //then
        assertNotNull(shouldNotBeNull);
    }

    @Test
    public void testRequestWiresTwiceShouldReturnNullTheSecondTime(){
        //given
        WireMockData.putMockWiresInWireCache();
        WireAddressDto wireAddress=WireMockData.getAddressOfA();

        //when
        Wire firstTimeShouldBeInstance=wireCache.requestWire(wireAddress);
        Wire secondTimeShouldBeNull=wireCache.requestWire(wireAddress);

        //then
        assertNotNull(firstTimeShouldBeInstance);
        assertNull(secondTimeShouldBeNull);

    }

    @Test
    public void testInvalidCircuitsContainerHashOfWireAddressShouldReturnNull(){
        //given
        WireAddressDto invalidWireAddress=new WireAddressDto(WireMockData.CIRCUITS_CONTAINER_HASH-1,WireMockData.CIRCUIT_HASH,WireMockData.WIRE_ID_A);

        //when
        Wire wireExpectedNull=wireCache.requestWire(invalidWireAddress);

        //then
        assertNull(wireExpectedNull);
    }

    @Test
    public void testInvalidCircuitHashOfWireAddressShouldReturnNull(){
        //given
        WireAddressDto invalidWireAddress=new WireAddressDto(WireMockData.CIRCUITS_CONTAINER_HASH,WireMockData.CIRCUIT_HASH-1,WireMockData.WIRE_ID_A);

        //when
        Wire wireExpectedNull=wireCache.requestWire(invalidWireAddress);

        //then
        assertNull(wireExpectedNull);
    }

    @Test
    public void testInvalidWireIdOfWireAddressShouldReturnNull(){
        //given
        WireAddressDto invalidWireAddress=new WireAddressDto(WireMockData.CIRCUITS_CONTAINER_HASH,WireMockData.CIRCUIT_HASH,WireMockData.WIRE_ID_NON_EXISTING);

        //when
        Wire wireExpectedNull=wireCache.requestWire(invalidWireAddress);

        //then
        assertNull(wireExpectedNull);
    }

}