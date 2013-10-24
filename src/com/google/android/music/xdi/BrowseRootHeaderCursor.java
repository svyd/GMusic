package com.google.android.music.xdi;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.MatrixCursor;
import android.net.Uri;
import android.net.Uri.Builder;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.mix.MixDescriptor.Type;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.AlbumArt;
import com.google.android.music.store.MusicContent.Mainstage;
import com.google.android.music.store.MusicContent.PlaylistArt;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.store.MusicContent.RadioStations;
import com.google.android.music.utils.LabelUtils;
import com.google.android.music.utils.MusicUtils;

class BrowseRootHeaderCursor extends XdiCursor
{
  private static final String[] PROJECTION_MAINSTAGE;
  private static final String[] PROJECTION_PLAYLISTS;
  private static final String[] PROJECTION_RADIO_STATIONS = arrayOfString3;
  private final int mArtistItemHeight;
  private final int mArtistItemWidth;
  private final long mHeaderId;
  private final int mImageHeight;
  private final int mImageWidth;

  static
  {
    String[] arrayOfString1 = new String[13];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "album_id";
    arrayOfString1[2] = "album_name";
    arrayOfString1[3] = "playlist_id";
    arrayOfString1[4] = "playlist_name";
    arrayOfString1[5] = "playlist_type";
    arrayOfString1[6] = "album_artist_id";
    arrayOfString1[7] = "album_artist";
    arrayOfString1[8] = "radio_id";
    arrayOfString1[9] = "radio_name";
    arrayOfString1[10] = "radio_art";
    arrayOfString1[11] = "StoreAlbumId";
    arrayOfString1[12] = "artworkUrl";
    PROJECTION_MAINSTAGE = arrayOfString1;
    String[] arrayOfString2 = new String[2];
    arrayOfString2[0] = "_id";
    arrayOfString2[1] = "playlist_name";
    PROJECTION_PLAYLISTS = arrayOfString2;
    String[] arrayOfString3 = new String[5];
    arrayOfString3[0] = "_id";
    arrayOfString3[1] = "radio_name";
    arrayOfString3[2] = "radio_art";
    arrayOfString3[3] = "radio_seed_source_id";
    arrayOfString3[4] = "radio_seed_source_type";
  }

  BrowseRootHeaderCursor(Context paramContext, String[] paramArrayOfString, long paramLong)
  {
    super(paramContext, paramArrayOfString, localCursor);
    this.mHeaderId = paramLong;
    int i = XdiUtils.getDefaultItemWidthPx(paramContext);
    this.mImageWidth = i;
    int j = XdiUtils.getDefaultItemHeightPx(paramContext);
    this.mImageHeight = j;
    int k = XdiUtils.getDefaultArtistItemWidthDp(paramContext);
    this.mArtistItemWidth = k;
    int m = XdiUtils.getDefaultArtistItemHeightDp(paramContext);
    this.mArtistItemHeight = m;
  }

