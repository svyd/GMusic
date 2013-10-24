package android.support.v7.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.SpinnerAdapter;

public abstract class ActionBar
{
  public abstract int getDisplayOptions();

  public Context getThemedContext()
  {
    return null;
  }

  public abstract void hide();

  public abstract void setBackgroundDrawable(Drawable paramDrawable);

  public abstract void setCustomView(View paramView, LayoutParams paramLayoutParams);

  public abstract void setDisplayHomeAsUpEnabled(boolean paramBoolean);

  public abstract void setDisplayOptions(int paramInt1, int paramInt2);

  public abstract void setDisplayShowCustomEnabled(boolean paramBoolean);

  public abstract void setDisplayShowHomeEnabled(boolean paramBoolean);

  public void setHomeButtonEnabled(boolean paramBoolean)
  {
  }

  public abstract void setListNavigationCallbacks(SpinnerAdapter paramSpinnerAdapter, OnNavigationListener paramOnNavigationListener);

  public abstract void setNavigationMode(int paramInt);

  public abstract void setSelectedNavigationItem(int paramInt);

  public abstract void setTitle(int paramInt);

  public abstract void setTitle(CharSequence paramCharSequence);

  public abstract void show();

  static abstract interface Callback
  {
  }

  public static class LayoutParams extends ViewGroup.MarginLayoutParams
  {
    public int gravity = -1;

    public LayoutParams(int paramInt)
    {
      this(-1, -1, paramInt);
    }

    public LayoutParams(int paramInt1, int paramInt2)
    {
      super(paramInt2);
      this.gravity = 19;
    }

    public LayoutParams(int paramInt1, int paramInt2, int paramInt3)
    {
      super(paramInt2);
      this.gravity = paramInt3;
    }

    public LayoutParams(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramAttributeSet);
      int[] arrayOfInt = R.styleable.ActionBarLayout;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      int i = localTypedArray.getInt(0, -1);
      this.gravity = i;
      localTypedArray.recycle();
    }

    public LayoutParams(ViewGroup.LayoutParams paramLayoutParams)
    {
      super();
    }
  }

  public static abstract interface TabListener
  {
    public abstract void onTabReselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction);

    public abstract void onTabSelected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction);

    public abstract void onTabUnselected(ActionBar.Tab paramTab, FragmentTransaction paramFragmentTransaction);
  }

  public static abstract class Tab
  {
    public abstract CharSequence getContentDescription();

    public abstract View getCustomView();

    public abstract Drawable getIcon();

    public abstract int getPosition();

    public abstract CharSequence getText();

    public abstract void select();
  }

  public static abstract interface OnNavigationListener
  {
    public abstract boolean onNavigationItemSelected(int paramInt, long paramLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.ActionBar
 * JD-Core Version:    0.6.2
 */