package android.support.v7.media;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.util.TimeUtils;

public final class MediaItemStatus
{
  private final Bundle mBundle;

  private MediaItemStatus(Bundle paramBundle)
  {
    this.mBundle = paramBundle;
  }

  public static MediaItemStatus fromBundle(Bundle paramBundle)
  {
    if (paramBundle != null);
    for (MediaItemStatus localMediaItemStatus = new MediaItemStatus(paramBundle); ; localMediaItemStatus = null)
      return localMediaItemStatus;
  }

  private static String playbackStateToString(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default:
      str = Integer.toString(paramInt);
    case 0:
    case 3:
    case 1:
    case 2:
    case 4:
    case 5:
    case 6:
    case 7:
    }
    while (true)
    {
      return str;
      str = "pending";
      continue;
      str = "buffering";
      continue;
      str = "playing";
      continue;
      str = "paused";
      continue;
      str = "finished";
      continue;
      str = "canceled";
      continue;
      str = "invalidated";
      continue;
      str = "error";
    }
  }

  public Bundle asBundle()
  {
    return this.mBundle;
  }

  public long getContentDuration()
  {
    return this.mBundle.getLong("contentDuration", 65535L);
  }

  public long getContentPosition()
  {
    return this.mBundle.getLong("contentPosition", 65535L);
  }

  public Bundle getExtras()
  {
    return this.mBundle.getBundle("extras");
  }

  public int getPlaybackState()
  {
    return this.mBundle.getInt("playbackState", 7);
  }

  public long getTimestamp()
  {
    return this.mBundle.getLong("timestamp");
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("MediaItemStatus{ ");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("timestamp=");
    long l1 = SystemClock.elapsedRealtime();
    long l2 = getTimestamp();
    TimeUtils.formatDuration(l1 - l2, localStringBuilder1);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" ms ago");
    StringBuilder localStringBuilder5 = localStringBuilder1.append(", playbackState=");
    String str = playbackStateToString(getPlaybackState());
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str);
    StringBuilder localStringBuilder7 = localStringBuilder1.append(", contentPosition=");
    long l3 = getContentPosition();
    StringBuilder localStringBuilder8 = localStringBuilder7.append(l3);
    StringBuilder localStringBuilder9 = localStringBuilder1.append(", contentDuration=");
    long l4 = getContentDuration();
    StringBuilder localStringBuilder10 = localStringBuilder9.append(l4);
    StringBuilder localStringBuilder11 = localStringBuilder1.append(", extras=");
    Bundle localBundle = getExtras();
    StringBuilder localStringBuilder12 = localStringBuilder11.append(localBundle);
    StringBuilder localStringBuilder13 = localStringBuilder1.append(" }");
    return localStringBuilder1.toString();
  }

  public static final class Builder
  {
    private final Bundle mBundle;

    public Builder(int paramInt)
    {
      Bundle localBundle = new Bundle();
      this.mBundle = localBundle;
      long l = SystemClock.elapsedRealtime();
      Builder localBuilder1 = setTimestamp(l);
      Builder localBuilder2 = setPlaybackState(paramInt);
    }

    public MediaItemStatus build()
    {
      Bundle localBundle = this.mBundle;
      return new MediaItemStatus(localBundle, null);
    }

    public Builder setContentDuration(long paramLong)
    {
      this.mBundle.putLong("contentDuration", paramLong);
      return this;
    }

    public Builder setContentPosition(long paramLong)
    {
      this.mBundle.putLong("contentPosition", paramLong);
      return this;
    }

    public Builder setExtras(Bundle paramBundle)
    {
      this.mBundle.putBundle("extras", paramBundle);
      return this;
    }

    public Builder setPlaybackState(int paramInt)
    {
      this.mBundle.putInt("playbackState", paramInt);
      return this;
    }

    public Builder setTimestamp(long paramLong)
    {
      this.mBundle.putLong("timestamp", paramLong);
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaItemStatus
 * JD-Core Version:    0.6.2
 */