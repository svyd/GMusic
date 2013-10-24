package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface f extends IInterface
{
  public abstract void a()
    throws RemoteException;

  public abstract void a(int paramInt)
    throws RemoteException;

  public abstract void a(boolean paramBoolean)
    throws RemoteException;

  public abstract void b()
    throws RemoteException;

  public abstract void c()
    throws RemoteException;

  public static abstract class a extends Binder
    implements f
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.IPlaybackEventListener");
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
        paramParcel2.writeString("com.google.android.youtube.player.internal.IPlaybackEventListener");
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaybackEventListener");
        a();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaybackEventListener");
        b();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaybackEventListener");
        c();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaybackEventListener");
        if (paramParcel1.readInt() != 0);
        for (boolean bool2 = true; ; bool2 = false)
        {
          a(bool2);
          paramParcel2.writeNoException();
          break;
        }
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IPlaybackEventListener");
        int i = paramParcel1.readInt();
        a(i);
        paramParcel2.writeNoException();
      }
    }

    private static class a
      implements f
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
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaybackEventListener");
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

      public final void a(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaybackEventListener");
          localParcel1.writeInt(paramInt);
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

      public final void a(boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaybackEventListener");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
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
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaybackEventListener");
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
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IPlaybackEventListener");
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
 * Qualified Name:     com.google.android.youtube.player.internal.f
 * JD-Core Version:    0.6.2
 */