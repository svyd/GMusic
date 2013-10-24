package com.google.android.music.ui.cardlib.layout.accessible;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import java.util.List;

public class AccessibleFrameLayout extends FrameLayout
{
  public AccessibleFrameLayout(Context paramContext)
  {
    super(paramContext);
  }

  public AccessibleFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    boolean bool = super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    paramAccessibilityEvent.getText().clear();
    return bool;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.accessible.AccessibleFrameLayout
 * JD-Core Version:    0.6.2
 */