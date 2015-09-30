
package javax.microedition.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;


public class RecordStore
{
	public static final int AUTHMODE_ANY = 1;
	public static final int AUTHMODE_PRIVATE = 0;
	
	private String name;
	private int authmode;
	private boolean writable;
	private long lastModified;
	private int version = 0;
	
	public Vector recordList;
	private Vector listenerList;
	private boolean isOpen = false;
	
	private Object rsLock = new Object();
	
	public static final int START_ID = 1;	//ID从0或1开始
	
	private RecordStore(String name, int authmode, boolean writable)
	{
		this.name = name;
		this.authmode = authmode;
		this.writable = writable;
		recordList = new Vector();
		listenerList = new Vector();
		isOpen = true;
	}
	
	public synchronized int addRecord(byte[] data, int offset, int numBytes)
		throws RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException
	{
		if(data!= null && offset + numBytes > data.length)
			throw new RecordStoreException("addRecord: numBytes is too large ");
		
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		SingleRecord item = null;
		
		if(numBytes > 0)
		{
			byte[] itemdata = new byte[numBytes];
			for(int i=0; i<numBytes; i++)
				itemdata[i] = data[i];
			item = new SingleRecord(recordList.size()+START_ID, numBytes, itemdata);
		}
		else
		{
			item = new SingleRecord(recordList.size()+START_ID, 0, null);
		}
		
		recordList.addElement( name + "_" + item.recordid  +".rms");
		//recordList.addElement(item);
		//version ++;
		
		if(listenerList!=null)
		{
			for(int i=0; i<listenerList.size(); i++)
			{
				RecordListener listener = (RecordListener)listenerList.elementAt(i);
				listener.recordAdded(this, item.recordid);
			}
		}
		
		writeSingleRecord(item);
		writeFile();
		
		return item.recordid;
	}
	
	public synchronized void addRecordListener(RecordListener listener)
	{
		if(!listenerList.contains(listener))
			listenerList.addElement(listener);
	}
	
	public synchronized void closeRecordStore()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		//writeFile();
		isOpen = false;
	}
	
	public synchronized void deleteRecord(int recordId) 
		throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		checkRecordId(recordId);
		recordList.setElementAt(null, recordId-START_ID);
		
		deleteSingleRecord(recordId);
		writeFile();
		//version ++;
		
		if(listenerList!=null)
		{
			for(int i=0; i<listenerList.size(); i++)
			{
				RecordListener listener = (RecordListener)listenerList.elementAt(i);
				listener.recordDeleted(this, recordId);
			}
		}
	}
	
	public static void deleteRecordStore(String recordStoreName)
			throws RecordStoreException, RecordStoreNotFoundException
	{
		try{
			String fileName = getRMSPath(null, -1);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
			{
				Enumeration list = fileConnection.list();
				while(list.hasMoreElements())
				{
					String nodeName = (String)list.nextElement();
//					if(nodeName.startsWith(nodeName))
					if(nodeName.equals(fileName+".rms") || nodeName.startsWith(nodeName+"_"))
					{
						FileConnection conn = (FileConnection)Connector.open(fileName+"//"+nodeName);
						conn.delete();
						conn.close();
					}
				}
			}
			else
			{
				fileConnection.close();
				throw new RecordStoreNotFoundException();
			}
			
			fileConnection.close();
		}catch (Exception e) {
			throw new RecordStoreException();
		}
	}
	
	public synchronized RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator, boolean keepUpdated)
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		RecordEnumerationImpl enumeration = new RecordEnumerationImpl(this, filter, comparator, keepUpdated);
		return enumeration;
	}
	
	public synchronized long getLastModified()
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		return lastModified;
	}
	
	public synchronized String getName()
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		return name;
	}
	
	public synchronized int getNextRecordID()
		throws RecordStoreNotOpenException, RecordStoreException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		return recordList.size()+START_ID;
	}
	
	public synchronized int getNumRecords()
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		int count = 0;
		for(int i=0; i<recordList.size(); i++)
		{
			if( recordList.elementAt(i) != null)
				count ++;
		}
		return count;
	}
	
	public synchronized byte[] getRecord(int recordId) 
		throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		checkRecordId(recordId);
		
