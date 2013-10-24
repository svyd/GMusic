package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.Playlists;

public class RecommendedRadioList extends RadioList
{
  public static final Parcelable.Creator<RecommendedRadioList> CREATOR = new Parcelable.Creator()
  {
    public RecommendedRadioList createFromParcel(Parcel paramAnonymousParcel)
    {
      return new RecommendedRadioList();
    }

    public RecommendedRadioList[] newArray(int paramAnonymousInt)
    {
      return new RecommendedRadioList[paramAnonymousInt];
    }
  };

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Playlists.getSuggestedMixesUri();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.RecommendedRadioList
 * JD-Core Version:    0.6.2
 */