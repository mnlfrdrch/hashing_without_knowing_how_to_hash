package mockdata;

import localhost.hashing_without_knowing_how_to_hash.dto.party.HostDto;
import lombok.experimental.UtilityClass;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UtilityClass
public class HostDtoMockData {

    public final String HOST= "localhost";
    public final String PORT="8080";

    public HostDto mockHost(){
        HostDto host=mock(HostDto.class);
        when(host.getHost()).thenReturn(HOST);
        when(host.getPort()).thenReturn(PORT);
        return host;
    }
}
