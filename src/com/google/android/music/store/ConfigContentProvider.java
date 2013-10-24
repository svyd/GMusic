package com.google.android.music.store;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;
import com.google.android.music.ui.cardlib.utils.Utils;
import com.google.android.music.utils.ConfigUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.PostFroyoUtils.CancellationSignalComp;
import com.google.android.music.utils.PostFroyoUtils.SQLiteDatabaseComp;
import java.util.HashMap;
import java.util.List;

public class ConfigContentProvider extends ContentProvider
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.CONTENT_PROVIDER);
  private static final HashMap<String, String> sConfigProjectionMap;
  private static final UriMatcher sUriMatcher = new UriMatcher(-1);

  static
  {
    sUriMatcher.addURI("com.google.android.music.ConfigContent", "server", 1);
    sUriMatcher.addURI("com.google.android.music.ConfigContent", "server/*", 2);
    sUriMatcher.addURI("com.google.android.music.ConfigContent", "app", 3);
    sUriMatcher.addURI("com.google.android.music.ConfigContent", "app/*", 4);
    sConfigProjectionMap = new HashMap();
    MusicContentProvider.addMapping(sConfigProjectionMap, "_id", "id");
    MusicContentProvider.addExistsAndCountMapping(sConfigProjectionMap, "count(*)");
    MusicContentProvider.addMapping(sConfigProjectionMap, "Name", "Name");
    MusicContentProvider.addMapping(sConfigProjectionMap, "Value", "Value");
    MusicContentProvider.addMapping(sConfigProjectionMap, "Type", "Type");
  }

  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString)
  {
    switch (sUriMatcher.match(paramUri))
    {
    default:
      String str = "Unmatched uri: " + paramUri;
      throw new IllegalStateException(str);
    case 1:
      ConfigStore localConfigStore = ConfigStore.getInstance(getContext());
      SQLiteDatabase localSQLiteDatabase = localConfigStore.beginWriteTxn();
      boolean bool = false;
      SQLiteStatement localSQLiteStatement = ConfigItem.compileInsertStatement(localSQLiteDatabase);
      try
      {
        int i = ConfigItem.deleteAllServerSettings(localSQLiteDatabase);
        int j = i;
        localConfigStore.endWriteTxn(localSQLiteDatabase, true);
        Store.safeClose(localSQLiteStatement);
        return j;
      }
      finally
      {
        localConfigStore.endWriteTxn(localSQLiteDatabase, bool);
        Store.safeClose(localSQLiteStatement);
      }
    case 2:
      throw new UnsupportedOperationException();
    case 3:
      throw new UnsupportedOperationException();
    case 4:
    }
    throw new UnsupportedOperationException();
  }

  public String getType(Uri paramUri)
  {
    String str2;
    switch (sUriMatcher.match(paramUri))
    {
    default:
      String str1 = "Unmatched uri: " + paramUri;
      throw new IllegalStateException(str1);
    case 1:
      str2 = "vnd.android.cursor.dir/vnd.google.music.config";
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      return str2;
      str2 = "vnd.android.cursor.item/vnd.google.music.config";
      continue;
      str2 = "vnd.android.cursor.dir/vnd.google.music.config";
      continue;
      str2 = "vnd.android.cursor.item/vnd.google.music.config";
    }
  }

  public Uri insert(Uri paramUri, ContentValues paramContentValues)
  {
    throw new UnsupportedOperationException();
  }

  public boolean onCreate()
  {
    ConfigUtils.init(getContext().getContentResolver());
    return true;
  }

  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2)
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[5];
      arrayOfObject[0] = paramUri;
      String str1 = DebugUtils.arrayToString(paramArrayOfString1);
      arrayOfObject[1] = str1;
      arrayOfObject[2] = paramString1;
      String str2 = DebugUtils.arrayToString(paramArrayOfString2);
      arrayOfObject[3] = str2;
      arrayOfObject[4] = paramString2;
      String str3 = String.format("query: uri: %s projectionIn=%s selection=%s selectionArgs=%s sortOrder=%s", arrayOfObject);
      int i = Log.d("ConfigContentProvider", str3);
    }
    HashMap localHashMap = sConfigProjectionMap;
    ConfigStore localConfigStore = ConfigStore.getInstance(getContext());
    SQLiteQueryBuilder localSQLiteQueryBuilder = new SQLiteQueryBuilder();
    UriMatcher localUriMatcher = sUriMatcher;
    Uri localUri1 = paramUri;
    String str5;
    String[] arrayOfString1;
    switch (localUriMatcher.match(localUri1))
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unmatched uri:");
      Uri localUri2 = paramUri;
      String str4 = localUri2;
      throw new IllegalStateException(str4);
    case 1:
      localSQLiteQueryBuilder.setTables("CONFIG");
      str5 = "Type=?";
      arrayOfString1 = new String[1];
      String str6 = String.valueOf(1);
      arrayOfString1[0] = str6;
    case 2:
    case 3:
    case 4:
    }
    while (true)
    {
      SQLiteDatabase localSQLiteDatabase = localConfigStore.beginRead();
      localSQLiteQueryBuilder.setProjectionMap(localHashMap);
      String str7 = null;
      String str8 = null;
      String str9 = null;
      PostFroyoUtils.CancellationSignalComp localCancellationSignalComp = null;
      String[] arrayOfString2 = paramArrayOfString1;
      String str10 = paramString2;
      try
      {
        Cursor localCursor = PostFroyoUtils.SQLiteDatabaseComp.query(localSQLiteQueryBuilder, localSQLiteDatabase, arrayOfString2, str5, arrayOfString1, str7, str8, str10, str9, localCancellationSignalComp);
        if (localCursor != null)
        {
          ContentResolver localContentResolver = getContext().getContentResolver();
          Uri localUri3 = paramUri;
          localCursor.setNotificationUri(localContentResolver, localUri3);
        }
        return localCursor;
        localSQLiteQueryBuilder.setTables("CONFIG");
        String str11 = Utils.urlDecode((String)paramUri.getPathSegments().get(1));
        str5 = "Type=? AND Name=?";
        arrayOfString1 = new String[2];
        String str12 = String.valueOf(1);
        arrayOfString1[0] = str12;
        arrayOfString1[1] = str11;
        continue;
        localSQLiteQueryBuilder.setTables("CONFIG");
        str5 = "Type=?";
        arrayOfString1 = new String[1];
        String str13 = String.valueOf(2);
        arrayOfString1[0] = str13;
        continue;
        String str14 = Utils.urlDecode((String)paramUri.getPathSegments().get(1));
        localSQLiteQueryBuilder.setTables("CONFIG");
        str5 = "Type=? AND Name=?";
        arrayOfString1 = new String[2];
        String str15 = String.valueOf(2);
        arrayOfString1[0] = str15;
        arrayOfString1[1] = str14;
      }
      finally
      {
        localConfigStore.endRead(localSQLiteDatabase);
      }
    }
  }

  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString)
  {
    throw new UnsupportedOperationException();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.ConfigContentProvider
 * JD-Core Version:    0.6.2
 */