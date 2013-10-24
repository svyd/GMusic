package com.google.android.music.youtube;

import com.google.android.music.log.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeVideo
{
  private String mAuthor = "";
  private int mDurationSeconds;
  private String mId;
  private boolean mIsHd;
  private String mThumbnailUrl;
  private String mTitle = "";
  private long mViewCount;
  private final JSONObject mYoutubeJson;

  public YouTubeVideo(JSONObject paramJSONObject)
  {
    this.mYoutubeJson = paramJSONObject;
  }

  private JSONArray getArray(JSONObject paramJSONObject, String paramString)
    throws JSONException
  {
    if (paramJSONObject.has(paramString));
    for (JSONArray localJSONArray = paramJSONObject.getJSONArray(paramString); ; localJSONArray = null)
      return localJSONArray;
  }

  private JSONObject getObject(JSONObject paramJSONObject, String paramString)
    throws JSONException
  {
    if (paramJSONObject.has(paramString));
    for (JSONObject localJSONObject = paramJSONObject.getJSONObject(paramString); ; localJSONObject = null)
      return localJSONObject;
  }

  public String getAuthor()
  {
    return this.mAuthor;
  }

  public int getDurationSeconds()
  {
    return this.mDurationSeconds;
  }

  public String getId()
  {
    return this.mId;
  }

  public String getThumbnailUrl()
  {
    return this.mThumbnailUrl;
  }

  public String getTitle()
  {
    return this.mTitle;
  }

  public long getViewCount()
  {
    return this.mViewCount;
  }

  public boolean isHd()
  {
    return this.mIsHd;
  }

  public boolean parse()
    throws JSONException
  {
    int i = 0;
    this.mDurationSeconds = i;
    JSONObject localJSONObject1 = this.mYoutubeJson.getJSONObject("media$group");
    String str1 = "media$content";
    JSONArray localJSONArray1 = localJSONObject1.getJSONArray(str1);
    int j = localJSONArray1.length();
    int k = 0;
    int m;
    String str4;
    if (k < j)
    {
      JSONObject localJSONObject2 = localJSONArray1.getJSONObject(k);
      String str2 = "yt$format";
      m = localJSONObject2.getInt(str2);
      int n = 5;
      if (m != n)
        break label198;
      String str3 = "url";
      str4 = localJSONObject2.getString(str3);
      String str5 = YouTubeUtils.getVideoId(str4);
      if (str5 == null)
        break label160;
      this.mId = str5;
      String str6 = "duration";
      if (localJSONObject2.has(str6))
      {
        String str7 = "duration";
        int i1 = localJSONObject2.getInt(str7);
        this.mDurationSeconds = i1;
      }
    }
    boolean bool1;
    if (this.mId == null)
    {
      Log.w("YouTubeVideo", "A video could not be extracted from the feed entry.");
      bool1 = false;
    }
    while (true)
    {
      return bool1;
      label160: String str8 = "Error parsing video ID from " + str4;
      Log.w("YouTubeVideo", str8);
      while (true)
      {
        k += 1;
        break;
        label198: String str9 = "Skipping video with format " + m;
        Log.i("YouTubeVideo", str9);
      }
      String str10 = "";
      this.mTitle = str10;
      YouTubeVideo localYouTubeVideo1 = this;
      String str11 = "media$title";
      JSONObject localJSONObject3 = localYouTubeVideo1.getObject(localJSONObject1, str11);
      if (localJSONObject3 != null)
      {
        String str12 = "$t";
        String str13 = localJSONObject3.getString(str12);
        this.mTitle = str13;
      }
      String str14 = "";
      this.mAuthor = str14;
      JSONObject localJSONObject4 = this.mYoutubeJson;
      YouTubeVideo localYouTubeVideo2 = this;
      JSONObject localJSONObject5 = localJSONObject4;
      String str15 = "author";
      JSONArray localJSONArray2 = localYouTubeVideo2.getArray(localJSONObject5, str15);
      if ((localJSONArray2 != null) && (localJSONArray2.length() > 0))
      {
        int i2 = 0;
        JSONObject localJSONObject6 = localJSONArray2.getJSONObject(i2);
        String str16 = "name";
        String str17 = localJSONObject6.getJSONObject(str16).getString("$t");
        this.mAuthor = str17;
      }
      JSONObject localJSONObject7 = this.mYoutubeJson;
      YouTubeVideo localYouTubeVideo3 = this;
      JSONObject localJSONObject8 = localJSONObject7;
      String str18 = "yt$hd";
      boolean bool2;
      label400: JSONObject localJSONObject11;
      if (localYouTubeVideo3.getObject(localJSONObject8, str18) != null)
      {
        bool2 = true;
        boolean bool3 = bool2;
        this.mIsHd = bool3;
        long l1 = 0L;
        this.mViewCount = l1;
        JSONObject localJSONObject9 = this.mYoutubeJson;
        YouTubeVideo localYouTubeVideo4 = this;
        JSONObject localJSONObject10 = localJSONObject9;
        String str19 = "yt$statistics";
        localJSONObject11 = localYouTubeVideo4.getObject(localJSONObject10, str19);
        if ((localJSONObject11 == null) || (!localJSONObject11.has("viewCount")));
      }
      try
      {
        long l2 = Long.parseLong(localJSONObject11.getString("viewCount"));
        this.mViewCount = l2;
        label482: YouTubeVideo localYouTubeVideo5 = this;
        String str20 = "media$thumbnail";
        JSONArray localJSONArray3 = localYouTubeVideo5.getArray(localJSONObject1, str20);
        if ((localJSONArray3 != null) && (localJSONArray3.length() > 0))
        {
          j = localJSONArray3.length();
          k = 0;
        }
        while (true)
        {
          if (k < j)
          {
            JSONObject localJSONObject12 = localJSONArray3.getJSONObject(k);
            String str21 = "yt$name";
            if (localJSONObject12.getString(str21).equalsIgnoreCase("hqdefault"))
            {
              String str22 = "url";
              String str23 = localJSONObject12.getString(str22);
              this.mThumbnailUrl = str23;
            }
          }
          else
          {
            if (this.mThumbnailUrl == null)
            {
              int i3 = 0;
              String str24 = localJSONArray3.getJSONObject(i3).getString("url");
              this.mThumbnailUrl = str24;
            }
            bool1 = true;
            break;
            bool2 = false;
            break label400;
          }
          k += 1;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        break label482;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.youtube.YouTubeVideo
 * JD-Core Version:    0.6.2
 */