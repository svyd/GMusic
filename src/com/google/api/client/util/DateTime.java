package com.google.api.client.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTime
{
  private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
  public final boolean dateOnly;
  public final Integer tzShift;
  public final long value;

  public DateTime(boolean paramBoolean, long paramLong, Integer paramInteger)
  {
    this.dateOnly = paramBoolean;
    this.value = paramLong;
    this.tzShift = paramInteger;
  }

  private static void appendInt(StringBuilder paramStringBuilder, int paramInt1, int paramInt2)
  {
    if (paramInt1 < 0)
    {
      StringBuilder localStringBuilder1 = paramStringBuilder.append('-');
      paramInt1 = -paramInt1;
    }
    int i = paramInt1;
    while (i > 0)
    {
      i /= 10;
      paramInt2 += -1;
    }
    int j = 0;
    while (j < paramInt2)
    {
      StringBuilder localStringBuilder2 = paramStringBuilder.append('0');
      j += 1;
    }
    if (paramInt1 == 0)
      return;
    StringBuilder localStringBuilder3 = paramStringBuilder.append(paramInt1);
  }

  public static DateTime parseRfc3339(String paramString)
    throws NumberFormatException
  {
    try
    {
      TimeZone localTimeZone = GMT;
      GregorianCalendar localGregorianCalendar = new GregorianCalendar(localTimeZone);
      String str1 = paramString;
      int i = 0;
      int j = 4;
      int k = Integer.parseInt(str1.substring(i, j));
      String str2 = paramString;
      int m = 5;
      int n = 7;
      int i1 = Integer.parseInt(str2.substring(m, n)) + -1;
      String str3 = paramString;
      int i2 = 8;
      int i3 = 10;
      int i4 = Integer.parseInt(str3.substring(i2, i3));
      int i5 = paramString.length();
      int i6 = 10;
      boolean bool;
      int i10;
      label155: Integer localInteger1;
      long l1;
      int i12;
      if (i5 > i6)
      {
        String str4 = paramString;
        int i7 = 10;
        int i8 = Character.toUpperCase(str4.charAt(i7));
        int i9 = 84;
        if (i8 == i9);
      }
      else
      {
        bool = true;
        if (!bool)
          break label240;
        localGregorianCalendar.set(k, i1, i4);
        i10 = 10;
        localInteger1 = null;
        l1 = localGregorianCalendar.getTimeInMillis();
        if (i5 > i10)
        {
          int i11 = Character.toUpperCase(paramString.charAt(i10));
          i11 = 90;
          if (i11 != i11)
            break label409;
          i12 = 0;
        }
      }
      while (true)
      {
        localInteger1 = Integer.valueOf(i12);
        DateTime localDateTime1 = new com/google/api/client/util/DateTime;
        DateTime localDateTime2 = localDateTime1;
        long l2 = l1;
        Integer localInteger2 = localInteger1;
        localDateTime2.<init>(bool, l2, localInteger2);
        return localDateTime1;
        bool = false;
        break;
        label240: String str5 = paramString;
        int i13 = 11;
        int i14 = 13;
        int i15 = Integer.parseInt(str5.substring(i13, i14));
        String str6 = paramString;
        int i16 = 14;
        int i17 = 16;
        int i18 = Integer.parseInt(str6.substring(i16, i17));
        String str7 = paramString;
        int i19 = 17;
        int i20 = 19;
        int i21 = Integer.parseInt(str7.substring(i19, i20));
        localGregorianCalendar.set(k, i1, i4, i15, i18, i21);
        String str8 = paramString;
        int i22 = 19;
        int i23 = str8.charAt(i22);
        i22 = 46;
        if (i23 == i22)
        {
          String str9 = paramString;
          int i24 = 20;
          int i25 = 23;
          int i26 = Integer.parseInt(str9.substring(i24, i25));
          int i27 = 14;
          localGregorianCalendar.set(i27, i26);
          i10 = 23;
          break label155;
        }
        i10 = 19;
        break label155;
        label409: int i28 = i10 + 1;
        int i29 = i10 + 3;
        String str10 = paramString;
        int i30 = i28;
        int i31 = i29;
        int i32 = Integer.parseInt(str10.substring(i30, i31)) * 60;
        int i33 = i10 + 4;
        int i34 = i10 + 6;
        String str11 = paramString;
        int i35 = i33;
        int i36 = i34;
        int i37 = Integer.parseInt(str11.substring(i35, i36));
        i12 = i32 + i37;
        int i38 = paramString.charAt(i10);
        int i39 = i38;
        i38 = 45;
        if (i39 == i38)
          i12 = -i12;
        long l3 = 60000 * i12;
        l1 -= l3;
      }
    }
    catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
    {
    }
    throw new NumberFormatException("Invalid date/time format.");
  }

  public boolean equals(Object paramObject)
  {
    boolean bool1 = true;
    if (paramObject == this);
    while (true)
    {
      return bool1;
      if (!(paramObject instanceof DateTime))
      {
        bool1 = false;
      }
      else
      {
        DateTime localDateTime = (DateTime)paramObject;
        boolean bool2 = this.dateOnly;
        boolean bool3 = localDateTime.dateOnly;
        if (bool2 != bool3)
        {
          long l1 = this.value;
          long l2 = localDateTime.value;
          if (l1 == l2);
        }
        else
        {
          bool1 = false;
        }
      }
    }
  }

  public String toString()
  {
    return toStringRfc3339();
  }

  public String toStringRfc3339()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    TimeZone localTimeZone = GMT;
    GregorianCalendar localGregorianCalendar = new GregorianCalendar(localTimeZone);
    long l1 = this.value;
    Integer localInteger = this.tzShift;
    if (localInteger != null)
    {
      long l2 = localInteger.longValue() * 60000L;
      l1 += l2;
    }
    localGregorianCalendar.setTimeInMillis(l1);
    int i = localGregorianCalendar.get(1);
    appendInt(localStringBuilder1, i, 4);
    StringBuilder localStringBuilder2 = localStringBuilder1.append('-');
    int j = localGregorianCalendar.get(2) + 1;
    appendInt(localStringBuilder1, j, 2);
    StringBuilder localStringBuilder3 = localStringBuilder1.append('-');
    int k = localGregorianCalendar.get(5);
    appendInt(localStringBuilder1, k, 2);
    if (!this.dateOnly)
    {
      StringBuilder localStringBuilder4 = localStringBuilder1.append('T');
      int m = localGregorianCalendar.get(11);
      appendInt(localStringBuilder1, m, 2);
      StringBuilder localStringBuilder5 = localStringBuilder1.append(':');
      int n = localGregorianCalendar.get(12);
      appendInt(localStringBuilder1, n, 2);
      StringBuilder localStringBuilder6 = localStringBuilder1.append(':');
      int i1 = localGregorianCalendar.get(13);
      appendInt(localStringBuilder1, i1, 2);
      if (localGregorianCalendar.isSet(14))
      {
        StringBuilder localStringBuilder7 = localStringBuilder1.append('.');
        int i2 = localGregorianCalendar.get(14);
        appendInt(localStringBuilder1, i2, 3);
      }
    }
    if (localInteger != null)
    {
      if (localInteger.intValue() == 0)
        StringBuilder localStringBuilder8 = localStringBuilder1.append('Z');
    }
    else
      return localStringBuilder1.toString();
    int i3 = localInteger.intValue();
    if (localInteger.intValue() > 0)
      StringBuilder localStringBuilder9 = localStringBuilder1.append('+');
    while (true)
    {
      int i4 = i3 / 60;
      int i5 = i3 % 60;
      appendInt(localStringBuilder1, i4, 2);
      StringBuilder localStringBuilder10 = localStringBuilder1.append(':');
      appendInt(localStringBuilder1, i5, 2);
      break;
      StringBuilder localStringBuilder11 = localStringBuilder1.append('-');
      i3 = -i3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.util.DateTime
 * JD-Core Version:    0.6.2
 */