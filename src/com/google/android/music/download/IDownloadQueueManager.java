package com.google.android.music.download;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract interface IDownloadQueueManager extends IInterface
{
  public abstract void cancelAndPurge(int paramInt1, int paramInt2)
    throws RemoteException;

  public abstract void download(List<DownloadRequest> paramList, IDownloadProgressListener paramIDownloadProgressListener, int paramInt)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IDownloadQueueManager
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.download.IDownloadQueueManager");
    }

    public static IDownloadQueueManager asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.download.IDownloadQueueManager");
        if ((localIInterface != null) && ((localIInterface instanceof IDownloadQueueManager)))
          localObject = (IDownloadQueueManager)localIInterface;
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
      case 2:
      }
      while (true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.music.download.IDownloadQueueManager");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.IDownloadQueueManager");
        Parcelable.Creator localCreator = DownloadRequest.CREATOR;
        ArrayList localArrayList = paramParcel1.createTypedArrayList(localCreator);
        IDownloadProgressListener localIDownloadProgressListener = IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder());
        int i = paramParcel1.readInt();
        download(localArrayList, localIDownloadProgressListener, i);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.IDownloadQueueManager");
        int j = paramParcel1.readInt();
        int k = paramParcel1.readInt();
        cancelAndPurge(j, k);
        paramParcel2.writeNoException();
      }
    }

    private static class Proxy
      implements IDownloadQueueManager
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

      public void cancelAndPurge(int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.IDownloadQueueManager");
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void download(List<DownloadRequest> paramList, IDownloadProgressListener paramIDownloadProgressListener, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.IDownloadQueueManager");
          localParcel1.writeTypedList(paramList);
          if (paramIDownloadProgressListener != null)
          {
            localIBinder = paramIDownloadProgressListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            localParcel1.writeInt(paramInt);
            boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
            localParcel2.readException();
            return;
          }
          IBinder localIBinder = null;
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
 * Qualified Name:     com.google.android.music.download.IDownloadQueueManager
 * JD-Core Version:    0.6.2
 */