package com.google.android.music.download.cache;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadRequest;

public abstract interface ICacheManager extends IInterface
{
  public abstract long getAvailableFreeStorageSpaceInBytes()
    throws RemoteException;

  public abstract FileLocation getTempFileLocation(ContentIdentifier paramContentIdentifier, int paramInt1, long paramLong, int paramInt2)
    throws RemoteException;

  public abstract long getTotalStorageSpaceInBytes()
    throws RemoteException;

  public abstract void registerDeleteFilter(IDeleteFilter paramIDeleteFilter)
    throws RemoteException;

  public abstract void requestDelete(DownloadRequest paramDownloadRequest)
    throws RemoteException;

  public abstract String storeInCache(DownloadRequest paramDownloadRequest, String paramString, long paramLong)
    throws RemoteException;

  public abstract void unregisterDeleteFilter(IDeleteFilter paramIDeleteFilter)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements ICacheManager
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.download.cache.ICacheManager");
    }

    public static ICacheManager asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.download.cache.ICacheManager");
        if ((localIInterface != null) && ((localIInterface instanceof ICacheManager)))
          localObject = (ICacheManager)localIInterface;
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
      boolean bool;
      switch (paramInt1)
      {
      default:
        bool = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      }
      while (true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.music.download.cache.ICacheManager");
        bool = true;
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        if (paramParcel1.readInt() != 0)
        {
          localObject = (ContentIdentifier)ContentIdentifier.CREATOR.createFromParcel(paramParcel1);
          label129: int i = paramParcel1.readInt();
          long l1 = paramParcel1.readLong();
          int j = paramParcel1.readInt();
          FileLocation localFileLocation = getTempFileLocation((ContentIdentifier)localObject, i, l1, j);
          paramParcel2.writeNoException();
          if (localFileLocation == null)
            break label194;
          paramParcel2.writeInt(1);
          localFileLocation.writeToParcel(paramParcel2, 1);
        }
        while (true)
        {
          bool = true;
          break;
          localObject = null;
          break label129;
          label194: paramParcel2.writeInt(0);
        }
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        if (paramParcel1.readInt() != 0);
        for (Object localObject = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1); ; localObject = null)
        {
          String str1 = paramParcel1.readString();
          long l2 = paramParcel1.readLong();
          String str2 = storeInCache((DownloadRequest)localObject, str1, l2);
          paramParcel2.writeNoException();
          paramParcel2.writeString(str2);
          bool = true;
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        if (paramParcel1.readInt() != 0);
        for (localObject = (DownloadRequest)DownloadRequest.CREATOR.createFromParcel(paramParcel1); ; localObject = null)
        {
          requestDelete((DownloadRequest)localObject);
          paramParcel2.writeNoException();
          bool = true;
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        long l3 = getAvailableFreeStorageSpaceInBytes();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l3);
        bool = true;
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        long l4 = getTotalStorageSpaceInBytes();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l4);
        bool = true;
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        IDeleteFilter localIDeleteFilter1 = IDeleteFilter.Stub.asInterface(paramParcel1.readStrongBinder());
        registerDeleteFilter(localIDeleteFilter1);
        paramParcel2.writeNoException();
        bool = true;
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.cache.ICacheManager");
        IDeleteFilter localIDeleteFilter2 = IDeleteFilter.Stub.asInterface(paramParcel1.readStrongBinder());
        unregisterDeleteFilter(localIDeleteFilter2);
        paramParcel2.writeNoException();
        bool = true;
      }
    }

    private static class Proxy
      implements ICacheManager
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

      public long getAvailableFreeStorageSpaceInBytes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
          boolean bool = this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public FileLocation getTempFileLocation(ContentIdentifier paramContentIdentifier, int paramInt1, long paramLong, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
            if (paramContentIdentifier != null)
            {
              localParcel1.writeInt(1);
              paramContentIdentifier.writeToParcel(localParcel1, 0);
              localParcel1.writeInt(paramInt1);
              localParcel1.writeLong(paramLong);
              localParcel1.writeInt(paramInt2);
              boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
              localParcel2.readException();
              if (localParcel2.readInt() != 0)
              {
                localFileLocation = (FileLocation)FileLocation.CREATOR.createFromParcel(localParcel2);
                return localFileLocation;
              }
            }
            else
            {
              int i = 0;
              localParcel1.writeInt(i);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          FileLocation localFileLocation = null;
        }
      }

      public long getTotalStorageSpaceInBytes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
          boolean bool = this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l1 = localParcel2.readLong();
          long l2 = l1;
          return l2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void registerDeleteFilter(IDeleteFilter paramIDeleteFilter)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
          if (paramIDeleteFilter != null)
          {
            localIBinder = paramIDeleteFilter.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(6, localParcel1, localParcel2, 0);
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

      public void requestDelete(DownloadRequest paramDownloadRequest)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            boolean bool = this.mRemote.transact(3, localParcel1, localParcel2, 0);
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

      public String storeInCache(DownloadRequest paramDownloadRequest, String paramString, long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
          if (paramDownloadRequest != null)
          {
            localParcel1.writeInt(1);
            paramDownloadRequest.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            localParcel1.writeString(paramString);
            localParcel1.writeLong(paramLong);
            boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
            localParcel2.readException();
            String str1 = localParcel2.readString();
            String str2 = str1;
            return str2;
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

      public void unregisterDeleteFilter(IDeleteFilter paramIDeleteFilter)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.cache.ICacheManager");
          if (paramIDeleteFilter != null)
          {
            localIBinder = paramIDeleteFilter.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(7, localParcel1, localParcel2, 0);
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
 * Qualified Name:     com.google.android.music.download.cache.ICacheManager
 * JD-Core Version:    0.6.2
 */