package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.store.MusicContent.Mainstage;

public class MainstageList extends MediaList
{
  public static final Parcelable.Creator<MainstageList> CREATOR = new Parcelable.Creator()
  {
    public MainstageList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new MainstageList();
    }

    public MainstageList[] newArray(int paramAnonymousInt)
    {
      return new MainstageList[paramAnonymousInt];
    }
  };

  public MainstageList()
  {
    super(localDomain, true, true);
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Mainstage.CONTENT_URI;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.MainstageList
 * JD-Core Version:    0.6.2
 */