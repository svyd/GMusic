package com.google.android.music.ui.cardlib.model;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.ArtistSongList;
import com.google.android.music.medialist.AutoPlaylist;
import com.google.android.music.medialist.ExternalSongList;
import com.google.android.music.medialist.GenreSongList;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.NautilusArtistSongList;
import com.google.android.music.medialist.NautilusSingleSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SharedWithMeSongList;
import com.google.android.music.medialist.SingleSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.ProjectionUtils;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.ui.cardlib.PlayDocument;
import com.google.android.music.utils.MusicUtils;

public class Document
  implements Parcelable, PlayDocument
{
  public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator()
  {
    public Document createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = 1;
      Document localDocument = new Document();
      String str1 = paramAnonymousParcel.readString();
      String str2 = Document.access$002(localDocument, str1);
      String str3 = paramAnonymousParcel.readString();
      String str4 = Document.access$102(localDocument, str3);
      String str5 = paramAnonymousParcel.readString();
      String str6 = Document.access$202(localDocument, str5);
      String str7 = paramAnonymousParcel.readString();
      String str8 = Document.access$302(localDocument, str7);
      String str9 = paramAnonymousParcel.readString();
      String str10 = Document.access$402(localDocument, str9);
      String str11 = paramAnonymousParcel.readString();
      String str12 = Document.access$502(localDocument, str11);
      long l1 = paramAnonymousParcel.readLong();
      long l2 = Document.access$602(localDocument, l1);
      String str13 = paramAnonymousParcel.readString();
      String str14 = Document.access$702(localDocument, str13);
      long l3 = paramAnonymousParcel.readLong();
      long l4 = Document.access$802(localDocument, l3);
      String str15 = paramAnonymousParcel.readString();
      String str16 = Document.access$902(localDocument, str15);
      long l5 = paramAnonymousParcel.readLong();
      long l6 = Document.access$1002(localDocument, l5);
      String str17 = paramAnonymousParcel.readString();
      String str18 = Document.access$1102(localDocument, str17);
      Document.Type localType1 = Document.Type.fromOrdinal(paramAnonymousParcel.readInt());
      Document.Type localType2 = Document.access$1202(localDocument, localType1);
      String str19 = paramAnonymousParcel.readString();
      String str20 = Document.access$1302(localDocument, str19);
      int k = paramAnonymousParcel.readInt();
      int m = Document.access$1402(localDocument, k);
      long l7 = paramAnonymousParcel.readLong();
      long l8 = Document.access$1502(localDocument, l7);
      String str21 = paramAnonymousParcel.readString();
      String str22 = Document.access$1602(localDocument, str21);
      String str23 = paramAnonymousParcel.readString();
      String str24 = Document.access$1702(localDocument, str23);
      String str25 = paramAnonymousParcel.readString();
      String str26 = Document.access$1802(localDocument, str25);
      boolean bool1;
      boolean bool3;
      label351: boolean bool5;
      label370: boolean bool7;
      if (paramAnonymousParcel.readInt() != i)
      {
        bool1 = true;
        boolean bool2 = Document.access$1902(localDocument, bool1);
        String str27 = paramAnonymousParcel.readString();
        String str28 = Document.access$2002(localDocument, str27);
        String str29 = paramAnonymousParcel.readString();
        String str30 = Document.access$2102(localDocument, str29);
        int n = paramAnonymousParcel.readInt();
        int i1 = Document.access$2202(localDocument, n);
        if (paramAnonymousParcel.readInt() != 1)
          break label476;
        bool3 = true;
        boolean bool4 = Document.access$2302(localDocument, bool3);
        if (paramAnonymousParcel.readInt() != 1)
          break label482;
        bool5 = true;
        boolean bool6 = Document.access$2402(localDocument, bool5);
        String str31 = paramAnonymousParcel.readString();
        String str32 = Document.access$2502(localDocument, str31);
        String str33 = paramAnonymousParcel.readString();
        String str34 = Document.access$2602(localDocument, str33);
        String str35 = paramAnonymousParcel.readString();
        String str36 = Document.access$2702(localDocument, str35);
        if (paramAnonymousParcel.readInt() != 1)
          break label488;
        bool7 = true;
        label431: boolean bool8 = Document.access$2802(localDocument, bool7);
        if (paramAnonymousParcel.readInt() != 1)
          break label494;
      }
      while (true)
      {
        boolean bool9 = Document.access$2902(localDocument, i);
        long l9 = paramAnonymousParcel.readLong();
        long l10 = Document.access$3002(localDocument, l9);
        return localDocument;
        bool1 = false;
        break;
        label476: bool3 = false;
        break label351;
        label482: bool5 = false;
        break label370;
        label488: bool7 = false;
        break label431;
        label494: int j = 0;
      }
    }

    public Document[] newArray(int paramAnonymousInt)
    {
      return new Document[paramAnonymousInt];
    }
  };
  private static String UNKNOWN_ALBUM = null;
  private static String UNKNOWN_ARTIST = null;
  private long mAlbumId;
  private String mAlbumMetajamId;
  private String mAlbumName;
  private String mArtUrl;
  private long mArtistId;
  private String mArtistMetajamId;
  private String mArtistName;
  private boolean mCanAddtoLibrary;
  private boolean mCanRemoveFromLibrary;
  private String mDescription;
  private long mDuration;
  private String mGenreId;
  private boolean mHasLocal;
  private long mId;
  private boolean mIsEmulatedRadio;
  private boolean mIsNautilus;
  private String mParentGenreId;
  private long mPlaylistMemberId;
  private String mPlaylistName;
  private String mPlaylistOwnerName;
  private String mPlaylistOwnerProfilePhotoUrl;
  private String mPlaylistShareToken;
  private int mPlaylistType;
  private String mReason1;
  private String mReason2;
  private String mSongStoreId;
  private String mSubTitle;
  private int mSubgenreCount;
  private String mTitle;
  private String mTrackMetajamId;
  private Type mType;

  public Document()
  {
    if ((UNKNOWN_ALBUM == null) || (UNKNOWN_ARTIST == null))
      throw new IllegalStateException("Document.init(Context) should be called before getting here");
    reset();
  }

  public static Document fromSongList(Context paramContext, SongList paramSongList)
  {
    Document localDocument = new Document();
    if ((paramSongList instanceof ExternalSongList))
    {
      localDocument.setIsNautilus(true);
      if ((paramSongList instanceof NautilusAlbumSongList))
      {
        Type localType1 = Type.ALBUM;
        localDocument.setType(localType1);
        String str1 = ((NautilusAlbumSongList)paramSongList).getNautilusId();
        localDocument.setAlbumMetajamId(str1);
      }
    }
    while (true)
    {
      return localDocument;
      if ((paramSongList instanceof NautilusArtistSongList))
      {
        Type localType2 = Type.ALL_SONGS_ARTIST;
        localDocument.setType(localType2);
        String str2 = ((NautilusArtistSongList)paramSongList).getNautilusId();
        localDocument.setArtistMetajamId(str2);
      }
      else if ((paramSongList instanceof SharedWithMeSongList))
      {
        Type localType3 = Type.PLAYLIST;
        localDocument.setType(localType3);
        localDocument.setPlaylistType(70);
        String str3 = ((SharedWithMeSongList)paramSongList).getShareToken();
        localDocument.setPlaylistShareToken(str3);
      }
      else
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Unsupported ExternalSongList type: ");
        String str4 = paramSongList.getClass().getName();
        String str5 = str4;
        Log.w("Document", str5);
        continue;
        if ((paramSongList instanceof AlbumSongList))
        {
          Type localType4 = Type.ALBUM;
          localDocument.setType(localType4);
          AlbumSongList localAlbumSongList = (AlbumSongList)paramSongList;
          long l1 = localAlbumSongList.getAlbumId(paramContext);
          localDocument.setAlbumId(l1);
          String str6 = localAlbumSongList.getAlbumMetajamId(paramContext);
          localDocument.setAlbumMetajamId(str6);
        }
        else if ((paramSongList instanceof ArtistSongList))
        {
          Type localType5 = Type.ALL_SONGS_ARTIST;
          localDocument.setType(localType5);
          ArtistSongList localArtistSongList = (ArtistSongList)paramSongList;
          long l2 = localArtistSongList.getArtistId();
          localDocument.setArtistId(l2);
          String str7 = localArtistSongList.getArtistMetajamId(paramContext);
          localDocument.setArtistMetajamId(str7);
        }
        else if ((paramSongList instanceof PlaylistSongList))
        {
          Type localType6 = Type.PLAYLIST;
          localDocument.setType(localType6);
          PlaylistSongList localPlaylistSongList = (PlaylistSongList)paramSongList;
          long l3 = localPlaylistSongList.getId();
          localDocument.setId(l3);
          String str8 = localPlaylistSongList.getPlaylistName();
          localDocument.setTitle(str8);
          String str9 = localPlaylistSongList.getPlaylistName();
          localDocument.setPlaylistName(str9);
          String str10 = localPlaylistSongList.getShareToken();
          localDocument.setPlaylistShareToken(str10);
          int i = localPlaylistSongList.getPlaylistType();
          localDocument.setPlaylistType(i);
        }
        else if ((paramSongList instanceof AutoPlaylist))
        {
          Type localType7 = Type.PLAYLIST;
          localDocument.setType(localType7);
          long l4 = ((AutoPlaylist)paramSongList).getId();
          localDocument.setId(l4);
          localDocument.setPlaylistType(100);
        }
        else if ((paramSongList instanceof GenreSongList))
        {
          Type localType8 = Type.ALL_SONGS_GENRE;
          localDocument.setType(localType8);
          long l5 = ((GenreSongList)paramSongList).getGenreId();
          localDocument.setId(l5);
        }
        else
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Unsupported ExternalSongList type: ");
          String str11 = paramSongList.getClass().getName();
          String str12 = str11;
          Log.w("Document", str12);
        }
      }
    }
  }

  public static Document getImFeelingLuckyDocument(Context paramContext, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4)
  {
    Document localDocument = new Document();
    Resources localResources = paramContext.getResources();
    Type localType = Type.IM_FEELING_LUCKY;
    localDocument.setType(localType);
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    MusicPreferences localMusicPreferences = localUIStateManager.getPrefs();
    boolean bool1 = localUIStateManager.isDisplayingLocalContent();
    boolean bool2 = localUIStateManager.isStreamingEnabled();
    boolean bool3 = localMusicPreferences.isNautilusEnabled();
    boolean bool4 = localMusicPreferences.hasStreamingAccount();
    int i;
    int j;
    label95: Object localObject;
    int n;
    label203: label212: int i1;
    if ((bool4) && (bool2) && (!bool1))
    {
      i = 1;
      if (!bool3)
        break label291;
      j = 2131231423;
      String str1 = localResources.getString(j);
      if ((bool1) || (!bool4))
        str1 = "";
      Object[] arrayOfObject1 = new Object[2];
      int k = 2131231422;
      String str2 = localResources.getString(k);
      arrayOfObject1[0] = str2;
      arrayOfObject1[1] = str1;
      int m = 2131231421;
      Object[] arrayOfObject2 = arrayOfObject1;
      String str3 = localResources.getString(m, arrayOfObject2);
      localDocument.setTitle(str3);
      localObject = null;
      str4 = null;
      if (paramBoolean1)
      {
        if (i == 0)
          break label307;
        if (!bool3)
          break label299;
        n = 2131231420;
        localObject = localResources.getString(n);
      }
      if (paramBoolean2)
      {
        if (i == 0)
          break label315;
        i1 = 2131231425;
      }
    }
    label291: label299: label307: label315: int i2;
    for (String str4 = localResources.getString(i1); ; str4 = localResources.getString(i2))
    {
      if (paramBoolean3)
        localObject = str4;
      if (localObject != null)
        localDocument.setSubTitle((String)localObject);
      if (str4 != null)
        localDocument.setReason1(str4);
      if (paramBoolean4)
      {
        boolean bool5 = true;
        localDocument.mHasLocal = bool5;
      }
      return localDocument;
      i = 0;
      break;
      j = 2131231424;
      break label95;
      n = 2131231308;
      break label203;
      localObject = "";
      break label212;
      i2 = 2131231426;
    }
  }

  /** @deprecated */
  public static void init(Context paramContext)
  {
    try
    {
      if ((UNKNOWN_ALBUM == null) || (UNKNOWN_ARTIST == null))
      {
        UNKNOWN_ALBUM = paramContext.getString(2131230891);
        UNKNOWN_ARTIST = paramContext.getString(2131230890);
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private SongList makeSongListForPlayList(Context paramContext)
  {
    Object localObject;
    switch (this.mPlaylistType)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unexpected playlist type: ");
      int i = this.mPlaylistType;
      String str1 = i;
      Log.wtf("Document", str1);
      localObject = null;
    case 100:
    case 0:
    case 1:
    case 50:
    case 60:
    case 71:
    case 70:
    }
    while (true)
    {
      return localObject;
      MusicPreferences localMusicPreferences = UIStateManager.getInstance(paramContext).getPrefs();
      localObject = AutoPlaylist.getAutoPlaylist(getId(), true, localMusicPreferences);
      continue;
      long l1 = getId();
      String str2 = this.mPlaylistName;
      int j = this.mPlaylistType;
      String str3 = this.mDescription;
      String str4 = this.mPlaylistOwnerName;
      String str5 = this.mPlaylistShareToken;
      String str6 = this.mArtUrl;
      String str7 = this.mPlaylistOwnerProfilePhotoUrl;
      localObject = new PlaylistSongList(l1, str2, j, str3, str4, str5, str6, str7, true);
      continue;
      long l2 = this.mId;
      String str8 = this.mPlaylistShareToken;
      String str9 = this.mPlaylistName;
      String str10 = this.mDescription;
      String str11 = this.mPlaylistOwnerName;
      String str12 = this.mArtUrl;
      String str13 = this.mPlaylistOwnerProfilePhotoUrl;
      localObject = new SharedWithMeSongList(l2, str8, str9, str10, str11, str12, str13);
    }
  }

  public boolean canAddToLibrary()
  {
    return this.mCanAddtoLibrary;
  }

  public boolean canRemoveFromLibrary()
  {
    return this.mCanRemoveFromLibrary;
  }

  public int describeContents()
  {
    return 0;
  }

  public long getAlbumId()
  {
    return this.mAlbumId;
  }

  public String getAlbumMetajamId()
  {
    StringBuilder localStringBuilder;
    String str1;
    if ((!TextUtils.isEmpty(this.mAlbumMetajamId)) && (this.mAlbumMetajamId.charAt(0) != 'B'))
    {
      localStringBuilder = new StringBuilder().append("B");
      str1 = this.mAlbumMetajamId;
    }
    for (String str2 = str1; ; str2 = this.mAlbumMetajamId)
      return str2;
  }

  public String getAlbumName()
  {
    if (MusicUtils.isUnknown(this.mAlbumName));
    for (String str = UNKNOWN_ALBUM; ; str = this.mAlbumName)
      return str;
  }

  public String getArtUrl()
  {
    return this.mArtUrl;
  }

  public long getArtistId()
  {
    return this.mArtistId;
  }

  public String getArtistMetajamId()
  {
    StringBuilder localStringBuilder;
    String str1;
    if ((!TextUtils.isEmpty(this.mArtistMetajamId)) && (this.mArtistMetajamId.charAt(0) != 'A'))
    {
      localStringBuilder = new StringBuilder().append("A");
      str1 = this.mArtistMetajamId;
    }
    for (String str2 = str1; ; str2 = this.mArtistMetajamId)
      return str2;
  }

  public String getArtistName()
  {
    if (MusicUtils.isUnknown(this.mArtistName));
    for (String str = UNKNOWN_ARTIST; ; str = this.mArtistName)
      return str;
  }

  public String getDescription()
  {
    return this.mDescription;
  }

  public long getDuration()
  {
    return this.mDuration;
  }

  public String getGenreId()
  {
    return this.mGenreId;
  }

  public long getId()
  {
    return this.mId;
  }

  public long getIdInParent()
  {
    return this.mPlaylistMemberId;
  }

  public boolean getIsAvailable()
  {
    if ((this.mHasLocal) || (UIStateManager.getInstance().isStreamingEnabled()));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean getIsEmulateRadio()
  {
    return this.mIsEmulatedRadio;
  }

  public String getParentGenreId()
  {
    return this.mParentGenreId;
  }

  public String getPlaylistName()
  {
    return this.mPlaylistName;
  }

  public String getPlaylistOwnerName()
  {
    return this.mPlaylistOwnerName;
  }

  public String getPlaylistOwnerProfilePhotoUrl()
  {
    return this.mPlaylistOwnerProfilePhotoUrl;
  }

  public String getPlaylistShareToken()
  {
    return this.mPlaylistShareToken;
  }

  public int getPlaylistType()
  {
    return this.mPlaylistType;
  }

  public String getReason1()
  {
    return this.mReason1;
  }

  public String getReason2()
  {
    return this.mReason2;
  }

  public SongList getSongList(Context paramContext)
  {
    Type localType1 = getType();
    Type localType2 = Type.ALBUM;
    Object localObject1;
    if (localType1 == localType2)
      if (isNautilus())
      {
        String str1 = this.mAlbumMetajamId;
        localObject1 = new NautilusAlbumSongList(str1);
      }
    while (true)
    {
      return localObject1;
      long l1 = this.mAlbumId;
      long l2 = this.mArtistId;
      String str2 = this.mAlbumName;
      String str3 = this.mArtistName;
      localObject1 = new AlbumSongList(l1, l2, str2, str3, null, false);
      continue;
      Type localType3 = getType();
      Type localType4 = Type.ARTIST;
      if (localType3 == localType4)
      {
        String str4;
        String str5;
        if (isNautilus())
        {
          str4 = this.mArtistMetajamId;
          str5 = this.mArtistName;
        }
        long l3;
        String str6;
        int i;
        for (Object localObject2 = new NautilusArtistSongList(str4, str5); ; localObject2 = new ArtistSongList(l3, str6, i, false))
        {
          localObject1 = localObject2;
          break;
          l3 = this.mArtistId;
          str6 = this.mArtistName;
          i = -1;
        }
      }
      Type localType5 = getType();
      Type localType6 = Type.PLAYLIST;
      if (localType5 == localType6)
      {
        localObject1 = makeSongListForPlayList(paramContext);
      }
      else
      {
        Type localType7 = getType();
        Type localType8 = Type.TRACK;
        if (localType7 == localType8)
        {
          if (isNautilus())
          {
            String str7 = this.mTrackMetajamId;
            String str8 = this.mTitle;
            localObject1 = new NautilusSingleSongList(str7, str8);
          }
          else
          {
            long l4 = this.mId;
            String str9 = this.mTitle;
            localObject1 = new SingleSongList(l4, str9);
          }
        }
        else
        {
          Type localType9 = getType();
          Type localType10 = Type.GENRE;
          if (localType9 == localType10)
          {
            long l5 = this.mId;
            String str10 = this.mTitle;
            localObject1 = new GenreSongList(l5, str10, -1);
          }
          else
          {
            Type localType11 = getType();
            Type localType12 = Type.RADIO;
            if (localType11 == localType12)
            {
              localObject1 = null;
            }
            else
            {
              Type localType13 = getType();
              Type localType14 = Type.ALL_SONGS_ARTIST;
              if (localType13 == localType14)
              {
                long l6 = this.mArtistId;
                String str11 = this.mArtistName;
                int j = -1;
                localObject1 = new ArtistSongList(l6, str11, j, false);
              }
              else
              {
                Type localType15 = getType();
                Type localType16 = Type.ALL_SONGS_GENRE;
                if (localType15 == localType16)
                {
                  long l7 = this.mId;
                  String str12 = this.mTitle;
                  localObject1 = new GenreSongList(l7, str12, -1);
                }
                else
                {
                  StringBuilder localStringBuilder = new StringBuilder().append("getSongList, unexpected document type: ");
                  Type localType17 = getType();
                  String str13 = localType17;
                  Log.e("Document", str13);
                  localObject1 = null;
                }
              }
            }
          }
        }
      }
    }
  }

  public String getSongStoreId()
  {
    return this.mSongStoreId;
  }

  public String getSubTitle()
  {
    if (MusicUtils.isUnknown(this.mSubTitle))
    {
      Type localType1 = this.mType;
      Type localType2 = Type.ALBUM;
      if (localType1 != localType2);
    }
    for (String str = UNKNOWN_ARTIST; ; str = this.mSubTitle)
      return str;
  }

  public int getSubgenreCount()
  {
    return this.mSubgenreCount;
  }

  public String getTitle()
  {
    String str;
    if (MusicUtils.isUnknown(this.mTitle))
    {
      Type localType1 = this.mType;
      Type localType2 = Type.ALBUM;
      if (localType1 == localType2)
        str = UNKNOWN_ALBUM;
    }
    while (true)
    {
      return str;
      Type localType3 = this.mType;
      Type localType4 = Type.ARTIST;
      if (localType3 == localType4)
        str = UNKNOWN_ARTIST;
      else
        str = this.mTitle;
    }
  }

  public String getTrackMetajamId()
  {
    return this.mTrackMetajamId;
  }

  public Type getType()
  {
    return this.mType;
  }

  public boolean isGenreRadio()
  {
    if ((!TextUtils.isEmpty(this.mGenreId)) && (this.mIsEmulatedRadio));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isNautilus()
  {
    if ((this.mIsNautilus) || (ProjectionUtils.isFauxNautilusId(this.mId)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isTopLevelGenre()
  {
    Type localType1 = this.mType;
    Type localType2 = Type.NAUTILUS_GENRE;
    if ((localType1 == localType2) && (TextUtils.isEmpty(this.mParentGenreId)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void reset()
  {
    this.mArtUrl = null;
    this.mTitle = null;
    this.mSubTitle = null;
    this.mDescription = null;
    this.mReason1 = null;
    this.mReason2 = null;
    this.mDuration = 0L;
    this.mId = 65535L;
    this.mIsNautilus = false;
    this.mTrackMetajamId = null;
    this.mAlbumMetajamId = null;
    this.mArtistMetajamId = null;
    this.mPlaylistMemberId = 65535L;
    this.mSongStoreId = null;
    this.mAlbumId = 65535L;
    this.mAlbumName = null;
    this.mArtistId = 65535L;
    this.mArtistName = null;
    this.mType = null;
    this.mPlaylistName = null;
    this.mPlaylistType = -1;
    this.mPlaylistShareToken = null;
    this.mPlaylistOwnerName = null;
    this.mPlaylistOwnerProfilePhotoUrl = null;
    this.mIsEmulatedRadio = false;
    this.mGenreId = null;
    this.mParentGenreId = null;
    this.mSubgenreCount = 0;
    this.mHasLocal = false;
    this.mCanAddtoLibrary = false;
    this.mCanRemoveFromLibrary = false;
  }

  public void setAlbumId(long paramLong)
  {
    this.mAlbumId = paramLong;
  }

  public void setAlbumMetajamId(String paramString)
  {
    this.mAlbumMetajamId = paramString;
  }

  public void setAlbumName(String paramString)
  {
    this.mAlbumName = paramString;
  }

  public void setArtUrl(String paramString)
  {
    this.mArtUrl = paramString;
  }

  public void setArtistId(long paramLong)
  {
    this.mArtistId = paramLong;
  }

  public void setArtistMetajamId(String paramString)
  {
    this.mArtistMetajamId = paramString;
  }

  public void setArtistName(String paramString)
  {
    this.mArtistName = paramString;
  }

  public void setCanAddToLibrary(boolean paramBoolean)
  {
    this.mCanAddtoLibrary = paramBoolean;
  }

  public void setCanRemoveFromLibrary(boolean paramBoolean)
  {
    this.mCanRemoveFromLibrary = paramBoolean;
  }

  public void setDescription(String paramString)
  {
    this.mDescription = paramString;
  }

  public void setDuration(long paramLong)
  {
    this.mDuration = paramLong;
  }

  public void setGenreId(String paramString)
  {
    this.mGenreId = paramString;
  }

  public void setHasLocal(boolean paramBoolean)
  {
    this.mHasLocal = paramBoolean;
  }

  public void setId(long paramLong)
  {
    this.mId = paramLong;
  }

  public void setIdInParent(long paramLong)
  {
    this.mPlaylistMemberId = paramLong;
  }

  public void setIsEmulatedRadio(boolean paramBoolean)
  {
    this.mIsEmulatedRadio = paramBoolean;
  }

  public void setIsNautilus(boolean paramBoolean)
  {
    this.mIsNautilus = paramBoolean;
  }

  public void setParentGenreId(String paramString)
  {
    this.mParentGenreId = paramString;
  }

  public void setPlaylistName(String paramString)
  {
    this.mPlaylistName = paramString;
  }

  public void setPlaylistOwnerName(String paramString)
  {
    this.mPlaylistOwnerName = paramString;
  }

  public void setPlaylistOwnerProfilePhotoUrl(String paramString)
  {
    this.mPlaylistOwnerProfilePhotoUrl = paramString;
  }

  public void setPlaylistShareToken(String paramString)
  {
    this.mPlaylistShareToken = paramString;
  }

  public void setPlaylistType(int paramInt)
  {
    this.mPlaylistType = paramInt;
  }

  public void setReason1(String paramString)
  {
    this.mReason1 = paramString;
  }

  public void setSongStoreId(String paramString)
  {
    this.mSongStoreId = paramString;
  }

  public void setSubTitle(String paramString)
  {
    this.mSubTitle = paramString;
  }

  public void setSubgenreCount(int paramInt)
  {
    this.mSubgenreCount = paramInt;
  }

  public void setTitle(String paramString)
  {
    this.mTitle = paramString;
  }

  public void setTrackMetajamId(String paramString)
  {
    this.mTrackMetajamId = paramString;
  }

  public void setType(Type paramType)
  {
    this.mType = paramType;
  }

  public boolean shouldShowContextMenu()
  {
    if (!isGenreRadio())
    {
      Type localType1 = this.mType;
      Type localType2 = Type.NAUTILUS_GENRE;
      if (localType1 != localType2)
      {
        Type localType3 = this.mType;
        Type localType4 = Type.IM_FEELING_LUCKY;
        if (localType3 != localType4)
        {
          Type localType5 = this.mType;
          Type localType6 = Type.ALL_SONGS_ARTIST;
          if (localType5 != localType6)
          {
            Type localType7 = this.mType;
            Type localType8 = Type.ALL_SONGS_GENRE;
            if (localType7 == localType8);
          }
        }
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean shouldShowContextMenuForArtist()
  {
    return UIStateManager.getInstance().getPrefs().hasStreamingAccount();
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("mArtUrl=");
    String str1 = this.mArtUrl;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(str1);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(", mTitle=");
    String str2 = this.mTitle;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str2);
    StringBuilder localStringBuilder7 = localStringBuilder1.append(", mSubTitle=");
    String str3 = this.mSubTitle;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str3);
    StringBuilder localStringBuilder9 = localStringBuilder1.append(", mDescription=");
    String str4 = this.mDescription;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(str4);
    StringBuilder localStringBuilder11 = localStringBuilder1.append(", mReason1=");
    String str5 = this.mReason1;
    StringBuilder localStringBuilder12 = localStringBuilder11.append(str5);
    StringBuilder localStringBuilder13 = localStringBuilder1.append(", mReason2=");
    String str6 = this.mReason2;
    StringBuilder localStringBuilder14 = localStringBuilder13.append(str6);
    StringBuilder localStringBuilder15 = localStringBuilder1.append(", mDuration=");
    long l1 = this.mDuration;
    StringBuilder localStringBuilder16 = localStringBuilder15.append(l1);
    StringBuilder localStringBuilder17 = localStringBuilder1.append(", mId=");
    long l2 = this.mId;
    StringBuilder localStringBuilder18 = localStringBuilder17.append(l2);
    StringBuilder localStringBuilder19 = localStringBuilder1.append(", mAlbumId=");
    long l3 = this.mAlbumId;
    StringBuilder localStringBuilder20 = localStringBuilder19.append(l3);
    StringBuilder localStringBuilder21 = localStringBuilder1.append(", mAlbumName=");
    String str7 = this.mAlbumName;
    StringBuilder localStringBuilder22 = localStringBuilder21.append(str7);
    StringBuilder localStringBuilder23 = localStringBuilder1.append(", mArtistId=");
    long l4 = this.mArtistId;
    StringBuilder localStringBuilder24 = localStringBuilder23.append(l4);
    StringBuilder localStringBuilder25 = localStringBuilder1.append(", mArtistName=");
    String str8 = this.mArtistName;
    StringBuilder localStringBuilder26 = localStringBuilder25.append(str8);
    StringBuilder localStringBuilder27 = localStringBuilder1.append(", mType=");
    Type localType = this.mType;
    StringBuilder localStringBuilder28 = localStringBuilder27.append(localType);
    StringBuilder localStringBuilder29 = localStringBuilder1.append(", mIsNautilus=");
    boolean bool1 = isNautilus();
    StringBuilder localStringBuilder30 = localStringBuilder29.append(bool1);
    StringBuilder localStringBuilder31 = localStringBuilder1.append(", mTrackMetajamId=");
    String str9 = this.mTrackMetajamId;
    StringBuilder localStringBuilder32 = localStringBuilder31.append(str9);
    StringBuilder localStringBuilder33 = localStringBuilder1.append(", mAlbumMetajamId=");
    String str10 = this.mAlbumMetajamId;
    StringBuilder localStringBuilder34 = localStringBuilder33.append(str10);
    StringBuilder localStringBuilder35 = localStringBuilder1.append(", mArtistMetajamId=");
    String str11 = this.mArtistMetajamId;
    StringBuilder localStringBuilder36 = localStringBuilder35.append(str11);
    StringBuilder localStringBuilder37 = localStringBuilder1.append(", mPlaylistName=");
    String str12 = this.mPlaylistName;
    StringBuilder localStringBuilder38 = localStringBuilder37.append(str12);
    StringBuilder localStringBuilder39 = localStringBuilder1.append(", mPlaylistType=");
    int i = this.mPlaylistType;
    StringBuilder localStringBuilder40 = localStringBuilder39.append(i);
    StringBuilder localStringBuilder41 = localStringBuilder1.append(", mPlaylistShareToken=");
    String str13 = this.mPlaylistShareToken;
    StringBuilder localStringBuilder42 = localStringBuilder41.append(str13);
    StringBuilder localStringBuilder43 = localStringBuilder1.append(", mPlaylistOwnerName=");
    String str14 = this.mPlaylistOwnerName;
    StringBuilder localStringBuilder44 = localStringBuilder43.append(str14);
    StringBuilder localStringBuilder45 = localStringBuilder1.append(", mPlaylistOwnerProfilePhotoUrl=");
    String str15 = this.mPlaylistOwnerProfilePhotoUrl;
    StringBuilder localStringBuilder46 = localStringBuilder45.append(str15);
    StringBuilder localStringBuilder47 = localStringBuilder1.append(", mPlaylistMemberId=");
    long l5 = this.mPlaylistMemberId;
    StringBuilder localStringBuilder48 = localStringBuilder47.append(l5);
    StringBuilder localStringBuilder49 = localStringBuilder1.append(", mSongStoreId=");
    String str16 = this.mSongStoreId;
    StringBuilder localStringBuilder50 = localStringBuilder49.append(str16);
    StringBuilder localStringBuilder51 = localStringBuilder1.append(", mIsEmulatedRadio=");
    boolean bool2 = this.mIsEmulatedRadio;
    StringBuilder localStringBuilder52 = localStringBuilder51.append(bool2);
    StringBuilder localStringBuilder53 = localStringBuilder1.append(", mGenreId=");
    String str17 = this.mGenreId;
    StringBuilder localStringBuilder54 = localStringBuilder53.append(str17);
    StringBuilder localStringBuilder55 = localStringBuilder1.append(", mParentGenreId=");
    String str18 = this.mParentGenreId;
    StringBuilder localStringBuilder56 = localStringBuilder55.append(str18);
    StringBuilder localStringBuilder57 = localStringBuilder1.append(", mSubgenreCount=");
    int j = this.mSubgenreCount;
    StringBuilder localStringBuilder58 = localStringBuilder57.append(j);
    StringBuilder localStringBuilder59 = localStringBuilder1.append(", mHasLocal=");
    boolean bool3 = this.mHasLocal;
    StringBuilder localStringBuilder60 = localStringBuilder59.append(bool3);
    StringBuilder localStringBuilder61 = localStringBuilder1.append(", mCanAddtoLibrary=");
    boolean bool4 = this.mCanAddtoLibrary;
    StringBuilder localStringBuilder62 = localStringBuilder61.append(bool4);
    StringBuilder localStringBuilder63 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    String str1 = this.mArtUrl;
    paramParcel.writeString(str1);
    String str2 = this.mTitle;
    paramParcel.writeString(str2);
    String str3 = this.mSubTitle;
    paramParcel.writeString(str3);
    String str4 = this.mDescription;
    paramParcel.writeString(str4);
    String str5 = this.mReason1;
    paramParcel.writeString(str5);
    String str6 = this.mReason2;
    paramParcel.writeString(str6);
    long l1 = this.mId;
    paramParcel.writeLong(l1);
    String str7 = this.mSongStoreId;
    paramParcel.writeString(str7);
    long l2 = this.mAlbumId;
    paramParcel.writeLong(l2);
    String str8 = this.mAlbumName;
    paramParcel.writeString(str8);
    long l3 = this.mArtistId;
    paramParcel.writeLong(l3);
    String str9 = this.mArtistName;
    paramParcel.writeString(str9);
    int j = this.mType.ordinal();
    paramParcel.writeInt(j);
    String str10 = this.mPlaylistName;
    paramParcel.writeString(str10);
    int k = this.mPlaylistType;
    paramParcel.writeInt(k);
    long l4 = this.mPlaylistMemberId;
    paramParcel.writeLong(l4);
    String str11 = this.mPlaylistShareToken;
    paramParcel.writeString(str11);
    String str12 = this.mPlaylistOwnerName;
    paramParcel.writeString(str12);
    String str13 = this.mPlaylistOwnerProfilePhotoUrl;
    paramParcel.writeString(str13);
    int m;
    int i1;
    label295: int i2;
    label311: int i3;
    if (this.mIsEmulatedRadio)
    {
      m = 1;
      paramParcel.writeInt(m);
      String str14 = this.mGenreId;
      paramParcel.writeString(str14);
      String str15 = this.mParentGenreId;
      paramParcel.writeString(str15);
      int n = this.mSubgenreCount;
      paramParcel.writeInt(n);
      if (!this.mHasLocal)
        break label400;
      i1 = 1;
      paramParcel.writeInt(i1);
      if (!this.mIsNautilus)
        break label406;
      i2 = 1;
      paramParcel.writeInt(i2);
      String str16 = this.mTrackMetajamId;
      paramParcel.writeString(str16);
      String str17 = this.mAlbumMetajamId;
      paramParcel.writeString(str17);
      String str18 = this.mArtistMetajamId;
      paramParcel.writeString(str18);
      if (!this.mCanAddtoLibrary)
        break label412;
      i3 = 1;
      label363: paramParcel.writeInt(i3);
      if (!this.mCanRemoveFromLibrary)
        break label418;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      long l5 = this.mDuration;
      paramParcel.writeLong(l5);
      return;
      m = 0;
      break;
      label400: i1 = 0;
      break label295;
      label406: i2 = 0;
      break label311;
      label412: i3 = 0;
      break label363;
      label418: i = 0;
    }
  }

  public static enum Type
  {
    static
    {
      ALBUM = new Type("ALBUM", 2);
      PLAYLIST = new Type("PLAYLIST", 3);
      RADIO = new Type("RADIO", 4);
      TRACK = new Type("TRACK", 5);
      NAUTILUS_GENRE = new Type("NAUTILUS_GENRE", 6);
      IM_FEELING_LUCKY = new Type("IM_FEELING_LUCKY", 7);
      ALL_SONGS_ARTIST = new Type("ALL_SONGS_ARTIST", 8);
      ALL_SONGS_GENRE = new Type("ALL_SONGS_GENRE", 9);
      Type[] arrayOfType = new Type[10];
      Type localType1 = ARTIST;
      arrayOfType[0] = localType1;
      Type localType2 = GENRE;
      arrayOfType[1] = localType2;
      Type localType3 = ALBUM;
      arrayOfType[2] = localType3;
      Type localType4 = PLAYLIST;
      arrayOfType[3] = localType4;
      Type localType5 = RADIO;
      arrayOfType[4] = localType5;
      Type localType6 = TRACK;
      arrayOfType[5] = localType6;
      Type localType7 = NAUTILUS_GENRE;
      arrayOfType[6] = localType7;
      Type localType8 = IM_FEELING_LUCKY;
      arrayOfType[7] = localType8;
      Type localType9 = ALL_SONGS_ARTIST;
      arrayOfType[8] = localType9;
      Type localType10 = ALL_SONGS_GENRE;
      arrayOfType[9] = localType10;
    }

    public static Type fromOrdinal(int paramInt)
    {
      return values()[paramInt];
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.model.Document
 * JD-Core Version:    0.6.2
 */