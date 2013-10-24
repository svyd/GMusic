package com.google.android.music.ui;

import android.content.Context;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.google.android.music.widgets.LinkableViewPager;

public class NowPlayingArtViewPager extends LinkableViewPager
{
  private static float MIN_SCALE = 0.75F;

  public NowPlayingArtViewPager(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    NowPlayingArtTransformer localNowPlayingArtTransformer = new NowPlayingArtTransformer(null);
    setPageTransformer(true, localNowPlayingArtTransformer);
  }

  protected int getChildDrawingOrder(int paramInt1, int paramInt2)
  {
    try
    {
      int i = super.getChildDrawingOrder(paramInt1, paramInt2);
      paramInt2 = i;
      return paramInt2;
    }
    catch (NullPointerException localNullPointerException)
    {
      while (true)
        int j = Log.i("NowPlayingArtViewPager", "Error in getChildDrawingOrder. Falling back to default impl");
    }
  }

  private class NowPlayingArtTransformer
    implements ViewPager.PageTransformer
  {
    private NowPlayingArtTransformer()
    {
    }

    public void transformPage(View paramView, float paramFloat)
    {
      int i = paramView.getWidth();
      if (paramFloat < -1.0F)
      {
        paramView.setAlpha(0.0F);
        return;
      }
      if (paramFloat <= 0.0F)
      {
        paramView.setAlpha(1.0F);
        paramView.setTranslationX(0.0F);
        paramView.setScaleX(1.0F);
        paramView.setScaleY(1.0F);
        return;
      }
      if (paramFloat <= 1.0F)
      {
        float f1 = 1.0F - paramFloat;
        paramView.setAlpha(f1);
        float f2 = i;
        float f3 = -paramFloat;
        float f4 = f2 * f3;
        paramView.setTranslationX(f4);
        float f5 = NowPlayingArtViewPager.MIN_SCALE;
        float f6 = NowPlayingArtViewPager.MIN_SCALE;
        float f7 = 1.0F - f6;
        float f8 = Math.abs(paramFloat);
        float f9 = 1.0F - f8;
        float f10 = f7 * f9;
        float f11 = f5 + f10;
        paramView.setScaleX(f11);
        paramView.setScaleY(f11);
        return;
      }
      paramView.setAlpha(0.0F);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.NowPlayingArtViewPager
 * JD-Core Version:    0.6.2
 */