package com.google.android.music.utils;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MatrixCursor.RowBuilder;
import android.text.TextUtils;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class DbUtils
{
  public static final StringBuilder addAndCondition(StringBuilder paramStringBuilder, String paramString)
  {
    if (TextUtils.isEmpty(paramString));
    while (true)
    {
      return paramStringBuilder;
      if (paramStringBuilder == null)
        paramStringBuilder = new StringBuilder();
      if (paramStringBuilder.length() == 0)
        StringBuilder localStringBuilder1 = paramStringBuilder.append(paramString);
      else
        StringBuilder localStringBuilder2 = paramStringBuilder.append(" AND ").append(paramString);
    }
  }

  public static void addRowToMatrixCursor(MatrixCursor paramMatrixCursor, Cursor paramCursor)
  {
    MatrixCursor.RowBuilder localRowBuilder1 = paramMatrixCursor.newRow();
    int i = paramMatrixCursor.getColumnCount();
    int j = 0;
    if (j >= i)
      return;
    if (paramCursor.isNull(j))
      MatrixCursor.RowBuilder localRowBuilder2 = localRowBuilder1.add(null);
    while (true)
    {
      j += 1;
      break;
      String str = paramCursor.getString(j);
      MatrixCursor.RowBuilder localRowBuilder3 = localRowBuilder1.add(str);
    }
  }

  public static StringBuffer appendIN(StringBuffer paramStringBuffer, Collection<Long> paramCollection)
  {
    if (paramCollection.isEmpty())
      throw new IllegalArgumentException("No values for IN operator");
    int i = (paramCollection.size() + 1) * 6;
    paramStringBuffer.ensureCapacity(i);
    StringBuffer localStringBuffer1 = paramStringBuffer.append(" IN (");
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      long l = ((Long)localIterator.next()).longValue();
      StringBuffer localStringBuffer2 = paramStringBuffer.append(l).append(',');
    }
    int j = paramStringBuffer.length() + -1;
    paramStringBuffer.setLength(j);
    StringBuffer localStringBuffer3 = paramStringBuffer.append(") ");
    return paramStringBuffer;
  }

  public static void appendIN(StringBuilder paramStringBuilder, Collection<Long> paramCollection)
  {
    if (paramCollection.isEmpty())
      throw new IllegalArgumentException("No values for IN operator");
    StringBuilder localStringBuilder1 = paramStringBuilder.append(" IN (");
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      long l = ((Long)localIterator.next()).longValue();
      StringBuilder localStringBuilder2 = paramStringBuilder.append(l).append(',');
    }
    int i = paramStringBuilder.length() + -1;
    paramStringBuilder.setLength(i);
    StringBuilder localStringBuilder3 = paramStringBuilder.append(") ");
  }

  public static void appendIN(StringBuilder paramStringBuilder, int[] paramArrayOfInt)
  {
    if ((paramArrayOfInt == null) || (paramArrayOfInt.length == 0))
      throw new IllegalArgumentException("No values for IN operator");
    StringBuilder localStringBuilder1 = paramStringBuilder.append(" IN (");
    int[] arrayOfInt = paramArrayOfInt;
    int i = arrayOfInt.length;
    int j = 0;
    while (j < i)
    {
      int k = arrayOfInt[j];
      StringBuilder localStringBuilder2 = paramStringBuilder.append(k).append(',');
      j += 1;
    }
    int m = paramStringBuilder.length() + -1;
    paramStringBuilder.setLength(m);
    StringBuilder localStringBuilder3 = paramStringBuilder.append(") ");
  }

  public static final String escapeForLikeOperator(String paramString, char paramChar)
  {
    int i = paramString.length();
    int j = i + 10;
    StringBuffer localStringBuffer1 = new StringBuffer(j);
    int k = 0;
    if (k < i)
    {
      char c = paramString.charAt(k);
      switch (c)
      {
      default:
        if (c != paramChar)
          StringBuffer localStringBuffer2 = localStringBuffer1.append(paramChar);
        break;
      case '%':
      case '_':
      }
      while (true)
      {
        StringBuffer localStringBuffer3 = localStringBuffer1.append(c);
        k += 1;
        break;
        StringBuffer localStringBuffer4 = localStringBuffer1.append(paramChar);
      }
    }
    return localStringBuffer1.toString();
  }

  public static int findIndirectlyReferencedItem(long paramLong1, long paramLong2, int paramInt1, Cursor paramCursor, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = paramCursor.getCount();
    int j;
    if (i < 1)
      j = -1;
    while (true)
    {
      return j;
      int k = paramInt1;
      j = 1;
      k = 1;
      if (i <= paramInt1)
      {
        j = 0;
        k = i + -1;
      }
      j = -1;
      if (paramCursor.moveToPosition(k))
      {
        int m = paramInt3;
        if (paramCursor.getLong(m) == paramLong2)
        {
          j = k;
          if (paramCursor.getLong(paramInt2) == paramLong1)
            continue;
        }
      }
      else
      {
        int n = 1;
        int i1 = paramInt4;
        if (n <= i1)
        {
          int i2;
          if (j != 0)
          {
            i2 = k + n;
            if (paramCursor.moveToPosition(i2))
            {
              int i3 = paramInt3;
              if (paramCursor.getLong(i3) == paramLong2)
              {
                if (paramCursor.getLong(paramInt2) == paramLong1)
                {
                  j = i2;
                  continue;
                }
                if (j == -1)
                  j = i2;
              }
            }
          }
          else
          {
            while (true)
              if (k != 0)
              {
                i2 = k - n;
                if (!paramCursor.moveToPosition(i2))
                  break label283;
                int i4 = paramInt3;
                if (paramCursor.getLong(i4) == paramLong2)
                {
                  if (paramCursor.getLong(paramInt2) == paramLong1)
                  {
                    j = i2;
                    break;
                    j = 0;
                    if (k != 0)
                      continue;
                    break;
                  }
                  if (j == -1)
                    j = i2;
                }
              }
            label283: 
            do
            {
              n += 1;
              break;
              k = 0;
            }
            while (j != 0);
          }
        }
      }
    }
  }

  public static int findItemInCursor(long paramLong, int paramInt1, Cursor paramCursor, int paramInt2, int paramInt3)
  {
    int i = paramCursor.getCount();
    int j;
    if (i < 1)
      j = -1;
    while (true)
    {
      return j;
      j = paramInt1;
      j = 1;
      int k = 1;
      if (i <= paramInt1)
      {
        j = 0;
        int m = i + -1;
      }
      if ((!paramCursor.moveToPosition(j)) || (paramCursor.getLong(paramInt2) != paramLong))
      {
        int n = 1;
        while (true)
        {
          if (n > paramInt3)
            break label211;
          if (j != 0)
          {
            int i1 = j + n;
            if (paramCursor.moveToPosition(i1))
            {
              if (paramCursor.getLong(paramInt2) != paramLong)
                break label141;
              j += n;
              break;
            }
            j = 0;
            if (k == 0)
            {
              j = -1;
              break;
            }
          }
          label141: if (k != 0)
          {
            int i2 = j - n;
            if (paramCursor.moveToPosition(i2))
            {
              if (paramCursor.getLong(paramInt2) != paramLong)
                break label202;
              j -= n;
              break;
            }
            k = 0;
            if (j == 0)
            {
              j = -1;
              break;
            }
          }
          label202: n += 1;
        }
        label211: j = -1;
      }
    }
  }

  public static String formatProjection(String[] paramArrayOfString, Map<String, String> paramMap)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      throw new IllegalArgumentException("Projection must not be empty");
    StringBuffer localStringBuffer1 = new StringBuffer();
    String[] arrayOfString = paramArrayOfString;
    int i = arrayOfString.length;
    int j = 0;
    if (j < i)
    {
      String str1 = arrayOfString[j];
      String str2 = (String)paramMap.get(str1);
      if ((str2 != null) && (str2.length() > 0))
        StringBuffer localStringBuffer2 = localStringBuffer1.append(str2);
      while (true)
      {
        StringBuffer localStringBuffer3 = localStringBuffer1.append(',');
        j += 1;
        break;
        StringBuffer localStringBuffer4 = localStringBuffer1.append(str1);
      }
    }
    int k = localStringBuffer1.length() + -1;
    localStringBuffer1.setLength(k);
    return localStringBuffer1.toString();
  }

  public static String getNotInClause(String paramString, Collection<Long> paramCollection)
  {
    StringBuilder localStringBuilder1;
    if ((paramCollection != null) && (!paramCollection.isEmpty()) && (!TextUtils.isEmpty(paramString)))
    {
      localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append(paramString).append(" NOT");
      appendIN(localStringBuilder1, paramCollection);
    }
    for (String str = localStringBuilder1.toString(); ; str = "")
      return str;
  }

  public static long getNullableLong(Cursor paramCursor, int paramInt, long paramLong)
  {
    if (paramCursor.isNull(paramInt));
    while (true)
    {
      return paramLong;
      paramLong = paramCursor.getLong(paramInt);
    }
  }

  public static boolean hasMore(Cursor paramCursor)
  {
    boolean bool = false;
    if ((paramCursor != null) && (!paramCursor.isAfterLast()))
      bool = true;
    return bool;
  }

  public static String[] injectColumnIntoProjection(String[] paramArrayOfString, String paramString)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0) || (TextUtils.isEmpty(paramString)))
      paramArrayOfString = null;
    while (true)
    {
      return paramArrayOfString;
      int i = 0;
      String[] arrayOfString1 = paramArrayOfString;
      int j = arrayOfString1.length;
      int k = 0;
      String[] arrayOfString2;
      while (true)
      {
        if (k < j)
        {
          String str1 = arrayOfString1[k];
          if (paramString.equals(str1))
            i = 1;
        }
        else
        {
          if (i != 0)
            break;
          arrayOfString2 = new String[paramArrayOfString.length + 1];
          int m = 0;
          while (true)
          {
            int n = paramArrayOfString.length;
            if (m >= n)
              break;
            String str2 = paramArrayOfString[m];
            arrayOfString2[m] = str2;
            m += 1;
          }
        }
        k += 1;
      }
      int i1 = arrayOfString2.length + -1;
      arrayOfString2[i1] = paramString;
      paramArrayOfString = arrayOfString2;
    }
  }

  public static boolean moveToNext(Cursor paramCursor)
  {
    if (paramCursor != null);
    for (boolean bool = paramCursor.moveToNext(); ; bool = false)
      return bool;
  }

  public static final String quoteStringValue(String paramString)
  {
    int i = paramString.length();
    int j = i + 2 + 12;
    StringBuffer localStringBuffer1 = new StringBuffer(j);
    StringBuffer localStringBuffer2 = localStringBuffer1.append('\'');
    int k = 0;
    while (k < i)
    {
      char c = paramString.charAt(k);
      StringBuffer localStringBuffer3 = localStringBuffer1.append(c);
      if (c == '\'')
        StringBuffer localStringBuffer4 = localStringBuffer1.append('\'');
      k += 1;
    }
    StringBuffer localStringBuffer5 = localStringBuffer1.append('\'');
    return localStringBuffer1.toString();
  }

  public static void stringAppendIN(StringBuilder paramStringBuilder, Collection<String> paramCollection)
  {
    if (paramCollection.isEmpty())
      throw new IllegalArgumentException("No values for IN operator");
    StringBuilder localStringBuilder1 = paramStringBuilder.append(" IN (");
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      String str = quoteStringValue((String)localIterator.next());
      StringBuilder localStringBuilder2 = paramStringBuilder.append(str).append(',');
    }
    int i = paramStringBuilder.length() + -1;
    paramStringBuilder.setLength(i);
    StringBuilder localStringBuilder3 = paramStringBuilder.append(") ");
  }

  public static class StringCursorHelper
    implements DbUtils.CursorHelper<String>
  {
    public void appendIN(StringBuilder paramStringBuilder, Collection<String> paramCollection)
    {
      DbUtils.stringAppendIN(paramStringBuilder, paramCollection);
    }

    public String getValue(Cursor paramCursor, int paramInt)
    {
      return paramCursor.getString(paramInt);
    }
  }

  public static class LongCursorHelper
    implements DbUtils.CursorHelper<Long>
  {
    public void appendIN(StringBuilder paramStringBuilder, Collection<Long> paramCollection)
    {
      DbUtils.appendIN(paramStringBuilder, paramCollection);
    }

    public Long getValue(Cursor paramCursor, int paramInt)
    {
      return Long.valueOf(paramCursor.getLong(paramInt));
    }
  }

  public static abstract interface CursorHelper<T>
  {
    public abstract void appendIN(StringBuilder paramStringBuilder, Collection<T> paramCollection);

    public abstract T getValue(Cursor paramCursor, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.DbUtils
 * JD-Core Version:    0.6.2
 */