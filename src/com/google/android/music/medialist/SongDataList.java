package com.google.android.music.medialist;

import android.util.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SongDataList extends GenericJson
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);

  @Key("list")
  public ArrayList<SongData> mList;

  public SongDataList()
  {
    ArrayList localArrayList = new ArrayList();
    this.mList = localArrayList;
  }

  public static SongDataList parseFromJson(String paramString)
  {
    if (LOGV)
    {
      String str1 = "parseFromJson: s=" + paramString;
      int i = Log.d("SongDataList", str1);
    }
    try
    {
      byte[] arrayOfByte = paramString.getBytes();
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(localByteArrayInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      localSongDataList = (SongDataList)Json.parseAndClose(localJsonParser, SongDataList.class, null);
      return localSongDataList;
    }
    catch (JsonParseException localJsonParseException)
    {
      while (true)
      {
        if (LOGV)
        {
          String str2 = "Failed to parse songs s=" + paramString;
          int j = Log.d("SongDataList", str2, localJsonParseException);
        }
        localSongDataList = null;
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        if (LOGV)
        {
          String str3 = "Failed to parse songs s=" + paramString;
          int k = Log.d("SongDataList", str3, localIOException);
        }
        SongDataList localSongDataList = null;
      }
    }
  }

  public static String toJson(SongDataList paramSongDataList)
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
        Json.serialize(localJsonGenerator2, paramSongDataList);
        localJsonGenerator2.close();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        str1 = new String(arrayOfByte);
        if (LOGV)
        {
          String str2 = "arrayToJson: s=" + str1;
          int i = Log.d("SongDataList", str2);
        }
        return str1;
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        int j = Log.e("SongDataList", "Unable to serialize Songs as JSON", localIOException);
        String str1 = null;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SongDataList
 * JD-Core Version:    0.6.2
 */