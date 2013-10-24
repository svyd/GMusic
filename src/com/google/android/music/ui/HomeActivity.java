package com.google.android.music.ui;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ListView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.tutorial.TutorialUtils;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends BaseActivity
{
  private static final Screen DEFAULT_SCREEN = Screen.MAINSTAGE;
  private boolean mActivityStarted = false;
  private ContentObserver mContentObserver;
  private boolean mContentObserverRegistered = false;
  private Screen mCurrentScreen;
  private NautilusStatus mNautilusStatus;
  private SyncStatusObserver mSyncObserver;
  private Object mSyncObserverHandle = null;
  private Screen mTargetScreen;

  public HomeActivity()
  {
    Handler localHandler = new Handler();
    ContentObserver local1 = new ContentObserver(localHandler)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        HomeActivity.this.updateHasAudioAndSyncState();
      }
    };
    this.mContentObserver = local1;
    SyncStatusObserver local2 = new SyncStatusObserver()
    {
      public void onStatusChanged(int paramAnonymousInt)
      {
        if (MusicUtils.isMainThread())
        {
          HomeActivity.this.updateHasAudioAndSyncState();
          return;
        }
        Runnable local1 = new Runnable()
        {
          public void run()
          {
            HomeActivity.this.updateHasAudioAndSyncState();
          }
        };
        HomeActivity localHomeActivity = HomeActivity.this;
        MusicUtils.runOnUiThread(local1, localHomeActivity);
      }
    };
    this.mSyncObserver = local2;
  }

  public static Intent createHomeScreenIntent(Context paramContext)
  {
    Intent localIntent1 = newHomeActivityIntent(paramContext);
    Intent localIntent2 = localIntent1.addFlags(67108864);
    return localIntent1;
  }

  public static Intent createHomeScreenIntent(Context paramContext, Screen paramScreen)
  {
    Intent localIntent = createHomeScreenIntent(paramContext);
    String str = paramScreen.getTag();
    return localIntent.putExtra("music:home:screen", str);
  }

  public static Intent newHomeActivityIntent(Context paramContext)
  {
    Intent localIntent1 = new Intent();
    Intent localIntent2 = localIntent1.setClassName(paramContext, "com.android.music.activitymanagement.TopLevelActivity");
    return localIntent1;
  }

  private void prepareScreenChange(Screen paramScreen)
  {
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    this.mCurrentScreen = paramScreen;
    if (getRootMenuList() != null)
    {
      ListView localListView = getRootMenuList();
      HomeMenu.selectScreen(paramScreen, localListView);
    }
    int i = paramScreen.mTitleResId;
    boolean bool = paramScreen.mOnDeviceSelector;
    setActionBarTitle(i, bool, true);
  }

  private void registerMusicEventListeners()
  {
    if (!this.mContentObserverRegistered)
    {
      ContentResolver localContentResolver = getContentResolver();
      Uri localUri = MusicContent.CONTENT_URI;
      ContentObserver localContentObserver = this.mContentObserver;
      localContentResolver.registerContentObserver(localUri, true, localContentObserver);
      this.mContentObserverRegistered = true;
    }
    if (this.mSyncObserverHandle == null)
    {
      SyncStatusObserver localSyncStatusObserver = this.mSyncObserver;
      Object localObject = ContentResolver.addStatusChangeListener(4, localSyncStatusObserver);
      this.mSyncObserverHandle = localObject;
    }
    updateHasAudioAndSyncState();
  }

  private void showSingleFragmentScreen(Screen paramScreen)
  {
    FragmentManager localFragmentManager = getSupportFragmentManager();
    String str1 = paramScreen.getTag();
    if (localFragmentManager.findFragmentByTag(str1) == null)
    {
      prepareScreenChange(paramScreen);
      Fragment localFragment = paramScreen.createFragment();
      String str2 = paramScreen.getTag();
      replaceContent(localFragment, false, str2);
      Screen localScreen = Screen.NO_CONTENT;
      if (paramScreen == localScreen)
      {
        setEmptyScreenShowing(true);
        enableSideDrawer(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      }
      while (true)
      {
        supportInvalidateOptionsMenu();
        return;
        setEmptyScreenShowing(false);
        enableSideDrawer(true);
        showDrawerIfRequested();
        autoHideDrawerIfRequested();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      }
    }
    prepareScreenChange(paramScreen);
  }

  private void updateHasAudioAndSyncState()
  {
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private boolean mHasAudio = false;
      private boolean mSyncPendingOrActive = false;

      public void backgroundTask()
      {
        Account localAccount = HomeActivity.this.getPreferences().getStreamingAccount();
        boolean bool1 = MusicContent.XAudio.hasAudio(HomeActivity.this.getApplicationContext());
        this.mHasAudio = bool1;
        if ((localAccount != null) && ((ContentResolver.isSyncActive(localAccount, "com.google.android.music.MusicContent")) || (ContentResolver.isSyncPending(localAccount, "com.google.android.music.MusicContent"))));
        for (boolean bool2 = true; ; bool2 = false)
        {
          this.mSyncPendingOrActive = bool2;
          return;
        }
      }

      public void taskCompleted()
      {
        if (HomeActivity.this.isFinishing())
          return;
        if (HomeActivity.this.isActivityDestroyed())
          return;
        boolean bool = HomeActivity.this.getPreferences().getShowSyncNotification();
        if (!this.mHasAudio)
        {
          if ((this.mSyncPendingOrActive) && (bool))
          {
            HomeActivity localHomeActivity1 = HomeActivity.this;
            HomeActivity.Screen localScreen1 = HomeActivity.Screen.SYNCING;
            localHomeActivity1.showSingleFragmentScreen(localScreen1);
            return;
          }
          NautilusStatus localNautilusStatus1 = HomeActivity.this.mNautilusStatus;
          NautilusStatus localNautilusStatus2 = NautilusStatus.GOT_NAUTILUS;
          if (localNautilusStatus1 != localNautilusStatus2)
          {
            HomeActivity localHomeActivity2 = HomeActivity.this;
            HomeActivity.Screen localScreen2 = HomeActivity.Screen.NO_CONTENT;
            localHomeActivity2.showSingleFragmentScreen(localScreen2);
            return;
          }
          HomeActivity localHomeActivity3 = HomeActivity.this;
          HomeActivity.Screen localScreen3 = HomeActivity.this.mTargetScreen;
          localHomeActivity3.showSingleFragmentScreen(localScreen3);
          return;
        }
        HomeActivity localHomeActivity4 = HomeActivity.this;
        HomeActivity.Screen localScreen4 = HomeActivity.this.mTargetScreen;
        localHomeActivity4.showSingleFragmentScreen(localScreen4);
      }
    });
  }

  private void updateUIStateIfNeeded(NautilusStatus paramNautilusStatus)
  {
    if (!this.mActivityStarted)
      return;
    if (this.mNautilusStatus == paramNautilusStatus)
      return;
    Screen localScreen1 = this.mCurrentScreen;
    Screen localScreen2 = Screen.EXPLORE;
    if (localScreen1 != localScreen2)
    {
      Screen localScreen3 = this.mCurrentScreen;
      Screen localScreen4 = Screen.INSTANT_MIXES;
      if (localScreen3 != localScreen4)
      {
        Screen localScreen5 = this.mCurrentScreen;
        Screen localScreen6 = Screen.RADIO;
        if (localScreen5 != localScreen6)
          break label78;
      }
    }
    Screen localScreen7 = Screen.MAINSTAGE;
    this.mTargetScreen = localScreen7;
    label78: this.mNautilusStatus = paramNautilusStatus;
    registerMusicEventListeners();
    NautilusStatus localNautilusStatus1 = this.mNautilusStatus;
    NautilusStatus localNautilusStatus2 = NautilusStatus.GOT_NAUTILUS;
    if (localNautilusStatus1 != localNautilusStatus2)
      return;
    Screen localScreen8 = this.mTargetScreen;
    showSingleFragmentScreen(localScreen8);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Intent localIntent = getIntent();
    if (localIntent != null)
    {
      String str1 = localIntent.getAction();
      if ("android.intent.action.MAIN".equals(str1))
      {
        MusicPreferences localMusicPreferences = getPreferences();
        if (TutorialUtils.launchTutorialOnStartupIfNeeded(this, localMusicPreferences))
          return;
      }
    }
    Bundle localBundle;
    if (paramBundle != null)
    {
      localBundle = paramBundle;
      if (localBundle != null)
        break label99;
    }
    label99: for (String str2 = null; ; str2 = localBundle.getString("music:home:screen"))
    {
      Screen localScreen1 = DEFAULT_SCREEN;
      Screen localScreen2 = Screen.fromTag(str2, localScreen1);
      this.mTargetScreen = localScreen2;
      return;
      if (localIntent != null)
      {
        localBundle = localIntent.getExtras();
        break;
      }
      localBundle = null;
      break;
    }
  }

  public void onNewIntent(Intent paramIntent)
  {
    super.onNewIntent(paramIntent);
    Bundle localBundle = paramIntent.getExtras();
    if (localBundle == null)
      return;
    String str1 = localBundle.getString("music:home:screen");
    Screen localScreen1 = DEFAULT_SCREEN;
    Screen localScreen2 = Screen.fromTag(str1, localScreen1);
    this.mTargetScreen = localScreen2;
    String str2 = null;
    Screen localScreen3 = this.mTargetScreen;
    Screen localScreen4 = Screen.EXPLORE;
    if (localScreen3 == localScreen4)
      str2 = "exploreScreen";
    while (true)
    {
      if (!TextUtils.isEmpty(str2))
      {
        MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(getApplicationContext());
        Object[] arrayOfObject = new Object[0];
        localMusicEventLogger.trackEvent(str2, arrayOfObject);
      }
      Screen localScreen5 = this.mTargetScreen;
      showSingleFragmentScreen(localScreen5);
      return;
      Screen localScreen6 = this.mTargetScreen;
      Screen localScreen7 = Screen.RADIO;
      if (localScreen6 == localScreen7)
        str2 = "radioScreen";
    }
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
    super.onNewNautilusStatus(paramNautilusStatus);
    updateUIStateIfNeeded(paramNautilusStatus);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    boolean bool;
    switch (paramMenuItem.getItemId())
    {
    default:
      bool = super.onOptionsItemSelected(paramMenuItem);
    case 16908332:
    }
    while (true)
    {
      return bool;
      if (!isSideDrawerOpen())
      {
        openSideDrawer();
        bool = true;
      }
      else
      {
        bool = super.onOptionsItemSelected(paramMenuItem);
      }
    }
  }

  protected void onPause()
  {
    super.onPause();
    if (this.mContentObserverRegistered)
    {
      ContentResolver localContentResolver = getContentResolver();
      ContentObserver localContentObserver = this.mContentObserver;
      localContentResolver.unregisterContentObserver(localContentObserver);
      this.mContentObserverRegistered = false;
    }
    if (this.mSyncObserverHandle == null)
      return;
    ContentResolver.removeStatusChangeListener(this.mSyncObserverHandle);
    this.mSyncObserverHandle = null;
  }

  protected void onResume()
  {
    super.onResume();
    registerMusicEventListeners();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if (this.mCurrentScreen == null)
      return;
    String str = this.mCurrentScreen.getTag();
    paramBundle.putString("music:home:screen", str);
  }

  protected void onStart()
  {
    super.onStart();
    this.mActivityStarted = true;
    NautilusStatus localNautilusStatus = UIStateManager.getInstance().getPrefs().getNautilusStatus();
    updateUIStateIfNeeded(localNautilusStatus);
  }

  public void onStop()
  {
    this.mActivityStarted = false;
    super.onStop();
  }

  protected boolean useActionBarHamburger()
  {
    return true;
  }

  public static enum Screen
  {
    private static final Map<String, Screen> TAG_TO_SCREEN = createTagToScreenMap();
    private final Class<? extends Fragment> mFragmentClass;
    private final boolean mOnDeviceSelector;
    private final String mTag;
    private final int mTitleResId;

    static
    {
      boolean bool1 = true;
      MY_LIBRARY = new Screen("MY_LIBRARY", 1, "library", 2131230841, bool1, MyLibraryFragment.class);
      int i = 2;
      boolean bool2 = true;
      PLAYLISTS = new Screen("PLAYLISTS", i, "playlists", 2131230842, bool2, PlaylistClustersFragment.class);
      int j = 3;
      boolean bool3 = false;
      INSTANT_MIXES = new Screen("INSTANT_MIXES", j, "mixes", 2131230844, bool3, InstantMixesFragment.class);
      int k = 4;
      boolean bool4 = false;
      RADIO = new Screen("RADIO", k, "radio", 2131230843, bool4, RadioFragment.class);
      boolean bool5 = false;
      EXPLORE = new Screen("EXPLORE", 5, "explore", 2131230845, bool5, ExploreFragment.class);
      boolean bool6 = false;
      SHOP = new Screen("SHOP", 6, "shop", 2131230846, bool6, null);
      boolean bool7 = false;
      NO_CONTENT = new Screen("NO_CONTENT", 7, "nocontent", 2131230928, bool7, EmptyLibraryFragment.class);
      boolean bool8 = false;
      SYNCING = new Screen("SYNCING", 8, "syncing", 2131230928, bool8, SyncingLibraryFragment.class);
      Screen[] arrayOfScreen = new Screen[9];
      Screen localScreen1 = MAINSTAGE;
      arrayOfScreen[0] = localScreen1;
      Screen localScreen2 = MY_LIBRARY;
      arrayOfScreen[1] = localScreen2;
      Screen localScreen3 = PLAYLISTS;
      arrayOfScreen[2] = localScreen3;
      Screen localScreen4 = INSTANT_MIXES;
      arrayOfScreen[3] = localScreen4;
      Screen localScreen5 = RADIO;
      arrayOfScreen[4] = localScreen5;
      Screen localScreen6 = EXPLORE;
      arrayOfScreen[5] = localScreen6;
      Screen localScreen7 = SHOP;
      arrayOfScreen[6] = localScreen7;
      Screen localScreen8 = NO_CONTENT;
      arrayOfScreen[7] = localScreen8;
      Screen localScreen9 = SYNCING;
      arrayOfScreen[8] = localScreen9;
    }

    private Screen(String paramString, int paramInt, boolean paramBoolean, Class<? extends Fragment> paramClass)
    {
      this.mTag = paramString;
      this.mTitleResId = paramInt;
      this.mOnDeviceSelector = paramBoolean;
      this.mFragmentClass = paramClass;
    }

    private Fragment createFragment()
    {
      try
      {
        localFragment = (Fragment)this.mFragmentClass.newInstance();
        return localFragment;
      }
      catch (InstantiationException localInstantiationException)
      {
        while (true)
        {
          Log.e("MusicHomeActivity", "showSingleFragmentScreen", localInstantiationException);
          Fragment localFragment = null;
        }
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        label14: break label14;
      }
    }

    private static Map<String, Screen> createTagToScreenMap()
    {
      HashMap localHashMap = Maps.newHashMap();
      Screen[] arrayOfScreen = values();
      int i = arrayOfScreen.length;
      int j = 0;
      while (j < i)
      {
        Screen localScreen = arrayOfScreen[j];
        String str = localScreen.mTag;
        Object localObject = localHashMap.put(str, localScreen);
        j += 1;
      }
      return Collections.unmodifiableMap(localHashMap);
    }

    public static Screen fromTag(String paramString, Screen paramScreen)
    {
      Screen localScreen = (Screen)TAG_TO_SCREEN.get(paramString);
      if (localScreen != null);
      while (true)
      {
        return localScreen;
        localScreen = paramScreen;
      }
    }

    public String getTag()
    {
      return this.mTag;
    }

    public String getTitle(Resources paramResources)
    {
      int i = this.mTitleResId;
      return paramResources.getString(i);
    }

    public boolean isExternalLink()
    {
      if (this.mFragmentClass == null);
      for (boolean bool = true; ; bool = false)
        return bool;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.HomeActivity
 * JD-Core Version:    0.6.2
 */