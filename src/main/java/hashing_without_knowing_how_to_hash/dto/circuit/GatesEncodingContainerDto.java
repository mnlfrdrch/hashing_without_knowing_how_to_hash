package main.java.hashing_without_knowing_how_to_hash.dto.circuit;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Provides a map of abbreviations of used encoded gate (id)
 * Gate abbreviations are numbers
 * Let's assume the encoded gates: gate1, gate2, gate3
 * The mapping could be 1->gate1, 2->gate2, 3->gate3
 * The circuit built with these gates can now be described shortly and with less characters by e.g: 1->2&3
 * Althought the structural information of the circuit is located in a GateChildrenMapping object*/
@NoArgsConstructor
public class GatesEncodingContainerDto {

    @Getter
    Map<String, byte[][]> idAndGates;

    public GatesEncodingContainerDto(Map<String, byte[][]> idAndGates){
        this.idAndGates=idAndGates;
    }
}
