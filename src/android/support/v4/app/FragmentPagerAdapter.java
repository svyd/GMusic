package android.support.v4.app;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class FragmentPagerAdapter extends PagerAdapter
{
  private static final boolean DEBUG = false;
  private static final String TAG = "FragmentPagerAdapter";
  private FragmentTransaction mCurTransaction = null;
  private Fragment mCurrentPrimaryItem = null;
  private final FragmentManager mFragmentManager;

  public FragmentPagerAdapter(FragmentManager paramFragmentManager)
  {
    this.mFragmentManager = paramFragmentManager;
  }

  private static String makeFragmentName(int paramInt, long paramLong)
  {
    return "android:switcher:" + paramInt + ":" + paramLong;
  }

  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    if (this.mCurTransaction == null)
    {
      FragmentTransaction localFragmentTransaction1 = this.mFragmentManager.beginTransaction();
      this.mCurTransaction = localFragmentTransaction1;
    }
    FragmentTransaction localFragmentTransaction2 = this.mCurTransaction;
    Fragment localFragment = (Fragment)paramObject;
    FragmentTransaction localFragmentTransaction3 = localFragmentTransaction2.detach(localFragment);
  }

  public void finishUpdate(ViewGroup paramViewGroup)
  {
    if (this.mCurTransaction == null)
      return;
    int i = this.mCurTransaction.commitAllowingStateLoss();
    this.mCurTransaction = null;
    boolean bool = this.mFragmentManager.executePendingTransactions();
  }

  public abstract Fragment getItem(int paramInt);

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    if (this.mCurTransaction == null)
    {
      FragmentTransaction localFragmentTransaction1 = this.mFragmentManager.beginTransaction();
      this.mCurTransaction = localFragmentTransaction1;
    }
    long l = getItemId(paramInt);
    String str1 = makeFragmentName(paramViewGroup.getId(), l);
    Fragment localFragment1 = this.mFragmentManager.findFragmentByTag(str1);
    if (localFragment1 != null)
      FragmentTransaction localFragmentTransaction2 = this.mCurTransaction.attach(localFragment1);
    while (true)
    {
      Fragment localFragment2 = this.mCurrentPrimaryItem;
      if (localFragment1 != localFragment2)
      {
        localFragment1.setMenuVisibility(false);
        localFragment1.setUserVisibleHint(false);
      }
      return localFragment1;
      localFragment1 = getItem(paramInt);
      FragmentTransaction localFragmentTransaction3 = this.mCurTransaction;
      int i = paramViewGroup.getId();
      String str2 = makeFragmentName(paramViewGroup.getId(), l);
      FragmentTransaction localFragmentTransaction4 = localFragmentTransaction3.add(i, localFragment1, str2);
    }
  }

  public boolean isViewFromObject(View paramView, Object paramObject)
  {
    if (((Fragment)paramObject).getView() == paramView);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void restoreState(Parcelable paramParcelable, ClassLoader paramClassLoader)
  {
  }

  public Parcelable saveState()
  {
    return null;
  }

  public void setPrimaryItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    Fragment localFragment1 = (Fragment)paramObject;
    Fragment localFragment2 = this.mCurrentPrimaryItem;
    if (localFragment1 == localFragment2)
      return;
    if (this.mCurrentPrimaryItem != null)
    {
      this.mCurrentPrimaryItem.setMenuVisibility(false);
      this.mCurrentPrimaryItem.setUserVisibleHint(false);
    }
    if (localFragment1 != null)
    {
      localFragment1.setMenuVisibility(true);
      localFragment1.setUserVisibleHint(true);
    }
    this.mCurrentPrimaryItem = localFragment1;
  }

  public void startUpdate(ViewGroup paramViewGroup)
  {
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentPagerAdapter
 * JD-Core Version:    0.6.2
 */