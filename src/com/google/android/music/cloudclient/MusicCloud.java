package com.google.android.music.cloudclient;

import android.accounts.Account;
import com.google.android.music.sync.api.MusicUrl.ExploreTabType;
import com.google.android.music.sync.google.model.SyncablePlaylist;
import com.google.android.music.sync.google.model.SyncableRadioStation;
import com.google.android.music.sync.google.model.Track;
import com.google.android.music.sync.google.model.TrackFeed;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.IOException;
import java.util.List;

public abstract interface MusicCloud
{
  public static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CLOUD_CLIENT);

  public abstract RadioEditStationsResponse createRadioStation(SyncableRadioStation paramSyncableRadioStation, boolean paramBoolean, int paramInt1, int paramInt2)
    throws InterruptedException, IOException;

  public abstract TrackFeed getEphemeralTopTracks(int paramInt1, String paramString, int paramInt2)
    throws InterruptedException, IOException;

  public abstract MusicGenresResponseJson getGenres(String paramString)
    throws InterruptedException, IOException;

  public abstract RadioFeedResponse getLuckyRadioFeed(int paramInt1, List<MixTrackId> paramList, int paramInt2)
    throws InterruptedException, IOException;

  public abstract RadioFeedResponse getMixesFeed(int paramInt1, int paramInt2, int paramInt3)
    throws InterruptedException, IOException;

  public abstract AlbumJson getNautilusAlbum(String paramString)
    throws InterruptedException, IOException;

  public abstract ArtistJson getNautilusArtist(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
    throws InterruptedException, IOException;

  public abstract Track getNautilusTrack(String paramString)
    throws InterruptedException, IOException;

  public abstract NewReleasesResponseJson getNewReleases(int paramInt)
    throws InterruptedException, IOException;

  public abstract OffersResponseJson getOffersForAccount(Account paramAccount)
    throws InterruptedException, IOException;

  public abstract SyncablePlaylist getPlaylist(String paramString)
    throws InterruptedException, IOException;

  public abstract RadioFeedResponse getRadioFeed(String paramString, int paramInt1, List<MixTrackId> paramList, int paramInt2)
    throws InterruptedException, IOException;

  public abstract GetSharedPlaylistEntriesResponseJson getSharedEntries(String paramString1, int paramInt, String paramString2, long paramLong)
    throws InterruptedException, IOException;

  public abstract TabJson getTab(MusicUrl.ExploreTabType paramExploreTabType, int paramInt, String paramString)
    throws InterruptedException, IOException;

  public abstract SearchClientResponseJson search(String paramString1, String paramString2, int paramInt)
    throws InterruptedException, IOException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.cloudclient.MusicCloud
 * JD-Core Version:    0.6.2
 */