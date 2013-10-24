package com.google.android.music.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import com.google.android.music.download.artwork.ArtDownloadServiceConnection.ArtChangeListener;
import com.google.android.music.download.artwork.CachedArtDownloadServiceConnection;
import com.google.android.music.toshare.HandlerExecutor;
import com.google.android.music.ui.UIStateManager;
import com.google.common.collect.ArrayListMultimap;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BitmapDiskCache
  implements ArtDownloadServiceConnection.ArtChangeListener<String>
{
  private static BitmapDiskCache sInstance;
  private final CachedArtDownloadServiceConnection mArtDownloadConnector;
  private final ExecutorService mBackgroundExecutorService;
  private final ArrayListMultimap<String, WeakReference<Callback>> mCallbacks;
  private final Context mContext;
  private final Executor mUiThreadExecutor;

  private BitmapDiskCache(Context paramContext)
  {
    ExecutorService localExecutorService = Executors.newSingleThreadExecutor();
    this.mBackgroundExecutorService = localExecutorService;
    Looper localLooper = paramContext.getMainLooper();
    Handler localHandler = new Handler(localLooper);
    HandlerExecutor localHandlerExecutor = new HandlerExecutor(localHandler);
    this.mUiThreadExecutor = localHandlerExecutor;
    this.mContext = paramContext;
    ArrayListMultimap localArrayListMultimap = ArrayListMultimap.create();
    this.mCallbacks = localArrayListMultimap;
    CachedArtDownloadServiceConnection localCachedArtDownloadServiceConnection = UIStateManager.getInstance(this.mContext).getCachedArtDownloadServiceConnection();
    this.mArtDownloadConnector = localCachedArtDownloadServiceConnection;
  }

  private void getBitmap(final String paramString, final WeakReference<Callback> paramWeakReference)
  {
    ExecutorService localExecutorService = this.mBackgroundExecutorService;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        final BitmapDiskCache.Callback localCallback = (BitmapDiskCache.Callback)paramWeakReference.get();
        if (localCallback == null)
          return;
        Context localContext = BitmapDiskCache.this.mContext;
        String str = paramString;
        final Bitmap localBitmap = AlbumArtUtils.getBitmapFromDisk(localContext, str);
        if (localBitmap == null)
          return;
        Executor localExecutor = BitmapDiskCache.this.mUiThreadExecutor;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            ArrayListMultimap localArrayListMultimap = BitmapDiskCache.this.mCallbacks;
            String str1 = BitmapDiskCache.1.this.val$remoteUrl;
            WeakReference localWeakReference = BitmapDiskCache.1.this.val$wrapper;
            boolean bool = localArrayListMultimap.remove(str1, localWeakReference);
            BitmapDiskCache.Callback localCallback = localCallback;
            String str2 = BitmapDiskCache.1.this.val$remoteUrl;
            Bitmap localBitmap = localBitmap;
            localCallback.onBitmapResult(str2, localBitmap, null);
          }
        };
        localExecutor.execute(local1);
      }
    };
    localExecutorService.execute(local1);
  }

  public static BitmapDiskCache getInstance(Context paramContext)
  {
    MusicUtils.assertUiThread();
    if (sInstance == null)
      sInstance = new BitmapDiskCache(paramContext);
    return sInstance;
  }

  public void getBitmap(String paramString, Callback paramCallback)
  {
    MusicUtils.assertUiThread();
    WeakReference localWeakReference = new WeakReference(paramCallback);
    if (!this.mCallbacks.containsKey(paramString))
      this.mArtDownloadConnector.registerArtChangeListener(paramString, this);
    boolean bool = this.mCallbacks.put(paramString, localWeakReference);
    getBitmap(paramString, localWeakReference);
  }

  public void notifyArtChanged(String paramString)
  {
    MusicUtils.assertUiThread();
    this.mArtDownloadConnector.removeArtChangeListener(paramString, this);
    Iterator localIterator = this.mCallbacks.removeAll(paramString).iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      getBitmap(paramString, localWeakReference);
    }
  }

  public static abstract interface Callback
  {
    public abstract void onBitmapResult(String paramString, Bitmap paramBitmap, Exception paramException);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.BitmapDiskCache
 * JD-Core Version:    0.6.2
 */