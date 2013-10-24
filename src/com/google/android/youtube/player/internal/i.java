package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface i extends IInterface
{
  public abstract void a(c paramc, int paramInt, String paramString1, String paramString2, String paramString3, Bundle paramBundle)
    throws RemoteException;

  public static abstract class a extends Binder
    implements i
  {
    public static i a(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.youtube.player.internal.IServiceBroker");
        if ((localIInterface != null) && ((localIInterface instanceof i)))
          localObject = (i)localIInterface;
        else
          localObject = new a(paramIBinder);
      }
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      Bundle localBundle = null;
      boolean bool;
      switch (paramInt1)
      {
      default:
      case 1598968902:
        for (bool = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2); ; bool = true)
        {
          return bool;
          paramParcel2.writeString("com.google.android.youtube.player.internal.IServiceBroker");
        }
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IServiceBroker");
      IBinder localIBinder = paramParcel1.readStrongBinder();
      Object localObject;
      if (localIBinder == null)
        localObject = null;
      while (true)
      {
        int i = paramParcel1.readInt();
        String str1 = paramParcel1.readString();
        String str2 = paramParcel1.readString();
        String str3 = paramParcel1.readString();
        if (paramParcel1.readInt() != 0)
          localBundle = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
        a((c)localObject, i, str1, str2, str3, localBundle);
        paramParcel2.writeNoException();
        bool = true;
        break;
        IInterface localIInterface = localIBinder.queryLocalInterface("com.google.android.youtube.player.internal.IConnectionCallbacks");
        if ((localIInterface != null) && ((localIInterface instanceof c)))
          localObject = (c)localIInterface;
        else
          localObject = new c.a.a(localIBinder);
      }
    }

    private static class a
      implements i
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final void a(c paramc, int paramInt, String paramString1, String paramString2, String paramString3, Bundle paramBundle)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IServiceBroker");
          IBinder localIBinder;
          if (paramc != null)
          {
            localIBinder = paramc.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            localParcel1.writeInt(paramInt);
            localParcel1.writeString(paramString1);
            localParcel1.writeString(paramString2);
            localParcel1.writeString(paramString3);
            if (paramBundle == null)
              break label120;
            localParcel1.writeInt(1);
            paramBundle.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            boolean bool = this.a.transact(1, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
            localIBinder = null;
            break;
            label120: localIBinder = null;
            localParcel1.writeInt(localIBinder);
          }
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
 * Qualified Name:     com.google.android.youtube.player.internal.i
 * JD-Core Version:    0.6.2
 */