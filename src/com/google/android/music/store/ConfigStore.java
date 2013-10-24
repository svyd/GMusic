package com.google.android.music.store;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConfigStore extends BaseStore
{
  static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.STORE);
  private static ConfigStore sInstance = new ConfigStore();
  private Context mContext;
  private DatabaseHelper mDbOpener;
  private final AtomicBoolean mDowngraded;
  private MusicEventLogger mTracker;

  public ConfigStore()
  {
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(false);
    this.mDowngraded = localAtomicBoolean;
  }

  private SQLiteDatabase downgrade(String paramString, boolean paramBoolean)
  {
    synchronized (this.mDowngraded)
    {
      if (!this.mDowngraded.get())
      {
        this.mDbOpener.close();
        if (new File(paramString).delete())
        {
          String str1 = "Sucessfully deleted old database file at " + paramString;
          Log.i("ConfigStore", str1);
          this.mDowngraded.set(true);
        }
      }
      else
      {
        SQLiteDatabase localSQLiteDatabase = this.mDbOpener.getWritableDatabase();
        return localSQLiteDatabase;
      }
      String str2 = "Failed to delete old database file at " + paramString;
      Log.e("ConfigStore", str2);
    }
  }

  public static ConfigStore getInstance(Context paramContext)
  {
    sInstance.init(paramContext);
    return sInstance;
  }

  /** @deprecated */
  private void init(Context paramContext)
  {
    try
    {
      if (this.mDbOpener == null)
      {
        this.mContext = paramContext;
        Log.i("ConfigStore", "Config Database version: 1");
        DatabaseHelper localDatabaseHelper = new DatabaseHelper();
        this.mDbOpener = localDatabaseHelper;
        MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramContext);
        this.mTracker = localMusicEventLogger;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  protected SQLiteDatabase getDatabase(boolean paramBoolean)
  {
    try
    {
      SQLiteDatabase localSQLiteDatabase1 = this.mDbOpener.getWritableDatabase();
      localSQLiteDatabase2 = localSQLiteDatabase1;
      return localSQLiteDatabase2;
    }
    catch (DowngradeException localDowngradeException)
    {
      while (true)
      {
        String str1 = localDowngradeException.getFilepath();
        localSQLiteDatabase2 = downgrade(str1, paramBoolean);
      }
    }
    catch (SQLiteException localSQLiteException)
    {
      while (true)
      {
        MusicEventLogger localMusicEventLogger = this.mTracker;
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = "failureReason";
        arrayOfObject[1] = "getDatabaseError";
        arrayOfObject[2] = "failureMsg";
        String str2 = localSQLiteException.getMessage();
        arrayOfObject[3] = str2;
        localMusicEventLogger.trackEvent("wtf", arrayOfObject);
        if (this.mDbOpener.mDBPath == null)
          break;
        Log.e("ConfigStore", "Error trying to open the DB", localSQLiteException);
        String str3 = this.mDbOpener.mDBPath;
        SQLiteDatabase localSQLiteDatabase2 = downgrade(str3, paramBoolean);
      }
      throw localSQLiteException;
    }
  }

  private class DatabaseHelper extends SQLiteOpenHelper
  {
    private String mDBPath = null;

    DatabaseHelper()
    {
      super("config.db", null, 1);
    }

    private void createConfigTable(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("CREATE TABLE CONFIG(id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT NOT NULL, Value TEXT, Type INTEGER NOT NULL DEFAULT 0, UNIQUE(Name, Type) ON CONFLICT REPLACE);");
      paramSQLiteDatabase.execSQL("CREATE INDEX CONFIG_NAME_TYPE_INDEX on CONFIG(Name,Type)");
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      if (ConfigStore.LOGV)
        Log.d("ConfigStore", "Config Database created");
      String str = paramSQLiteDatabase.getPath();
      this.mDBPath = str;
      onUpgrade(paramSQLiteDatabase, -1, 1);
    }

    public void onDowngrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      String str = paramSQLiteDatabase.getPath();
      throw new DowngradeException(str);
    }

    public void onOpen(SQLiteDatabase paramSQLiteDatabase)
    {
      Store.configureDatabaseConnection(paramSQLiteDatabase);
      super.onOpen(paramSQLiteDatabase);
      if (!ConfigStore.LOGV)
        return;
      Log.d("ConfigStore", "Database opened");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      String str = paramSQLiteDatabase.getPath();
      this.mDBPath = str;
      if (paramInt1 >= 1)
        return;
      createConfigTable(paramSQLiteDatabase);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.ConfigStore
 * JD-Core Version:    0.6.2
 */