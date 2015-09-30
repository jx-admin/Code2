package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState
  implements Parcelable
{
  public static final Parcelable.Creator<BackStackState> CREATOR = new BackStackState.1();
  final int mBreadCrumbShortTitleRes;
  final CharSequence mBreadCrumbShortTitleText;
  final int mBreadCrumbTitleRes;
  final CharSequence mBreadCrumbTitleText;
  final int mIndex;
  final String mName;
  final int[] mOps;
  final int mTransition;
  final int mTransitionStyle;

  public BackStackState(Parcel paramParcel)
  {
    this.mOps = paramParcel.createIntArray();
    this.mTransition = paramParcel.readInt();
    this.mTransitionStyle = paramParcel.readInt();
    this.mName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    this.mBreadCrumbTitleRes = paramParcel.readInt();
    this.mBreadCrumbTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    this.mBreadCrumbShortTitleRes = paramParcel.readInt();
    this.mBreadCrumbShortTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }

  public BackStackState(FragmentManagerImpl paramFragmentManagerImpl, BackStackRecord paramBackStackRecord)
  {
    int i = 0;
    for (BackStackRecord.Op localOp1 = paramBackStackRecord.mHead; localOp1 != null; localOp1 = localOp1.next)
    {
      if (localOp1.removed == null)
        continue;
      i += localOp1.removed.size();
    }
    this.mOps = new int[i + 7 * paramBackStackRecord.mNumOp];
    if (!paramBackStackRecord.mAddToBackStack)
      throw new IllegalStateException("Not on back stack");
    BackStackRecord.Op localOp2 = paramBackStackRecord.mHead;
    int j = 0;
    if (localOp2 != null)
    {
      label87: int[] arrayOfInt1 = this.mOps;
      int k = j + 1;
      arrayOfInt1[j] = localOp2.cmd;
      int[] arrayOfInt2 = this.mOps;
      int l = k + 1;
      if (localOp2.fragment != null);
      int i5;
      int i10;
      for (int i1 = localOp2.fragment.mIndex; ; i1 = -1)
      {
        arrayOfInt2[k] = i1;
        int[] arrayOfInt3 = this.mOps;
        int i2 = l + 1;
        arrayOfInt3[l] = localOp2.enterAnim;
        int[] arrayOfInt4 = this.mOps;
        int i3 = i2 + 1;
        arrayOfInt4[i2] = localOp2.exitAnim;
        int[] arrayOfInt5 = this.mOps;
        int i4 = i3 + 1;
        arrayOfInt5[i3] = localOp2.popEnterAnim;
        int[] arrayOfInt6 = this.mOps;
        i5 = i4 + 1;
        arrayOfInt6[i4] = localOp2.popExitAnim;
        if (localOp2.removed == null)
          break label358;
        int i7 = localOp2.removed.size();
        int[] arrayOfInt8 = this.mOps;
        int i8 = i5 + 1;
        arrayOfInt8[i5] = i7;
        int i9 = 0;
        int i11;
        for (i10 = i8; ; i10 = i11)
        {
          if (i9 >= i7)
            break label340;
          int[] arrayOfInt9 = this.mOps;
          i11 = i10 + 1;
          arrayOfInt9[i10] = ((Fragment)localOp2.removed.get(i9)).mIndex;
          ++i9;
        }
      }
      label340: int i6 = i10;
      while (true)
      {
        localOp2 = localOp2.next;
        j = i6;
        break label87:
        label358: int[] arrayOfInt7 = this.mOps;
        i6 = i5 + 1;
        arrayOfInt7[i5] = 0;
      }
    }
    this.mTransition = paramBackStackRecord.mTransition;
    this.mTransitionStyle = paramBackStackRecord.mTransitionStyle;
    this.mName = paramBackStackRecord.mName;
    this.mIndex = paramBackStackRecord.mIndex;
    this.mBreadCrumbTitleRes = paramBackStackRecord.mBreadCrumbTitleRes;
    this.mBreadCrumbTitleText = paramBackStackRecord.mBreadCrumbTitleText;
    this.mBreadCrumbShortTitleRes = paramBackStackRecord.mBreadCrumbShortTitleRes;
    this.mBreadCrumbShortTitleText = paramBackStackRecord.mBreadCrumbShortTitleText;
  }

  public int describeContents()
  {
    return 0;
  }

  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl)
  {
    BackStackRecord localBackStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int i = 0;
    for (int j = 0; i < this.mOps.length; ++j)
    {
      BackStackRecord.Op localOp = new BackStackRecord.Op();
      int[] arrayOfInt1 = this.mOps;
      int k = i + 1;
      localOp.cmd = arrayOfInt1[i];
      if (FragmentManagerImpl.DEBUG)
        Log.v("FragmentManager", "Instantiate " + localBackStackRecord + " op #" + j + " base fragment #" + this.mOps[k]);
      int[] arrayOfInt2 = this.mOps;
      int l = k + 1;
      int i1 = arrayOfInt2[k];
      if (i1 >= 0);
      int i6;
      for (localOp.fragment = ((Fragment)paramFragmentManagerImpl.mActive.get(i1)); ; localOp.fragment = null)
      {
        int[] arrayOfInt3 = this.mOps;
        int i2 = l + 1;
        localOp.enterAnim = arrayOfInt3[l];
        int[] arrayOfInt4 = this.mOps;
        int i3 = i2 + 1;
        localOp.exitAnim = arrayOfInt4[i2];
        int[] arrayOfInt5 = this.mOps;
        int i4 = i3 + 1;
        localOp.popEnterAnim = arrayOfInt5[i3];
        int[] arrayOfInt6 = this.mOps;
        int i5 = i4 + 1;
        localOp.popExitAnim = arrayOfInt6[i4];
        int[] arrayOfInt7 = this.mOps;
        i6 = i5 + 1;
        int i7 = arrayOfInt7[i5];
        if (i7 <= 0)
          break;
        localOp.removed = new ArrayList(i7);
        int i8 = 0;
        while (true)
        {
          if (i8 >= i7)
            break label394;
          if (FragmentManagerImpl.DEBUG)
            Log.v("FragmentManager", "Instantiate " + localBackStackRecord + " set remove fragment #" + this.mOps[i6]);
          ArrayList localArrayList = paramFragmentManagerImpl.mActive;
          int[] arrayOfInt8 = this.mOps;
          int i9 = i6 + 1;
          Fragment localFragment = (Fragment)localArrayList.get(arrayOfInt8[i6]);
          localOp.removed.add(localFragment);
          ++i8;
          i6 = i9;
        }
      }
      label394: i = i6;
      localBackStackRecord.addOp(localOp);
    }
    localBackStackRecord.mTransition = this.mTransition;
    localBackStackRecord.mTransitionStyle = this.mTransitionStyle;
    localBackStackRecord.mName = this.mName;
    localBackStackRecord.mIndex = this.mIndex;
    localBackStackRecord.mAddToBackStack = true;
    localBackStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
    localBackStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
    localBackStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
    localBackStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
    localBackStackRecord.bumpBackStackNesting(1);
    return localBackStackRecord;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeIntArray(this.mOps);
    paramParcel.writeInt(this.mTransition);
    paramParcel.writeInt(this.mTransitionStyle);
    paramParcel.writeString(this.mName);
    paramParcel.writeInt(this.mIndex);
    paramParcel.writeInt(this.mBreadCrumbTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(this.mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, paramParcel, 0);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.BackStackState
 * JD-Core Version:    0.5.4
 */