package com.google.android.music.animator;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class PropertyAnimator extends Animator
{
  private HashMap<Object, HashMap<String, Method>> getterPropertyMap;
  private Method mGetter = null;
  private String mPropertyName;
  private Method mSetter;
  private Object mTarget;
  private HashMap<Object, HashMap<String, Method>> setterPropertyMap;

  public PropertyAnimator(int paramInt1, Object paramObject, String paramString, int paramInt2, int paramInt3)
  {
    super(paramInt1, paramInt2, paramInt3);
    HashMap localHashMap1 = new HashMap();
    this.setterPropertyMap = localHashMap1;
    HashMap localHashMap2 = new HashMap();
    this.getterPropertyMap = localHashMap2;
    this.mTarget = paramObject;
    this.mPropertyName = paramString;
  }

  private Method getPropertyFunction(String paramString)
  {
    Object localObject = null;
    String str1 = this.mPropertyName.substring(0, 1);
    String str2 = this.mPropertyName.substring(1);
    String str3 = str1.toUpperCase();
    String str4 = paramString + str3 + str2;
    Class[] arrayOfClass = new Class[1];
    Class localClass = getValueType();
    arrayOfClass[0] = localClass;
    try
    {
      Method localMethod = this.mTarget.getClass().getMethod(str4, arrayOfClass);
      localObject = localMethod;
      return localObject;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      while (true)
      {
        PrintStream localPrintStream = System.out;
        StringBuilder localStringBuilder = new StringBuilder().append("Couldn't find setter for property ");
        String str5 = this.mPropertyName;
        String str6 = str5 + ": " + localNoSuchMethodException;
        localPrintStream.println(str6);
      }
    }
  }

  void animateValue(float paramFloat)
  {
    super.animateValue(paramFloat);
    if (this.mSetter == null)
      return;
    try
    {
      Method localMethod = this.mSetter;
      Object localObject1 = this.mTarget;
      Object[] arrayOfObject = new Object[1];
      Object localObject2 = getAnimatedValue();
      arrayOfObject[0] = localObject2;
      Object localObject3 = localMethod.invoke(localObject1, arrayOfObject);
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localInvocationTargetException.printStackTrace();
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
    }
  }

  void initAnimation()
  {
    super.initAnimation();
    HashMap localHashMap2;
    if (this.mSetter == null)
    {
      HashMap localHashMap1 = this.setterPropertyMap;
      Object localObject1 = this.mTarget;
      localHashMap2 = (HashMap)localHashMap1.get(localObject1);
      if (localHashMap2 != null)
      {
        String str1 = this.mPropertyName;
        Method localMethod1 = (Method)localHashMap2.get(str1);
        this.mSetter = localMethod1;
        if (this.mSetter != null)
          return;
      }
      Method localMethod2 = getPropertyFunction("set");
      this.mSetter = localMethod2;
      if (localHashMap2 == null)
      {
        localHashMap2 = new HashMap();
        HashMap localHashMap3 = this.setterPropertyMap;
        Object localObject2 = this.mTarget;
        Object localObject3 = localHashMap3.put(localObject2, localHashMap2);
      }
      String str2 = this.mPropertyName;
      Method localMethod3 = this.mSetter;
      Object localObject4 = localHashMap2.put(str2, localMethod3);
    }
    if ((getValueFrom() != null) && (getValueTo() != null))
      return;
    if (this.mGetter == null)
    {
      HashMap localHashMap4 = this.getterPropertyMap;
      Object localObject5 = this.mTarget;
      localHashMap2 = (HashMap)localHashMap4.get(localObject5);
      if (localHashMap2 != null)
      {
        String str3 = this.mPropertyName;
        Method localMethod4 = (Method)localHashMap2.get(str3);
        this.mGetter = localMethod4;
        if (this.mGetter != null)
          return;
      }
      Method localMethod5 = getPropertyFunction("get");
      this.mGetter = localMethod5;
      if (localHashMap2 == null)
      {
        localHashMap2 = new HashMap();
        HashMap localHashMap5 = this.getterPropertyMap;
        Object localObject6 = this.mTarget;
        Object localObject7 = localHashMap5.put(localObject6, localHashMap2);
      }
      String str4 = this.mPropertyName;
      Method localMethod6 = this.mGetter;
      Object localObject8 = localHashMap2.put(str4, localMethod6);
    }
    try
    {
      if (getValueFrom() == null)
      {
        Method localMethod7 = this.mGetter;
        Object localObject9 = this.mTarget;
        Object[] arrayOfObject1 = new Object[0];
        Object localObject10 = localMethod7.invoke(localObject9, arrayOfObject1);
        setValueFrom(localObject10);
      }
      if (getValueTo() != null)
        return;
      Method localMethod8 = this.mGetter;
      Object localObject11 = this.mTarget;
      Object[] arrayOfObject2 = new Object[0];
      Object localObject12 = localMethod8.invoke(localObject11, arrayOfObject2);
      setValueTo(localObject12);
      return;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      localInvocationTargetException.printStackTrace();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.animator.PropertyAnimator
 * JD-Core Version:    0.6.2
 */