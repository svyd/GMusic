package com.google.android.music.preferences;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import com.google.android.music.SharedPreferencesCompat;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MusicPreferenceService extends IPreferenceService.Stub
{
  private final List<IPreferenceChangeListener> mChangeListeners;
  private final Context mContext;
  private final SharedPreferences mSharedPreferences;

  public MusicPreferenceService(Context paramContext)
  {
    LinkedList localLinkedList = Lists.newLinkedList();
    this.mChangeListeners = localLinkedList;
    this.mContext = paramContext;
    int i = 0;
    if (MusicPreferences.isHoneycombOrGreater())
      i = 0x0 | 0x4;
    SharedPreferences localSharedPreferences = this.mContext.getSharedPreferences("MusicPreferences", i);
    this.mSharedPreferences = localSharedPreferences;
    if (!this.mSharedPreferences.getBoolean("StreamOnlyHighQuality", false))
      return;
    Integer localInteger = Integer.valueOf(2);
    setPreference("StreamQuality", localInteger);
    Boolean localBoolean = Boolean.valueOf(false);
    setPreference("StreamOnlyHighQuality", localBoolean);
  }

  private void notifyBooleanPreferenceChanged(String paramString, boolean paramBoolean)
  {
    synchronized (this.mChangeListeners)
    {
      Iterator localIterator = this.mChangeListeners.iterator();
      while (localIterator.hasNext())
      {
        IPreferenceChangeListener localIPreferenceChangeListener = (IPreferenceChangeListener)localIterator.next();
        try
        {
          localIPreferenceChangeListener.onBooleanChanged(paramString, paramBoolean);
        }
        catch (RemoteException localRemoteException)
        {
        }
      }
      return;
    }
  }

  private void notifyIntPreferenceChanged(String paramString, int paramInt)
  {
    synchronized (this.mChangeListeners)
    {
      Iterator localIterator = this.mChangeListeners.iterator();
      while (localIterator.hasNext())
      {
        IPreferenceChangeListener localIPreferenceChangeListener = (IPreferenceChangeListener)localIterator.next();
        try
        {
          localIPreferenceChangeListener.onIntChanged(paramString, paramInt);
        }
        catch (RemoteException localRemoteException)
        {
        }
      }
      return;
    }
  }

  private void notifyLongPreferenceChanged(String paramString, long paramLong)
  {
    synchronized (this.mChangeListeners)
    {
      Iterator localIterator = this.mChangeListeners.iterator();
      while (localIterator.hasNext())
      {
        IPreferenceChangeListener localIPreferenceChangeListener = (IPreferenceChangeListener)localIterator.next();
        try
        {
          localIPreferenceChangeListener.onLongChanged(paramString, paramLong);
        }
        catch (RemoteException localRemoteException)
        {
        }
      }
      return;
    }
  }

  private void notifyPreferenceRemoved(String paramString)
  {
    synchronized (this.mChangeListeners)
    {
      Iterator localIterator = this.mChangeListeners.iterator();
      while (localIterator.hasNext())
      {
        IPreferenceChangeListener localIPreferenceChangeListener = (IPreferenceChangeListener)localIterator.next();
        try
        {
          localIPreferenceChangeListener.onPreferenceRemoved(paramString);
        }
        catch (RemoteException localRemoteException)
        {
        }
      }
      return;
    }
  }

  private void notifyStringPreferenceChanged(String paramString1, String paramString2)
  {
    synchronized (this.mChangeListeners)
    {
      Iterator localIterator = this.mChangeListeners.iterator();
      while (localIterator.hasNext())
      {
        IPreferenceChangeListener localIPreferenceChangeListener = (IPreferenceChangeListener)localIterator.next();
        try
        {
          localIPreferenceChangeListener.onStringChanged(paramString1, paramString2);
        }
        catch (RemoteException localRemoteException)
        {
        }
      }
      return;
    }
  }

  private void setPreference(String paramString, Object paramObject)
  {
    SharedPreferences.Editor localEditor1 = this.mSharedPreferences.edit();
    if (paramObject == null)
      SharedPreferences.Editor localEditor2 = localEditor1.remove(paramString);
    while (true)
    {
      SharedPreferencesCompat.apply(localEditor1);
      return;
      if ((paramObject instanceof Integer))
      {
        int i = ((Integer)paramObject).intValue();
        SharedPreferences.Editor localEditor3 = localEditor1.putInt(paramString, i);
      }
      else if ((paramObject instanceof Long))
      {
        long l = ((Long)paramObject).longValue();
        SharedPreferences.Editor localEditor4 = localEditor1.putLong(paramString, l);
      }
      else if ((paramObject instanceof String))
      {
        String str1 = (String)paramObject;
        SharedPreferences.Editor localEditor5 = localEditor1.putString(paramString, str1);
      }
      else
      {
        if (!(paramObject instanceof Boolean))
          break;
        boolean bool = ((Boolean)paramObject).booleanValue();
        SharedPreferences.Editor localEditor6 = localEditor1.putBoolean(paramString, bool);
      }
    }
    String str2 = "Unknown value type: " + paramObject;
    throw new IllegalArgumentException(str2);
  }

  public Map getCurrentPreferences()
  {
    return this.mSharedPreferences.getAll();
  }

  public void registerPreferenceChangeListener(IPreferenceChangeListener paramIPreferenceChangeListener)
  {
    synchronized (this.mChangeListeners)
    {
      boolean bool = this.mChangeListeners.add(paramIPreferenceChangeListener);
      return;
    }
  }

  public void remove(String paramString)
  {
    setPreference(paramString, null);
    notifyPreferenceRemoved(paramString);
  }

  public void setBooleanPreference(String paramString, boolean paramBoolean)
  {
    Boolean localBoolean = Boolean.valueOf(paramBoolean);
    setPreference(paramString, localBoolean);
    notifyBooleanPreferenceChanged(paramString, paramBoolean);
  }

  public void setIntPreference(String paramString, int paramInt)
  {
    Integer localInteger = Integer.valueOf(paramInt);
    setPreference(paramString, localInteger);
    notifyIntPreferenceChanged(paramString, paramInt);
  }

  public void setLongPreference(String paramString, long paramLong)
  {
    Long localLong = Long.valueOf(paramLong);
    setPreference(paramString, localLong);
    notifyLongPreferenceChanged(paramString, paramLong);
  }

  public void setStringPreference(String paramString1, String paramString2)
  {
    setPreference(paramString1, paramString2);
    notifyStringPreferenceChanged(paramString1, paramString2);
  }

  public void unregisterPreferenceChangeListener(IPreferenceChangeListener paramIPreferenceChangeListener)
    throws RemoteException
  {
    synchronized (this.mChangeListeners)
    {
      boolean bool = this.mChangeListeners.remove(paramIPreferenceChangeListener);
      return;
    }
  }

  public static class MusicPreferenceServiceBinder extends Service
  {
    private Binder mMusicPreferenceService = null;

    /** @deprecated */
    public IBinder onBind(Intent paramIntent)
    {
      try
      {
        Binder localBinder = this.mMusicPreferenceService;
        return localBinder;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    public void onCreate()
    {
      super.onCreate();
      if (this.mMusicPreferenceService != null)
        return;
      MusicPreferenceService localMusicPreferenceService = new MusicPreferenceService(this);
      this.mMusicPreferenceService = localMusicPreferenceService;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.preferences.MusicPreferenceService
 * JD-Core Version:    0.6.2
 */