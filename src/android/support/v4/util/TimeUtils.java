package android.support.v4.util;

import java.io.PrintWriter;

public class TimeUtils
{
  private static char[] sFormatStr = new char[24];
  private static final Object sFormatSync = new Object();

  private static int accumField(int paramInt1, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    int i;
    if ((paramInt1 > 99) || ((paramBoolean) && (paramInt3 >= 3)))
      i = paramInt2 + 3;
    while (true)
    {
      return i;
      if ((paramInt1 > 9) || ((paramBoolean) && (paramInt3 >= 2)))
        i = paramInt2 + 2;
      else if ((paramBoolean) || (paramInt1 > 0))
        i = paramInt2 + 1;
      else
        i = 0;
    }
  }

  public static void formatDuration(long paramLong1, long paramLong2, PrintWriter paramPrintWriter)
  {
    if (paramLong1 == 0L)
    {
      paramPrintWriter.print("--");
      return;
    }
    formatDuration(paramLong1 - paramLong2, paramPrintWriter, 0);
  }

  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter)
  {
    formatDuration(paramLong, paramPrintWriter, 0);
  }

  public static void formatDuration(long paramLong, PrintWriter paramPrintWriter, int paramInt)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, paramInt);
      char[] arrayOfChar = sFormatStr;
      String str = new String(arrayOfChar, 0, i);
      paramPrintWriter.print(str);
      return;
    }
  }

  public static void formatDuration(long paramLong, StringBuilder paramStringBuilder)
  {
    synchronized (sFormatSync)
    {
      int i = formatDurationLocked(paramLong, 0);
      char[] arrayOfChar = sFormatStr;
      StringBuilder localStringBuilder = paramStringBuilder.append(arrayOfChar, 0, i);
      return;
    }
  }

  private static int formatDurationLocked(long paramLong, int paramInt)
  {
    int i = sFormatStr.length;
    int j = paramInt;
    if (i < j)
      sFormatStr = new char[paramInt];
    char[] arrayOfChar1 = sFormatStr;
    int n;
    if (paramLong == 0L)
    {
      int k = 0;
      paramInt += -1;
      while (true)
      {
        int m = paramInt;
        if (k >= m)
          break;
        arrayOfChar1[k] = ' ';
      }
      arrayOfChar1[k] = '0';
      n = 1;
      return n;
    }
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    int i6;
    int i10;
    boolean bool1;
    label230: boolean bool2;
    label260: boolean bool3;
    label290: int i20;
    int i21;
    boolean bool4;
    if (paramLong > 0L)
    {
      i1 = 43;
      i2 = (int)(paramLong % 1000L);
      i3 = (int)Math.floor(paramLong / 1000L);
      i4 = 0;
      i5 = 0;
      i6 = 0;
      if (i3 > 86400)
      {
        i4 = i3 / 86400;
        int i7 = 86400 * i4;
        i3 -= i7;
      }
      if (i3 > 3600)
      {
        i5 = i3 / 3600;
        int i8 = i5 * 3600;
        i3 -= i8;
      }
      if (i3 > 60)
      {
        i6 = i3 / 60;
        int i9 = i6 * 60;
        i3 -= i9;
      }
      i10 = 0;
      if (paramInt == 0)
        break label411;
      int i11 = accumField(i4, 1, false, 0);
      int i12 = 1;
      if (i11 <= 0)
        break label387;
      bool1 = true;
      int i13 = accumField(i5, i12, bool1, 2);
      int i14 = i11 + i13;
      int i15 = 1;
      if (i14 <= 0)
        break label393;
      bool2 = true;
      int i16 = accumField(i6, i15, bool2, 2);
      int i17 = i14 + i16;
      int i18 = 1;
      if (i17 <= 0)
        break label399;
      bool3 = true;
      int i19 = accumField(i3, i18, bool3, 2);
      i20 = i17 + i19;
      i21 = 2;
      bool4 = true;
      if (i20 <= 0)
        break label405;
    }
    label387: label393: label399: label405: for (int i22 = 3; ; i22 = 0)
    {
      int i23 = accumField(i2, i21, bool4, i22) + 1;
      int i24 = i20 + i23;
      while (true)
      {
        int i25 = paramInt;
        if (i24 >= i25)
          break;
        arrayOfChar1[i10] = ' ';
        i10 += 1;
        i24 += 1;
      }
      i1 = 45;
      paramLong = -paramLong;
      break;
      bool1 = false;
      break label230;
      bool2 = false;
      break label260;
      bool3 = false;
      break label290;
    }
    label411: arrayOfChar1[i10] = i1;
    int i26 = i10 + 1;
    int i27 = i26;
    label435: boolean bool5;
    label468: int i30;
    label476: boolean bool6;
    label523: int i35;
    label531: boolean bool7;
    label578: int i40;
    label586: int i43;
    char c4;
    boolean bool8;
    if (paramInt != 0)
    {
      i27 = 1;
      int i28 = printField(arrayOfChar1, i4, 'd', i26, false, 0);
      char c1 = 'h';
      int i29 = i27;
      if (i28 == i29)
        break label692;
      bool5 = true;
      if (i27 == 0)
        break label698;
      i30 = 2;
      char[] arrayOfChar2 = arrayOfChar1;
      int i31 = i5;
      int i32 = i28;
      int i33 = printField(arrayOfChar2, i31, c1, i32, bool5, i30);
      char c2 = 'm';
      int i34 = i27;
      if (i33 == i34)
        break label704;
      bool6 = true;
      if (i27 == 0)
        break label710;
      i35 = 2;
      char[] arrayOfChar3 = arrayOfChar1;
      int i36 = i6;
      int i37 = i33;
      int i38 = printField(arrayOfChar3, i36, c2, i37, bool6, i35);
      char c3 = 's';
      int i39 = i27;
      if (i38 == i39)
        break label716;
      bool7 = true;
      if (i27 == 0)
        break label722;
      i40 = 2;
      char[] arrayOfChar4 = arrayOfChar1;
      int i41 = i3;
      int i42 = i38;
      i43 = printField(arrayOfChar4, i41, c3, i42, bool7, i40);
      c4 = 'm';
      bool8 = true;
      if (i27 == 0)
        break label728;
      int i44 = i27;
      if (i43 == i44)
        break label728;
    }
    label692: label698: label704: label710: label716: label722: label728: for (int i45 = 3; ; i45 = 0)
    {
      char[] arrayOfChar5 = arrayOfChar1;
      int i46 = i2;
      int i47 = i43;
      int i48 = printField(arrayOfChar5, i46, c4, i47, bool8, i45);
      arrayOfChar1[i48] = 's';
      n = i48 + 1;
      break;
      i27 = 0;
      break label435;
      bool5 = false;
      break label468;
      i30 = 0;
      break label476;
      bool6 = false;
      break label523;
      i35 = 0;
      break label531;
      bool7 = false;
      break label578;
      i40 = 0;
      break label586;
    }
  }

  private static int printField(char[] paramArrayOfChar, int paramInt1, char paramChar, int paramInt2, boolean paramBoolean, int paramInt3)
  {
    if ((paramBoolean) || (paramInt1 > 0))
    {
      int i = paramInt2;
      if (((paramBoolean) && (paramInt3 >= 3)) || (paramInt1 > 99))
      {
        int j = paramInt1 / 100;
        int k = (char)(j + 48);
        paramArrayOfChar[paramInt2] = k;
        paramInt2 += 1;
        int m = j * 100;
        paramInt1 -= m;
      }
      if (((paramBoolean) && (paramInt3 >= 2)) || (paramInt1 > 9) || (i != paramInt2))
      {
        int n = paramInt1 / 10;
        int i1 = (char)(n + 48);
        paramArrayOfChar[paramInt2] = i1;
        paramInt2 += 1;
        int i2 = n * 10;
        paramInt1 -= i2;
      }
      int i3 = (char)(paramInt1 + 48);
      paramArrayOfChar[paramInt2] = i3;
      int i4 = paramInt2 + 1;
      paramArrayOfChar[i4] = paramChar;
      paramInt2 = i4 + 1;
    }
    return paramInt2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.TimeUtils
 * JD-Core Version:    0.6.2
 */