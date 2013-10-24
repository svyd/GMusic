package android.support.v4.app;

import android.app.Activity;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

class ShareCompatICS
{
  private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

  public static void configureMenuItem(MenuItem paramMenuItem, Activity paramActivity, Intent paramIntent)
  {
    ActionProvider localActionProvider = paramMenuItem.getActionProvider();
    if (!(localActionProvider instanceof ShareActionProvider));
    for (ShareActionProvider localShareActionProvider = new ShareActionProvider(paramActivity); ; localShareActionProvider = (ShareActionProvider)localActionProvider)
    {
      StringBuilder localStringBuilder = new StringBuilder().append(".sharecompat_");
      String str1 = paramActivity.getClass().getName();
      String str2 = str1;
      localShareActionProvider.setShareHistoryFileName(str2);
      localShareActionProvider.setShareIntent(paramIntent);
      MenuItem localMenuItem = paramMenuItem.setActionProvider(localShareActionProvider);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ShareCompatICS
 * JD-Core Version:    0.6.2
 */