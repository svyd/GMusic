package com.google.android.music;

import android.app.Application;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.keepon.KeeponSchedulingService;
import com.google.android.music.download.keepon.KeeponSchedulingServiceConnection;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.store.MusicContent;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import com.google.android.music.utils.async.AsyncWorkers;
import java.util.ArrayList;

public class DownloadQueueActivity extends ListActivity
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private static final String[] QUERY_PROJECTION = arrayOfString;
  private DownloadQueueAdapter mAdapter = null;
  private final KeeponSchedulingServiceConnection mKeeponSchedulingServiceConnection;
  private final ContentObserver mRequeryOnChangeContentObserver;
  private MusicEventLogger mTracker;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "Artist";
    arrayOfString[2] = "Title";
    arrayOfString[3] = "DownloadStatus";
  }

  public DownloadQueueActivity()
  {
    KeeponSchedulingServiceConnection localKeeponSchedulingServiceConnection = new KeeponSchedulingServiceConnection();
    this.mKeeponSchedulingServiceConnection = localKeeponSchedulingServiceConnection;
    LoggableHandler localLoggableHandler = AsyncWorkers.sUIBackgroundWorker;
    ContentObserver local2 = new ContentObserver(localLoggableHandler)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        DownloadQueueActivity localDownloadQueueActivity1 = DownloadQueueActivity.this;
        Uri localUri = MusicContent.DOWNLOAD_QUEUE_URI;
        String[] arrayOfString1 = DownloadQueueActivity.QUERY_PROJECTION;
        String[] arrayOfString2 = null;
        String str = null;
        final Cursor localCursor = MusicUtils.query(localDownloadQueueActivity1, localUri, arrayOfString1, null, arrayOfString2, str);
        DownloadQueueActivity localDownloadQueueActivity2 = DownloadQueueActivity.this;
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            DownloadQueueActivity localDownloadQueueActivity = DownloadQueueActivity.this;
            Cursor localCursor = localCursor;
            localDownloadQueueActivity.init(localCursor);
          }
        };
        localDownloadQueueActivity2.runOnUiThread(local1);
      }
    };
    this.mRequeryOnChangeContentObserver = local2;
  }

  private void init(Cursor paramCursor)
  {
    if (this.mAdapter == null)
      return;
    this.mAdapter.changeCursor(paramCursor);
    if (paramCursor.getCount() == 0)
    {
      findViewById(2131296395).setVisibility(0);
      return;
    }
    findViewById(2131296395).setVisibility(8);
  }

  public String getPageUrlForTracking()
  {
    return "downloadQueue";
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    KeeponSchedulingServiceConnection localKeeponSchedulingServiceConnection = this.mKeeponSchedulingServiceConnection;
    Intent localIntent = new Intent(this, KeeponSchedulingService.class);
    boolean bool1 = localKeeponSchedulingServiceConnection.bindService(this, localIntent, 1);
    boolean bool2 = requestWindowFeature(5);
    boolean bool3 = requestWindowFeature(1);
    setContentView(2130968614);
    Application localApplication = getApplication();
    DownloadQueueAdapter localDownloadQueueAdapter1 = new DownloadQueueAdapter(localApplication);
    this.mAdapter = localDownloadQueueAdapter1;
    DownloadQueueAdapter localDownloadQueueAdapter2 = this.mAdapter;
    setListAdapter(localDownloadQueueAdapter2);
    ListView localListView = getListView();
    localListView.setCacheColorHint(0);
    DownloadQueueAdapter localDownloadQueueAdapter3 = this.mAdapter;
    localListView.setRecyclerListener(localDownloadQueueAdapter3);
    ContentResolver localContentResolver = getContentResolver();
    Uri localUri = MusicContent.DOWNLOAD_QUEUE_URI;
    ContentObserver localContentObserver = this.mRequeryOnChangeContentObserver;
    localContentResolver.registerContentObserver(localUri, false, localContentObserver);
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      Cursor c;

      public void backgroundTask()
      {
        DownloadQueueActivity localDownloadQueueActivity = DownloadQueueActivity.this;
        Uri localUri = MusicContent.DOWNLOAD_QUEUE_URI;
        String[] arrayOfString1 = DownloadQueueActivity.QUERY_PROJECTION;
        String[] arrayOfString2 = null;
        String str = null;
        Cursor localCursor = MusicUtils.query(localDownloadQueueActivity, localUri, arrayOfString1, null, arrayOfString2, str);
        this.c = localCursor;
      }

      public void taskCompleted()
      {
        if (DownloadQueueActivity.this.isFinishing())
          return;
        DownloadQueueActivity localDownloadQueueActivity = DownloadQueueActivity.this;
        Cursor localCursor = this.c;
        localDownloadQueueActivity.init(localCursor);
      }
    });
  }

  public void onDestroy()
  {
    ContentResolver localContentResolver = getContentResolver();
    ContentObserver localContentObserver = this.mRequeryOnChangeContentObserver;
    localContentResolver.unregisterContentObserver(localContentObserver);
    ListView localListView = getListView();
    ArrayList localArrayList = new ArrayList();
    localListView.reclaimViews(localArrayList);
    if (this.mAdapter != null)
      this.mAdapter.changeCursor(null);
    setListAdapter(null);
    this.mAdapter = null;
    this.mKeeponSchedulingServiceConnection.unbindService(this);
    super.onDestroy();
  }

  protected void onResume()
  {
    super.onResume();
    MusicEventLogger localMusicEventLogger = this.mTracker;
    String str = getPageUrlForTracking();
    Object[] arrayOfObject = new Object[0];
    localMusicEventLogger.trackScreenView(this, str, arrayOfObject);
  }

  private static class ViewHolder
  {
    BufferProgressListener bufferProgressListener;
    ImageView downloadIndicator;
    ImageView downloadType;
    TextView line1;
    TextView line2;
    SeekBar progressBar;
  }

  private class DownloadQueueAdapter extends SimpleCursorAdapter
    implements AbsListView.RecyclerListener
  {
    private int mArtistIdx;
    private int mIdIdx;
    private int mTrackNameIdx;

    public DownloadQueueAdapter(Context arg2)
    {
    }

    private void getColumnIndexes(Cursor paramCursor)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      int i = 0;
      while (true)
      {
        int j = paramCursor.getColumnCount();
        if (i >= j)
          break;
        String str = paramCursor.getColumnName(i);
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(", ");
        i += 1;
      }
      int k = paramCursor.getColumnIndexOrThrow("_id");
      this.mIdIdx = k;
      int m = paramCursor.getColumnIndexOrThrow("Artist");
      this.mArtistIdx = m;
      int n = paramCursor.getColumnIndexOrThrow("Title");
      this.mTrackNameIdx = n;
    }

    public void bindView(View paramView, Context paramContext, Cursor paramCursor)
    {
      DownloadQueueActivity.ViewHolder localViewHolder = (DownloadQueueActivity.ViewHolder)paramView.getTag();
      TextView localTextView1 = localViewHolder.line1;
      int i = this.mTrackNameIdx;
      String str1 = paramCursor.getString(i);
      localTextView1.setText(str1);
      TextView localTextView2 = localViewHolder.line2;
      int j = this.mArtistIdx;
      String str2 = paramCursor.getString(j);
      localTextView2.setText(str2);
      localViewHolder.downloadType.setImageResource(2130837880);
      if (paramCursor.getPosition() == 0)
      {
        localViewHolder.downloadIndicator.setVisibility(0);
        localViewHolder.progressBar.setVisibility(0);
        localViewHolder.progressBar.setProgress(0);
        int k = this.mIdIdx;
        long l = paramCursor.getLong(k);
        BufferProgressListener localBufferProgressListener = localViewHolder.bufferProgressListener;
        ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.DEFAULT;
        ContentIdentifier localContentIdentifier = new ContentIdentifier(l, localDomain);
        localBufferProgressListener.updateCurrentSong(localContentIdentifier, true);
        return;
      }
      localViewHolder.downloadIndicator.setVisibility(8);
      localViewHolder.progressBar.setVisibility(8);
    }

    public void changeCursor(Cursor paramCursor)
    {
      super.changeCursor(paramCursor);
      if (paramCursor == null)
        return;
      getColumnIndexes(paramCursor);
    }

    public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
    {
      View localView = super.newView(paramContext, paramCursor, paramViewGroup);
      DownloadQueueActivity.ViewHolder localViewHolder = new DownloadQueueActivity.ViewHolder(null);
      TextView localTextView1 = (TextView)localView.findViewById(2131296347);
      localViewHolder.line1 = localTextView1;
      TextView localTextView2 = (TextView)localView.findViewById(2131296348);
      localViewHolder.line2 = localTextView2;
      ImageView localImageView1 = (ImageView)localView.findViewById(2131296396);
      localViewHolder.downloadIndicator = localImageView1;
      ImageView localImageView2 = (ImageView)localView.findViewById(2131296397);
      localViewHolder.downloadType = localImageView2;
      SeekBar localSeekBar1 = (SeekBar)localView.findViewById(2131296354);
      localViewHolder.progressBar = localSeekBar1;
      localViewHolder.progressBar.setMax(100);
      SeekBar localSeekBar2 = localViewHolder.progressBar;
      KeeponSchedulingServiceConnection localKeeponSchedulingServiceConnection = DownloadQueueActivity.this.mKeeponSchedulingServiceConnection;
      KeeponBufferProgressListener localKeeponBufferProgressListener = new KeeponBufferProgressListener(localSeekBar2, localKeeponSchedulingServiceConnection);
      localViewHolder.bufferProgressListener = localKeeponBufferProgressListener;
      localView.setTag(localViewHolder);
      return localView;
    }

    protected void onContentChanged()
    {
    }

    public void onMovedToScrapHeap(View paramView)
    {
      Object localObject = paramView.getTag();
      if (!(localObject instanceof DownloadQueueActivity.ViewHolder))
        return;
      ((DownloadQueueActivity.ViewHolder)localObject).bufferProgressListener.cleanup();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.DownloadQueueActivity
 * JD-Core Version:    0.6.2
 */