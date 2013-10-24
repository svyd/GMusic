package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

final class SortedLists
{
  public static <E> int binarySearch(List<? extends E> paramList, E paramE, Comparator<? super E> paramComparator, KeyPresentBehavior paramKeyPresentBehavior, KeyAbsentBehavior paramKeyAbsentBehavior)
  {
    Object localObject1 = Preconditions.checkNotNull(paramComparator);
    Object localObject2 = Preconditions.checkNotNull(paramList);
    Object localObject3 = Preconditions.checkNotNull(paramKeyPresentBehavior);
    Object localObject4 = Preconditions.checkNotNull(paramKeyAbsentBehavior);
    if (!(paramList instanceof RandomAccess))
      paramList = Lists.newArrayList(paramList);
    int i = 0;
    int j = paramList.size() + -1;
    List localList;
    int i1;
    while (i <= j)
    {
      int k = i + j >>> 1;
      Object localObject5 = paramList.get(k);
      int m = paramComparator.compare(paramE, localObject5);
      if (m < 0)
      {
        j = k + -1;
      }
      else if (m > 0)
      {
        i = k + 1;
      }
      else
      {
        int n = j + 1;
        localList = paramList.subList(i, n);
        i1 = k - i;
      }
    }
    for (int i2 = paramKeyPresentBehavior.resultIndex(paramComparator, paramE, localList, i1) + i; ; i2 = paramKeyAbsentBehavior.resultIndex(i))
      return i2;
  }

  public static abstract enum KeyAbsentBehavior
  {
    static
    {
      // Byte code:
      //   0: new 7	com/google/common/collect/SortedLists$KeyAbsentBehavior$1
      //   3: dup
      //   4: ldc 24
      //   6: iconst_0
      //   7: invokespecial 28	com/google/common/collect/SortedLists$KeyAbsentBehavior$1:<init>	(Ljava/lang/String;I)V
      //   10: putstatic 30	com/google/common/collect/SortedLists$KeyAbsentBehavior:NEXT_LOWER	Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   13: new 9	com/google/common/collect/SortedLists$KeyAbsentBehavior$2
      //   16: dup
      //   17: ldc 31
      //   19: iconst_1
      //   20: invokespecial 32	com/google/common/collect/SortedLists$KeyAbsentBehavior$2:<init>	(Ljava/lang/String;I)V
      //   23: putstatic 34	com/google/common/collect/SortedLists$KeyAbsentBehavior:NEXT_HIGHER	Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   26: new 11	com/google/common/collect/SortedLists$KeyAbsentBehavior$3
      //   29: dup
      //   30: ldc 35
      //   32: iconst_2
      //   33: invokespecial 36	com/google/common/collect/SortedLists$KeyAbsentBehavior$3:<init>	(Ljava/lang/String;I)V
      //   36: putstatic 38	com/google/common/collect/SortedLists$KeyAbsentBehavior:INVERTED_INSERTION_INDEX	Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   39: iconst_3
      //   40: anewarray 2	com/google/common/collect/SortedLists$KeyAbsentBehavior
      //   43: astore_0
      //   44: getstatic 30	com/google/common/collect/SortedLists$KeyAbsentBehavior:NEXT_LOWER	Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   47: astore_1
      //   48: aload_0
      //   49: iconst_0
      //   50: aload_1
      //   51: aastore
      //   52: getstatic 34	com/google/common/collect/SortedLists$KeyAbsentBehavior:NEXT_HIGHER	Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   55: astore_2
      //   56: aload_0
      //   57: iconst_1
      //   58: aload_2
      //   59: aastore
      //   60: getstatic 38	com/google/common/collect/SortedLists$KeyAbsentBehavior:INVERTED_INSERTION_INDEX	Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   63: astore_3
      //   64: aload_0
      //   65: iconst_2
      //   66: aload_3
      //   67: aastore
      //   68: aload_0
      //   69: putstatic 40	com/google/common/collect/SortedLists$KeyAbsentBehavior:$VALUES	[Lcom/google/common/collect/SortedLists$KeyAbsentBehavior;
      //   72: return
    }

    abstract <E> int resultIndex(int paramInt);
  }

