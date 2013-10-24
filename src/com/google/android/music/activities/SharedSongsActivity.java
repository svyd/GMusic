package com.google.android.music.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.music.cloudclient.MusicCloud;
import com.google.android.music.cloudclient.MusicCloudImpl;
import com.google.android.music.cloudclient.TrackJson.ImageRef;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.medialist.SharedAlbumSongList;
import com.google.android.music.medialist.SharedSingleSongList;
import com.google.android.music.medialist.SharedWithMeSongList;
import com.google.android.music.medialist.SongData;
import com.google.android.music.medialist.SongDataList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.sharedpreview.JsonResponse;
import com.google.android.music.sharedpreview.SharedAlbumResponse;
import com.google.android.music.sharedpreview.SharedPreviewClient;
import com.google.android.music.sharedpreview.SharedSongResponse;
import com.google.android.music.sharedpreview.Track;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.tutorial.SignupStatus;
import com.google.android.music.tutorial.TutorialUtils;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SharedSongsActivity extends Activity
  implements View.OnClickListener
{
  private final boolean LOGV;
  private AsyncWorker mAsyncWorker;
  private Button mButton;
  private volatile MusicCloud mCloudClient;
  private boolean mIsSharedPlaylist;
  private MusicPreferences mMusicPreferences;
  private ProgressBar mProgressBar;
  private BroadcastReceiver mReceiver;
  private Button mSecondaryButton;
  private TextView mTextView;
  private MusicEventLogger mTracker;
  private Uri mUri;

  public SharedSongsActivity()
  {
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
    this.LOGV = bool;
    this.mIsSharedPlaylist = false;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str = paramAnonymousIntent.getAction();
        if (!"com.google.android.music.VERIFIED_ACCOUNTS_CHANGED".equals(str))
          return;
        boolean bool = SharedSongsActivity.this.mAsyncWorker.sendEmptyMessage(1);
      }
    };
    this.mReceiver = local1;
  }

  public String getPageUrlForTracking()
  {
    return "sharedSongs";
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 != 1)
      return;
    boolean bool = this.mAsyncWorker.sendEmptyMessage(2);
  }

  public void onClick(View paramView)
  {
    Button localButton1 = this.mButton;
    if (paramView == localButton1)
    {
      finish();
      return;
    }
    Button localButton2 = this.mSecondaryButton;
    if (paramView != localButton2)
      return;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackEvent("openSharedSelectAccount", arrayOfObject);
    boolean bool = TutorialUtils.launchTutorialToChooseAccountForResult(this, true, 1);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences;
    MusicCloudImpl localMusicCloudImpl = new MusicCloudImpl(this);
    this.mCloudClient = localMusicCloudImpl;
    AsyncWorker localAsyncWorker = new AsyncWorker();
    this.mAsyncWorker = localAsyncWorker;
    setContentView(2130968713);
    ProgressBar localProgressBar = (ProgressBar)findViewById(2131296354);
    this.mProgressBar = localProgressBar;
    TextView localTextView = (TextView)findViewById(2131296386);
    this.mTextView = localTextView;
    Button localButton1 = (Button)findViewById(2131296572);
    this.mButton = localButton1;
    this.mButton.setOnClickListener(this);
    Button localButton2 = (Button)findViewById(2131296573);
    this.mSecondaryButton = localButton2;
    this.mSecondaryButton.setOnClickListener(this);
    Intent localIntent = getIntent();
    String str1 = localIntent.getStringExtra("url");
    if (str1 != null)
    {
      Uri localUri1 = Uri.parse(str1);
      this.mUri = localUri1;
    }
    if (this.mUri == null)
    {
      Uri localUri2 = localIntent.getData();
      this.mUri = localUri2;
      this.mIsSharedPlaylist = true;
    }
    if (!this.LOGV)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("Shared url=");
    Uri localUri3 = this.mUri;
    String str2 = localUri3;
    int i = Log.d("SharedSongsActivity", str2);
  }

  protected void onDestroy()
  {
    super.onDestroy();
    MusicPreferences.releaseMusicPreferences(this);
    this.mAsyncWorker.quit();
  }

  protected void onPause()
  {
    super.onPause();
    BroadcastReceiver localBroadcastReceiver = this.mReceiver;
    unregisterReceiver(localBroadcastReceiver);
  }

  protected void onResume()
  {
    super.onResume();
    IntentFilter localIntentFilter = new IntentFilter("com.google.android.music.VERIFIED_ACCOUNTS_CHANGED");
    BroadcastReceiver localBroadcastReceiver = this.mReceiver;
    Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter);
    if (this.mIsSharedPlaylist)
      boolean bool1 = this.mAsyncWorker.sendEmptyMessage(5);
    while (true)
    {
      MusicEventLogger localMusicEventLogger = this.mTracker;
      String str = getPageUrlForTracking();
      Object[] arrayOfObject = new Object[0];
      localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
      return;
      boolean bool2 = this.mAsyncWorker.sendEmptyMessage(2);
    }
  }

  private class AsyncWorker extends LoggableHandler
  {
    public AsyncWorker()
    {
      super();
    }

    private void checkAccountsMatch()
    {
      Account localAccount = SharedSongsActivity.this.mMusicPreferences.getSyncAccount();
      if (localAccount == null)
      {
        MusicEventLogger localMusicEventLogger1 = SharedSongsActivity.this.mTracker;
        Object[] arrayOfObject1 = new Object[0];
        localMusicEventLogger1.trackEvent("signUpFromGPlusLink", arrayOfObject1);
        if (TutorialUtils.launchTutorialOnDemand(SharedSongsActivity.this))
          return;
        SharedSongsActivity.this.finish();
        return;
      }
      String str1 = SharedSongsActivity.this.getIntent().getStringExtra("authAccount");
      if (str1 == null)
      {
        int i = Log.e("SharedSongsActivity", "G+ did not provide account extra");
        MusicEventLogger localMusicEventLogger2 = SharedSongsActivity.this.mTracker;
        Object[] arrayOfObject2 = new Object[4];
        arrayOfObject2[0] = "activeScreen";
        String str2 = SharedSongsActivity.this.getPageUrlForTracking();
        arrayOfObject2[1] = str2;
        arrayOfObject2[2] = "failureMsg";
        arrayOfObject2[3] = "G+ did not provide extra: authAccount";
        localMusicEventLogger2.trackEvent("wtf", arrayOfObject2);
        SharedSongsActivity.this.finish();
        return;
      }
      String str3 = localAccount.name;
      if (!str1.equalsIgnoreCase(str3))
      {
        MusicEventLogger localMusicEventLogger3 = SharedSongsActivity.this.mTracker;
        Object[] arrayOfObject3 = new Object[4];
        arrayOfObject3[0] = "activeScreen";
        String str4 = SharedSongsActivity.this.getPageUrlForTracking();
        arrayOfObject3[1] = str4;
        arrayOfObject3[2] = "failureMsg";
        arrayOfObject3[3] = "G+/music accounts mismatch";
        localMusicEventLogger3.trackEvent("failure", arrayOfObject3);
        TextView localTextView1 = SharedSongsActivity.this.mTextView;
        SharedSongsActivity localSharedSongsActivity = SharedSongsActivity.this;
        Object[] arrayOfObject4 = new Object[2];
        arrayOfObject4[0] = str1;
        String str5 = localAccount.name;
        arrayOfObject4[1] = str5;
        String str6 = localSharedSongsActivity.getString(2131231215, arrayOfObject4);
        localTextView1.setText(str6);
        SharedSongsActivity.this.mSecondaryButton.setText(2131231216);
        SharedSongsActivity.this.mSecondaryButton.setVisibility(0);
        SharedSongsActivity.this.mTextView.setGravity(19);
        SharedSongsActivity.this.mProgressBar.setVisibility(8);
        return;
      }
      TextView localTextView2 = SharedSongsActivity.this.mTextView;
      String str7 = SharedSongsActivity.this.getResources().getString(2131231220);
      localTextView2.setText(str7);
      SharedSongsActivity.this.mProgressBar.setVisibility(0);
      SharedSongsActivity.this.mSecondaryButton.setVisibility(8);
      SharedSongsActivity.this.mTextView.setGravity(17);
      boolean bool = SharedSongsActivity.this.mAsyncWorker.sendEmptyMessage(3);
    }

    private void processGetShared()
    {
      Context localContext = SharedSongsActivity.this.getApplicationContext();
      SharedPreviewClient localSharedPreviewClient = new SharedPreviewClient(localContext);
      String str1 = SharedSongsActivity.this.mUri.toString();
      JsonResponse localJsonResponse = localSharedPreviewClient.getMetaDataResponse(str1);
      if (localJsonResponse == null)
      {
        int i = Log.w("SharedSongsActivity", "Failed to retrieve shared content.");
        MusicEventLogger localMusicEventLogger = SharedSongsActivity.this.mTracker;
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = "activeScreen";
        String str2 = SharedSongsActivity.this.getPageUrlForTracking();
        arrayOfObject[1] = str2;
        arrayOfObject[2] = "failureMsg";
        arrayOfObject[3] = "invalid json response";
        localMusicEventLogger.trackEvent("failure", arrayOfObject);
        boolean bool1 = sendEmptyMessage(4);
        return;
      }
      SongDataList localSongDataList;
      String str10;
      String str11;
      if ((localJsonResponse instanceof SharedAlbumResponse))
      {
        SharedAlbumResponse localSharedAlbumResponse = (SharedAlbumResponse)localJsonResponse;
        localSongDataList = new SongDataList();
        Iterator localIterator = localSharedAlbumResponse.mTracks.iterator();
        while (localIterator.hasNext())
        {
          Track localTrack = (Track)localIterator.next();
          SongData localSongData1 = new SongData();
          String str3 = localSharedAlbumResponse.mAlbumArtist;
          localSongData1.mAlbumArtist = str3;
          String str4 = localSharedAlbumResponse.mAlbumArtist;
          localSongData1.mArtist = str4;
          String str5 = localSharedAlbumResponse.mAlbumTitle;
          localSongData1.mAlbum = str5;
          String str6 = localTrack.mTitle;
          localSongData1.mTitle = str6;
          long l1 = localTrack.mDurationMsecs;
          localSongData1.mDuration = l1;
          localSongData1.mAlbumId = 0L;
          localSongData1.mAlbumArtistId = 0L;
          localSongData1.mArtistSort = "";
          localSongData1.mHasRemote = 0;
          localSongData1.mHasLocal = 0;
          localSongData1.mRating = 0;
          localSongData1.mSourceId = "";
          String str7 = SharedSongsActivity.this.mUri.toString();
          StringBuilder localStringBuilder1 = new StringBuilder(str7);
          StringBuilder localStringBuilder2 = localStringBuilder1.append("&mode=streaming");
          StringBuilder localStringBuilder3 = localStringBuilder1.append("&tid=");
          String str8 = localTrack.mId;
          StringBuilder localStringBuilder4 = localStringBuilder1.append(str8);
          String str9 = localStringBuilder1.toString();
          localSongData1.mDomainParam = str9;
          boolean bool2 = localSongDataList.mList.add(localSongData1);
        }
        str10 = localSharedAlbumResponse.mAlbumArtUrl;
        str11 = localSharedAlbumResponse.mStoreUrl;
      }
      SongData localSongData2;
      String str18;
      String str19;
      for (Object localObject = new SharedAlbumSongList(str10, str11, localSongDataList); ; localObject = new SharedSingleSongList(str18, str19, localSongData2))
      {
        showSongList((SongList)localObject);
        return;
        if (!(localJsonResponse instanceof SharedSongResponse))
          break;
        SharedSongResponse localSharedSongResponse = (SharedSongResponse)localJsonResponse;
        localSongData2 = new SongData();
        String str12 = localSharedSongResponse.mTrackArtist;
        localSongData2.mArtist = str12;
        String str13 = localSharedSongResponse.mAlbumTitle;
        localSongData2.mAlbum = str13;
        String str14 = localSharedSongResponse.mTrackTitle;
        localSongData2.mTitle = str14;
        long l2 = localSharedSongResponse.mDurationMsecs;
        localSongData2.mDuration = l2;
        String str15 = localSongData2.mArtist;
        localSongData2.mAlbumArtist = str15;
        localSongData2.mAlbumId = 0L;
        localSongData2.mAlbumArtistId = 0L;
        localSongData2.mArtistSort = "";
        localSongData2.mHasRemote = 0;
        localSongData2.mHasLocal = 0;
        localSongData2.mRating = 0;
        localSongData2.mSourceId = "";
        String str16 = SharedSongsActivity.this.mUri.toString();
        StringBuilder localStringBuilder5 = new StringBuilder(str16);
        StringBuilder localStringBuilder6 = localStringBuilder5.append("&mode=streaming");
        String str17 = localStringBuilder5.toString();
        localSongData2.mDomainParam = str17;
        str18 = localSharedSongResponse.mAlbumArtUrl;
        str19 = localSharedSongResponse.mStoreUrl;
      }
      String str20 = "Unknown song list: " + localJsonResponse;
      int j = Log.e("SharedSongsActivity", str20);
      boolean bool3 = sendEmptyMessage(4);
    }

    private void processGetSharedPlaylist()
    {
      List localList1 = SharedSongsActivity.this.mUri.getPathSegments();
      if (localList1.size() < 3)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Invalid url: ");
        Uri localUri = SharedSongsActivity.this.mUri;
        String str1 = localUri;
        int i = Log.e("SharedSongsActivity", str1);
        boolean bool1 = sendEmptyMessage(4);
        return;
      }
      String str2 = (String)localList1.get(2);
      if (SharedSongsActivity.this.LOGV)
      {
        String str3 = "shareToken=" + str2;
        int j = Log.d("SharedSongsActivity", str3);
      }
      if (str2 == null)
      {
        int k = Log.e("SharedSongsActivity", "Failed to get share token");
        boolean bool2 = sendEmptyMessage(4);
        return;
      }
      SyncablePlaylist localSyncablePlaylist2;
      try
      {
        SyncablePlaylist localSyncablePlaylist1 = SharedSongsActivity.this.mCloudClient.getPlaylist(str2);
        localSyncablePlaylist2 = localSyncablePlaylist1;
        if (localSyncablePlaylist2 == null)
        {
          int m = Log.e("SharedSongsActivity", "Failed to get playlist");
          boolean bool3 = sendEmptyMessage(4);
          return;
        }
      }
      catch (IOException localIOException)
      {
        int n = Log.e("SharedSongsActivity", "Failed to get shared playlist", localIOException);
        boolean bool4 = sendEmptyMessage(4);
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        int i1 = Log.e("SharedSongsActivity", "Failed to get shared playlist", localInterruptedException);
        boolean bool5 = sendEmptyMessage(4);
        return;
      }
      List localList2 = localSyncablePlaylist2.mArtUrls;
      String str4 = null;
      if ((localList2 != null) && (!localList2.isEmpty()))
        str4 = ((TrackJson.ImageRef)localList2.get(0)).mUrl;
      String str5 = localSyncablePlaylist2.mName;
      String str6 = localSyncablePlaylist2.mDescription;
      String str7 = localSyncablePlaylist2.mOwnerName;
      String str8 = localSyncablePlaylist2.mOwnerProfilePhotoUrl;
      SharedWithMeSongList localSharedWithMeSongList = new SharedWithMeSongList(65535L, str2, str5, str6, str7, str4, str8);
      showSongList(localSharedWithMeSongList);
    }

    private void showSongList(final SongList paramSongList)
    {
      SharedSongsActivity localSharedSongsActivity = SharedSongsActivity.this;
      Runnable local3 = new Runnable()
      {
        public void run()
        {
          SharedSongsActivity localSharedSongsActivity = SharedSongsActivity.this;
          SongList localSongList = paramSongList;
          Intent localIntent1 = AppNavigation.getShowSonglistIntent(localSharedSongsActivity, localSongList);
          Intent localIntent2 = localIntent1.addFlags(335544320);
          SharedSongsActivity.this.startActivity(localIntent1);
          SharedSongsActivity.this.finish();
        }
      };
      localSharedSongsActivity.runOnUiThread(local3);
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
        if (SharedSongsActivity.this.mMusicPreferences.getStoreAvailableLastChecked() <= 0L)
        {
          SignupStatus.launchVerificationCheck(SharedSongsActivity.this);
          return;
        }
        if (!SharedSongsActivity.this.mMusicPreferences.getStoreAvailable())
        {
          Intent localIntent1 = new Intent("android.intent.action.VIEW");
          Uri localUri = SharedSongsActivity.this.mUri;
          Intent localIntent2 = localIntent1.setData(localUri);
          Intent localIntent3 = localIntent1.addFlags(268435456);
          Intent localIntent4 = localIntent1.addFlags(32768);
          Intent localIntent5 = localIntent1.addFlags(524288);
          SharedSongsActivity.this.startActivity(localIntent1);
          SharedSongsActivity.this.finish();
          return;
        }
        boolean bool = sendEmptyMessage(2);
        return;
      case 3:
        processGetShared();
        return;
      case 5:
        processGetSharedPlaylist();
        return;
      case 2:
        SharedSongsActivity localSharedSongsActivity1 = SharedSongsActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            SharedSongsActivity.AsyncWorker.this.checkAccountsMatch();
          }
        };
        localSharedSongsActivity1.runOnUiThread(local1);
        return;
      case 4:
      }
      SharedSongsActivity localSharedSongsActivity2 = SharedSongsActivity.this;
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          SharedSongsActivity.this.mButton.setText(2131230741);
          TextView localTextView = SharedSongsActivity.this.mTextView;
          String str = SharedSongsActivity.this.getResources().getString(2131231221);
          localTextView.setText(str);
          SharedSongsActivity.this.mProgressBar.setVisibility(8);
        }
      };
      localSharedSongsActivity2.runOnUiThread(local2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.activities.SharedSongsActivity
 * JD-Core Version:    0.6.2
 */