package com.google.android.music.sync.google.model;

import android.content.Context;
import com.google.android.music.sync.api.MusicUrl;
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

public class ConfigEntryFeed extends GenericJson
  implements MusicQueueableSyncEntity.Feed<ConfigEntry>
{

  @Key("data")
  public ConfigEntryListData configEntryListData;

  public static ConfigEntryFeed parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      ConfigEntryFeed localConfigEntryFeed = (ConfigEntryFeed)Json.parse(localJsonParser, ConfigEntryFeed.class, null);
      return localConfigEntryFeed;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to parse config feed: ");
      String str1 = localJsonParseException.getMessage();
      String str2 = str1;
      throw new IOException(str2);
    }
  }

  public List<ConfigEntry> getItemList()
  {
    if (this.configEntryListData == null);
    for (Object localObject = null; ; localObject = this.configEntryListData.items)
      return localObject;
  }

  public String getNextPageToken()
  {
    return null;
  }

  public MusicUrl getUrl(Context paramContext)
  {
    return MusicUrl.forConfigEntriesFeed();
  }

  public static class ConfigEntryListData extends GenericJson
  {

    @Key("entries")
    public List<ConfigEntry> items;

    public ConfigEntryListData()
    {
      ArrayList localArrayList = new ArrayList();
      this.items = localArrayList;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.ConfigEntryFeed
 * JD-Core Version:    0.6.2
 */