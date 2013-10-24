package com.google.android.music.store;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class Syncable
{
  protected boolean mNeedsSync;
  protected int mSourceAccount;
  protected String mSourceId;
  protected String mSourceVersion;

  protected boolean createTombstoneIfNeeded(SQLiteDatabase paramSQLiteDatabase, String paramString)
  {
    boolean bool = false;
    if ((this.mSourceAccount != 0) && (this.mSourceId != null))
    {
      ContentValues localContentValues = new ContentValues();
      Integer localInteger = Integer.valueOf(this.mSourceAccount);
      localContentValues.put("SourceAccount", localInteger);
      String str1 = this.mSourceId;
      localContentValues.put("SourceId", str1);
      if (this.mSourceVersion != null)
      {
        String str2 = this.mSourceVersion;
        localContentValues.put("_sync_version", str2);
      }
      if (paramSQLiteDatabase.insertWithOnConflict(paramString, null, localContentValues, 5) != 65535L)
        break label107;
      int i = Log.wtf("Syncable", "Failed to created a tombstone");
    }
    while (true)
    {
      return bool;
      label107: bool = true;
    }
  }

  public final int getSourceAccount()
  {
    return this.mSourceAccount;
  }

  public final String getSourceId()
  {
    return this.mSourceId;
  }

  public final String getSourceVersion()
  {
    return this.mSourceVersion;
  }

  public void reset()
  {
    this.mSourceAccount = 0;
    this.mSourceVersion = null;
    this.mSourceId = null;
    this.mNeedsSync = false;
  }

  public final void setNeedsSync(boolean paramBoolean)
  {
    this.mNeedsSync = paramBoolean;
  }

  public final void setSourceAccount(int paramInt)
  {
    this.mSourceAccount = paramInt;
  }

  public final void setSourceId(String paramString)
  {
    this.mSourceId = paramString;
  }

  public final void setSourceVersion(String paramString)
  {
    this.mSourceVersion = paramString;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.Syncable
 * JD-Core Version:    0.6.2
 */