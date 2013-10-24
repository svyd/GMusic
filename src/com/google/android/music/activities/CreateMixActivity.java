package com.google.android.music.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.mix.MixDescriptor;
import com.google.android.music.mix.MixDescriptor.Type;
import com.google.android.music.mix.MixGenerationState;
import com.google.android.music.mix.MixGenerationState.State;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;

public class CreateMixActivity extends Activity
  implements View.OnClickListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private AsyncMixCreatorWorker mAsyncWorker;
  private Context mContext;
  private View mDivider;
  private boolean mIflRadio;
  private MediaController mMediaController;
  BroadcastReceiver mMixGenerationReceiver;
  private Button mOkButton;
  private MediaPlayer.OnCompletionListener mOnCompletionListener;
  private MediaPlayer.OnErrorListener mOnErrorListener;
  private MediaPlayer.OnPreparedListener mOnPreparedListener;
  private TextView mText;
  private MusicEventLogger mTracker;
  private VideoView mVideoView;

  public CreateMixActivity()
  {
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        boolean bool = CreateMixActivity.this.mAsyncWorker.sendEmptyMessage(2);
      }
    };
    this.mMixGenerationReceiver = local1;
    MediaPlayer.OnPreparedListener local2 = new MediaPlayer.OnPreparedListener()
    {
      public void onPrepared(MediaPlayer paramAnonymousMediaPlayer)
      {
        int i = CreateMixActivity.this.mMediaController.getChildCount();
        int j = 0;
        while (true)
        {
          if (j >= i)
            return;
          CreateMixActivity.this.mMediaController.getChildAt(j).setVisibility(8);
          j += 1;
        }
      }
    };
    this.mOnPreparedListener = local2;
    MediaPlayer.OnCompletionListener local3 = new MediaPlayer.OnCompletionListener()
    {
      public void onCompletion(MediaPlayer paramAnonymousMediaPlayer)
      {
        CreateMixActivity.this.mVideoView.seekTo(0);
        CreateMixActivity.this.mVideoView.start();
      }
    };
    this.mOnCompletionListener = local3;
    MediaPlayer.OnErrorListener local4 = new MediaPlayer.OnErrorListener()
    {
      public boolean onError(MediaPlayer paramAnonymousMediaPlayer, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        CreateMixActivity.this.showFallbackImage();
        return true;
      }
    };
    this.mOnErrorListener = local4;
  }

  private void showFallbackImage()
  {
    ImageView localImageView = (ImageView)findViewById(2131296384);
    this.mVideoView.setVisibility(8);
    if (this.mIflRadio);
    for (int i = 2130837704; ; i = 2130837736)
    {
      localImageView.setImageResource(i);
      localImageView.setVisibility(0);
      return;
    }
  }

  public String getPageUrlForTracking()
  {
    return "createMix";
  }

  public void onClick(View paramView)
  {
    Button localButton = this.mOkButton;
    if (paramView != localButton)
      return;
    boolean bool = this.mAsyncWorker.sendEmptyMessage(5);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    setResult(0);
    setContentView(2130968610);
    Button localButton = (Button)findViewById(2131296387);
    this.mOkButton = localButton;
    this.mOkButton.setOnClickListener(this);
    TextView localTextView = (TextView)findViewById(2131296386);
    this.mText = localTextView;
    View localView = findViewById(2131296381);
    this.mDivider = localView;
    this.mContext = this;
    int i = getIntent().getIntExtra("mixType", -1);
    int j = MixDescriptor.Type.LUCKY_RADIO.ordinal();
    boolean bool1;
    int k;
    if (i != j)
    {
      bool1 = true;
      this.mIflRadio = bool1;
      getWindow().setFormat(-1);
      Uri.Builder localBuilder1 = new Uri.Builder();
      Uri.Builder localBuilder2 = localBuilder1.scheme("android.resource");
      String str1 = getPackageName();
      Uri.Builder localBuilder3 = localBuilder1.authority(str1);
      if (!this.mIflRadio)
        break label409;
      k = 2131165184;
      label175: String str2 = Integer.toString(k);
      Uri.Builder localBuilder4 = localBuilder1.path(str2);
      Uri localUri = localBuilder1.build();
      MediaController localMediaController1 = new MediaController(this);
      this.mMediaController = localMediaController1;
      VideoView localVideoView1 = (VideoView)findViewById(2131296385);
      this.mVideoView = localVideoView1;
      VideoView localVideoView2 = this.mVideoView;
      MediaController localMediaController2 = this.mMediaController;
      localVideoView2.setMediaController(localMediaController2);
      VideoView localVideoView3 = this.mVideoView;
      MediaPlayer.OnPreparedListener localOnPreparedListener = this.mOnPreparedListener;
      localVideoView3.setOnPreparedListener(localOnPreparedListener);
      VideoView localVideoView4 = this.mVideoView;
      MediaPlayer.OnCompletionListener localOnCompletionListener = this.mOnCompletionListener;
      localVideoView4.setOnCompletionListener(localOnCompletionListener);
      VideoView localVideoView5 = this.mVideoView;
      MediaPlayer.OnErrorListener localOnErrorListener = this.mOnErrorListener;
      localVideoView5.setOnErrorListener(localOnErrorListener);
      this.mVideoView.setVideoURI(localUri);
      this.mVideoView.setZOrderOnTop(true);
      if (!UIStateManager.getInstance().getPrefs().isRadioAnimationEnabled())
        break label417;
      this.mVideoView.start();
    }
    while (true)
    {
      AsyncMixCreatorWorker localAsyncMixCreatorWorker = new AsyncMixCreatorWorker();
      this.mAsyncWorker = localAsyncMixCreatorWorker;
      IntentFilter localIntentFilter = new IntentFilter();
      localIntentFilter.addAction("com.google.android.music.mix.generationfinished");
      BroadcastReceiver localBroadcastReceiver = this.mMixGenerationReceiver;
      Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter);
      boolean bool2 = this.mAsyncWorker.sendEmptyMessage(1);
      return;
      bool1 = false;
      break;
      label409: k = 2131165185;
      break label175;
      label417: showFallbackImage();
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    BroadcastReceiver localBroadcastReceiver = this.mMixGenerationReceiver;
    unregisterReceiver(localBroadcastReceiver);
    this.mAsyncWorker.quit();
  }

  protected void onPause()
  {
    super.onPause();
    UIStateManager.getInstance().onPause();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
    UIStateManager.getInstance().onResume();
  }

  private class AsyncMixCreatorWorker extends LoggableHandler
  {
    private MixDescriptor mMix;

    public AsyncMixCreatorWorker()
    {
      super();
    }

    private void onFailure()
    {
      CreateMixActivity localCreateMixActivity = CreateMixActivity.this;
      Runnable local3 = new Runnable()
      {
        public void run()
        {
          if (UIStateManager.getInstance().getPrefs().isNautilusEnabled());
          for (String str = CreateMixActivity.this.getString(2131231164); ; str = CreateMixActivity.this.getString(2131231165))
          {
            CreateMixActivity.this.mText.setText(str);
            CreateMixActivity.this.mOkButton.setVisibility(0);
            CreateMixActivity.this.mDivider.setVisibility(0);
            return;
          }
        }
      };
      localCreateMixActivity.runOnUiThread(local3);
    }

    private void onSuccess()
    {
      if (CreateMixActivity.this.isFinishing())
        return;
      AppNavigation.openNowPlayingDrawer(CreateMixActivity.this);
      CreateMixActivity.this.setResult(-1);
      CreateMixActivity.this.finish();
    }

    private void startMixCreation()
    {
      Intent localIntent = CreateMixActivity.this.getIntent();
      long l1 = localIntent.getLongExtra("localId", 65535L);
      String str1 = localIntent.getStringExtra("remoteId");
      long l2 = localIntent.getLongExtra("radioLocalId", 65535L);
      String str2 = localIntent.getStringExtra("name");
      String str3 = localIntent.getStringExtra("artLocation");
      int i = localIntent.getIntExtra("mixType", -1);
      if ((l1 == 65535L) && (TextUtils.isEmpty(str1)) && (l2 == 65535L))
      {
        int j = MixDescriptor.Type.LUCKY_RADIO.ordinal();
        if (i != j)
          throw new IllegalArgumentException("Invalid seed id to play the mix.");
      }
      if (i == -1)
        throw new IllegalArgumentException("Invalid mix type");
      if (str2 == null)
      {
        int k = MixDescriptor.Type.LUCKY_RADIO.ordinal();
        if (i != k)
          throw new IllegalArgumentException("Invalid mix name");
      }
      if (str1 != null)
      {
        MixDescriptor.Type localType1 = MixDescriptor.Type.values()[i];
        String str4 = str1;
        MixDescriptor localMixDescriptor1 = new MixDescriptor(str4, localType1, str2, str3);
        this.mMix = localMixDescriptor1;
      }
      IMusicPlaybackService localIMusicPlaybackService;
      while (true)
      {
        if (CreateMixActivity.LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("Creating mix: ");
          MixDescriptor localMixDescriptor2 = this.mMix;
          String str5 = localMixDescriptor2;
          int m = Log.d("CreateMixActivity", str5);
        }
        localIMusicPlaybackService = MusicUtils.sService;
        if (localIMusicPlaybackService != null)
          break label419;
        MixDescriptor localMixDescriptor3 = this.mMix;
        Message localMessage1 = Message.obtain(this, 4, localMixDescriptor3);
        boolean bool1 = sendMessage(localMessage1);
        return;
        if (l1 != 65535L)
        {
          MixDescriptor.Type localType2 = MixDescriptor.Type.values()[i];
          MixDescriptor localMixDescriptor4 = new MixDescriptor(l1, localType2, str2, str3);
          this.mMix = localMixDescriptor4;
        }
        else if (l2 != 65535L)
        {
          MixDescriptor.Type localType3 = MixDescriptor.Type.values()[i];
          String str6 = str2;
          String str7 = str3;
          MixDescriptor localMixDescriptor5 = new MixDescriptor(l2, localType3, str6, str7);
          this.mMix = localMixDescriptor5;
        }
        else
        {
          int n = MixDescriptor.Type.LUCKY_RADIO.ordinal();
          if (i == n)
            break;
          MixDescriptor localMixDescriptor6 = MixDescriptor.getLuckyRadio(CreateMixActivity.this.mContext);
          this.mMix = localMixDescriptor6;
        }
      }
      throw new IllegalStateException("Missing mix seed information");
      label419: String str8;
      try
      {
        MixDescriptor localMixDescriptor7 = this.mMix;
        localIMusicPlaybackService.openMix(localMixDescriptor7);
        str8 = this.mMix.getName();
        if (str8 == null)
        {
          MixDescriptor localMixDescriptor8 = this.mMix;
          Message localMessage2 = Message.obtain(this, 4, localMixDescriptor8);
          boolean bool2 = sendMessage(localMessage2);
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        MixDescriptor localMixDescriptor9 = this.mMix;
        Message localMessage3 = Message.obtain(this, 4, localMixDescriptor9);
        boolean bool3 = sendMessage(localMessage3);
        return;
      }
      CreateMixActivity localCreateMixActivity = CreateMixActivity.this;
      AsyncMixCreatorWorker localAsyncMixCreatorWorker = this;
      final String str9 = str8;
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
          {
            TextView localTextView1 = CreateMixActivity.this.mText;
            Context localContext1 = CreateMixActivity.this.mContext;
            Object[] arrayOfObject1 = new Object[1];
            String str1 = str9;
            arrayOfObject1[0] = str1;
            String str2 = localContext1.getString(2131231162, arrayOfObject1);
            localTextView1.setText(str2);
            return;
          }
          TextView localTextView2 = CreateMixActivity.this.mText;
          Context localContext2 = CreateMixActivity.this.mContext;
          Object[] arrayOfObject2 = new Object[1];
          String str3 = str9;
          arrayOfObject2[0] = str3;
          String str4 = localContext2.getString(2131231163, arrayOfObject2);
          localTextView2.setText(str4);
        }
      };
      localCreateMixActivity.runOnUiThread(local2);
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown message type: ");
        int i = paramMessage.what;
        String str1 = i;
        throw new IllegalArgumentException(str1);
      case 1:
        startMixCreation();
        return;
      case 2:
        localIMusicPlaybackService = MusicUtils.sService;
        if (localIMusicPlaybackService == null)
        {
          int j = Log.w("CreateMixActivity", "Failed to get service instance");
          return;
        }
        MixGenerationState localMixGenerationState2;
        try
        {
          MixGenerationState localMixGenerationState1 = localIMusicPlaybackService.getMixState();
          localMixGenerationState2 = localMixGenerationState1;
          if (localMixGenerationState2 == null)
          {
            int k = Log.w("CreateMixActivity", "Failed to retrieve mix state");
            return;
          }
        }
        catch (RemoteException localRemoteException1)
        {
          int m = Log.w("CreateMixActivity", "Failed to call the music playback service");
          return;
        }
        MixDescriptor localMixDescriptor1 = this.mMix;
        if (!localMixGenerationState2.isMyMix(localMixDescriptor1))
        {
          Object[] arrayOfObject = new Object[2];
          arrayOfObject[0] = localMixGenerationState2;
          MixDescriptor localMixDescriptor2 = this.mMix;
          arrayOfObject[1] = localMixDescriptor2;
          String str2 = String.format("Mix state %s for different mix: %s", arrayOfObject);
          int n = Log.w("CreateMixActivity", str2);
          return;
        }
        MixGenerationState.State localState1 = localMixGenerationState2.getState();
        MixGenerationState.State localState2 = MixGenerationState.State.FINISHED;
        if (localState1 == localState2)
        {
          boolean bool1 = sendEmptyMessage(3);
          return;
        }
        MixGenerationState.State localState3 = localMixGenerationState2.getState();
        MixGenerationState.State localState4 = MixGenerationState.State.FAILED;
        if (localState3 != localState4)
          return;
        boolean bool2 = sendEmptyMessage(4);
        return;
      case 4:
        onFailure();
        return;
      case 3:
        onSuccess();
        return;
      case 5:
      }
      IMusicPlaybackService localIMusicPlaybackService = MusicUtils.sService;
      try
      {
        localIMusicPlaybackService.cancelMix();
        CreateMixActivity localCreateMixActivity = CreateMixActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            CreateMixActivity.this.setResult(0);
            CreateMixActivity.this.finish();
          }
        };
        localCreateMixActivity.runOnUiThread(local1);
        return;
      }
      catch (RemoteException localRemoteException2)
      {
        while (true)
          int i1 = Log.w("CreateMixActivity", "Failed to cancel mix creation");
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.activities.CreateMixActivity
 * JD-Core Version:    0.6.2
 */