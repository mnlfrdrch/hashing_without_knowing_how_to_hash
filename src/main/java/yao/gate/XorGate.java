package yao.gate;


public class XorGate extends Gate
{
	public XorGate(Wire i1,Wire i2,Wire r) throws Exception
	{
		genEncryptedLut(0,1,1,0,i1,i2,r);
	}
}
