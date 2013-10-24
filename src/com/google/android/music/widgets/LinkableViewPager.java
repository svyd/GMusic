package com.google.android.music.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LinkableViewPager extends ViewPager
{
  private boolean mForwardingToChild;
  private boolean mIsFakeDragging;
  private float mOriginalDownX;
  private float mOriginalDownY;
  private final float mTouchSlop;
  private List<LinkableViewPager> mViewPagers;

  public LinkableViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LinkedList localLinkedList = new LinkedList();
    this.mViewPagers = localLinkedList;
    this.mForwardingToChild = false;
    this.mIsFakeDragging = false;
    float f = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    this.mTouchSlop = f;
  }

  private boolean superOnTouchEvent(MotionEvent paramMotionEvent)
  {
    if (paramMotionEvent.getAction() == 0);
    for (this.mIsFakeDragging = true; ; this.mIsFakeDragging = false)
      do
        return super.onTouchEvent(paramMotionEvent);
      while ((paramMotionEvent.getAction() != 1) && (paramMotionEvent.getAction() != 3));
  }

  private void superSetCurrentItem(int paramInt)
  {
    super.setCurrentItem(paramInt);
  }

  private void superSetCurrentItem(int paramInt, boolean paramBoolean)
  {
    super.setCurrentItem(paramInt, paramBoolean);
  }

  public void link(LinkableViewPager paramLinkableViewPager)
  {
    boolean bool1 = paramLinkableViewPager.mViewPagers.add(this);
    boolean bool2 = this.mViewPagers.add(paramLinkableViewPager);
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = true;
    if (this.mIsFakeDragging)
      return bool1;
    Iterator localIterator = this.mViewPagers.iterator();
    while (localIterator.hasNext())
    {
      LinkableViewPager localLinkableViewPager = (LinkableViewPager)localIterator.next();
      if (localLinkableViewPager.getVisibility() == 0)
        boolean bool2 = localLinkableViewPager.superOnTouchEvent(paramMotionEvent);
    }
    label96: float f5;
    int i;
    if (paramMotionEvent.getAction() == 0)
    {
      this.mForwardingToChild = true;
      float f1 = paramMotionEvent.getX();
      this.mOriginalDownX = f1;
      float f2 = paramMotionEvent.getY();
      this.mOriginalDownY = f2;
      if (this.mForwardingToChild)
      {
        float f3 = paramMotionEvent.getX();
        float f4 = getScrollX();
        f5 = f3 + f4;
        i = 0;
      }
    }
    while (true)
    {
      int j = getChildCount();
      if (i < j)
      {
        View localView = getChildAt(i);
        if ((localView.getLeft() < f5) && (localView.getRight() > f5))
          boolean bool3 = localView.dispatchTouchEvent(paramMotionEvent);
      }
      else
      {
        bool1 = super.onTouchEvent(paramMotionEvent);
        break;
        if (paramMotionEvent.getAction() == 2)
        {
          float f6 = paramMotionEvent.getY();
          float f7 = this.mOriginalDownY;
          float f8 = Math.abs(f6 - f7);
          float f9 = this.mTouchSlop;
          if (f8 <= f9)
          {
            float f10 = paramMotionEvent.getX();
            float f11 = this.mOriginalDownX;
            float f12 = Math.abs(f10 - f11);
            float f13 = this.mTouchSlop;
            if (f12 <= f13);
          }
          else
          {
            this.mForwardingToChild = false;
            break label96;
          }
        }
        if (paramMotionEvent.getAction() != 3)
          break label96;
        this.mForwardingToChild = false;
        break label96;
      }
      i += 1;
    }
  }

  public void setCurrentItem(int paramInt)
  {
    Iterator localIterator = this.mViewPagers.iterator();
    while (localIterator.hasNext())
      ((LinkableViewPager)localIterator.next()).superSetCurrentItem(paramInt);
    super.setCurrentItem(paramInt);
  }

  public void setCurrentItem(int paramInt, boolean paramBoolean)
  {
    Iterator localIterator = this.mViewPagers.iterator();
    while (localIterator.hasNext())
      ((LinkableViewPager)localIterator.next()).superSetCurrentItem(paramInt, paramBoolean);
    super.setCurrentItem(paramInt, paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.LinkableViewPager
 * JD-Core Version:    0.6.2
 */