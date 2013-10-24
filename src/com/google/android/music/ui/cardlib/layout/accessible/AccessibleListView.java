package com.google.android.music.ui.cardlib.layout.accessible;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;
import java.util.List;

public class AccessibleListView extends ListView
{
  public AccessibleListView(Context paramContext)
  {
    super(paramContext);
  }

  public AccessibleListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public AccessibleListView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    boolean bool1 = super.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    View localView1 = getSelectedView();
    View localView2;
    if (localView1 != null)
    {
      localView2 = localView1.findFocus();
      if (localView2 != null);
    }
    for (boolean bool2 = bool1; ; bool2 = bool1)
    {
      return bool2;
      paramAccessibilityEvent.getText().clear();
      bool1 = localView2.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.accessible.AccessibleListView
 * JD-Core Version:    0.6.2
 */