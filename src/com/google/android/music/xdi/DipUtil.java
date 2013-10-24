package com.google.android.music.xdi;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public final class DipUtil
{
  public static float getPxFromDip(Context paramContext, float paramFloat)
  {
    DisplayMetrics localDisplayMetrics = paramContext.getResources().getDisplayMetrics();
    return TypedValue.applyDimension(1, paramFloat, localDisplayMetrics);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DipUtil
 * JD-Core Version:    0.6.2
 */