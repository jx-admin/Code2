package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

class AccessibilityNodeProviderCompatJellyBean
{
  public static Object newAccessibilityNodeProviderBridge(AccessibilityNodeInfoBridge paramAccessibilityNodeInfoBridge)
  {
    return new AccessibilityNodeProvider(paramAccessibilityNodeInfoBridge)
    {
      public AccessibilityNodeInfo createAccessibilityNodeInfo(int paramInt)
      {
        return (AccessibilityNodeInfo)this.val$bridge.createAccessibilityNodeInfo(paramInt);
      }

      public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String paramString, int paramInt)
      {
        return this.val$bridge.findAccessibilityNodeInfosByText(paramString, paramInt);
      }

      public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle)
      {
        return this.val$bridge.performAction(paramInt1, paramInt2, paramBundle);
      }
    };
  }

  static abstract interface AccessibilityNodeInfoBridge
  {
    public abstract Object createAccessibilityNodeInfo(int paramInt);

    public abstract List<Object> findAccessibilityNodeInfosByText(String paramString, int paramInt);

    public abstract boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityNodeProviderCompatJellyBean
 * JD-Core Version:    0.5.4
 */