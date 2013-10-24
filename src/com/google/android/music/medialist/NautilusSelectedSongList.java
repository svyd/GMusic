package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import com.google.android.music.store.MusicContent.XAudio;

public class NautilusSelectedSongList extends NautilusSongList
{
  private final String[] mMetajamIds;

  public NautilusSelectedSongList(String[] paramArrayOfString)
  {
    this.mMetajamIds = paramArrayOfString;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = encodeArg(this.mMetajamIds);
    arrayOfString[0] = str;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.XAudio.getNautilusSelectedAudioUri(this.mMetajamIds);
  }

  public String getNautilusId()
  {
    return null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.NautilusSelectedSongList
 * JD-Core Version:    0.6.2
 */