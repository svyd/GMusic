package com.google.android.music.sync.google;

import android.accounts.Account;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.android.music.sync.common.ProviderException;
import com.google.common.io.protocol.ProtoBuf;
import com.google.common.io.protocol.ProtoBufType;
import java.io.IOException;

public class ClientSyncState
{
  private static final ProtoBufType type = new ProtoBufType("MusicMetadataSyncState");
  final String mEtagPlaylist;
  final String mEtagPlaylistEntry;
  final String mEtagTrack;
  final Integer mRemoteAccount;
  final Long mRemotePlaylistVersion;
  final Long mRemotePlentryVersion;
  final Long mRemoteRadioStationVersion;
  final Long mRemoteTrackVersion;

  static
  {
    ProtoBufType localProtoBufType1 = type.addElement(533, 1, null);
    ProtoBufType localProtoBufType2 = type.addElement(531, 2, null);
    ProtoBufType localProtoBufType3 = type.addElement(531, 3, null);
    ProtoBufType localProtoBufType4 = type.addElement(531, 4, null);
    ProtoBufType localProtoBufType5 = type.addElement(548, 5, null);
    ProtoBufType localProtoBufType6 = type.addElement(548, 6, null);
    ProtoBufType localProtoBufType7 = type.addElement(548, 7, null);
    ProtoBufType localProtoBufType8 = type.addElement(531, 8, null);
  }

  private ClientSyncState(Integer paramInteger, Long paramLong1, Long paramLong2, Long paramLong3, Long paramLong4, String paramString1, String paramString2, String paramString3)
  {
    this.mRemoteAccount = paramInteger;
    this.mRemoteTrackVersion = paramLong1;
    this.mRemotePlaylistVersion = paramLong2;
    this.mRemotePlentryVersion = paramLong3;
    this.mRemoteRadioStationVersion = paramLong4;
    this.mEtagTrack = paramString1;
    this.mEtagPlaylist = paramString2;
    this.mEtagPlaylistEntry = paramString3;
  }

  public static Builder newBuilder()
  {
    return new Builder(null);
  }

  public static Builder newBuilder(ClientSyncState paramClientSyncState)
  {
    return new Builder(paramClientSyncState, null);
  }

  public static ClientSyncState parseFrom(byte[] paramArrayOfByte)
    throws ProviderException
  {
    ProtoBufType localProtoBufType = type;
    ProtoBuf localProtoBuf1 = new ProtoBuf(localProtoBufType);
    while (true)
    {
      try
      {
        ProtoBuf localProtoBuf2 = localProtoBuf1.parse(paramArrayOfByte);
        if (localProtoBuf1.has(1))
        {
          localInteger = Integer.valueOf(localProtoBuf1.getInt(1));
          if (!localProtoBuf1.has(2))
            break label202;
          localLong1 = Long.valueOf(localProtoBuf1.getLong(2));
          if (!localProtoBuf1.has(3))
            break label208;
          localLong2 = Long.valueOf(localProtoBuf1.getLong(3));
          if (!localProtoBuf1.has(4))
            break label214;
          localLong3 = Long.valueOf(localProtoBuf1.getLong(4));
          if (!localProtoBuf1.has(8))
            break label220;
          localLong4 = Long.valueOf(localProtoBuf1.getLong(8));
          if (!localProtoBuf1.has(5))
            break label226;
          str1 = localProtoBuf1.getString(5);
          if (!localProtoBuf1.has(6))
            break label232;
          str2 = localProtoBuf1.getString(6);
          if (!localProtoBuf1.has(7))
            break label238;
          str3 = localProtoBuf1.getString(7);
          return new ClientSyncState(localInteger, localLong1, localLong2, localLong3, localLong4, str1, str2, str3);
        }
      }
      catch (IOException localIOException)
      {
        throw new ProviderException(localIOException);
      }
      Integer localInteger = null;
      continue;
      label202: Long localLong1 = null;
      continue;
      label208: Long localLong2 = null;
      continue;
      label214: Long localLong3 = null;
      continue;
      label220: Long localLong4 = null;
      continue;
      label226: String str1 = null;
      continue;
      label232: String str2 = null;
      continue;
      label238: String str3 = null;
    }
  }

  public Integer getRemoteAccount()
  {
    return this.mRemoteAccount;
  }

