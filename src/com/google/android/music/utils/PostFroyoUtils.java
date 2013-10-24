package com.google.android.music.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.os.Environment;
import android.util.Log;
import java.lang.reflect.Field;

public class PostFroyoUtils
{
  public static class BitmapFactoryOptionsCompat
  {
    public static void setInBitmap(BitmapFactory.Options paramOptions, Bitmap paramBitmap)
    {
      if (Build.VERSION.SDK_INT < 11)
        return;
      try
      {
        BitmapFactory.Options.class.getField("inBitmap").set(paramOptions, paramBitmap);
        return;
      }
      catch (Exception localException)
      {
        int i = Log.e("PostFroyoUtils", "Exception while setting BitmapFactory.Options.inBitmap via reflection", localException);
      }
    }
  }

  public static class SQLiteDatabaseComp
  {
    public static Cursor query(SQLiteQueryBuilder paramSQLiteQueryBuilder, SQLiteDatabase paramSQLiteDatabase, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2, String paramString3, String paramString4, String paramString5, PostFroyoUtils.CancellationSignalComp paramCancellationSignalComp)
    {
      CancellationSignal localCancellationSignal;
      SQLiteQueryBuilder localSQLiteQueryBuilder;
      SQLiteDatabase localSQLiteDatabase;
      String[] arrayOfString1;
      String str1;
      String[] arrayOfString2;
      String str2;
      String str3;
      String str4;
      String str5;
      if ((paramCancellationSignalComp != null) && (paramCancellationSignalComp.hasSignal()))
      {
        localCancellationSignal = paramCancellationSignalComp.getCancellationSignal();
        localSQLiteQueryBuilder = paramSQLiteQueryBuilder;
        localSQLiteDatabase = paramSQLiteDatabase;
        arrayOfString1 = paramArrayOfString1;
        str1 = paramString1;
        arrayOfString2 = paramArrayOfString2;
        str2 = paramString2;
        str3 = paramString3;
        str4 = paramString4;
        str5 = paramString5;
      }
      for (Cursor localCursor = localSQLiteQueryBuilder.query(localSQLiteDatabase, arrayOfString1, str1, arrayOfString2, str2, str3, str4, str5, localCancellationSignal); ; localCursor = paramSQLiteQueryBuilder.query(paramSQLiteDatabase, paramArrayOfString1, paramString1, paramArrayOfString2, paramString2, paramString3, paramString4, paramString5))
        return localCursor;
    }

    public static Cursor rawQuery(SQLiteDatabase paramSQLiteDatabase, String paramString, String[] paramArrayOfString, PostFroyoUtils.CancellationSignalComp paramCancellationSignalComp)
    {
      CancellationSignal localCancellationSignal;
      if (paramCancellationSignalComp.hasSignal())
        localCancellationSignal = paramCancellationSignalComp.getCancellationSignal();
      for (Cursor localCursor = paramSQLiteDatabase.rawQuery(paramString, paramArrayOfString, localCancellationSignal); ; localCursor = paramSQLiteDatabase.rawQuery(paramString, paramArrayOfString))
        return localCursor;
    }
  }

  public static class CancellationSignalComp
  {
    private final CancellationSignal mSignal;

    public CancellationSignalComp()
    {
      this.mSignal = null;
    }

    public CancellationSignalComp(CancellationSignal paramCancellationSignal)
    {
      this.mSignal = paramCancellationSignal;
    }

    public CancellationSignal getCancellationSignal()
    {
      return this.mSignal;
    }

    public boolean hasSignal()
    {
      boolean bool = false;
      if ((Build.VERSION.SDK_INT >= 16) && (this.mSignal != null))
        bool = true;
      return bool;
    }
  }

  public static class EnvironmentCompat
  {
    public static boolean isExternalStorageEmulated()
    {
      if (Build.VERSION.SDK_INT >= 11);
      for (boolean bool = Environment.isExternalStorageEmulated(); ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.PostFroyoUtils
 * JD-Core Version:    0.6.2
 */