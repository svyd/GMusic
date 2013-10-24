package com.google.android.music.preferences;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.Map;

public abstract interface IPreferenceService extends IInterface
{
  public abstract Map getCurrentPreferences()
    throws RemoteException;

  public abstract void registerPreferenceChangeListener(IPreferenceChangeListener paramIPreferenceChangeListener)
    throws RemoteException;

  public abstract void remove(String paramString)
    throws RemoteException;

  public abstract void setBooleanPreference(String paramString, boolean paramBoolean)
    throws RemoteException;

  public abstract void setIntPreference(String paramString, int paramInt)
    throws RemoteException;

  public abstract void setLongPreference(String paramString, long paramLong)
    throws RemoteException;

  public abstract void setStringPreference(String paramString1, String paramString2)
    throws RemoteException;

  public abstract void unregisterPreferenceChangeListener(IPreferenceChangeListener paramIPreferenceChangeListener)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IPreferenceService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.preferences.IPreferenceService");
    }

    public static IPreferenceService asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.preferences.IPreferenceService");
        if ((localIInterface != null) && ((localIInterface instanceof IPreferenceService)))
          localObject = (IPreferenceService)localIInterface;
        else
          localObject = new Proxy(paramIBinder);
      }
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      boolean bool1 = true;
      switch (paramInt1)
      {
      default:
        bool1 = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      }
      while (true)
      {
        return bool1;
        paramParcel2.writeString("com.google.android.music.preferences.IPreferenceService");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        Map localMap = getCurrentPreferences();
        paramParcel2.writeNoException();
        paramParcel2.writeMap(localMap);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        String str1 = paramParcel1.readString();
        remove(str1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        String str2 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0);
        for (boolean bool2 = true; ; bool2 = false)
        {
          setBooleanPreference(str2, bool2);
          paramParcel2.writeNoException();
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        String str3 = paramParcel1.readString();
        String str4 = paramParcel1.readString();
        setStringPreference(str3, str4);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        String str5 = paramParcel1.readString();
        long l = paramParcel1.readLong();
        setLongPreference(str5, l);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        String str6 = paramParcel1.readString();
        int i = paramParcel1.readInt();
        setIntPreference(str6, i);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        IPreferenceChangeListener localIPreferenceChangeListener1 = IPreferenceChangeListener.Stub.asInterface(paramParcel1.readStrongBinder());
        registerPreferenceChangeListener(localIPreferenceChangeListener1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceService");
        IPreferenceChangeListener localIPreferenceChangeListener2 = IPreferenceChangeListener.Stub.asInterface(paramParcel1.readStrongBinder());
        unregisterPreferenceChangeListener(localIPreferenceChangeListener2);
        paramParcel2.writeNoException();
      }
    }

    private static class Proxy
      implements IPreferenceService
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public Map getCurrentPreferences()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ClassLoader localClassLoader = getClass().getClassLoader();
          HashMap localHashMap1 = localParcel2.readHashMap(localClassLoader);
          HashMap localHashMap2 = localHashMap1;
          return localHashMap2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void registerPreferenceChangeListener(IPreferenceChangeListener paramIPreferenceChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          if (paramIPreferenceChangeListener != null)
          {
            localIBinder = paramIPreferenceChangeListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(7, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
          IBinder localIBinder = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void remove(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          localParcel1.writeString(paramString);
          boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setBooleanPreference(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          localParcel1.writeString(paramString);
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          boolean bool = this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setIntPreference(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          boolean bool = this.mRemote.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setLongPreference(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void setStringPreference(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
          boolean bool = this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void unregisterPreferenceChangeListener(IPreferenceChangeListener paramIPreferenceChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceService");
          if (paramIPreferenceChangeListener != null)
          {
            localIBinder = paramIPreferenceChangeListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(8, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
          IBinder localIBinder = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.preferences.IPreferenceService
 * JD-Core Version:    0.6.2
 */