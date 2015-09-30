package android.support.v4.view;

import android.os.Bundle;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

class AccessibilityDelegateCompatJellyBean
{
  public static Object getAccessibilityNodeProvider(Object paramObject, View paramView)
  {
    return ((View.AccessibilityDelegate)paramObject).getAccessibilityNodeProvider(paramView);
  }

  public static Object newAccessibilityDelegateBridge(AccessibilityDelegateBridgeJellyBean paramAccessibilityDelegateBridgeJellyBean)
  {
    return new View.AccessibilityDelegate(paramAccessibilityDelegateBridgeJellyBean)
    {
      public boolean dispatchPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
      {
        return this.val$bridge.dispatchPopulateAccessibilityEvent(paramView, paramAccessibilityEvent);
      }

      public AccessibilityNodeProvider getAccessibilityNodeProvider(View paramView)
      {
        return (AccessibilityNodeProvider)this.val$bridge.getAccessibilityNodeProvider(paramView);
      }

      public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
      {
        this.val$bridge.onInitializeAccessibilityEvent(paramView, paramAccessibilityEvent);
      }

      public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfo paramAccessibilityNodeInfo)
      {
        this.val$bridge.onInitializeAccessibilityNodeInfo(paramView, paramAccessibilityNodeInfo);
      }

      public void onPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
      {
        this.val$bridge.onPopulateAccessibilityEvent(paramView, paramAccessibilityEvent);
      }

      public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
      {
        return this.val$bridge.onRequestSendAccessibilityEvent(paramViewGroup, paramView, paramAccessibilityEvent);
      }

      public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
      {
        return this.val$bridge.performAccessibilityAction(paramView, paramInt, paramBundle);
      }

      public void sendAccessibilityEvent(View paramView, int paramInt)
      {
        this.val$bridge.sendAccessibilityEvent(paramView, paramInt);
      }

      public void sendAccessibilityEventUnchecked(View paramView, AccessibilityEvent paramAccessibilityEvent)
      {
        this.val$bridge.sendAccessibilityEventUnchecked(paramView, paramAccessibilityEvent);
      }
    };
  }

  public static boolean performAccessibilityAction(Object paramObject, View paramView, int paramInt, Bundle paramBundle)
  {
    return ((View.AccessibilityDelegate)paramObject).performAccessibilityAction(paramView, paramInt, paramBundle);
  }

  public static abstract interface AccessibilityDelegateBridgeJellyBean
  {
    public abstract boolean dispatchPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract Object getAccessibilityNodeProvider(View paramView);

    public abstract void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract void onInitializeAccessibilityNodeInfo(View paramView, Object paramObject);

    public abstract void onPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle);

    public abstract void sendAccessibilityEvent(View paramView, int paramInt);

    public abstract void sendAccessibilityEventUnchecked(View paramView, AccessibilityEvent paramAccessibilityEvent);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.AccessibilityDelegateCompatJellyBean
 * JD-Core Version:    0.5.4
 */