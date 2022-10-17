package mockdata;

import localhost.hashing_without_knowing_how_to_hash.cache.WireCache;
import localhost.hashing_without_knowing_how_to_hash.dto.WireAddressDto;
import localhost.hashing_without_knowing_how_to_hash.security.access_control_tree.AccessControlTreeFinalLayer;
import lombok.experimental.UtilityClass;
import org.mockito.Mockito;
import yao.gate.Wire;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

@UtilityClass
public class WireMockData {

    public final int CIRCUITS_CONTAINER_HASH=2562;
    public final int CIRCUIT_HASH=4569;
    public final String WIRE_ID_A="A";
    public final String WIRE_ID_B="B";
    public final String WIRE_ID_C="C";
    public final String WIRE_ID_NON_EXISTING="Z";

    public final String random11bytes ="hvlncqsafet";

    public WireCache putMockWiresInWireCache(){
        WireCache wireCache=WireCache.getInstance();

        Map<String, Wire> idToWires=getWireEncodingOfMockedWires();
        AccessControlTreeFinalLayer<Wire> finalLayer=new AccessControlTreeFinalLayer<>(idToWires);
        wireCache.cacheWires(CIRCUITS_CONTAINER_HASH,CIRCUIT_HASH,finalLayer);

        return wireCache;
    }

    public Map<String, Wire> getWireEncodingOfMockedWires(){
        Map<String, Wire> idToWires=new HashMap<>();
        idToWires.put(WIRE_ID_A, mockWireA());
        idToWires.put(WIRE_ID_B, mockWireB());
        return idToWires;
    }

    public Wire mockWireA(){
        Wire a= Mockito.mock(Wire.class);
        byte[] val0=("aVal0"+ random11bytes).getBytes();
        byte[] val1=("aVal1"+ random11bytes).getBytes();
        when(a.getValue0()).thenReturn(val0);
        when(a.getValue1()).thenReturn(val1);
        return a;
    }

    public Wire mockWireB(){
        Wire b=Mockito.mock(Wire.class);
        byte[] val0=("bVal0"+ random11bytes).getBytes();
        byte[] val1=("bVal1"+ random11bytes).getBytes();
        when(b.getValue0()).thenReturn(val0);
        when(b.getValue1()).thenReturn(val1);
        return b;
    }

    public Wire mockWireC(){
        Wire c=Mockito.mock(Wire.class);
        byte[] val0=("cVal0"+ random11bytes).getBytes();
        byte[] val1=("cVal1"+ random11bytes).getBytes();
        when(c.getValue0()).thenReturn(val0);
        when(c.getValue1()).thenReturn(val1);
        return c;
    }

    public WireAddressDto getAddressOfA(){
        return new WireAddressDto(CIRCUITS_CONTAINER_HASH, CIRCUIT_HASH, WIRE_ID_A);
    }

    public WireAddressDto getAddressOfB(){
        return new WireAddressDto(CIRCUITS_CONTAINER_HASH, CIRCUIT_HASH, WIRE_ID_B);
    }

    public WireAddressDto getAddressOfC(){
        return new WireAddressDto(CIRCUITS_CONTAINER_HASH, CIRCUIT_HASH, WIRE_ID_C);
    }

}
