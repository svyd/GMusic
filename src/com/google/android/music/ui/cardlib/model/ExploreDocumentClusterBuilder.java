package com.google.android.music.ui.cardlib.model;

import android.database.Cursor;
import com.google.android.music.store.MusicContent.SharedWithMePlaylist;
import java.util.ArrayList;

public class ExploreDocumentClusterBuilder
{
  public static final String[] ALBUM_COLUMNS;
  public static final String[] ARTIST_COLUMNS;
  public static final String[] SHARED_WITH_ME_PLAYLIST_COLUMNS = arrayOfString4;
  public static final String[] SONG_COLUMNS;

  static
  {
    String[] arrayOfString1 = new String[11];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "Nid";
    arrayOfString1[2] = "StoreAlbumId";
    arrayOfString1[3] = "ArtistMetajamId";
    arrayOfString1[4] = "title";
    arrayOfString1[5] = "album";
    arrayOfString1[6] = "album_id";
    arrayOfString1[7] = "artist";
    arrayOfString1[8] = "artist_id";
    arrayOfString1[9] = "artworkUrl";
    arrayOfString1[10] = "duration";
    SONG_COLUMNS = arrayOfString1;
    String[] arrayOfString2 = new String[8];
    arrayOfString2[0] = "_id";
    arrayOfString2[1] = "StoreAlbumId";
    arrayOfString2[2] = "ArtistMetajamId";
    arrayOfString2[3] = "album_name";
    arrayOfString2[4] = "album_artist";
    arrayOfString2[5] = "album_artist_id";
    arrayOfString2[6] = "album_art";
    arrayOfString2[7] = "artworkUrl";
    ALBUM_COLUMNS = arrayOfString2;
    String[] arrayOfString3 = new String[4];
    arrayOfString3[0] = "_id";
    arrayOfString3[1] = "ArtistMetajamId";
    arrayOfString3[2] = "artist";
    arrayOfString3[3] = "artworkUrl";
    ARTIST_COLUMNS = arrayOfString3;
    String[] arrayOfString4 = new String[9];
    arrayOfString4[0] = "_id";
    String str1 = MusicContent.SharedWithMePlaylist.NAME;
    arrayOfString4[1] = str1;
    String str2 = MusicContent.SharedWithMePlaylist.DESCRIPTION;
    arrayOfString4[2] = str2;
    String str3 = MusicContent.SharedWithMePlaylist.SHARE_TOKEN;
    arrayOfString4[3] = str3;
    String str4 = MusicContent.SharedWithMePlaylist.OWNER_NAME;
    arrayOfString4[4] = str4;
    String str5 = MusicContent.SharedWithMePlaylist.ART_URL;
    arrayOfString4[5] = str5;
    String str6 = MusicContent.SharedWithMePlaylist.OWNER_PROFILE_PHOTO_URL;
    arrayOfString4[6] = str6;
    String str7 = MusicContent.SharedWithMePlaylist.CREATION_TIMESTAMP;
    arrayOfString4[7] = str7;
    String str8 = MusicContent.SharedWithMePlaylist.LAST_MODIFIED_TIMESTAMP;
    arrayOfString4[8] = str8;
  }

  public static ArrayList<Document> buildAlbumDocumentList(Cursor paramCursor)
  {
    ArrayList localArrayList = new ArrayList();
    int i;
    int j;
    if (paramCursor.moveToFirst())
    {
      i = paramCursor.getCount();
      j = 0;
    }
    while (true)
    {
      if (j < i)
      {
        Document localDocument = getAlbumDocument(new Document(), paramCursor);
        boolean bool = localArrayList.add(localDocument);
        if (paramCursor.moveToNext());
      }
      else
      {
        return localArrayList;
      }
      j += 1;
    }
  }

  public static ArrayList<Document> buildArtistDocumentList(Cursor paramCursor)
  {
    ArrayList localArrayList = new ArrayList();
    int i;
    int j;
    if (paramCursor.moveToFirst())
    {
      i = paramCursor.getCount();
      j = 0;
    }
    while (true)
    {
      if (j < i)
      {
        Document localDocument = getArtistDocument(new Document(), paramCursor);
        boolean bool = localArrayList.add(localDocument);
        if (paramCursor.moveToNext());
      }
      else
      {
        return localArrayList;
      }
      j += 1;
    }
  }

  public static ArrayList<Document> buildPlaylistDocumentList(Cursor paramCursor)
  {
    ArrayList localArrayList = new ArrayList();
    int i;
    int j;
    if (paramCursor.moveToFirst())
    {
      i = paramCursor.getCount();
      j = 0;
    }
    while (true)
    {
      if (j < i)
      {
        Document localDocument = getPlaylistDocument(new Document(), paramCursor);
        boolean bool = localArrayList.add(localDocument);
        if (paramCursor.moveToNext());
      }
      else
      {
        return localArrayList;
      }
      j += 1;
    }
  }

  public static ArrayList<Document> buildTrackDocumentList(Cursor paramCursor)
  {
    ArrayList localArrayList = new ArrayList();
    int i;
    int j;
    if (paramCursor.moveToFirst())
    {
      i = paramCursor.getCount();
      j = 0;
    }
    while (true)
    {
      if (j < i)
      {
        Document localDocument = getTrackDocument(new Document(), paramCursor);
        boolean bool = localArrayList.add(localDocument);
        if (paramCursor.moveToNext());
      }
      else
      {
        return localArrayList;
      }
      j += 1;
    }
  }

