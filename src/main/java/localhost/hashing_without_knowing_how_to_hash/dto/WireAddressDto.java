package localhost.hashing_without_knowing_how_to_hash.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Addressing schema for assigning corresponding wire or wire labels or sender-receiver pair
 * The receiver will find the necessary parameters in a CircuitsContainerDto instance
 * Contains all information required to identify a certain Wire / WireLabel instance
 */
@NoArgsConstructor
public class WireAddressDto {

    @Getter
    private int circuitsContainerHash;
    @Getter
    private int circuitHash;
    @Getter
    private String wireId;

    public WireAddressDto(int circuitsContainerHash, int circuitHash, String wireId){
        this.circuitsContainerHash=circuitsContainerHash;
        this.circuitHash=circuitHash;
        this.wireId=wireId;
    }

    @Override
    public String toString(){
        return String.valueOf(circuitsContainerHash)+String.valueOf(circuitHash)+wireId;
    }

}
