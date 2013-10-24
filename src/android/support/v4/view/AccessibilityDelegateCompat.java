package android.support.v4.view;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityDelegateCompat
{
  private static final Object DEFAULT_DELEGATE;
  private static final AccessibilityDelegateImpl IMPL;
  final Object mBridge;

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
      IMPL = new AccessibilityDelegateJellyBeanImpl();
    while (true)
    {
      DEFAULT_DELEGATE = IMPL.newAccessiblityDelegateDefaultImpl();
      return;
      if (Build.VERSION.SDK_INT >= 14)
        IMPL = new AccessibilityDelegateIcsImpl();
      else
        IMPL = new AccessibilityDelegateStubImpl();
    }
  }

  public AccessibilityDelegateCompat()
  {
    Object localObject = IMPL.newAccessiblityDelegateBridge(this);
    this.mBridge = localObject;
  }

  public boolean dispatchPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    return localAccessibilityDelegateImpl.dispatchPopulateAccessibilityEvent(localObject, paramView, paramAccessibilityEvent);
  }

  public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View paramView)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    return localAccessibilityDelegateImpl.getAccessibilityNodeProvider(localObject, paramView);
  }

  Object getBridge()
  {
    return this.mBridge;
  }

  public void onInitializeAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    localAccessibilityDelegateImpl.onInitializeAccessibilityEvent(localObject, paramView, paramAccessibilityEvent);
  }

  public void onInitializeAccessibilityNodeInfo(View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    localAccessibilityDelegateImpl.onInitializeAccessibilityNodeInfo(localObject, paramView, paramAccessibilityNodeInfoCompat);
  }

  public void onPopulateAccessibilityEvent(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    localAccessibilityDelegateImpl.onPopulateAccessibilityEvent(localObject, paramView, paramAccessibilityEvent);
  }

  public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    return localAccessibilityDelegateImpl.onRequestSendAccessibilityEvent(localObject, paramViewGroup, paramView, paramAccessibilityEvent);
  }

  public boolean performAccessibilityAction(View paramView, int paramInt, Bundle paramBundle)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    return localAccessibilityDelegateImpl.performAccessibilityAction(localObject, paramView, paramInt, paramBundle);
  }

  public void sendAccessibilityEvent(View paramView, int paramInt)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    localAccessibilityDelegateImpl.sendAccessibilityEvent(localObject, paramView, paramInt);
  }

  public void sendAccessibilityEventUnchecked(View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    AccessibilityDelegateImpl localAccessibilityDelegateImpl = IMPL;
    Object localObject = DEFAULT_DELEGATE;
    localAccessibilityDelegateImpl.sendAccessibilityEventUnchecked(localObject, paramView, paramAccessibilityEvent);
  }

  static class AccessibilityDelegateJellyBeanImpl extends AccessibilityDelegateCompat.AccessibilityDelegateIcsImpl
  {
    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object paramObject, View paramView)
    {
      Object localObject = AccessibilityDelegateCompatJellyBean.getAccessibilityNodeProvider(paramObject, paramView);
      if (localObject != null);
      for (AccessibilityNodeProviderCompat localAccessibilityNodeProviderCompat = new AccessibilityNodeProviderCompat(localObject); ; localAccessibilityNodeProviderCompat = null)
        return localAccessibilityNodeProviderCompat;
    }

    public Object newAccessiblityDelegateBridge(final AccessibilityDelegateCompat paramAccessibilityDelegateCompat)
    {
      return AccessibilityDelegateCompatJellyBean.newAccessibilityDelegateBridge(new AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean()
      {
        public boolean dispatchPopulateAccessibilityEvent(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          return paramAccessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public Object getAccessibilityNodeProvider(View paramAnonymousView)
        {
          AccessibilityNodeProviderCompat localAccessibilityNodeProviderCompat = paramAccessibilityDelegateCompat.getAccessibilityNodeProvider(paramAnonymousView);
          if (localAccessibilityNodeProviderCompat != null);
          for (Object localObject = localAccessibilityNodeProviderCompat.getProvider(); ; localObject = null)
            return localObject;
        }

        public void onInitializeAccessibilityEvent(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          paramAccessibilityDelegateCompat.onInitializeAccessibilityEvent(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public void onInitializeAccessibilityNodeInfo(View paramAnonymousView, Object paramAnonymousObject)
        {
          AccessibilityDelegateCompat localAccessibilityDelegateCompat = paramAccessibilityDelegateCompat;
          AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = new AccessibilityNodeInfoCompat(paramAnonymousObject);
          localAccessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(paramAnonymousView, localAccessibilityNodeInfoCompat);
        }

        public void onPopulateAccessibilityEvent(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          paramAccessibilityDelegateCompat.onPopulateAccessibilityEvent(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup paramAnonymousViewGroup, View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          return paramAccessibilityDelegateCompat.onRequestSendAccessibilityEvent(paramAnonymousViewGroup, paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public boolean performAccessibilityAction(View paramAnonymousView, int paramAnonymousInt, Bundle paramAnonymousBundle)
        {
          return paramAccessibilityDelegateCompat.performAccessibilityAction(paramAnonymousView, paramAnonymousInt, paramAnonymousBundle);
        }

        public void sendAccessibilityEvent(View paramAnonymousView, int paramAnonymousInt)
        {
          paramAccessibilityDelegateCompat.sendAccessibilityEvent(paramAnonymousView, paramAnonymousInt);
        }

        public void sendAccessibilityEventUnchecked(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          paramAccessibilityDelegateCompat.sendAccessibilityEventUnchecked(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }
      });
    }

    public boolean performAccessibilityAction(Object paramObject, View paramView, int paramInt, Bundle paramBundle)
    {
      return AccessibilityDelegateCompatJellyBean.performAccessibilityAction(paramObject, paramView, paramInt, paramBundle);
    }
  }

  static class AccessibilityDelegateIcsImpl extends AccessibilityDelegateCompat.AccessibilityDelegateStubImpl
  {
    public boolean dispatchPopulateAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return AccessibilityDelegateCompatIcs.dispatchPopulateAccessibilityEvent(paramObject, paramView, paramAccessibilityEvent);
    }

    public Object newAccessiblityDelegateBridge(final AccessibilityDelegateCompat paramAccessibilityDelegateCompat)
    {
      return AccessibilityDelegateCompatIcs.newAccessibilityDelegateBridge(new AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge()
      {
        public boolean dispatchPopulateAccessibilityEvent(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          return paramAccessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public void onInitializeAccessibilityEvent(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          paramAccessibilityDelegateCompat.onInitializeAccessibilityEvent(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public void onInitializeAccessibilityNodeInfo(View paramAnonymousView, Object paramAnonymousObject)
        {
          AccessibilityDelegateCompat localAccessibilityDelegateCompat = paramAccessibilityDelegateCompat;
          AccessibilityNodeInfoCompat localAccessibilityNodeInfoCompat = new AccessibilityNodeInfoCompat(paramAnonymousObject);
          localAccessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(paramAnonymousView, localAccessibilityNodeInfoCompat);
        }

        public void onPopulateAccessibilityEvent(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          paramAccessibilityDelegateCompat.onPopulateAccessibilityEvent(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public boolean onRequestSendAccessibilityEvent(ViewGroup paramAnonymousViewGroup, View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          return paramAccessibilityDelegateCompat.onRequestSendAccessibilityEvent(paramAnonymousViewGroup, paramAnonymousView, paramAnonymousAccessibilityEvent);
        }

        public void sendAccessibilityEvent(View paramAnonymousView, int paramAnonymousInt)
        {
          paramAccessibilityDelegateCompat.sendAccessibilityEvent(paramAnonymousView, paramAnonymousInt);
        }

        public void sendAccessibilityEventUnchecked(View paramAnonymousView, AccessibilityEvent paramAnonymousAccessibilityEvent)
        {
          paramAccessibilityDelegateCompat.sendAccessibilityEventUnchecked(paramAnonymousView, paramAnonymousAccessibilityEvent);
        }
      });
    }

    public Object newAccessiblityDelegateDefaultImpl()
    {
      return AccessibilityDelegateCompatIcs.newAccessibilityDelegateDefaultImpl();
    }

    public void onInitializeAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      AccessibilityDelegateCompatIcs.onInitializeAccessibilityEvent(paramObject, paramView, paramAccessibilityEvent);
    }

    public void onInitializeAccessibilityNodeInfo(Object paramObject, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
      Object localObject = paramAccessibilityNodeInfoCompat.getInfo();
      AccessibilityDelegateCompatIcs.onInitializeAccessibilityNodeInfo(paramObject, paramView, localObject);
    }

    public void onPopulateAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      AccessibilityDelegateCompatIcs.onPopulateAccessibilityEvent(paramObject, paramView, paramAccessibilityEvent);
    }

    public boolean onRequestSendAccessibilityEvent(Object paramObject, ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return AccessibilityDelegateCompatIcs.onRequestSendAccessibilityEvent(paramObject, paramViewGroup, paramView, paramAccessibilityEvent);
    }

    public void sendAccessibilityEvent(Object paramObject, View paramView, int paramInt)
    {
      AccessibilityDelegateCompatIcs.sendAccessibilityEvent(paramObject, paramView, paramInt);
    }

    public void sendAccessibilityEventUnchecked(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      AccessibilityDelegateCompatIcs.sendAccessibilityEventUnchecked(paramObject, paramView, paramAccessibilityEvent);
    }
  }

  static class AccessibilityDelegateStubImpl
    implements AccessibilityDelegateCompat.AccessibilityDelegateImpl
  {
    public boolean dispatchPopulateAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return false;
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object paramObject, View paramView)
    {
      return null;
    }

    public Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat paramAccessibilityDelegateCompat)
    {
      return null;
    }

    public Object newAccessiblityDelegateDefaultImpl()
    {
      return null;
    }

    public void onInitializeAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
    }

    public void onInitializeAccessibilityNodeInfo(Object paramObject, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat)
    {
    }

    public void onPopulateAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
    }

    public boolean onRequestSendAccessibilityEvent(Object paramObject, ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return true;
    }

    public boolean performAccessibilityAction(Object paramObject, View paramView, int paramInt, Bundle paramBundle)
    {
      return false;
    }

    public void sendAccessibilityEvent(Object paramObject, View paramView, int paramInt)
    {
    }

    public void sendAccessibilityEventUnchecked(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
    }
  }

  static abstract interface AccessibilityDelegateImpl
  {
    public abstract boolean dispatchPopulateAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object paramObject, View paramView);

    public abstract Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat paramAccessibilityDelegateCompat);

    public abstract Object newAccessiblityDelegateDefaultImpl();

    public abstract void onInitializeAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract void onInitializeAccessibilityNodeInfo(Object paramObject, View paramView, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat);

    public abstract void onPopulateAccessibilityEvent(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract boolean onRequestSendAccessibilityEvent(Object paramObject, ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent);

    public abstract boolean performAccessibilityAction(Object paramObject, View paramView, int paramInt, Bundle paramBundle);

    public abstract void sendAccessibilityEvent(Object paramObject, View paramView, int paramInt);

    public abstract void sendAccessibilityEventUnchecked(Object paramObject, View paramView, AccessibilityEvent paramAccessibilityEvent);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.AccessibilityDelegateCompat
 * JD-Core Version:    0.6.2
 */