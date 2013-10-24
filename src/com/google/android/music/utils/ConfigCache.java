package com.google.android.music.utils;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import com.google.android.music.store.ConfigContent;
import com.google.android.music.ui.UIStateManager;
import java.util.HashMap;

public class ConfigCache
{
  private final HashMap<String, String> mCache;
  private long mCacheVersion;
  private volatile ContentResolver mContentResolver;
  private final Object mLock;
  private final ConfigContentObserver mObserver;
  private final int mType;

  ConfigCache(int paramInt, Handler paramHandler)
  {
    HashMap localHashMap = new HashMap();
    this.mCache = localHashMap;
    this.mCacheVersion = 0L;
    Object localObject = new Object();
    this.mLock = localObject;
    this.mType = paramInt;
    ConfigContentObserver localConfigContentObserver = new ConfigContentObserver(paramHandler);
    this.mObserver = localConfigContentObserver;
  }

  public String get(String paramString)
  {
    while (true)
    {
      long l1;
      Cursor localCursor;
      synchronized (this.mLock)
      {
        l1 = this.mCacheVersion;
        if (this.mCache.containsKey(paramString))
        {
          str1 = (String)this.mCache.get(paramString);
          return str1;
        }
        Uri localUri = ConfigContent.getConfigSettingUri(this.mType, paramString);
        ContentResolver localContentResolver = this.mContentResolver;
        String[] arrayOfString1 = new String[1];
        arrayOfString1[0] = "Value";
        String[] arrayOfString2 = null;
        String str2 = null;
        localCursor = localContentResolver.query(localUri, arrayOfString1, null, arrayOfString2, str2);
        if (localCursor != null)
          break label138;
      }
      synchronized (this.mLock)
      {
        Object localObject2 = this.mCache.put(paramString, null);
        str1 = null;
        continue;
        localObject3 = finally;
        throw localObject3;
      }
      try
      {
        label138: if ((!localCursor.moveToFirst()) || (localCursor.isNull(0)))
          synchronized (this.mLock)
          {
            Object localObject8 = this.mCache.put(paramString, null);
            localCursor.close();
            str1 = null;
          }
      }
      finally
      {
        localCursor.close();
      }
      int i = null;
      String str1 = localCursor.getString(i);
      synchronized (this.mLock)
      {
        long l2 = this.mCacheVersion;
        if (l1 == l2)
          Object localObject10 = this.mCache.put(paramString, str1);
        localCursor.close();
      }
    }
  }

  public void init(ContentResolver paramContentResolver)
  {
    if (paramContentResolver == null)
      throw new IllegalArgumentException("Missing ContentResolver");
    synchronized (this.mLock)
    {
      if (this.mContentResolver == null)
      {
        this.mContentResolver = paramContentResolver;
        switch (this.mType)
        {
        default:
          StringBuilder localStringBuilder = new StringBuilder().append("Unhandled type: ");
          int i = this.mType;
          String str = i;
          throw new IllegalStateException(str);
        case 1:
        case 2:
        }
      }
    }
    for (Uri localUri = ConfigContent.SERVER_SETTINGS_URI; ; localUri = ConfigContent.APP_SETTINGS_URI)
    {
      ContentResolver localContentResolver = this.mContentResolver;
      ConfigContentObserver localConfigContentObserver = this.mObserver;
      localContentResolver.registerContentObserver(localUri, true, localConfigContentObserver);
      return;
    }
  }

  private class ConfigContentObserver extends ContentObserver
  {
    public ConfigContentObserver(Handler arg2)
    {
      super();
    }

    public void onChange(boolean paramBoolean)
    {
      synchronized (ConfigCache.this.mLock)
      {
        ConfigCache.this.mCache.clear();
        long l = ConfigCache.access$208(ConfigCache.this);
        if (!UIStateManager.isInitialized())
          return;
        UIStateManager localUIStateManager = UIStateManager.getInstance();
        int i = ConfigCache.this.mType;
        localUIStateManager.onConfigChange(i);
        return;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.ConfigCache
 * JD-Core Version:    0.6.2
 */