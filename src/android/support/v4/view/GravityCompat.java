package android.support.v4.view;

import android.os.Build.VERSION;

public class GravityCompat
{
  static final GravityCompatImpl IMPL = new GravityCompatImplBase();

  static
  {
    if (Build.VERSION.SDK_INT >= 17)
    {
      IMPL = new GravityCompatImplJellybeanMr1();
      return;
    }
  }

  public static int getAbsoluteGravity(int paramInt1, int paramInt2)
  {
    return IMPL.getAbsoluteGravity(paramInt1, paramInt2);
  }

  static class GravityCompatImplJellybeanMr1
    implements GravityCompat.GravityCompatImpl
  {
    public int getAbsoluteGravity(int paramInt1, int paramInt2)
    {
      return GravityCompatJellybeanMr1.getAbsoluteGravity(paramInt1, paramInt2);
    }
  }

  static class GravityCompatImplBase
    implements GravityCompat.GravityCompatImpl
  {
    public int getAbsoluteGravity(int paramInt1, int paramInt2)
    {
      return 0xFF7FFFFF & paramInt1;
    }
  }

  static abstract interface GravityCompatImpl
  {
    public abstract int getAbsoluteGravity(int paramInt1, int paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.GravityCompat
 * JD-Core Version:    0.6.2
 */