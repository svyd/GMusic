package com.google.android.music;

import android.util.Log;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.TreeSet;

public class StrictShuffler
{
  private LinkedList<Integer> mHistoryOfNumbers;
  private int mHistorySize;
  private final int mMaxHistorySize;
  private TreeSet<Integer> mPreviousNumbers;
  private Random mRandom;

  public StrictShuffler()
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    this.mHistoryOfNumbers = localLinkedList;
    TreeSet localTreeSet = Sets.newTreeSet();
    this.mPreviousNumbers = localTreeSet;
    this.mHistorySize = 1;
    Random localRandom = new Random();
    this.mRandom = localRandom;
    this.mMaxHistorySize = 200;
  }

  public StrictShuffler(int paramInt)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    this.mHistoryOfNumbers = localLinkedList;
    TreeSet localTreeSet = Sets.newTreeSet();
    this.mPreviousNumbers = localTreeSet;
    this.mHistorySize = 1;
    Random localRandom = new Random();
    this.mRandom = localRandom;
    this.mMaxHistorySize = paramInt;
  }

  private void cleanUpHistory()
  {
    if (this.mHistoryOfNumbers.isEmpty())
      return;
    int i = this.mHistoryOfNumbers.size();
    int j = this.mHistorySize;
    if (i < j)
      return;
    int k = 0;
    while (true)
    {
      int m = this.mHistorySize / 2;
      int n = Math.max(1, m);
      if (k >= n)
        return;
      TreeSet localTreeSet = this.mPreviousNumbers;
      Object localObject = this.mHistoryOfNumbers.removeFirst();
      boolean bool = localTreeSet.remove(localObject);
      k += 1;
    }
  }

  private int getNextAvailableInt(int paramInt)
  {
    int i = 0;
    Iterator localIterator = this.mPreviousNumbers.iterator();
    while (localIterator.hasNext())
    {
      if (((Integer)localIterator.next()).intValue() == i)
        break label57;
      i += 1;
    }
    if (i >= paramInt)
      throw new IllegalArgumentException("No valid return");
    label57: return i;
  }

  public void injectHistoricalValue(int paramInt)
  {
    TreeSet localTreeSet = this.mPreviousNumbers;
    Integer localInteger1 = Integer.valueOf(paramInt);
    boolean bool1 = localTreeSet.add(localInteger1);
    LinkedList localLinkedList = this.mHistoryOfNumbers;
    Integer localInteger2 = Integer.valueOf(paramInt);
    boolean bool2 = localLinkedList.add(localInteger2);
    cleanUpHistory();
  }

  public int nextInt(int paramInt)
  {
    int i = this.mHistorySize;
    if (paramInt < i)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Given range (").append(paramInt).append(") must be larger than the assigned history size: ");
      int j = this.mHistorySize;
      String str = j;
      throw new IllegalArgumentException(str);
    }
    cleanUpHistory();
    int k = 0;
    int m = this.mRandom.nextInt(paramInt);
    TreeSet localTreeSet1 = this.mPreviousNumbers;
    Integer localInteger1 = Integer.valueOf(m);
    if (!localTreeSet1.contains(localInteger1))
    {
      TreeSet localTreeSet2 = this.mPreviousNumbers;
      Integer localInteger2 = Integer.valueOf(m);
      boolean bool1 = localTreeSet2.add(localInteger2);
      LinkedList localLinkedList1 = this.mHistoryOfNumbers;
      Integer localInteger3 = Integer.valueOf(m);
      boolean bool2 = localLinkedList1.add(localInteger3);
    }
    int i2;
    for (int n = m; ; n = i2)
    {
      return n;
      k += 1;
      if (k <= 20)
        break;
      int i1 = Log.w("StrictShuffler", "Too many iterations to get a non-repeated random number.  Returning next available integer");
      i2 = getNextAvailableInt(paramInt);
      TreeSet localTreeSet3 = this.mPreviousNumbers;
      Integer localInteger4 = Integer.valueOf(i2);
      boolean bool3 = localTreeSet3.add(localInteger4);
      LinkedList localLinkedList2 = this.mHistoryOfNumbers;
      Integer localInteger5 = Integer.valueOf(i2);
      boolean bool4 = localLinkedList2.add(localInteger5);
    }
  }

  public void setHistorySize(int paramInt)
  {
    this.mHistoryOfNumbers.clear();
    this.mPreviousNumbers.clear();
    int i = this.mMaxHistorySize;
    if (paramInt > i)
    {
      int j = this.mMaxHistorySize;
      this.mHistorySize = j;
      return;
    }
    this.mHistorySize = paramInt;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.StrictShuffler
 * JD-Core Version:    0.6.2
 */