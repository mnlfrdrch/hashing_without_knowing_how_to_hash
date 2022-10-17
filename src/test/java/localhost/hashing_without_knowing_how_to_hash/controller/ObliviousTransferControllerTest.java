package test.java.localhost.hashing_without_knowing_how_to_hash.controller;

import localhost.hashing_without_knowing_how_to_hash.cache.SenderCache;
import localhost.hashing_without_knowing_how_to_hash.cache.WireCache;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.EncryptedSecretPairDto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.PublicKeyRSADto;
import localhost.hashing_without_knowing_how_to_hash.dto.ot.RandomMessagePairDto;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.Sender;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTree;
import mockdata.ObliviousTransferMockData;
import mockdata.WireMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
class ObliviousTransferControllerTest {

    @LocalServerPort
    private int PORT;

    @Autowired
    private ObliviousTransferController obliviousTransferController;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void setUp(){
        Map<String, Sender> addressToMockedSender=new HashMap<>();
        addressToMockedSender.put(WireMockData.getAddressOfA().toString(), ObliviousTransferMockData.mockSender());
        ReflectionTestUtils.setField(SenderCache.getInstance(), "wireAddressToSender", addressToMockedSender);
    }

    @AfterEach
    public void tearDown(){
        ReflectionTestUtils.setField(SenderCache.getInstance(), "wireAddressToSender", new HashMap());
        ReflectionTestUtils.setField(WireCache.getInstance(), "wireCacheMap", new AccessControlTree<Wire>());
    }

    @Test
    public void testGetPublicKeyShouldReturnMockedPublicKey(){
        //given
        WireAddressDto wireAddress=WireMockData.getAddressOfA();

        //when
        PublicKeyRSADto publicKey=obliviousTransferController.getPublicKey(wireAddress.getCircuitsContainerHash(), wireAddress.getCircuitHash(), wireAddress.getWireId());

        //then
        assertEquals(ObliviousTransferMockData.getN(), publicKey.getN());
        assertEquals(ObliviousTransferMockData.getE(), publicKey.getE());
    }

    @Test
    public void testGetRandomMessagesShouldReturnMockedRandomMessages(){
        //given
        WireAddressDto wireAddress=WireMockData.getAddressOfA();

        //when
        RandomMessagePairDto randomMessages=obliviousTransferController.getRandomMessages(wireAddress.getCircuitsContainerHash(), wireAddress.getCircuitHash(), wireAddress.getWireId());

        //then
        assertEquals(ObliviousTransferMockData.getFirstRandomMessage(), randomMessages.getX0());
        assertEquals(ObliviousTransferMockData.getSecondRandomMessage(), randomMessages.getX1());
    }

    @Test
    public void testGetEncryptedSecretsShouldReturnMockedEncryptedSecrets(){
        //given
        WireAddressDto wireAddress=WireMockData.getAddressOfA();

        //when
        EncryptedSecretPairDto encryptedSecrets=obliviousTransferController.getEncryptedSecrets(wireAddress.getCircuitsContainerHash(), wireAddress.getCircuitHash(), wireAddress.getWireId());

        //then
        assertEquals(ObliviousTransferMockData.getFirstEncryptedSecret(), encryptedSecrets.getEncM0());
        assertEquals(ObliviousTransferMockData.getSecondEncryptedSecret(), encryptedSecrets.getEncM1());
    }

}