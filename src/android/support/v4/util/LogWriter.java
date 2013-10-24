package android.support.v4.util;

import android.util.Log;
import java.io.Writer;

public class LogWriter extends Writer
{
  private StringBuilder mBuilder;
  private final String mTag;

  public LogWriter(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder(128);
    this.mBuilder = localStringBuilder;
    this.mTag = paramString;
  }

  private void flushBuilder()
  {
    if (this.mBuilder.length() <= 0)
      return;
    String str1 = this.mTag;
    String str2 = this.mBuilder.toString();
    int i = Log.d(str1, str2);
    StringBuilder localStringBuilder1 = this.mBuilder;
    int j = this.mBuilder.length();
    StringBuilder localStringBuilder2 = localStringBuilder1.delete(0, j);
  }

  public void close()
  {
    flushBuilder();
  }

  public void flush()
  {
    flushBuilder();
  }

  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    int i = 0;
    if (i >= paramInt2)
      return;
    int j = paramInt1 + i;
    char c = paramArrayOfChar[j];
    if (c == '\n')
      flushBuilder();
    while (true)
    {
      i += 1;
      break;
      StringBuilder localStringBuilder = this.mBuilder.append(c);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.LogWriter
 * JD-Core Version:    0.6.2
 */