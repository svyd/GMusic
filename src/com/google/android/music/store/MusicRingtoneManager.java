package com.google.android.music.store;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings.System;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import com.google.android.music.utils.IOUtils;
import com.google.android.music.utils.MusicUtils;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MusicRingtoneManager
{
  private static final String[] AUDIO_COLUMNS = arrayOfString2;
  private static final String[] MUSIC_COLUMNS;
  private Store mStore;

  static
  {
    String[] arrayOfString1 = new String[6];
    arrayOfString1[0] = "SourceId";
    arrayOfString1[1] = "SourceAccount";
    arrayOfString1[2] = "LocalCopyPath";
    arrayOfString1[3] = "LocalCopyType";
    arrayOfString1[4] = "Title";
    arrayOfString1[5] = "LocalCopyStorageType";
    MUSIC_COLUMNS = arrayOfString1;
    String[] arrayOfString2 = new String[1];
    arrayOfString2[0] = "_data";
  }

  MusicRingtoneManager(Store paramStore)
  {
    this.mStore = paramStore;
  }

  private static int deleteInvalidRingtoneRequests(SQLiteDatabase paramSQLiteDatabase)
  {
    long l = new Date().getTime() - 2592000000L;
    String[] arrayOfString = new String[1];
    String str1 = Long.toString(l);
    arrayOfString[0] = str1;
    int i = paramSQLiteDatabase.delete("RINGTONES", "RequestDate<?", arrayOfString);
    if (i > 0)
    {
      String str2 = "Expired " + i + " ringtone requests";
      int j = Log.w("MusicRingtones", str2);
    }
    int k = paramSQLiteDatabase.delete("RINGTONES", "Id IN (SELECT RINGTONES.Id FROM RINGTONES LEFT  JOIN MUSIC ON (MusicId = MUSIC.Id)  WHERE MusicId IS NULL)", null);
    if (k > 0)
    {
      String str3 = "Deleted " + k + " ringtone requests";
      int m = Log.w("MusicRingtones", str3);
    }
    return i + k;
  }

  private boolean deleteRingtoneRequest(long paramLong)
  {
    boolean bool = true;
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    while (true)
    {
      try
      {
        String[] arrayOfString = new String[1];
        String str1 = Long.toString(paramLong);
        arrayOfString[0] = str1;
        int i = localSQLiteDatabase.delete("RINGTONES", "MusicId=?", arrayOfString);
        if (i > 1)
        {
          String str2 = "Unexpected number of deleted requests:" + i;
          int j = Log.e("MusicRingtones", str2);
        }
        this.mStore.endWriteTxn(localSQLiteDatabase, true);
        if (i > 0)
          return bool;
      }
      finally
      {
        this.mStore.endWriteTxn(localSQLiteDatabase, false);
      }
      bool = false;
    }
  }

  public static Intent getEditRingtoneIntent(Context paramContext, String paramString, long paramLong)
  {
    Intent localIntent1;
    if (paramString != null)
    {
      localIntent1 = new Intent("com.google.android.music.EDIT_RINGTONE");
      Uri localUri = Uri.parse(paramString);
      Intent localIntent2 = localIntent1.setDataAndType(localUri, "audio/*");
      Intent localIntent3 = localIntent1.putExtra("musicId", paramLong);
    }
    while (true)
    {
      return localIntent1;
      localIntent1 = null;
    }
  }

  private String getFilepathFromMediaStore(Context paramContext, Uri paramUri)
  {
    String[] arrayOfString1 = AUDIO_COLUMNS;
    Context localContext = paramContext;
    Uri localUri = paramUri;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    Object localObject1 = null;
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        String str2 = localCursor.getString(0);
        localObject1 = str2;
      }
      return localObject1;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  private static long getMediaStoreItem(Context paramContext, File paramFile)
  {
    long l1 = 65535L;
    Uri localUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "_id";
    String[] arrayOfString2 = new String[1];
    String str = paramFile.getAbsolutePath();
    arrayOfString2[0] = str;
    Cursor localCursor = MusicUtils.query(paramContext, localUri, arrayOfString1, "_data=?", arrayOfString2, null);
    if (localCursor != null);
    try
    {
      if (localCursor.moveToFirst())
      {
        long l2 = localCursor.getLong(0);
        l1 = l2;
      }
      return l1;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  public static File getRingtoneFile(String paramString)
  {
    File localFile1 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
    if (!localFile1.exists())
      boolean bool1 = localFile1.mkdirs();
    File localFile2;
    if (!localFile1.exists())
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Ringtones directory \"");
      String str1 = localFile1.getAbsolutePath();
      String str2 = str1 + "\" does not exist" + " and could not be created.";
      int i = Log.e("MusicRingtones", str2);
      localFile2 = null;
    }
    while (true)
    {
      return localFile2;
      String str3 = IOUtils.getSafeFilename(paramString, ".mp3");
      localFile2 = new File(localFile1, str3);
      if (localFile2.exists())
      {
        if (!localFile2.canWrite())
          localFile2 = null;
      }
      else
        try
        {
          if (localFile2.createNewFile())
            boolean bool2 = localFile2.delete();
        }
        catch (IOException localIOException)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("Failed to create ringtone file at ");
          String str4 = localFile2.getAbsolutePath();
          String str5 = str4;
          int j = Log.e("MusicRingtones", str5, localIOException);
          localFile2 = null;
          continue;
          localFile2 = null;
        }
        catch (SecurityException localSecurityException)
        {
          StringBuilder localStringBuilder3 = new StringBuilder().append("Not allowed to create ringtone file at ");
          String str6 = localFile2.getAbsolutePath();
          String str7 = str6;
          int k = Log.e("MusicRingtones", str7, localSecurityException);
          localFile2 = null;
        }
    }
  }

  public static long getRingtoneFileSize(int paramInt)
  {
    return Math.min(paramInt * 125L * 480L, 52428800L);
  }

  public static int insertRingtoneInMediaStore(Context paramContext, File paramFile, String paramString)
  {
    int i = 1;
    if (paramContext == null);
    long l;
    Uri localUri2;
    do
    {
      return i;
      l = getMediaStoreItem(paramContext, paramFile);
      if (l != 65535L)
        break;
      ContentValues localContentValues = new ContentValues();
      String str = paramFile.getAbsolutePath();
      localContentValues.put("_data", str);
      localContentValues.put("title", paramString);
      Long localLong = Long.valueOf(paramFile.length());
      localContentValues.put("_size", localLong);
      localContentValues.put("mime_type", "audio/mp3");
      localContentValues.put("is_ringtone", "1");
      localContentValues.put("is_notification", "0");
      localContentValues.put("is_alarm", "0");
      localContentValues.put("is_music", "0");
      Uri localUri1 = MediaStore.Audio.Media.getContentUriForPath(paramFile.getAbsolutePath());
      localUri2 = paramContext.getContentResolver().insert(localUri1, localContentValues);
    }
    while (localUri2 == null);
    setSystemRingtone(paramContext, localUri2);
    while (true)
    {
      i = 0;
      break;
      if (setRingtoneFlagInMediaStore(paramContext, l))
      {
        Uri localUri3 = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, l);
        setSystemRingtone(paramContext, localUri3);
      }
    }
  }

  private void insertRingtoneRequest(long paramLong)
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginWriteTxn();
    try
    {
      int i = deleteInvalidRingtoneRequests(localSQLiteDatabase);
      ContentValues localContentValues = new ContentValues(2);
      Long localLong1 = Long.valueOf(paramLong);
      localContentValues.put("MusicId", localLong1);
      Long localLong2 = Long.valueOf(new Date().getTime());
      localContentValues.put("RequestDate", localLong2);
      if (localSQLiteDatabase.insertOrThrow("RINGTONES", null, localContentValues) == 65535L)
      {
        String str = "Failed to insert ringtone request for music with id " + paramLong;
        throw new RuntimeException(str);
      }
    }
    finally
    {
      this.mStore.endWriteTxn(localSQLiteDatabase, false);
    }
    this.mStore.endWriteTxn(localSQLiteDatabase, true);
  }

  private static RingtoneSource localCopyTypeToRingtoneSource(int paramInt)
  {
    RingtoneSource localRingtoneSource;
    switch (paramInt)
    {
    default:
      String str = "Unexpected localCopyType value:" + paramInt;
      int i = Log.wtf("MusicRingtones", str);
      localRingtoneSource = RingtoneSource.RINGTONE_DOWNLOAD;
    case 0:
    case 300:
    case 200:
    case 100:
    }
    while (true)
    {
      return localRingtoneSource;
      localRingtoneSource = RingtoneSource.RINGTONE_DOWNLOAD;
      continue;
      localRingtoneSource = RingtoneSource.SIDE_LOADED_TRACK;
      continue;
      localRingtoneSource = RingtoneSource.KEEPON_TRACK;
      continue;
      localRingtoneSource = RingtoneSource.CACHED_TRACK;
    }
  }

  private int makeRingtoneFile(long paramLong1, String paramString1, String paramString2, RingtoneSource paramRingtoneSource, String paramString3, long paramLong2, boolean paramBoolean1, boolean paramBoolean2)
  {
    StringBuffer localStringBuffer = new StringBuffer(100);
    MusicRingtoneManager localMusicRingtoneManager1 = this;
    long l1 = paramLong1;
    String str1 = paramString2;
    String str2 = paramString3;
    long l2 = paramLong2;
    boolean bool1 = paramBoolean1;
    try
    {
      int i = localMusicRingtoneManager1.makeRingtoneFileHelper(l1, str1, str2, l2, bool1, localStringBuffer);
      j = i;
      boolean bool2 = deleteRingtoneRequest(paramLong1);
      if (paramBoolean2)
      {
        String str3 = localStringBuffer.toString();
        MusicRingtoneManager localMusicRingtoneManager2 = this;
        long l3 = paramLong1;
        String str4 = paramString3;
        localMusicRingtoneManager2.notifyRingtoneSet(l3, str4, j, str3);
      }
      if (j == 0)
      {
        long l4 = paramLong1;
        String str5 = paramString1;
        RingtoneSource localRingtoneSource = paramRingtoneSource;
        String str6 = paramString3;
        writeRingtoneSavedEvent(l4, str5, localRingtoneSource, str6);
      }
      return j;
    }
    catch (Exception localException1)
    {
      while (true)
      {
        Exception localException2 = localException1;
        int k = Log.e("MusicRingtones", "Failed to create ringtone", localException2);
        int j = 1;
        boolean bool3 = deleteRingtoneRequest(paramLong1);
        if (paramBoolean2)
        {
          String str7 = localStringBuffer.toString();
          MusicRingtoneManager localMusicRingtoneManager3 = this;
          long l5 = paramLong1;
          String str8 = paramString3;
          localMusicRingtoneManager3.notifyRingtoneSet(l5, str8, j, str7);
        }
      }
    }
    finally
    {
      boolean bool4 = deleteRingtoneRequest(paramLong1);
      if (paramBoolean2)
      {
        String str9 = localStringBuffer.toString();
        MusicRingtoneManager localMusicRingtoneManager4 = this;
        long l6 = paramLong1;
        String str10 = paramString3;
        localMusicRingtoneManager4.notifyRingtoneSet(l6, str10, 1, str9);
      }
    }
  }

  private int makeRingtoneFileHelper(long paramLong1, String paramString1, String paramString2, long paramLong2, boolean paramBoolean, StringBuffer paramStringBuffer)
  {
    File localFile1 = new File(paramString1);
    int j;
    if (!localFile1.exists())
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Music file \"");
      String str1 = localFile1.getAbsolutePath();
      String str2 = str1 + "\" does not exist.";
      int i = Log.e("MusicRingtones", str2);
      j = 1;
    }
    while (true)
    {
      return j;
      int k;
      if (localFile1.length() < 16384L)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Music file \"");
        String str3 = localFile1.getAbsolutePath();
        String str4 = str3 + "\" is too small to be used as a ringtone.";
        k = Log.e("MusicRingtones", str4);
        j = 1;
      }
      else
      {
        File localFile2 = getRingtoneFile(paramString2);
        if (localFile2 == null)
        {
          j = 2;
        }
        else
        {
          if (!localFile2.exists());
          for (k = 1; ; k = 0)
          {
            if ((k != 0) || (paramBoolean))
              break label191;
            j = 3;
            break;
          }
          try
          {
            label191: IOUtils.copyFile(localFile1, localFile2, paramLong2);
            String str5 = localFile2.getAbsolutePath();
            StringBuffer localStringBuffer = paramStringBuffer.append(str5);
            j = insertRingtoneInMediaStore(this.mStore.getContext(), localFile2, paramString2);
          }
          catch (IOException localIOException)
          {
            StringBuilder localStringBuilder3 = new StringBuilder().append("Failed to create ringtone file at \"");
            String str6 = localFile2.getAbsolutePath();
            String str7 = str6 + "\"";
            int m = Log.e("MusicRingtones", str7, localIOException);
            j = 2;
          }
        }
      }
    }
  }

  private void notifyRingtoneSet(long paramLong, String paramString1, int paramInt, String paramString2)
  {
    Intent localIntent1 = new Intent("com.google.android.music.RINGTONE_REQUEST_END");
    Intent localIntent2 = localIntent1.putExtra("musicId", paramLong);
    Intent localIntent3 = localIntent1.putExtra("name", paramString1);
    Intent localIntent4 = localIntent1.putExtra("status", paramInt);
    Intent localIntent5 = localIntent1.putExtra("filepath", paramString2);
    this.mStore.getContext().sendBroadcast(localIntent1);
  }

  private static boolean setRingtoneFlagInMediaStore(Context paramContext, long paramLong)
  {
    boolean bool = false;
    try
    {
      Uri localUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, paramLong);
      ContentValues localContentValues = new ContentValues(2);
      localContentValues.put("is_ringtone", "1");
      int i = paramContext.getContentResolver().update(localUri, localContentValues, null, null);
      if (i > 0);
      for (bool = true; ; bool = false)
        return bool;
    }
    catch (UnsupportedOperationException localUnsupportedOperationException)
    {
      while (true)
      {
        String str = "couldn't set ringtone flag for id " + paramLong;
        int j = Log.e("MusicRingtones", str);
      }
    }
  }

  private static void setSystemRingtone(Context paramContext, Uri paramUri)
  {
    ContentResolver localContentResolver = paramContext.getContentResolver();
    String str = paramUri.toString();
    boolean bool = Settings.System.putString(localContentResolver, "ringtone", str);
  }

  private static void writeRingtoneSavedEvent(long paramLong, String paramString1, RingtoneSource paramRingtoneSource, String paramString2)
  {
    if (paramString1 == null);
    for (String str1 = ""; ; str1 = paramString1)
    {
      Object[] arrayOfObject = new Object[3];
      Long localLong = Long.valueOf(paramLong);
      arrayOfObject[0] = localLong;
      arrayOfObject[1] = str1;
      Integer localInteger = Integer.valueOf(paramRingtoneSource.getNumber());
      arrayOfObject[2] = localInteger;
      int i = EventLog.writeEvent(74004, arrayOfObject);
      String str2 = "Event logging MUSIC_TRACK_SAVED_AS_RINGTONE: " + paramString2 + " (localId:" + paramLong + ", serverId: " + str1 + ")";
      int j = Log.d("MusicRingtones", str2);
      return;
    }
  }

  public Pair<Integer, Long> getRingtoneRequestTotals(int paramInt)
  {
    long l1 = getRingtoneFileSize(paramInt);
    int i = 0;
    long l2 = 0L;
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
    try
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "Size";
      localCursor = localSQLiteDatabase.query("RINGTONES JOIN MUSIC ON (MusicId = MUSIC.Id) ", arrayOfString, null, null, null, null, null);
      if (localCursor != null)
      {
        i = localCursor.getCount();
        while (localCursor.moveToNext())
        {
          long l3 = Math.min(localCursor.getLong(0), l1);
          long l4 = l3;
          l2 += l4;
        }
      }
      Store.safeClose(localCursor);
      this.mStore.endRead(localSQLiteDatabase);
      Integer localInteger = Integer.valueOf(i);
      Long localLong = Long.valueOf(l2);
      return new Pair(localInteger, localLong);
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
      this.mStore.endRead(localSQLiteDatabase);
    }
  }

  public long[] getRingtoneRequests(int paramInt)
  {
    SQLiteDatabase localSQLiteDatabase = this.mStore.beginRead();
    try
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "MusicId";
      String str = Integer.toString(paramInt);
      localCursor = localSQLiteDatabase.query("RINGTONES JOIN MUSIC ON (MusicId = MUSIC.Id) ", arrayOfString, null, null, null, null, "RINGTONES.Id ASC", str);
      if (localCursor != null)
      {
        i = localCursor.getCount();
        int k;
        for (int j = 0; localCursor.moveToNext(); j = k)
        {
          k = j + 1;
          long l = localCursor.getLong(0);
          i[j] = l;
        }
      }
      int i = 0;
      return i;
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
      this.mStore.endRead(localSQLiteDatabase);
    }
  }

  public int makeRingtoneFile(long paramLong1, String paramString1, String paramString2, RingtoneSource paramRingtoneSource, String paramString3, long paramLong2, boolean paramBoolean)
  {
    MusicRingtoneManager localMusicRingtoneManager = this;
    long l1 = paramLong1;
    String str1 = paramString1;
    String str2 = paramString2;
    RingtoneSource localRingtoneSource = paramRingtoneSource;
    String str3 = paramString3;
    long l2 = paramLong2;
    boolean bool = paramBoolean;
    return localMusicRingtoneManager.makeRingtoneFile(l1, str1, str2, localRingtoneSource, str3, l2, bool, true);
  }

  // ERROR //
  public int setRingtone(Context paramContext, long paramLong, boolean paramBoolean, StringBuffer paramStringBuffer)
  {
    // Byte code:
    //   0: new 532	com/google/android/music/download/ContentIdentifier
    //   3: astore 6
    //   5: getstatic 538	com/google/android/music/download/ContentIdentifier$Domain:DEFAULT	Lcom/google/android/music/download/ContentIdentifier$Domain;
    //   8: astore 7
    //   10: aload 6
    //   12: astore 8
    //   14: lload_2
    //   15: lstore 9
    //   17: aload 8
    //   19: lload 9
    //   21: aload 7
    //   23: invokespecial 541	com/google/android/music/download/ContentIdentifier:<init>	(JLcom/google/android/music/download/ContentIdentifier$Domain;)V
    //   26: aload_0
    //   27: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   30: astore 11
    //   32: aload 6
    //   34: astore 12
    //   36: aload 11
    //   38: aload 12
    //   40: iconst_0
    //   41: invokevirtual 545	com/google/android/music/store/Store:getPreferredMusicId	(Lcom/google/android/music/download/ContentIdentifier;I)Lcom/google/android/music/download/ContentIdentifier;
    //   44: invokevirtual 548	com/google/android/music/download/ContentIdentifier:getId	()J
    //   47: lstore 13
    //   49: lload 13
    //   51: lstore_2
    //   52: iconst_0
    //   53: istore 15
    //   55: aconst_null
    //   56: astore 16
    //   58: aconst_null
    //   59: astore 17
    //   61: ldc2_w 168
    //   64: lstore 18
    //   66: aconst_null
    //   67: astore 20
    //   69: aconst_null
    //   70: astore 21
    //   72: aload_0
    //   73: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   76: invokevirtual 492	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   79: astore 22
    //   81: getstatic 31	com/google/android/music/store/MusicRingtoneManager:MUSIC_COLUMNS	[Ljava/lang/String;
    //   84: astore 23
    //   86: iconst_1
    //   87: anewarray 17	java/lang/String
    //   90: astore 24
    //   92: lload_2
    //   93: invokestatic 58	java/lang/Long:toString	(J)Ljava/lang/String;
    //   96: astore 25
    //   98: aload 24
    //   100: iconst_0
    //   101: aload 25
    //   103: aastore
    //   104: aload 22
    //   106: ldc_w 550
    //   109: aload 23
    //   111: ldc_w 552
    //   114: aload 24
    //   116: aconst_null
    //   117: aconst_null
    //   118: aconst_null
    //   119: invokevirtual 499	android/database/sqlite/SQLiteDatabase:query	(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   122: astore 26
    //   124: aload 26
    //   126: ifnull +656 -> 782
    //   129: aload 26
    //   131: invokeinterface 157 1 0
    //   136: ifeq +646 -> 782
    //   139: aload 26
    //   141: iconst_0
    //   142: invokeinterface 161 2 0
    //   147: astore 27
    //   149: aload 27
    //   151: astore 28
    //   153: iconst_1
    //   154: istore 29
    //   156: aload 26
    //   158: iload 29
    //   160: invokeinterface 188 2 0
    //   165: ldc2_w 488
    //   168: lcmp
    //   169: ifne +178 -> 347
    //   172: aload 28
    //   174: invokestatic 556	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   177: lstore 18
    //   179: lload 18
    //   181: ldc2_w 557
    //   184: lcmp
    //   185: ifge +43 -> 228
    //   188: new 70	java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial 71	java/lang/StringBuilder:<init>	()V
    //   195: ldc_w 560
    //   198: invokevirtual 77	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   201: astore 30
    //   203: lload 18
    //   205: lstore 31
    //   207: aload 30
    //   209: lload 31
    //   211: invokevirtual 344	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   214: invokevirtual 85	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   217: astore 33
    //   219: ldc 87
    //   221: aload 33
    //   223: invokestatic 112	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   226: istore 34
    //   228: getstatic 175	android/provider/MediaStore$Audio$Media:EXTERNAL_CONTENT_URI	Landroid/net/Uri;
    //   231: astore 35
    //   233: lload 18
    //   235: lstore 36
    //   237: aload 35
    //   239: lload 36
    //   241: invokestatic 324	android/content/ContentUris:withAppendedId	(Landroid/net/Uri;J)Landroid/net/Uri;
    //   244: astore 27
    //   246: aload 27
    //   248: astore 20
    //   250: aload 16
    //   252: astore 38
    //   254: iconst_3
    //   255: istore 39
    //   257: aload 26
    //   259: iload 39
    //   261: invokeinterface 564 2 0
    //   266: istore 15
    //   268: aload 26
    //   270: iconst_4
    //   271: invokeinterface 161 2 0
    //   276: astore 27
    //   278: aload 27
    //   280: astore 40
    //   282: aload 26
    //   284: invokestatic 165	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   287: aload_0
    //   288: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   291: aload 22
    //   293: invokevirtual 509	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   296: aload 20
    //   298: ifnull +256 -> 554
    //   301: lload 18
    //   303: ldc2_w 168
    //   306: lcmp
    //   307: ifeq +247 -> 554
    //   310: aload_0
    //   311: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   314: invokevirtual 413	com/google/android/music/store/Store:getContext	()Landroid/content/Context;
    //   317: astore 41
    //   319: lload 18
    //   321: lstore 42
    //   323: aload 41
    //   325: lload 42
    //   327: invokestatic 318	com/google/android/music/store/MusicRingtoneManager:setRingtoneFlagInMediaStore	(Landroid/content/Context;J)Z
    //   330: ifne +108 -> 438
    //   333: iconst_1
    //   334: istore 44
    //   336: iload 44
    //   338: ireturn
    //   339: astore 45
    //   341: iconst_1
    //   342: istore 44
    //   344: goto -8 -> 336
    //   347: iconst_2
    //   348: istore 29
    //   350: aload 26
    //   352: iload 29
    //   354: invokeinterface 568 2 0
    //   359: ifne +416 -> 775
    //   362: aload 26
    //   364: iconst_2
    //   365: invokeinterface 161 2 0
    //   370: astore 46
    //   372: aload 26
    //   374: iconst_5
    //   375: invokeinterface 564 2 0
    //   380: istore 47
    //   382: aload_1
    //   383: aload 46
    //   385: iload 47
    //   387: invokestatic 574	com/google/android/music/download/cache/CacheUtils:resolveMusicPath	(Landroid/content/Context;Ljava/lang/String;I)Ljava/io/File;
    //   390: astore 48
    //   392: aload 48
    //   394: ifnull +381 -> 775
    //   397: aload 48
    //   399: invokevirtual 182	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   402: astore 27
    //   404: aload 27
    //   406: astore 38
    //   408: goto -154 -> 254
    //   411: astore 49
    //   413: aload 17
    //   415: astore 50
    //   417: aload 16
    //   419: astore 51
    //   421: aload 26
    //   423: invokestatic 165	com/google/android/music/store/Store:safeClose	(Landroid/database/Cursor;)V
    //   426: aload_0
    //   427: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   430: aload 22
    //   432: invokevirtual 509	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   435: aload 49
    //   437: athrow
    //   438: aload_1
    //   439: astore 52
    //   441: aload 20
    //   443: astore 53
    //   445: aload 52
    //   447: aload 53
    //   449: invokestatic 314	com/google/android/music/store/MusicRingtoneManager:setSystemRingtone	(Landroid/content/Context;Landroid/net/Uri;)V
    //   452: getstatic 361	com/google/android/music/store/MusicRingtoneManager$RingtoneSource:SIDE_LOADED_TRACK	Lcom/google/android/music/store/MusicRingtoneManager$RingtoneSource;
    //   455: astore 54
    //   457: lload_2
    //   458: aconst_null
    //   459: aload 54
    //   461: aload 40
    //   463: invokestatic 389	com/google/android/music/store/MusicRingtoneManager:writeRingtoneSavedEvent	(JLjava/lang/String;Lcom/google/android/music/store/MusicRingtoneManager$RingtoneSource;Ljava/lang/String;)V
    //   466: aload_0
    //   467: astore 55
    //   469: aload_1
    //   470: astore 56
    //   472: aload 20
    //   474: astore 57
    //   476: aload 55
    //   478: aload 56
    //   480: aload 57
    //   482: invokespecial 576	com/google/android/music/store/MusicRingtoneManager:getFilepathFromMediaStore	(Landroid/content/Context;Landroid/net/Uri;)Ljava/lang/String;
    //   485: astore 58
    //   487: aload 58
    //   489: ifnonnull +9 -> 498
    //   492: iconst_1
    //   493: istore 44
    //   495: goto -159 -> 336
    //   498: aload 5
    //   500: astore 59
    //   502: aload 58
    //   504: astore 60
    //   506: aload 59
    //   508: aload 60
    //   510: invokevirtual 409	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   513: astore 61
    //   515: iconst_0
    //   516: istore 44
    //   518: iload 44
    //   520: ifne -184 -> 336
    //   523: aload 58
    //   525: ifnull -189 -> 336
    //   528: aload_0
    //   529: astore 62
    //   531: lload_2
    //   532: lstore 63
    //   534: aload 40
    //   536: astore 65
    //   538: aload 62
    //   540: lload 63
    //   542: aload 65
    //   544: iload 44
    //   546: aload 58
    //   548: invokespecial 385	com/google/android/music/store/MusicRingtoneManager:notifyRingtoneSet	(JLjava/lang/String;ILjava/lang/String;)V
    //   551: goto -215 -> 336
    //   554: aload 40
    //   556: invokestatic 402	com/google/android/music/store/MusicRingtoneManager:getRingtoneFile	(Ljava/lang/String;)Ljava/io/File;
    //   559: astore 66
    //   561: aload 66
    //   563: ifnonnull +9 -> 572
    //   566: iconst_2
    //   567: istore 44
    //   569: goto -233 -> 336
    //   572: aload 66
    //   574: invokevirtual 206	java/io/File:exists	()Z
    //   577: ifeq +53 -> 630
    //   580: aload 66
    //   582: invokevirtual 182	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   585: astore 67
    //   587: aload 5
    //   589: aload 67
    //   591: invokevirtual 409	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   594: astore 68
    //   596: aload_0
    //   597: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   600: invokevirtual 413	com/google/android/music/store/Store:getContext	()Landroid/content/Context;
    //   603: astore 69
    //   605: aload 66
    //   607: astore 70
    //   609: aload 69
    //   611: aload 70
    //   613: aload 40
    //   615: invokestatic 415	com/google/android/music/store/MusicRingtoneManager:insertRingtoneInMediaStore	(Landroid/content/Context;Ljava/io/File;Ljava/lang/String;)I
    //   618: istore 44
    //   620: aload 66
    //   622: invokevirtual 182	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   625: astore 58
    //   627: goto -109 -> 518
    //   630: aload 38
    //   632: invokestatic 582	com/google/android/common/base/Strings:isNullOrEmpty	(Ljava/lang/String;)Z
    //   635: ifne +62 -> 697
    //   638: aload 66
    //   640: invokevirtual 182	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   643: astore 71
    //   645: aload 5
    //   647: aload 71
    //   649: invokevirtual 409	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   652: astore 72
    //   654: iload 15
    //   656: invokestatic 584	com/google/android/music/store/MusicRingtoneManager:localCopyTypeToRingtoneSource	(I)Lcom/google/android/music/store/MusicRingtoneManager$RingtoneSource;
    //   659: astore 73
    //   661: aload_0
    //   662: astore 74
    //   664: lload_2
    //   665: lstore 75
    //   667: iload 4
    //   669: istore 77
    //   671: aload 74
    //   673: lload 75
    //   675: aload 28
    //   677: aload 38
    //   679: aload 73
    //   681: aload 40
    //   683: ldc2_w 247
    //   686: iload 77
    //   688: iconst_0
    //   689: invokespecial 526	com/google/android/music/store/MusicRingtoneManager:makeRingtoneFile	(JLjava/lang/String;Ljava/lang/String;Lcom/google/android/music/store/MusicRingtoneManager$RingtoneSource;Ljava/lang/String;JZZ)I
    //   692: istore 44
    //   694: goto -74 -> 620
    //   697: aload_0
    //   698: astore 78
    //   700: lload_2
    //   701: lstore 79
    //   703: aload 78
    //   705: lload 79
    //   707: invokespecial 586	com/google/android/music/store/MusicRingtoneManager:insertRingtoneRequest	(J)V
    //   710: new 120	android/content/Intent
    //   713: astore 81
    //   715: aload 81
    //   717: ldc_w 588
    //   720: invokespecial 125	android/content/Intent:<init>	(Ljava/lang/String;)V
    //   723: aload 81
    //   725: ldc_w 423
    //   728: aload 40
    //   730: invokevirtual 426	android/content/Intent:putExtra	(Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
    //   733: astore 82
    //   735: aload_0
    //   736: getfield 41	com/google/android/music/store/MusicRingtoneManager:mStore	Lcom/google/android/music/store/Store;
    //   739: invokevirtual 413	com/google/android/music/store/Store:getContext	()Landroid/content/Context;
    //   742: astore 83
    //   744: aload 81
    //   746: astore 84
    //   748: aload 83
    //   750: aload 84
    //   752: invokevirtual 437	android/content/Context:sendBroadcast	(Landroid/content/Intent;)V
    //   755: iconst_4
    //   756: istore 44
    //   758: goto -422 -> 336
    //   761: astore 49
    //   763: aload 16
    //   765: astore 85
    //   767: goto -346 -> 421
    //   770: astore 49
    //   772: goto -351 -> 421
    //   775: aload 16
    //   777: astore 38
    //   779: goto -525 -> 254
    //   782: aload 21
    //   784: astore 40
    //   786: aload 17
    //   788: astore 28
    //   790: aload 16
    //   792: astore 38
    //   794: goto -512 -> 282
    //
    // Exception table:
    //   from	to	target	type
    //   0	49	339	java/io/FileNotFoundException
    //   81	149	411	finally
    //   156	246	761	finally
    //   350	404	761	finally
    //   257	278	770	finally
  }

  public int setRingtoneAttempt(long paramLong, String paramString, boolean paramBoolean)
  {
    Context localContext = this.mStore.getContext();
    if (localContext == null);
    StringBuffer localStringBuffer;
    MusicRingtoneManager localMusicRingtoneManager;
    long l;
    boolean bool;
    for (int i = 1; ; i = localMusicRingtoneManager.setRingtone(localContext, l, bool, localStringBuffer))
    {
      return i;
      localStringBuffer = new StringBuffer(100);
      localMusicRingtoneManager = this;
      l = paramLong;
      bool = paramBoolean;
    }
  }

  public static enum RingtoneSource
  {
    private int mNumber;

    static
    {
      KEEPON_TRACK = new RingtoneSource("KEEPON_TRACK", 1, 1);
      CACHED_TRACK = new RingtoneSource("CACHED_TRACK", 2, 2);
      RINGTONE_DOWNLOAD = new RingtoneSource("RINGTONE_DOWNLOAD", 3, 3);
      RingtoneSource[] arrayOfRingtoneSource = new RingtoneSource[4];
      RingtoneSource localRingtoneSource1 = SIDE_LOADED_TRACK;
      arrayOfRingtoneSource[0] = localRingtoneSource1;
      RingtoneSource localRingtoneSource2 = KEEPON_TRACK;
      arrayOfRingtoneSource[1] = localRingtoneSource2;
      RingtoneSource localRingtoneSource3 = CACHED_TRACK;
      arrayOfRingtoneSource[2] = localRingtoneSource3;
      RingtoneSource localRingtoneSource4 = RINGTONE_DOWNLOAD;
      arrayOfRingtoneSource[3] = localRingtoneSource4;
    }

    private RingtoneSource(int paramInt)
    {
      this.mNumber = paramInt;
    }

    public int getNumber()
    {
      return this.mNumber;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.MusicRingtoneManager
 * JD-Core Version:    0.6.2
 */