package com.google.android.music.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.support.v4.view.ViewCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;
import com.google.android.music.preferences.MusicPreferences;

public class ViewUtils
{
  public static boolean canScrollVertically(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool = true;
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int i = paramView.getScrollX();
      int j = paramView.getScrollY();
      int k = localViewGroup.getChildCount() + -1;
      if (k >= 0)
      {
        View localView = localViewGroup.getChildAt(k);
        if (localView.getVisibility() != 0);
        int i9;
        int i12;
        do
        {
          int i5;
          int i6;
          do
          {
            int i3;
            int i4;
            do
            {
              int i1;
              int i2;
              do
              {
                int m;
                int n;
                do
                {
                  k += -1;
                  break;
                  m = paramInt2 + i;
                  n = localView.getLeft();
                }
                while (m < n);
                i1 = paramInt2 + i;
                i2 = localView.getRight();
              }
              while (i1 >= i2);
              i3 = paramInt3 + j;
              i4 = localView.getTop();
            }
            while (i3 < i4);
            i5 = paramInt3 + j;
            i6 = localView.getBottom();
          }
          while (i5 >= i6);
          int i7 = paramInt2 + i;
          int i8 = localView.getLeft();
          i9 = i7 - i8;
          int i10 = paramInt3 + j;
          int i11 = localView.getTop();
          i12 = i10 - i11;
        }
        while (!canScrollVertically(localView, true, paramInt1, i9, i12));
      }
    }
    while (true)
    {
      return bool;
      if (paramBoolean)
      {
        if (!(paramView instanceof ListView))
        {
          int i13 = -paramInt1;
          if (ViewCompat.canScrollVertically(paramView, i13));
        }
      }
      else
        bool = false;
    }
  }

  public static View createOrReuseView(View paramView, ViewGroup paramViewGroup, int paramInt)
  {
    if (paramView == null)
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(paramInt, paramViewGroup, false);
    return paramView;
  }

  public static void fadeViewForPosition(View paramView, int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt1 - paramInt2;
    float f1 = paramInt3 + 1.0F;
    float f2 = 1.0F / f1;
    if ((paramInt2 != -1) && (paramInt1 != -1) && (i <= paramInt3))
    {
      if (i <= 0)
        return;
      float f3 = i * f2;
      setAlpha(paramView, f3);
      return;
    }
    setAlpha(paramView, 1.0F);
  }

  public static void getCurrentSizeRange(Display paramDisplay, Point paramPoint1, Point paramPoint2)
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      getCurrentSizeRangeJb(paramDisplay, paramPoint1, paramPoint2);
      return;
    }
    getCurrentSizeRangePreJb(paramDisplay, paramPoint1, paramPoint2);
  }

  @TargetApi(16)
  private static void getCurrentSizeRangeJb(Display paramDisplay, Point paramPoint1, Point paramPoint2)
  {
    paramDisplay.getCurrentSizeRange(paramPoint1, paramPoint2);
  }

  private static void getCurrentSizeRangePreJb(Display paramDisplay, Point paramPoint1, Point paramPoint2)
  {
    getSize(paramDisplay, paramPoint1);
    int i = paramPoint1.x;
    int j = paramPoint1.y;
    int k = Math.min(i, j);
    int m = paramPoint1.x;
    int n = paramPoint1.y;
    int i1 = Math.max(m, n);
    paramPoint1.set(k, k);
    paramPoint2.set(i1, i1);
  }

  public static int getScreenColumnCount(Resources paramResources, MusicPreferences paramMusicPreferences)
  {
    int i = paramResources.getInteger(2131492869);
    if (paramMusicPreferences.isTvMusic())
      i = paramResources.getInteger(2131492870);
    return i;
  }

  public static void getSize(Display paramDisplay, Point paramPoint)
  {
    if (Build.VERSION.SDK_INT >= 13)
    {
      getSizeHCMR2(paramDisplay, paramPoint);
      return;
    }
    getSizePreHCMR2(paramDisplay, paramPoint);
  }

  @TargetApi(13)
  private static void getSizeHCMR2(Display paramDisplay, Point paramPoint)
  {
    paramDisplay.getSize(paramPoint);
  }

  private static void getSizePreHCMR2(Display paramDisplay, Point paramPoint)
  {
    int i = paramDisplay.getWidth();
    int j = paramDisplay.getHeight();
    paramPoint.set(i, j);
  }

  public static void removeViewFromParent(View paramView)
  {
    ViewGroup localViewGroup = (ViewGroup)paramView.getParent();
    if (localViewGroup == null)
      return;
    localViewGroup.removeView(paramView);
  }

  public static void setAlpha(View paramView, float paramFloat)
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      paramView.setAlpha(paramFloat);
      return;
    }
    AlphaAnimation localAlphaAnimation = new AlphaAnimation(paramFloat, paramFloat);
    localAlphaAnimation.setFillEnabled(true);
    localAlphaAnimation.setFillAfter(true);
    paramView.setAnimation(localAlphaAnimation);
    paramView.startAnimation(localAlphaAnimation);
  }

  public static void setEnabledAll(View paramView, boolean paramBoolean)
  {
    paramView.setEnabled(paramBoolean);
    if (!(paramView instanceof ViewGroup))
      return;
    ViewGroup localViewGroup = (ViewGroup)paramView;
    int i = localViewGroup.getChildCount();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      setEnabledAll(localViewGroup.getChildAt(j), paramBoolean);
      j += 1;
    }
  }

  public static int setWidthToShortestEdge(Context paramContext, ViewGroup paramViewGroup)
  {
    Display localDisplay = ((WindowManager)paramContext.getSystemService("window")).getDefaultDisplay();
    Point localPoint = new Point();
    getSize(localDisplay, localPoint);
    int i = localPoint.x;
    int j = localPoint.y;
    int k = Math.min(i, j);
    ViewGroup.LayoutParams localLayoutParams = paramViewGroup.getLayoutParams();
    if (localLayoutParams != null)
      localLayoutParams.width = k;
    return k;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.ViewUtils
 * JD-Core Version:    0.6.2
 */