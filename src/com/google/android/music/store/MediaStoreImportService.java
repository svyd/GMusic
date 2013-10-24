package com.google.android.music.store;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Audio.Playlists;
import android.util.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import java.util.concurrent.atomic.AtomicBoolean;

public class MediaStoreImportService extends Service
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE_IMPORTER);
  private final IBinder mBinder;
  private Runnable mContentChangeProcessor;
  private final ContentObserver mContentObserver;
  private Runnable mDelayedImport;
  private long mFirstChangeTimeSinceLastImport;
  private final AtomicBoolean mImportPending;
  private long mLastImportTime;
  private final LoggableHandler mWorker;

  public MediaStoreImportService()
  {
    LocalBinder localLocalBinder = new LocalBinder();
    this.mBinder = localLocalBinder;
    LoggableHandler localLoggableHandler1 = new LoggableHandler("MediaStoreImportService");
    this.mWorker = localLoggableHandler1;
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    this.mImportPending = localAtomicBoolean;
    this.mLastImportTime = 0L;
    this.mFirstChangeTimeSinceLastImport = 0L;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        long l1 = System.currentTimeMillis();
        long l2 = MediaStoreImportService.this.mLastImportTime;
        long l3 = l1 - l2 - 10000L;
        if (l3 >= 0L)
        {
          MediaStoreImportService.this.importMediaStore();
          return;
        }
        LoggableHandler localLoggableHandler = MediaStoreImportService.this.mWorker;
        Runnable localRunnable = MediaStoreImportService.this.mDelayedImport;
        long l4 = Math.max(l3, 200L);
        boolean bool = localLoggableHandler.postDelayed(localRunnable, l4);
      }
    };
    this.mDelayedImport = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        int i = 1;
        long l1 = System.currentTimeMillis();
        if (MediaStoreImportService.this.mFirstChangeTimeSinceLastImport > 0L)
        {
          long l2 = MediaStoreImportService.this.mFirstChangeTimeSinceLastImport;
          if (l1 - l2 > 30000L)
            i = 0;
        }
        while (true)
        {
          if (i == 0)
            return;
          LoggableHandler localLoggableHandler1 = MediaStoreImportService.this.mWorker;
          Runnable localRunnable1 = MediaStoreImportService.this.mDelayedImport;
          localLoggableHandler1.removeCallbacks(localRunnable1);
          LoggableHandler localLoggableHandler2 = MediaStoreImportService.this.mWorker;
          Runnable localRunnable2 = MediaStoreImportService.this.mDelayedImport;
          boolean bool = localLoggableHandler2.postDelayed(localRunnable2, 1000L);
          return;
          long l3 = MediaStoreImportService.access$402(MediaStoreImportService.this, l1);
        }
      }
    };
    this.mContentChangeProcessor = local2;
    LoggableHandler localLoggableHandler2 = this.mWorker;
    ContentObserver local3 = new ContentObserver(localLoggableHandler2)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        if (MediaStoreImportService.LOGV)
          int i = Log.d("MediaStoreImportService", "Media Store Updated");
        LoggableHandler localLoggableHandler = MediaStoreImportService.this.mWorker;
        Runnable localRunnable = MediaStoreImportService.this.mContentChangeProcessor;
        boolean bool = localLoggableHandler.post(localRunnable);
      }
    };
    this.mContentObserver = local3;
  }

  private void importMediaStore()
  {
    this.mImportPending.set(true);
    try
    {
      Store.getInstance(this).importMediaStore(this);
      this.mImportPending.set(false);
      Intent localIntent1 = new Intent("com.google.android.music.IMPORT_COMPLETE");
      sendBroadcast(localIntent1);
      long l = System.currentTimeMillis();
      this.mLastImportTime = l;
      this.mFirstChangeTimeSinceLastImport = 0L;
      return;
    }
    finally
    {
      this.mImportPending.set(false);
      Intent localIntent2 = new Intent("com.google.android.music.IMPORT_COMPLETE");
      sendBroadcast(localIntent2);
    }
  }

  private void sendStatusBroadcast(Context paramContext)
  {
    if (this.mImportPending.get());
    for (String str = "com.google.android.music.IMPORT_PENDING"; ; str = "com.google.android.music.IMPORT_COMPLETE")
    {
      if (str == null)
        return;
      Intent localIntent = new Intent(str);
      paramContext.sendBroadcast(localIntent);
      return;
    }
  }

  public IBinder onBind(Intent paramIntent)
  {
    if (LOGV)
      int i = Log.d("MediaStoreImportService", "onBind");
    LoggableHandler localLoggableHandler = this.mWorker;
    Runnable localRunnable = this.mContentChangeProcessor;
    boolean bool = localLoggableHandler.post(localRunnable);
    ContentResolver localContentResolver1 = getContentResolver();
    Uri localUri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    ContentObserver localContentObserver1 = this.mContentObserver;
    localContentResolver1.registerContentObserver(localUri1, true, localContentObserver1);
    ContentResolver localContentResolver2 = getContentResolver();
    Uri localUri2 = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
    ContentObserver localContentObserver2 = this.mContentObserver;
    localContentResolver2.registerContentObserver(localUri2, true, localContentObserver2);
    return this.mBinder;
  }

  public void onDestroy()
  {
    if (LOGV)
      int i = Log.d("MediaStoreImportService", "onDestroy");
    ContentResolver localContentResolver = getContentResolver();
    ContentObserver localContentObserver = this.mContentObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
    this.mWorker.quit();
    super.onDestroy();
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, final int paramInt2)
  {
    String str1 = paramIntent.getAction();
    if (LOGV)
    {
      String str2 = "Handle action: " + str1;
      int i = Log.d("MediaStoreImportService", str2);
    }
    if ("MediaStoreImportService.import".equals(str1))
    {
      LoggableHandler localLoggableHandler1 = this.mWorker;
      Runnable local4 = new Runnable()
      {
        public void run()
        {
          LoggableHandler localLoggableHandler = MediaStoreImportService.this.mWorker;
          Runnable localRunnable = MediaStoreImportService.this.mDelayedImport;
          localLoggableHandler.removeCallbacks(localRunnable);
          MediaStoreImportService.this.importMediaStore();
          MediaStoreImportService localMediaStoreImportService = MediaStoreImportService.this;
          int i = paramInt2;
          localMediaStoreImportService.stopSelf(i);
        }
      };
      boolean bool1 = localLoggableHandler1.post(local4);
    }
    while (true)
    {
      return 3;
      if ("MediaStoreImportService.status".equals(str1))
      {
        sendStatusBroadcast(this);
        LoggableHandler localLoggableHandler2 = this.mWorker;
        Runnable local5 = new Runnable()
        {
          public void run()
          {
            MediaStoreImportService localMediaStoreImportService = MediaStoreImportService.this;
            int i = paramInt2;
            localMediaStoreImportService.stopSelf(i);
          }
        };
        boolean bool2 = localLoggableHandler2.post(local5);
      }
      else if ("MediaStoreImportService.import_pending".equals(str1))
      {
        this.mImportPending.set(true);
        LoggableHandler localLoggableHandler3 = this.mWorker;
        Runnable local6 = new Runnable()
        {
          public void run()
          {
            MediaStoreImportService localMediaStoreImportService = MediaStoreImportService.this;
            int i = paramInt2;
            localMediaStoreImportService.stopSelf(i);
          }
        };
        boolean bool3 = localLoggableHandler3.post(local6);
      }
      else
      {
        String str3 = "Unexpected action requested: " + str1;
        int j = Log.w("MediaStoreImportService", str3);
        LoggableHandler localLoggableHandler4 = this.mWorker;
        Runnable local7 = new Runnable()
        {
          public void run()
          {
            MediaStoreImportService localMediaStoreImportService = MediaStoreImportService.this;
            int i = paramInt2;
            localMediaStoreImportService.stopSelf(i);
          }
        };
        boolean bool4 = localLoggableHandler4.post(local7);
      }
    }
  }

  public boolean onUnbind(Intent paramIntent)
  {
    if (LOGV)
      int i = Log.d("MediaStoreImportService", "onUnbind");
    ContentResolver localContentResolver = getContentResolver();
    ContentObserver localContentObserver = this.mContentObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
    return super.onUnbind(paramIntent);
  }

  public static class Receiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      int i = 1;
      String str1 = paramIntent.getAction();
      if ("android.provider.action.MTP_SESSION_END".equals(str1));
      while (true)
      {
        if (i == 0)
          return;
        Intent localIntent1 = new Intent(paramContext, MediaStoreImportService.class);
        if (MediaStoreImportService.LOGV)
          int k = Log.d("MediaStoreImportService", "Scanner finished. Starting media store import");
        Intent localIntent2 = localIntent1.setAction("MediaStoreImportService.import");
        ComponentName localComponentName1 = paramContext.startService(localIntent1);
        return;
        int j;
        if ("android.intent.action.MEDIA_SCANNER_STARTED".equals(str1))
        {
          String str2 = paramIntent.getDataString();
          if ((str2 != null) && (str2.startsWith("file:///system/")));
          while (true)
          {
            j = 0;
            break;
            Intent localIntent3 = new Intent(paramContext, MediaStoreImportService.class);
            Intent localIntent4 = localIntent3.setAction("MediaStoreImportService.import_pending");
            ComponentName localComponentName2 = paramContext.startService(localIntent3);
            Intent localIntent5 = new Intent("com.google.android.music.IMPORT_PENDING");
            paramContext.sendBroadcast(localIntent5);
          }
        }
        if ("android.intent.action.MEDIA_SCANNER_FINISHED".equals(str1))
        {
          String str3 = paramIntent.getDataString();
          if ((str3 != null) && (str3.startsWith("file:///system/")))
          {
            j = 0;
          }
          else
          {
            MediaStoreImporter.requestMediaStoreVersionCheck(paramContext);
            Intent localIntent6 = new Intent(paramContext, MediaStoreImportService.class);
            Intent localIntent7 = localIntent6.setAction("MediaStoreImportService.import_pending");
            ComponentName localComponentName3 = paramContext.startService(localIntent6);
            Intent localIntent8 = new Intent("com.google.android.music.IMPORT_PENDING");
            paramContext.sendBroadcast(localIntent8);
          }
        }
        else
        {
          j = 0;
        }
      }
    }
  }

  public class LocalBinder extends Binder
  {
    public LocalBinder()
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.MediaStoreImportService
 * JD-Core Version:    0.6.2
 */