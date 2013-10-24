package com.google.android.music.youtube;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.MusicUtils;
import java.util.List;

public class VideoSearchActivity extends Activity
{
  YouTubeQueryAsyncTask mAsyncTask;
  private Context mContext;
  private ListView mList;
  View mListContainer;
  boolean mListShown;
  View mProgressContainer;

  private void runQuery()
  {
    stopQueryIfRunning();
    Intent localIntent = getIntent();
    String str1 = localIntent.getStringExtra("trackName");
    String str2 = localIntent.getStringExtra("artistName");
    String str3 = this.mContext.getString(2131230890);
    if (str2.equalsIgnoreCase(str3))
      str2 = "";
    YouTubeQueryAsyncTask localYouTubeQueryAsyncTask1 = new YouTubeQueryAsyncTask(null);
    this.mAsyncTask = localYouTubeQueryAsyncTask1;
    YouTubeQueryAsyncTask localYouTubeQueryAsyncTask2 = this.mAsyncTask;
    String[] arrayOfString = new String[2];
    arrayOfString[0] = str1;
    arrayOfString[1] = str2;
    AsyncTask localAsyncTask = localYouTubeQueryAsyncTask2.execute(arrayOfString);
  }

  private void stopQueryIfRunning()
  {
    if (this.mAsyncTask == null)
      return;
    if (this.mAsyncTask.isCancelled())
      return;
    boolean bool = this.mAsyncTask.cancel(true);
  }

