package yao.gate;

public class NAndGate extends Gate {
    public NAndGate(Wire i1, Wire i2, Wire r) throws Exception
    {
        genEncryptedLut(1,1,1,0,i1,i2,r);
    }
}
