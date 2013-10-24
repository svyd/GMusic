package com.google.android.music;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import com.google.android.gsf.Gservices;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.TreeSet;

public class MusicExpandableListView extends ExpandableListView
  implements ExpandableListView.OnGroupCollapseListener, ExpandableListView.OnGroupExpandListener
{
  boolean mDrawingFix;
  private Set<Integer> mExpandedGroups;

  public MusicExpandableListView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    TreeSet localTreeSet = Sets.newTreeSet();
    this.mExpandedGroups = localTreeSet;
    boolean bool = Gservices.getBoolean(paramContext.getContentResolver(), "music_overdraw_explistview", true);
    this.mDrawingFix = bool;
    setOnGroupCollapseListener(this);
    setOnGroupExpandListener(this);
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

  public void onGroupCollapse(int paramInt)
  {
    Set localSet = this.mExpandedGroups;
    Integer localInteger = Integer.valueOf(paramInt);
    boolean bool = localSet.remove(localInteger);
  }

  public void onGroupExpand(int paramInt)
  {
    Set localSet = this.mExpandedGroups;
    Integer localInteger = Integer.valueOf(paramInt);
    boolean bool = localSet.add(localInteger);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.MusicExpandableListView
 * JD-Core Version:    0.6.2
 */