  private void extractDataForMainstage(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Context localContext = getContext();
    int i = 0;
    long l1 = localCursor.getLong(i);
    String str1 = null;
    String str2 = null;
    Object localObject1 = null;
    Intent localIntent1 = null;
    int j = 1;
    if (!localCursor.isNull(j))
    {
      int k = 1;
      long l2 = localCursor.getLong(k);
      int m = 2;
      str2 = localCursor.getString(m);
      int n = 7;
      localObject1 = localCursor.getString(n);
      int i1 = this.mImageWidth;
      int i2 = this.mImageHeight;
      boolean bool = true;
      int i3 = i1;
      int i4 = i2;
      str1 = MusicContent.AlbumArt.getAlbumArtUri(l2, bool, i3, i4).toString();
      Uri.Builder localBuilder1 = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("details/albums");
      String str3 = String.valueOf(l2);
      localIntent1 = XdiContract.getDetailsIntent(localBuilder1.appendPath(str3).build());
    }
    while (true)
    {
      Long localLong1 = Long.valueOf(l1);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor1 = this;
      Object[] arrayOfObject1 = paramArrayOfObject;
      String str4 = "_id";
      Long localLong2 = localLong1;
      localBrowseRootHeaderCursor1.writeValueToArray(arrayOfObject1, str4, localLong2);
      Integer localInteger1 = Integer.valueOf(1);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor2 = this;
      Object[] arrayOfObject2 = paramArrayOfObject;
      String str5 = "parent_id";
      Integer localInteger2 = localInteger1;
      localBrowseRootHeaderCursor2.writeValueToArray(arrayOfObject2, str5, localInteger2);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor3 = this;
      Object[] arrayOfObject3 = paramArrayOfObject;
      String str6 = "display_name";
      String str7 = str2;
      localBrowseRootHeaderCursor3.writeValueToArray(arrayOfObject3, str6, str7);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor4 = this;
      Object[] arrayOfObject4 = paramArrayOfObject;
      String str8 = "display_description";
      localBrowseRootHeaderCursor4.writeValueToArray(arrayOfObject4, str8, localObject1);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor5 = this;
      Object[] arrayOfObject5 = paramArrayOfObject;
      String str9 = "image_uri";
      localBrowseRootHeaderCursor5.writeValueToArray(arrayOfObject5, str9, str1);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor6 = this;
      Object[] arrayOfObject6 = paramArrayOfObject;
      String str10 = "width";
      Object localObject2 = null;
      localBrowseRootHeaderCursor6.writeValueToArray(arrayOfObject6, str10, localObject2);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor7 = this;
      Object[] arrayOfObject7 = paramArrayOfObject;
      String str11 = "height";
      Object localObject3 = null;
      localBrowseRootHeaderCursor7.writeValueToArray(arrayOfObject7, str11, localObject3);
      int i5 = 1;
      String str12 = localIntent1.toUri(i5);
      BrowseRootHeaderCursor localBrowseRootHeaderCursor8 = this;
      Object[] arrayOfObject8 = paramArrayOfObject;
      String str13 = "intent_uri";
      String str14 = str12;
      localBrowseRootHeaderCursor8.writeValueToArray(arrayOfObject8, str13, str14);
      return;
      int i6 = 3;
      if (!localCursor.isNull(i6))
      {
        int i7 = 3;
        long l3 = localCursor.getLong(i7);
        int i8 = 4;
        String str15 = localCursor.getString(i8);
        int i9 = 5;
        int i10 = localCursor.getInt(i9);
        int i11 = i10;
        str2 = LabelUtils.getPlaylistPrimaryLabel(localContext, str15, i11);
        BrowseRootHeaderCursor localBrowseRootHeaderCursor9 = this;
        MusicPreferences localMusicPreferences1 = MusicPreferences.getMusicPreferences(localContext, localBrowseRootHeaderCursor9);
        Intent localIntent2;
        try
        {
          MusicPreferences localMusicPreferences2 = localMusicPreferences1;
          int i12 = i10;
          String str16 = LabelUtils.getPlaylistSecondaryLabel(localContext, localMusicPreferences2, i12);
          localObject1 = str16;
          MusicPreferences.releaseMusicPreferences(this);
          int i13 = this.mImageWidth;
          int i14 = this.mImageHeight;
          int i15 = i13;
          int i16 = i14;
          str1 = MusicContent.PlaylistArt.getPlaylistArtUri(l3, i15, i16).toString();
          switch (i10)
          {
          default:
            Uri localUri1 = XdiContentProvider.BASE_URI;
            String str17 = "details/playlists/" + l3;
            Uri localUri2 = Uri.withAppendedPath(localUri1, str17);
            String str18 = "com.google.android.xdi.action.DETAIL";
            Uri localUri3 = localUri2;
            localIntent1 = new Intent(str18, localUri3);
          case 60:
          case 50:
          }
        }
        finally
        {
          MusicPreferences.releaseMusicPreferences(this);
        }
        String str19 = "name";
        String str20 = str2;
        Intent localIntent3 = localIntent2.putExtra(str19, str20);
        String str21 = "id";
        localIntent1 = localIntent3.putExtra(str21, l3);
        continue;
        Intent localIntent4 = XdiUtils.newXdiPlayIntent().putExtra("container", 3);
        String str22 = "name";
        String str23 = str2;
        Intent localIntent5 = localIntent4.putExtra(str22, str23);
        String str24 = "id";
        localIntent1 = localIntent5.putExtra(str24, l3);
      }
      else
      {
        int i17 = 8;
        if (!localCursor.isNull(i17))
        {
          int i18 = 8;
          long l4 = localCursor.getLong(i18);
          int i19 = 9;
          str2 = localCursor.getString(i19);
          int i20 = 2131231307;
          localObject1 = localContext.getString(i20);
          int i21 = 10;
          str1 = localCursor.getString(i21);
          Intent localIntent6 = XdiUtils.newXdiPlayIntent().putExtra("container", 4);
          String str25 = "id";
          long l5 = l4;
          Intent localIntent7 = localIntent6.putExtra(str25, l5);
          int i22 = MixDescriptor.Type.RADIO.ordinal();
          Intent localIntent8 = localIntent7.putExtra("seed_type", i22);
          String str26 = "art_uri";
          Intent localIntent9 = localIntent8.putExtra(str26, str1);
          String str27 = "name";
          String str28 = str2;
          localIntent1 = localIntent9.putExtra(str27, str28);
        }
        else
        {
          int i23 = 11;
          if (!localCursor.isNull(i23))
          {
            int i24 = 11;
            String str29 = localCursor.getString(i24);
            int i25 = 2;
            str2 = localCursor.getString(i25);
            int i26 = 7;
            localObject1 = localCursor.getString(i26);
            int i27 = 12;
            int i28;
            if (!localCursor.isNull(i27))
              i28 = 12;
            for (str1 = localCursor.getString(i28); ; str1 = XdiUtils.getDefaultAlbumArtUri(localContext))
            {
              Uri.Builder localBuilder2 = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("details/albums");
              String str30 = String.valueOf(str29);
              localIntent1 = XdiContract.getDetailsIntent(localBuilder2.appendPath(str30).build());
              break;
            }
          }
          StringBuilder localStringBuilder = new StringBuilder().append("Unexpected mainstage item: ");
          String str31 = DatabaseUtils.dumpCursorToString(localCursor);
          String str32 = str31;
          Exception localException = new Exception();
          Log.wtf("MusicXdi", str32, localException);
        }
      }
    }
  }

