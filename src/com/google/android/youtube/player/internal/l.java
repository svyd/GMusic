package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface l extends IInterface
{
  public abstract IBinder a()
    throws RemoteException;

  public abstract k a(j paramj)
    throws RemoteException;

  public abstract void a(boolean paramBoolean)
    throws RemoteException;

  public static abstract class a extends Binder
    implements l
  {
    public static l a(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.youtube.player.internal.IYouTubeService");
        if ((localIInterface != null) && ((localIInterface instanceof l)))
          localObject = (l)localIInterface;
        else
          localObject = new a(paramIBinder);
      }
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      IBinder localIBinder1 = null;
      boolean bool = true;
      k localk;
      switch (paramInt1)
      {
      default:
        bool = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
        while (true)
        {
          return bool;
          paramParcel2.writeString("com.google.android.youtube.player.internal.IYouTubeService");
          continue;
          paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IYouTubeService");
          IBinder localIBinder2 = a();
          paramParcel2.writeNoException();
          paramParcel2.writeStrongBinder(localIBinder2);
        }
      case 2:
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IYouTubeService");
        IBinder localIBinder3 = paramParcel1.readStrongBinder();
        Object localObject;
        if (localIBinder3 == null)
          localObject = null;
        while (true)
        {
          localk = a((j)localObject);
          paramParcel2.writeNoException();
          if (localk != null)
            localIBinder1 = localk.asBinder();
          paramParcel2.writeStrongBinder(localIBinder1);
          break;
          IInterface localIInterface = localIBinder3.queryLocalInterface("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
          if ((localIInterface != null) && ((localIInterface instanceof j)))
            localObject = (j)localIInterface;
          else
            localObject = new j.a.a(localIBinder3);
        }
      case 3:
      }
      paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IYouTubeService");
      if (paramParcel1.readInt() != 0)
        localk = null;
      while (true)
      {
        a(localk);
        paramParcel2.writeNoException();
        break;
        int i = 0;
      }
    }

    private static class a
      implements l
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final IBinder a()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IYouTubeService");
          boolean bool = this.a.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          IBinder localIBinder1 = localParcel2.readStrongBinder();
          IBinder localIBinder2 = localIBinder1;
          return localIBinder2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final k a(j paramj)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IYouTubeService");
          if (paramj != null)
          {
            localIBinder = paramj.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.a.transact(2, localParcel1, localParcel2, 0);
            localParcel2.readException();
            k localk1 = k.a.a(localParcel2.readStrongBinder());
            k localk2 = localk1;
            return localk2;
          }
          IBinder localIBinder = null;
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
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IYouTubeService");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
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

      public final IBinder asBinder()
      {
        return this.a;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.l
 * JD-Core Version:    0.6.2
 */