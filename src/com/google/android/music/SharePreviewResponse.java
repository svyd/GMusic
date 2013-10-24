package com.google.android.music;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SharePreviewResponse extends GenericJson
{
  public static final String TAG = "SharePreviewResponse";

  @Key("externalId")
  public String mExternalId;

  @Key("url")
  public String mUrl;

  public static SharePreviewResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      SharePreviewResponse localSharePreviewResponse = (SharePreviewResponse)Json.parse(localJsonParser, SharePreviewResponse.class, null);
      return localSharePreviewResponse;
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
    StringBuilder localStringBuilder2 = localStringBuilder1.append("externalId=");
    String str = this.mExternalId;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" url=");
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.SharePreviewResponse
 * JD-Core Version:    0.6.2
 */