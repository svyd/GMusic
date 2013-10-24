package com.google.android.music.medialist;

public class StoreSongList extends AutoPlaylist
{
  private final boolean mHasStore;

  public StoreSongList(int paramInt, boolean paramBoolean)
  {
    super(paramInt, 65533L);
    this.mHasStore = paramBoolean;
  }

  public String[] getArgs()
  {
    String[] arrayOfString = new String[2];
    String str1 = Integer.toString(getSortOrder());
    arrayOfString[0] = str1;
    String str2 = Boolean.toString(this.mHasStore);
    arrayOfString[1] = str2;
    return arrayOfString;
  }

  protected int getListingNameResourceId()
  {
    if (this.mHasStore);
    for (int i = 2131230884; ; i = 2131230886)
      return i;
  }

  protected int getTitleResourceId()
  {
    if (this.mHasStore);
    for (int i = 2131230885; ; i = 2131230887)
      return i;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.medialist.StoreSongList
 * JD-Core Version:    0.6.2
 */