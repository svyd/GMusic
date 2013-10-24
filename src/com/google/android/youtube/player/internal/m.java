package com.google.android.youtube.player.internal;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import java.util.Locale;
import java.util.Map;

public final class m
{
  public final String a;
  public final String b;
  public final String c;
  public final String d;
  public final String e;
  public final String f;
  public final String g;
  public final String h;
  public final String i;
  public final String j;

  public m(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    if ((localResources != null) && (localResources.getConfiguration() != null) && (localResources.getConfiguration().locale != null));
    for (Locale localLocale = localResources.getConfiguration().locale; ; localLocale = Locale.getDefault())
    {
      Map localMap = x.a(localLocale);
      String str1 = (String)localMap.get("error_initializing_player");
      this.a = str1;
      String str2 = (String)localMap.get("get_youtube_app_title");
      this.b = str2;
      String str3 = (String)localMap.get("get_youtube_app_text");
      this.c = str3;
      String str4 = (String)localMap.get("get_youtube_app_action");
      this.d = str4;
      String str5 = (String)localMap.get("enable_youtube_app_title");
      this.e = str5;
      String str6 = (String)localMap.get("enable_youtube_app_text");
      this.f = str6;
      String str7 = (String)localMap.get("enable_youtube_app_action");
      this.g = str7;
      String str8 = (String)localMap.get("update_youtube_app_title");
      this.h = str8;
      String str9 = (String)localMap.get("update_youtube_app_text");
      this.i = str9;
      String str10 = (String)localMap.get("update_youtube_app_action");
      this.j = str10;
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.m
 * JD-Core Version:    0.6.2
 */