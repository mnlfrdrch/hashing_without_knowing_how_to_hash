package main.java.hashing_without_knowing_how_to_hash.dto.party;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Holds 'host' and 'port' in one place.
 * Addresses host service by saved parameters 'host name' and 'port' in order to perform HTTP GET and POST operation
 */
@NoArgsConstructor
public class HostDto {

    @Getter
    String host;
    @Getter
    String port;

    public HostDto(String host, String port){
        this.host=host;
        this.port=port;
    }
}
