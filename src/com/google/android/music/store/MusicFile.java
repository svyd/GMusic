package com.google.android.music.store;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.utils.DbUtils;
import java.io.File;
import java.util.Collection;

public class MusicFile extends Syncable
{
  private static final String[] DELETE_PROJECTION;
  static final String[] FULL_PROJECTION;
  public static final Integer MEDIA_STORE_SOURCE_ACCOUNT_AS_INTEGER;
  public static final String MEDIA_STORE_SOURCE_ACCOUNT_AS_STRING = String.valueOf(0);
  public static final String[] PLAYCOUNT_SYNC_PROJECTION = arrayOfString4;
  private static String[] SUMMARY_PROJECTION;
  private long mAddedTime;
  private String mAlbumArtLocation;
  private boolean mAlbumArtLocationChanged = false;
  private String mAlbumArtist;
  private long mAlbumArtistId;
  private int mAlbumArtistOrigin = 0;
  private long mAlbumId;
  private String mAlbumIdSourceText;
  private String mAlbumMetajamId;
  private String mAlbumName;
  private String mArtistArtLocation;
  private String mArtistMetajamId;
  private int mBitrate;
  private String mCanonicalAlbum;
  private String mCanonicalAlbumArtist;
  private String mCanonicalGenre;
  private String mCanonicalTitle;
  private String mCanonicalTrackArtist;
  private String mClientId;
  private boolean mCompilation;
  private String mComposer;
  private short mDiscCount;
  private short mDiscPosition;
  private int mDomain = 0;
  private long mDurationInMilliSec;
  private int mFileType = 0;
  private String mGenre;
  private long mGenreId;
  private long mLastPlayDate;
  private int mLocalCopyBitrate;
  private String mLocalCopyPath;
  private long mLocalCopySize;
  private int mLocalCopyStorageType = 0;
  private int mLocalCopyType = 0;
  private long mLocalId;
  private TagNormalizer mNormalizer;
  private int mPlayCount;
  private int mRating = 0;
  private long mSize;
  private long mSongId;
  private int mSourceType = 0;
  private String mStoreSongId;
  private String mTitle;
  private String mTrackArtist;
  private long mTrackArtistId;
  private int mTrackArtistOrigin = 0;
  private short mTrackCountInAlbum;
  private String mTrackMetajamId;
  private short mTrackPositionInAlbum;
  private int mTrackType = 0;
  private short mYear;

  static
  {
    MEDIA_STORE_SOURCE_ACCOUNT_AS_INTEGER = Integer.valueOf(0);
    String[] arrayOfString1 = new String[18];
    arrayOfString1[0] = "MUSIC.Id";
    arrayOfString1[1] = "SourceAccount";
    arrayOfString1[2] = "SourceId";
    arrayOfString1[3] = "Size";
    arrayOfString1[4] = "LocalCopyPath";
    arrayOfString1[5] = "LocalCopyType";
    arrayOfString1[6] = "Duration";
    arrayOfString1[7] = "Album";
    arrayOfString1[8] = "AlbumArtist";
    arrayOfString1[9] = "AlbumArtistOrigin";
    arrayOfString1[10] = "Artist";
    arrayOfString1[11] = "Title";
    arrayOfString1[12] = "LocalCopyStorageType";
    arrayOfString1[13] = "LocalCopySize";
    arrayOfString1[14] = "Domain";
    arrayOfString1[15] = "SourceType";
    arrayOfString1[16] = "Nid";
    arrayOfString1[17] = "TrackType";
    SUMMARY_PROJECTION = arrayOfString1;
    String[] arrayOfString2 = new String[53];
    arrayOfString2[0] = "MUSIC.Id";
    arrayOfString2[1] = "SourceAccount";
    arrayOfString2[2] = "SourceId";
    arrayOfString2[3] = "_sync_version";
    arrayOfString2[4] = "Size";
    arrayOfString2[5] = "FileType";
    arrayOfString2[6] = "FileDate";
    arrayOfString2[7] = "LocalCopyPath";
    arrayOfString2[8] = "LocalCopyType";
    arrayOfString2[9] = "Title";
    arrayOfString2[10] = "Album";
    arrayOfString2[11] = "Artist";
    arrayOfString2[12] = "AlbumArtist";
    arrayOfString2[13] = "Composer";
    arrayOfString2[14] = "Genre";
    arrayOfString2[15] = "Year";
    arrayOfString2[16] = "Duration";
    arrayOfString2[17] = "TrackCount";
    arrayOfString2[18] = "TrackNumber";
    arrayOfString2[19] = "DiscCount";
    arrayOfString2[20] = "DiscNumber";
    arrayOfString2[21] = "Compilation";
    arrayOfString2[22] = "BitRate";
    arrayOfString2[23] = "AlbumArtLocation";
    arrayOfString2[24] = "SongId";
    arrayOfString2[25] = "AlbumId";
    arrayOfString2[26] = "AlbumArtistId";
    arrayOfString2[27] = "GenreId";
    arrayOfString2[28] = "CanonicalName";
    arrayOfString2[29] = "CanonicalAlbum";
    arrayOfString2[30] = "CanonicalAlbumArtist";
    arrayOfString2[31] = "CanonicalGenre";
    arrayOfString2[32] = "PlayCount";
    arrayOfString2[33] = "LastPlayDate";
    arrayOfString2[34] = "AlbumArtistOrigin";
    arrayOfString2[35] = "LocalCopySize";
    arrayOfString2[36] = "LocalCopyBitrate";
    arrayOfString2[37] = "TrackType";
    arrayOfString2[38] = "ArtistOrigin";
    arrayOfString2[39] = "ArtistId";
    arrayOfString2[40] = "CanonicalArtist";
    arrayOfString2[41] = "Rating";
    arrayOfString2[42] = "_sync_dirty";
    arrayOfString2[43] = "StoreId";
    arrayOfString2[44] = "StoreAlbumId";
    arrayOfString2[45] = "LocalCopyStorageType";
    arrayOfString2[46] = "Domain";
    arrayOfString2[47] = "ArtistArtLocation";
    arrayOfString2[48] = "SourceType";
    arrayOfString2[49] = "Nid";
    arrayOfString2[50] = "ClientId";
    arrayOfString2[51] = "ArtistMetajamId";
    arrayOfString2[52] = "AlbumIdSourceText";
    FULL_PROJECTION = arrayOfString2;
    String[] arrayOfString3 = new String[4];
    arrayOfString3[0] = "Id";
    arrayOfString3[1] = "LocalCopyType";
    arrayOfString3[2] = "LocalCopyPath";
    arrayOfString3[3] = "LocalCopyStorageType";
    DELETE_PROJECTION = arrayOfString3;
    String[] arrayOfString4 = new String[6];
    arrayOfString4[0] = "Id";
    arrayOfString4[1] = "SourceAccount";
    arrayOfString4[2] = "SourceId";
    arrayOfString4[3] = "PlayCount";
    arrayOfString4[4] = "LastPlayDate";
    arrayOfString4[5] = "SourceType";
  }

