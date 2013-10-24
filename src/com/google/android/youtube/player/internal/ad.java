package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;

public final class ad extends ab
{
  public final b a(Context paramContext, String paramString, t.a parama, t.b paramb)
  {
    String str1 = paramContext.getPackageName();
    String str2 = z.d(paramContext);
    Context localContext = paramContext;
    String str3 = paramString;
    t.a locala = parama;
    t.b localb = paramb;
    return new o(localContext, str3, str1, str2, locala, localb);
  }

  public final d a(Activity paramActivity, b paramb)
    throws w.a
  {
    IBinder localIBinder = paramb.a();
    return w.a(paramActivity, localIBinder);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.ad
 * JD-Core Version:    0.6.2
 */