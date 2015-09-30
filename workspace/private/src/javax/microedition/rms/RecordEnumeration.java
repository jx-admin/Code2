package javax.microedition.rms;

public interface RecordEnumeration
{
	public boolean hasNextElement();

	public int nextRecordId()throws InvalidRecordIDException;
	
	byte[] nextRecord() throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;
	
	public void destroy();
}