  public static Document getAlbumDocument(Document paramDocument, Cursor paramCursor)
  {
    paramDocument.reset();
    Document.Type localType = Document.Type.ALBUM;
    paramDocument.setType(localType);
    Document localDocument = populateDocumentFromAlbumCursor(paramDocument, paramCursor);
    String str1 = localDocument.getAlbumName();
    localDocument.setTitle(str1);
    String str2 = localDocument.getArtistName();
    localDocument.setSubTitle(str2);
    return localDocument;
  }

  public static Document getArtistDocument(Document paramDocument, Cursor paramCursor)
  {
    paramDocument.reset();
    Document.Type localType = Document.Type.ARTIST;
    paramDocument.setType(localType);
    Document localDocument = populateDocumentFromArtistCursor(paramDocument, paramCursor);
    String str = localDocument.getArtistName();
    localDocument.setTitle(str);
    return localDocument;
  }

  public static Document getPlaylistDocument(Document paramDocument, Cursor paramCursor)
  {
    paramDocument.reset();
    Document.Type localType = Document.Type.PLAYLIST;
    paramDocument.setType(localType);
    Document localDocument = populateDocumentFromPlaylistCursor(paramDocument, paramCursor);
    String str = localDocument.getPlaylistName();
    localDocument.setTitle(str);
    return localDocument;
  }

  public static Document getTrackDocument(Document paramDocument, Cursor paramCursor)
  {
    paramDocument.reset();
    Document.Type localType = Document.Type.TRACK;
    paramDocument.setType(localType);
    Document localDocument = populateDocumentFromSongCursor(paramDocument, paramCursor);
    String str = localDocument.getArtistName();
    localDocument.setSubTitle(str);
    return localDocument;
  }

  private static Document populateDocumentFromAlbumCursor(Document paramDocument, Cursor paramCursor)
  {
    long l1 = paramCursor.getLong(0);
    paramDocument.setId(l1);
    long l2 = paramCursor.getLong(0);
    paramDocument.setAlbumId(l2);
    String str1 = paramCursor.getString(3);
    paramDocument.setAlbumName(str1);
    String str2 = paramCursor.getString(4);
    paramDocument.setArtistName(str2);
    long l3 = paramCursor.getLong(5);
    paramDocument.setArtistId(l3);
    if (!paramCursor.isNull(7))
    {
      String str3 = paramCursor.getString(7);
      paramDocument.setArtUrl(str3);
    }
    if (!paramCursor.isNull(1))
    {
      String str4 = paramCursor.getString(1);
      paramDocument.setAlbumMetajamId(str4);
    }
    if (!paramCursor.isNull(2))
    {
      String str5 = paramCursor.getString(2);
      paramDocument.setArtistMetajamId(str5);
    }
    return paramDocument;
  }

  private static Document populateDocumentFromArtistCursor(Document paramDocument, Cursor paramCursor)
  {
    long l = paramCursor.getLong(0);
    paramDocument.setId(l);
    String str1 = paramCursor.getString(2);
    paramDocument.setArtistName(str1);
    paramDocument.setArtistId(0L);
    if (!paramCursor.isNull(3))
    {
      String str2 = paramCursor.getString(3);
      paramDocument.setArtUrl(str2);
    }
    if (!paramCursor.isNull(1))
    {
      String str3 = paramCursor.getString(1);
      paramDocument.setArtistMetajamId(str3);
    }
    return paramDocument;
  }

  private static Document populateDocumentFromPlaylistCursor(Document paramDocument, Cursor paramCursor)
  {
    paramDocument.setPlaylistType(70);
    long l = paramCursor.getLong(0);
    paramDocument.setId(l);
    String str1 = paramCursor.getString(1);
    paramDocument.setPlaylistName(str1);
    String str2 = paramCursor.getString(2);
    paramDocument.setDescription(str2);
    String str3 = paramCursor.getString(3);
    paramDocument.setPlaylistShareToken(str3);
    String str4 = paramCursor.getString(4);
    paramDocument.setPlaylistOwnerName(str4);
    String str5 = paramCursor.getString(5);
    paramDocument.setArtUrl(str5);
    String str6 = paramCursor.getString(6);
    paramDocument.setPlaylistOwnerProfilePhotoUrl(str6);
    return paramDocument;
  }

  private static Document populateDocumentFromSongCursor(Document paramDocument, Cursor paramCursor)
  {
    long l1 = paramCursor.getLong(0);
    paramDocument.setId(l1);
    String str1 = paramCursor.getString(4);
    paramDocument.setTitle(str1);
    long l2 = paramCursor.getLong(6);
    paramDocument.setAlbumId(l2);
    String str2 = paramCursor.getString(5);
    paramDocument.setAlbumName(str2);
    String str3 = paramCursor.getString(7);
    paramDocument.setArtistName(str3);
    long l3 = paramCursor.getLong(8);
    paramDocument.setArtistId(l3);
    if (!paramCursor.isNull(9))
    {
      String str4 = paramCursor.getString(9);
      paramDocument.setArtUrl(str4);
    }
    if (!paramCursor.isNull(1))
    {
      String str5 = paramCursor.getString(1);
      paramDocument.setTrackMetajamId(str5);
    }
    if (!paramCursor.isNull(2))
    {
      String str6 = paramCursor.getString(2);
      paramDocument.setAlbumMetajamId(str6);
    }
    if (!paramCursor.isNull(3))
    {
      String str7 = paramCursor.getString(3);
      paramDocument.setArtistMetajamId(str7);
    }
    if (!paramCursor.isNull(10))
    {
      long l4 = paramCursor.getLong(10);
      paramDocument.setDuration(l4);
    }
    return paramDocument;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.model.ExploreDocumentClusterBuilder
 * JD-Core Version:    0.6.2
 */