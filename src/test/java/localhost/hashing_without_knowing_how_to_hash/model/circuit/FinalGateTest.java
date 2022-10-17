package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import mockdata.GateMockData;
import mockdata.WireMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class FinalGateTest {

    private FinalGate finalGate;

    @BeforeEach
    public void setUp(){
        Gate mockedGate=GateMockData.mockAndGate();
        Wire mockedOutWire=WireMockData.mockWireC();
        finalGate=new FinalGate(mockedGate, mockedOutWire);
    }


    @Test
    public void testGetEncodedGateShouldReturnLut(){
        //given
        byte[][] expectedLut=GateMockData.mockAndGate().getLut();

        //when
        byte[][] actualLut=finalGate.getEncodedGate();

        //then
        assertTrue(Arrays.deepEquals(expectedLut, actualLut));

    }

    @Test
    public void testIsLegitimateShouldBeTrueForMockedWireCLabel0(){
        //given
        byte[] wireCLabel0=WireMockData.mockWireC().getValue0();

        //when
        boolean isLegitimate=finalGate.isLegitimate(wireCLabel0);

        //then
        assertTrue(isLegitimate);
    }

    @Test
    public void testIsLegitimateShouldBeTrueForMockedWireCLabel1(){
        //given
        byte[] wireCLabel1=WireMockData.mockWireC().getValue1();

        //when
        boolean isLegitimate=finalGate.isLegitimate(wireCLabel1);

        //then
        assertTrue(isLegitimate);
    }

    @Test
    public void testIsLegitimateShouldBeFalseForWireLabelWhichIsNotOfOutWire(){
        //given
        byte[] wireBLabel0=WireMockData.mockWireB().getValue0();

        //when
        boolean isLegitimate=finalGate.isLegitimate(wireBLabel0);

        //then
        assertFalse(isLegitimate);
    }

    @Test
    public void testIsTrueShouldBeTrueForOutWireLabelTrue(){
        //given
        byte[] wireLabelTrue=WireMockData.mockWireC().getValue1();

        //when
        boolean shouldBeTrue=finalGate.isTrue(wireLabelTrue);

        //then
        assertTrue(shouldBeTrue);
    }

    @Test
    public void testIsTrueShouldBeFalseForOutWireLabelFalse(){
        //given
        byte[] wireLabelFalse=WireMockData.mockWireC().getValue0();

        //when
        boolean shouldBeTrue=finalGate.isTrue(wireLabelFalse);

        //then
        assertFalse(shouldBeTrue);
    }
}