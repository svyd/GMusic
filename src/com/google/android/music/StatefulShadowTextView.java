package com.google.android.music;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.TextView;

public class StatefulShadowTextView extends TextView
{
  private static ColorStateList mPrimaryOffline;
  private static ColorStateList mPrimaryOnline;
  private static ColorStateList mSecondaryOffline;
  private static ColorStateList mSecondaryOnline;
  private static int mShadowOffline;
  private static int mShadowOnline;
  private boolean mOnline = true;
  private boolean mShowShadowWhenDeselected = true;
  private boolean mShowShadowWhenSelected = false;

  public StatefulShadowTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    updateShadowState();
    if (mPrimaryOnline != null)
      return;
    Resources localResources = paramContext.getResources();
    mPrimaryOnline = localResources.getColorStateList(2131427380);
    mPrimaryOffline = localResources.getColorStateList(2131427379);
    mSecondaryOnline = localResources.getColorStateList(2131427381);
    mSecondaryOffline = localResources.getColorStateList(2131427379);
    mShadowOnline = localResources.getColorStateList(2131427408).getDefaultColor();
    mShadowOffline = localResources.getColorStateList(2131427409).getDefaultColor();
  }

  private void updateShadowState()
  {
    if ((isPressed()) || (isSelected()) || (isFocused()))
    {
      boolean bool1 = this.mShowShadowWhenSelected;
      return;
    }
    boolean bool2 = this.mShowShadowWhenDeselected;
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    updateShadowState();
  }

  public void setPrimaryAndOnline(boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      if (paramBoolean2);
      for (localColorStateList = mPrimaryOnline; ; localColorStateList = mPrimaryOffline)
      {
        setTextColor(localColorStateList);
        this.mOnline = paramBoolean2;
        return;
      }
    }
    if (paramBoolean2);
    for (ColorStateList localColorStateList = mSecondaryOnline; ; localColorStateList = mSecondaryOffline)
    {
      setTextColor(localColorStateList);
      break;
    }
  }

  public void setShowShadowWhenDeselected(boolean paramBoolean)
  {
    this.mShowShadowWhenDeselected = paramBoolean;
    updateShadowState();
  }

  public void setShowShadowWhenSelected(boolean paramBoolean)
  {
    this.mShowShadowWhenSelected = paramBoolean;
    updateShadowState();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.StatefulShadowTextView
 * JD-Core Version:    0.6.2
 */