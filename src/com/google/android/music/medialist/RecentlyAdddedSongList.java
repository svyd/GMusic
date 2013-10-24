package com.google.android.music.medialist;

public class RecentlyAdddedSongList extends AutoPlaylist
{
  public RecentlyAdddedSongList(int paramInt)
  {
    super(paramInt, 65535L);
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[1];
    String str = Integer.toString(getSortOrder());
    arrayOfString[0] = str;
    return arrayOfString;
  }

  protected int getListingNameResourceId()
  {
    return 2131230880;
  }

  protected int getTitleResourceId()
  {
    return 2131230881;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.RecentlyAdddedSongList
 * JD-Core Version:    0.6.2
 */