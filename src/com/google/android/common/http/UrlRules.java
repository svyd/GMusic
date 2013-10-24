package com.google.android.common.http;

import android.content.ContentResolver;
import android.util.Log;
import com.google.android.gsf.Gservices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlRules
{
  private static final Pattern PATTERN_SPACE_PLUS = Pattern.compile(" +");
  private static final Pattern RULE_PATTERN = Pattern.compile("\\W");
  private static UrlRules sCachedRules = new UrlRules(arrayOfRule);
  private static Object sCachedVersionToken;
  private final Pattern mPattern;
  private final Rule[] mRules;

  static
  {
    Rule[] arrayOfRule = new Rule[0];
  }

  public UrlRules(Rule[] paramArrayOfRule)
  {
    Arrays.sort(paramArrayOfRule);
    StringBuilder localStringBuilder1 = new StringBuilder("(");
    int i = 0;
    while (true)
    {
      int j = paramArrayOfRule.length;
      if (i >= j)
        break;
      if (i > 0)
        StringBuilder localStringBuilder2 = localStringBuilder1.append(")|(");
      Pattern localPattern1 = RULE_PATTERN;
      String str1 = paramArrayOfRule[i].mPrefix;
      String str2 = localPattern1.matcher(str1).replaceAll("\\\\$0");
      StringBuilder localStringBuilder3 = localStringBuilder1.append(str2);
      i += 1;
    }
    Pattern localPattern2 = Pattern.compile(")");
    this.mPattern = localPattern2;
    this.mRules = paramArrayOfRule;
  }

  /** @deprecated */
  public static UrlRules getRules(ContentResolver paramContentResolver)
  {
    while (true)
    {
      Object localObject1;
      ArrayList localArrayList;
      try
      {
        localObject1 = Gservices.getVersionToken(paramContentResolver);
        Object localObject2 = sCachedVersionToken;
        if (localObject1 == localObject2)
        {
          if (Log.isLoggable("UrlRules", 2))
          {
            String str1 = "Using cached rules, versionToken: " + localObject1;
            int i = Log.v("UrlRules", str1);
          }
          localUrlRules = sCachedRules;
          return localUrlRules;
        }
        if (Log.isLoggable("UrlRules", 2))
          int j = Log.v("UrlRules", "Scanning for Gservices \"url:*\" rules");
        String[] arrayOfString = new String[1];
        arrayOfString[0] = "url:";
        Map localMap = Gservices.getStringsByPrefix(paramContentResolver, arrayOfString);
        localArrayList = new ArrayList();
        Iterator localIterator = localMap.entrySet().iterator();
        if (localIterator.hasNext())
        {
          Map.Entry localEntry = (Map.Entry)localIterator.next();
          try
          {
            String str2 = ((String)localEntry.getKey()).substring(4);
            String str3 = (String)localEntry.getValue();
            if ((str3 == null) || (str3.length() == 0))
              continue;
            if (Log.isLoggable("UrlRules", 2))
            {
              String str4 = "  Rule " + str2 + ": " + str3;
              int k = Log.v("UrlRules", str4);
            }
            Rule localRule = new Rule(str2, str3);
            boolean bool = localArrayList.add(localRule);
          }
          catch (RuleFormatException localRuleFormatException)
          {
            int m = Log.e("UrlRules", "Invalid rule from Gservices", localRuleFormatException);
          }
          continue;
        }
      }
      finally
      {
      }
      Rule[] arrayOfRule1 = new Rule[localArrayList.size()];
      Rule[] arrayOfRule2 = (Rule[])localArrayList.toArray(arrayOfRule1);
      sCachedRules = new UrlRules(arrayOfRule2);
      sCachedVersionToken = localObject1;
      if (Log.isLoggable("UrlRules", 2))
      {
        String str5 = "New rules stored, versionToken: " + localObject1;
        int n = Log.v("UrlRules", str5);
      }
      UrlRules localUrlRules = sCachedRules;
    }
  }

  public Rule matchRule(String paramString)
  {
    Matcher localMatcher = this.mPattern.matcher(paramString);
    int i;
    if (localMatcher.lookingAt())
    {
      i = 0;
      int j = this.mRules.length;
      if (i < j)
      {
        int k = i + 1;
        if (localMatcher.group(k) == null);
      }
    }
    for (Rule localRule = this.mRules[i]; ; localRule = Rule.DEFAULT)
    {
      return localRule;
      i += 1;
      break;
    }
  }

  public static class Rule
    implements Comparable
  {
    public static final Rule DEFAULT = new Rule();
    public final boolean mBlock;
    public final String mName;
    public final String mPrefix;
    public final String mRewrite;

    private Rule()
    {
      this.mName = "DEFAULT";
      this.mPrefix = "";
      this.mRewrite = null;
      this.mBlock = false;
    }

    public Rule(String paramString1, String paramString2)
      throws UrlRules.RuleFormatException
    {
      this.mName = paramString1;
      String[] arrayOfString = UrlRules.PATTERN_SPACE_PLUS.split(paramString2);
      if (arrayOfString.length == 0)
        throw new UrlRules.RuleFormatException("Empty rule");
      String str1 = arrayOfString[0];
      this.mPrefix = str1;
      String str2 = null;
      boolean bool = false;
      int i = 1;
      while (true)
      {
        int j = arrayOfString.length;
        if (i >= j)
          break label173;
        String str3 = arrayOfString[i].toLowerCase();
        if (str3.equals("rewrite"))
        {
          int k = i + 1;
          int m = arrayOfString.length;
          if (k < m)
          {
            int n = i + 1;
            str2 = arrayOfString[n];
            i += 2;
          }
        }
        else
        {
          if (!str3.equals("block"))
            break;
          bool = true;
          i += 1;
        }
      }
      String str4 = "Illegal rule: " + paramString2;
      throw new UrlRules.RuleFormatException(str4);
      label173: this.mRewrite = str2;
      this.mBlock = bool;
    }

    public String apply(String paramString)
    {
      if (this.mBlock)
        paramString = null;
      while (true)
      {
        return paramString;
        if (this.mRewrite != null)
        {
          StringBuilder localStringBuilder1 = new StringBuilder();
          String str1 = this.mRewrite;
          StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
          int i = this.mPrefix.length();
          String str2 = paramString.substring(i);
          paramString = str2;
        }
      }
    }

    public int compareTo(Object paramObject)
    {
      String str1 = ((Rule)paramObject).mPrefix;
      String str2 = this.mPrefix;
      return str1.compareTo(str2);
    }
  }

  public static class RuleFormatException extends Exception
  {
    public RuleFormatException(String paramString)
    {
      super();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.UrlRules
 * JD-Core Version:    0.6.2
 */