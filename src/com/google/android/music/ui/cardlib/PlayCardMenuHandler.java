package com.google.android.music.ui.cardlib;

import android.content.res.Resources;
import android.view.View;
import com.google.android.music.ui.cardlib.layout.PlayPopupMenu.OnActionSelectedListener;

public abstract interface PlayCardMenuHandler
{
  public abstract void showPopupMenu(View paramView);

  public static class MenuTask
    implements Runnable
  {
    private final PlayCardMenuHandler.MenuEntry mEntry;

    public MenuTask(PlayCardMenuHandler.MenuEntry paramMenuEntry)
    {
      this.mEntry = paramMenuEntry;
    }

    public void run()
    {
      this.mEntry.onActionSelected();
    }
  }

  public static abstract class UIThreadMenuEntry extends PlayCardMenuHandler.MenuEntry
  {
    public UIThreadMenuEntry(Resources paramResources, int paramInt1, int paramInt2)
    {
      super(str, null);
    }

    public UIThreadMenuEntry(Resources paramResources, int paramInt1, int paramInt2, View paramView)
    {
      super(str, paramView);
    }

    public boolean shouldRunAsync()
    {
      return false;
    }
  }

  public static abstract class AsyncMenuEntry extends PlayCardMenuHandler.MenuEntry
  {
    public AsyncMenuEntry(Resources paramResources, int paramInt1, int paramInt2)
    {
      super(str, null);
    }

    public boolean shouldRunAsync()
    {
      return true;
    }
  }

  public static abstract class MenuEntry
    implements PlayPopupMenu.OnActionSelectedListener
  {
    public final View customView;
    public final int menuId;
    public final String menuTitle;

    public MenuEntry(int paramInt, String paramString, View paramView)
    {
      this.menuId = paramInt;
      this.menuTitle = paramString;
      this.customView = paramView;
    }

    public abstract boolean shouldRunAsync();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.PlayCardMenuHandler
 * JD-Core Version:    0.6.2
 */