package com.google.android.youtube.player.internal;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class aa extends y
{
  private static final Class<?>[] a = arrayOfClass;
  private final Resources b;
  private final LayoutInflater c;
  private final Resources.Theme d;

  static
  {
    Class[] arrayOfClass = new Class[2];
    arrayOfClass[0] = Context.class;
    arrayOfClass[1] = AttributeSet.class;
  }

  public aa(Activity paramActivity, Resources paramResources, ClassLoader paramClassLoader, int paramInt)
  {
    super(paramActivity);
    Resources localResources = (Resources)ac.a(paramResources, "resources cannot be null");
    this.b = localResources;
    LayoutInflater localLayoutInflater = ((LayoutInflater)super.getSystemService("layout_inflater")).cloneInContext(this);
    a locala = new a(paramClassLoader);
    localLayoutInflater.setFactory(locala);
    this.c = localLayoutInflater;
    Resources.Theme localTheme = paramResources.newTheme();
    this.d = localTheme;
    this.d.applyStyle(paramInt, false);
  }

  public final Context getApplicationContext()
  {
    return super.getApplicationContext();
  }

  public final Context getBaseContext()
  {
    return super.getBaseContext();
  }

  public final Resources getResources()
  {
    return this.b;
  }

  public final Object getSystemService(String paramString)
  {
    if ("layout_inflater".equals(paramString));
    for (Object localObject = this.c; ; localObject = super.getSystemService(paramString))
      return localObject;
  }

  public final Resources.Theme getTheme()
  {
    return this.d;
  }

  private static final class a
    implements LayoutInflater.Factory
  {
    private final ClassLoader a;

    public a(ClassLoader paramClassLoader)
    {
      ClassLoader localClassLoader = (ClassLoader)ac.a(paramClassLoader, "remoteClassLoader cannot be null");
      this.a = localClassLoader;
    }

    public final View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
    {
      try
      {
        Class localClass = this.a.loadClass(paramString).asSubclass(View.class);
        Class[] arrayOfClass = aa.a();
        Constructor localConstructor = localClass.getConstructor(arrayOfClass);
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = paramContext;
        arrayOfObject[1] = paramAttributeSet;
        localView = (View)localConstructor.newInstance(arrayOfObject);
        return localView;
      }
      catch (NoClassDefFoundError localNoClassDefFoundError)
      {
        while (true)
          localView = null;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        while (true)
          localView = null;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        while (true)
          localView = null;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        while (true)
          localView = null;
      }
      catch (InstantiationException localInstantiationException)
      {
        while (true)
          localView = null;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        while (true)
          localView = null;
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        while (true)
          View localView = null;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.aa
 * JD-Core Version:    0.6.2
 */