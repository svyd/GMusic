package com.google.android.music.download;

public abstract interface DownloadTask extends Runnable
{
  public abstract void cancel();

  public abstract DownloadRequest getDownloadRequest();

  public abstract boolean upgrade(DownloadTask paramDownloadTask);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadTask
 * JD-Core Version:    0.6.2
 */