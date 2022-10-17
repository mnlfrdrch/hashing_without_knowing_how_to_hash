package localhost.hashing_without_knowing_how_to_hash.dto.circuit;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Is the main object which is transferred between the partys
 * Holds the CircuitDtos which are required to securely evaluate exactly one hash value
 * A single CircuitDto can only be used to evaluate a single bit
 * Therefore holds all the information which are required to evaluate the circuit, except input wire labels
 * Also comes with its own addressing field necessary to later on request the wire labels to valuate the circuit
 */
@NoArgsConstructor
public class CircuitsContainerDto {

    @Getter
    private int circuitsContainerHash;
    @Getter
    private List<CircuitDto> circuits;

    public CircuitsContainerDto(List<CircuitDto> circuits){
        circuitsContainerHash=hashCode();
        this.circuits=circuits;
    }
}
