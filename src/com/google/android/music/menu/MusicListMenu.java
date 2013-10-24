package com.google.android.music.menu;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import java.util.ArrayList;

public abstract class MusicListMenu extends MusicMenu
  implements View.OnKeyListener
{
  protected boolean mRadioButtonsEnabled;

  public boolean getRadioButtonsEnabled()
  {
    return this.mRadioButtonsEnabled;
  }

  protected void onItemChecked(MusicMenuItem paramMusicMenuItem, boolean paramBoolean)
  {
    if (!paramBoolean)
      return;
    if (!this.mRadioButtonsEnabled)
      return;
    int i = this.mItems.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      MusicListMenuItem localMusicListMenuItem = (MusicListMenuItem)this.mItems.get(j);
      if (localMusicListMenuItem != paramMusicMenuItem)
        localMusicListMenuItem.setCheckedInternal(false);
      j += 1;
    }
  }

  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    if ((paramInt == 4) || (paramInt == 82))
      if (paramKeyEvent.getAction() == 1)
        close();
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  public boolean performItemAction(MusicMenuItem paramMusicMenuItem)
  {
    boolean bool1;
    if ((paramMusicMenuItem == null) || (!paramMusicMenuItem.isEnabled()))
      bool1 = false;
    while (true)
    {
      return bool1;
      bool1 = false;
      if (paramMusicMenuItem.getSubMenu() != null)
      {
        bool1 = true;
        close();
        paramMusicMenuItem.getSubMenu().show();
      }
      else
      {
        if (this.mCallback != null)
          boolean bool2 = this.mCallback.onMusicMenuItemSelected(paramMusicMenuItem);
        close();
      }
    }
  }

  public abstract void show();
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicListMenu
 * JD-Core Version:    0.6.2
 */