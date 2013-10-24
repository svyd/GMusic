package com.google.android.youtube.player.internal;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

public final class o extends r<l>
  implements b
{
  private final String b;
  private final String c;
  private final String d;
  private boolean e;

  public o(Context paramContext, String paramString1, String paramString2, String paramString3, t.a parama, t.b paramb)
  {
    super(paramContext, parama, paramb);
    String str1 = (String)ac.a(paramString1, "developerKey cannot be null");
    this.b = str1;
    String str2 = ac.a(paramString2, "callingPackage cannot be null or empty");
    this.c = str2;
    String str3 = ac.a(paramString3, "callingAppVersion cannot be null or empty");
    this.d = str3;
  }

  private final void k()
  {
    i();
    if (!this.e)
      return;
    throw new IllegalStateException("Connection client has been released");
  }

  public final IBinder a()
  {
    k();
    try
    {
      IBinder localIBinder = ((l)j()).a();
      return localIBinder;
    }
    catch (RemoteException localRemoteException)
    {
      throw new IllegalStateException(localRemoteException);
    }
  }

  protected final void a(i parami, r<l>.d paramr)
    throws RemoteException
  {
    String str1 = this.c;
    String str2 = this.d;
    String str3 = this.b;
    i locali = parami;
    r<l>.d localr = paramr;
    locali.a(localr, 1000, str1, str2, str3, null);
  }

  public final void a(boolean paramBoolean)
  {
    if (!f())
      return;
    try
    {
      ((l)j()).a(paramBoolean);
      label21: this.e = true;
      return;
    }
    catch (RemoteException localRemoteException)
    {
      break label21;
    }
  }

  protected final String b()
  {
    return "com.google.android.youtube.player.internal.IYouTubeService";
  }

  protected final String c()
  {
    return "com.google.android.youtube.api.service.START";
  }

  public final void d()
  {
    if (!this.e)
      a(true);
    super.d();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.o
 * JD-Core Version:    0.6.2
 */