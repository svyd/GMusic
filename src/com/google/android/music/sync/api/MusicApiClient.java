package com.google.android.music.sync.api;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.sync.common.QueueableSyncEntity;
import com.google.android.music.sync.common.SyncHttpException;
import com.google.android.music.sync.google.model.BatchMutateResponse.MutateResponse;
import com.google.android.music.sync.google.model.ConfigEntry;
import com.google.android.music.sync.google.model.MusicQueueableSyncEntity;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncablePlaylistEntry;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public abstract interface MusicApiClient
{
  public abstract GetResult<ConfigEntry> getConfig(Account paramAccount, boolean paramBoolean)
    throws SyncHttpException, IOException, AuthenticatorException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException, NotModifiedException;

  public abstract SyncablePlaylist getPlaylist(Account paramAccount, String paramString)
    throws SyncHttpException, IOException, AuthenticatorException, NotModifiedException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException;

  public abstract GetResult<SyncablePlaylistEntry> getPlaylistEntries(Account paramAccount, int paramInt, String paramString1, long paramLong, String paramString2)
    throws SyncHttpException, IOException, AuthenticatorException, NotModifiedException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException;

  public abstract SyncablePlaylistEntry getPlaylistEntry(Account paramAccount, String paramString)
    throws SyncHttpException, IOException, AuthenticatorException, NotModifiedException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException;

  public abstract GetResult<SyncablePlaylist> getPlaylists(Account paramAccount, int paramInt, String paramString1, long paramLong, String paramString2)
    throws SyncHttpException, IOException, AuthenticatorException, NotModifiedException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException;

  public abstract GetResult<SyncableRadioStation> getRadioStations(Account paramAccount, int paramInt, String paramString1, long paramLong, String paramString2)
    throws SyncHttpException, IOException, AuthenticatorException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException, NotModifiedException;

  public abstract Track getTrack(Account paramAccount, String paramString)
    throws SyncHttpException, IOException, AuthenticatorException, NotModifiedException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException;

  public abstract GetResult<Track> getTracks(Account paramAccount, int paramInt, String paramString1, long paramLong, String paramString2)
    throws SyncHttpException, IOException, AuthenticatorException, NotModifiedException, BadRequestException, ForbiddenException, ServiceUnavailableException, ResourceNotFoundException;

  public abstract <T extends MusicQueueableSyncEntity> void mutateItem(Account paramAccount, T paramT, OpType paramOpType)
    throws SyncHttpException, IOException, AuthenticatorException, InvalidDataException, ConflictException, BadRequestException, ForbiddenException, ResourceNotFoundException, ServiceUnavailableException;

  public abstract <T extends MusicQueueableSyncEntity> List<BatchMutateResponse.MutateResponse> mutateItems(Account paramAccount, List<T> paramList)
    throws SyncHttpException, IOException, AuthenticatorException, InvalidDataException, BadRequestException, ForbiddenException, ResourceNotFoundException, ServiceUnavailableException;

  public abstract void reportTrackStats(Account paramAccount, List<? extends QueueableSyncEntity> paramList)
    throws SyncHttpException, IOException, AuthenticatorException, BadRequestException, ForbiddenException, ServiceUnavailableException, ConflictException, ResourceNotFoundException, NotModifiedException;

  public static enum OpType
  {
    static
    {
      DELETE = new OpType("DELETE", 2);
      OpType[] arrayOfOpType = new OpType[3];
      OpType localOpType1 = INSERT;
      arrayOfOpType[0] = localOpType1;
      OpType localOpType2 = UPDATE;
      arrayOfOpType[1] = localOpType2;
      OpType localOpType3 = DELETE;
      arrayOfOpType[2] = localOpType3;
    }
  }

  public static class GetResult<T extends MusicQueueableSyncEntity>
  {
    public String mContinuationToken;
    public String mEtag;
    public Iterator<T> mItems;

    public GetResult(Iterator<T> paramIterator, String paramString1, String paramString2)
    {
      this.mItems = paramIterator;
      this.mContinuationToken = paramString1;
      this.mEtag = paramString2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.api.MusicApiClient
 * JD-Core Version:    0.6.2
 */