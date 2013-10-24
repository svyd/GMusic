package com.google.android.music.download.cache;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.music.download.ContentIdentifier;

public abstract interface IDeleteFilter extends IInterface
{
  public abstract ContentIdentifier[] getFilteredIds()
    throws RemoteException;

  public abstract boolean shouldFilter(String paramString)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IDeleteFilter
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.download.cache.IDeleteFilter");
    }

    public static IDeleteFilter asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.download.cache.IDeleteFilter");
        if ((localIInterface != null) && ((localIInterface instanceof IDeleteFilter)))
          localObject = (IDeleteFilter)localIInterface;
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
      case 1:
      case 2:
      }
      while (true)
      {
        return bool1;
        paramParcel2.writeString("com.google.android.music.download.cache.IDeleteFilter");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.cache.IDeleteFilter");
        String str = paramParcel1.readString();
        boolean bool2 = shouldFilter(str);
        paramParcel2.writeNoException();
        if (bool2);
        for (int i = 1; ; i = 0)
        {
          paramParcel2.writeInt(i);
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.download.cache.IDeleteFilter");
        ContentIdentifier[] arrayOfContentIdentifier = getFilteredIds();
        paramParcel2.writeNoException();
        paramParcel2.writeTypedArray(arrayOfContentIdentifier, 1);
      }
    }

    private static class Proxy
      implements IDeleteFilter
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

      public ContentIdentifier[] getFilteredIds()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.IDeleteFilter");
          boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          Parcelable.Creator localCreator = ContentIdentifier.CREATOR;
          ContentIdentifier[] arrayOfContentIdentifier = (ContentIdentifier[])localParcel2.createTypedArray(localCreator);
          return arrayOfContentIdentifier;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean shouldFilter(String paramString)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.IDeleteFilter");
          localParcel1.writeString(paramString);
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
 * Qualified Name:     com.google.android.music.download.cache.IDeleteFilter
 * JD-Core Version:    0.6.2
 */