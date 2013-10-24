package com.google.android.music.menu;

import android.content.Context;
import java.util.ArrayList;

public abstract class MusicMenu
{
  protected Callback mCallback;
  protected Context mContext;
  protected ArrayList<MusicMenuItem> mItems;

  public abstract void close();

  public Context getContext()
  {
    return this.mContext;
  }

  public boolean getRadioButtonsEnabled()
  {
    throw new UnsupportedOperationException();
  }

  public ArrayList<MusicMenuItem> getVisibleItems()
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    while (true)
    {
      int j = this.mItems.size();
      if (i >= j)
        break;
      if (((MusicMenuItem)this.mItems.get(i)).isVisible())
      {
        Object localObject = this.mItems.get(i);
        boolean bool = localArrayList.add(localObject);
      }
      i += 1;
    }
    return localArrayList;
  }

  protected void onItemChecked(MusicMenuItem paramMusicMenuItem, boolean paramBoolean)
  {
    throw new UnsupportedOperationException();
  }

  public abstract boolean performItemAction(MusicMenuItem paramMusicMenuItem);

  public static abstract interface Callback
  {
    public abstract boolean onMusicMenuItemSelected(MusicMenuItem paramMusicMenuItem);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicMenu
 * JD-Core Version:    0.6.2
 */