package test.java.localhost.hashing_without_knowing_how_to_hash.util;

import localhost.hashing_without_knowing_how_to_hash.cache.FormulaCache;
import localhost.hashing_without_knowing_how_to_hash.constants.ControllerPaths;
import localhost.hashing_without_knowing_how_to_hash.controller.CircuitController;
import localhost.hashing_without_knowing_how_to_hash.controller.ObliviousTransferController;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.circuit.CircuitsContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.formula.FormulaContainerDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.EncryptedSecretPairDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.PublicKeyRSADto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.RandomMessagePairDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.VValueDto;
import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.WireLabelSecretDecoder;
import localhost.hashing_without_knowing_how_to_hash.model.ot.receiver.Receiver;
import mockdata.FormulaTestData;
import mockdata.HostDtoMockData;
import mockdata.WireMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import java.math.BigInteger;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HttpUtilTest {

    @LocalServerPort
    int PORT;

    @Autowired
    CircuitController circuitController;

    @Autowired
    ObliviousTransferController obliviousTransferController;

    @BeforeEach
    public void setUp(){
        FormulaContainerDto formulaContainer=new FormulaContainerDto(FormulaTestData.get12BitAAndBFunction());
        ReflectionTestUtils.setField(FormulaCache.getInstance(), "formulaContainerDto", formulaContainer);
    }

    private HostDto getHost(){
        return new HostDto("localhost", String.valueOf(PORT));
    }

    @Test
    public void testBuildBasicUrl(){
        //given
        HostDto host=HostDtoMockData.mockHost();
        String path="path/1";
        String expectedUrl="http://localhost:8080/path/1";

        //when
        String actualUrl=HttpUtil.buildBasicUrl(host, path);

        //then
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testBuildWireLabelReceiverUrl(){
        //given
        HostDto host= HostDtoMockData.mockHost();
        String step= ControllerPaths.PUBLIC_KEY;
        WireAddressDto address= WireMockData.getAddressOfA();
        String expectedUrl="http://localhost:8080/ot/publicKey?circuitContainerHash=2562&circuitHash=4569&wireId=A";

        //when
        String actualUrl=HttpUtil.buildWireLabelReceiverUrl(host, step, address);

        //then
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void testHttpGetStatusShouldBeTrueBecauseFormulaIsAlreadyLoaded(){
        //given
        HostDto host=getHost();

        //when
        boolean isStatus=HttpUtil.httpGetStatus(host);

        //then
        assertTrue(isStatus);
    }

    @Test
    public void testHttpGetCircuitsContainerDtoShouldReturnRealCircuitsContainerInstance(){
        //given
        HostDto host=getHost();

        //when
        CircuitsContainerDto circuitsContainer=HttpUtil.httpGetCircuitsContainerDto(host);

        //then
        assertNotNull(circuitsContainer);
    }

    @Test
    public void testHttpGetPublicKeyRSADto(){
        //given
        HostDto host=getHost();
        CircuitsContainerDto circuitsContainer=HttpUtil.httpGetCircuitsContainerDto(host);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        int circuitHash=circuitsContainer.getCircuits().iterator().next().getCircuitHash();
        WireAddressDto wireAddressA=new WireAddressDto(circuitsContainerHash, circuitHash, "A");

        //when
        PublicKeyRSADto publicKey=HttpUtil.httpGetPublicKeyRSADto(host, wireAddressA);

        //then
        assertNotNull(publicKey);
    }

    @Test
    public void testHttpGetRandomMessagePairDto(){
        //given
        HostDto host=getHost();
        CircuitsContainerDto circuitsContainer=HttpUtil.httpGetCircuitsContainerDto(host);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        int circuitHash=circuitsContainer.getCircuits().iterator().next().getCircuitHash();
        WireAddressDto wireAddressA=new WireAddressDto(circuitsContainerHash, circuitHash, "A");

        //when
        RandomMessagePairDto randomMessages=HttpUtil.httpGetRandomMessagePairDto(host, wireAddressA);

        //then
        assertNotNull(randomMessages);
    }

    @Test
    public void testHttpPostVValueDtoShouldBeAbleToWorkInAnObliviousTransfer(){
        //given
        HostDto host=getHost();
        CircuitsContainerDto circuitsContainer=HttpUtil.httpGetCircuitsContainerDto(host);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        int circuitHash=circuitsContainer.getCircuits().iterator().next().getCircuitHash();
        WireAddressDto wireAddressA=new WireAddressDto(circuitsContainerHash, circuitHash, "A");
        Receiver receiver=new Receiver(true);
        receiver.setPublicKeyRsa(HttpUtil.httpGetPublicKeyRSADto(host, wireAddressA));
        receiver.setRandomMessagePair(HttpUtil.httpGetRandomMessagePairDto(host, wireAddressA));
        VValueDto vValue=receiver.getVValue();


        //when
        HttpUtil.httpPostVValueDto(host, vValue, wireAddressA);

        //then
        EncryptedSecretPairDto encryptedSecrets=HttpUtil.httpGetEncryptedSecretPairDto(host, wireAddressA);
        receiver.setEncryptedSecretPair(encryptedSecrets);
        WireLabelSecretDecoder decoder=new WireLabelSecretDecoder(receiver.getSelectedSecret().getM());
        assertFalse(Arrays.equals(new byte[Wire.AES_KEYLENGTH], decoder.decode()));
    }

    @Test
    public void testHttpGetEncryptedSecretPairDtoShouldCreateAnInstance(){
        //given
        HostDto host=getHost();
        CircuitsContainerDto circuitsContainer=HttpUtil.httpGetCircuitsContainerDto(host);
        int circuitsContainerHash=circuitsContainer.getCircuitsContainerHash();
        int circuitHash=circuitsContainer.getCircuits().iterator().next().getCircuitHash();
        WireAddressDto wireAddressA=new WireAddressDto(circuitsContainerHash, circuitHash, "A");
        Receiver receiver=new Receiver(true);
        receiver.setPublicKeyRsa(HttpUtil.httpGetPublicKeyRSADto(host, wireAddressA));
        receiver.setRandomMessagePair(HttpUtil.httpGetRandomMessagePairDto(host, wireAddressA));
        VValueDto vValue=receiver.getVValue();
        HttpUtil.httpPostVValueDto(host, vValue, wireAddressA);

        //when
        EncryptedSecretPairDto encryptedSecrets=HttpUtil.httpGetEncryptedSecretPairDto(host, wireAddressA);

        //then
        assertNotNull(encryptedSecrets);

    }

}