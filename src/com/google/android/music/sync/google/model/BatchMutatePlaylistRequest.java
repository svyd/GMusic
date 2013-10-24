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

public class BatchMutatePlaylistRequest extends GenericJson
{

  @Key("mutations")
  public List<MutatePlaylistRequest> mMutatePlaylistRequests;

  public BatchMutatePlaylistRequest()
  {
    ArrayList localArrayList = new ArrayList();
    this.mMutatePlaylistRequests = localArrayList;
  }

  public void addRequest(MutatePlaylistRequest paramMutatePlaylistRequest)
  {
    boolean bool = this.mMutatePlaylistRequests.add(paramMutatePlaylistRequest);
  }

  public List<MutatePlaylistRequest> getRequests()
  {
    return this.mMutatePlaylistRequests;
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
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize batched playlists as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize batched playlists for upstream sync.", localIOException);
    }
  }

  public static class MutatePlaylistRequest
  {

    @Key("create")
    public SyncablePlaylist mCreatePlaylist;

    @Key("delete")
    public String mDeletePlaylistRemoteId;

    @Key("update")
    public SyncablePlaylist mUpdatePlaylist;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.BatchMutatePlaylistRequest
 * JD-Core Version:    0.6.2
 */