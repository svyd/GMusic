package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

final class FragmentState
  implements Parcelable
{
  public static final Parcelable.Creator<FragmentState> CREATOR = new Parcelable.Creator()
  {
    public FragmentState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FragmentState(paramAnonymousParcel);
    }

    public FragmentState[] newArray(int paramAnonymousInt)
    {
      return new FragmentState[paramAnonymousInt];
    }
  };
  final Bundle mArguments;
  final String mClassName;
  final int mContainerId;
  final boolean mDetached;
  final int mFragmentId;
  final boolean mFromLayout;
  final int mIndex;
  Fragment mInstance;
  final boolean mRetainInstance;
  Bundle mSavedFragmentState;
  final String mTag;

  public FragmentState(Parcel paramParcel)
  {
    String str1 = paramParcel.readString();
    this.mClassName = str1;
    int i = paramParcel.readInt();
    this.mIndex = i;
    boolean bool2;
    boolean bool3;
    if (paramParcel.readInt() != 0)
    {
      bool2 = true;
      this.mFromLayout = bool2;
      int j = paramParcel.readInt();
      this.mFragmentId = j;
      int k = paramParcel.readInt();
      this.mContainerId = k;
      String str2 = paramParcel.readString();
      this.mTag = str2;
      if (paramParcel.readInt() == 0)
        break label139;
      bool3 = true;
      label90: this.mRetainInstance = bool3;
      if (paramParcel.readInt() == 0)
        break label145;
    }
    while (true)
    {
      this.mDetached = bool1;
      Bundle localBundle1 = paramParcel.readBundle();
      this.mArguments = localBundle1;
      Bundle localBundle2 = paramParcel.readBundle();
      this.mSavedFragmentState = localBundle2;
      return;
      bool2 = false;
      break;
      label139: bool3 = false;
      break label90;
      label145: bool1 = false;
    }
  }

  public FragmentState(Fragment paramFragment)
  {
    String str1 = paramFragment.getClass().getName();
    this.mClassName = str1;
    int i = paramFragment.mIndex;
    this.mIndex = i;
    boolean bool1 = paramFragment.mFromLayout;
    this.mFromLayout = bool1;
    int j = paramFragment.mFragmentId;
    this.mFragmentId = j;
    int k = paramFragment.mContainerId;
    this.mContainerId = k;
    String str2 = paramFragment.mTag;
    this.mTag = str2;
    boolean bool2 = paramFragment.mRetainInstance;
    this.mRetainInstance = bool2;
    boolean bool3 = paramFragment.mDetached;
    this.mDetached = bool3;
    Bundle localBundle = paramFragment.mArguments;
    this.mArguments = localBundle;
  }

  public int describeContents()
  {
    return 0;
  }

  public Fragment instantiate(FragmentActivity paramFragmentActivity, Fragment paramFragment)
  {
    if (this.mInstance != null);
    for (Fragment localFragment1 = this.mInstance; ; localFragment1 = this.mInstance)
    {
      return localFragment1;
      if (this.mArguments != null)
      {
        Bundle localBundle1 = this.mArguments;
        ClassLoader localClassLoader1 = paramFragmentActivity.getClassLoader();
        localBundle1.setClassLoader(localClassLoader1);
      }
      String str1 = this.mClassName;
      Bundle localBundle2 = this.mArguments;
      Fragment localFragment2 = Fragment.instantiate(paramFragmentActivity, str1, localBundle2);
      this.mInstance = localFragment2;
      if (this.mSavedFragmentState != null)
      {
        Bundle localBundle3 = this.mSavedFragmentState;
        ClassLoader localClassLoader2 = paramFragmentActivity.getClassLoader();
        localBundle3.setClassLoader(localClassLoader2);
        Fragment localFragment3 = this.mInstance;
        Bundle localBundle4 = this.mSavedFragmentState;
        localFragment3.mSavedFragmentState = localBundle4;
      }
      Fragment localFragment4 = this.mInstance;
      int i = this.mIndex;
      localFragment4.setIndex(i, paramFragment);
      Fragment localFragment5 = this.mInstance;
      boolean bool1 = this.mFromLayout;
      localFragment5.mFromLayout = bool1;
      this.mInstance.mRestored = true;
      Fragment localFragment6 = this.mInstance;
      int j = this.mFragmentId;
      localFragment6.mFragmentId = j;
      Fragment localFragment7 = this.mInstance;
      int k = this.mContainerId;
      localFragment7.mContainerId = k;
      Fragment localFragment8 = this.mInstance;
      String str2 = this.mTag;
      localFragment8.mTag = str2;
      Fragment localFragment9 = this.mInstance;
      boolean bool2 = this.mRetainInstance;
      localFragment9.mRetainInstance = bool2;
      Fragment localFragment10 = this.mInstance;
      boolean bool3 = this.mDetached;
      localFragment10.mDetached = bool3;
      Fragment localFragment11 = this.mInstance;
      FragmentManagerImpl localFragmentManagerImpl = paramFragmentActivity.mFragments;
      localFragment11.mFragmentManager = localFragmentManagerImpl;
      if (FragmentManagerImpl.DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Instantiated fragment ");
        Fragment localFragment12 = this.mInstance;
        String str3 = localFragment12;
        int m = Log.v("FragmentManager", str3);
      }
    }
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = 1;
    String str1 = this.mClassName;
    paramParcel.writeString(str1);
    int j = this.mIndex;
    paramParcel.writeInt(j);
    int k;
    int i1;
    if (this.mFromLayout)
    {
      k = 1;
      paramParcel.writeInt(k);
      int m = this.mFragmentId;
      paramParcel.writeInt(m);
      int n = this.mContainerId;
      paramParcel.writeInt(n);
      String str2 = this.mTag;
      paramParcel.writeString(str2);
      if (!this.mRetainInstance)
        break label137;
      i1 = 1;
      label88: paramParcel.writeInt(i1);
      if (!this.mDetached)
        break label143;
    }
    while (true)
    {
      paramParcel.writeInt(i);
      Bundle localBundle1 = this.mArguments;
      paramParcel.writeBundle(localBundle1);
      Bundle localBundle2 = this.mSavedFragmentState;
      paramParcel.writeBundle(localBundle2);
      return;
      k = 0;
      break;
      label137: i1 = 0;
      break label88;
      label143: i = 0;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentState
 * JD-Core Version:    0.6.2
 */