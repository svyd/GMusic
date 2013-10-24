package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

public final class UriUtils
{
  public static Uri getAccountImageUri(String paramString)
  {
    return Uri.parse("image.account://" + paramString);
  }

  public static String getAndroidResourceUri(Context paramContext, int paramInt)
  {
    return getAndroidResourceUri(paramContext.getResources(), paramInt);
  }

  public static String getAndroidResourceUri(Resources paramResources, int paramInt)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("android.resource://");
    String str = paramResources.getResourceName(paramInt).replace(":", "/");
    return str;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.UriUtils
 * JD-Core Version:    0.6.2
 */