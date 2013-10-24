package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.IBinder;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class w
{
  private static IBinder a(Class<?> paramClass, IBinder paramIBinder1, IBinder paramIBinder2)
    throws w.a
  {
    try
    {
      Class[] arrayOfClass = new Class[2];
      arrayOfClass[0] = IBinder.class;
      arrayOfClass[1] = IBinder.class;
      Constructor localConstructor = paramClass.getConstructor(arrayOfClass);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramIBinder1;
      arrayOfObject[1] = paramIBinder2;
      IBinder localIBinder = (IBinder)localConstructor.newInstance(arrayOfObject);
      return localIBinder;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      StringBuilder localStringBuilder1 = new StringBuilder("Could not find the right constructor for ");
      String str1 = paramClass.getName();
      String str2 = str1;
      throw new a(str2, localNoSuchMethodException);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      StringBuilder localStringBuilder2 = new StringBuilder("Exception thrown by invoked constructor in ");
      String str3 = paramClass.getName();
      String str4 = str3;
      throw new a(str4, localInvocationTargetException);
    }
    catch (InstantiationException localInstantiationException)
    {
      StringBuilder localStringBuilder3 = new StringBuilder("Unable to instantiate the dynamic class ");
      String str5 = paramClass.getName();
      String str6 = str5;
      throw new a(str6, localInstantiationException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      StringBuilder localStringBuilder4 = new StringBuilder("Unable to call the default constructor of ");
      String str7 = paramClass.getName();
      String str8 = str7;
      throw new a(str8, localIllegalAccessException);
    }
  }

  private static IBinder a(ClassLoader paramClassLoader, String paramString, IBinder paramIBinder1, IBinder paramIBinder2)
    throws w.a
  {
    try
    {
      IBinder localIBinder = a(paramClassLoader.loadClass(paramString), paramIBinder1, paramIBinder2);
      return localIBinder;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      String str = "Unable to find dynamic class " + paramString;
      throw new a(str, localClassNotFoundException);
    }
  }

  public static d a(Activity paramActivity, IBinder paramIBinder)
    throws w.a
  {
    Object localObject1 = ac.a(paramActivity, "activity cannot be null");
    Object localObject2 = ac.a(paramIBinder, "serviceBinder cannot be null");
    Context localContext = z.b(paramActivity);
    if (localContext == null)
      throw new a("Could not create remote context");
    Resources localResources = localContext.getResources();
    ClassLoader localClassLoader1 = localContext.getClassLoader();
    int i = z.a(paramActivity, localContext);
    aa localaa = new aa(paramActivity, localResources, localClassLoader1, i);
    ClassLoader localClassLoader2 = localContext.getClassLoader();
    IBinder localIBinder = v.a(localaa).asBinder();
    return d.a.a(a(localClassLoader2, "com.google.android.youtube.api.jar.client.RemoteEmbeddedPlayer", localIBinder, paramIBinder));
  }

  public static final class a extends Exception
  {
    public a(String paramString)
    {
      super();
    }

    public a(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.w
 * JD-Core Version:    0.6.2
 */