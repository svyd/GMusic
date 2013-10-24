package com.google.android.music.store;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class RadioStation extends Syncable
{
  private static final String[] PROJECTION;
  private static final String[] TOMBSTONE_PROJECTION = arrayOfString2;
  private static int TOMBSTONE_PROJECTION_INDEX_ID = 0;
  private static int TOMBSTONE_PROJECTION_SOURCE_ID = 1;
  private static int TOMBSTONE_PROJECTION_SOURCE_VERSION = 2;
  private String mArtworkLocation;
  private int mArtworkType;
  private String mClientId;
  private String mDescription;
  private long mId;
  private String mName;
  private long mRecentTimestampMicrosec;
  private String mSeedSourceId;
  private int mSeedSourceType;

  static
  {
    String[] arrayOfString1 = new String[13];
    arrayOfString1[0] = "RADIO_STATIONS.Id";
    arrayOfString1[1] = "RADIO_STATIONS.ClientId";
    arrayOfString1[2] = "RADIO_STATIONS.Name";
    arrayOfString1[3] = "RADIO_STATIONS.Description";
    arrayOfString1[4] = "RADIO_STATIONS.RecentTimestamp";
    arrayOfString1[5] = "RADIO_STATIONS.ArtworkLocation";
    arrayOfString1[6] = "RADIO_STATIONS.ArtworkType";
    arrayOfString1[7] = "RADIO_STATIONS.SeedSourceId";
    arrayOfString1[8] = "RADIO_STATIONS.SeedSourceType";
    arrayOfString1[9] = "RADIO_STATIONS._sync_dirty";
    arrayOfString1[10] = "RADIO_STATIONS._sync_version";
    arrayOfString1[11] = "RADIO_STATIONS.SourceAccount";
    arrayOfString1[12] = "RADIO_STATIONS.SourceId";
    PROJECTION = arrayOfString1;
    String[] arrayOfString2 = new String[3];
    arrayOfString2[0] = "Id";
    arrayOfString2[1] = "SourceId";
    arrayOfString2[2] = "_sync_version";
  }

  public static SQLiteStatement compileDeleteStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("delete from RADIO_STATIONS where SourceAccount=? AND SourceId=?");
  }

  public static SQLiteStatement compileInsertStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("insert into RADIO_STATIONS ( ClientId, Name, Description, RecentTimestamp, ArtworkLocation, ArtworkType, SeedSourceId, SeedSourceType, _sync_dirty, _sync_version, SourceAccount, SourceId) values (?,?,?,?,?,?,?,?,?,?,?,?)");
  }

  public static SQLiteStatement compileUpdateStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("update RADIO_STATIONS set ClientId=?, Name=?, Description=?, RecentTimestamp=?, ArtworkLocation=?, ArtworkType=?, SeedSourceId=?, SeedSourceType=?, _sync_dirty=?, _sync_version=?, SourceAccount=?,SourceId=? where Id=?");
  }

  public static void deleteById(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    String[] arrayOfString = new String[1];
    String str = Long.toString(paramLong);
    arrayOfString[0] = str;
    int i = paramSQLiteDatabase.delete("RADIO_STATIONS", "Id=?", arrayOfString);
  }

  public static void deleteBySourceInfo(SQLiteStatement paramSQLiteStatement, int paramInt, String paramString)
  {
    paramSQLiteStatement.clearBindings();
    long l = paramInt;
    paramSQLiteStatement.bindLong(1, l);
    paramSQLiteStatement.bindString(2, paramString);
    paramSQLiteStatement.execute();
  }

  public static Cursor getRadioStationTombstones(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString1 = TOMBSTONE_PROJECTION;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("RADIO_STATION_TOMBSTONES", arrayOfString1, null, arrayOfString2, str1, str2, str3);
  }

  public static Cursor getRadioStationsToSync(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString = PROJECTION;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("RADIO_STATIONS", arrayOfString, "RADIO_STATIONS._sync_dirty=1", null, str1, str2, str3);
  }

  private void prepareInsertOrFullUpdate(SQLiteStatement paramSQLiteStatement)
  {
    paramSQLiteStatement.clearBindings();
    if (this.mClientId == null)
      throw new InvalidDataException("Client id must be set before storing");
    String str1 = this.mClientId;
    paramSQLiteStatement.bindString(1, str1);
    if (this.mName == null)
      throw new InvalidDataException("Name must be set before storing");
    String str2 = this.mName;
    paramSQLiteStatement.bindString(2, str2);
    if (this.mDescription == null)
    {
      paramSQLiteStatement.bindNull(3);
      long l1 = this.mRecentTimestampMicrosec;
      paramSQLiteStatement.bindLong(4, l1);
      if (this.mArtworkLocation != null)
        break label145;
      paramSQLiteStatement.bindNull(5);
    }
    while (true)
    {
      long l2 = this.mArtworkType;
      paramSQLiteStatement.bindLong(6, l2);
      if (this.mSeedSourceId != null)
        break label161;
      throw new InvalidDataException("Seed source id must be set before storing");
      String str3 = this.mDescription;
      paramSQLiteStatement.bindString(3, str3);
      break;
      label145: String str4 = this.mArtworkLocation;
      paramSQLiteStatement.bindString(5, str4);
    }
    label161: String str5 = this.mSeedSourceId;
    paramSQLiteStatement.bindString(7, str5);
    long l3 = this.mSeedSourceType;
    paramSQLiteStatement.bindLong(8, l3);
    int i = 9;
    long l4;
    if (this.mNeedsSync)
    {
      l4 = 1L;
      paramSQLiteStatement.bindLong(i, l4);
      if (this.mSourceVersion != null)
        break label264;
      paramSQLiteStatement.bindNull(10);
    }
    while (true)
    {
      long l5 = this.mSourceAccount;
      paramSQLiteStatement.bindLong(11, l5);
      if (this.mSourceId != null)
        break label281;
      paramSQLiteStatement.bindNull(12);
      return;
      l4 = 0L;
      break;
      label264: String str6 = this.mSourceVersion;
      paramSQLiteStatement.bindString(10, str6);
    }
    label281: String str7 = this.mSourceId;
    paramSQLiteStatement.bindString(12, str7);
  }

  public static RadioStation read(SQLiteDatabase paramSQLiteDatabase, long paramLong, RadioStation paramRadioStation)
  {
    Object localObject1 = null;
    String[] arrayOfString1 = PROJECTION;
    String[] arrayOfString2 = new String[1];
    String str = String.valueOf(paramLong);
    arrayOfString2[0] = str;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    Object localObject2 = localObject1;
    Object localObject3 = localObject1;
    Cursor localCursor = localSQLiteDatabase.query("RADIO_STATIONS", arrayOfString1, "Id=?", arrayOfString2, (String)localObject1, localObject2, localObject3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        if (paramRadioStation == null)
          paramRadioStation = new RadioStation();
        paramRadioStation.populateFromFullProjectionCursor(localCursor);
        Store.safeClose(localCursor);
        return localObject1;
      }
      Store.safeClose(localCursor);
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public static RadioStation read(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, RadioStation paramRadioStation)
  {
    Object localObject1 = null;
    String[] arrayOfString1 = PROJECTION;
    String[] arrayOfString2 = new String[2];
    arrayOfString2[0] = paramString1;
    arrayOfString2[1] = paramString2;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    Object localObject2 = localObject1;
    Object localObject3 = localObject1;
    Cursor localCursor = localSQLiteDatabase.query("RADIO_STATIONS", arrayOfString1, "SourceAccount=? AND SourceId=?", arrayOfString2, (String)localObject1, localObject2, localObject3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        if (paramRadioStation == null)
          paramRadioStation = new RadioStation();
        paramRadioStation.populateFromFullProjectionCursor(localCursor);
        Store.safeClose(localCursor);
        return localObject1;
      }
      Store.safeClose(localCursor);
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  boolean delete(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    boolean bool1 = true;
    if (this.mId == 0L)
      throw new InvalidDataException("Cannot delete object that was not loaded or created");
    String[] arrayOfString = new String[1];
    String str = String.valueOf(this.mId);
    arrayOfString[0] = str;
    if (paramSQLiteDatabase.delete("RADIO_STATIONS", "Id=?", arrayOfString) > 0);
    while (true)
    {
      if ((paramBoolean) && (bool1))
        boolean bool2 = createTombstoneIfNeeded(paramSQLiteDatabase, "RADIO_STATION_TOMBSTONES");
      return bool1;
      bool1 = false;
    }
  }

  public final String getArtworkLocation()
  {
    return this.mArtworkLocation;
  }

  public final int getArtworkType()
  {
    return this.mArtworkType;
  }

  public final String getClientId()
  {
    return this.mClientId;
  }

  public final String getDescription()
  {
    return this.mDescription;
  }

  public final long getId()
  {
    return this.mId;
  }

  public final String getName()
  {
    return this.mName;
  }

  public final long getRecentTimestampMicrosec()
  {
    return this.mRecentTimestampMicrosec;
  }

  public final String getSeedSourceId()
  {
    return this.mSeedSourceId;
  }

  public final int getSeedSourceType()
  {
    return this.mSeedSourceType;
  }

  public long insert(SQLiteStatement paramSQLiteStatement)
  {
    if (this.mId != 0L)
      throw new InvalidDataException("The local id of a radio station must not be set for an insert.");
    if (this.mClientId == null)
    {
      String str = Store.generateClientId();
      this.mClientId = str;
    }
    prepareInsertOrFullUpdate(paramSQLiteStatement);
    long l = paramSQLiteStatement.executeInsert();
    if (l == 65535L)
      throw new RuntimeException("Failed to insert into radio stations");
    this.mId = l;
    return this.mId;
  }

  public void populateFromFullProjectionCursor(Cursor paramCursor)
  {
    int i = 1;
    long l1 = paramCursor.getLong(0);
    this.mId = l1;
    String str1 = paramCursor.getString(i);
    this.mClientId = str1;
    String str2 = paramCursor.getString(2);
    this.mName = str2;
    if (paramCursor.isNull(3))
    {
      this.mDescription = null;
      long l2 = paramCursor.getLong(4);
      this.mRecentTimestampMicrosec = l2;
      if (!paramCursor.isNull(5))
        break label222;
      this.mArtworkLocation = null;
      label90: int j = paramCursor.getInt(6);
      this.mArtworkType = j;
      int k = paramCursor.getInt(8);
      this.mSeedSourceType = k;
      String str3 = paramCursor.getString(7);
      this.mSeedSourceId = str3;
      if (!paramCursor.isNull(10))
        break label240;
      this.mSourceVersion = null;
      label154: if (paramCursor.getInt(9) != 1)
        break label259;
    }
    while (true)
    {
      this.mNeedsSync = i;
      int m = paramCursor.getInt(11);
      this.mSourceAccount = m;
      if (!paramCursor.isNull(10))
        break label264;
      this.mSourceId = null;
      return;
      String str4 = paramCursor.getString(3);
      this.mDescription = str4;
      break;
      label222: String str5 = paramCursor.getString(5);
      this.mArtworkLocation = str5;
      break label90;
      label240: String str6 = paramCursor.getString(10);
      this.mSourceVersion = str6;
      break label154;
      label259: i = 0;
    }
    label264: String str7 = paramCursor.getString(12);
    this.mSourceId = str7;
  }

  public void populateFromTombstoneProjectionCursor(Cursor paramCursor)
  {
    int i = TOMBSTONE_PROJECTION_INDEX_ID;
    long l = paramCursor.getLong(i);
    this.mId = l;
    int j = TOMBSTONE_PROJECTION_SOURCE_ID;
    if (!paramCursor.isNull(j))
    {
      int k = TOMBSTONE_PROJECTION_SOURCE_ID;
      String str1 = paramCursor.getString(k);
      this.mSourceId = str1;
    }
    int m = TOMBSTONE_PROJECTION_SOURCE_VERSION;
    if (paramCursor.isNull(m))
      return;
    int n = TOMBSTONE_PROJECTION_SOURCE_VERSION;
    String str2 = paramCursor.getString(n);
    this.mSourceVersion = str2;
  }

  public void reset()
  {
    super.reset();
    this.mId = 0L;
    this.mClientId = null;
    this.mName = null;
    this.mDescription = null;
    this.mRecentTimestampMicrosec = 0L;
    this.mArtworkLocation = null;
    this.mArtworkType = 0;
    this.mSeedSourceId = null;
    this.mSeedSourceType = 0;
  }

  public final void setArtworkLocation(String paramString)
  {
    this.mArtworkLocation = paramString;
  }

  public final void setArtworkType(int paramInt)
  {
    this.mArtworkType = paramInt;
  }

  public final void setClientId(String paramString)
  {
    this.mClientId = paramString;
  }

  public final void setDescription(String paramString)
  {
    this.mDescription = paramString;
  }

  public final void setName(String paramString)
  {
    this.mName = paramString;
  }

  public final void setRecentTimestampMicrosec(long paramLong)
  {
    this.mRecentTimestampMicrosec = paramLong;
  }

  public final void setSeedSourceId(String paramString)
  {
    this.mSeedSourceId = paramString;
  }

  public final void setSeedSourceType(int paramInt)
  {
    this.mSeedSourceType = paramInt;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("mId=");
    long l1 = this.mId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(l1).append(",");
    StringBuilder localStringBuilder5 = localStringBuilder1.append("mClientId=");
    String str1 = this.mClientId;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str1).append(",");
    StringBuilder localStringBuilder7 = localStringBuilder1.append("mName=");
    String str2 = this.mName;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str2).append(",");
    StringBuilder localStringBuilder9 = localStringBuilder1.append("mDescription=");
    String str3 = this.mDescription;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(str3).append(",");
    StringBuilder localStringBuilder11 = localStringBuilder1.append("mRecentTimestampMicrosec=");
    long l2 = this.mRecentTimestampMicrosec;
    StringBuilder localStringBuilder12 = localStringBuilder11.append(l2).append(",");
    StringBuilder localStringBuilder13 = localStringBuilder1.append("mArtworkLocation=");
    String str4 = this.mArtworkLocation;
    StringBuilder localStringBuilder14 = localStringBuilder13.append(str4).append(",");
    StringBuilder localStringBuilder15 = localStringBuilder1.append("mArtworkType=");
    int i = this.mArtworkType;
    StringBuilder localStringBuilder16 = localStringBuilder15.append(i).append(",");
    StringBuilder localStringBuilder17 = localStringBuilder1.append("mSeedSourceId=");
    String str5 = this.mSeedSourceId;
    StringBuilder localStringBuilder18 = localStringBuilder17.append(str5).append(",");
    StringBuilder localStringBuilder19 = localStringBuilder1.append("mSeedSourceType=");
    int j = this.mSeedSourceType;
    StringBuilder localStringBuilder20 = localStringBuilder19.append(j).append(",");
    StringBuilder localStringBuilder21 = localStringBuilder1.append("mSourceAccount=");
    int k = this.mSourceAccount;
    StringBuilder localStringBuilder22 = localStringBuilder21.append(k).append(",");
    StringBuilder localStringBuilder23 = localStringBuilder1.append("mSourceVersion=");
    String str6 = this.mSourceVersion;
    StringBuilder localStringBuilder24 = localStringBuilder23.append(str6).append(",");
    StringBuilder localStringBuilder25 = localStringBuilder1.append("mSourceId=");
    String str7 = this.mSourceId;
    StringBuilder localStringBuilder26 = localStringBuilder25.append(str7).append(",");
    StringBuilder localStringBuilder27 = localStringBuilder1.append("mNeedsSync=");
    boolean bool = this.mNeedsSync;
    StringBuilder localStringBuilder28 = localStringBuilder27.append(bool);
    StringBuilder localStringBuilder29 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  public void update(SQLiteStatement paramSQLiteStatement)
  {
    if (this.mId == 0L)
      throw new InvalidDataException("Object cannot be updated before it's created");
    prepareInsertOrFullUpdate(paramSQLiteStatement);
    long l = this.mId;
    paramSQLiteStatement.bindLong(13, l);
    paramSQLiteStatement.execute();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.RadioStation
 * JD-Core Version:    0.6.2
 */