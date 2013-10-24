package android.support.v4.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;

public class ActivityOptionsCompat
{
  public static ActivityOptionsCompat makeCustomAnimation(Context paramContext, int paramInt1, int paramInt2)
  {
    ActivityOptionsCompatJB localActivityOptionsCompatJB;
    if (Build.VERSION.SDK_INT >= 16)
      localActivityOptionsCompatJB = ActivityOptionsCompatJB.makeCustomAnimation(paramContext, paramInt1, paramInt2);
    for (Object localObject = new ActivityOptionsImplJB(localActivityOptionsCompatJB); ; localObject = new ActivityOptionsCompat())
      return localObject;
  }

  public static ActivityOptionsCompat makeScaleUpAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ActivityOptionsCompatJB localActivityOptionsCompatJB;
    if (Build.VERSION.SDK_INT >= 16)
      localActivityOptionsCompatJB = ActivityOptionsCompatJB.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    for (Object localObject = new ActivityOptionsImplJB(localActivityOptionsCompatJB); ; localObject = new ActivityOptionsCompat())
      return localObject;
  }

  public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    ActivityOptionsCompatJB localActivityOptionsCompatJB;
    if (Build.VERSION.SDK_INT >= 16)
      localActivityOptionsCompatJB = ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2);
    for (Object localObject = new ActivityOptionsImplJB(localActivityOptionsCompatJB); ; localObject = new ActivityOptionsCompat())
      return localObject;
  }

  public Bundle toBundle()
  {
    return null;
  }

  public void update(ActivityOptionsCompat paramActivityOptionsCompat)
  {
  }

  private static class ActivityOptionsImplJB extends ActivityOptionsCompat
  {
    private final ActivityOptionsCompatJB mImpl;

    ActivityOptionsImplJB(ActivityOptionsCompatJB paramActivityOptionsCompatJB)
    {
      this.mImpl = paramActivityOptionsCompatJB;
    }

    public Bundle toBundle()
    {
      return this.mImpl.toBundle();
    }

    public void update(ActivityOptionsCompat paramActivityOptionsCompat)
    {
      if (!(paramActivityOptionsCompat instanceof ActivityOptionsImplJB))
        return;
      ActivityOptionsImplJB localActivityOptionsImplJB = (ActivityOptionsImplJB)paramActivityOptionsCompat;
      ActivityOptionsCompatJB localActivityOptionsCompatJB1 = this.mImpl;
      ActivityOptionsCompatJB localActivityOptionsCompatJB2 = localActivityOptionsImplJB.mImpl;
      localActivityOptionsCompatJB1.update(localActivityOptionsCompatJB2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ActivityOptionsCompat
 * JD-Core Version:    0.6.2
 */