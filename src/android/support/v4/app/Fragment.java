package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.util.DebugUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;

public class Fragment
  implements ComponentCallbacks, View.OnCreateContextMenuListener
{
  static final int ACTIVITY_CREATED = 2;
  static final int CREATED = 1;
  static final int INITIALIZING = 0;
  static final int RESUMED = 5;
  static final int STARTED = 4;
  static final int STOPPED = 3;
  private static final HashMap<String, Class<?>> sClassMap = new HashMap();
  FragmentActivity mActivity;
  boolean mAdded;
  View mAnimatingAway;
  Bundle mArguments;
  int mBackStackNesting;
  boolean mCalled;
  boolean mCheckedForLoaderManager;
  FragmentManagerImpl mChildFragmentManager;
  ViewGroup mContainer;
  int mContainerId;
  boolean mDeferStart;
  boolean mDetached;
  int mFragmentId;
  FragmentManagerImpl mFragmentManager;
  boolean mFromLayout;
  boolean mHasMenu;
  boolean mHidden;
  boolean mInLayout;
  int mIndex = -1;
  View mInnerView;
  LoaderManagerImpl mLoaderManager;
  boolean mLoadersStarted;
  boolean mMenuVisible = true;
  int mNextAnim;
  Fragment mParentFragment;
  boolean mRemoving;
  boolean mRestored;
  boolean mResumed;
  boolean mRetainInstance;
  boolean mRetaining;
  Bundle mSavedFragmentState;
  SparseArray<Parcelable> mSavedViewState;
  int mState = 0;
  int mStateAfterAnimating;
  String mTag;
  Fragment mTarget;
  int mTargetIndex = -1;
  int mTargetRequestCode;
  boolean mUserVisibleHint = true;
  View mView;
  String mWho;

  public static Fragment instantiate(Context paramContext, String paramString)
  {
    return instantiate(paramContext, paramString, null);
  }

  public static Fragment instantiate(Context paramContext, String paramString, Bundle paramBundle)
  {
    try
    {
      Class localClass = (Class)sClassMap.get(paramString);
      if (localClass == null)
      {
        localClass = paramContext.getClassLoader().loadClass(paramString);
        Object localObject = sClassMap.put(paramString, localClass);
      }
      Fragment localFragment = (Fragment)localClass.newInstance();
      if (paramBundle != null)
      {
        ClassLoader localClassLoader = localFragment.getClass().getClassLoader();
        paramBundle.setClassLoader(localClassLoader);
        localFragment.mArguments = paramBundle;
      }
      return localFragment;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      String str1 = "Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an" + " empty constructor that is public";
      throw new InstantiationException(str1, localClassNotFoundException);
    }
    catch (InstantiationException localInstantiationException)
    {
      String str2 = "Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an" + " empty constructor that is public";
      throw new InstantiationException(str2, localInstantiationException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      String str3 = "Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an" + " empty constructor that is public";
      throw new InstantiationException(str3, localIllegalAccessException);
    }
  }

  static boolean isSupportFragmentClass(Context paramContext, String paramString)
  {
    try
    {
      Class localClass = (Class)sClassMap.get(paramString);
      if (localClass == null)
      {
        localClass = paramContext.getClassLoader().loadClass(paramString);
        Object localObject = sClassMap.put(paramString, localClass);
      }
      boolean bool1 = Fragment.class.isAssignableFrom(localClass);
      bool2 = bool1;
      return bool2;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      while (true)
        boolean bool2 = false;
    }
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mFragmentId=#");
    String str1 = Integer.toHexString(this.mFragmentId);
    paramPrintWriter.print(str1);
    paramPrintWriter.print(" mContainerId=#");
    String str2 = Integer.toHexString(this.mContainerId);
    paramPrintWriter.print(str2);
    paramPrintWriter.print(" mTag=");
    String str3 = this.mTag;
    paramPrintWriter.println(str3);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mState=");
    int i = this.mState;
    paramPrintWriter.print(i);
    paramPrintWriter.print(" mIndex=");
    int j = this.mIndex;
    paramPrintWriter.print(j);
    paramPrintWriter.print(" mWho=");
    String str4 = this.mWho;
    paramPrintWriter.print(str4);
    paramPrintWriter.print(" mBackStackNesting=");
    int k = this.mBackStackNesting;
    paramPrintWriter.println(k);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mAdded=");
    boolean bool1 = this.mAdded;
    paramPrintWriter.print(bool1);
    paramPrintWriter.print(" mRemoving=");
    boolean bool2 = this.mRemoving;
    paramPrintWriter.print(bool2);
    paramPrintWriter.print(" mResumed=");
    boolean bool3 = this.mResumed;
    paramPrintWriter.print(bool3);
    paramPrintWriter.print(" mFromLayout=");
    boolean bool4 = this.mFromLayout;
    paramPrintWriter.print(bool4);
    paramPrintWriter.print(" mInLayout=");
    boolean bool5 = this.mInLayout;
    paramPrintWriter.println(bool5);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mHidden=");
    boolean bool6 = this.mHidden;
    paramPrintWriter.print(bool6);
    paramPrintWriter.print(" mDetached=");
    boolean bool7 = this.mDetached;
    paramPrintWriter.print(bool7);
    paramPrintWriter.print(" mMenuVisible=");
    boolean bool8 = this.mMenuVisible;
    paramPrintWriter.print(bool8);
    paramPrintWriter.print(" mHasMenu=");
    boolean bool9 = this.mHasMenu;
    paramPrintWriter.println(bool9);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mRetainInstance=");
    boolean bool10 = this.mRetainInstance;
    paramPrintWriter.print(bool10);
    paramPrintWriter.print(" mRetaining=");
    boolean bool11 = this.mRetaining;
    paramPrintWriter.print(bool11);
    paramPrintWriter.print(" mUserVisibleHint=");
    boolean bool12 = this.mUserVisibleHint;
    paramPrintWriter.println(bool12);
    if (this.mFragmentManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mFragmentManager=");
      FragmentManagerImpl localFragmentManagerImpl1 = this.mFragmentManager;
      paramPrintWriter.println(localFragmentManagerImpl1);
    }
    if (this.mActivity != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mActivity=");
      FragmentActivity localFragmentActivity = this.mActivity;
      paramPrintWriter.println(localFragmentActivity);
    }
    if (this.mParentFragment != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mParentFragment=");
      Fragment localFragment1 = this.mParentFragment;
      paramPrintWriter.println(localFragment1);
    }
    if (this.mArguments != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mArguments=");
      Bundle localBundle1 = this.mArguments;
      paramPrintWriter.println(localBundle1);
    }
    if (this.mSavedFragmentState != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedFragmentState=");
      Bundle localBundle2 = this.mSavedFragmentState;
      paramPrintWriter.println(localBundle2);
    }
    if (this.mSavedViewState != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedViewState=");
      SparseArray localSparseArray = this.mSavedViewState;
      paramPrintWriter.println(localSparseArray);
    }
    if (this.mTarget != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mTarget=");
      Fragment localFragment2 = this.mTarget;
      paramPrintWriter.print(localFragment2);
      paramPrintWriter.print(" mTargetRequestCode=");
      int m = this.mTargetRequestCode;
      paramPrintWriter.println(m);
    }
    if (this.mNextAnim != 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mNextAnim=");
      int n = this.mNextAnim;
      paramPrintWriter.println(n);
    }
    if (this.mContainer != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mContainer=");
      ViewGroup localViewGroup = this.mContainer;
      paramPrintWriter.println(localViewGroup);
    }
    if (this.mView != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mView=");
      View localView1 = this.mView;
      paramPrintWriter.println(localView1);
    }
    if (this.mInnerView != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mInnerView=");
      View localView2 = this.mView;
      paramPrintWriter.println(localView2);
    }
    if (this.mAnimatingAway != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mAnimatingAway=");
      View localView3 = this.mAnimatingAway;
      paramPrintWriter.println(localView3);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStateAfterAnimating=");
      int i1 = this.mStateAfterAnimating;
      paramPrintWriter.println(i1);
    }
    if (this.mLoaderManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Loader Manager:");
      LoaderManagerImpl localLoaderManagerImpl = this.mLoaderManager;
      String str5 = paramString + "  ";
      localLoaderManagerImpl.dump(str5, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    if (this.mChildFragmentManager == null)
      return;
    paramPrintWriter.print(paramString);
    StringBuilder localStringBuilder = new StringBuilder().append("Child ");
    FragmentManagerImpl localFragmentManagerImpl2 = this.mChildFragmentManager;
    String str6 = localFragmentManagerImpl2 + ":";
    paramPrintWriter.println(str6);
    FragmentManagerImpl localFragmentManagerImpl3 = this.mChildFragmentManager;
    String str7 = paramString + "  ";
    localFragmentManagerImpl3.dump(str7, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
  }

  public final boolean equals(Object paramObject)
  {
    return super.equals(paramObject);
  }

  Fragment findFragmentByWho(String paramString)
  {
    String str = this.mWho;
    if (paramString.equals(str));
    while (true)
    {
      return this;
      if (this.mChildFragmentManager != null)
        this = this.mChildFragmentManager.findFragmentByWho(paramString);
      else
        this = null;
    }
  }

  public final FragmentActivity getActivity()
  {
    return this.mActivity;
  }

  public final Bundle getArguments()
  {
    return this.mArguments;
  }

  public final FragmentManager getChildFragmentManager()
  {
    if (this.mChildFragmentManager == null)
    {
      instantiateChildFragmentManager();
      if (this.mState < 5)
        break label31;
      this.mChildFragmentManager.dispatchResume();
    }
    while (true)
    {
      return this.mChildFragmentManager;
      label31: if (this.mState >= 4)
        this.mChildFragmentManager.dispatchStart();
      else if (this.mState >= 2)
        this.mChildFragmentManager.dispatchActivityCreated();
      else if (this.mState >= 1)
        this.mChildFragmentManager.dispatchCreate();
    }
  }

  public final FragmentManager getFragmentManager()
  {
    return this.mFragmentManager;
  }

  public final int getId()
  {
    return this.mFragmentId;
  }

  public LayoutInflater getLayoutInflater(Bundle paramBundle)
  {
    return this.mActivity.getLayoutInflater();
  }

  public LoaderManager getLoaderManager()
  {
    if (this.mLoaderManager != null);
    for (LoaderManagerImpl localLoaderManagerImpl1 = this.mLoaderManager; ; localLoaderManagerImpl1 = this.mLoaderManager)
    {
      return localLoaderManagerImpl1;
      if (this.mActivity == null)
      {
        String str1 = "Fragment " + this + " not attached to Activity";
        throw new IllegalStateException(str1);
      }
      this.mCheckedForLoaderManager = true;
      FragmentActivity localFragmentActivity = this.mActivity;
      String str2 = this.mWho;
      boolean bool = this.mLoadersStarted;
      LoaderManagerImpl localLoaderManagerImpl2 = localFragmentActivity.getLoaderManager(str2, bool, true);
      this.mLoaderManager = localLoaderManagerImpl2;
    }
  }

  public final Fragment getParentFragment()
  {
    return this.mParentFragment;
  }

  public final Resources getResources()
  {
    if (this.mActivity == null)
    {
      String str = "Fragment " + this + " not attached to Activity";
      throw new IllegalStateException(str);
    }
    return this.mActivity.getResources();
  }

  public final boolean getRetainInstance()
  {
    return this.mRetainInstance;
  }

  public final String getString(int paramInt)
  {
    return getResources().getString(paramInt);
  }

  public final String getString(int paramInt, Object[] paramArrayOfObject)
  {
    return getResources().getString(paramInt, paramArrayOfObject);
  }

  public final String getTag()
  {
    return this.mTag;
  }

  public final Fragment getTargetFragment()
  {
    return this.mTarget;
  }

  public final int getTargetRequestCode()
  {
    return this.mTargetRequestCode;
  }

  public final CharSequence getText(int paramInt)
  {
    return getResources().getText(paramInt);
  }

  public boolean getUserVisibleHint()
  {
    return this.mUserVisibleHint;
  }

  public View getView()
  {
    return this.mView;
  }

  public final boolean hasOptionsMenu()
  {
    return this.mHasMenu;
  }

  public final int hashCode()
  {
    return super.hashCode();
  }

  void initState()
  {
    this.mIndex = -1;
    this.mWho = null;
    this.mAdded = false;
    this.mRemoving = false;
    this.mResumed = false;
    this.mFromLayout = false;
    this.mInLayout = false;
    this.mRestored = false;
    this.mBackStackNesting = 0;
    this.mFragmentManager = null;
    this.mActivity = null;
    this.mFragmentId = 0;
    this.mContainerId = 0;
    this.mTag = null;
    this.mHidden = false;
    this.mDetached = false;
    this.mRetaining = false;
    this.mLoaderManager = null;
    this.mLoadersStarted = false;
    this.mCheckedForLoaderManager = false;
  }

  void instantiateChildFragmentManager()
  {
    FragmentManagerImpl localFragmentManagerImpl1 = new FragmentManagerImpl();
    this.mChildFragmentManager = localFragmentManagerImpl1;
    FragmentManagerImpl localFragmentManagerImpl2 = this.mChildFragmentManager;
    FragmentActivity localFragmentActivity = this.mActivity;
    FragmentContainer local1 = new FragmentContainer()
    {
      public View findViewById(int paramAnonymousInt)
      {
        if (Fragment.this.mView == null)
          throw new IllegalStateException("Fragment does not have a view");
        return Fragment.this.mView.findViewById(paramAnonymousInt);
      }
    };
    localFragmentManagerImpl2.attachActivity(localFragmentActivity, local1, this);
  }

  public final boolean isAdded()
  {
    if ((this.mActivity != null) && (this.mAdded));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final boolean isDetached()
  {
    return this.mDetached;
  }

  public final boolean isHidden()
  {
    return this.mHidden;
  }

  final boolean isInBackStack()
  {
    if (this.mBackStackNesting > 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public final boolean isInLayout()
  {
    return this.mInLayout;
  }

  public final boolean isMenuVisible()
  {
    return this.mMenuVisible;
  }

  public final boolean isRemoving()
  {
    return this.mRemoving;
  }

  public final boolean isResumed()
  {
    return this.mResumed;
  }

  public final boolean isVisible()
  {
    if ((isAdded()) && (!isHidden()) && (this.mView != null) && (this.mView.getWindowToken() != null) && (this.mView.getVisibility() == 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    this.mCalled = true;
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
  }

  public void onAttach(Activity paramActivity)
  {
    this.mCalled = true;
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    this.mCalled = true;
  }

  public boolean onContextItemSelected(MenuItem paramMenuItem)
  {
    return false;
  }

  public void onCreate(Bundle paramBundle)
  {
    this.mCalled = true;
  }

  public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2)
  {
    return null;
  }

  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    getActivity().onCreateContextMenu(paramContextMenu, paramView, paramContextMenuInfo);
  }

  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return null;
  }

  public void onDestroy()
  {
    this.mCalled = true;
    if (!this.mCheckedForLoaderManager)
    {
      this.mCheckedForLoaderManager = true;
      FragmentActivity localFragmentActivity = this.mActivity;
      String str = this.mWho;
      boolean bool = this.mLoadersStarted;
      LoaderManagerImpl localLoaderManagerImpl = localFragmentActivity.getLoaderManager(str, bool, false);
      this.mLoaderManager = localLoaderManagerImpl;
    }
    if (this.mLoaderManager == null)
      return;
    this.mLoaderManager.doDestroy();
  }

  public void onDestroyOptionsMenu()
  {
  }

  public void onDestroyView()
  {
    this.mCalled = true;
  }

  public void onDetach()
  {
    this.mCalled = true;
  }

  public void onHiddenChanged(boolean paramBoolean)
  {
  }

  public void onInflate(Activity paramActivity, AttributeSet paramAttributeSet, Bundle paramBundle)
  {
    this.mCalled = true;
  }

  public void onLowMemory()
  {
    this.mCalled = true;
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    return false;
  }

  public void onOptionsMenuClosed(Menu paramMenu)
  {
  }

  public void onPause()
  {
    this.mCalled = true;
  }

  public void onPrepareOptionsMenu(Menu paramMenu)
  {
  }

  public void onResume()
  {
    this.mCalled = true;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
  }

  public void onStart()
  {
    this.mCalled = true;
    if (this.mLoadersStarted)
      return;
    this.mLoadersStarted = true;
    if (!this.mCheckedForLoaderManager)
    {
      this.mCheckedForLoaderManager = true;
      FragmentActivity localFragmentActivity = this.mActivity;
      String str = this.mWho;
      boolean bool = this.mLoadersStarted;
      LoaderManagerImpl localLoaderManagerImpl = localFragmentActivity.getLoaderManager(str, bool, false);
      this.mLoaderManager = localLoaderManagerImpl;
    }
    if (this.mLoaderManager == null)
      return;
    this.mLoaderManager.doStart();
  }

  public void onStop()
  {
    this.mCalled = true;
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
  }

  public void onViewStateRestored(Bundle paramBundle)
  {
    this.mCalled = true;
  }

  void performActivityCreated(Bundle paramBundle)
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.noteStateNotSaved();
    this.mCalled = false;
    onActivityCreated(paramBundle);
    if (!this.mCalled)
    {
      String str = "Fragment " + this + " did not call through to super.onActivityCreated()";
      throw new SuperNotCalledException(str);
    }
    if (this.mChildFragmentManager == null)
      return;
    this.mChildFragmentManager.dispatchActivityCreated();
  }

  void performConfigurationChanged(Configuration paramConfiguration)
  {
    onConfigurationChanged(paramConfiguration);
    if (this.mChildFragmentManager == null)
      return;
    this.mChildFragmentManager.dispatchConfigurationChanged(paramConfiguration);
  }

  boolean performContextItemSelected(MenuItem paramMenuItem)
  {
    boolean bool = true;
    if (!this.mHidden)
      if (!onContextItemSelected(paramMenuItem));
    while (true)
    {
      return bool;
      if ((this.mChildFragmentManager == null) || (!this.mChildFragmentManager.dispatchContextItemSelected(paramMenuItem)))
        bool = false;
    }
  }

  void performCreate(Bundle paramBundle)
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.noteStateNotSaved();
    this.mCalled = false;
    onCreate(paramBundle);
    if (!this.mCalled)
    {
      String str = "Fragment " + this + " did not call through to super.onCreate()";
      throw new SuperNotCalledException(str);
    }
    if (paramBundle == null)
      return;
    Parcelable localParcelable = paramBundle.getParcelable("android:support:fragments");
    if (localParcelable == null)
      return;
    if (this.mChildFragmentManager == null)
      instantiateChildFragmentManager();
    this.mChildFragmentManager.restoreAllState(localParcelable, null);
    this.mChildFragmentManager.dispatchCreate();
  }

  boolean performCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater)
  {
    boolean bool1 = false;
    if (!this.mHidden)
    {
      if ((this.mHasMenu) && (this.mMenuVisible))
      {
        bool1 = true;
        onCreateOptionsMenu(paramMenu, paramMenuInflater);
      }
      if (this.mChildFragmentManager != null)
      {
        boolean bool2 = this.mChildFragmentManager.dispatchCreateOptionsMenu(paramMenu, paramMenuInflater);
        bool1 |= bool2;
      }
    }
    return bool1;
  }

  View performCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.noteStateNotSaved();
    return onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }

  void performDestroy()
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchDestroy();
    this.mCalled = false;
    onDestroy();
    if (this.mCalled)
      return;
    String str = "Fragment " + this + " did not call through to super.onDestroy()";
    throw new SuperNotCalledException(str);
  }

  void performDestroyView()
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchDestroyView();
    this.mCalled = false;
    onDestroyView();
    if (!this.mCalled)
    {
      String str = "Fragment " + this + " did not call through to super.onDestroyView()";
      throw new SuperNotCalledException(str);
    }
    if (this.mLoaderManager == null)
      return;
    this.mLoaderManager.doReportNextStart();
  }

  void performLowMemory()
  {
    onLowMemory();
    if (this.mChildFragmentManager == null)
      return;
    this.mChildFragmentManager.dispatchLowMemory();
  }

  boolean performOptionsItemSelected(MenuItem paramMenuItem)
  {
    boolean bool = true;
    if (!this.mHidden)
      if ((!this.mHasMenu) || (!this.mMenuVisible) || (!onOptionsItemSelected(paramMenuItem)));
    while (true)
    {
      return bool;
      if ((this.mChildFragmentManager == null) || (!this.mChildFragmentManager.dispatchOptionsItemSelected(paramMenuItem)))
        bool = false;
    }
  }

  void performOptionsMenuClosed(Menu paramMenu)
  {
    if (this.mHidden)
      return;
    if ((this.mHasMenu) && (this.mMenuVisible))
      onOptionsMenuClosed(paramMenu);
    if (this.mChildFragmentManager == null)
      return;
    this.mChildFragmentManager.dispatchOptionsMenuClosed(paramMenu);
  }

  void performPause()
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchPause();
    this.mCalled = false;
    onPause();
    if (this.mCalled)
      return;
    String str = "Fragment " + this + " did not call through to super.onPause()";
    throw new SuperNotCalledException(str);
  }

  boolean performPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool1 = false;
    if (!this.mHidden)
    {
      if ((this.mHasMenu) && (this.mMenuVisible))
      {
        bool1 = true;
        onPrepareOptionsMenu(paramMenu);
      }
      if (this.mChildFragmentManager != null)
      {
        boolean bool2 = this.mChildFragmentManager.dispatchPrepareOptionsMenu(paramMenu);
        bool1 |= bool2;
      }
    }
    return bool1;
  }

  void performReallyStop()
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchReallyStop();
    if (!this.mLoadersStarted)
      return;
    this.mLoadersStarted = false;
    if (!this.mCheckedForLoaderManager)
    {
      this.mCheckedForLoaderManager = true;
      FragmentActivity localFragmentActivity = this.mActivity;
      String str = this.mWho;
      boolean bool = this.mLoadersStarted;
      LoaderManagerImpl localLoaderManagerImpl = localFragmentActivity.getLoaderManager(str, bool, false);
      this.mLoaderManager = localLoaderManagerImpl;
    }
    if (this.mLoaderManager == null)
      return;
    if (!this.mActivity.mRetaining)
    {
      this.mLoaderManager.doStop();
      return;
    }
    this.mLoaderManager.doRetain();
  }

  void performResume()
  {
    if (this.mChildFragmentManager != null)
    {
      this.mChildFragmentManager.noteStateNotSaved();
      boolean bool1 = this.mChildFragmentManager.execPendingActions();
    }
    this.mCalled = false;
    onResume();
    if (!this.mCalled)
    {
      String str = "Fragment " + this + " did not call through to super.onResume()";
      throw new SuperNotCalledException(str);
    }
    if (this.mChildFragmentManager == null)
      return;
    this.mChildFragmentManager.dispatchResume();
    boolean bool2 = this.mChildFragmentManager.execPendingActions();
  }

  void performSaveInstanceState(Bundle paramBundle)
  {
    onSaveInstanceState(paramBundle);
    if (this.mChildFragmentManager == null)
      return;
    Parcelable localParcelable = this.mChildFragmentManager.saveAllState();
    if (localParcelable == null)
      return;
    paramBundle.putParcelable("android:support:fragments", localParcelable);
  }

  void performStart()
  {
    if (this.mChildFragmentManager != null)
    {
      this.mChildFragmentManager.noteStateNotSaved();
      boolean bool = this.mChildFragmentManager.execPendingActions();
    }
    this.mCalled = false;
    onStart();
    if (!this.mCalled)
    {
      String str = "Fragment " + this + " did not call through to super.onStart()";
      throw new SuperNotCalledException(str);
    }
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchStart();
    if (this.mLoaderManager == null)
      return;
    this.mLoaderManager.doReportStart();
  }

  void performStop()
  {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchStop();
    this.mCalled = false;
    onStop();
    if (this.mCalled)
      return;
    String str = "Fragment " + this + " did not call through to super.onStop()";
    throw new SuperNotCalledException(str);
  }

  public void registerForContextMenu(View paramView)
  {
    paramView.setOnCreateContextMenuListener(this);
  }

  final void restoreViewState(Bundle paramBundle)
  {
    if (this.mSavedViewState != null)
    {
      View localView = this.mInnerView;
      SparseArray localSparseArray = this.mSavedViewState;
      localView.restoreHierarchyState(localSparseArray);
      this.mSavedViewState = null;
    }
    this.mCalled = false;
    onViewStateRestored(paramBundle);
    if (this.mCalled)
      return;
    String str = "Fragment " + this + " did not call through to super.onViewStateRestored()";
    throw new SuperNotCalledException(str);
  }

  public void setArguments(Bundle paramBundle)
  {
    if (this.mIndex >= 0)
      throw new IllegalStateException("Fragment already active");
    this.mArguments = paramBundle;
  }

  public void setHasOptionsMenu(boolean paramBoolean)
  {
    if (this.mHasMenu != paramBoolean)
      return;
    this.mHasMenu = paramBoolean;
    if (!isAdded())
      return;
    if (isHidden())
      return;
    this.mActivity.supportInvalidateOptionsMenu();
  }

  final void setIndex(int paramInt, Fragment paramFragment)
  {
    this.mIndex = paramInt;
    if (paramFragment != null)
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      String str1 = paramFragment.mWho;
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(":");
      int i = this.mIndex;
      String str2 = i;
      this.mWho = str2;
      return;
    }
    StringBuilder localStringBuilder3 = new StringBuilder().append("android:fragment:");
    int j = this.mIndex;
    String str3 = j;
    this.mWho = str3;
  }

  public void setInitialSavedState(SavedState paramSavedState)
  {
    if (this.mIndex >= 0)
      throw new IllegalStateException("Fragment already active");
    if ((paramSavedState != null) && (paramSavedState.mState != null));
    for (Bundle localBundle = paramSavedState.mState; ; localBundle = null)
    {
      this.mSavedFragmentState = localBundle;
      return;
    }
  }

  public void setMenuVisibility(boolean paramBoolean)
  {
    if (this.mMenuVisible != paramBoolean)
      return;
    this.mMenuVisible = paramBoolean;
    if (!this.mHasMenu)
      return;
    if (!isAdded())
      return;
    if (isHidden())
      return;
    this.mActivity.supportInvalidateOptionsMenu();
  }

  public void setRetainInstance(boolean paramBoolean)
  {
    if ((paramBoolean) && (this.mParentFragment != null))
      throw new IllegalStateException("Can't retain fragements that are nested in other fragments");
    this.mRetainInstance = paramBoolean;
  }

  public void setTargetFragment(Fragment paramFragment, int paramInt)
  {
    this.mTarget = paramFragment;
    this.mTargetRequestCode = paramInt;
  }

  public void setUserVisibleHint(boolean paramBoolean)
  {
    if ((!this.mUserVisibleHint) && (paramBoolean) && (this.mState < 4))
      this.mFragmentManager.performPendingDeferredStart(this);
    this.mUserVisibleHint = paramBoolean;
    if (!paramBoolean);
    for (boolean bool = true; ; bool = false)
    {
      this.mDeferStart = bool;
      return;
    }
  }

  public void startActivity(Intent paramIntent)
  {
    if (this.mActivity == null)
    {
      String str = "Fragment " + this + " not attached to Activity";
      throw new IllegalStateException(str);
    }
    this.mActivity.startActivityFromFragment(this, paramIntent, -1);
  }

  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if (this.mActivity == null)
    {
      String str = "Fragment " + this + " not attached to Activity";
      throw new IllegalStateException(str);
    }
    this.mActivity.startActivityFromFragment(this, paramIntent, paramInt);
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    DebugUtils.buildShortClassTag(this, localStringBuilder1);
    if (this.mIndex >= 0)
    {
      StringBuilder localStringBuilder2 = localStringBuilder1.append(" #");
      int i = this.mIndex;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(i);
    }
    if (this.mFragmentId != 0)
    {
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" id=0x");
      String str1 = Integer.toHexString(this.mFragmentId);
      StringBuilder localStringBuilder5 = localStringBuilder1.append(str1);
    }
    if (this.mTag != null)
    {
      StringBuilder localStringBuilder6 = localStringBuilder1.append(" ");
      String str2 = this.mTag;
      StringBuilder localStringBuilder7 = localStringBuilder1.append(str2);
    }
    StringBuilder localStringBuilder8 = localStringBuilder1.append('}');
    return localStringBuilder1.toString();
  }

  public void unregisterForContextMenu(View paramView)
  {
    paramView.setOnCreateContextMenuListener(null);
  }

  public static class InstantiationException extends RuntimeException
  {
    public InstantiationException(String paramString, Exception paramException)
    {
      super(paramException);
    }
  }

  public static class SavedState
    implements Parcelable
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public Fragment.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new Fragment.SavedState(paramAnonymousParcel, null);
      }

      public Fragment.SavedState[] newArray(int paramAnonymousInt)
      {
        return new Fragment.SavedState[paramAnonymousInt];
      }
    };
    final Bundle mState;

    SavedState(Bundle paramBundle)
    {
      this.mState = paramBundle;
    }

    SavedState(Parcel paramParcel, ClassLoader paramClassLoader)
    {
      Bundle localBundle = paramParcel.readBundle();
      this.mState = localBundle;
      if (paramClassLoader == null)
        return;
      if (this.mState == null)
        return;
      this.mState.setClassLoader(paramClassLoader);
    }

    public int describeContents()
    {
      return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      Bundle localBundle = this.mState;
      paramParcel.writeBundle(localBundle);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.Fragment
 * JD-Core Version:    0.6.2
 */