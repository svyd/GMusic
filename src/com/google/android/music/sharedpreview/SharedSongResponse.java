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

public class SharedSongResponse extends GenericJson
  implements JsonResponse
{

  @Key("album_art_url")
  public String mAlbumArtUrl;

  @Key("album_title")
  public String mAlbumTitle;

  @Key("duration_msecs")
  public int mDurationMsecs;

  @Key("store_url")
  public String mStoreUrl;

  @Key("track_artist")
  public String mTrackArtist;

  @Key("track_title")
  public String mTrackTitle;

  public static SharedSongResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      SharedSongResponse localSharedSongResponse = (SharedSongResponse)Json.parse(localJsonParser, SharedSongResponse.class, null);
      return localSharedSongResponse;
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
    StringBuilder localStringBuilder2 = localStringBuilder1.append(" mTrackTitle=");
    String str1 = this.mTrackTitle;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" mAlbumTitle=");
    String str2 = this.mAlbumTitle;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(str2);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(" mTackArtist=");
    String str3 = this.mTrackArtist;
    StringBuilder localStringBuilder7 = localStringBuilder1.append(str3);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" mAlbumArtUrl=");
    String str4 = this.mAlbumArtUrl;
    StringBuilder localStringBuilder9 = localStringBuilder1.append(str4);
    StringBuilder localStringBuilder10 = localStringBuilder1.append(" mStoreUrl");
    String str5 = this.mStoreUrl;
    StringBuilder localStringBuilder11 = localStringBuilder1.append(str5);
    StringBuilder localStringBuilder12 = localStringBuilder1.append(" mDurationMsecs=");
    int i = this.mDurationMsecs;
    StringBuilder localStringBuilder13 = localStringBuilder1.append(i);
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sharedpreview.SharedSongResponse
 * JD-Core Version:    0.6.2
 */