  private void extractDataForMyMusic(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Long localLong1 = Long.valueOf(localCursor.getLong(0));
    writeValueToArray(paramArrayOfObject, "_id", localLong1);
    Long localLong2 = Long.valueOf(localCursor.getLong(1));
    writeValueToArray(paramArrayOfObject, "parent_id", localLong2);
    String str1 = localCursor.getString(2);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    String str2 = localCursor.getString(3);
    writeValueToArray(paramArrayOfObject, "display_description", str2);
    String str3 = localCursor.getString(4);
    writeValueToArray(paramArrayOfObject, "image_uri", str3);
    Integer localInteger1 = Integer.valueOf(localCursor.getInt(5));
    writeValueToArray(paramArrayOfObject, "width", localInteger1);
    Integer localInteger2 = Integer.valueOf(localCursor.getInt(6));
    writeValueToArray(paramArrayOfObject, "height", localInteger2);
    String str4 = localCursor.getString(7);
    writeValueToArray(paramArrayOfObject, "intent_uri", str4);
  }

  private void extractDataForPlaylists(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    long l = localCursor.getLong(0);
    String str1 = localCursor.getString(1);
    int i = this.mImageWidth;
    int j = this.mImageHeight;
    String str2 = MusicContent.PlaylistArt.getPlaylistArtUri(l, i, j).toString();
    Uri.Builder localBuilder = XdiContentProvider.BASE_URI.buildUpon().appendEncodedPath("details/playlists");
    String str3 = String.valueOf(l);
    Intent localIntent = XdiContract.getDetailsIntent(localBuilder.appendPath(str3).build());
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    Integer localInteger = Integer.valueOf(3);
    writeValueToArray(paramArrayOfObject, "parent_id", localInteger);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    String str4 = getContext().getString(2131230951);
    writeValueToArray(paramArrayOfObject, "display_description", str4);
    writeValueToArray(paramArrayOfObject, "image_uri", str2);
    writeValueToArray(paramArrayOfObject, "width", null);
    writeValueToArray(paramArrayOfObject, "height", null);
    String str5 = localIntent.toUri(1);
    writeValueToArray(paramArrayOfObject, "intent_uri", str5);
  }

