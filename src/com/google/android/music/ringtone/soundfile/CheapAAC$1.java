package com.google.android.music.ringtone.soundfile;

class CheapAAC$1
  implements CheapSoundFile.Factory
{
  public CheapSoundFile create()
  {
    return new CheapAAC();
  }

  public String[] getSupportedExtensions()
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "aac";
    arrayOfString[1] = "m4a";
    return arrayOfString;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.soundfile.CheapAAC.1
 * JD-Core Version:    0.6.2
 */