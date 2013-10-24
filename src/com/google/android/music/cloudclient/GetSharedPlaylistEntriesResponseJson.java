package com.google.android.music.cloudclient;

import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.util.ArrayList;
import java.util.List;

public class GetSharedPlaylistEntriesResponseJson extends GenericJson
{
  public static String TAG = "GetSharedPlaylists";

  @Key("entries")
  public List<Entry> mEntries;

  @Key("nextPageToken")
  public String mNextPageToken;

  public GetSharedPlaylistEntriesResponseJson()
  {
    ArrayList localArrayList = new ArrayList();
    this.mEntries = localArrayList;
  }

  public static class Entry extends GenericJson
  {

    @Key("playlistEntry")
    public List<SyncablePlaylistEntry> mPlaylistEntry;

    @Key("responseCode")
    public String mResponseCode;

    @Key("shareToken")
    public String mShareToken;

    public Entry()
    {
      ArrayList localArrayList = new ArrayList();
      this.mPlaylistEntry = localArrayList;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.GetSharedPlaylistEntriesResponseJson
 * JD-Core Version:    0.6.2
 */