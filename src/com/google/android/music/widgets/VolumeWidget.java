package com.google.android.music.widgets;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class VolumeWidget extends ImageView
  implements Runnable
{
  private static final int[] IMAGES = { 2130837692, 2130837693, 2130837694, 2130837695, 2130837696 };
  private static final boolean LOGV = Log.isLoggable("VolumeWidget", 2);
  private final AudioManager mAudioManager;
  private final Handler mHandler;
  private boolean mIsMute;
  private boolean mListeningForVolumeChanges;
  private final int mMaxVolumeIndex;
  private final BroadcastReceiver mVolumeChangeReceiver;
  private int mVolumeLevel;
  private final ContentObserver mVolumeObserver;

  public VolumeWidget(Context paramContext)
  {
    super(paramContext);
    Handler localHandler1 = new Handler();
    this.mHandler = localHandler1;
    this.mListeningForVolumeChanges = false;
    this.mIsMute = false;
    this.mVolumeLevel = -1;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        if (!"android.media.VOLUME_CHANGED_ACTION".equals(str))
          return;
        Bundle localBundle = paramAnonymousIntent.getExtras();
        int i = localBundle.getInt("android.media.EXTRA_VOLUME_STREAM_TYPE");
        if (3 != i)
          return;
        int j = localBundle.getInt("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
        VolumeWidget.this.requestUpdate();
      }
    };
    this.mVolumeChangeReceiver = local1;
    Handler localHandler2 = this.mHandler;
    ContentObserver local2 = new ContentObserver(localHandler2)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        super.onChange(paramAnonymousBoolean);
        VolumeWidget.this.requestUpdate();
      }
    };
    this.mVolumeObserver = local2;
    AudioManager localAudioManager = getAudioManager(paramContext);
    this.mAudioManager = localAudioManager;
    int i = this.mAudioManager.getStreamMaxVolume(3);
    this.mMaxVolumeIndex = i;
  }

  public VolumeWidget(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Handler localHandler1 = new Handler();
    this.mHandler = localHandler1;
    this.mListeningForVolumeChanges = false;
    this.mIsMute = false;
    this.mVolumeLevel = -1;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        if (!"android.media.VOLUME_CHANGED_ACTION".equals(str))
          return;
        Bundle localBundle = paramAnonymousIntent.getExtras();
        int i = localBundle.getInt("android.media.EXTRA_VOLUME_STREAM_TYPE");
        if (3 != i)
          return;
        int j = localBundle.getInt("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
        VolumeWidget.this.requestUpdate();
      }
    };
    this.mVolumeChangeReceiver = local1;
    Handler localHandler2 = this.mHandler;
    ContentObserver local2 = new ContentObserver(localHandler2)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        super.onChange(paramAnonymousBoolean);
        VolumeWidget.this.requestUpdate();
      }
    };
    this.mVolumeObserver = local2;
    AudioManager localAudioManager = getAudioManager(paramContext);
    this.mAudioManager = localAudioManager;
    int i = this.mAudioManager.getStreamMaxVolume(3);
    this.mMaxVolumeIndex = i;
  }

  public VolumeWidget(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Handler localHandler1 = new Handler();
    this.mHandler = localHandler1;
    this.mListeningForVolumeChanges = false;
    this.mIsMute = false;
    this.mVolumeLevel = -1;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        if (!"android.media.VOLUME_CHANGED_ACTION".equals(str))
          return;
        Bundle localBundle = paramAnonymousIntent.getExtras();
        int i = localBundle.getInt("android.media.EXTRA_VOLUME_STREAM_TYPE");
        if (3 != i)
          return;
        int j = localBundle.getInt("android.media.EXTRA_VOLUME_STREAM_VALUE", -1);
        VolumeWidget.this.requestUpdate();
      }
    };
    this.mVolumeChangeReceiver = local1;
    Handler localHandler2 = this.mHandler;
    ContentObserver local2 = new ContentObserver(localHandler2)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        super.onChange(paramAnonymousBoolean);
        VolumeWidget.this.requestUpdate();
      }
    };
    this.mVolumeObserver = local2;
    AudioManager localAudioManager = getAudioManager(paramContext);
    this.mAudioManager = localAudioManager;
    int i = this.mAudioManager.getStreamMaxVolume(3);
    this.mMaxVolumeIndex = i;
  }

  private void cancelUpdate()
  {
    this.mHandler.removeCallbacks(this);
  }

  private static AudioManager getAudioManager(Context paramContext)
  {
    return (AudioManager)paramContext.getSystemService("audio");
  }

  private void requestUpdate()
  {
    this.mHandler.removeCallbacks(this);
    boolean bool = this.mHandler.post(this);
  }

  private void startListeningForVolumeUpdate()
  {
    if (LOGV)
      int i = Log.d("VolumeWidget", "Start listening");
    requestUpdate();
    if (this.mListeningForVolumeChanges)
      return;
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
    Context localContext = getContext();
    BroadcastReceiver localBroadcastReceiver = this.mVolumeChangeReceiver;
    Intent localIntent = localContext.registerReceiver(localBroadcastReceiver, localIntentFilter);
    ContentResolver localContentResolver = getContext().getContentResolver();
    Uri localUri = Settings.System.getUriFor(Settings.System.VOLUME_SETTINGS[3]);
    ContentObserver localContentObserver = this.mVolumeObserver;
    localContentResolver.registerContentObserver(localUri, false, localContentObserver);
    this.mListeningForVolumeChanges = true;
  }

  private void stopListeningForVolumeUpdate()
  {
    if (LOGV)
      int i = Log.d("VolumeWidget", "Stop listening");
    if (!this.mListeningForVolumeChanges)
      return;
    Context localContext = getContext();
    BroadcastReceiver localBroadcastReceiver = this.mVolumeChangeReceiver;
    localContext.unregisterReceiver(localBroadcastReceiver);
    ContentResolver localContentResolver = getContext().getContentResolver();
    ContentObserver localContentObserver = this.mVolumeObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
    cancelUpdate();
    this.mListeningForVolumeChanges = false;
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    startListeningForVolumeUpdate();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    stopListeningForVolumeUpdate();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    startListeningForVolumeUpdate();
  }

  protected void onVisibilityChanged(View paramView, int paramInt)
  {
    super.onVisibilityChanged(paramView, paramInt);
    if (paramInt == 0)
    {
      startListeningForVolumeUpdate();
      return;
    }
    stopListeningForVolumeUpdate();
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    String str1;
    if (LOGV)
    {
      str1 = "VolumeWidget";
      if (!paramBoolean)
        break label28;
    }
    label28: for (String str2 = "gained focus"; ; str2 = "lost focus")
    {
      int i = Log.d(str1, str2);
      requestUpdate();
      return;
    }
  }

  protected void onWindowVisibilityChanged(int paramInt)
  {
    super.onWindowVisibilityChanged(paramInt);
    if (paramInt == 0)
    {
      startListeningForVolumeUpdate();
      return;
    }
    stopListeningForVolumeUpdate();
  }

  public void run()
  {
    updateVolumeIndicator();
  }

  public void updateVolumeIndicator()
  {
    updateVolumeIndicator(-1);
  }

  public void updateVolumeIndicator(int paramInt)
  {
    int i = IMAGES.length;
    if (paramInt == -1)
      paramInt = this.mAudioManager.getStreamVolume(3);
    int j = this.mMaxVolumeIndex;
    boolean bool1;
    if (paramInt > j)
    {
      paramInt = this.mMaxVolumeIndex;
      float f1 = this.mMaxVolumeIndex;
      float f2 = paramInt / f1;
      float f3 = i;
      int k = (int)(f2 * f3);
      if (k >= i)
        k = i + -1;
      if (paramInt != 0)
        break label146;
      bool1 = true;
      label80: int m = this.mVolumeLevel;
      if (k != m)
      {
        boolean bool2 = this.mIsMute;
        if (bool1 != bool2)
          return;
      }
      this.mVolumeLevel = k;
      this.mIsMute = bool1;
      if (!this.mIsMute)
        break label152;
    }
    label146: label152: int[] arrayOfInt;
    int i1;
    for (int n = 2130837697; ; n = arrayOfInt[i1])
    {
      setImageResource(n);
      return;
      if (paramInt >= 0)
        break;
      paramInt = 0;
      break;
      bool1 = false;
      break label80;
      arrayOfInt = IMAGES;
      i1 = this.mVolumeLevel;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.VolumeWidget
 * JD-Core Version:    0.6.2
 */