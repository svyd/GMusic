package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.store.MusicContent;

public class AllOnDeviceSongsList extends AllSongsList
{
  public AllOnDeviceSongsList(int paramInt)
  {
    super(paramInt, false);
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = Integer.toString(getSortOrder());
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    Uri.Builder localBuilder1 = super.getContentUri(paramContext).buildUpon();
    String str = MusicContent.PARAM_FILTER_VALUE_LOCAL_KEPT_AND_CACHED;
    Uri.Builder localBuilder2 = localBuilder1.appendQueryParameter("filter", str);
    return localBuilder1.build();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AllOnDeviceSongsList
 * JD-Core Version:    0.6.2
 */