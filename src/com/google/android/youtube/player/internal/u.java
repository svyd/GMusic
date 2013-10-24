package com.google.android.youtube.player.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface u extends IInterface
{
  public static abstract class a extends Binder
    implements u
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.dynamic.IObjectWrapper");
    }

    public static u a(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.youtube.player.internal.dynamic.IObjectWrapper");
        if ((localIInterface != null) && ((localIInterface instanceof u)))
          localObject = (u)localIInterface;
        else
          localObject = new a(paramIBinder);
      }
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      switch (paramInt1)
      {
      default:
      case 1598968902:
      }
      for (boolean bool = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2); ; bool = true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.youtube.player.internal.dynamic.IObjectWrapper");
      }
    }

    private static class a
      implements u
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final IBinder asBinder()
      {
        return this.a;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.u
 * JD-Core Version:    0.6.2
 */