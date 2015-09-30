package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState
  implements Parcelable
{
  public static final Parcelable.Creator<FragmentState> CREATOR = new FragmentState.1();
  final Bundle mArguments;
  final String mClassName;
  final int mContainerId;
  final boolean mDetached;
  final int mFragmentId;
  final boolean mFromLayout;
  final int mIndex;
  Fragment mInstance;
  final boolean mRetainInstance;
  Bundle mSavedFragmentState;
  final String mTag;

  public FragmentState(Parcel paramParcel)
  {
    this.mClassName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    int j;
    label31: int k;
    if (paramParcel.readInt() != 0)
    {
      j = i;
      this.mFromLayout = j;
      this.mFragmentId = paramParcel.readInt();
      this.mContainerId = paramParcel.readInt();
      this.mTag = paramParcel.readString();
      if (paramParcel.readInt() == 0)
        break label110;
      k = i;
      label70: this.mRetainInstance = k;
      if (paramParcel.readInt() == 0)
        break label116;
    }
    while (true)
    {
      this.mDetached = i;
      this.mArguments = paramParcel.readBundle();
      this.mSavedFragmentState = paramParcel.readBundle();
      return;
      j = 0;
      break label31:
      label110: k = 0;
      break label70:
      label116: i = 0;
    }
  }

  public FragmentState(Fragment paramFragment)
  {
    this.mClassName = paramFragment.getClass().getName();
    this.mIndex = paramFragment.mIndex;
    this.mFromLayout = paramFragment.mFromLayout;
    this.mFragmentId = paramFragment.mFragmentId;
    this.mContainerId = paramFragment.mContainerId;
    this.mTag = paramFragment.mTag;
    this.mRetainInstance = paramFragment.mRetainInstance;
    this.mDetached = paramFragment.mDetached;
    this.mArguments = paramFragment.mArguments;
  }

  public int describeContents()
  {
    return 0;
  }

  public Fragment instantiate(FragmentActivity paramFragmentActivity, Fragment paramFragment)
  {
    if (this.mInstance != null);
    for (Fragment localFragment = this.mInstance; ; localFragment = this.mInstance)
    {
      return localFragment;
      if (this.mArguments != null)
        this.mArguments.setClassLoader(paramFragmentActivity.getClassLoader());
      this.mInstance = Fragment.instantiate(paramFragmentActivity, this.mClassName, this.mArguments);
      if (this.mSavedFragmentState != null)
      {
        this.mSavedFragmentState.setClassLoader(paramFragmentActivity.getClassLoader());
        this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
      }
      this.mInstance.setIndex(this.mIndex, paramFragment);
      this.mInstance.mFromLayout = this.mFromLayout;
      this.mInstance.mRestored = true;
      this.mInstance.mFragmentId = this.mFragmentId;
      this.mInstance.mContainerId = this.mContainerId;
      this.mInstance.mTag = this.mTag;
      this.mInstance.mRetainInstance = this.mRetainInstance;
      this.mInstance.mDetached = this.mDetached;
      this.mInstance.mFragmentManager = paramFragmentActivity.mFragments;
      if (!FragmentManagerImpl.DEBUG)
        continue;
      Log.v("FragmentManager", "Instantiated fragment " + this.mInstance);
    }
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    paramParcel.writeString(this.mClassName);
    paramParcel.writeInt(this.mIndex);
    int j;
    label28: int k;
    if (this.mFromLayout)
    {
      j = i;
      paramParcel.writeInt(j);
      paramParcel.writeInt(this.mFragmentId);
      paramParcel.writeInt(this.mContainerId);
      paramParcel.writeString(this.mTag);
      if (!this.mRetainInstance)
        break label109;
      k = i;
      label68: paramParcel.writeInt(k);
      if (!this.mDetached)
        break label115;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      paramParcel.writeBundle(this.mArguments);
      paramParcel.writeBundle(this.mSavedFragmentState);
      return;
      j = 0;
      break label28:
      label109: k = 0;
      break label68:
      label115: i = 0;
    }
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentState
 * JD-Core Version:    0.5.4
 */