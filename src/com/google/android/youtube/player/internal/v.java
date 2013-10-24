package com.google.android.youtube.player.internal;

import android.os.IBinder;
import java.lang.reflect.Field;

public final class v<T> extends u.a
{
  private final T a;

  private v(T paramT)
  {
    this.a = paramT;
  }

  public static <T> u a(T paramT)
  {
    return new v(paramT);
  }

  public static <T> T a(u paramu)
  {
    Object localObject1;
    if ((paramu instanceof v))
      localObject1 = ((v)paramu).a;
    while (true)
    {
      return localObject1;
      IBinder localIBinder = paramu.asBinder();
      Field[] arrayOfField = localIBinder.getClass().getDeclaredFields();
      if (arrayOfField.length != 1)
        break label123;
      Field localField = arrayOfField[0];
      if (!localField.isAccessible())
      {
        localField.setAccessible(true);
        try
        {
          Object localObject2 = localField.get(localIBinder);
          localObject1 = localObject2;
        }
        catch (NullPointerException localNullPointerException)
        {
          throw new IllegalArgumentException("Binder object is null.", localNullPointerException);
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          throw new IllegalArgumentException("remoteBinder is the wrong class.", localIllegalArgumentException);
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          throw new IllegalArgumentException("Could not access the field in remoteBinder.", localIllegalAccessException);
        }
      }
    }
    throw new IllegalArgumentException("The concrete class implementing IObjectWrapper must have exactly one declared *private* field for the wrapped object. Preferably, this is an instance of the ObjectWrapper<T> class.");
    label123: throw new IllegalArgumentException("The concrete class implementing IObjectWrapper must have exactly *one* declared private field for the wrapped object.  Preferably, this is an instance of the ObjectWrapper<T> class.");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.v
 * JD-Core Version:    0.6.2
 */