  void makeListShown()
  {
    if (this.mListShown)
      return;
    this.mListShown = true;
    View localView1 = this.mProgressContainer;
    Animation localAnimation1 = AnimationUtils.loadAnimation(this, 17432577);
    localView1.startAnimation(localAnimation1);
    this.mProgressContainer.setVisibility(8);
    View localView2 = this.mListContainer;
    Animation localAnimation2 = AnimationUtils.loadAnimation(this, 17432576);
    localView2.startAnimation(localAnimation2);
    this.mListContainer.setVisibility(0);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.mContext = this;
    setContentView(2130968712);
    View localView1 = findViewById(2131296427);
    this.mProgressContainer = localView1;
    View localView2 = findViewById(2131296428);
    this.mListContainer = localView2;
    ListView localListView1 = (ListView)findViewById(2131296530);
    this.mList = localListView1;
    ListView localListView2 = this.mList;
    AdapterView.OnItemClickListener local1 = new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        YouTubeVideo localYouTubeVideo = (YouTubeVideo)paramAnonymousAdapterView.getItemAtPosition(paramAnonymousInt);
        Context localContext = VideoSearchActivity.this.mContext;
        String str = localYouTubeVideo.getId();
        MusicUtils.startVideoPlayerActivity(localContext, str);
        VideoSearchActivity.this.finish();
      }
    };
    localListView2.setOnItemClickListener(local1);
    getWindow().setLayout(-1, -1);
    runQuery();
  }

  protected void onPause()
  {
    super.onPause();
    stopQueryIfRunning();
    UIStateManager.getInstance().onPause();
  }

  protected void onResume()
  {
    super.onResume();
    runQuery();
    UIStateManager.getInstance().onResume();
  }

  private class VideoListAdapter extends ArrayAdapter<YouTubeVideo>
  {
    private final LayoutInflater mInflater;

    public VideoListAdapter(int paramList, List<YouTubeVideo> arg3)
    {
      super(i, localList);
      LayoutInflater localLayoutInflater = LayoutInflater.from(paramList);
      this.mInflater = localLayoutInflater;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView;
      if (paramView == null)
      {
        localView = this.mInflater.inflate(2130968710, paramViewGroup, false);
        ViewHolder localViewHolder1 = new ViewHolder(localView);
        localView.setTag(localViewHolder1);
      }
      while (true)
      {
        ViewHolder localViewHolder2 = (ViewHolder)localView.getTag();
        YouTubeVideo localYouTubeVideo = (YouTubeVideo)getItem(paramInt);
        localViewHolder2.show(localYouTubeVideo);
        return localView;
        localView = paramView;
      }
    }

    private class ViewHolder
    {
      public final TextView mAuthor;
      public final TextView mDetails;
      public final AsyncAlbumArtImageView mThumbnail;
      public final TextView mTitle;

      public ViewHolder(View arg2)
      {
        Object localObject;
        AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)localObject.findViewById(2131296567);
        this.mThumbnail = localAsyncAlbumArtImageView;
        TextView localTextView1 = (TextView)localObject.findViewById(2131296326);
        this.mTitle = localTextView1;
        TextView localTextView2 = (TextView)localObject.findViewById(2131296568);
        this.mAuthor = localTextView2;
        TextView localTextView3 = (TextView)localObject.findViewById(2131296569);
        this.mDetails = localTextView3;
      }

      public void show(YouTubeVideo paramYouTubeVideo)
      {
        String str1 = paramYouTubeVideo.getThumbnailUrl();
        if (!TextUtils.isEmpty(str1))
          this.mThumbnail.setExternalAlbumArt(str1);
        TextView localTextView1 = this.mTitle;
        String str2 = paramYouTubeVideo.getTitle();
        localTextView1.setText(str2);
        TextView localTextView2 = this.mAuthor;
        String str3 = paramYouTubeVideo.getAuthor();
        localTextView2.setText(str3);
        if (paramYouTubeVideo.isHd());
        for (int i = 2131230736; ; i = 2131230737)
        {
          TextView localTextView3 = this.mDetails;
          String str4 = VideoSearchActivity.this.getString(i);
          Object[] arrayOfObject = new Object[2];
          Context localContext = VideoSearchActivity.this.mContext;
          int j = paramYouTubeVideo.getDurationSeconds();
          String str5 = YouTubeUtils.formatDurationSeconds(localContext, j);
          arrayOfObject[0] = str5;
          Long localLong = Long.valueOf(paramYouTubeVideo.getViewCount());
          arrayOfObject[1] = localLong;
          String str6 = String.format(str4, arrayOfObject);
          localTextView3.setText(str6);
          return;
        }
      }
    }
  }

  private class YouTubeQueryAsyncTask extends AsyncTask<String, Void, List<YouTubeVideo>>
  {
    private YouTubeQueryAsyncTask()
    {
    }

    protected List<YouTubeVideo> doInBackground(String[] paramArrayOfString)
    {
      String str1 = paramArrayOfString[0];
      String str2 = paramArrayOfString[1];
      String str3 = str1;
      if (!str2.isEmpty())
        str3 = str3 + " " + str2;
      return YouTubeUtils.searchForMatchingVideos(str3.replaceAll("[,&\\-\\|\\s]+", " "), 1, 5);
    }

    protected void onPostExecute(List<YouTubeVideo> paramList)
    {
      if (!paramList.isEmpty())
      {
        if (paramList.size() == 1)
        {
          Context localContext1 = VideoSearchActivity.this.mContext;
          String str1 = ((YouTubeVideo)paramList.get(0)).getId();
          MusicUtils.startVideoPlayerActivity(localContext1, str1);
          VideoSearchActivity.this.finish();
          return;
        }
        ListView localListView = VideoSearchActivity.this.mList;
        VideoSearchActivity localVideoSearchActivity = VideoSearchActivity.this;
        Context localContext2 = VideoSearchActivity.this.mContext;
        VideoSearchActivity.VideoListAdapter localVideoListAdapter = new VideoSearchActivity.VideoListAdapter(localVideoSearchActivity, localContext2, 2130968712, paramList);
        localListView.setAdapter(localVideoListAdapter);
        VideoSearchActivity.this.makeListShown();
        return;
      }
      VideoSearchActivity.this.makeListShown();
      Context localContext3 = VideoSearchActivity.this.mContext;
      AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localContext3);
      String str2 = VideoSearchActivity.this.getString(2131230735);
      AlertDialog.Builder localBuilder2 = localBuilder1.setMessage(str2).setNegativeButton(2131230741, null);
      DialogInterface.OnDismissListener local1 = new DialogInterface.OnDismissListener()
      {
        public void onDismiss(DialogInterface paramAnonymousDialogInterface)
        {
          VideoSearchActivity.this.finish();
        }
      };
      AlertDialog localAlertDialog = localBuilder2.setOnDismissListener(local1).show();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.youtube.VideoSearchActivity
 * JD-Core Version:    0.6.2
 */