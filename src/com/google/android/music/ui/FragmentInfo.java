package com.google.android.music.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public final class FragmentInfo
{
  private final Bundle mArgs;
  private final Class<?> mFragmentClass;

  public FragmentInfo(Class<?> paramClass, Bundle paramBundle)
  {
    this.mFragmentClass = paramClass;
    this.mArgs = paramBundle;
  }

  public Fragment instantiate(Context paramContext)
  {
    String str = this.mFragmentClass.getName();
    Bundle localBundle = this.mArgs;
    return Fragment.instantiate(paramContext, str, localBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.FragmentInfo
 * JD-Core Version:    0.6.2
 */