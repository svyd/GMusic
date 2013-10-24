package com.google.android.music.store;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.HashMap;
import java.util.Map;

public abstract interface IStoreService extends IInterface
{
  public abstract Map getAlbumIdsAndAlbumKeepOnIdsForArtist(long paramLong)
    throws RemoteException;

  public abstract long[] getArtistIdsForAlbum(long paramLong)
    throws RemoteException;

  public abstract long getSizeAlbum(long paramLong)
    throws RemoteException;

  public abstract long getSizeAutoPlaylist(long paramLong)
    throws RemoteException;

  public abstract long getSizePlaylist(long paramLong)
    throws RemoteException;

  public abstract boolean isAlbumSelectedAsKeepOn(long paramLong, boolean paramBoolean)
    throws RemoteException;

  public abstract boolean isArtistSelectedAsKeepOn(long paramLong)
    throws RemoteException;

  public abstract boolean isAutoPlaylistSelectedAsKeepOn(long paramLong)
    throws RemoteException;

  public abstract boolean isPlaylistSelectedAsKeepOn(long paramLong)
    throws RemoteException;

  public abstract int setRingtone(long paramLong, String paramString)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IStoreService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.music.store.IStoreService");
    }

    public static IStoreService asInterface(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.music.store.IStoreService");
        if ((localIInterface != null) && ((localIInterface instanceof IStoreService)))
          localObject = (IStoreService)localIInterface;
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
      case 10:
      }
      while (true)
      {
        return bool1;
        paramParcel2.writeString("com.google.android.music.store.IStoreService");
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l1 = paramParcel1.readLong();
        long l2 = getSizeAlbum(l1);
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l2);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l3 = paramParcel1.readLong();
        long l4 = getSizePlaylist(l3);
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l4);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l5 = paramParcel1.readLong();
        long l6 = getSizeAutoPlaylist(l5);
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l6);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l7 = paramParcel1.readLong();
        long[] arrayOfLong = getArtistIdsForAlbum(l7);
        paramParcel2.writeNoException();
        paramParcel2.writeLongArray(arrayOfLong);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l8 = paramParcel1.readLong();
        Map localMap = getAlbumIdsAndAlbumKeepOnIdsForArtist(l8);
        paramParcel2.writeNoException();
        paramParcel2.writeMap(localMap);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l9 = paramParcel1.readLong();
        if (paramParcel1.readInt() != 0);
        for (boolean bool2 = true; ; bool2 = false)
        {
          boolean bool3 = isAlbumSelectedAsKeepOn(l9, bool2);
          paramParcel2.writeNoException();
          if (bool3)
            i = 1;
          paramParcel2.writeInt(i);
          break;
        }
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l10 = paramParcel1.readLong();
        boolean bool4 = isArtistSelectedAsKeepOn(l10);
        paramParcel2.writeNoException();
        if (bool4)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l11 = paramParcel1.readLong();
        boolean bool5 = isPlaylistSelectedAsKeepOn(l11);
        paramParcel2.writeNoException();
        if (bool5)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l12 = paramParcel1.readLong();
        boolean bool6 = isAutoPlaylistSelectedAsKeepOn(l12);
        paramParcel2.writeNoException();
        if (bool6)
          i = 1;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.music.store.IStoreService");
        long l13 = paramParcel1.readLong();
        String str = paramParcel1.readString();
        int j = setRingtone(l13, str);
        paramParcel2.writeNoException();
        paramParcel2.writeInt(j);
      }
    }

    private static class Proxy
      implements IStoreService
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

      public Map getAlbumIdsAndAlbumKeepOnIdsForArtist(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          ClassLoader localClassLoader = getClass().getClassLoader();
          HashMap localHashMap1 = localParcel2.readHashMap(localClassLoader);
          HashMap localHashMap2 = localHashMap1;
          return localHashMap2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long[] getArtistIdsForAlbum(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long[] arrayOfLong1 = localParcel2.createLongArray();
          long[] arrayOfLong2 = arrayOfLong1;
          return arrayOfLong2;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public long getSizeAlbum(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(1, localParcel1, localParcel2, 0);
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

      public long getSizeAutoPlaylist(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(3, localParcel1, localParcel2, 0);
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

      public long getSizePlaylist(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool = this.mRemote.transact(2, localParcel1, localParcel2, 0);
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

      public boolean isAlbumSelectedAsKeepOn(long paramLong, boolean paramBoolean)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          if (paramBoolean);
          for (int i = 1; ; i = 0)
          {
            localParcel1.writeInt(i);
            boolean bool2 = this.mRemote.transact(6, localParcel1, localParcel2, 0);
            localParcel2.readException();
            int j = localParcel2.readInt();
            if (j == 0)
              break;
            return bool1;
          }
          bool1 = false;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public boolean isArtistSelectedAsKeepOn(long paramLong)
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool2 = this.mRemote.transact(7, localParcel1, localParcel2, 0);
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

      public boolean isAutoPlaylistSelectedAsKeepOn(long paramLong)
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool2 = this.mRemote.transact(9, localParcel1, localParcel2, 0);
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

      public boolean isPlaylistSelectedAsKeepOn(long paramLong)
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          boolean bool2 = this.mRemote.transact(8, localParcel1, localParcel2, 0);
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

      public int setRingtone(long paramLong, String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.music.store.IStoreService");
          localParcel1.writeLong(paramLong);
          localParcel1.writeString(paramString);
          boolean bool = this.mRemote.transact(10, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          int j = i;
          return j;
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
 * Qualified Name:     com.google.android.music.store.IStoreService
 * JD-Core Version:    0.6.2
 */