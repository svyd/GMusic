package android.support.v4.app;

import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

class ActivityOptionsCompatJB
{
  private final ActivityOptions mActivityOptions;

  private ActivityOptionsCompatJB(ActivityOptions paramActivityOptions)
  {
    this.mActivityOptions = paramActivityOptions;
  }

  public static ActivityOptionsCompatJB makeCustomAnimation(Context paramContext, int paramInt1, int paramInt2)
  {
    ActivityOptions localActivityOptions = ActivityOptions.makeCustomAnimation(paramContext, paramInt1, paramInt2);
    return new ActivityOptionsCompatJB(localActivityOptions);
  }

  public static ActivityOptionsCompatJB makeScaleUpAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    ActivityOptions localActivityOptions = ActivityOptions.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4);
    return new ActivityOptionsCompatJB(localActivityOptions);
  }

  public static ActivityOptionsCompatJB makeThumbnailScaleUpAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    ActivityOptions localActivityOptions = ActivityOptions.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2);
    return new ActivityOptionsCompatJB(localActivityOptions);
  }

  public Bundle toBundle()
  {
    return this.mActivityOptions.toBundle();
  }

  public void update(ActivityOptionsCompatJB paramActivityOptionsCompatJB)
  {
    ActivityOptions localActivityOptions1 = this.mActivityOptions;
    ActivityOptions localActivityOptions2 = paramActivityOptionsCompatJB.mActivityOptions;
    localActivityOptions1.update(localActivityOptions2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ActivityOptionsCompatJB
 * JD-Core Version:    0.6.2
 */