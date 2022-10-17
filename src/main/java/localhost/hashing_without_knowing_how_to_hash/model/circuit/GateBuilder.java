package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds;
import yao.gate.*;

/**
 * Instantiates gates according to enum GateTypeIds
 * Implements simple instantiation of gates for building a garbled circuit
 * Potentially thrown exceptions are handled
 */
public class GateBuilder {

    private Gate defaultGate;

    public GateBuilder() throws Exception{
        defaultGate=instantiateDefaultGate();
    }

    /**
     * Instantiates a gate for specified parameters
     * If not gate could be instantiated, then a message is print to stacktrace and a default gate instance is returned
     * Implements gate instantiation including exception handling
     * @param gateTypeId e.g. AND, OR, NAND, NOR
     * @param leftIn the wire that leads into the left side of the gate
     * @param rightIn the wire that leads into the right side of the gate
     * @param out the wire heading out of the gate
     * @return either the newly instantiated gate or the default gate
     */
    public Gate instantiateGate(GateTypeIds gateTypeId, Wire leftIn, Wire rightIn, Wire out){
        Gate gate=null;

        try{
            gate=handleGateTypeSelection(gateTypeId, leftIn, rightIn, out);
        }
        catch (Exception exception){
            exception.printStackTrace();
            gate=defaultGate;
        }

        return gate;
    }

    private Gate handleGateTypeSelection(GateTypeIds gateTypeId, Wire leftIn, Wire rightIn, Wire out) throws Exception{
        Gate gate=null;
        switch (gateTypeId){
            case AND:
                gate=new AndGate(leftIn,rightIn,out);
                break;
            case OR:
                gate=new OrGate(leftIn, rightIn, out);
                break;
            case NAND:
                gate=new NAndGate(leftIn,rightIn,out);
                break;
            case NOR:
                gate=new NOrGate(leftIn,rightIn,out);
                break;
            default:
                throwIllegalGateTypeIdException(gateTypeId);
                break;
            }
        return gate;
        }

    private Gate instantiateDefaultGate() throws Exception{
        Wire defaultLeftIn=new Wire();
        Wire defaultRightIn=new Wire();
        Wire defaultOut=new Wire();
        Gate defaultGate=new GenericGate(defaultLeftIn, defaultRightIn, defaultOut, 0,0,0,0);
        return defaultGate;
    }
    private void throwIllegalGateTypeIdException(GateTypeIds illegalGateTypeId) throws Exception{
        throw new Exception("'" + illegalGateTypeId + "' is illegal GateTypeId; should be a value defined in enum 'GateTypeIds'");
    }
}
