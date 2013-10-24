package com.google.android.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import com.google.android.gsf.Gservices;
import com.google.android.music.utils.MusicUtils;
import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.List;

public class MusicListView extends ListView
{
  boolean mDrawingFix = true;
  private List<View> mHeaderViews;

  public MusicListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LinkedList localLinkedList = Lists.newLinkedList();
    this.mHeaderViews = localLinkedList;
    boolean bool = Gservices.getBoolean(paramContext.getContentResolver(), "music_overdraw_listview", true);
    this.mDrawingFix = bool;
  }

  public void addHeaderView(View paramView)
  {
    super.addHeaderView(paramView);
    boolean bool = this.mHeaderViews.add(paramView);
  }

  public void addHeaderView(View paramView, Object paramObject, boolean paramBoolean)
  {
    super.addHeaderView(paramView, paramObject, paramBoolean);
    boolean bool = this.mHeaderViews.add(paramView);
  }

  public void draw(Canvas paramCanvas)
  {
    if (this.mDrawingFix)
    {
      Drawable localDrawable = getBackground();
      if (localDrawable != null)
        localDrawable.draw(paramCanvas);
      int i = getChildCount() + -1;
      View localView = getChildAt(i);
      if (localView == null)
        return;
      Rect localRect1 = new Rect();
      getDrawingRect(localRect1);
      int j = localView.getBottom();
      localRect1.bottom = j;
      Rect localRect2 = paramCanvas.getClipBounds();
      boolean bool1 = localRect2.intersect(localRect1);
      if (!localRect2.isEmpty())
      {
        localDrawable = getDivider();
        if (localDrawable != null)
        {
          Rect localRect3 = localDrawable.getBounds();
          int k = localRect3.left;
          localRect3.offsetTo(k, j);
          localDrawable.setBounds(localRect3);
          localDrawable.draw(paramCanvas);
        }
        boolean bool2 = paramCanvas.clipRect(localRect2);
      }
      super.draw(paramCanvas);
      return;
    }
    super.draw(paramCanvas);
  }

  public void invalidateViews()
  {
    MusicUtils.assertUiThread();
    super.invalidateViews();
  }

  public boolean isHeaderView(View paramView)
  {
    return this.mHeaderViews.contains(paramView);
  }

  protected void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    if (paramBoolean)
    {
      int i = getFirstVisiblePosition();
      int j = getChildCount();
      int[] arrayOfInt = new int[j];
      int k = 0;
      if (k < j)
      {
        View localView = getChildAt(k);
        if (localView == null);
        for (int m = 0; ; m = localView.getTop())
        {
          arrayOfInt[k] = m;
          k += 1;
          break;
        }
      }
      super.onFocusChanged(paramBoolean, paramInt, paramRect);
      int n = getSelectedItemPosition();
      int i1 = n - i;
      if (i1 < 0)
        return;
      if (i1 >= j)
        return;
      int i2 = arrayOfInt[i1];
      setSelectionFromTop(n, i2);
      return;
    }
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.MusicListView
 * JD-Core Version:    0.6.2
 */