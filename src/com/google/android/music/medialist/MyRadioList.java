package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.RadioStations;

public class MyRadioList extends RadioList
{
  public static final Parcelable.Creator<MyRadioList> CREATOR = new Parcelable.Creator()
  {
    public MyRadioList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MyRadioList();
    }

    public MyRadioList[] newArray(int paramAnonymousInt)
    {
      return new MyRadioList[paramAnonymousInt];
    }
  };

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.RadioStations.CONTENT_URI;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.MyRadioList
 * JD-Core Version:    0.6.2
 */