//		SingleRecord item = (SingleRecord)recordList.elementAt(recordId-START_ID);
		SingleRecord item = readSingleRecord(recordId);
		if(item==null)
			return null;
		return item.data;
	}
	
	public synchronized int getRecord(int recordId, byte[] buffer, int offset)
		throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		checkRecordId(recordId);
		
		//SingleRecord item = (SingleRecord)recordList.elementAt(recordId-START_ID);
		SingleRecord item = readSingleRecord(recordId);
		if(item == null || buffer.length < item.dataSize + offset)
			throw new RecordStoreException();
		
		for(int i=0; i<item.dataSize; i++)
			buffer[i+offset] = item.data[i];
		return item.dataSize;
	}
	
	public synchronized int getRecordSize(int recordId) 
		throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		checkRecordId(recordId);
		
//		//SingleRecord item = (SingleRecord)recordList.elementAt(recordId-START_ID);
//		SingleRecord item = readSingleRecord(recordId);
//		if(item == null)
//			return 0;
//		return item.dataSize;
		
		int result = 0;
		
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		try{
			
			String fileName = getRMSPath(name, recordId);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
				result = (int)fileConnection.fileSize();
			fileConnection.close();
		} catch (Exception e)
		{
			throw new RecordStoreException();
		}
		return result;

	}
	
	public synchronized int getSize()
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		int size = 21;
		try{
			size += name.getBytes("utf-8").length;
		}catch (Exception e) {
			size += name.length()*2 + 2;
		}
		for(int i=0; i<recordList.size(); i++)
		{
//			SingleRecord item = (SingleRecord)recordList.elementAt(i);
//			size += 1;
//			if(item!=null)
//				size += 8 + item.dataSize;
			
			String itemName = (String)recordList.elementAt(i);
			if(itemName!=null)
			{
				try{
					size += getRecordSize(i + START_ID);
				}catch (Exception e) {
				}
			}
		}
		return size;
	}
	
	public synchronized int getSizeAvailable() 
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		return 1024*1024;
	}
	
	public synchronized int getVersion()
		throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();

		return version;
	}
	
	public static String[] listRecordStores() //impl
	{
		try {
			Vector files = new Vector();
			String path = getRMSPath(null, -1);
			FileConnection fileConnection = (FileConnection)Connector.open(path);
			Enumeration e = fileConnection.list();
			while(e.hasMoreElements())
			{
				String element = (String)e.nextElement();
				if ( element.endsWith(".rms") && element.indexOf("_")==-1)
				{
					files.addElement(element.substring(0, element.length() - 4));
				}
			}
			fileConnection.close();
			
			String[] ret = new String[files.size()];
			for(int i=0; i<files.size(); i++)
				ret[i] = (String)files.elementAt(i);
			return ret;
			
		}catch (Exception e) {
		}
		return null;
	}
	
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary)
			throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		return openRecordStore(recordStoreName, createIfNecessary, AUTHMODE_PRIVATE, true);
	}
	
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary, int authmode, boolean writable)
			throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		try{
			RecordStore store = new RecordStore(recordStoreName, authmode, writable);
			if(store.readFile(recordStoreName))
			{
//				String fileName = getRMSPath(null, -1);
//				FileConnection fileConnection = (FileConnection)Connector.open(fileName);
//				Enumeration fileEnum = fileConnection.list();
//				while(fileEnum.hasMoreElements())
//				{
//					String nodeName = (String)fileEnum.nextElement();
//					if(nodeName.startsWith(recordStoreName+"_") && nodeName.endsWith(".rms"))
//					{
//						String mid = nodeName.substring(recordStoreName.length()+1, nodeName.length()-4);
//						try{
//							int id = Integer.parseInt(mid);
//							if(store.recordList.size() <= id-START_ID)
//							{
//								for(int i = store.recordList.size(); i<=id-START_ID; i ++)
//								{
//									store.recordList.addElement(null);
//								}
//							}
//							store.recordList.setElementAt(mid, id-START_ID);
//						}catch (Exception e) {
//						}
//					}
//				}
//				fileConnection.close();
				return store;
			}
			else
			{
				if(createIfNecessary)
				{
					String fileName = getRMSPath(recordStoreName, -1);
					FileConnection fileConnection = (FileConnection)Connector.open(fileName);
					fileConnection.create();
					fileConnection.close();
					//DataOutputStream out = fileConnection.openDataOutputStream();
					store.writeFile();
					//out.flush();
					return store;
				}
				else
					return null;
			}
		}catch (Exception e) {
			return null;
		}
	}
	
