package com.google.android.music.sync.google.model;

import android.util.Log;
import com.google.android.music.store.InvalidDataException;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.Json;
import com.google.api.client.util.Key;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

public class MagicPlaylistRequest extends GenericJson
{

  @Key("includeAllEntries")
  public boolean mIncludeAllEntries = true;

  @Key("name")
  public String mName;

  @Key("numRecommendations")
  public int mNumEntries = 25;

  @Key("seed")
  public List<MagicPlaylistSeed> mSeeds;

  public static Builder newBuilder()
  {
    return new Builder();
  }

  public boolean isValid()
  {
    boolean bool = false;
    if ((this.mName == null) || (this.mName.length() == 0));
    while (true)
    {
      return bool;
      if ((this.mSeeds != null) && (!this.mSeeds.isEmpty()))
      {
        Iterator localIterator = this.mSeeds.iterator();
        while (true)
          if (localIterator.hasNext())
          {
            MagicPlaylistSeed localMagicPlaylistSeed = (MagicPlaylistSeed)localIterator.next();
            if ((!MagicPlaylistRequest.MagicPlaylistSeed.SeedType.isValidOrdinal(localMagicPlaylistSeed.mSeedType)) || (localMagicPlaylistSeed.mSeedValue == null))
              break;
            if (localMagicPlaylistSeed.mSeedValue.length() == 0)
              break;
          }
        bool = true;
      }
    }
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
        localJsonGenerator2.close();
        byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
        if (Log.isLoggable("MusicSyncAdapter", 2))
        {
          String str1 = localByteArrayOutputStream.toString();
          int i = Log.v("MusicSyncAdapter", str1);
        }
        return arrayOfByte;
      }
      finally
      {
        localJsonGenerator2.close();
      }
    }
    catch (IOException localIOException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unable to serialize a magic playlist request as JSON: ");
      String str2 = toString();
      String str3 = str2 + ": ";
      int j = Log.e("MusicSyncAdapter", str3, localIOException);
      throw new InvalidDataException("Unable to serialize a magic playlist request.", localIOException);
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("name:");
    String str1 = this.mName;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("; numRecommendations:");
    int i = this.mNumEntries;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(i).append("; includeAllEntries:");
    boolean bool = this.mIncludeAllEntries;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(bool).append("; seeds:");
    String str2 = this.mSeeds.toString();
    return str2;
  }

  public static class MagicPlaylistSeed extends GenericJson
  {

    @Key("seedType")
    public int mSeedType;

    @Key("seed")
    public String mSeedValue;

    public static Builder newBuilder()
    {
      return new Builder();
    }

    public static enum SeedType
    {
      private int mOrdinal;

      static
      {
        ARTIST = new SeedType("ARTIST", 1, 1);
        ALBUM = new SeedType("ALBUM", 2, 2);
        SeedType[] arrayOfSeedType = new SeedType[3];
        SeedType localSeedType1 = TRACK;
        arrayOfSeedType[0] = localSeedType1;
        SeedType localSeedType2 = ARTIST;
        arrayOfSeedType[1] = localSeedType2;
        SeedType localSeedType3 = ALBUM;
        arrayOfSeedType[2] = localSeedType3;
      }

      private SeedType(int paramInt)
      {
        this.mOrdinal = paramInt;
      }

      public static boolean isValidOrdinal(int paramInt)
      {
        boolean bool = true;
        if ((paramInt == 0) || (paramInt == 1) || (paramInt == 2));
        while (true)
        {
          return bool;
          bool = false;
        }
      }
    }

    public static class Builder
    {
      private MagicPlaylistRequest.MagicPlaylistSeed mSeed;

      public Builder()
      {
        MagicPlaylistRequest.MagicPlaylistSeed localMagicPlaylistSeed = new MagicPlaylistRequest.MagicPlaylistSeed();
        this.mSeed = localMagicPlaylistSeed;
      }
    }
  }

  public static class Builder
  {
    private MagicPlaylistRequest mPlaylistRequest;

    public Builder()
    {
      MagicPlaylistRequest localMagicPlaylistRequest = new MagicPlaylistRequest();
      this.mPlaylistRequest = localMagicPlaylistRequest;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.model.MagicPlaylistRequest
 * JD-Core Version:    0.6.2
 */