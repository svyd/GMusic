package com.google.android.music.store;

import android.content.Context;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DbUtils;

class Filters
{
  static final String[] FILTERS = arrayOfString;

  static
  {
    String[] arrayOfString = new String[6];
    arrayOfString[0] = "";
    arrayOfString[1] = "LocalCopyType IN (200,300)";
    arrayOfString[2] = "LocalCopyType IN (100,200,300)";
    arrayOfString[3] = "Domain=0";
    arrayOfString[4] = "+Domain=0 AND LocalCopyType IN (200,300)";
    arrayOfString[5] = "+Domain=0 AND LocalCopyType IN (100,200,300)";
  }

  static StringBuilder addFilteringCondition(StringBuilder paramStringBuilder, int paramInt)
  {
    String str = FILTERS[paramInt];
    return DbUtils.addAndCondition(paramStringBuilder, str);
  }

  static String appendUserAllFilter(boolean paramBoolean)
  {
    if (paramBoolean);
    for (String str = "+Domain=0"; ; str = "Domain=0")
      return str;
  }

  static boolean doesExcludeOnlineMusic(int paramInt)
  {
    switch (paramInt)
    {
    default:
      String str = "Unknown filter value: " + paramInt;
      throw new IllegalArgumentException(str);
    case 0:
    case 3:
    case 1:
    case 2:
    case 4:
    case 5:
    }
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  static int getMusicFilterIndex(Context paramContext, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    int j = 1;
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, localObject1);
    while (true)
    {
      int n;
      try
      {
        boolean bool = localMusicPreferences.isCachedStreamingMusicEnabled();
        if (paramBoolean1)
        {
          int k = localMusicPreferences.getDisplayOptions();
          if (k == 1);
        }
        else
        {
          m = 1;
          MusicPreferences.releaseMusicPreferences(localObject1);
          if (paramBoolean2)
            continue;
          n = 1;
          if (m != 0)
            break label121;
          if (!bool)
            break label107;
          if (n == 0)
            break label102;
          return i;
        }
        int m = 0;
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(localObject1);
      }
      continue;
      label102: i = 2;
      continue;
      label107: if (n != 0)
        j = 4;
      i = j;
      continue;
      label121: if (n != 0)
        i = 3;
    }
  }

  static int setExternalFiltering(int paramInt, boolean paramBoolean)
  {
    if (paramBoolean)
      switch (paramInt)
      {
      default:
        String str = "Unknown filter value: " + paramInt;
        throw new IllegalArgumentException(str);
      case 3:
        paramInt = 0;
      case 0:
      case 1:
      case 2:
      case 4:
      case 5:
      }
    while (true)
    {
      return paramInt;
      paramInt = 1;
      continue;
      paramInt = 2;
      continue;
      switch (paramInt)
      {
      case 3:
      case 4:
      case 5:
      default:
        break;
      case 0:
        paramInt = 3;
        break;
      case 1:
        paramInt = 4;
        break;
      case 2:
        paramInt = 5;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.Filters
 * JD-Core Version:    0.6.2
 */