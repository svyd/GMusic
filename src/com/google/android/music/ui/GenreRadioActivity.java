package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

public class GenreRadioActivity extends BaseActivity
{
  public static Intent buildStartIntent(Context paramContext, String paramString1, String paramString2, String paramString3)
  {
    if ((TextUtils.isEmpty(paramString1)) || (TextUtils.isEmpty(paramString2)))
      throw new IllegalArgumentException("Genre name and its NautilusId are needed to display its sub genres. ");
    Intent localIntent1 = new Intent(paramContext, GenreRadioActivity.class);
    Bundle localBundle = new Bundle();
    localBundle.putString("nautilusId", paramString1);
    localBundle.putString("name", paramString2);
    localBundle.putString("artUrls", paramString3);
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
    GenreRadioStationsFragment localGenreRadioStationsFragment = new GenreRadioStationsFragment();
    localGenreRadioStationsFragment.setArguments(localBundle);
    replaceContent(localGenreRadioStationsFragment, false);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GenreRadioActivity
 * JD-Core Version:    0.6.2
 */