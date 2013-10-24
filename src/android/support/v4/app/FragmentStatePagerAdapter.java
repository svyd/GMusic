package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class FragmentStatePagerAdapter extends PagerAdapter
{
  private static final boolean DEBUG = false;
  private static final String TAG = "FragmentStatePagerAdapter";
  private FragmentTransaction mCurTransaction = null;
  private Fragment mCurrentPrimaryItem;
  private final FragmentManager mFragmentManager;
  private ArrayList<Fragment> mFragments;
  private ArrayList<Fragment.SavedState> mSavedState;

  public FragmentStatePagerAdapter(FragmentManager paramFragmentManager)
  {
    ArrayList localArrayList1 = new ArrayList();
    this.mSavedState = localArrayList1;
    ArrayList localArrayList2 = new ArrayList();
    this.mFragments = localArrayList2;
    this.mCurrentPrimaryItem = null;
    this.mFragmentManager = paramFragmentManager;
  }

  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject)
  {
    Fragment localFragment = (Fragment)paramObject;
    if (this.mCurTransaction == null)
    {
      FragmentTransaction localFragmentTransaction1 = this.mFragmentManager.beginTransaction();
      this.mCurTransaction = localFragmentTransaction1;
    }
    while (this.mSavedState.size() <= paramInt)
      boolean bool = this.mSavedState.add(null);
    ArrayList localArrayList = this.mSavedState;
    Fragment.SavedState localSavedState = this.mFragmentManager.saveFragmentInstanceState(localFragment);
    Object localObject1 = localArrayList.set(paramInt, localSavedState);
    Object localObject2 = this.mFragments.set(paramInt, null);
    FragmentTransaction localFragmentTransaction2 = this.mCurTransaction.remove(localFragment);
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

  public Object instantiateItem(ViewGroup paramViewGroup, int paramInt)
  {
    Object localObject1;
    if (this.mFragments.size() > paramInt)
    {
      localObject1 = (Fragment)this.mFragments.get(paramInt);
      if (localObject1 == null);
    }
    while (true)
    {
      return localObject1;
      if (this.mCurTransaction == null)
      {
        FragmentTransaction localFragmentTransaction1 = this.mFragmentManager.beginTransaction();
        this.mCurTransaction = localFragmentTransaction1;
      }
      Fragment localFragment = getItem(paramInt);
      if (this.mSavedState.size() > paramInt)
      {
        Fragment.SavedState localSavedState = (Fragment.SavedState)this.mSavedState.get(paramInt);
        if (localSavedState != null)
          localFragment.setInitialSavedState(localSavedState);
      }
      while (this.mFragments.size() <= paramInt)
        boolean bool = this.mFragments.add(null);
      localFragment.setMenuVisibility(false);
      localFragment.setUserVisibleHint(false);
      Object localObject2 = this.mFragments.set(paramInt, localFragment);
      FragmentTransaction localFragmentTransaction2 = this.mCurTransaction;
      int i = paramViewGroup.getId();
      FragmentTransaction localFragmentTransaction3 = localFragmentTransaction2.add(i, localFragment);
      localObject1 = localFragment;
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
    if (paramParcelable == null)
      return;
    Bundle localBundle = (Bundle)paramParcelable;
    localBundle.setClassLoader(paramClassLoader);
    Parcelable[] arrayOfParcelable = localBundle.getParcelableArray("states");
    this.mSavedState.clear();
    this.mFragments.clear();
    if (arrayOfParcelable != null)
    {
      int i = 0;
      while (true)
      {
        int j = arrayOfParcelable.length;
        if (i >= j)
          break;
        ArrayList localArrayList = this.mSavedState;
        Fragment.SavedState localSavedState = (Fragment.SavedState)arrayOfParcelable[i];
        boolean bool1 = localArrayList.add(localSavedState);
        i += 1;
      }
    }
    Iterator localIterator = localBundle.keySet().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      String str1 = (String)localIterator.next();
      if (str1.startsWith("f"))
      {
        int k = Integer.parseInt(str1.substring(1));
        Fragment localFragment = this.mFragmentManager.getFragment(localBundle, str1);
        if (localFragment != null)
        {
          while (this.mFragments.size() <= k)
            boolean bool2 = this.mFragments.add(null);
          localFragment.setMenuVisibility(false);
          Object localObject = this.mFragments.set(k, localFragment);
        }
        else
        {
          String str2 = "Bad fragment at key " + str1;
          int m = Log.w("FragmentStatePagerAdapter", str2);
        }
      }
    }
  }

  public Parcelable saveState()
  {
    Bundle localBundle = null;
    if (this.mSavedState.size() > 0)
    {
      localBundle = new Bundle();
      Fragment.SavedState[] arrayOfSavedState = new Fragment.SavedState[this.mSavedState.size()];
      Object[] arrayOfObject = this.mSavedState.toArray(arrayOfSavedState);
      localBundle.putParcelableArray("states", arrayOfSavedState);
    }
    int i = 0;
    while (true)
    {
      int j = this.mFragments.size();
      if (i >= j)
        break;
      Fragment localFragment = (Fragment)this.mFragments.get(i);
      if (localFragment != null)
      {
        if (localBundle == null)
          localBundle = new Bundle();
        String str = "f" + i;
        this.mFragmentManager.putFragment(localBundle, str, localFragment);
      }
      i += 1;
    }
    return localBundle;
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
 * Qualified Name:     android.support.v4.app.FragmentStatePagerAdapter
 * JD-Core Version:    0.6.2
 */