package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.PlayingIndicator;
import com.google.android.music.StatefulShadowTextView;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SelectedSongList;
import com.google.android.music.medialist.SharedSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.ViewUtils;

public class TrackListAdapter extends MediaListCursorAdapter
{
  static String[] PROJECTION = arrayOfString;
  int mAlbumArtistIdIdx;
  int mAlbumIdIdx;
  int mAlbumIdx;
  int mAlbumMetajamIdx;
  int mAlbumStoreIdIdx;
  int mArtistIdx;
  int mArtistMetajamIdx;
  int mArtworkUrlIdx;
  int mAudioIdIdx;
  private BaseTrackListView.OnContentChangedCallback mCallback;
  private Boolean mCanShowPlayIndicator;
  private View.OnClickListener mContextClickListener;
  private ContentIdentifier mCurrentAudioId;
  private int mCurrentPlayPosition;
  int mDomainIdx;
  private Drawable mDragHandleBG;
  int mDurationIdx;
  private boolean mEditMode;
  int mHasRemoteIdx;
  private boolean mHasRemovedItem;
  private Boolean mIsInInfiniteMix;
  int mIsLocalIdx;
  private Boolean mIsShowingNowPlaying;
  int mPlaylistMemberIdIdx;
  private boolean mShowAlbumArt;
  private boolean mShowTrackArtist;
  int mSongIdIdx;
  private SongList mSongList;
  int mStoreSongIdx;
  private SparseIntArray mTempRowMapping;
  int mTitleIdx;
  int mTrackArtistIdIdx;
  int mTrackMetajamIdx;
  private final String mUnknownAlbum;
  private final String mUnknownArtist;
  int mYearIdx;

  static
  {
    String[] arrayOfString = new String[23];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "audio_id";
    arrayOfString[2] = "title";
    arrayOfString[3] = "album";
    arrayOfString[4] = "artist";
    arrayOfString[5] = "AlbumArtistId";
    arrayOfString[6] = "artist_id";
    arrayOfString[7] = "StoreAlbumId";
    arrayOfString[8] = "artistSort";
    arrayOfString[9] = "duration";
    arrayOfString[10] = "album_id";
    arrayOfString[11] = "hasRemote";
    arrayOfString[12] = "hasLocal";
    arrayOfString[13] = "year";
    arrayOfString[14] = "Genre";
    arrayOfString[15] = "StoreId";
    arrayOfString[16] = "SongId";
    arrayOfString[17] = "Domain";
    arrayOfString[18] = "Nid";
    arrayOfString[19] = "StoreAlbumId";
    arrayOfString[20] = "ArtistMetajamId";
    arrayOfString[21] = "ArtistArtLocation";
    arrayOfString[22] = "artworkUrl";
  }

  TrackListAdapter(MusicFragment paramMusicFragment, boolean paramBoolean, MediaList paramMediaList)
  {
  }

  TrackListAdapter(MusicFragment paramMusicFragment, boolean paramBoolean, MediaList paramMediaList, Cursor paramCursor)
  {
  }

  private String getAlbum(Cursor paramCursor)
  {
    int i = this.mAlbumIdx;
    String str = paramCursor.getString(i);
    if (MusicUtils.isUnknown(str))
      str = this.mUnknownAlbum;
    return str;
  }

  private String getAlbumMetajamId(Cursor paramCursor)
  {
    int j;
    if ((this.mAlbumMetajamIdx >= 0) && (paramCursor != null))
    {
      int i = this.mAlbumMetajamIdx;
      if (!paramCursor.isNull(i))
        j = this.mAlbumMetajamIdx;
    }
    for (String str = paramCursor.getString(j); ; str = null)
      return str;
  }

  private String getArtistMetajamId(Cursor paramCursor)
  {
    int j;
    if ((this.mArtistMetajamIdx >= 0) && (paramCursor != null))
    {
      int i = this.mArtistMetajamIdx;
      if (!paramCursor.isNull(i))
        j = this.mArtistMetajamIdx;
    }
    for (String str = paramCursor.getString(j); ; str = null)
      return str;
  }

