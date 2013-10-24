package android.support.v4.widget;

import android.content.Context;
import android.widget.SearchView;

class SearchViewCompatIcs
{
  public static class MySearchView extends SearchView
  {
    public MySearchView(Context paramContext)
    {
      super();
    }

    public void onActionViewCollapsed()
    {
      setQuery("", false);
      super.onActionViewCollapsed();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.SearchViewCompatIcs
 * JD-Core Version:    0.6.2
 */