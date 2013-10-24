package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;

public abstract class ab
{
  private static final ab a = b();

  public static ab a()
  {
    return a;
  }

  private static ab b()
  {
    try
    {
      Class localClass = Class.forName("com.google.android.youtube.api.locallylinked.LocallyLinkedFactory").asSubclass(ab.class);
      localObject = localClass;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      try
      {
        Object localObject = (ab)((Class)localObject).newInstance();
        return localObject;
      }
      catch (InstantiationException localInstantiationException)
      {
        while (true)
        {
          throw new IllegalStateException(localInstantiationException);
          localClassNotFoundException = localClassNotFoundException;
          ad localad = new ad();
        }
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        throw new IllegalStateException(localIllegalAccessException);
      }
    }
  }

  public abstract b a(Context paramContext, String paramString, t.a parama, t.b paramb);

  public abstract d a(Activity paramActivity, b paramb)
    throws w.a;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.ab
 * JD-Core Version:    0.6.2
 */