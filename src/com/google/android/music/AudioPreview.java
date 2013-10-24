package com.google.android.music;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.music.eventlog.MusicEventLogger;
import java.io.IOException;

public class AudioPreview extends Activity
  implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener
{
  private AudioManager.OnAudioFocusChangeListener mAudioFocusListener;
  private AudioManager mAudioManager;
  private BroadcastReceiver mAudioNoisyReceiver;
  private int mDuration;
  private TextView mLoadingText;
  private long mMediaId = 65535L;
  private boolean mPausedByTransientLossOfFocus;
  private PreviewPlayer mPlayer;
  private Handler mProgressRefresher;
  private SeekBar mSeekBar;
  private SeekBar.OnSeekBarChangeListener mSeekListener;
  private boolean mSeeking = false;
  private TextView mTextLine1;
  private TextView mTextLine2;
  private MusicEventLogger mTracker;
  private Uri mUri;

  public AudioPreview()
  {
    AudioManager.OnAudioFocusChangeListener local2 = new AudioManager.OnAudioFocusChangeListener()
    {
      public void onAudioFocusChange(int paramAnonymousInt)
      {
        if (AudioPreview.this.mPlayer == null)
        {
          int i = AudioPreview.this.mAudioManager.abandonAudioFocus(this);
          return;
        }
        switch (paramAnonymousInt)
        {
        case 0:
        default:
        case -1:
        case -3:
        case -2:
        case 1:
        }
        while (true)
        {
          AudioPreview.this.updatePlayPause();
          return;
          boolean bool1 = AudioPreview.access$602(AudioPreview.this, false);
          AudioPreview.this.mPlayer.pause();
          continue;
          if (AudioPreview.this.mPlayer.isPlaying())
          {
            boolean bool2 = AudioPreview.access$602(AudioPreview.this, true);
            AudioPreview.this.mPlayer.pause();
            continue;
            if (AudioPreview.this.mPausedByTransientLossOfFocus)
            {
              boolean bool3 = AudioPreview.access$602(AudioPreview.this, false);
              AudioPreview.this.start();
            }
          }
        }
      }
    };
    this.mAudioFocusListener = local2;
    SeekBar.OnSeekBarChangeListener local3 = new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramAnonymousSeekBar, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        if (!paramAnonymousBoolean)
          return;
        AudioPreview.this.mPlayer.seekTo(paramAnonymousInt);
      }

