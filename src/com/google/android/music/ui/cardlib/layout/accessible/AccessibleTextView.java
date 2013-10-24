package com.google.android.music.ui.cardlib.layout.accessible;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;
import java.util.List;

public class AccessibleTextView extends TextView
{
  public AccessibleTextView(Context paramContext)
  {
    super(paramContext);
  }

  public AccessibleTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public AccessibleTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    if (!isShown());
    while (true)
    {
      return false;
      CharSequence localCharSequence = getText();
      if (TextUtils.isEmpty(localCharSequence))
        localCharSequence = getHint();
      if (!TextUtils.isEmpty(localCharSequence))
      {
        if (localCharSequence.length() > 500)
          localCharSequence = localCharSequence.subSequence(0, 501);
        List localList = paramAccessibilityEvent.getText();
        String str = localCharSequence.toString().toLowerCase();
        boolean bool = localList.add(str);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.accessible.AccessibleTextView
 * JD-Core Version:    0.6.2
 */