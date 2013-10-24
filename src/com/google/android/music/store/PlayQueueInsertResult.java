package com.google.android.music.store;

public final class PlayQueueInsertResult
{
  private final long mGroupId;
  private final int mGroupPosition;
  private final int mGroupSize;
  private final int mNewPlayPosition;

  PlayQueueInsertResult(long paramLong, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mGroupId = paramLong;
    this.mGroupSize = paramInt1;
    this.mNewPlayPosition = paramInt3;
    this.mGroupPosition = paramInt2;
  }

  public long getGroupId()
  {
    return this.mGroupId;
  }

  public int getGroupPosition()
  {
    if (this.mGroupPosition < 0)
      throw new IllegalStateException("Attempt to get the position of non-contiguous group");
    return this.mGroupPosition;
  }

  public int getNewPlayPosition()
  {
    return this.mNewPlayPosition;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.PlayQueueInsertResult
 * JD-Core Version:    0.6.2
 */