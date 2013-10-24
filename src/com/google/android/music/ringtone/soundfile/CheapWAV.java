package com.google.android.music.ringtone.soundfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CheapWAV extends CheapSoundFile
{
  private int mChannels;
  private int mFileSize;
  private int mFrameBytes;
  private int[] mFrameGains;
  private int[] mFrameLens;
  private int[] mFrameOffsets;
  private int mNumFrames;
  private int mOffset;
  private int mSampleRate;

  public static CheapSoundFile.Factory getFactory()
  {
    return new CheapSoundFile.Factory()
    {
      public CheapSoundFile create()
      {
        return new CheapWAV();
      }

      public String[] getSupportedExtensions()
      {
        String[] arrayOfString = new String[1];
        arrayOfString[0] = "wav";
        return arrayOfString;
      }
    };
  }

  public void ReadFile(File paramFile)
    throws FileNotFoundException, IOException
  {
    super.ReadFile(paramFile);
    int i = (int)this.mInputFile.length();
    this.mFileSize = i;
    int j = this.mFileSize;
    int k = 128;
    if (j < k)
      throw new IOException("File too small to parse");
    File localFile = this.mInputFile;
    FileInputStream localFileInputStream = new FileInputStream(localFile);
    byte[] arrayOfByte1 = new byte[12];
    int m = 0;
    int n = 12;
    int i1 = localFileInputStream.read(arrayOfByte1, m, n);
    int i2 = this.mOffset + 12;
    this.mOffset = i2;
    int i3 = arrayOfByte1[0];
    int i4 = 82;
    if (i3 == i4)
    {
      int i5 = arrayOfByte1[1];
      int i6 = 73;
      if (i5 == i6)
      {
        int i7 = arrayOfByte1[2];
        int i8 = 70;
        if (i7 == i8)
        {
          int i9 = arrayOfByte1[3];
          int i10 = 70;
          if (i9 == i10)
          {
            int i11 = arrayOfByte1[8];
            int i12 = 87;
            if (i11 == i12)
            {
              int i13 = arrayOfByte1[9];
              int i14 = 65;
              if (i13 == i14)
              {
                int i15 = arrayOfByte1[10];
                int i16 = 86;
                if (i15 == i16)
                {
                  int i17 = arrayOfByte1[11];
                  int i18 = 69;
                  if (i17 == i18)
                    break label253;
                }
              }
            }
          }
        }
      }
    }
    throw new IOException("Not a WAV file");
    label253: int i19 = 0;
    this.mChannels = i19;
    int i20 = 0;
    this.mSampleRate = i20;
    while (true)
    {
      int i21 = this.mOffset + 8;
      int i22 = this.mFileSize;
      int i23 = i21;
      int i24 = i22;
      if (i23 > i24)
        return;
      byte[] arrayOfByte2 = new byte[8];
      int i25 = 0;
      int i26 = 8;
      int i27 = localFileInputStream.read(arrayOfByte2, i25, i26);
      int i28 = this.mOffset + 8;
      this.mOffset = i28;
      int i29 = (arrayOfByte2[7] & 0xFF) << 24;
      int i30 = (arrayOfByte2[6] & 0xFF) << 16;
      int i31 = i29 | i30;
      int i32 = (arrayOfByte2[5] & 0xFF) << 8;
      int i33 = i31 | i32;
      int i34 = arrayOfByte2[4] & 0xFF;
      int i35 = i33 | i34;
      int i36 = arrayOfByte2[0];
      int i37 = 102;
      if (i36 == i37)
      {
        int i38 = arrayOfByte2[1];
        int i39 = 109;
        if (i38 == i39)
        {
          int i40 = arrayOfByte2[2];
          int i41 = 116;
          if (i40 == i41)
          {
            int i42 = arrayOfByte2[3];
            int i43 = 32;
            if (i42 == i43)
            {
              int i44 = 16;
              if (i35 >= i44)
              {
                int i45 = 1024;
                if (i35 <= i45);
              }
              else
              {
                throw new IOException("WAV file has bad fmt chunk");
              }
              byte[] arrayOfByte3 = new byte[i35];
              int i46 = 0;
              int i47 = localFileInputStream.read(arrayOfByte3, i46, i35);
              int i48 = this.mOffset + i35;
              this.mOffset = i48;
              int i49 = (arrayOfByte3[1] & 0xFF) << 8;
              int i50 = arrayOfByte3[0] & 0xFF;
              int i51 = i49 | i50;
              int i52 = (arrayOfByte3[3] & 0xFF) << 8;
              int i53 = arrayOfByte3[2] & 0xFF;
              int i54 = i52 | i53;
              this.mChannels = i54;
              int i55 = (arrayOfByte3[7] & 0xFF) << 24;
              int i56 = (arrayOfByte3[6] & 0xFF) << 16;
              int i57 = i55 | i56;
              int i58 = (arrayOfByte3[5] & 0xFF) << 8;
              int i59 = i57 | i58;
              int i60 = arrayOfByte3[4] & 0xFF;
              int i61 = i59 | i60;
              this.mSampleRate = i61;
              int i62 = 1;
              if (i51 == i62)
                continue;
              throw new IOException("Unsupported WAV file encoding");
            }
          }
        }
      }
      else
      {
        int i63 = arrayOfByte2[0];
        int i64 = 100;
        if (i63 == i64)
        {
          int i65 = arrayOfByte2[1];
          int i66 = 97;
          if (i65 == i66)
          {
            int i67 = arrayOfByte2[2];
            int i68 = 116;
            if (i67 == i68)
            {
              int i69 = arrayOfByte2[3];
              int i70 = 97;
              if (i69 == i70)
              {
                if ((this.mChannels == 0) || (this.mSampleRate == 0))
                  throw new IOException("Bad WAV file: data chunk before fmt chunk");
                int i71 = this.mSampleRate;
                int i72 = this.mChannels;
                int i73 = i71 * i72 / 50 * 2;
                this.mFrameBytes = i73;
                int i74 = this.mFrameBytes + -1 + i35;
                int i75 = this.mFrameBytes;
                int i76 = i74 / i75;
                this.mNumFrames = i76;
                int[] arrayOfInt1 = new int[this.mNumFrames];
                this.mFrameOffsets = arrayOfInt1;
                int[] arrayOfInt2 = new int[this.mNumFrames];
                this.mFrameLens = arrayOfInt2;
                int[] arrayOfInt3 = new int[this.mNumFrames];
                this.mFrameGains = arrayOfInt3;
                byte[] arrayOfByte4 = new byte[this.mFrameBytes];
                int i77 = 0;
                int i78 = 0;
                if (i77 >= i35)
                  continue;
                int i79 = this.mFrameBytes;
                if (i77 + i79 > i35)
                  i77 = i35 - i79;
                int i80 = 0;
                int i81 = localFileInputStream.read(arrayOfByte4, i80, i79);
                int i82 = 0;
                int i83 = 1;
                while (i83 < i79)
                {
                  int i84 = Math.abs(arrayOfByte4[i83]);
                  if (i84 > i82)
                    i82 = i84;
                  int i85 = this.mChannels * 4;
                  i83 += i85;
                }
                int[] arrayOfInt4 = this.mFrameOffsets;
                int i86 = this.mOffset;
                arrayOfInt4[i78] = i86;
                this.mFrameLens[i78] = i79;
                this.mFrameGains[i78] = i82;
                i78 += 1;
                int i87 = this.mOffset + i79;
                this.mOffset = i87;
                i77 += i79;
                if (this.mProgressListener == null)
                  break;
                CheapSoundFile.ProgressListener localProgressListener = this.mProgressListener;
                float f = i77 * 0.0F;
                double d1 = i35;
                double d2 = f / d1;
                if (localProgressListener.reportProgress(d2))
                  break;
                continue;
              }
            }
          }
        }
        long l1 = i35;
        long l2 = localFileInputStream.skip(l1);
        int i88 = this.mOffset + i35;
        this.mOffset = i88;
      }
    }
  }

  public void WriteFile(File paramFile, int paramInt1, int paramInt2)
    throws IOException
  {
    boolean bool = paramFile.createNewFile();
    File localFile1 = this.mInputFile;
    FileInputStream localFileInputStream = new FileInputStream(localFile1);
    File localFile2 = paramFile;
    FileOutputStream localFileOutputStream = new FileOutputStream(localFile2);
    long l1 = 0L;
    int i = 0;
    while (true)
    {
      int j = paramInt2;
      if (i >= j)
        break;
      int[] arrayOfInt1 = this.mFrameLens;
      int k = paramInt1 + i;
      long l2 = arrayOfInt1[k];
      l1 += l2;
      i += 1;
    }
    long l3 = l1 + 36L;
    long l4 = this.mSampleRate;
    int m = this.mSampleRate * 2;
    int n = this.mChannels;
    long l5 = m * n;
    byte[] arrayOfByte1 = new byte[44];
    arrayOfByte1[0] = 82;
    arrayOfByte1[1] = 73;
    arrayOfByte1[2] = 70;
    arrayOfByte1[3] = 70;
    int i1 = (byte)(int)(0xFF & l3);
    arrayOfByte1[4] = i1;
    int i2 = (byte)(int)(l3 >> 8 & 0xFF);
    arrayOfByte1[5] = i2;
    int i3 = (byte)(int)(l3 >> 16 & 0xFF);
    arrayOfByte1[6] = i3;
    int i4 = (byte)(int)(l3 >> 24 & 0xFF);
    arrayOfByte1[7] = i4;
    arrayOfByte1[8] = 87;
    arrayOfByte1[9] = 65;
    arrayOfByte1[10] = 86;
    arrayOfByte1[11] = 69;
    arrayOfByte1[12] = 102;
    arrayOfByte1[13] = 109;
    arrayOfByte1[14] = 116;
    arrayOfByte1[15] = 32;
    arrayOfByte1[16] = 16;
    arrayOfByte1[17] = 0;
    arrayOfByte1[18] = 0;
    arrayOfByte1[19] = 0;
    arrayOfByte1[20] = 1;
    arrayOfByte1[21] = 0;
    int i5 = (byte)this.mChannels;
    arrayOfByte1[22] = i5;
    arrayOfByte1[23] = 0;
    int i6 = (byte)(int)(0xFF & l4);
    arrayOfByte1[24] = i6;
    int i7 = (byte)(int)(l4 >> 8 & 0xFF);
    arrayOfByte1[25] = i7;
    int i8 = (byte)(int)(l4 >> 16 & 0xFF);
    arrayOfByte1[26] = i8;
    int i9 = (byte)(int)(l4 >> 24 & 0xFF);
    arrayOfByte1[27] = i9;
    int i10 = (byte)(int)(0xFF & l5);
    arrayOfByte1[28] = i10;
    int i11 = (byte)(int)(l5 >> 8 & 0xFF);
    arrayOfByte1[29] = i11;
    int i12 = (byte)(int)(l5 >> 16 & 0xFF);
    arrayOfByte1[30] = i12;
    int i13 = (byte)(int)(l5 >> 24 & 0xFF);
    arrayOfByte1[31] = i13;
    int i14 = (byte)(this.mChannels * 2);
    arrayOfByte1[32] = i14;
    arrayOfByte1[33] = 0;
    arrayOfByte1[34] = 16;
    arrayOfByte1[35] = 0;
    arrayOfByte1[36] = 100;
    arrayOfByte1[37] = 97;
    arrayOfByte1[38] = 116;
    arrayOfByte1[39] = 97;
    int i15 = (byte)(int)(0xFF & l1);
    arrayOfByte1[40] = i15;
    int i16 = (byte)(int)(l1 >> 8 & 0xFF);
    arrayOfByte1[41] = i16;
    int i17 = (byte)(int)(l1 >> 16 & 0xFF);
    arrayOfByte1[42] = i17;
    int i18 = (byte)(int)(l1 >> 24 & 0xFF);
    arrayOfByte1[43] = i18;
    int i19 = 0;
    int i20 = 44;
    localFileOutputStream.write(arrayOfByte1, i19, i20);
    byte[] arrayOfByte2 = new byte[this.mFrameBytes];
    int i21 = 0;
    int i22 = 0;
    int i23 = paramInt2;
    if (i22 < i23)
    {
      int[] arrayOfInt2 = this.mFrameOffsets;
      int i24 = paramInt1 + i22;
      int i25 = arrayOfInt2[i24] - i21;
      int[] arrayOfInt3 = this.mFrameLens;
      int i26 = paramInt1 + i22;
      int i27 = arrayOfInt3[i26];
      if (i25 < 0);
      while (true)
      {
        i22 += 1;
        break;
        if (i25 > 0)
        {
          long l6 = i25;
          long l7 = localFileInputStream.skip(l6);
          i21 += i25;
        }
        int i28 = 0;
        int i29 = localFileInputStream.read(arrayOfByte2, i28, i27);
        int i30 = 0;
        localFileOutputStream.write(arrayOfByte2, i30, i27);
        i21 += i27;
      }
    }
    localFileInputStream.close();
    localFileOutputStream.close();
  }

  public int getAvgBitrateKbps()
  {
    int i = this.mSampleRate;
    int j = this.mChannels;
    return i * j * 2 / 1024;
  }

  public String getFiletype()
  {
    return "WAV";
  }

  public int[] getFrameGains()
  {
    return this.mFrameGains;
  }

  public int getNumFrames()
  {
    return this.mNumFrames;
  }

  public int getSampleRate()
  {
    return this.mSampleRate;
  }

  public int getSamplesPerFrame()
  {
    return this.mSampleRate / 50;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.soundfile.CheapWAV
 * JD-Core Version:    0.6.2
 */