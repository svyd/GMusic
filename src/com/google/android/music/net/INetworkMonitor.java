package com.google.android.music.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface INetworkMonitor extends IInterface
{
  public abstract boolean hasHighSpeedConnection()
    throws RemoteException;

  public abstract boolean hasMobileOrMeteredConnection()
    throws RemoteException;

  public abstract boolean hasWifiConnection()
    throws RemoteException;

  public abstract boolean isConnected()
    throws RemoteException;

  public abstract boolean isStreamingAvailable()
    throws RemoteException;

  public abstract void registerNetworkChangeListener(INetworkChangeListener paramINetworkChangeListener)
    throws RemoteException;

  public abstract void registerStreamabilityChangeListener(IStreamabilityChangeListener paramIStreamabilityChangeListener)
    throws RemoteException;

  public abstract void unregisterNetworkChangeListener(INetworkChangeListener paramINetworkChangeListener)
    throws RemoteException;

  public abstract void unregisterStreamabilityChangeListener(IStreamabilityChangeListener paramIStreamabilityChangeListener)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements INetworkMonitor
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.net.INetworkMonitor");
    }

    public static INetworkMonitor asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.net.INetworkMonitor");
        if ((localIInterface != null) && ((localIInterface instanceof INetworkMonitor)))
          localObject = (INetworkMonitor)localIInterface;
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
      int i = 0;
      boolean bool1 = true;
      switch (paramInt1)
      {
      default:
        bool1 = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
      }
      while (true)
      {
        return bool1;
        paramParcel2.writeString("com.google.android.music.net.INetworkMonitor");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        boolean bool2 = isConnected();
        paramParcel2.writeNoException();
        if (bool2)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        boolean bool3 = hasMobileOrMeteredConnection();
        paramParcel2.writeNoException();
        if (bool3)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        boolean bool4 = hasWifiConnection();
        paramParcel2.writeNoException();
        if (bool4)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        boolean bool5 = hasHighSpeedConnection();
        paramParcel2.writeNoException();
        if (bool5)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        boolean bool6 = isStreamingAvailable();
        paramParcel2.writeNoException();
        if (bool6)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        IStreamabilityChangeListener localIStreamabilityChangeListener1 = IStreamabilityChangeListener.Stub.asInterface(paramParcel1.readStrongBinder());
        registerStreamabilityChangeListener(localIStreamabilityChangeListener1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        IStreamabilityChangeListener localIStreamabilityChangeListener2 = IStreamabilityChangeListener.Stub.asInterface(paramParcel1.readStrongBinder());
        unregisterStreamabilityChangeListener(localIStreamabilityChangeListener2);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        INetworkChangeListener localINetworkChangeListener1 = INetworkChangeListener.Stub.asInterface(paramParcel1.readStrongBinder());
        registerNetworkChangeListener(localINetworkChangeListener1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.music.net.INetworkMonitor");
        INetworkChangeListener localINetworkChangeListener2 = INetworkChangeListener.Stub.asInterface(paramParcel1.readStrongBinder());
        unregisterNetworkChangeListener(localINetworkChangeListener2);
        paramParcel2.writeNoException();
      }
    }

    private static class Proxy
      implements INetworkMonitor
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

      public boolean hasHighSpeedConnection()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          boolean bool2 = this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean hasMobileOrMeteredConnection()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          boolean bool2 = this.mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean hasWifiConnection()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          boolean bool2 = this.mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isConnected()
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
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

      public boolean isStreamingAvailable()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          boolean bool2 = this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          if (i != 0)
            bool1 = true;
          return bool1;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public void registerNetworkChangeListener(INetworkChangeListener paramINetworkChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          if (paramINetworkChangeListener != null)
          {
            localIBinder = paramINetworkChangeListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(8, localParcel1, localParcel2, 0);
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

      public void registerStreamabilityChangeListener(IStreamabilityChangeListener paramIStreamabilityChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          if (paramIStreamabilityChangeListener != null)
          {
            localIBinder = paramIStreamabilityChangeListener.asBinder();
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

      public void unregisterNetworkChangeListener(INetworkChangeListener paramINetworkChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          if (paramINetworkChangeListener != null)
          {
            localIBinder = paramINetworkChangeListener.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.mRemote.transact(9, localParcel1, localParcel2, 0);
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

      public void unregisterStreamabilityChangeListener(IStreamabilityChangeListener paramIStreamabilityChangeListener)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.net.INetworkMonitor");
          if (paramIStreamabilityChangeListener != null)
          {
            localIBinder = paramIStreamabilityChangeListener.asBinder();
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
 * Qualified Name:     com.google.android.music.net.INetworkMonitor
 * JD-Core Version:    0.6.2
 */