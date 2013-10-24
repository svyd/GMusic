package com.google.android.music.medialist;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.music.download.artwork.AlbumIdSink;
import com.google.android.music.store.MusicContent.Genres;
import com.google.android.music.utils.MusicUtils;

public class GenreAlbumList extends AlbumList
{
  public static final Parcelable.Creator<GenreAlbumList> CREATOR = new Parcelable.Creator()
  {
    public GenreAlbumList createFromParcel(Parcel paramAnonymousParcel)
    {
      int i = 1;
      long l = paramAnonymousParcel.readLong();
      if (paramAnonymousParcel.readInt() != i);
      while (true)
      {
        return new GenreAlbumList(l, i);
        int j = 0;
      }
    }

    public GenreAlbumList[] newArray(int paramAnonymousInt)
    {
      return new GenreAlbumList[paramAnonymousInt];
    }
  };
  private long mGenreId;
  private String mGenreName;

  public GenreAlbumList(long paramLong, boolean paramBoolean)
  {
    super(paramBoolean);
    if (paramLong <= 0L)
    {
      String str = "Invalid genreId id: " + paramLong;
      throw new IllegalArgumentException(str);
    }
    this.mGenreId = paramLong;
  }

  private void getNames(Context paramContext)
  {
    if (this.mGenreName != null)
      return;
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "name";
    Uri localUri = MusicContent.Genres.getGenreUri(Long.valueOf(this.mGenreId));
    Context localContext = paramContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor == null)
      return;
    boolean bool = localCursor.moveToFirst();
    if (!localCursor.isAfterLast())
    {
      String str2 = localCursor.getString(0);
      this.mGenreName = str2;
    }
    localCursor.close();
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[2];
    StringBuilder localStringBuilder = new StringBuilder().append("");
    long l = this.mGenreId;
    String str1 = l;
    arrayOfString[0] = str1;
    String str2 = Boolean.toString(this.mShouldFilter);
    arrayOfString[1] = str2;
    return arrayOfString;
  }

  public Uri getContentUri(Context paramContext)
  {
    return MusicContent.Genres.getAlbumsOfGenreUri(this.mGenreId);
  }

  public long getGenreId()
  {
    return this.mGenreId;
  }

  public Bitmap getImage(Context paramContext, int paramInt1, int paramInt2, AlbumIdSink paramAlbumIdSink, boolean paramBoolean)
  {
    return null;
  }

  public String getName(Context paramContext)
  {
    getNames(paramContext);
    return this.mGenreName;
  }

  public String getSecondaryName(Context paramContext)
  {
    return null;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    long l = this.mGenreId;
    paramParcel.writeLong(l);
    if (this.mShouldFilter);
    for (int i = 1; ; i = 0)
    {
      paramParcel.writeInt(i);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.GenreAlbumList
 * JD-Core Version:    0.6.2
 */