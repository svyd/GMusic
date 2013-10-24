package com.google.android.music.sync.google.model;

import android.util.Log;
import com.google.android.music.store.InvalidDataException;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

public class BatchReportTrackStats extends GenericJson
{

  @Key("track_stats")
  public List<TrackStat> mTrackStats;

  public BatchReportTrackStats()
  {
    ArrayList localArrayList = new ArrayList();
    this.mTrackStats = localArrayList;
  }

  public void addTrackStat(TrackStat paramTrackStat)
  {
    boolean bool = this.mTrackStats.add(paramTrackStat);
  }

  public List<TrackStat> getTrackStats()
  {
    return this.mTrackStats;
  }

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
        return localByteArrayOutputStream.toByteArray();
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize batched track stats as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize batched track stats for upstream sync.", localIOException);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.BatchReportTrackStats
 * JD-Core Version:    0.6.2
 */