package com.google.android.music.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import com.google.android.music.utils.MusicUtils;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;

public class MultipleListenerViewPager extends ViewPager
{
  private ViewPager.OnPageChangeListener mPageChangeListener;
  private LinkedList<ViewPager.OnPageChangeListener> mPageChangeListeners;

  public MultipleListenerViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    LinkedList localLinkedList = Lists.newLinkedList();
    this.mPageChangeListeners = localLinkedList;
    ViewPager.OnPageChangeListener local1 = new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt)
      {
        Iterator localIterator = MultipleListenerViewPager.this.mPageChangeListeners.iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            return;
          ((ViewPager.OnPageChangeListener)localIterator.next()).onPageScrollStateChanged(paramAnonymousInt);
        }
      }

      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
      {
        Iterator localIterator = MultipleListenerViewPager.this.mPageChangeListeners.iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            return;
          ((ViewPager.OnPageChangeListener)localIterator.next()).onPageScrolled(paramAnonymousInt1, paramAnonymousFloat, paramAnonymousInt2);
        }
      }

      public void onPageSelected(int paramAnonymousInt)
      {
        Iterator localIterator = MultipleListenerViewPager.this.mPageChangeListeners.iterator();
        while (true)
        {
          if (!localIterator.hasNext())
            return;
          ((ViewPager.OnPageChangeListener)localIterator.next()).onPageSelected(paramAnonymousInt);
        }
      }
    };
    this.mPageChangeListener = local1;
    ViewPager.OnPageChangeListener localOnPageChangeListener = this.mPageChangeListener;
    super.setOnPageChangeListener(localOnPageChangeListener);
  }

  public void addOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener)
  {
    MusicUtils.assertUiThread();
    boolean bool = this.mPageChangeListeners.add(paramOnPageChangeListener);
  }

  public void removeOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener)
  {
    MusicUtils.assertUiThread();
    boolean bool = this.mPageChangeListeners.remove(paramOnPageChangeListener);
  }

  public void setOnPageChangeListener(ViewPager.OnPageChangeListener paramOnPageChangeListener)
  {
    throw new UnsupportedOperationException("setOnPageChangeListener not supported in MultipleListenerViewPager.  Use addOnPageChangeListener");
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.MultipleListenerViewPager
 * JD-Core Version:    0.6.2
 */