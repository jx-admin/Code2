package javax.microedition.pim;

public abstract class PIM
{
	public static final int CONTACT_LIST = 0;
	
	public static final int READ_ONLY = 0;
	
	public static PIM pim = null;
	
	public static PIM getInstance()
	{
		if(pim == null)
			pim = new PIMImpl();
		return pim;
	}
	
	public abstract PIMList openPIMList(int pimListType, int mode) throws PIMException;
	
}
