package com.google.android.music.medialist;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.store.MusicContent.Playlists;

public class AllPlaylists extends PlaylistsList
{
  public static final Parcelable.Creator<AllPlaylists> CREATOR = new Parcelable.Creator()
  {
    public AllPlaylists createFromParcel(Parcel paramAnonymousParcel)
    {
      return new AllPlaylists();
    }

    public AllPlaylists[] newArray(int paramAnonymousInt)
    {
      return new AllPlaylists[paramAnonymousInt];
    }
  };

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Playlists.CONTENT_URI;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.AllPlaylists
 * JD-Core Version:    0.6.2
 */