package android.support.v4.widget;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

class SearchViewCompatHoneycomb
{
  public static Object newOnQueryTextListener(OnQueryTextListenerCompatBridge paramOnQueryTextListenerCompatBridge)
  {
    return new SearchView.OnQueryTextListener(paramOnQueryTextListenerCompatBridge)
    {
      public boolean onQueryTextChange(String paramString)
      {
        return this.val$listener.onQueryTextChange(paramString);
      }

      public boolean onQueryTextSubmit(String paramString)
      {
        return this.val$listener.onQueryTextSubmit(paramString);
      }
    };
  }

  public static View newSearchView(Context paramContext)
  {
    return new SearchView(paramContext);
  }

  public static void setOnQueryTextListener(Object paramObject1, Object paramObject2)
  {
    ((SearchView)paramObject1).setOnQueryTextListener((SearchView.OnQueryTextListener)paramObject2);
  }

  static abstract interface OnQueryTextListenerCompatBridge
  {
    public abstract boolean onQueryTextChange(String paramString);

    public abstract boolean onQueryTextSubmit(String paramString);
  }
}

/* Location:           C:\Users\junxu.wang\Desktop\goldfree app\apk_tools\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.SearchViewCompatHoneycomb
 * JD-Core Version:    0.5.4
 */