  public byte[] toBytes()
    throws ProviderException
  {
    ProtoBufType localProtoBufType = type;
    ProtoBuf localProtoBuf = new ProtoBuf(localProtoBufType);
    if (this.mRemoteAccount != null)
    {
      int i = this.mRemoteAccount.intValue();
      localProtoBuf.addInt(1, i);
    }
    if (this.mRemoteTrackVersion != null)
    {
      long l1 = this.mRemoteTrackVersion.longValue();
      localProtoBuf.addLong(2, l1);
    }
    if (this.mRemotePlaylistVersion != null)
    {
      long l2 = this.mRemotePlaylistVersion.longValue();
      localProtoBuf.addLong(3, l2);
    }
    if (this.mRemotePlentryVersion != null)
    {
      long l3 = this.mRemotePlentryVersion.longValue();
      localProtoBuf.addLong(4, l3);
    }
    if (this.mRemoteRadioStationVersion != null)
    {
      long l4 = this.mRemoteRadioStationVersion.longValue();
      localProtoBuf.addLong(8, l4);
    }
    if (this.mEtagTrack != null)
    {
      String str1 = this.mEtagTrack;
      localProtoBuf.addString(5, str1);
    }
    if (this.mEtagPlaylist != null)
    {
      String str2 = this.mEtagPlaylist;
      localProtoBuf.addString(6, str2);
    }
    if (this.mEtagPlaylistEntry != null)
    {
      String str3 = this.mEtagPlaylistEntry;
      localProtoBuf.addString(7, str3);
    }
    try
    {
      byte[] arrayOfByte = localProtoBuf.toByteArray();
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new ProviderException(localIOException);
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder().append("remoteAccount:");
    Integer localInteger = this.mRemoteAccount;
    StringBuilder localStringBuilder2 = localStringBuilder1.append(localInteger).append("; remoteTrackVersion: ");
    Long localLong1 = this.mRemoteTrackVersion;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(localLong1).append("; remotePlaylistVersion: ");
    Long localLong2 = this.mRemotePlaylistVersion;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(localLong2).append("; remotePlaylistEntryVersion: ");
    Long localLong3 = this.mRemotePlentryVersion;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(localLong3).append("; remoteRadioStationVersion: ");
    Long localLong4 = this.mRemoteRadioStationVersion;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(localLong4).append("; etagTrack: ");
    String str1 = this.mEtagTrack;
    StringBuilder localStringBuilder7 = localStringBuilder6.append(str1).append("; etagPlaylist: ");
    String str2 = this.mEtagPlaylist;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str2).append("; etagPlaylistEntry: ");
    String str3 = this.mEtagPlaylistEntry;
    return str3;
  }

  public static class Helpers
  {
    public static Account get(SQLiteDatabase paramSQLiteDatabase, int paramInt)
      throws ProviderException
    {
      String[] arrayOfString1 = new String[3];
      arrayOfString1[0] = "data";
      arrayOfString1[1] = "account_name";
      arrayOfString1[2] = "account_type";
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      String[] arrayOfString2 = null;
      String str1 = null;
      String str2 = null;
      String str3 = null;
      Cursor localCursor = localSQLiteDatabase.query("_sync_state", arrayOfString1, null, arrayOfString2, str1, str2, str3);
      if (localCursor != null);
      try
      {
        while (localCursor.moveToNext())
          if (ClientSyncState.parseFrom(localCursor.getBlob(0)).getRemoteAccount().intValue() != paramInt)
          {
            String str4 = localCursor.getString(1);
            String str5 = localCursor.getString(2);
            Account localAccount = new Account(str4, str5);
            return localAccount;
          }
        String str6 = "Sync state for account hash " + paramInt + " is not found";
        throw new ProviderException(str6);
      }
      finally
      {
        if (localCursor != null)
          localCursor.close();
      }
    }

    public static ClientSyncState get(SQLiteDatabase paramSQLiteDatabase, Account paramAccount)
      throws ProviderException
    {
      Object localObject1 = null;
      String[] arrayOfString1 = new String[1];
      arrayOfString1[0] = "data";
      String[] arrayOfString2 = new String[2];
      String str1 = paramAccount.name;
      arrayOfString2[0] = str1;
      String str2 = paramAccount.type;
      arrayOfString2[1] = str2;
      SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
      Object localObject2 = localObject1;
      Object localObject3 = localObject1;
      Cursor localCursor = localSQLiteDatabase.query("_sync_state", arrayOfString1, "account_name=? AND account_type=?", arrayOfString2, (String)localObject1, localObject2, localObject3);
      while (true)
      {
        try
        {
          boolean bool = localCursor.moveToFirst();
          if (bool == null)
            return localObject1;
          if (localCursor.getCount() > 1)
            throw new ProviderException("There are multiple sync state entries for this account.");
        }
        finally
        {
          if (localCursor != null)
            localCursor.close();
        }
        int i = 0;
        ClientSyncState localClientSyncState = ClientSyncState.parseFrom(localCursor.getBlob(i));
        localObject1 = localClientSyncState;
        if (localCursor != null)
          localCursor.close();
      }
    }

