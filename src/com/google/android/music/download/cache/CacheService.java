package com.google.android.music.download.cache;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.os.Message;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;

public class CacheService extends Service
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private volatile CacheManagerImpl mCacheManager;
  private CacheWorker mCacheWorker;
  private volatile MusicPreferences mMusicPreferences;
  private final SharedPreferences.OnSharedPreferenceChangeListener mPreferenceChangeListener;

  public CacheService()
  {
    CacheWorker localCacheWorker = new CacheWorker("CacheService");
    this.mCacheWorker = localCacheWorker;
    SharedPreferences.OnSharedPreferenceChangeListener local1 = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
      public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
      {
        boolean bool = CacheService.this.mMusicPreferences.isCachedStreamingMusicEnabled();
        CacheService.this.mCacheManager.setAllowCaching(bool);
        if (bool)
        {
          CacheService.this.cancelClearCacheMessage();
          return;
        }
        CacheService.this.sendClearCachedMessage(-1, 10000L);
      }
    };
    this.mPreferenceChangeListener = local1;
  }

  private void cancelClearCacheMessage()
  {
    this.mCacheWorker.removeMessages(2);
  }

  private void handleClearCache(Message paramMessage)
  {
    this.mCacheManager.clearCachedFiles();
    if (paramMessage.arg1 == -1)
      return;
    int i = paramMessage.arg1;
    stopSelf(i);
  }

  private void handleClearOrphaned(Message paramMessage)
  {
    this.mCacheManager.clearOrphanedFiles();
    if (paramMessage.arg1 == -1)
      return;
    int i = paramMessage.arg1;
    stopSelf(i);
  }

  private void sendClearCachedMessage(int paramInt, long paramLong)
  {
    Message localMessage = this.mCacheWorker.obtainMessage(2);
    localMessage.arg1 = paramInt;
    boolean bool = this.mCacheWorker.sendMessageDelayed(localMessage, paramLong);
  }

  private void sendClearOrphanedMessage(int paramInt)
  {
    Message localMessage = this.mCacheWorker.obtainMessage(1);
    localMessage.arg1 = paramInt;
    boolean bool = this.mCacheWorker.sendMessage(localMessage);
  }

  public IBinder onBind(Intent paramIntent)
  {
    Log.d("CacheService", "onBind");
    return this.mCacheManager;
  }

  public void onCreate()
  {
    super.onCreate();
    Log.d("CacheService", "onCreate");
    MusicPreferences localMusicPreferences1 = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences1;
    MusicPreferences localMusicPreferences2 = this.mMusicPreferences;
    CacheManagerImpl localCacheManagerImpl = new CacheManagerImpl(this, localMusicPreferences2);
    this.mCacheManager = localCacheManagerImpl;
    MusicPreferences localMusicPreferences3 = this.mMusicPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences3.registerOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
  }

  public void onDestroy()
  {
    super.onDestroy();
    Log.d("CacheService", "onDestroy");
    this.mCacheWorker.quit();
    MusicPreferences localMusicPreferences = this.mMusicPreferences;
    SharedPreferences.OnSharedPreferenceChangeListener localOnSharedPreferenceChangeListener = this.mPreferenceChangeListener;
    localMusicPreferences.unregisterOnSharedPreferenceChangeListener(localOnSharedPreferenceChangeListener);
    MusicPreferences.releaseMusicPreferences(this);
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    if (LOGV)
    {
      String str1 = "intent=" + paramIntent;
      Log.d("CacheService", str1);
    }
    String str2;
    if (paramIntent != null)
    {
      str2 = paramIntent.getAction();
      if (!"com.google.android.music.download.cache.CacheService.CLEAR_ORPHANED".equals(str2))
        break label67;
      sendClearOrphanedMessage(paramInt2);
    }
    while (true)
    {
      return 3;
      str2 = null;
      break;
      label67: if ("com.google.android.music.download.cache.CacheService.CLEAR_CACHE".equals(str2))
      {
        sendClearCachedMessage(paramInt2, 0L);
      }
      else
      {
        String str3 = "Unknown action=" + str2;
        Log.w("CacheService", str3);
        stopSelf(paramInt2);
      }
    }
  }

  private class CacheWorker extends LoggableHandler
  {
    public CacheWorker(String arg2)
    {
      super();
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
      case 1:
      case 2:
      }
      while (true)
      {
        CacheService localCacheService = CacheService.this;
        int i = paramMessage.arg1;
        localCacheService.stopSelf(i);
        return;
        CacheService.this.handleClearOrphaned(paramMessage);
        continue;
        CacheService.this.handleClearCache(paramMessage);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.CacheService
 * JD-Core Version:    0.6.2
 */