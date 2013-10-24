package com.google.android.music.download;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicRingtoneManager;
import com.google.android.music.store.MusicRingtoneManager.RingtoneSource;
import com.google.android.music.store.Store;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.io.File;

public class DownloadUtils
{
  private static long DELAY_PURGE_MS = 30000L;
  public static final ImmutableMap<String, String> ExtensionToMimeMap = new ImmutableMap.Builder().put("mp3", "audio/mpeg").put("aac", "audio/aac").put("ogg", "audio/ogg").put("m4a", "audio/mp4").put("wav", "audio/x-wav").put("wma", "audio/x-ms-wma").put("flac", "audio/flac").put("mka", "audio/x-matroska").build();
  public static final ImmutableMap<String, String> MimeToExtensionMap = new ImmutableMap.Builder().put("audio/mpeg", "mp3").put("audio/mp3", "mp3").put("audio/aac", "aac").put("audio/ogg", "ogg").put("audio/mp4", "m4a").put("audio/x-wav", "wav").put("audio/x-ms-wma", "wma").put("audio/flac", "flac").put("audio/x-matroska", "mka").build();

  public static NetworkInfo getActiveNetworkInfo(Context paramContext)
  {
    NetworkInfo localNetworkInfo = null;
    if (paramContext == null);
    while (true)
    {
      return localNetworkInfo;
      ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
      if (localConnectivityManager != null)
        localNetworkInfo = localConnectivityManager.getActiveNetworkInfo();
    }
  }

  public static String getUserAgent(Context paramContext)
  {
    StringBuilder localStringBuilder1 = new StringBuilder("Android-Music/");
    try
    {
      PackageManager localPackageManager = paramContext.getPackageManager();
      String str = paramContext.getPackageName();
      int i = localPackageManager.getPackageInfo(str, 0).versionCode;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(i);
      return localStringBuilder1.toString();
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
        StringBuilder localStringBuilder3 = localStringBuilder1.append("1.0");
    }
  }

  public static boolean isMobileOrMeteredNetworkType(NetworkInfo paramNetworkInfo, Context paramContext)
  {
    boolean bool = false;
    if ((paramNetworkInfo == null) || (paramContext == null));
    while (true)
    {
      return bool;
      ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
      int i = paramNetworkInfo.getType();
      if ((i == 0) || (i == 6) || (i == 7) || ((i == 14) && (MusicPreferences.isGlass())) || (ConnectivityManagerCompat.isActiveNetworkMetered(localConnectivityManager)))
        bool = true;
    }
  }

  public static boolean isSupportedNetworkType(int paramInt)
  {
    boolean bool = true;
    if ((paramInt == 0) || (paramInt == 1) || (paramInt == 6) || (paramInt == 7) || (paramInt == 9) || ((paramInt == 14) && (MusicPreferences.isGlass())));
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public static boolean isUnmeteredEthernetNetworkType(NetworkInfo paramNetworkInfo, Context paramContext)
  {
    boolean bool = false;
    if ((paramNetworkInfo == null) || (paramContext == null));
    while (true)
    {
      return bool;
      ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
      if ((paramNetworkInfo.getType() == 9) && (!ConnectivityManagerCompat.isActiveNetworkMetered(localConnectivityManager)))
        bool = true;
    }
  }

  public static boolean isUnmeteredWifiNetworkType(NetworkInfo paramNetworkInfo, Context paramContext)
  {
    boolean bool = true;
    if ((paramNetworkInfo == null) || (paramContext == null));
    for (bool = false; ; bool = false)
    {
      ConnectivityManager localConnectivityManager;
      do
      {
        return bool;
        localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
      }
      while ((paramNetworkInfo.getType() == 1) && (!ConnectivityManagerCompat.isActiveNetworkMetered(localConnectivityManager)));
    }
  }

  public static boolean isWifiNetworkType(NetworkInfo paramNetworkInfo)
  {
    boolean bool1 = true;
    boolean bool2 = false;
    if (paramNetworkInfo == null)
      return bool2;
    if (paramNetworkInfo.getType() == 1);
    while (true)
    {
      bool2 = bool1;
      break;
      bool1 = false;
    }
  }

  public static boolean makeRingtoneFromDownloadRequest(Context paramContext, DownloadRequest paramDownloadRequest, long paramLong)
  {
    boolean bool = true;
    MusicRingtoneManager localMusicRingtoneManager = Store.getInstance(paramContext).getRingtoneManager();
    if (paramLong > 0L)
    {
      MusicRingtoneManager.RingtoneSource localRingtoneSource = MusicRingtoneManager.RingtoneSource.RINGTONE_DOWNLOAD;
      long l1 = paramDownloadRequest.getId().getId();
      String str1 = paramDownloadRequest.getRemoteId();
      String str2 = paramDownloadRequest.getFileLocation().getFullPath().getAbsolutePath();
      String str3 = paramDownloadRequest.getTrackTitle();
      long l2 = paramLong;
      int i = localMusicRingtoneManager.makeRingtoneFile(l1, str1, str2, localRingtoneSource, str3, l2, true);
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public static void purgeNautilusTrackByLocalId(Context paramContext, final ContentIdentifier paramContentIdentifier)
  {
    if ((paramContentIdentifier.isNautilusDomain()) || (paramContentIdentifier.isDefaultDomain()))
    {
      LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          Store localStore = Store.getInstance(DownloadUtils.this);
          Context localContext = DownloadUtils.this;
          long l = paramContentIdentifier.getId();
          boolean bool = localStore.purgeNautilusTrackByLocalId(localContext, l);
        }
      };
      long l = DELAY_PURGE_MS;
      boolean bool = localLoggableHandler.postDelayed(local1, l);
      return;
    }
    Log.w("DownloadUtils", "Purge request for track not in Nautilus or Default domain");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadUtils
 * JD-Core Version:    0.6.2
 */