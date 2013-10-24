package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.store.MusicContent.XAudio;

public class NautilusSingleSongList extends NautilusSongList
{
  private final String mNautilusTrackId;
  private final String mTrackTitle;

  public NautilusSingleSongList(String paramString1, String paramString2)
  {
    if (TextUtils.isEmpty(paramString1))
    {
      String str = "Invalid nautilus track id: " + paramString1;
      throw new IllegalArgumentException(str);
    }
    this.mNautilusTrackId = paramString1;
    this.mTrackTitle = paramString2;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[2];
    String str1 = this.mNautilusTrackId;
    arrayOfString[0] = str1;
    String str2 = this.mTrackTitle;
    arrayOfString[1] = str2;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.XAudio.getNautilusAudioUri(this.mNautilusTrackId);
  }

  public String getName(Context paramContext)
  {
    return this.mTrackTitle;
  }

  public String getNautilusId()
  {
    return this.mNautilusTrackId;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusSingleSongList
 * JD-Core Version:    0.6.2
 */