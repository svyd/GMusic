package com.google.android.music.youtube;

import android.content.Context;
import android.net.Uri;
import android.net.Uri.Builder;
import com.google.android.music.log.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeUtils
{
  public static final String YOUTUBE_FORMAT_SHOCKWAVE_FLASH_STRING = String.valueOf(5);
  private static final Pattern YOUTUBE_VIDEO_PATTERN = Pattern.compile("^http[s]?://www.youtube.com/v/([^\\?]+)\\?.*");

  public static String formatDurationSeconds(Context paramContext, int paramInt)
  {
    int i = paramInt / 60;
    int j = i / 60;
    if (j > 0)
      i %= 60;
    int k = paramInt % 60;
    String str2;
    Object[] arrayOfObject1;
    if (j > 0)
    {
      String str1 = paramContext.getString(2131230738);
      str2 = paramContext.getString(2131230738);
      arrayOfObject1 = new Object[3];
      Integer localInteger1 = Integer.valueOf(j);
      arrayOfObject1[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(i);
      arrayOfObject1[1] = localInteger2;
      Integer localInteger3 = Integer.valueOf(k);
      arrayOfObject1[2] = localInteger3;
    }
    String str5;
    Object[] arrayOfObject2;
    for (String str3 = String.format(str2, arrayOfObject1); ; str3 = String.format(str5, arrayOfObject2))
    {
      return str3;
      String str4 = paramContext.getString(2131230739);
      str5 = paramContext.getString(2131230739);
      arrayOfObject2 = new Object[2];
      Integer localInteger4 = Integer.valueOf(i);
      arrayOfObject2[0] = localInteger4;
      Integer localInteger5 = Integer.valueOf(k);
      arrayOfObject2[1] = localInteger5;
    }
  }

  static JSONArray getEntries(String paramString)
    throws JSONException
  {
    JSONObject localJSONObject = new JSONObject(paramString).getJSONObject("feed");
    if (!localJSONObject.has("entry"));
    for (JSONArray localJSONArray = null; ; localJSONArray = localJSONObject.getJSONArray("entry"))
      return localJSONArray;
  }

  static String getVideoId(String paramString)
  {
    Pattern localPattern = YOUTUBE_VIDEO_PATTERN;
    String str1 = paramString.trim();
    Matcher localMatcher = localPattern.matcher(str1);
    if (localMatcher.matches());
    for (String str2 = localMatcher.group(1); ; str2 = null)
      return str2;
  }

  static void parseEntries(JSONArray paramJSONArray, List<YouTubeVideo> paramList)
    throws JSONException
  {
    int i = 0;
    int j = paramJSONArray.length();
    if (i >= j)
      return;
    JSONObject localJSONObject = paramJSONArray.getJSONObject(i);
    YouTubeVideo localYouTubeVideo = new YouTubeVideo(localJSONObject);
    if (!localYouTubeVideo.parse())
      Log.i("YouTubeUtils", "Error parsing entry in YouTube feed.");
    while (true)
    {
      i += 1;
      break;
      boolean bool = paramList.add(localYouTubeVideo);
    }
  }

  public static List<YouTubeVideo> searchForMatchingVideos(String paramString, int paramInt1, int paramInt2)
  {
    ArrayList localArrayList = new ArrayList();
    try
    {
      String str1 = URLEncoder.encode(paramString, "utf-8");
      String str2 = str1;
      Uri.Builder localBuilder1 = Uri.parse("https://gdata.youtube.com/feeds/api/videos").buildUpon().appendQueryParameter("q", paramString).appendQueryParameter("alt", "json");
      String str3 = YOUTUBE_FORMAT_SHOCKWAVE_FLASH_STRING;
      Uri.Builder localBuilder2 = localBuilder1.appendQueryParameter("format", str3).appendQueryParameter("v", "2").appendQueryParameter("key", "AI39si7E6CHtSMfmqL04cFEUgotVb0C6eozj6Nb9M4EsFxZ-hCKWVwpFfRemzEQ0o8pTlkW_U6d6lF23MY1etGLlY18QkfBDeA").appendQueryParameter("orderby", "relevance");
      String str4 = String.valueOf(paramInt1);
      Uri.Builder localBuilder3 = localBuilder2.appendQueryParameter("start-index", str4);
      String str5 = String.valueOf(paramInt2);
      str6 = localBuilder3.appendQueryParameter("max-results", str5).appendQueryParameter("fields", "entry(author,yt:statistics,yt:hd,media:group(media:content,media:title,media:thumbnail))").build().toString();
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      try
      {
        String str6;
        BasicHttpParams localBasicHttpParams = new BasicHttpParams();
        Boolean localBoolean = Boolean.TRUE;
        HttpParams localHttpParams = localBasicHttpParams.setParameter("http.protocol.handle-redirects", localBoolean);
        HttpConnectionParams.setConnectionTimeout(localBasicHttpParams, 10000);
        HttpConnectionParams.setSoTimeout(localBasicHttpParams, 10000);
        DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient(localBasicHttpParams);
        HttpGet localHttpGet = new HttpGet(str6);
        HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpGet);
        StatusLine localStatusLine = localHttpResponse.getStatusLine();
        if ((localStatusLine != null) && (localStatusLine.getStatusCode() == 200))
        {
          String str7 = EntityUtils.toString(localHttpResponse.getEntity());
          if (str7 != null)
          {
            JSONArray localJSONArray = getEntries(str7);
            if (localJSONArray != null)
              parseEntries(localJSONArray, localArrayList);
          }
        }
        while (true)
        {
          return localArrayList;
          localUnsupportedEncodingException = localUnsupportedEncodingException;
          Log.e("YouTubeUtils", "Unsupported encoding");
        }
      }
      catch (IOException localIOException)
      {
        while (true)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Error querying YouTube gdata API.  ");
          String str8 = localIOException.toString();
          String str9 = str8;
          Log.v("YouTubeUtils", str9);
        }
      }
      catch (JSONException localJSONException)
      {
        while (true)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Error in JSON.  ");
          String str10 = localJSONException.toString();
          String str11 = str10;
          Log.v("YouTubeUtils", str11);
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.youtube.YouTubeUtils
 * JD-Core Version:    0.6.2
 */