package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface g extends IInterface
{
  public abstract void a()
    throws RemoteException;

  public abstract void a(String paramString)
    throws RemoteException;

  public abstract void b()
    throws RemoteException;

  public abstract void b(String paramString)
    throws RemoteException;

  public abstract void c()
    throws RemoteException;

  public abstract void d()
    throws RemoteException;

  public static abstract class a extends Binder
    implements g
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.IPlayerStateChangeListener");
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      boolean bool = true;
      switch (paramInt1)
      {
      default:
        bool = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      }
      while (true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        a();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        String str1 = paramParcel1.readString();
        a(str1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        b();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        c();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        d();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
        String str2 = paramParcel1.readString();
        b(str2);
        paramParcel2.writeNoException();
      }
    }

    private static class a
      implements g
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final void a()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          boolean bool = this.a.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          localParcel1.writeString(paramString);
          boolean bool = this.a.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final IBinder asBinder()
      {
        return this.a;
      }

      public final void b()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          boolean bool = this.a.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void b(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          localParcel1.writeString(paramString);
          boolean bool = this.a.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void c()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          boolean bool = this.a.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void d()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          boolean bool = this.a.transact(5, localParcel1, localParcel2, 0);
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
 * Qualified Name:     com.google.android.youtube.player.internal.g
 * JD-Core Version:    0.6.2
 */