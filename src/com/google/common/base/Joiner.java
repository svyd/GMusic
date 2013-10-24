package com.google.common.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Joiner
{
  private final String separator;

  private Joiner(String paramString)
  {
    String str = (String)Preconditions.checkNotNull(paramString);
    this.separator = str;
  }

  public static Joiner on(String paramString)
  {
    return new Joiner(paramString);
  }

  public <A extends Appendable> A appendTo(A paramA, Iterator<?> paramIterator)
    throws IOException
  {
    Object localObject1 = Preconditions.checkNotNull(paramA);
    if (paramIterator.hasNext())
    {
      Object localObject2 = paramIterator.next();
      CharSequence localCharSequence1 = toString(localObject2);
      Appendable localAppendable1 = paramA.append(localCharSequence1);
      while (paramIterator.hasNext())
      {
        String str = this.separator;
        Appendable localAppendable2 = paramA.append(str);
        Object localObject3 = paramIterator.next();
        CharSequence localCharSequence2 = toString(localObject3);
        Appendable localAppendable3 = paramA.append(localCharSequence2);
      }
    }
    return paramA;
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Iterable<?> paramIterable)
  {
    Iterator localIterator = paramIterable.iterator();
    return appendTo(paramStringBuilder, localIterator);
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Iterator<?> paramIterator)
  {
    try
    {
      Appendable localAppendable = appendTo(paramStringBuilder, paramIterator);
      return paramStringBuilder;
    }
    catch (IOException localIOException)
    {
      throw new AssertionError(localIOException);
    }
  }

  public final StringBuilder appendTo(StringBuilder paramStringBuilder, Object[] paramArrayOfObject)
  {
    List localList = Arrays.asList(paramArrayOfObject);
    return appendTo(paramStringBuilder, localList);
  }

  CharSequence toString(Object paramObject)
  {
    Object localObject1 = Preconditions.checkNotNull(paramObject);
    if ((paramObject instanceof CharSequence));
    for (Object localObject2 = (CharSequence)paramObject; ; localObject2 = paramObject.toString())
      return localObject2;
  }

  public MapJoiner withKeyValueSeparator(String paramString)
  {
    return new MapJoiner(this, paramString, null);
  }

  public static final class MapJoiner
  {
    private final Joiner joiner;
    private final String keyValueSeparator;

    private MapJoiner(Joiner paramJoiner, String paramString)
    {
      this.joiner = paramJoiner;
      String str = (String)Preconditions.checkNotNull(paramString);
      this.keyValueSeparator = str;
    }

    public <A extends Appendable> A appendTo(A paramA, Iterator<? extends Map.Entry<?, ?>> paramIterator)
      throws IOException
    {
      Object localObject1 = Preconditions.checkNotNull(paramA);
      if (paramIterator.hasNext())
      {
        Map.Entry localEntry1 = (Map.Entry)paramIterator.next();
        Joiner localJoiner1 = this.joiner;
        Object localObject2 = localEntry1.getKey();
        CharSequence localCharSequence1 = localJoiner1.toString(localObject2);
        Appendable localAppendable1 = paramA.append(localCharSequence1);
        String str1 = this.keyValueSeparator;
        Appendable localAppendable2 = paramA.append(str1);
        Joiner localJoiner2 = this.joiner;
        Object localObject3 = localEntry1.getValue();
        CharSequence localCharSequence2 = localJoiner2.toString(localObject3);
        Appendable localAppendable3 = paramA.append(localCharSequence2);
        while (paramIterator.hasNext())
        {
          String str2 = this.joiner.separator;
          Appendable localAppendable4 = paramA.append(str2);
          Map.Entry localEntry2 = (Map.Entry)paramIterator.next();
          Joiner localJoiner3 = this.joiner;
          Object localObject4 = localEntry2.getKey();
          CharSequence localCharSequence3 = localJoiner3.toString(localObject4);
          Appendable localAppendable5 = paramA.append(localCharSequence3);
          String str3 = this.keyValueSeparator;
          Appendable localAppendable6 = paramA.append(str3);
          Joiner localJoiner4 = this.joiner;
          Object localObject5 = localEntry2.getValue();
          CharSequence localCharSequence4 = localJoiner4.toString(localObject5);
          Appendable localAppendable7 = paramA.append(localCharSequence4);
        }
      }
      return paramA;
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Iterable<? extends Map.Entry<?, ?>> paramIterable)
    {
      Iterator localIterator = paramIterable.iterator();
      return appendTo(paramStringBuilder, localIterator);
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Iterator<? extends Map.Entry<?, ?>> paramIterator)
    {
      try
      {
        Appendable localAppendable = appendTo(paramStringBuilder, paramIterator);
        return paramStringBuilder;
      }
      catch (IOException localIOException)
      {
        throw new AssertionError(localIOException);
      }
    }

    public StringBuilder appendTo(StringBuilder paramStringBuilder, Map<?, ?> paramMap)
    {
      Set localSet = paramMap.entrySet();
      return appendTo(paramStringBuilder, localSet);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Joiner
 * JD-Core Version:    0.6.2
 */