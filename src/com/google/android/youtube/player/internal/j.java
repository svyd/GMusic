package com.google.android.youtube.player.internal;

import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;

public abstract interface j extends IInterface
{
  public abstract void a(Bitmap paramBitmap, String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;

  public abstract void a(String paramString, boolean paramBoolean1, boolean paramBoolean2)
    throws RemoteException;

  public static abstract class a extends Binder
    implements j
  {
    public a()
    {
      attachInterface(this, "com.google.android.youtube.player.internal.IThumbnailLoaderClient");
    }

    public IBinder asBinder()
    {
      return this;
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      boolean bool1 = false;
      boolean bool2 = true;
      Bitmap localBitmap;
      switch (paramInt1)
      {
      default:
        bool2 = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902:
        while (true)
        {
          return bool2;
          paramParcel2.writeString("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
        }
      case 1:
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
        label90: String str1;
        if (paramParcel1.readInt() != 0)
        {
          localBitmap = (Bitmap)Bitmap.CREATOR.createFromParcel(paramParcel1);
          str1 = paramParcel1.readString();
          if (paramParcel1.readInt() == 0)
            break label141;
        }
        label141: for (boolean bool3 = null; ; str2 = null)
        {
          if (paramParcel1.readInt() != 0)
            bool1 = true;
          a(localBitmap, str1, bool3, bool1);
          paramParcel2.writeNoException();
          break;
          localBitmap = null;
          break label90;
        }
      case 2:
      }
      paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
      String str2 = paramParcel1.readString();
      if (paramParcel1.readInt() != 0)
        localBitmap = null;
      while (true)
      {
        if (paramParcel1.readInt() != 0)
          bool1 = true;
        a(str2, localBitmap, bool1);
        paramParcel2.writeNoException();
        break;
        int i = 0;
      }
    }

    private static class a
      implements j
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final void a(Bitmap paramBitmap, String paramString, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
            if (paramBitmap != null)
            {
              localParcel1.writeInt(1);
              paramBitmap.writeToParcel(localParcel1, 0);
              localParcel1.writeString(paramString);
              if (paramBoolean1)
              {
                j = 1;
                localParcel1.writeInt(j);
                if (!paramBoolean2)
                  break label136;
                localParcel1.writeInt(i);
                boolean bool = this.a.transact(1, localParcel1, localParcel2, 0);
                localParcel2.readException();
              }
            }
            else
            {
              j = 0;
              localParcel1.writeInt(j);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          int j = 0;
          continue;
          label136: i = 0;
        }
      }

      public final void a(String paramString, boolean paramBoolean1, boolean paramBoolean2)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IThumbnailLoaderClient");
          localParcel1.writeString(paramString);
          if (paramBoolean1);
          for (int j = 1; ; j = 0)
          {
            localParcel1.writeInt(j);
            if (!paramBoolean2)
              break;
            localParcel1.writeInt(i);
            boolean bool = this.a.transact(2, localParcel1, localParcel2, 0);
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

      public final IBinder asBinder()
      {
        return this.a;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.j
 * JD-Core Version:    0.6.2
 */