package android.support.v4.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

class ActionBarDrawerToggleHoneycomb
{
  private static final String TAG = "ActionBarDrawerToggleHoneycomb";
  private static final int[] THEME_ATTRS = arrayOfInt;

  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16843531;
  }

  public static Drawable getThemeUpIndicator(Activity paramActivity)
  {
    int[] arrayOfInt = THEME_ATTRS;
    TypedArray localTypedArray = paramActivity.obtainStyledAttributes(arrayOfInt);
    Drawable localDrawable = localTypedArray.getDrawable(0);
    localTypedArray.recycle();
    return localDrawable;
  }

  public static Object setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
  {
    if (paramObject == null)
      paramObject = new SetIndicatorInfo(paramActivity);
    SetIndicatorInfo localSetIndicatorInfo = (SetIndicatorInfo)paramObject;
    if (localSetIndicatorInfo.setHomeAsUpIndicator != null);
    try
    {
      ActionBar localActionBar = paramActivity.getActionBar();
      Method localMethod = localSetIndicatorInfo.setHomeActionContentDescription;
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      Object localObject = localMethod.invoke(localActionBar, arrayOfObject);
      return paramObject;
    }
    catch (Exception localException)
    {
      while (true)
        int i = Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set content description via JB-MR2 API", localException);
    }
  }

  public static Object setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
  {
    if (paramObject == null)
      paramObject = new SetIndicatorInfo(paramActivity);
    SetIndicatorInfo localSetIndicatorInfo = (SetIndicatorInfo)paramObject;
    if (localSetIndicatorInfo.setHomeAsUpIndicator != null);
    while (true)
    {
      try
      {
        ActionBar localActionBar = paramActivity.getActionBar();
        Method localMethod1 = localSetIndicatorInfo.setHomeAsUpIndicator;
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = paramDrawable;
        Object localObject1 = localMethod1.invoke(localActionBar, arrayOfObject1);
        Method localMethod2 = localSetIndicatorInfo.setHomeActionContentDescription;
        Object[] arrayOfObject2 = new Object[1];
        Integer localInteger = Integer.valueOf(paramInt);
        arrayOfObject2[0] = localInteger;
        Object localObject2 = localMethod2.invoke(localActionBar, arrayOfObject2);
        return paramObject;
      }
      catch (Exception localException)
      {
        int i = Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator via JB-MR2 API", localException);
        continue;
      }
      if (localSetIndicatorInfo.upIndicatorView != null)
        localSetIndicatorInfo.upIndicatorView.setImageDrawable(paramDrawable);
      else
        int j = Log.w("ActionBarDrawerToggleHoneycomb", "Couldn't set home-as-up indicator");
    }
  }

  private static class SetIndicatorInfo
  {
    public Method setHomeActionContentDescription;
    public Method setHomeAsUpIndicator;
    public ImageView upIndicatorView;

    SetIndicatorInfo(Activity paramActivity)
    {
      Object localObject;
      View localView;
      try
      {
        Class[] arrayOfClass1 = new Class[1];
        arrayOfClass1[0] = Drawable.class;
        Method localMethod1 = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", arrayOfClass1);
        this.setHomeAsUpIndicator = localMethod1;
        Class[] arrayOfClass2 = new Class[1];
        Class localClass = Integer.TYPE;
        arrayOfClass2[0] = localClass;
        Method localMethod2 = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", arrayOfClass2);
        this.setHomeActionContentDescription = localMethod2;
        return;
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        localObject = paramActivity.findViewById(16908332);
        if (localObject == null)
          return;
        localObject = (ViewGroup)((View)localObject).getParent();
        if (((ViewGroup)localObject).getChildCount() != 2)
          return;
        localView = ((ViewGroup)localObject).getChildAt(0);
        localObject = ((ViewGroup)localObject).getChildAt(1);
        if (localView.getId() != 16908332);
      }
      while (true)
      {
        if (!(localObject instanceof ImageView))
          return;
        ImageView localImageView = (ImageView)localObject;
        this.upIndicatorView = localImageView;
        return;
        localObject = localView;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ActionBarDrawerToggleHoneycomb
 * JD-Core Version:    0.6.2
 */