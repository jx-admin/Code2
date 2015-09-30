// Copyright (c) 2005 Sony Ericsson Mobile Communications AB
//
// This software is provided "AS IS," without a warranty of any kind. 
// ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, 
// INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A 
// PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. 
//
// THIS SOFTWARE IS COMPLEMENTARY OF JAYWAY AB (www.jayway.se)

package com.ultrapower.facade;


import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

import com.ultrapower.common.CommandResources;


/**
 * <p>
 * A facade for the RMS functionality, wrapping it in a key-property manner.
 * This facade adds an overhead to the RMS of (numberOfKeys * 2) bytes comprising
 * the lookup table for key-value / rms-value.
 * </p><p>
 * Keys are given as integers, but are masked to an unsigned 16-bit integer in
 * the lookup table. This implies that only keys with values 0 to 65535 are unique.
 * E.g., keys 1 and 65537 are the same, keys 65535 and -1
 * are the same.
 * </p>
 * @author Peter Andersson
 */
public class RmsFacade
{
  /** The record store singleton instance of this midlet */
  protected static RecordStore m_rs = null;
  /** The name of the recordstore */
  protected static final String RS_NAME = 
	  String.valueOf(CommandResources.getChars(CommandResources.TXT_RMS_STORENAME));
  /** Index/Key cache */
  protected static byte[] m_keyCache;
  /** Number of keys in this cache */
  protected static int m_nbrOfKeys;
  /** boolean flag true identifier */
  protected static final byte[] TRUE =
  { (byte) 1 };
  /** boolean flag false identifier */
  protected static final byte[] FALSE =
  { (byte) 0 };
  /** Dummy data for new keys */
  protected static final byte[] DUMMY = new byte[0];

  /** Prevent construction */
  private RmsFacade() {}

