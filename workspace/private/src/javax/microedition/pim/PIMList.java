package javax.microedition.pim;

import java.util.Enumeration;

public interface PIMList
{

	public Enumeration items() throws PIMException;
    
	
	public int[] getSupportedFields();
	
	public boolean isSupportedField(int field);
	
	public boolean isSupportedArrayElement(int stringArrayField, int arrayElement);
	
	
}
