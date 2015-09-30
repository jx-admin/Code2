package javax.microedition.rms;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class SingleRecord
{
	public int recordid = 0;
	public int dataSize = 0;
	public byte[] data = null;
	
	public SingleRecord()
	{}
	
	public SingleRecord(int recordid, int dataSize, byte[] data)
	{
		this.recordid = recordid;
		this.dataSize = dataSize;
		this.data = data;
	}
	
	public boolean read(DataInputStream din)
	{
		try{
			int start = 0, end = 0;
			recordid = din.readInt();
			dataSize = din.readInt();
			data = new byte[dataSize];
			
			while(end < dataSize)
			{
				start = end;
				end += din.read(data, start, dataSize - end);
			}
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public boolean write(DataOutputStream out)
	{
		try{
			int start = 0, end = 0;
			out.writeInt(recordid);
			out.writeInt(dataSize);
			if(dataSize > 0)
				out.write(data);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
}
