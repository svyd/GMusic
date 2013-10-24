package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.NautilusSelectedSongList;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.Store;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.ExploreDocumentClusterBuilder;
import com.google.android.music.utils.MusicUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DetailArtistTopSongsCursor extends MatrixCursor
{
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  DetailArtistTopSongsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    if (MusicUtils.isNautilusId(paramString))
    {
      addRowsForNautilusArtist(paramString);
      return;
    }
    addRowsForArtist(paramString);
  }

  private void addRowsForArtist(String paramString)
  {
    String str1;
    try
    {
      Long localLong = Long.valueOf(paramString);
      long l = localLong.longValue();
      str1 = getArtistMetajamId(l);
      if (TextUtils.isEmpty(str1))
      {
        String str2 = "Could not get nautilus ID for artist: " + paramString;
        Log.d("MusicXdi", str2);
        return;
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      String str3 = "Error converting artist ID to long: " + paramString;
      Log.wtf("MusicXdi", str3);
      return;
    }
    addRowsForNautilusArtist(str1);
  }

  private void addRowsForNautilusArtist(String paramString)
  {
    Object[] arrayOfObject1 = new Object[this.mProjectionMap.size()];
    Context localContext = this.mContext;
    Uri localUri = MusicContent.Artists.getTopSongsByArtistUri(paramString);
    String[] arrayOfString1 = MusicProjections.SONG_COLUMNS;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, null, null);
    if (localCursor == null)
      return;
    ArrayList localArrayList;
    String[] arrayOfString2;
    try
    {
      int i = localCursor.getCount();
      localArrayList = Lists.newArrayListWithCapacity(i);
      arrayOfString2 = new String[i];
      while (localCursor.moveToNext())
      {
        Document localDocument1 = ExploreDocumentClusterBuilder.getTrackDocument(new Document(), localCursor);
        boolean bool1 = localArrayList.add(localDocument1);
        int j = localCursor.getPosition();
        String str1 = localDocument1.getTrackMetajamId();
        arrayOfString2[j] = str1;
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
    boolean bool2 = localArrayList.isEmpty();
    if (bool2)
    {
      Store.safeClose(localCursor);
      return;
    }
    String[] arrayOfString3 = arrayOfString2;
    String str2 = new NautilusSelectedSongList(arrayOfString3).freeze();
    int k = 0;
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      Document localDocument2 = (Document)localIterator.next();
      String str3 = localDocument2.getArtUrl();
      if (TextUtils.isEmpty(str3))
        str3 = XdiUtils.getDefaultArtUri(this.mContext);
      Intent localIntent = XdiUtils.newXdiPlayIntent().putExtra("container", 12).putExtra("song_list", str2).putExtra("offset", k);
      ProjectionMap localProjectionMap1 = this.mProjectionMap;
      Integer localInteger1 = Integer.valueOf(k);
      Object[] arrayOfObject2 = arrayOfObject1;
      localProjectionMap1.writeValueToArray(arrayOfObject2, "_id", localInteger1);
      ProjectionMap localProjectionMap2 = this.mProjectionMap;
      String str4 = localDocument2.getTitle();
      Object[] arrayOfObject3 = arrayOfObject1;
      localProjectionMap2.writeValueToArray(arrayOfObject3, "display_name", str4);
      ProjectionMap localProjectionMap3 = this.mProjectionMap;
      String str5 = localDocument2.getAlbumName();
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
      localProjectionMap6.writeValueToArray(arrayOfObject7, "image_uri", str3);
      ProjectionMap localProjectionMap7 = this.mProjectionMap;
      Integer localInteger2 = Integer.valueOf(0);
      Object[] arrayOfObject8 = arrayOfObject1;
      localProjectionMap7.writeValueToArray(arrayOfObject8, "item_display_type", localInteger2);
      ProjectionMap localProjectionMap8 = this.mProjectionMap;
      Object[] arrayOfObject9 = arrayOfObject1;
      localProjectionMap8.writeValueToArray(arrayOfObject9, "user_rating_count", null);
      ProjectionMap localProjectionMap9 = this.mProjectionMap;
      Integer localInteger3 = Integer.valueOf(-1);
      Object[] arrayOfObject10 = arrayOfObject1;
      localProjectionMap9.writeValueToArray(arrayOfObject10, "user_rating", localInteger3);
      ProjectionMap localProjectionMap10 = this.mProjectionMap;
      String str6 = localIntent.toUri(1);
      Object[] arrayOfObject11 = arrayOfObject1;
      localProjectionMap10.writeValueToArray(arrayOfObject11, "action_uri", str6);
      ProjectionMap localProjectionMap11 = this.mProjectionMap;
      Long localLong = Long.valueOf(localDocument2.getDuration());
      Object[] arrayOfObject12 = arrayOfObject1;
      localProjectionMap11.writeValueToArray(arrayOfObject12, "music_duration", localLong);
      ProjectionMap localProjectionMap12 = this.mProjectionMap;
      String str7 = localDocument2.getArtistName();
      Object[] arrayOfObject13 = arrayOfObject1;
      localProjectionMap12.writeValueToArray(arrayOfObject13, "music_trackArtist", str7);
      ProjectionMap localProjectionMap13 = this.mProjectionMap;
      String str8 = localDocument2.getAlbumName();
      Object[] arrayOfObject14 = arrayOfObject1;
      localProjectionMap13.writeValueToArray(arrayOfObject14, "music_album", str8);
      DetailArtistTopSongsCursor localDetailArtistTopSongsCursor = this;
      Object[] arrayOfObject15 = arrayOfObject1;
      localDetailArtistTopSongsCursor.addRow(arrayOfObject15);
      k += 1;
    }
    Store.safeClose(localCursor);
  }

  public String getArtistMetajamId(long paramLong)
  {
    String[] arrayOfString = new String[1];
    arrayOfString[0] = "ArtistMetajamId";
    try
    {
      Context localContext = this.mContext;
      Uri localUri = MusicContent.Artists.getArtistsUri(paramLong);
      localCursor = MusicUtils.query(localContext, localUri, arrayOfString, null, null, null);
      if ((localCursor != null) && (localCursor.moveToFirst()) && (!localCursor.isNull(0)))
      {
        String str1 = localCursor.getString(0);
        str2 = str1;
        return str2;
      }
      Store.safeClose(localCursor);
      String str2 = null;
    }
    finally
    {
      Cursor localCursor;
      Store.safeClose(localCursor);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.DetailArtistTopSongsCursor
 * JD-Core Version:    0.6.2
 */