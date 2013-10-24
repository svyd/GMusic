package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState
  implements Parcelable
{
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator()
  {
    public BackStackState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BackStackState(paramAnonymousParcel);
    }

    public BackStackState[] newArray(int paramAnonymousInt)
    {
      return new BackStackState[paramAnonymousInt];
    }
  };
  final int mBreadCrumbShortTitleRes;
  final CharSequence mBreadCrumbShortTitleText;
  final int mBreadCrumbTitleRes;
  final CharSequence mBreadCrumbTitleText;
  final int mIndex;
  final String mName;
  final int[] mOps;
  final int mTransition;
  final int mTransitionStyle;

  public BackStackState(Parcel paramParcel)
  {
    int[] arrayOfInt = paramParcel.createIntArray();
    this.mOps = arrayOfInt;
    int i = paramParcel.readInt();
    this.mTransition = i;
    int j = paramParcel.readInt();
    this.mTransitionStyle = j;
    String str = paramParcel.readString();
    this.mName = str;
    int k = paramParcel.readInt();
    this.mIndex = k;
    int m = paramParcel.readInt();
    this.mBreadCrumbTitleRes = m;
    CharSequence localCharSequence1 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mBreadCrumbTitleText = localCharSequence1;
    int n = paramParcel.readInt();
    this.mBreadCrumbShortTitleRes = n;
    CharSequence localCharSequence2 = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mBreadCrumbShortTitleText = localCharSequence2;
  }

  public BackStackState(FragmentManagerImpl paramFragmentManagerImpl, BackStackRecord paramBackStackRecord)
  {
    int i = 0;
    for (BackStackRecord.Op localOp1 = paramBackStackRecord.mHead; localOp1 != null; localOp1 = localOp1.next)
      if (localOp1.removed != null)
      {
        int j = localOp1.removed.size();
        i += j;
      }
    int[] arrayOfInt1 = new int[paramBackStackRecord.mNumOp * 7 + i];
    this.mOps = arrayOfInt1;
    if (!paramBackStackRecord.mAddToBackStack)
      throw new IllegalStateException("Not on back stack");
    BackStackRecord.Op localOp2 = paramBackStackRecord.mHead;
    int k = 0;
    if (localOp2 != null)
    {
      int[] arrayOfInt2 = this.mOps;
      int m = k + 1;
      int n = localOp2.cmd;
      arrayOfInt2[k] = n;
      int[] arrayOfInt3 = this.mOps;
      int i1 = m + 1;
      if (localOp2.fragment != null);
      int i9;
      for (int i2 = localOp2.fragment.mIndex; ; i2 = -1)
      {
        arrayOfInt3[m] = i2;
        int[] arrayOfInt4 = this.mOps;
        int i3 = i1 + 1;
        int i4 = localOp2.enterAnim;
        arrayOfInt4[i1] = i4;
        int[] arrayOfInt5 = this.mOps;
        int i5 = i3 + 1;
        int i6 = localOp2.exitAnim;
        arrayOfInt5[i3] = i6;
        int[] arrayOfInt6 = this.mOps;
        int i7 = i5 + 1;
        int i8 = localOp2.popEnterAnim;
        arrayOfInt6[i5] = i8;
        int[] arrayOfInt7 = this.mOps;
        i9 = i7 + 1;
        int i10 = localOp2.popExitAnim;
        arrayOfInt7[i7] = i10;
        if (localOp2.removed == null)
          break label393;
        int i11 = localOp2.removed.size();
        int[] arrayOfInt8 = this.mOps;
        int i12 = i9 + 1;
        arrayOfInt8[i9] = i11;
        int i13 = 0;
        int i14;
        for (i9 = i12; i13 < i11; i9 = i14)
        {
          int[] arrayOfInt9 = this.mOps;
          i14 = i9 + 1;
          int i15 = ((Fragment)localOp2.removed.get(i13)).mIndex;
          arrayOfInt9[i9] = i15;
          i13 += 1;
        }
      }
      int i16 = i9;
      while (true)
      {
        localOp2 = localOp2.next;
        k = i16;
        break;
        label393: int[] arrayOfInt10 = this.mOps;
        i16 = i9 + 1;
        arrayOfInt10[i9] = 0;
      }
    }
    int i17 = paramBackStackRecord.mTransition;
    this.mTransition = i17;
    int i18 = paramBackStackRecord.mTransitionStyle;
    this.mTransitionStyle = i18;
    String str = paramBackStackRecord.mName;
    this.mName = str;
    int i19 = paramBackStackRecord.mIndex;
    this.mIndex = i19;
    int i20 = paramBackStackRecord.mBreadCrumbTitleRes;
    this.mBreadCrumbTitleRes = i20;
    CharSequence localCharSequence1 = paramBackStackRecord.mBreadCrumbTitleText;
    this.mBreadCrumbTitleText = localCharSequence1;
    int i21 = paramBackStackRecord.mBreadCrumbShortTitleRes;
    this.mBreadCrumbShortTitleRes = i21;
    CharSequence localCharSequence2 = paramBackStackRecord.mBreadCrumbShortTitleText;
    this.mBreadCrumbShortTitleText = localCharSequence2;
  }

  public int describeContents()
  {
    return 0;
  }

  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl)
  {
    BackStackRecord localBackStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int i = 0;
    int j = 0;
    while (true)
    {
      int k = this.mOps.length;
      if (i >= k)
        break;
      BackStackRecord.Op localOp = new BackStackRecord.Op();
      int[] arrayOfInt1 = this.mOps;
      int m = i + 1;
      int n = arrayOfInt1[i];
      localOp.cmd = n;
      if (FragmentManagerImpl.DEBUG)
      {
        StringBuilder localStringBuilder1 = new StringBuilder().append("Instantiate ").append(localBackStackRecord).append(" op #").append(j).append(" base fragment #");
        int i1 = this.mOps[m];
        String str1 = i1;
        int i2 = Log.v("FragmentManager", str1);
      }
      int[] arrayOfInt2 = this.mOps;
      int i3 = m + 1;
      int i4 = arrayOfInt2[m];
      Fragment localFragment1;
      if (i4 >= 0)
        localFragment1 = (Fragment)paramFragmentManagerImpl.mActive.get(i4);
      int i13;
      for (localOp.fragment = localFragment1; ; localOp.fragment = null)
      {
        int[] arrayOfInt3 = this.mOps;
        int i5 = i3 + 1;
        int i6 = arrayOfInt3[i3];
        localOp.enterAnim = i6;
        int[] arrayOfInt4 = this.mOps;
        int i7 = i5 + 1;
        int i8 = arrayOfInt4[i5];
        localOp.exitAnim = i8;
        int[] arrayOfInt5 = this.mOps;
        int i9 = i7 + 1;
        int i10 = arrayOfInt5[i7];
        localOp.popEnterAnim = i10;
        int[] arrayOfInt6 = this.mOps;
        int i11 = i9 + 1;
        int i12 = arrayOfInt6[i9];
        localOp.popExitAnim = i12;
        int[] arrayOfInt7 = this.mOps;
        i13 = i11 + 1;
        int i14 = arrayOfInt7[i11];
        if (i14 <= 0)
          break;
        ArrayList localArrayList1 = new ArrayList(i14);
        localOp.removed = localArrayList1;
        int i15 = 0;
        while (i15 < i14)
        {
          if (FragmentManagerImpl.DEBUG)
          {
            StringBuilder localStringBuilder2 = new StringBuilder().append("Instantiate ").append(localBackStackRecord).append(" set remove fragment #");
            int i16 = this.mOps[i13];
            String str2 = i16;
            int i17 = Log.v("FragmentManager", str2);
          }
          ArrayList localArrayList2 = paramFragmentManagerImpl.mActive;
          int[] arrayOfInt8 = this.mOps;
          int i18 = i13 + 1;
          int i19 = arrayOfInt8[i13];
          Fragment localFragment2 = (Fragment)localArrayList2.get(i19);
          boolean bool = localOp.removed.add(localFragment2);
          i15 += 1;
          i13 = i18;
        }
      }
      i = i13;
      localBackStackRecord.addOp(localOp);
      j += 1;
    }
    int i20 = this.mTransition;
    localBackStackRecord.mTransition = i20;
    int i21 = this.mTransitionStyle;
    localBackStackRecord.mTransitionStyle = i21;
    String str3 = this.mName;
    localBackStackRecord.mName = str3;
    int i22 = this.mIndex;
    localBackStackRecord.mIndex = i22;
    localBackStackRecord.mAddToBackStack = true;
    int i23 = this.mBreadCrumbTitleRes;
    localBackStackRecord.mBreadCrumbTitleRes = i23;
    CharSequence localCharSequence1 = this.mBreadCrumbTitleText;
    localBackStackRecord.mBreadCrumbTitleText = localCharSequence1;
    int i24 = this.mBreadCrumbShortTitleRes;
    localBackStackRecord.mBreadCrumbShortTitleRes = i24;
    CharSequence localCharSequence2 = this.mBreadCrumbShortTitleText;
    localBackStackRecord.mBreadCrumbShortTitleText = localCharSequence2;
    localBackStackRecord.bumpBackStackNesting(1);
    return localBackStackRecord;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int[] arrayOfInt = this.mOps;
    paramParcel.writeIntArray(arrayOfInt);
    int i = this.mTransition;
    paramParcel.writeInt(i);
    int j = this.mTransitionStyle;
    paramParcel.writeInt(j);
    String str = this.mName;
    paramParcel.writeString(str);
    int k = this.mIndex;
    paramParcel.writeInt(k);
    int m = this.mBreadCrumbTitleRes;
    paramParcel.writeInt(m);
    TextUtils.writeToParcel(this.mBreadCrumbTitleText, paramParcel, 0);
    int n = this.mBreadCrumbShortTitleRes;
    paramParcel.writeInt(n);
    TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, paramParcel, 0);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.BackStackState
 * JD-Core Version:    0.6.2
 */