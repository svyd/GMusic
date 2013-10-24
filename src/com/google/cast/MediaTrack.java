package com.google.cast;

public final class MediaTrack
  implements Cloneable
{
  private long a;
  private Type b;
  private String c;
  private String d;
  private boolean e;

  MediaTrack(long paramLong, Type paramType, String paramString1, String paramString2, boolean paramBoolean)
  {
    this.a = paramLong;
    this.b = paramType;
    this.c = paramString1;
    this.d = paramString2;
    this.e = paramBoolean;
  }

  void a(boolean paramBoolean)
  {
    this.e = paramBoolean;
  }

  public Object clone()
    throws CloneNotSupportedException
  {
    return super.clone();
  }

  public boolean isEnabled()
  {
    return this.e;
  }

  public String toString()
  {
    StringBuffer localStringBuffer1 = new StringBuffer(40);
    StringBuffer localStringBuffer2 = localStringBuffer1.append("[MediaTrack #");
    long l = this.a;
    StringBuffer localStringBuffer3 = localStringBuffer2.append(l).append(' ');
    Type localType = this.b;
    StringBuffer localStringBuffer4 = localStringBuffer3.append(localType);
    if (this.c != null)
    {
      StringBuffer localStringBuffer5 = localStringBuffer1.append("; name: ");
      String str1 = this.c;
      StringBuffer localStringBuffer6 = localStringBuffer5.append(str1);
    }
    if (this.d != null)
    {
      StringBuffer localStringBuffer7 = localStringBuffer1.append("; lang: ");
      String str2 = this.d;
      StringBuffer localStringBuffer8 = localStringBuffer7.append(str2);
    }
    StringBuffer localStringBuffer9 = localStringBuffer1.append(']');
    return localStringBuffer1.toString();
  }

  public static enum Type
  {
    static
    {
      CAPTIONS = new Type("CAPTIONS", 1);
      AUDIO = new Type("AUDIO", 2);
      VIDEO = new Type("VIDEO", 3);
      Type[] arrayOfType = new Type[4];
      Type localType1 = SUBTITLES;
      arrayOfType[0] = localType1;
      Type localType2 = CAPTIONS;
      arrayOfType[1] = localType2;
      Type localType3 = AUDIO;
      arrayOfType[2] = localType3;
      Type localType4 = VIDEO;
      arrayOfType[3] = localType4;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.MediaTrack
 * JD-Core Version:    0.6.2
 */