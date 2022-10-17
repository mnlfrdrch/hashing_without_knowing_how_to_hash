package main.java.hashing_without_knowing_how_to_hash.model.circuit;

import lombok.Getter;
import org.logicng.formulas.Literal;
import yao.gate.Gate;
import yao.gate.Wire;

import java.util.Set;


/**
 * Holds encoding of input wires as well as encoding of all the intermediate gates
 * input wires are encoded as alphabetic letter string while gates are encoded as numerical string
 */
public class CircuitEncoding {

    @Getter
    private GateEncoding gateEncoding;
    @Getter
    private WireEncoding wireEncoding;


    public CircuitEncoding(Set<Literal> literals) {
        gateEncoding = new GateEncoding();
        wireEncoding = new WireEncoding(literals);
    }

    /**
     * store gate instance g and get unique numerical id back
     * @param gate is Gate to set
     * @return id of this specific gate
     */
    public String setGate(Gate gate) {
        return gateEncoding.setGate(gate);
    }

    public String setWire(Wire wire) {
        return wireEncoding.getId(wire);
    }

    public Gate getGate(String id) {
        return gateEncoding.getGate(id);
    }

    public Wire getWire(String id) {
        return wireEncoding.getWire(id);
    }

}
