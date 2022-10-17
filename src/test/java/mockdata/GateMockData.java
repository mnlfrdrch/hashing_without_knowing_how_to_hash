package mockdata;

import lombok.experimental.UtilityClass;
import org.mockito.Mockito;
import yao.Utils;
import yao.gate.AndGate;
import yao.gate.Gate;
import yao.gate.Wire;

@UtilityClass
public class GateMockData {

    public Gate mockAndGate() {
        byte[] left0=WireMockData.mockWireA().getValue0();
        byte[] left1=WireMockData.mockWireA().getValue1();
        byte[] right0=WireMockData.mockWireB().getValue0();
        byte[] right1=WireMockData.mockWireB().getValue1();
        byte[] out0=WireMockData.mockWireC().getValue0();
        byte[] out1=WireMockData.mockWireC().getValue1();
        Gate mockedAndGate=Mockito.mock(AndGate.class);

        try {
            Mockito.when(mockedAndGate.operate(left0, right0)).thenReturn(out0);
            Mockito.when(mockedAndGate.operate(left0, right1)).thenReturn(out0);
            Mockito.when(mockedAndGate.operate(left1, right0)).thenReturn(out0);
            Mockito.when(mockedAndGate.operate(left1, right1)).thenReturn(out1);

            byte[][] lut = new byte[4][];
            lut[0] = Utils.AESencrypt(Utils.AESencrypt(out0, left0), right0);
            lut[1] = Utils.AESencrypt(Utils.AESencrypt(out0, left0), right1);
            lut[2] = Utils.AESencrypt(Utils.AESencrypt(out0, left1), right0);
            lut[3] = Utils.AESencrypt(Utils.AESencrypt(out1, left1), right1);

            Mockito.when(mockedAndGate.getLut()).thenReturn(lut);

            Mockito.when(mockedAndGate.getLutEntry(0)).thenReturn(lut[0]);
            Mockito.when(mockedAndGate.getLutEntry(1)).thenReturn(lut[1]);
            Mockito.when(mockedAndGate.getLutEntry(2)).thenReturn(lut[2]);
            Mockito.when(mockedAndGate.getLutEntry(3)).thenReturn(lut[3]);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return mockedAndGate;
    }
}
