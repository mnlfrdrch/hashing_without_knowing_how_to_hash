package localhost.hashing_without_knowing_how_to_hash.model.circuit;

import yao.gate.Gate;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;

/**
 * Saves pairs of a gate and the wire leading out of this gate
 * Is required by the GateInstantiationTracker in order to bring the circuit into a transferable format
 */
public class OutWireGateMapping {

    private Map<Wire, Gate> outWireToGate;

    public OutWireGateMapping(){
        outWireToGate=new HashMap<>();
    }

    /**
     * Saves a pair of
     * @param gate a gate
     * @param out and the wire leading out of the gate
     */
    public void set(Gate gate, Wire out){
        outWireToGate.put(out, gate);
    }

    /**
     * Finds previously saves gates so that
     * @param wire is the wire leading out of an unknown gate
     * @return the gate instance of which the specified wire instance is leading out
     */
    public Gate getGateTo(Wire wire){
        return outWireToGate.get(wire);
    }

    /**
     * Tells, if a given wire instance is an initial/input wire
     * and therefore no output wire of any gate
     * @param wire the wire to make the query about
     * @return *true* if it is an input wire, else *false*
     */
    public boolean isInputWire(Wire wire){
        return !outWireToGate.containsKey(wire);
    }

}
