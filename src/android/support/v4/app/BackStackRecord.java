package android.support.v4.app;

import android.support.v4.util.LogWriter;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

final class BackStackRecord extends FragmentTransaction
  implements FragmentManager.BackStackEntry, Runnable
{
  static final int OP_ADD = 1;
  static final int OP_ATTACH = 7;
  static final int OP_DETACH = 6;
  static final int OP_HIDE = 4;
  static final int OP_NULL = 0;
  static final int OP_REMOVE = 3;
  static final int OP_REPLACE = 2;
  static final int OP_SHOW = 5;
  static final String TAG = "FragmentManager";
  boolean mAddToBackStack;
  boolean mAllowAddToBackStack = true;
  int mBreadCrumbShortTitleRes;
  CharSequence mBreadCrumbShortTitleText;
  int mBreadCrumbTitleRes;
  CharSequence mBreadCrumbTitleText;
  boolean mCommitted;
  int mEnterAnim;
  int mExitAnim;
  Op mHead;
  int mIndex = -1;
  final FragmentManagerImpl mManager;
  String mName;
  int mNumOp;
  int mPopEnterAnim;
  int mPopExitAnim;
  Op mTail;
  int mTransition;
  int mTransitionStyle;

  public BackStackRecord(FragmentManagerImpl paramFragmentManagerImpl)
  {
    this.mManager = paramFragmentManagerImpl;
  }

  private void doAddOp(int paramInt1, Fragment paramFragment, String paramString, int paramInt2)
  {
    FragmentManagerImpl localFragmentManagerImpl = this.mManager;
    paramFragment.mFragmentManager = localFragmentManagerImpl;
    if (paramString != null)
    {
      if (paramFragment.mTag != null)
      {
        String str1 = paramFragment.mTag;
        if (!paramString.equals(str1))
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Can't change tag of fragment ").append(paramFragment).append(": was ");
          String str2 = paramFragment.mTag;
          String str3 = str2 + " now " + paramString;
          throw new IllegalStateException(str3);
        }
      }
      paramFragment.mTag = paramString;
    }
    if (paramInt1 != 0)
    {
      if ((paramFragment.mFragmentId != 0) && (paramFragment.mFragmentId != paramInt1))
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("Can't change container ID of fragment ").append(paramFragment).append(": was ");
        int i = paramFragment.mFragmentId;
        String str4 = i + " now " + paramInt1;
        throw new IllegalStateException(str4);
      }
      paramFragment.mFragmentId = paramInt1;
      paramFragment.mContainerId = paramInt1;
    }
    Op localOp = new Op();
    localOp.cmd = paramInt2;
    localOp.fragment = paramFragment;
    addOp(localOp);
  }

  public FragmentTransaction add(int paramInt, Fragment paramFragment)
  {
    doAddOp(paramInt, paramFragment, null, 1);
    return this;
  }

  public FragmentTransaction add(int paramInt, Fragment paramFragment, String paramString)
  {
    doAddOp(paramInt, paramFragment, paramString, 1);
    return this;
  }

  public FragmentTransaction add(Fragment paramFragment, String paramString)
  {
    doAddOp(0, paramFragment, paramString, 1);
    return this;
  }

  void addOp(Op paramOp)
  {
    if (this.mHead == null)
    {
      this.mTail = paramOp;
      this.mHead = paramOp;
    }
    while (true)
    {
      int i = this.mEnterAnim;
      paramOp.enterAnim = i;
      int j = this.mExitAnim;
      paramOp.exitAnim = j;
      int k = this.mPopEnterAnim;
      paramOp.popEnterAnim = k;
      int m = this.mPopExitAnim;
      paramOp.popExitAnim = m;
      int n = this.mNumOp + 1;
      this.mNumOp = n;
      return;
      Op localOp = this.mTail;
      paramOp.prev = localOp;
      this.mTail.next = paramOp;
      this.mTail = paramOp;
    }
  }

  public FragmentTransaction addToBackStack(String paramString)
  {
    if (!this.mAllowAddToBackStack)
      throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack.");
    this.mAddToBackStack = true;
    this.mName = paramString;
    return this;
  }

  public FragmentTransaction attach(Fragment paramFragment)
  {
    Op localOp = new Op();
    localOp.cmd = 7;
    localOp.fragment = paramFragment;
    addOp(localOp);
    return this;
  }

  void bumpBackStackNesting(int paramInt)
  {
    if (!this.mAddToBackStack)
      return;
    if (FragmentManagerImpl.DEBUG)
    {
      String str1 = "Bump nesting in " + this + " by " + paramInt;
      int i = Log.v("FragmentManager", str1);
    }
    for (Op localOp = this.mHead; ; localOp = localOp.next)
    {
      if (localOp == null)
        return;
      if (localOp.fragment != null)
      {
        Fragment localFragment1 = localOp.fragment;
        int j = localFragment1.mBackStackNesting + paramInt;
        localFragment1.mBackStackNesting = j;
        if (FragmentManagerImpl.DEBUG)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("Bump nesting of ");
          Fragment localFragment2 = localOp.fragment;
          StringBuilder localStringBuilder2 = localStringBuilder1.append(localFragment2).append(" to ");
          int k = localOp.fragment.mBackStackNesting;
          String str2 = k;
          int m = Log.v("FragmentManager", str2);
        }
      }
      if (localOp.removed != null)
      {
        int n = localOp.removed.size() + -1;
        while (n >= 0)
        {
          Fragment localFragment3 = (Fragment)localOp.removed.get(n);
          int i1 = localFragment3.mBackStackNesting + paramInt;
          localFragment3.mBackStackNesting = i1;
          if (FragmentManagerImpl.DEBUG)
          {
            StringBuilder localStringBuilder3 = new StringBuilder().append("Bump nesting of ").append(localFragment3).append(" to ");
            int i2 = localFragment3.mBackStackNesting;
            String str3 = i2;
            int i3 = Log.v("FragmentManager", str3);
          }
          n += -1;
        }
      }
    }
  }

  public int commit()
  {
    return commitInternal(false);
  }

  public int commitAllowingStateLoss()
  {
    return commitInternal(true);
  }

  int commitInternal(boolean paramBoolean)
  {
    if (this.mCommitted)
      throw new IllegalStateException("commit already called");
    if (FragmentManagerImpl.DEBUG)
    {
      String str = "Commit: " + this;
      int i = Log.v("FragmentManager", str);
      LogWriter localLogWriter = new LogWriter("FragmentManager");
      PrintWriter localPrintWriter = new PrintWriter(localLogWriter);
      dump("  ", null, localPrintWriter, null);
    }
    this.mCommitted = true;
    int j;
    if (this.mAddToBackStack)
      j = this.mManager.allocBackStackIndex(this);
    for (this.mIndex = j; ; this.mIndex = -1)
    {
      this.mManager.enqueueAction(this, paramBoolean);
      return this.mIndex;
    }
  }

  public FragmentTransaction detach(Fragment paramFragment)
  {
    Op localOp = new Op();
    localOp.cmd = 6;
    localOp.fragment = paramFragment;
    addOp(localOp);
    return this;
  }

  public FragmentTransaction disallowAddToBackStack()
  {
    if (this.mAddToBackStack)
      throw new IllegalStateException("This transaction is already being added to the back stack");
    this.mAllowAddToBackStack = false;
    return this;
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    dump(paramString, paramPrintWriter, true);
  }

  public void dump(String paramString, PrintWriter paramPrintWriter, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mName=");
      String str1 = this.mName;
      paramPrintWriter.print(str1);
      paramPrintWriter.print(" mIndex=");
      int i = this.mIndex;
      paramPrintWriter.print(i);
      paramPrintWriter.print(" mCommitted=");
      boolean bool = this.mCommitted;
      paramPrintWriter.println(bool);
      if (this.mTransition != 0)
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mTransition=#");
        String str2 = Integer.toHexString(this.mTransition);
        paramPrintWriter.print(str2);
        paramPrintWriter.print(" mTransitionStyle=#");
        String str3 = Integer.toHexString(this.mTransitionStyle);
        paramPrintWriter.println(str3);
      }
      if ((this.mEnterAnim != 0) || (this.mExitAnim != 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mEnterAnim=#");
        String str4 = Integer.toHexString(this.mEnterAnim);
        paramPrintWriter.print(str4);
        paramPrintWriter.print(" mExitAnim=#");
        String str5 = Integer.toHexString(this.mExitAnim);
        paramPrintWriter.println(str5);
      }
      if ((this.mPopEnterAnim != 0) || (this.mPopExitAnim != 0))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mPopEnterAnim=#");
        String str6 = Integer.toHexString(this.mPopEnterAnim);
        paramPrintWriter.print(str6);
        paramPrintWriter.print(" mPopExitAnim=#");
        String str7 = Integer.toHexString(this.mPopExitAnim);
        paramPrintWriter.println(str7);
      }
      if ((this.mBreadCrumbTitleRes != 0) || (this.mBreadCrumbTitleText != null))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mBreadCrumbTitleRes=#");
        String str8 = Integer.toHexString(this.mBreadCrumbTitleRes);
        paramPrintWriter.print(str8);
        paramPrintWriter.print(" mBreadCrumbTitleText=");
        CharSequence localCharSequence1 = this.mBreadCrumbTitleText;
        paramPrintWriter.println(localCharSequence1);
      }
      if ((this.mBreadCrumbShortTitleRes != 0) || (this.mBreadCrumbShortTitleText != null))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mBreadCrumbShortTitleRes=#");
        String str9 = Integer.toHexString(this.mBreadCrumbShortTitleRes);
        paramPrintWriter.print(str9);
        paramPrintWriter.print(" mBreadCrumbShortTitleText=");
        CharSequence localCharSequence2 = this.mBreadCrumbShortTitleText;
        paramPrintWriter.println(localCharSequence2);
      }
    }
    if (this.mHead == null)
      return;
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("Operations:");
    String str10 = paramString + "    ";
    Op localOp = this.mHead;
    int j = 0;
    while (true)
    {
      if (localOp == null)
        return;
      String str11;
      int m;
      switch (localOp.cmd)
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("cmd=");
        int k = localOp.cmd;
        str11 = k;
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  Op #");
        paramPrintWriter.print(j);
        paramPrintWriter.print(": ");
        paramPrintWriter.print(str11);
        paramPrintWriter.print(" ");
        Fragment localFragment = localOp.fragment;
        paramPrintWriter.println(localFragment);
        if (paramBoolean)
        {
          if ((localOp.enterAnim != 0) || (localOp.exitAnim != 0))
          {
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("enterAnim=#");
            String str12 = Integer.toHexString(localOp.enterAnim);
            paramPrintWriter.print(str12);
            paramPrintWriter.print(" exitAnim=#");
            String str13 = Integer.toHexString(localOp.exitAnim);
            paramPrintWriter.println(str13);
          }
          if ((localOp.popEnterAnim != 0) || (localOp.popExitAnim != 0))
          {
            paramPrintWriter.print(paramString);
            paramPrintWriter.print("popEnterAnim=#");
            String str14 = Integer.toHexString(localOp.popEnterAnim);
            paramPrintWriter.print(str14);
            paramPrintWriter.print(" popExitAnim=#");
            String str15 = Integer.toHexString(localOp.popExitAnim);
            paramPrintWriter.println(str15);
          }
        }
        if ((localOp.removed == null) || (localOp.removed.size() <= 0))
          break label895;
        m = 0;
        label721: int n = localOp.removed.size();
        if (m >= n)
          break label895;
        paramPrintWriter.print(str10);
        if (localOp.removed.size() == 1)
          paramPrintWriter.print("Removed: ");
        break;
      case 0:
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      }
      while (true)
      {
        Object localObject = localOp.removed.get(m);
        paramPrintWriter.println(localObject);
        m += 1;
        break label721;
        str11 = "NULL";
        break;
        str11 = "ADD";
        break;
        str11 = "REPLACE";
        break;
        str11 = "REMOVE";
        break;
        str11 = "HIDE";
        break;
        str11 = "SHOW";
        break;
        str11 = "DETACH";
        break;
        str11 = "ATTACH";
        break;
        if (m == 0)
          paramPrintWriter.println("Removed:");
        paramPrintWriter.print(str10);
        paramPrintWriter.print("  #");
        paramPrintWriter.print(m);
        paramPrintWriter.print(": ");
      }
      label895: localOp = localOp.next;
      j += 1;
    }
  }

  public CharSequence getBreadCrumbShortTitle()
  {
    FragmentActivity localFragmentActivity;
    int i;
    if (this.mBreadCrumbShortTitleRes != 0)
    {
      localFragmentActivity = this.mManager.mActivity;
      i = this.mBreadCrumbShortTitleRes;
    }
    for (CharSequence localCharSequence = localFragmentActivity.getText(i); ; localCharSequence = this.mBreadCrumbShortTitleText)
      return localCharSequence;
  }

  public int getBreadCrumbShortTitleRes()
  {
    return this.mBreadCrumbShortTitleRes;
  }

  public CharSequence getBreadCrumbTitle()
  {
    FragmentActivity localFragmentActivity;
    int i;
    if (this.mBreadCrumbTitleRes != 0)
    {
      localFragmentActivity = this.mManager.mActivity;
      i = this.mBreadCrumbTitleRes;
    }
    for (CharSequence localCharSequence = localFragmentActivity.getText(i); ; localCharSequence = this.mBreadCrumbTitleText)
      return localCharSequence;
  }

  public int getBreadCrumbTitleRes()
  {
    return this.mBreadCrumbTitleRes;
  }

  public int getId()
  {
    return this.mIndex;
  }

  public String getName()
  {
    return this.mName;
  }

  public int getTransition()
  {
    return this.mTransition;
  }

  public int getTransitionStyle()
  {
    return this.mTransitionStyle;
  }

  public FragmentTransaction hide(Fragment paramFragment)
  {
    Op localOp = new Op();
    localOp.cmd = 4;
    localOp.fragment = paramFragment;
    addOp(localOp);
    return this;
  }

  public boolean isAddToBackStackAllowed()
  {
    return this.mAllowAddToBackStack;
  }

  public boolean isEmpty()
  {
    if (this.mNumOp == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void popFromBackStack(boolean paramBoolean)
  {
    if (FragmentManagerImpl.DEBUG)
    {
      String str1 = "popFromBackStack: " + this;
      int i = Log.v("FragmentManager", str1);
      LogWriter localLogWriter = new LogWriter("FragmentManager");
      PrintWriter localPrintWriter = new PrintWriter(localLogWriter);
      dump("  ", null, localPrintWriter, null);
    }
    bumpBackStackNesting(-1);
    Op localOp = this.mTail;
    if (localOp != null)
    {
      switch (localOp.cmd)
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown cmd: ");
        int j = localOp.cmd;
        String str2 = j;
        throw new IllegalArgumentException(str2);
      case 1:
        Fragment localFragment1 = localOp.fragment;
        int k = localOp.popExitAnim;
        localFragment1.mNextAnim = k;
        FragmentManagerImpl localFragmentManagerImpl1 = this.mManager;
        int m = FragmentManagerImpl.reverseTransit(this.mTransition);
        int n = this.mTransitionStyle;
        localFragmentManagerImpl1.removeFragment(localFragment1, m, n);
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      }
      while (true)
      {
        localOp = localOp.prev;
        break;
        Fragment localFragment2 = localOp.fragment;
        if (localFragment2 != null)
        {
          int i1 = localOp.popExitAnim;
          localFragment2.mNextAnim = i1;
          FragmentManagerImpl localFragmentManagerImpl2 = this.mManager;
          int i2 = FragmentManagerImpl.reverseTransit(this.mTransition);
          int i3 = this.mTransitionStyle;
          localFragmentManagerImpl2.removeFragment(localFragment2, i2, i3);
        }
        if (localOp.removed != null)
        {
          int i4 = 0;
          while (true)
          {
            int i5 = localOp.removed.size();
            if (i4 >= i5)
              break;
            Fragment localFragment3 = (Fragment)localOp.removed.get(i4);
            int i6 = localOp.popEnterAnim;
            localFragment3.mNextAnim = i6;
            this.mManager.addFragment(localFragment3, false);
            i4 += 1;
          }
          Fragment localFragment4 = localOp.fragment;
          int i7 = localOp.popEnterAnim;
          localFragment4.mNextAnim = i7;
          this.mManager.addFragment(localFragment4, false);
          continue;
          Fragment localFragment5 = localOp.fragment;
          int i8 = localOp.popEnterAnim;
          localFragment5.mNextAnim = i8;
          FragmentManagerImpl localFragmentManagerImpl3 = this.mManager;
          int i9 = FragmentManagerImpl.reverseTransit(this.mTransition);
          int i10 = this.mTransitionStyle;
          localFragmentManagerImpl3.showFragment(localFragment5, i9, i10);
          continue;
          Fragment localFragment6 = localOp.fragment;
          int i11 = localOp.popExitAnim;
          localFragment6.mNextAnim = i11;
          FragmentManagerImpl localFragmentManagerImpl4 = this.mManager;
          int i12 = FragmentManagerImpl.reverseTransit(this.mTransition);
          int i13 = this.mTransitionStyle;
          localFragmentManagerImpl4.hideFragment(localFragment6, i12, i13);
          continue;
          Fragment localFragment7 = localOp.fragment;
          int i14 = localOp.popEnterAnim;
          localFragment7.mNextAnim = i14;
          FragmentManagerImpl localFragmentManagerImpl5 = this.mManager;
          int i15 = FragmentManagerImpl.reverseTransit(this.mTransition);
          int i16 = this.mTransitionStyle;
          localFragmentManagerImpl5.attachFragment(localFragment7, i15, i16);
          continue;
          Fragment localFragment8 = localOp.fragment;
          int i17 = localOp.popEnterAnim;
          localFragment8.mNextAnim = i17;
          FragmentManagerImpl localFragmentManagerImpl6 = this.mManager;
          int i18 = FragmentManagerImpl.reverseTransit(this.mTransition);
          int i19 = this.mTransitionStyle;
          localFragmentManagerImpl6.detachFragment(localFragment8, i18, i19);
        }
      }
    }
    if (paramBoolean)
    {
      FragmentManagerImpl localFragmentManagerImpl7 = this.mManager;
      int i20 = this.mManager.mCurState;
      int i21 = FragmentManagerImpl.reverseTransit(this.mTransition);
      int i22 = this.mTransitionStyle;
      localFragmentManagerImpl7.moveToState(i20, i21, i22, true);
    }
    if (this.mIndex < 0)
      return;
    FragmentManagerImpl localFragmentManagerImpl8 = this.mManager;
    int i23 = this.mIndex;
    localFragmentManagerImpl8.freeBackStackIndex(i23);
    this.mIndex = -1;
  }

  public FragmentTransaction remove(Fragment paramFragment)
  {
    Op localOp = new Op();
    localOp.cmd = 3;
    localOp.fragment = paramFragment;
    addOp(localOp);
    return this;
  }

  public FragmentTransaction replace(int paramInt, Fragment paramFragment)
  {
    return replace(paramInt, paramFragment, null);
  }

  public FragmentTransaction replace(int paramInt, Fragment paramFragment, String paramString)
  {
    if (paramInt == 0)
      throw new IllegalArgumentException("Must use non-zero containerViewId");
    doAddOp(paramInt, paramFragment, paramString, 2);
    return this;
  }

  public void run()
  {
    if (FragmentManagerImpl.DEBUG)
    {
      String str1 = "Run: " + this;
      int i = Log.v("FragmentManager", str1);
    }
    if ((this.mAddToBackStack) && (this.mIndex < 0))
      throw new IllegalStateException("addToBackStack() called after commit()");
    bumpBackStackNesting(1);
    Op localOp = this.mHead;
    if (localOp != null)
    {
      switch (localOp.cmd)
      {
      default:
        StringBuilder localStringBuilder1 = new StringBuilder().append("Unknown cmd: ");
        int j = localOp.cmd;
        String str2 = j;
        throw new IllegalArgumentException(str2);
      case 1:
        Fragment localFragment1 = localOp.fragment;
        int k = localOp.enterAnim;
        localFragment1.mNextAnim = k;
        this.mManager.addFragment(localFragment1, false);
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      }
      while (true)
      {
        localOp = localOp.next;
        break;
        Fragment localFragment2 = localOp.fragment;
        if (this.mManager.mAdded != null)
        {
          int m = 0;
          int n = this.mManager.mAdded.size();
          if (m < n)
          {
            Fragment localFragment3 = (Fragment)this.mManager.mAdded.get(m);
            if (FragmentManagerImpl.DEBUG)
            {
              String str3 = "OP_REPLACE: adding=" + localFragment2 + " old=" + localFragment3;
              int i1 = Log.v("FragmentManager", str3);
            }
            if (localFragment2 != null)
            {
              int i2 = localFragment3.mContainerId;
              int i3 = localFragment2.mContainerId;
              if (i2 == i3);
            }
            else
            {
              if (localFragment3 != localFragment2)
                break label351;
              localOp.fragment = null;
            }
            while (true)
            {
              m += 1;
              break;
              label351: if (localOp.removed == null)
              {
                ArrayList localArrayList = new ArrayList();
                localOp.removed = localArrayList;
              }
              boolean bool = localOp.removed.add(localFragment3);
              int i4 = localOp.exitAnim;
              localFragment3.mNextAnim = i4;
              if (this.mAddToBackStack)
              {
                int i5 = localFragment3.mBackStackNesting + 1;
                localFragment3.mBackStackNesting = i5;
                if (FragmentManagerImpl.DEBUG)
                {
                  StringBuilder localStringBuilder2 = new StringBuilder().append("Bump nesting of ").append(localFragment3).append(" to ");
                  int i6 = localFragment3.mBackStackNesting;
                  String str4 = i6;
                  int i7 = Log.v("FragmentManager", str4);
                }
              }
              FragmentManagerImpl localFragmentManagerImpl1 = this.mManager;
              int i8 = this.mTransition;
              int i9 = this.mTransitionStyle;
              localFragmentManagerImpl1.removeFragment(localFragment3, i8, i9);
            }
          }
        }
        if (localFragment2 != null)
        {
          int i10 = localOp.enterAnim;
          localFragment2.mNextAnim = i10;
          this.mManager.addFragment(localFragment2, false);
          continue;
          Fragment localFragment4 = localOp.fragment;
          int i11 = localOp.exitAnim;
          localFragment4.mNextAnim = i11;
          FragmentManagerImpl localFragmentManagerImpl2 = this.mManager;
          int i12 = this.mTransition;
          int i13 = this.mTransitionStyle;
          localFragmentManagerImpl2.removeFragment(localFragment4, i12, i13);
          continue;
          Fragment localFragment5 = localOp.fragment;
          int i14 = localOp.exitAnim;
          localFragment5.mNextAnim = i14;
          FragmentManagerImpl localFragmentManagerImpl3 = this.mManager;
          int i15 = this.mTransition;
          int i16 = this.mTransitionStyle;
          localFragmentManagerImpl3.hideFragment(localFragment5, i15, i16);
          continue;
          Fragment localFragment6 = localOp.fragment;
          int i17 = localOp.enterAnim;
          localFragment6.mNextAnim = i17;
          FragmentManagerImpl localFragmentManagerImpl4 = this.mManager;
          int i18 = this.mTransition;
          int i19 = this.mTransitionStyle;
          localFragmentManagerImpl4.showFragment(localFragment6, i18, i19);
          continue;
          Fragment localFragment7 = localOp.fragment;
          int i20 = localOp.exitAnim;
          localFragment7.mNextAnim = i20;
          FragmentManagerImpl localFragmentManagerImpl5 = this.mManager;
          int i21 = this.mTransition;
          int i22 = this.mTransitionStyle;
          localFragmentManagerImpl5.detachFragment(localFragment7, i21, i22);
          continue;
          Fragment localFragment8 = localOp.fragment;
          int i23 = localOp.enterAnim;
          localFragment8.mNextAnim = i23;
          FragmentManagerImpl localFragmentManagerImpl6 = this.mManager;
          int i24 = this.mTransition;
          int i25 = this.mTransitionStyle;
          localFragmentManagerImpl6.attachFragment(localFragment8, i24, i25);
        }
      }
    }
    FragmentManagerImpl localFragmentManagerImpl7 = this.mManager;
    int i26 = this.mManager.mCurState;
    int i27 = this.mTransition;
    int i28 = this.mTransitionStyle;
    localFragmentManagerImpl7.moveToState(i26, i27, i28, true);
    if (!this.mAddToBackStack)
      return;
    this.mManager.addBackStackState(this);
  }

  public FragmentTransaction setBreadCrumbShortTitle(int paramInt)
  {
    this.mBreadCrumbShortTitleRes = paramInt;
    this.mBreadCrumbShortTitleText = null;
    return this;
  }

  public FragmentTransaction setBreadCrumbShortTitle(CharSequence paramCharSequence)
  {
    this.mBreadCrumbShortTitleRes = 0;
    this.mBreadCrumbShortTitleText = paramCharSequence;
    return this;
  }

  public FragmentTransaction setBreadCrumbTitle(int paramInt)
  {
    this.mBreadCrumbTitleRes = paramInt;
    this.mBreadCrumbTitleText = null;
    return this;
  }

  public FragmentTransaction setBreadCrumbTitle(CharSequence paramCharSequence)
  {
    this.mBreadCrumbTitleRes = 0;
    this.mBreadCrumbTitleText = paramCharSequence;
    return this;
  }

  public FragmentTransaction setCustomAnimations(int paramInt1, int paramInt2)
  {
    return setCustomAnimations(paramInt1, paramInt2, 0, 0);
  }

  public FragmentTransaction setCustomAnimations(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mEnterAnim = paramInt1;
    this.mExitAnim = paramInt2;
    this.mPopEnterAnim = paramInt3;
    this.mPopExitAnim = paramInt4;
    return this;
  }

  public FragmentTransaction setTransition(int paramInt)
  {
    this.mTransition = paramInt;
    return this;
  }

  public FragmentTransaction setTransitionStyle(int paramInt)
  {
    this.mTransitionStyle = paramInt;
    return this;
  }

  public FragmentTransaction show(Fragment paramFragment)
  {
    Op localOp = new Op();
    localOp.cmd = 5;
    localOp.fragment = paramFragment;
    addOp(localOp);
    return this;
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    StringBuilder localStringBuilder2 = localStringBuilder1.append("BackStackEntry{");
    String str1 = Integer.toHexString(System.identityHashCode(this));
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str1);
    if (this.mIndex >= 0)
    {
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" #");
      int i = this.mIndex;
      StringBuilder localStringBuilder5 = localStringBuilder1.append(i);
    }
    if (this.mName != null)
    {
      StringBuilder localStringBuilder6 = localStringBuilder1.append(" ");
      String str2 = this.mName;
      StringBuilder localStringBuilder7 = localStringBuilder1.append(str2);
    }
    StringBuilder localStringBuilder8 = localStringBuilder1.append("}");
    return localStringBuilder1.toString();
  }

  static final class Op
  {
    int cmd;
    int enterAnim;
    int exitAnim;
    Fragment fragment;
    Op next;
    int popEnterAnim;
    int popExitAnim;
    Op prev;
    ArrayList<Fragment> removed;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.BackStackRecord
 * JD-Core Version:    0.6.2
 */