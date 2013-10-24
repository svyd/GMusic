package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface c extends IInterface
{
  public abstract void a(String paramString, IBinder paramIBinder)
    throws RemoteException;

  public static abstract class a extends Binder
    implements c
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.IConnectionCallbacks");
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
      }
      while (true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.youtube.player.internal.IConnectionCallbacks");
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IConnectionCallbacks");
        String str = paramParcel1.readString();
        IBinder localIBinder = paramParcel1.readStrongBinder();
        a(str, localIBinder);
        paramParcel2.writeNoException();
      }
    }

    private static class a
      implements c
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final void a(String paramString, IBinder paramIBinder)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IConnectionCallbacks");
          localParcel1.writeString(paramString);
          localParcel1.writeStrongBinder(paramIBinder);
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
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.c
 * JD-Core Version:    0.6.2
 */