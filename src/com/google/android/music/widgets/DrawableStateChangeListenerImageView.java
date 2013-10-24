package com.google.android.music.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class DrawableStateChangeListenerImageView extends ImageView
  implements DrawableStateChangeListener
{
  public DrawableStateChangeListenerImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
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
 * Qualified Name:     com.google.android.music.widgets.DrawableStateChangeListenerImageView
 * JD-Core Version:    0.6.2
 */