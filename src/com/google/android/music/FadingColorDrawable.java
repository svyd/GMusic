package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

public class FadingColorDrawable extends ColorDrawable
{
  private boolean hadFocus = false;
  private boolean inPressed = false;
  private View mContainingView = null;
  private final int mMaxAlpha;

  public FadingColorDrawable(Context paramContext, View paramView)
  {
    this(localResources);
    if (paramView == null)
      throw new IllegalArgumentException("Containing view must not be null");
    this.mContainingView = paramView;
  }

  private FadingColorDrawable(Resources paramResources)
  {
    super(i);
    int j = getAlpha();
    this.mMaxAlpha = j;
    setAlpha(0);
  }

  public static boolean containsFocused(int[] paramArrayOfInt)
  {
    int[] arrayOfInt = paramArrayOfInt;
    int i = arrayOfInt.length;
    int j = 0;
    if (j < i)
      if (arrayOfInt[j] != 16842908);
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  public static boolean containsPressed(int[] paramArrayOfInt)
  {
    int[] arrayOfInt = paramArrayOfInt;
    int i = arrayOfInt.length;
    int j = 0;
    if (j < i)
      if (arrayOfInt[j] != 16842919);
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  public boolean isStateful()
  {
    return true;
  }

  protected boolean onStateChange(int[] paramArrayOfInt)
  {
    if (!this.mContainingView.isEnabled());
    label140: 
    while (true)
    {
      return false;
      View localView = (View)this.mContainingView.getParent();
      if ((localView == null) || (!containsPressed(localView.getDrawableState())))
      {
        int i = 0;
        if (containsFocused(paramArrayOfInt))
        {
          int j = this.mMaxAlpha;
          setAlpha(j);
          this.hadFocus = true;
          label63: if (!containsPressed(paramArrayOfInt))
            break label125;
          this.inPressed = true;
          int k = this.mMaxAlpha;
          setAlpha(k);
          this.mContainingView.invalidate();
          i = 0;
        }
        while (true)
        {
          if (i == 0)
            break label140;
          setAlpha(0);
          break;
          if (!this.hadFocus)
            break label63;
          this.hadFocus = false;
          i = 1;
          break label63;
          label125: if (this.inPressed)
          {
            i = 1;
            this.inPressed = false;
          }
        }
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.FadingColorDrawable
 * JD-Core Version:    0.6.2
 */