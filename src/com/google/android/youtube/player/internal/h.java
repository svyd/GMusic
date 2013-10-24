package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface h extends IInterface
{
  public abstract void a()
    throws RemoteException;

  public abstract void b()
    throws RemoteException;

  public abstract void c()
    throws RemoteException;

  public static abstract class a extends Binder
    implements h
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.IPlaylistEventListener");
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
      }
      while (true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.youtube.player.internal.IPlaylistEventListener");
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaylistEventListener");
        a();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaylistEventListener");
        b();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaylistEventListener");
        c();
        paramParcel2.writeNoException();
      }
    }

    private static class a
      implements h
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
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaylistEventListener");
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
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaylistEventListener");
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

      public final void c()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaylistEventListener");
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
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.h
 * JD-Core Version:    0.6.2
 */