package com.google.android.music.playback;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMusicCastMediaRouterCallback extends IInterface
{
  public abstract void onRouteVolumeChanged(String paramString, double paramDouble)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IMusicCastMediaRouterCallback
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.playback.IMusicCastMediaRouterCallback");
    }

    public static IMusicCastMediaRouterCallback asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.playback.IMusicCastMediaRouterCallback");
        if ((localIInterface != null) && ((localIInterface instanceof IMusicCastMediaRouterCallback)))
          localObject = (IMusicCastMediaRouterCallback)localIInterface;
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
        paramParcel2.writeString("com.google.android.music.playback.IMusicCastMediaRouterCallback");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.playback.IMusicCastMediaRouterCallback");
        String str = paramParcel1.readString();
        double d = paramParcel1.readDouble();
        onRouteVolumeChanged(str, d);
        paramParcel2.writeNoException();
      }
    }

    private static class Proxy
      implements IMusicCastMediaRouterCallback
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

      public void onRouteVolumeChanged(String paramString, double paramDouble)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.playback.IMusicCastMediaRouterCallback");
          localParcel1.writeString(paramString);
          localParcel1.writeDouble(paramDouble);
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
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.IMusicCastMediaRouterCallback
 * JD-Core Version:    0.6.2
 */