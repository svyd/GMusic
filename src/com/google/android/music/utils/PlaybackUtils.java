package com.google.android.music.utils;

import android.content.Context;
import com.google.android.gsf.Gservices;

public class PlaybackUtils
{
  public static boolean isPlayed(Context paramContext, long paramLong1, long paramLong2)
  {
    boolean bool = false;
    if (paramLong1 > 0L)
    {
      float f1 = (float)paramLong2;
      float f2 = (float)paramLong1 * 0.65F;
      if (f1 < f2)
      {
        long l = Gservices.getLong(paramContext.getContentResolver(), "music_mark_as_played_seconds", 30L) * 1000L;
        if (paramLong2 < l);
      }
      else
      {
        bool = true;
      }
    }
    return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.PlaybackUtils
 * JD-Core Version:    0.6.2
 */