package com.google.android.music.sync.google.model;

import android.content.Context;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.sync.common.QueueableSyncEntity;
import java.util.List;

public abstract interface MusicQueueableSyncEntity extends QueueableSyncEntity
{
  public abstract MusicUrl getBatchMutationUrl(Context paramContext);

  public abstract MusicUrl getFeedUrl(Context paramContext);

  public abstract MusicUrl getFeedUrlAsPost(Context paramContext);

  public abstract long getLocalId();

  public abstract String getRemoteId();

  public abstract MusicUrl getUrl(Context paramContext, String paramString);

  public abstract boolean isDeleted();

  public abstract boolean isInsert();

  public abstract boolean isUpdate();

  public abstract byte[] serializeAsJson()
    throws InvalidDataException;

  public abstract void setIsDeleted(boolean paramBoolean);

  public abstract void setRemoteId(String paramString);

  public abstract void validateForUpstreamDelete()
    throws InvalidDataException;

  public abstract void validateForUpstreamInsert()
    throws InvalidDataException;

  public abstract void validateForUpstreamUpdate()
    throws InvalidDataException;

  public static abstract interface Feed<T extends MusicQueueableSyncEntity>
  {
    public abstract List<T> getItemList();

    public abstract String getNextPageToken();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.MusicQueueableSyncEntity
 * JD-Core Version:    0.6.2
 */