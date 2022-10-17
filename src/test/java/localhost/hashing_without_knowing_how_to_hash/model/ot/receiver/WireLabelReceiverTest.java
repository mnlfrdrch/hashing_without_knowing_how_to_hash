package test.java.localhost.hashing_without_knowing_how_to_hash.model.ot.receiver;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.constants.ControllerPaths;
import localhost.hashing_without_knowing_how_to_hash.controller.CircuitController;
import localhost.hashing_without_knowing_how_to_hash.controller.ObliviousTransferController;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.util.HttpUtil;
import mockdata.FormulaTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WireLabelReceiverTest {

    @LocalServerPort
    private int PORT;

    @Autowired
    private CircuitController circuitController;

    @Autowired
    private ObliviousTransferController obliviousTransferController;

    private WireLabelReceiver wireLabelReceiver;

    @BeforeEach
    public void setUp(){
        FormulaContainerDto formulaContainer=new FormulaContainerDto(FormulaTestData.get12BitAAndBFunction());
        ReflectionTestUtils.setField(FormulaCache.getInstance(), "formulaContainerDto", formulaContainer);

        HostDto host=new HostDto("localhost", String.valueOf(PORT));
        WireAddressDto wireAddress=getWireAddress();
        boolean selection=false;
        wireLabelReceiver=new WireLabelReceiver(host, wireAddress, selection);
    }

    private HostDto getHost(){
        return new HostDto("localhost", String.valueOf(PORT));
    }

    private WireAddressDto getWireAddress(){
        WireAddressDto wireAddress=null;
        String url= HttpUtil.buildBasicUrl(getHost(), ControllerPaths.CIRCUIT);
        TestRestTemplate testRestTemplate=new TestRestTemplate();
        CircuitsContainerDto circuitsContainer=testRestTemplate.getForObject(url, CircuitsContainerDto.class);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        int circuitHash=circuitsContainer.getCircuits().iterator().next().getCircuitHash();
        wireAddress=new WireAddressDto(circuitsContainerHash,circuitHash, "A");
        return wireAddress;
    }

    @Test
    public void testReceiveShouldNotReturnNull(){
        //given

        //when
        byte[] receivedWireLabel=wireLabelReceiver.receive();

        //then
        assertNotNull(receivedWireLabel);
    }

    @Test
    public void testReceiveShouldNotReturnDefaultWireLabel(){
        //given
        byte[] defaultWireLabel=new byte[Wire.AES_KEYLENGTH];

        //when
        byte[] receivedWireLabel=wireLabelReceiver.receive();

        //then
        assertFalse(Arrays.equals(defaultWireLabel, receivedWireLabel));
    }

}