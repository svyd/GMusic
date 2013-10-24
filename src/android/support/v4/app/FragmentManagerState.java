package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

final class FragmentManagerState
  implements Parcelable
{
  public static final Parcelable.Creator<FragmentManagerState> CREATOR = new Parcelable.Creator()
  {
    public FragmentManagerState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new FragmentManagerState(paramAnonymousParcel);
    }

    public FragmentManagerState[] newArray(int paramAnonymousInt)
    {
      return new FragmentManagerState[paramAnonymousInt];
    }
  };
  FragmentState[] mActive;
  int[] mAdded;
  BackStackState[] mBackStack;

  public FragmentManagerState()
  {
  }

  public FragmentManagerState(Parcel paramParcel)
  {
    Parcelable.Creator localCreator1 = FragmentState.CREATOR;
    FragmentState[] arrayOfFragmentState = (FragmentState[])paramParcel.createTypedArray(localCreator1);
    this.mActive = arrayOfFragmentState;
    int[] arrayOfInt = paramParcel.createIntArray();
    this.mAdded = arrayOfInt;
    Parcelable.Creator localCreator2 = BackStackState.CREATOR;
    BackStackState[] arrayOfBackStackState = (BackStackState[])paramParcel.createTypedArray(localCreator2);
    this.mBackStack = arrayOfBackStackState;
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    FragmentState[] arrayOfFragmentState = this.mActive;
    paramParcel.writeTypedArray(arrayOfFragmentState, paramInt);
    int[] arrayOfInt = this.mAdded;
    paramParcel.writeIntArray(arrayOfInt);
    BackStackState[] arrayOfBackStackState = this.mBackStack;
    paramParcel.writeTypedArray(arrayOfBackStackState, paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentManagerState
 * JD-Core Version:    0.6.2
 */