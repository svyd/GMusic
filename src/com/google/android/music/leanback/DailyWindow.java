package com.google.android.music.leanback;

import com.google.android.music.log.Log;
import java.util.Calendar;
import java.util.Random;

public class DailyWindow
{
  private static final boolean LOGV = LeanbackLog.LOGV;
  private final int mDurationInMillisec;
  private final long mFrequencyInMillisec;
  private final int mStartInMillisecSinceMidnight;
  private final int mVariableTriggerPercentage;

  private DailyWindow(int paramInt1, int paramInt2, long paramLong, int paramInt3)
  {
    this.mStartInMillisecSinceMidnight = paramInt1;
    this.mDurationInMillisec = paramInt2;
    this.mFrequencyInMillisec = paramLong;
    this.mVariableTriggerPercentage = paramInt3;
  }

  private static Calendar getMillsecSinceMidnight(long paramLong, int paramInt)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    localCalendar.set(11, 0);
    localCalendar.set(12, 0);
    localCalendar.set(13, 0);
    localCalendar.set(14, paramInt);
    return localCalendar;
  }

  private static long moveByDaysAndMillisec(Calendar paramCalendar, long paramLong)
  {
    int i = (int)(paramLong / 86400000L);
    if (i > 0)
      paramCalendar.add(6, i);
    int j = (int)(paramLong % 86400000L);
    if (j > 0)
      paramCalendar.add(14, j);
    return paramCalendar.getTimeInMillis();
  }

  private long moveByDuration(Calendar paramCalendar)
  {
    long l = this.mDurationInMillisec;
    return moveByDaysAndMillisec(paramCalendar, l);
  }

  private long moveByFrequency(Calendar paramCalendar)
  {
    long l = this.mFrequencyInMillisec;
    return moveByDaysAndMillisec(paramCalendar, l);
  }

  public long geFrequencyInMillisec()
  {
    return this.mFrequencyInMillisec;
  }

  long getTriggerTime(long paramLong)
  {
    long l1 = System.currentTimeMillis();
    long l2 = l1;
    if (paramLong > 0L)
    {
      Calendar localCalendar1 = Calendar.getInstance();
      long l3 = paramLong;
      localCalendar1.setTimeInMillis(l3);
      if (moveByFrequency(localCalendar1) < l1)
        long l4 = l1;
    }
    int i = this.mStartInMillisecSinceMidnight;
    Calendar localCalendar2 = getMillsecSinceMidnight(l2, i);
    long l5 = localCalendar2.getTimeInMillis();
    long l6 = moveByDuration(localCalendar2);
    if (l2 > l6)
    {
      localCalendar2.setTimeInMillis(l5);
      int j = 1;
      localCalendar2.add(6, j);
      l5 = localCalendar2.getTimeInMillis();
      l6 = moveByDuration(localCalendar2);
    }
    if (l2 > l6)
    {
      Exception localException = new Exception();
      Log.wtf("MusicLeanback", "Error in window calculation", localException);
    }
    if (this.mVariableTriggerPercentage > 0)
    {
      long l7 = l6 - l5;
      int k = (int)(this.mVariableTriggerPercentage * l7 / 100L);
      long l8 = new Random().nextInt(k);
      l2 = l5 + l8;
    }
    while (true)
    {
      if (l2 < l1)
        l2 = l1;
      return l2;
      if (l2 < l5)
        l2 = l5;
    }
  }

  public boolean isInSameWindow(long paramLong1, long paramLong2)
  {
    long l1;
    long l2;
    if (paramLong1 > paramLong2)
    {
      l1 = paramLong2;
      l2 = paramLong1;
      int i = this.mStartInMillisecSinceMidnight;
      Calendar localCalendar = getMillsecSinceMidnight(l1, i);
      long l3 = localCalendar.getTimeInMillis();
      long l4 = moveByDuration(localCalendar);
      if ((l1 < l3) || (l1 > l4) || (l2 < l3) || (l2 > l4))
        break label89;
    }
    label89: for (boolean bool = true; ; bool = false)
    {
      return bool;
      l1 = paramLong1;
      l2 = paramLong2;
      break;
    }
  }

  public boolean isInWindow(long paramLong)
  {
    int i = this.mStartInMillisecSinceMidnight;
    Calendar localCalendar = getMillsecSinceMidnight(paramLong, i);
    long l1 = localCalendar.getTimeInMillis();
    long l2 = moveByDuration(localCalendar);
    if ((paramLong >= l1) && (paramLong <= l2));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("Start: ");
    int i = this.mStartInMillisecSinceMidnight;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(i).append(". ");
    StringBuilder localStringBuilder4 = localStringBuilder1.append("Duration: ");
    int j = this.mDurationInMillisec;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(j).append(". ");
    StringBuilder localStringBuilder6 = localStringBuilder1.append("Frequency: ");
    long l = this.mFrequencyInMillisec;
    StringBuilder localStringBuilder7 = localStringBuilder6.append(l).append(". ");
    StringBuilder localStringBuilder8 = localStringBuilder1.append("Trigger %: ");
    int k = this.mVariableTriggerPercentage;
    StringBuilder localStringBuilder9 = localStringBuilder8.append(k).append(". ");
    return localStringBuilder1.toString();
  }

  public static class Builder
  {
    private final int mDurationInMillisec;
    private long mFrequencyInMillisec = 65535L;
    private final int mStartInMillisecSinceMidnight;
    private int mVariableTriggerPercentage = -1;

    private Builder(int paramInt1, int paramInt2)
      throws IllegalArgumentException
    {
      if ((paramInt1 < 0) || (paramInt1 > 86400000))
      {
        String str1 = "Invalid startInMillisecSinceMidnight:" + paramInt1;
        throw new IllegalArgumentException(str1);
      }
      if ((paramInt2 < 0) || (paramInt2 > 86400000))
      {
        String str2 = "Invalid durationInMillisec:" + paramInt2;
        throw new IllegalArgumentException(str2);
      }
      if (paramInt1 + paramInt2 > 86400000)
      {
        String str3 = "Combined start and duration are greater than a day. startInMillisecSinceMidnight: " + paramInt1 + ". durationInMillisec: " + paramInt2;
        Exception localException = new Exception();
        Log.wtf("MusicLeanback", str3, localException);
        paramInt2 = 86400000 - paramInt1;
      }
      this.mStartInMillisecSinceMidnight = paramInt1;
      this.mDurationInMillisec = paramInt2;
    }

    public static Builder startBuildingInMinutes(int paramInt1, int paramInt2)
    {
      if ((paramInt1 < 0) || (paramInt1 > 1440))
      {
        String str1 = "Invalid startInMinutesSinceMidnight:" + paramInt1;
        throw new IllegalArgumentException(str1);
      }
      if ((paramInt2 < 0) || (paramInt2 > 1440))
      {
        String str2 = "Invalid durationInMinutes:" + paramInt2;
        throw new IllegalArgumentException(str2);
      }
      int i = paramInt1 * 60000;
      int j = 60000 * paramInt2;
      return new Builder(i, j);
    }

    public DailyWindow build()
    {
      if (this.mFrequencyInMillisec == 65535L)
        this.mFrequencyInMillisec = 86400000L;
      int i = this.mStartInMillisecSinceMidnight;
      int j = this.mDurationInMillisec;
      long l = this.mFrequencyInMillisec;
      int k = this.mVariableTriggerPercentage;
      return new DailyWindow(i, j, l, k, null);
    }

    public Builder setFrequencyInMinutes(int paramInt)
    {
      long l = paramInt * 60000L;
      this.mFrequencyInMillisec = l;
      return this;
    }

    public Builder setVariableTriggerPercentage(int paramInt)
    {
      if ((paramInt < 0) || (paramInt > 100))
      {
        String str = "Invalid trigger percentage: " + paramInt;
        Exception localException = new Exception();
        Log.wtf("MusicLeanback", str, localException);
      }
      this.mVariableTriggerPercentage = paramInt;
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.leanback.DailyWindow
 * JD-Core Version:    0.6.2
 */