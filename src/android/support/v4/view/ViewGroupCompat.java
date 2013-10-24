package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewGroup;

public class ViewGroupCompat
{
  static final ViewGroupCompatImpl IMPL = new ViewGroupCompatStubImpl();

  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i >= 18)
    {
      IMPL = new ViewGroupCompatJellybeanMR2Impl();
      return;
    }
    if (i >= 14)
    {
      IMPL = new ViewGroupCompatIcsImpl();
      return;
    }
    if (i >= 11)
    {
      IMPL = new ViewGroupCompatHCImpl();
      return;
    }
  }

  public static void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean)
  {
    IMPL.setMotionEventSplittingEnabled(paramViewGroup, paramBoolean);
  }

  static class ViewGroupCompatJellybeanMR2Impl extends ViewGroupCompat.ViewGroupCompatIcsImpl
  {
  }

  static class ViewGroupCompatIcsImpl extends ViewGroupCompat.ViewGroupCompatHCImpl
  {
  }

  static class ViewGroupCompatHCImpl extends ViewGroupCompat.ViewGroupCompatStubImpl
  {
    public void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean)
    {
      ViewGroupCompatHC.setMotionEventSplittingEnabled(paramViewGroup, paramBoolean);
    }
  }

  static class ViewGroupCompatStubImpl
    implements ViewGroupCompat.ViewGroupCompatImpl
  {
    public void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean)
    {
    }
  }

  static abstract interface ViewGroupCompatImpl
  {
    public abstract void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewGroupCompat
 * JD-Core Version:    0.6.2
 */