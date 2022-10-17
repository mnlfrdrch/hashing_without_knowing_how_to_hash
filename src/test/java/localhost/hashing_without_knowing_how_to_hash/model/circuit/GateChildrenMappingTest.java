package test.java.localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds;
import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import mockdata.LiteralTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.logicng.formulas.Literal;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GateChildrenMappingTest {

    private GateChildrenMapping gateChildrenMapping;
    private CircuitEncoding circuitEncoding;
    private GateInstantiationTracker gateInstantiator;

    private List<Gate> gates;

    @BeforeEach
    public void setUp(){
        Set<Literal> literals= LiteralTestData.getLiteralsAtoG();

        circuitEncoding=new CircuitEncoding(literals);
        gateChildrenMapping=new GateChildrenMapping(circuitEncoding);

        try{
            gateInstantiator=new GateInstantiationTracker(literals);
        }
        catch (Exception e){
            fail(e.getMessage());
        }

        gates=getGates();
    }

    private List<Gate> getGates(){
        List<Gate> gates=new ArrayList<>();

        Gate leftGate=gateInstantiator.instantiateGate(GateTypeIds.AND, circuitEncoding.getWire("A"), circuitEncoding.getWire("B"), circuitEncoding.getWire("C"));
        Gate rightGate=gateInstantiator.instantiateGate(GateTypeIds.OR, circuitEncoding.getWire("D"), circuitEncoding.getWire("E"), circuitEncoding.getWire("F"));
        Gate outGate=gateInstantiator.instantiateGate(GateTypeIds.NAND, circuitEncoding.getWire("C"), circuitEncoding.getWire("F"), circuitEncoding.getWire("G"));

        gates.add(leftGate);
        gates.add(rightGate);
        gates.add(outGate);

        return gates;
    }

    @Test
    public void testSetMappingGateGateGateShouldAddNewMapEntry(){
        //given
        Gate beforeLeft=gates.get(0);
        Gate beforeRight=gates.get(1);
        Gate out=gates.get(2);

        //when
        gateChildrenMapping.setMapping(beforeLeft, beforeRight, out);

        //then
        int sizeAfter=gateChildrenMapping.getChildrenToParentGate().size();
        assertEquals(1, sizeAfter);
    }

    @Test
    public void testSetMappingGateWireGateShouldAddNewMapEntry(){
        //given
        Gate beforeLeft=gates.get(0);
        Wire beforeRight=circuitEncoding.getWire("F");
        Gate out=gates.get(2);

        //when
        gateChildrenMapping.setMapping(beforeLeft,beforeRight, out);

        //then
        int sizeAfter=gateChildrenMapping.getChildrenToParentGate().size();
        assertEquals(1, sizeAfter);
    }

    @Test
    public void testSetMappingWireGateGateShouldAddNewMapEntry(){
        //given
        Wire beforeLeft=circuitEncoding.getWire("C");
        Gate beforeRight=gates.get(1);
        Gate out=gates.get(2);

        //when
        gateChildrenMapping.setMapping(beforeLeft,beforeRight, out);

        //then
        int sizeAfter=gateChildrenMapping.getChildrenToParentGate().size();
        assertEquals(1, sizeAfter);
    }

    @Test
    public void testSetMappingWireWireGateShouldAddNewMapEntry(){
        //given
        Wire beforeLeft=circuitEncoding.getWire("C");
        Wire beforeRight=circuitEncoding.getWire("F");
        Gate out=gates.get(2);

        //when
        gateChildrenMapping.setMapping(beforeLeft,beforeRight, out);

        //then
        int sizeAfter=gateChildrenMapping.getChildrenToParentGate().size();
        assertEquals(1, sizeAfter);
    }

    @Test
    public void testSetMappingGateGateGateShouldAddsExpectedEncoding(){
        //given
        Gate beforeLeft=gates.get(0);
        Gate beforeRight=gates.get(1);
        Gate out=gates.get(2);

        String expectedKey="3";
        String expectedValue="1"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"2";

        //when
        gateChildrenMapping.setMapping(beforeLeft, beforeRight, out);

        //then
        String actualValue=gateChildrenMapping.getChildrenToParentGate().get(expectedKey);

        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testSetMappingGateWireGateShouldAddsExpectedEncoding(){
        //given
        Gate beforeLeft=gates.get(0);
        Wire beforeRight=circuitEncoding.getWire("F");
        Gate out=gates.get(2);

        String expectedKey="2";
        String expectedValue="1"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"F";

        //when
        gateChildrenMapping.setMapping(beforeLeft, beforeRight, out);

        //then
        String actualValue=gateChildrenMapping.getChildrenToParentGate().get(expectedKey);

        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testSetMappingWireGateGateShouldAddsExpectedEncoding(){
        //given
        Wire beforeLeft=circuitEncoding.getWire("C");
        Gate beforeRight=gates.get(1);
        Gate out=gates.get(2);

        String expectedKey="2";
        String expectedValue="C"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"1";

        //when
        gateChildrenMapping.setMapping(beforeLeft, beforeRight, out);

        //then
        String actualValue=gateChildrenMapping.getChildrenToParentGate().get(expectedKey);

        assertEquals(expectedValue,actualValue);
    }

    @Test
    public void testSetMappingWireWireGateShouldAddsExpectedEncoding(){
        //given
        Wire beforeLeft=circuitEncoding.getWire("C");
        Wire beforeRight=circuitEncoding.getWire("F");
        Gate out=gates.get(2);

        String expectedKey="1";
        String expectedValue="C"+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+"F";

        //when
        gateChildrenMapping.setMapping(beforeLeft, beforeRight, out);

        //then
        String actualValue=gateChildrenMapping.getChildrenToParentGate().get(expectedKey);

        assertEquals(expectedValue,actualValue);
    }
}