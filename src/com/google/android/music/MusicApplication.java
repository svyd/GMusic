package com.google.android.music;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v7.media.MediaRouter;
import com.google.android.gsf.Gservices;
import com.google.android.music.cast.CastMediaProviderFactory;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.ConfigUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncWorkers;
import java.io.File;

public class MusicApplication extends Application
{
  private CastMediaProviderFactory mCastProviderFactory;
  private MediaRouter mMediaRouter;
  Thread.UncaughtExceptionHandler mNewUncaughtExceptionHandler;
  Thread.UncaughtExceptionHandler mOldUncaughtExceptionHandler = null;

  public MusicApplication()
  {
    Thread.UncaughtExceptionHandler local2 = new Thread.UncaughtExceptionHandler()
    {
      // ERROR //
      public void uncaughtException(Thread paramAnonymousThread, java.lang.Throwable paramAnonymousThrowable)
      {
        // Byte code:
        //   0: aload_2
        //   1: instanceof 23
        //   4: ifeq +10 -> 14
        //   7: aload_2
        //   8: checkcast 23	java/lang/OutOfMemoryError
        //   11: invokestatic 29	com/google/android/music/utils/AlbumArtUtils:report	(Ljava/lang/OutOfMemoryError;)V
        //   14: aload_0
        //   15: getfield 14	com/google/android/music/MusicApplication$2:this$0	Lcom/google/android/music/MusicApplication;
        //   18: getfield 35	com/google/android/music/MusicApplication:mOldUncaughtExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
        //   21: aload_1
        //   22: aload_2
        //   23: invokeinterface 37 3 0
        //   28: return
        //   29: astore_3
        //   30: aload_0
        //   31: getfield 14	com/google/android/music/MusicApplication$2:this$0	Lcom/google/android/music/MusicApplication;
        //   34: getfield 35	com/google/android/music/MusicApplication:mOldUncaughtExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
        //   37: aload_1
        //   38: aload_2
        //   39: invokeinterface 37 3 0
        //   44: return
        //   45: astore 4
        //   47: aload_0
        //   48: getfield 14	com/google/android/music/MusicApplication$2:this$0	Lcom/google/android/music/MusicApplication;
        //   51: getfield 35	com/google/android/music/MusicApplication:mOldUncaughtExceptionHandler	Ljava/lang/Thread$UncaughtExceptionHandler;
        //   54: aload_1
        //   55: aload_2
        //   56: invokeinterface 37 3 0
        //   61: aload 4
        //   63: athrow
        //
        // Exception table:
        //   from	to	target	type
        //   0	14	29	java/lang/Throwable
        //   0	14	45	finally
      }
    };
    this.mNewUncaughtExceptionHandler = local2;
  }

  public void onCreate()
  {
    ComponentName localComponentName = null;
    super.onCreate();
    Context localContext = getApplicationContext();
    Document.init(localContext);
    ConfigUtils.init(localContext.getContentResolver());
    boolean bool1 = MusicUtils.isUIProcess(this);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    MusicPreferences localMusicPreferences;
    boolean bool2;
    if (bool1)
    {
      AlbumArtUtils.initBitmapPoolForUIProcess();
      UIStateManager localUIStateManager = UIStateManager.getInstance(this);
      localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
      bool2 = false;
    }
    while (true)
    {
      File localFile2;
      try
      {
        if (Gservices.getBoolean(localContext.getContentResolver(), "music_debug_logs_enabled", false))
        {
          boolean bool3 = localMusicPreferences.isLogFilesEnabled();
          bool2 = bool3;
          MusicPreferences.releaseMusicPreferences(this);
          if (bool2)
          {
            DebugUtils.setAutoLogAll(true);
            File localFile1 = Environment.getExternalStorageDirectory();
            localFile2 = new File(localFile1, "music2_logs");
            if (!localFile2.exists())
              boolean bool4 = localFile2.mkdir();
            if (!bool1)
              break label340;
            Log.configure(localFile2, "music_ui.log");
          }
          if (bool1)
            MusicUtils.openPlaylistCursor(this);
          Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
          this.mOldUncaughtExceptionHandler = localUncaughtExceptionHandler;
          Thread.setDefaultUncaughtExceptionHandler(this.mNewUncaughtExceptionHandler);
          if (bool1)
          {
            Object[] arrayOfObject = new Object[0];
            localMusicEventLogger.trackEvent("uiStarted", arrayOfObject);
          }
          if (!bool1)
          {
            LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
            Runnable local1 = new Runnable()
            {
              public void run()
              {
                Intent localIntent = new Intent("com.google.android.music.START_DOWNLOAD_SCHEDULING");
                MusicApplication.this.sendBroadcast(localIntent);
              }
            };
            boolean bool5 = localLoggableHandler.postDelayed(local1, 5000L);
            PackageManager localPackageManager = getPackageManager();
            localComponentName = new ComponentName(this, "com.android.music.MediaAppWidgetProvider");
            if (!getResources().getBoolean(2131361795))
              break label359;
            i = 1;
            localPackageManager.setComponentEnabledSetting(localComponentName, i, 1);
          }
          if (!localMusicPreferences.isMediaRouteSupportEnabled())
            return;
          if (!MusicPreferences.isGingerbreadOrGreater())
            return;
          CastMediaProviderFactory localCastMediaProviderFactory = new CastMediaProviderFactory(localContext, bool2);
          this.mCastProviderFactory = localCastMediaProviderFactory;
          MediaRouter localMediaRouter = MediaRouter.getInstance(localContext);
          this.mMediaRouter = localMediaRouter;
          if (!bool1)
            break label365;
          this.mCastProviderFactory.registerMinimalMediaRouteProvider();
          return;
          AlbumArtUtils.initBitmapPoolForMainProcess();
          break;
        }
        boolean bool6 = false;
        localMusicPreferences.setLogFilesEnable(bool6);
        continue;
      }
      finally
      {
        MusicPreferences.releaseMusicPreferences(this);
      }
      label340: Log.configure(localFile2, "music_main.log");
      Log.configureLogFile("com.google.android.music.pin", localFile2, "music_pin.log");
      continue;
      label359: int i = 2;
    }
    label365: this.mCastProviderFactory.registerMediaRouteProvider();
  }

  public void onTerminate()
  {
    if (this.mCastProviderFactory != null)
    {
      this.mCastProviderFactory.destroy();
      this.mCastProviderFactory = null;
    }
    MusicUtils.closePlaylistCursor();
    MusicEventLogger.destroy();
    super.onTerminate();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.MusicApplication
 * JD-Core Version:    0.6.2
 */