package com.google.android.music.ringtone.soundfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CheapSoundFile
{
  private static final char[] HEX_CHARS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  static HashMap<String, Factory> sExtensionMap;
  static Factory[] sSubclassFactories;
  static ArrayList<String> sSupportedExtensions;
  protected File mInputFile = null;
  protected ProgressListener mProgressListener = null;

  static
  {
    Factory[] arrayOfFactory1 = new Factory[4];
    Factory localFactory1 = CheapAAC.getFactory();
    arrayOfFactory1[0] = localFactory1;
    Factory localFactory2 = CheapAMR.getFactory();
    arrayOfFactory1[1] = localFactory2;
    Factory localFactory3 = CheapMP3.getFactory();
    arrayOfFactory1[2] = localFactory3;
    Factory localFactory4 = CheapWAV.getFactory();
    arrayOfFactory1[3] = localFactory4;
    sSubclassFactories = arrayOfFactory1;
    sSupportedExtensions = new ArrayList();
    sExtensionMap = new HashMap();
    Factory[] arrayOfFactory2 = sSubclassFactories;
    int i = arrayOfFactory2.length;
    int j = 0;
    while (j < i)
    {
      Factory localFactory5 = arrayOfFactory2[j];
      String[] arrayOfString = localFactory5.getSupportedExtensions();
      int k = arrayOfString.length;
      int m = 0;
      while (m < k)
      {
        String str = arrayOfString[m];
        boolean bool = sSupportedExtensions.add(str);
        Object localObject = sExtensionMap.put(str, localFactory5);
        m += 1;
      }
      j += 1;
    }
  }

  public static CheapSoundFile create(String paramString, ProgressListener paramProgressListener)
    throws FileNotFoundException, IOException
  {
    CheapSoundFile localCheapSoundFile = null;
    File localFile = new File(paramString);
    if (!localFile.exists())
      throw new FileNotFoundException(paramString);
    String[] arrayOfString = localFile.getName().toLowerCase().split("\\.");
    if (arrayOfString.length < 2);
    while (true)
    {
      return localCheapSoundFile;
      HashMap localHashMap = sExtensionMap;
      int i = arrayOfString.length + -1;
      String str = arrayOfString[i];
      Factory localFactory = (Factory)localHashMap.get(str);
      if (localFactory != null)
      {
        localCheapSoundFile = localFactory.create();
        localCheapSoundFile.setProgressListener(paramProgressListener);
        localCheapSoundFile.ReadFile(localFile);
      }
    }
  }

  public void ReadFile(File paramFile)
    throws FileNotFoundException, IOException
  {
    this.mInputFile = paramFile;
  }

  public void WriteFile(File paramFile, int paramInt1, int paramInt2)
    throws IOException
  {
  }

  public int getAvgBitrateKbps()
  {
    return 0;
  }

  public String getFiletype()
  {
    return "Unknown";
  }

  public int[] getFrameGains()
  {
    return null;
  }

  public int getNumFrames()
  {
    return 0;
  }

  public int getSampleRate()
  {
    return 0;
  }

  public int getSamplesPerFrame()
  {
    return 0;
  }

  public int getSeekableFrameOffset(int paramInt)
  {
    return -1;
  }

  public void setProgressListener(ProgressListener paramProgressListener)
  {
    this.mProgressListener = paramProgressListener;
  }

  public static abstract interface Factory
  {
    public abstract CheapSoundFile create();

    public abstract String[] getSupportedExtensions();
  }

  public static abstract interface ProgressListener
  {
    public abstract boolean reportProgress(double paramDouble);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.soundfile.CheapSoundFile
 * JD-Core Version:    0.6.2
 */