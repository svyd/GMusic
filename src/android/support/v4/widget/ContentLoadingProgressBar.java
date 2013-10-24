package android.support.v4.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ContentLoadingProgressBar extends ProgressBar
{
  private final Runnable mDelayedHide;
  private final Runnable mDelayedShow;
  private boolean mDismissed = false;
  private boolean mPostedHide = false;
  private boolean mPostedShow = false;
  private long mStartTime = 65535L;

  public ContentLoadingProgressBar(Context paramContext)
  {
    this(paramContext, null);
  }

  public ContentLoadingProgressBar(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet, 0);
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        boolean bool = ContentLoadingProgressBar.access$002(ContentLoadingProgressBar.this, false);
        long l = ContentLoadingProgressBar.access$102(ContentLoadingProgressBar.this, 65535L);
        ContentLoadingProgressBar.this.setVisibility(8);
      }
    };
    this.mDelayedHide = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        boolean bool = ContentLoadingProgressBar.access$202(ContentLoadingProgressBar.this, false);
        if (ContentLoadingProgressBar.this.mDismissed)
          return;
        ContentLoadingProgressBar localContentLoadingProgressBar = ContentLoadingProgressBar.this;
        long l1 = System.currentTimeMillis();
        long l2 = ContentLoadingProgressBar.access$102(localContentLoadingProgressBar, l1);
        ContentLoadingProgressBar.this.setVisibility(0);
      }
    };
    this.mDelayedShow = local2;
  }

  private void removeCallbacks()
  {
    Runnable localRunnable1 = this.mDelayedHide;
    boolean bool1 = removeCallbacks(localRunnable1);
    Runnable localRunnable2 = this.mDelayedShow;
    boolean bool2 = removeCallbacks(localRunnable2);
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    removeCallbacks();
  }

  public void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    removeCallbacks();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.widget.ContentLoadingProgressBar
 * JD-Core Version:    0.6.2
 */