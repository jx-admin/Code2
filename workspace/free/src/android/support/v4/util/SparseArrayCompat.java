package android.support.v4.util;

public class SparseArrayCompat<E>
{
  private static final Object DELETED = new Object();
  private boolean mGarbage = false;
  private int[] mKeys;
  private int mSize;
  private Object[] mValues;

  public SparseArrayCompat()
  {
    this(10);
  }

  public SparseArrayCompat(int paramInt)
  {
    int i = idealIntArraySize(paramInt);
    this.mKeys = new int[i];
    this.mValues = new Object[i];
    this.mSize = 0;
  }

  private static int binarySearch(int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 + paramInt2;
    int j = paramInt1 - 1;
    while (i - j > 1)
    {
      int k = (i + j) / 2;
      if (paramArrayOfInt[k] < paramInt3)
        j = k;
      i = k;
    }
    if (i == paramInt1 + paramInt2)
      i = 0xFFFFFFFF ^ paramInt1 + paramInt2;
    while (true)
    {
      return i;
      if (paramArrayOfInt[i] == paramInt3)
        continue;
      i ^= -1;
    }
  }

  private void gc()
  {
    int i = this.mSize;
    int j = 0;
    int[] arrayOfInt = this.mKeys;
    Object[] arrayOfObject = this.mValues;
    for (int k = 0; k < i; ++k)
    {
      Object localObject = arrayOfObject[k];
      if (localObject == DELETED)
        continue;
      if (k != j)
      {
        arrayOfInt[j] = arrayOfInt[k];
        arrayOfObject[j] = localObject;
      }
      ++j;
    }
    this.mGarbage = false;
    this.mSize = j;
  }

  static int idealByteArraySize(int paramInt)
  {
    for (int i = 4; ; ++i)
    {
      if (i < 32)
      {
        if (paramInt > -12 + (1 << i))
          continue;
        paramInt = -12 + (1 << i);
      }
      return paramInt;
    }
  }

  static int idealIntArraySize(int paramInt)
  {
    return idealByteArraySize(paramInt * 4) / 4;
  }

  public void append(int paramInt, E paramE)
  {
    if ((this.mSize != 0) && (paramInt <= this.mKeys[(-1 + this.mSize)]))
      put(paramInt, paramE);
    while (true)
    {
      return;
      if ((this.mGarbage) && (this.mSize >= this.mKeys.length))
        gc();
      int i = this.mSize;
      if (i >= this.mKeys.length)
      {
        int j = idealIntArraySize(i + 1);
        int[] arrayOfInt = new int[j];
        Object[] arrayOfObject = new Object[j];
        System.arraycopy(this.mKeys, 0, arrayOfInt, 0, this.mKeys.length);
        System.arraycopy(this.mValues, 0, arrayOfObject, 0, this.mValues.length);
        this.mKeys = arrayOfInt;
        this.mValues = arrayOfObject;
      }
      this.mKeys[i] = paramInt;
      this.mValues[i] = paramE;
      this.mSize = (i + 1);
    }
  }

  public void clear()
  {
    int i = this.mSize;
    Object[] arrayOfObject = this.mValues;
    for (int j = 0; j < i; ++j)
      arrayOfObject[j] = null;
    this.mSize = 0;
    this.mGarbage = false;
  }

  public void delete(int paramInt)
  {
    int i = binarySearch(this.mKeys, 0, this.mSize, paramInt);
    if ((i < 0) || (this.mValues[i] == DELETED))
      return;
    this.mValues[i] = DELETED;
    this.mGarbage = true;
  }

  public E get(int paramInt)
  {
    return get(paramInt, null);
  }

  public E get(int paramInt, E paramE)
  {
    int i = binarySearch(this.mKeys, 0, this.mSize, paramInt);
    if ((i < 0) || (this.mValues[i] == DELETED));
    while (true)
    {
      return paramE;
      paramE = this.mValues[i];
    }
  }

  public int indexOfKey(int paramInt)
  {
    if (this.mGarbage)
      gc();
    return binarySearch(this.mKeys, 0, this.mSize, paramInt);
  }

  public int indexOfValue(E paramE)
  {
    if (this.mGarbage)
      gc();
    int i = 0;
    if (i < this.mSize)
      label13: if (this.mValues[i] != paramE);
    while (true)
    {
      return i;
      ++i;
      break label13:
      i = -1;
    }
  }

  public int keyAt(int paramInt)
  {
    if (this.mGarbage)
      gc();
    return this.mKeys[paramInt];
  }

  public void put(int paramInt, E paramE)
  {
    int i = binarySearch(this.mKeys, 0, this.mSize, paramInt);
    if (i >= 0)
      this.mValues[i] = paramE;
    while (true)
    {
      return;
      int j = i ^ 0xFFFFFFFF;
      if ((j < this.mSize) && (this.mValues[j] == DELETED))
      {
        this.mKeys[j] = paramInt;
        this.mValues[j] = paramE;
      }
      if ((this.mGarbage) && (this.mSize >= this.mKeys.length))
      {
        gc();
        j = 0xFFFFFFFF ^ binarySearch(this.mKeys, 0, this.mSize, paramInt);
      }
      if (this.mSize >= this.mKeys.length)
      {
        int k = idealIntArraySize(1 + this.mSize);
        int[] arrayOfInt = new int[k];
        Object[] arrayOfObject = new Object[k];
        System.arraycopy(this.mKeys, 0, arrayOfInt, 0, this.mKeys.length);
        System.arraycopy(this.mValues, 0, arrayOfObject, 0, this.mValues.length);
        this.mKeys = arrayOfInt;
        this.mValues = arrayOfObject;
      }
      if (this.mSize - j != 0)
      {
        System.arraycopy(this.mKeys, j, this.mKeys, j + 1, this.mSize - j);
        System.arraycopy(this.mValues, j, this.mValues, j + 1, this.mSize - j);
      }
      this.mKeys[j] = paramInt;
      this.mValues[j] = paramE;
      this.mSize = (1 + this.mSize);
    }
  }

  public void remove(int paramInt)
  {
    delete(paramInt);
  }

  public void removeAt(int paramInt)
  {
    if (this.mValues[paramInt] == DELETED)
      return;
    this.mValues[paramInt] = DELETED;
    this.mGarbage = true;
  }

  public void removeAtRange(int paramInt1, int paramInt2)
  {
    int i = Math.min(this.mSize, paramInt1 + paramInt2);
    for (int j = paramInt1; j < i; ++j)
      removeAt(j);
  }

  public void setValueAt(int paramInt, E paramE)
  {
    if (this.mGarbage)
      gc();
    this.mValues[paramInt] = paramE;
  }

  public int size()
  {
    if (this.mGarbage)
      gc();
    return this.mSize;
  }

  public E valueAt(int paramInt)
  {
    if (this.mGarbage)
      gc();
    return this.mValues[paramInt];
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.SparseArrayCompat
 * JD-Core Version:    0.5.4
 */