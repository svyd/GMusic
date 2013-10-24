package com.google.android.music.download.keepon;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.IDownloadProgressListener.Stub;

public abstract interface IKeeponCallbackManager extends IInterface
{
  public abstract boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException;

  public abstract void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IKeeponCallbackManager
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.download.keepon.IKeeponCallbackManager");
    }

    public static IKeeponCallbackManager asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.download.keepon.IKeeponCallbackManager");
        if ((localIInterface != null) && ((localIInterface instanceof IKeeponCallbackManager)))
          localObject = (IKeeponCallbackManager)localIInterface;
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
        paramParcel2.writeString("com.google.android.music.download.keepon.IKeeponCallbackManager");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.download.keepon.IKeeponCallbackManager");
        ContentIdentifier localContentIdentifier;
        if (paramParcel1.readInt() != 0)
        {
          localContentIdentifier = (ContentIdentifier)ContentIdentifier.CREATOR.createFromParcel(paramParcel1);
          label90: IDownloadProgressListener localIDownloadProgressListener1 = IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder());
          boolean bool2 = addDownloadProgressListener(localContentIdentifier, localIDownloadProgressListener1);
          paramParcel2.writeNoException();
          if (!bool2)
            break label136;
        }
        label136: for (int i = 1; ; i = 0)
        {
          paramParcel2.writeInt(i);
          break;
          localContentIdentifier = null;
          break label90;
        }
        paramParcel1.enforceInterface("com.google.android.music.download.keepon.IKeeponCallbackManager");
        IDownloadProgressListener localIDownloadProgressListener2 = IDownloadProgressListener.Stub.asInterface(paramParcel1.readStrongBinder());
        removeDownloadProgressListener(localIDownloadProgressListener2);
        paramParcel2.writeNoException();
      }
    }

    private static class Proxy
      implements IKeeponCallbackManager
    {
      private IBinder mRemote;

      Proxy(IBinder paramIBinder)
      {
        this.mRemote = paramIBinder;
      }

      public boolean addDownloadProgressListener(ContentIdentifier paramContentIdentifier, IDownloadProgressListener paramIDownloadProgressListener)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.music.download.keepon.IKeeponCallbackManager");
            if (paramContentIdentifier != null)
            {
              localParcel1.writeInt(1);
              paramContentIdentifier.writeToParcel(localParcel1, 0);
              if (paramIDownloadProgressListener != null)
              {
                localIBinder = paramIDownloadProgressListener.asBinder();
                localParcel1.writeStrongBinder(localIBinder);
                boolean bool2 = this.mRemote.transact(1, localParcel1, localParcel2, 0);
                localParcel2.readException();
                int i = localParcel2.readInt();
                if (i == 0)
                  break label135;
                return bool1;
              }
            }
            else
            {
              localIBinder = null;
              localParcel1.writeInt(localIBinder);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          IBinder localIBinder = null;
          continue;
          label135: bool1 = false;
        }
      }

      public IBinder asBinder()
      {
        return this.mRemote;
      }

      public void removeDownloadProgressListener(IDownloadProgressListener paramIDownloadProgressListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.download.keepon.IKeeponCallbackManager");
          if (paramIDownloadProgressListener != null)
          {
            localIBinder = paramIDownloadProgressListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
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
 * Qualified Name:     com.google.android.music.download.keepon.IKeeponCallbackManager
 * JD-Core Version:    0.6.2
 */