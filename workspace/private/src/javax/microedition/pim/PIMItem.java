package javax.microedition.pim;

public interface PIMItem
{

	public int countValues(int field);
	
	public String getString(int field, int index);
	
	public String[] getStringArray(int field, int index);
}
