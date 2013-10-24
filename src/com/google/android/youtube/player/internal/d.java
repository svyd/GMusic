package com.google.android.youtube.player.internal;

import android.content.res.Configuration;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.view.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract interface d extends IInterface
{
  public abstract void a()
    throws RemoteException;

  public abstract void a(int paramInt)
    throws RemoteException;

  public abstract void a(Configuration paramConfiguration)
    throws RemoteException;

  public abstract void a(e parame)
    throws RemoteException;

  public abstract void a(f paramf)
    throws RemoteException;

  public abstract void a(g paramg)
    throws RemoteException;

  public abstract void a(h paramh)
    throws RemoteException;

  public abstract void a(String paramString)
    throws RemoteException;

  public abstract void a(String paramString, int paramInt)
    throws RemoteException;

  public abstract void a(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;

  public abstract void a(List<String> paramList, int paramInt1, int paramInt2)
    throws RemoteException;

  public abstract void a(boolean paramBoolean)
    throws RemoteException;

  public abstract boolean a(int paramInt, KeyEvent paramKeyEvent)
    throws RemoteException;

  public abstract boolean a(Bundle paramBundle)
    throws RemoteException;

  public abstract void b()
    throws RemoteException;

  public abstract void b(int paramInt)
    throws RemoteException;

  public abstract void b(String paramString, int paramInt)
    throws RemoteException;

  public abstract void b(String paramString, int paramInt1, int paramInt2)
    throws RemoteException;

  public abstract void b(List<String> paramList, int paramInt1, int paramInt2)
    throws RemoteException;

  public abstract void b(boolean paramBoolean)
    throws RemoteException;

  public abstract boolean b(int paramInt, KeyEvent paramKeyEvent)
    throws RemoteException;

  public abstract void c(int paramInt)
    throws RemoteException;

  public abstract void c(boolean paramBoolean)
    throws RemoteException;

  public abstract boolean c()
    throws RemoteException;

  public abstract void d(int paramInt)
    throws RemoteException;

  public abstract void d(boolean paramBoolean)
    throws RemoteException;

  public abstract boolean d()
    throws RemoteException;

  public abstract void e(boolean paramBoolean)
    throws RemoteException;

  public abstract boolean e()
    throws RemoteException;

  public abstract void f()
    throws RemoteException;

  public abstract void g()
    throws RemoteException;

  public abstract int h()
    throws RemoteException;

  public abstract int i()
    throws RemoteException;

  public abstract int j()
    throws RemoteException;

  public abstract void k()
    throws RemoteException;

  public abstract void l()
    throws RemoteException;

  public abstract void m()
    throws RemoteException;

  public abstract void n()
    throws RemoteException;

  public abstract void o()
    throws RemoteException;

  public abstract void p()
    throws RemoteException;

  public abstract void q()
    throws RemoteException;

  public abstract Bundle r()
    throws RemoteException;

  public abstract u s()
    throws RemoteException;

  public static abstract class a extends Binder
    implements d
  {
    public static d a(IBinder paramIBinder)
    {
      Object localObject;
      if (paramIBinder == null)
        localObject = null;
      while (true)
      {
        return localObject;
        IInterface localIInterface = paramIBinder.queryLocalInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        if ((localIInterface != null) && ((localIInterface instanceof d)))
          localObject = (d)localIInterface;
        else
          localObject = new a(paramIBinder);
      }
    }

    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      boolean bool1 = null;
      int i = null;
      boolean bool5 = true;
      switch (paramInt1)
      {
      default:
        bool5 = super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
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
      case 11:
      case 12:
      case 13:
      case 14:
      case 15:
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 33:
      case 34:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      }
      while (true)
      {
        return bool5;
        paramParcel2.writeString("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        if (paramParcel1.readInt() != 0);
        for (bool1 = null; ; localObject1 = null)
        {
          a(bool1);
          paramParcel2.writeNoException();
          break;
        }
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        String str1 = paramParcel1.readString();
        int m = paramParcel1.readInt();
        a(str1, m);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        String str2 = paramParcel1.readString();
        int n = paramParcel1.readInt();
        b(str2, n);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        String str3 = paramParcel1.readString();
        int i1 = paramParcel1.readInt();
        int i2 = paramParcel1.readInt();
        a(str3, i1, i2);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        String str4 = paramParcel1.readString();
        int i3 = paramParcel1.readInt();
        int i4 = paramParcel1.readInt();
        b(str4, i3, i4);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        ArrayList localArrayList1 = paramParcel1.createStringArrayList();
        int i5 = paramParcel1.readInt();
        int i6 = paramParcel1.readInt();
        a(localArrayList1, i5, i6);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        ArrayList localArrayList2 = paramParcel1.createStringArrayList();
        int i7 = paramParcel1.readInt();
        int i8 = paramParcel1.readInt();
        b(localArrayList2, i7, i8);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        a();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        b();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        boolean bool6 = c();
        paramParcel2.writeNoException();
        if (bool6)
          i = null;
        paramParcel2.writeInt(i);
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        boolean bool7 = d();
        paramParcel2.writeNoException();
        int j;
        if (bool7)
          j = null;
        paramParcel2.writeInt(j);
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        boolean bool8 = e();
        paramParcel2.writeNoException();
        int k;
        if (bool8)
          k = null;
        paramParcel2.writeInt(k);
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        f();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        g();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i9 = h();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(i9);
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i10 = i();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(i10);
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i11 = paramParcel1.readInt();
        a(i11);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i12 = paramParcel1.readInt();
        b(i12);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        boolean bool2;
        if (paramParcel1.readInt() != 0)
          bool2 = null;
        b(bool2);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i13 = paramParcel1.readInt();
        c(i13);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i14 = j();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(i14);
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        int i15 = paramParcel1.readInt();
        d(i15);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        String str5 = paramParcel1.readString();
        a(str5);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        boolean bool3;
        if (paramParcel1.readInt() != 0)
          bool3 = null;
        c(bool3);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        boolean bool4;
        if (paramParcel1.readInt() != 0)
          bool4 = null;
        d(bool4);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        Object localObject2 = paramParcel1.readStrongBinder();
        if (localObject2 == null);
        while (true)
        {
          a((e)localObject1);
          paramParcel2.writeNoException();
          break;
          IInterface localIInterface1 = ((IBinder)localObject2).queryLocalInterface("com.google.android.youtube.player.internal.IOnFullscreenListener");
          if ((localIInterface1 != null) && ((localIInterface1 instanceof e)))
            localObject1 = (e)localIInterface1;
          else
            localObject1 = new e.a.a((IBinder)localObject2);
        }
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        localObject2 = paramParcel1.readStrongBinder();
        if (localObject2 == null);
        while (true)
        {
          a((h)localObject1);
          paramParcel2.writeNoException();
          break;
          IInterface localIInterface2 = ((IBinder)localObject2).queryLocalInterface("com.google.android.youtube.player.internal.IPlaylistEventListener");
          if ((localIInterface2 != null) && ((localIInterface2 instanceof h)))
            localObject1 = (h)localIInterface2;
          else
            localObject1 = new h.a.a((IBinder)localObject2);
        }
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        localObject2 = paramParcel1.readStrongBinder();
        if (localObject2 == null);
        while (true)
        {
          a((g)localObject1);
          paramParcel2.writeNoException();
          break;
          IInterface localIInterface3 = ((IBinder)localObject2).queryLocalInterface("com.google.android.youtube.player.internal.IPlayerStateChangeListener");
          if ((localIInterface3 != null) && ((localIInterface3 instanceof g)))
            localObject1 = (g)localIInterface3;
          else
            localObject1 = new g.a.a((IBinder)localObject2);
        }
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        localObject2 = paramParcel1.readStrongBinder();
        if (localObject2 == null);
        while (true)
        {
          a((f)localObject1);
          paramParcel2.writeNoException();
          break;
          IInterface localIInterface4 = ((IBinder)localObject2).queryLocalInterface("com.google.android.youtube.player.internal.IPlaybackEventListener");
          if ((localIInterface4 != null) && ((localIInterface4 instanceof f)))
            localObject1 = (f)localIInterface4;
          else
            localObject1 = new f.a.a((IBinder)localObject2);
        }
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        k();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        l();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        if (paramParcel1.readInt() != 0)
          localObject1 = (Configuration)Configuration.CREATOR.createFromParcel(paramParcel1);
        a((Configuration)localObject1);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        m();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        n();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        o();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        p();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        if (paramParcel1.readInt() != 0)
          localObject2 = null;
        e(()localObject2);
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        q();
        paramParcel2.writeNoException();
        continue;
        paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
        Object localObject1 = r();
        paramParcel2.writeNoException();
        if (localObject1 != null)
        {
          paramParcel2.writeInt(1);
          ((Bundle)localObject1).writeToParcel(paramParcel2, 1);
        }
        else
        {
          paramParcel2.writeInt(0);
          continue;
          paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramParcel1.readInt() != 0)
            localObject1 = (Bundle)Bundle.CREATOR.createFromParcel(paramParcel1);
          boolean bool9 = a((Bundle)localObject1);
          paramParcel2.writeNoException();
          if (bool9)
            localObject2 = null;
          paramParcel2.writeInt((int)localObject2);
          continue;
          paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          int i16 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0)
            localObject1 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          boolean bool10 = a(i16, (KeyEvent)localObject1);
          paramParcel2.writeNoException();
          if (bool10)
            localObject2 = null;
          paramParcel2.writeInt((int)localObject2);
          continue;
          paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          i16 = paramParcel1.readInt();
          if (paramParcel1.readInt() != 0)
            localObject1 = (KeyEvent)KeyEvent.CREATOR.createFromParcel(paramParcel1);
          boolean bool11 = b(i16, (KeyEvent)localObject1);
          paramParcel2.writeNoException();
          if (bool11)
            localObject2 = null;
          paramParcel2.writeInt((int)localObject2);
          continue;
          paramParcel1.enforceInterface("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localObject2 = s();
          paramParcel2.writeNoException();
          if (localObject2 != null)
            localObject1 = ((u)localObject2).asBinder();
          paramParcel2.writeStrongBinder((IBinder)localObject1);
        }
      }
    }

    private static class a
      implements d
    {
      private IBinder a;

      a(IBinder paramIBinder)
      {
        this.a = paramIBinder;
      }

      public final void a()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(8, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeInt(paramInt);
          boolean bool = this.a.transact(17, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(Configuration paramConfiguration)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramConfiguration != null)
          {
            localParcel1.writeInt(1);
            paramConfiguration.writeToParcel(localParcel1, 0);
          }
          while (true)
          {
            boolean bool = this.a.transact(32, localParcel1, localParcel2, 0);
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

      public final void a(e parame)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (parame != null)
          {
            localIBinder = parame.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.a.transact(26, localParcel1, localParcel2, 0);
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

      public final void a(f paramf)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramf != null)
          {
            localIBinder = paramf.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.a.transact(29, localParcel1, localParcel2, 0);
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

      public final void a(g paramg)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramg != null)
          {
            localIBinder = paramg.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.a.transact(28, localParcel1, localParcel2, 0);
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

      public final void a(h paramh)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramh != null)
          {
            localIBinder = paramh.asBinder();
            localParcel1.writeStrongBinder(localIBinder);
            boolean bool = this.a.transact(27, localParcel1, localParcel2, 0);
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

      public final void a(String paramString)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeString(paramString);
          boolean bool = this.a.transact(23, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          boolean bool = this.a.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = this.a.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(List<String> paramList, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeStringList(paramList);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = this.a.transact(6, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void a(boolean paramBoolean)
        throws RemoteException
      {
        int i = 1;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramBoolean)
          {
            localParcel1.writeInt(i);
            boolean bool = this.a.transact(1, localParcel1, localParcel2, 0);
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

      public final boolean a(int paramInt, KeyEvent paramKeyEvent)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
            localParcel1.writeInt(paramInt);
            if (paramKeyEvent != null)
            {
              localParcel1.writeInt(1);
              paramKeyEvent.writeToParcel(localParcel1, 0);
              boolean bool2 = this.a.transact(41, localParcel1, localParcel2, 0);
              localParcel2.readException();
              int i = localParcel2.readInt();
              if (i != 0)
                return bool1;
            }
            else
            {
              int j = 0;
              localParcel1.writeInt(j);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          bool1 = false;
        }
      }

      public final boolean a(Bundle paramBundle)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
            if (paramBundle != null)
            {
              localParcel1.writeInt(1);
              paramBundle.writeToParcel(localParcel1, 0);
              boolean bool2 = this.a.transact(40, localParcel1, localParcel2, 0);
              localParcel2.readException();
              int i = localParcel2.readInt();
              if (i != 0)
                return bool1;
            }
            else
            {
              int j = 0;
              localParcel1.writeInt(j);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          bool1 = false;
        }
      }

      public final IBinder asBinder()
      {
        return this.a;
      }

      public final void b()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(9, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void b(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeInt(paramInt);
          boolean bool = this.a.transact(18, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void b(String paramString, int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt);
          boolean bool = this.a.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void b(String paramString, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeString(paramString);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = this.a.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void b(List<String> paramList, int paramInt1, int paramInt2)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeStringList(paramList);
          localParcel1.writeInt(paramInt1);
          localParcel1.writeInt(paramInt2);
          boolean bool = this.a.transact(7, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void b(boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          boolean bool = this.a.transact(19, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final boolean b(int paramInt, KeyEvent paramKeyEvent)
        throws RemoteException
      {
        boolean bool1 = true;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        while (true)
        {
          try
          {
            localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
            localParcel1.writeInt(paramInt);
            if (paramKeyEvent != null)
            {
              localParcel1.writeInt(1);
              paramKeyEvent.writeToParcel(localParcel1, 0);
              boolean bool2 = this.a.transact(42, localParcel1, localParcel2, 0);
              localParcel2.readException();
              int i = localParcel2.readInt();
              if (i != 0)
                return bool1;
            }
            else
            {
              int j = 0;
              localParcel1.writeInt(j);
              continue;
            }
          }
          finally
          {
            localParcel2.recycle();
            localParcel1.recycle();
          }
          bool1 = false;
        }
      }

      public final void c(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeInt(paramInt);
          boolean bool = this.a.transact(20, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void c(boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          boolean bool = this.a.transact(24, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final boolean c()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool2 = this.a.transact(10, localParcel1, localParcel2, 0);
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

      public final void d(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          localParcel1.writeInt(paramInt);
          boolean bool = this.a.transact(22, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void d(boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          boolean bool = this.a.transact(25, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final boolean d()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool2 = this.a.transact(11, localParcel1, localParcel2, 0);
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

      public final void e(boolean paramBoolean)
        throws RemoteException
      {
        int i = 0;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          if (paramBoolean)
            i = 1;
          localParcel1.writeInt(i);
          boolean bool = this.a.transact(37, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final boolean e()
        throws RemoteException
      {
        boolean bool1 = false;
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool2 = this.a.transact(12, localParcel1, localParcel2, 0);
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

      public final void f()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(13, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void g()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(14, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final int h()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(15, localParcel1, localParcel2, 0);
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

      public final int i()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(16, localParcel1, localParcel2, 0);
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

      public final int j()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(21, localParcel1, localParcel2, 0);
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

      public final void k()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(30, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void l()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(31, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void m()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(33, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void n()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(34, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void o()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(35, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void p()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(36, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final void q()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(38, localParcel1, localParcel2, 0);
          localParcel2.readException();
          return;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final Bundle r()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(39, localParcel1, localParcel2, 0);
          localParcel2.readException();
          if (localParcel2.readInt() != 0)
          {
            localBundle = (Bundle)Bundle.CREATOR.createFromParcel(localParcel2);
            return localBundle;
          }
          Bundle localBundle = null;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        }
      }

      public final u s()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.google.android.youtube.player.internal.IEmbeddedPlayer");
          boolean bool = this.a.transact(43, localParcel1, localParcel2, 0);
          localParcel2.readException();
          u localu1 = u.a.a(localParcel2.readStrongBinder());
          u localu2 = localu1;
          return localu2;
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
 * Qualified Name:     com.google.android.youtube.player.internal.d
 * JD-Core Version:    0.6.2
 */