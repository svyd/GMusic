package com.google.android.music.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils
{
  public static void addFragment(FragmentActivity paramFragmentActivity, Fragment paramFragment, Bundle paramBundle)
  {
    FragmentTransaction localFragmentTransaction1 = paramFragmentActivity.getSupportFragmentManager().beginTransaction();
    paramFragment.setArguments(paramBundle);
    FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.add(paramFragment, null);
    int i = localFragmentTransaction1.commitAllowingStateLoss();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.FragmentUtils
 * JD-Core Version:    0.6.2
 */