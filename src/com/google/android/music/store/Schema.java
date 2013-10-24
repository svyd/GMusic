package com.google.android.music.store;

public class Schema
{
  public static final String[] ALL_TABLES = arrayOfString;

  static
  {
    String[] arrayOfString = new String[16];
    arrayOfString[0] = "LISTITEMS";
    arrayOfString[1] = "LISTS";
    arrayOfString[2] = "MUSIC";
    arrayOfString[3] = "KEEPON";
    arrayOfString[4] = "SHOULDKEEPON";
    arrayOfString[5] = "ARTWORK";
    arrayOfString[6] = "ARTWORK_CACHE";
    arrayOfString[7] = "RECENT";
    arrayOfString[8] = "_sync_state";
    arrayOfString[9] = "LIST_TOMBSTONES";
    arrayOfString[10] = "LISTITEM_TOMBSTONES";
    arrayOfString[11] = "RINGTONES";
    arrayOfString[12] = "SUGGESTED_SEEDS";
    arrayOfString[13] = "PLAYQ_GROUPS";
    arrayOfString[14] = "RADIO_STATIONS";
    arrayOfString[15] = "RADIO_STATION_TOMBSTONES";
  }

  public static abstract interface Music
  {
    public static final String EMPTY_CANONICAL_SORT_KEY = new String(arrayOfChar);

    static
    {
      char[] arrayOfChar = Character.toChars(1114111);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.Schema
 * JD-Core Version:    0.6.2
 */