package com.google.android.music;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.widget.ImageView;
import com.google.android.music.widgets.DrawableStateChangeListener;

public class PassClickImageView extends ImageView
  implements View.OnClickListener, DrawableStateChangeListener
{
  private View mClickReceiver = null;
  private final int mClickReceiverId;
  private boolean mSearchedForClickReceiver = false;

  public PassClickImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int[] arrayOfInt = R.styleable.PassClick;
    int i = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt).getResourceId(0, -1);
    this.mClickReceiverId = i;
    if (this.mClickReceiverId == -1)
      throw new IllegalArgumentException("PassClickImageView must be given a click receiver");
    setOnClickListener(this);
  }

  private View getClickReceiver()
  {
    ViewParent localViewParent = getParent();
    View localView2;
    if ((localViewParent != null) && ((localViewParent instanceof View)))
    {
      View localView1 = (View)localViewParent;
      int i = this.mClickReceiverId;
      localView2 = localView1.findViewById(i);
      if (localView2 == null);
    }
    while (true)
    {
      return localView2;
      localViewParent = localViewParent.getParent();
      break;
      localView2 = null;
    }
  }

  public void onClick(View paramView)
  {
    if (!this.mSearchedForClickReceiver)
    {
      View localView = getClickReceiver();
      this.mClickReceiver = localView;
      this.mSearchedForClickReceiver = true;
    }
    if (this.mClickReceiver == null)
      return;
    if (!this.mClickReceiver.isEnabled())
      return;
    if (this.mClickReceiver.getVisibility() != 0)
      return;
    boolean bool = this.mClickReceiver.performClick();
  }

  public void onDrawableStateChanged(int[] paramArrayOfInt)
  {
    Drawable localDrawable = getDrawable();
    if (localDrawable == null)
      return;
    if (!localDrawable.isStateful())
      return;
    boolean bool = localDrawable.setState(paramArrayOfInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.PassClickImageView
 * JD-Core Version:    0.6.2
 */