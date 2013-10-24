package com.google.android.finsky.services;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IMarketCatalogService extends IInterface
{
  public abstract boolean isBackendEnabled(String paramString, int paramInt)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IMarketCatalogService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.finsky.services.IMarketCatalogService");
    }

    public static IMarketCatalogService asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.finsky.services.IMarketCatalogService");
        if ((localIInterface != null) && ((localIInterface instanceof IMarketCatalogService)))
          localObject = (IMarketCatalogService)localIInterface;
        else
          localObject = new Proxy(paramIBinder);
      }
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
          paramParcel2.writeString("com.google.android.finsky.services.IMarketCatalogService");
        }
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.finsky.services.IMarketCatalogService");
      String str = paramParcel1.readString();
      int i = paramParcel1.readInt();
      boolean bool2 = isBackendEnabled(str, i);
      paramParcel2.writeNoException();
      if (bool2);
      for (int j = 1; ; j = 0)
      {
        paramParcel2.writeInt(j);
        break;
      }
    }

    private static class Proxy
      implements IMarketCatalogService
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

      public boolean isBackendEnabled(String paramString, int paramInt)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.finsky.services.IMarketCatalogService");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          boolean bool2 = this.mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            return bool1;
          bool1 = false;
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
 * Qualified Name:     com.google.android.finsky.services.IMarketCatalogService
 * JD-Core Version:    0.6.2
 */