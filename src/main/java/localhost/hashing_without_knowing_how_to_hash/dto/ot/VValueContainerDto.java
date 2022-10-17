package localhost.hashing_without_knowing_how_to_hash.dto.ot;

import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * VValueContainer is wraps a VValueContainer object and adds additional addressing information
 * The Receiver needs to send this object back to the (specifically addressed) sender
 */
@NoArgsConstructor
public class VValueContainerDto {

    @Getter
    private WireAddressDto wireAddress;
    @Getter
    private VValueDto vvalue;

    public VValueContainerDto(WireAddressDto wireAddress, VValueDto vValueDto){
        this.wireAddress=wireAddress;
        this.vvalue= vValueDto;
    }

}
