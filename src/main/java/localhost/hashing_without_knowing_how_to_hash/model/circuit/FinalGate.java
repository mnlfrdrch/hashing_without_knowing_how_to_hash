package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Arrays;

/**
 * The FinalGate class contains the last gate of a garbled circuit to evaluate as well as both potential output wire labels
 * This serves two purposes: First, the evaluator can identify the gate where to start the evaluation recursively
 * Second, the evaluator can actually decode the boolean values, which are represented by wire labels
 * So final wire label is being resolved to its corresponding wire value
 */
@NoArgsConstructor
public class FinalGate {

    @Getter
    @Setter
    private byte[] valueFalse;
    @Getter
    @Setter
    private byte[] valueTrue;
    @Getter
    @Setter
    private byte[][] encodedGate;

    /**
     * Constructor which takes
     * @param gate the final gate which determines the boolean value, to which the garbled circuit is evaluated
     * @param wire the wire leading out of the final gate
     */
    public FinalGate(Gate gate, Wire wire){
        valueFalse=wire.getValue0();
        valueTrue=wire.getValue1();
        encodedGate=gate.getLut();
    }

    /**
     * Compared the evaluated wire label of the last gate to be evaluated
     * Also implements a check, if the evaluated wire is illegitimate
     * If illegitimate, the value will always be evaluated to *false*
     * @param wireLabel the actually evaluated wire label from the garbled circuit
     * @return *true* if garbled circuit is evaluated to *true* wire value and *false* if the wire value is *false* or the wire label is illegitimate
     */
    public boolean isTrue(byte[] wireLabel){
        boolean isTrue=Arrays.equals(valueTrue, wireLabel);
        return isLegitimate(wireLabel) && isTrue;
    }

    /**
     * Checks if the wire label is illegitimate
     * by ensuring that the given wire label is either the *true* wire label or the *false* wire label
     * @param wireLabel evaluated wire label of final gate, which needs to be checked
     * @return *true* if it is either the *true* wire label or the *false* wire label and *false* if it is illegitimate
     */
    public boolean isLegitimate(byte[] wireLabel){
        boolean isTrue=Arrays.equals(valueTrue, wireLabel);
        boolean isFalse=Arrays.equals(valueFalse, wireLabel);
        return isTrue || isFalse;
    }
}
