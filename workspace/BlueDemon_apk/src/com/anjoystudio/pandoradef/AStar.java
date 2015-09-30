package com.anjoystudio.pandoradef;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Vector;

public class AStar
  implements Serializable
{
  private final byte G_OFFSET = 1;
  private byte canMoveIndex = 0;
  Vector<MGE_Node> closeNode = new Vector();
  private int cols = 0;
  private int destinationCol;
  private int destinationRow;
  private byte[][] map;
  Vector<MGE_Node> openNode = new Vector();
  private int rows = 0;
  private byte tileSize = 1;

  private MGE_Node checkOpen(int paramInt1, int paramInt2)
  {
    for (int i = 0; ; ++i)
    {
      if (i >= this.openNode.size());
      for (MGE_Node localMGE_Node = null; ; localMGE_Node = (MGE_Node)this.openNode.elementAt(i))
      {
        return localMGE_Node;
        if ((((MGE_Node)this.openNode.elementAt(i)).row != paramInt1) || (((MGE_Node)this.openNode.elementAt(i)).col != paramInt2))
          break;
      }
    }
  }

  private void creatSeccessionNode(MGE_Node paramMGE_Node, int paramInt1, int paramInt2)
  {
    int i = 1 + paramMGE_Node.g;
    if (!isInClose(paramInt1, paramInt2))
    {
      MGE_Node localMGE_Node1 = checkOpen(paramInt1, paramInt2);
      if (localMGE_Node1 == null)
        break label67;
      if (localMGE_Node1.g < i)
      {
        localMGE_Node1.parent = paramMGE_Node;
        localMGE_Node1.g = i;
        localMGE_Node1.f = (i + localMGE_Node1.h);
      }
    }
    while (true)
    {
      return;
      label67: MGE_Node localMGE_Node2 = new MGE_Node();
      localMGE_Node2.parent = paramMGE_Node;
      localMGE_Node2.g = i;
      localMGE_Node2.h = getH(paramInt1, paramInt2);
      localMGE_Node2.f = (localMGE_Node2.g + localMGE_Node2.h);
      localMGE_Node2.row = paramInt1;
      localMGE_Node2.col = paramInt2;
      this.openNode.addElement(localMGE_Node2);
    }
  }

  private MGE_Node getBesetNode()
  {
    MGE_Node localMGE_Node = null;
    int i = 999999999;
    for (int j = 0; ; ++j)
    {
      if (j >= this.openNode.size())
        return localMGE_Node;
      if (((MGE_Node)this.openNode.elementAt(j)).f >= i)
        continue;
      i = ((MGE_Node)this.openNode.elementAt(j)).f;
      localMGE_Node = (MGE_Node)this.openNode.elementAt(j);
    }
  }

  private int getColPosition(int paramInt)
  {
    return paramInt / this.tileSize;
  }

  private int getH(int paramInt1, int paramInt2)
  {
    return Math.abs(this.destinationRow - paramInt1) + Math.abs(this.destinationCol - paramInt2);
  }

  private int getRowPosition(int paramInt)
  {
    return paramInt / this.tileSize;
  }

  private boolean isCanMove(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 >= this.cols));
    for (int i = 0; ; i = 0)
      while (true)
      {
        return i;
        if ((paramInt2 < 0) || (paramInt2 >= this.rows))
          i = 0;
        if (this.map[paramInt1][paramInt2] != this.canMoveIndex)
          break;
        i = 1;
      }
  }

  private boolean isInClose(int paramInt1, int paramInt2)
  {
    for (int i = 0; ; ++i)
    {
      if (i >= this.closeNode.size());
      for (int j = 0; ; j = 1)
      {
        return j;
        if ((((MGE_Node)this.closeNode.elementAt(i)).row != paramInt1) || (((MGE_Node)this.closeNode.elementAt(i)).col != paramInt2))
          break;
      }
    }
  }

  private void seachSeccessionNode(MGE_Node paramMGE_Node)
  {
    int i = paramMGE_Node.row;
    int j = paramMGE_Node.col - 1;
    if (isCanMove(i, j))
      creatSeccessionNode(paramMGE_Node, i, j);
    int k = paramMGE_Node.row;
    int l = 1 + paramMGE_Node.col;
    if (isCanMove(k, l))
      creatSeccessionNode(paramMGE_Node, k, l);
    int i1 = paramMGE_Node.row - 1;
    int i2 = paramMGE_Node.col;
    if (isCanMove(i1, i2))
      creatSeccessionNode(paramMGE_Node, i1, i2);
    int i3 = 1 + paramMGE_Node.row;
    int i4 = paramMGE_Node.col;
    if (isCanMove(i3, i4))
      creatSeccessionNode(paramMGE_Node, i3, i4);
    this.closeNode.addElement(paramMGE_Node);
    for (int i5 = 0; ; ++i5)
    {
      if (i5 >= this.openNode.size());
      while (true)
      {
        return;
        if ((((MGE_Node)this.openNode.elementAt(i5)).row != paramMGE_Node.row) || (((MGE_Node)this.openNode.elementAt(i5)).col != paramMGE_Node.col))
          break;
        this.openNode.removeElementAt(i5);
      }
    }
  }

  public int[][] getPath(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Object localObject = null;
    ((int[][])null);
    Vector localVector = new Vector();
    this.destinationRow = getRowPosition(paramInt4);
    this.destinationCol = getColPosition(paramInt3);
    MGE_Node localMGE_Node1 = new MGE_Node();
    localMGE_Node1.row = getRowPosition(paramInt2);
    localMGE_Node1.col = getColPosition(paramInt1);
    localMGE_Node1.g = 0;
    localMGE_Node1.h = getH(localMGE_Node1.row, localMGE_Node1.col);
    localMGE_Node1.f = (localMGE_Node1.g + localMGE_Node1.h);
    this.openNode.add(localMGE_Node1);
    while (true)
    {
      MGE_Node localMGE_Node2 = getBesetNode();
      if (localMGE_Node2 == null)
        label127: return localObject;
      if ((localMGE_Node2.row == getRowPosition(paramInt4)) && (localMGE_Node2.col == getColPosition(paramInt3)))
      {
        MGE_Node localMGE_Node3 = localMGE_Node2;
        int[][] arrayOfInt1;
        if (localMGE_Node3.parent == null)
        {
          int i = 1 + localVector.size();
          int[] arrayOfInt = new int[2];
          arrayOfInt[0] = i;
          arrayOfInt[1] = 2;
          arrayOfInt1 = (int[][])Array.newInstance(Integer.TYPE, arrayOfInt);
          arrayOfInt1[localVector.size()][0] = paramInt2;
          arrayOfInt1[localVector.size()][1] = paramInt1;
        }
        for (int j = 0; ; ++j)
        {
          if (j >= localVector.size())
          {
            localObject = arrayOfInt1;
            break label127:
            localVector.add(localMGE_Node3);
            localMGE_Node3 = localMGE_Node3.parent;
          }
          arrayOfInt1[j][0] = ((MGE_Node)localVector.elementAt(j)).row;
          arrayOfInt1[j][1] = ((MGE_Node)localVector.elementAt(j)).col;
        }
      }
      seachSeccessionNode(localMGE_Node2);
    }
  }

  public void setMap(byte[][] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.map = paramArrayOfByte;
    this.rows = paramInt1;
    this.cols = paramInt2;
    this.closeNode.removeAllElements();
    this.openNode.removeAllElements();
  }
}

/* Location:           C:\Users\junxu.wang\Documents\mysvn\sample\utils\apk_tools\classes_dex2jar.jar
 * Qualified Name:     com.anjoystudio.pandoradef.AStar
 * JD-Core Version:    0.5.4
 */