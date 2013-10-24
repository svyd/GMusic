package com.google.android.music.medialist;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.store.IStoreService;

public abstract class SongList extends MediaList
{
  public static final Parcelable.Creator<SongList> CREATOR = new Parcelable.Creator()
  {
    public SongList createFromParcel(Parcel paramAnonymousParcel)
    {
      return (SongList)MediaList.thaw(paramAnonymousParcel.readString());
    }

    public SongList[] newArray(int paramAnonymousInt)
    {
      return new SongList[paramAnonymousInt];
    }
  };
  protected int mSortOrder;

  public SongList(int paramInt, ContentIdentifier.Domain paramDomain, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(paramDomain, paramBoolean1, paramBoolean2);
    this.mSortOrder = paramInt;
  }

  public SongList(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    super(localDomain, paramBoolean1, paramBoolean2);
    this.mSortOrder = paramInt;
  }

  public int appendToPlaylist(Context paramContext, long paramLong)
  {
    throw new UnsupportedOperationException("appending to playlist not supported by base SongList");
  }

  public boolean containsRemoteItems(Context paramContext)
  {
    return false;
  }

  public final int describeContents()
  {
    return 0;
  }

  public int getDownloadedSongCount(Context paramContext)
  {
    return -1;
  }

  public int getKeepOnSongCount(Context paramContext)
  {
    return -1;
  }

  public int getSortOrder()
  {
    return this.mSortOrder;
  }

  protected String getSortParam()
  {
    String str;
    switch (this.mSortOrder)
    {
    default:
      str = null;
    case 1:
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      return str;
      str = "name";
      continue;
      str = "album";
      continue;
      str = "artist";
      continue;
      str = "date";
    }
  }

  public int getSuggestedPositionSearchRadius()
  {
    return 250;
  }

  public boolean hasArtistArt()
  {
    return false;
  }

  public boolean hasStablePrimaryIds()
  {
    return true;
  }

  public boolean hasUniqueAudioId()
  {
    return true;
  }

  public boolean isAllLocal(Context paramContext)
  {
    return false;
  }

  public boolean isSelectedForOfflineCaching(Context paramContext, IStoreService paramIStoreService)
  {
    return false;
  }

  public void refreshMetaData(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    String str1 = getClass().getName();
    String str2 = str1 + " has no metadata";
    throw new UnsupportedOperationException(str2);
  }

  public boolean supportsAppendToPlaylist()
  {
    return false;
  }

  public boolean supportsOfflineCaching()
  {
    return false;
  }

  public final void writeToParcel(Parcel paramParcel, int paramInt)
  {
    String str = freeze();
    paramParcel.writeString(str);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SongList
 * JD-Core Version:    0.6.2
 */