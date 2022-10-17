package test.java.localhost.hashing_without_knowing_how_to_hash.model.circuit;

import mockdata.GateMockData;
import mockdata.WireMockData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.logicng.formulas.FormulaFactory;
import org.logicng.formulas.Literal;
import org.springframework.test.util.ReflectionTestUtils;
import yao.gate.AndGate;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CircuitEncodingTest {

    private CircuitEncoding circuitEncoding;

    @BeforeEach
    public void setUp(){
        FormulaFactory factory=new FormulaFactory();
        Literal a=factory.literal("A", true);
        Set<Literal> literals=new HashSet<>();
        literals.add(a);

        circuitEncoding=new CircuitEncoding(literals);
    }


    @Test
    public void testSetGateShouldPassObjectToGatesEncodingContainer(){
        //given
        Gate mockedGate=GateMockData.mockAndGate();

        //when
        circuitEncoding.setGate(mockedGate);
        byte[][] actualLut=circuitEncoding.getGateEncoding().getGatesEncodingContainer().getIdAndGates().get("1");

        //then
        assertTrue(Arrays.deepEquals(mockedGate.getLut(), actualLut));
    }

    @Test
    public void testGetGateShouldGetPreviouslySetGate(){
        //given
        Gate mockedGate=GateMockData.mockAndGate();
        circuitEncoding.setGate(mockedGate);

        //when
        Gate actualGate=circuitEncoding.getGate("1");

        //then
        assertTrue(Arrays.deepEquals(mockedGate.getLut(), actualGate.getLut()));
    }

    @Test
    public void testGetWireShouldReturnAnAutomaticallyGeneratedWire(){
        //given

        //when
        Wire actualWire=circuitEncoding.getWire("A");

        //then
        assertNotNull(actualWire);
    }

    @Test
    public void testSetWireShouldReturnWireId(){
        //given
        Wire automaticallyGeneratedInstance=circuitEncoding.getWireEncoding().getWire("A");

        //when
        String actualId=circuitEncoding.setWire(automaticallyGeneratedInstance);

        //then
        String expectedId="A";
        assertEquals(expectedId, actualId);

    }

}