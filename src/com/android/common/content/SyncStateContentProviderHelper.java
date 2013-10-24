package com.android.common.content;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SyncStateContentProviderHelper
{
  private static final String[] ACCOUNT_PROJECTION = arrayOfString;
  private static long DB_VERSION = 1L;

  static
  {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = "account_name";
    arrayOfString[1] = "account_type";
  }

  public void createDatabase(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS _sync_state");
    paramSQLiteDatabase.execSQL("CREATE TABLE _sync_state (_id INTEGER PRIMARY KEY,account_name TEXT NOT NULL,account_type TEXT NOT NULL,data TEXT,UNIQUE(account_name, account_type));");
    paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS _sync_state_metadata");
    paramSQLiteDatabase.execSQL("CREATE TABLE _sync_state_metadata (version INTEGER);");
    ContentValues localContentValues = new ContentValues();
    Long localLong = Long.valueOf(DB_VERSION);
    localContentValues.put("version", localLong);
    long l = paramSQLiteDatabase.insert("_sync_state_metadata", "version", localContentValues);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.android.common.content.SyncStateContentProviderHelper
 * JD-Core Version:    0.6.2
 */