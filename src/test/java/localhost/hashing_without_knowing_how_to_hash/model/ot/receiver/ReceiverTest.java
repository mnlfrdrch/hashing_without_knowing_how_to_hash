package localhost.hashing_without_knowing_how_to_hash.model.ot.receiver;

import localhost.hashing_without_knowing_how_to_hash.dto.ot.*;
import localhost.hashing_without_knowing_how_to_hash.model.ot.WireLabelSecretDecoder;
import localhost.hashing_without_knowing_how_to_hash.model.ot.WireLabelSecretEncoder;
import localhost.hashing_without_knowing_how_to_hash.model.ot.sender.Sender;
import mockdata.WireMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.Wire;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReceiverTest {

    private Receiver receiver;

    @BeforeEach
    public void setUp(){
        receiver=new Receiver(false);
    }

    @AfterEach
    public void tearDown(){
        receiver=null;
    }

    private PublicKeyRSADto mockPublicKey(){
        PublicKeyRSADto mockedPublicKey=mock(PublicKeyRSADto.class);
        BigInteger n=new BigInteger("10");
        BigInteger e=new BigInteger("2");
        when(mockedPublicKey.getN()).thenReturn(n);
        when(mockedPublicKey.getE()).thenReturn(e);
        return mockedPublicKey;
    }

    private RandomMessagePairDto mockRandomMessagePair(){
        RandomMessagePairDto mockedRandomMessagePair=mock(RandomMessagePairDto.class);
        BigInteger message0=new BigInteger("3");
        BigInteger message1=new BigInteger("5");
        when(mockedRandomMessagePair.getX0()).thenReturn(message0);
        when(mockedRandomMessagePair.getX1()).thenReturn(message1);
        return mockedRandomMessagePair;
    }

    private void injectReceiverWithMockedSelectionDto(){
        SelectionDto selection=mock(SelectionDto.class);
        when(selection.getK()).thenReturn(new BigInteger("0"));
        ReflectionTestUtils.setField(receiver, "selectionDto", selection);
    }

    @Test
    public void testReceiverShouldBeCreated(){
        //given
        PublicKeyRSADto mockedPublicKey=mockPublicKey();
        RandomMessagePairDto mockedRandomMessagePair=mockRandomMessagePair();
        injectReceiverWithMockedSelectionDto();
        receiver.setPublicKeyRsa(mockedPublicKey);
        receiver.setRandomMessagePair(mockedRandomMessagePair);
        BigInteger expectedVValue=new BigInteger("3"); // v = (x+(k^e)) mod n   =   (3+(0^2) mod 10   =   3+0 mod 10   =   3

        //when
        VValueDto vValueDto=receiver.getVValue();
        BigInteger actualVValue=vValueDto.getV();

        //then
        assertEquals(expectedVValue, actualVValue);
    }

    private Sender getSenderForWire(Wire wire){
        Sender sender=null;
        WireLabelSecretEncoder wireLabelEncoder0=new WireLabelSecretEncoder(wire.getValue0());
        WireLabelSecretEncoder wireLabelEncoder1=new WireLabelSecretEncoder(wire.getValue1());
        PlaintextSecretPairDto secrets=new PlaintextSecretPairDto(wireLabelEncoder0.encode(), wireLabelEncoder1.encode());

        try{
            sender=new Sender(secrets);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
        return sender;
    }

    @Test
    public void testWholeObliviousTransferProcedure(){
        //given
        Wire wireA=WireMockData.mockWireA();
        byte[] expectedWireLabel=wireA.getValue0();
        Sender sender=getSenderForWire(wireA);

        //when
        receiver.setPublicKeyRsa(sender.getPublicKeyRsa());
        receiver.setRandomMessagePair(sender.getRandomMessagePair());
        sender.setVValue(receiver.getVValue());
        receiver.setEncryptedSecretPair(sender.getEncryptedSecretPair());

        SelectedSecretDto receivedSecret=receiver.getSelectedSecret();

        WireLabelSecretDecoder wireLabelSecretDecoder=new WireLabelSecretDecoder(receivedSecret.getM());
        byte[] receivedWireLabel=wireLabelSecretDecoder.decode();

        //then
        assertArrayEquals(expectedWireLabel, receivedWireLabel);
    }

}