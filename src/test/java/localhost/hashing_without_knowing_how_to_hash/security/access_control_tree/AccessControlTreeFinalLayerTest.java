package localhost.hashing_without_knowing_how_to_hash.security.access_control_tree;

import mockdata.WireMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yao.gate.Wire;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AccessControlTreeFinalLayerTest {

    AccessControlTreeFinalLayer<Wire> finalLayer;

    @BeforeEach
    public void setUp(){
        Map<String, Wire> idAndWire= WireMockData.getWireEncodingOfMockedWires();
        finalLayer=new AccessControlTreeFinalLayer<>(idAndWire);
    }

    @Test
    public void testIsInvalidShouldBeFalseIfFinalLayerIsStillValid(){
        //given

        //when
        boolean shouldBeFalse=finalLayer.isInvalid();

        //then
        assertFalse(shouldBeFalse);
    }

    @Test
    public void testIsInvalidShouldBeTrueIfAllWiresHaveBeenAccessed(){
        //given
        finalLayer.accessElement("A");
        finalLayer.accessElement("B");

        //when
        boolean shouldBeTrue=finalLayer.isInvalid();

        //then
        assertTrue(shouldBeTrue);
    }

    @Test
    public void testAccessElementsShouldReturnReferencedElement(){
        //given
        Wire expectedWireA=WireMockData.mockWireA();
        String wireIdA=WireMockData.WIRE_ID_A;

        //when
        Wire actualWire=finalLayer.accessElement(wireIdA);

        //then
        assertArrayEquals(expectedWireA.getValue0(), actualWire.getValue0());
        assertArrayEquals(expectedWireA.getValue1(), actualWire.getValue1());
    }

    @Test
    public void testGetNumElementsAlreadyAccessedShouldBe1AfterAnAccess(){
        //given
        String wireIdA=WireMockData.WIRE_ID_A;
        finalLayer.accessElement(wireIdA);

        //when
        int shouldBeSingleAccess=finalLayer.getNumElementsAlreadyAccessed();

        //then
        assertEquals(1, shouldBeSingleAccess);
    }
}