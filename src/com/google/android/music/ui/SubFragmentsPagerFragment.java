package com.google.android.music.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.music.log.Log;
import java.util.ArrayList;

public class SubFragmentsPagerFragment extends BaseFragment
{
  private MyPageAdapter mAdapter;
  private String mDefaultTab;
  private ArrayList<FragmentTabInfo> mTabs;

  private int getDefaultTabIndex()
  {
    int i = this.mTabs.size();
    int j = 0;
    if (j < i)
    {
      String str1 = ((FragmentTabInfo)this.mTabs.get(j)).mTag;
      String str2 = this.mDefaultTab;
      if (!str1.equals(str2));
    }
    while (true)
    {
      return j;
      j += 1;
      break;
      StringBuilder localStringBuilder = new StringBuilder().append("Default tab not found: ");
      String str3 = this.mDefaultTab;
      String str4 = str3;
      Log.e("SubFgtPagerFragment", str4);
      j = 0;
    }
  }

  protected int getLayoutId()
  {
    return 2130968648;
  }

  protected void init(ArrayList<FragmentTabInfo> paramArrayList, String paramString)
  {
    this.mTabs = paramArrayList;
    this.mDefaultTab = paramString;
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    int i = getLayoutId();
    View localView = paramLayoutInflater.inflate(i, paramViewGroup, false);
    final PlayTabContainer localPlayTabContainer = (PlayTabContainer)localView.findViewById(2131296460);
    ViewPager localViewPager = (ViewPager)localView.findViewById(2131296462);
    FragmentManager localFragmentManager = getChildFragmentManager();
    MyPageAdapter localMyPageAdapter1 = new MyPageAdapter(localFragmentManager);
    this.mAdapter = localMyPageAdapter1;
    MyPageAdapter localMyPageAdapter2 = this.mAdapter;
    localViewPager.setAdapter(localMyPageAdapter2);
    localViewPager.setOffscreenPageLimit(3);
    int j = getDefaultTabIndex();
    localViewPager.setCurrentItem(j);
    ViewPager.OnPageChangeListener local1 = new ViewPager.OnPageChangeListener()
    {
      public void onPageScrollStateChanged(int paramAnonymousInt)
      {
        localPlayTabContainer.onPageScrollStateChanged(paramAnonymousInt);
      }

      public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2)
      {
        localPlayTabContainer.onPageScrolled(paramAnonymousInt1, paramAnonymousFloat, paramAnonymousInt2);
      }

      public void onPageSelected(int paramAnonymousInt)
      {
        localPlayTabContainer.onPageSelected(paramAnonymousInt);
      }
    };
    localViewPager.setOnPageChangeListener(local1);
    int k = getResources().getColor(2131427356);
    localPlayTabContainer.setSelectedIndicatorColor(k);
    localPlayTabContainer.setViewPager(localViewPager);
    localPlayTabContainer.onPageSelected(j);
    return localView;
  }

  private class MyPageAdapter extends FragmentPagerAdapter
  {
    public MyPageAdapter(FragmentManager arg2)
    {
      super();
    }

    public int getCount()
    {
      return SubFragmentsPagerFragment.this.mTabs.size();
    }

    public Fragment getItem(int paramInt)
    {
      FragmentInfo localFragmentInfo = ((FragmentTabInfo)SubFragmentsPagerFragment.this.mTabs.get(paramInt)).mFragmentInfo;
      FragmentActivity localFragmentActivity = SubFragmentsPagerFragment.this.getActivity();
      return localFragmentInfo.instantiate(localFragmentActivity);
    }

    public CharSequence getPageTitle(int paramInt)
    {
      Resources localResources = SubFragmentsPagerFragment.this.getResources();
      int i = ((FragmentTabInfo)SubFragmentsPagerFragment.this.mTabs.get(paramInt)).mResId;
      return localResources.getString(i).toUpperCase();
    }

    public float getPageWidth(int paramInt)
    {
      return ((FragmentTabInfo)SubFragmentsPagerFragment.this.mTabs.get(paramInt)).mPageWidth;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SubFragmentsPagerFragment
 * JD-Core Version:    0.6.2
 */