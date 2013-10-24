package com.google.android.music.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IStreamabilityChangeListener extends IInterface
{
  public abstract void onStreamabilityChanged(boolean paramBoolean)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IStreamabilityChangeListener
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.net.IStreamabilityChangeListener");
    }

    public static IStreamabilityChangeListener asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.net.IStreamabilityChangeListener");
        if ((localIInterface != null) && ((localIInterface instanceof IStreamabilityChangeListener)))
          localObject = (IStreamabilityChangeListener)localIInterface;
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
        while (true)
        {
          return bool1;
          paramParcel2.writeString("com.google.android.music.net.IStreamabilityChangeListener");
        }
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.music.net.IStreamabilityChangeListener");
      if (paramParcel1.readInt() != 0);
      for (boolean bool2 = true; ; bool2 = false)
      {
        onStreamabilityChanged(bool2);
        paramParcel2.writeNoException();
        break;
      }
    }

    private static class Proxy
      implements IStreamabilityChangeListener
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

      public void onStreamabilityChanged(boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.IStreamabilityChangeListener");
          if (paramBoolean)
          {
            localParcel1.writeInt(i);
            boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
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
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.net.IStreamabilityChangeListener
 * JD-Core Version:    0.6.2
 */