      public void onStartTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        boolean bool = AudioPreview.access$902(AudioPreview.this, true);
      }

      public void onStopTrackingTouch(SeekBar paramAnonymousSeekBar)
      {
        boolean bool = AudioPreview.access$902(AudioPreview.this, false);
      }
    };
    this.mSeekListener = local3;
    BroadcastReceiver local4 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        if (paramAnonymousIntent == null)
          return;
        String str = paramAnonymousIntent.getAction();
        if (!"android.media.AUDIO_BECOMING_NOISY".equals(str))
          return;
        if (!AudioPreview.this.mPlayer.isPlaying())
          return;
        AudioPreview.this.mPlayer.pause();
        AudioPreview.this.updatePlayPause();
      }
    };
    this.mAudioNoisyReceiver = local4;
  }

  private void showPostPrepareUI()
  {
    int i = 1;
    ((ProgressBar)findViewById(2131296352)).setVisibility(8);
    int j = this.mPlayer.getDuration();
    this.mDuration = j;
    if (this.mDuration != 0)
    {
      SeekBar localSeekBar1 = this.mSeekBar;
      int k = this.mDuration;
      localSeekBar1.setMax(k);
      this.mSeekBar.setVisibility(0);
    }
    SeekBar localSeekBar2 = this.mSeekBar;
    SeekBar.OnSeekBarChangeListener localOnSeekBarChangeListener = this.mSeekListener;
    localSeekBar2.setOnSeekBarChangeListener(localOnSeekBarChangeListener);
    this.mLoadingText.setVisibility(8);
    findViewById(2131296355).setVisibility(0);
    AudioManager localAudioManager = this.mAudioManager;
    AudioManager.OnAudioFocusChangeListener localOnAudioFocusChangeListener = this.mAudioFocusListener;
    int m = localAudioManager.requestAudioFocus(localOnAudioFocusChangeListener, 3, 2);
    if (1 != m);
    while (true)
    {
      if (i == 0)
      {
        int n = Log.e("AudioPreview", "showPostPrepareUI did not obtain audio focus");
        this.mPausedByTransientLossOfFocus = false;
        this.mPlayer.pause();
      }
      Handler localHandler = this.mProgressRefresher;
      ProgressRefresher localProgressRefresher = new ProgressRefresher();
      boolean bool = localHandler.postDelayed(localProgressRefresher, 200L);
      updatePlayPause();
      return;
      i = 0;
    }
  }

  private void start()
  {
    int i = 1;
    AudioManager localAudioManager = this.mAudioManager;
    AudioManager.OnAudioFocusChangeListener localOnAudioFocusChangeListener = this.mAudioFocusListener;
    int j = localAudioManager.requestAudioFocus(localOnAudioFocusChangeListener, 3, 2);
    if (i != j);
    while (i == 0)
    {
      int k = Log.e("AudioPreview", "start did not obtain audio focus");
      return;
      i = 0;
    }
    this.mPlayer.start();
    Handler localHandler = this.mProgressRefresher;
    ProgressRefresher localProgressRefresher = new ProgressRefresher();
    boolean bool = localHandler.postDelayed(localProgressRefresher, 200L);
  }

  private void stopPlayback()
  {
    if (this.mProgressRefresher != null)
      this.mProgressRefresher.removeCallbacksAndMessages(null);
    if (this.mPlayer == null)
      return;
    this.mPlayer.release();
    this.mPlayer = null;
    AudioManager localAudioManager = this.mAudioManager;
    AudioManager.OnAudioFocusChangeListener localOnAudioFocusChangeListener = this.mAudioFocusListener;
    int i = localAudioManager.abandonAudioFocus(localOnAudioFocusChangeListener);
  }

  private void updatePlayPause()
  {
    ImageButton localImageButton = (ImageButton)findViewById(2131296356);
    if (localImageButton == null)
      return;
    if (this.mPlayer.isPlaying())
    {
      localImageButton.setImageResource(2130837723);
      return;
    }
    localImageButton.setImageResource(2130837728);
    this.mProgressRefresher.removeCallbacksAndMessages(null);
  }

  public String getPageUrlForTracking()
  {
    return "audioPreview";
  }

  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    SeekBar localSeekBar = this.mSeekBar;
    int i = this.mDuration;
    localSeekBar.setProgress(i);
    updatePlayPause();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    Intent localIntent1 = getIntent();
    if (localIntent1 == null)
    {
      finish();
      return;
    }
    Uri localUri1 = localIntent1.getData();
    this.mUri = localUri1;
    if (this.mUri == null)
    {
      finish();
      return;
    }
    String str1 = this.mUri.getScheme();
    setVolumeControlStream(3);
    boolean bool = requestWindowFeature(1);
    setContentView(2130968605);
    TextView localTextView1 = (TextView)findViewById(2131296347);
    this.mTextLine1 = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296348);
    this.mTextLine2 = localTextView2;
    TextView localTextView3 = (TextView)findViewById(2131296353);
    this.mLoadingText = localTextView3;
    PreviewPlayer localPreviewPlayer1;
    if (str1.equals("http"))
    {
      Object[] arrayOfObject = new Object[1];
      String str2 = this.mUri.getHost();
      arrayOfObject[0] = str2;
      String str3 = getString(2131230936, arrayOfObject);
      this.mLoadingText.setText(str3);
      SeekBar localSeekBar = (SeekBar)findViewById(2131296354);
      this.mSeekBar = localSeekBar;
      Handler localHandler = new Handler();
      this.mProgressRefresher = localHandler;
      AudioManager localAudioManager = (AudioManager)getSystemService("audio");
      this.mAudioManager = localAudioManager;
      localPreviewPlayer1 = (PreviewPlayer)getLastNonConfigurationInstance();
      if (localPreviewPlayer1 != null)
        break label473;
      PreviewPlayer localPreviewPlayer2 = new PreviewPlayer(null);
      this.mPlayer = localPreviewPlayer2;
      this.mPlayer.setActivity(this);
    }
    while (true)
    {
      AsyncQueryHandler local1;
      try
      {
        PreviewPlayer localPreviewPlayer3 = this.mPlayer;
        Uri localUri2 = this.mUri;
        localPreviewPlayer3.setDataSourceAndPrepare(localUri2);
        ContentResolver localContentResolver = getContentResolver();
        local1 = new AsyncQueryHandler(localContentResolver)
        {
          protected void onQueryComplete(int paramAnonymousInt, Object paramAnonymousObject, Cursor paramAnonymousCursor)
          {
            int m;
            if ((paramAnonymousCursor != null) && (paramAnonymousCursor.moveToFirst()))
            {
              int i = paramAnonymousCursor.getColumnIndex("title");
              int j = paramAnonymousCursor.getColumnIndex("artist");
              int k = paramAnonymousCursor.getColumnIndex("_id");
              m = paramAnonymousCursor.getColumnIndex("_display_name");
              if (k >= 0)
              {
                AudioPreview localAudioPreview = AudioPreview.this;
                long l1 = paramAnonymousCursor.getLong(k);
                long l2 = AudioPreview.access$102(localAudioPreview, l1);
              }
              if (i >= 0)
              {
                String str1 = paramAnonymousCursor.getString(i);
                AudioPreview.this.mTextLine1.setText(str1);
                if (j >= 0)
                {
                  String str2 = paramAnonymousCursor.getString(j);
                  AudioPreview.this.mTextLine2.setText(str2);
                }
              }
            }
            while (true)
            {
              if (paramAnonymousCursor != null)
                paramAnonymousCursor.close();
              AudioPreview.this.setNames();
              return;
              if (m >= 0)
              {
                String str3 = paramAnonymousCursor.getString(m);
                AudioPreview.this.mTextLine1.setText(str3);
              }
              else
              {
                int n = Log.w("AudioPreview", "Cursor had no names for us");
                continue;
                int i1 = Log.w("AudioPreview", "empty cursor");
              }
            }
          }
        };
        if (!str1.equals("content"))
          break label526;
        if (this.mUri.getAuthority() != "media")
          break label504;
        Uri localUri3 = this.mUri;
        String[] arrayOfString1 = new String[2];
        arrayOfString1[0] = "title";
        arrayOfString1[1] = "artist";
        local1.startQuery(0, null, localUri3, arrayOfString1, null, null, null);
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.media.AUDIO_BECOMING_NOISY");
        BroadcastReceiver localBroadcastReceiver = this.mAudioNoisyReceiver;
        Intent localIntent2 = registerReceiver(localBroadcastReceiver, localIntentFilter);
        return;
        this.mLoadingText.setVisibility(8);
      }
      catch (Exception localException)
      {
        String str4 = "Failed to open file: " + localException;
        int i = Log.d("AudioPreview", str4);
        Toast.makeText(this, 2131230932, 0).show();
        finish();
        return;
      }
      label473: this.mPlayer = localPreviewPlayer1;
      this.mPlayer.setActivity(this);
      if (this.mPlayer.isPrepared())
      {
        showPostPrepareUI();
        continue;
        label504: Uri localUri4 = this.mUri;
        local1.startQuery(0, null, localUri4, null, null, null, null);
        continue;
        label526: if (str1.equals("file"))
        {
          String str5 = this.mUri.getPath();
          Uri localUri5 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
          String[] arrayOfString2 = new String[3];
          arrayOfString2[0] = "_id";
          arrayOfString2[1] = "title";
          arrayOfString2[2] = "artist";
          String[] arrayOfString3 = new String[1];
          arrayOfString3[0] = str5;
          local1.startQuery(0, null, localUri5, arrayOfString2, "_data=?", arrayOfString3, null);
        }
        else if (this.mPlayer.isPrepared())
        {
          setNames();
        }
      }
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    boolean bool = super.onCreateOptionsMenu(paramMenu);
    MenuItem localMenuItem = paramMenu.add(0, 1, 0, "open in music");
    return true;
  }

  public void onDestroy()
  {
    stopPlayback();
    BroadcastReceiver localBroadcastReceiver = this.mAudioNoisyReceiver;
    unregisterReceiver(localBroadcastReceiver);
    super.onDestroy();
  }

  public boolean onError(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
  {
    Toast.makeText(this, 2131230932, 0).show();
    finish();
    return true;
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    switch (paramInt)
    {
    default:
      bool = super.onKeyDown(paramInt, paramKeyEvent);
    case 87:
    case 88:
    case 89:
    case 90:
    case 79:
    case 85:
    case 126:
    case 127:
    case 4:
    case 86:
    }
    while (true)
    {
      return bool;
      if (this.mPlayer.isPlaying())
        this.mPlayer.pause();
      while (true)
      {
        updatePlayPause();
        break;
        start();
      }
      start();
      updatePlayPause();
      continue;
      if (this.mPlayer.isPlaying())
        this.mPlayer.pause();
      updatePlayPause();
      continue;
      stopPlayback();
      finish();
    }
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    int i = 1;
    MenuItem localMenuItem1 = paramMenu.findItem(i);
    if (this.mMediaId >= 0L)
      MenuItem localMenuItem2 = localMenuItem1.setVisible(true);
    while (true)
    {
      return i;
      MenuItem localMenuItem3 = localMenuItem1.setVisible(false);
      int j = 0;
    }
  }

  public void onPrepared(MediaPlayer paramMediaPlayer)
  {
    if (isFinishing())
      return;
    PreviewPlayer localPreviewPlayer = (PreviewPlayer)paramMediaPlayer;
    this.mPlayer = localPreviewPlayer;
    setNames();
    this.mPlayer.start();
    showPostPrepareUI();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str1 = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = "uri";
    String str2 = this.mUri.toString();
    arrayOfObject[1] = str2;
    localMusicEventLogger.trackScreenView(this, str1, arrayOfObject);
  }

  public Object onRetainNonConfigurationInstance()
  {
    PreviewPlayer localPreviewPlayer = this.mPlayer;
    this.mPlayer = null;
    return localPreviewPlayer;
  }

  public void onUserLeaveHint()
  {
    stopPlayback();
    finish();
    super.onUserLeaveHint();
  }

  public void playPauseClicked(View paramView)
  {
    if (this.mPlayer.isPlaying())
      this.mPlayer.pause();
    while (true)
    {
      updatePlayPause();
      return;
      start();
    }
  }

  public void setNames()
  {
    if (TextUtils.isEmpty(this.mTextLine1.getText()))
    {
      TextView localTextView = this.mTextLine1;
      String str = this.mUri.getLastPathSegment();
      localTextView.setText(str);
    }
    if (TextUtils.isEmpty(this.mTextLine2.getText()))
    {
      this.mTextLine2.setVisibility(8);
      return;
    }
    this.mTextLine2.setVisibility(0);
  }

  private static class PreviewPlayer extends MediaPlayer
    implements MediaPlayer.OnPreparedListener
  {
    AudioPreview mActivity;
    boolean mIsPrepared = false;

    boolean isPrepared()
    {
      return this.mIsPrepared;
    }

    public void onPrepared(MediaPlayer paramMediaPlayer)
    {
      this.mIsPrepared = true;
      this.mActivity.onPrepared(paramMediaPlayer);
    }

    public void setActivity(AudioPreview paramAudioPreview)
    {
      this.mActivity = paramAudioPreview;
      setOnPreparedListener(this);
      AudioPreview localAudioPreview1 = this.mActivity;
      setOnErrorListener(localAudioPreview1);
      AudioPreview localAudioPreview2 = this.mActivity;
      setOnCompletionListener(localAudioPreview2);
    }

    public void setDataSourceAndPrepare(Uri paramUri)
      throws IllegalArgumentException, SecurityException, IllegalStateException, IOException
    {
      AudioPreview localAudioPreview = this.mActivity;
      setDataSource(localAudioPreview, paramUri);
      prepareAsync();
    }
  }

  class ProgressRefresher
    implements Runnable
  {
    ProgressRefresher()
    {
    }

    public void run()
    {
      if ((AudioPreview.this.mPlayer != null) && (!AudioPreview.this.mSeeking) && (AudioPreview.this.mDuration != 0))
      {
        SeekBar localSeekBar = AudioPreview.this.mSeekBar;
        int i = AudioPreview.this.mPlayer.getCurrentPosition();
        localSeekBar.setProgress(i);
      }
      AudioPreview.this.mProgressRefresher.removeCallbacksAndMessages(null);
      Handler localHandler = AudioPreview.this.mProgressRefresher;
      AudioPreview localAudioPreview = AudioPreview.this;
      ProgressRefresher localProgressRefresher = new ProgressRefresher(localAudioPreview);
      boolean bool = localHandler.postDelayed(localProgressRefresher, 200L);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.AudioPreview
 * JD-Core Version:    0.6.2
 */