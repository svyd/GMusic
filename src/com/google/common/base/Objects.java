package com.google.common.base;

public final class Objects
{
  public static boolean equal(Object paramObject1, Object paramObject2)
  {
    if ((paramObject1 == paramObject2) || ((paramObject1 != null) && (paramObject1.equals(paramObject2))));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static <T> T firstNonNull(T paramT1, T paramT2)
  {
    if (paramT1 != null);
    while (true)
    {
      return paramT1;
      paramT1 = Preconditions.checkNotNull(paramT2);
    }
  }

  private static String simpleName(Class<?> paramClass)
  {
    String str = paramClass.getName().replaceAll("\\$[0-9]+", "\\$");
    int i = str.lastIndexOf('$');
    if (i == -1)
      i = str.lastIndexOf('.');
    int j = i + 1;
    return str.substring(j);
  }

  public static ToStringHelper toStringHelper(Object paramObject)
  {
    String str = simpleName(paramObject.getClass());
    return new ToStringHelper(str, null);
  }

  public static final class ToStringHelper
  {
    private final StringBuilder builder;
    private boolean needsSeparator = false;

    private ToStringHelper(String paramString)
    {
      Object localObject = Preconditions.checkNotNull(paramString);
      StringBuilder localStringBuilder = new StringBuilder(32).append(paramString).append('{');
      this.builder = localStringBuilder;
    }

    private StringBuilder checkNameAndAppend(String paramString)
    {
      Object localObject = Preconditions.checkNotNull(paramString);
      return maybeAppendSeparator().append(paramString).append('=');
    }

    private StringBuilder maybeAppendSeparator()
    {
      if (this.needsSeparator);
      for (StringBuilder localStringBuilder = this.builder.append(", "); ; localStringBuilder = this.builder)
      {
        return localStringBuilder;
        this.needsSeparator = true;
      }
    }

    public ToStringHelper add(String paramString, int paramInt)
    {
      StringBuilder localStringBuilder = checkNameAndAppend(paramString).append(paramInt);
      return this;
    }

    public ToStringHelper add(String paramString, Object paramObject)
    {
      StringBuilder localStringBuilder = checkNameAndAppend(paramString).append(paramObject);
      return this;
    }

    public ToStringHelper addValue(Object paramObject)
    {
      StringBuilder localStringBuilder = maybeAppendSeparator().append(paramObject);
      return this;
    }

    public String toString()
    {
      try
      {
        String str1 = '}';
        String str2 = str1;
        StringBuilder localStringBuilder1;
        int i;
        return str2;
      }
      finally
      {
        StringBuilder localStringBuilder2 = this.builder;
        int j = this.builder.length() + -1;
        localStringBuilder2.setLength(j);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.Objects
 * JD-Core Version:    0.6.2
 */