  private void getColumnIndices(Cursor paramCursor)
  {
    if (paramCursor == null)
      return;
    int i = paramCursor.getColumnIndexOrThrow("title");
    this.mTitleIdx = i;
    int j = paramCursor.getColumnIndexOrThrow("artist");
    this.mArtistIdx = j;
    int k = paramCursor.getColumnIndexOrThrow("AlbumArtistId");
    this.mAlbumArtistIdIdx = k;
    int m = paramCursor.getColumnIndexOrThrow("artist_id");
    this.mTrackArtistIdIdx = m;
    int n = paramCursor.getColumnIndexOrThrow("year");
    this.mYearIdx = n;
    int i1 = paramCursor.getColumnIndexOrThrow("duration");
    this.mDurationIdx = i1;
    int i2 = paramCursor.getColumnIndexOrThrow("audio_id");
    this.mAudioIdIdx = i2;
    if ((this.mSongList instanceof PlaylistSongList));
    for (int i3 = paramCursor.getColumnIndexOrThrow("_id"); ; i3 = -1)
    {
      this.mPlaylistMemberIdIdx = i3;
      int i4 = paramCursor.getColumnIndexOrThrow("album");
      this.mAlbumIdx = i4;
      int i5 = paramCursor.getColumnIndexOrThrow("album_id");
      this.mAlbumIdIdx = i5;
      int i6 = paramCursor.getColumnIndexOrThrow("StoreAlbumId");
      this.mAlbumStoreIdIdx = i6;
      int i7 = paramCursor.getColumnIndexOrThrow("hasLocal");
      this.mIsLocalIdx = i7;
      int i8 = paramCursor.getColumnIndexOrThrow("hasRemote");
      this.mHasRemoteIdx = i8;
      int i9 = paramCursor.getColumnIndexOrThrow("Domain");
      this.mDomainIdx = i9;
      int i10 = paramCursor.getColumnIndexOrThrow("SongId");
      this.mSongIdIdx = i10;
      int i11 = paramCursor.getColumnIndexOrThrow("StoreId");
      this.mStoreSongIdx = i11;
      int i12 = paramCursor.getColumnIndex("Nid");
      this.mTrackMetajamIdx = i12;
      int i13 = paramCursor.getColumnIndex("StoreAlbumId");
      this.mAlbumMetajamIdx = i13;
      int i14 = paramCursor.getColumnIndex("ArtistMetajamId");
      this.mArtistMetajamIdx = i14;
      int i15 = paramCursor.getColumnIndex("artworkUrl");
      this.mArtworkUrlIdx = i15;
      return;
    }
  }

  private long getPlaylistMemberId(Cursor paramCursor)
  {
    int i;
    if ((this.mPlaylistMemberIdIdx >= 0) && (paramCursor != null))
      i = this.mPlaylistMemberIdIdx;
    for (long l = paramCursor.getLong(i); ; l = 65535L)
      return l;
  }

  private String getTrackArtist(Cursor paramCursor)
  {
    int i = this.mArtistIdx;
    String str = paramCursor.getString(i);
    if (MusicUtils.isUnknown(str))
      str = this.mUnknownArtist;
    return str;
  }

  private String getTrackMetajamId(Cursor paramCursor)
  {
    int j;
    if ((this.mTrackMetajamIdx >= 0) && (paramCursor != null))
    {
      int i = this.mTrackMetajamIdx;
      if (!paramCursor.isNull(i))
        j = this.mTrackMetajamIdx;
    }
    for (String str = paramCursor.getString(j); ; str = null)
      return str;
  }

  private boolean isInInfiniteMixMode()
  {
    if (this.mIsInInfiniteMix == null)
    {
      if (MusicUtils.sService == null)
        break label35;
      Boolean localBoolean = Boolean.valueOf(MusicUtils.isInIniniteMixMode());
      this.mIsInInfiniteMix = localBoolean;
    }
    label35: for (boolean bool = this.mIsInInfiniteMix.booleanValue(); ; bool = false)
      return bool;
  }

  private boolean isShowingNowPlaying()
  {
    if (this.mIsShowingNowPlaying == null)
    {
      if (this.mSongList == null)
        break label49;
      SongList localSongList1 = this.mSongList;
      SongList localSongList2 = MusicUtils.getNowPlayingList();
      Boolean localBoolean = Boolean.valueOf(localSongList1.equals(localSongList2));
      this.mIsShowingNowPlaying = localBoolean;
    }
    label49: for (boolean bool = this.mIsShowingNowPlaying.booleanValue(); ; bool = false)
      return bool;
  }

