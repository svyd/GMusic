package com.google.android.music.ui;

import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.common.base.Preconditions;

final class FragmentTabInfo
{
  public final FragmentInfo mFragmentInfo;
  public final float mPageWidth;
  public final int mResId;
  public final String mTag;

  FragmentTabInfo(String paramString, int paramInt, Class<?> paramClass)
  {
    this(paramString, paramInt, paramClass, null);
  }

  FragmentTabInfo(String paramString, int paramInt, Class<?> paramClass, Bundle paramBundle)
  {
  }

  FragmentTabInfo(String paramString, int paramInt, Class<?> paramClass, Bundle paramBundle, float paramFloat)
  {
    if (!TextUtils.isEmpty(paramString));
    for (boolean bool = true; ; bool = false)
    {
      Preconditions.checkArgument(bool, "invalid tag");
      this.mTag = paramString;
      this.mResId = paramInt;
      FragmentInfo localFragmentInfo = new FragmentInfo(paramClass, paramBundle);
      this.mFragmentInfo = localFragmentInfo;
      this.mPageWidth = paramFloat;
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.FragmentTabInfo
 * JD-Core Version:    0.6.2
 */