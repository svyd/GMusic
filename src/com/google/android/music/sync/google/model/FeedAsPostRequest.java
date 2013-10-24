package com.google.android.music.sync.google.model;

import android.util.Log;
import com.google.android.music.store.InvalidDataException;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

public class FeedAsPostRequest extends GenericJson
{

  @Key("max-results")
  public int mMaxResults;

  @Key("start-token")
  public String mStartToken;

  public byte[] serializeAsJson()
    throws InvalidDataException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      JsonFactory localJsonFactory = Json.JSON_FACTORY;
      JsonEncoding localJsonEncoding = JsonEncoding.UTF8;
      JsonGenerator localJsonGenerator1 = localJsonFactory.createJsonGenerator(localByteArrayOutputStream, localJsonEncoding);
      JsonGenerator localJsonGenerator2 = localJsonGenerator1;
      try
      {
        Json.serialize(localJsonGenerator2, this);
        localJsonGenerator2.close();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        if (Log.isLoggable("MusicSyncAdapter", 2))
        {
          String str1 = localByteArrayOutputStream.toString();
          int i = Log.v("MusicSyncAdapter", str1);
        }
        return arrayOfByte;
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a FeedAsPostRequest as JSON: ");
      String str2 = toString();
      String str3 = str2 + ": ";
      int j = Log.e("MusicSyncAdapter", str3, localIOException);
      throw new InvalidDataException("Unable to serialize aFeedAsPostRequest.", localIOException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.FeedAsPostRequest
 * JD-Core Version:    0.6.2
 */