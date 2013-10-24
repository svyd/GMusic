package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class PlayTabContainer extends HorizontalScrollView
  implements ViewPager.OnPageChangeListener
{
  private int mLastScrollTo;
  private int mScrollState;
  private PlayTabStrip mTabStrip;
  private final int mTitleOffset;
  private ViewPager mViewPager;

  public PlayTabContainer(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayTabContainer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setupBackground();
    setHorizontalScrollBarEnabled(false);
    int i = paramContext.getResources().getDimensionPixelSize(2131558489);
    this.mTitleOffset = i;
  }

  private void scrollToChild(int paramInt1, int paramInt2)
  {
    if (this.mTabStrip.getChildCount() == 0)
      return;
    int i = this.mTabStrip.getChildAt(paramInt1).getLeft() + paramInt2;
    if ((paramInt1 > 0) || (paramInt2 > 0))
    {
      int j = this.mTitleOffset;
      i -= j;
    }
    int k = this.mLastScrollTo;
    if (i != k)
      return;
    this.mLastScrollTo = i;
    scrollTo(i, 0);
  }

  private void setupBackground()
  {
    BitmapDrawable localBitmapDrawable = (BitmapDrawable)getResources().getDrawable(2130837854);
    Shader.TileMode localTileMode1 = Shader.TileMode.REPEAT;
    Shader.TileMode localTileMode2 = Shader.TileMode.REPEAT;
    localBitmapDrawable.setTileModeXY(localTileMode1, localTileMode2);
    setBackgroundDrawable(localBitmapDrawable);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    PlayTabStrip localPlayTabStrip = (PlayTabStrip)findViewById(2131296461);
    this.mTabStrip = localPlayTabStrip;
  }

  public void onPageScrollStateChanged(int paramInt)
  {
    this.mScrollState = paramInt;
  }

  public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2)
  {
    if (this.mTabStrip.getChildCount() == 0)
      return;
    this.mTabStrip.onPageScrolled(paramInt1, paramFloat, paramInt2);
    int i = (int)(this.mTabStrip.getChildAt(paramInt1).getWidth() * paramFloat);
    scrollToChild(paramInt1, i);
  }

  public void onPageSelected(int paramInt)
  {
    if (this.mScrollState != 0)
      return;
    this.mTabStrip.onPageSelected(paramInt);
    scrollToChild(paramInt, 0);
  }

  public void setSelectedIndicatorColor(int paramInt)
  {
    this.mTabStrip.setSelectedIndicatorColor(paramInt);
  }

  public void setViewPager(ViewPager paramViewPager)
  {
    this.mViewPager = paramViewPager;
    PagerAdapter localPagerAdapter = this.mViewPager.getAdapter();
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    int i = 0;
    while (true)
    {
      int j = localPagerAdapter.getCount();
      if (i >= j)
        break;
      PlayTabStrip localPlayTabStrip = this.mTabStrip;
      TextView localTextView = (TextView)localLayoutInflater.inflate(2130968662, localPlayTabStrip, false);
      CharSequence localCharSequence = localPagerAdapter.getPageTitle(i);
      localTextView.setText(localCharSequence);
      final int k = i;
      View.OnClickListener local1 = new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          ViewPager localViewPager = PlayTabContainer.this.mViewPager;
          int i = k;
          localViewPager.setCurrentItem(i);
        }
      };
      localTextView.setOnClickListener(local1);
      this.mTabStrip.addView(localTextView);
      i += 1;
    }
    ViewTreeObserver localViewTreeObserver = this.mTabStrip.getViewTreeObserver();
    ViewTreeObserver.OnGlobalLayoutListener local2 = new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        PlayTabContainer localPlayTabContainer = PlayTabContainer.this;
        int i = PlayTabContainer.this.mViewPager.getCurrentItem();
        localPlayTabContainer.scrollToChild(i, 0);
        PlayTabContainer.this.mTabStrip.getViewTreeObserver().removeGlobalOnLayoutListener(this);
      }
    };
    localViewTreeObserver.addOnGlobalLayoutListener(local2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.PlayTabContainer
 * JD-Core Version:    0.6.2
 */