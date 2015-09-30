package javax.microedition.rms;

public class RecordEnumerationImpl implements RecordEnumeration
{
	private RecordStore store = null;
	
	private RecordFilter filter;
	private RecordComparator comparator;
	private boolean keepUpdated;
	
	private int curID = -1;
	
	protected RecordEnumerationImpl(RecordStore store, RecordFilter filter,
			RecordComparator comparator, boolean keepUpdated)
	{
		this.store = store;
		this.filter = filter;
		this.comparator = comparator;
		this.keepUpdated = keepUpdated;
		
		curID = -1;
	}

	public boolean hasNextElement()
	{
		for(int i=0; i<store.recordList.size(); i++)
		{
//			SingleRecord item = (SingleRecord)store.recordList.elementAt(i);
//			if(item != null && item.recordid > curID)
//			{
//				return true;
//			}
			String path = (String)store.recordList.elementAt(i);
			if(path != null && i+RecordStore.START_ID > curID)
			{
				return true;
			}
		}
		return false;
	}

	public int nextRecordId() throws InvalidRecordIDException
	{
		for(int i=0; i<store.recordList.size(); i++)
		{
//			SingleRecord item = (SingleRecord)store.recordList.elementAt(i);
//			if(item != null && item.recordid > curID)
//			{
//				curID = item.recordid;
//				return curID;
//			}
			String path = (String)store.recordList.elementAt(i);
			if(path != null && i+RecordStore.START_ID > curID)
			{
				curID = i+RecordStore.START_ID;
				return curID;
			}
		}
		
		throw new InvalidRecordIDException();
	}
	
	public byte[] nextRecord() throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		if(curID<RecordStore.START_ID)
		{
			nextRecordId();
		}
		
		if(curID >= RecordStore.START_ID)
		{
			return store.getRecord(curID);
		}
		
		throw new InvalidRecordIDException();
	}

	public void destroy()
	{
		filter = null;
		comparator = null;
		filter = null;
	}

	}
