package localhost.hashing_without_knowing_how_to_hash.dto.circuit;

import localhost.hashing_without_knowing_how_to_hash.model.circuit.FinalGate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Is used to transfer the information of a single garbled circuit from sender to receiver of oblivious transfer
 * Is build from data of a garbled circuit by CircuitBuilder
 * After transfer the exact same garbled circuit can be recreated with this data in order to be evaluated
 * Also holds addressing meta data, so the corresponding wire labels can later be requested and identified
 */
@NoArgsConstructor
public class CircuitDto {
    @Getter
    @Setter
    private int circuitHash;
    @Getter
    @Setter
    private FinalGate finalGate;
    @Getter
    @Setter
    private Map<String, String> gateIdToChildrenGateIdsOrWireIds;
    @Getter
    @Setter
    private GatesEncodingContainerDto gatesAbbreviation;

    public CircuitDto(FinalGate finalGate, Map<String, String> gateIdToChildrenGateIdsOrWireIds, GatesEncodingContainerDto encodingContainer){
        this.finalGate=finalGate;
        this.gateIdToChildrenGateIdsOrWireIds = gateIdToChildrenGateIdsOrWireIds;
        this.gatesAbbreviation=encodingContainer;
        circuitHash=hashCode();
    }

}
