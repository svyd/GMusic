package com.google.android.music;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.net.Uri.Builder;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.google.android.music.utils.MusicUtils;
import java.io.IOException;
import java.text.Collator;
import java.util.Formatter;
import java.util.Locale;

public class MusicPicker extends ListActivity
  implements MediaPlayer.OnCompletionListener, View.OnClickListener
{
  static final String[] CURSOR_COLS;
  static StringBuilder sFormatBuilder;
  static Formatter sFormatter = new Formatter(localStringBuilder, localLocale);
  static final Object[] sTimeArgs = new Object[5];
  TrackListAdapter mAdapter;
  Uri mBaseUri;
  View mCancelButton;
  Cursor mCursor;
  View mListContainer;
  boolean mListHasFocus;
  boolean mListShown;
  Parcelable mListState = null;
  MediaPlayer mMediaPlayer;
  View mOkayButton;
  long mPlayingId = 65535L;
  View mProgressContainer;
  QueryHandler mQueryHandler;
  long mSelectedId = 65535L;
  Uri mSelectedUri;
  int mSortMode = -1;
  String mSortOrder;

  static
  {
    String[] arrayOfString = new String[9];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "title_key";
    arrayOfString[3] = "_data";
    arrayOfString[4] = "album";
    arrayOfString[5] = "artist";
    arrayOfString[6] = "artist_id";
    arrayOfString[7] = "duration";
    arrayOfString[8] = "track";
    CURSOR_COLS = arrayOfString;
    sFormatBuilder = new StringBuilder();
    StringBuilder localStringBuilder = sFormatBuilder;
    Locale localLocale = Locale.getDefault();
  }

  Cursor doQuery(boolean paramBoolean, String paramString)
  {
    this.mQueryHandler.cancelOperation(42);
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("title != ''");
    String[] arrayOfString1 = null;
    if (paramString != null)
    {
      String[] arrayOfString2 = paramString.split(" ");
      arrayOfString1 = new String[arrayOfString2.length];
      Collator.getInstance().setStrength(0);
      int i = 0;
      while (true)
      {
        int j = arrayOfString2.length;
        if (i >= j)
          break;
        String str1 = MediaStore.Audio.keyFor(arrayOfString2[i]).replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        StringBuilder localStringBuilder3 = new StringBuilder().append('%');
        String str2 = str1;
        String str3 = str2 + '%';
        arrayOfString1[i] = str3;
        i += 1;
      }
      int k = 0;
      while (true)
      {
        int m = arrayOfString2.length;
        if (k >= m)
          break;
        StringBuilder localStringBuilder4 = localStringBuilder1.append(" AND ");
        StringBuilder localStringBuilder5 = localStringBuilder1.append("artist_key||");
        StringBuilder localStringBuilder6 = localStringBuilder1.append("album_key||");
        StringBuilder localStringBuilder7 = localStringBuilder1.append("title_key LIKE ? ESCAPE '\\'");
        k += 1;
      }
    }
    if (paramBoolean);
    try
    {
      Uri localUri1 = this.mBaseUri;
      String[] arrayOfString3 = CURSOR_COLS;
      String str4 = localStringBuilder1.toString();
      String str5 = this.mSortOrder;
      Cursor localCursor1 = MusicUtils.query(this, localUri1, arrayOfString3, str4, arrayOfString1, str5, false, false);
      label324: for (Cursor localCursor2 = localCursor1; ; localCursor2 = null)
      {
        return localCursor2;
        this.mAdapter.setLoading(true);
        setProgressBarIndeterminateVisibility(true);
        QueryHandler localQueryHandler = this.mQueryHandler;
        Uri localUri2 = this.mBaseUri;
        String[] arrayOfString4 = CURSOR_COLS;
        String str6 = localStringBuilder1.toString();
        String str7 = this.mSortOrder;
        String[] arrayOfString5 = arrayOfString1;
        localQueryHandler.startQuery(42, null, localUri2, arrayOfString4, str6, arrayOfString5, str7);
      }
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      break label324;
    }
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

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    default:
      return;
    case 2131296429:
      if (this.mSelectedId < 0L)
        return;
      Intent localIntent1 = new Intent();
      Uri localUri = this.mSelectedUri;
      Intent localIntent2 = localIntent1.setData(localUri);
      setResult(-1, localIntent2);
      finish();
      return;
    case 2131296430:
    }
    finish();
  }

  public void onCompletion(MediaPlayer paramMediaPlayer)
  {
    if (this.mMediaPlayer != paramMediaPlayer)
      return;
    paramMediaPlayer.stop();
    paramMediaPlayer.release();
    this.mMediaPlayer = null;
    this.mPlayingId = 65535L;
    getListView().invalidateViews();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool1 = requestWindowFeature(5);
    int i = 1;
    if (paramBundle == null)
    {
      Uri localUri1 = (Uri)getIntent().getParcelableExtra("android.intent.extra.ringtone.EXISTING_URI");
      this.mSelectedUri = localUri1;
      String str1 = getIntent().getAction();
      if (!"android.intent.action.GET_CONTENT".equals(str1))
        break label420;
      Uri localUri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
      this.mBaseUri = localUri2;
    }
    label420: 
    do
    {
      setContentView(2130968636);
      this.mSortOrder = "title_key";
      final ListView localListView = getListView();
      localListView.setItemsCanFocus(false);
      String[] arrayOfString = new String[0];
      int[] arrayOfInt = new int[0];
      MusicPicker localMusicPicker1 = this;
      MusicPicker localMusicPicker2 = this;
      TrackListAdapter localTrackListAdapter1 = new TrackListAdapter(localMusicPicker2, localListView, 2130968637, arrayOfString, arrayOfInt);
      this.mAdapter = localTrackListAdapter1;
      TrackListAdapter localTrackListAdapter2 = this.mAdapter;
      setListAdapter(localTrackListAdapter2);
      localListView.setTextFilterEnabled(true);
      localListView.setSaveEnabled(false);
      QueryHandler localQueryHandler = new QueryHandler(this);
      this.mQueryHandler = localQueryHandler;
      View localView1 = findViewById(2131296427);
      this.mProgressContainer = localView1;
      View localView2 = findViewById(2131296428);
      this.mListContainer = localView2;
      View localView3 = findViewById(2131296429);
      this.mOkayButton = localView3;
      this.mOkayButton.setOnClickListener(this);
      View localView4 = findViewById(2131296430);
      this.mCancelButton = localView4;
      this.mCancelButton.setOnClickListener(this);
      if (this.mSelectedUri != null)
      {
        Uri.Builder localBuilder1 = this.mSelectedUri.buildUpon();
        String str2 = this.mSelectedUri.getEncodedPath();
        int j = str2.lastIndexOf('/');
        if (j >= 0)
          str2 = str2.substring(0, j);
        Uri.Builder localBuilder2 = localBuilder1.encodedPath(str2);
        Uri localUri3 = localBuilder1.build();
        Uri localUri4 = this.mBaseUri;
        if (localUri3.equals(localUri4))
        {
          long l = ContentUris.parseId(this.mSelectedUri);
          this.mSelectedId = l;
        }
      }
      boolean bool2 = setSortMode(i);
      return;
      Uri localUri5 = (Uri)paramBundle.getParcelable("android.intent.extra.ringtone.EXISTING_URI");
      this.mSelectedUri = localUri5;
      Parcelable localParcelable = paramBundle.getParcelable("liststate");
      this.mListState = localParcelable;
      boolean bool3 = paramBundle.getBoolean("focused");
      this.mListHasFocus = bool3;
      i = paramBundle.getInt("sortMode", 1);
      break;
      Uri localUri6 = getIntent().getData();
      this.mBaseUri = localUri6;
    }
    while (this.mBaseUri != null);
    int k = Log.w("MusicPicker", "No data URI given to PICK action");
    finish();
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    boolean bool = super.onCreateOptionsMenu(paramMenu);
    MenuItem localMenuItem1 = paramMenu.add(0, 1, 0, 2131230944);
    MenuItem localMenuItem2 = paramMenu.add(0, 2, 0, 2131231041);
    MenuItem localMenuItem3 = paramMenu.add(0, 3, 0, 2131231040);
    return true;
  }

  protected void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    boolean bool = this.mCursor.moveToPosition(paramInt);
    Cursor localCursor = this.mCursor;
    setSelected(localCursor);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getItemId();
    if (setSortMode(i));
    for (boolean bool = true; ; bool = super.onOptionsItemSelected(paramMenuItem))
      return bool;
  }

  public void onPause()
  {
    super.onPause();
    stopMediaPlayer();
  }

  public void onRestart()
  {
    super.onRestart();
    Cursor localCursor = doQuery(false, null);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Parcelable localParcelable = getListView().onSaveInstanceState();
    paramBundle.putParcelable("liststate", localParcelable);
    boolean bool = getListView().hasFocus();
    paramBundle.putBoolean("focused", bool);
    int i = this.mSortMode;
    paramBundle.putInt("sortMode", i);
  }

  public void onStop()
  {
    super.onStop();
    this.mAdapter.setLoading(true);
    this.mAdapter.changeCursor(null);
  }

  void setSelected(Cursor paramCursor)
  {
    Uri localUri1 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    Cursor localCursor = this.mCursor;
    int i = this.mCursor.getColumnIndex("_id");
    long l1 = localCursor.getLong(i);
    Uri localUri2 = ContentUris.withAppendedId(localUri1, l1);
    this.mSelectedUri = localUri2;
    this.mSelectedId = l1;
    long l2 = this.mPlayingId;
    if ((l1 != l2) || (this.mMediaPlayer == null))
    {
      stopMediaPlayer();
      MediaPlayer localMediaPlayer1 = new MediaPlayer();
      this.mMediaPlayer = localMediaPlayer1;
      try
      {
        MediaPlayer localMediaPlayer2 = this.mMediaPlayer;
        Uri localUri3 = this.mSelectedUri;
        localMediaPlayer2.setDataSource(this, localUri3);
        this.mMediaPlayer.setOnCompletionListener(this);
        this.mMediaPlayer.setAudioStreamType(3);
        this.mMediaPlayer.prepare();
        this.mMediaPlayer.start();
        this.mPlayingId = l1;
        getListView().invalidateViews();
        return;
      }
      catch (IOException localIOException)
      {
        int j = Log.w("MusicPicker", "Unable to play track", localIOException);
        return;
      }
    }
    if (this.mMediaPlayer == null)
      return;
    stopMediaPlayer();
    getListView().invalidateViews();
  }

  boolean setSortMode(int paramInt)
  {
    boolean bool = true;
    int i = this.mSortMode;
    if (paramInt != i);
    switch (paramInt)
    {
    default:
      bool = false;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return bool;
      this.mSortMode = paramInt;
      this.mSortOrder = "title_key";
      Cursor localCursor1 = doQuery(false, null);
      continue;
      this.mSortMode = paramInt;
      this.mSortOrder = "album_key ASC, track ASC, title_key ASC";
      Cursor localCursor2 = doQuery(false, null);
      continue;
      this.mSortMode = paramInt;
      this.mSortOrder = "artist_key ASC, album_key ASC, track ASC, title_key ASC";
      Cursor localCursor3 = doQuery(false, null);
    }
  }

  void stopMediaPlayer()
  {
    if (this.mMediaPlayer == null)
      return;
    this.mMediaPlayer.stop();
    this.mMediaPlayer.release();
    this.mMediaPlayer = null;
    this.mPlayingId = 65535L;
  }

  private final class QueryHandler extends AsyncQueryHandler
  {
    public QueryHandler(Context arg2)
    {
      super();
    }

    protected void onQueryComplete(int paramInt, Object paramObject, Cursor paramCursor)
    {
      if (!MusicPicker.this.isFinishing())
      {
        MusicPicker.this.mAdapter.setLoading(false);
        MusicPicker.this.mAdapter.changeCursor(paramCursor);
        MusicPicker.this.setProgressBarIndeterminateVisibility(false);
        if (MusicPicker.this.mListState == null)
          return;
        ListView localListView = MusicPicker.this.getListView();
        Parcelable localParcelable = MusicPicker.this.mListState;
        localListView.onRestoreInstanceState(localParcelable);
        if (MusicPicker.this.mListHasFocus)
          boolean bool = MusicPicker.this.getListView().requestFocus();
        MusicPicker.this.mListHasFocus = false;
        MusicPicker.this.mListState = null;
        return;
      }
      paramCursor.close();
    }
  }

  class TrackListAdapter extends SimpleCursorAdapter
  {
    private int mAlbumIdx;
    private int mArtistIdx;
    private final StringBuilder mBuilder;
    private int mDurationIdx;
    private int mIdIdx;
    final ListView mListView;
    private boolean mLoading;
    private int mTitleIdx;
    private final String mUnknownAlbum;
    private final String mUnknownArtist;

    TrackListAdapter(Context paramListView, ListView paramInt, int paramArrayOfString, String[] paramArrayOfInt, int[] arg6)
    {
    }

    public void bindView(View paramView, Context paramContext, Cursor paramCursor)
    {
      ViewHolder localViewHolder = (ViewHolder)paramView.getTag();
      int i = this.mTitleIdx;
      CharArrayBuffer localCharArrayBuffer = localViewHolder.buffer1;
      paramCursor.copyStringToBuffer(i, localCharArrayBuffer);
      TextView localTextView1 = localViewHolder.line1;
      char[] arrayOfChar1 = localViewHolder.buffer1.data;
      int j = localViewHolder.buffer1.sizeCopied;
      localTextView1.setText(arrayOfChar1, 0, j);
      int k = this.mDurationIdx;
      int m = paramCursor.getInt(k) / 1000;
      StringBuilder localStringBuilder1;
      String str1;
      label173: String str3;
      label228: long l1;
      RadioButton localRadioButton;
      if (m == 0)
      {
        localViewHolder.duration.setText("");
        localStringBuilder1 = this.mBuilder;
        int n = localStringBuilder1.length();
        StringBuilder localStringBuilder2 = localStringBuilder1.delete(0, n);
        int i1 = this.mAlbumIdx;
        str1 = paramCursor.getString(i1);
        if ((str1 != null) && (!str1.equals("<unknown>")))
          break label419;
        String str2 = this.mUnknownAlbum;
        StringBuilder localStringBuilder3 = localStringBuilder1.append(str2);
        StringBuilder localStringBuilder4 = localStringBuilder1.append('\n');
        int i2 = this.mArtistIdx;
        str3 = paramCursor.getString(i2);
        if ((str3 != null) && (!str3.equals("<unknown>")))
          break label431;
        String str4 = this.mUnknownArtist;
        StringBuilder localStringBuilder5 = localStringBuilder1.append(str4);
        int i3 = localStringBuilder1.length();
        if (localViewHolder.buffer2.length < i3)
        {
          char[] arrayOfChar2 = new char[i3];
          localViewHolder.buffer2 = arrayOfChar2;
        }
        char[] arrayOfChar3 = localViewHolder.buffer2;
        localStringBuilder1.getChars(0, i3, arrayOfChar3, 0);
        TextView localTextView2 = localViewHolder.line2;
        char[] arrayOfChar4 = localViewHolder.buffer2;
        localTextView2.setText(arrayOfChar4, 0, i3);
        int i4 = this.mIdIdx;
        l1 = paramCursor.getLong(i4);
        localRadioButton = localViewHolder.radio;
        long l2 = MusicPicker.this.mSelectedId;
        if (l1 != l2)
          break label443;
      }
      ImageView localImageView;
      label419: label431: label443: for (boolean bool = true; ; bool = false)
      {
        localRadioButton.setChecked(bool);
        localImageView = localViewHolder.play_indicator;
        long l3 = MusicPicker.this.mPlayingId;
        if (l1 != l3)
          break label449;
        localImageView.setImageResource(2130837783);
        localImageView.setVisibility(0);
        return;
        TextView localTextView3 = localViewHolder.duration;
        long l4 = m;
        String str5 = MusicUtils.makeTimeString(paramContext, l4);
        localTextView3.setText(str5);
        break;
        StringBuilder localStringBuilder6 = localStringBuilder1.append(str1);
        break label173;
        StringBuilder localStringBuilder7 = localStringBuilder1.append(str3);
        break label228;
      }
      label449: localImageView.setVisibility(8);
    }

    public void changeCursor(Cursor paramCursor)
    {
      super.changeCursor(paramCursor);
      MusicPicker.this.mCursor = paramCursor;
      if (paramCursor != null)
      {
        int i = paramCursor.getColumnIndex("_id");
        this.mIdIdx = i;
        int j = paramCursor.getColumnIndex("title");
        this.mTitleIdx = j;
        int k = paramCursor.getColumnIndex("artist");
        this.mArtistIdx = k;
        int m = paramCursor.getColumnIndex("album");
        this.mAlbumIdx = m;
        int n = paramCursor.getColumnIndex("duration");
        this.mDurationIdx = n;
      }
      MusicPicker.this.makeListShown();
    }

    public boolean isEmpty()
    {
      if (this.mLoading);
      for (boolean bool = false; ; bool = super.isEmpty())
        return bool;
    }

    public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
    {
      View localView = super.newView(paramContext, paramCursor, paramViewGroup);
      ViewHolder localViewHolder = new ViewHolder();
      TextView localTextView1 = (TextView)localView.findViewById(2131296347);
      localViewHolder.line1 = localTextView1;
      TextView localTextView2 = (TextView)localView.findViewById(2131296348);
      localViewHolder.line2 = localTextView2;
      TextView localTextView3 = (TextView)localView.findViewById(2131296399);
      localViewHolder.duration = localTextView3;
      RadioButton localRadioButton = (RadioButton)localView.findViewById(2131296330);
      localViewHolder.radio = localRadioButton;
      ImageView localImageView = (ImageView)localView.findViewById(2131296415);
      localViewHolder.play_indicator = localImageView;
      CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(100);
      localViewHolder.buffer1 = localCharArrayBuffer;
      char[] arrayOfChar = new char['È'];
      localViewHolder.buffer2 = arrayOfChar;
      localView.setTag(localViewHolder);
      return localView;
    }

    public Cursor runQueryOnBackgroundThread(CharSequence paramCharSequence)
    {
      MusicPicker localMusicPicker = MusicPicker.this;
      String str = paramCharSequence.toString();
      return localMusicPicker.doQuery(true, str);
    }

    public void setLoading(boolean paramBoolean)
    {
      this.mLoading = paramBoolean;
    }

    class ViewHolder
    {
      CharArrayBuffer buffer1;
      char[] buffer2;
      TextView duration;
      TextView line1;
      TextView line2;
      ImageView play_indicator;
      RadioButton radio;

      ViewHolder()
      {
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.MusicPicker
 * JD-Core Version:    0.6.2
 */