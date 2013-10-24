package com.google.android.music.sync.google.model;

import android.content.Context;
import android.util.Log;
import com.google.android.common.base.Strings;
import com.google.android.music.cloudclient.JsonUtils;
import com.google.android.music.cloudclient.TrackJson;
import com.google.android.music.cloudclient.TrackJson.ImageRef;
import com.google.android.music.cloudclient.TrackJson.TrackOrigin;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.store.InvalidDataException;
import com.google.android.music.store.MusicFile;
import com.google.android.music.sync.api.MusicUrl;
import com.google.android.music.utils.AlbumArtUtils;
import java.io.IOException;
import java.util.List;

public class Track extends TrackJson
  implements MusicQueueableSyncEntity
{
  private MusicFile mMusicFile;

  public static Track filterForRatingsUpdate(Track paramTrack)
  {
    paramTrack.validateForUpstreamUpdate();
    Track localTrack = new Track();
    if (paramTrack.hasLockerId())
    {
      String str1 = paramTrack.mRemoteId;
      localTrack.mRemoteId = str1;
    }
    while (true)
    {
      String str2 = paramTrack.mRating;
      localTrack.mRating = str2;
      long l = paramTrack.mLastModifiedTimestamp;
      localTrack.mLastModifiedTimestamp = l;
      int i = paramTrack.mTrackType;
      localTrack.mTrackType = i;
      return localTrack;
      String str3 = paramTrack.getNormalizedNautilusId();
      localTrack.mNautilusId = str3;
    }
  }

  public static Track parse(MusicFile paramMusicFile)
  {
    Track localTrack = new Track();
    localTrack.mMusicFile = paramMusicFile;
    if (paramMusicFile.getSourceType() == 2)
    {
      String str1 = paramMusicFile.getSourceId();
      localTrack.mRemoteId = str1;
    }
    try
    {
      String str2 = paramMusicFile.getSourceVersion();
      if (str2 == null);
      long l3;
      for (localTrack.mLastModifiedTimestamp = 0L; ; localTrack.mLastModifiedTimestamp = l3)
      {
        String str3 = paramMusicFile.getTitle();
        localTrack.mTitle = str3;
        String str4 = paramMusicFile.getTrackArtist();
        localTrack.mArtist = str4;
        String str5 = paramMusicFile.getComposer();
        localTrack.mComposer = str5;
        String str6 = paramMusicFile.getAlbumName();
        localTrack.mAlbum = str6;
        String str7 = paramMusicFile.getAlbumArtist();
        localTrack.mAlbumArtist = str7;
        int i = paramMusicFile.getYear();
        localTrack.mYear = i;
        int j = paramMusicFile.getTrackPositionInAlbum();
        localTrack.mTrackNumber = j;
        String str8 = paramMusicFile.getGenre();
        localTrack.mGenre = str8;
        long l1 = paramMusicFile.getDurationInMilliSec();
        localTrack.mDurationMillis = l1;
        long l2 = paramMusicFile.getSize();
        localTrack.mEstimatedSize = l2;
        int k = paramMusicFile.getDiscPosition();
        localTrack.mDiscNumber = k;
        int m = paramMusicFile.getDiscCount();
        localTrack.mTotalDiscCount = m;
        if ((paramMusicFile.getTrackType() != 2) && (paramMusicFile.getTrackType() != 3))
          break label354;
        localTrack.mTrackType = 6;
        String str9 = Integer.toString(paramMusicFile.getRating());
        localTrack.mRating = str9;
        String str10 = paramMusicFile.getStoreSongId();
        localTrack.mStoreId = str10;
        String str11 = paramMusicFile.getAlbumMetajamId();
        localTrack.mAlbumId = str11;
        String str12 = paramMusicFile.getClientId();
        localTrack.mClientId = str12;
        return localTrack;
        if (paramMusicFile.getSourceType() != 3)
          break;
        String str13 = paramMusicFile.getSourceId();
        localTrack.mNautilusId = str13;
        break;
        l3 = Long.parseLong(str2);
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
      {
        Object[] arrayOfObject = new Object[1];
        String str14 = localTrack.mRemoteId;
        arrayOfObject[0] = str14;
        String str15 = String.format("Non-numeric version for music file with remoteId=%s.  Replacing with 0.", arrayOfObject);
        int n = Log.w("MusicSyncAdapter", str15, localNumberFormatException);
        localTrack.mLastModifiedTimestamp = 0L;
        continue;
        label354: if (paramMusicFile.getTrackType() == 1)
        {
          localTrack.mTrackType = 4;
        }
        else if (paramMusicFile.getTrackType() == 4)
        {
          localTrack.mTrackType = 7;
        }
        else if (paramMusicFile.getTrackType() == 5)
        {
          localTrack.mTrackType = 8;
        }
        else
        {
          int i1 = paramMusicFile.getTrackType();
          localTrack.mTrackType = i1;
        }
      }
    }
  }

  public MusicFile formatAsMusicFile(MusicFile paramMusicFile)
  {
    if (paramMusicFile == null)
      paramMusicFile = new MusicFile();
    String str1 = getEffectiveRemoteId();
    label43: int i;
    int j;
    if (str1 != null)
    {
      paramMusicFile.setSourceId(str1);
      if (this.mTitle == null)
        break label675;
      String str2 = this.mTitle;
      paramMusicFile.setTitle(str2);
      if (this.mArtist != null)
      {
        String str3 = this.mArtist;
        paramMusicFile.setTrackArtist(str3);
      }
      if (this.mComposer != null)
      {
        String str4 = this.mComposer;
        paramMusicFile.setComposer(str4);
      }
      if (this.mAlbum != null)
      {
        String str5 = this.mAlbum;
        paramMusicFile.setAlbumName(str5);
      }
      if (this.mAlbumArtist != null)
      {
        String str6 = this.mAlbumArtist;
        paramMusicFile.setAlbumArtist(str6);
      }
      if (this.mYear != -1)
      {
        short s1 = Integer.valueOf(this.mYear).shortValue();
        paramMusicFile.setYear(s1);
      }
      if (this.mTrackNumber != -1)
      {
        short s2 = Integer.valueOf(this.mTrackNumber).shortValue();
        paramMusicFile.setTrackPositionInAlbum(s2);
      }
      if (this.mGenre != null)
      {
        String str7 = this.mGenre;
        paramMusicFile.setGenre(str7);
      }
      if (this.mDurationMillis != 65535L)
      {
        long l1 = this.mDurationMillis;
        paramMusicFile.setDurationInMilliSec(l1);
      }
      if (this.mCreationTimestamp != 65535L)
      {
        long l2 = this.mCreationTimestamp / 1000L;
        paramMusicFile.setAddedTime(l2);
      }
      if (this.mLastModifiedTimestamp != 65535L)
      {
        String str8 = Long.toString(this.mLastModifiedTimestamp);
        paramMusicFile.setSourceVersion(str8);
      }
      if (this.mEstimatedSize != 65535L)
      {
        long l3 = this.mEstimatedSize;
        paramMusicFile.setSize(l3);
      }
      if (this.mTotalDiscCount != -1)
      {
        short s3 = Integer.valueOf(this.mTotalDiscCount).shortValue();
        paramMusicFile.setDiscCount(s3);
      }
      if (this.mDiscNumber != -1)
      {
        short s4 = Integer.valueOf(this.mDiscNumber).shortValue();
        paramMusicFile.setDiscPosition(s4);
      }
      i = -1;
      if ((this.mTrackOrigin != null) && (this.mTrackOrigin.size() > 0) && (((TrackJson.TrackOrigin)this.mTrackOrigin.get(0)).mValue == 4))
        i = 4;
      if (this.mTrackType == -1)
        break label757;
      if (this.mTrackType != 6)
        break label691;
      if (i != 4)
        break label685;
      j = 3;
      label418: paramMusicFile.setTrackType(j);
      label424: int k = getRatingAsInt();
      if (k != -1)
        paramMusicFile.setRating(k);
      if ((this.mAlbumArtRef != null) && (!this.mAlbumArtRef.isEmpty()))
      {
        String str9 = ((TrackJson.ImageRef)this.mAlbumArtRef.get(0)).mUrl;
        paramMusicFile.setAlbumArtLocation(str9);
      }
      if ((this.mArtistArtRef != null) && (!this.mArtistArtRef.isEmpty()))
      {
        String str10 = ((TrackJson.ImageRef)this.mArtistArtRef.get(0)).mUrl;
        paramMusicFile.setArtistArtLocation(str10);
      }
      if (this.mStoreId != null)
      {
        String str11 = this.mStoreId;
        paramMusicFile.setStoreSongId(str11);
      }
      if (this.mAlbumId != null)
      {
        String str12 = this.mAlbumId;
        paramMusicFile.setAlbumMetajamId(str12);
      }
      paramMusicFile.setFileType(5);
      ContentIdentifier.Domain localDomain1 = getDomain();
      paramMusicFile.setDomain(localDomain1);
      String str13 = this.mClientId;
      paramMusicFile.setClientId(str13);
      if (!hasLockerId())
        break label783;
      paramMusicFile.setSourceType(2);
    }
    while (true)
    {
      String str14 = getNormalizedNautilusId();
      paramMusicFile.setTrackMetajamId(str14);
      if ((this.mArtistId != null) && (!this.mArtistId.isEmpty()))
      {
        String str15 = (String)this.mArtistId.get(0);
        paramMusicFile.setArtistMetajamId(str15);
      }
      return paramMusicFile;
      int m = Log.e("MusicSyncAdapter", "Exporting a track to a music file, but no canonical id defined.");
      break;
      label675: this.mTitle = "";
      break label43;
      label685: j = 2;
      break label418;
      label691: if (this.mTrackType == 4)
      {
        if (i == 4);
        for (j = 3; ; j = 1)
        {
          paramMusicFile.setTrackType(j);
          break;
        }
      }
      if (this.mTrackType == 7)
      {
        paramMusicFile.setTrackType(4);
        break label424;
      }
      if (this.mTrackType != 8)
        break label424;
      paramMusicFile.setTrackType(5);
      break label424;
      label757: ContentIdentifier.Domain localDomain2 = getDomain();
      ContentIdentifier.Domain localDomain3 = ContentIdentifier.Domain.NAUTILUS;
      if (localDomain2 != localDomain3)
        break label424;
      paramMusicFile.setTrackType(4);
      break label424;
      label783: paramMusicFile.setSourceType(3);
    }
  }

  public MusicUrl getBatchMutationUrl(Context paramContext)
  {
    return MusicUrl.forTracksBatchMutation();
  }

  public long getCreationTimestamp()
  {
    return this.mCreationTimestamp;
  }

  public MusicFile getEncapsulatedMusicFile()
  {
    return this.mMusicFile;
  }

  public MusicUrl getFeedUrl(Context paramContext)
  {
    return MusicUrl.forTracksFeed(AlbumArtUtils.getMaxAlbumArtSize(paramContext));
  }

  public MusicUrl getFeedUrlAsPost(Context paramContext)
  {
    return MusicUrl.forTracksFeedAsPost(AlbumArtUtils.getMaxAlbumArtSize(paramContext));
  }

  public long getLastModifiedTimestamp()
  {
    return this.mLastModifiedTimestamp;
  }

  public long getLocalId()
  {
    if (this.mMusicFile == null);
    for (long l = 65535L; ; l = this.mMusicFile.getLocalId())
      return l;
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
    return this.mIsDeleted;
  }

  public boolean isInsert()
  {
    if ((!hasLockerId()) && (this.mTrackType == 8));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isUpdate()
  {
    if ((!this.mIsDeleted) && ((hasLockerId()) || (hasNautilusId())));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public byte[] serializeAsJson()
    throws InvalidDataException
  {
    try
    {
      byte[] arrayOfByte = JsonUtils.toJsonByteArray(this);
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a track as JSON: ");
      String str1 = toString();
      String str2 = str1 + ": ";
      int i = Log.e("MusicSyncAdapter", str2, localIOException);
      throw new InvalidDataException("Unable to serialize track for upstream sync.", localIOException);
    }
  }

  public void setCreationTimestamp(long paramLong)
  {
    this.mCreationTimestamp = paramLong;
  }

  public void setIsDeleted(boolean paramBoolean)
  {
    this.mIsDeleted = paramBoolean;
  }

  public void setLastModifiedTimestamp(long paramLong)
  {
    this.mLastModifiedTimestamp = paramLong;
  }

  public void setNautilusId(String paramString)
  {
    this.mNautilusId = paramString;
  }

  public void setRemoteId(String paramString)
  {
    this.mRemoteId = paramString;
  }

  public void validateForUpstreamDelete()
  {
    throw new UnsupportedOperationException("Track deletes should be done through TrackTombstone.");
  }

  public void validateForUpstreamInsert()
  {
    if ((!hasLockerId()) && (hasNautilusId()))
      return;
    throw new UnsupportedOperationException("Only nautilus track can sync upstream.");
  }

  public void validateForUpstreamUpdate()
  {
    if (((!Strings.isNullOrEmpty(this.mRemoteId)) || (hasNautilusId())) && (getRatingAsInt() >= 0) && (getRatingAsInt() <= 5))
      return;
    throw new InvalidDataException("Invalid track for upstream update.");
  }

  public void wipeAllFields()
  {
    super.wipeAllFields();
    this.mMusicFile = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.Track
 * JD-Core Version:    0.6.2
 */