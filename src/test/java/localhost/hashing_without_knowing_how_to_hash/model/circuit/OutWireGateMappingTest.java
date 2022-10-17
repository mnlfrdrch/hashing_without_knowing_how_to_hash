package test.java.localhost.hashing_without_knowing_how_to_hash.model.circuit;

import mockdata.GateMockData;
import mockdata.WireMockData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import yao.gate.Gate;
import yao.gate.Wire;

import static org.junit.jupiter.api.Assertions.*;

class OutWireGateMappingTest {

    private OutWireGateMapping outWireGateMapping;

    @BeforeEach
    public void setUp(){
        outWireGateMapping=new OutWireGateMapping();
    }

    @Test
    public void testSetShouldSaveGateAndMakeItRequestableByWire(){
        //given
        Gate gate= GateMockData.mockAndGate();
        Wire out= WireMockData.mockWireC();

        //when
        outWireGateMapping.set(gate, out);

        //then
        Gate savedGate=outWireGateMapping.getGateTo(out);
        assertEquals(gate, savedGate);
    }

    @Test
    public void testIsInputWireShouldBeTrueForActualInputWire(){
        //given
        Wire leftInputWire=WireMockData.mockWireA();
        Wire out=WireMockData.mockWireC();
        Gate gate=GateMockData.mockAndGate();
        outWireGateMapping.set(gate, out);

        //when
        boolean shouldBeInputWire=outWireGateMapping.isInputWire(leftInputWire);

        //then
        assertTrue(shouldBeInputWire);
    }

    @Test
    public void testIsInputWireShouldBeFalseForNoInputWire(){
        //given
        Wire out=WireMockData.mockWireC();
        Gate gate=GateMockData.mockAndGate();
        outWireGateMapping.set(gate, out);

        //when
        boolean shouldNotBeInputWire=outWireGateMapping.isInputWire(out);

        //then
        assertFalse(shouldNotBeInputWire);
    }

}