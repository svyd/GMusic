package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.store.MusicContent.Playlists.Members;
import com.google.android.music.store.Store;
import com.google.android.music.utils.MusicUtils;

class DetailPlaylistTracklistCursor extends MatrixCursor
{
  private static final String[] PROJECTION_PLAYLIST_TRACKS = arrayOfString;
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  static
  {
    String[] arrayOfString = new String[7];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "SongId";
    arrayOfString[2] = "title";
    arrayOfString[3] = "artist";
    arrayOfString[4] = "duration";
    arrayOfString[5] = "album";
    arrayOfString[6] = "ArtistArtLocation";
  }

  DetailPlaylistTracklistCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    addRowsForTracks(paramLong);
  }

  private void addRowsForTracks(long paramLong)
  {
    Object[] arrayOfObject1 = new Object[this.mProjectionMap.size()];
    Context localContext1 = this.mContext;
    Uri localUri = MusicContent.Playlists.Members.getPlaylistItemsUri(paramLong);
    String[] arrayOfString = PROJECTION_PLAYLIST_TRACKS;
    Cursor localCursor = MusicUtils.query(localContext1, localUri, arrayOfString, null, null, null);
    if (localCursor == null)
      return;
    int i = 0;
    try
    {
      while (localCursor.moveToNext())
      {
        String str1 = localCursor.getString(1);
        String str2 = localCursor.getString(2);
        String str3 = localCursor.getString(3);
        int j = localCursor.getInt(4) / 1000;
        String str4 = localCursor.getString(5);
        String str5 = null;
        if (!localCursor.isNull(6))
          str5 = localCursor.getString(6);
        if (TextUtils.isEmpty(str5))
          str5 = XdiUtils.getDefaultArtUri(this.mContext);
        Context localContext2 = this.mContext;
        long l1 = j;
        String str6 = MusicUtils.makeTimeString(localContext2, l1);
        Intent localIntent1 = XdiUtils.newXdiPlayIntent().putExtra("container", 2);
        long l2 = paramLong;
        Intent localIntent2 = localIntent1.putExtra("id", l2).putExtra("offset", i).putExtra("name", "playlist");
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Object[] arrayOfObject2 = arrayOfObject1;
        String str7 = str1;
        localProjectionMap1.writeValueToArray(arrayOfObject2, "_id", str7);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        Object[] arrayOfObject3 = arrayOfObject1;
        String str8 = str2;
        localProjectionMap2.writeValueToArray(arrayOfObject3, "display_name", str8);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        Object[] arrayOfObject4 = arrayOfObject1;
        localProjectionMap3.writeValueToArray(arrayOfObject4, "display_subname", str6);
        ProjectionMap localProjectionMap4 = this.mProjectionMap;
        Object[] arrayOfObject5 = arrayOfObject1;
        localProjectionMap4.writeValueToArray(arrayOfObject5, "display_description", str3);
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
        String str9 = localIntent2.toUri(1);
        Object[] arrayOfObject11 = arrayOfObject1;
        localProjectionMap10.writeValueToArray(arrayOfObject11, "action_uri", str9);
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
        localProjectionMap14.writeValueToArray(arrayOfObject15, "music_trackImageUri", str5);
        DetailPlaylistTracklistCursor localDetailPlaylistTracklistCursor = this;
        Object[] arrayOfObject16 = arrayOfObject1;
        localDetailPlaylistTracklistCursor.addRow(arrayOfObject16);
        i += 1;
      }
      return;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailPlaylistTracklistCursor
 * JD-Core Version:    0.6.2
 */