//	public static RecordStore openRecordStore(String recordStoreName, String vendorName, String suiteName)
//	{}
	
	public synchronized void removeRecordListener(RecordListener listener)
	{
		listenerList.removeElement(listener);
	}
	
	public synchronized void setMode(int authmode, boolean writable)
	{
		this.authmode = authmode;
		this.writable = writable;
	}
	
	public synchronized void setRecord(int recordId, byte[] newData, int offset, int numBytes) 
		throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		checkRecordId(recordId);
		
		SingleRecord item = new SingleRecord();
		//SingleRecord item = (SingleRecord)recordList.elementAt(recordId-START_ID);
		item.recordid = recordId;
		item.dataSize = numBytes;
		item.data = new byte[numBytes];
		for(int i=0; i<numBytes; i++)
		{
			item.data[i] = newData[offset + i];
		}
		
		if(listenerList!=null)
		{
			for(int i=0; i<listenerList.size(); i++)
			{
				RecordListener listener = (RecordListener)listenerList.elementAt(i);
				listener.recordChanged(this, recordId);
			}
		}
		
		deleteSingleRecord(recordId);
		writeSingleRecord(item);
		writeFile();
	}
	
	public boolean isOpen()
	{
		return isOpen;
	}
	
	public synchronized int[] getRecordIDs() throws RecordStoreNotOpenException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		int[] ids = new int[getNumRecords()];
		int count = 0;
		for(int i=0; i<recordList.size(); i++)
		{
			//SingleRecord item = (SingleRecord)recordList.elementAt(i);
			//if(item != null)
			//	ids[count++] = item.recordid;
			String item = (String)recordList.elementAt(i);
			if(item != null)
				ids[count++] = i + START_ID;
		}
		return ids;
	}
	
//	private void read(DataInputStream in)
//	{
//		try{
//			name = in.readUTF();
//			authmode = in.readInt();
//			writable = in.readBoolean();
//			lastModified = in.readLong();
//			version = in.readInt();
//			int size = in.readInt();
//			recordList.removeAllElements();
//			for(int i=0; i<size; i++)
//			{
//				boolean exsit = in.readBoolean();
//				SingleRecord item = null;
//				if(exsit)
//				{
//					item = new SingleRecord();
//					item.read(in);
//				}
//				recordList.addElement(item);
//			}
//		}catch (Exception e) {
//		}
//	}
//	
//	private void write(DataOutputStream out)
//	{
//		try{
//			out.writeUTF(name);
//			out.writeInt(authmode);
//			out.writeBoolean(writable);
//			lastModified = System.currentTimeMillis();
//			out.writeLong(lastModified);
//			out.writeInt(version);
//			out.writeInt(recordList.size());
//			for(int i=0; i<recordList.size(); i++)
//			{
//				SingleRecord item = (SingleRecord)recordList.elementAt(i);
//				if(item==null)
//					out.writeBoolean(false);
//				else
//				{
//					out.writeBoolean(true);
//					item.write(out);
//				}
//			}
//		}catch (Exception e) {
//		}
//	}
	
	private void checkRecordId(int recordId) 
		throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		if( recordId < START_ID || recordId >= recordList.size() + START_ID)
			throw new InvalidRecordIDException();
		
		if(recordList.elementAt(recordId-START_ID) == null)
			throw new InvalidRecordIDException();
	}
	
	private static String getRMSPath(String rmsName, int id)
	{
		//String fileName = "file://root1//rms";
		String fileName = "file://output/rms";
		try{
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(!fileConnection.exists())
			{
				fileConnection.mkdir();
			}
			fileConnection.close();
			
			if(rmsName!=null)
			{
				if(id >= START_ID)
					fileName = fileName + "//" + rmsName + "_" + id + ".rms";
				else
					fileName = fileName + "//" + rmsName + ".rms";
			}
			else
			{
				fileName = fileName + "//";
				return fileName;
			}
		}catch (Exception e) {
		}
		return fileName;
	}
	
