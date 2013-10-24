package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class FragmentActivity extends Activity
{
  static final String FRAGMENTS_TAG = "android:support:fragments";
  private static final int HONEYCOMB = 11;
  static final int MSG_REALLY_STOPPED = 1;
  static final int MSG_RESUME_PENDING = 2;
  private static final String TAG = "FragmentActivity";
  HashMap<String, LoaderManagerImpl> mAllLoaderManagers;
  boolean mCheckedForLoaderManager;
  final FragmentContainer mContainer;
  boolean mCreated;
  final FragmentManagerImpl mFragments;
  final Handler mHandler;
  LoaderManagerImpl mLoaderManager;
  boolean mLoadersStarted;
  boolean mOptionsMenuInvalidated;
  boolean mReallyStopped;
  boolean mResumed;
  boolean mRetaining;
  boolean mStopped;

  public FragmentActivity()
  {
    Handler local1 = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        switch (paramAnonymousMessage.what)
        {
        default:
          super.handleMessage(paramAnonymousMessage);
          return;
        case 1:
          if (!FragmentActivity.this.mStopped)
            return;
          FragmentActivity.this.doReallyStop(false);
          return;
        case 2:
        }
        FragmentActivity.this.onResumeFragments();
        boolean bool = FragmentActivity.this.mFragments.execPendingActions();
      }
    };
    this.mHandler = local1;
    FragmentManagerImpl localFragmentManagerImpl = new FragmentManagerImpl();
    this.mFragments = localFragmentManagerImpl;
    FragmentContainer local2 = new FragmentContainer()
    {
      public View findViewById(int paramAnonymousInt)
      {
        return FragmentActivity.this.findViewById(paramAnonymousInt);
      }
    };
    this.mContainer = local2;
  }

  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView)
  {
    paramPrintWriter.print(paramString);
    if (paramView == null)
    {
      paramPrintWriter.println("null");
      return;
    }
    String str = viewToString(paramView);
    paramPrintWriter.println(str);
    if (!(paramView instanceof ViewGroup))
      return;
    ViewGroup localViewGroup = (ViewGroup)paramView;
    int i = localViewGroup.getChildCount();
    if (i <= 0)
      return;
    paramString = paramString + "  ";
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      View localView = localViewGroup.getChildAt(j);
      dumpViewHierarchy(paramString, paramPrintWriter, localView);
      j += 1;
    }
  }

  private static String viewToString(View paramView)
  {
    char c1 = 'F';
    char c2 = '.';
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    String str1 = paramView.getClass().getName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1);
    StringBuilder localStringBuilder3 = localStringBuilder1.append('{');
    String str2 = Integer.toHexString(System.identityHashCode(paramView));
    StringBuilder localStringBuilder4 = localStringBuilder1.append(str2);
    StringBuilder localStringBuilder5 = localStringBuilder1.append(' ');
    switch (paramView.getVisibility())
    {
    default:
      StringBuilder localStringBuilder6 = localStringBuilder1.append('.');
    case 0:
    case 4:
    case 8:
    }
    while (true)
    {
      char c3;
      label123: char c4;
      label142: char c5;
      label161: char c6;
      label180: char c7;
      label199: char c8;
      label218: char c9;
      label237: char c10;
      label260: label278: int n;
      Resources localResources;
      if (paramView.isFocusable())
      {
        c3 = 'F';
        StringBuilder localStringBuilder7 = localStringBuilder1.append(c3);
        if (!paramView.isEnabled())
          break label604;
        c4 = 'E';
        StringBuilder localStringBuilder8 = localStringBuilder1.append(c4);
        if (!paramView.willNotDraw())
          break label611;
        c5 = '.';
        StringBuilder localStringBuilder9 = localStringBuilder1.append(c5);
        if (!paramView.isHorizontalScrollBarEnabled())
          break label618;
        c6 = 'H';
        StringBuilder localStringBuilder10 = localStringBuilder1.append(c6);
        if (!paramView.isVerticalScrollBarEnabled())
          break label625;
        c7 = 'V';
        StringBuilder localStringBuilder11 = localStringBuilder1.append(c7);
        if (!paramView.isClickable())
          break label632;
        c8 = 'C';
        StringBuilder localStringBuilder12 = localStringBuilder1.append(c8);
        if (!paramView.isLongClickable())
          break label639;
        c9 = 'L';
        StringBuilder localStringBuilder13 = localStringBuilder1.append(c9);
        StringBuilder localStringBuilder14 = localStringBuilder1.append(' ');
        if (!paramView.isFocused())
          break label646;
        StringBuilder localStringBuilder15 = localStringBuilder1.append(c1);
        if (!paramView.isSelected())
          break label652;
        c10 = 'S';
        StringBuilder localStringBuilder16 = localStringBuilder1.append(c10);
        if (paramView.isPressed())
          c2 = 'P';
        StringBuilder localStringBuilder17 = localStringBuilder1.append(c2);
        StringBuilder localStringBuilder18 = localStringBuilder1.append(' ');
        int i = paramView.getLeft();
        StringBuilder localStringBuilder19 = localStringBuilder1.append(i);
        StringBuilder localStringBuilder20 = localStringBuilder1.append(',');
        int j = paramView.getTop();
        StringBuilder localStringBuilder21 = localStringBuilder1.append(j);
        StringBuilder localStringBuilder22 = localStringBuilder1.append('-');
        int k = paramView.getRight();
        StringBuilder localStringBuilder23 = localStringBuilder1.append(k);
        StringBuilder localStringBuilder24 = localStringBuilder1.append(',');
        int m = paramView.getBottom();
        StringBuilder localStringBuilder25 = localStringBuilder1.append(m);
        n = paramView.getId();
        if (n != -1)
        {
          StringBuilder localStringBuilder26 = localStringBuilder1.append(" #");
          String str3 = Integer.toHexString(n);
          StringBuilder localStringBuilder27 = localStringBuilder1.append(str3);
          localResources = paramView.getResources();
          if ((n != 0) && (localResources != null))
            switch (0xFF000000 & n)
            {
            default:
            case 2130706432:
            case 16777216:
            }
        }
      }
      try
      {
        String str4 = localResources.getResourcePackageName(n);
        while (true)
        {
          String str5 = localResources.getResourceTypeName(n);
          String str6 = localResources.getResourceEntryName(n);
          StringBuilder localStringBuilder28 = localStringBuilder1.append(" ");
          StringBuilder localStringBuilder29 = localStringBuilder1.append(str4);
          StringBuilder localStringBuilder30 = localStringBuilder1.append(":");
          StringBuilder localStringBuilder31 = localStringBuilder1.append(str5);
          StringBuilder localStringBuilder32 = localStringBuilder1.append("/");
          StringBuilder localStringBuilder33 = localStringBuilder1.append(str6);
          label551: StringBuilder localStringBuilder34 = localStringBuilder1.append("}");
          return localStringBuilder1.toString();
          StringBuilder localStringBuilder35 = localStringBuilder1.append('V');
          break;
          StringBuilder localStringBuilder36 = localStringBuilder1.append('I');
          break;
          StringBuilder localStringBuilder37 = localStringBuilder1.append('G');
          break;
          c3 = '.';
          break label123;
          label604: c4 = '.';
          break label142;
          label611: c5 = 'D';
          break label161;
          label618: c6 = '.';
          break label180;
          label625: c7 = '.';
          break label199;
          label632: c8 = '.';
          break label218;
          label639: c9 = '.';
          break label237;
          label646: c1 = '.';
          break label260;
          label652: c10 = '.';
          break label278;
          str4 = "app";
          continue;
          str4 = "android";
        }
      }
      catch (Resources.NotFoundException localNotFoundException)
      {
        break label551;
      }
    }
  }

  void doReallyStop(boolean paramBoolean)
  {
    if (this.mReallyStopped)
      return;
    this.mReallyStopped = true;
    this.mRetaining = paramBoolean;
    this.mHandler.removeMessages(1);
    onReallyStop();
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (Build.VERSION.SDK_INT >= 11);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local FragmentActivity ");
    String str1 = Integer.toHexString(System.identityHashCode(this));
    paramPrintWriter.print(str1);
    paramPrintWriter.println(" State:");
    String str2 = paramString + "  ";
    paramPrintWriter.print(str2);
    paramPrintWriter.print("mCreated=");
    boolean bool1 = this.mCreated;
    paramPrintWriter.print(bool1);
    paramPrintWriter.print("mResumed=");
    boolean bool2 = this.mResumed;
    paramPrintWriter.print(bool2);
    paramPrintWriter.print(" mStopped=");
    boolean bool3 = this.mStopped;
    paramPrintWriter.print(bool3);
    paramPrintWriter.print(" mReallyStopped=");
    boolean bool4 = this.mReallyStopped;
    paramPrintWriter.println(bool4);
    paramPrintWriter.print(str2);
    paramPrintWriter.print("mLoadersStarted=");
    boolean bool5 = this.mLoadersStarted;
    paramPrintWriter.println(bool5);
    if (this.mLoaderManager != null)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("Loader Manager ");
      String str3 = Integer.toHexString(System.identityHashCode(this.mLoaderManager));
      paramPrintWriter.print(str3);
      paramPrintWriter.println(":");
      LoaderManagerImpl localLoaderManagerImpl = this.mLoaderManager;
      String str4 = paramString + "  ";
      localLoaderManagerImpl.dump(str4, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }
    this.mFragments.dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    String str5 = paramString + "  ";
    View localView = getWindow().getDecorView();
    dumpViewHierarchy(str5, paramPrintWriter, localView);
  }

  public Object getLastCustomNonConfigurationInstance()
  {
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null);
    for (Object localObject = localNonConfigurationInstances.custom; ; localObject = null)
      return localObject;
  }

  LoaderManagerImpl getLoaderManager(String paramString, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (this.mAllLoaderManagers == null)
    {
      HashMap localHashMap = new HashMap();
      this.mAllLoaderManagers = localHashMap;
    }
    LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)this.mAllLoaderManagers.get(paramString);
    if (localLoaderManagerImpl == null)
      if (paramBoolean2)
      {
        localLoaderManagerImpl = new LoaderManagerImpl(paramString, this, paramBoolean1);
        Object localObject = this.mAllLoaderManagers.put(paramString, localLoaderManagerImpl);
      }
    while (true)
    {
      return localLoaderManagerImpl;
      localLoaderManagerImpl.updateActivity(this);
    }
  }

  public FragmentManager getSupportFragmentManager()
  {
    return this.mFragments;
  }

  public LoaderManager getSupportLoaderManager()
  {
    if (this.mLoaderManager != null);
    for (LoaderManagerImpl localLoaderManagerImpl1 = this.mLoaderManager; ; localLoaderManagerImpl1 = this.mLoaderManager)
    {
      return localLoaderManagerImpl1;
      this.mCheckedForLoaderManager = true;
      boolean bool = this.mLoadersStarted;
      LoaderManagerImpl localLoaderManagerImpl2 = getLoaderManager(null, bool, true);
      this.mLoaderManager = localLoaderManagerImpl2;
    }
  }

  void invalidateSupportFragment(String paramString)
  {
    if (this.mAllLoaderManagers == null)
      return;
    LoaderManagerImpl localLoaderManagerImpl = (LoaderManagerImpl)this.mAllLoaderManagers.get(paramString);
    if (localLoaderManagerImpl == null)
      return;
    if (localLoaderManagerImpl.mRetaining)
      return;
    localLoaderManagerImpl.doDestroy();
    Object localObject = this.mAllLoaderManagers.remove(paramString);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mFragments.noteStateNotSaved();
    int i = paramInt1 >> 16;
    if (i != 0)
    {
      i += -1;
      if ((this.mFragments.mActive != null) && (i >= 0))
      {
        int j = this.mFragments.mActive.size();
        if (i < j);
      }
      else
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Activity result fragment index out of range: 0x");
        String str1 = Integer.toHexString(paramInt1);
        String str2 = str1;
        int k = Log.w("FragmentActivity", str2);
        return;
      }
      Fragment localFragment = (Fragment)this.mFragments.mActive.get(i);
      if (localFragment == null)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Activity result no fragment exists for index: 0x");
        String str3 = Integer.toHexString(paramInt1);
        String str4 = str3;
        int m = Log.w("FragmentActivity", str4);
        return;
      }
      int n = 0xFFFF & paramInt1;
      localFragment.onActivityResult(n, paramInt2, paramIntent);
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onAttachFragment(Fragment paramFragment)
  {
  }

  public void onBackPressed()
  {
    if (this.mFragments.popBackStackImmediate())
      return;
    finish();
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mFragments.dispatchConfigurationChanged(paramConfiguration);
  }

  protected void onCreate(Bundle paramBundle)
  {
    Object localObject = null;
    FragmentManagerImpl localFragmentManagerImpl1 = this.mFragments;
    FragmentContainer localFragmentContainer = this.mContainer;
    localFragmentManagerImpl1.attachActivity(this, localFragmentContainer, (Fragment)localObject);
    if (getLayoutInflater().getFactory() == null)
      getLayoutInflater().setFactory(this);
    super.onCreate(paramBundle);
    NonConfigurationInstances localNonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (localNonConfigurationInstances != null)
    {
      HashMap localHashMap = localNonConfigurationInstances.loaders;
      this.mAllLoaderManagers = localHashMap;
    }
    if (paramBundle != null)
    {
      Parcelable localParcelable = paramBundle.getParcelable("android:support:fragments");
      FragmentManagerImpl localFragmentManagerImpl2 = this.mFragments;
      if (localNonConfigurationInstances != null)
        localObject = localNonConfigurationInstances.fragments;
      localFragmentManagerImpl2.restoreAllState(localParcelable, (ArrayList)localObject);
    }
    this.mFragments.dispatchCreate();
  }

  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu)
  {
    boolean bool3;
    if (paramInt == 0)
    {
      boolean bool1 = super.onCreatePanelMenu(paramInt, paramMenu);
      FragmentManagerImpl localFragmentManagerImpl = this.mFragments;
      MenuInflater localMenuInflater = getMenuInflater();
      boolean bool2 = localFragmentManagerImpl.dispatchCreateOptionsMenu(paramMenu, localMenuInflater);
      bool3 = bool1 | bool2;
      if (Build.VERSION.SDK_INT < 11);
    }
    while (true)
    {
      return bool3;
      bool3 = true;
      continue;
      bool3 = super.onCreatePanelMenu(paramInt, paramMenu);
    }
  }

  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet)
  {
    Fragment localFragment = null;
    int i = 0;
    View localView;
    if (!"fragment".equals(paramString))
      localView = super.onCreateView(paramString, paramContext, paramAttributeSet);
    while (true)
    {
      return localView;
      String str1 = paramAttributeSet.getAttributeValue(null, "class");
      int[] arrayOfInt = FragmentTag.Fragment;
      TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt);
      if (str1 == null)
        str1 = localTypedArray.getString(0);
      int j = localTypedArray.getResourceId(1, -1);
      String str2 = localTypedArray.getString(2);
      localTypedArray.recycle();
      if (!Fragment.isSupportFragmentClass(this, str1))
      {
        localView = super.onCreateView(paramString, paramContext, paramAttributeSet);
      }
      else
      {
        Object localObject = null;
        if (localObject != null)
          i = localObject.getId();
        if ((i == -1) && (j == -1) && (str2 == null))
        {
          StringBuilder localStringBuilder1 = new StringBuilder();
          String str3 = paramAttributeSet.getPositionDescription();
          String str4 = str3 + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str1;
          throw new IllegalArgumentException(str4);
        }
        if (j != -1)
          localFragment = this.mFragments.findFragmentById(j);
        if ((localFragment == null) && (str2 != null))
          localFragment = this.mFragments.findFragmentByTag(str2);
        if ((localFragment == null) && (i != -1))
          localFragment = this.mFragments.findFragmentById(i);
        if (FragmentManagerImpl.DEBUG)
        {
          StringBuilder localStringBuilder2 = new StringBuilder().append("onCreateView: id=0x");
          String str5 = Integer.toHexString(j);
          String str6 = str5 + " fname=" + str1 + " existing=" + localFragment;
          int k = Log.v("FragmentActivity", str6);
        }
        int m;
        if (localFragment == null)
        {
          localFragment = Fragment.instantiate(this, str1);
          localFragment.mFromLayout = true;
          if (j != 0)
          {
            m = j;
            localFragment.mFragmentId = m;
            localFragment.mContainerId = i;
            localFragment.mTag = str2;
            localFragment.mInLayout = true;
            FragmentManagerImpl localFragmentManagerImpl = this.mFragments;
            localFragment.mFragmentManager = localFragmentManagerImpl;
            Bundle localBundle1 = localFragment.mSavedFragmentState;
            localFragment.onInflate(this, paramAttributeSet, localBundle1);
            this.mFragments.addFragment(localFragment, true);
          }
        }
        while (true)
        {
          if (localFragment.mView != null)
            break label631;
          String str7 = "Fragment " + str1 + " did not create a view.";
          throw new IllegalStateException(str7);
          m = i;
          break;
          if (localFragment.mInLayout)
          {
            StringBuilder localStringBuilder3 = new StringBuilder();
            String str8 = paramAttributeSet.getPositionDescription();
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str8).append(": Duplicate id 0x");
            String str9 = Integer.toHexString(j);
            StringBuilder localStringBuilder5 = localStringBuilder4.append(str9).append(", tag ").append(str2).append(", or parent id 0x");
            String str10 = Integer.toHexString(i);
            String str11 = str10 + " with another fragment for " + str1;
            throw new IllegalArgumentException(str11);
          }
          localFragment.mInLayout = true;
          if (!localFragment.mRetaining)
          {
            Bundle localBundle2 = localFragment.mSavedFragmentState;
            localFragment.onInflate(this, paramAttributeSet, localBundle2);
          }
          this.mFragments.moveToState(localFragment);
        }
        label631: if (j != 0)
          localFragment.mView.setId(j);
        if (localFragment.mView.getTag() == null)
          localFragment.mView.setTag(str2);
        localView = localFragment.mView;
      }
    }
  }

  protected void onDestroy()
  {
    super.onDestroy();
    doReallyStop(false);
    this.mFragments.dispatchDestroy();
    if (this.mLoaderManager == null)
      return;
    this.mLoaderManager.doDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((Build.VERSION.SDK_INT < 5) && (paramInt == 4) && (paramKeyEvent.getRepeatCount() == 0))
      onBackPressed();
    for (boolean bool = true; ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      return bool;
  }

  public void onLowMemory()
  {
    super.onLowMemory();
    this.mFragments.dispatchLowMemory();
  }

  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    boolean bool;
    if (super.onMenuItemSelected(paramInt, paramMenuItem))
      bool = true;
    while (true)
    {
      return bool;
      switch (paramInt)
      {
      default:
        bool = false;
        break;
      case 0:
        bool = this.mFragments.dispatchOptionsItemSelected(paramMenuItem);
        break;
      case 6:
        bool = this.mFragments.dispatchContextItemSelected(paramMenuItem);
      }
    }
  }

  protected void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    this.mFragments.noteStateNotSaved();
  }

  public void onPanelClosed(int paramInt, Menu paramMenu)
  {
    switch (paramInt)
    {
    default:
    case 0:
    }
    while (true)
    {
      super.onPanelClosed(paramInt, paramMenu);
      return;
      this.mFragments.dispatchOptionsMenuClosed(paramMenu);
    }
  }

  protected void onPause()
  {
    super.onPause();
    this.mResumed = false;
    if (this.mHandler.hasMessages(2))
    {
      this.mHandler.removeMessages(2);
      onResumeFragments();
    }
    this.mFragments.dispatchPause();
  }

  protected void onPostResume()
  {
    super.onPostResume();
    this.mHandler.removeMessages(2);
    onResumeFragments();
    boolean bool = this.mFragments.execPendingActions();
  }

  protected boolean onPrepareOptionsPanel(View paramView, Menu paramMenu)
  {
    return super.onPreparePanel(0, paramView, paramMenu);
  }

  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu)
  {
    boolean bool2;
    boolean bool3;
    if ((paramInt == 0) && (paramMenu != null))
    {
      if (this.mOptionsMenuInvalidated)
      {
        this.mOptionsMenuInvalidated = false;
        paramMenu.clear();
        boolean bool1 = onCreatePanelMenu(paramInt, paramMenu);
      }
      bool2 = onPrepareOptionsPanel(paramView, paramMenu);
      bool3 = this.mFragments.dispatchPrepareOptionsMenu(paramMenu);
    }
    for (boolean bool4 = bool2 | bool3; ; bool4 = super.onPreparePanel(paramInt, paramView, paramMenu))
      return bool4;
  }

  void onReallyStop()
  {
    if (this.mLoadersStarted)
    {
      this.mLoadersStarted = false;
      if (this.mLoaderManager != null)
      {
        if (this.mRetaining)
          break label41;
        this.mLoaderManager.doStop();
      }
    }
    while (true)
    {
      this.mFragments.dispatchReallyStop();
      return;
      label41: this.mLoaderManager.doRetain();
    }
  }

  protected void onResume()
  {
    super.onResume();
    boolean bool1 = this.mHandler.sendEmptyMessage(2);
    this.mResumed = true;
    boolean bool2 = this.mFragments.execPendingActions();
  }

  protected void onResumeFragments()
  {
    this.mFragments.dispatchResume();
  }

  public Object onRetainCustomNonConfigurationInstance()
  {
    return null;
  }

  public final Object onRetainNonConfigurationInstance()
  {
    if (this.mStopped)
      doReallyStop(true);
    Object localObject1 = onRetainCustomNonConfigurationInstance();
    ArrayList localArrayList = this.mFragments.retainNonConfig();
    int i = 0;
    if (this.mAllLoaderManagers != null)
    {
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[this.mAllLoaderManagers.size()];
      Object[] arrayOfObject = this.mAllLoaderManagers.values().toArray(arrayOfLoaderManagerImpl);
      if (arrayOfLoaderManagerImpl != null)
      {
        int j = 0;
        int k = arrayOfLoaderManagerImpl.length;
        if (j < k)
        {
          LoaderManagerImpl localLoaderManagerImpl = arrayOfLoaderManagerImpl[j];
          if (localLoaderManagerImpl.mRetaining)
            i = 1;
          while (true)
          {
            j += 1;
            break;
            localLoaderManagerImpl.doDestroy();
            HashMap localHashMap1 = this.mAllLoaderManagers;
            String str = localLoaderManagerImpl.mWho;
            Object localObject2 = localHashMap1.remove(str);
          }
        }
      }
    }
    Object localObject3;
    if ((localArrayList == null) && (i == 0) && (localObject1 == null))
      localObject3 = null;
    while (true)
    {
      return localObject3;
      localObject3 = new NonConfigurationInstances();
      ((NonConfigurationInstances)localObject3).activity = null;
      ((NonConfigurationInstances)localObject3).custom = localObject1;
      ((NonConfigurationInstances)localObject3).children = null;
      ((NonConfigurationInstances)localObject3).fragments = localArrayList;
      HashMap localHashMap2 = this.mAllLoaderManagers;
      ((NonConfigurationInstances)localObject3).loaders = localHashMap2;
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    Parcelable localParcelable = this.mFragments.saveAllState();
    if (localParcelable == null)
      return;
    paramBundle.putParcelable("android:support:fragments", localParcelable);
  }

  protected void onStart()
  {
    super.onStart();
    this.mStopped = false;
    this.mReallyStopped = false;
    this.mHandler.removeMessages(1);
    if (!this.mCreated)
    {
      this.mCreated = true;
      this.mFragments.dispatchActivityCreated();
    }
    this.mFragments.noteStateNotSaved();
    boolean bool1 = this.mFragments.execPendingActions();
    if (!this.mLoadersStarted)
    {
      this.mLoadersStarted = true;
      if (this.mLoaderManager == null)
        break label172;
      this.mLoaderManager.doStart();
    }
    while (true)
    {
      this.mCheckedForLoaderManager = true;
      this.mFragments.dispatchStart();
      if (this.mAllLoaderManagers == null)
        return;
      LoaderManagerImpl[] arrayOfLoaderManagerImpl = new LoaderManagerImpl[this.mAllLoaderManagers.size()];
      Object[] arrayOfObject = this.mAllLoaderManagers.values().toArray(arrayOfLoaderManagerImpl);
      if (arrayOfLoaderManagerImpl == null)
        return;
      int i = 0;
      while (true)
      {
        int j = arrayOfLoaderManagerImpl.length;
        if (i >= j)
          return;
        LoaderManagerImpl localLoaderManagerImpl1 = arrayOfLoaderManagerImpl[i];
        localLoaderManagerImpl1.finishRetain();
        localLoaderManagerImpl1.doReportStart();
        i += 1;
      }
      label172: if (!this.mCheckedForLoaderManager)
      {
        boolean bool2 = this.mLoadersStarted;
        LoaderManagerImpl localLoaderManagerImpl2 = getLoaderManager(null, bool2, false);
        this.mLoaderManager = localLoaderManagerImpl2;
        if ((this.mLoaderManager != null) && (!this.mLoaderManager.mStarted))
          this.mLoaderManager.doStart();
      }
    }
  }

  protected void onStop()
  {
    super.onStop();
    this.mStopped = true;
    boolean bool = this.mHandler.sendEmptyMessage(1);
    this.mFragments.dispatchStop();
  }

  public void startActivityForResult(Intent paramIntent, int paramInt)
  {
    if ((paramInt != -1) && ((0xFFFF0000 & paramInt) != 0))
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
    super.startActivityForResult(paramIntent, paramInt);
  }

  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt)
  {
    if (paramInt == -1)
    {
      super.startActivityForResult(paramIntent, -1);
      return;
    }
    if ((0xFFFF0000 & paramInt) != 0)
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
    int i = paramFragment.mIndex + 1 << 16;
    int j = 0xFFFF & paramInt;
    int k = i + j;
    super.startActivityForResult(paramIntent, k);
  }

  public void supportInvalidateOptionsMenu()
  {
    if (Build.VERSION.SDK_INT >= 11)
    {
      ActivityCompatHoneycomb.invalidateOptionsMenu(this);
      return;
    }
    this.mOptionsMenuInvalidated = true;
  }

  static class FragmentTag
  {
    public static final int[] Fragment = { 16842755, 16842960, 16842961 };
    public static final int Fragment_id = 1;
    public static final int Fragment_name = 0;
    public static final int Fragment_tag = 2;
  }

  static final class NonConfigurationInstances
  {
    Object activity;
    HashMap<String, Object> children;
    Object custom;
    ArrayList<Fragment> fragments;
    HashMap<String, LoaderManagerImpl> loaders;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentActivity
 * JD-Core Version:    0.6.2
 */