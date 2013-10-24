package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SubGenresExploreActivity extends BaseActivity
{
  public static final Intent buildStartIntent(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    if ((paramString1 == null) || (paramString2 == null))
      throw new IllegalArgumentException("Genre name and its NautilusId are needed to display new releases and top charts for sub genres. ");
    Intent localIntent1 = new Intent(paramContext, SubGenresExploreActivity.class);
    Bundle localBundle = new Bundle();
    localBundle.putString("nautilusId", paramString1);
    localBundle.putString("name", paramString2);
    localBundle.putString("parentNautilusId", paramString3);
    Intent localIntent2 = localIntent1.putExtras(localBundle);
    return localIntent1;
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Bundle localBundle = getIntent().getExtras();
    String str = localBundle.getString("name");
    setActionBarTitle(str);
    if (getContent() != null)
      return;
    SubGenresExploreTabFragment localSubGenresExploreTabFragment = new SubGenresExploreTabFragment();
    localSubGenresExploreTabFragment.setArguments(localBundle);
    replaceContent(localSubGenresExploreTabFragment, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SubGenresExploreActivity
 * JD-Core Version:    0.6.2
 */