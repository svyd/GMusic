package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.preferences.MusicPreferences;

public class HomeMenu
{
  private static final HomeActivity.Screen[] FREE_ITEM_SCREENS;
  private static final HomeActivity.Screen[] PAID_ITEM_SCREENS;
  private static final HomeActivity.Screen[] SIDELOADED_ITEM_SCREENS = arrayOfScreen3;

  static
  {
    HomeActivity.Screen[] arrayOfScreen1 = new HomeActivity.Screen[5];
    HomeActivity.Screen localScreen1 = HomeActivity.Screen.MAINSTAGE;
    arrayOfScreen1[0] = localScreen1;
    HomeActivity.Screen localScreen2 = HomeActivity.Screen.MY_LIBRARY;
    arrayOfScreen1[1] = localScreen2;
    HomeActivity.Screen localScreen3 = HomeActivity.Screen.PLAYLISTS;
    arrayOfScreen1[2] = localScreen3;
    HomeActivity.Screen localScreen4 = HomeActivity.Screen.RADIO;
    arrayOfScreen1[3] = localScreen4;
    HomeActivity.Screen localScreen5 = HomeActivity.Screen.EXPLORE;
    arrayOfScreen1[4] = localScreen5;
    PAID_ITEM_SCREENS = arrayOfScreen1;
    HomeActivity.Screen[] arrayOfScreen2 = new HomeActivity.Screen[5];
    HomeActivity.Screen localScreen6 = HomeActivity.Screen.MAINSTAGE;
    arrayOfScreen2[0] = localScreen6;
    HomeActivity.Screen localScreen7 = HomeActivity.Screen.MY_LIBRARY;
    arrayOfScreen2[1] = localScreen7;
    HomeActivity.Screen localScreen8 = HomeActivity.Screen.PLAYLISTS;
    arrayOfScreen2[2] = localScreen8;
    HomeActivity.Screen localScreen9 = HomeActivity.Screen.INSTANT_MIXES;
    arrayOfScreen2[3] = localScreen9;
    HomeActivity.Screen localScreen10 = HomeActivity.Screen.SHOP;
    arrayOfScreen2[4] = localScreen10;
    FREE_ITEM_SCREENS = arrayOfScreen2;
    HomeActivity.Screen[] arrayOfScreen3 = new HomeActivity.Screen[3];
    HomeActivity.Screen localScreen11 = HomeActivity.Screen.MAINSTAGE;
    arrayOfScreen3[0] = localScreen11;
    HomeActivity.Screen localScreen12 = HomeActivity.Screen.MY_LIBRARY;
    arrayOfScreen3[1] = localScreen12;
    HomeActivity.Screen localScreen13 = HomeActivity.Screen.PLAYLISTS;
    arrayOfScreen3[2] = localScreen13;
  }

  public static HomeMenuAdapter configureListView(ListView paramListView, BaseActivity paramBaseActivity)
  {
    HomeMenuAdapter localHomeMenuAdapter1 = (HomeMenuAdapter)paramListView.getAdapter();
    if (localHomeMenuAdapter1 != null);
    for (int i = localHomeMenuAdapter1.getSelectedIndex(); ; i = -2147483648)
    {
      HomeMenuAdapter localHomeMenuAdapter2 = createListAdapter(paramBaseActivity);
      int j = localHomeMenuAdapter2.getCount();
      if (i < j)
        boolean bool = localHomeMenuAdapter2.onItemSelected(i);
      paramListView.setAdapter(localHomeMenuAdapter2);
      AdapterView.OnItemClickListener localOnItemClickListener = createClickListener(paramBaseActivity, localHomeMenuAdapter2);
      paramListView.setOnItemClickListener(localOnItemClickListener);
      return localHomeMenuAdapter2;
    }
  }

  private static AdapterView.OnItemClickListener createClickListener(final BaseActivity paramBaseActivity, HomeMenuAdapter paramHomeMenuAdapter)
  {
    return new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        HomeActivity.Screen localScreen = HomeMenu.access$000()[paramAnonymousInt];
        if ((localScreen.isExternalLink()) || (HomeMenu.this.onItemSelected(paramAnonymousInt)))
        {
          AppNavigation.showHomeScreen(paramBaseActivity, localScreen);
          return;
        }
        paramBaseActivity.closeSideDrawer();
      }
    };
  }

  private static HomeMenuAdapter createListAdapter(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    HomeActivity.Screen[] arrayOfScreen = getMenuScreens();
    String[] arrayOfString = new String[arrayOfScreen.length];
    int i = 0;
    while (true)
    {
      int j = arrayOfScreen.length;
      if (i >= j)
        break;
      String str = arrayOfScreen[i].getTitle(localResources);
      arrayOfString[i] = str;
      i += 1;
    }
    return new HomeMenuAdapter(paramContext, 2130968679, arrayOfString);
  }

  private static HomeActivity.Screen[] getMenuScreens()
  {
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    HomeActivity.Screen[] arrayOfScreen;
    if (localMusicPreferences.isNautilusEnabled())
      arrayOfScreen = PAID_ITEM_SCREENS;
    while (true)
    {
      return arrayOfScreen;
      if (localMusicPreferences.hasStreamingAccount())
        arrayOfScreen = FREE_ITEM_SCREENS;
      else
        arrayOfScreen = SIDELOADED_ITEM_SCREENS;
    }
  }

  private static int getScreenIndex(HomeActivity.Screen paramScreen)
  {
    HomeActivity.Screen[] arrayOfScreen = getMenuScreens();
    int i = arrayOfScreen.length;
    int j = 0;
    if (j < i)
      if (!arrayOfScreen[j].equals(paramScreen));
    while (true)
    {
      return j;
      j += 1;
      break;
      j = 0;
    }
  }

  public static void selectScreen(HomeActivity.Screen paramScreen, ListView paramListView)
  {
    int i = getScreenIndex(paramScreen);
    boolean bool = ((HomeMenuAdapter)paramListView.getAdapter()).onItemSelected(i);
  }

  private static class HomeMenuAdapter extends ArrayAdapter<String>
  {
    private final int mNormalColor;
    private final int mSelectedColor;
    private int mSelectedIndex = -2147483648;

    public HomeMenuAdapter(Context paramContext, int paramInt, String[] paramArrayOfString)
    {
      super(paramInt, paramArrayOfString);
      Resources localResources = paramContext.getResources();
      int i = localResources.getColor(2131427362);
      this.mSelectedColor = i;
      int j = localResources.getColor(2131427338);
      this.mNormalColor = j;
    }

    public int getSelectedIndex()
    {
      return this.mSelectedIndex;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      TextView localTextView = (TextView)super.getView(paramInt, paramView, paramViewGroup);
      int i = this.mSelectedIndex;
      int j;
      if (paramInt != i)
      {
        j = 1;
        if (j == 0)
          break label54;
      }
      label54: for (int k = this.mSelectedColor; ; k = this.mNormalColor)
      {
        localTextView.setBackgroundColor(k);
        return localTextView;
        j = 0;
        break;
      }
    }

    public boolean onItemSelected(int paramInt)
    {
      if (this.mSelectedIndex != paramInt)
      {
        this.mSelectedIndex = paramInt;
        notifyDataSetChanged();
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.HomeMenu
 * JD-Core Version:    0.6.2
 */