  private void populateDocumentFromCursor(Document paramDocument, Cursor paramCursor)
  {
    int i = 0;
    paramDocument.reset();
    Document.Type localType = Document.Type.TRACK;
    paramDocument.setType(localType);
    long l1 = paramCursor.getLong(i);
    paramDocument.setId(l1);
    if (!paramDocument.isNautilus())
    {
      int j = this.mAudioIdIdx;
      long l2 = paramCursor.getLong(j);
      paramDocument.setId(l2);
    }
    int k = this.mTitleIdx;
    String str1 = paramCursor.getString(k);
    paramDocument.setTitle(str1);
    long l3 = getPlaylistMemberId(paramCursor);
    paramDocument.setIdInParent(l3);
    int m = this.mAlbumIdIdx;
    long l4 = paramCursor.getLong(m);
    paramDocument.setAlbumId(l4);
    int n = this.mAlbumStoreIdIdx;
    if (!paramCursor.isNull(n))
    {
      int i1 = this.mAlbumStoreIdIdx;
      String str2 = paramCursor.getString(i1);
      paramDocument.setAlbumMetajamId(str2);
    }
    String str3 = getAlbum(paramCursor);
    paramDocument.setAlbumName(str3);
    String str4 = getTrackArtist(paramCursor);
    paramDocument.setArtistName(str4);
    int i2 = this.mTrackArtistIdIdx;
    long l5 = paramCursor.getLong(i2);
    paramDocument.setArtistId(l5);
    String str5 = getTrackMetajamId(paramCursor);
    paramDocument.setTrackMetajamId(str5);
    String str6 = getAlbumMetajamId(paramCursor);
    paramDocument.setAlbumMetajamId(str6);
    String str7 = getArtistMetajamId(paramCursor);
    paramDocument.setArtistMetajamId(str7);
    int i3 = this.mStoreSongIdx;
    if (!paramCursor.isNull(i3))
    {
      int i4 = this.mStoreSongIdx;
      String str8 = paramCursor.getString(i4);
      paramDocument.setSongStoreId(str8);
    }
    int i5 = this.mArtworkUrlIdx;
    if (!paramCursor.isNull(i5))
    {
      int i6 = this.mArtworkUrlIdx;
      String str9 = paramCursor.getString(i6);
      paramDocument.setArtUrl(str9);
    }
    int i7 = this.mDomainIdx;
    if (paramCursor.getInt(i7) == 4)
      i = 1;
    paramDocument.setIsNautilus(i);
  }

  private void resetTempState()
  {
    this.mTempRowMapping.clear();
    this.mHasRemovedItem = false;
  }

  protected void bindViewToLoadingItem(View paramView, Context paramContext)
  {
  }

