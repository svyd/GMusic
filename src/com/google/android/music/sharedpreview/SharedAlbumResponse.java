package com.google.android.music.sharedpreview;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SharedAlbumResponse extends GenericJson
  implements JsonResponse
{
  public static final String TAG = "SharedAlbumResponse";

  @Key("album_art_url")
  public String mAlbumArtUrl;

  @Key("album_artist")
  public String mAlbumArtist;

  @Key("album_title")
  public String mAlbumTitle;

  @Key("store_url")
  public String mStoreUrl;

  @Key("tracks")
  public List<Track> mTracks;

  public SharedAlbumResponse()
  {
    ArrayList localArrayList = new ArrayList();
    this.mTracks = localArrayList;
  }

  public static SharedAlbumResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      SharedAlbumResponse localSharedAlbumResponse = (SharedAlbumResponse)Json.parse(localJsonParser, SharedAlbumResponse.class, null);
      return localSharedAlbumResponse;
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
    StringBuilder localStringBuilder2 = localStringBuilder1.append("mAlbumTitle=");
    String str1 = this.mAlbumTitle;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" mAlbumArtist=");
    String str2 = this.mAlbumArtist;
    StringBuilder localStringBuilder5 = localStringBuilder1.append(str2);
    StringBuilder localStringBuilder6 = localStringBuilder1.append(" mAlbumArtUrl=");
    String str3 = this.mAlbumArtUrl;
    StringBuilder localStringBuilder7 = localStringBuilder1.append(str3);
    StringBuilder localStringBuilder8 = localStringBuilder1.append(" mStoreUrl=");
    String str4 = this.mStoreUrl;
    StringBuilder localStringBuilder9 = localStringBuilder1.append(str4);
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sharedpreview.SharedAlbumResponse
 * JD-Core Version:    0.6.2
 */