package com.google.android.music.download.stream;

import android.content.Context;
import com.google.android.gsf.Gservices;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class TailStream extends InputStream
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private AtomicBoolean isClosed;
  private final Context mContext;
  private StreamingInput mInput;
  private boolean mIsFroyoInitDone = false;
  private volatile long mStartReadPoint;
  private final StreamingContent mStreamingContent;
  private int mTailFillCnt = -1;
  private final int mTailFillSize;
  private long mTotalRead = 0L;

  public TailStream(Context paramContext, StreamingContent paramStreamingContent, long paramLong)
  {
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    this.isClosed = localAtomicBoolean;
    if (LOGV)
    {
      String str = "New TailStream for: " + paramStreamingContent;
      Log.i("TailStream", str);
    }
    this.mContext = paramContext;
    this.mStreamingContent = paramStreamingContent;
    this.mStartReadPoint = paramLong;
    int i = Gservices.getInt(this.mContext.getContentResolver(), "music_local_http_stuffing", 65536);
    this.mTailFillSize = i;
  }

  private int readFromFile(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    if (this.mInput == null)
    {
      StreamingContent localStreamingContent1 = this.mStreamingContent;
      long l1 = this.mStartReadPoint;
      long l2 = this.mTotalRead;
      long l3 = l1 + l2;
      StreamingInput localStreamingInput = localStreamingContent1.getStreamingInput(l3);
      this.mInput = localStreamingInput;
      if (this.mInput == null)
      {
        if (LOGV)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("read(");
          StreamingContent localStreamingContent2 = this.mStreamingContent;
          String str = localStreamingContent2 + ") returning -1 since the file location doesn't exists";
          Log.i("TailStream", str);
        }
        i = -1;
      }
    }
    while (true)
    {
      return i;
      i = this.mInput.read(paramArrayOfByte, paramInt1, paramInt2);
      if (i > 0)
      {
        long l4 = this.mTotalRead;
        long l5 = i;
        long l6 = l4 + l5;
        this.mTotalRead = l6;
      }
      else
      {
        this.mInput.close();
        this.mInput = null;
      }
    }
  }

  public void close()
    throws IOException
  {
    if (this.mInput != null)
    {
      this.mInput.close();
      this.mInput = null;
    }
    synchronized (this.isClosed)
    {
      this.isClosed.set(true);
      this.isClosed.notifyAll();
      return;
    }
  }

  public int read()
  {
    throw new UnsupportedOperationException();
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    if (this.isClosed.get())
    {
      if (LOGV)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("read(");
        StreamingContent localStreamingContent1 = this.mStreamingContent;
        String str1 = localStreamingContent1 + ") returning -1 since we were closed";
        Log.i("TailStream", str1);
      }
      i = -1;
    }
    while (true)
    {
      return i;
      if ((MusicPreferences.isPreGingerbread()) && (!this.mIsFroyoInitDone));
      try
      {
        String str2 = this.mStreamingContent.waitForContentType();
        long l1 = this.mStartReadPoint;
        long l2 = this.mStreamingContent.getStartReadPoint();
        long l3 = l1 + l2;
        this.mStartReadPoint = l3;
        this.mIsFroyoInitDone = true;
        if (this.mTailFillCnt < 0)
          break label215;
        int j = this.mTailFillCnt;
        i = Math.min(paramInt2, j);
        if (i <= 0)
          i = -1;
      }
      catch (InterruptedException localInterruptedException1)
      {
        throw new InterruptedIOException("Interrupted while waiting for content type");
      }
      int k = 0;
      while (k < i)
      {
        int m = paramInt1 + k;
        paramArrayOfByte[m] = 0;
        k += 1;
      }
      int n = this.mTailFillCnt - i;
      this.mTailFillCnt = n;
      continue;
      try
      {
        label215: StreamingContent localStreamingContent2 = this.mStreamingContent;
        long l4 = this.mStartReadPoint;
        long l5 = this.mTotalRead;
        long l6 = l4 + l5 + 1L;
        localStreamingContent2.waitForData(l6);
        if (!this.isClosed.get())
          break label377;
        if (LOGV)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("read(");
          StreamingContent localStreamingContent3 = this.mStreamingContent;
          String str3 = localStreamingContent3 + ") returning -1 since we were closed";
          Log.i("TailStream", str3);
        }
        i = -1;
      }
      catch (InterruptedException localInterruptedException2)
      {
        if (LOGV)
        {
          StringBuilder localStringBuilder3 = new StringBuilder().append("TailStream for: ");
          StreamingContent localStreamingContent4 = this.mStreamingContent;
          String str4 = localStreamingContent4 + " interrupted";
          Log.w("TailStream", str4);
        }
        i = -1;
      }
      continue;
      label377: int i1 = readFromFile(paramArrayOfByte, paramInt1, paramInt2);
      if (i1 < 0)
      {
        i1 = readFromFile(paramArrayOfByte, paramInt1, paramInt2);
        if ((i1 < 0) && (this.mStreamingContent.isFinished()))
        {
          if (LOGV)
          {
            StringBuilder localStringBuilder4 = new StringBuilder().append("read(");
            StreamingContent localStreamingContent5 = this.mStreamingContent;
            StringBuilder localStringBuilder5 = localStringBuilder4.append(localStreamingContent5).append("): ");
            long l7 = this.mTotalRead;
            String str5 = l7 + "; returning -1";
            Log.i("TailStream", str5);
          }
          if ((this.mTailFillCnt < 0) && (this.mTailFillSize > 0))
          {
            int i2 = this.mTailFillSize;
            this.mTailFillCnt = i2;
            i = 0;
            continue;
          }
          i = -1;
        }
      }
      else
      {
        i = i1;
      }
    }
  }

  public String toString()
  {
    return this.mStreamingContent.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.TailStream
 * JD-Core Version:    0.6.2
 */