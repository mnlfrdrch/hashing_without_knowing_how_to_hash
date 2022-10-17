package test.java.localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds;
import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import mockdata.LiteralTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GateInstantiationTrackerTest {

    private GateInstantiationTracker gateInstantiationTracker;
    private Wire left;
    private Wire right;
    private Wire out;

    @BeforeEach
    public void setUp(){
        try {
            gateInstantiationTracker = new GateInstantiationTracker(LiteralTestData.getLiteralsAtoC());
            WireEncoding wireEncoding=gateInstantiationTracker.getGateChildrenMapping().getEncoding().getWireEncoding();
            left=wireEncoding.getWire("A");
            right=wireEncoding.getWire("B");
            out=wireEncoding.getWire("C");
        }
        catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testInstantiateGateShouldMakeAnEntryInGateChildrenMapping(){
        //given

        //when
        Gate gate=gateInstantiationTracker.instantiateGate(GateTypeIds.AND, left, right, out);
        int actualSize=gateInstantiationTracker.getGateChildrenMapping().getChildrenToParentGate().size();

        //then
        assertEquals(1,actualSize);
    }

    @Test
    public void testInstantiateGateShouldKeepTrackOfGeneratedGates(){
        //given
        String nameWireLeft="A";
        String nameWireRight="B";
        String expectedValue=nameWireLeft+ ReservedCharacters.GATE_CHILDREN_SEPARATOR+nameWireRight;

        //when
        Gate gate=gateInstantiationTracker.instantiateGate(GateTypeIds.AND, left, right, out);

        //then
        Map<String, String> childrenToParentGate=gateInstantiationTracker.getGateChildrenMapping().getChildrenToParentGate();
        assertEquals(expectedValue, childrenToParentGate.get("1"));
    }

    @Test
    public void testInstantiateGateShouldSaveWireOutOfGateAndHoldItRequestable(){
        //given
            // wireOut

        //when
        Gate expectedGate=gateInstantiationTracker.instantiateGate(GateTypeIds.AND, left, right, out);

        //then
        Gate actualGate=gateInstantiationTracker.getOutWireGateMapping().getGateTo(out);
        assertEquals(expectedGate, actualGate);
    }
}