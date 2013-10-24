package com.google.android.music.download;

import com.google.android.music.download.cp.CpUtils;

public class DownloadState
{
  private long mCompletedBytes;
  private long mDownloadByteLength;
  private int mError;
  private long mEstimatedDownloadByteLength;
  private boolean mExperiencedGlitch;
  private String mHttpContentType;
  private boolean mIsCpOn;
  private int mRecommendedBitrate;
  private long mStartTimeUTC;
  private State mState;

  public DownloadState()
  {
    State localState = State.NOT_STARTED;
    this.mState = localState;
    this.mStartTimeUTC = 0L;
    this.mCompletedBytes = 0L;
    this.mDownloadByteLength = 0L;
    this.mEstimatedDownloadByteLength = 0L;
    this.mError = 1;
    this.mExperiencedGlitch = false;
    this.mRecommendedBitrate = 0;
    this.mIsCpOn = false;
  }

  public void adjustDownloadLengthUsingHttpContentLength(long paramLong)
  {
    if (paramLong < 0L)
    {
      String str = "Negative contentLength:" + paramLong;
      throw new IllegalArgumentException(str);
    }
    long l = this.mCompletedBytes + paramLong;
    this.mDownloadByteLength = l;
  }

  public long calculateLatency()
  {
    long l1 = System.currentTimeMillis();
    long l2 = this.mStartTimeUTC;
    return l1 - l2;
  }

  public long getCompletedBytes()
  {
    return this.mCompletedBytes;
  }

  public long getCompletedFileSize()
  {
    if (this.mIsCpOn);
    for (long l = CpUtils.getEncryptedSize(this.mCompletedBytes); ; l = this.mCompletedBytes)
      return l;
  }

  public long getDownloadByteLength()
  {
    if (this.mDownloadByteLength != 0L);
    for (long l = this.mDownloadByteLength; ; l = this.mEstimatedDownloadByteLength)
      return l;
  }

  public int getError()
  {
    return this.mError;
  }

  public String getHttpContentType()
  {
    return this.mHttpContentType;
  }

  public int getRecommendedBitrate()
  {
    return this.mRecommendedBitrate;
  }

  public State getState()
  {
    return this.mState;
  }

  public long incrementCompletedBytes(long paramLong)
  {
    if (paramLong < 0L)
    {
      String str = "Negative value:" + paramLong;
      throw new IllegalArgumentException(str);
    }
    long l = this.mCompletedBytes + paramLong;
    this.mCompletedBytes = l;
    return this.mCompletedBytes;
  }

  public boolean isCpOn()
  {
    return this.mIsCpOn;
  }

  public boolean isDownloadByteLengthEstimated()
  {
    if ((this.mDownloadByteLength == 0L) && (this.mEstimatedDownloadByteLength != 0L));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isExperiencedGlitch()
  {
    return this.mExperiencedGlitch;
  }

  public void resetCompletedBytes()
  {
    this.mCompletedBytes = 0L;
  }

  public void setCanceledState()
  {
    State localState = State.CANCELED;
    this.mState = localState;
  }

  public void setCompletedState()
  {
    State localState = State.COMPLETED;
    this.mState = localState;
  }

  public void setCp()
  {
    this.mIsCpOn = true;
  }

  public void setDownloadingState()
  {
    State localState = State.DOWNLOADING;
    this.mState = localState;
    long l = System.currentTimeMillis();
    this.mStartTimeUTC = l;
  }

  public void setEstimatedDownloadByteLength(long paramLong)
  {
    this.mEstimatedDownloadByteLength = paramLong;
  }

  public void setExperiencedGlitch()
  {
    this.mExperiencedGlitch = true;
  }

  public void setFailedState(int paramInt)
  {
    State localState = State.FAILED;
    this.mState = localState;
    this.mError = paramInt;
  }

  public void setHttpContentType(String paramString)
  {
    this.mHttpContentType = paramString;
  }

  public void setRecommendedBitrate(int paramInt)
  {
    this.mRecommendedBitrate = paramInt;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("mState=");
    State localState = this.mState;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localState);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" mStartTimeUTC=");
    long l1 = this.mStartTimeUTC;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(l1);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(" mCompletedBytes=");
    long l2 = this.mCompletedBytes;
    StringBuilder localStringBuilder7 = localStringBuilder6.append(l2);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" mDownloadByteLength=");
    long l3 = this.mDownloadByteLength;
    StringBuilder localStringBuilder9 = localStringBuilder8.append(l3);
    StringBuilder localStringBuilder10 = localStringBuilder1.append(" mEstimatedDownloadByteLength=");
    long l4 = this.mEstimatedDownloadByteLength;
    StringBuilder localStringBuilder11 = localStringBuilder10.append(l4);
    StringBuilder localStringBuilder12 = localStringBuilder1.append(" mError=");
    int i = this.mError;
    StringBuilder localStringBuilder13 = localStringBuilder12.append(i);
    StringBuilder localStringBuilder14 = localStringBuilder1.append(" mExperiencedGlitch=");
    boolean bool1 = this.mExperiencedGlitch;
    StringBuilder localStringBuilder15 = localStringBuilder14.append(bool1);
    StringBuilder localStringBuilder16 = localStringBuilder1.append(" mRecommendedBitrate=");
    int j = this.mRecommendedBitrate;
    StringBuilder localStringBuilder17 = localStringBuilder16.append(j);
    StringBuilder localStringBuilder18 = localStringBuilder1.append(" mHttpContentType=");
    String str = this.mHttpContentType;
    StringBuilder localStringBuilder19 = localStringBuilder18.append(str);
    StringBuilder localStringBuilder20 = localStringBuilder1.append(" mIsCpOn=");
    boolean bool2 = this.mIsCpOn;
    StringBuilder localStringBuilder21 = localStringBuilder20.append(bool2);
    return localStringBuilder1.toString();
  }

  public static enum State
  {
    static
    {
      DOWNLOADING = new State("DOWNLOADING", 1);
      COMPLETED = new State("COMPLETED", 2);
      FAILED = new State("FAILED", 3);
      CANCELED = new State("CANCELED", 4);
      State[] arrayOfState = new State[5];
      State localState1 = NOT_STARTED;
      arrayOfState[0] = localState1;
      State localState2 = DOWNLOADING;
      arrayOfState[1] = localState2;
      State localState3 = COMPLETED;
      arrayOfState[2] = localState3;
      State localState4 = FAILED;
      arrayOfState[3] = localState4;
      State localState5 = CANCELED;
      arrayOfState[4] = localState5;
    }

    public boolean isFinished()
    {
      State localState1 = COMPLETED;
      if (this != localState1)
      {
        State localState2 = FAILED;
        if (this != localState2)
        {
          State localState3 = CANCELED;
          if (this != localState3)
            break label33;
        }
      }
      label33: for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadState
 * JD-Core Version:    0.6.2
 */