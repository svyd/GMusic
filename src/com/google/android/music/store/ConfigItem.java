package com.google.android.music.store;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

public class ConfigItem
{
  private static final String[] PROJECTION = arrayOfString;
  private long mId;
  private String mName;
  private int mType;
  private String mValue;

  static
  {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "CONFIG.id";
    arrayOfString[1] = "CONFIG.Name";
    arrayOfString[2] = "CONFIG.Value";
    arrayOfString[3] = "CONFIG.Type";
  }

  public static SQLiteStatement compileInsertStatement(SQLiteDatabase paramSQLiteDatabase)
  {
    return paramSQLiteDatabase.compileStatement("insert into CONFIG ( Name, Value, Type) values (?,?,?)");
  }

  public static int deleteAllServerSettings(SQLiteDatabase paramSQLiteDatabase)
  {
    String[] arrayOfString = new String[1];
    String str = Integer.toString(1);
    arrayOfString[0] = str;
    return paramSQLiteDatabase.delete("CONFIG", "Type=?", arrayOfString);
  }

  private void prepareInsertOrFullUpdate(SQLiteStatement paramSQLiteStatement)
  {
    paramSQLiteStatement.clearBindings();
    if (TextUtils.isEmpty(this.mName))
      throw new InvalidDataException("Name must be set before storing");
    String str1 = this.mName;
    paramSQLiteStatement.bindString(1, str1);
    if (this.mValue == null)
      paramSQLiteStatement.bindNull(2);
    while (true)
    {
      long l = this.mType;
      paramSQLiteStatement.bindLong(3, l);
      return;
      String str2 = this.mValue;
      paramSQLiteStatement.bindString(2, str2);
    }
  }

  public long insert(SQLiteStatement paramSQLiteStatement)
  {
    if (this.mId != 0L)
      throw new InvalidDataException("The local id of a config entry must not be set for an insert.");
    prepareInsertOrFullUpdate(paramSQLiteStatement);
    long l = paramSQLiteStatement.executeInsert();
    if (l == 65535L)
      throw new RuntimeException("Failed to insert into config");
    this.mId = l;
    return this.mId;
  }

  public void reset()
  {
    this.mId = 0L;
    this.mName = null;
    this.mValue = null;
    this.mType = 0;
  }

  public final void setName(String paramString)
  {
    this.mName = paramString;
  }

  public void setType(int paramInt)
  {
    this.mType = paramInt;
  }

  public final void setValue(String paramString)
  {
    this.mValue = paramString;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
    StringBuilder localStringBuilder3 = localStringBuilder1.append("mId=");
    long l = this.mId;
    StringBuilder localStringBuilder4 = localStringBuilder3.append(l).append(",");
    StringBuilder localStringBuilder5 = localStringBuilder1.append("mName=");
    String str1 = this.mName;
    StringBuilder localStringBuilder6 = localStringBuilder5.append(str1).append(",");
    StringBuilder localStringBuilder7 = localStringBuilder1.append("mValue=");
    String str2 = this.mValue;
    StringBuilder localStringBuilder8 = localStringBuilder7.append(str2).append(",");
    StringBuilder localStringBuilder9 = localStringBuilder1.append("mType=");
    int i = this.mType;
    StringBuilder localStringBuilder10 = localStringBuilder9.append(i).append(",");
    StringBuilder localStringBuilder11 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.ConfigItem
 * JD-Core Version:    0.6.2
 */