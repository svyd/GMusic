package com.google.android.music;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class ScrollViewFillingLayout extends RelativeLayout
{
  public ScrollViewFillingLayout(Context paramContext)
  {
    super(paramContext);
  }

  public ScrollViewFillingLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    ViewParent localViewParent = getParent();
    if ((localViewParent instanceof ScrollView))
    {
      paramInt2 = ((ScrollView)localViewParent).getHeight();
      setMinimumHeight(paramInt2);
    }
    super.onMeasure(paramInt1, paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ScrollViewFillingLayout
 * JD-Core Version:    0.6.2
 */