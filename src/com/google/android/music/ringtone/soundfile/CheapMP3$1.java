package com.google.android.music.ringtone.soundfile;

class CheapMP3$1
  implements CheapSoundFile.Factory
{
  public CheapSoundFile create()
  {
    return new CheapMP3();
  }

  public String[] getSupportedExtensions()
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "mp3";
    return arrayOfString;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.soundfile.CheapMP3.1
 * JD-Core Version:    0.6.2
 */