  public void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong)
  {
    ViewHolder localViewHolder = (ViewHolder)paramView.getTag();
    Document localDocument = localViewHolder.document;
    TrackListAdapter localTrackListAdapter = this;
    Cursor localCursor1 = paramCursor;
    localTrackListAdapter.populateDocumentFromCursor(localDocument, localCursor1);
    int i = this.mTitleIdx;
    CharArrayBuffer localCharArrayBuffer1 = localViewHolder.titleBuffer;
    Cursor localCursor2 = paramCursor;
    CharArrayBuffer localCharArrayBuffer2 = localCharArrayBuffer1;
    localCursor2.copyStringToBuffer(i, localCharArrayBuffer2);
    StatefulShadowTextView localStatefulShadowTextView1 = localViewHolder.title;
    char[] arrayOfChar = localViewHolder.titleBuffer.data;
    int j = localViewHolder.titleBuffer.sizeCopied;
    localStatefulShadowTextView1.setText(arrayOfChar, 0, j);
    boolean bool1 = this.mSongList instanceof SharedSongList;
    boolean bool2;
    label183: int n;
    label226: label248: boolean bool3;
    label277: boolean bool4;
    if (localViewHolder.contextMenu != null)
    {
      if (bool1)
        localViewHolder.contextMenu.setVisibility(4);
    }
    else
    {
      bool2 = isShowingNowPlaying();
      if (localViewHolder.play_indicator != null)
      {
        bool2 = false;
        if (canShowPlaybackIndicator())
        {
          if (!bool2)
            break label787;
          int k = paramCursor.getPosition();
          int m = this.mCurrentPlayPosition;
          if (k == m)
            break label781;
          bool2 = true;
          if ((!bool2) && (((this.mSongList instanceof PlaylistSongList)) || ((this.mSongList instanceof SelectedSongList))))
            bool2 = false;
        }
        PlayingIndicator localPlayingIndicator = localViewHolder.play_indicator;
        if (!bool2)
          break label839;
        n = 0;
        localPlayingIndicator.setVisibility(n);
        if (!bool2)
          break label846;
        localViewHolder.title.setTypeface(null, 1);
      }
      if (!UIStateManager.getInstance().isStreamingEnabled())
      {
        int i1 = this.mIsLocalIdx;
        if (paramCursor.getInt(i1) == 0)
          break label859;
      }
      bool3 = true;
      localViewHolder.isAvailable = bool3;
      int i2 = this.mHasRemoteIdx;
      if (paramCursor.getInt(i2) == 0)
        break label865;
      bool4 = true;
      label304: localViewHolder.hasRemote = bool4;
      StatefulShadowTextView localStatefulShadowTextView2 = localViewHolder.title;
      boolean bool5 = true;
      localStatefulShadowTextView2.setPrimaryAndOnline(bool5, bool3);
      if (localViewHolder.duration != null)
      {
        StatefulShadowTextView localStatefulShadowTextView3 = localViewHolder.duration;
        boolean bool6 = true;
        localStatefulShadowTextView3.setPrimaryAndOnline(bool6, bool3);
      }
      if (localViewHolder.artist != null)
      {
        StatefulShadowTextView localStatefulShadowTextView4 = localViewHolder.artist;
        boolean bool7 = false;
        localStatefulShadowTextView4.setPrimaryAndOnline(bool7, bool3);
      }
      if (localViewHolder.album != null)
      {
        StatefulShadowTextView localStatefulShadowTextView5 = localViewHolder.album;
        boolean bool8 = true;
        localStatefulShadowTextView5.setPrimaryAndOnline(bool8, bool3);
      }
      if (localViewHolder.year != null)
      {
        StatefulShadowTextView localStatefulShadowTextView6 = localViewHolder.year;
        boolean bool9 = true;
        localStatefulShadowTextView6.setPrimaryAndOnline(bool9, bool3);
      }
      if ((localViewHolder.year != null) && (this.mYearIdx >= 0))
      {
        int i3 = this.mYearIdx;
        int i4 = paramCursor.getInt(i3);
        if (i4 <= 0)
          break label871;
        StatefulShadowTextView localStatefulShadowTextView7 = localViewHolder.year;
        String str1 = Integer.toString(i4);
        localStatefulShadowTextView7.setText(str1);
        localViewHolder.year.setVisibility(0);
      }
      label504: if (localViewHolder.album != null)
      {
        StatefulShadowTextView localStatefulShadowTextView8 = localViewHolder.album;
        String str2 = localViewHolder.document.getAlbumName();
        localStatefulShadowTextView8.setText(str2);
      }
      if (localViewHolder.artist != null)
      {
        if (!this.mShowTrackArtist)
          break label883;
        StatefulShadowTextView localStatefulShadowTextView9 = localViewHolder.artist;
        String str3 = localViewHolder.document.getArtistName();
        localStatefulShadowTextView9.setText(str3);
        localViewHolder.artist.setVisibility(0);
      }
      label584: if (localViewHolder.duration != null)
      {
        int i5 = this.mDurationIdx;
        int i6 = paramCursor.getInt(i5) / 1000;
        StatefulShadowTextView localStatefulShadowTextView10 = localViewHolder.duration;
        Context localContext = getContext();
        long l1 = i6;
        String str4 = MusicUtils.makeTimeString(localContext, l1);
        localStatefulShadowTextView10.setText(str4);
      }
      if (localViewHolder.icon != null)
      {
        if ((this.mShowAlbumArt) && (!bool1))
          break label896;
        localViewHolder.icon.setVisibility(8);
      }
      label676: if (bool2)
      {
        if (!isInInfiniteMixMode())
          break label985;
        int i7 = paramCursor.getCount();
        int i8 = paramCursor.getPosition();
        ViewUtils.fadeViewForPosition(paramView, i7, i8, 4);
      }
    }
    while (true)
    {
      if (!this.mEditMode)
        break label995;
      int i9 = paramView.getPaddingLeft();
      int i10 = paramView.getPaddingTop();
      int i11 = paramView.getPaddingRight();
      int i12 = paramView.getPaddingBottom();
      Drawable localDrawable = this.mDragHandleBG;
      paramView.setBackgroundDrawable(localDrawable);
      paramView.setPadding(i9, i10, i11, i12);
      return;
      localViewHolder.contextMenu.setVisibility(0);
      break;
      label781: bool2 = false;
      break label183;
      label787: if (this.mCurrentAudioId == null)
        break label183;
      int i13 = this.mAudioIdIdx;
      long l2 = paramCursor.getLong(i13);
      long l3 = this.mCurrentAudioId.getId();
      if (l2 == l3);
      for (bool2 = true; ; bool2 = false)
        break;
      label839: n = 8;
      break label226;
      label846: localViewHolder.title.setTypeface(null, 0);
      break label248;
      label859: bool3 = false;
      break label277;
      label865: bool4 = false;
      break label304;
      label871: localViewHolder.year.setVisibility(4);
      break label504;
      label883: localViewHolder.artist.setVisibility(8);
      break label584;
      label896: localViewHolder.icon.setVisibility(0);
      localViewHolder.icon.setAvailable(bool3);
      if (!TextUtils.isEmpty(localViewHolder.document.getArtUrl()))
      {
        AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = localViewHolder.icon;
        String str5 = localViewHolder.document.getArtUrl();
        localAsyncAlbumArtImageView1.setExternalAlbumArt(str5);
        break label676;
      }
      AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = localViewHolder.icon;
      long l4 = localViewHolder.document.getAlbumId();
      localAsyncAlbumArtImageView2.setAlbumId(l4, null, null);
      break label676;
      label985: ViewUtils.setAlpha(paramView, 1.0F);
    }
    label995: paramView.setBackgroundDrawable(null);
  }

  boolean canShowPlaybackIndicator()
  {
    if ((this.mCanShowPlayIndicator == null) || (this.mCanShowPlayIndicator.booleanValue()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void changeCursor(Cursor paramCursor)
  {
    super.changeCursor(paramCursor);
  }

  void disablePlaybackIndicator()
  {
    if ((this.mCanShowPlayIndicator != null) && (!this.mCanShowPlayIndicator.booleanValue()))
      return;
    Boolean localBoolean = Boolean.valueOf(false);
    this.mCanShowPlayIndicator = localBoolean;
    notifyDataSetChanged();
  }

  void enablePlaybackIndicator()
  {
    if ((this.mCanShowPlayIndicator != null) && (this.mCanShowPlayIndicator.booleanValue()))
      return;
    Boolean localBoolean = Boolean.valueOf(true);
    this.mCanShowPlayIndicator = localBoolean;
    notifyDataSetChanged();
  }

  public int getCount()
  {
    int i = super.getCount();
    if (this.mHasRemovedItem)
      i += -1;
    return i;
  }

  public Document getDocument(int paramInt)
  {
    MusicUtils.assertUiThread();
    Document localDocument = new Document();
    if (paramInt >= 0)
    {
      int i = getCount();
      if (paramInt <= i);
    }
    else
    {
      Object[] arrayOfObject = new Object[2];
      Integer localInteger1 = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(getCount());
      arrayOfObject[1] = localInteger2;
      String str1 = String.format("Position out of range. pos=%d, count=%d", arrayOfObject);
      int j = Log.w("TrackListAdapter", str1);
    }
    while (true)
    {
      return localDocument;
      Cursor localCursor = getCursor();
      if ((localCursor != null) && (!localCursor.isClosed()) && (MusicUtils.hasCount(localCursor)))
      {
        int k = localCursor.getPosition();
        if (localCursor.moveToPosition(paramInt))
          populateDocumentFromCursor(localDocument, localCursor);
        if (!localCursor.moveToPosition(k))
        {
          String str2 = "Failed to restore the cursor to the original position: " + k;
          int m = Log.w("TrackListAdapter", str2);
        }
      }
    }
  }

  public View getDropDownView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = this.mTempRowMapping.get(paramInt, paramInt);
    return super.getDropDownView(i, paramView, paramViewGroup);
  }

  public Object getItem(int paramInt)
  {
    int i = this.mTempRowMapping.get(paramInt, paramInt);
    return super.getItem(i);
  }

  public long getItemId(int paramInt)
  {
    int i = this.mTempRowMapping.get(paramInt, paramInt);
    return super.getItemId(i);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = this.mTempRowMapping.get(paramInt, paramInt);
    return super.getView(i, paramView, paramViewGroup);
  }

  public void moveItemTemp(int paramInt1, int paramInt2)
  {
    if (paramInt1 != paramInt2)
      return;
    int i = this.mTempRowMapping.get(paramInt1, paramInt1);
    if (paramInt1 > paramInt2)
    {
      j = paramInt1;
      while (j > paramInt2)
      {
        SparseIntArray localSparseIntArray1 = this.mTempRowMapping;
        SparseIntArray localSparseIntArray2 = this.mTempRowMapping;
        int k = j + -1;
        int m = j + -1;
        int n = localSparseIntArray2.get(k, m);
        localSparseIntArray1.put(j, n);
        j += -1;
      }
    }
    int j = paramInt1;
    while (j < paramInt2)
    {
      SparseIntArray localSparseIntArray3 = this.mTempRowMapping;
      SparseIntArray localSparseIntArray4 = this.mTempRowMapping;
      int i1 = j + 1;
      int i2 = j + 1;
      int i3 = localSparseIntArray4.get(i1, i2);
      localSparseIntArray3.put(j, i3);
      j += 1;
    }
    this.mTempRowMapping.put(paramInt2, i);
  }

  public View newView(Context paramContext, Cursor paramCursor, ViewGroup paramViewGroup)
  {
    View localView1 = super.newView(paramContext, paramCursor, paramViewGroup);
    ViewHolder localViewHolder = new ViewHolder();
    Document localDocument = new Document();
    localViewHolder.document = localDocument;
    StatefulShadowTextView localStatefulShadowTextView1 = (StatefulShadowTextView)localView1.findViewById(2131296326);
    localViewHolder.title = localStatefulShadowTextView1;
    if (localViewHolder.title == null)
    {
      StatefulShadowTextView localStatefulShadowTextView2 = (StatefulShadowTextView)localView1.findViewById(2131296347);
      localViewHolder.title = localStatefulShadowTextView2;
    }
    View localView2 = localView1.findViewById(2131296412);
    localViewHolder.comboColumn = localView2;
    View localView3 = localView1.findViewById(2131296546);
    localViewHolder.contextMenu = localView3;
    View localView4 = localViewHolder.contextMenu;
    View.OnClickListener localOnClickListener = this.mContextClickListener;
    localView4.setOnClickListener(localOnClickListener);
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)localView1.findViewById(2131296325);
    localViewHolder.icon = localAsyncAlbumArtImageView;
    PlayingIndicator localPlayingIndicator = (PlayingIndicator)localView1.findViewById(2131296415);
    localViewHolder.play_indicator = localPlayingIndicator;
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(100);
    localViewHolder.titleBuffer = localCharArrayBuffer;
    StatefulShadowTextView localStatefulShadowTextView3 = (StatefulShadowTextView)localView1.findViewById(2131296545);
    localViewHolder.year = localStatefulShadowTextView3;
    StatefulShadowTextView localStatefulShadowTextView4 = (StatefulShadowTextView)localView1.findViewById(2131296399);
    localViewHolder.duration = localStatefulShadowTextView4;
    StatefulShadowTextView localStatefulShadowTextView5 = (StatefulShadowTextView)localView1.findViewById(2131296401);
    localViewHolder.album = localStatefulShadowTextView5;
    StatefulShadowTextView localStatefulShadowTextView6 = (StatefulShadowTextView)localView1.findViewById(2131296400);
    localViewHolder.artist = localStatefulShadowTextView6;
    if (localViewHolder.artist == null)
    {
      StatefulShadowTextView localStatefulShadowTextView7 = (StatefulShadowTextView)localView1.findViewById(2131296348);
      localViewHolder.artist = localStatefulShadowTextView7;
    }
    localView1.setTag(localViewHolder);
    localViewHolder.contextMenu.setTag(localViewHolder);
    return localView1;
  }

  protected void onContentChanged()
  {
    resetTempState();
    updatePlaybackState();
    Cursor localCursor = getCursor();
    getColumnIndices(localCursor);
    super.onContentChanged();
    if (this.mCallback == null)
      return;
    this.mCallback.onContentChanged();
  }

  public void removeItemTemp(int paramInt)
  {
    this.mHasRemovedItem = true;
    int i = getCount();
    int j = paramInt;
    while (true)
    {
      if (j >= i)
        return;
      SparseIntArray localSparseIntArray1 = this.mTempRowMapping;
      SparseIntArray localSparseIntArray2 = this.mTempRowMapping;
      int k = j + 1;
      int m = j + 1;
      int n = localSparseIntArray2.get(k, m);
      localSparseIntArray1.put(j, n);
      j += 1;
    }
  }

  public void setEditMode(boolean paramBoolean)
  {
    this.mEditMode = paramBoolean;
    Drawable localDrawable = getContext().getResources().getDrawable(2130837705);
    this.mDragHandleBG = localDrawable;
  }

  public void setOnContentChangedCallback(BaseTrackListView.OnContentChangedCallback paramOnContentChangedCallback)
  {
    this.mCallback = paramOnContentChangedCallback;
  }

  public void setSongList(SongList paramSongList)
  {
    this.mSongList = paramSongList;
    this.mIsShowingNowPlaying = null;
    notifyDataSetChanged();
  }

  public void showAlbumArt(boolean paramBoolean)
  {
    if (this.mCursor == null)
      return;
    if (this.mCursor.isClosed())
      return;
    boolean bool = this.mShowAlbumArt;
    if (paramBoolean != bool)
      return;
    this.mShowAlbumArt = paramBoolean;
    notifyDataSetChanged();
  }

  public void showTrackArtist(boolean paramBoolean)
  {
    if (this.mCursor == null)
      return;
    if (this.mCursor.isClosed())
      return;
    if (this.mShowTrackArtist != paramBoolean)
      return;
    this.mShowTrackArtist = paramBoolean;
    notifyDataSetChanged();
  }

  public Cursor swapCursor(Cursor paramCursor)
  {
    resetTempState();
    updatePlaybackState();
    getColumnIndices(paramCursor);
    return super.swapCursor(paramCursor);
  }

  void updatePlaybackState()
  {
    this.mIsInInfiniteMix = null;
    if (MusicUtils.sService == null)
      return;
    try
    {
      if (isShowingNowPlaying())
      {
        int i = MusicUtils.sService.getQueuePosition();
        this.mCurrentPlayPosition = i;
      }
      while (true)
      {
        if (this.mTempRowMapping.size() != 0)
          return;
        notifyDataSetChanged();
        return;
        ContentIdentifier localContentIdentifier = MusicUtils.sService.getAudioId();
        this.mCurrentAudioId = localContentIdentifier;
      }
    }
    catch (RemoteException localRemoteException)
    {
      String str = localRemoteException.getMessage();
      int j = Log.w("TrackListAdapter", str, localRemoteException);
    }
  }

  public class ViewHolder
  {
    StatefulShadowTextView album;
    StatefulShadowTextView artist;
    View comboColumn;
    View contextMenu;
    Document document;
    StatefulShadowTextView duration;
    boolean hasRemote;
    AsyncAlbumArtImageView icon;
    boolean isAvailable;
    PlayingIndicator play_indicator;
    StatefulShadowTextView title;
    CharArrayBuffer titleBuffer;
    StatefulShadowTextView year;

    public ViewHolder()
    {
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.TrackListAdapter
 * JD-Core Version:    0.6.2
 */