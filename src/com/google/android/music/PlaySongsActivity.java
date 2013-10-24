package com.google.android.music;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.NautilusSingleSongList;
import com.google.android.music.medialist.SingleSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.tutorial.TutorialUtils;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncWorkers;

public class PlaySongsActivity extends Activity
  implements View.OnClickListener
{
  private AsyncCheckupWorker mAsyncWorker;
  private ContentObserver mContentObserver;
  private CheckState mCurrentState = null;
  private String mMarketAccount;
  private String mMusicAccount;
  private MusicPreferences mMusicPreferences;
  private Button mPositiveButton;
  private ProgressBar mProgressBar;
  private Button mSecondaryButton;
  private String mStoreId;
  private SyncStatusObserver mSyncObserver;
  private Object mSyncObserverHandle;
  private TextView mTextView;
  private MusicEventLogger mTracker;

  public PlaySongsActivity()
  {
    SyncStatusObserver local2 = new SyncStatusObserver()
    {
      public void onStatusChanged(int paramAnonymousInt)
      {
        if (PlaySongsActivity.this.isSyncActive())
          return;
        boolean bool = PlaySongsActivity.this.mAsyncWorker.sendEmptyMessage(3);
      }
    };
    this.mSyncObserver = local2;
    LoggableHandler localLoggableHandler = AsyncWorkers.sUIBackgroundWorker;
    ContentObserver local3 = new ContentObserver(localLoggableHandler)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        boolean bool = PlaySongsActivity.this.mAsyncWorker.sendEmptyMessage(2);
      }
    };
    this.mContentObserver = local3;
  }

  private boolean doesMarketAccountMatchMusicAccount()
  {
    boolean bool = false;
    String str1 = getIntent().getStringExtra("authAccount");
    this.mMarketAccount = str1;
    Account localAccount;
    if (this.mMarketAccount != null)
    {
      localAccount = this.mMusicPreferences.getSyncAccount();
      if (localAccount == null)
      {
        MusicEventLogger localMusicEventLogger = this.mTracker;
        Object[] arrayOfObject = new Object[0];
        localMusicEventLogger.trackEvent("signUpFromStoreListen", arrayOfObject);
        if (!TutorialUtils.launchTutorialOnDemand(this))
          finish();
      }
    }
    while (true)
    {
      return bool;
      String str2 = localAccount.name;
      String str3 = this.mMarketAccount;
      if (!str2.equalsIgnoreCase(str3))
      {
        String str4 = localAccount.name;
        this.mMusicAccount = str4;
        continue;
        int i = Log.w("PlaySongsAct", "Market did not provide the account name");
      }
      else
      {
        bool = true;
      }
    }
  }

  private boolean isSyncActive()
  {
    Account localAccount = this.mMusicPreferences.getStreamingAccount();
    if (localAccount == null);
    for (boolean bool = false; ; bool = ContentResolver.isSyncActive(localAccount, "com.google.android.music.MusicContent"))
      return bool;
  }

  /** @deprecated */
  private void startState(CheckState paramCheckState)
  {
    while (true)
    {
      try
      {
        this.mCurrentState = paramCheckState;
        int[] arrayOfInt = 4.$SwitchMap$com$google$android$music$PlaySongsActivity$CheckState;
        int i = paramCheckState.ordinal();
        int j = arrayOfInt[i];
        switch (j)
        {
        default:
          return;
        case 1:
          this.mProgressBar.setVisibility(0);
          this.mTextView.setGravity(48);
          TextView localTextView1 = this.mTextView;
          String str1 = getResources().getString(2131231220);
          localTextView1.setText(str1);
          this.mSecondaryButton.setVisibility(8);
          Bundle localBundle = new Bundle();
          localBundle.putBoolean("expedited", true);
          localBundle.putBoolean("force", true);
          ContentResolver.requestSync(this.mMusicPreferences.getSyncAccount(), "com.google.android.music.MusicContent", localBundle);
          continue;
        case 2:
        case 3:
        }
      }
      finally
      {
      }
      TextView localTextView2 = this.mTextView;
      Object[] arrayOfObject = new Object[2];
      String str2 = this.mMarketAccount;
      arrayOfObject[0] = str2;
      String str3 = this.mMusicAccount;
      arrayOfObject[1] = str3;
      String str4 = getString(2131231214, arrayOfObject);
      localTextView2.setText(str4);
      this.mSecondaryButton.setText(2131231216);
      this.mSecondaryButton.setVisibility(0);
      this.mTextView.setGravity(19);
      this.mProgressBar.setVisibility(8);
      continue;
      this.mSecondaryButton.setText(2131231219);
      this.mSecondaryButton.setVisibility(0);
      this.mTextView.setGravity(17);
      TextView localTextView3 = this.mTextView;
      String str5 = getResources().getString(2131231221);
      localTextView3.setText(str5);
      this.mProgressBar.setVisibility(8);
    }
  }

  public String getPageUrlForTracking()
  {
    return "playSongs";
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 != 1)
      return;
    if (doesMarketAccountMatchMusicAccount())
    {
      CheckState localCheckState1 = CheckState.WAITING_FOR_SYNC;
      startState(localCheckState1);
      boolean bool = this.mAsyncWorker.sendEmptyMessage(1);
      return;
    }
    if (isFinishing())
      return;
    CheckState localCheckState2 = CheckState.ACCOUNT_MATCH_FAILURE;
    startState(localCheckState2);
  }

  public void onClick(View paramView)
  {
    Button localButton1 = this.mPositiveButton;
    if (paramView == localButton1)
    {
      finish();
      return;
    }
    Button localButton2 = this.mSecondaryButton;
    if (paramView != localButton2)
      return;
    int[] arrayOfInt = 4.$SwitchMap$com$google$android$music$PlaySongsActivity$CheckState;
    int i = this.mCurrentState.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      return;
    case 2:
      MusicEventLogger localMusicEventLogger = this.mTracker;
      Object[] arrayOfObject = new Object[0];
      localMusicEventLogger.trackEvent("listenSelectAccount", arrayOfObject);
      boolean bool = TutorialUtils.launchTutorialToChooseAccountForResult(this, true, 1);
      return;
    case 3:
    }
    CheckState localCheckState = CheckState.WAITING_FOR_SYNC;
    startState(localCheckState);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    MusicPreferences localMusicPreferences1 = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences1;
    AsyncCheckupWorker localAsyncCheckupWorker = new AsyncCheckupWorker();
    this.mAsyncWorker = localAsyncCheckupWorker;
    setContentView(2130968713);
    ProgressBar localProgressBar = (ProgressBar)findViewById(2131296354);
    this.mProgressBar = localProgressBar;
    TextView localTextView = (TextView)findViewById(2131296386);
    this.mTextView = localTextView;
    Button localButton1 = (Button)findViewById(2131296572);
    this.mPositiveButton = localButton1;
    this.mPositiveButton.setOnClickListener(this);
    Button localButton2 = (Button)findViewById(2131296573);
    this.mSecondaryButton = localButton2;
    this.mSecondaryButton.setOnClickListener(this);
    SyncStatusObserver localSyncStatusObserver = this.mSyncObserver;
    Object localObject = ContentResolver.addStatusChangeListener(4, localSyncStatusObserver);
    this.mSyncObserverHandle = localObject;
    ContentResolver localContentResolver = getContentResolver();
    Uri localUri = MusicContent.CONTENT_URI;
    ContentObserver localContentObserver = this.mContentObserver;
    localContentResolver.registerContentObserver(localUri, true, localContentObserver);
    String str = getIntent().getStringExtra("storeId");
    this.mStoreId = str;
    if (this.mStoreId == null)
    {
      int i = Log.e("PlaySongsAct", "storeId extra was not supplied");
      finish();
      return;
    }
    MusicPreferences localMusicPreferences2 = this.mMusicPreferences;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        PlaySongsActivity localPlaySongsActivity = PlaySongsActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            if (PlaySongsActivity.this.mMusicPreferences.isNautilusEnabled())
            {
              boolean bool1 = PlaySongsActivity.this.mAsyncWorker.sendEmptyMessage(6);
              return;
            }
            if (PlaySongsActivity.this.doesMarketAccountMatchMusicAccount())
            {
              PlaySongsActivity localPlaySongsActivity1 = PlaySongsActivity.this;
              PlaySongsActivity.CheckState localCheckState1 = PlaySongsActivity.CheckState.WAITING_FOR_SYNC;
              localPlaySongsActivity1.startState(localCheckState1);
              boolean bool2 = PlaySongsActivity.this.mAsyncWorker.sendEmptyMessage(1);
              return;
            }
            if (PlaySongsActivity.this.isFinishing())
              return;
            MusicEventLogger localMusicEventLogger = PlaySongsActivity.this.mTracker;
            Object[] arrayOfObject = new Object[4];
            arrayOfObject[0] = "failureReason";
            arrayOfObject[1] = "accountMismatch";
            arrayOfObject[2] = "activeScreen";
            String str = PlaySongsActivity.this.getPageUrlForTracking();
            arrayOfObject[3] = str;
            localMusicEventLogger.trackEvent("failure", arrayOfObject);
            PlaySongsActivity localPlaySongsActivity2 = PlaySongsActivity.this;
            PlaySongsActivity.CheckState localCheckState2 = PlaySongsActivity.CheckState.ACCOUNT_MATCH_FAILURE;
            localPlaySongsActivity2.startState(localCheckState2);
          }
        };
        localPlaySongsActivity.runOnUiThread(local1);
      }
    };
    localMusicPreferences2.runWithPreferenceService(local1);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    this.mAsyncWorker.quit();
    ContentResolver.removeStatusChangeListener(this.mSyncObserverHandle);
    ContentResolver localContentResolver = getContentResolver();
    ContentObserver localContentObserver = this.mContentObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
    MusicPreferences.releaseMusicPreferences(this);
  }

  protected void onPause()
  {
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
  }

  private class AsyncCheckupWorker extends LoggableHandler
  {
    private boolean mFinished = false;
    private long mFoundLocalId = 65535L;
    private boolean mFoundSingleSong = false;
    private String mTitle = null;

    public AsyncCheckupWorker()
    {
      super();
    }

    // ERROR //
    private boolean checkIfExists()
    {
      // Byte code:
      //   0: iconst_2
      //   1: anewarray 48	java/lang/String
      //   4: astore_1
      //   5: aload_1
      //   6: iconst_0
      //   7: ldc 50
      //   9: aastore
      //   10: aload_1
      //   11: iconst_1
      //   12: ldc 52
      //   14: aastore
      //   15: aload_0
      //   16: getfield 25	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:this$0	Lcom/google/android/music/PlaySongsActivity;
      //   19: invokestatic 56	com/google/android/music/PlaySongsActivity:access$500	(Lcom/google/android/music/PlaySongsActivity;)Ljava/lang/String;
      //   22: astore_2
      //   23: ldc2_w 57
      //   26: aload_2
      //   27: invokestatic 64	com/google/android/music/store/MusicContent$AutoPlaylists$Members:getAutoPlaylistItemUri	(JLjava/lang/String;)Landroid/net/Uri;
      //   30: astore_3
      //   31: aload_0
      //   32: getfield 25	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:this$0	Lcom/google/android/music/PlaySongsActivity;
      //   35: astore 4
      //   37: aconst_null
      //   38: astore 5
      //   40: aconst_null
      //   41: astore 6
      //   43: aload 4
      //   45: aload_3
      //   46: aload_1
      //   47: aconst_null
      //   48: aload 5
      //   50: aload 6
      //   52: invokestatic 70	com/google/android/music/utils/MusicUtils:query	(Landroid/content/Context;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   55: astore 7
      //   57: aload 7
      //   59: ifnull +67 -> 126
      //   62: aload 7
      //   64: invokeinterface 75 1 0
      //   69: ifeq +57 -> 126
      //   72: aload 7
      //   74: iconst_0
      //   75: invokeinterface 79 2 0
      //   80: lstore 8
      //   82: aload_0
      //   83: lload 8
      //   85: putfield 38	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:mFoundLocalId	J
      //   88: aload_1
      //   89: arraylength
      //   90: iconst_1
      //   91: if_icmple +19 -> 110
      //   94: aload 7
      //   96: iconst_1
      //   97: invokeinterface 83 2 0
      //   102: astore 10
      //   104: aload_0
      //   105: aload 10
      //   107: putfield 40	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:mTitle	Ljava/lang/String;
      //   110: aload_0
      //   111: iconst_1
      //   112: putfield 42	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:mFoundSingleSong	Z
      //   115: aload 7
      //   117: invokestatic 89	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
      //   120: iconst_1
      //   121: istore 11
      //   123: iload 11
      //   125: ireturn
      //   126: aload 7
      //   128: invokestatic 89	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
      //   131: iconst_1
      //   132: anewarray 48	java/lang/String
      //   135: astore 12
      //   137: aload 12
      //   139: iconst_0
      //   140: ldc 91
      //   142: aastore
      //   143: aload_0
      //   144: getfield 25	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:this$0	Lcom/google/android/music/PlaySongsActivity;
      //   147: invokestatic 56	com/google/android/music/PlaySongsActivity:access$500	(Lcom/google/android/music/PlaySongsActivity;)Ljava/lang/String;
      //   150: invokestatic 97	com/google/android/music/store/MusicContent$Albums:getStoreAlbumUri	(Ljava/lang/String;)Landroid/net/Uri;
      //   153: astore 13
      //   155: aload_0
      //   156: getfield 25	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:this$0	Lcom/google/android/music/PlaySongsActivity;
      //   159: astore 14
      //   161: aconst_null
      //   162: astore 15
      //   164: aconst_null
      //   165: astore 16
      //   167: aload 14
      //   169: aload 13
      //   171: aload 12
      //   173: aconst_null
      //   174: aload 15
      //   176: aload 16
      //   178: invokestatic 70	com/google/android/music/utils/MusicUtils:query	(Landroid/content/Context;Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
      //   181: astore 7
      //   183: aload 7
      //   185: ifnull +55 -> 240
      //   188: aload 7
      //   190: invokeinterface 75 1 0
      //   195: ifeq +45 -> 240
      //   198: aload 7
      //   200: iconst_0
      //   201: invokeinterface 79 2 0
      //   206: lstore 17
      //   208: aload_0
      //   209: lload 17
      //   211: putfield 38	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:mFoundLocalId	J
      //   214: aload_0
      //   215: iconst_0
      //   216: putfield 42	com/google/android/music/PlaySongsActivity$AsyncCheckupWorker:mFoundSingleSong	Z
      //   219: aload 7
      //   221: invokestatic 89	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
      //   224: iconst_1
      //   225: istore 11
      //   227: goto -104 -> 123
      //   230: astore 19
      //   232: aload 7
      //   234: invokestatic 89	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
      //   237: aload 19
      //   239: athrow
      //   240: aload 7
      //   242: invokestatic 89	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
      //   245: iconst_0
      //   246: istore 11
      //   248: goto -125 -> 123
      //   251: astore 20
      //   253: aload 7
      //   255: invokestatic 89	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
      //   258: aload 20
      //   260: athrow
      //
      // Exception table:
      //   from	to	target	type
      //   62	115	230	finally
      //   188	219	251	finally
    }

    private void playSonglistAsync(final SongList paramSongList)
    {
      PlaySongsActivity localPlaySongsActivity = PlaySongsActivity.this;
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          PlaySongsActivity localPlaySongsActivity = PlaySongsActivity.this;
          SongList localSongList = paramSongList;
          MusicUtils.playMediaList(localPlaySongsActivity, localSongList);
          AppNavigation.goHome(PlaySongsActivity.this);
          PlaySongsActivity.this.finish();
        }
      };
      localPlaySongsActivity.runOnUiThread(local2);
    }

    private void postFailure()
    {
      boolean bool = sendEmptyMessage(5);
    }

    private void postSuccess()
    {
      Message localMessage = obtainMessage();
      localMessage.what = 4;
      boolean bool = sendMessage(localMessage);
    }

    private void processNautilusSuccess()
    {
      if (this.mFinished)
        return;
      this.mFinished = true;
      Object localObject = null;
      if (PlaySongsActivity.this.mStoreId.startsWith("T"))
      {
        String str1 = PlaySongsActivity.this.mStoreId;
        localObject = new NautilusSingleSongList(str1, "");
      }
      while (localObject == null)
      {
        boolean bool = sendEmptyMessage(5);
        return;
        if (PlaySongsActivity.this.mStoreId.startsWith("B"))
        {
          String str2 = PlaySongsActivity.this.mStoreId;
          localObject = new NautilusAlbumSongList(str2);
        }
      }
      playSonglistAsync((SongList)localObject);
    }

    private void processSuccess()
    {
      if (this.mFinished)
        return;
      this.mFinished = true;
      long l1;
      String str;
      if (this.mFoundSingleSong)
      {
        l1 = this.mFoundLocalId;
        str = this.mTitle;
      }
      long l2;
      for (Object localObject = new SingleSongList(l1, str); ; localObject = new AlbumSongList(l2, false))
      {
        playSonglistAsync((SongList)localObject);
        return;
        l2 = this.mFoundLocalId;
      }
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      case 2:
      case 3:
        if (checkIfExists())
        {
          if (paramMessage.what == 1);
          while (true)
          {
            postSuccess();
            return;
            if (paramMessage.what == 3);
          }
        }
        if (paramMessage.what != 3)
          return;
        postFailure();
        return;
      case 4:
        processSuccess();
        return;
      case 6:
        processNautilusSuccess();
        return;
      case 5:
      }
      MusicEventLogger localMusicEventLogger = PlaySongsActivity.this.mTracker;
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = "failureReason";
      arrayOfObject[1] = "couldNotFindSongs";
      arrayOfObject[2] = "activeScreen";
      String str = PlaySongsActivity.this.getPageUrlForTracking();
      arrayOfObject[3] = str;
      localMusicEventLogger.trackEvent("failure", arrayOfObject);
      PlaySongsActivity localPlaySongsActivity = PlaySongsActivity.this;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          PlaySongsActivity localPlaySongsActivity = PlaySongsActivity.this;
          PlaySongsActivity.CheckState localCheckState = PlaySongsActivity.CheckState.SYNC_FAILURE;
          localPlaySongsActivity.startState(localCheckState);
        }
      };
      localPlaySongsActivity.runOnUiThread(local1);
    }
  }

  private static enum CheckState
  {
    static
    {
      ACCOUNT_MATCH_FAILURE = new CheckState("ACCOUNT_MATCH_FAILURE", 1);
      SYNC_FAILURE = new CheckState("SYNC_FAILURE", 2);
      CheckState[] arrayOfCheckState = new CheckState[3];
      CheckState localCheckState1 = WAITING_FOR_SYNC;
      arrayOfCheckState[0] = localCheckState1;
      CheckState localCheckState2 = ACCOUNT_MATCH_FAILURE;
      arrayOfCheckState[1] = localCheckState2;
      CheckState localCheckState3 = SYNC_FAILURE;
      arrayOfCheckState[2] = localCheckState3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.PlaySongsActivity
 * JD-Core Version:    0.6.2
 */