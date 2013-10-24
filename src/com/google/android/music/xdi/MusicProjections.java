package com.google.android.music.xdi;

class MusicProjections
{
  static final String[] PROJECTION_ENTITY_GROUPS;
  static final String[] PROJECTION_GENRES;
  static final String[] SONG_COLUMNS = arrayOfString3;

  static
  {
    String[] arrayOfString1 = new String[3];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "genreServerId";
    arrayOfString1[2] = "name";
    PROJECTION_GENRES = arrayOfString1;
    String[] arrayOfString2 = new String[5];
    arrayOfString2[0] = "_id";
    arrayOfString2[1] = "title";
    arrayOfString2[2] = "description";
    arrayOfString2[3] = "size";
    arrayOfString2[4] = "groupType";
    PROJECTION_ENTITY_GROUPS = arrayOfString2;
    String[] arrayOfString3 = new String[11];
    arrayOfString3[0] = "_id";
    arrayOfString3[1] = "Nid";
    arrayOfString3[2] = "StoreAlbumId";
    arrayOfString3[3] = "ArtistMetajamId";
    arrayOfString3[4] = "title";
    arrayOfString3[5] = "album";
    arrayOfString3[6] = "album_id";
    arrayOfString3[7] = "artist";
    arrayOfString3[8] = "artist_id";
    arrayOfString3[9] = "artworkUrl";
    arrayOfString3[10] = "duration";
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.MusicProjections
 * JD-Core Version:    0.6.2
 */