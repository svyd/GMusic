package com.google.android.music.playback;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.SongList;

public class PlaybackState
  implements Parcelable
{
  public static final Parcelable.Creator<PlaybackState> CREATOR = new Parcelable.Creator()
  {
    public PlaybackState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new PlaybackState(paramAnonymousParcel);
    }

    public PlaybackState[] newArray(int paramAnonymousInt)
    {
      return new PlaybackState[paramAnonymousInt];
    }
  };
  private final int mErrorType;
  private final boolean mHasValidPlaylist;
  private final boolean mIsCurrentSongLoaded;
  private final boolean mIsInErrorState;
  private final boolean mIsInFatalErrorState;
  private final boolean mIsInIniniteMixMode;
  private final boolean mIsLocalDevicePlayback;
  private final boolean mIsPlaying;
  private final boolean mIsPlaylistLoading;
  private final boolean mIsPreparing;
  private final boolean mIsStreaming;
  private final boolean mIsStreamingFullyBuffered;
  private final SongList mMediaList;
  private final long mPosition;
  private final int mQueuePosition;
  private final int mQueueSize;
  private final int mRepeatMode;
  private final int mShuffleMode;

  public PlaybackState(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2, SongList paramSongList, long paramLong, int paramInt3, int paramInt4, boolean paramBoolean3, boolean paramBoolean4, boolean paramBoolean5, int paramInt5, boolean paramBoolean6, boolean paramBoolean7, boolean paramBoolean8, boolean paramBoolean9, boolean paramBoolean10, boolean paramBoolean11)
  {
    this.mQueuePosition = paramInt1;
    this.mQueueSize = paramInt2;
    this.mIsPlaying = paramBoolean1;
    this.mIsCurrentSongLoaded = paramBoolean2;
    this.mMediaList = paramSongList;
    this.mPosition = paramLong;
    this.mShuffleMode = paramInt3;
    this.mRepeatMode = paramInt4;
    this.mIsPreparing = paramBoolean3;
    this.mIsStreaming = paramBoolean4;
    this.mIsInErrorState = paramBoolean5;
    this.mErrorType = paramInt5;
    this.mIsStreamingFullyBuffered = paramBoolean6;
    boolean bool1 = paramBoolean7;
    this.mIsInFatalErrorState = bool1;
    boolean bool2 = paramBoolean8;
    this.mHasValidPlaylist = bool2;
    boolean bool3 = paramBoolean9;
    this.mIsPlaylistLoading = bool3;
    boolean bool4 = paramBoolean10;
    this.mIsLocalDevicePlayback = bool4;
    boolean bool5 = paramBoolean11;
    this.mIsInIniniteMixMode = bool5;
  }

  PlaybackState(Parcel paramParcel)
  {
    int j = paramParcel.readInt();
    this.mQueuePosition = j;
    int k = paramParcel.readInt();
    this.mQueueSize = k;
    boolean bool1;
    boolean bool2;
    label56: boolean bool3;
    label133: boolean bool4;
    label150: boolean bool5;
    label167: boolean bool6;
    label196: boolean bool7;
    label213: boolean bool8;
    label230: boolean bool9;
    label247: boolean bool10;
    if (paramParcel.readInt() != i)
    {
      bool1 = true;
      this.mIsPlaying = bool1;
      if (paramParcel.readInt() != 1)
        break label290;
      bool2 = true;
      this.mIsCurrentSongLoaded = bool2;
      ClassLoader localClassLoader = MediaList.class.getClassLoader();
      SongList localSongList = (SongList)paramParcel.readParcelable(localClassLoader);
      this.mMediaList = localSongList;
      long l = paramParcel.readLong();
      this.mPosition = l;
      int m = paramParcel.readInt();
      this.mShuffleMode = m;
      int n = paramParcel.readInt();
      this.mRepeatMode = n;
      if (paramParcel.readInt() != 1)
        break label296;
      bool3 = true;
      this.mIsPreparing = bool3;
      if (paramParcel.readInt() != 1)
        break label302;
      bool4 = true;
      this.mIsStreaming = bool4;
      if (paramParcel.readInt() != 1)
        break label308;
      bool5 = true;
      this.mIsInErrorState = bool5;
      int i1 = paramParcel.readInt();
      this.mErrorType = i1;
      if (paramParcel.readInt() != 1)
        break label314;
      bool6 = true;
      this.mIsStreamingFullyBuffered = bool6;
      if (paramParcel.readInt() != 1)
        break label320;
      bool7 = true;
      this.mIsInFatalErrorState = bool7;
      if (paramParcel.readInt() != 1)
        break label326;
      bool8 = true;
      this.mHasValidPlaylist = bool8;
      if (paramParcel.readInt() != 1)
        break label332;
      bool9 = true;
      this.mIsPlaylistLoading = bool9;
      if (paramParcel.readInt() != 1)
        break label338;
      bool10 = true;
      label264: this.mIsLocalDevicePlayback = bool10;
      if (paramParcel.readInt() != 1)
        break label344;
    }
    while (true)
    {
      this.mIsInIniniteMixMode = i;
      return;
      bool1 = false;
      break;
      label290: bool2 = false;
      break label56;
      label296: bool3 = false;
      break label133;
      label302: bool4 = false;
      break label150;
      label308: bool5 = false;
      break label167;
      label314: bool6 = false;
      break label196;
      label320: bool7 = false;
      break label213;
      label326: bool8 = false;
      break label230;
      label332: bool9 = false;
      break label247;
      label338: bool10 = false;
      break label264;
      label344: i = 0;
    }
  }

  public int describeContents()
  {
    return 0;
  }

  public int getErrorType()
  {
    return this.mErrorType;
  }

  public SongList getMediaList()
  {
    return this.mMediaList;
  }

  public long getPosition()
  {
    return this.mPosition;
  }

  public int getQueuePosition()
  {
    return this.mQueuePosition;
  }

  public int getQueueSize()
  {
    return this.mQueueSize;
  }

  public int getRepeatMode()
  {
    return this.mRepeatMode;
  }

  public int getShuffleMode()
  {
    return this.mShuffleMode;
  }

  public boolean hasValidPlaylist()
  {
    return this.mHasValidPlaylist;
  }

  public boolean isCurrentSongLoaded()
  {
    return this.mIsCurrentSongLoaded;
  }

  public boolean isInErrorState()
  {
    return this.mIsInErrorState;
  }

  public boolean isInIniniteMixMode()
  {
    return this.mIsInIniniteMixMode;
  }

  public boolean isLocalDevicePlayback()
  {
    return this.mIsLocalDevicePlayback;
  }

  public boolean isPlaying()
  {
    return this.mIsPlaying;
  }

  public boolean isPlaylistLoading()
  {
    return this.mIsPlaylistLoading;
  }

  public boolean isPreparing()
  {
    return this.mIsPreparing;
  }

  public boolean isStreaming()
  {
    return this.mIsStreaming;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("PlaybackState [mQueuePosition=");
    int i = this.mQueuePosition;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(i).append(", mQueueSize=");
    int j = this.mQueueSize;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(j).append(", mIsPlaying=");
    boolean bool1 = this.mIsPlaying;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(bool1).append(", mIsCurrentSongLoaded=");
    boolean bool2 = this.mIsCurrentSongLoaded;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(bool2).append(", mMediaList=");
    SongList localSongList = this.mMediaList;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(localSongList).append(", mPosition=");
    long l = this.mPosition;
    StringBuilder localStringBuilder7 = localStringBuilder6.append(l).append(", mShuffleMode=");
    int k = this.mShuffleMode;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(k).append(", mRepeatMode=");
    int m = this.mRepeatMode;
    StringBuilder localStringBuilder9 = localStringBuilder8.append(m).append(", mIsPreparing=");
    boolean bool3 = this.mIsPreparing;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(bool3).append(", mIsStreaming=");
    boolean bool4 = this.mIsStreaming;
    StringBuilder localStringBuilder11 = localStringBuilder10.append(bool4).append(", mIsInErrorState=");
    boolean bool5 = this.mIsInErrorState;
    StringBuilder localStringBuilder12 = localStringBuilder11.append(bool5).append(", mErrorType=");
    int n = this.mErrorType;
    StringBuilder localStringBuilder13 = localStringBuilder12.append(n).append(", mIsStreamingFullyBuffered=");
    boolean bool6 = this.mIsStreamingFullyBuffered;
    StringBuilder localStringBuilder14 = localStringBuilder13.append(bool6).append(", mIsInFatalErrorState=");
    boolean bool7 = this.mIsInFatalErrorState;
    StringBuilder localStringBuilder15 = localStringBuilder14.append(bool7).append(", mHasValidPlaylist=");
    boolean bool8 = this.mHasValidPlaylist;
    StringBuilder localStringBuilder16 = localStringBuilder15.append(bool8).append(", mIsPlaylistLoading=");
    boolean bool9 = this.mIsPlaylistLoading;
    StringBuilder localStringBuilder17 = localStringBuilder16.append(bool9).append(", mIsLocalDevicePlayback=");
    boolean bool10 = this.mIsLocalDevicePlayback;
    StringBuilder localStringBuilder18 = localStringBuilder17.append(bool10).append(", mIsInIniniteMixMode=");
    boolean bool11 = this.mIsInIniniteMixMode;
    return bool11 + "]";
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    int j = this.mQueuePosition;
    paramParcel.writeInt(j);
    int k = this.mQueueSize;
    paramParcel.writeInt(k);
    int m;
    int n;
    label52: int i3;
    label117: int i4;
    label133: int i5;
    label149: int i7;
    label177: int i8;
    label193: int i9;
    label209: int i10;
    label225: int i11;
    if (this.mIsPlaying)
    {
      m = 1;
      paramParcel.writeInt(m);
      if (!this.mIsCurrentSongLoaded)
        break label266;
      n = 1;
      paramParcel.writeInt(n);
      SongList localSongList = this.mMediaList;
      paramParcel.writeParcelable(localSongList, paramInt);
      long l = this.mPosition;
      paramParcel.writeLong(l);
      int i1 = this.mShuffleMode;
      paramParcel.writeInt(i1);
      int i2 = this.mRepeatMode;
      paramParcel.writeInt(i2);
      if (!this.mIsPreparing)
        break label272;
      i3 = 1;
      paramParcel.writeInt(i3);
      if (!this.mIsStreaming)
        break label278;
      i4 = 1;
      paramParcel.writeInt(i4);
      if (!this.mIsInErrorState)
        break label284;
      i5 = 1;
      paramParcel.writeInt(i5);
      int i6 = this.mErrorType;
      paramParcel.writeInt(i6);
      if (!this.mIsStreamingFullyBuffered)
        break label290;
      i7 = 1;
      paramParcel.writeInt(i7);
      if (!this.mIsInFatalErrorState)
        break label296;
      i8 = 1;
      paramParcel.writeInt(i8);
      if (!this.mHasValidPlaylist)
        break label302;
      i9 = 1;
      paramParcel.writeInt(i9);
      if (!this.mIsPlaylistLoading)
        break label308;
      i10 = 1;
      paramParcel.writeInt(i10);
      if (!this.mIsLocalDevicePlayback)
        break label314;
      i11 = 1;
      label241: paramParcel.writeInt(i11);
      if (!this.mIsInIniniteMixMode)
        break label320;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      return;
      m = 0;
      break;
      label266: n = 0;
      break label52;
      label272: i3 = 0;
      break label117;
      label278: i4 = 0;
      break label133;
      label284: i5 = 0;
      break label149;
      label290: i7 = 0;
      break label177;
      label296: i8 = 0;
      break label193;
      label302: i9 = 0;
      break label209;
      label308: i10 = 0;
      break label225;
      label314: i11 = 0;
      break label241;
      label320: i = 0;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.PlaybackState
 * JD-Core Version:    0.6.2
 */