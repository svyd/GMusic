package com.google.android.music.mix;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MixGenerationState
  implements Parcelable
{
  public static final Parcelable.Creator<MixGenerationState> CREATOR = new Parcelable.Creator()
  {
    public MixGenerationState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MixGenerationState(paramAnonymousParcel, null);
    }

    public MixGenerationState[] newArray(int paramAnonymousInt)
    {
      return new MixGenerationState[paramAnonymousInt];
    }
  };
  private MixDescriptor mMix;
  private MixDescriptor mOriginalMix;
  private State mState;

  private MixGenerationState(Parcel paramParcel)
  {
    State localState1 = State.NOT_STARTED;
    this.mState = localState1;
    ClassLoader localClassLoader1 = MixDescriptor.class.getClassLoader();
    MixDescriptor localMixDescriptor1 = (MixDescriptor)paramParcel.readParcelable(localClassLoader1);
    this.mMix = localMixDescriptor1;
    ClassLoader localClassLoader2 = MixDescriptor.class.getClassLoader();
    MixDescriptor localMixDescriptor2 = (MixDescriptor)paramParcel.readParcelable(localClassLoader2);
    this.mOriginalMix = localMixDescriptor2;
    State[] arrayOfState = State.values();
    int i = paramParcel.readInt();
    State localState2 = arrayOfState[i];
    this.mState = localState2;
  }

  public MixGenerationState(MixDescriptor paramMixDescriptor)
  {
    State localState = State.NOT_STARTED;
    this.mState = localState;
    this.mMix = paramMixDescriptor;
  }

  public int describeContents()
  {
    return 0;
  }

  /** @deprecated */
  public MixDescriptor getMix()
  {
    try
    {
      MixDescriptor localMixDescriptor = this.mMix;
      return localMixDescriptor;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public State getState()
  {
    try
    {
      State localState = this.mState;
      return localState;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public boolean isMyMix(MixDescriptor paramMixDescriptor)
  {
    try
    {
      if ((this.mMix == null) || (!this.mMix.equals(paramMixDescriptor)))
      {
        if (this.mOriginalMix != null)
        {
          boolean bool1 = this.mOriginalMix.equals(paramMixDescriptor);
          if (!bool1);
        }
      }
      else
      {
        bool2 = true;
        return bool2;
      }
      boolean bool2 = false;
    }
    finally
    {
    }
  }

  /** @deprecated */
  public void setState(State paramState)
  {
    try
    {
      this.mState = paramState;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public String toString()
  {
    try
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
      MixDescriptor localMixDescriptor1 = this.mMix;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(localMixDescriptor1);
      StringBuilder localStringBuilder4 = localStringBuilder1.append(", ");
      MixDescriptor localMixDescriptor2 = this.mOriginalMix;
      StringBuilder localStringBuilder5 = localStringBuilder1.append(localMixDescriptor2);
      StringBuilder localStringBuilder6 = localStringBuilder1.append(", ");
      State localState = this.mState;
      StringBuilder localStringBuilder7 = localStringBuilder1.append(localState);
      StringBuilder localStringBuilder8 = localStringBuilder1.append("]");
      String str1 = localStringBuilder1.toString();
      String str2 = str1;
      return str2;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public void transitionToNewMix(MixDescriptor paramMixDescriptor)
  {
    try
    {
      State localState = State.FINISHED;
      this.mState = localState;
      MixDescriptor localMixDescriptor = this.mMix;
      this.mOriginalMix = localMixDescriptor;
      this.mMix = paramMixDescriptor;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  /** @deprecated */
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    try
    {
      MixDescriptor localMixDescriptor1 = this.mMix;
      paramParcel.writeParcelable(localMixDescriptor1, paramInt);
      MixDescriptor localMixDescriptor2 = this.mOriginalMix;
      paramParcel.writeParcelable(localMixDescriptor2, paramInt);
      int i = this.mState.ordinal();
      paramParcel.writeInt(i);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public static enum State
  {
    static
    {
      FINISHED = new State("FINISHED", 2);
      CANCELED = new State("CANCELED", 3);
      FAILED = new State("FAILED", 4);
      State[] arrayOfState = new State[5];
      State localState1 = NOT_STARTED;
      arrayOfState[0] = localState1;
      State localState2 = RUNNING;
      arrayOfState[1] = localState2;
      State localState3 = FINISHED;
      arrayOfState[2] = localState3;
      State localState4 = CANCELED;
      arrayOfState[3] = localState4;
      State localState5 = FAILED;
      arrayOfState[4] = localState5;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.mix.MixGenerationState
 * JD-Core Version:    0.6.2
 */