package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yao.gate.*;

import static org.junit.jupiter.api.Assertions.*;

class GateBuilderTest {

    private GateBuilder gateBuilder;

    private Wire left;
    private Wire right;
    private Wire out;

    @BeforeEach
    public void setUp(){
        try {
            gateBuilder = new GateBuilder();
            left=new Wire();//WireMockData.mockWireA();
            right=new Wire();//WireMockData.mockWireB();
            out=new Wire();//WireMockData.mockWireC();
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testInstantiateGateOfTypeOrShouldCreateOrGateInstance(){
        //given
        GateTypeIds gateType=GateTypeIds.OR;

        //when
        Gate instantiatedGate=gateBuilder.instantiateGate(gateType, left, right, out);

        //then
        assertInstanceOf(OrGate.class, instantiatedGate);
    }

    @Test
    public void testInstantiateGateOfTypeAndShouldCreateAndGateInstance(){
        //given
        GateTypeIds gateType=GateTypeIds.AND;

        //when
        Gate instantiatedGate=gateBuilder.instantiateGate(gateType, left, right, out);

        //then
        assertInstanceOf(AndGate.class, instantiatedGate);
    }

    @Test
    public void testInstantiateGateOfTypeNorShouldCreateNorGateInstance(){
        //given
        GateTypeIds gateType=GateTypeIds.NOR;

        //when
        Gate instantiatedGate=gateBuilder.instantiateGate(gateType, left, right, out);

        //then
        assertInstanceOf(NOrGate.class, instantiatedGate);
    }

    @Test
    public void testInstantiateGateOfTypeNandShouldCreateOrGateInstance(){
        //given
        GateTypeIds gateType=GateTypeIds.NAND;

        //when
        Gate instantiatedGate=gateBuilder.instantiateGate(gateType, left, right, out);

        //then
        assertInstanceOf(NAndGate.class, instantiatedGate);
    }
}