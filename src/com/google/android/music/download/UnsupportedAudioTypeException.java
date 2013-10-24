package com.google.android.music.download;

import java.io.IOException;

public class UnsupportedAudioTypeException extends IOException
{
  private final String mAudioType;

  public UnsupportedAudioTypeException(String paramString)
  {
    this.mAudioType = paramString;
  }

  public String getAudioType()
  {
    return this.mAudioType;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.UnsupportedAudioTypeException
 * JD-Core Version:    0.6.2
 */