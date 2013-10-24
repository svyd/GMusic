package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R.attr;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class ActionBarOverlayLayout extends FrameLayout
{
  static final int[] mActionBarSizeAttr = arrayOfInt;
  private ActionBar mActionBar;
  private int mActionBarHeight;
  private final Rect mZeroRect;

  static
  {
    int[] arrayOfInt = new int[1];
    int i = R.attr.actionBarSize;
    arrayOfInt[0] = i;
  }

  public ActionBarOverlayLayout(Context paramContext)
  {
    super(paramContext);
    Rect localRect = new Rect(0, 0, 0, 0);
    this.mZeroRect = localRect;
    init(paramContext);
  }

  public ActionBarOverlayLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Rect localRect = new Rect(0, 0, 0, 0);
    this.mZeroRect = localRect;
    init(paramContext);
  }

  private void init(Context paramContext)
  {
    Resources.Theme localTheme = getContext().getTheme();
    int[] arrayOfInt = mActionBarSizeAttr;
    TypedArray localTypedArray = localTheme.obtainStyledAttributes(arrayOfInt);
    int i = localTypedArray.getDimensionPixelSize(0, 0);
    this.mActionBarHeight = i;
    localTypedArray.recycle();
  }

  public void setActionBar(ActionBar paramActionBar)
  {
    this.mActionBar = paramActionBar;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActionBarOverlayLayout
 * JD-Core Version:    0.6.2
 */