package com.google.android.music.download;

import java.io.IOException;
import java.io.InputStream;

public class ID3v1FooterInputStream extends InputStream
{
  private final DownloadState mDownloadState;
  private boolean mEndingChecked;
  private final byte[] mLastBytes;
  private int mLastBytesIndex;
  private byte[] mSingleByte;
  private final InputStream mWrappedStream;

  public ID3v1FooterInputStream(InputStream paramInputStream, DownloadState paramDownloadState)
  {
    byte[] arrayOfByte = new byte[''];
    this.mLastBytes = arrayOfByte;
    this.mLastBytesIndex = 0;
    this.mEndingChecked = false;
    this.mSingleByte = null;
    this.mWrappedStream = paramInputStream;
    this.mDownloadState = paramDownloadState;
  }

  private void checkEndingBuffer()
    throws IOException
  {
    if (this.mEndingChecked)
      return;
    this.mEndingChecked = true;
    DownloadState.State localState1 = this.mDownloadState.getState();
    DownloadState.State localState2 = DownloadState.State.FAILED;
    if (localState1 == localState2)
      return;
    int i = this.mLastBytesIndex;
    int j = this.mLastBytes.length;
    if (i != j)
      throw new IOException("Footer bytes were never received");
    byte[] arrayOfByte = this.mLastBytes;
    if (new String(arrayOfByte, 0, 3).equals("TAG"))
      return;
    StringBuilder localStringBuilder1 = new StringBuilder().append("File did not have ID3V1 tag at the end.  Last ");
    int k = this.mLastBytes.length;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(k).append(" bytes start with: { ");
    int m = this.mLastBytes[0];
    StringBuilder localStringBuilder3 = localStringBuilder2.append(m).append(", ");
    int n = this.mLastBytes[1];
    StringBuilder localStringBuilder4 = localStringBuilder3.append(n).append(", ");
    int i1 = this.mLastBytes[2];
    String str = i1 + " }";
    throw new IOException(str);
  }

  public void close()
    throws IOException
  {
    this.mWrappedStream.close();
    checkEndingBuffer();
  }

  public int read()
    throws IOException
  {
    int i = -1;
    if (this.mSingleByte == null)
    {
      byte[] arrayOfByte1 = new byte[1];
      this.mSingleByte = arrayOfByte1;
    }
    byte[] arrayOfByte2 = this.mSingleByte;
    if (read(arrayOfByte2, 0, 1) == -1);
    while (true)
    {
      return i;
      i = this.mSingleByte[0] & 0xFF;
    }
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.mWrappedStream.read(paramArrayOfByte, paramInt1, paramInt2);
    int j = Math.min(this.mLastBytes.length, i);
    if (i == -1)
      checkEndingBuffer();
    while (true)
    {
      return i;
      int k = this.mLastBytes.length;
      if (j != k)
      {
        int m = paramInt1 + i - j;
        byte[] arrayOfByte1 = this.mLastBytes;
        System.arraycopy(paramArrayOfByte, m, arrayOfByte1, 0, j);
        int n = this.mLastBytes.length;
        this.mLastBytesIndex = n;
      }
      else if (j > 0)
      {
        int i1 = this.mLastBytesIndex + j;
        int i2 = this.mLastBytes.length;
        if (i1 <= i2)
        {
          int i3 = paramInt1 + i - j;
          byte[] arrayOfByte2 = this.mLastBytes;
          int i4 = this.mLastBytesIndex;
          System.arraycopy(paramArrayOfByte, i3, arrayOfByte2, i4, j);
          int i5 = this.mLastBytesIndex + j;
          this.mLastBytesIndex = i5;
        }
        else
        {
          int i6 = this.mLastBytesIndex + j;
          int i7 = this.mLastBytes.length;
          int i8 = i6 - i7;
          int i9 = this.mLastBytesIndex - i8;
          byte[] arrayOfByte3 = this.mLastBytes;
          byte[] arrayOfByte4 = this.mLastBytes;
          System.arraycopy(arrayOfByte3, i8, arrayOfByte4, 0, i9);
          int i10 = paramInt1 + i - j;
          byte[] arrayOfByte5 = this.mLastBytes;
          int i11 = this.mLastBytes.length - j;
          System.arraycopy(paramArrayOfByte, i10, arrayOfByte5, i11, j);
          int i12 = this.mLastBytes.length;
          this.mLastBytesIndex = i12;
        }
      }
    }
  }

  public long skip(long paramLong)
    throws IOException
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.ID3v1FooterInputStream
 * JD-Core Version:    0.6.2
 */