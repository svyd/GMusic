package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.store.MusicContent.Albums;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailAlbumTracklistCursor extends MatrixCursor
{
  private static final String[] PROJECTION_ALBUM_TRACKS = arrayOfString;
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[8];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "SongId";
    arrayOfString[2] = "title";
    arrayOfString[3] = "duration";
    arrayOfString[4] = "album";
    arrayOfString[5] = "artist";
    arrayOfString[6] = "AlbumArtist";
    arrayOfString[7] = "ArtistArtLocation";
  }

  DetailAlbumTracklistCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowsForTracks(paramString);
  }

  private void addRowsForTracks(String paramString)
  {
    Object[] arrayOfObject1 = new Object[this.mProjectionMap.size()];
    boolean bool = MusicUtils.isNautilusId(paramString);
    if (bool);
    Cursor localCursor;
    for (Uri localUri = MusicContent.Albums.getAudioInNautilusAlbumUri(paramString); ; localUri = MusicContent.Albums.getAudioInAlbumUri(Long.valueOf(paramString).longValue()))
    {
      Context localContext1 = this.mContext;
      String[] arrayOfString = PROJECTION_ALBUM_TRACKS;
      localCursor = MusicUtils.query(localContext1, localUri, arrayOfString, null, null, null);
      if (localCursor != null)
        break;
      return;
    }
    int i = 0;
    try
    {
      while (localCursor.moveToNext())
      {
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        int j = localCursor.getInt(3) / 1000;
        String str3 = localCursor.getString(5);
        String str4 = localCursor.getString(4);
        Context localContext2 = this.mContext;
        long l = j;
        String str5 = MusicUtils.makeTimeString(localContext2, l);
        String str6 = null;
        if (!localCursor.isNull(7))
          str6 = localCursor.getString(7);
        if (TextUtils.isEmpty(str6))
          str6 = XdiUtils.getDefaultArtUri(this.mContext);
        Intent localIntent1 = XdiUtils.newXdiPlayIntent();
        if (bool)
        {
          Intent localIntent2 = localIntent1.putExtra("container", 5);
          String str7 = paramString;
          Intent localIntent3 = localIntent1.putExtra("id_string", str7);
          Intent localIntent4 = localIntent1.putExtra("offset", i);
          ProjectionMap localProjectionMap1 = this.mProjectionMap;
          Object[] arrayOfObject2 = arrayOfObject1;
          String str8 = str1;
          localProjectionMap1.writeValueToArray(arrayOfObject2, "_id", str8);
          ProjectionMap localProjectionMap2 = this.mProjectionMap;
          Object[] arrayOfObject3 = arrayOfObject1;
          String str9 = str2;
          localProjectionMap2.writeValueToArray(arrayOfObject3, "display_name", str9);
          ProjectionMap localProjectionMap3 = this.mProjectionMap;
          Object[] arrayOfObject4 = arrayOfObject1;
          localProjectionMap3.writeValueToArray(arrayOfObject4, "display_subname", str5);
          ProjectionMap localProjectionMap4 = this.mProjectionMap;
          Object[] arrayOfObject5 = arrayOfObject1;
          localProjectionMap4.writeValueToArray(arrayOfObject5, "display_description", null);
          ProjectionMap localProjectionMap5 = this.mProjectionMap;
          Object[] arrayOfObject6 = arrayOfObject1;
          localProjectionMap5.writeValueToArray(arrayOfObject6, "display_number", null);
          ProjectionMap localProjectionMap6 = this.mProjectionMap;
          Object[] arrayOfObject7 = arrayOfObject1;
          localProjectionMap6.writeValueToArray(arrayOfObject7, "image_uri", null);
          ProjectionMap localProjectionMap7 = this.mProjectionMap;
          Integer localInteger1 = Integer.valueOf(1);
          Object[] arrayOfObject8 = arrayOfObject1;
          localProjectionMap7.writeValueToArray(arrayOfObject8, "item_display_type", localInteger1);
          ProjectionMap localProjectionMap8 = this.mProjectionMap;
          Object[] arrayOfObject9 = arrayOfObject1;
          localProjectionMap8.writeValueToArray(arrayOfObject9, "user_rating_count", null);
          ProjectionMap localProjectionMap9 = this.mProjectionMap;
          Integer localInteger2 = Integer.valueOf(-1);
          Object[] arrayOfObject10 = arrayOfObject1;
          localProjectionMap9.writeValueToArray(arrayOfObject10, "user_rating", localInteger2);
          ProjectionMap localProjectionMap10 = this.mProjectionMap;
          String str10 = localIntent1.toUri(1);
          Object[] arrayOfObject11 = arrayOfObject1;
          localProjectionMap10.writeValueToArray(arrayOfObject11, "action_uri", str10);
          ProjectionMap localProjectionMap11 = this.mProjectionMap;
          Integer localInteger3 = Integer.valueOf(j);
          Object[] arrayOfObject12 = arrayOfObject1;
          localProjectionMap11.writeValueToArray(arrayOfObject12, "music_duration", localInteger3);
          ProjectionMap localProjectionMap12 = this.mProjectionMap;
          Object[] arrayOfObject13 = arrayOfObject1;
          localProjectionMap12.writeValueToArray(arrayOfObject13, "music_trackArtist", str3);
          ProjectionMap localProjectionMap13 = this.mProjectionMap;
          Object[] arrayOfObject14 = arrayOfObject1;
          localProjectionMap13.writeValueToArray(arrayOfObject14, "music_album", str4);
          ProjectionMap localProjectionMap14 = this.mProjectionMap;
          Object[] arrayOfObject15 = arrayOfObject1;
          localProjectionMap14.writeValueToArray(arrayOfObject15, "music_trackImageUri", str6);
          DetailAlbumTracklistCursor localDetailAlbumTracklistCursor = this;
          Object[] arrayOfObject16 = arrayOfObject1;
          localDetailAlbumTracklistCursor.addRow(arrayOfObject16);
          i += 1;
        }
        else
        {
          Intent localIntent5 = localIntent1.putExtra("container", 1);
          Long localLong = Long.valueOf(paramString);
          Intent localIntent6 = localIntent1.putExtra("id", localLong);
        }
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailAlbumTracklistCursor
 * JD-Core Version:    0.6.2
 */