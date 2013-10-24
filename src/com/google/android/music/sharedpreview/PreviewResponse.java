package com.google.android.music.sharedpreview;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class PreviewResponse extends GenericJson
  implements JsonResponse
{

  @Key("durationMillis")
  public int mDurationMillis = 0;

  @Key("nextPlayType")
  public String mNextPlayType;

  @Key("playType")
  public String mPlayType;

  @Key("previewDurationMillis")
  public int mPreviewDurationMillis = 0;

  @Key("url")
  public String mUrl;

  public static int convertPreviewType(String paramString)
  {
    int i = 1;
    if (paramString != null)
    {
      if (!paramString.equalsIgnoreCase("90SP"))
        break label19;
      i = 2;
    }
    while (true)
    {
      return i;
      label19: if (paramString.equalsIgnoreCase("30SP"))
        i = 3;
      else if (paramString.equalsIgnoreCase("FULL"))
        i = 1;
    }
  }

  public static PreviewResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      PreviewResponse localPreviewResponse = (PreviewResponse)Json.parse(localJsonParser, PreviewResponse.class, null);
      return localPreviewResponse;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to parse preview: ");
      String str1 = localJsonParseException.getMessage();
      String str2 = str1;
      throw new IOException(str2);
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("mPreviewDurationMillis=");
    int i = this.mPreviewDurationMillis;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(i);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" mDurationMillis=");
    int j = this.mDurationMillis;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(j);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(" mPlayType=");
    String str1 = this.mPlayType;
    StringBuilder localStringBuilder7 = localStringBuilder1.append(str1);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" mNextPlayType");
    String str2 = this.mNextPlayType;
    StringBuilder localStringBuilder9 = localStringBuilder1.append(str2);
    StringBuilder localStringBuilder10 = localStringBuilder1.append(" mUrl=");
    String str3 = this.mUrl;
    StringBuilder localStringBuilder11 = localStringBuilder1.append(str3);
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sharedpreview.PreviewResponse
 * JD-Core Version:    0.6.2
 */