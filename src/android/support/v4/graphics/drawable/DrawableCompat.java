package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public class DrawableCompat
{
  static final DrawableImpl IMPL = new BaseDrawableImpl();

  static
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new HoneycombDrawableImpl();
      return;
    }
  }

  public static void jumpToCurrentState(Drawable paramDrawable)
  {
    IMPL.jumpToCurrentState(paramDrawable);
  }

  static class HoneycombDrawableImpl
    implements DrawableCompat.DrawableImpl
  {
    public void jumpToCurrentState(Drawable paramDrawable)
    {
      DrawableCompatHoneycomb.jumpToCurrentState(paramDrawable);
    }
  }

  static class BaseDrawableImpl
    implements DrawableCompat.DrawableImpl
  {
    public void jumpToCurrentState(Drawable paramDrawable)
    {
    }
  }

  static abstract interface DrawableImpl
  {
    public abstract void jumpToCurrentState(Drawable paramDrawable);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.graphics.drawable.DrawableCompat
 * JD-Core Version:    0.6.2
 */