package com.google.android.music.cloudclient;

import android.util.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.IOException;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class ProfileRequest
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CLOUD_CLIENT);

  public static class ProfileInfo extends GenericJson
  {

    @Key("familyName")
    public String mFamilyName;

    @Key("givenName")
    public String mGivenName;

    @Key("photoUrl")
    public String mPhotoUrl;

    public static ProfileInfo parse(byte[] paramArrayOfByte)
      throws IOException
    {
      try
      {
        String str1 = new String(paramArrayOfByte);
        int i = Log.i("ProfileRequest", str1);
        JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(paramArrayOfByte);
        JsonToken localJsonToken = localJsonParser.nextToken();
        ProfileInfo localProfileInfo = (ProfileInfo)Json.parse(localJsonParser, ProfileInfo.class, null);
        return localProfileInfo;
      }
      catch (JsonParseException localJsonParseException)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Failed to parse preview: ");
        String str2 = localJsonParseException.getMessage();
        String str3 = str2;
        throw new IOException(str3);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.ProfileRequest
 * JD-Core Version:    0.6.2
 */