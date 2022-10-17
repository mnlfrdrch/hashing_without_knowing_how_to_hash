package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.GateTypeIds;
import lombok.Getter;
import org.logicng.formulas.Literal;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Set;

/**
 * Allows for simple instantiation of the gates.
 * At the same time instanced gates are kept track of.
 * This allows for a format, in which the garbled circuit can be transferred
 * When the CircuitService is called, the 'transport format' of the garbled circuit is being transferred
 * instead of the actual garbled circuit itself
 */
public class GateInstantiationTracker {


    /**
     * Allows simple instantiation of the gates
     */
    private GateBuilder gateBuilder;

    /**
     * Holds the information, which parent gate is mapped to which children gates and initial wire, respectively
     * Can be shared later by the CircuitService
     */
    @Getter
    private GateChildrenMapping gateChildrenMapping;

    /**
     * Saves which wire leads out of which gate.
     * Is not important for sharing, but for building the garbled circuit
     */
    @Getter
    private OutWireGateMapping outWireGateMapping;

    public GateInstantiationTracker(Set<Literal> literals) throws Exception{
        gateBuilder=new GateBuilder();
        CircuitEncoding encoding=new CircuitEncoding(literals);
        gateChildrenMapping=new GateChildrenMapping(encoding);
        outWireGateMapping=new OutWireGateMapping();
    }

    /**
     * Instantiates Gate of a specified type and with the given wires
     * While being instantiated, the wire is also kept track of
     * This way the garbled circuit can be brought in a transferable format easily
     * @param type e.g. AND, OR, NAND, NOR
     * @param leftIn wire, that leads into the gate from the 'left side'
     * @param rightIn wire, that leads into the gate from the 'right side'
     * @param out wire, that leads out of the gate
     * @return tracked instance of the specified gate
     */
    public Gate instantiateGate(GateTypeIds type, Wire leftIn, Wire rightIn, Wire out){
        Gate parentGate=instantiateParentGate(type, leftIn, rightIn, out);
        setParentGateToChildrenMapping(parentGate, leftIn, rightIn);
        return parentGate;
    }

    private Gate instantiateParentGate(GateTypeIds type, Wire leftIn, Wire rightIn, Wire out){
        Gate parentGate=gateBuilder.instantiateGate(type,leftIn,rightIn,out);
        outWireGateMapping.set(parentGate, out);
        return parentGate;
    }

    private void setParentGateToChildrenMapping(Gate parentGate, Wire outWireLeftChild, Wire outWireRightChild){
        boolean leftIsInputWire=outWireGateMapping.isInputWire(outWireLeftChild);
        boolean rightIsInputWire=outWireGateMapping.isInputWire(outWireRightChild);

        if(!leftIsInputWire && !rightIsInputWire){
            mapChildrenLeftGateAndRightGate(parentGate,outWireLeftChild,outWireRightChild);
        }else if(!leftIsInputWire && rightIsInputWire){
            mapChildrenLeftGateAndRightWire(parentGate, outWireLeftChild, outWireRightChild);
        }else if(leftIsInputWire && !rightIsInputWire){
            mapChildrenLeftWireAndRightGate(parentGate, outWireLeftChild, outWireRightChild);
        }else{
            mapChildrenLeftWireAndRightWire(parentGate, outWireLeftChild, outWireRightChild);
        }
    }

    private void mapChildrenLeftGateAndRightGate(Gate parentGate, Wire leftChildWire, Wire rightChildWire){
        Gate leftChildGate=outWireGateMapping.getGateTo(leftChildWire);
        Gate rightChildGate=outWireGateMapping.getGateTo(rightChildWire);
        gateChildrenMapping.setMapping(leftChildGate, rightChildGate, parentGate);
    }

    private void mapChildrenLeftGateAndRightWire(Gate parentGate, Wire leftChildWire, Wire rightChildWire){
        Gate leftChildGate=outWireGateMapping.getGateTo(leftChildWire);
        gateChildrenMapping.setMapping(leftChildGate, rightChildWire, parentGate);
    }

    private void mapChildrenLeftWireAndRightGate(Gate parentGate, Wire leftChildWire, Wire rightChildWire){
        Gate rightChildGate=outWireGateMapping.getGateTo(rightChildWire);
        gateChildrenMapping.setMapping(leftChildWire, rightChildGate, parentGate);
    }

    private void mapChildrenLeftWireAndRightWire(Gate parentGate, Wire leftChildWire, Wire rightChildWire){
        gateChildrenMapping.setMapping(leftChildWire,rightChildWire,parentGate);
    }
}
