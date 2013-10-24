package com.google.cast;

public class SessionError
{
  private static final String[] a;
  private static final String[] b = arrayOfString2;
  private int c;
  private int d;

  static
  {
    String[] arrayOfString1 = new String[5];
    arrayOfString1[0] = "start application";
    arrayOfString1[1] = "create channel";
    arrayOfString1[2] = "connect channel";
    arrayOfString1[3] = "disconnect channel";
    arrayOfString1[4] = "stop application";
    a = arrayOfString1;
    String[] arrayOfString2 = new String[6];
    arrayOfString2[0] = "network I/O timeout";
    arrayOfString2[1] = "request failed";
    arrayOfString2[2] = "protocol error";
    arrayOfString2[3] = "no application is running";
    arrayOfString2[4] = "application instance changed";
    arrayOfString2[5] = "no channel info received";
  }

  SessionError(int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 1) || (paramInt1 > 4) || (paramInt2 < 1) || (paramInt2 > 6))
      throw new IllegalArgumentException("Invalid category and/or code");
    this.c = paramInt1;
    this.d = paramInt2;
  }

  public String toString()
  {
    Object[] arrayOfObject = new Object[2];
    String[] arrayOfString1 = a;
    int i = this.c + -1;
    String str1 = arrayOfString1[i];
    arrayOfObject[0] = str1;
    String[] arrayOfString2 = b;
    int j = this.d + -1;
    String str2 = arrayOfString2[j];
    arrayOfObject[1] = str2;
    return String.format("failed to %s: %s", arrayOfObject);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.SessionError
 * JD-Core Version:    0.6.2
 */