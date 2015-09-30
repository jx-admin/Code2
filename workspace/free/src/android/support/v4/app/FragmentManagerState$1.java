package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class FragmentManagerState$1
  implements Parcelable.Creator<FragmentManagerState>
{
  public FragmentManagerState createFromParcel(Parcel paramParcel)
  {
    return new FragmentManagerState(paramParcel);
  }

  public FragmentManagerState[] newArray(int paramInt)
  {
    return new FragmentManagerState[paramInt];
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentManagerState.1
 * JD-Core Version:    0.5.4
 */