package com.google.android.music.medialist;

public class ThumbsUpList extends AutoPlaylist
{
  public ThumbsUpList(int paramInt)
  {
    super(paramInt, 65532L, true);
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
    return 2131230888;
  }

  protected int getTitleResourceId()
  {
    return 2131230889;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.ThumbsUpList
 * JD-Core Version:    0.6.2
 */