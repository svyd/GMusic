package com.google.android.music.medialist;

import android.util.Log;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.store.MusicFile;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

public class SongData extends GenericJson
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.MEDIA_LIST);

  @Key("album")
  public String mAlbum;

  @Key("albumArtist")
  public String mAlbumArtist;

  @Key("albumArtistId")
  public long mAlbumArtistId;

  @Key("albumId")
  public long mAlbumId;

  @Key("artist")
  public String mArtist;

  @Key("artistSort")
  public String mArtistSort;

  @Key("domainParam")
  public String mDomainParam;

  @Key("duration")
  public long mDuration;

  @Key("hasLocal")
  public int mHasLocal;

  @Key("hasRemote")
  public int mHasRemote;

  @Key("rating")
  public int mRating;

  @Key("sourceId")
  public String mSourceId;

  @Key("title")
  public String mTitle;

  public static SongData parseFromJson(String paramString)
  {
    try
    {
      byte[] arrayOfByte = paramString.getBytes();
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      JsonParser localJsonParser = Json.JSON_FACTORY.createJsonParser(localByteArrayInputStream);
      JsonToken localJsonToken = localJsonParser.nextToken();
      localSongData = (SongData)Json.parseAndClose(localJsonParser, SongData.class, null);
      return localSongData;
    }
    catch (JsonParseException localJsonParseException)
    {
      while (true)
      {
        if (LOGV)
        {
          String str1 = "Failed to parse song s=" + paramString;
          int i = Log.d("SongData", str1, localJsonParseException);
        }
        localSongData = null;
      }
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        if (LOGV)
        {
          String str2 = "Failed to parse song s=" + paramString;
          int j = Log.d("SongData", str2, localIOException);
        }
        SongData localSongData = null;
      }
    }
  }

  public static String toJson(SongData paramSongData)
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
        Json.serialize(localJsonGenerator2, paramSongData);
        localJsonGenerator2.close();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        str1 = new String(arrayOfByte);
        if (LOGV)
        {
          String str2 = "toJson: s=" + str1;
          int i = Log.d("SongData", str2);
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
        int j = Log.e("SongData", "Unable to serialize Song as JSON", localIOException);
        String str1 = null;
      }
    }
  }

  public MusicFile formatAsMusicFile(MusicFile paramMusicFile)
  {
    if (paramMusicFile == null)
      paramMusicFile = new MusicFile();
    paramMusicFile.reset();
    String str1 = this.mArtist;
    paramMusicFile.setTrackArtist(str1);
    String str2 = this.mAlbum;
    paramMusicFile.setAlbumName(str2);
    String str3 = this.mTitle;
    paramMusicFile.setTitle(str3);
    String str4 = this.mAlbumArtist;
    paramMusicFile.setAlbumArtist(str4);
    long l = this.mDuration;
    paramMusicFile.setDurationInMilliSec(l);
    ContentIdentifier.Domain localDomain = ContentIdentifier.Domain.SHARED;
    paramMusicFile.setDomain(localDomain);
    return paramMusicFile;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.SongData
 * JD-Core Version:    0.6.2
 */