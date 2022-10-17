package test.java.localhost.hashing_without_knowing_how_to_hash.model.circuit;

import mockdata.GateMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yao.gate.Gate;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GateEncodingTest {

    private GateEncoding gateEncoding;

    @BeforeEach
    public void setUp(){
        gateEncoding=new GateEncoding();
    }

    @Test
    public void testSetGateShouldIncreaseSizeOfEncodedGates(){
        //given

        //when
        gateEncoding.setGate(GateMockData.mockAndGate());

        //then
        assertEquals(1, gateEncoding.getGateToId().size());
    }

    @Test
    public void testSetGateShouldSaveInstanceDataIntoMap(){
        //given
        Gate mockedGate=GateMockData.mockAndGate();
        byte[][] expectedLut=mockedGate.getLut();

        //when
        gateEncoding.setGate(mockedGate);

        //then
        byte[][] actualLut=gateEncoding.getGateToId().keySet().iterator().next();
        assertTrue(Arrays.deepEquals(expectedLut, actualLut));
    }

    @Test
    public void testGetGateShouldInstantiateEquivalentGateForNumericalId(){
        //given
        Gate mockedGate=GateMockData.mockAndGate();
        gateEncoding.setGate(mockedGate);
        String gateId="1";

        //when
        Gate actualGate=gateEncoding.getGate(gateId);

        //then
        assertTrue(Arrays.deepEquals(mockedGate.getLut(), actualGate.getLut()));
    }

    @Test
    public void testGetGatesToShouldBeReversedOfGetIdAndGates(){
        //given
        gateEncoding.setGate(GateMockData.mockAndGate());

        //when
        Map<byte[][], String> gatesToId=gateEncoding.getGateToId();
        Map<String, byte[][]> idAndGates=gateEncoding.getGatesEncodingContainer().getIdAndGates();

        //then
        byte[][] gatesToIdLut=gatesToId.keySet().iterator().next();
        String gatesToIdId=gatesToId.get(gatesToIdLut);
        byte[][] idAndGatesLut=idAndGates.get(gatesToIdId);

        assertTrue(Arrays.deepEquals(gatesToIdLut, idAndGatesLut));

    }

}