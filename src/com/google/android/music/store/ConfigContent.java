package com.google.android.music.store;

import android.net.Uri;
import android.provider.BaseColumns;
import com.google.android.music.ui.cardlib.utils.Utils;

public class ConfigContent
  implements BaseColumns
{
  public static final Uri APP_SETTINGS_URI = Uri.withAppendedPath(CONTENT_URI, "app");
  public static final Uri CONTENT_URI = Uri.parse("content://com.google.android.music.ConfigContent");
  public static final Uri SERVER_SETTINGS_URI = Uri.withAppendedPath(CONTENT_URI, "server");

  public static Uri getConfigSettingUri(int paramInt, String paramString)
  {
    String str1 = Utils.urlEncode(paramString);
    switch (paramInt)
    {
    default:
      String str2 = "Unhandled type: " + paramInt;
      throw new IllegalStateException(str2);
    case 1:
    case 2:
    }
    for (Uri localUri = Uri.withAppendedPath(SERVER_SETTINGS_URI, str1); ; localUri = Uri.withAppendedPath(APP_SETTINGS_URI, str1))
      return localUri;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.ConfigContent
 * JD-Core Version:    0.6.2
 */