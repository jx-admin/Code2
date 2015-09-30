/**
 * Vector主要用来保存各种类型的对象（包括相同类型和不同类型的对象）。
 * 但是在一些情况下使用会给程序带来性能上的影响。这主要是由Vector类的两个特点所决定的。
 * 第一，Vector提供了线程的安全保护功能。即使Vector类中的许多方法同步。
 * 但是如果你已经确认你的应用程序是单线程，这些方法的同步就完全不必要了。
 * 第二，在Vector查找存储的各种对象时，常常要花很多的时间进行类型的匹配。
 * 而当这些对象都是同一类型时，这些匹配就完全不必要了。
 * 因此，有必要设计一个单线程的，保存特定类型对象的类或集合来替代Vector类
 */
package com.ultrapower.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * @author VictorZheng
 *
 */
public class StringVector 
{
	// 这儿的transient标示这个属性不需要自动序列化
	private transient String[] data; 
	private int count; 
	public int size()
	{
		return data.length;//count;
	}
	public StringVector() 
	{ 
	    // default size is 10
		this(10);   
	}
	public StringVector(int initialSize) 
	{ 
		count=initialSize;
		data = new String[initialSize]; 
	} 
	public void add(String str) 
	{ 
		//	 ignore null strings 
		if(str == null) { return; } 
		ensureCapacity(count + 1); 
		data[count++] = str; 
	} 

	private void ensureCapacity(int minCapacity) 
	{ 
		int oldCapacity = data.length; 
		if (minCapacity > oldCapacity) 
		{ 
			String oldData[] = data; 
			int newCapacity = oldCapacity * 2; 
			data = new String[newCapacity]; 
			System.arraycopy(oldData, 0, data, 0, count); 
		} 
	} 
	public void remove(String str) 
	{ 
		if(str == null) 
		{
			return; // ignore null str   
		}
		for(int i = 0; i < count; i++) 
		{ 
			//	 check for a match 
			if(data[i].equals(str)) 
			{ 
				System.arraycopy(data,i+1,data,i,count-1); // copy data 
				//	 allow previously valid array element be gc'd 
				data[--count] = null; 
				return; 
			} 
		} 
	}
	
	public final String getStringAt(int index) 
	{ 
		if(index < 0) 
		{ return null; } 
		else if(index > count) 
		{ 
			return null; // index is > # strings 
		} 
		else 
		{ 
			return data[index]; // index is good  
		}
	}
	
	public synchronized void writeObject(java.io.DataOutputStream s) 
	throws java.io.IOException  
	{  
		//	 Write out array length 
		s.writeInt(count);  
		//	 Write out all elements in the proper order.   
		for (int i=0; i<count; i++) 
			s.writeUTF(data[i]);  
	} 
	
	public synchronized void readObject(java.io.DataInputStream s) 
	throws java.io.IOException, ClassNotFoundException   
	{
		System.out.println("Enter readObject");
		//	 Read in array length and allocate array   
		int arrayLength = s.readInt(); 
		System.out.println("StringVector count=" + arrayLength);
		data = new String[arrayLength];
		// 同步data的大小
		count = arrayLength;
		//	 Read in all elements in the proper order.  
		for (int i=0; i<arrayLength; i++) 
		{
			data[i] = s.readUTF();
			System.out.println("读入:" + data[i]);
		} 
	}
	
	
	public byte[] serialize()
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);

		try
		{
			writeObject(dos);
			baos.close();
			dos.close();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
		}
		finally
		{
		}

		return baos.toByteArray();
	}
	
	public static StringVector deserialize(byte[] data) {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		DataInputStream dis = new DataInputStream(bais);
		StringVector sv = new StringVector();
		
		try
		{
			sv.readObject(dis);
	
			bais.close();
			dis.close();
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			sv = null;
		}
		finally
		{
		}
		return sv;
	}
}