  private void extractDataForRadioStations(Object[] paramArrayOfObject)
  {
    Integer localInteger1 = null;
    int i = 0;
    Cursor localCursor = getWrappedCursor();
    long l = localCursor.getLong(i);
    String str1 = localCursor.getString(1);
    if (localCursor.getInt(4) == 4)
      i = 1;
    String str2 = localCursor.getString(2);
    if (TextUtils.isEmpty(str2))
      str2 = XdiUtils.getDefaultArtUri(getContext());
    Intent localIntent = XdiUtils.newXdiPlayIntent().putExtra("container", 4).putExtra("id", l).putExtra("name", str1).putExtra("art_uri", str2);
    Long localLong = Long.valueOf(l);
    writeValueToArray(paramArrayOfObject, "_id", localLong);
    Integer localInteger2 = Integer.valueOf(4);
    writeValueToArray(paramArrayOfObject, "parent_id", localInteger2);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    String str3 = getContext().getString(2131230843);
    writeValueToArray(paramArrayOfObject, "display_description", str3);
    writeValueToArray(paramArrayOfObject, "image_uri", str2);
    String str4 = "width";
    if (i != 0);
    for (Integer localInteger3 = Integer.valueOf(this.mArtistItemWidth); ; localInteger3 = null)
    {
      writeValueToArray(paramArrayOfObject, str4, localInteger3);
      String str5 = "height";
      if (i != 0)
        localInteger1 = Integer.valueOf(this.mArtistItemHeight);
      writeValueToArray(paramArrayOfObject, str5, localInteger1);
      String str6 = localIntent.toUri(1);
      writeValueToArray(paramArrayOfObject, "intent_uri", str6);
      return;
    }
  }

  private void extractDataFromItemCursor(Object[] paramArrayOfObject)
  {
    Cursor localCursor = getWrappedCursor();
    Long localLong1 = Long.valueOf(localCursor.getLong(0));
    writeValueToArray(paramArrayOfObject, "_id", localLong1);
    Long localLong2 = Long.valueOf(localCursor.getLong(1));
    writeValueToArray(paramArrayOfObject, "parent_id", localLong2);
    String str1 = localCursor.getString(2);
    writeValueToArray(paramArrayOfObject, "display_name", str1);
    String str2 = localCursor.getString(3);
    writeValueToArray(paramArrayOfObject, "display_description", str2);
    String str3 = localCursor.getString(4);
    writeValueToArray(paramArrayOfObject, "image_uri", str3);
    Integer localInteger1 = Integer.valueOf(localCursor.getInt(5));
    writeValueToArray(paramArrayOfObject, "width", localInteger1);
    Integer localInteger2 = Integer.valueOf(localCursor.getInt(6));
    writeValueToArray(paramArrayOfObject, "height", localInteger2);
    String str4 = localCursor.getString(7);
    writeValueToArray(paramArrayOfObject, "intent_uri", str4);
  }

  private static Cursor getCursorForHeader(Context paramContext, long paramLong)
  {
    Object localObject;
    switch ((int)paramLong)
    {
    default:
      String str1 = "Unexpected header id: " + paramLong;
      Log.wtf("MusicXdi", str1);
      localObject = new MatrixCursor(null);
    case 1:
    case 2:
    case 3:
    case 4:
    case 0:
    case 6:
    case 5:
    }
    while (true)
    {
      return localObject;
      Uri localUri1 = MusicContent.Mainstage.CONTENT_URI;
      String[] arrayOfString1 = PROJECTION_MAINSTAGE;
      Context localContext1 = paramContext;
      String[] arrayOfString2 = null;
      String str2 = null;
      localObject = MusicUtils.query(localContext1, localUri1, arrayOfString1, null, arrayOfString2, str2);
      continue;
      localObject = new MyMusicCursor(paramContext, null);
      continue;
      Uri localUri2 = MusicContent.Playlists.CONTENT_URI;
      String[] arrayOfString3 = PROJECTION_PLAYLISTS;
      Context localContext2 = paramContext;
      String[] arrayOfString4 = null;
      String str3 = null;
      localObject = MusicUtils.query(localContext2, localUri2, arrayOfString3, null, arrayOfString4, str3);
      continue;
      Uri localUri3 = MusicContent.RadioStations.CONTENT_URI;
      String[] arrayOfString5 = PROJECTION_RADIO_STATIONS;
      Context localContext3 = paramContext;
      String[] arrayOfString6 = null;
      String str4 = null;
      localObject = MusicUtils.query(localContext3, localUri3, arrayOfString5, null, arrayOfString6, str4);
      continue;
      localObject = new SearchCursor(paramContext, null);
      continue;
      localObject = new SettingsCursor(paramContext, null);
      continue;
      localObject = new ExploreCursor(paramContext, null);
    }
  }

