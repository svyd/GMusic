package com.google.android.music.xdi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.SelectedSongList;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.Store;
import com.google.android.music.store.TagNormalizer;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.ExploreDocumentClusterBuilder;
import com.google.android.music.utils.MusicUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class DetailArtistLockerSongsCursor extends MatrixCursor
{
  private final Context mContext;
  private final ProjectionMap mProjectionMap;

  DetailArtistLockerSongsCursor(Context paramContext, String[] paramArrayOfString, String paramString)
  {
    super(paramArrayOfString);
    this.mContext = paramContext;
    ProjectionMap localProjectionMap = new ProjectionMap(paramArrayOfString);
    this.mProjectionMap = localProjectionMap;
    long l1;
    if (MusicUtils.isNautilusId(paramString))
      l1 = ((Long)Store.canonicalizeAndGenerateId(new TagNormalizer(), paramString).first).longValue();
    while (true)
    {
      addRowsForLockerArtist(l1);
      return;
      try
      {
        long l2 = Long.valueOf(paramString).longValue();
        l1 = l2;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        String str = "Error converting artist ID to long: " + paramString;
        Log.wtf("MusicXdi", str);
      }
    }
  }

  private void addRowsForLockerArtist(long paramLong)
  {
    Object[] arrayOfObject1 = new Object[this.mProjectionMap.size()];
    Context localContext = this.mContext;
    Uri localUri = MusicContent.Artists.getAudioByArtistUri(paramLong, null);
    String[] arrayOfString = MusicProjections.SONG_COLUMNS;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString, null, null, null);
    if (localCursor == null)
      return;
    try
    {
      int i = localCursor.getCount();
      ArrayList localArrayList = Lists.newArrayListWithCapacity(i);
      int j = i;
      label72: Document localDocument;
      for (String str1 = null; localCursor.moveToNext(); str1 = localDocument.getArtistName())
      {
        localDocument = ExploreDocumentClusterBuilder.getTrackDocument(new Document(), localCursor);
        boolean bool1 = localArrayList.add(localDocument);
        int k = localCursor.getPosition();
        long l = localDocument.getId();
        j[k] = l;
        if ((str1 != null) || (TextUtils.isEmpty(localDocument.getArtistName())))
          break label72;
      }
      boolean bool2 = localArrayList.isEmpty();
      if (bool2)
        return;
      SelectedSongList localSelectedSongList = new com/google/android/music/medialist/SelectedSongList;
      if (str1 == null)
        str1 = "";
      int m = j;
      localSelectedSongList.<init>(str1, m);
      String str2 = localSelectedSongList.freeze();
      int n = 0;
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        localDocument = (Document)localIterator.next();
        String str3 = localDocument.getArtUrl();
        if (TextUtils.isEmpty(str3))
          str3 = XdiUtils.getDefaultArtUri(this.mContext);
        Intent localIntent1 = XdiUtils.newXdiPlayIntent().putExtra("container", 23);
        String str4 = str2;
        Intent localIntent2 = localIntent1.putExtra("song_list", str4).putExtra("offset", n);
        ProjectionMap localProjectionMap1 = this.mProjectionMap;
        Integer localInteger1 = Integer.valueOf(n);
        Object[] arrayOfObject2 = arrayOfObject1;
        localProjectionMap1.writeValueToArray(arrayOfObject2, "_id", localInteger1);
        ProjectionMap localProjectionMap2 = this.mProjectionMap;
        String str5 = localDocument.getTitle();
        Object[] arrayOfObject3 = arrayOfObject1;
        localProjectionMap2.writeValueToArray(arrayOfObject3, "display_name", str5);
        ProjectionMap localProjectionMap3 = this.mProjectionMap;
        String str6 = localDocument.getAlbumName();
        Object[] arrayOfObject4 = arrayOfObject1;
        localProjectionMap3.writeValueToArray(arrayOfObject4, "display_subname", str6);
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
        String str7 = localIntent2.toUri(1);
        Object[] arrayOfObject11 = arrayOfObject1;
        localProjectionMap10.writeValueToArray(arrayOfObject11, "action_uri", str7);
        ProjectionMap localProjectionMap11 = this.mProjectionMap;
        Long localLong = Long.valueOf(localDocument.getDuration());
        Object[] arrayOfObject12 = arrayOfObject1;
        localProjectionMap11.writeValueToArray(arrayOfObject12, "music_duration", localLong);
        ProjectionMap localProjectionMap12 = this.mProjectionMap;
        String str8 = localDocument.getArtistName();
        Object[] arrayOfObject13 = arrayOfObject1;
        localProjectionMap12.writeValueToArray(arrayOfObject13, "music_trackArtist", str8);
        ProjectionMap localProjectionMap13 = this.mProjectionMap;
        String str9 = localDocument.getAlbumName();
        Object[] arrayOfObject14 = arrayOfObject1;
        localProjectionMap13.writeValueToArray(arrayOfObject14, "music_album", str9);
        DetailArtistLockerSongsCursor localDetailArtistLockerSongsCursor = this;
        Object[] arrayOfObject15 = arrayOfObject1;
        localDetailArtistLockerSongsCursor.addRow(arrayOfObject15);
        n += 1;
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
 * Qualified Name:     com.google.android.music.xdi.DetailArtistLockerSongsCursor
 * JD-Core Version:    0.6.2
 */