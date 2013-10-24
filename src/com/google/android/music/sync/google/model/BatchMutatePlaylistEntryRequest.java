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

public class BatchMutatePlaylistEntryRequest extends GenericJson
{

  @Key("mutations")
  public List<MutatePlaylistEntryRequest> mMutatePlaylistEntryRequests;

  public BatchMutatePlaylistEntryRequest()
  {
    ArrayList localArrayList = new ArrayList();
    this.mMutatePlaylistEntryRequests = localArrayList;
  }

  public void addRequest(MutatePlaylistEntryRequest paramMutatePlaylistEntryRequest)
  {
    boolean bool = this.mMutatePlaylistEntryRequests.add(paramMutatePlaylistEntryRequest);
  }

  public List<MutatePlaylistEntryRequest> getRequests()
  {
    return this.mMutatePlaylistEntryRequests;
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
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize batched entries as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize batched entries for upstream sync.", localIOException);
    }
  }

  public static class MutatePlaylistEntryRequest
  {

    @Key("create")
    public SyncablePlaylistEntry mCreatePlaylistEntry;

    @Key("delete")
    public String mDeletePlaylistEntryRemoteId;

    @Key("update")
    public SyncablePlaylistEntry mUpdatePlaylistEntry;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.BatchMutatePlaylistEntryRequest
 * JD-Core Version:    0.6.2
 */