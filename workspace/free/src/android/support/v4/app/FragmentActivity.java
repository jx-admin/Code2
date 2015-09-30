package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FragmentActivity extends Activity
{
  static final String FRAGMENTS_TAG = "android:support:fragments";
  private static final int HONEYCOMB = 11;
  static final int MSG_REALLY_STOPPED = 1;
  static final int MSG_RESUME_PENDING = 2;
  private static final String TAG = "FragmentActivity";
  HashMap<String, LoaderManagerImpl> mAllLoaderManagers;
  boolean mCheckedForLoaderManager;
  final FragmentContainer mContainer = new FragmentContainer()
  {
    public View findViewById(int paramInt)
    {
      return FragmentActivity.this.findViewById(paramInt);
    }
  };
  boolean mCreated;
  final FragmentManagerImpl mFragments = new FragmentManagerImpl();
  final Handler mHandler = new Handler()
  {
    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        super.handleMessage(paramMessage);
      case 1:
      case 2:
      }
      while (true)
      {
        return;
        if (!FragmentActivity.this.mStopped)
          continue;
        FragmentActivity.this.doReallyStop(false);
        continue;
        FragmentActivity.this.onResumeFragments();
        FragmentActivity.this.mFragments.execPendingActions();
      }
    }
  };
  LoaderManagerImpl mLoaderManager;
  boolean mLoadersStarted;
  boolean mOptionsMenuInvalidated;
  boolean mReallyStopped;
  boolean mResumed;
  boolean mRetaining;
  boolean mStopped;

  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView)
  {
    paramPrintWriter.print(paramString);
    if (paramView == null)
      paramPrintWriter.println("null");
    ViewGroup localViewGroup;
    int i;
    do
    {
      do
      {
        return;
        paramPrintWriter.println(viewToString(paramView));
      }
      while (!paramView instanceof ViewGroup);
      localViewGroup = (ViewGroup)paramView;
      i = localViewGroup.getChildCount();
    }
    while (i <= 0);
    String str = paramString + "  ";
    for (int j = 0; ; ++j)
    {
      if (j < i);
      dumpViewHierarchy(str, paramPrintWriter, localViewGroup.getChildAt(j));
    }
  }

  private static String viewToString(View paramView)
  {
    char c1 = 'F';
    char c2 = '.';
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append(paramView.getClass().getName());
    localStringBuilder.append('{');
    localStringBuilder.append(Integer.toHexString(System.identityHashCode(paramView)));
    localStringBuilder.append(' ');
    label98: char c3;
    label108: char c4;
    label126: char c5;
    label143: char c6;
    label161: char c7;
    label179: char c8;
    label197: char c9;
    label215: label236: char c10;
    label253: int i;
    Resources localResources;
    switch (paramView.getVisibility())
    {
    default:
      localStringBuilder.append(c2);
      if (!paramView.isFocusable())
        break label527;
      c3 = c1;
      localStringBuilder.append(c3);
      if (!paramView.isEnabled())
        break label533;
      c4 = 'E';
      localStringBuilder.append(c4);
      if (!paramView.willNotDraw())
        break label539;
      c5 = c2;
      localStringBuilder.append(c5);
      if (!paramView.isHorizontalScrollBarEnabled())
        break label546;
      c6 = 'H';
      localStringBuilder.append(c6);
      if (!paramView.isVerticalScrollBarEnabled())
        break label552;
      c7 = 'V';
      localStringBuilder.append(c7);
      if (!paramView.isClickable())
        break label558;
      c8 = 'C';
      localStringBuilder.append(c8);
      if (!paramView.isLongClickable())
        break label564;
      c9 = 'L';
      localStringBuilder.append(c9);
      localStringBuilder.append(' ');
      if (!paramView.isFocused())
        break label570;
      localStringBuilder.append(c1);
      if (!paramView.isSelected())
        break label575;
      c10 = 'S';
      localStringBuilder.append(c10);
      if (paramView.isPressed())
        c2 = 'P';
      localStringBuilder.append(c2);
      localStringBuilder.append(' ');
      localStringBuilder.append(paramView.getLeft());
      localStringBuilder.append(',');
      localStringBuilder.append(paramView.getTop());
      localStringBuilder.append('-');
      localStringBuilder.append(paramView.getRight());
      localStringBuilder.append(',');
      localStringBuilder.append(paramView.getBottom());
      i = paramView.getId();
      if (i == -1)
        break label485;
      localStringBuilder.append(" #");
      localStringBuilder.append(Integer.toHexString(i));
      localResources = paramView.getResources();
      if ((i == 0) || (localResources == null))
        break label485;
      switch (0xFF000000 & i)
      {
      default:
      case 2130706432:
      case 16777216:
      }
    case 0:
    case 4:
    case 8:
    }
    while (true)
      try
      {
        for (String str1 = localResources.getResourcePackageName(i); ; str1 = "app")
        {
          String str2 = localResources.getResourceTypeName(i);
          String str3 = localResources.getResourceEntryName(i);
          localStringBuilder.append(" ");
          localStringBuilder.append(str1);
          localStringBuilder.append(":");
          localStringBuilder.append(str2);
          localStringBuilder.append("/");
          localStringBuilder.append(str3);
          label485: localStringBuilder.append("}");
          return localStringBuilder.toString();
          localStringBuilder.append('V');
          break label98:
          localStringBuilder.append('I');
          break label98:
          localStringBuilder.append('G');
          break label98:
          label527: c3 = c2;
          break label108:
          label533: c4 = c2;
          break label126:
          label539: c5 = 'D';
          break label143:
          label546: c6 = c2;
          break label161:
          label552: c7 = c2;
          break label179:
          label558: c8 = c2;
          break label197:
          label564: c9 = c2;
          break label215:
          label570: c1 = c2;
          break label236:
          label575: c10 = c2;
          break label253:
        }
        str1 = "android";
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        break label485:
      }
  }

  void doReallyStop(boolean paramBoolean)
  {
    if (this.mReallyStopped)
      return;
    this.mReallyStopped = true;
    this.mRetaining = paramBoolean;
    this.mHandler.removeMessages(1);
    onReallyStop();
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (Build.VERSION.SDK_INT >= 11);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local FragmentActivity ");
    paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
    paramPrintWriter.println(" State:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("mCreated=");
    paramPrintWriter.print(this.mCreated);
    paramPrintWriter.print("mResumed=");
    paramPrintWriter.print(this.mResumed);
    paramPrintWriter.print(" mStopped=");
    paramPrintWriter.print(this.mStopped);
    paramPrintWriter.print(" mReallyStopped=");
    paramPrintWriter.println(this.mReallyStopped);
    paramPrintWriter.print(str);
    paramPrintWriter.print("mLoadersStarted=");
    paramPrintWriter.println(this.mLoadersStarted);
    if (this.mLoaderManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Loader Manager ");
      paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this.mLoaderManager)));
      paramPrintWriter.println(":");
      this.mLoaderManager.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    this.mFragments.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    dumpViewHierarchy(paramString + "  ", paramPrintWriter, getWindow().getDecorView());
  }

  public Object getLastCustomNonConfigurationInstance()
  {
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null);
    for (Object localObject = localNonConfigurationInstances.custom; ; localObject = null)
      return localObject;
  }

  LoaderManagerImpl getLoaderManager(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mAllLoaderManagers == null)
      this.mAllLoaderManagers = new HashMap();
    LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)this.mAllLoaderManagers.get(paramString);
    if (localLoaderManagerImpl == null)
      if (paramBoolean2)
      {
        localLoaderManagerImpl = new LoaderManagerImpl(paramString, this, paramBoolean1);
        this.mAllLoaderManagers.put(paramString, localLoaderManagerImpl);
      }
    while (true)
    {
      return localLoaderManagerImpl;
      localLoaderManagerImpl.updateActivity(this);
    }
  }

  public FragmentManager getSupportFragmentManager()
  {
    return this.mFragments;
  }

  public LoaderManager getSupportLoaderManager()
  {
    if (this.mLoaderManager != null);
    for (LoaderManagerImpl localLoaderManagerImpl = this.mLoaderManager; ; localLoaderManagerImpl = this.mLoaderManager)
    {
      return localLoaderManagerImpl;
      this.mCheckedForLoaderManager = true;
      this.mLoaderManager = getLoaderManager(null, this.mLoadersStarted, true);
    }
  }

  void invalidateSupportFragment(String paramString)
  {
    if (this.mAllLoaderManagers == null)
      return;
    LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)this.mAllLoaderManagers.get(paramString);
    if ((localLoaderManagerImpl == null) || (localLoaderManagerImpl.mRetaining))
      return;
    localLoaderManagerImpl.doDestroy();
    this.mAllLoaderManagers.remove(paramString);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mFragments.noteStateNotSaved();
    int i = paramInt1 >> 16;
    int j;
    if (i != 0)
    {
      j = i - 1;
      if ((this.mFragments.mActive == null) || (j < 0) || (j >= this.mFragments.mActive.size()))
        Log.w("FragmentActivity", "Activity result fragment index out of range: 0x" + Integer.toHexString(paramInt1));
    }
    while (true)
    {
      return;
      Fragment localFragment = (Fragment)this.mFragments.mActive.get(j);
      if (localFragment == null)
        Log.w("FragmentActivity", "Activity result no fragment exists for index: 0x" + Integer.toHexString(paramInt1));
      localFragment.onActivityResult(0xFFFF & paramInt1, paramInt2, paramIntent);
      continue;
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
    }
  }

  public void onAttachFragment(Fragment paramFragment)
  {
  }

  public void onBackPressed()
  {
    if (this.mFragments.popBackStackImmediate())
      return;
    finish();
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mFragments.dispatchConfigurationChanged(paramConfiguration);
  }

  protected void onCreate(Bundle paramBundle)
  {
    ArrayList localArrayList = null;
    this.mFragments.attachActivity(this, this.mContainer, null);
    if (getLayoutInflater().getFactory() == null)
      getLayoutInflater().setFactory(this);
    super.onCreate(paramBundle);
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null)
      this.mAllLoaderManagers = localNonConfigurationInstances.loaders;
    if (paramBundle != null)
    {
      Parcelable localParcelable = paramBundle.getParcelable("android:support:fragments");
      FragmentManagerImpl localFragmentManagerImpl = this.mFragments;
      if (localNonConfigurationInstances != null)
        localArrayList = localNonConfigurationInstances.fragments;
      localFragmentManagerImpl.restoreAllState(localParcelable, localArrayList);
    }
    this.mFragments.dispatchCreate();
  }

  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    boolean bool;
    if (paramInt == 0)
    {
      bool = super.onCreatePanelMenu(paramInt, paramMenu) | this.mFragments.dispatchCreateOptionsMenu(paramMenu, getMenuInflater());
      if (Build.VERSION.SDK_INT < 11);
    }
    while (true)
    {
      return bool;
      bool = true;
      continue;
      bool = super.onCreatePanelMenu(paramInt, paramMenu);
    }
  }

  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    Fragment localFragment = null;
    int i = 0;
    if (!"fragment".equals(paramString));
    for (View localView = super.onCreateView(paramString, paramContext, paramAttributeSet); ; localView = localFragment.mView)
    {
      return localView;
      String str1 = paramAttributeSet.getAttributeValue(null, "class");
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
      if (str1 == null)
        str1 = localTypedArray.getString(0);
      int j = localTypedArray.getResourceId(1, -1);
      String str2 = localTypedArray.getString(2);
      localTypedArray.recycle();
      if (0 != 0)
        i = null.getId();
      if ((i == -1) && (j == -1) && (str2 == null))
        throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str1);
      if (j != -1)
        localFragment = this.mFragments.findFragmentById(j);
      if ((localFragment == null) && (str2 != null))
        localFragment = this.mFragments.findFragmentByTag(str2);
      if ((localFragment == null) && (i != -1))
        localFragment = this.mFragments.findFragmentById(i);
      if (FragmentManagerImpl.DEBUG)
        Log.v("FragmentActivity", "onCreateView: id=0x" + Integer.toHexString(j) + " fname=" + str1 + " existing=" + localFragment);
      int k;
      if (localFragment == null)
      {
        localFragment = Fragment.instantiate(this, str1);
        localFragment.mFromLayout = true;
        if (j != 0)
        {
          k = j;
          label301: localFragment.mFragmentId = k;
          localFragment.mContainerId = i;
          localFragment.mTag = str2;
          localFragment.mInLayout = true;
          localFragment.mFragmentManager = this.mFragments;
          localFragment.onInflate(this, paramAttributeSet, localFragment.mSavedFragmentState);
          this.mFragments.addFragment(localFragment, true);
        }
      }
      while (localFragment.mView == null)
      {
        throw new IllegalStateException("Fragment " + str1 + " did not create a view.");
        k = i;
        break label301:
        if (localFragment.mInLayout)
          throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(j) + ", tag " + str2 + ", or parent id 0x" + Integer.toHexString(i) + " with another fragment for " + str1);
        localFragment.mInLayout = true;
        if (!localFragment.mRetaining)
          localFragment.onInflate(this, paramAttributeSet, localFragment.mSavedFragmentState);
        this.mFragments.moveToState(localFragment);
      }
      if (j != 0)
        localFragment.mView.setId(j);
      if (localFragment.mView.getTag() != null)
        continue;
      localFragment.mView.setTag(str2);
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    doReallyStop(false);
    this.mFragments.dispatchDestroy();
    if (this.mLoaderManager == null)
      return;
    this.mLoaderManager.doDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((Build.VERSION.SDK_INT < 5) && (paramInt == 4) && (paramKeyEvent.getRepeatCount() == 0))
      onBackPressed();
    for (boolean bool = true; ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      return bool;
  }

  public void onLowMemory()
  {
    super.onLowMemory();
    this.mFragments.dispatchLowMemory();
  }

  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    if (super.onMenuItemSelected(paramInt, paramMenuItem));
    for (boolean bool = true; ; bool = this.mFragments.dispatchContextItemSelected(paramMenuItem))
      while (true)
      {
        return bool;
        switch (paramInt)
        {
        default:
          bool = false;
          break;
        case 0:
          bool = this.mFragments.dispatchOptionsItemSelected(paramMenuItem);
        case 6:
        }
      }
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    this.mFragments.noteStateNotSaved();
  }

  public void onPanelClosed(int paramInt, Menu paramMenu)
  {
    switch (paramInt)
    {
    default:
    case 0:
    }
    while (true)
    {
      super.onPanelClosed(paramInt, paramMenu);
      return;
      this.mFragments.dispatchOptionsMenuClosed(paramMenu);
    }
  }

  protected void onPause()
  {
    super.onPause();
    this.mResumed = false;
    if (this.mHandler.hasMessages(2))
    {
      this.mHandler.removeMessages(2);
      onResumeFragments();
    }
    this.mFragments.dispatchPause();
  }

  protected void onPostResume()
  {
    super.onPostResume();
    this.mHandler.removeMessages(2);
    onResumeFragments();
    this.mFragments.execPendingActions();
  }

  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    boolean bool = false;
    if ((paramInt == 0) && (paramMenu != null))
    {
      if (this.mOptionsMenuInvalidated)
      {
        this.mOptionsMenuInvalidated = false;
        paramMenu.clear();
        onCreatePanelMenu(paramInt, paramMenu);
      }
      if ((!((super.onPreparePanel(paramInt, paramView, paramMenu) | this.mFragments.dispatchPrepareOptionsMenu(paramMenu)))) || (!paramMenu.hasVisibleItems()));
    }
    for (bool = true; ; bool = super.onPreparePanel(paramInt, paramView, paramMenu))
      return bool;
  }

  void onReallyStop()
  {
    if (this.mLoadersStarted)
    {
      this.mLoadersStarted = false;
      if (this.mLoaderManager != null)
      {
        if (this.mRetaining)
          break label41;
        this.mLoaderManager.doStop();
      }
    }
    while (true)
    {
      this.mFragments.dispatchReallyStop();
      return;
      label41: this.mLoaderManager.doRetain();
    }
  }

  protected void onResume()
  {
    super.onResume();
    this.mHandler.sendEmptyMessage(2);
    this.mResumed = true;
    this.mFragments.execPendingActions();
  }

  protected void onResumeFragments()
  {
    this.mFragments.dispatchResume();
  }

  public Object onRetainCustomNonConfigurationInstance()
  {
    return null;
  }

  public final Object onRetainNonConfigurationInstance()
  {
    if (this.mStopped)
      doReallyStop(true);
    Object localObject = onRetainCustomNonConfigurationInstance();
    ArrayList localArrayList = this.mFragments.retainNonConfig();
    int i = 0;
    if (this.mAllLoaderManagers != null)
    {
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[this.mAllLoaderManagers.size()];
      this.mAllLoaderManagers.values().toArray(arrayOfLoaderManagerImpl);
      if (arrayOfLoaderManagerImpl != null)
      {
        int j = 0;
        if (j < arrayOfLoaderManagerImpl.length)
        {
          label69: LoaderManagerImpl localLoaderManagerImpl = arrayOfLoaderManagerImpl[j];
          if (localLoaderManagerImpl.mRetaining)
            i = 1;
          while (true)
          {
            ++j;
            break label69:
            localLoaderManagerImpl.doDestroy();
            this.mAllLoaderManagers.remove(localLoaderManagerImpl.mWho);
          }
        }
      }
    }
    NonConfigurationInstances localNonConfigurationInstances;
    if ((localArrayList == null) && (i == 0) && (localObject == null))
      localNonConfigurationInstances = null;
    while (true)
    {
      return localNonConfigurationInstances;
      localNonConfigurationInstances = new NonConfigurationInstances();
      localNonConfigurationInstances.activity = null;
      localNonConfigurationInstances.custom = localObject;
      localNonConfigurationInstances.children = null;
      localNonConfigurationInstances.fragments = localArrayList;
      localNonConfigurationInstances.loaders = this.mAllLoaderManagers;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Parcelable localParcelable = this.mFragments.saveAllState();
    if (localParcelable == null)
      return;
    paramBundle.putParcelable("android:support:fragments", localParcelable);
  }

  protected void onStart()
  {
    super.onStart();
    this.mStopped = false;
    this.mReallyStopped = false;
    this.mHandler.removeMessages(1);
    if (!this.mCreated)
    {
      this.mCreated = true;
      this.mFragments.dispatchActivityCreated();
    }
    this.mFragments.noteStateNotSaved();
    this.mFragments.execPendingActions();
    if (!this.mLoadersStarted)
    {
      this.mLoadersStarted = true;
      if (this.mLoaderManager == null)
        break label162;
      this.mLoaderManager.doStart();
    }
    while (true)
    {
      this.mCheckedForLoaderManager = true;
      this.mFragments.dispatchStart();
      if (this.mAllLoaderManagers == null)
        return;
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[this.mAllLoaderManagers.size()];
      this.mAllLoaderManagers.values().toArray(arrayOfLoaderManagerImpl);
      if (arrayOfLoaderManagerImpl == null)
        return;
      for (int i = 0; ; ++i)
      {
        if (i >= arrayOfLoaderManagerImpl.length)
          return;
        LoaderManagerImpl localLoaderManagerImpl = arrayOfLoaderManagerImpl[i];
        localLoaderManagerImpl.finishRetain();
        localLoaderManagerImpl.doReportStart();
      }
      label162: if (this.mCheckedForLoaderManager)
        continue;
      this.mLoaderManager = getLoaderManager(null, this.mLoadersStarted, false);
      if ((this.mLoaderManager == null) || (this.mLoaderManager.mStarted))
        continue;
      this.mLoaderManager.doStart();
    }
  }

  protected void onStop()
  {
    super.onStop();
    this.mStopped = true;
    this.mHandler.sendEmptyMessage(1);
    this.mFragments.dispatchStop();
  }

  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if ((paramInt != -1) && ((0xFFFF0000 & paramInt) != 0))
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
    super.startActivityForResult(paramIntent, paramInt);
  }

  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
  {
    if (paramInt == -1)
      super.startActivityForResult(paramIntent, -1);
    while (true)
    {
      return;
      if ((0xFFFF0000 & paramInt) != 0)
        throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
      super.startActivityForResult(paramIntent, (1 + paramFragment.mIndex << 16) + (0xFFFF & paramInt));
    }
  }

  public void supportInvalidateOptionsMenu()
  {
    if (Build.VERSION.SDK_INT >= 11)
      ActivityCompatHoneycomb.invalidateOptionsMenu(this);
    while (true)
    {
      return;
      this.mOptionsMenuInvalidated = true;
    }
  }

  static class FragmentTag
  {
    public static final int[] Fragment;
    public static final int Fragment_id = 1;
    public static final int Fragment_name = 0;
    public static final int Fragment_tag = 2;

    static
    {
      int[] arrayOfInt = new int[3];
      arrayOfInt[0] = 16842755;
      arrayOfInt[1] = 16842960;
      arrayOfInt[2] = 16842961;
      Fragment = arrayOfInt;
    }
  }

  static final class NonConfigurationInstances
  {
    Object activity;
    HashMap<String, Object> children;
    Object custom;
    ArrayList<Fragment> fragments;
    HashMap<String, LoaderManagerImpl> loaders;
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentActivity
 * JD-Core Version:    0.5.4
 */