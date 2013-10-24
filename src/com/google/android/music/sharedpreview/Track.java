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

public class Track extends GenericJson
{

  @Key("duration_msecs")
  public int mDurationMsecs;

  @Key("id")
  public String mId;

  @Key("title")
  public String mTitle;

  @Key("track_number")
  public int mTrackNumber;

  public static Track parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      Track localTrack = (Track)Json.parse(localJsonParser, Track.class, null);
      return localTrack;
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
    StringBuilder localStringBuilder2 = localStringBuilder1.append("mId=");
    String str1 = this.mId;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" mTrackNumber=");
    int i = this.mTrackNumber;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(i);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(" mDurationSecs=");
    int j = this.mDurationMsecs;
    StringBuilder localStringBuilder7 = localStringBuilder1.append(j);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" mTitle=");
    String str2 = this.mTitle;
    StringBuilder localStringBuilder9 = localStringBuilder1.append(str2);
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sharedpreview.Track
 * JD-Core Version:    0.6.2
 */