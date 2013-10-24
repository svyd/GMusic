package com.google.android.youtube.player.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

public final class n extends FrameLayout
{
  private final ProgressBar a;
  private final TextView b;

  public n(Context paramContext)
  {
    super(paramContext, null, i);
    m localm = new m(paramContext);
    setBackgroundColor(-16777216);
    ProgressBar localProgressBar1 = new ProgressBar(paramContext);
    this.a = localProgressBar1;
    this.a.setVisibility(8);
    ProgressBar localProgressBar2 = this.a;
    FrameLayout.LayoutParams localLayoutParams1 = new FrameLayout.LayoutParams(-1, -1, 17);
    addView(localProgressBar2, localLayoutParams1);
    float f = paramContext.getResources().getDisplayMetrics().density;
    int j = (int)(10.0F * f + 0.5F);
    TextView localTextView1 = new TextView(paramContext);
    this.b = localTextView1;
    this.b.setTextAppearance(paramContext, 16973894);
    this.b.setTextColor(-1);
    this.b.setVisibility(8);
    this.b.setPadding(j, j, j, j);
    this.b.setGravity(17);
    TextView localTextView2 = this.b;
    String str = localm.a;
    localTextView2.setText(str);
    TextView localTextView3 = this.b;
    FrameLayout.LayoutParams localLayoutParams2 = new FrameLayout.LayoutParams(-1, -1, 17);
    addView(localTextView3, localLayoutParams2);
  }

  public final void a()
  {
    this.a.setVisibility(8);
    this.b.setVisibility(8);
  }

  public final void b()
  {
    this.a.setVisibility(0);
    this.b.setVisibility(8);
  }

  public final void c()
  {
    this.a.setVisibility(8);
    this.b.setVisibility(0);
  }

  protected final void onMeasure(int paramInt1, int paramInt2)
  {
    int i = 0;
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int m = View.MeasureSpec.getSize(paramInt1);
    int n = View.MeasureSpec.getSize(paramInt2);
    if ((j == 1073741824) && (k == 1073741824))
      i = m;
    while (true)
    {
      int i1 = resolveSize(i, paramInt1);
      int i2 = resolveSize(n, paramInt2);
      int i3 = View.MeasureSpec.makeMeasureSpec(i1, 1073741824);
      int i4 = View.MeasureSpec.makeMeasureSpec(i2, 1073741824);
      super.onMeasure(i3, i4);
      return;
      if ((j == 1073741824) || ((j == -2147483648) && (k == 0)))
      {
        n = (int)(m / 1.777F);
        i = m;
      }
      else if ((k == 1073741824) || ((k == -2147483648) && (j == 0)))
      {
        i = (int)(n * 1.777F);
      }
      else if ((j == -2147483648) && (k == -2147483648))
      {
        float f1 = n;
        float f2 = m / 1.777F;
        if (f1 < f2)
        {
          i = (int)(n * 1.777F);
        }
        else
        {
          n = (int)(m / 1.777F);
          i = m;
        }
      }
      else
      {
        n = 0;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.youtube.player.internal.n
 * JD-Core Version:    0.6.2
 */