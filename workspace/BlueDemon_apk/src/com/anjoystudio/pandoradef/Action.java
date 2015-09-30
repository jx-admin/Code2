package com.anjoystudio.pandoradef;

import android.R.raw;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import java.io.DataInputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Action
  implements Serializable
{
  public static final byte TRANS_MIRROR = 2;
  public static final byte TRANS_MIRROR_ROT180 = 1;
  public static final byte TRANS_MIRROR_ROT270 = 4;
  public static final byte TRANS_MIRROR_ROT90 = 7;
  public static final byte TRANS_NONE = 0;
  public static final byte TRANS_ROT180 = 3;
  public static final byte TRANS_ROT270 = 6;
  public static final byte TRANS_ROT90 = 5;
  private static final byte modulesCol = 4;
  private static final byte spritCol = 4;
  private int[][] actionArr;
  private int actionCount;
  private int actionFile;
  private int actionIndex;
  private int alpha = 255;
  int attracCount = 4;
  public int[][] attrctCollion;
  public int[][] collion;
  int collionCol = 4;
  private int[][][] framesArr;
  private int framesCount;
  private Bitmap img;
  boolean isVisible;
  Matrix matrix = new Matrix();
  private int[][] modulesArr;
  private int modulesCount;
  Resources res;
  private int sequenceCount;
  private int sequenceIndex;
  private int spritCount;
  int x;
  int y;

  private Action()
  {
  }

  public Action(Resources paramResources, Bitmap paramBitmap, int paramInt)
  {
    this.img = paramBitmap;
    this.actionFile = paramInt;
    loadAction(paramResources, this.actionFile);
    this.isVisible = true;
    this.res = paramResources;
  }

  public Action(Resources paramResources, Bitmap paramBitmap, String paramString)
    throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException
  {
    int i = R.raw.class.getField(paramString).getInt(R.raw.class);
    this.img = paramBitmap;
    this.actionFile = i;
    loadAction(paramResources, this.actionFile);
  }

  public Action(Action paramAction)
  {
    this.isVisible = true;
    this.img = paramAction.img;
    this.actionFile = paramAction.actionFile;
    this.modulesCount = paramAction.modulesCount;
    this.framesCount = paramAction.framesCount;
    this.spritCount = paramAction.spritCount;
    this.actionCount = paramAction.actionCount;
    this.sequenceCount = paramAction.sequenceCount;
    int i = paramAction.modulesArr.length;
    int j = paramAction.modulesArr[0].length;
    int[] arrayOfInt1 = new int[2];
    arrayOfInt1[0] = i;
    arrayOfInt1[1] = j;
    this.modulesArr = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt1));
    int k = 0;
    label140: int i1;
    int i6;
    int i7;
    label223: int i9;
    int i10;
    int i11;
    if (k >= i)
    {
      int l = paramAction.framesArr.length;
      this.framesArr = new int[l][][];
      i1 = 0;
      if (i1 < l)
        break label373;
      int i5 = paramAction.collion.length;
      i6 = paramAction.collion[0].length;
      int[] arrayOfInt2 = new int[2];
      arrayOfInt2[0] = i5;
      arrayOfInt2[1] = i6;
      this.collion = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt2));
      i7 = 0;
      if (i7 < i5)
        break label472;
      int i8 = paramAction.attrctCollion.length;
      i9 = paramAction.attrctCollion[0].length;
      int[] arrayOfInt3 = new int[2];
      arrayOfInt3[0] = i8;
      arrayOfInt3[1] = i9;
      this.attrctCollion = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt3));
      i10 = 0;
      if (i10 < i8)
        break label499;
      i11 = paramAction.actionArr.length;
      this.actionArr = new int[i11][];
    }
    for (int i12 = 0; ; ++i12)
    {
      if (i12 >= i11)
      {
        this.actionIndex = paramAction.actionIndex;
        this.sequenceIndex = paramAction.sequenceIndex;
        this.x = paramAction.x;
        this.y = paramAction.y;
        return;
        System.arraycopy(paramAction.modulesArr[k], 0, this.modulesArr[k], 0, j);
        ++k;
        break label140:
        label373: int i2 = paramAction.framesArr[i1].length;
        this.framesArr[i1] = new int[i2][];
        for (int i3 = i2 - 1; ; --i3)
        {
          if (i3 < 0)
            ++i1;
          int i4 = paramAction.framesArr[i1][i3].length;
          this.framesArr[i1][i3] = new int[i4];
          System.arraycopy(paramAction.framesArr[i1][i3], 0, this.framesArr[i1][i3], 0, i4);
        }
        label472: System.arraycopy(paramAction.collion[i7], 0, this.collion[i7], 0, i6);
        ++i7;
        break label223:
        label499: System.arraycopy(paramAction.attrctCollion[i10], 0, this.attrctCollion[i10], 0, i9);
        ++i10;
      }
      int i13 = paramAction.actionArr[i12].length;
      this.actionArr[i12] = new int[i13];
      System.arraycopy(paramAction.actionArr[i12], 0, this.actionArr[i12], 0, i13);
    }
  }

  public static boolean collidesWith(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    if ((paramInt1 + paramInt3 < paramInt5) || (paramInt1 > paramInt5 + paramInt7) || (paramInt2 + paramInt4 < paramInt6) || (paramInt2 > paramInt6 + paramInt8));
    for (int i = 0; ; i = 1)
      return i;
  }

  public static Action copy(Action paramAction)
  {
    Action localAction = new Action();
    localAction.img = paramAction.img;
    localAction.actionFile = paramAction.actionFile;
    localAction.modulesCount = paramAction.modulesCount;
    localAction.framesCount = paramAction.framesCount;
    localAction.spritCount = paramAction.spritCount;
    localAction.actionCount = paramAction.actionCount;
    localAction.sequenceCount = paramAction.sequenceCount;
    int i = paramAction.modulesArr.length;
    int j = paramAction.modulesArr[0].length;
    int[] arrayOfInt1 = new int[2];
    arrayOfInt1[0] = i;
    arrayOfInt1[1] = j;
    localAction.modulesArr = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt1));
    int k = 0;
    label111: int i1;
    int i6;
    int i7;
    label194: int i9;
    int i10;
    int i11;
    if (k >= i)
    {
      int l = paramAction.framesArr.length;
      localAction.framesArr = new int[l][][];
      i1 = 0;
      if (i1 < l)
        break label345;
      int i5 = paramAction.collion.length;
      i6 = paramAction.collion[0].length;
      int[] arrayOfInt2 = new int[2];
      arrayOfInt2[0] = i5;
      arrayOfInt2[1] = i6;
      localAction.collion = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt2));
      i7 = 0;
      if (i7 < i5)
        break label444;
      int i8 = paramAction.attrctCollion.length;
      i9 = paramAction.attrctCollion[0].length;
      int[] arrayOfInt3 = new int[2];
      arrayOfInt3[0] = i8;
      arrayOfInt3[1] = i9;
      localAction.attrctCollion = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt3));
      i10 = 0;
      if (i10 < i8)
        break label471;
      i11 = paramAction.actionArr.length;
      localAction.actionArr = new int[i11][];
    }
    for (int i12 = 0; ; ++i12)
    {
      if (i12 >= i11)
      {
        localAction.actionIndex = paramAction.actionIndex;
        localAction.sequenceIndex = paramAction.sequenceIndex;
        localAction.x = paramAction.x;
        localAction.y = paramAction.y;
        return localAction;
        System.arraycopy(paramAction.modulesArr[k], 0, localAction.modulesArr[k], 0, j);
        ++k;
        break label111:
        label345: int i2 = paramAction.framesArr[i1].length;
        localAction.framesArr[i1] = new int[i2][];
        for (int i3 = i2 - 1; ; --i3)
        {
          if (i3 < 0)
            ++i1;
          int i4 = paramAction.framesArr[i1][i3].length;
          localAction.framesArr[i1][i3] = new int[i4];
          System.arraycopy(paramAction.framesArr[i1][i3], 0, localAction.framesArr[i1][i3], 0, i4);
        }
        label444: System.arraycopy(paramAction.collion[i7], 0, localAction.collion[i7], 0, i6);
        ++i7;
        break label194:
        label471: System.arraycopy(paramAction.attrctCollion[i10], 0, localAction.attrctCollion[i10], 0, i9);
        ++i10;
      }
      int i13 = paramAction.actionArr[i12].length;
      localAction.actionArr[i12] = new int[i13];
      System.arraycopy(paramAction.actionArr[i12], 0, localAction.actionArr[i12], 0, i13);
    }
  }

  public boolean attractWith(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.actionArr[this.actionIndex][this.sequenceIndex];
    return collidesWith(this.x + this.attrctCollion[i][0], this.y + this.attrctCollion[i][1], this.attrctCollion[i][2], this.attrctCollion[i][3], paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public boolean attractWith(Action paramAction)
  {
    int i = this.actionArr[this.actionIndex][this.sequenceIndex];
    int j = paramAction.actionArr[this.actionIndex][this.sequenceIndex];
    return collidesWith(this.x + this.attrctCollion[i][0], this.y + this.attrctCollion[i][1], this.attrctCollion[i][2], this.attrctCollion[i][3], paramAction.x + paramAction.collion[j][0], paramAction.y + paramAction.collion[j][1], paramAction.collion[j][2], paramAction.collion[j][3]);
  }

  public boolean collidesWith(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = this.actionArr[this.actionIndex][this.sequenceIndex];
    return collidesWith(this.x + this.collion[i][0], this.y + this.collion[i][1], this.collion[i][2], this.collion[i][3], paramInt1, paramInt2, paramInt3, paramInt4);
  }

  public boolean collidesWith(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Action paramAction)
  {
    int i = paramAction.actionArr[this.actionIndex][this.sequenceIndex];
    return collidesWith(paramInt1, paramInt2, paramInt3, paramInt4, paramAction.x + paramAction.collion[i][0], paramAction.y + paramAction.collion[i][1], paramAction.collion[i][2], paramAction.collion[i][3]);
  }

  public boolean collidesWith(Action paramAction)
  {
    int i = this.actionArr[this.actionIndex][this.sequenceIndex];
    int j = paramAction.actionArr[this.actionIndex][this.sequenceIndex];
    return collidesWith(this.x + this.collion[i][0], this.y + this.collion[i][1], this.collion[i][2], this.collion[i][3], paramAction.x + paramAction.collion[j][0], paramAction.y + paramAction.collion[j][1], paramAction.collion[j][2], paramAction.collion[j][3]);
  }

  public Action copyInstence()
  {
    Action localAction = new Action();
    localAction.img = this.img;
    localAction.actionFile = this.actionFile;
    localAction.modulesCount = this.modulesCount;
    localAction.framesCount = this.framesCount;
    localAction.spritCount = this.spritCount;
    localAction.actionCount = this.actionCount;
    localAction.sequenceCount = this.sequenceCount;
    localAction.modulesArr = this.modulesArr;
    localAction.framesArr = this.framesArr;
    localAction.collion = this.collion;
    localAction.attrctCollion = this.attrctCollion;
    localAction.actionArr = this.actionArr;
    localAction.actionIndex = this.actionIndex;
    localAction.sequenceIndex = this.sequenceIndex;
    localAction.x = this.x;
    localAction.y = this.y;
    return localAction;
  }

  public void draw(Canvas paramCanvas, Paint paramPaint)
  {
    if (!this.isVisible);
    while (true)
    {
      return;
      drawFrame(paramCanvas, paramPaint, this.x, this.y, this.actionArr[this.actionIndex][this.sequenceIndex]);
    }
  }

  public void draw(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2)
  {
    if (!this.isVisible);
    while (true)
    {
      return;
      paramPaint.setAlpha(this.alpha);
      drawFrame(paramCanvas, paramPaint, paramInt1, paramInt2, this.actionArr[this.actionIndex][this.sequenceIndex]);
      paramPaint.setAlpha(255);
    }
  }

  public void drawFrame(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = 0;
    if (i >= this.framesArr[paramInt3].length)
      label3: return;
    if (this.framesArr[paramInt3][i][0] >= 0)
      drawSprit(paramCanvas, paramPaint, paramInt1 + this.framesArr[paramInt3][i][1], paramInt2 + this.framesArr[paramInt3][i][2], this.framesArr[paramInt3][i][0], 0);
    while (true)
    {
      ++i;
      break label3:
      paramCanvas.drawBitmap(getMatrix(this.img, this.modulesArr[this.framesArr[paramInt3][i][0]][0], this.modulesArr[this.framesArr[paramInt3][i][0]][1], this.modulesArr[this.framesArr[paramInt3][i][0]][2], this.modulesArr[this.framesArr[paramInt3][i][0]][3], (byte)this.framesArr[paramInt3][i][3], this.matrix), paramInt1 + this.framesArr[paramInt3][i][1], paramInt2 + this.framesArr[paramInt3][i][2], paramPaint);
    }
  }

  public void drawModel(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2)
  {
    paramCanvas.save();
    paramCanvas.clipRect(paramInt1, paramInt2, paramInt1 + this.img.getWidth(), paramInt2 + this.img.getHeight());
    paramCanvas.drawBitmap(this.img, paramInt1, paramInt2, paramPaint);
    drawRectArr(paramCanvas, paramPaint, 16711680, paramInt1, paramInt2, this.modulesArr);
    paramCanvas.restore();
  }

  public void drawRectArr(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int[][] paramArrayOfInt)
  {
    paramPaint.setColor(0xFF000000 | paramInt1);
    for (int i = 0; ; ++i)
    {
      if (i >= this.modulesArr.length)
        return;
      paramCanvas.drawRect(paramInt2 + paramArrayOfInt[i][0], paramInt3 + paramArrayOfInt[i][1], paramInt2 + paramArrayOfInt[i][0] + paramArrayOfInt[i][2], paramInt3 + paramArrayOfInt[i][1] + paramArrayOfInt[i][3], paramPaint);
    }
  }

  public void drawSprit(Canvas paramCanvas, Paint paramPaint, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    paramCanvas.save();
    paramCanvas.clipRect(paramInt1, paramInt2, paramInt1 + this.modulesArr[paramInt3][2], paramInt2 + this.modulesArr[paramInt3][3]);
    paramCanvas.drawBitmap(this.img, paramInt1 - this.modulesArr[paramInt3][0], paramInt2 - this.modulesArr[paramInt3][1], paramPaint);
    paramCanvas.restore();
  }

  public int getAction()
  {
    return this.actionIndex;
  }

  public int getAlpha()
  {
    return this.alpha;
  }

  public int getAttractHeight()
  {
    return this.attrctCollion[this.actionArr[this.actionIndex][this.sequenceIndex]][3];
  }

  public int getAttractWidth()
  {
    return this.attrctCollion[this.actionArr[this.actionIndex][this.sequenceIndex]][2];
  }

  public int getCollideHeight()
  {
    return this.collion[this.actionArr[this.actionIndex][this.sequenceIndex]][3];
  }

  public int getCollideWidth()
  {
    return this.collion[this.actionArr[this.actionIndex][this.sequenceIndex]][2];
  }

  public int getFrame()
  {
    return this.sequenceIndex;
  }

  public int getFrameId()
  {
    return this.actionArr[this.actionIndex][this.sequenceIndex];
  }

  public Bitmap getMatrix(Bitmap paramBitmap, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte paramByte, Matrix paramMatrix)
  {
    paramMatrix.reset();
    switch (paramByte)
    {
    case 0:
    default:
    case 5:
    case 3:
    case 6:
    case 2:
    case 7:
    case 1:
    case 4:
    }
    while (true)
    {
      return Bitmap.createBitmap(paramBitmap, paramInt1, paramInt2, paramInt3, paramInt4, paramMatrix, false);
      paramMatrix.postRotate(90.0F);
      continue;
      paramMatrix.postRotate(180.0F);
      continue;
      paramMatrix.postRotate(270.0F);
      continue;
      paramMatrix.postScale(-1.0F, 1.0F);
      continue;
      paramMatrix.postScale(-1.0F, 1.0F);
      paramMatrix.postRotate(90.0F);
      continue;
      paramMatrix.postScale(-1.0F, 1.0F);
      paramMatrix.postRotate(180.0F);
      continue;
      paramMatrix.postScale(-1.0F, 1.0F);
      paramMatrix.postRotate(270.0F);
    }
  }

  public int getMaxFrame()
  {
    return this.sequenceCount - 1;
  }

  public int getTatolAction()
  {
    return this.actionArr.length;
  }

  public int getTatolActionFrame()
  {
    return this.sequenceCount;
  }

  public int getTatolFrame()
  {
    return this.framesArr.length;
  }

  public boolean isActionEnd()
  {
    if (this.sequenceIndex >= this.sequenceCount - 1);
    for (int i = 1; ; i = 0)
      return i;
  }

  public boolean isFrame(int paramInt)
  {
    if (paramInt == this.sequenceIndex);
    for (int i = 1; ; i = 0)
      return i;
  }

  public void loadAction(Resources paramResources, int paramInt)
  {
    while (true)
    {
      DataInputStream localDataInputStream;
      int j;
      int i4;
      int i15;
      int i14;
      try
      {
        localDataInputStream = new DataInputStream(paramResources.openRawResource(paramInt));
        this.modulesCount = localDataInputStream.readShort();
        int i = this.modulesCount;
        int[] arrayOfInt1 = new int[2];
        arrayOfInt1[0] = i;
        arrayOfInt1[1] = 4;
        this.modulesArr = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt1));
        j = 0;
        if (j < this.modulesCount)
          break label1100;
        this.framesCount = localDataInputStream.readShort();
        this.framesArr = new int[this.framesCount][][];
        int l = this.framesCount;
        int i1 = this.collionCol;
        int[] arrayOfInt2 = new int[2];
        arrayOfInt2[0] = l;
        arrayOfInt2[1] = i1;
        this.collion = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt2));
        int i2 = this.framesCount;
        int i3 = this.collionCol;
        int[] arrayOfInt3 = new int[2];
        arrayOfInt3[0] = i2;
        arrayOfInt3[1] = i3;
        this.attrctCollion = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt3));
        for (i4 = 0; ; ++i4)
        {
          if (i4 >= this.framesCount)
          {
            this.actionCount = localDataInputStream.readShort();
            this.actionArr = new int[this.actionCount][];
            i15 = 0;
            if (i15 < this.actionCount)
              break label1035;
            localDataInputStream.close();
            break label1099:
            this.modulesArr[j][k] = localDataInputStream.readShort();
            ++k;
            break label1103:
          }
          this.spritCount = localDataInputStream.readShort();
          int[][][] arrayOfInt = this.framesArr;
          int i5 = this.spritCount;
          int[] arrayOfInt4 = new int[2];
          arrayOfInt4[0] = i5;
          arrayOfInt4[1] = 4;
          arrayOfInt[i4] = ((int[][])Array.newInstance(Integer.TYPE, arrayOfInt4));
          int i6 = localDataInputStream.readShort();
          int i7 = localDataInputStream.readShort();
          int i8 = localDataInputStream.readShort();
          int i9 = localDataInputStream.readShort();
          this.collion[i4][0] = i8;
          this.collion[i4][1] = i7;
          this.collion[i4][2] = (i6 - i8);
          this.collion[i4][3] = (i9 - i7);
          int i10 = localDataInputStream.readShort();
          int i11 = localDataInputStream.readShort();
          int i12 = localDataInputStream.readShort();
          int i13 = localDataInputStream.readShort();
          this.attrctCollion[i4][0] = i12;
          this.attrctCollion[i4][1] = i11;
          this.attrctCollion[i4][2] = (i10 - i12);
          this.attrctCollion[i4][3] = (i13 - i11);
          i14 = 0;
          if (i14 < this.spritCount)
            break;
        }
        this.framesArr[i4][i14][0] = localDataInputStream.readShort();
        this.framesArr[i4][i14][1] = localDataInputStream.readShort();
        this.framesArr[i4][i14][2] = localDataInputStream.readShort();
        this.framesArr[i4][i14][3] = localDataInputStream.readShort();
        if (this.framesArr[i4][i14][3] == 5)
        {
          int[] arrayOfInt13 = this.framesArr[i4][i14];
          label1035: label1099: arrayOfInt13[1] -= this.modulesArr[this.framesArr[i4][i14][0]][3];
        }
        else if (this.framesArr[i4][i14][3] == 6)
        {
          int[] arrayOfInt12 = this.framesArr[i4][i14];
          arrayOfInt12[2] -= this.modulesArr[this.framesArr[i4][i14][0]][2];
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
        if (this.framesArr[i4][i14][3] == 3)
        {
          int[] arrayOfInt10 = this.framesArr[i4][i14];
          arrayOfInt10[1] -= this.modulesArr[this.framesArr[i4][i14][0]][2];
          int[] arrayOfInt11 = this.framesArr[i4][i14];
          arrayOfInt11[2] -= this.modulesArr[this.framesArr[i4][i14][0]][3];
        }
        else if (this.framesArr[i4][i14][3] == 2)
        {
          int[] arrayOfInt9 = this.framesArr[i4][i14];
          arrayOfInt9[1] -= this.modulesArr[this.framesArr[i4][i14][0]][2];
        }
        else if (this.framesArr[i4][i14][3] == 7)
        {
          int[] arrayOfInt7 = this.framesArr[i4][i14];
          arrayOfInt7[1] -= this.modulesArr[this.framesArr[i4][i14][0]][3];
          int[] arrayOfInt8 = this.framesArr[i4][i14];
          arrayOfInt8[2] -= this.modulesArr[this.framesArr[i4][i14][0]][2];
        }
        else if (this.framesArr[i4][i14][3] == 3)
        {
          int[] arrayOfInt6 = this.framesArr[i4][i14];
          arrayOfInt6[2] -= this.modulesArr[this.framesArr[i4][i14][0]][2];
        }
        else if (this.framesArr[i4][i14][3] == 6)
        {
          int[] arrayOfInt5 = this.framesArr[i4][i14];
          arrayOfInt5[2] -= this.modulesArr[this.framesArr[i4][i14][0]][2];
          break label1115:
          this.sequenceCount = localDataInputStream.readShort();
          this.actionArr[i15] = new int[this.sequenceCount];
          for (int i16 = 0; ; ++i16)
          {
            if (i16 >= this.sequenceCount)
              ++i15;
            localDataInputStream.readShort();
            this.actionArr[i15][i16] = localDataInputStream.readShort();
          }
          return;
        }
      }
      label1100: int k = 0;
      label1103: if (k < 4)
        continue;
      ++j;
      continue;
      label1115: ++i14;
    }
  }

  public void nextAction()
  {
    this.actionIndex = (1 + this.actionIndex);
    if (this.actionIndex >= this.actionCount)
      this.actionIndex = 0;
    setAction(this.actionIndex);
  }

  public void nextFrame()
  {
    this.sequenceIndex = (1 + this.sequenceIndex);
    if (this.sequenceIndex < this.sequenceCount)
      return;
    this.sequenceIndex = 0;
  }

  public void print2(int[][] paramArrayOfInt)
  {
    int i = 0;
    if (i >= paramArrayOfInt.length)
      return;
    for (int j = 0; ; ++j)
    {
      if (j >= paramArrayOfInt[i].length)
        ++i;
      System.out.print("  " + paramArrayOfInt[i][j]);
    }
  }

  public void print3(int[][][] paramArrayOfInt)
  {
    for (int i = 0; ; ++i)
    {
      if (i >= paramArrayOfInt.length)
        return;
      print2(paramArrayOfInt[i]);
    }
  }

  public void printInfo()
  {
    System.out.println("moduleCount" + this.modulesCount);
    if (this.modulesArr != null)
      print2(this.modulesArr);
    if (this.framesArr != null)
      print3(this.framesArr);
    if (this.actionArr == null)
      return;
    print2(this.actionArr);
  }

  public void setAction(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.actionCount));
    while (true)
    {
      return;
      this.actionIndex = paramInt;
      this.sequenceCount = this.actionArr[this.actionIndex].length;
      this.sequenceIndex = 0;
    }
  }

  public void setActionFile(int paramInt)
  {
    this.actionFile = paramInt;
    loadAction(this.res, this.actionFile);
  }

  public void setAlpha(int paramInt)
  {
    this.alpha = paramInt;
  }

  public void setBitmap(Bitmap paramBitmap)
  {
    this.img = paramBitmap;
  }

  public void setFrame(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.sequenceCount - 1));
    while (true)
    {
      return;
      this.sequenceIndex = paramInt;
    }
  }

  public void setPosition(int paramInt1, int paramInt2)
  {
    this.x = paramInt1;
    this.y = paramInt2;
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.Action
 * JD-Core Version:    0.5.4
 */