package android.support.v4.os;

import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class ParcelableCompat
{
  public static <T> Parcelable.Creator<T> newCreator(ParcelableCompatCreatorCallbacks<T> paramParcelableCompatCreatorCallbacks)
  {
    if (Build.VERSION.SDK_INT >= 13)
      Parcelable.Creator localCreator = ParcelableCompatCreatorHoneycombMR2Stub.instantiate(paramParcelableCompatCreatorCallbacks);
    return new CompatCreator(paramParcelableCompatCreatorCallbacks);
  }

  static class CompatCreator<T>
    implements Parcelable.Creator<T>
  {
    final ParcelableCompatCreatorCallbacks<T> mCallbacks;

    public CompatCreator(ParcelableCompatCreatorCallbacks<T> paramParcelableCompatCreatorCallbacks)
    {
      this.mCallbacks = paramParcelableCompatCreatorCallbacks;
    }

    public T createFromParcel(Parcel paramParcel)
    {
      return this.mCallbacks.createFromParcel(paramParcel, null);
    }

    public T[] newArray(int paramInt)
    {
      return this.mCallbacks.newArray(paramInt);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.os.ParcelableCompat
 * JD-Core Version:    0.6.2
 */