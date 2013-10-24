package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface e extends IInterface
{
  public abstract void a(boolean paramBoolean)
    throws RemoteException;

  public static abstract class a extends Binder
    implements e
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.IOnFullscreenListener");
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
        while (true)
        {
          return bool1;
          paramParcel2.writeString("com.google.android.youtube.player.internal.IOnFullscreenListener");
        }
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IOnFullscreenListener");
      if (paramParcel1.readInt() != 0);
      for (boolean bool2 = true; ; bool2 = false)
      {
        a(bool2);
        paramParcel2.writeNoException();
        break;
      }
    }

    private static class a
      implements e
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final void a(boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IOnFullscreenListener");
          if (paramBoolean)
          {
            localParcel1.writeInt(i);
            boolean bool = this.a.transact(1, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
          i = 0;
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
 * Qualified Name:     com.google.android.youtube.player.internal.e
 * JD-Core Version:    0.6.2
 */