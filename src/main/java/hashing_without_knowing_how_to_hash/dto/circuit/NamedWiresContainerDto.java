package main.java.hashing_without_knowing_how_to_hash.dto.circuit;

import lombok.Getter;
import yao.gate.Wire;

import java.util.Map;

/**
 * This object stays completely at the sender side of the oblivious transfer
 * It is created during creation of the original garbled circuit on the sender side
 * and it contains all the input wires, which are required to fully evaluate the circuit.
 * It also contains the circuitHash addressing information to be assignable to the corresponding circuit
 * This way it can be used to provide the both wire labels, which function as the secrets of performed oblivious transfer
 */
public class NamedWiresContainerDto {

    @Getter
    Map<String, Wire> wireIdToWire;
    @Getter
    int circuitHash;


    public NamedWiresContainerDto(Map<String, Wire> wireIdToWire, int circuitHash){
        this.wireIdToWire = wireIdToWire;
        this.circuitHash = circuitHash;
    }
}
