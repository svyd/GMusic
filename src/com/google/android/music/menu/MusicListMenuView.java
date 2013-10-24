package com.google.android.music.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;

public class MusicListMenuView extends LinearLayout
{
  private Drawable mDivider;
  private int mDividerHeight;
  private boolean mHasStaleChildren = true;
  private MusicListMenu mMenu;
  private LinearLayout mSubView;
  private ScrollView mSubViewScrollContainer;
  private TextView mTitleView;
  private boolean mUseDividers = false;

  public MusicListMenuView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setWillNotDraw(false);
    setFocusableInTouchMode(true);
    setDescendantFocusability(262144);
  }

  private void addItemView(MusicListMenuItemView paramMusicListMenuItemView)
  {
    if (paramMusicListMenuItemView.getParent() != null)
    {
      String str1 = "The itemView already has a parent, need to remove this parent. " + paramMusicListMenuItemView;
      int i = Log.v("MusicListMenuView", str1);
      if ((paramMusicListMenuItemView.getParent() instanceof ViewGroup))
        ((ViewGroup)paramMusicListMenuItemView.getParent()).removeView(paramMusicListMenuItemView);
    }
    else
    {
      this.mSubView.addView(paramMusicListMenuItemView);
      return;
    }
    String str2 = "Failed to add an itemView since its parent is not a ViewGroup " + paramMusicListMenuItemView;
    int j = Log.e("MusicListMenuView", str2);
  }

  private void updateChildren()
  {
    ArrayList localArrayList = this.mMenu.getVisibleItems();
    int i = localArrayList.size();
    this.mSubView.removeAllViews();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      MusicListMenuItemView localMusicListMenuItemView = (MusicListMenuItemView)((MusicListMenuItem)localArrayList.get(j)).getItemView(this);
      MusicListMenu localMusicListMenu = this.mMenu;
      localMusicListMenuItemView.setOnKeyListener(localMusicListMenu);
      addItemView(localMusicListMenuItemView);
      j += 1;
    }
  }

  protected void onDraw(Canvas paramCanvas)
  {
    if (!this.mUseDividers)
      return;
    int i = this.mSubViewScrollContainer.getScrollY();
    int j = 1;
    while (true)
    {
      int k = this.mSubView.getChildCount();
      if (j >= k)
        return;
      View localView = this.mSubView.getChildAt(j);
      int m = localView.getTop() - i;
      int n = getWidth();
      int i1 = localView.getTop();
      int i2 = this.mDividerHeight;
      int i3 = i1 + i2 - i;
      Rect localRect = new Rect(0, m, n, i3);
      this.mDivider.setBounds(localRect);
      this.mDivider.draw(paramCanvas);
      j += 1;
    }
  }

  protected void onFinishInflate()
  {
    LinearLayout localLinearLayout = (LinearLayout)findViewById(2131296424);
    this.mSubView = localLinearLayout;
    ScrollView localScrollView = (ScrollView)findViewById(2131296423);
    this.mSubViewScrollContainer = localScrollView;
    TextView localTextView = (TextView)findViewById(2131296326);
    this.mTitleView = localTextView;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    if (this.mHasStaleChildren)
    {
      this.mHasStaleChildren = false;
      updateChildren();
    }
    super.onMeasure(paramInt1, paramInt2);
  }

  public void sendAccessibilityEvent(int paramInt)
  {
    if (paramInt != 32)
      return;
    super.sendAccessibilityEvent(paramInt);
  }

  public void setDividerDrawable(Drawable paramDrawable)
  {
    this.mDivider = paramDrawable;
    if (this.mDivider != null)
    {
      int i = this.mDivider.getIntrinsicHeight();
      this.mDividerHeight = i;
      if (this.mDividerHeight != -1)
        return;
      this.mDividerHeight = 1;
      return;
    }
    this.mDividerHeight = 0;
  }

  public void setHeader(CharSequence paramCharSequence)
  {
    if (this.mTitleView == null)
      return;
    this.mTitleView.setText(paramCharSequence);
  }

  public void setUseDividers(boolean paramBoolean)
  {
    if ((paramBoolean) && (this.mDivider == null))
    {
      Drawable localDrawable = getContext().getResources().getDrawable(2130837799);
      setDividerDrawable(localDrawable);
    }
    this.mUseDividers = paramBoolean;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicListMenuView
 * JD-Core Version:    0.6.2
 */