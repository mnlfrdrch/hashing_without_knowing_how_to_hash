package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import localhost.hashing_without_knowing_how_to_hash.constants.ReservedCharacters;
import lombok.Getter;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps gates/input wires to their predecessors in build phase of the circuit
 * This allows for efficient recreation of the circuit while keeping network traffic low
 */
public class GateChildrenMapping {

    @Getter
    private Map<String, String> childrenToParentGate;
    @Getter
    private CircuitEncoding encoding;

    public GateChildrenMapping(CircuitEncoding encoding)
    {
        childrenToParentGate=new HashMap<>();
        this.encoding=encoding;
    }

    /**
     * The correct Mapping-Function to use, if for each input wire there is a gate that needs to be computed before
     * e.g. formula is: (A & B) | (B & C)
     * @param leftIn the "&" gate of "A & B"
     * @param rightIn the "&" gate of "B & C"
     * @param out the "|" gate of the operation "(A & B) | (B & C)"
     */
    public void setMapping(Gate leftIn, Gate rightIn, Gate out){
        String leftGateId=encoding.setGate(leftIn);
        String rightGateId=encoding.setGate(rightIn);
        String outGateId=encoding.setGate(out);

        String concatenatedChildrenId=concatenateIds(leftGateId, rightGateId);
        childrenToParentGate.put(outGateId, concatenatedChildrenId);
    }

    /**
     * The correct Mapping-Function to use, if only for the left input wire there is a gate that needs to be computed before.
     * The right input is an initial wire of a literal.
     * e.g. formula is: (A & B) | C
     * @param leftIn the "&" gate of "A & B"
     * @param rightIn the wire of "C"
     * @param out the "|" gate of the operation "(A & B) | C"
     */
    public void setMapping(Gate leftIn, Wire rightIn, Gate out){
        String leftGateId=encoding.setGate(leftIn);
        String rightWireId=encoding.setWire(rightIn);
        String outGateId=encoding.setGate(out);

        String concatenatedChildrenId=concatenateIds(leftGateId, rightWireId);
        childrenToParentGate.put(outGateId, concatenatedChildrenId);
    }

    /**
     * The correct Mapping-Function to use, if only for the right input wire there is a gate that needs to be computed before.
     * The left input is an initial wire of a literal.
     * e.g. formula is: A | (B & C)
     * @param leftIn the wire of "A"
     * @param rightIn the "&" gate of "B & C"
     * @param out the "|" gate of the operation "A | (B & C)"
     */
    public void setMapping(Wire leftIn, Gate rightIn, Gate out){
        String leftWireId=encoding.setWire(leftIn);
        String rightGateId=encoding.setGate(rightIn);
        String outGateId=encoding.setGate(out);

        String concatenatedChildrenId=concatenateIds(leftWireId, rightGateId);
        childrenToParentGate.put(outGateId, concatenatedChildrenId);
    }

    /**
     * The correct Mapping-Function to use, if both inputs of the current gate are initial wires
     * e.g. formula is: A | B
     * @param leftIn the wire of "A"
     * @param rightIn the wire of "B"
     * @param out the "|" gate of the operation "A | B"
     */
    public void setMapping(Wire leftIn, Wire rightIn, Gate out){
        String leftWireId=encoding.setWire(leftIn);
        String rightWireId=encoding.setWire(rightIn);
        String outGateId=encoding.setGate(out);

        String concatenatedChildrenId=concatenateIds(leftWireId, rightWireId);
        childrenToParentGate.put(outGateId, concatenatedChildrenId);
    }

    private String concatenateIds(String leftChildId, String rightChildId){
        return leftChildId+String.valueOf(ReservedCharacters.GATE_CHILDREN_SEPARATOR)+rightChildId;
    }
}
