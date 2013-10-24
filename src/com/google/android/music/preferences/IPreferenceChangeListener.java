package com.google.android.music.preferences;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IPreferenceChangeListener extends IInterface
{
  public abstract void onBooleanChanged(String paramString, boolean paramBoolean)
    throws RemoteException;

  public abstract void onIntChanged(String paramString, int paramInt)
    throws RemoteException;

  public abstract void onLongChanged(String paramString, long paramLong)
    throws RemoteException;

  public abstract void onPreferenceRemoved(String paramString)
    throws RemoteException;

  public abstract void onStringChanged(String paramString1, String paramString2)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IPreferenceChangeListener
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.preferences.IPreferenceChangeListener");
    }

    public static IPreferenceChangeListener asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.preferences.IPreferenceChangeListener");
        if ((localIInterface != null) && ((localIInterface instanceof IPreferenceChangeListener)))
          localObject = (IPreferenceChangeListener)localIInterface;
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
      }
      while (true)
      {
        return bool1;
        paramParcel2.writeString("com.google.android.music.preferences.IPreferenceChangeListener");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceChangeListener");
        String str1 = paramParcel1.readString();
        onPreferenceRemoved(str1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceChangeListener");
        String str2 = paramParcel1.readString();
        int i = paramParcel1.readInt();
        onIntChanged(str2, i);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceChangeListener");
        String str3 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0);
        for (boolean bool2 = true; ; bool2 = false)
        {
          onBooleanChanged(str3, bool2);
          paramParcel2.writeNoException();
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceChangeListener");
        String str4 = paramParcel1.readString();
        long l = paramParcel1.readLong();
        onLongChanged(str4, l);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.preferences.IPreferenceChangeListener");
        String str5 = paramParcel1.readString();
        String str6 = paramParcel1.readString();
        onStringChanged(str5, str6);
        paramParcel2.writeNoException();
      }
    }

    private static class Proxy
      implements IPreferenceChangeListener
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

      public void onBooleanChanged(String paramString, boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceChangeListener");
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

      public void onIntChanged(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceChangeListener");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
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

      public void onLongChanged(String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceChangeListener");
          localParcel1.writeString(paramString);
          localParcel1.writeLong(paramLong);
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

      public void onPreferenceRemoved(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceChangeListener");
          localParcel1.writeString(paramString);
          boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void onStringChanged(String paramString1, String paramString2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.preferences.IPreferenceChangeListener");
          localParcel1.writeString(paramString1);
          localParcel1.writeString(paramString2);
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
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.preferences.IPreferenceChangeListener
 * JD-Core Version:    0.6.2
 */