  public static SQLiteStatement compileDeleteByLocalIdStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("delete from MUSIC where MUSIC.Id=?1");
  }

  public static SQLiteStatement compileFullUpdateStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("update MUSIC set SourceAccount=?, SourceId=?, _sync_version=?, Size=?, FileType=?, FileDate=?, LocalCopyPath=?, LocalCopyType=?, Title=?, Album=?, Artist=?, AlbumArtist=?, AlbumArtistOrigin=?, Composer=?, Genre=?, Year=?, Duration=?, TrackCount=?, TrackNumber=?, DiscCount=?, DiscNumber=?, Compilation=?, BitRate=?, AlbumArtLocation=?, SongId=?, AlbumId=?, AlbumArtistId=?, GenreId=?, CanonicalName=?, CanonicalAlbum=?, CanonicalAlbumArtist=?, CanonicalGenre=?, PlayCount=?, LastPlayDate=?, LocalCopySize=?, LocalCopyBitrate=?, TrackType=?, ArtistOrigin=?, ArtistId=?, CanonicalArtist=?, Rating=?, _sync_dirty=?, StoreId=?,StoreAlbumId=?,LocalCopyStorageType=?,Domain=?,ArtistArtLocation=?,SourceType=?,Nid=?,ClientId=?,ArtistMetajamId=?,AlbumIdSourceText=? where Id=?");
  }

  public static SQLiteStatement compileMusicInsertStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("insert into MUSIC ( SourceAccount, SourceId, _sync_version, Size, FileType, FileDate, LocalCopyPath, LocalCopyType, Title, Album, Artist, AlbumArtist, AlbumArtistOrigin, Composer, Genre, Year, Duration, TrackCount, TrackNumber, DiscCount, DiscNumber, Compilation, BitRate, AlbumArtLocation, SongId, AlbumId, AlbumArtistId, GenreId, CanonicalName, CanonicalAlbum, CanonicalAlbumArtist, CanonicalGenre, PlayCount, LastPlayDate, LocalCopySize, LocalCopyBitrate, TrackType, ArtistOrigin, ArtistId, CanonicalArtist, Rating, _sync_dirty, StoreId, StoreAlbumId, LocalCopyStorageType, Domain, ArtistArtLocation, SourceType, Nid,ClientId,ArtistMetajamId,AlbumIdSourceText) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
  }

  private static int convertDomain(ContentIdentifier.Domain paramDomain)
  {
    int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$download$ContentIdentifier$Domain;
    int i = paramDomain.ordinal();
    int j;
    switch (arrayOfInt[i])
    {
    default:
      String str = "Unknown domain: " + paramDomain;
      throw new IllegalArgumentException(str);
    case 1:
      j = 0;
    case 2:
    case 3:
    }
    while (true)
    {
      return j;
      j = 1;
      continue;
      j = 4;
    }
  }

  private static ContentIdentifier.Domain convertDomain(int paramInt)
  {
    ContentIdentifier.Domain localDomain;
    switch (paramInt)
    {
    case 2:
    case 3:
    default:
      String str = "Unknown domain: " + paramInt;
      throw new IllegalArgumentException(str);
    case 0:
      localDomain = ContentIdentifier.Domain.DEFAULT;
    case 1:
    case 4:
    }
    while (true)
    {
      return localDomain;
      localDomain = ContentIdentifier.Domain.SHARED;
      continue;
      localDomain = ContentIdentifier.Domain.NAUTILUS;
    }
  }

  public static void delete(SQLiteStatement paramSQLiteStatement, long paramLong)
  {
    paramSQLiteStatement.clearBindings();
    paramSQLiteStatement.bindLong(1, paramLong);
    paramSQLiteStatement.execute();
  }

  public static final String deleteAndGetLocalCacheFilepath(Context paramContext, SQLiteDatabase paramSQLiteDatabase, int paramInt, String paramString)
  {
    String str1 = null;
    Object localObject1 = null;
    String[] arrayOfString1 = new String[2];
    String str2 = String.valueOf(paramInt);
    arrayOfString1[0] = str2;
    arrayOfString1[1] = paramString;
    String[] arrayOfString2 = DELETE_PROJECTION;
    Cursor localCursor = paramSQLiteDatabase.query("MUSIC", arrayOfString2, "SourceAccount=?1 AND SourceId=?2", arrayOfString1, null, null, null, "1");
    if (localCursor != null);
    while (true)
    {
      try
      {
        if (localCursor.moveToFirst())
        {
          str1 = localCursor.getString(0);
          if ((localCursor.getInt(1) != 300) && (!localCursor.isNull(2)))
          {
            String str3 = localCursor.getString(2);
            if (str3.length() > 0)
            {
              int i = localCursor.getInt(3);
              Context localContext = paramContext;
              int j = i;
              File localFile = CacheUtils.resolveMusicPath(localContext, str3, j);
              localObject1 = localFile;
            }
          }
        }
        Store.safeClose(localCursor);
        if (str1 != null)
        {
          String[] arrayOfString3 = new String[1];
          arrayOfString3[0] = str1;
          SQLiteDatabase localSQLiteDatabase1 = paramSQLiteDatabase;
          String[] arrayOfString4 = arrayOfString3;
          int k = localSQLiteDatabase1.delete("MUSIC", "MUSIC.Id=?1", arrayOfString4);
          SQLiteDatabase localSQLiteDatabase2 = paramSQLiteDatabase;
          String[] arrayOfString5 = arrayOfString3;
          int m = localSQLiteDatabase2.delete("SHOULDKEEPON", "MusicId=?", arrayOfString5);
        }
        if (localObject1 == null)
        {
          str4 = null;
          return str4;
        }
      }
      finally
      {
        Store.safeClose(localCursor);
      }
      String str4 = localObject1.getAbsolutePath();
    }
  }

  public static void deleteByLocalId(Context paramContext, long paramLong)
  {
    Store localStore = Store.getInstance(paramContext);
    SQLiteDatabase localSQLiteDatabase = localStore.beginWriteTxn();
    try
    {
      localSQLiteStatement = compileDeleteByLocalIdStatement(localSQLiteDatabase);
      delete(localSQLiteStatement, paramLong);
      Store.safeClose(localSQLiteStatement);
      localStore.endWriteTxn(localSQLiteDatabase, true);
      if (1 == 0)
        return;
      ContentResolver localContentResolver = paramContext.getContentResolver();
      Uri localUri = MusicContent.CONTENT_URI;
      localContentResolver.notifyChange(localUri, null, false);
      return;
    }
    finally
    {
      SQLiteStatement localSQLiteStatement;
      Store.safeClose(localSQLiteStatement);
      localStore.endWriteTxn(localSQLiteDatabase, false);
    }
  }

  public static Cursor getMusicFilesToSync(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    String[] arrayOfString1 = FULL_PROJECTION;
    String[] arrayOfString2 = new String[1];
    String str1 = Long.toString(paramLong);
    arrayOfString2[0] = str1;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("MUSIC", arrayOfString1, "SourceAccount=? AND _sync_dirty=1", arrayOfString2, null, str2, str3);
  }

  public static Cursor getPlaycountsToSync(SQLiteDatabase paramSQLiteDatabase, long paramLong)
  {
    String[] arrayOfString1 = PLAYCOUNT_SYNC_PROJECTION;
    String[] arrayOfString2 = new String[1];
    String str1 = Long.toString(paramLong);
    arrayOfString2[0] = str1;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("MUSIC", arrayOfString1, "SourceAccount=? AND PlayCount>0", arrayOfString2, null, str2, str3);
  }

  // ERROR //
  public static MusicFile getSummaryMusicFile(Store paramStore, MusicFile paramMusicFile, long paramLong)
    throws DataNotFoundException
  {
    // Byte code:
    //   0: invokestatic 425	com/google/android/music/store/MusicFile:getSummaryProjection	()[Ljava/lang/String;
    //   3: astore 4
    //   5: new 427	android/database/sqlite/SQLiteQueryBuilder
    //   8: dup
    //   9: invokespecial 428	android/database/sqlite/SQLiteQueryBuilder:<init>	()V
    //   12: astore 5
    //   14: aload 5
    //   16: ldc_w 309
    //   19: invokevirtual 431	android/database/sqlite/SQLiteQueryBuilder:setTables	(Ljava/lang/String;)V
    //   22: aload_0
    //   23: invokevirtual 434	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   26: astore 6
    //   28: iconst_1
    //   29: anewarray 76	java/lang/String
    //   32: astore 7
    //   34: lload_2
    //   35: invokestatic 436	java/lang/String:valueOf	(J)Ljava/lang/String;
    //   38: astore 8
    //   40: aload 7
    //   42: iconst_0
    //   43: aload 8
    //   45: aastore
    //   46: aload 5
    //   48: aload 6
    //   50: aload 4
    //   52: ldc_w 438
    //   55: aload 7
    //   57: aconst_null
    //   58: aconst_null
    //   59: aconst_null
    //   60: invokevirtual 441	android/database/sqlite/SQLiteQueryBuilder:query	(Landroid/database/sqlite/SQLiteDatabase;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   63: astore 9
    //   65: aload 9
    //   67: astore 10
    //   69: aload 10
    //   71: ifnull +13 -> 84
    //   74: aload 10
    //   76: invokeinterface 444 1 0
    //   81: ifne +51 -> 132
    //   84: new 258	java/lang/StringBuilder
    //   87: dup
    //   88: invokespecial 259	java/lang/StringBuilder:<init>	()V
    //   91: ldc_w 446
    //   94: invokevirtual 265	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: lload_2
    //   98: invokevirtual 449	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   101: invokevirtual 272	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   104: astore 11
    //   106: new 421	com/google/android/music/store/DataNotFoundException
    //   109: dup
    //   110: aload 11
    //   112: invokespecial 450	com/google/android/music/store/DataNotFoundException:<init>	(Ljava/lang/String;)V
    //   115: athrow
    //   116: astore 12
    //   118: aload 10
    //   120: invokestatic 349	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   123: aload_0
    //   124: aload 6
    //   126: invokevirtual 454	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   129: aload 12
    //   131: athrow
    //   132: aload_1
    //   133: ifnonnull +31 -> 164
    //   136: new 2	com/google/android/music/store/MusicFile
    //   139: dup
    //   140: invokespecial 455	com/google/android/music/store/MusicFile:<init>	()V
    //   143: astore_1
    //   144: aload_1
    //   145: aload 10
    //   147: iconst_0
    //   148: invokevirtual 459	com/google/android/music/store/MusicFile:populateFromSummary	(Landroid/database/Cursor;I)V
    //   151: aload 10
    //   153: invokestatic 349	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   156: aload_0
    //   157: aload 6
    //   159: invokevirtual 454	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   162: aload_1
    //   163: areturn
    //   164: aload_1
    //   165: invokevirtual 462	com/google/android/music/store/MusicFile:reset	()V
    //   168: goto -24 -> 144
    //   171: astore 12
    //   173: aconst_null
    //   174: astore 10
    //   176: goto -58 -> 118
    //
    // Exception table:
    //   from	to	target	type
    //   74	116	116	finally
    //   136	151	116	finally
    //   164	168	116	finally
    //   28	65	171	finally
  }

  static String[] getSummaryProjection()
  {
    return SUMMARY_PROJECTION;
  }

  private void prepareInsertOrFullUpdate(SQLiteStatement paramSQLiteStatement)
  {
    if ((this.mSourceId == null) || (this.mSourceId.length() == 0))
      throw new InvalidDataException("Source id must be set before saving to DB");
    int i;
    int j;
    label52: boolean bool;
    if (!TextUtils.isEmpty(this.mStoreSongId))
    {
      i = 1;
      if (TextUtils.isEmpty(this.mAlbumMetajamId))
        break label144;
      j = 1;
      bool = TextUtils.isEmpty(this.mLocalCopyPath);
      switch (this.mLocalCopyStorageType)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 3:
      }
    }
    label144: 
    do
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Invalid storage type:");
      int k = this.mLocalCopyStorageType;
      String str1 = k;
      throw new InvalidDataException(str1);
      i = 0;
      break;
      j = 0;
      break label52;
      if (bool)
        break label245;
      StringBuilder localStringBuilder2 = new StringBuilder().append("Local path is set for storage type NONE: ");
      String str2 = this.mLocalCopyPath;
      String str3 = str2;
      throw new InvalidDataException(str3);
      if (!bool)
        break label245;
      throw new InvalidDataException("Local path is not set for storage type INTERNAL");
      if (!bool)
        break label245;
      throw new InvalidDataException("Local path is not set for storage type EXTERNAL");
    }
    while (!bool);
    throw new InvalidDataException("Local path is not set for storage type SECONDARY EXTERNAL");
    label245: setDerivedFields();
    paramSQLiteStatement.clearBindings();
    long l1 = this.mSourceAccount;
    paramSQLiteStatement.bindLong(1, l1);
    long l2 = this.mTrackType;
    paramSQLiteStatement.bindLong(37, l2);
    String str4 = this.mSourceId;
    paramSQLiteStatement.bindString(2, str4);
    label359: String str5;
    label420: String str6;
    label444: String str7;
    label468: String str8;
    label507: String str9;
    label546: String str10;
    label570: long l17;
    label683: label729: label752: String str11;
    label838: String str12;
    label862: String str13;
    label886: String str14;
    label910: String str15;
    label934: long l28;
    label1016: int i12;
    if (this.mSourceVersion == null)
    {
      paramSQLiteStatement.bindNull(3);
      long l3 = this.mSize;
      paramSQLiteStatement.bindLong(4, l3);
      long l4 = this.mFileType;
      paramSQLiteStatement.bindLong(5, l4);
      long l5 = this.mAddedTime;
      paramSQLiteStatement.bindLong(6, l5);
      if (!bool)
        break label1194;
      paramSQLiteStatement.bindNull(7);
      long l6 = this.mLocalCopyType;
      paramSQLiteStatement.bindLong(8, l6);
      long l7 = this.mLocalCopyStorageType;
      paramSQLiteStatement.bindLong(45, l7);
      long l8 = this.mLocalCopyBitrate;
      paramSQLiteStatement.bindLong(36, l8);
      int m = 9;
      if (this.mTitle != null)
        break label1211;
      str5 = "";
      paramSQLiteStatement.bindString(m, str5);
      int n = 10;
      if (this.mAlbumName != null)
        break label1220;
      str6 = "";
      paramSQLiteStatement.bindString(n, str6);
      int i1 = 11;
      if (this.mTrackArtist != null)
        break label1229;
      str7 = "";
      paramSQLiteStatement.bindString(i1, str7);
      long l9 = this.mTrackArtistOrigin;
      paramSQLiteStatement.bindLong(38, l9);
      int i2 = 12;
      if (this.mAlbumArtist != null)
        break label1238;
      str8 = "";
      paramSQLiteStatement.bindString(i2, str8);
      long l10 = this.mAlbumArtistOrigin;
      paramSQLiteStatement.bindLong(13, l10);
      int i3 = 14;
      if (this.mComposer != null)
        break label1247;
      str9 = "";
      paramSQLiteStatement.bindString(i3, str9);
      int i4 = 15;
      if (this.mGenre != null)
        break label1256;
      str10 = "";
      paramSQLiteStatement.bindString(i4, str10);
      long l11 = this.mYear;
      paramSQLiteStatement.bindLong(16, l11);
      long l12 = this.mDurationInMilliSec;
      paramSQLiteStatement.bindLong(17, l12);
      long l13 = this.mTrackCountInAlbum;
      paramSQLiteStatement.bindLong(18, l13);
      long l14 = this.mTrackPositionInAlbum;
      paramSQLiteStatement.bindLong(19, l14);
      long l15 = this.mDiscCount;
      paramSQLiteStatement.bindLong(20, l15);
      long l16 = this.mDiscPosition;
      paramSQLiteStatement.bindLong(21, l16);
      int i5 = 22;
      if (!this.mCompilation)
        break label1265;
      l17 = 1L;
      paramSQLiteStatement.bindLong(i5, l17);
      long l18 = this.mBitrate;
      paramSQLiteStatement.bindLong(23, l18);
      if ((this.mAlbumArtLocation != null) && (this.mAlbumArtLocation.length() != 0))
        break label1273;
      paramSQLiteStatement.bindNull(24);
      if ((this.mArtistArtLocation != null) && (this.mArtistArtLocation.length() != 0))
        break label1290;
      paramSQLiteStatement.bindNull(47);
      long l19 = this.mSongId;
      paramSQLiteStatement.bindLong(25, l19);
      long l20 = this.mAlbumId;
      paramSQLiteStatement.bindLong(26, l20);
      long l21 = this.mAlbumArtistId;
      paramSQLiteStatement.bindLong(27, l21);
      long l22 = this.mTrackArtistId;
      paramSQLiteStatement.bindLong(39, l22);
      long l23 = this.mGenreId;
      paramSQLiteStatement.bindLong(28, l23);
      int i6 = 29;
      if (this.mCanonicalTitle != null)
        break label1307;
      str11 = "";
      paramSQLiteStatement.bindString(i6, str11);
      int i7 = 30;
      if (this.mCanonicalAlbum != null)
        break label1316;
      str12 = "";
      paramSQLiteStatement.bindString(i7, str12);
      int i8 = 31;
      if (this.mCanonicalAlbumArtist != null)
        break label1325;
      str13 = "";
      paramSQLiteStatement.bindString(i8, str13);
      int i9 = 40;
      if (this.mCanonicalTrackArtist != null)
        break label1334;
      str14 = "";
      paramSQLiteStatement.bindString(i9, str14);
      int i10 = 32;
      if (this.mCanonicalGenre != null)
        break label1343;
      str15 = "";
      paramSQLiteStatement.bindString(i10, str15);
      long l24 = this.mPlayCount;
      paramSQLiteStatement.bindLong(33, l24);
      long l25 = this.mLastPlayDate;
      paramSQLiteStatement.bindLong(34, l25);
      long l26 = this.mLocalCopySize;
      paramSQLiteStatement.bindLong(35, l26);
      long l27 = this.mRating;
      paramSQLiteStatement.bindLong(41, l27);
      int i11 = 42;
      if (!this.mNeedsSync)
        break label1352;
      l28 = 1L;
      paramSQLiteStatement.bindLong(i11, l28);
      if (i == 0)
        break label1360;
      String str16 = this.mStoreSongId;
      paramSQLiteStatement.bindString(43, str16);
      label1042: if (j == 0)
        break label1369;
      String str17 = this.mAlbumMetajamId;
      paramSQLiteStatement.bindString(44, str17);
      label1060: long l29 = this.mDomain;
      paramSQLiteStatement.bindLong(46, l29);
      long l30 = this.mSourceType;
      paramSQLiteStatement.bindLong(48, l30);
      if (this.mTrackMetajamId == null)
        break label1378;
      String str18 = this.mTrackMetajamId;
      paramSQLiteStatement.bindString(49, str18);
      label1111: if (this.mClientId == null)
        break label1387;
      String str19 = this.mClientId;
      paramSQLiteStatement.bindString(50, str19);
      label1132: if (this.mArtistMetajamId == null)
        break label1396;
      String str20 = this.mArtistMetajamId;
      paramSQLiteStatement.bindString(51, str20);
      label1153: i12 = 52;
      if (this.mAlbumIdSourceText != null)
        break label1405;
    }
    label1194: label1211: label1220: label1229: label1238: label1247: label1256: label1265: label1273: label1405: for (String str21 = ""; ; str21 = this.mAlbumIdSourceText)
    {
      paramSQLiteStatement.bindString(i12, str21);
      return;
      String str22 = this.mSourceVersion;
      paramSQLiteStatement.bindString(3, str22);
      break;
      String str23 = this.mLocalCopyPath;
      paramSQLiteStatement.bindString(7, str23);
      break label359;
      str5 = this.mTitle;
      break label420;
      str6 = this.mAlbumName;
      break label444;
      str7 = this.mTrackArtist;
      break label468;
      str8 = this.mAlbumArtist;
      break label507;
      str9 = this.mComposer;
      break label546;
      str10 = this.mGenre;
      break label570;
      l17 = 0L;
      break label683;
      String str24 = this.mAlbumArtLocation;
      paramSQLiteStatement.bindString(24, str24);
      break label729;
      String str25 = this.mArtistArtLocation;
      paramSQLiteStatement.bindString(47, str25);
      break label752;
      str11 = this.mCanonicalTitle;
      break label838;
      str12 = this.mCanonicalAlbum;
      break label862;
      str13 = this.mCanonicalAlbumArtist;
      break label886;
      str14 = this.mCanonicalTrackArtist;
      break label910;
      str15 = this.mCanonicalGenre;
      break label934;
      l28 = 0L;
      break label1016;
      paramSQLiteStatement.bindNull(43);
      break label1042;
      paramSQLiteStatement.bindNull(44);
      break label1060;
      paramSQLiteStatement.bindNull(49);
      break label1111;
      paramSQLiteStatement.bindNull(50);
      break label1132;
      paramSQLiteStatement.bindNull(51);
      break label1153;
    }
  }

  public static MusicFile readMusicFile(SQLiteDatabase paramSQLiteDatabase, String paramString1, String paramString2, MusicFile paramMusicFile)
  {
    Object localObject1 = null;
    String[] arrayOfString1 = FULL_PROJECTION;
    String[] arrayOfString2 = new String[2];
    arrayOfString2[0] = paramString1;
    arrayOfString2[1] = paramString2;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    Object localObject2 = localObject1;
    Object localObject3 = localObject1;
    Cursor localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString1, "SourceAccount=? AND SourceId=?", arrayOfString2, (String)localObject1, localObject2, localObject3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        if (paramMusicFile == null)
          paramMusicFile = new MusicFile();
        paramMusicFile.populateFromFullProjectionCursor(localCursor);
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

  public static void resetPlayCount(SQLiteDatabase paramSQLiteDatabase, Collection<Long> paramCollection)
  {
    if (paramCollection == null)
      return;
    if (paramCollection.isEmpty())
      return;
    ContentValues localContentValues = new ContentValues(1);
    Integer localInteger = Integer.valueOf(0);
    localContentValues.put("PlayCount", localInteger);
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("Id");
    DbUtils.appendIN(localStringBuilder1, paramCollection);
    String str = localStringBuilder1.toString();
    int i = paramSQLiteDatabase.update("MUSIC", localContentValues, str, null);
  }

  private final void setAlbumArtist(String paramString, int paramInt)
  {
    if ((this.mAlbumArtistOrigin != paramInt) && (!stringChanged(this.mAlbumArtist, paramString)))
      return;
    this.mAlbumArtist = paramString;
    this.mAlbumArtistId = 0L;
    this.mCanonicalAlbumArtist = null;
    this.mAlbumId = 0L;
    this.mSongId = 0L;
    this.mAlbumArtistOrigin = paramInt;
  }

  private final void setTrackArtist(String paramString, int paramInt)
  {
    if ((this.mTrackArtistOrigin != paramInt) && (!stringChanged(this.mTrackArtist, paramString)))
      return;
    this.mTrackArtist = paramString;
    this.mTrackArtistOrigin = paramInt;
    this.mCanonicalTrackArtist = null;
    this.mTrackArtistId = 0L;
    if (this.mAlbumArtistOrigin != 1)
      return;
    if (this.mTrackArtistOrigin == 1)
      throw new IllegalArgumentException("Both track artist and album artist can't be derived");
    String str = this.mTrackArtist;
    setAlbumArtist(str, 1);
  }

  private static boolean stringChanged(String paramString1, String paramString2)
  {
    boolean bool = true;
    if (paramString1 == null)
      if ((paramString2 == null) || (paramString2.length() <= 0));
    while (true)
    {
      return bool;
      bool = false;
      continue;
      if (paramString2 == null)
      {
        if (paramString1.length() <= 0)
          bool = false;
      }
      else if (paramString1.equals(paramString2))
        bool = false;
    }
  }

  public static void throwIfInvalidRating(int paramInt)
  {
    if ((paramInt >= 0) && (paramInt <= 5))
      return;
    String str = "Rating value of " + paramInt + " is out of expected range";
    throw new InvalidDataException(str);
  }

  public final void forceAlbumArtist(String paramString)
  {
    setAlbumArtist(paramString, 2);
  }

  public final long getAddedTime()
  {
    return this.mAddedTime;
  }

  public String getAlbumArtLocation()
  {
    return this.mAlbumArtLocation;
  }

  public final String getAlbumArtist()
  {
    return this.mAlbumArtist;
  }

  public long getAlbumId()
  {
    return this.mAlbumId;
  }

  public final String getAlbumMetajamId()
  {
    return this.mAlbumMetajamId;
  }

  public final String getAlbumName()
  {
    return this.mAlbumName;
  }

  public final String getClientId()
  {
    return this.mClientId;
  }

  public final String getComposer()
  {
    return this.mComposer;
  }

  public final short getDiscCount()
  {
    return this.mDiscCount;
  }

  public final short getDiscPosition()
  {
    return this.mDiscPosition;
  }

  public ContentIdentifier.Domain getDomain()
  {
    return convertDomain(this.mDomain);
  }

  public final long getDurationInMilliSec()
  {
    return this.mDurationInMilliSec;
  }

  public final String getGenre()
  {
    return this.mGenre;
  }

  public final boolean getIsCloudFile()
  {
    if (this.mSourceAccount != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final long getLastPlayDate()
  {
    return this.mLastPlayDate;
  }

  public final String getLocalCopyPath()
  {
    return this.mLocalCopyPath;
  }

  public final long getLocalCopySize()
  {
    return this.mLocalCopySize;
  }

  public final int getLocalCopyStorageType()
  {
    return this.mLocalCopyStorageType;
  }

  public final int getLocalCopyType()
  {
    return this.mLocalCopyType;
  }

  public final long getLocalId()
  {
    return this.mLocalId;
  }

  public final int getPlayCount()
  {
    return this.mPlayCount;
  }

  public final int getRating()
  {
    return this.mRating;
  }

  public final long getSize()
  {
    return this.mSize;
  }

  public final int getSourceType()
  {
    return this.mSourceType;
  }

  public final String getStoreSongId()
  {
    return this.mStoreSongId;
  }

  public final String getTitle()
  {
    return this.mTitle;
  }

  public final String getTrackArtist()
  {
    return this.mTrackArtist;
  }

  public final short getTrackPositionInAlbum()
  {
    return this.mTrackPositionInAlbum;
  }

  public final int getTrackType()
  {
    return this.mTrackType;
  }

  public final short getYear()
  {
    return this.mYear;
  }

  public final long insertMusicFile(SQLiteStatement paramSQLiteStatement)
  {
    if (this.mLocalId != 0L)
      throw new IllegalStateException("MusicFile already created. Forgot to call reset()?");
    if (this.mClientId == null)
    {
      String str = Store.generateClientId();
      this.mClientId = str;
    }
    prepareInsertOrFullUpdate(paramSQLiteStatement);
    long l = paramSQLiteStatement.executeInsert();
    if (l == 65535L)
      throw new RuntimeException("Failed to insert into MUSIC");
    this.mLocalId = l;
    return this.mLocalId;
  }

  public final boolean isNautilus()
  {
    if ((this.mTrackType == 4) || (this.mTrackType == 5));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isPurchasedTrack()
  {
    int i = 1;
    if (this.mTrackType != i);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public void load(SQLiteDatabase paramSQLiteDatabase, long paramLong)
    throws DataNotFoundException
  {
    String[] arrayOfString1 = FULL_PROJECTION;
    String[] arrayOfString2 = new String[1];
    String str1 = String.valueOf(paramLong);
    arrayOfString2[0] = str1;
    SQLiteDatabase localSQLiteDatabase = paramSQLiteDatabase;
    String str2 = null;
    String str3 = null;
    Cursor localCursor = localSQLiteDatabase.query("MUSIC", arrayOfString1, "Id=?", arrayOfString2, null, str2, str3);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        populateFromFullProjectionCursor(localCursor);
        return;
      }
      String str4 = "MusicFile with id " + paramLong + " is not found";
      throw new DataNotFoundException(str4);
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public void populateFromFullProjectionCursor(Cursor paramCursor)
  {
    int i = 1;
    long l1 = paramCursor.getLong(0);
    this.mLocalId = l1;
    int j = paramCursor.getInt(37);
    this.mTrackType = j;
    int k = paramCursor.getInt(i);
    this.mSourceAccount = k;
    String str1 = paramCursor.getString(2);
    this.mSourceId = str1;
    boolean bool;
    if (paramCursor.isNull(3))
    {
      this.mSourceVersion = null;
      long l2 = paramCursor.getLong(4);
      this.mSize = l2;
      int m = paramCursor.getInt(5);
      this.mFileType = m;
      long l3 = paramCursor.getLong(6);
      this.mAddedTime = l3;
      String str2 = paramCursor.getString(7);
      this.mLocalCopyPath = str2;
      int n = paramCursor.getInt(8);
      this.mLocalCopyType = n;
      int i1 = paramCursor.getInt(45);
      this.mLocalCopyStorageType = i1;
      int i2 = paramCursor.getInt(36);
      this.mLocalCopyBitrate = i2;
      String str3 = paramCursor.getString(9);
      this.mTitle = str3;
      String str4 = paramCursor.getString(10);
      this.mAlbumName = str4;
      String str5 = paramCursor.getString(11);
      this.mTrackArtist = str5;
      int i3 = paramCursor.getInt(38);
      this.mTrackArtistOrigin = i3;
      String str6 = paramCursor.getString(12);
      this.mAlbumArtist = str6;
      int i4 = paramCursor.getInt(34);
      this.mAlbumArtistOrigin = i4;
      String str7 = paramCursor.getString(13);
      this.mComposer = str7;
      String str8 = paramCursor.getString(14);
      this.mGenre = str8;
      short s1 = paramCursor.getShort(15);
      this.mYear = s1;
      long l4 = paramCursor.getLong(16);
      this.mDurationInMilliSec = l4;
      short s2 = paramCursor.getShort(17);
      this.mTrackCountInAlbum = s2;
      short s3 = paramCursor.getShort(18);
      this.mTrackPositionInAlbum = s3;
      short s4 = paramCursor.getShort(19);
      this.mDiscCount = s4;
      short s5 = paramCursor.getShort(20);
      this.mDiscPosition = s5;
      if (paramCursor.getInt(21) != 1)
        break label872;
      bool = true;
      label425: this.mCompilation = bool;
      int i5 = paramCursor.getInt(22);
      this.mBitrate = i5;
      if (!paramCursor.isNull(23))
        break label878;
      this.mAlbumArtLocation = null;
      label463: if (!paramCursor.isNull(47))
        break label897;
      this.mArtistArtLocation = null;
      label479: this.mAlbumArtLocationChanged = false;
      long l5 = paramCursor.getLong(24);
      this.mSongId = l5;
      long l6 = paramCursor.getLong(25);
      this.mAlbumId = l6;
      long l7 = paramCursor.getLong(26);
      this.mAlbumArtistId = l7;
      long l8 = paramCursor.getLong(39);
      this.mTrackArtistId = l8;
      long l9 = paramCursor.getLong(27);
      this.mGenreId = l9;
      String str9 = paramCursor.getString(28);
      this.mCanonicalTitle = str9;
      String str10 = paramCursor.getString(29);
      this.mCanonicalAlbum = str10;
      String str11 = paramCursor.getString(30);
      this.mCanonicalAlbumArtist = str11;
      String str12 = paramCursor.getString(40);
      this.mCanonicalTrackArtist = str12;
      String str13 = paramCursor.getString(31);
      this.mCanonicalGenre = str13;
      int i6 = paramCursor.getInt(32);
      this.mPlayCount = i6;
      long l10 = paramCursor.getLong(33);
      this.mLastPlayDate = l10;
      long l11 = paramCursor.getLong(35);
      this.mLocalCopySize = l11;
      int i7 = paramCursor.getInt(41);
      this.mRating = i7;
      if (paramCursor.getInt(42) != 1)
        break label916;
      label720: this.mNeedsSync = i;
      if (!paramCursor.isNull(43))
        break label921;
      this.mStoreSongId = null;
      label741: if (!paramCursor.isNull(44))
        break label940;
      this.mAlbumMetajamId = null;
      label757: int i8 = paramCursor.getInt(46);
      this.mDomain = i8;
      int i9 = paramCursor.getInt(48);
      this.mSourceType = i9;
      if (!paramCursor.isNull(49))
        break label959;
      this.mTrackMetajamId = null;
      label805: if (!paramCursor.isNull(50))
        break label978;
      this.mClientId = null;
      label821: if (!paramCursor.isNull(51))
        break label997;
    }
    label872: label878: String str22;
    for (this.mArtistMetajamId = null; ; this.mArtistMetajamId = str22)
    {
      String str14 = paramCursor.getString(52);
      this.mAlbumIdSourceText = str14;
      return;
      String str15 = paramCursor.getString(3);
      this.mSourceVersion = str15;
      break;
      bool = false;
      break label425;
      String str16 = paramCursor.getString(23);
      this.mAlbumArtLocation = str16;
      break label463;
      label897: String str17 = paramCursor.getString(47);
      this.mArtistArtLocation = str17;
      break label479;
      label916: i = 0;
      break label720;
      label921: String str18 = paramCursor.getString(43);
      this.mStoreSongId = str18;
      break label741;
      label940: String str19 = paramCursor.getString(44);
      this.mAlbumMetajamId = str19;
      break label757;
      label959: String str20 = paramCursor.getString(49);
      this.mTrackMetajamId = str20;
      break label805;
      label978: String str21 = paramCursor.getString(50);
      this.mClientId = str21;
      break label821;
      label997: str22 = paramCursor.getString(51);
    }
  }

  void populateFromSummary(Cursor paramCursor, int paramInt)
  {
    int i = paramInt + 0;
    long l1 = paramCursor.getLong(i);
    this.mLocalId = l1;
    int j = paramInt + 1;
    int k = paramCursor.getInt(j);
    this.mSourceAccount = k;
    int m = paramInt + 2;
    String str1 = paramCursor.getString(m);
    this.mSourceId = str1;
    int n = paramInt + 3;
    long l2 = paramCursor.getLong(n);
    this.mSize = l2;
    int i1 = paramInt + 4;
    String str2 = paramCursor.getString(i1);
    this.mLocalCopyPath = str2;
    int i2 = paramInt + 5;
    int i3 = paramCursor.getInt(i2);
    this.mLocalCopyType = i3;
    int i4 = paramInt + 6;
    long l3 = paramCursor.getLong(i4);
    this.mDurationInMilliSec = l3;
    int i5 = paramInt + 7;
    String str3 = paramCursor.getString(i5);
    this.mAlbumName = str3;
    int i6 = paramInt + 8;
    String str4 = paramCursor.getString(i6);
    this.mAlbumArtist = str4;
    int i7 = paramInt + 9;
    int i8 = paramCursor.getInt(i7);
    this.mAlbumArtistOrigin = i8;
    int i9 = paramInt + 10;
    String str5 = paramCursor.getString(i9);
    this.mTrackArtist = str5;
    int i10 = paramInt + 11;
    String str6 = paramCursor.getString(i10);
    this.mTitle = str6;
    int i11 = paramInt + 12;
    int i12 = paramCursor.getInt(i11);
    this.mLocalCopyStorageType = i12;
    int i13 = paramInt + 13;
    long l4 = paramCursor.getInt(i13);
    this.mLocalCopySize = l4;
    int i14 = paramInt + 14;
    int i15 = paramCursor.getInt(i14);
    this.mDomain = i15;
    int i16 = paramInt + 15;
    int i17 = paramCursor.getInt(i16);
    this.mSourceType = i17;
    int i18 = paramInt + 16;
    String str7 = paramCursor.getString(i18);
    this.mTrackMetajamId = str7;
    int i19 = paramInt + 17;
    int i20 = paramCursor.getInt(i19);
    this.mTrackType = i20;
  }

  public void reset()
  {
    super.reset();
    this.mLocalId = 0L;
    this.mTrackType = 0;
    this.mAddedTime = 0L;
    this.mSize = 0L;
    this.mFileType = 0;
    this.mAddedTime = 0L;
    this.mLocalCopyPath = null;
    this.mLocalCopyType = 0;
    this.mLocalCopySize = 0L;
    this.mLocalCopyBitrate = 0;
    this.mLocalCopyStorageType = 0;
    this.mPlayCount = 0;
    this.mLastPlayDate = 0L;
    this.mTitle = null;
    this.mTrackArtist = null;
    this.mTrackArtistOrigin = 0;
    this.mAlbumArtist = null;
    this.mAlbumArtistOrigin = 0;
    this.mAlbumName = null;
    this.mComposer = null;
    this.mGenre = null;
    this.mYear = 0;
    this.mTrackCountInAlbum = 0;
    this.mTrackPositionInAlbum = 0;
    this.mDiscCount = 0;
    this.mDiscPosition = 0;
    this.mCompilation = false;
    this.mBitrate = 0;
    this.mDurationInMilliSec = 0L;
    this.mAlbumArtLocation = null;
    this.mAlbumArtLocationChanged = false;
    this.mArtistArtLocation = null;
    this.mRating = 0;
    this.mStoreSongId = null;
    this.mAlbumMetajamId = null;
    this.mDomain = 0;
    resetDerivedFields();
    this.mSourceType = 0;
    this.mTrackMetajamId = null;
    this.mClientId = null;
    this.mArtistMetajamId = null;
    this.mAlbumIdSourceText = null;
  }

  public void resetDerivedFields()
  {
    this.mSongId = 0L;
    this.mAlbumArtistId = 0L;
    this.mTrackArtistId = 0L;
    this.mAlbumId = 0L;
    this.mGenreId = 0L;
    this.mCanonicalTitle = null;
    this.mCanonicalAlbum = null;
    this.mCanonicalAlbumArtist = null;
    this.mCanonicalTrackArtist = null;
    this.mCanonicalGenre = null;
  }

  public final void setAddedTime(long paramLong)
  {
    if ((paramLong < 315532800000L) || (paramLong > 4102444800000L))
    {
      String str = "Unexpected added time: " + paramLong;
      int i = Log.w("MusicStore", str);
    }
    this.mAddedTime = paramLong;
  }

  public void setAlbumArtLocation(String paramString)
  {
    if (stringChanged(this.mAlbumArtLocation, paramString))
      this.mAlbumArtLocationChanged = true;
    this.mAlbumArtLocation = paramString;
  }

  public final void setAlbumArtist(String paramString)
  {
    setAlbumArtist(paramString, 0);
  }

  public final void setAlbumMetajamId(String paramString)
  {
    this.mAlbumMetajamId = paramString;
  }

  public final void setAlbumName(String paramString)
  {
    if (!stringChanged(this.mAlbumName, paramString))
      return;
    this.mAlbumName = paramString;
    this.mAlbumId = 0L;
    this.mCanonicalAlbum = null;
    this.mSongId = 0L;
  }

  public void setArtistArtLocation(String paramString)
  {
    this.mArtistArtLocation = paramString;
  }

  public final void setArtistMetajamId(String paramString)
  {
    this.mArtistMetajamId = paramString;
  }

  public final void setClientId(String paramString)
  {
    this.mClientId = paramString;
  }

  public final void setComposer(String paramString)
  {
    this.mComposer = paramString;
  }

  void setDerivedFields()
  {
    String str1 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
    String str2 = this.mCanonicalAlbum;
    if (str1.equals(str2))
      this.mCanonicalAlbum = null;
    String str3 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
    String str4 = this.mCanonicalAlbumArtist;
    if (str3.equals(str4))
      this.mCanonicalAlbumArtist = null;
    String str5 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
    String str6 = this.mCanonicalTrackArtist;
    if (str5.equals(str6))
      this.mCanonicalTrackArtist = null;
    if (this.mNormalizer == null)
    {
      TagNormalizer localTagNormalizer1 = new TagNormalizer();
      this.mNormalizer = localTagNormalizer1;
    }
    int i;
    int j;
    if ((this.mAlbumArtist != null) && (this.mAlbumArtist.trim().length() > 0))
    {
      i = 1;
      if ((this.mTrackArtist == null) || (this.mTrackArtist.trim().length() <= 0))
        break label339;
      j = 1;
      label140: if ((i != 0) || (j == 0))
        break label345;
      String str7 = this.mTrackArtist;
      setAlbumArtist(str7, 1);
    }
    while (true)
    {
      if (this.mGenreId == 0L)
      {
        TagNormalizer localTagNormalizer2 = this.mNormalizer;
        String str8 = this.mGenre;
        String str9 = localTagNormalizer2.normalize(str8);
        this.mCanonicalGenre = str9;
        if ((TextUtils.isEmpty(this.mCanonicalGenre)) && (this.mGenre != null))
        {
          String str10 = this.mGenre;
          this.mCanonicalGenre = str10;
        }
        long l1 = Store.generateId(this.mCanonicalGenre);
        this.mGenreId = l1;
      }
      if (this.mCanonicalTitle != null)
        break label427;
      if ((this.mTitle != null) && (this.mTitle.length() != 0))
        break label371;
      StringBuilder localStringBuilder1 = new StringBuilder().append("Song title must not be empty. mTrackMetajamId: ");
      String str11 = this.mTrackMetajamId;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str11).append(" mSourceId: ");
      String str12 = this.mSourceId;
      String str13 = str12;
      throw new InvalidDataException(str13);
      i = 0;
      break;
      label339: j = 0;
      break label140;
      label345: if ((j == 0) && (i != 0))
      {
        String str14 = this.mAlbumArtist;
        setTrackArtist(str14, 1);
      }
    }
    label371: TagNormalizer localTagNormalizer3 = this.mNormalizer;
    String str15 = this.mTitle;
    String str16 = localTagNormalizer3.normalize(str15);
    this.mCanonicalTitle = str16;
    if ((TextUtils.isEmpty(this.mCanonicalTitle)) && (this.mTitle != null))
    {
      String str17 = this.mTitle;
      this.mCanonicalTitle = str17;
    }
    label427: if (this.mCanonicalAlbum == null)
    {
      TagNormalizer localTagNormalizer4 = this.mNormalizer;
      String str18 = this.mAlbumName;
      String str19 = localTagNormalizer4.normalize(str18);
      this.mCanonicalAlbum = str19;
      if ((TextUtils.isEmpty(this.mCanonicalAlbum)) && (this.mAlbumName != null))
      {
        String str20 = this.mAlbumName;
        this.mCanonicalAlbum = str20;
      }
    }
    boolean bool;
    if ((this.mCanonicalAlbumArtist == null) || (this.mCanonicalTrackArtist == null) || (this.mAlbumArtistId == 0L) || (this.mTrackArtistId == 0L))
    {
      TagNormalizer localTagNormalizer5 = this.mNormalizer;
      String str21 = this.mAlbumArtist;
      String str22 = localTagNormalizer5.normalize(str21);
      this.mCanonicalAlbumArtist = str22;
      if ((TextUtils.isEmpty(this.mCanonicalAlbumArtist)) && (this.mAlbumArtist != null))
      {
        String str23 = this.mAlbumArtist;
        this.mCanonicalAlbumArtist = str23;
      }
      long l2 = Store.generateId(this.mCanonicalAlbumArtist);
      this.mAlbumArtistId = l2;
      if ((this.mAlbumArtistOrigin != 1) && (this.mTrackArtistOrigin != 1))
      {
        String str24 = this.mTrackArtist;
        String str25 = this.mAlbumArtist;
        if (stringChanged(str24, str25));
      }
      else
      {
        String str26 = this.mCanonicalAlbumArtist;
        this.mCanonicalTrackArtist = str26;
        long l3 = this.mAlbumArtistId;
        this.mTrackArtistId = l3;
      }
    }
    else if ((this.mSongId == 0L) || (this.mAlbumId == 0L))
    {
      bool = TextUtils.isEmpty(this.mCanonicalAlbum);
      StringBuilder localStringBuilder3 = new StringBuilder();
      String str27 = this.mCanonicalAlbum;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str27);
      k = 0;
      if (((this.mAlbumArtistOrigin != 0) && (this.mAlbumArtistOrigin != 2)) || ((bool) && (TextUtils.isEmpty(this.mCanonicalAlbumArtist))))
        break label1108;
      StringBuilder localStringBuilder5 = k.append('\037');
      String str28 = this.mCanonicalAlbumArtist;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(str28);
    }
    for (int k = 1; ; k = 1)
    {
      label1108: 
      do
      {
        if (this.mAlbumId == 0L)
        {
          String str29 = k.toString();
          this.mAlbumIdSourceText = str29;
          long l4 = Store.generateId(this.mAlbumIdSourceText);
          this.mAlbumId = l4;
        }
        if (this.mSongId == 0L)
        {
          StringBuilder localStringBuilder7 = k.append('\037');
          String str30 = this.mCanonicalTitle;
          StringBuilder localStringBuilder8 = localStringBuilder7.append(str30);
          StringBuilder localStringBuilder9 = k.append('\037');
          int m = this.mDiscPosition;
          StringBuilder localStringBuilder10 = localStringBuilder9.append(m).append('\037');
          int n = this.mTrackPositionInAlbum;
          StringBuilder localStringBuilder11 = localStringBuilder10.append(n);
          if (this.mCanonicalTrackArtist.length() > 0)
            if (k != 0)
            {
              String str31 = this.mCanonicalTrackArtist;
              String str32 = this.mCanonicalAlbumArtist;
              if (str31.equals(str32));
            }
            else
            {
              StringBuilder localStringBuilder12 = k.append('\037');
              String str33 = this.mCanonicalTrackArtist;
              StringBuilder localStringBuilder13 = localStringBuilder12.append(str33);
            }
          long l5 = Store.generateId(k.toString());
          this.mSongId = l5;
        }
        if (TextUtils.isEmpty(this.mCanonicalAlbum))
        {
          String str34 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
          this.mCanonicalAlbum = str34;
        }
        if (TextUtils.isEmpty(this.mCanonicalAlbumArtist))
        {
          String str35 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
          this.mCanonicalAlbumArtist = str35;
        }
        if (!TextUtils.isEmpty(this.mCanonicalTrackArtist))
          return;
        String str36 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
        this.mCanonicalTrackArtist = str36;
        return;
        TagNormalizer localTagNormalizer6 = this.mNormalizer;
        String str37 = this.mTrackArtist;
        String str38 = localTagNormalizer6.normalize(str37);
        this.mCanonicalTrackArtist = str38;
        if ((TextUtils.isEmpty(this.mCanonicalTrackArtist)) && (this.mTrackArtist != null))
        {
          String str39 = this.mTrackArtist;
          this.mCanonicalTrackArtist = str39;
        }
        long l6 = Store.generateId(this.mCanonicalTrackArtist);
        this.mTrackArtistId = l6;
        break;
      }
      while ((!bool) || (TextUtils.isEmpty(this.mCanonicalTrackArtist)));
      StringBuilder localStringBuilder14 = k.append('\037');
      String str40 = this.mCanonicalTrackArtist;
      StringBuilder localStringBuilder15 = localStringBuilder14.append(str40);
    }
  }

  public final void setDiscCount(short paramShort)
  {
    this.mDiscCount = paramShort;
  }

  public final void setDiscPosition(short paramShort)
  {
    this.mDiscPosition = paramShort;
  }

  public void setDomain(ContentIdentifier.Domain paramDomain)
  {
    int i = convertDomain(paramDomain);
    this.mDomain = i;
  }

  public final void setDurationInMilliSec(long paramLong)
  {
    this.mDurationInMilliSec = paramLong;
  }

  public final void setFileType(int paramInt)
  {
    this.mFileType = paramInt;
  }

  public final void setGenre(String paramString)
  {
    if (!stringChanged(this.mGenre, paramString))
      return;
    this.mGenre = paramString;
    this.mGenreId = 0L;
    this.mCanonicalGenre = null;
  }

  public final void setLocalCopyType(int paramInt)
  {
    this.mLocalCopyType = paramInt;
  }

  public final void setMimeType(String paramString)
  {
    this.mFileType = 5;
  }

  public final void setRating(int paramInt)
  {
    throwIfInvalidRating(paramInt);
    this.mRating = paramInt;
  }

  public final void setSize(long paramLong)
  {
    this.mSize = paramLong;
  }

  public final void setSourceType(int paramInt)
  {
    this.mSourceType = paramInt;
  }

  public final void setStoreSongId(String paramString)
  {
    this.mStoreSongId = paramString;
  }

  public final void setTagNormalizer(TagNormalizer paramTagNormalizer)
  {
    this.mNormalizer = paramTagNormalizer;
  }

  public final void setTitle(String paramString)
  {
    if (!stringChanged(this.mTitle, paramString))
      return;
    this.mTitle = paramString;
    this.mCanonicalTitle = null;
    this.mSongId = 0L;
  }

  public final void setTrackArtist(String paramString)
  {
    setTrackArtist(paramString, 0);
  }

  public final void setTrackMetajamId(String paramString)
  {
    this.mTrackMetajamId = paramString;
  }

  public final void setTrackPositionInAlbum(short paramShort)
  {
    this.mTrackPositionInAlbum = paramShort;
  }

  public final void setTrackType(int paramInt)
  {
    this.mTrackType = paramInt;
  }

  public final void setYear(short paramShort)
  {
    this.mYear = paramShort;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    StringBuilder localStringBuilder2 = localStringBuilder1.append("id=");
    long l1 = this.mLocalId;
    StringBuilder localStringBuilder3 = localStringBuilder2.append(l1).append("; ");
    StringBuilder localStringBuilder4 = localStringBuilder1.append("account=");
    int i = this.mSourceAccount;
    StringBuilder localStringBuilder5 = localStringBuilder4.append(i).append("; ");
    StringBuilder localStringBuilder6 = localStringBuilder1.append("sourceId=");
    String str1 = this.mSourceId;
    StringBuilder localStringBuilder7 = localStringBuilder6.append(str1).append("; ");
    StringBuilder localStringBuilder8 = localStringBuilder1.append("sourceType=");
    int j = this.mSourceType;
    StringBuilder localStringBuilder9 = localStringBuilder8.append(j).append("; ");
    StringBuilder localStringBuilder10 = localStringBuilder1.append("addedTime=");
    long l2 = this.mAddedTime;
    StringBuilder localStringBuilder11 = localStringBuilder10.append(l2).append("; ");
    StringBuilder localStringBuilder12 = localStringBuilder1.append("size=");
    long l3 = this.mSize;
    StringBuilder localStringBuilder13 = localStringBuilder12.append(l3).append("; ");
    StringBuilder localStringBuilder14 = localStringBuilder1.append("fileType=");
    int k = this.mFileType;
    StringBuilder localStringBuilder15 = localStringBuilder14.append(k).append("; ");
    StringBuilder localStringBuilder16 = localStringBuilder1.append("localPath=");
    String str2 = this.mLocalCopyPath;
    StringBuilder localStringBuilder17 = localStringBuilder16.append(str2).append("; ");
    StringBuilder localStringBuilder18 = localStringBuilder1.append("localCopyType=");
    int m = this.mLocalCopyType;
    StringBuilder localStringBuilder19 = localStringBuilder18.append(m).append("; ");
    StringBuilder localStringBuilder20 = localStringBuilder1.append("localCopyStorageType=");
    int n = this.mLocalCopyStorageType;
    StringBuilder localStringBuilder21 = localStringBuilder20.append(n).append("; ");
    StringBuilder localStringBuilder22 = localStringBuilder1.append("title=");
    String str3 = this.mTitle;
    StringBuilder localStringBuilder23 = localStringBuilder22.append(str3).append("; ");
    StringBuilder localStringBuilder24 = localStringBuilder1.append("artist=");
    String str4 = this.mTrackArtist;
    StringBuilder localStringBuilder25 = localStringBuilder24.append(str4).append("; ");
    StringBuilder localStringBuilder26 = localStringBuilder1.append("artistOrigin=");
    int i1 = this.mTrackArtistOrigin;
    StringBuilder localStringBuilder27 = localStringBuilder26.append(i1).append("; ");
    StringBuilder localStringBuilder28 = localStringBuilder1.append("albumArtist=");
    String str5 = this.mAlbumArtist;
    StringBuilder localStringBuilder29 = localStringBuilder28.append(str5).append("; ");
    StringBuilder localStringBuilder30 = localStringBuilder1.append("albumArtistOrigin=");
    int i2 = this.mAlbumArtistOrigin;
    StringBuilder localStringBuilder31 = localStringBuilder30.append(i2).append("; ");
    StringBuilder localStringBuilder32 = localStringBuilder1.append("album=");
    String str6 = this.mAlbumName;
    StringBuilder localStringBuilder33 = localStringBuilder32.append(str6).append("; ");
    StringBuilder localStringBuilder34 = localStringBuilder1.append("composer=");
    String str7 = this.mComposer;
    StringBuilder localStringBuilder35 = localStringBuilder34.append(str7).append("; ");
    StringBuilder localStringBuilder36 = localStringBuilder1.append("genre=");
    String str8 = this.mGenre;
    StringBuilder localStringBuilder37 = localStringBuilder36.append(str8).append("; ");
    StringBuilder localStringBuilder38 = localStringBuilder1.append("position=");
    int i3 = this.mTrackPositionInAlbum;
    StringBuilder localStringBuilder39 = localStringBuilder38.append(i3).append("; ");
    StringBuilder localStringBuilder40 = localStringBuilder1.append("year=");
    int i4 = this.mYear;
    StringBuilder localStringBuilder41 = localStringBuilder40.append(i4).append("; ");
    StringBuilder localStringBuilder42 = localStringBuilder1.append("albumArtLocation=");
    String str9 = this.mAlbumArtLocation;
    StringBuilder localStringBuilder43 = localStringBuilder42.append(str9).append("; ");
    StringBuilder localStringBuilder44 = localStringBuilder1.append("artistArtLocation=");
    String str10 = this.mArtistArtLocation;
    StringBuilder localStringBuilder45 = localStringBuilder44.append(str10).append("; ");
    StringBuilder localStringBuilder46 = localStringBuilder1.append("rating=");
    int i5 = this.mRating;
    StringBuilder localStringBuilder47 = localStringBuilder46.append(i5).append(": ");
    if (!TextUtils.isEmpty(this.mStoreSongId))
    {
      StringBuilder localStringBuilder48 = localStringBuilder1.append("storeSongId=");
      String str11 = this.mStoreSongId;
      StringBuilder localStringBuilder49 = localStringBuilder48.append(str11).append(": ");
    }
    if (!TextUtils.isEmpty(this.mAlbumMetajamId))
    {
      StringBuilder localStringBuilder50 = localStringBuilder1.append("albumMetajamId=");
      String str12 = this.mAlbumMetajamId;
      StringBuilder localStringBuilder51 = localStringBuilder50.append(str12).append(": ");
    }
    if (!TextUtils.isEmpty(this.mTrackMetajamId))
    {
      StringBuilder localStringBuilder52 = localStringBuilder1.append("trackMetajamId=");
      String str13 = this.mTrackMetajamId;
      StringBuilder localStringBuilder53 = localStringBuilder52.append(str13).append(": ");
    }
    if (!TextUtils.isEmpty(this.mClientId))
    {
      StringBuilder localStringBuilder54 = localStringBuilder1.append("clientId=");
      String str14 = this.mClientId;
      StringBuilder localStringBuilder55 = localStringBuilder54.append(str14);
    }
    if (!TextUtils.isEmpty(this.mArtistMetajamId))
    {
      StringBuilder localStringBuilder56 = localStringBuilder1.append("artistMetajamId=");
      String str15 = this.mArtistMetajamId;
      StringBuilder localStringBuilder57 = localStringBuilder56.append(str15);
    }
    return localStringBuilder1.toString();
  }

  public final long tryToInsertMusicFile(SQLiteStatement paramSQLiteStatement)
  {
    long l1 = 65535L;
    if (this.mLocalId != 0L)
      throw new IllegalStateException("MusicFile already created. Forgot to call reset()?");
    if (this.mClientId == null)
    {
      String str1 = Store.generateClientId();
      this.mClientId = str1;
    }
    try
    {
      prepareInsertOrFullUpdate(paramSQLiteStatement);
      l2 = paramSQLiteStatement.executeInsert();
      if (l2 == 65535L)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Failed to insert music file: ");
        String str2 = this.mSourceId;
        String str3 = str2;
        int i = Log.e("MusicStore", str3);
        return l1;
      }
    }
    catch (InvalidDataException localInvalidDataException)
    {
      while (true)
      {
        long l2;
        Object[] arrayOfObject = new Object[1];
        String str4 = this.mSourceId;
        arrayOfObject[0] = str4;
        String str5 = String.format("Failed to insert music file %s. Invalid Data", arrayOfObject);
        int j = Log.e("MusicStore", str5, localInvalidDataException);
        continue;
        this.mLocalId = l2;
        l1 = this.mLocalId;
      }
    }
  }

  public final boolean tryToUpdateMusicFile(SQLiteStatement paramSQLiteStatement, SQLiteDatabase paramSQLiteDatabase)
  {
    boolean bool = false;
    try
    {
      updateMusicFile(paramSQLiteStatement, paramSQLiteDatabase);
      bool = true;
      return bool;
    }
    catch (RuntimeException localRuntimeException)
    {
      while (true)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Failed to update file: ");
        String str1 = this.mSourceId;
        String str2 = str1;
        int i = Log.e("MusicStore", str2, localRuntimeException);
      }
    }
  }

  public final void updateMusicFile(SQLiteStatement paramSQLiteStatement, SQLiteDatabase paramSQLiteDatabase)
  {
    if (this.mLocalId == 0L)
      throw new IllegalStateException("Music file ID must be known in order to update db record");
    prepareInsertOrFullUpdate(paramSQLiteStatement);
    long l = this.mLocalId;
    paramSQLiteStatement.bindLong(53, l);
    paramSQLiteStatement.execute();
    if (!this.mAlbumArtLocationChanged)
      return;
    String[] arrayOfString = new String[1];
    String str = Long.toString(this.mAlbumId);
    arrayOfString[0] = str;
    int i = paramSQLiteDatabase.delete("ARTWORK", "AlbumId=?", arrayOfString);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.MusicFile
 * JD-Core Version:    0.6.2
 */