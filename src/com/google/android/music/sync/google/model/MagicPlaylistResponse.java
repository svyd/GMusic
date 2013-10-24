package com.google.android.music.sync.google.model;

import android.util.Log;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class MagicPlaylistResponse extends GenericJson
{

  @Key("entries")
  public List<SyncablePlaylistEntry> mPlaylistEntries;

  @Key("id")
  public String mRemotePlaylistId;

  @Key("status_code")
  public String mStatusCode;

  public static MagicPlaylistResponse parseFromJsonInputStream(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      MagicPlaylistResponse localMagicPlaylistResponse = (MagicPlaylistResponse)Json.parse(localJsonParser, MagicPlaylistResponse.class, null);
      return localMagicPlaylistResponse;
    }
    catch (JsonParseException localJsonParseException)
    {
      String str = localJsonParseException.getMessage();
      throw new IOException(str);
    }
  }

  public StatusCode getStatusCode()
  {
    StatusCode[] arrayOfStatusCode = StatusCode.values();
    int i = arrayOfStatusCode.length;
    int j = 0;
    StatusCode localStatusCode;
    if (j < i)
    {
      localStatusCode = arrayOfStatusCode[j];
      String str1 = localStatusCode.name();
      String str2 = this.mStatusCode;
      if (!str1.equals(str2));
    }
    while (true)
    {
      return localStatusCode;
      j += 1;
      break;
      StringBuilder localStringBuilder = new StringBuilder().append("Unknown error code received from server: ");
      String str3 = this.mStatusCode;
      String str4 = str3;
      int k = Log.w("MusicSyncAdapter", str4);
      localStatusCode = StatusCode.OK;
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("remotePlaylistId:");
    String str1 = this.mRemotePlaylistId;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("; mPlaylistEntries:");
    String str2 = this.mPlaylistEntries.toString();
    return str2;
  }

  public static enum StatusCode
  {
    static
    {
      INSUFFICIENT_RESULTS = new StatusCode("INSUFFICIENT_RESULTS", 2);
      INTERNAL_SERVER_ERROR = new StatusCode("INTERNAL_SERVER_ERROR", 3);
      StatusCode[] arrayOfStatusCode = new StatusCode[4];
      StatusCode localStatusCode1 = OK;
      arrayOfStatusCode[0] = localStatusCode1;
      StatusCode localStatusCode2 = TRY_AGAIN;
      arrayOfStatusCode[1] = localStatusCode2;
      StatusCode localStatusCode3 = INSUFFICIENT_RESULTS;
      arrayOfStatusCode[2] = localStatusCode3;
      StatusCode localStatusCode4 = INTERNAL_SERVER_ERROR;
      arrayOfStatusCode[3] = localStatusCode4;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.MagicPlaylistResponse
 * JD-Core Version:    0.6.2
 */