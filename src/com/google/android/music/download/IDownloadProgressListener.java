package com.google.android.music.download;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface IDownloadProgressListener extends IInterface
{
  public abstract void onDownloadProgress(DownloadProgress paramDownloadProgress)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IDownloadProgressListener
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.download.IDownloadProgressListener");
    }

    public static IDownloadProgressListener asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.download.IDownloadProgressListener");
        if ((localIInterface != null) && ((localIInterface instanceof IDownloadProgressListener)))
          localObject = (IDownloadProgressListener)localIInterface;
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
        while (true)
        {
          return bool;
          paramParcel2.writeString("com.google.android.music.download.IDownloadProgressListener");
        }
      case 1:
      }
      paramParcel1.enforceInterface("com.google.android.music.download.IDownloadProgressListener");
      if (paramParcel1.readInt() != 0);
      for (DownloadProgress localDownloadProgress = (DownloadProgress)DownloadProgress.CREATOR.createFromParcel(paramParcel1); ; localDownloadProgress = null)
      {
        onDownloadProgress(localDownloadProgress);
        paramParcel2.writeNoException();
        break;
      }
    }

    private static class Proxy
      implements IDownloadProgressListener
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

      public void onDownloadProgress(DownloadProgress paramDownloadProgress)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.IDownloadProgressListener");
          if (paramDownloadProgress != null)
          {
            localParcel1.writeInt(1);
            paramDownloadProgress.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
            int i = 0;
            localParcel1.writeInt(i);
          }
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
 * Qualified Name:     com.google.android.music.download.IDownloadProgressListener
 * JD-Core Version:    0.6.2
 */