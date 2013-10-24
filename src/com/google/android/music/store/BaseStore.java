package com.google.android.music.store;

import android.database.sqlite.SQLiteDatabase;

public abstract class BaseStore
{
  private void lock()
  {
  }

  private void unlock()
  {
  }

  public SQLiteDatabase beginRead()
  {
    lock();
    try
    {
      SQLiteDatabase localSQLiteDatabase1 = getDatabase(true);
      SQLiteDatabase localSQLiteDatabase2 = localSQLiteDatabase1;
      if (localSQLiteDatabase2 == null)
        unlock();
      return localSQLiteDatabase2;
    }
    finally
    {
      if (0 == 0)
        unlock();
    }
  }

  public SQLiteDatabase beginWriteTxn()
  {
    lock();
    try
    {
      SQLiteDatabase localSQLiteDatabase = getDatabase(false);
      localSQLiteDatabase.beginTransaction();
      if (1 == 0)
        unlock();
      return localSQLiteDatabase;
    }
    finally
    {
      if (0 == 0)
        unlock();
    }
  }

  public void endRead(SQLiteDatabase paramSQLiteDatabase)
  {
    unlock();
  }

  public void endWriteTxn(SQLiteDatabase paramSQLiteDatabase, boolean paramBoolean)
  {
    if (paramBoolean);
    try
    {
      paramSQLiteDatabase.setTransactionSuccessful();
      paramSQLiteDatabase.endTransaction();
      return;
    }
    finally
    {
      unlock();
    }
  }

  protected abstract SQLiteDatabase getDatabase(boolean paramBoolean);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.BaseStore
 * JD-Core Version:    0.6.2
 */