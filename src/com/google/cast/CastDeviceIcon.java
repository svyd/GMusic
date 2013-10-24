package com.google.cast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CastDeviceIcon
  implements Parcelable
{
  public static final Parcelable.Creator<CastDeviceIcon> CREATOR = new Parcelable.Creator()
  {
    public CastDeviceIcon a(Parcel paramAnonymousParcel)
    {
      return new CastDeviceIcon(paramAnonymousParcel);
    }

    public CastDeviceIcon[] a(int paramAnonymousInt)
    {
      return new CastDeviceIcon[paramAnonymousInt];
    }
  };
  private int a;
  private int b;
  private int c;
  private Uri d;

  public CastDeviceIcon(int paramInt1, int paramInt2, int paramInt3, Uri paramUri)
  {
    this.a = paramInt1;
    this.b = paramInt2;
    this.c = paramInt3;
    this.d = paramUri;
  }

  public CastDeviceIcon(Parcel paramParcel)
  {
    int i = paramParcel.readInt();
    this.a = i;
    int j = paramParcel.readInt();
    this.b = j;
    int k = paramParcel.readInt();
    this.c = k;
    String str = (String)paramParcel.readValue(null);
    if (str == null)
      return;
    Uri localUri = Uri.parse(str);
    this.d = localUri;
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    int i = this.a;
    paramParcel.writeInt(i);
    int j = this.b;
    paramParcel.writeInt(j);
    int k = this.c;
    paramParcel.writeInt(k);
    if (this.d != null);
    for (String str = this.d.toString(); ; str = null)
    {
      paramParcel.writeValue(str);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.CastDeviceIcon
 * JD-Core Version:    0.6.2
 */