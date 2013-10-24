package com.google.android.music.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import com.google.android.music.R.styleable;

public class PassStateChangeTextView extends TextView
{
  private DrawableStateChangeListener mStateChangeListener = null;
  private final int mStateChangeListenerId;
  private boolean mStateChangeListeneredSearchedFor = false;

  public PassStateChangeTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int[] arrayOfInt = R.styleable.PassClick;
    int i = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt).getResourceId(1, -1);
    this.mStateChangeListenerId = i;
    if (this.mStateChangeListenerId != -1)
      return;
    StringBuilder localStringBuilder = new StringBuilder();
    String str1 = getClass().getSimpleName();
    String str2 = str1 + " must be given a passDrawableStateChange";
    throw new IllegalArgumentException(str2);
  }

  private DrawableStateChangeListener findStateChangeListener()
  {
    ViewParent localViewParent = getParent();
    View localView1;
    View localView2;
    if ((localViewParent != null) && ((localViewParent instanceof View)))
    {
      localView1 = (View)localViewParent;
      int i = this.mStateChangeListenerId;
      localView2 = localView1.findViewById(i);
      if (localView2 != null)
        if (!(localView2 instanceof DrawableStateChangeListener))
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Provided view must be an instance of ");
          String str1 = DrawableStateChangeListener.class.getSimpleName();
          String str2 = str1;
          throw new IllegalArgumentException(str2);
        }
    }
    for (DrawableStateChangeListener localDrawableStateChangeListener = (DrawableStateChangeListener)localView2; ; localDrawableStateChangeListener = null)
    {
      return localDrawableStateChangeListener;
      localViewParent = localView1.getParent();
      break;
      StringBuilder localStringBuilder2 = new StringBuilder().append("Could not find the passDrawableStateChange id: ");
      int j = this.mStateChangeListenerId;
      String str3 = j;
      int k = Log.e("PassStateChangeTextView", str3);
    }
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (!this.mStateChangeListeneredSearchedFor)
    {
      DrawableStateChangeListener localDrawableStateChangeListener1 = findStateChangeListener();
      this.mStateChangeListener = localDrawableStateChangeListener1;
      this.mStateChangeListeneredSearchedFor = true;
    }
    if (this.mStateChangeListener == null)
      return;
    DrawableStateChangeListener localDrawableStateChangeListener2 = this.mStateChangeListener;
    int[] arrayOfInt = getDrawableState();
    localDrawableStateChangeListener2.onDrawableStateChanged(arrayOfInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.PassStateChangeTextView
 * JD-Core Version:    0.6.2
 */