package com.google.android.music.ringtone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.FadingColorDrawable;
import com.google.android.music.PlayPauseButton;
import com.google.android.music.ringtone.soundfile.CheapSoundFile;
import com.google.android.music.ringtone.soundfile.CheapSoundFile.ProgressListener;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.store.MusicRingtoneManager;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.widgets.ActionBarItem;
import java.io.File;
import java.io.IOException;

public class RingtoneEditActivity extends Activity
  implements View.OnClickListener, MarkerView.MarkerListener, WaveformView.WaveformListener
{
  private static final boolean LOGD = Log.isLoggable("MusicRingtones", 2);
  private static final String[] sCursorCols = arrayOfString;
  private String mAlbum;
  private AsyncAlbumArtImageView mAlbumCover;
  private long mAlbumId;
  private String mArtist;
  private TextView mArtistNameView;
  private int mAutoScrollDirection = 1;
  private boolean mAutoScrollOnEdge;
  private boolean mCanSeekAccurately;
  private String mCaption = "";
  private ActionBarItem mDoneButton;
  private int mEndPos;
  private String mExtension;
  private ImageButton mFfwdButton;
  private File mFile;
  private String mFilename;
  private int mFlingVelocity;
  private Handler mHandler;
  private TextView mInfo;
  private boolean mIsPlaying;
  private boolean mKeyDown;
  private final CheapSoundFile.ProgressListener mLoadListener;
  private final Thread mLoadSongThread;
  private boolean mLoadingKeepGoing;
  private long mLoadingLastUpdateTime;
  private int mMarkerHeight = 100;
  private boolean mMarkerTouched = false;
  private int mMarkerWidth = 100;
  private int mMaxPos;
  private long mMusicId = 65535L;
  private int mOffset;
  private int mOffsetGoal;
  private final Thread mPlayBackThread;
  private PlayPauseButton mPlayButton;
  private int mPlayEndMsec;
  private int mPlayStartMsec;
  private int mPlayStartOffset;
  private MediaPlayer mPlayer;
  private ProgressDialog mProgressDialog;
  private float mRatio;
  private String mRecordingFilename;
  private Uri mRecordingUri;
  private MarkerView mResizeMarker;
  private ImageButton mRewindButton;
  private TextView mSongNameView;
  private CheapSoundFile mSoundFile;
  private MarkerView mStartMarker;
  private int mStartPos;
  private String mTitle;
  private ActionBarItem mTopbar;
  private boolean mTouchDragging;
  private int mTouchInitialEndPos;
  private int mTouchInitialOffset;
  private int mTouchInitialStartPos;
  private float mTouchStart;
  private WaveformView mWaveformView;
  private int mWidth;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "title";
    arrayOfString[1] = "album";
    arrayOfString[2] = "album_id";
    arrayOfString[3] = "artist";
  }

  public RingtoneEditActivity()
  {
    CheapSoundFile.ProgressListener local1 = new CheapSoundFile.ProgressListener()
    {
      public boolean reportProgress(double paramAnonymousDouble)
      {
        long l1 = System.currentTimeMillis();
        long l2 = RingtoneEditActivity.this.mLoadingLastUpdateTime;
        if (l1 - l2 > 100L)
        {
          ProgressDialog localProgressDialog = RingtoneEditActivity.this.mProgressDialog;
          int i = (int)(RingtoneEditActivity.this.mProgressDialog.getMax() * paramAnonymousDouble);
          localProgressDialog.setProgress(i);
          long l3 = RingtoneEditActivity.access$002(RingtoneEditActivity.this, l1);
        }
        return RingtoneEditActivity.this.mLoadingKeepGoing;
      }
    };
    this.mLoadListener = local1;
    Thread local2 = new Thread()
    {
      public void run()
      {
        RingtoneEditActivity localRingtoneEditActivity = RingtoneEditActivity.this;
        boolean bool1 = SeekTest.CanSeekAccurately(RingtoneEditActivity.this.getPreferences(0));
        boolean bool2 = RingtoneEditActivity.access$302(localRingtoneEditActivity, bool1);
        if (RingtoneEditActivity.LOGD)
          int i = Log.i("MusicRingtones", "Seek test done, creating media player.");
        try
        {
          MediaPlayer localMediaPlayer1 = new MediaPlayer();
          String str = RingtoneEditActivity.this.mFile.getAbsolutePath();
          localMediaPlayer1.setDataSource(str);
          localMediaPlayer1.setAudioStreamType(3);
          localMediaPlayer1.prepare();
          MediaPlayer localMediaPlayer2 = RingtoneEditActivity.access$602(RingtoneEditActivity.this, localMediaPlayer1);
          return;
        }
        catch (IOException localIOException)
        {
          int j = Log.e("MusicRingtones", "Read Error", localIOException);
        }
      }
    };
    this.mPlayBackThread = local2;
    Thread local3 = new Thread()
    {
      public void run()
      {
        try
        {
          RingtoneEditActivity localRingtoneEditActivity = RingtoneEditActivity.this;
          String str1 = RingtoneEditActivity.this.mFile.getAbsolutePath();
          CheapSoundFile.ProgressListener localProgressListener = RingtoneEditActivity.this.mLoadListener;
          CheapSoundFile localCheapSoundFile1 = CheapSoundFile.create(str1, localProgressListener);
          CheapSoundFile localCheapSoundFile2 = RingtoneEditActivity.access$702(localRingtoneEditActivity, localCheapSoundFile1);
          if (RingtoneEditActivity.this.mSoundFile == null)
          {
            RingtoneEditActivity.this.mProgressDialog.dismiss();
            String[] arrayOfString = RingtoneEditActivity.this.mFile.getName().toLowerCase().split("\\.");
            if (arrayOfString.length < 2);
            String str5;
            for (Object localObject = RingtoneEditActivity.this.getResources().getString(2131231237); ; localObject = str5)
            {
              String str2 = "UnsupportedExtension: " + (String)localObject;
              int i = Log.e("MusicRingtones", str2);
              return;
              StringBuilder localStringBuilder1 = new StringBuilder();
              String str3 = RingtoneEditActivity.this.getResources().getString(2131231236);
              StringBuilder localStringBuilder2 = localStringBuilder1.append(str3).append(" ");
              int j = arrayOfString.length + -1;
              String str4 = arrayOfString[j];
              str5 = str4;
            }
          }
        }
        catch (Exception localException)
        {
          RingtoneEditActivity.this.mProgressDialog.dismiss();
          TextView localTextView = RingtoneEditActivity.this.mInfo;
          String str6 = localException.toString();
          localTextView.setText(str6);
          int k = Log.e("MusicRingtones", "ReadError", localException);
          return;
        }
        RingtoneEditActivity.this.mProgressDialog.dismiss();
        if (RingtoneEditActivity.this.mLoadingKeepGoing)
        {
          Runnable local1 = new Runnable()
          {
            public void run()
            {
              RingtoneEditActivity.this.finishOpeningSoundFile();
            }
          };
          boolean bool = RingtoneEditActivity.this.mHandler.post(local1);
          return;
        }
        RingtoneEditActivity.this.finish();
      }
    };
    this.mLoadSongThread = local3;
  }

  private void afterSavingRingtone(final String paramString, File paramFile, int paramInt)
  {
    if (MusicRingtoneManager.insertRingtoneInMediaStore(this, paramFile, paramString) == 0)
    {
      Runnable local10 = new Runnable()
      {
        public void run()
        {
          String str1 = RingtoneEditActivity.this.getResources().getString(2131231230);
          Object[] arrayOfObject = new Object[1];
          String str2 = paramString;
          arrayOfObject[0] = str2;
          String str3 = String.format(str1, arrayOfObject);
          Toast.makeText(RingtoneEditActivity.this, str3, 0).show();
        }
      };
      runOnUiThread(local10);
    }
    while (true)
    {
      finish();
      return;
      Runnable local11 = new Runnable()
      {
        public void run()
        {
          String str1 = RingtoneEditActivity.this.getResources().getString(2131231231);
          Object[] arrayOfObject = new Object[1];
          String str2 = paramString;
          arrayOfObject[0] = str2;
          String str3 = String.format(str1, arrayOfObject);
          Toast.makeText(RingtoneEditActivity.this, str3, 0).show();
        }
      };
      runOnUiThread(local11);
    }
  }

  private void finishOpeningSoundFile()
  {
    WaveformView localWaveformView = this.mWaveformView;
    CheapSoundFile localCheapSoundFile = this.mSoundFile;
    localWaveformView.setSoundFile(localCheapSoundFile);
    this.mWaveformView.recomputeHeights();
    int i = this.mWaveformView.getmaxPos();
    this.mMaxPos = i;
    this.mTouchDragging = false;
    this.mOffset = 65436;
    this.mOffsetGoal = 65436;
    this.mFlingVelocity = 0;
    resetPositions();
    int j = this.mEndPos;
    int k = this.mMaxPos;
    if (j > k)
    {
      int m = this.mMaxPos;
      this.mEndPos = m;
    }
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.mSoundFile.getFiletype();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(", ");
    int n = this.mSoundFile.getSampleRate();
    StringBuilder localStringBuilder3 = localStringBuilder2.append(n).append(" Hz, ");
    int i1 = this.mSoundFile.getAvgBitrateKbps();
    String str2 = i1 + " kbps, ";
    this.mCaption = str2;
    TextView localTextView = this.mInfo;
    String str3 = this.mCaption;
    localTextView.setText(str3);
    updateDisplay();
  }

  private String formatDecimal(double paramDouble)
  {
    int i = (int)paramDouble;
    double d1 = i;
    double d2 = paramDouble - d1;
    int j = (int)(100.0D * d2 + 0.5D);
    if (j >= 100)
    {
      i += 1;
      j += -100;
      if (j < 10)
        j *= 10;
    }
    if (j < 10);
    for (String str = i + ".0" + j; ; str = i + "." + j)
      return str;
  }

  private String formatTime(int paramInt)
  {
    double d;
    if ((this.mWaveformView != null) && (this.mWaveformView.isInitialized()))
      d = this.mWaveformView.pixelsToSeconds(paramInt);
    for (String str = formatDecimal(d); ; str = "")
      return str;
  }

  private String getDefaultRingtoneName()
  {
    String str1 = getResources().getString(2131231225);
    Object[] arrayOfObject = new Object[1];
    String str2 = this.mTitle;
    arrayOfObject[0] = str2;
    return String.format(str1, arrayOfObject);
  }

  private String getExtensionFromFilename(String paramString)
  {
    int i = paramString.lastIndexOf('.');
    int j = paramString.length();
    return paramString.substring(i, j);
  }

  private String getFilenameFromUri(Uri paramUri)
  {
    Object localObject1 = null;
    RingtoneEditActivity localRingtoneEditActivity = this;
    Uri localUri = paramUri;
    Object localObject2 = localObject1;
    Object localObject3 = localObject1;
    Cursor localCursor = localRingtoneEditActivity.managedQuery(localUri, (String[])localObject1, "", localObject2, localObject3);
    if (localCursor.getCount() == 0);
    while (true)
    {
      return localObject1;
      boolean bool = localCursor.moveToFirst();
      int i = localCursor.getColumnIndexOrThrow("_data");
      localObject1 = localCursor.getString(i);
    }
  }

  private int getMinLengthInPixel()
  {
    return this.mWaveformView.millisecsToPixels(5000);
  }

  /** @deprecated */
  private void handlePause()
  {
    try
    {
      if ((this.mPlayer != null) && (this.mPlayer.isPlaying()))
        this.mPlayer.pause();
      this.mWaveformView.setPlayback(-1);
      this.mIsPlaying = false;
      updatePlayButtonImage();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private boolean isInAutoScrollRegion(float paramFloat)
  {
    boolean bool = true;
    float f = this.mWidth + -40;
    if (paramFloat >= f)
      this.mAutoScrollDirection = 1;
    while (true)
    {
      return bool;
      if (paramFloat < 40.0F)
        this.mAutoScrollDirection = -1;
      else
        bool = false;
    }
  }

  private void loadFromFile()
  {
    String str1 = this.mFilename;
    File localFile = new File(str1);
    this.mFile = localFile;
    String str2 = this.mFilename;
    String str3 = getExtensionFromFilename(str2);
    this.mExtension = str3;
    long l = System.currentTimeMillis();
    this.mLoadingLastUpdateTime = l;
    this.mLoadingKeepGoing = true;
    ProgressDialog localProgressDialog1 = new ProgressDialog(this);
    this.mProgressDialog = localProgressDialog1;
    this.mProgressDialog.setProgressStyle(1);
    this.mProgressDialog.setTitle(2131230943);
    this.mProgressDialog.setCancelable(true);
    ProgressDialog localProgressDialog2 = this.mProgressDialog;
    DialogInterface.OnCancelListener local5 = new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramAnonymousDialogInterface)
      {
        boolean bool = RingtoneEditActivity.access$202(RingtoneEditActivity.this, false);
      }
    };
    localProgressDialog2.setOnCancelListener(local5);
    this.mProgressDialog.show();
    this.mCanSeekAccurately = false;
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        RingtoneEditActivity.this.queryTrackInfo();
      }
    });
    this.mPlayBackThread.start();
    this.mLoadSongThread.start();
  }

  private void loadGui()
  {
    setContentView(2130968672);
    ActionBarItem localActionBarItem1 = (ActionBarItem)findViewById(2131296518);
    this.mTopbar = localActionBarItem1;
    this.mTopbar.setOnClickListener(this);
    ActionBarItem localActionBarItem2 = (ActionBarItem)findViewById(2131296522);
    this.mDoneButton = localActionBarItem2;
    this.mDoneButton.setOnClickListener(this);
    ImageButton localImageButton1 = (ImageButton)findViewById(2131296515);
    this.mRewindButton = localImageButton1;
    ImageButton localImageButton2 = this.mRewindButton;
    prepButton(localImageButton2);
    PlayPauseButton localPlayPauseButton1 = (PlayPauseButton)findViewById(2131296516);
    this.mPlayButton = localPlayPauseButton1;
    PlayPauseButton localPlayPauseButton2 = this.mPlayButton;
    prepButton(localPlayPauseButton2);
    ImageButton localImageButton3 = (ImageButton)findViewById(2131296517);
    this.mFfwdButton = localImageButton3;
    ImageButton localImageButton4 = this.mFfwdButton;
    prepButton(localImageButton4);
    updatePlayButtonImage();
    WaveformView localWaveformView1 = (WaveformView)findViewById(2131296526);
    this.mWaveformView = localWaveformView1;
    this.mWaveformView.setActivity(this);
    this.mWaveformView.setListener(this);
    TextView localTextView1 = (TextView)findViewById(2131296512);
    this.mInfo = localTextView1;
    TextView localTextView2 = this.mInfo;
    String str = this.mCaption;
    localTextView2.setText(str);
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296523);
    this.mAlbumCover = localAsyncAlbumArtImageView;
    TextView localTextView3 = (TextView)findViewById(2131296524);
    this.mSongNameView = localTextView3;
    TextView localTextView4 = (TextView)findViewById(2131296525);
    this.mArtistNameView = localTextView4;
    this.mMaxPos = 0;
    if (this.mSoundFile != null)
    {
      WaveformView localWaveformView2 = this.mWaveformView;
      CheapSoundFile localCheapSoundFile = this.mSoundFile;
      localWaveformView2.setSoundFile(localCheapSoundFile);
      this.mWaveformView.recomputeHeights();
      int i = this.mWaveformView.getmaxPos();
      this.mMaxPos = i;
    }
    int j = (int)getResources().getDimension(2131558437);
    this.mMarkerWidth = j;
    MarkerView localMarkerView1 = (MarkerView)findViewById(2131296527);
    this.mStartMarker = localMarkerView1;
    this.mStartMarker.setListener(this);
    this.mStartMarker.setFocusable(true);
    this.mStartMarker.setFocusableInTouchMode(true);
    MarkerView localMarkerView2 = (MarkerView)findViewById(2131296528);
    this.mResizeMarker = localMarkerView2;
    this.mResizeMarker.setListener(this);
    this.mResizeMarker.setFocusable(true);
    this.mResizeMarker.setFocusableInTouchMode(true);
    updateDisplay();
  }

  /** @deprecated */
  // ERROR //
  private void onPlay(int paramInt)
  {
    // Byte code:
    //   0: ldc2_w 565
    //   3: istore_2
    //   4: aload_0
    //   5: monitorenter
    //   6: aload_0
    //   7: getfield 422	com/google/android/music/ringtone/RingtoneEditActivity:mIsPlaying	Z
    //   10: ifeq +10 -> 20
    //   13: aload_0
    //   14: invokespecial 206	com/google/android/music/ringtone/RingtoneEditActivity:handlePause	()V
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: aload_0
    //   21: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   24: istore_3
    //   25: iload_3
    //   26: ifeq -9 -> 17
    //   29: aload_0
    //   30: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   33: iload_1
    //   34: invokevirtual 569	com/google/android/music/ringtone/WaveformView:pixelsToMillisecs	(I)I
    //   37: istore 4
    //   39: aload_0
    //   40: iload 4
    //   42: putfield 571	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartMsec	I
    //   45: aload_0
    //   46: getfield 573	com/google/android/music/ringtone/RingtoneEditActivity:mStartPos	I
    //   49: istore 5
    //   51: iload_1
    //   52: iload 5
    //   54: if_icmpge +364 -> 418
    //   57: aload_0
    //   58: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   61: astore 6
    //   63: aload_0
    //   64: getfield 573	com/google/android/music/ringtone/RingtoneEditActivity:mStartPos	I
    //   67: istore 7
    //   69: aload 6
    //   71: iload 7
    //   73: invokevirtual 569	com/google/android/music/ringtone/WaveformView:pixelsToMillisecs	(I)I
    //   76: istore 8
    //   78: aload_0
    //   79: iload 8
    //   81: putfield 575	com/google/android/music/ringtone/RingtoneEditActivity:mPlayEndMsec	I
    //   84: aload_0
    //   85: iconst_0
    //   86: putfield 577	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartOffset	I
    //   89: aload_0
    //   90: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   93: astore 9
    //   95: aload_0
    //   96: getfield 571	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartMsec	I
    //   99: i2d
    //   100: ldc2_w 578
    //   103: dmul
    //   104: dstore 10
    //   106: aload 9
    //   108: dload 10
    //   110: invokevirtual 583	com/google/android/music/ringtone/WaveformView:secondsToFrames	(D)I
    //   113: istore 12
    //   115: aload_0
    //   116: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   119: astore 13
    //   121: aload_0
    //   122: getfield 575	com/google/android/music/ringtone/RingtoneEditActivity:mPlayEndMsec	I
    //   125: i2d
    //   126: ldc2_w 578
    //   129: dmul
    //   130: dstore 14
    //   132: aload 13
    //   134: dload 14
    //   136: invokevirtual 583	com/google/android/music/ringtone/WaveformView:secondsToFrames	(D)I
    //   139: istore 16
    //   141: aload_0
    //   142: getfield 239	com/google/android/music/ringtone/RingtoneEditActivity:mSoundFile	Lcom/google/android/music/ringtone/soundfile/CheapSoundFile;
    //   145: iload 12
    //   147: invokevirtual 586	com/google/android/music/ringtone/soundfile/CheapSoundFile:getSeekableFrameOffset	(I)I
    //   150: istore_2
    //   151: aload_0
    //   152: getfield 239	com/google/android/music/ringtone/RingtoneEditActivity:mSoundFile	Lcom/google/android/music/ringtone/soundfile/CheapSoundFile;
    //   155: iload 16
    //   157: invokevirtual 586	com/google/android/music/ringtone/soundfile/CheapSoundFile:getSeekableFrameOffset	(I)I
    //   160: istore 17
    //   162: aload_0
    //   163: getfield 225	com/google/android/music/ringtone/RingtoneEditActivity:mCanSeekAccurately	Z
    //   166: istore_3
    //   167: iload_3
    //   168: ifeq +101 -> 269
    //   171: iload_2
    //   172: iflt +97 -> 269
    //   175: iload 17
    //   177: iflt +92 -> 269
    //   180: aload_0
    //   181: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   184: invokevirtual 589	android/media/MediaPlayer:reset	()V
    //   187: aload_0
    //   188: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   191: iconst_3
    //   192: invokevirtual 592	android/media/MediaPlayer:setAudioStreamType	(I)V
    //   195: aload_0
    //   196: getfield 231	com/google/android/music/ringtone/RingtoneEditActivity:mFile	Ljava/io/File;
    //   199: invokevirtual 595	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   202: astore 18
    //   204: new 597	java/io/FileInputStream
    //   207: dup
    //   208: aload 18
    //   210: invokespecial 598	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   213: astore 19
    //   215: aload_0
    //   216: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   219: astore 20
    //   221: aload 19
    //   223: invokevirtual 602	java/io/FileInputStream:getFD	()Ljava/io/FileDescriptor;
    //   226: astore 21
    //   228: iload_2
    //   229: i2l
    //   230: lstore 22
    //   232: iload 17
    //   234: iload_2
    //   235: isub
    //   236: i2l
    //   237: lstore 24
    //   239: aload 20
    //   241: aload 21
    //   243: lload 22
    //   245: lload 24
    //   247: invokevirtual 606	android/media/MediaPlayer:setDataSource	(Ljava/io/FileDescriptor;JJ)V
    //   250: aload_0
    //   251: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   254: invokevirtual 609	android/media/MediaPlayer:prepare	()V
    //   257: aload_0
    //   258: getfield 571	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartMsec	I
    //   261: istore 26
    //   263: aload_0
    //   264: iload 26
    //   266: putfield 577	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartOffset	I
    //   269: aload_0
    //   270: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   273: astore 27
    //   275: new 30	com/google/android/music/ringtone/RingtoneEditActivity$7
    //   278: dup
    //   279: aload_0
    //   280: invokespecial 610	com/google/android/music/ringtone/RingtoneEditActivity$7:<init>	(Lcom/google/android/music/ringtone/RingtoneEditActivity;)V
    //   283: astore 28
    //   285: aload 27
    //   287: aload 28
    //   289: invokevirtual 614	android/media/MediaPlayer:setOnCompletionListener	(Landroid/media/MediaPlayer$OnCompletionListener;)V
    //   292: aload_0
    //   293: iconst_1
    //   294: putfield 422	com/google/android/music/ringtone/RingtoneEditActivity:mIsPlaying	Z
    //   297: aload_0
    //   298: getfield 577	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartOffset	I
    //   301: ifne +22 -> 323
    //   304: aload_0
    //   305: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   308: astore 29
    //   310: aload_0
    //   311: getfield 571	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartMsec	I
    //   314: istore 30
    //   316: aload 29
    //   318: iload 30
    //   320: invokevirtual 617	android/media/MediaPlayer:seekTo	(I)V
    //   323: aload_0
    //   324: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   327: invokevirtual 618	android/media/MediaPlayer:start	()V
    //   330: aload_0
    //   331: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   334: invokevirtual 621	android/media/MediaPlayer:getCurrentPosition	()I
    //   337: istore 31
    //   339: aload_0
    //   340: getfield 577	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartOffset	I
    //   343: istore 32
    //   345: iload 31
    //   347: iload 32
    //   349: iadd
    //   350: istore 33
    //   352: aload_0
    //   353: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   356: iload 33
    //   358: invokevirtual 408	com/google/android/music/ringtone/WaveformView:millisecsToPixels	(I)I
    //   361: istore 34
    //   363: aload_0
    //   364: getfield 429	com/google/android/music/ringtone/RingtoneEditActivity:mWidth	I
    //   367: iconst_4
    //   368: idiv
    //   369: istore 35
    //   371: iload 34
    //   373: iload 35
    //   375: isub
    //   376: istore 36
    //   378: aload_0
    //   379: iload 36
    //   381: invokespecial 624	com/google/android/music/ringtone/RingtoneEditActivity:setOffsetGoalNoUpdate	(I)V
    //   384: aload_0
    //   385: invokespecial 198	com/google/android/music/ringtone/RingtoneEditActivity:updateDisplay	()V
    //   388: aload_0
    //   389: invokespecial 425	com/google/android/music/ringtone/RingtoneEditActivity:updatePlayButtonImage	()V
    //   392: goto -375 -> 17
    //   395: astore_3
    //   396: ldc_w 625
    //   399: istore 37
    //   401: aload_0
    //   402: aload_3
    //   403: iload 37
    //   405: invokespecial 629	com/google/android/music/ringtone/RingtoneEditActivity:showFinalAlert	(Ljava/lang/Exception;I)V
    //   408: goto -391 -> 17
    //   411: astore 38
    //   413: aload_0
    //   414: monitorexit
    //   415: aload 38
    //   417: athrow
    //   418: aload_0
    //   419: getfield 293	com/google/android/music/ringtone/RingtoneEditActivity:mEndPos	I
    //   422: istore 39
    //   424: iload_1
    //   425: iload 39
    //   427: if_icmple +33 -> 460
    //   430: aload_0
    //   431: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   434: astore 40
    //   436: aload_0
    //   437: getfield 279	com/google/android/music/ringtone/RingtoneEditActivity:mMaxPos	I
    //   440: istore 41
    //   442: aload 40
    //   444: iload 41
    //   446: invokevirtual 569	com/google/android/music/ringtone/WaveformView:pixelsToMillisecs	(I)I
    //   449: istore 42
    //   451: aload_0
    //   452: iload 42
    //   454: putfield 575	com/google/android/music/ringtone/RingtoneEditActivity:mPlayEndMsec	I
    //   457: goto -373 -> 84
    //   460: aload_0
    //   461: getfield 194	com/google/android/music/ringtone/RingtoneEditActivity:mWaveformView	Lcom/google/android/music/ringtone/WaveformView;
    //   464: astore 43
    //   466: aload_0
    //   467: getfield 293	com/google/android/music/ringtone/RingtoneEditActivity:mEndPos	I
    //   470: istore 44
    //   472: aload 43
    //   474: iload 44
    //   476: invokevirtual 569	com/google/android/music/ringtone/WaveformView:pixelsToMillisecs	(I)I
    //   479: istore 45
    //   481: aload_0
    //   482: iload 45
    //   484: putfield 575	com/google/android/music/ringtone/RingtoneEditActivity:mPlayEndMsec	I
    //   487: goto -403 -> 84
    //   490: astore 46
    //   492: ldc 118
    //   494: ldc_w 631
    //   497: invokestatic 635	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   500: istore 47
    //   502: aload_0
    //   503: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   506: invokevirtual 589	android/media/MediaPlayer:reset	()V
    //   509: aload_0
    //   510: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   513: iconst_3
    //   514: invokevirtual 592	android/media/MediaPlayer:setAudioStreamType	(I)V
    //   517: aload_0
    //   518: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   521: astore 48
    //   523: aload_0
    //   524: getfield 231	com/google/android/music/ringtone/RingtoneEditActivity:mFile	Ljava/io/File;
    //   527: invokevirtual 595	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   530: astore 49
    //   532: aload 48
    //   534: aload 49
    //   536: invokevirtual 637	android/media/MediaPlayer:setDataSource	(Ljava/lang/String;)V
    //   539: aload_0
    //   540: getfield 235	com/google/android/music/ringtone/RingtoneEditActivity:mPlayer	Landroid/media/MediaPlayer;
    //   543: invokevirtual 609	android/media/MediaPlayer:prepare	()V
    //   546: aload_0
    //   547: iconst_0
    //   548: putfield 577	com/google/android/music/ringtone/RingtoneEditActivity:mPlayStartOffset	I
    //   551: goto -282 -> 269
    //
    // Exception table:
    //   from	to	target	type
    //   29	167	395	java/lang/Exception
    //   269	392	395	java/lang/Exception
    //   418	551	395	java/lang/Exception
    //   6	17	411	finally
    //   20	25	411	finally
    //   29	167	411	finally
    //   180	269	411	finally
    //   269	392	411	finally
    //   401	408	411	finally
    //   418	551	411	finally
    //   180	269	490	java/lang/Exception
  }

  private void onSave()
  {
    if (this.mIsPlaying)
      handlePause();
    String str = getDefaultRingtoneName();
    saveRingtone(str);
  }

  private void prepButton(View paramView)
  {
    paramView.setOnClickListener(this);
    FadingColorDrawable localFadingColorDrawable = new FadingColorDrawable(this, paramView);
    paramView.setBackgroundDrawable(localFadingColorDrawable);
  }

  private void queryTrackInfo()
  {
    if (LOGD)
      int i = Log.w("MusicRingtones", "queryTrackInfo");
    Uri localUri = MusicContent.XAudio.getAudioUri(this.mMusicId);
    String[] arrayOfString1 = sCursorCols;
    RingtoneEditActivity localRingtoneEditActivity = this;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localRingtoneEditActivity, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (LOGD)
      int j = Log.i("MusicRingtones", "creating new cursor");
    if ((localCursor != null) && (localCursor.moveToFirst()))
    {
      String str2 = localCursor.getString(0);
      this.mTitle = str2;
      String str3 = localCursor.getString(1);
      this.mAlbum = str3;
      long l = localCursor.getLong(2);
      this.mAlbumId = l;
      String str4 = localCursor.getString(3);
      this.mArtist = str4;
      Runnable local12 = new Runnable()
      {
        public void run()
        {
          RingtoneEditActivity.this.updateInfoViews();
        }
      };
      runOnUiThread(local12);
    }
    Store.safeClose(localCursor);
  }

  private void resetPositions()
  {
    int i = this.mWaveformView.secondsToPixels(0.0D);
    this.mStartPos = i;
    int j = this.mWaveformView.secondsToPixels(30.0D);
    this.mEndPos = j;
  }

  private void saveRingtone(String paramString)
  {
    final File localFile = MusicRingtoneManager.getRingtoneFile(paramString);
    if (localFile == null)
    {
      int i = Log.e("MusicRingtones", "Failed to create ringtone file for writing.");
      return;
    }
    WaveformView localWaveformView1 = this.mWaveformView;
    int j = this.mStartPos;
    double d1 = localWaveformView1.pixelsToSeconds(j);
    WaveformView localWaveformView2 = this.mWaveformView;
    int k = this.mEndPos;
    double d2 = localWaveformView2.pixelsToSeconds(k);
    final int m = this.mWaveformView.secondsToFrames(d1);
    final int n = this.mWaveformView.secondsToFrames(d2);
    final int i1 = (int)(d2 - d1 + 0.5D);
    ProgressDialog localProgressDialog = new ProgressDialog(this);
    this.mProgressDialog = localProgressDialog;
    this.mProgressDialog.setProgressStyle(0);
    this.mProgressDialog.setTitle(2131231226);
    this.mProgressDialog.setIndeterminate(true);
    this.mProgressDialog.setCancelable(false);
    this.mProgressDialog.show();
    RingtoneEditActivity localRingtoneEditActivity = this;
    final String str = paramString;
    MusicUtils.runAsync(new Runnable()
    {
      public void run()
      {
        try
        {
          CheapSoundFile localCheapSoundFile = RingtoneEditActivity.this.mSoundFile;
          File localFile1 = localFile;
          int i = m;
          int j = n;
          int k = m;
          int m = j - k;
          localCheapSoundFile.WriteFile(localFile1, i, m);
          RingtoneEditActivity.this.mProgressDialog.dismiss();
          RingtoneEditActivity localRingtoneEditActivity = RingtoneEditActivity.this;
          String str = str;
          File localFile2 = localFile;
          int n = i1;
          localRingtoneEditActivity.afterSavingRingtone(str, localFile2, n);
          return;
        }
        catch (Exception localException)
        {
          RingtoneEditActivity.this.mProgressDialog.dismiss();
          int i1 = Log.e("MusicRingtones", "WriteError", localException);
        }
      }
    });
  }

  private void setOffsetGoal(int paramInt)
  {
    setOffsetGoalNoUpdate(paramInt);
    updateDisplay();
  }

  private void setOffsetGoalEnd()
  {
    int i = this.mEndPos;
    int j = this.mWidth / 2;
    int k = i - j;
    setOffsetGoal(k);
  }

  private void setOffsetGoalEndNoUpdate()
  {
    int i = this.mEndPos;
    int j = this.mWidth / 2;
    int k = i - j;
    setOffsetGoalNoUpdate(k);
  }

  private void setOffsetGoalNoUpdate(int paramInt)
  {
    if (this.mTouchDragging)
      return;
    this.mOffsetGoal = paramInt;
    int i = this.mOffsetGoal;
    int j = this.mMaxPos;
    int k = this.mWidth;
    int m = j - k;
    if (i > m)
    {
      int n = this.mMaxPos;
      int i1 = this.mWidth;
      int i2 = n - i1;
      this.mOffsetGoal = i2;
    }
    if (this.mOffsetGoal >= 0)
      return;
    this.mOffsetGoal = 0;
  }

  private void setOffsetGoalStart()
  {
    int i = this.mStartPos;
    int j = this.mWidth / 2;
    int k = i - j;
    setOffsetGoal(k);
  }

  private void setOffsetGoalStartNoUpdate()
  {
    int i = this.mStartPos;
    int j = this.mWidth / 2;
    int k = i - j;
    setOffsetGoalNoUpdate(k);
  }

  private boolean shouldHandleTouch(float paramFloat)
  {
    int i = this.mWaveformView.waveBottom();
    int j = this.mWaveformView.waveTop();
    float f = i - j + -60;
    if (paramFloat >= f);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void showFinalAlert(Exception paramException, int paramInt)
  {
    CharSequence localCharSequence = getResources().getText(paramInt);
    showFinalAlert(paramException, localCharSequence);
  }

  private void showFinalAlert(Exception paramException, CharSequence paramCharSequence)
  {
    if (paramException != null)
    {
      String str1 = "Error: " + paramCharSequence;
      int i = Log.e("Ringdroid", str1);
      Intent localIntent = new Intent();
      setResult(0, localIntent);
    }
    while (true)
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this).setMessage(paramCharSequence);
      DialogInterface.OnClickListener local8 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          RingtoneEditActivity.this.finish();
        }
      };
      AlertDialog localAlertDialog = localBuilder.setPositiveButton(2131231239, local8).setCancelable(false).show();
      return;
      String str2 = "Success: " + paramCharSequence;
      int j = Log.i("Ringdroid", str2);
    }
  }

  private int trap(int paramInt)
  {
    if (paramInt < 65436)
      paramInt = 65436;
    while (true)
    {
      return paramInt;
      int i = this.mMaxPos + 100;
      int j = this.mWidth;
      int k = i - j;
      if (paramInt > k)
      {
        int m = this.mMaxPos + 100;
        int n = this.mWidth;
        paramInt = m - n;
      }
    }
  }

  private int trapMarker(int paramInt)
  {
    if (paramInt < 0)
      paramInt = 0;
    while (true)
    {
      return paramInt;
      int i = this.mMaxPos;
      if (paramInt > i)
        paramInt = this.mMaxPos;
    }
  }

  private int trapStartMarker(int paramInt)
  {
    if (paramInt < 0)
      paramInt = 0;
    while (true)
    {
      return paramInt;
      int i = this.mMaxPos;
      int j = getMinLengthInPixel();
      int k = i - j;
      if (paramInt > k)
      {
        int m = this.mMaxPos;
        int n = getMinLengthInPixel();
        paramInt = m - n;
      }
    }
  }

  /** @deprecated */
  private void updateDisplay()
  {
    while (true)
    {
      try
      {
        if (this.mIsPlaying)
        {
          int i = this.mPlayer.getCurrentPosition();
          int j = this.mPlayStartOffset;
          int k = i + j;
          int m = this.mWaveformView.millisecsToPixels(k);
          this.mWaveformView.setPlayback(m);
          int n = this.mPlayEndMsec;
          if (k >= n)
            handlePause();
        }
        if (this.mAutoScrollOnEdge)
        {
          int i1 = this.mAutoScrollDirection * 10;
          int i2 = this.mStartPos + i1;
          int i3 = trapStartMarker(i2);
          this.mStartPos = i3;
          int i4 = this.mOffset + i1;
          this.mOffset = i4;
          int i5 = this.mEndPos + i1;
          int i6 = trapMarker(i5);
          this.mEndPos = i6;
          if (this.mStartPos != i2)
            this.mAutoScrollOnEdge = false;
          WaveformView localWaveformView = this.mWaveformView;
          int i7 = this.mStartPos;
          int i8 = this.mEndPos;
          int i9 = this.mOffset;
          localWaveformView.setParameters(i7, i8, i9);
          this.mWaveformView.invalidate();
          updateMarkers();
          return;
        }
        if (this.mTouchDragging)
          continue;
        if (this.mFlingVelocity == 0)
          break;
        i10 = this.mFlingVelocity / 30;
        if (this.mFlingVelocity > 80)
        {
          int i11 = this.mFlingVelocity + -80;
          this.mFlingVelocity = i11;
          int i12 = this.mOffset + i10;
          int i13 = trap(i12);
          this.mOffset = i13;
          if (this.mOffset != i12)
            this.mFlingVelocity = 0;
          int i14 = this.mOffset;
          this.mOffsetGoal = i14;
          continue;
        }
      }
      finally
      {
      }
      if (this.mFlingVelocity < 65456)
      {
        int i15 = this.mFlingVelocity + 80;
        this.mFlingVelocity = i15;
      }
      else
      {
        this.mFlingVelocity = 0;
      }
    }
    int i16 = this.mOffsetGoal;
    int i17 = this.mOffset;
    int i10 = i16 - i17;
    if (i10 > 10)
      i10 /= 10;
    while (true)
    {
      int i18 = this.mOffset + i10;
      this.mOffset = i18;
      break;
      if (i10 > 0)
        i10 = 1;
      else if (i10 < 65526)
        i10 /= 10;
      else if (i10 < 0)
        i10 = -1;
      else
        i10 = 0;
    }
  }

  private void updateInfoViews()
  {
    TextView localTextView1 = this.mSongNameView;
    String str1 = this.mTitle;
    localTextView1.setText(str1);
    TextView localTextView2 = this.mArtistNameView;
    String str2 = this.mArtist;
    localTextView2.setText(str2);
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = this.mAlbumCover;
    long l = this.mAlbumId;
    String str3 = this.mAlbum;
    String str4 = this.mArtist;
    localAsyncAlbumArtImageView.setAlbumId(l, str3, str4);
    String str5 = this.mTitle;
    if ((this.mArtist != null) && (this.mArtist.length() > 0))
    {
      StringBuilder localStringBuilder = new StringBuilder().append(str5).append(" - ");
      String str6 = this.mArtist;
      str5 = str6;
    }
    setTitle(str5);
  }

  private void updateMarkers()
  {
    MarkerView localMarkerView1 = this.mStartMarker;
    StringBuilder localStringBuilder1 = new StringBuilder();
    CharSequence localCharSequence1 = getResources().getText(2131231227);
    StringBuilder localStringBuilder2 = localStringBuilder1.append(localCharSequence1).append(" ");
    int i = this.mStartPos;
    String str1 = formatTime(i);
    String str2 = str1;
    localMarkerView1.setContentDescription(str2);
    MarkerView localMarkerView2 = this.mResizeMarker;
    StringBuilder localStringBuilder3 = new StringBuilder();
    CharSequence localCharSequence2 = getResources().getText(2131231228);
    StringBuilder localStringBuilder4 = localStringBuilder3.append(localCharSequence2).append(" ");
    int j = this.mEndPos;
    String str3 = formatTime(j);
    String str4 = str3;
    localMarkerView2.setContentDescription(str4);
    int k = this.mStartPos;
    int m = this.mOffset;
    int n = k - m;
    int i1 = this.mStartMarker.getWidth() / 2;
    int i2 = n - i1;
    int i3 = this.mEndPos;
    int i4 = this.mOffset;
    int i5 = i3 - i4;
    int i6 = this.mResizeMarker.getWidth() / 2;
    int i7 = i5 - i6;
    int i8 = this.mWaveformView.getHeight();
    int i9 = this.mWaveformView.sBorderYOffset;
    int i10 = i8 - i9;
    this.mMarkerHeight = i10;
    MarkerView localMarkerView3 = this.mStartMarker;
    int i11 = this.mMarkerWidth;
    int i12 = this.mMarkerHeight;
    int i13 = this.mWaveformView.sBorderYOffset;
    AbsoluteLayout.LayoutParams localLayoutParams1 = new AbsoluteLayout.LayoutParams(i11, i12, i2, i13);
    localMarkerView3.setLayoutParams(localLayoutParams1);
    MarkerView localMarkerView4 = this.mResizeMarker;
    int i14 = this.mMarkerWidth;
    int i15 = this.mMarkerHeight;
    int i16 = this.mWaveformView.sBorderYOffset;
    AbsoluteLayout.LayoutParams localLayoutParams2 = new AbsoluteLayout.LayoutParams(i14, i15, i7, i16);
    localMarkerView4.setLayoutParams(localLayoutParams2);
  }

  private void updatePlayButtonImage()
  {
    if (this.mIsPlaying)
    {
      this.mPlayButton.setCurrentPlayState(2);
      return;
    }
    this.mPlayButton.setCurrentPlayState(3);
  }

  boolean getMarkerTouched()
  {
    return this.mMarkerTouched;
  }

  public void markerDraw()
  {
  }

  public void markerEnter(MarkerView paramMarkerView)
  {
  }

  public void markerFocus(MarkerView paramMarkerView)
  {
    this.mKeyDown = false;
    MarkerView localMarkerView = this.mStartMarker;
    if (paramMarkerView == localMarkerView)
      setOffsetGoalStartNoUpdate();
    while (true)
    {
      updateDisplay();
      return;
      setOffsetGoalEndNoUpdate();
    }
  }

  public void markerKeyUp()
  {
    this.mKeyDown = false;
    updateDisplay();
  }

  public void markerLeft(MarkerView paramMarkerView, int paramInt)
  {
    this.mKeyDown = true;
    MarkerView localMarkerView1 = this.mStartMarker;
    if (paramMarkerView == localMarkerView1)
    {
      int i = this.mStartPos;
      int j = this.mStartPos - paramInt;
      int k = trap(j);
      this.mStartPos = k;
      int m = this.mEndPos;
      int n = this.mStartPos;
      int i1 = i - n;
      int i2 = m - i1;
      int i3 = trap(i2);
      this.mEndPos = i3;
      setOffsetGoalStart();
    }
    MarkerView localMarkerView2 = this.mResizeMarker;
    int i8;
    if (paramMarkerView == localMarkerView2)
    {
      int i4 = this.mEndPos;
      int i5 = this.mStartPos;
      if (i4 == i5)
        break label161;
      int i6 = this.mStartPos - paramInt;
      int i7 = trap(i6);
      this.mStartPos = i7;
      i8 = this.mStartPos;
    }
    label161: int i10;
    for (this.mEndPos = i8; ; this.mEndPos = i10)
    {
      setOffsetGoalEnd();
      updateDisplay();
      return;
      int i9 = this.mEndPos - paramInt;
      i10 = trap(i9);
    }
  }

  public void markerRight(MarkerView paramMarkerView, int paramInt)
  {
    this.mKeyDown = true;
    MarkerView localMarkerView1 = this.mStartMarker;
    if (paramMarkerView == localMarkerView1)
    {
      int i = this.mStartPos;
      int j = this.mStartPos + paramInt;
      this.mStartPos = j;
      int k = this.mStartPos;
      int m = this.mMaxPos;
      if (k > m)
      {
        int n = this.mMaxPos;
        this.mStartPos = n;
      }
      int i1 = this.mEndPos;
      int i2 = this.mStartPos - i;
      int i3 = i1 + i2;
      this.mEndPos = i3;
      int i4 = this.mEndPos;
      int i5 = this.mMaxPos;
      if (i4 > i5)
      {
        int i6 = this.mMaxPos;
        this.mEndPos = i6;
      }
      setOffsetGoalStart();
    }
    MarkerView localMarkerView2 = this.mResizeMarker;
    if (paramMarkerView == localMarkerView2)
    {
      int i7 = this.mEndPos + paramInt;
      this.mEndPos = i7;
      int i8 = this.mEndPos;
      int i9 = this.mMaxPos;
      if (i8 > i9)
      {
        int i10 = this.mMaxPos;
        this.mEndPos = i10;
      }
      setOffsetGoalEnd();
    }
    updateDisplay();
  }

  public boolean markerTouchEnd(MarkerView paramMarkerView, float paramFloat)
  {
    this.mTouchDragging = false;
    this.mMarkerTouched = false;
    this.mAutoScrollOnEdge = false;
    MarkerView localMarkerView = this.mStartMarker;
    if (paramMarkerView == localMarkerView)
      setOffsetGoalStart();
    while (true)
    {
      return true;
      setOffsetGoalEnd();
    }
  }

  public boolean markerTouchMove(MarkerView paramMarkerView, float paramFloat1, float paramFloat2)
  {
    boolean bool1 = true;
    if (!shouldHandleTouch(paramFloat2))
      bool1 = false;
    float f2;
    do
    {
      return bool1;
      float f1 = this.mTouchStart;
      f2 = paramFloat1 - f1;
      MarkerView localMarkerView = this.mStartMarker;
      if (paramMarkerView != localMarkerView)
        break;
      boolean bool2 = isInAutoScrollRegion(paramFloat1);
      this.mAutoScrollOnEdge = bool2;
    }
    while (this.mAutoScrollOnEdge);
    int i = (int)(this.mTouchInitialStartPos + f2);
    int j = trapStartMarker(i);
    this.mStartPos = j;
    if (this.mStartPos != i)
    {
      int k = (int)(this.mTouchInitialEndPos + f2);
      int m = trapMarker(k);
      this.mEndPos = m;
    }
    while (true)
    {
      updateDisplay();
      break;
      int n = (int)(this.mTouchInitialEndPos + f2);
      int i1 = trapMarker(n);
      this.mEndPos = i1;
      int i2 = this.mEndPos;
      int i3 = this.mStartPos;
      int i4 = getMinLengthInPixel();
      int i5 = i3 + i4;
      if (i2 < i5)
      {
        int i6 = this.mStartPos;
        int i7 = getMinLengthInPixel();
        int i8 = i6 + i7;
        this.mEndPos = i8;
      }
    }
  }

  public boolean markerTouchStart(MarkerView paramMarkerView, float paramFloat1, float paramFloat2)
  {
    boolean bool = true;
    if (!shouldHandleTouch(paramFloat2))
      bool = false;
    while (true)
    {
      return bool;
      this.mTouchDragging = true;
      this.mMarkerTouched = true;
      this.mTouchStart = paramFloat1;
      int i = this.mStartPos;
      this.mTouchInitialStartPos = i;
      int j = this.mEndPos;
      this.mTouchInitialEndPos = j;
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 2)
      return;
    if (paramInt1 != 1)
      return;
    if (paramInt2 != -1)
    {
      finish();
      return;
    }
    if (paramIntent == null)
    {
      finish();
      return;
    }
    Uri localUri1 = paramIntent.getData();
    this.mRecordingUri = localUri1;
    Uri localUri2 = this.mRecordingUri;
    String str1 = getFilenameFromUri(localUri2);
    this.mRecordingFilename = str1;
    String str2 = this.mRecordingFilename;
    this.mFilename = str2;
    loadFromFile();
  }

  public void onClick(View paramView)
  {
    ActionBarItem localActionBarItem1 = this.mTopbar;
    if (paramView == localActionBarItem1)
    {
      finish();
      return;
    }
    ActionBarItem localActionBarItem2 = this.mDoneButton;
    if (paramView == localActionBarItem2)
    {
      onSave();
      return;
    }
    ImageButton localImageButton1 = this.mRewindButton;
    int i;
    if (paramView == localImageButton1)
    {
      if (this.mIsPlaying)
      {
        i = this.mPlayer.getCurrentPosition() + -5000;
        int j = this.mPlayStartMsec;
        if (i < j)
          i = this.mPlayStartMsec;
        this.mPlayer.seekTo(i);
        return;
      }
      boolean bool1 = this.mStartMarker.requestFocus();
      MarkerView localMarkerView1 = this.mStartMarker;
      markerFocus(localMarkerView1);
      return;
    }
    PlayPauseButton localPlayPauseButton = this.mPlayButton;
    if (paramView == localPlayPauseButton)
    {
      int k = this.mStartPos;
      onPlay(k);
      return;
    }
    ImageButton localImageButton2 = this.mFfwdButton;
    if (paramView != localImageButton2)
      return;
    if (this.mIsPlaying)
    {
      i = this.mPlayer.getCurrentPosition() + 5000;
      int m = this.mPlayEndMsec;
      if (i > m)
        int n = this.mPlayEndMsec;
      this.mPlayer.seekTo(i);
      return;
    }
    boolean bool2 = this.mResizeMarker.requestFocus();
    MarkerView localMarkerView2 = this.mResizeMarker;
    markerFocus(localMarkerView2);
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    double d = this.mWaveformView.getZoomFactor();
    super.onConfigurationChanged(paramConfiguration);
    loadGui();
    updateInfoViews();
    Handler localHandler = this.mHandler;
    Runnable local4 = new Runnable()
    {
      public void run()
      {
        boolean bool = RingtoneEditActivity.this.mStartMarker.requestFocus();
        RingtoneEditActivity localRingtoneEditActivity = RingtoneEditActivity.this;
        MarkerView localMarkerView = RingtoneEditActivity.this.mStartMarker;
        localRingtoneEditActivity.markerFocus(localMarkerView);
        RingtoneEditActivity.this.mWaveformView.recomputeHeights();
        RingtoneEditActivity.this.updateDisplay();
      }
    };
    boolean bool = localHandler.postDelayed(local4, 500L);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool = requestWindowFeature(1);
    this.mRecordingFilename = null;
    this.mRecordingUri = null;
    this.mPlayer = null;
    this.mIsPlaying = false;
    Intent localIntent = getIntent();
    String str1 = localIntent.getData().toString();
    this.mFilename = str1;
    Bundle localBundle = localIntent.getExtras();
    if (localBundle != null)
    {
      long l1 = localBundle.getLong("musicId", 65535L);
      this.mMusicId = l1;
      StringBuilder localStringBuilder = new StringBuilder().append("Get music id: ");
      long l2 = this.mMusicId;
      String str2 = l2;
      int i = Log.w("MusicRingtones", str2);
    }
    this.mSoundFile = null;
    this.mKeyDown = false;
    Handler localHandler = new Handler();
    this.mHandler = localHandler;
    loadGui();
    if (this.mFilename.equals("record"))
      return;
    loadFromFile();
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    boolean bool = super.onCreateOptionsMenu(paramMenu);
    MenuItem localMenuItem1 = paramMenu.add(0, 1, 0, 2131230743);
    MenuItem localMenuItem2 = paramMenu.add(0, 2, 1, 2131231229);
    return true;
  }

  protected void onDestroy()
  {
    int i = Log.i("Ringdroid", "EditActivity OnDestroy");
    if ((this.mPlayer != null) && (this.mPlayer.isPlaying()))
      this.mPlayer.stop();
    this.mPlayer = null;
    if (this.mProgressDialog != null)
      this.mProgressDialog.dismiss();
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 62)
    {
      int i = this.mStartPos;
      onPlay(i);
    }
    for (boolean bool = true; ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      return bool;
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    boolean bool = true;
    switch (paramMenuItem.getItemId())
    {
    default:
      bool = false;
    case 1:
    case 2:
    }
    while (true)
    {
      return bool;
      onSave();
      continue;
      resetPositions();
      this.mOffsetGoal = 0;
      updateDisplay();
    }
  }

  void updatePosition()
  {
    int i = this.mWaveformView.getStart();
    this.mStartPos = i;
    int j = this.mWaveformView.getEnd();
    this.mEndPos = j;
    int k = this.mWaveformView.getmaxPos();
    this.mMaxPos = k;
  }

  public void waveformDoubleClick(float paramFloat1, float paramFloat2)
  {
    if (this.mIsPlaying)
    {
      WaveformView localWaveformView = this.mWaveformView;
      int i = (int)(this.mOffset + paramFloat1);
      int j = localWaveformView.pixelsToMillisecs(i);
      int k = this.mPlayStartMsec;
      if (j >= k)
      {
        int m = this.mPlayEndMsec;
        if (j < m)
        {
          MediaPlayer localMediaPlayer = this.mPlayer;
          int n = this.mPlayStartOffset;
          int i1 = j - n;
          localMediaPlayer.seekTo(i1);
          return;
        }
      }
      handlePause();
      return;
    }
    int i2 = (int)(this.mOffset + paramFloat1);
    onPlay(i2);
  }

  public void waveformDraw()
  {
    int i = this.mWaveformView.getWidth();
    this.mWidth = i;
    int j = this.mOffsetGoal;
    int k = this.mOffset;
    if ((j != k) && (!this.mKeyDown))
    {
      updateDisplay();
      return;
    }
    if (this.mIsPlaying)
    {
      updateDisplay();
      return;
    }
    if (this.mFlingVelocity == 0)
      return;
    updateDisplay();
  }

  public void waveformFling(float paramFloat)
  {
    this.mTouchDragging = false;
    int i = this.mOffset;
    this.mOffsetGoal = i;
    int j = (int)-paramFloat;
    this.mFlingVelocity = j;
    updateDisplay();
  }

  public void waveformScale(float paramFloat)
  {
    this.mRatio = paramFloat;
  }

  public void waveformTouchEnd()
  {
    if (this.mTouchDragging)
    {
      int i = this.mOffset;
      this.mOffsetGoal = i;
    }
    this.mTouchDragging = false;
  }

  public void waveformTouchMove(float paramFloat)
  {
    if (!this.mTouchDragging)
      return;
    float f1 = this.mTouchInitialOffset;
    float f2 = this.mTouchStart - paramFloat;
    float f3 = f1 + f2;
    float f4 = this.mRatio;
    int i = (int)(f3 * f4);
    int j = trap(i);
    this.mOffset = j;
    updateDisplay();
  }

  public void waveformTouchStart(float paramFloat)
  {
    this.mTouchDragging = true;
    this.mTouchStart = paramFloat;
    int i = this.mOffset;
    this.mTouchInitialOffset = i;
    this.mFlingVelocity = 0;
    this.mRatio = 1.0F;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.RingtoneEditActivity
 * JD-Core Version:    0.6.2
 */