  public static abstract enum KeyPresentBehavior
  {
    static
    {
      // Byte code:
      //   0: new 7	com/google/common/collect/SortedLists$KeyPresentBehavior$1
      //   3: dup
      //   4: ldc 30
      //   6: iconst_0
      //   7: invokespecial 34	com/google/common/collect/SortedLists$KeyPresentBehavior$1:<init>	(Ljava/lang/String;I)V
      //   10: putstatic 36	com/google/common/collect/SortedLists$KeyPresentBehavior:ANY_PRESENT	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   13: new 9	com/google/common/collect/SortedLists$KeyPresentBehavior$2
      //   16: dup
      //   17: ldc 37
      //   19: iconst_1
      //   20: invokespecial 38	com/google/common/collect/SortedLists$KeyPresentBehavior$2:<init>	(Ljava/lang/String;I)V
      //   23: putstatic 40	com/google/common/collect/SortedLists$KeyPresentBehavior:LAST_PRESENT	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   26: new 11	com/google/common/collect/SortedLists$KeyPresentBehavior$3
      //   29: dup
      //   30: ldc 41
      //   32: iconst_2
      //   33: invokespecial 42	com/google/common/collect/SortedLists$KeyPresentBehavior$3:<init>	(Ljava/lang/String;I)V
      //   36: putstatic 44	com/google/common/collect/SortedLists$KeyPresentBehavior:FIRST_PRESENT	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   39: new 13	com/google/common/collect/SortedLists$KeyPresentBehavior$4
      //   42: dup
      //   43: ldc 45
      //   45: iconst_3
      //   46: invokespecial 46	com/google/common/collect/SortedLists$KeyPresentBehavior$4:<init>	(Ljava/lang/String;I)V
      //   49: putstatic 48	com/google/common/collect/SortedLists$KeyPresentBehavior:FIRST_AFTER	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   52: new 15	com/google/common/collect/SortedLists$KeyPresentBehavior$5
      //   55: dup
      //   56: ldc 49
      //   58: iconst_4
      //   59: invokespecial 50	com/google/common/collect/SortedLists$KeyPresentBehavior$5:<init>	(Ljava/lang/String;I)V
      //   62: putstatic 52	com/google/common/collect/SortedLists$KeyPresentBehavior:LAST_BEFORE	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   65: iconst_5
      //   66: anewarray 2	com/google/common/collect/SortedLists$KeyPresentBehavior
      //   69: astore_0
      //   70: getstatic 36	com/google/common/collect/SortedLists$KeyPresentBehavior:ANY_PRESENT	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   73: astore_1
      //   74: aload_0
      //   75: iconst_0
      //   76: aload_1
      //   77: aastore
      //   78: getstatic 40	com/google/common/collect/SortedLists$KeyPresentBehavior:LAST_PRESENT	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   81: astore_2
      //   82: aload_0
      //   83: iconst_1
      //   84: aload_2
      //   85: aastore
      //   86: getstatic 44	com/google/common/collect/SortedLists$KeyPresentBehavior:FIRST_PRESENT	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   89: astore_3
      //   90: aload_0
      //   91: iconst_2
      //   92: aload_3
      //   93: aastore
      //   94: getstatic 48	com/google/common/collect/SortedLists$KeyPresentBehavior:FIRST_AFTER	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   97: astore 4
      //   99: aload_0
      //   100: iconst_3
      //   101: aload 4
      //   103: aastore
      //   104: getstatic 52	com/google/common/collect/SortedLists$KeyPresentBehavior:LAST_BEFORE	Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   107: astore 5
      //   109: aload_0
      //   110: iconst_4
      //   111: aload 5
      //   113: aastore
      //   114: aload_0
      //   115: putstatic 54	com/google/common/collect/SortedLists$KeyPresentBehavior:$VALUES	[Lcom/google/common/collect/SortedLists$KeyPresentBehavior;
      //   118: return
    }

    abstract <E> int resultIndex(Comparator<? super E> paramComparator, E paramE, List<? extends E> paramList, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.SortedLists
 * JD-Core Version:    0.6.2
 */