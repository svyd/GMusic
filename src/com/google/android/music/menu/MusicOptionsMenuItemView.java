package com.google.android.music.menu;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class MusicOptionsMenuItemView extends TextView
{
  private Drawable mIcon;
  private MusicMenuItem mMenuItem;
  private Rect mPositionIconAvailable;
  private Rect mPositionIconOutput;

  public MusicOptionsMenuItemView(Context paramContext)
  {
    super(paramContext);
    Rect localRect1 = new Rect();
    this.mPositionIconAvailable = localRect1;
    Rect localRect2 = new Rect();
    this.mPositionIconOutput = localRect2;
  }

  public MusicOptionsMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Rect localRect1 = new Rect();
    this.mPositionIconAvailable = localRect1;
    Rect localRect2 = new Rect();
    this.mPositionIconOutput = localRect2;
  }

  private void positionIcon()
  {
    if (this.mIcon == null)
      return;
    Rect localRect1 = this.mPositionIconOutput;
    int i = getLineBounds(0, localRect1);
    Rect localRect2 = this.mPositionIconAvailable;
    int j = getWidth();
    int k = localRect1.top;
    localRect2.set(0, 0, j, k);
    int m = this.mIcon.getIntrinsicWidth();
    int n = this.mIcon.getIntrinsicHeight();
    Rect localRect3 = this.mPositionIconAvailable;
    Rect localRect4 = this.mPositionIconOutput;
    Gravity.apply(19, m, n, localRect3, localRect4);
    Drawable localDrawable = this.mIcon;
    Rect localRect5 = this.mPositionIconOutput;
    localDrawable.setBounds(localRect5);
  }

  public MusicOptionsMenuView.LayoutParams getTextAppropriateLayoutParams()
  {
    MusicOptionsMenuView.LayoutParams localLayoutParams = (MusicOptionsMenuView.LayoutParams)getLayoutParams();
    if (localLayoutParams == null)
      localLayoutParams = new MusicOptionsMenuView.LayoutParams(-1, -1);
    CharSequence localCharSequence = getText();
    TextPaint localTextPaint = getPaint();
    int i = (int)Layout.getDesiredWidth(localCharSequence, localTextPaint);
    localLayoutParams.desiredWidth = i;
    return localLayoutParams;
  }

  public void initialize(MusicMenuItem paramMusicMenuItem)
  {
    this.mMenuItem = paramMusicMenuItem;
    String str = paramMusicMenuItem.getTitle();
    Drawable localDrawable = paramMusicMenuItem.getIcon();
    initialize(str, localDrawable);
    boolean bool = paramMusicMenuItem.isEnabled();
    setEnabled(bool);
  }

  void initialize(CharSequence paramCharSequence, Drawable paramDrawable)
  {
    setTitle(paramCharSequence);
    setIcon(paramDrawable);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    positionIcon();
  }

  protected void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
  {
    super.onTextChanged(paramCharSequence, paramInt1, paramInt2, paramInt3);
    MusicOptionsMenuView.LayoutParams localLayoutParams = getTextAppropriateLayoutParams();
    setLayoutParams(localLayoutParams);
  }

  public boolean performClick()
  {
    boolean bool = true;
    if (super.performClick());
    while (true)
    {
      return bool;
      if (this.mMenuItem.invoke())
        playSoundEffect(0);
      else
        bool = false;
    }
  }

  public void setIcon(Drawable paramDrawable)
  {
    this.mIcon = paramDrawable;
    if (this.mIcon != null)
    {
      int i = paramDrawable.getIntrinsicWidth();
      int j = paramDrawable.getIntrinsicHeight();
      paramDrawable.setBounds(0, 0, i, j);
      setCompoundDrawables(null, paramDrawable, null, null);
      setGravity(81);
      requestLayout();
      return;
    }
    setCompoundDrawables(null, null, null, null);
    setGravity(17);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    setText(paramCharSequence);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicOptionsMenuItemView
 * JD-Core Version:    0.6.2
 */