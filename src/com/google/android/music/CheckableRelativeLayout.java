package com.google.android.music;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckableRelativeLayout extends RelativeLayout
  implements Checkable
{
  private static final int[] CHECKED_STATE_SET = arrayOfInt;
  private boolean mChecked;

  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16842912;
  }

  public CheckableRelativeLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public boolean isChecked()
  {
    return this.mChecked;
  }

  protected int[] onCreateDrawableState(int paramInt)
  {
    int i = paramInt + 1;
    int[] arrayOfInt1 = super.onCreateDrawableState(i);
    if (isChecked())
    {
      int[] arrayOfInt2 = CHECKED_STATE_SET;
      int[] arrayOfInt3 = mergeDrawableStates(arrayOfInt1, arrayOfInt2);
    }
    return arrayOfInt1;
  }

  public void setChecked(boolean paramBoolean)
  {
    if (this.mChecked != paramBoolean)
      return;
    this.mChecked = paramBoolean;
    refreshDrawableState();
  }

  public void toggle()
  {
    if (!this.mChecked);
    for (boolean bool = true; ; bool = false)
    {
      setChecked(bool);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.CheckableRelativeLayout
 * JD-Core Version:    0.6.2
 */