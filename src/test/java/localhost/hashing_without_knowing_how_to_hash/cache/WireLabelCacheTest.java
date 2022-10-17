package test.java.localhost.hashing_without_knowing_how_to_hash.cache;

import localhost.hashing_without_knowing_how_to_hash.controller.CircuitController;
import localhost.hashing_without_knowing_how_to_hash.controller.ObliviousTransferController;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.receiver.WireLabelReceiver;
import localhost.hashing_without_knowing_how_to_hash.util.HttpUtil;
import mockdata.FormulaTestData;
import mockdata.HostDtoMockData;
import mockdata.WireMockData;
import org.junit.jupiter.api.*;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WireLabelCacheTest {

    @LocalServerPort
    private int PORT;

    @Autowired
    private CircuitController circuitController;

    @Autowired
    private ObliviousTransferController obliviousTransferController;

    private WireLabelCache wireLabelCache;

    private HostDto getHost(){
        return new HostDto("localhost", String.valueOf(PORT));
    }

    @BeforeEach
    public void setUp(){
        wireLabelCache=WireLabelCache.getInstance();
        FormulaContainerDto formulaContainer=new FormulaContainerDto(FormulaTestData.get12BitAAndBFunction());
        ReflectionTestUtils.setField(FormulaCache.getInstance(), "formulaContainerDto", formulaContainer);
    }

    @AfterEach
    public void tearDown(){
        ReflectionTestUtils.setField(wireLabelCache, "wireAddressToWireLabel", new HashMap());
        wireLabelCache=null;
    }

    @Test
    public void testInstantiationTwiceShouldReturnTheSameInstanceOfWireLabelCache(){
        //given

        //when
        WireLabelCache firstInstance=WireLabelCache.getInstance();
        WireLabelCache secondInstance=WireLabelCache.getInstance();

        //then
        assertSame(firstInstance,secondInstance);
    }

    private WireAddressDto getWireAddressA(){
        TestRestTemplate testRestTemplate=new TestRestTemplate();
        String circuitUrl= HttpUtil.buildBasicUrl(getHost(), "circuit");
        CircuitsContainerDto circuitsContainer=testRestTemplate.getForObject(circuitUrl,CircuitsContainerDto.class);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        int circuitHash=circuitsContainer.getCircuits().iterator().next().getCircuitHash();
        return new WireAddressDto(circuitsContainerHash, circuitHash, "A");
    }

    @Test
    public void testCacheNewWireLabelShouldReceiveNewWireLabel(){
        //given
        HostDto host=getHost();
        WireAddressDto wireAddress= getWireAddressA();
        boolean selection=false;

        //when
        byte[] receivedWireLabel=wireLabelCache.cacheNewWireLabel(host, wireAddress, selection);

        //then
        assertNotNull(receivedWireLabel);
    }

    private WireLabelCache mockWireLabelCachePartially(HostDto mockedHost, WireAddressDto mockedAddress, Wire mockedWire){
        WireLabelCache partiallyMockedCache=mock(WireLabelCache.class);
        byte[] val0=mockedWire.getValue0();
        byte[] val1=mockedWire.getValue1();

        when(partiallyMockedCache.cacheNewWireLabel(mockedHost, mockedAddress, false))
                .thenReturn(val0);
        when(partiallyMockedCache.cacheNewWireLabel(mockedHost, mockedAddress, true))
                .thenReturn(val1);

        ReflectionTestUtils.setField(partiallyMockedCache, "wireAddressToWireLabel", new HashMap());

        when(partiallyMockedCache.existsWireLabel(mockedAddress)).thenCallRealMethod();
        when(partiallyMockedCache.requestWireLabel(mockedHost,mockedAddress)).thenCallRealMethod();
        return partiallyMockedCache;
    }

    @Test
    public void testRequestWireLabelForNonExistingWireLabelShouldRunCacheNewWireLabel(){
        //given
        HostDto mockedHost=HostDtoMockData.mockHost();
        WireAddressDto mockedAddress=WireMockData.getAddressOfA();
        Wire mockedWire=WireMockData.mockWireA();
        WireLabelCache partiallyMockedCache=mockWireLabelCachePartially(mockedHost, mockedAddress, mockedWire);

        byte[] mockedWireLabel=mockedWire.getValue0();

        //when
        byte[] actualWireLabel=partiallyMockedCache.requestWireLabel(mockedHost, mockedAddress);

        //then
        assertArrayEquals(mockedWireLabel, actualWireLabel);
    }

    private void putMockedWireLabelInCache(WireAddressDto mockedAddress, byte[] wireLabel){
        Map<String, byte[]> inCache=Map.of(mockedAddress.toString(), wireLabel);
        ReflectionTestUtils.setField(wireLabelCache, "wireAddressToWireLabel", inCache);
    }

    @Test
    public void testRequestWireLabelForAlreadyExistingWireLabelShouldReadWireLabelFromCache(){
        //given
        HostDto mockedHost=HostDtoMockData.mockHost();
        WireAddressDto mockedAddress=WireMockData.getAddressOfA();
        byte[] expectedWireLabel=WireMockData.mockWireA().getValue1();
        putMockedWireLabelInCache(mockedAddress, expectedWireLabel);

        //when
        byte[] actualWireLabel=wireLabelCache.requestWireLabel(mockedHost, mockedAddress);

        //then
        assertArrayEquals(expectedWireLabel, actualWireLabel);
    }

    @Test
    public void testExistsWireLabelShouldReturnTrueIfRequestedWireLabelIsInCache(){
        //given
        WireAddressDto address=WireMockData.getAddressOfA();
        byte[] expectedWireLabel=WireMockData.mockWireA().getValue1();
        putMockedWireLabelInCache(address, expectedWireLabel);

        //when
        boolean actualValue=wireLabelCache.existsWireLabel(address);

        //then
        assertTrue(actualValue);
    }

    @Test
    public void testExistsWireLabelShouldReturnFalseIfRequestedWireLabelIsNotInCache(){
        //given
        WireAddressDto address=WireMockData.getAddressOfA();

        //when
        boolean actualValue=wireLabelCache.existsWireLabel(address);

        //then
        assertFalse(actualValue);
    }

}