  protected boolean extractDataForCurrentRow(Object[] paramArrayOfObject)
  {
    boolean bool;
    switch ((int)this.mHeaderId)
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unexpected header id: ");
      long l = this.mHeaderId;
      String str = l;
      Log.wtf("MusicXdi", str);
      bool = false;
      return bool;
    case 1:
      extractDataForMainstage(paramArrayOfObject);
    case 2:
    case 3:
    case 4:
    case 0:
    case 6:
    case 5:
    }
    while (true)
    {
      bool = true;
      break;
      extractDataForMyMusic(paramArrayOfObject);
      continue;
      extractDataForPlaylists(paramArrayOfObject);
      continue;
      extractDataForRadioStations(paramArrayOfObject);
      continue;
      extractDataFromItemCursor(paramArrayOfObject);
      continue;
      extractDataFromItemCursor(paramArrayOfObject);
      continue;
      extractDataFromItemCursor(paramArrayOfObject);
    }
  }

  private static class ExploreCursor extends MatrixCursor
  {
    private ExploreCursor(Context paramContext)
    {
      super();
      String[] arrayOfString2 = XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      ProjectionMap localProjectionMap = new ProjectionMap(arrayOfString2);
      int i = 0;
      while (true)
      {
        int j = XdiConstants.BROWSE_EXPLORE_HEADER_NAMES.length;
        if (i >= j)
          return;
        Object[] arrayOfObject = new Object[XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION.length];
        int k = XdiConstants.BROWSE_EXPLORE_HEADER_NAMES[i];
        String str1 = paramContext.getString(k);
        Uri localUri = XdiContentProvider.BASE_URI.buildUpon().appendPath("explore").build();
        int m = XdiConstants.BROWSE_EXPLORE_INDEX[i];
        Intent localIntent1 = XdiContract.getBrowseIntent(localUri, m);
        String str2 = XdiUtils.getMetaUri(3L).toString();
        Intent localIntent2 = localIntent1.putExtra("meta_uri", str2);
        Integer localInteger1 = Integer.valueOf(i + 1);
        localProjectionMap.writeValueToArray(arrayOfObject, "_id", localInteger1);
        Integer localInteger2 = Integer.valueOf(5);
        localProjectionMap.writeValueToArray(arrayOfObject, "parent_id", localInteger2);
        localProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
        localProjectionMap.writeValueToArray(arrayOfObject, "display_description", null);
        String str3 = XdiUtils.getDefaultArtUri(paramContext);
        localProjectionMap.writeValueToArray(arrayOfObject, "image_uri", str3);
        localProjectionMap.writeValueToArray(arrayOfObject, "width", null);
        localProjectionMap.writeValueToArray(arrayOfObject, "height", null);
        String str4 = localIntent1.toUri(1);
        localProjectionMap.writeValueToArray(arrayOfObject, "intent_uri", str4);
        addRow(arrayOfObject);
        i += 1;
      }
    }
  }

  private static class SettingsCursor extends MatrixCursor
  {
    private SettingsCursor(Context paramContext)
    {
      super();
      String[] arrayOfString2 = XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      ProjectionMap localProjectionMap = new ProjectionMap(arrayOfString2);
      Object[] arrayOfObject = new Object[XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION.length];
      Account localAccount = XdiUtils.getSelectedAccount(paramContext);
      Object localObject = null;
      if (localAccount == null);
      for (String str1 = UriUtils.getAndroidResourceUri(paramContext, 2130837702); ; str1 = UriUtils.getAccountImageUri(localAccount.name).toString())
      {
        Integer localInteger = Integer.valueOf(0);
        localProjectionMap.writeValueToArray(arrayOfObject, "_id", localInteger);
        String str2 = paramContext.getString(2131230765);
        localProjectionMap.writeValueToArray(arrayOfObject, "display_name", str2);
        localProjectionMap.writeValueToArray(arrayOfObject, "display_description", localObject);
        localProjectionMap.writeValueToArray(arrayOfObject, "image_uri", str1);
        String str3 = AccountSwitcherActivity.createIntentUri(paramContext);
        localProjectionMap.writeValueToArray(arrayOfObject, "intent_uri", str3);
        addRow(arrayOfObject);
        return;
        localObject = localAccount.name;
      }
    }
  }

  private static class SearchCursor extends MatrixCursor
  {
    private SearchCursor(Context paramContext)
    {
      super();
      String[] arrayOfString2 = XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      ProjectionMap localProjectionMap = new ProjectionMap(arrayOfString2);
      Object[] arrayOfObject = new Object[XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION.length];
      Uri localUri = XdiContentProvider.BASE_URI.buildUpon().appendPath("search").build();
      Intent localIntent1 = new Intent("com.google.android.xdi.action.SEARCH", localUri);
      Intent localIntent2 = localIntent1.putExtra("display_mode", 2);
      Integer localInteger1 = Integer.valueOf(1);
      localProjectionMap.writeValueToArray(arrayOfObject, "_id", localInteger1);
      Integer localInteger2 = Integer.valueOf(0);
      localProjectionMap.writeValueToArray(arrayOfObject, "parent_id", localInteger2);
      localProjectionMap.writeValueToArray(arrayOfObject, "display_name", null);
      String str1 = UriUtils.getAndroidResourceUri(paramContext, 2130837781);
      localProjectionMap.writeValueToArray(arrayOfObject, "image_uri", str1);
      String str2 = localIntent1.toUri(1);
      localProjectionMap.writeValueToArray(arrayOfObject, "intent_uri", str2);
      addRow(arrayOfObject);
    }
  }

  private static class MyMusicCursor extends MatrixCursor
  {
    private MyMusicCursor(Context paramContext)
    {
      super();
      String[] arrayOfString2 = XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION;
      ProjectionMap localProjectionMap = new ProjectionMap(arrayOfString2);
      int i = 0;
      while (true)
      {
        int j = XdiConstants.BROWSE_MY_MUSIC_HEADER_NAMES.length;
        if (i >= j)
          return;
        Object[] arrayOfObject = new Object[XdiContentProvider.DEFAULT_BROWSE_HEADER_ID_PROJECTION.length];
        int k = XdiConstants.BROWSE_MY_MUSIC_HEADER_NAMES[i];
        String str1 = paramContext.getString(k);
        Uri localUri = XdiContentProvider.BASE_URI.buildUpon().appendPath("mymusic").build();
        int m = XdiConstants.BROWSE_MY_MUSIC_INDEX[i];
        Intent localIntent1 = XdiContract.getBrowseIntent(localUri, m);
        String str2 = XdiUtils.getMetaUri(1L).toString();
        Intent localIntent2 = localIntent1.putExtra("meta_uri", str2);
        Integer localInteger1 = Integer.valueOf(i + 1);
        localProjectionMap.writeValueToArray(arrayOfObject, "_id", localInteger1);
        Integer localInteger2 = Integer.valueOf(2);
        localProjectionMap.writeValueToArray(arrayOfObject, "parent_id", localInteger2);
        localProjectionMap.writeValueToArray(arrayOfObject, "display_name", str1);
        localProjectionMap.writeValueToArray(arrayOfObject, "display_description", null);
        String str3 = XdiUtils.getDefaultArtUri(paramContext);
        localProjectionMap.writeValueToArray(arrayOfObject, "image_uri", str3);
        localProjectionMap.writeValueToArray(arrayOfObject, "width", null);
        localProjectionMap.writeValueToArray(arrayOfObject, "height", null);
        String str4 = localIntent1.toUri(1);
        localProjectionMap.writeValueToArray(arrayOfObject, "intent_uri", str4);
        addRow(arrayOfObject);
        i += 1;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.BrowseRootHeaderCursor
 * JD-Core Version:    0.6.2
 */