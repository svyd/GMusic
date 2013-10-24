package com.google.android.music.utils;

import android.content.Context;
import com.google.android.music.preferences.MusicPreferences;

public class LabelUtils
{
  public static String getPlaylistPrimaryLabel(Context paramContext, String paramString, int paramInt)
  {
    if (paramInt == 50)
    {
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramString;
      paramString = paramContext.getString(2131230962, arrayOfObject);
    }
    return paramString;
  }

  public static String getPlaylistSecondaryLabel(Context paramContext, MusicPreferences paramMusicPreferences, int paramInt)
  {
    String str;
    if (paramInt == 50)
      if (paramMusicPreferences.isNautilusEnabled())
        str = paramContext.getString(2131230963);
    while (true)
    {
      return str;
      str = paramContext.getString(2131230964);
      continue;
      if ((paramInt == 0) || (paramInt == 1))
        str = paramContext.getString(2131230952);
      else if (paramInt == 71)
        str = paramContext.getString(2131230954);
      else if (paramInt == 70)
        str = paramContext.getString(2131230955);
      else if (paramInt == 100)
        str = paramContext.getString(2131230953);
      else
        str = paramContext.getString(2131230951);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.LabelUtils
 * JD-Core Version:    0.6.2
 */