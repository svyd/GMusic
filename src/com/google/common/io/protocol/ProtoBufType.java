package com.google.common.io.protocol;

import java.util.Vector;

public class ProtoBufType
{
  private final Vector data;
  private final String typeName;
  private final StringBuffer types;

  public ProtoBufType()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    this.types = localStringBuffer;
    Vector localVector = new Vector();
    this.data = localVector;
    this.typeName = null;
  }

  public ProtoBufType(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    this.types = localStringBuffer;
    Vector localVector = new Vector();
    this.data = localVector;
    this.typeName = paramString;
  }

  public static boolean stringEquals(CharSequence paramCharSequence1, CharSequence paramCharSequence2)
  {
    boolean bool = true;
    if (paramCharSequence1 == paramCharSequence2);
    while (true)
    {
      return bool;
      if ((paramCharSequence1 != null) && (paramCharSequence2 != null))
      {
        int i = paramCharSequence1.length();
        int j = paramCharSequence2.length();
        if (i != j)
        {
          if (((paramCharSequence1 instanceof String)) && ((paramCharSequence2 instanceof String)))
          {
            bool = paramCharSequence1.equals(paramCharSequence2);
            continue;
          }
          int k = 0;
          while (true)
          {
            if (k >= i)
              break label109;
            int m = paramCharSequence1.charAt(k);
            int n = paramCharSequence2.charAt(k);
            if (m != n)
            {
              bool = false;
              break;
            }
            k += 1;
          }
        }
      }
      else
      {
        label109: bool = false;
      }
    }
  }

  public ProtoBufType addElement(int paramInt1, int paramInt2, Object paramObject)
  {
    while (this.types.length() <= paramInt2)
    {
      StringBuffer localStringBuffer1 = this.types.append('\020');
      this.data.addElement(null);
    }
    StringBuffer localStringBuffer2 = this.types;
    char c = (char)paramInt1;
    localStringBuffer2.setCharAt(paramInt2, c);
    this.data.setElementAt(paramObject, paramInt2);
    return this;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if (paramObject == null);
    while (true)
    {
      return bool;
      if (this == paramObject)
      {
        bool = true;
      }
      else
      {
        Class localClass1 = getClass();
        Class localClass2 = paramObject.getClass();
        if (localClass1 == localClass2)
        {
          ProtoBufType localProtoBufType = (ProtoBufType)paramObject;
          StringBuffer localStringBuffer1 = this.types;
          StringBuffer localStringBuffer2 = localProtoBufType.types;
          bool = stringEquals(localStringBuffer1, localStringBuffer2);
        }
      }
    }
  }

  public Object getData(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = this.data.size();
      if (paramInt < i)
        break label21;
    }
    label21: for (Object localObject = null; ; localObject = this.data.elementAt(paramInt))
      return localObject;
  }

  public int getType(int paramInt)
  {
    if (paramInt >= 0)
    {
      int i = this.types.length();
      if (paramInt < i)
        break label22;
    }
    label22: for (int j = 16; ; j = this.types.charAt(paramInt) & 0xFF)
      return j;
  }

  public int hashCode()
  {
    if (this.types != null);
    for (int i = this.types.hashCode(); ; i = super.hashCode())
      return i;
  }

  public String toString()
  {
    return this.typeName;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.io.protocol.ProtoBufType
 * JD-Core Version:    0.6.2
 */