  /**
   * Closes and frees resources of the RMS facade
   */
  public static void shutdown()
  {
    try
    {
      m_rs.closeRecordStore();
      m_rs = null;
    }
    catch (Throwable e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Starts up the RMS facade. If the max number of keys specified as an argument is
   * different from the one stored in RMS from a previous invokation, all values
   * are deleted and a new database is created. The bigger the maximum number of keys
   * are, the bigger the overhead in the rms will be - (maxNbrOfKeys * 2) bytes.
   * 
   * @param maxNbrOfKeys	Maximum number of keys, 1 to 65533.
   * @return true on success, false otherwise
   */
  public static boolean init(int maxNbrOfKeys)
  {
    m_nbrOfKeys = maxNbrOfKeys;
    boolean initKeys = false;
    try
    {
      if (getStore().getNumRecords() == 0)
      {
        // First time, create keyindices
        initKeys = true;
      }
      else
      {
        // Get keyindices
        try
        {
          m_keyCache = getStore().getRecord(1);
          if (m_keyCache.length != maxNbrOfKeys * 2)
          {
            // Incorrect length, reinstall keystore
            if (m_rs != null)
            {
              m_rs.closeRecordStore();
            }
            m_rs = null;
            RecordStore.deleteRecordStore(RS_NAME);
            initKeys = true;
          }
        }
        catch (RecordStoreException re)
        {
          initKeys = true;
          if (m_rs != null)
          {
            m_rs.closeRecordStore();
          }
          m_rs = null;
          RecordStore.deleteRecordStore(RS_NAME);
        }
      }

      if (initKeys)
      {
        // Create key indices
        m_keyCache = new byte[maxNbrOfKeys * 2];
        getStore().addRecord(m_keyCache, 0, maxNbrOfKeys);
      }
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      return false;
    }
    return true;
  }
  
  /**
   * Returns the value of a key as a char array.
   * 
   * @param key  The key.
   * @return The char array value of the key.
   */
  public static char[] getChars(int key)
  {
    byte[] b = get(key);
    if (b == null)
    {
      return null;
    }
    // If returned bytearray has uneven length, this cannot be a char array
    if ((b.length & 1) > 0)
    {
      throw new IllegalArgumentException("RmsFacade key " +
          key + " does not point to char array");
    }
    char[] txt = new char[b.length >> 1];
    for (int i = 0; i < txt.length; i++)
    {
      txt[i] = (char)(((b[i << 1] & 0xff) << 8) | (b[(i << 1) + 1] & 0xff));
    }
    return txt;
  }

  /**
   * Sets the value of a key as a char array.
   * 
   * @param key		The key.
   * @param txt		The char array.
   */
  public static void setChars(int key, char[] txt)
  {
    byte[] b = null;
    if (txt != null)
    {
      b = new byte[txt.length << 1];
      for (int i = 0; i < txt.length; i++)
      {
        b[(i << 1)  ] = (byte)((txt[i] & 0xff00) >> 8);
        b[(i << 1)+1] = (byte) (txt[i] & 0x00ff);
      }
    }
    set(key, b);
  }

  /**
   * Returns the value of a key as a boolean.
   * 
   * @param key  The key.
   * @return The boolean value of the key.
   */
  public static boolean getBoolean(int key)
  {
    byte[] b = get(key);
    if (b == null || b.length == 0)
      return false;
    else if (b[0] > 0)
      return true;
    else
      return false;
  }

  /**
   * Sets the value of a key as a boolean.
   * 
   * @param key		The key.
   * @param b		The boolean value.
   */
  public static void setBoolean(int key, boolean b)
  {
    set(key, b ? TRUE : FALSE);
  }

  /**
   * Returns the value of a key as a long.
   * If the data does not match a long, 0 is returned.
   * 
   * @param key  The key.
   * @return The long value of the key.
   */
  public static long getLong(int key)
  {
    byte[] b = get(key);
    if (b == null || b.length != 8)
    {
      return 0;
    }
    else 
    {
      int i =   ((b[0] & 0xff) << 56) |
				((b[1] & 0xff) << 48) |
				((b[2] & 0xff) << 40) |
  				((b[3] & 0xff) << 32) |
  				((b[4] & 0xff) << 24) |
  				((b[5] & 0xff) << 16) |
      			((b[6] & 0xff) << 8)  |
      			 (b[7] & 0xff);
      return i;
    }
  }

  /**
   * Sets the value of a key as a long.
   * 
   * @param key		The key.
   * @param l		The long value.
   */
  public static void setLong(int key, long l)
  {
    byte[] longData = new byte[8];
    longData[0] = (byte)((l & 0xff00000000000000L) >> 58);
    longData[1] = (byte)((l & 0x00ff000000000000L) >> 48);
    longData[2] = (byte)((l & 0x0000ff0000000000L) >> 40);
    longData[3] = (byte)((l & 0x000000ff00000000L) >> 32);
    longData[4] = (byte)((l & 0x00000000ff000000L) >> 24);
    longData[5] = (byte)((l & 0x0000000000ff0000L) >> 16);
    longData[6] = (byte)((l & 0x000000000000ff00L) >> 8);
    longData[7] = (byte) (l & 0x00000000000000ffL);
    set(key, longData);
  }
  
  /**
   * Returns the value of a key as an int.
   * If the data does not match an integer, 0 is returned.
   * 
   * @param key  The key.
   * @return The int value of the key.
   */
  public static int getInt(int key)
  {
    byte[] b = get(key);
    if (b == null || b.length != 4)
    {
      return 0;
    }
    else 
    {
      int i =   ((b[0] & 0xff) << 24) |
      			((b[1] & 0xff) << 16) |
      			((b[2] & 0xff) << 8)  |
      			 (b[3] & 0xff);
      return i;
    }
  }

  /**
   * Sets the value of a key as an int.
   * 
   * @param key		The key.
   * @param i		The int value.
   */
  public static void setInt(int key, int i)
  {
    byte[] intData = new byte[4];
    intData[0] = (byte)((i & 0xff000000) >> 24);
    intData[1] = (byte)((i & 0x00ff0000) >> 16);
    intData[2] = (byte)((i & 0x0000ff00) >> 8);
    intData[3] = (byte) (i & 0x000000ff);
    set(key, intData);
  }

  /**
   * Returns data for a specified key. If key has not been set before, null is
   * returned.
   * 
   * @param key		The key.
   * @return The data or null if no data is set.
   */
  public static byte[] get(int key)
  {
    byte[] data = null;
    try
    {
      data = getStore().getRecord(getKeyIndex(key));
    }
    catch (RecordStoreException e)
    {
      e.printStackTrace();
    }
    return (data == null || data.length == 0) ? null : data;
  }

  /**
   * Sets a key to specified data.
   * 
   * @param key		The key.
   * @param data	The data.
   */
  public static void set(int key, byte[] data)
  {
    try
    {
      if (data == null)
      {
        getStore().setRecord(getKeyIndex(key), DUMMY, 0, 0);
      }
      else
      {
        getStore().setRecord(getKeyIndex(key), data, 0, data.length);
      }
    }
    catch (RecordStoreFullException e)
    {
      e.printStackTrace();
    }
    catch (RecordStoreException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Returns the record store. Opens it if it is not opened.
   * 
   * @return The record store.
   * @throws RecordStoreFullException
   * @throws RecordStoreNotFoundException
   * @throws RecordStoreException
   */
  protected static RecordStore getStore() throws RecordStoreFullException,
      RecordStoreNotFoundException, RecordStoreException
  {
    if (m_rs == null)
    {
      m_rs = RecordStore.openRecordStore(RS_NAME, true);
    }
    return m_rs;
  }

  /**
   * Returns action record store index for specified key.
   * 
   * @param key		The key.
   * @return The corresponding index in recordstore.
   * @throws RecordStoreNotOpenException
   * @throws RecordStoreFullException
   * @throws RecordStoreNotFoundException
   * @throws RecordStoreException
   */
  protected synchronized static int getKeyIndex(int key)
      throws RecordStoreNotOpenException, RecordStoreFullException,
      RecordStoreNotFoundException, RecordStoreException
  {
    key = (key & 0xffff) << 1;
    int keyIndex = (int)(((m_keyCache[key  ] & 0xff) << 8) |
        				  (m_keyCache[key+1] & 0xff));
    if (keyIndex == 0)
    {
      // Create new key index
      keyIndex = getStore().addRecord(DUMMY, 0, 0);
      m_keyCache[key  ] = (byte)((keyIndex & 0xff00) >> 8);
      m_keyCache[key+1] = (byte) (keyIndex & 0x00ff);
      // Store the lookup table with the new index
      getStore().setRecord(1, m_keyCache, 0, m_keyCache.length);
    }
    return keyIndex;
  }
}
