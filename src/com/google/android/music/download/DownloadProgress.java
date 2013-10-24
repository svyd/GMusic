package com.google.android.music.download;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.android.music.download.cache.FileLocation;
import java.io.File;

public class DownloadProgress
  implements Parcelable
{
  public static final Parcelable.Creator<DownloadProgress> CREATOR = new Parcelable.Creator()
  {
    public DownloadProgress createFromParcel(Parcel paramAnonymousParcel)
    {
      return new DownloadProgress(paramAnonymousParcel, null);
    }

    public DownloadProgress[] newArray(int paramAnonymousInt)
    {
      return new DownloadProgress[paramAnonymousInt];
    }
  };
  private final long mCompletedBytes;
  private final long mDownloadByteLength;
  private final int mError;
  private final String mHttpContentType;
  private final ContentIdentifier mId;
  private final boolean mIsFullCopy;
  private final boolean mIsSavable;
  private final DownloadRequest.Owner mOwner;
  private final int mPriority;
  private final long mSeekMs;
  private final DownloadState.State mState;
  private final String mTrackTitle;

  private DownloadProgress(Parcel paramParcel)
  {
    ClassLoader localClassLoader = ContentIdentifier.class.getClassLoader();
    ContentIdentifier localContentIdentifier = (ContentIdentifier)paramParcel.readParcelable(localClassLoader);
    this.mId = localContentIdentifier;
    String str1 = paramParcel.readString();
    this.mTrackTitle = str1;
    int j = paramParcel.readInt();
    this.mPriority = j;
    DownloadRequest.Owner[] arrayOfOwner = DownloadRequest.Owner.values();
    int k = paramParcel.readInt();
    DownloadRequest.Owner localOwner = arrayOfOwner[k];
    this.mOwner = localOwner;
    long l1 = paramParcel.readLong();
    this.mSeekMs = l1;
    DownloadState.State[] arrayOfState = DownloadState.State.values();
    int m = paramParcel.readInt();
    DownloadState.State localState = arrayOfState[m];
    this.mState = localState;
    long l2 = paramParcel.readLong();
    this.mCompletedBytes = l2;
    long l3 = paramParcel.readLong();
    this.mDownloadByteLength = l3;
    int n = paramParcel.readInt();
    this.mError = n;
    String str2 = paramParcel.readString();
    this.mHttpContentType = str2;
    boolean bool;
    if (paramParcel.readInt() != i)
    {
      bool = true;
      this.mIsSavable = bool;
      if (paramParcel.readInt() != 1)
        break label197;
    }
    while (true)
    {
      this.mIsFullCopy = i;
      return;
      bool = false;
      break;
      label197: i = 0;
    }
  }

  DownloadProgress(DownloadRequest paramDownloadRequest, DownloadState paramDownloadState)
  {
    ContentIdentifier localContentIdentifier = paramDownloadRequest.getId();
    this.mId = localContentIdentifier;
    String str1 = paramDownloadRequest.getTrackTitle();
    this.mTrackTitle = str1;
    int i = paramDownloadRequest.getPriority();
    this.mPriority = i;
    DownloadRequest.Owner localOwner = paramDownloadRequest.getOwner();
    this.mOwner = localOwner;
    long l1 = paramDownloadRequest.getSeekMs();
    this.mSeekMs = l1;
    DownloadState.State localState = paramDownloadState.getState();
    this.mState = localState;
    long l2 = paramDownloadState.getCompletedBytes();
    this.mCompletedBytes = l2;
    long l3 = paramDownloadState.getDownloadByteLength();
    this.mDownloadByteLength = l3;
    int j = paramDownloadState.getError();
    this.mError = j;
    String str2 = paramDownloadState.getHttpContentType();
    this.mHttpContentType = str2;
    boolean bool1 = checkSavable(paramDownloadRequest, paramDownloadState);
    this.mIsSavable = bool1;
    boolean bool2 = checkFullCopy(paramDownloadRequest, paramDownloadState);
    this.mIsFullCopy = bool2;
  }

  private boolean checkFullCopy(DownloadRequest paramDownloadRequest, DownloadState paramDownloadState)
  {
    DownloadState.State localState1 = paramDownloadState.getState();
    DownloadState.State localState2 = DownloadState.State.COMPLETED;
    if ((localState1 == localState2) && (paramDownloadRequest.getSeekMs() == 0L) && (!paramDownloadState.isExperiencedGlitch()) && (paramDownloadState.getCompletedBytes() != 0L))
    {
      long l1 = paramDownloadRequest.getFileLocation().getFullPath().length();
      long l2 = paramDownloadState.getCompletedFileSize();
      if (l1 == l2)
      {
        bool = true;
        if ((bool) && (!paramDownloadState.isDownloadByteLengthEstimated()))
        {
          long l3 = paramDownloadState.getDownloadByteLength();
          long l4 = paramDownloadState.getCompletedBytes();
          if (l3 != l4)
            break label118;
        }
      }
    }
    label118: for (boolean bool = true; ; bool = false)
    {
      return bool;
      bool = false;
      break;
    }
  }

  private boolean checkSavable(DownloadRequest paramDownloadRequest, DownloadState paramDownloadState)
  {
    if (paramDownloadRequest.getFileLocation().getCacheType() == 1);
    for (boolean bool = false; ; bool = checkFullCopy(paramDownloadRequest, paramDownloadState))
      return bool;
  }

  public int describeContents()
  {
    return 0;
  }

  public long getCompletedBytes()
  {
    return this.mCompletedBytes;
  }

  public long getDownloadByteLength()
  {
    return this.mDownloadByteLength;
  }

  public int getError()
  {
    return this.mError;
  }

  public String getHttpContentType()
  {
    return this.mHttpContentType;
  }

  public ContentIdentifier getId()
  {
    return this.mId;
  }

  public DownloadRequest.Owner getOwner()
  {
    return this.mOwner;
  }

  public long getSeekMs()
  {
    return this.mSeekMs;
  }

  public DownloadState.State getState()
  {
    return this.mState;
  }

  public String getTrackTitle()
  {
    return this.mTrackTitle;
  }

  public boolean isFullCopy()
  {
    return this.mIsFullCopy;
  }

  public boolean isSavable()
  {
    return this.mIsSavable;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("mId=");
    ContentIdentifier localContentIdentifier = this.mId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(localContentIdentifier);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(" mTrackTitle=\"");
    String str1 = this.mTrackTitle;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str1).append("\"");
    StringBuilder localStringBuilder7 = localStringBuilder1.append(" mPriority=");
    int i = this.mPriority;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(i);
    StringBuilder localStringBuilder9 = localStringBuilder1.append(" mOwner=");
    DownloadRequest.Owner localOwner = this.mOwner;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(localOwner);
    StringBuilder localStringBuilder11 = localStringBuilder1.append(" mSeekMs=");
    long l1 = this.mSeekMs;
    StringBuilder localStringBuilder12 = localStringBuilder11.append(l1);
    StringBuilder localStringBuilder13 = localStringBuilder1.append(" mState=");
    DownloadState.State localState = this.mState;
    StringBuilder localStringBuilder14 = localStringBuilder13.append(localState);
    StringBuilder localStringBuilder15 = localStringBuilder1.append(" mCompletedBytes=");
    long l2 = this.mCompletedBytes;
    StringBuilder localStringBuilder16 = localStringBuilder15.append(l2);
    StringBuilder localStringBuilder17 = localStringBuilder1.append(" mDownloadByteLength=");
    long l3 = this.mDownloadByteLength;
    StringBuilder localStringBuilder18 = localStringBuilder17.append(l3);
    StringBuilder localStringBuilder19 = localStringBuilder1.append(" mError=");
    int j = this.mError;
    StringBuilder localStringBuilder20 = localStringBuilder19.append(j);
    StringBuilder localStringBuilder21 = localStringBuilder1.append(" mHttpContentType=");
    String str2 = this.mHttpContentType;
    StringBuilder localStringBuilder22 = localStringBuilder21.append(str2);
    StringBuilder localStringBuilder23 = localStringBuilder1.append(" mIsSavable=");
    boolean bool1 = this.mIsSavable;
    StringBuilder localStringBuilder24 = localStringBuilder23.append(bool1);
    StringBuilder localStringBuilder25 = localStringBuilder1.append(" mIsFullCopy=");
    boolean bool2 = this.mIsFullCopy;
    StringBuilder localStringBuilder26 = localStringBuilder25.append(bool2);
    StringBuilder localStringBuilder27 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    ContentIdentifier localContentIdentifier = this.mId;
    paramParcel.writeParcelable(localContentIdentifier, paramInt);
    String str1 = this.mTrackTitle;
    paramParcel.writeString(str1);
    int j = this.mPriority;
    paramParcel.writeInt(j);
    int k = this.mOwner.ordinal();
    paramParcel.writeInt(k);
    long l1 = this.mSeekMs;
    paramParcel.writeLong(l1);
    int m = this.mState.ordinal();
    paramParcel.writeInt(m);
    long l2 = this.mCompletedBytes;
    paramParcel.writeLong(l2);
    long l3 = this.mDownloadByteLength;
    paramParcel.writeLong(l3);
    int n = this.mError;
    paramParcel.writeInt(n);
    String str2 = this.mHttpContentType;
    paramParcel.writeString(str2);
    int i1;
    if (this.mIsSavable)
    {
      i1 = 1;
      paramParcel.writeInt(i1);
      if (!this.mIsFullCopy)
        break label164;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      return;
      i1 = 0;
      break;
      label164: i = 0;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadProgress
 * JD-Core Version:    0.6.2
 */