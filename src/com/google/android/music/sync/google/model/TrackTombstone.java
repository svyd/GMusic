package com.google.android.music.sync.google.model;

import android.content.Context;
import android.util.Log;
import com.google.android.common.base.Strings;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.MusicFileTombstone;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.utils.AlbumArtUtils;

public class TrackTombstone
  implements MusicQueueableSyncEntity
{
  private long mLastModifiedTimestamp = 65535L;
  private long mLocalId = 65535L;
  private String mRemoteId = null;

  public static TrackTombstone parse(MusicFileTombstone paramMusicFileTombstone)
  {
    TrackTombstone localTrackTombstone = new TrackTombstone();
    String str = paramMusicFileTombstone.getSourceId();
    localTrackTombstone.mRemoteId = str;
    long l1 = paramMusicFileTombstone.getLocalId();
    localTrackTombstone.mLocalId = l1;
    try
    {
      long l2 = Long.parseLong(paramMusicFileTombstone.getSourceVersion());
      localTrackTombstone.mLastModifiedTimestamp = l2;
      return localTrackTombstone;
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
      {
        int i = Log.w("MusicSyncAdapter", "Non-numeric version for music tombstone.  Replacing with 0.", localNumberFormatException);
        localTrackTombstone.mLastModifiedTimestamp = 0L;
      }
    }
  }

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    return MusicUrl.forTracksBatchMutation();
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    throw new UnsupportedOperationException("TrackTombstone: getFeedUrl not supported");
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    throw new UnsupportedOperationException("TrackTombstone: getFeedUrlAsPost not supported");
  }

  public long getLocalId()
  {
    return this.mLocalId;
  }

  public String getRemoteId()
  {
    return this.mRemoteId;
  }

  public MusicUrl getUrl(Context paramContext, String paramString)
  {
    int i = AlbumArtUtils.getMaxAlbumArtSize(paramContext);
    return MusicUrl.forTrack(paramString, i);
  }

  public boolean isDeleted()
  {
    return true;
  }

  public boolean isInsert()
  {
    return false;
  }

  public boolean isUpdate()
  {
    return false;
  }

  public byte[] serializeAsJson()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("TrackTombstone: serializeAsJson not supported");
  }

  public void setIsDeleted(boolean paramBoolean)
  {
  }

  public void setRemoteId(String paramString)
  {
    this.mRemoteId = paramString;
  }

  public void validateForUpstreamDelete()
    throws InvalidDataException
  {
    if (!Strings.isNullOrEmpty(this.mRemoteId))
      return;
    throw new InvalidDataException("Invalid track for upstream delete.");
  }

  public void validateForUpstreamInsert()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("TrackTombstone: validateForUpstreamInsert not supported");
  }

  public void validateForUpstreamUpdate()
    throws InvalidDataException
  {
    throw new UnsupportedOperationException("TrackTombstone: validateForUpstreamInsert not supported");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.TrackTombstone
 * JD-Core Version:    0.6.2
 */