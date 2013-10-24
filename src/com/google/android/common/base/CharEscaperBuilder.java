package com.google.android.common.base;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class CharEscaperBuilder
{
  private final Map<Character, String> map;
  private int max = -1;

  public CharEscaperBuilder()
  {
    HashMap localHashMap = new HashMap();
    this.map = localHashMap;
  }

  public CharEscaperBuilder addEscape(char paramChar, String paramString)
  {
    Map localMap = this.map;
    Character localCharacter = Character.valueOf(paramChar);
    Object localObject = localMap.put(localCharacter, paramString);
    char c = this.max;
    if (paramChar > c)
      this.max = paramChar;
    return this;
  }

  public char[][] toArray()
  {
    char[] arrayOfChar1 = new char[this.max + 1];
    Iterator localIterator = this.map.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      int i = ((Character)localEntry.getKey()).charValue();
      char[] arrayOfChar2 = ((String)localEntry.getValue()).toCharArray();
      arrayOfChar1[i] = arrayOfChar2;
    }
    return arrayOfChar1;
  }

  public CharEscaper toEscaper()
  {
    char[][] arrayOfChar = toArray();
    return new CharArrayDecorator(arrayOfChar);
  }

  private static class CharArrayDecorator extends CharEscaper
  {
    private final int replaceLength;
    private final char[][] replacements;

    CharArrayDecorator(char[][] paramArrayOfChar)
    {
      this.replacements = paramArrayOfChar;
      int i = paramArrayOfChar.length;
      this.replaceLength = i;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.base.CharEscaperBuilder
 * JD-Core Version:    0.6.2
 */