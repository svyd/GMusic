package android.support.v4.view.accessibility;

import android.os.Build.VERSION;

public class AccessibilityRecordCompat
{
  private static final AccessibilityRecordImpl IMPL = new AccessibilityRecordStubImpl();
  private final Object mRecord;

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new AccessibilityRecordJellyBeanImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 15)
    {
      IMPL = new AccessibilityRecordIcsMr1Impl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new AccessibilityRecordIcsImpl();
      return;
    }
  }

  public AccessibilityRecordCompat(Object paramObject)
  {
    this.mRecord = paramObject;
  }

  public static AccessibilityRecordCompat obtain()
  {
    Object localObject = IMPL.obtain();
    return new AccessibilityRecordCompat(localObject);
  }

  public boolean equals(Object paramObject)
  {
    boolean bool = true;
    if (this == paramObject);
    while (true)
    {
      return bool;
      if (paramObject == null)
      {
        bool = false;
      }
      else
      {
        Class localClass1 = getClass();
        Class localClass2 = paramObject.getClass();
        if (localClass1 != localClass2)
        {
          bool = false;
        }
        else
        {
          AccessibilityRecordCompat localAccessibilityRecordCompat = (AccessibilityRecordCompat)paramObject;
          if (this.mRecord == null)
          {
            if (localAccessibilityRecordCompat.mRecord != null)
              bool = false;
          }
          else
          {
            Object localObject1 = this.mRecord;
            Object localObject2 = localAccessibilityRecordCompat.mRecord;
            if (!localObject1.equals(localObject2))
              bool = false;
          }
        }
      }
    }
  }

  public int hashCode()
  {
    if (this.mRecord == null);
    for (int i = 0; ; i = this.mRecord.hashCode())
      return i;
  }

  public void setFromIndex(int paramInt)
  {
    AccessibilityRecordImpl localAccessibilityRecordImpl = IMPL;
    Object localObject = this.mRecord;
    localAccessibilityRecordImpl.setFromIndex(localObject, paramInt);
  }

  public void setItemCount(int paramInt)
  {
    AccessibilityRecordImpl localAccessibilityRecordImpl = IMPL;
    Object localObject = this.mRecord;
    localAccessibilityRecordImpl.setItemCount(localObject, paramInt);
  }

  public void setScrollable(boolean paramBoolean)
  {
    AccessibilityRecordImpl localAccessibilityRecordImpl = IMPL;
    Object localObject = this.mRecord;
    localAccessibilityRecordImpl.setScrollable(localObject, paramBoolean);
  }

  public void setToIndex(int paramInt)
  {
    AccessibilityRecordImpl localAccessibilityRecordImpl = IMPL;
    Object localObject = this.mRecord;
    localAccessibilityRecordImpl.setToIndex(localObject, paramInt);
  }

  static class AccessibilityRecordJellyBeanImpl extends AccessibilityRecordCompat.AccessibilityRecordIcsMr1Impl
  {
  }

  static class AccessibilityRecordIcsMr1Impl extends AccessibilityRecordCompat.AccessibilityRecordIcsImpl
  {
  }

  static class AccessibilityRecordIcsImpl extends AccessibilityRecordCompat.AccessibilityRecordStubImpl
  {
    public Object obtain()
    {
      return AccessibilityRecordCompatIcs.obtain();
    }

    public void setFromIndex(Object paramObject, int paramInt)
    {
      AccessibilityRecordCompatIcs.setFromIndex(paramObject, paramInt);
    }

    public void setItemCount(Object paramObject, int paramInt)
    {
      AccessibilityRecordCompatIcs.setItemCount(paramObject, paramInt);
    }

    public void setScrollable(Object paramObject, boolean paramBoolean)
    {
      AccessibilityRecordCompatIcs.setScrollable(paramObject, paramBoolean);
    }

    public void setToIndex(Object paramObject, int paramInt)
    {
      AccessibilityRecordCompatIcs.setToIndex(paramObject, paramInt);
    }
  }

  static class AccessibilityRecordStubImpl
    implements AccessibilityRecordCompat.AccessibilityRecordImpl
  {
    public Object obtain()
    {
      return null;
    }

    public void setFromIndex(Object paramObject, int paramInt)
    {
    }

    public void setItemCount(Object paramObject, int paramInt)
    {
    }

    public void setScrollable(Object paramObject, boolean paramBoolean)
    {
    }

    public void setToIndex(Object paramObject, int paramInt)
    {
    }
  }

  static abstract interface AccessibilityRecordImpl
  {
    public abstract Object obtain();

    public abstract void setFromIndex(Object paramObject, int paramInt);

    public abstract void setItemCount(Object paramObject, int paramInt);

    public abstract void setScrollable(Object paramObject, boolean paramBoolean);

    public abstract void setToIndex(Object paramObject, int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityRecordCompat
 * JD-Core Version:    0.6.2
 */