package com.google.android.music.utils;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.log.Log;
import com.google.common.collect.ImmutableSet.Builder;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DebugUtils
{
  public static final String HTTP_TAG = MusicTag.HTTP.toString();
  public static final boolean IS_DEBUG_BUILD;
  private static final Set<MusicTag> sAutoDebugOff;
  private static final Set<MusicTag> sAutoDebugOn;
  private static volatile boolean sAutoLogAll;

  static
  {
    ImmutableSet.Builder localBuilder1 = new ImmutableSet.Builder();
    MusicTag localMusicTag1 = MusicTag.PLAYBACK_SERVICE;
    ImmutableSet.Builder localBuilder2 = localBuilder1.add(localMusicTag1);
    MusicTag localMusicTag2 = MusicTag.CONTENT_PROVIDER;
    ImmutableSet.Builder localBuilder3 = localBuilder2.add(localMusicTag2);
    MusicTag localMusicTag3 = MusicTag.STORE_IMPORTER;
    ImmutableSet.Builder localBuilder4 = localBuilder3.add(localMusicTag3);
    MusicTag localMusicTag4 = MusicTag.SYNC;
    ImmutableSet.Builder localBuilder5 = localBuilder4.add(localMusicTag4);
    MusicTag localMusicTag5 = MusicTag.HTTP;
    ImmutableSet.Builder localBuilder6 = localBuilder5.add(localMusicTag5);
    MusicTag localMusicTag6 = MusicTag.LOG_FILE;
    ImmutableSet.Builder localBuilder7 = localBuilder6.add(localMusicTag6);
    MusicTag localMusicTag7 = MusicTag.MEDIA_LIST;
    ImmutableSet.Builder localBuilder8 = localBuilder7.add(localMusicTag7);
    MusicTag localMusicTag8 = MusicTag.SEARCH;
    ImmutableSet.Builder localBuilder9 = localBuilder8.add(localMusicTag8);
    MusicTag localMusicTag9 = MusicTag.CALLS;
    ImmutableSet.Builder localBuilder10 = localBuilder9.add(localMusicTag9);
    MusicTag localMusicTag10 = MusicTag.CLOUD_CLIENT;
    ImmutableSet.Builder localBuilder11 = localBuilder10.add(localMusicTag10);
    MusicTag localMusicTag11 = MusicTag.CAST;
    sAutoDebugOff = localBuilder11.add(localMusicTag11).build();
    sAutoDebugOn = new ImmutableSet.Builder().build();
    if ((Build.TYPE.contains("debug")) || (Build.TYPE.contains("eng")));
    for (boolean bool = true; ; bool = false)
    {
      IS_DEBUG_BUILD = bool;
      sAutoLogAll = IS_DEBUG_BUILD;
      return;
    }
  }

  public static String arrayToString(String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    if (paramArrayOfString != null)
    {
      int i = 0;
      while (true)
      {
        int j = paramArrayOfString.length;
        if (i >= j)
          break;
        if (i > 0)
          StringBuilder localStringBuilder2 = localStringBuilder1.append("#");
        String str = paramArrayOfString[i];
        StringBuilder localStringBuilder3 = localStringBuilder1.append(str);
        i += 1;
      }
    }
    return localStringBuilder1.toString();
  }

  public static final String bundleToString(Bundle paramBundle)
  {
    if (paramBundle == null);
    for (String str = "NULL"; ; str = paramBundle.toString())
    {
      return str;
      unparcel(paramBundle);
    }
  }

  public static final String getStackTrace(Throwable paramThrowable)
  {
    StringWriter localStringWriter = new StringWriter();
    PrintWriter localPrintWriter = new PrintWriter(localStringWriter, false);
    paramThrowable.printStackTrace(localPrintWriter);
    localPrintWriter.flush();
    localPrintWriter.close();
    return localStringWriter.getBuffer().toString();
  }

  public static boolean isAutoLogAll()
  {
    return sAutoLogAll;
  }

  public static final boolean isLoggable(MusicTag paramMusicTag)
  {
    if (((sAutoLogAll) && (!sAutoDebugOff.contains(paramMusicTag))) || (Log.isLoggable(paramMusicTag.toString(), 3)) || ((!sAutoDebugOff.contains(paramMusicTag)) && (sAutoDebugOn.contains(paramMusicTag))));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static String listToString(List<ContentIdentifier> paramList)
  {
    if (paramList == null);
    for (String str = "null"; ; str = TextUtils.join(",", paramList))
      return str;
  }

  public static final void maybeLogMethodName(MusicTag paramMusicTag, Object paramObject)
  {
    if (!isLoggable(paramMusicTag))
      return;
    StackTraceElement localStackTraceElement = new Throwable().getStackTrace()[1];
    String str1 = paramMusicTag.toString();
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str2 = paramObject.getClass().getName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str2).append("#");
    String str3 = localStackTraceElement.getMethodName();
    String str4 = str3;
    Log.d(str1, str4);
  }

  public static void setAutoLogAll(boolean paramBoolean)
  {
    sAutoLogAll = paramBoolean;
  }

  private static void unparcel(Bundle paramBundle)
  {
    Iterator localIterator = paramBundle.keySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      String str = (String)localIterator.next();
      Object localObject = paramBundle.get(str);
      if ((localObject instanceof Bundle))
        unparcel((Bundle)localObject);
    }
  }

  public static enum MusicTag
  {
    private final String mText;

    static
    {
      DOWNLOAD = new MusicTag("DOWNLOAD", 1, "MusicDownload");
      CONTENT_PROVIDER = new MusicTag("CONTENT_PROVIDER", 2, "MusicContentProvider");
      STORE_IMPORTER = new MusicTag("STORE_IMPORTER", 3, "MusicStoreImporter");
      SYNC = new MusicTag("SYNC", 4, "MusicSync");
      HTTP = new MusicTag("HTTP", 5, "MusicHttp");
      LOG_FILE = new MusicTag("LOG_FILE", 6, "MusicLogFile");
      MEDIA_LIST = new MusicTag("MEDIA_LIST", 7, "MusicMediaList");
      AAH = new MusicTag("AAH", 8, "aah.Music");
      STORE = new MusicTag("STORE", 9, "MusicStore");
      PREFERENCES = new MusicTag("PREFERENCES", 10, "MusicPreferences");
      CLOUD_CLIENT = new MusicTag("CLOUD_CLIENT", 11, "MusicCloudClient");
      ALBUM_ART = new MusicTag("ALBUM_ART", 12, "MusicAlbumArt");
      ASYNC = new MusicTag("ASYNC", 13, "MusicAsync");
      UI = new MusicTag("UI", 14, "MusicUI");
      CALLS = new MusicTag("CALLS", 15, "MusicCalls");
      LEANBACK = new MusicTag("LEANBACK", 16, "MusicLeanback");
      SEARCH = new MusicTag("SEARCH", 17, "Search");
      CAST = new MusicTag("CAST", 18, "MusicCast");
      XDI = new MusicTag("XDI", 19, "MusicXdi");
      MusicTag[] arrayOfMusicTag = new MusicTag[20];
      MusicTag localMusicTag1 = PLAYBACK_SERVICE;
      arrayOfMusicTag[0] = localMusicTag1;
      MusicTag localMusicTag2 = DOWNLOAD;
      arrayOfMusicTag[1] = localMusicTag2;
      MusicTag localMusicTag3 = CONTENT_PROVIDER;
      arrayOfMusicTag[2] = localMusicTag3;
      MusicTag localMusicTag4 = STORE_IMPORTER;
      arrayOfMusicTag[3] = localMusicTag4;
      MusicTag localMusicTag5 = SYNC;
      arrayOfMusicTag[4] = localMusicTag5;
      MusicTag localMusicTag6 = HTTP;
      arrayOfMusicTag[5] = localMusicTag6;
      MusicTag localMusicTag7 = LOG_FILE;
      arrayOfMusicTag[6] = localMusicTag7;
      MusicTag localMusicTag8 = MEDIA_LIST;
      arrayOfMusicTag[7] = localMusicTag8;
      MusicTag localMusicTag9 = AAH;
      arrayOfMusicTag[8] = localMusicTag9;
      MusicTag localMusicTag10 = STORE;
      arrayOfMusicTag[9] = localMusicTag10;
      MusicTag localMusicTag11 = PREFERENCES;
      arrayOfMusicTag[10] = localMusicTag11;
      MusicTag localMusicTag12 = CLOUD_CLIENT;
      arrayOfMusicTag[11] = localMusicTag12;
      MusicTag localMusicTag13 = ALBUM_ART;
      arrayOfMusicTag[12] = localMusicTag13;
      MusicTag localMusicTag14 = ASYNC;
      arrayOfMusicTag[13] = localMusicTag14;
      MusicTag localMusicTag15 = UI;
      arrayOfMusicTag[14] = localMusicTag15;
      MusicTag localMusicTag16 = CALLS;
      arrayOfMusicTag[15] = localMusicTag16;
      MusicTag localMusicTag17 = LEANBACK;
      arrayOfMusicTag[16] = localMusicTag17;
      MusicTag localMusicTag18 = SEARCH;
      arrayOfMusicTag[17] = localMusicTag18;
      MusicTag localMusicTag19 = CAST;
      arrayOfMusicTag[18] = localMusicTag19;
      MusicTag localMusicTag20 = XDI;
      arrayOfMusicTag[19] = localMusicTag20;
    }

    private MusicTag(String paramString)
    {
      if (paramString.length() > 23)
        throw new IllegalArgumentException("Tag length is longer than the max 23");
      this.mText = paramString;
    }

    public String toString()
    {
      return this.mText;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.DebugUtils
 * JD-Core Version:    0.6.2
 */