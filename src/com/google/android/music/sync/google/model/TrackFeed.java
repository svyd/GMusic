package com.google.android.music.sync.google.model;

import android.content.Context;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.utils.AlbumArtUtils;
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

public class TrackFeed extends GenericJson
  implements MusicQueueableSyncEntity.Feed<Track>
{

  @Key("apiVersion")
  public String apiVersion;

  @Key("nextPageToken")
  public String nextPageToken;

  @Key("data")
  public TrackListData trackListData;

  public static TrackFeed parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      TrackFeed localTrackFeed = (TrackFeed)Json.parse(localJsonParser, TrackFeed.class, null);
      return localTrackFeed;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to parse track feed: ");
      String str1 = localJsonParseException.getMessage();
      String str2 = str1;
      throw new IOException(str2);
    }
  }

  public List<Track> getItemList()
  {
    if (this.trackListData == null);
    for (Object localObject = null; ; localObject = this.trackListData.items)
      return localObject;
  }

  public String getNextPageToken()
  {
    return this.nextPageToken;
  }

  public MusicUrl getUrl(Context paramContext)
  {
    return MusicUrl.forTracksFeed(AlbumArtUtils.getMaxAlbumArtSize(paramContext));
  }

  public static class TrackListData extends GenericJson
  {

    @Key("items")
    public List<Track> items;

    public TrackListData()
    {
      ArrayList localArrayList = new ArrayList();
      this.items = localArrayList;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.TrackFeed
 * JD-Core Version:    0.6.2
 */