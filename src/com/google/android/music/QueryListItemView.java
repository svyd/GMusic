package com.google.android.music;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView.SelectionBoundsAdjuster;
import android.widget.FrameLayout;

public class QueryListItemView extends FrameLayout
  implements AbsListView.SelectionBoundsAdjuster
{
  public QueryListItemView(Context paramContext)
  {
    super(paramContext);
  }

  public QueryListItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void adjustListItemSelectionBounds(Rect paramRect)
  {
    View localView = findViewById(2131296547);
    if (localView == null)
      return;
    if (localView.getVisibility() != 0)
      return;
    int i = paramRect.top;
    int j = localView.getHeight();
    int k = i + j;
    paramRect.top = k;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.QueryListItemView
 * JD-Core Version:    0.6.2
 */