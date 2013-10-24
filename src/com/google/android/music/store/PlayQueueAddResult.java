package com.google.android.music.store;

public class PlayQueueAddResult
{
  private final int mAddedSize;
  private final int mRequestedSize;

  public PlayQueueAddResult(int paramInt1, int paramInt2)
  {
    this.mRequestedSize = paramInt2;
    this.mAddedSize = paramInt1;
  }

  public int getAddedSize()
  {
    return this.mAddedSize;
  }

  public int getRequestedSize()
  {
    return this.mRequestedSize;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.PlayQueueAddResult
 * JD-Core Version:    0.6.2
 */