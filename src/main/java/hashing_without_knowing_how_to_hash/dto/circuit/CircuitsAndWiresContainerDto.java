package main.java.hashing_without_knowing_how_to_hash.dto.circuit;

import lombok.Getter;

import java.util.List;

/**
 * Contains the CircuitsContainerDto required to evaluate a single hash value and
 * their wires located in a List of NamedWireContainerDto
 * This object is necessary to create garbled circuits and all their potential input wire labels in one place
 * and later separate them
 * So the circuit can be transferred regularly and just the required wire labels can be obliviously transferred
 * This object stays completely on the sender side of oblivious transfer
 */
public class CircuitsAndWiresContainerDto {

    @Getter
    CircuitsContainerDto circuitsContainerDto;
    @Getter
    List<NamedWiresContainerDto> namedWiresContainerDtoList;

    public CircuitsAndWiresContainerDto(CircuitsContainerDto circuitsContainerDto, List<NamedWiresContainerDto> namedWiresContainerDtoList){
        this.circuitsContainerDto = circuitsContainerDto;
        this.namedWiresContainerDtoList = namedWiresContainerDtoList;
    }
}
