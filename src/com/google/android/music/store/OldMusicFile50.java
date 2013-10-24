package com.google.android.music.store;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

public class OldMusicFile50 extends Syncable
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
  private String mAlbumName;
  private int mBitrate;
  private String mCanonicalAlbum;
  private String mCanonicalAlbumArtist;
  private String mCanonicalGenre;
  private String mCanonicalTitle;
  private String mCanonicalTrackArtist;
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
  private String mStoreAlbumId;
  private String mStoreSongId;
  private String mTitle;
  private String mTrackArtist;
  private long mTrackArtistId;
  private int mTrackArtistOrigin = 0;
  private short mTrackCountInAlbum;
  private short mTrackPositionInAlbum;
  private int mTrackType = 0;
  private short mYear;

  static
  {
    MEDIA_STORE_SOURCE_ACCOUNT_AS_INTEGER = new Integer(0);
    String[] arrayOfString1 = new String[15];
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
    SUMMARY_PROJECTION = arrayOfString1;
    String[] arrayOfString2 = new String[47];
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
    FULL_PROJECTION = arrayOfString2;
    String[] arrayOfString3 = new String[4];
    arrayOfString3[0] = "Id";
    arrayOfString3[1] = "LocalCopyType";
    arrayOfString3[2] = "LocalCopyPath";
    arrayOfString3[3] = "LocalCopyStorageType";
    DELETE_PROJECTION = arrayOfString3;
    String[] arrayOfString4 = new String[4];
    arrayOfString4[0] = "Id";
    arrayOfString4[1] = "SourceAccount";
    arrayOfString4[2] = "SourceId";
    arrayOfString4[3] = "PlayCount";
  }

  // ERROR //
  public static void fixUnknownAlbumsAndArtists50(SQLiteDatabase paramSQLiteDatabase)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: iconst_1
    //   3: anewarray 68	java/lang/String
    //   6: astore_2
    //   7: aload_2
    //   8: iconst_0
    //   9: ldc 84
    //   11: aastore
    //   12: aload_0
    //   13: astore_3
    //   14: iload_1
    //   15: istore 4
    //   17: iload_1
    //   18: istore 5
    //   20: iload_1
    //   21: istore 6
    //   23: aload_3
    //   24: ldc 212
    //   26: aload_2
    //   27: ldc 214
    //   29: iload_1
    //   30: iload 4
    //   32: iload 5
    //   34: iload 6
    //   36: invokevirtual 220	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   39: istore 7
    //   41: iload 7
    //   43: ifeq +226 -> 269
    //   46: new 2	com/google/android/music/store/OldMusicFile50
    //   49: dup
    //   50: invokespecial 221	com/google/android/music/store/OldMusicFile50:<init>	()V
    //   53: astore 8
    //   55: aload_0
    //   56: ldc 223
    //   58: invokevirtual 227	android/database/sqlite/SQLiteDatabase:compileStatement	(Ljava/lang/String;)Landroid/database/sqlite/SQLiteStatement;
    //   61: astore 9
    //   63: aload 9
    //   65: astore 10
    //   67: iload 7
    //   69: invokeinterface 233 1 0
    //   74: istore 11
    //   76: iconst_0
    //   77: istore 12
    //   79: iload 7
    //   81: invokeinterface 237 1 0
    //   86: ifeq +33 -> 119
    //   89: iload 7
    //   91: iconst_0
    //   92: invokeinterface 241 2 0
    //   97: lstore 13
    //   99: iload 12
    //   101: iconst_1
    //   102: iadd
    //   103: istore 15
    //   105: iload 11
    //   107: iload 12
    //   109: lload 13
    //   111: lastore
    //   112: iload 15
    //   114: istore 16
    //   116: goto -37 -> 79
    //   119: iload 7
    //   121: invokestatic 247	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   124: iload 11
    //   126: arraylength
    //   127: istore 12
    //   129: iconst_0
    //   130: istore 7
    //   132: iload 7
    //   134: iload 12
    //   136: if_icmpge +92 -> 228
    //   139: iload 11
    //   141: iload 7
    //   143: laload
    //   144: lstore 17
    //   146: aload 8
    //   148: aload_0
    //   149: lload 17
    //   151: invokevirtual 251	com/google/android/music/store/OldMusicFile50:load	(Landroid/database/sqlite/SQLiteDatabase;J)V
    //   154: aload 8
    //   156: invokevirtual 254	com/google/android/music/store/OldMusicFile50:resetDerivedFields	()V
    //   159: aload 8
    //   161: aload 10
    //   163: aload_0
    //   164: invokevirtual 258	com/google/android/music/store/OldMusicFile50:updateMusicFile	(Landroid/database/sqlite/SQLiteStatement;Landroid/database/sqlite/SQLiteDatabase;)V
    //   167: iload 7
    //   169: iconst_1
    //   170: iadd
    //   171: istore 7
    //   173: goto -41 -> 132
    //   176: astore 19
    //   178: new 260	java/lang/StringBuilder
    //   181: dup
    //   182: invokespecial 261	java/lang/StringBuilder:<init>	()V
    //   185: ldc_w 263
    //   188: invokevirtual 267	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   191: lload 17
    //   193: invokevirtual 270	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   196: invokevirtual 274	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   199: astore 20
    //   201: ldc_w 276
    //   204: aload 20
    //   206: invokestatic 282	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   209: istore 21
    //   211: goto -44 -> 167
    //   214: astore 22
    //   216: iload_1
    //   217: invokestatic 247	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   220: aload 10
    //   222: invokestatic 285	com/google/android/music/store/Store:safeClose	(Landroid/database/sqlite/SQLiteProgram;)V
    //   225: aload 22
    //   227: athrow
    //   228: aload 10
    //   230: astore 23
    //   232: iload_1
    //   233: invokestatic 247	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   236: aload 23
    //   238: invokestatic 285	com/google/android/music/store/Store:safeClose	(Landroid/database/sqlite/SQLiteProgram;)V
    //   241: return
    //   242: astore 24
    //   244: aconst_null
    //   245: astore 10
    //   247: iload 7
    //   249: istore_1
    //   250: aload 24
    //   252: astore 22
    //   254: goto -38 -> 216
    //   257: astore 25
    //   259: iload 7
    //   261: istore_1
    //   262: aload 25
    //   264: astore 22
    //   266: goto -50 -> 216
    //   269: iload 7
    //   271: istore 26
    //   273: aconst_null
    //   274: astore 23
    //   276: iload 26
    //   278: istore_1
    //   279: goto -47 -> 232
    //
    // Exception table:
    //   from	to	target	type
    //   146	167	176	com/google/android/music/store/DataNotFoundException
    //   124	146	214	finally
    //   146	167	214	finally
    //   178	211	214	finally
    //   46	63	242	finally
    //   67	124	257	finally
  }

  private void prepareInsertOrFullUpdate(SQLiteStatement paramSQLiteStatement)
  {
    if ((this.mSourceId == null) || (this.mSourceId.length() == 0))
      throw new InvalidDataException("Source id must be set before saving to DB");
    int i;
    if (!TextUtils.isEmpty(this.mStoreSongId))
    {
      i = 1;
      if ((this.mTrackType != 1) && (this.mTrackType != 3) && (this.mTrackType != 2))
        break label164;
    }
    label164: for (int j = 1; ; j = 0)
    {
      if ((i == 0) || (j != 0))
        break label169;
      StringBuilder localStringBuilder1 = new StringBuilder().append("Store song id is set for track that is not promo or purchase. Store song id: ");
      String str1 = this.mStoreSongId;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" ServerId:");
      String str2 = this.mSourceId;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(str2).append(" Title: ");
      String str3 = this.mTitle;
      String str4 = str3;
      throw new InvalidDataException(str4);
      i = 0;
      break;
    }
    label169: if (!TextUtils.isEmpty(this.mStoreAlbumId));
    for (int k = 1; (k != 0) && (j == 0); k = 0)
    {
      StringBuilder localStringBuilder4 = new StringBuilder().append("Store album ID is set for track that is not promo or purchase. Store album ID: ");
      String str5 = this.mStoreAlbumId;
      StringBuilder localStringBuilder5 = localStringBuilder4.append(str5).append(" ServerId:");
      String str6 = this.mSourceId;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(str6).append(" Title: ");
      String str7 = this.mTitle;
      String str8 = str7;
      throw new InvalidDataException(str8);
    }
    boolean bool = TextUtils.isEmpty(this.mLocalCopyPath);
    switch (this.mLocalCopyStorageType)
    {
    default:
      StringBuilder localStringBuilder7 = new StringBuilder().append("Invalid storage type:");
      int m = this.mLocalCopyStorageType;
      String str9 = m;
      throw new InvalidDataException(str9);
    case 0:
      if (!bool)
      {
        StringBuilder localStringBuilder8 = new StringBuilder().append("Local path is set for storage type NONE: ");
        String str10 = this.mLocalCopyPath;
        String str11 = str10;
        throw new InvalidDataException(str11);
      }
      break;
    case 1:
      if (bool)
        throw new InvalidDataException("Local path is not set for storage type INTERNAL");
      break;
    case 2:
      if (bool)
        throw new InvalidDataException("Local path is not set for storage type EXTERNAL");
      break;
    }
    setDerivedFields();
    paramSQLiteStatement.clearBindings();
    long l1 = this.mSourceAccount;
    paramSQLiteStatement.bindLong(1, l1);
    long l2 = this.mTrackType;
    paramSQLiteStatement.bindLong(37, l2);
    String str12 = this.mSourceId;
    paramSQLiteStatement.bindString(2, str12);
    label557: String str13;
    label618: String str14;
    label642: String str15;
    label666: String str16;
    label705: String str17;
    label744: String str18;
    label768: long l17;
    label881: String str19;
    label927: label1013: String str20;
    label1037: String str21;
    label1061: String str22;
    label1085: String str23;
    label1109: long l28;
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
        break label1268;
      paramSQLiteStatement.bindNull(7);
      long l6 = this.mLocalCopyType;
      paramSQLiteStatement.bindLong(8, l6);
      long l7 = this.mLocalCopyStorageType;
      paramSQLiteStatement.bindLong(45, l7);
      long l8 = this.mLocalCopyBitrate;
      paramSQLiteStatement.bindLong(36, l8);
      int n = 9;
      if (this.mTitle != null)
        break label1285;
      str13 = "";
      paramSQLiteStatement.bindString(n, str13);
      int i1 = 10;
      if (this.mAlbumName != null)
        break label1294;
      str14 = "";
      paramSQLiteStatement.bindString(i1, str14);
      int i2 = 11;
      if (this.mTrackArtist != null)
        break label1303;
      str15 = "";
      paramSQLiteStatement.bindString(i2, str15);
      long l9 = this.mTrackArtistOrigin;
      paramSQLiteStatement.bindLong(38, l9);
      int i3 = 12;
      if (this.mAlbumArtist != null)
        break label1312;
      str16 = "";
      paramSQLiteStatement.bindString(i3, str16);
      long l10 = this.mAlbumArtistOrigin;
      paramSQLiteStatement.bindLong(13, l10);
      int i4 = 14;
      if (this.mComposer != null)
        break label1321;
      str17 = "";
      paramSQLiteStatement.bindString(i4, str17);
      int i5 = 15;
      if (this.mGenre != null)
        break label1330;
      str18 = "";
      paramSQLiteStatement.bindString(i5, str18);
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
      int i6 = 22;
      if (!this.mCompilation)
        break label1339;
      l17 = 1L;
      paramSQLiteStatement.bindLong(i6, l17);
      long l18 = this.mBitrate;
      paramSQLiteStatement.bindLong(23, l18);
      if ((this.mAlbumArtLocation != null) && (this.mAlbumArtLocation.length() != 0))
        break label1347;
      paramSQLiteStatement.bindNull(24);
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
      int i7 = 29;
      if (this.mCanonicalTitle != null)
        break label1364;
      str19 = "";
      paramSQLiteStatement.bindString(i7, str19);
      int i8 = 30;
      if (this.mCanonicalAlbum != null)
        break label1373;
      str20 = "";
      paramSQLiteStatement.bindString(i8, str20);
      int i9 = 31;
      if (this.mCanonicalAlbumArtist != null)
        break label1382;
      str21 = "";
      paramSQLiteStatement.bindString(i9, str21);
      int i10 = 40;
      if (this.mCanonicalTrackArtist != null)
        break label1391;
      str22 = "";
      paramSQLiteStatement.bindString(i10, str22);
      int i11 = 32;
      if (this.mCanonicalGenre != null)
        break label1400;
      str23 = "";
      paramSQLiteStatement.bindString(i11, str23);
      long l24 = this.mPlayCount;
      paramSQLiteStatement.bindLong(33, l24);
      long l25 = this.mLastPlayDate;
      paramSQLiteStatement.bindLong(34, l25);
      long l26 = this.mLocalCopySize;
      paramSQLiteStatement.bindLong(35, l26);
      long l27 = this.mRating;
      paramSQLiteStatement.bindLong(41, l27);
      int i12 = 42;
      if (!this.mNeedsSync)
        break label1409;
      l28 = 1L;
      label1191: paramSQLiteStatement.bindLong(i12, l28);
      if (i == 0)
        break label1417;
      String str24 = this.mStoreSongId;
      paramSQLiteStatement.bindString(43, str24);
      label1217: if (k == 0)
        break label1426;
      String str25 = this.mStoreAlbumId;
      paramSQLiteStatement.bindString(44, str25);
    }
    while (true)
    {
      long l29 = this.mDomain;
      paramSQLiteStatement.bindLong(46, l29);
      return;
      String str26 = this.mSourceVersion;
      paramSQLiteStatement.bindString(3, str26);
      break;
      label1268: String str27 = this.mLocalCopyPath;
      paramSQLiteStatement.bindString(7, str27);
      break label557;
      label1285: str13 = this.mTitle;
      break label618;
      label1294: str14 = this.mAlbumName;
      break label642;
      label1303: str15 = this.mTrackArtist;
      break label666;
      label1312: str16 = this.mAlbumArtist;
      break label705;
      label1321: str17 = this.mComposer;
      break label744;
      label1330: str18 = this.mGenre;
      break label768;
      label1339: l17 = 0L;
      break label881;
      label1347: String str28 = this.mAlbumArtLocation;
      paramSQLiteStatement.bindString(24, str28);
      break label927;
      label1364: str19 = this.mCanonicalTitle;
      break label1013;
      label1373: str20 = this.mCanonicalAlbum;
      break label1037;
      label1382: str21 = this.mCanonicalAlbumArtist;
      break label1061;
      label1391: str22 = this.mCanonicalTrackArtist;
      break label1085;
      label1400: str23 = this.mCanonicalGenre;
      break label1109;
      label1409: l28 = 0L;
      break label1191;
      label1417: paramSQLiteStatement.bindNull(43);
      break label1217;
      label1426: paramSQLiteStatement.bindNull(44);
    }
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

  void load(SQLiteDatabase paramSQLiteDatabase, long paramLong)
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
        break label776;
      bool = true;
      label425: this.mCompilation = bool;
      int i5 = paramCursor.getInt(22);
      this.mBitrate = i5;
      if (!paramCursor.isNull(23))
        break label782;
      this.mAlbumArtLocation = null;
      label463: this.mAlbumArtLocationChanged = false;
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
        break label801;
      label704: this.mNeedsSync = i;
      if (!paramCursor.isNull(43))
        break label806;
      this.mStoreSongId = null;
      label725: if (!paramCursor.isNull(44))
        break label825;
    }
    label776: label782: label801: label806: label825: String str17;
    for (this.mStoreAlbumId = null; ; this.mStoreAlbumId = str17)
    {
      int i8 = paramCursor.getInt(46);
      this.mDomain = i8;
      return;
      String str14 = paramCursor.getString(3);
      this.mSourceVersion = str14;
      break;
      bool = false;
      break label425;
      String str15 = paramCursor.getString(23);
      this.mAlbumArtLocation = str15;
      break label463;
      i = 0;
      break label704;
      String str16 = paramCursor.getString(43);
      this.mStoreSongId = str16;
      break label725;
      str17 = paramCursor.getString(44);
    }
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
    this.mRating = 0;
    this.mStoreSongId = null;
    this.mStoreAlbumId = null;
    this.mDomain = 0;
    resetDerivedFields();
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
        break label286;
      j = 1;
      label140: if ((i != 0) || (j == 0))
        break label292;
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
        break label374;
      if ((this.mTitle != null) && (this.mTitle.length() != 0))
        break label318;
      throw new InvalidDataException("Song title must not be empty");
      i = 0;
      break;
      label286: j = 0;
      break label140;
      label292: if ((j == 0) && (i != 0))
      {
        String str11 = this.mAlbumArtist;
        setTrackArtist(str11, 1);
      }
    }
    label318: TagNormalizer localTagNormalizer3 = this.mNormalizer;
    String str12 = this.mTitle;
    String str13 = localTagNormalizer3.normalize(str12);
    this.mCanonicalTitle = str13;
    if ((TextUtils.isEmpty(this.mCanonicalTitle)) && (this.mTitle != null))
    {
      String str14 = this.mTitle;
      this.mCanonicalTitle = str14;
    }
    label374: if (this.mCanonicalAlbum == null)
    {
      TagNormalizer localTagNormalizer4 = this.mNormalizer;
      String str15 = this.mAlbumName;
      String str16 = localTagNormalizer4.normalize(str15);
      this.mCanonicalAlbum = str16;
      if ((TextUtils.isEmpty(this.mCanonicalAlbum)) && (this.mAlbumName != null))
      {
        String str17 = this.mAlbumName;
        this.mCanonicalAlbum = str17;
      }
    }
    boolean bool;
    if ((this.mCanonicalAlbumArtist == null) || (this.mCanonicalTrackArtist == null) || (this.mAlbumArtistId == 0L) || (this.mTrackArtistId == 0L))
    {
      TagNormalizer localTagNormalizer5 = this.mNormalizer;
      String str18 = this.mAlbumArtist;
      String str19 = localTagNormalizer5.normalize(str18);
      this.mCanonicalAlbumArtist = str19;
      if ((TextUtils.isEmpty(this.mCanonicalAlbumArtist)) && (this.mAlbumArtist != null))
      {
        String str20 = this.mAlbumArtist;
        this.mCanonicalAlbumArtist = str20;
      }
      long l2 = Store.generateId(this.mCanonicalAlbumArtist);
      this.mAlbumArtistId = l2;
      if ((this.mAlbumArtistOrigin != 1) && (this.mTrackArtistOrigin != 1))
      {
        String str21 = this.mTrackArtist;
        String str22 = this.mAlbumArtist;
        if (stringChanged(str21, str22));
      }
      else
      {
        String str23 = this.mCanonicalAlbumArtist;
        this.mCanonicalTrackArtist = str23;
        long l3 = this.mAlbumArtistId;
        this.mTrackArtistId = l3;
      }
    }
    else if ((this.mSongId == 0L) || (this.mAlbumId == 0L))
    {
      bool = TextUtils.isEmpty(this.mCanonicalAlbum);
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str24 = this.mCanonicalAlbum;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str24);
      k = 0;
      if (((this.mAlbumArtistOrigin != 0) && (this.mAlbumArtistOrigin != 2)) || ((bool) && (TextUtils.isEmpty(this.mCanonicalAlbumArtist))))
        break label1043;
      StringBuilder localStringBuilder3 = k.append('\037');
      String str25 = this.mCanonicalAlbumArtist;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str25);
    }
    for (int k = 1; ; k = 1)
    {
      label1043: 
      do
      {
        if (this.mAlbumId == 0L)
        {
          long l4 = Store.generateId(k.toString());
          this.mAlbumId = l4;
        }
        if (this.mSongId == 0L)
        {
          StringBuilder localStringBuilder5 = k.append('\037');
          String str26 = this.mCanonicalTitle;
          StringBuilder localStringBuilder6 = localStringBuilder5.append(str26);
          StringBuilder localStringBuilder7 = k.append('\037');
          int m = this.mDiscPosition;
          StringBuilder localStringBuilder8 = localStringBuilder7.append(m).append('\037');
          int n = this.mTrackPositionInAlbum;
          StringBuilder localStringBuilder9 = localStringBuilder8.append(n);
          if (this.mCanonicalTrackArtist.length() > 0)
            if (k != 0)
            {
              String str27 = this.mCanonicalTrackArtist;
              String str28 = this.mCanonicalAlbumArtist;
              if (str27.equals(str28));
            }
            else
            {
              StringBuilder localStringBuilder10 = k.append('\037');
              String str29 = this.mCanonicalTrackArtist;
              StringBuilder localStringBuilder11 = localStringBuilder10.append(str29);
            }
          long l5 = Store.generateId(k.toString());
          this.mSongId = l5;
        }
        if (TextUtils.isEmpty(this.mCanonicalAlbum))
        {
          String str30 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
          this.mCanonicalAlbum = str30;
        }
        if (TextUtils.isEmpty(this.mCanonicalAlbumArtist))
        {
          String str31 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
          this.mCanonicalAlbumArtist = str31;
        }
        if (!TextUtils.isEmpty(this.mCanonicalTrackArtist))
          return;
        String str32 = Schema.Music.EMPTY_CANONICAL_SORT_KEY;
        this.mCanonicalTrackArtist = str32;
        return;
        TagNormalizer localTagNormalizer6 = this.mNormalizer;
        String str33 = this.mTrackArtist;
        String str34 = localTagNormalizer6.normalize(str33);
        this.mCanonicalTrackArtist = str34;
        if ((TextUtils.isEmpty(this.mCanonicalTrackArtist)) && (this.mTrackArtist != null))
        {
          String str35 = this.mTrackArtist;
          this.mCanonicalTrackArtist = str35;
        }
        long l6 = Store.generateId(this.mCanonicalTrackArtist);
        this.mTrackArtistId = l6;
        break;
      }
      while ((!bool) || (TextUtils.isEmpty(this.mCanonicalTrackArtist)));
      StringBuilder localStringBuilder12 = k.append('\037');
      String str36 = this.mCanonicalTrackArtist;
      StringBuilder localStringBuilder13 = localStringBuilder12.append(str36);
    }
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
    StringBuilder localStringBuilder8 = localStringBuilder1.append("addedTime=");
    long l2 = this.mAddedTime;
    StringBuilder localStringBuilder9 = localStringBuilder8.append(l2).append("; ");
    StringBuilder localStringBuilder10 = localStringBuilder1.append("size=");
    long l3 = this.mSize;
    StringBuilder localStringBuilder11 = localStringBuilder10.append(l3).append("; ");
    StringBuilder localStringBuilder12 = localStringBuilder1.append("fileType=");
    int j = this.mFileType;
    StringBuilder localStringBuilder13 = localStringBuilder12.append(j).append("; ");
    StringBuilder localStringBuilder14 = localStringBuilder1.append("localPath=");
    String str2 = this.mLocalCopyPath;
    StringBuilder localStringBuilder15 = localStringBuilder14.append(str2).append("; ");
    StringBuilder localStringBuilder16 = localStringBuilder1.append("localCopyType=");
    int k = this.mLocalCopyType;
    StringBuilder localStringBuilder17 = localStringBuilder16.append(k).append("; ");
    StringBuilder localStringBuilder18 = localStringBuilder1.append("localCopyStorageType=");
    int m = this.mLocalCopyStorageType;
    StringBuilder localStringBuilder19 = localStringBuilder18.append(m).append("; ");
    StringBuilder localStringBuilder20 = localStringBuilder1.append("title=");
    String str3 = this.mTitle;
    StringBuilder localStringBuilder21 = localStringBuilder20.append(str3).append("; ");
    StringBuilder localStringBuilder22 = localStringBuilder1.append("artist=");
    String str4 = this.mTrackArtist;
    StringBuilder localStringBuilder23 = localStringBuilder22.append(str4).append("; ");
    StringBuilder localStringBuilder24 = localStringBuilder1.append("artistOrigin=");
    int n = this.mTrackArtistOrigin;
    StringBuilder localStringBuilder25 = localStringBuilder24.append(n).append("; ");
    StringBuilder localStringBuilder26 = localStringBuilder1.append("albumArtist=");
    String str5 = this.mAlbumArtist;
    StringBuilder localStringBuilder27 = localStringBuilder26.append(str5).append("; ");
    StringBuilder localStringBuilder28 = localStringBuilder1.append("albumArtistOrigin=");
    int i1 = this.mAlbumArtistOrigin;
    StringBuilder localStringBuilder29 = localStringBuilder28.append(i1).append("; ");
    StringBuilder localStringBuilder30 = localStringBuilder1.append("album=");
    String str6 = this.mAlbumName;
    StringBuilder localStringBuilder31 = localStringBuilder30.append(str6).append("; ");
    StringBuilder localStringBuilder32 = localStringBuilder1.append("composer=");
    String str7 = this.mComposer;
    StringBuilder localStringBuilder33 = localStringBuilder32.append(str7).append("; ");
    StringBuilder localStringBuilder34 = localStringBuilder1.append("genre=");
    String str8 = this.mGenre;
    StringBuilder localStringBuilder35 = localStringBuilder34.append(str8).append("; ");
    StringBuilder localStringBuilder36 = localStringBuilder1.append("position=");
    int i2 = this.mTrackPositionInAlbum;
    StringBuilder localStringBuilder37 = localStringBuilder36.append(i2).append("; ");
    StringBuilder localStringBuilder38 = localStringBuilder1.append("year=");
    int i3 = this.mYear;
    StringBuilder localStringBuilder39 = localStringBuilder38.append(i3).append("; ");
    StringBuilder localStringBuilder40 = localStringBuilder1.append("artLocation=");
    String str9 = this.mAlbumArtLocation;
    StringBuilder localStringBuilder41 = localStringBuilder40.append(str9).append("; ");
    StringBuilder localStringBuilder42 = localStringBuilder1.append("rating=");
    int i4 = this.mRating;
    StringBuilder localStringBuilder43 = localStringBuilder42.append(i4).append(": ");
    if (!TextUtils.isEmpty(this.mStoreSongId))
    {
      StringBuilder localStringBuilder44 = localStringBuilder1.append("storeSongId=");
      String str10 = this.mStoreSongId;
      StringBuilder localStringBuilder45 = localStringBuilder44.append(str10).append(": ");
    }
    if (!TextUtils.isEmpty(this.mStoreAlbumId))
    {
      StringBuilder localStringBuilder46 = localStringBuilder1.append("storeAlbumId=");
      String str11 = this.mStoreAlbumId;
      StringBuilder localStringBuilder47 = localStringBuilder46.append(str11).append(": ");
    }
    return localStringBuilder1.toString();
  }

  public final void updateMusicFile(SQLiteStatement paramSQLiteStatement, SQLiteDatabase paramSQLiteDatabase)
  {
    if (this.mLocalId == 0L)
      throw new IllegalStateException("Music file ID must be known in order to update db record");
    prepareInsertOrFullUpdate(paramSQLiteStatement);
    long l = this.mLocalId;
    paramSQLiteStatement.bindLong(47, l);
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
 * Qualified Name:     com.google.android.music.store.OldMusicFile50
 * JD-Core Version:    0.6.2
 */