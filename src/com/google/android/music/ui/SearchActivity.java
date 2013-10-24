package com.google.android.music.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SingleSongList;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.Store;
import com.google.android.music.utils.ConfigUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SearchActivity extends BaseActivity
  implements SearchView.OnQueryTextListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  public static final Uri SUGGEST_DATA_ALBUM = Uri.withAppendedPath(MusicContent.CONTENT_URI, "album");
  public static final Uri SUGGEST_DATA_ALBUM_ARTIST = Uri.withAppendedPath(MusicContent.CONTENT_URI, "albumartist");
  public static final Uri SUGGEST_DATA_ARTIST = Uri.withAppendedPath(MusicContent.CONTENT_URI, "artist");
  public static final Uri SUGGEST_DATA_PLAYLIST = Uri.withAppendedPath(MusicContent.CONTENT_URI, "playlist");
  public static final Uri SUGGEST_DATA_TRACK = Uri.withAppendedPath(MusicContent.CONTENT_URI, "track");
  private boolean mCreateRadio;
  private ScheduledThreadPoolExecutor mExecutor;
  private String mFilterString;
  private Handler mHandler;
  private boolean mInstantSearchEnabled;
  private boolean mPlayFirstSong;
  private long mSearchDelayMs;
  private Future<Void> mSearchFuture;
  private Callable<Void> mSearchTask;
  private SearchView mSearchView;

  public SearchActivity()
  {
    ScheduledThreadPoolExecutor localScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
    this.mExecutor = localScheduledThreadPoolExecutor;
    Handler localHandler = new Handler();
    this.mHandler = localHandler;
    Callable local1 = new Callable()
    {
      public Void call()
        throws Exception
      {
        if (Thread.interrupted())
          if (SearchActivity.LOGV)
            int i = Log.d("SearchActivity", "Search was interrupted early. Return.");
        while (true)
        {
          return null;
          SearchActivity.this.handleSearchResult();
        }
      }
    };
    this.mSearchTask = local1;
  }

  private static String getFilterStringForIntent(Intent paramIntent)
  {
    String str1;
    Bundle localBundle;
    label52: Object localObject;
    String str5;
    String str6;
    String str7;
    String str8;
    if (paramIntent != null)
    {
      str1 = paramIntent.getAction();
      if (LOGV)
      {
        String str2 = "getFilterStringForIntent: action = " + str1;
        int i = Log.d("SearchActivity", str2);
        if (paramIntent == null)
          break label253;
        localBundle = paramIntent.getExtras();
        if (localBundle != null)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("  - has extras: size = ");
          int j = localBundle.size();
          String str3 = j;
          int k = Log.d("SearchActivity", str3);
          String str4 = "  - extras = " + localBundle;
          int m = Log.d("SearchActivity", str4);
        }
      }
      localObject = paramIntent.getStringExtra("query");
      if ("android.intent.action.MEDIA_SEARCH".equals(str1))
      {
        str5 = paramIntent.getStringExtra("android.intent.extra.focus");
        str6 = paramIntent.getStringExtra("android.intent.extra.artist");
        str7 = paramIntent.getStringExtra("android.intent.extra.album");
        str8 = paramIntent.getStringExtra("android.intent.extra.title");
        if (str5 != null)
        {
          if ((!str5.startsWith("audio/")) || (str8 == null))
            break label259;
          localObject = str8;
        }
      }
    }
    while (true)
    {
      if (LOGV)
      {
        String str9 = "getFilterStringForIntent: new filter is '" + (String)localObject + "'";
        int n = Log.d("SearchActivity", str9);
      }
      return localObject;
      str1 = null;
      break;
      label253: localBundle = null;
      break label52;
      label259: if (str5.equals("vnd.android.cursor.item/album"))
      {
        if (!TextUtils.isEmpty(str7))
          localObject = str7;
      }
      else if (str5.equals("vnd.android.cursor.item/artist"))
      {
        if (!TextUtils.isEmpty(str6))
          localObject = str6;
      }
      else
      {
        String[] arrayOfString = new String[3];
        arrayOfString[0] = str6;
        arrayOfString[1] = str7;
        arrayOfString[2] = str8;
        localObject = getFirstNonNullString(arrayOfString);
      }
    }
  }

  private static String getFirstNonNullString(String[] paramArrayOfString)
  {
    String[] arrayOfString = paramArrayOfString;
    int i = arrayOfString.length;
    int j = 0;
    String str;
    if (j < i)
    {
      str = arrayOfString[j];
      if (MusicUtils.isUnknown(str));
    }
    while (true)
    {
      return str;
      j += 1;
      break;
      str = null;
    }
  }

  private void handleSearchResult()
  {
    Handler localHandler = this.mHandler;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        Fragment localFragment = SearchActivity.this.getContent();
        if ((SearchActivity.this.getContent() != null) && ((localFragment instanceof SearchClustersFragment)))
        {
          SearchClustersFragment localSearchClustersFragment = (SearchClustersFragment)localFragment;
          String str = SearchActivity.this.mFilterString;
          boolean bool = SearchActivity.this.mPlayFirstSong;
          localSearchClustersFragment.setSearchString(str, bool);
          return;
        }
        int i = Log.wtf("SearchActivity", "SearchClustersFragment not set in content");
      }
    };
    boolean bool = localHandler.post(local2);
  }

  private void handleSearchResult(Intent paramIntent)
  {
    Uri localUri = paramIntent.getData();
    String str1 = localUri.toString();
    String str2 = SUGGEST_DATA_TRACK.toString();
    if (str1.startsWith(str2))
    {
      long l1 = Long.parseLong(localUri.getLastPathSegment());
      SingleSongList localSingleSongList = new SingleSongList(l1, "");
      MusicUtils.playMediaList(this, localSingleSongList, -1);
      AppNavigation.goHome(this);
      finish();
      return;
    }
    String str3 = SUGGEST_DATA_ALBUM_ARTIST.toString();
    if (str1.startsWith(str3))
    {
      final long l2 = Long.parseLong(localUri.getLastPathSegment());
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        String mArtistName;

        public void backgroundTask()
        {
          String[] arrayOfString = new String[1];
          arrayOfString[0] = "artist";
          try
          {
            SearchActivity localSearchActivity = SearchActivity.this;
            Uri localUri = MusicContent.Artists.getArtistsUri(l2);
            localCursor = MusicUtils.query(localSearchActivity, localUri, arrayOfString, null, null, null);
            if ((localCursor != null) && (localCursor.moveToNext()))
            {
              String str = localCursor.getString(0);
              this.mArtistName = str;
            }
            return;
          }
          finally
          {
            Cursor localCursor;
            Store.safeClose(localCursor);
          }
        }

        public void taskCompleted()
        {
          SearchActivity localSearchActivity = SearchActivity.this;
          long l = l2;
          String str = this.mArtistName;
          ArtistPageActivity.showArtist(localSearchActivity, l, str, false);
          SearchActivity.this.finish();
        }
      });
      return;
    }
    String str4 = SUGGEST_DATA_ALBUM.toString();
    if (str1.startsWith(str4))
    {
      long l3 = Long.valueOf(localUri.getLastPathSegment()).longValue();
      TrackContainerActivity.showAlbum(this, l3, null, false);
      finish();
      return;
    }
    String str5 = SUGGEST_DATA_ARTIST.toString();
    if (str1.startsWith(str5))
    {
      final long l4 = Long.valueOf(localUri.getLastPathSegment()).longValue();
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        final String[] cols;
        String mArtistName;

        public void backgroundTask()
        {
          SearchActivity localSearchActivity = SearchActivity.this;
          Uri localUri = MusicContent.Artists.getArtistsUri(l4);
          String[] arrayOfString1 = this.cols;
          String[] arrayOfString2 = null;
          String str1 = null;
          Cursor localCursor = MusicUtils.query(localSearchActivity, localUri, arrayOfString1, null, arrayOfString2, str1);
          if (localCursor != null);
          try
          {
            if (localCursor.moveToNext())
            {
              String str2 = localCursor.getString(0);
              this.mArtistName = str2;
            }
            return;
          }
          finally
          {
            Store.safeClose(localCursor);
          }
        }

        public void taskCompleted()
        {
          SearchActivity localSearchActivity = SearchActivity.this;
          long l = l4;
          String str = this.mArtistName;
          ArtistPageActivity.showArtist(localSearchActivity, l, str, false);
          SearchActivity.this.finish();
        }
      });
      return;
    }
    String str6 = SUGGEST_DATA_PLAYLIST.toString();
    if (!str1.startsWith(str6))
      return;
    final long l5 = Long.valueOf(localUri.getLastPathSegment()).longValue();
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      final String[] cols;
      String mPlaylistName;
      int mPlaylistType;

      public void backgroundTask()
      {
        SearchActivity localSearchActivity = SearchActivity.this;
        Uri localUri = MusicContent.Playlists.getPlaylistUri(l5);
        String[] arrayOfString1 = this.cols;
        String[] arrayOfString2 = null;
        String str1 = null;
        Cursor localCursor = MusicUtils.query(localSearchActivity, localUri, arrayOfString1, null, arrayOfString2, str1);
        if (localCursor != null);
        try
        {
          if (localCursor.moveToNext())
          {
            String str2 = localCursor.getString(0);
            this.mPlaylistName = str2;
            int i = localCursor.getInt(1);
            this.mPlaylistType = i;
          }
          return;
        }
        finally
        {
          Store.safeClose(localCursor);
        }
      }

      public void taskCompleted()
      {
        SearchActivity localSearchActivity = SearchActivity.this;
        long l = l5;
        String str1 = this.mPlaylistName;
        int i = this.mPlaylistType;
        String str2 = null;
        String str3 = null;
        String str4 = null;
        String str5 = null;
        PlaylistSongList localPlaylistSongList = new PlaylistSongList(l, str1, i, null, str2, str3, str4, str5, false);
        TrackContainerActivity.showPlaylist(localSearchActivity, localPlaylistSongList, null);
        SearchActivity.this.finish();
      }
    });
  }

  private void hideSoftKeyboard()
  {
    if (this.mSearchView == null)
      return;
    InputMethodManager localInputMethodManager = (InputMethodManager)getApplicationContext().getSystemService("input_method");
    IBinder localIBinder = this.mSearchView.getWindowToken();
    boolean bool = localInputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
  }

  private void processNewSearchQuery()
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("processNewSearchQuery: mFilterString '");
      String str1 = this.mFilterString;
      String str2 = str1 + "'...";
      int i = Log.d("SearchActivity", str2);
    }
    if ((this.mSearchFuture != null) && (!this.mSearchFuture.isDone()))
    {
      if (LOGV)
        int j = Log.d("SearchActivity", "Cancelling a previous search.");
      if (!this.mSearchFuture.cancel(true))
        int k = Log.w("SearchActivity", "Failed to cancel a previous search.");
    }
    if ((this.mFilterString != null) && (this.mFilterString.length() >= 2));
    for (long l = this.mSearchDelayMs; ; l = 0L)
    {
      ScheduledThreadPoolExecutor localScheduledThreadPoolExecutor = this.mExecutor;
      Callable localCallable = this.mSearchTask;
      TimeUnit localTimeUnit = TimeUnit.MILLISECONDS;
      ScheduledFuture localScheduledFuture = localScheduledThreadPoolExecutor.schedule(localCallable, l, localTimeUnit);
      this.mSearchFuture = localScheduledFuture;
      return;
    }
  }

  public static final void showCreateRadioSearch(Context paramContext)
  {
    Intent localIntent1 = new Intent(paramContext, SearchActivity.class);
    Intent localIntent2 = localIntent1.putExtra("createNewRadioStation", true);
    paramContext.startActivity(localIntent1);
  }

  public static final void showSearch(Context paramContext)
  {
    Intent localIntent1 = new Intent(paramContext, SearchActivity.class);
    Intent localIntent2 = localIntent1.putExtra("createNewRadioStation", false);
    Intent localIntent3 = localIntent1.addFlags(67108864);
    paramContext.startActivity(localIntent1);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ActionBar localActionBar = getSupportActionBar();
    localActionBar.setDisplayShowHomeEnabled(true);
    localActionBar.setDisplayHomeAsUpEnabled(true);
    localActionBar.setTitle("");
    Object localObject;
    Intent localIntent;
    String str3;
    if (getContent() == null)
    {
      String str1 = new String();
      this.mFilterString = str1;
      boolean bool1 = getIntent().getBooleanExtra("createNewRadioStation", false);
      this.mCreateRadio = bool1;
      if (!this.mCreateRadio)
      {
        String str2 = this.mFilterString;
        localObject = new SearchMusicClustersFragment(str2);
        replaceContent((Fragment)localObject, false);
      }
    }
    else
    {
      localIntent = getIntent();
      if (localIntent == null)
        break label210;
      str3 = localIntent.getAction();
      label114: if (paramBundle == null)
        break label216;
      String str4 = paramBundle.getString("com.google.android.music.ui.searchactivity.filterstring");
      this.mFilterString = str4;
      boolean bool2 = paramBundle.getBoolean("createNewRadioStation");
      this.mCreateRadio = bool2;
    }
    while (true)
    {
      boolean bool3 = ConfigUtils.isInstantSearchEnabled();
      this.mInstantSearchEnabled = bool3;
      long l = ConfigUtils.getInstantSearchDelayMs();
      this.mSearchDelayMs = l;
      if (this.mSearchDelayMs >= 250L)
        return;
      this.mSearchDelayMs = 250L;
      return;
      String str5 = this.mFilterString;
      localObject = new CreateNewStationClusterFragment(str5);
      break;
      label210: str3 = null;
      break label114;
      label216: if (("android.intent.action.SEARCH".equals(str3)) || ("android.media.action.MEDIA_PLAY_FROM_SEARCH".equals(str3)) || ("android.intent.action.MEDIA_SEARCH".equals(str3)))
      {
        String str6 = getFilterStringForIntent(localIntent);
        this.mFilterString = str6;
        boolean bool4 = "android.media.action.MEDIA_PLAY_FROM_SEARCH".equals(str3);
        this.mPlayFirstSong = bool4;
        processNewSearchQuery();
      }
      else if ("android.intent.action.SEARCH_RESULT".equals(str3))
      {
        handleSearchResult(localIntent);
      }
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (isFinishing());
    while (true)
    {
      return true;
      getMenuInflater().inflate(2131820548, paramMenu);
      SearchView localSearchView1 = (SearchView)MenuItemCompat.getActionView(paramMenu.findItem(2131296583));
      this.mSearchView = localSearchView1;
      this.mSearchView.setIconifiedByDefault(true);
      this.mSearchView.setIconified(false);
      SearchView localSearchView2 = this.mSearchView;
      SearchView.OnCloseListener local3 = new SearchView.OnCloseListener()
      {
        public boolean onClose()
        {
          return true;
        }
      };
      localSearchView2.setOnCloseListener(local3);
      this.mSearchView.setOnQueryTextListener(this);
      if (this.mPlayFirstSong)
        this.mSearchView.clearFocus();
      SearchView localSearchView3 = this.mSearchView;
      String str1 = getResources().getString(2131230938);
      localSearchView3.setQueryHint(str1);
      int i = this.mSearchView.getImeOptions();
      SearchView localSearchView4 = this.mSearchView;
      int j = 0x10000000 | i;
      localSearchView4.setImeOptions(j);
      if (this.mFilterString != null)
      {
        SearchView localSearchView5 = this.mSearchView;
        String str2 = this.mFilterString;
        localSearchView5.setQuery(str2, false);
      }
    }
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if ((paramMenuItem.getItemId() == 16908332) && (!TextUtils.isEmpty(this.mFilterString)))
      AppNavigation.goHome(this);
    for (boolean bool = true; ; bool = super.onOptionsItemSelected(paramMenuItem))
      return bool;
  }

  public boolean onQueryTextChange(String paramString)
  {
    this.mPlayFirstSong = false;
    if ((this.mInstantSearchEnabled) && (paramString != null))
    {
      String str = this.mFilterString;
      if (!paramString.equals(str))
      {
        this.mFilterString = paramString;
        processNewSearchQuery();
      }
    }
    return false;
  }

  public boolean onQueryTextSubmit(String paramString)
  {
    this.mPlayFirstSong = false;
    if ((!this.mInstantSearchEnabled) && (paramString != null))
    {
      String str = this.mFilterString;
      if (!paramString.equals(str))
      {
        this.mFilterString = paramString;
        processNewSearchQuery();
      }
    }
    hideSoftKeyboard();
    return false;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mFilterString != null)
    {
      String str = this.mFilterString;
      paramBundle.putString("com.google.android.music.ui.searchactivity.filterstring", str);
    }
    boolean bool = this.mCreateRadio;
    paramBundle.putBoolean("createNewRadioStation", bool);
  }

  protected boolean useActionBarHamburger()
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SearchActivity
 * JD-Core Version:    0.6.2
 */