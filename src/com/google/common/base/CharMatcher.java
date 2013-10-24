package com.google.common.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public abstract class CharMatcher
{
  public static final CharMatcher ANY = new CharMatcher()
  {
    public boolean matches(char paramAnonymousChar)
    {
      return true;
    }

    public CharMatcher or(CharMatcher paramAnonymousCharMatcher)
    {
      Object localObject = Preconditions.checkNotNull(paramAnonymousCharMatcher);
      return this;
    }

    public CharMatcher precomputed()
    {
      return this;
    }
  };
  public static final CharMatcher ASCII;
  public static final CharMatcher BREAKING_WHITESPACE;
  public static final CharMatcher DIGIT;
  public static final CharMatcher INVISIBLE;
  public static final CharMatcher JAVA_DIGIT;
  public static final CharMatcher JAVA_ISO_CONTROL;
  public static final CharMatcher JAVA_LETTER;
  public static final CharMatcher JAVA_LETTER_OR_DIGIT;
  public static final CharMatcher JAVA_LOWER_CASE;
  public static final CharMatcher JAVA_UPPER_CASE;
  public static final CharMatcher NONE = new CharMatcher()
  {
    public boolean matches(char paramAnonymousChar)
    {
      return false;
    }

    public CharMatcher or(CharMatcher paramAnonymousCharMatcher)
    {
      return (CharMatcher)Preconditions.checkNotNull(paramAnonymousCharMatcher);
    }

    public CharMatcher precomputed()
    {
      return this;
    }

    void setBits(CharMatcher.LookupTable paramAnonymousLookupTable)
    {
    }
  };
  public static final CharMatcher SINGLE_WIDTH;
  public static final CharMatcher WHITESPACE;

  static
  {
    CharMatcher localCharMatcher1 = anyOf("\t\n\013\f\r     　 ᠎ ");
    CharMatcher localCharMatcher2 = inRange(' ', ' ');
    WHITESPACE = localCharMatcher1.or(localCharMatcher2).precomputed();
    CharMatcher localCharMatcher3 = anyOf("\t\n\013\f\r     　");
    CharMatcher localCharMatcher4 = inRange(' ', ' ');
    CharMatcher localCharMatcher5 = localCharMatcher3.or(localCharMatcher4);
    CharMatcher localCharMatcher6 = inRange(' ', ' ');
    BREAKING_WHITESPACE = localCharMatcher5.or(localCharMatcher6).precomputed();
    ASCII = inRange('\000', '');
    CharMatcher localCharMatcher7 = inRange('0', '9');
    char[] arrayOfChar = "٠۰߀०০੦૦୦௦౦೦൦๐໐༠၀႐០᠐᥆᧐᭐᮰᱀᱐꘠꣐꤀꩐０".toCharArray();
    int i = arrayOfChar.length;
    int j = 0;
    while (j < i)
    {
      char c1 = arrayOfChar[j];
      char c2 = (char)(c1 + '\t');
      CharMatcher localCharMatcher8 = inRange(c1, c2);
      localCharMatcher7 = localCharMatcher7.or(localCharMatcher8);
      j += 1;
    }
    DIGIT = localCharMatcher7.precomputed();
    JAVA_DIGIT = new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        return Character.isDigit(paramAnonymousChar);
      }
    };
    JAVA_LETTER = new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        return Character.isLetter(paramAnonymousChar);
      }
    };
    JAVA_LETTER_OR_DIGIT = new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        return Character.isLetterOrDigit(paramAnonymousChar);
      }
    };
    JAVA_UPPER_CASE = new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        return Character.isUpperCase(paramAnonymousChar);
      }
    };
    JAVA_LOWER_CASE = new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        return Character.isLowerCase(paramAnonymousChar);
      }
    };
    CharMatcher localCharMatcher9 = inRange('\000', '\037');
    CharMatcher localCharMatcher10 = inRange('', '');
    JAVA_ISO_CONTROL = localCharMatcher9.or(localCharMatcher10);
    CharMatcher localCharMatcher11 = inRange('\000', ' ');
    CharMatcher localCharMatcher12 = inRange('', ' ');
    CharMatcher localCharMatcher13 = localCharMatcher11.or(localCharMatcher12);
    CharMatcher localCharMatcher14 = is('­');
    CharMatcher localCharMatcher15 = localCharMatcher13.or(localCharMatcher14);
    CharMatcher localCharMatcher16 = inRange('؀', '؃');
    CharMatcher localCharMatcher17 = localCharMatcher15.or(localCharMatcher16);
    CharMatcher localCharMatcher18 = anyOf("۝܏ ឴឵᠎");
    CharMatcher localCharMatcher19 = localCharMatcher17.or(localCharMatcher18);
    CharMatcher localCharMatcher20 = inRange(' ', '‏');
    CharMatcher localCharMatcher21 = localCharMatcher19.or(localCharMatcher20);
    CharMatcher localCharMatcher22 = inRange(' ', ' ');
    CharMatcher localCharMatcher23 = localCharMatcher21.or(localCharMatcher22);
    CharMatcher localCharMatcher24 = inRange(' ', '⁤');
    CharMatcher localCharMatcher25 = localCharMatcher23.or(localCharMatcher24);
    CharMatcher localCharMatcher26 = inRange('⁪', '⁯');
    CharMatcher localCharMatcher27 = localCharMatcher25.or(localCharMatcher26);
    CharMatcher localCharMatcher28 = is('　');
    CharMatcher localCharMatcher29 = localCharMatcher27.or(localCharMatcher28);
    CharMatcher localCharMatcher30 = inRange(55296, 63743);
    CharMatcher localCharMatcher31 = localCharMatcher29.or(localCharMatcher30);
    CharMatcher localCharMatcher32 = anyOf("﻿￹￺￻");
    INVISIBLE = localCharMatcher31.or(localCharMatcher32).precomputed();
    CharMatcher localCharMatcher33 = inRange('\000', 'ӹ');
    CharMatcher localCharMatcher34 = is('־');
    CharMatcher localCharMatcher35 = localCharMatcher33.or(localCharMatcher34);
    CharMatcher localCharMatcher36 = inRange('א', 'ת');
    CharMatcher localCharMatcher37 = localCharMatcher35.or(localCharMatcher36);
    CharMatcher localCharMatcher38 = is('׳');
    CharMatcher localCharMatcher39 = localCharMatcher37.or(localCharMatcher38);
    CharMatcher localCharMatcher40 = is('״');
    CharMatcher localCharMatcher41 = localCharMatcher39.or(localCharMatcher40);
    CharMatcher localCharMatcher42 = inRange('؀', 'ۿ');
    CharMatcher localCharMatcher43 = localCharMatcher41.or(localCharMatcher42);
    CharMatcher localCharMatcher44 = inRange('ݐ', 'ݿ');
    CharMatcher localCharMatcher45 = localCharMatcher43.or(localCharMatcher44);
    CharMatcher localCharMatcher46 = inRange('฀', '๿');
    CharMatcher localCharMatcher47 = localCharMatcher45.or(localCharMatcher46);
    CharMatcher localCharMatcher48 = inRange('Ḁ', '₯');
    CharMatcher localCharMatcher49 = localCharMatcher47.or(localCharMatcher48);
    CharMatcher localCharMatcher50 = inRange('℀', '℺');
    CharMatcher localCharMatcher51 = localCharMatcher49.or(localCharMatcher50);
    CharMatcher localCharMatcher52 = inRange(64336, 65023);
    CharMatcher localCharMatcher53 = localCharMatcher51.or(localCharMatcher52);
    CharMatcher localCharMatcher54 = inRange(65136, 65279);
    CharMatcher localCharMatcher55 = localCharMatcher53.or(localCharMatcher54);
    CharMatcher localCharMatcher56 = inRange(65377, 65500);
    SINGLE_WIDTH = localCharMatcher55.or(localCharMatcher56).precomputed();
  }

  public static CharMatcher anyOf(CharSequence paramCharSequence)
  {
    Object localObject;
    switch (paramCharSequence.length())
    {
    default:
      char[] arrayOfChar = paramCharSequence.toString().toCharArray();
      Arrays.sort(arrayOfChar);
      localObject = new CharMatcher()
      {
        public boolean matches(char paramAnonymousChar)
        {
          if (Arrays.binarySearch(CharMatcher.this, paramAnonymousChar) >= 0);
          for (boolean bool = true; ; bool = false)
            return bool;
        }

        void setBits(CharMatcher.LookupTable paramAnonymousLookupTable)
        {
          char[] arrayOfChar = CharMatcher.this;
          int i = arrayOfChar.length;
          int j = 0;
          while (true)
          {
            if (j >= i)
              return;
            char c = arrayOfChar[j];
            paramAnonymousLookupTable.set(c);
            j += 1;
          }
        }
      };
    case 0:
    case 1:
    case 2:
    }
    while (true)
    {
      return localObject;
      localObject = NONE;
      continue;
      localObject = is(paramCharSequence.charAt(0));
      continue;
      char c1 = paramCharSequence.charAt(0);
      final char c2 = paramCharSequence.charAt(1);
      localObject = new CharMatcher()
      {
        public boolean matches(char paramAnonymousChar)
        {
          char c1 = this.val$match1;
          if (paramAnonymousChar != c1)
          {
            char c2 = c2;
            if (paramAnonymousChar == c2)
              break label26;
          }
          label26: for (boolean bool = true; ; bool = false)
            return bool;
        }

        public CharMatcher precomputed()
        {
          return this;
        }

        void setBits(CharMatcher.LookupTable paramAnonymousLookupTable)
        {
          char c1 = this.val$match1;
          paramAnonymousLookupTable.set(c1);
          char c2 = c2;
          paramAnonymousLookupTable.set(c2);
        }
      };
    }
  }

  public static CharMatcher inRange(char paramChar1, final char paramChar2)
  {
    if (paramChar2 >= paramChar1);
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool);
      return new CharMatcher()
      {
        public boolean matches(char paramAnonymousChar)
        {
          if (this.val$startInclusive <= paramAnonymousChar)
          {
            char c = paramChar2;
            if (paramAnonymousChar > c);
          }
          for (boolean bool = true; ; bool = false)
            return bool;
        }

        public CharMatcher precomputed()
        {
          return this;
        }

        void setBits(CharMatcher.LookupTable paramAnonymousLookupTable)
        {
          char c2;
          for (char c1 = this.val$startInclusive; ; c1 = c2)
          {
            paramAnonymousLookupTable.set(c1);
            c2 = (char)(c1 + '\001');
            char c3 = paramChar2;
            if (c1 != c3)
              return;
          }
        }
      };
    }
  }

  public static CharMatcher is(char paramChar)
  {
    return new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        char c = this.val$match;
        if (paramAnonymousChar != c);
        for (boolean bool = true; ; bool = false)
          return bool;
      }

      public CharMatcher or(CharMatcher paramAnonymousCharMatcher)
      {
        char c = this.val$match;
        if (paramAnonymousCharMatcher.matches(c));
        while (true)
        {
          return paramAnonymousCharMatcher;
          paramAnonymousCharMatcher = super.or(paramAnonymousCharMatcher);
        }
      }

      public CharMatcher precomputed()
      {
        return this;
      }

      void setBits(CharMatcher.LookupTable paramAnonymousLookupTable)
      {
        char c = this.val$match;
        paramAnonymousLookupTable.set(c);
      }
    };
  }

  public abstract boolean matches(char paramChar);

  public CharMatcher or(CharMatcher paramCharMatcher)
  {
    CharMatcher[] arrayOfCharMatcher = new CharMatcher[2];
    arrayOfCharMatcher[0] = this;
    CharMatcher localCharMatcher = (CharMatcher)Preconditions.checkNotNull(paramCharMatcher);
    arrayOfCharMatcher[1] = localCharMatcher;
    List localList = Arrays.asList(arrayOfCharMatcher);
    return new Or(localList);
  }

  public CharMatcher precomputed()
  {
    return Platform.precomputeCharMatcher(this);
  }

  CharMatcher precomputedInternal()
  {
    final LookupTable localLookupTable = new LookupTable(null);
    setBits(localLookupTable);
    return new CharMatcher()
    {
      public boolean matches(char paramAnonymousChar)
      {
        return localLookupTable.get(paramAnonymousChar);
      }

      public CharMatcher precomputed()
      {
        return this;
      }
    };
  }

  void setBits(LookupTable paramLookupTable)
  {
    int j;
    for (int i = 0; ; i = j)
    {
      if (matches(i))
        paramLookupTable.set(i);
      j = (char)(i + 1);
      if (i == 65535)
        return;
    }
  }

  private static final class LookupTable
  {
    int[] data;

    private LookupTable()
    {
      int[] arrayOfInt = new int[2048];
      this.data = arrayOfInt;
    }

    boolean get(char paramChar)
    {
      char c = '\001';
      int[] arrayOfInt = this.data;
      int i = paramChar >> '\005';
      int j = arrayOfInt[i];
      int k = c << paramChar;
      if ((j & k) != 0);
      while (true)
      {
        return c;
        c = '\000';
      }
    }

    void set(char paramChar)
    {
      int[] arrayOfInt = this.data;
      int i = paramChar >> '\005';
      int j = arrayOfInt[i];
      int k = '\001' << paramChar;
      int m = j | k;
      arrayOfInt[i] = m;
    }
  }

  private static class Or extends CharMatcher
  {
    List<CharMatcher> components;

    Or(List<CharMatcher> paramList)
    {
      this.components = paramList;
    }

    public boolean matches(char paramChar)
    {
      Iterator localIterator = this.components.iterator();
      do
        if (!localIterator.hasNext())
          break;
      while (!((CharMatcher)localIterator.next()).matches(paramChar));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public CharMatcher or(CharMatcher paramCharMatcher)
    {
      List localList = this.components;
      ArrayList localArrayList = new ArrayList(localList);
      Object localObject = Preconditions.checkNotNull(paramCharMatcher);
      boolean bool = localArrayList.add(localObject);
      return new Or(localArrayList);
    }

    void setBits(CharMatcher.LookupTable paramLookupTable)
    {
      Iterator localIterator = this.components.iterator();
      while (true)
      {
        if (!localIterator.hasNext())
          return;
        ((CharMatcher)localIterator.next()).setBits(paramLookupTable);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.CharMatcher
 * JD-Core Version:    0.6.2
 */