//	private void writeFile() 
//		throws RecordStoreNotOpenException, RecordStoreException
//	{
//		if(!isOpen)
//			throw new RecordStoreNotOpenException();
//		
//		try{
//			String fileName = getRMSPath(name, -1);
//			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
//			if(fileConnection.exists())
//			{
//				DataOutputStream out = fileConnection.openDataOutputStream();
//				this.write(out);
//				out.flush();
//				out.close();
//			}
//			fileConnection.close();
//		} catch (Exception e)
//		{
//			throw new RecordStoreException();
//		}
//	}
	
	private void writeFile() 
		throws RecordStoreNotOpenException, RecordStoreException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		try{
			String fileName = getRMSPath(name, -1);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
					fileConnection.delete();
			fileConnection.create();
			DataOutputStream out = fileConnection.openDataOutputStream();
			out.writeInt(authmode);
			out.writeBoolean(writable);
			lastModified = System.currentTimeMillis();
			out.writeLong(lastModified);
			out.writeInt(version++);
			out.writeInt(recordList.size());
			for(int i=0; i<recordList.size(); i++)
			{
				String node = (String)recordList.elementAt(i);
				if(node == null)
					out.writeUTF("null");
				else
					out.writeUTF(node);
			}
			out.flush();
			out.close();
			fileConnection.close();
		} catch (Exception e)
		{
			throw new RecordStoreException();
		}
	}
	
	private boolean readFile(String recordStoreName)
		throws RecordStoreNotOpenException, RecordStoreException
	{
		try{
			String fileName = getRMSPath(recordStoreName, -1);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
			{
				DataInputStream din = fileConnection.openDataInputStream();
				RecordStore store = new RecordStore(recordStoreName, authmode, writable);
				authmode = din.readInt();
				writable = din.readBoolean();
				lastModified = din.readLong();
				version = din.readInt();
				int size = din.readInt();
				recordList.removeAllElements();
				for(int i=0; i<size; i++)
				{
					String node = din.readUTF();
					if(node!=null && equals("null"))
						node = null;
					recordList.addElement(node);
				}
				fileConnection.close();
				return true;
			}
			fileConnection.close();
			return false;
		} catch (Exception e)
		{
			throw new RecordStoreException();
		}
	}
	
	private void writeSingleRecord(SingleRecord singleRecord)
		throws RecordStoreNotOpenException, RecordStoreException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		try{
			String fileName = getRMSPath(name, singleRecord.recordid);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
				fileConnection.delete();
			
			fileConnection.create();
			DataOutputStream out = fileConnection.openDataOutputStream();
			singleRecord.write(out);
			out.flush();
			out.close();
			fileConnection.close();
		} catch (Exception e)
		{
			throw new RecordStoreException();
		}
	}
	
	private SingleRecord readSingleRecord(int id)
		throws RecordStoreNotOpenException, RecordStoreException
	{
		SingleRecord singleRecord = new SingleRecord();
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		
		try{
			String fileName = getRMSPath(name, id);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
			{
				DataInputStream in = fileConnection.openDataInputStream();
				singleRecord.read(in);
				fileConnection.close();
			}
			else
			{
				fileConnection.close();
				return null;
			}
		}catch (Exception e) {
		}
		
		return singleRecord;
	}
	
	private void deleteSingleRecord(int id)
		throws RecordStoreNotOpenException, RecordStoreException
	{
		if(!isOpen)
			throw new RecordStoreNotOpenException();
		try{
			String fileName = getRMSPath(name, id);
			FileConnection fileConnection = (FileConnection)Connector.open(fileName);
			if(fileConnection.exists())
				fileConnection.delete();
			fileConnection.close();
		} catch (Exception e)
		{
			throw new RecordStoreException();
		}
	}
	
}