    public static void set(SQLiteDatabase paramSQLiteDatabase, Account paramAccount, ClientSyncState paramClientSyncState)
      throws ProviderException
    {
      ContentValues localContentValues = new ContentValues();
      String str1 = paramAccount.name;
      localContentValues.put("account_name", str1);
      String str2 = paramAccount.type;
      localContentValues.put("account_type", str2);
      byte[] arrayOfByte = paramClientSyncState.toBytes();
      localContentValues.put("data", arrayOfByte);
      long l = paramSQLiteDatabase.replace("_sync_state", "data", localContentValues);
      if (!Log.isLoggable("MusicSyncAdapter", 2))
        return;
      StringBuilder localStringBuilder = new StringBuilder().append("Setting sync state: ");
      String str3 = paramClientSyncState.toString();
      String str4 = str3;
      int i = Log.v("MusicSyncAdapter", str4);
    }
  }

  public static class Builder
  {
    private String mEtagPlaylist;
    private String mEtagPlaylistEntry;
    private String mEtagTrack;
    private Integer mRemoteAccount;
    private Long mRemotePlaylistVersion;
    private Long mRemotePlentryVersion;
    private Long mRemoteRadioStationVersion;
    private Long mRemoteTrackVersion;

    private Builder()
    {
    }

    private Builder(ClientSyncState paramClientSyncState)
    {
      Long localLong1 = paramClientSyncState.mRemoteTrackVersion;
      this.mRemoteTrackVersion = localLong1;
      Long localLong2 = paramClientSyncState.mRemotePlaylistVersion;
      this.mRemotePlaylistVersion = localLong2;
      Long localLong3 = paramClientSyncState.mRemotePlentryVersion;
      this.mRemotePlentryVersion = localLong3;
      Integer localInteger = paramClientSyncState.mRemoteAccount;
      this.mRemoteAccount = localInteger;
      String str1 = paramClientSyncState.mEtagPlaylist;
      this.mEtagPlaylist = str1;
      String str2 = paramClientSyncState.mEtagPlaylistEntry;
      this.mEtagPlaylistEntry = str2;
      String str3 = paramClientSyncState.mEtagTrack;
      this.mEtagTrack = str3;
      Long localLong4 = paramClientSyncState.mRemoteRadioStationVersion;
      this.mRemoteRadioStationVersion = localLong4;
    }

    public ClientSyncState build()
    {
      Integer localInteger = this.mRemoteAccount;
      Long localLong1 = this.mRemoteTrackVersion;
      Long localLong2 = this.mRemotePlaylistVersion;
      Long localLong3 = this.mRemotePlentryVersion;
      Long localLong4 = this.mRemoteRadioStationVersion;
      String str1 = this.mEtagTrack;
      String str2 = this.mEtagPlaylist;
      String str3 = this.mEtagPlaylistEntry;
      return new ClientSyncState(localInteger, localLong1, localLong2, localLong3, localLong4, str1, str2, str3, null);
    }

    public Builder setEtagPlaylist(String paramString)
    {
      this.mEtagPlaylist = paramString;
      return this;
    }

    public Builder setEtagPlaylistEntry(String paramString)
    {
      this.mEtagPlaylistEntry = paramString;
      return this;
    }

    public Builder setEtagTrack(String paramString)
    {
      this.mEtagTrack = paramString;
      return this;
    }

    public Builder setRemoteAccount(int paramInt)
    {
      Integer localInteger = Integer.valueOf(paramInt);
      this.mRemoteAccount = localInteger;
      return this;
    }

    public Builder setRemotePlaylistVersion(long paramLong)
    {
      Long localLong = Long.valueOf(paramLong);
      this.mRemotePlaylistVersion = localLong;
      return this;
    }

    public Builder setRemotePlentryVersion(long paramLong)
    {
      Long localLong = Long.valueOf(paramLong);
      this.mRemotePlentryVersion = localLong;
      return this;
    }

    public Builder setRemoteRadioStationVersion(long paramLong)
    {
      Long localLong = Long.valueOf(paramLong);
      this.mRemoteRadioStationVersion = localLong;
      return this;
    }

    public Builder setRemoteTrackVersion(long paramLong)
    {
      Long localLong = Long.valueOf(paramLong);
      this.mRemoteTrackVersion = localLong;
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.ClientSyncState
 * JD-Core Version:    0.6.2
 */