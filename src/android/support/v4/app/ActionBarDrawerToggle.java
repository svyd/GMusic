package android.support.v4.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class ActionBarDrawerToggle
  implements DrawerLayout.DrawerListener
{
  private static final int ID_HOME = 16908332;
  private static final ActionBarDrawerToggleImpl IMPL = new ActionBarDrawerToggleImplBase(null);
  private final Activity mActivity;
  private final Delegate mActivityImpl;
  private final int mCloseDrawerContentDescRes;
  private Drawable mDrawerImage;
  private final int mDrawerImageResource;
  private boolean mDrawerIndicatorEnabled = true;
  private final DrawerLayout mDrawerLayout;
  private final int mOpenDrawerContentDescRes;
  private Object mSetIndicatorInfo;
  private SlideDrawable mSlider;
  private Drawable mThemeImage;

  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new ActionBarDrawerToggleImplHC(null);
      return;
    }
  }

  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, int paramInt1, int paramInt2, int paramInt3)
  {
    this.mActivity = paramActivity;
    Delegate localDelegate;
    if ((paramActivity instanceof DelegateProvider))
      localDelegate = ((DelegateProvider)paramActivity).getDrawerToggleDelegate();
    for (this.mActivityImpl = localDelegate; ; this.mActivityImpl = null)
    {
      this.mDrawerLayout = paramDrawerLayout;
      this.mDrawerImageResource = paramInt1;
      this.mOpenDrawerContentDescRes = paramInt2;
      this.mCloseDrawerContentDescRes = paramInt3;
      Drawable localDrawable1 = getThemeUpIndicator();
      this.mThemeImage = localDrawable1;
      Drawable localDrawable2 = paramActivity.getResources().getDrawable(paramInt1);
      this.mDrawerImage = localDrawable2;
      Drawable localDrawable3 = this.mDrawerImage;
      SlideDrawable localSlideDrawable = new SlideDrawable(localDrawable3);
      this.mSlider = localSlideDrawable;
      this.mSlider.setOffsetBy(0.3333333F);
      return;
    }
  }

  Drawable getThemeUpIndicator()
  {
    if (this.mActivityImpl != null);
    ActionBarDrawerToggleImpl localActionBarDrawerToggleImpl;
    Activity localActivity;
    for (Drawable localDrawable = this.mActivityImpl.getThemeUpIndicator(); ; localDrawable = localActionBarDrawerToggleImpl.getThemeUpIndicator(localActivity))
    {
      return localDrawable;
      localActionBarDrawerToggleImpl = IMPL;
      localActivity = this.mActivity;
    }
  }

  public boolean isDrawerIndicatorEnabled()
  {
    return this.mDrawerIndicatorEnabled;
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    Drawable localDrawable1 = getThemeUpIndicator();
    this.mThemeImage = localDrawable1;
    Resources localResources = this.mActivity.getResources();
    int i = this.mDrawerImageResource;
    Drawable localDrawable2 = localResources.getDrawable(i);
    this.mDrawerImage = localDrawable2;
    syncState();
  }

  public void onDrawerClosed(View paramView)
  {
    this.mSlider.setOffset(0.0F);
    if (!this.mDrawerIndicatorEnabled)
      return;
    int i = this.mCloseDrawerContentDescRes;
    setActionBarDescription(i);
  }

  public void onDrawerOpened(View paramView)
  {
    this.mSlider.setOffset(1.0F);
    if (!this.mDrawerIndicatorEnabled)
      return;
    int i = this.mOpenDrawerContentDescRes;
    setActionBarDescription(i);
  }

  public void onDrawerSlide(View paramView, float paramFloat)
  {
    float f1 = this.mSlider.getOffset();
    float f3;
    if (paramFloat > 0.5F)
    {
      float f2 = paramFloat - 0.5F;
      f3 = Math.max(0.0F, f2) * 2.0F;
    }
    float f5;
    for (float f4 = Math.max(f1, f3); ; f4 = Math.min(f1, f5))
    {
      this.mSlider.setOffset(f4);
      return;
      f5 = paramFloat * 2.0F;
    }
  }

  public void onDrawerStateChanged(int paramInt)
  {
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if ((paramMenuItem != null) && (paramMenuItem.getItemId() == 16908332) && (this.mDrawerIndicatorEnabled))
      if (this.mDrawerLayout.isDrawerVisible(8388611))
        this.mDrawerLayout.closeDrawer(8388611);
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      this.mDrawerLayout.openDrawer(8388611);
      break;
    }
  }

  void setActionBarDescription(int paramInt)
  {
    if (this.mActivityImpl != null)
    {
      this.mActivityImpl.setActionBarDescription(paramInt);
      return;
    }
    ActionBarDrawerToggleImpl localActionBarDrawerToggleImpl = IMPL;
    Object localObject1 = this.mSetIndicatorInfo;
    Activity localActivity = this.mActivity;
    Object localObject2 = localActionBarDrawerToggleImpl.setActionBarDescription(localObject1, localActivity, paramInt);
    this.mSetIndicatorInfo = localObject2;
  }

  void setActionBarUpIndicator(Drawable paramDrawable, int paramInt)
  {
    if (this.mActivityImpl != null)
    {
      this.mActivityImpl.setActionBarUpIndicator(paramDrawable, paramInt);
      return;
    }
    ActionBarDrawerToggleImpl localActionBarDrawerToggleImpl = IMPL;
    Object localObject1 = this.mSetIndicatorInfo;
    Activity localActivity = this.mActivity;
    Object localObject2 = localActionBarDrawerToggleImpl.setActionBarUpIndicator(localObject1, localActivity, paramDrawable, paramInt);
    this.mSetIndicatorInfo = localObject2;
  }

  public void setDrawerIndicatorEnabled(boolean paramBoolean)
  {
    boolean bool = this.mDrawerIndicatorEnabled;
    if (paramBoolean != bool)
      return;
    int i;
    if (paramBoolean)
    {
      SlideDrawable localSlideDrawable = this.mSlider;
      if (this.mDrawerLayout.isDrawerOpen(8388611))
      {
        i = this.mOpenDrawerContentDescRes;
        setActionBarUpIndicator(localSlideDrawable, i);
      }
    }
    while (true)
    {
      this.mDrawerIndicatorEnabled = paramBoolean;
      return;
      i = this.mCloseDrawerContentDescRes;
      break;
      Drawable localDrawable = this.mThemeImage;
      setActionBarUpIndicator(localDrawable, 0);
    }
  }

  public void syncState()
  {
    SlideDrawable localSlideDrawable;
    if (this.mDrawerLayout.isDrawerOpen(8388611))
    {
      this.mSlider.setOffset(1.0F);
      if (!this.mDrawerIndicatorEnabled)
        return;
      localSlideDrawable = this.mSlider;
      if (!this.mDrawerLayout.isDrawerOpen(8388611))
        break label70;
    }
    label70: for (int i = this.mOpenDrawerContentDescRes; ; i = this.mCloseDrawerContentDescRes)
    {
      setActionBarUpIndicator(localSlideDrawable, i);
      return;
      this.mSlider.setOffset(0.0F);
      break;
    }
  }

  private class SlideDrawable extends Drawable
    implements Drawable.Callback
  {
    private float mOffset;
    private float mOffsetBy;
    private final Rect mTmpRect;
    private Drawable mWrapped;

    public SlideDrawable(Drawable arg2)
    {
      Rect localRect = new Rect();
      this.mTmpRect = localRect;
      Object localObject;
      this.mWrapped = localObject;
    }

    public void clearColorFilter()
    {
      this.mWrapped.clearColorFilter();
    }

    public void draw(Canvas paramCanvas)
    {
      int i = 1;
      Drawable localDrawable = this.mWrapped;
      Rect localRect = this.mTmpRect;
      localDrawable.copyBounds(localRect);
      int j = paramCanvas.save();
      if (ViewCompat.getLayoutDirection(ActionBarDrawerToggle.this.mActivity.getWindow().getDecorView()) != i)
        i = -1;
      float f1 = this.mOffsetBy;
      float f2 = this.mTmpRect.width();
      float f3 = f1 * f2;
      float f4 = -this.mOffset;
      float f5 = f3 * f4;
      float f6 = i;
      float f7 = f5 * f6;
      paramCanvas.translate(f7, 0.0F);
      this.mWrapped.draw(paramCanvas);
      paramCanvas.restore();
    }

    public int getChangingConfigurations()
    {
      return this.mWrapped.getChangingConfigurations();
    }

    public Drawable.ConstantState getConstantState()
    {
      return super.getConstantState();
    }

    public Drawable getCurrent()
    {
      return this.mWrapped.getCurrent();
    }

    public int getIntrinsicHeight()
    {
      return this.mWrapped.getIntrinsicHeight();
    }

    public int getIntrinsicWidth()
    {
      return this.mWrapped.getIntrinsicWidth();
    }

    public int getMinimumHeight()
    {
      return this.mWrapped.getMinimumHeight();
    }

    public int getMinimumWidth()
    {
      return this.mWrapped.getMinimumWidth();
    }

    public float getOffset()
    {
      return this.mOffset;
    }

    public int getOpacity()
    {
      return this.mWrapped.getOpacity();
    }

    public boolean getPadding(Rect paramRect)
    {
      return this.mWrapped.getPadding(paramRect);
    }

    public int[] getState()
    {
      return this.mWrapped.getState();
    }

    public Region getTransparentRegion()
    {
      return this.mWrapped.getTransparentRegion();
    }

    public void invalidateDrawable(Drawable paramDrawable)
    {
      Drawable localDrawable = this.mWrapped;
      if (paramDrawable != localDrawable)
        return;
      invalidateSelf();
    }

    public boolean isStateful()
    {
      return this.mWrapped.isStateful();
    }

    protected void onBoundsChange(Rect paramRect)
    {
      super.onBoundsChange(paramRect);
      this.mWrapped.setBounds(paramRect);
    }

    protected boolean onStateChange(int[] paramArrayOfInt)
    {
      boolean bool = this.mWrapped.setState(paramArrayOfInt);
      return super.onStateChange(paramArrayOfInt);
    }

    public void scheduleDrawable(Drawable paramDrawable, Runnable paramRunnable, long paramLong)
    {
      Drawable localDrawable = this.mWrapped;
      if (paramDrawable != localDrawable)
        return;
      scheduleSelf(paramRunnable, paramLong);
    }

    public void setAlpha(int paramInt)
    {
      this.mWrapped.setAlpha(paramInt);
    }

    public void setChangingConfigurations(int paramInt)
    {
      this.mWrapped.setChangingConfigurations(paramInt);
    }

    public void setColorFilter(int paramInt, PorterDuff.Mode paramMode)
    {
      this.mWrapped.setColorFilter(paramInt, paramMode);
    }

    public void setColorFilter(ColorFilter paramColorFilter)
    {
      this.mWrapped.setColorFilter(paramColorFilter);
    }

    public void setDither(boolean paramBoolean)
    {
      this.mWrapped.setDither(paramBoolean);
    }

    public void setFilterBitmap(boolean paramBoolean)
    {
      this.mWrapped.setFilterBitmap(paramBoolean);
    }

    public void setOffset(float paramFloat)
    {
      this.mOffset = paramFloat;
      invalidateSelf();
    }

    public void setOffsetBy(float paramFloat)
    {
      this.mOffsetBy = paramFloat;
      invalidateSelf();
    }

    public boolean setState(int[] paramArrayOfInt)
    {
      return this.mWrapped.setState(paramArrayOfInt);
    }

    public boolean setVisible(boolean paramBoolean1, boolean paramBoolean2)
    {
      return super.setVisible(paramBoolean1, paramBoolean2);
    }

    public void unscheduleDrawable(Drawable paramDrawable, Runnable paramRunnable)
    {
      Drawable localDrawable = this.mWrapped;
      if (paramDrawable != localDrawable)
        return;
      unscheduleSelf(paramRunnable);
    }
  }

  private static class ActionBarDrawerToggleImplHC
    implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl
  {
    public Drawable getThemeUpIndicator(Activity paramActivity)
    {
      return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(paramActivity);
    }

    public Object setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
    {
      return ActionBarDrawerToggleHoneycomb.setActionBarDescription(paramObject, paramActivity, paramInt);
    }

    public Object setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
    {
      return ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(paramObject, paramActivity, paramDrawable, paramInt);
    }
  }

  private static class ActionBarDrawerToggleImplBase
    implements ActionBarDrawerToggle.ActionBarDrawerToggleImpl
  {
    public Drawable getThemeUpIndicator(Activity paramActivity)
    {
      return null;
    }

    public Object setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
    {
      return paramObject;
    }

    public Object setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
    {
      return paramObject;
    }
  }

  private static abstract interface ActionBarDrawerToggleImpl
  {
    public abstract Drawable getThemeUpIndicator(Activity paramActivity);

    public abstract Object setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt);

    public abstract Object setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt);
  }

  public static abstract interface Delegate
  {
    public abstract Drawable getThemeUpIndicator();

    public abstract void setActionBarDescription(int paramInt);

    public abstract void setActionBarUpIndicator(Drawable paramDrawable, int paramInt);
  }

  public static abstract interface DelegateProvider
  {
    public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ActionBarDrawerToggle
 * JD-Core Version:    0.6.2
 */