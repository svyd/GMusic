package com.google.api.client.http;

import com.google.api.client.escape.CharEscapers;
import com.google.api.client.escape.Escaper;
import com.google.api.client.escape.PercentEscaper;
import com.google.api.client.util.GenericData;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class GenericUrl extends GenericData
{
  private static final Escaper URI_FRAGMENT_ESCAPER = new PercentEscaper("=&-_.!~*'()@:$,;/?:", false);
  public String fragment;
  public String host;
  public List<String> pathParts;
  public int port = -1;
  public String scheme;

  public GenericUrl()
  {
  }

  public GenericUrl(String paramString)
  {
    try
    {
      URI localURI = new URI(paramString);
      String str1 = localURI.getScheme().toLowerCase();
      this.scheme = str1;
      String str2 = localURI.getHost();
      this.host = str2;
      int i = localURI.getPort();
      this.port = i;
      List localList = toPathParts(localURI.getRawPath());
      this.pathParts = localList;
      String str3 = localURI.getFragment();
      this.fragment = str3;
      String str4 = localURI.getRawQuery();
      if (str4 == null)
        return;
      UrlEncodedParser.parse(str4, this);
      return;
    }
    catch (URISyntaxException localURISyntaxException)
    {
      throw new IllegalArgumentException(localURISyntaxException);
    }
  }

  private static boolean appendParam(boolean paramBoolean, StringBuilder paramStringBuilder, String paramString, Object paramObject)
  {
    if (paramBoolean)
    {
      paramBoolean = false;
      StringBuilder localStringBuilder1 = paramStringBuilder.append('?');
    }
    while (true)
    {
      StringBuilder localStringBuilder2 = paramStringBuilder.append(paramString);
      String str = CharEscapers.escapeUriQuery(paramObject.toString());
      if (str.length() != 0)
        StringBuilder localStringBuilder3 = paramStringBuilder.append('=').append(str);
      return paramBoolean;
      StringBuilder localStringBuilder4 = paramStringBuilder.append('&');
    }
  }

  private void appendRawPathFromParts(StringBuilder paramStringBuilder)
  {
    List localList = this.pathParts;
    int i = localList.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      String str1 = (String)localList.get(j);
      if (j != 0)
        StringBuilder localStringBuilder1 = paramStringBuilder.append('/');
      if (str1.length() != 0)
      {
        String str2 = CharEscapers.escapeUriPath(str1);
        StringBuilder localStringBuilder2 = paramStringBuilder.append(str2);
      }
      j += 1;
    }
  }

  public static List<String> toPathParts(String paramString)
  {
    if ((paramString == null) || (paramString.length() == 0))
    {
      localObject = null;
      return localObject;
    }
    Object localObject = new ArrayList();
    int i = 0;
    int j = 1;
    label27: int k;
    if (j != 0)
    {
      k = paramString.indexOf('/', i);
      if (k == -1)
        break label87;
      j = 1;
      label49: if (j == 0)
        break label92;
    }
    label87: label92: for (String str1 = paramString.substring(i, k); ; str1 = paramString.substring(i))
    {
      String str2 = CharEscapers.decodeUri(str1);
      boolean bool = ((List)localObject).add(str2);
      i = k + 1;
      break label27;
      break;
      j = 0;
      break label49;
    }
  }

  public final String build()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = this.scheme;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("://");
    String str2 = this.host;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(str2);
    int i = this.port;
    if (i != -1)
      StringBuilder localStringBuilder4 = localStringBuilder1.append(':').append(i);
    if (this.pathParts != null)
      appendRawPathFromParts(localStringBuilder1);
    boolean bool = true;
    Iterator localIterator1 = entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      Object localObject1 = localEntry.getValue();
      if (localObject1 != null)
      {
        String str3 = CharEscapers.escapeUriQuery((String)localEntry.getKey());
        if ((localObject1 instanceof Collection))
        {
          Iterator localIterator2 = ((Collection)localObject1).iterator();
          while (localIterator2.hasNext())
          {
            Object localObject2 = localIterator2.next();
            bool = appendParam(bool, localStringBuilder1, str3, localObject2);
          }
        }
        else
        {
          bool = appendParam(bool, localStringBuilder1, str3, localObject1);
        }
      }
    }
    String str4 = this.fragment;
    if (str4 != null)
    {
      StringBuilder localStringBuilder5 = localStringBuilder1.append('#');
      String str5 = URI_FRAGMENT_ESCAPER.escape(str4);
      StringBuilder localStringBuilder6 = localStringBuilder5.append(str5);
    }
    return localStringBuilder1.toString();
  }

  public GenericUrl clone()
  {
    GenericUrl localGenericUrl = (GenericUrl)super.clone();
    List localList = this.pathParts;
    ArrayList localArrayList = new ArrayList(localList);
    localGenericUrl.pathParts = localArrayList;
    return localGenericUrl;
  }

  public boolean equals(Object paramObject)
  {
    boolean bool;
    if (this == paramObject)
      bool = true;
    while (true)
    {
      return bool;
      if ((!super.equals(paramObject)) || (!(paramObject instanceof GenericUrl)))
      {
        bool = false;
      }
      else
      {
        GenericUrl localGenericUrl = (GenericUrl)paramObject;
        String str1 = build();
        String str2 = localGenericUrl.toString();
        bool = str1.equals(str2);
      }
    }
  }

  public int hashCode()
  {
    return build().hashCode();
  }

  public String toString()
  {
    return build();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.api.client.http.GenericUrl
 * JD-Core Version:    0.6.2
 */