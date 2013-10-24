package com.google.common.base;

public final class Preconditions
{
  private static String badElementIndex(int paramInt1, int paramInt2, String paramString)
  {
    Object[] arrayOfObject1;
    if (paramInt1 < 0)
    {
      arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramString;
      Integer localInteger1 = Integer.valueOf(paramInt1);
      arrayOfObject1[1] = localInteger1;
    }
    Object[] arrayOfObject2;
    for (String str1 = format("%s (%s) must not be negative", arrayOfObject1); ; str1 = format("%s (%s) must be less than size (%s)", arrayOfObject2))
    {
      return str1;
      if (paramInt2 < 0)
      {
        String str2 = "negative size: " + paramInt2;
        throw new IllegalArgumentException(str2);
      }
      arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = paramString;
      Integer localInteger2 = Integer.valueOf(paramInt1);
      arrayOfObject2[1] = localInteger2;
      Integer localInteger3 = Integer.valueOf(paramInt2);
      arrayOfObject2[2] = localInteger3;
    }
  }

  private static String badPositionIndex(int paramInt1, int paramInt2, String paramString)
  {
    Object[] arrayOfObject1;
    if (paramInt1 < 0)
    {
      arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = paramString;
      Integer localInteger1 = Integer.valueOf(paramInt1);
      arrayOfObject1[1] = localInteger1;
    }
    Object[] arrayOfObject2;
    for (String str1 = format("%s (%s) must not be negative", arrayOfObject1); ; str1 = format("%s (%s) must not be greater than size (%s)", arrayOfObject2))
    {
      return str1;
      if (paramInt2 < 0)
      {
        String str2 = "negative size: " + paramInt2;
        throw new IllegalArgumentException(str2);
      }
      arrayOfObject2 = new Object[3];
      arrayOfObject2[0] = paramString;
      Integer localInteger2 = Integer.valueOf(paramInt1);
      arrayOfObject2[1] = localInteger2;
      Integer localInteger3 = Integer.valueOf(paramInt2);
      arrayOfObject2[2] = localInteger3;
    }
  }

  private static String badPositionIndexes(int paramInt1, int paramInt2, int paramInt3)
  {
    String str;
    if ((paramInt1 < 0) || (paramInt1 > paramInt3))
      str = badPositionIndex(paramInt1, paramInt3, "start index");
    while (true)
    {
      return str;
      if ((paramInt2 < 0) || (paramInt2 > paramInt3))
      {
        str = badPositionIndex(paramInt2, paramInt3, "end index");
      }
      else
      {
        Object[] arrayOfObject = new Object[2];
        Integer localInteger1 = Integer.valueOf(paramInt2);
        arrayOfObject[0] = localInteger1;
        Integer localInteger2 = Integer.valueOf(paramInt1);
        arrayOfObject[1] = localInteger2;
        str = format("end index (%s) must not be less than start index (%s)", arrayOfObject);
      }
    }
  }

  public static void checkArgument(boolean paramBoolean)
  {
    if (paramBoolean)
      return;
    throw new IllegalArgumentException();
  }

  public static void checkArgument(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean)
      return;
    String str = String.valueOf(paramObject);
    throw new IllegalArgumentException(str);
  }

  public static void checkArgument(boolean paramBoolean, String paramString, Object[] paramArrayOfObject)
  {
    if (paramBoolean)
      return;
    String str = format(paramString, paramArrayOfObject);
    throw new IllegalArgumentException(str);
  }

  public static int checkElementIndex(int paramInt1, int paramInt2)
  {
    return checkElementIndex(paramInt1, paramInt2, "index");
  }

  public static int checkElementIndex(int paramInt1, int paramInt2, String paramString)
  {
    if ((paramInt1 < 0) || (paramInt1 >= paramInt2))
    {
      String str = badElementIndex(paramInt1, paramInt2, paramString);
      throw new IndexOutOfBoundsException(str);
    }
    return paramInt1;
  }

  public static <T> T checkNotNull(T paramT)
  {
    if (paramT == null)
      throw new NullPointerException();
    return paramT;
  }

  public static <T> T checkNotNull(T paramT, Object paramObject)
  {
    if (paramT == null)
    {
      String str = String.valueOf(paramObject);
      throw new NullPointerException(str);
    }
    return paramT;
  }

  public static int checkPositionIndex(int paramInt1, int paramInt2)
  {
    return checkPositionIndex(paramInt1, paramInt2, "index");
  }

  public static int checkPositionIndex(int paramInt1, int paramInt2, String paramString)
  {
    if ((paramInt1 < 0) || (paramInt1 > paramInt2))
    {
      String str = badPositionIndex(paramInt1, paramInt2, paramString);
      throw new IndexOutOfBoundsException(str);
    }
    return paramInt1;
  }

  public static void checkPositionIndexes(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 >= 0) && (paramInt2 >= paramInt1) && (paramInt2 <= paramInt3))
      return;
    String str = badPositionIndexes(paramInt1, paramInt2, paramInt3);
    throw new IndexOutOfBoundsException(str);
  }

  public static void checkState(boolean paramBoolean)
  {
    if (paramBoolean)
      return;
    throw new IllegalStateException();
  }

  public static void checkState(boolean paramBoolean, Object paramObject)
  {
    if (paramBoolean)
      return;
    String str = String.valueOf(paramObject);
    throw new IllegalStateException(str);
  }

  public static void checkState(boolean paramBoolean, String paramString, Object[] paramArrayOfObject)
  {
    if (paramBoolean)
      return;
    String str = format(paramString, paramArrayOfObject);
    throw new IllegalStateException(str);
  }

  static String format(String paramString, Object[] paramArrayOfObject)
  {
    String str1 = String.valueOf(paramString);
    int i = str1.length();
    int j = paramArrayOfObject.length * 16;
    int k = i + j;
    StringBuilder localStringBuilder1 = new StringBuilder(k);
    int m = 0;
    int i8;
    for (int n = 0; ; n = i8)
    {
      int i1 = paramArrayOfObject.length;
      int i2;
      if (n < i1)
      {
        i2 = str1.indexOf("%s", m);
        if (i2 != -1);
      }
      else
      {
        String str2 = str1.substring(m);
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str2);
        int i3 = paramArrayOfObject.length;
        if (n >= i3)
          break label240;
        StringBuilder localStringBuilder3 = localStringBuilder1.append(" [");
        int i4 = n + 1;
        Object localObject1 = paramArrayOfObject[n];
        StringBuilder localStringBuilder4 = localStringBuilder1.append(localObject1);
        n = i4;
        while (true)
        {
          int i5 = paramArrayOfObject.length;
          if (n >= i5)
            break;
          StringBuilder localStringBuilder5 = localStringBuilder1.append(", ");
          int i6 = n + 1;
          Object localObject2 = paramArrayOfObject[n];
          StringBuilder localStringBuilder6 = localStringBuilder1.append(localObject2);
          int i7 = i6;
        }
      }
      String str3 = str1.substring(m, i2);
      StringBuilder localStringBuilder7 = localStringBuilder1.append(str3);
      i8 = n + 1;
      Object localObject3 = paramArrayOfObject[n];
      StringBuilder localStringBuilder8 = localStringBuilder1.append(localObject3);
      m = i2 + 2;
    }
    StringBuilder localStringBuilder9 = localStringBuilder1.append(']');
    label240: return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Preconditions
 * JD-Core Version:    0.6.2
 */