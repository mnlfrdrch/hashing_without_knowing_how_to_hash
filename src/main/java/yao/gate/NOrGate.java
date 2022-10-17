package main.java.yao.gate;

public class NOrGate extends Gate{

    public NOrGate(Wire i1, Wire i2, Wire r) throws Exception
    {
        genEncryptedLut(1,0,0,0,i1,i2,r);
    }
}
