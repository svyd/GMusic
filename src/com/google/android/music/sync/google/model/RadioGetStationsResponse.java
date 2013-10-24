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

public class RadioGetStationsResponse extends GenericJson
  implements MusicQueueableSyncEntity.Feed<SyncableRadioStation>
{

  @Key("nextPageToken")
  public String mNextPageToken;

  @Key("data")
  public RadioStationsData mRadioStationsData;

  public static RadioGetStationsResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      RadioGetStationsResponse localRadioGetStationsResponse = (RadioGetStationsResponse)Json.parse(localJsonParser, RadioGetStationsResponse.class, null);
      return localRadioGetStationsResponse;
    }
    catch (JsonParseException localJsonParseException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Failed to parse radio station response: ");
      String str1 = localJsonParseException.getMessage();
      String str2 = str1;
      throw new IOException(str2);
    }
  }

  public List<SyncableRadioStation> getItemList()
  {
    if (this.mRadioStationsData == null);
    for (Object localObject = null; ; localObject = this.mRadioStationsData.mItems)
      return localObject;
  }

  public String getNextPageToken()
  {
    return this.mNextPageToken;
  }

  public MusicUrl getUrl(Context paramContext)
  {
    return MusicUrl.forRadioGetStations();
  }

  public static class RadioStationsData extends GenericJson
  {

    @Key("items")
    public List<SyncableRadioStation> mItems;

    public RadioStationsData()
    {
      ArrayList localArrayList = new ArrayList();
      this.mItems = localArrayList;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.RadioGetStationsResponse
 * JD-Core Version:    0.6.2
 */