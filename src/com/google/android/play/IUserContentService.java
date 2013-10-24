package com.google.android.play;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public abstract interface IUserContentService extends IInterface
{
  public abstract List<Bundle> getDocuments(int paramInt1, int paramInt2)
    throws RemoteException;

  public static abstract class Stub extends Binder
    implements IUserContentService
  {
    public Stub()
    {
      attachInterface(this, "com.google.android.play.IUserContentService");
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
      }
      while (true)
      {
        return bool;
        paramParcel2.writeString("com.google.android.play.IUserContentService");
        continue;
        paramParcel1.enforceInterface("com.google.android.play.IUserContentService");
        int i = paramParcel1.readInt();
        int j = paramParcel1.readInt();
        List localList = getDocuments(i, j);
        paramParcel2.writeNoException();
        paramParcel2.writeTypedList(localList);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.play.IUserContentService
 * JD-Core Version:    0.6.2
 */