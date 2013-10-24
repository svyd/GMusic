package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

public class GenresExploreActivity extends BaseActivity
{
  public static final Intent buildStartIntent(Context paramContext, String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    if ((TextUtils.isEmpty(paramString1)) || (TextUtils.isEmpty(paramString2)))
      throw new IllegalArgumentException("Genre name and its NautilusId are needed to display its sub genres. ");
    Intent localIntent1 = new Intent(paramContext, GenresExploreActivity.class);
    Bundle localBundle = new Bundle();
    localBundle.putString("nautilusId", paramString1);
    localBundle.putString("name", paramString2);
    localBundle.putInt("level", paramInt2);
    localBundle.putInt("subgenreCount", paramInt1);
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
    GenresExploreTabFragment localGenresExploreTabFragment = new GenresExploreTabFragment();
    localGenresExploreTabFragment.setArguments(localBundle);
    replaceContent(localGenresExploreTabFragment, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenresExploreActivity
 * JD-Core Version:    0.6.2
 */