package android.support.v4.view.accessibility;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;

public class AccessibilityNodeInfoCompat
{
  private static final AccessibilityNodeInfoImpl IMPL = new AccessibilityNodeInfoStubImpl();
  private final Object mInfo;

  static
  {
    String str = Build.VERSION.CODENAME;
    if ("JellyBeanMR2".equals(str))
    {
      IMPL = new AccessibilityNodeInfoJellybeanMr2Impl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new AccessibilityNodeInfoJellybeanImpl();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new AccessibilityNodeInfoIcsImpl();
      return;
    }
  }

  public AccessibilityNodeInfoCompat(Object paramObject)
  {
    this.mInfo = paramObject;
  }

  private static String getActionSymbolicName(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default:
      str = "ACTION_UNKNOWN";
    case 1:
    case 2:
    case 4:
    case 8:
    case 16:
    case 32:
    case 64:
    case 128:
    case 256:
    case 512:
    case 1024:
    case 2048:
    case 4096:
    case 8192:
    case 65536:
    case 16384:
    case 32768:
    case 131072:
    }
    while (true)
    {
      return str;
      str = "ACTION_FOCUS";
      continue;
      str = "ACTION_CLEAR_FOCUS";
      continue;
      str = "ACTION_SELECT";
      continue;
      str = "ACTION_CLEAR_SELECTION";
      continue;
      str = "ACTION_CLICK";
      continue;
      str = "ACTION_LONG_CLICK";
      continue;
      str = "ACTION_ACCESSIBILITY_FOCUS";
      continue;
      str = "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
      continue;
      str = "ACTION_NEXT_AT_MOVEMENT_GRANULARITY";
      continue;
      str = "ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY";
      continue;
      str = "ACTION_NEXT_HTML_ELEMENT";
      continue;
      str = "ACTION_PREVIOUS_HTML_ELEMENT";
      continue;
      str = "ACTION_SCROLL_FORWARD";
      continue;
      str = "ACTION_SCROLL_BACKWARD";
      continue;
      str = "ACTION_CUT";
      continue;
      str = "ACTION_COPY";
      continue;
      str = "ACTION_PASTE";
      continue;
      str = "ACTION_SET_SELECTION";
    }
  }

  public static AccessibilityNodeInfoCompat obtain(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = paramAccessibilityNodeInfoCompat.mInfo;
    return wrapNonNullInstance(localAccessibilityNodeInfoImpl.obtain(localObject));
  }

  static AccessibilityNodeInfoCompat wrapNonNullInstance(Object paramObject)
  {
    if (paramObject != null);
    for (AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = new AccessibilityNodeInfoCompat(paramObject); ; localAccessibilityNodeInfoCompat = null)
      return localAccessibilityNodeInfoCompat;
  }

  public void addAction(int paramInt)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.addAction(localObject, paramInt);
  }

  public void addChild(View paramView)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.addChild(localObject, paramView);
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
          AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = (AccessibilityNodeInfoCompat)paramObject;
          if (this.mInfo == null)
          {
            if (localAccessibilityNodeInfoCompat.mInfo != null)
              bool = false;
          }
          else
          {
            Object localObject1 = this.mInfo;
            Object localObject2 = localAccessibilityNodeInfoCompat.mInfo;
            if (!localObject1.equals(localObject2))
              bool = false;
          }
        }
      }
    }
  }

  public int getActions()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getActions(localObject);
  }

  public void getBoundsInParent(Rect paramRect)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.getBoundsInParent(localObject, paramRect);
  }

  public void getBoundsInScreen(Rect paramRect)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.getBoundsInScreen(localObject, paramRect);
  }

  public CharSequence getClassName()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getClassName(localObject);
  }

  public CharSequence getContentDescription()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getContentDescription(localObject);
  }

  public Object getInfo()
  {
    return this.mInfo;
  }

  public int getMovementGranularities()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getMovementGranularities(localObject);
  }

  public CharSequence getPackageName()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getPackageName(localObject);
  }

  public CharSequence getText()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getText(localObject);
  }

  public String getViewIdResourceName()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.getViewIdResourceName(localObject);
  }

  public int hashCode()
  {
    if (this.mInfo == null);
    for (int i = 0; ; i = this.mInfo.hashCode())
      return i;
  }

  public boolean isAccessibilityFocused()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isAccessibilityFocused(localObject);
  }

  public boolean isCheckable()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isCheckable(localObject);
  }

  public boolean isChecked()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isChecked(localObject);
  }

  public boolean isClickable()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isClickable(localObject);
  }

  public boolean isEnabled()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isEnabled(localObject);
  }

  public boolean isFocusable()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isFocusable(localObject);
  }

  public boolean isFocused()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isFocused(localObject);
  }

  public boolean isLongClickable()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isLongClickable(localObject);
  }

  public boolean isPassword()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isPassword(localObject);
  }

  public boolean isScrollable()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isScrollable(localObject);
  }

  public boolean isSelected()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isSelected(localObject);
  }

  public boolean isVisibleToUser()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    return localAccessibilityNodeInfoImpl.isVisibleToUser(localObject);
  }

  public void recycle()
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.recycle(localObject);
  }

  public void setAccessibilityFocused(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setAccessibilityFocused(localObject, paramBoolean);
  }

  public void setBoundsInParent(Rect paramRect)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setBoundsInParent(localObject, paramRect);
  }

  public void setBoundsInScreen(Rect paramRect)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setBoundsInScreen(localObject, paramRect);
  }

  public void setClassName(CharSequence paramCharSequence)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setClassName(localObject, paramCharSequence);
  }

  public void setClickable(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setClickable(localObject, paramBoolean);
  }

  public void setContentDescription(CharSequence paramCharSequence)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setContentDescription(localObject, paramCharSequence);
  }

  public void setEnabled(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setEnabled(localObject, paramBoolean);
  }

  public void setFocusable(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setFocusable(localObject, paramBoolean);
  }

  public void setFocused(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setFocused(localObject, paramBoolean);
  }

  public void setLongClickable(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setLongClickable(localObject, paramBoolean);
  }

  public void setMovementGranularities(int paramInt)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setMovementGranularities(localObject, paramInt);
  }

  public void setPackageName(CharSequence paramCharSequence)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setPackageName(localObject, paramCharSequence);
  }

  public void setParent(View paramView)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setParent(localObject, paramView);
  }

  public void setScrollable(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setScrollable(localObject, paramBoolean);
  }

  public void setSelected(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setSelected(localObject, paramBoolean);
  }

  public void setSource(View paramView)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setSource(localObject, paramView);
  }

  public void setVisibleToUser(boolean paramBoolean)
  {
    AccessibilityNodeInfoImpl localAccessibilityNodeInfoImpl = IMPL;
    Object localObject = this.mInfo;
    localAccessibilityNodeInfoImpl.setVisibleToUser(localObject, paramBoolean);
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    String str1 = super.toString();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
    Rect localRect = new Rect();
    getBoundsInParent(localRect);
    String str2 = "; boundsInParent: " + localRect;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str2);
    getBoundsInScreen(localRect);
    String str3 = "; boundsInScreen: " + localRect;
    StringBuilder localStringBuilder4 = localStringBuilder1.append(str3);
    StringBuilder localStringBuilder5 = localStringBuilder1.append("; packageName: ");
    CharSequence localCharSequence1 = getPackageName();
    StringBuilder localStringBuilder6 = localStringBuilder5.append(localCharSequence1);
    StringBuilder localStringBuilder7 = localStringBuilder1.append("; className: ");
    CharSequence localCharSequence2 = getClassName();
    StringBuilder localStringBuilder8 = localStringBuilder7.append(localCharSequence2);
    StringBuilder localStringBuilder9 = localStringBuilder1.append("; text: ");
    CharSequence localCharSequence3 = getText();
    StringBuilder localStringBuilder10 = localStringBuilder9.append(localCharSequence3);
    StringBuilder localStringBuilder11 = localStringBuilder1.append("; contentDescription: ");
    CharSequence localCharSequence4 = getContentDescription();
    StringBuilder localStringBuilder12 = localStringBuilder11.append(localCharSequence4);
    StringBuilder localStringBuilder13 = localStringBuilder1.append("; viewId: ");
    String str4 = getViewIdResourceName();
    StringBuilder localStringBuilder14 = localStringBuilder13.append(str4);
    StringBuilder localStringBuilder15 = localStringBuilder1.append("; checkable: ");
    boolean bool1 = isCheckable();
    StringBuilder localStringBuilder16 = localStringBuilder15.append(bool1);
    StringBuilder localStringBuilder17 = localStringBuilder1.append("; checked: ");
    boolean bool2 = isChecked();
    StringBuilder localStringBuilder18 = localStringBuilder17.append(bool2);
    StringBuilder localStringBuilder19 = localStringBuilder1.append("; focusable: ");
    boolean bool3 = isFocusable();
    StringBuilder localStringBuilder20 = localStringBuilder19.append(bool3);
    StringBuilder localStringBuilder21 = localStringBuilder1.append("; focused: ");
    boolean bool4 = isFocused();
    StringBuilder localStringBuilder22 = localStringBuilder21.append(bool4);
    StringBuilder localStringBuilder23 = localStringBuilder1.append("; selected: ");
    boolean bool5 = isSelected();
    StringBuilder localStringBuilder24 = localStringBuilder23.append(bool5);
    StringBuilder localStringBuilder25 = localStringBuilder1.append("; clickable: ");
    boolean bool6 = isClickable();
    StringBuilder localStringBuilder26 = localStringBuilder25.append(bool6);
    StringBuilder localStringBuilder27 = localStringBuilder1.append("; longClickable: ");
    boolean bool7 = isLongClickable();
    StringBuilder localStringBuilder28 = localStringBuilder27.append(bool7);
    StringBuilder localStringBuilder29 = localStringBuilder1.append("; enabled: ");
    boolean bool8 = isEnabled();
    StringBuilder localStringBuilder30 = localStringBuilder29.append(bool8);
    StringBuilder localStringBuilder31 = localStringBuilder1.append("; password: ");
    boolean bool9 = isPassword();
    StringBuilder localStringBuilder32 = localStringBuilder31.append(bool9);
    StringBuilder localStringBuilder33 = new StringBuilder().append("; scrollable: ");
    boolean bool10 = isScrollable();
    String str5 = bool10;
    StringBuilder localStringBuilder34 = localStringBuilder1.append(str5);
    StringBuilder localStringBuilder35 = localStringBuilder1.append("; [");
    int i = getActions();
    while (i != 0)
    {
      int j = Integer.numberOfTrailingZeros(i);
      int k = 1 << j;
      int m = k ^ 0xFFFFFFFF;
      i &= m;
      String str6 = getActionSymbolicName(k);
      StringBuilder localStringBuilder36 = localStringBuilder1.append(str6);
      if (i != 0)
        StringBuilder localStringBuilder37 = localStringBuilder1.append(", ");
    }
    StringBuilder localStringBuilder38 = localStringBuilder1.append("]");
    return localStringBuilder1.toString();
  }

  static class AccessibilityNodeInfoJellybeanMr2Impl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoJellybeanImpl
  {
    public String getViewIdResourceName(Object paramObject)
    {
      return AccessibilityNodeInfoCompatJellybeanMr2.getViewIdResourceName(paramObject);
    }
  }

  static class AccessibilityNodeInfoJellybeanImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoIcsImpl
  {
    public int getMovementGranularities(Object paramObject)
    {
      return AccessibilityNodeInfoCompatJellyBean.getMovementGranularities(paramObject);
    }

    public boolean isAccessibilityFocused(Object paramObject)
    {
      return AccessibilityNodeInfoCompatJellyBean.isAccessibilityFocused(paramObject);
    }

    public boolean isVisibleToUser(Object paramObject)
    {
      return AccessibilityNodeInfoCompatJellyBean.isVisibleToUser(paramObject);
    }

    public void setAccessibilityFocused(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatJellyBean.setAccesibilityFocused(paramObject, paramBoolean);
    }

    public void setMovementGranularities(Object paramObject, int paramInt)
    {
      AccessibilityNodeInfoCompatJellyBean.setMovementGranularities(paramObject, paramInt);
    }

    public void setVisibleToUser(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatJellyBean.setVisibleToUser(paramObject, paramBoolean);
    }
  }

  static class AccessibilityNodeInfoIcsImpl extends AccessibilityNodeInfoCompat.AccessibilityNodeInfoStubImpl
  {
    public void addAction(Object paramObject, int paramInt)
    {
      AccessibilityNodeInfoCompatIcs.addAction(paramObject, paramInt);
    }

    public void addChild(Object paramObject, View paramView)
    {
      AccessibilityNodeInfoCompatIcs.addChild(paramObject, paramView);
    }

    public int getActions(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.getActions(paramObject);
    }

    public void getBoundsInParent(Object paramObject, Rect paramRect)
    {
      AccessibilityNodeInfoCompatIcs.getBoundsInParent(paramObject, paramRect);
    }

    public void getBoundsInScreen(Object paramObject, Rect paramRect)
    {
      AccessibilityNodeInfoCompatIcs.getBoundsInScreen(paramObject, paramRect);
    }

    public CharSequence getClassName(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.getClassName(paramObject);
    }

    public CharSequence getContentDescription(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.getContentDescription(paramObject);
    }

    public CharSequence getPackageName(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.getPackageName(paramObject);
    }

    public CharSequence getText(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.getText(paramObject);
    }

    public boolean isCheckable(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isCheckable(paramObject);
    }

    public boolean isChecked(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isChecked(paramObject);
    }

    public boolean isClickable(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isClickable(paramObject);
    }

    public boolean isEnabled(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isEnabled(paramObject);
    }

    public boolean isFocusable(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isFocusable(paramObject);
    }

    public boolean isFocused(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isFocused(paramObject);
    }

    public boolean isLongClickable(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isLongClickable(paramObject);
    }

    public boolean isPassword(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isPassword(paramObject);
    }

    public boolean isScrollable(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isScrollable(paramObject);
    }

    public boolean isSelected(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.isSelected(paramObject);
    }

    public Object obtain(Object paramObject)
    {
      return AccessibilityNodeInfoCompatIcs.obtain(paramObject);
    }

    public void recycle(Object paramObject)
    {
      AccessibilityNodeInfoCompatIcs.recycle(paramObject);
    }

    public void setBoundsInParent(Object paramObject, Rect paramRect)
    {
      AccessibilityNodeInfoCompatIcs.setBoundsInParent(paramObject, paramRect);
    }

    public void setBoundsInScreen(Object paramObject, Rect paramRect)
    {
      AccessibilityNodeInfoCompatIcs.setBoundsInScreen(paramObject, paramRect);
    }

    public void setClassName(Object paramObject, CharSequence paramCharSequence)
    {
      AccessibilityNodeInfoCompatIcs.setClassName(paramObject, paramCharSequence);
    }

    public void setClickable(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setClickable(paramObject, paramBoolean);
    }

    public void setContentDescription(Object paramObject, CharSequence paramCharSequence)
    {
      AccessibilityNodeInfoCompatIcs.setContentDescription(paramObject, paramCharSequence);
    }

    public void setEnabled(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setEnabled(paramObject, paramBoolean);
    }

    public void setFocusable(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setFocusable(paramObject, paramBoolean);
    }

    public void setFocused(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setFocused(paramObject, paramBoolean);
    }

    public void setLongClickable(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setLongClickable(paramObject, paramBoolean);
    }

    public void setPackageName(Object paramObject, CharSequence paramCharSequence)
    {
      AccessibilityNodeInfoCompatIcs.setPackageName(paramObject, paramCharSequence);
    }

    public void setParent(Object paramObject, View paramView)
    {
      AccessibilityNodeInfoCompatIcs.setParent(paramObject, paramView);
    }

    public void setScrollable(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setScrollable(paramObject, paramBoolean);
    }

    public void setSelected(Object paramObject, boolean paramBoolean)
    {
      AccessibilityNodeInfoCompatIcs.setSelected(paramObject, paramBoolean);
    }

    public void setSource(Object paramObject, View paramView)
    {
      AccessibilityNodeInfoCompatIcs.setSource(paramObject, paramView);
    }
  }

  static class AccessibilityNodeInfoStubImpl
    implements AccessibilityNodeInfoCompat.AccessibilityNodeInfoImpl
  {
    public void addAction(Object paramObject, int paramInt)
    {
    }

    public void addChild(Object paramObject, View paramView)
    {
    }

    public int getActions(Object paramObject)
    {
      return 0;
    }

    public void getBoundsInParent(Object paramObject, Rect paramRect)
    {
    }

    public void getBoundsInScreen(Object paramObject, Rect paramRect)
    {
    }

    public CharSequence getClassName(Object paramObject)
    {
      return null;
    }

    public CharSequence getContentDescription(Object paramObject)
    {
      return null;
    }

    public int getMovementGranularities(Object paramObject)
    {
      return 0;
    }

    public CharSequence getPackageName(Object paramObject)
    {
      return null;
    }

    public CharSequence getText(Object paramObject)
    {
      return null;
    }

    public String getViewIdResourceName(Object paramObject)
    {
      return null;
    }

    public boolean isAccessibilityFocused(Object paramObject)
    {
      return false;
    }

    public boolean isCheckable(Object paramObject)
    {
      return false;
    }

    public boolean isChecked(Object paramObject)
    {
      return false;
    }

    public boolean isClickable(Object paramObject)
    {
      return false;
    }

    public boolean isEnabled(Object paramObject)
    {
      return false;
    }

    public boolean isFocusable(Object paramObject)
    {
      return false;
    }

    public boolean isFocused(Object paramObject)
    {
      return false;
    }

    public boolean isLongClickable(Object paramObject)
    {
      return false;
    }

    public boolean isPassword(Object paramObject)
    {
      return false;
    }

    public boolean isScrollable(Object paramObject)
    {
      return false;
    }

    public boolean isSelected(Object paramObject)
    {
      return false;
    }

    public boolean isVisibleToUser(Object paramObject)
    {
      return false;
    }

    public Object obtain(Object paramObject)
    {
      return null;
    }

    public void recycle(Object paramObject)
    {
    }

    public void setAccessibilityFocused(Object paramObject, boolean paramBoolean)
    {
    }

    public void setBoundsInParent(Object paramObject, Rect paramRect)
    {
    }

    public void setBoundsInScreen(Object paramObject, Rect paramRect)
    {
    }

    public void setClassName(Object paramObject, CharSequence paramCharSequence)
    {
    }

    public void setClickable(Object paramObject, boolean paramBoolean)
    {
    }

    public void setContentDescription(Object paramObject, CharSequence paramCharSequence)
    {
    }

    public void setEnabled(Object paramObject, boolean paramBoolean)
    {
    }

    public void setFocusable(Object paramObject, boolean paramBoolean)
    {
    }

    public void setFocused(Object paramObject, boolean paramBoolean)
    {
    }

    public void setLongClickable(Object paramObject, boolean paramBoolean)
    {
    }

    public void setMovementGranularities(Object paramObject, int paramInt)
    {
    }

    public void setPackageName(Object paramObject, CharSequence paramCharSequence)
    {
    }

    public void setParent(Object paramObject, View paramView)
    {
    }

    public void setScrollable(Object paramObject, boolean paramBoolean)
    {
    }

    public void setSelected(Object paramObject, boolean paramBoolean)
    {
    }

    public void setSource(Object paramObject, View paramView)
    {
    }

    public void setVisibleToUser(Object paramObject, boolean paramBoolean)
    {
    }
  }

  static abstract interface AccessibilityNodeInfoImpl
  {
    public abstract void addAction(Object paramObject, int paramInt);

    public abstract void addChild(Object paramObject, View paramView);

    public abstract int getActions(Object paramObject);

    public abstract void getBoundsInParent(Object paramObject, Rect paramRect);

    public abstract void getBoundsInScreen(Object paramObject, Rect paramRect);

    public abstract CharSequence getClassName(Object paramObject);

    public abstract CharSequence getContentDescription(Object paramObject);

    public abstract int getMovementGranularities(Object paramObject);

    public abstract CharSequence getPackageName(Object paramObject);

    public abstract CharSequence getText(Object paramObject);

    public abstract String getViewIdResourceName(Object paramObject);

    public abstract boolean isAccessibilityFocused(Object paramObject);

    public abstract boolean isCheckable(Object paramObject);

    public abstract boolean isChecked(Object paramObject);

    public abstract boolean isClickable(Object paramObject);

    public abstract boolean isEnabled(Object paramObject);

    public abstract boolean isFocusable(Object paramObject);

    public abstract boolean isFocused(Object paramObject);

    public abstract boolean isLongClickable(Object paramObject);

    public abstract boolean isPassword(Object paramObject);

    public abstract boolean isScrollable(Object paramObject);

    public abstract boolean isSelected(Object paramObject);

    public abstract boolean isVisibleToUser(Object paramObject);

    public abstract Object obtain(Object paramObject);

    public abstract void recycle(Object paramObject);

    public abstract void setAccessibilityFocused(Object paramObject, boolean paramBoolean);

    public abstract void setBoundsInParent(Object paramObject, Rect paramRect);

    public abstract void setBoundsInScreen(Object paramObject, Rect paramRect);

    public abstract void setClassName(Object paramObject, CharSequence paramCharSequence);

    public abstract void setClickable(Object paramObject, boolean paramBoolean);

    public abstract void setContentDescription(Object paramObject, CharSequence paramCharSequence);

    public abstract void setEnabled(Object paramObject, boolean paramBoolean);

    public abstract void setFocusable(Object paramObject, boolean paramBoolean);

    public abstract void setFocused(Object paramObject, boolean paramBoolean);

    public abstract void setLongClickable(Object paramObject, boolean paramBoolean);

    public abstract void setMovementGranularities(Object paramObject, int paramInt);

    public abstract void setPackageName(Object paramObject, CharSequence paramCharSequence);

    public abstract void setParent(Object paramObject, View paramView);

    public abstract void setScrollable(Object paramObject, boolean paramBoolean);

    public abstract void setSelected(Object paramObject, boolean paramBoolean);

    public abstract void setSource(Object paramObject, View paramView);

    public abstract void setVisibleToUser(Object paramObject, boolean paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityNodeInfoCompat
 * JD-Core Version:    0.6.2
 */