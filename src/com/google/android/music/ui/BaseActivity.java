package com.google.android.music.ui;

import android.accounts.Account;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.LayoutParams;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.music.DeleteConfirmationDialog;
import com.google.android.music.DeleteConfirmationDialog.DeletionType;
import com.google.android.music.NautilusStatus;
import com.google.android.music.activities.MusicSettingsActivity;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.ContentIdentifier.Domain;
import com.google.android.music.playback.ErrorInfo;
import com.google.android.music.playback.ErrorInfo.OnErrorAlertDismissed;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.BugReporter;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.widgets.ExpandingScrollView;
import com.google.android.music.widgets.ExpandingScrollView.ExpandingScrollViewListener;
import com.google.android.music.widgets.ExpandingScrollView.ExpandingState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

public class BaseActivity extends ActionBarActivity
  implements ActionBarController
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private static LinkedList<Intent> sUpList = new LinkedList();
  private int mActionBarAlpha = 255;
  private Drawable mActionBarBGDrawable;
  private View mActionBarCustomView;
  private ActionBarDrawerToggle mActionBarDrawerToggle;
  private ActionBarMenuAdapter mActionBarMenuAdapter;
  private final ActionBar.OnNavigationListener mActionBarNavListener;
  private String mActionBarTitle = null;
  private int mActionBarTitleResId = -1;
  private Fragment mContentFragment = null;
  private ArrayList<ViewGroup> mContentViews;
  private ContentIdentifier mCurrentFailureDialogsMusicId = null;
  private View mCustomMainView = null;
  private boolean mDrawerAutoHide = true;
  private boolean mDrawerRestoredStateOpen;
  private boolean mEmptyScreenShowing = false;
  private boolean mIsDestroyed = false;
  private final BroadcastReceiver mMediaPlayerBroadcastReceiver;
  private MediaRouteManager mMediaRouteManager;
  private NowPlayingScreenFragment mNowPlayingFragment = null;
  private boolean mOnDeviceSelector;
  private Runnable mRequestSetAlphaRunnable;
  private ListView mRootMenuList;
  private boolean mShowHome;
  private boolean mShowingFailureAlert = false;
  private View mSideDrawer;
  private DrawerLayout mSideDrawerContainer;
  private boolean mSideDrawerEnabled = true;
  private ViewGroup mTVSideDrawerContainer;
  private final UIStateManager.UIStateChangeListener mUIStateChangeListener;
  private Intent mUpIntent;

  public BaseActivity()
  {
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        BaseActivity.this.mActionBarCustomView.requestLayout();
      }
    };
    this.mRequestSetAlphaRunnable = local1;
    ActionBar.OnNavigationListener local2 = new ActionBar.OnNavigationListener()
    {
      public boolean onNavigationItemSelected(int paramAnonymousInt, long paramAnonymousLong)
      {
        if (paramAnonymousInt == 0)
          BaseActivity.this.setOfflineOnly(false);
        while (true)
        {
          return true;
          BaseActivity.this.setOfflineOnly(true);
        }
      }
    };
    this.mActionBarNavListener = local2;
    UIStateManager.UIStateChangeListener local3 = new UIStateManager.UIStateChangeListener()
    {
      public void onAccountStatusUpdate(Account paramAnonymousAccount, NautilusStatus paramAnonymousNautilusStatus)
      {
        BaseActivity.this.onNewNautilusStatus(paramAnonymousNautilusStatus);
      }
    };
    this.mUIStateChangeListener = local3;
    BroadcastReceiver local10 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        String str1 = paramAnonymousIntent.getAction();
        if ("com.android.music.playbackfailed".equals(str1))
        {
          int i = paramAnonymousIntent.getIntExtra("errorType", -1);
          Long localLong = Long.valueOf(paramAnonymousIntent.getLongExtra("id", 65535L));
          ContentIdentifier localContentIdentifier = null;
          if (localLong.longValue() != 65535L)
          {
            long l = localLong.longValue();
            ContentIdentifier.Domain[] arrayOfDomain = ContentIdentifier.Domain.values();
            int j = paramAnonymousIntent.getIntExtra("domain", -1);
            ContentIdentifier.Domain localDomain = arrayOfDomain[j];
            localContentIdentifier = new ContentIdentifier(l, localDomain);
          }
          if (i == -1)
          {
            String str2 = "Expected extra: errorType for action: " + str1 + " in mMediaPlayerBroadcastReceiver";
            int k = Log.e("MusicBaseActivity", str2);
            return;
          }
          BaseActivity.this.alertFailureIfNecessary(localContentIdentifier, i);
          return;
        }
        String str3 = "Unknown action coming to mMediaPlayerBroadcastReceiver: " + str1;
        int m = Log.e("MusicBaseActivity", str3);
      }
    };
    this.mMediaPlayerBroadcastReceiver = local10;
  }

  private void initializeDrawerState(Bundle paramBundle)
  {
    if (paramBundle != null)
    {
      boolean bool1 = getDrawerStateOpenDefault();
      boolean bool2 = paramBundle.getBoolean("music:base:drawer_state_open", bool1);
      this.mDrawerRestoredStateOpen = bool2;
      boolean bool3 = paramBundle.getBoolean("music:base:drawer_auto_hide", true);
      this.mDrawerAutoHide = bool3;
    }
    if (!Log.isLoggable("MusicBaseActivity", 3))
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("initializeDrawerState, open: ");
    boolean bool4 = this.mDrawerRestoredStateOpen;
    String str = bool4;
    int i = Log.d("MusicBaseActivity", str);
  }

  private void loadTVSideDrawer(int paramInt)
  {
    ViewGroup localViewGroup1 = (ViewGroup)findViewById(2131296357);
    this.mTVSideDrawerContainer = localViewGroup1;
    LayoutInflater localLayoutInflater = getLayoutInflater();
    ViewGroup localViewGroup2 = this.mTVSideDrawerContainer;
    View localView1 = localLayoutInflater.inflate(paramInt, localViewGroup2, false);
    this.mSideDrawer = localView1;
    ViewGroup localViewGroup3 = this.mTVSideDrawerContainer;
    View localView2 = this.mSideDrawer;
    localViewGroup3.addView(localView2);
    ViewGroup.LayoutParams localLayoutParams = this.mSideDrawer.getLayoutParams();
    int i = calculateSideDrawerWidth();
    localLayoutParams.width = i;
  }

  public static void putExtraDrawerState(Intent paramIntent, boolean paramBoolean1, boolean paramBoolean2)
  {
    Intent localIntent1 = paramIntent.putExtra("music:base:drawer_state_open", paramBoolean1);
    Intent localIntent2 = paramIntent.putExtra("music:base:drawer_auto_hide", paramBoolean2);
  }

  private void restoreActionBarTitle()
  {
    if (this.mActionBarTitleResId != -1)
    {
      int i = this.mActionBarTitleResId;
      boolean bool1 = this.mOnDeviceSelector;
      boolean bool2 = this.mShowHome;
      setActionBarTitleInternal(i, bool1, bool2);
      return;
    }
    if (this.mActionBarTitle != null)
    {
      String str = this.mActionBarTitle;
      setActionBarTitleInternal(str);
      getSupportActionBar().setDisplayShowHomeEnabled(false);
      return;
    }
    setActionBarTitleInternal("");
  }

  private void setActionBarAlphaInternal(int paramInt)
  {
    if (this.mActionBarBGDrawable == null)
    {
      ActionBar localActionBar = getSupportActionBar();
      Drawable localDrawable1 = getResources().getDrawable(2130837593);
      this.mActionBarBGDrawable = localDrawable1;
      View localView1 = new View(this);
      this.mActionBarCustomView = localView1;
      View localView2 = this.mActionBarCustomView;
      ActionBar.LayoutParams localLayoutParams = new ActionBar.LayoutParams(0, 0);
      localActionBar.setCustomView(localView2, localLayoutParams);
      localActionBar.setDisplayShowCustomEnabled(true);
      Drawable localDrawable2 = this.mActionBarBGDrawable;
      localActionBar.setBackgroundDrawable(localDrawable2);
      this.mActionBarCustomView.requestLayout();
    }
    this.mActionBarBGDrawable.setAlpha(paramInt);
    MusicUtils.runOnUiThread(this.mRequestSetAlphaRunnable, this);
  }

  private void setActionBarTitleInternal(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    ActionBar localActionBar = getSupportActionBar();
    if ((paramBoolean1) && (getPreferences().isOnDeviceMusicCapable()))
    {
      localActionBar.setDisplayOptions(6, 6);
      ActionBarMenuAdapter localActionBarMenuAdapter1 = this.mActionBarMenuAdapter;
      String str = getResources().getString(paramInt);
      localActionBarMenuAdapter1.setTitle(str);
      localActionBar.setTitle("");
      localActionBar.setNavigationMode(1);
      ActionBarMenuAdapter localActionBarMenuAdapter2 = this.mActionBarMenuAdapter;
      ActionBar.OnNavigationListener localOnNavigationListener = this.mActionBarNavListener;
      localActionBar.setListNavigationCallbacks(localActionBarMenuAdapter2, localOnNavigationListener);
      int j;
      if (UIStateManager.getInstance(this).getPrefs().getDisplayOptions() == 0)
      {
        j = 1;
        if (j == 0)
          break label125;
      }
      while (true)
      {
        localActionBar.setSelectedNavigationItem(i);
        return;
        j = 0;
        break;
        label125: i = 1;
      }
    }
    if (paramBoolean2);
    for (int k = 6; ; k = 4)
    {
      localActionBar.setDisplayOptions(k, 6);
      localActionBar.setNavigationMode(0);
      localActionBar.setTitle(paramInt);
      return;
    }
  }

  private void setActionBarTitleInternal(String paramString)
  {
    getSupportActionBar().setTitle(paramString);
  }

  private void setOfflineOnly(boolean paramBoolean)
  {
    int i = 1;
    MusicPreferences localMusicPreferences = UIStateManager.getInstance(this).getPrefs();
    boolean bool;
    if (localMusicPreferences.getDisplayOptions() != 0)
    {
      bool = true;
      if (bool != paramBoolean)
        return;
      if (!paramBoolean)
        break label43;
    }
    while (true)
    {
      localMusicPreferences.setDisplayOptions(i);
      return;
      bool = false;
      break;
      label43: i = 0;
    }
  }

  private void syncActionBarIconState()
  {
    if (this.mActionBarDrawerToggle == null)
      return;
    this.mActionBarDrawerToggle.syncState();
  }

  private void updateDrawerLockMode()
  {
    if (this.mSideDrawerContainer == null)
      return;
    if (this.mSideDrawer == null)
      return;
    if (this.mSideDrawerEnabled);
    for (int i = 0; ; i = 1)
    {
      DrawerLayout localDrawerLayout = this.mSideDrawerContainer;
      View localView = this.mSideDrawer;
      localDrawerLayout.setDrawerLockMode(i, localView);
      return;
    }
  }

  public void alertFailureIfNecessary(ContentIdentifier paramContentIdentifier, int paramInt)
  {
    if (this.mShowingFailureAlert)
      return;
    if (isFinishing())
      return;
    if (paramContentIdentifier != null)
    {
      ContentIdentifier localContentIdentifier = this.mCurrentFailureDialogsMusicId;
      if (paramContentIdentifier.equals(localContentIdentifier))
        return;
    }
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("musicId", paramContentIdentifier);
    localBundle.putInt("errorType", paramInt);
    boolean bool = showDialog(108, localBundle);
  }

  protected void autoHideDrawerIfRequested()
  {
    if (!this.mDrawerRestoredStateOpen)
      return;
    if (!this.mDrawerAutoHide)
      return;
    if (this.mSideDrawerContainer == null)
      return;
    if (Log.isLoggable("MusicBaseActivity", 3))
      int i = Log.d("MusicBaseActivity", "Scheduling side drawer to be closed");
    this.mDrawerAutoHide = false;
    DrawerLayout localDrawerLayout = this.mSideDrawerContainer;
    Runnable local5 = new Runnable()
    {
      public void run()
      {
        BaseActivity.this.closeSideDrawer();
        BaseActivity.this.restoreActionBarTitle();
      }
    };
    boolean bool = localDrawerLayout.postDelayed(local5, 100L);
  }

  protected int calculateSideDrawerWidth()
  {
    Resources localResources = getResources();
    int i = UIStateManager.getInstance().getScreenWidth();
    int j = localResources.getDimensionPixelSize(2131558473);
    int k = localResources.getConfiguration().orientation;
    int m;
    if (getPreferences().isTvMusic())
      m = Math.min(i / 5, j);
    while (true)
    {
      return m;
      if (k == 1)
        m = Math.min(i * 4 / 5, j);
      else
        m = Math.min(i / 2, j);
    }
  }

  protected void clearMainView()
  {
    if (this.mCustomMainView == null)
      return;
    ViewGroup localViewGroup = (ViewGroup)findViewById(2131296358);
    View localView = this.mCustomMainView;
    localViewGroup.removeView(localView);
    this.mCustomMainView = null;
  }

  protected void closeSideDrawer()
  {
    if (this.mSideDrawerContainer == null)
      return;
    if (!this.mSideDrawerEnabled)
      return;
    DrawerLayout localDrawerLayout = this.mSideDrawerContainer;
    View localView = this.mSideDrawer;
    localDrawerLayout.closeDrawer(localView);
  }

  public void enableSideDrawer(boolean paramBoolean)
  {
    if (this.mSideDrawerEnabled != paramBoolean)
      return;
    this.mSideDrawerEnabled = paramBoolean;
    updateDrawerLockMode();
  }

  public ExpandingScrollView getBottomDrawer()
  {
    return (ExpandingScrollView)findViewById(2131296359);
  }

  protected Fragment getContent()
  {
    return getSupportFragmentManager().findFragmentById(2131296358);
  }

  protected boolean getDrawerAutoHideDefault()
  {
    return getPreferences().wasSidePannelClosedOnce();
  }

  protected boolean getDrawerStateOpenDefault()
  {
    return false;
  }

  protected int getHelpLinkResId()
  {
    if (getPreferences().getDisplayOptions() == 1);
    for (int i = 2131230833; ; i = 2131230832)
      return i;
  }

  protected ViewGroup getMainContainer()
  {
    return (ViewGroup)findViewById(2131296358);
  }

  protected MusicPreferences getPreferences()
  {
    return getUIStateManager().getPrefs();
  }

  protected final ListView getRootMenuList()
  {
    return this.mRootMenuList;
  }

  protected UIStateManager getUIStateManager()
  {
    return UIStateManager.getInstance(this);
  }

  public void hideActionBar()
  {
    getSupportActionBar().hide();
  }

  protected boolean isActivityDestroyed()
  {
    return this.mIsDestroyed;
  }

  public boolean isEmptyScreenShowing()
  {
    return this.mEmptyScreenShowing;
  }

  protected boolean isSideDrawerOpen()
  {
    if ((this.mSideDrawerContainer != null) && (this.mSideDrawerEnabled))
    {
      DrawerLayout localDrawerLayout = this.mSideDrawerContainer;
      View localView = this.mSideDrawer;
      if (!localDrawerLayout.isDrawerOpen(localView));
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected void loadSideDrawer(int paramInt)
  {
    if (this.mSideDrawerContainer == null)
      return;
    LayoutInflater localLayoutInflater = getLayoutInflater();
    DrawerLayout localDrawerLayout1 = this.mSideDrawerContainer;
    View localView1 = localLayoutInflater.inflate(paramInt, localDrawerLayout1, false);
    this.mSideDrawer = localView1;
    DrawerLayout localDrawerLayout2 = this.mSideDrawerContainer;
    View localView2 = this.mSideDrawer;
    localDrawerLayout2.addView(localView2);
    DrawerLayout.LayoutParams localLayoutParams = (DrawerLayout.LayoutParams)this.mSideDrawer.getLayoutParams();
    localLayoutParams.gravity = 3;
    int i = calculateSideDrawerWidth();
    localLayoutParams.width = i;
    if (!getPreferences().isTvMusic())
    {
      DrawerLayout localDrawerLayout3 = this.mSideDrawerContainer;
      BaseActivity localBaseActivity = this;
      int j = 0;
      ActionBarDrawerToggle localActionBarDrawerToggle1 = new ActionBarDrawerToggle(localBaseActivity, localDrawerLayout3, 2130837706, 0, j);
      this.mActionBarDrawerToggle = localActionBarDrawerToggle1;
      ActionBarDrawerToggle localActionBarDrawerToggle2 = this.mActionBarDrawerToggle;
      boolean bool = useActionBarHamburger();
      localActionBarDrawerToggle2.setDrawerIndicatorEnabled(bool);
      syncActionBarIconState();
    }
    updateDrawerLockMode();
  }

  public void onBackPressed()
  {
    if (isSideDrawerOpen())
    {
      closeSideDrawer();
      return;
    }
    ExpandingScrollView.ExpandingState localExpandingState1 = getBottomDrawer().getExpandingState();
    ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
    if (localExpandingState1 == localExpandingState2)
    {
      boolean bool = getBottomDrawer().collapse();
      return;
    }
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    boolean bool1 = supportRequestWindowFeature(9);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    UIStateManager localUIStateManager1 = UIStateManager.getInstance(this);
    getWindow().setType(1001);
    setVolumeControlStream(3);
    boolean bool2 = getPreferences().isTvMusic();
    label227: boolean bool5;
    if (bool2)
    {
      setContentView(2130968607);
      ArrayList localArrayList1 = new ArrayList(2);
      this.mContentViews = localArrayList1;
      ArrayList localArrayList2 = this.mContentViews;
      ViewGroup localViewGroup1 = getMainContainer();
      boolean bool3 = localArrayList2.add(localViewGroup1);
      setupNowPlayingBar();
      UIStateManager localUIStateManager2 = UIStateManager.getInstance();
      UIStateManager.UIStateChangeListener localUIStateChangeListener = this.mUIStateChangeListener;
      localUIStateManager2.registerUIStateChangeListener(localUIStateChangeListener);
      getSupportActionBar().setDisplayOptions(4, 6);
      Resources localResources = getResources();
      String[] arrayOfString = new String[2];
      String str1 = localResources.getString(2131231261);
      arrayOfString[0] = str1;
      String str2 = localResources.getString(2131231260);
      arrayOfString[1] = str2;
      ActionBarMenuAdapter localActionBarMenuAdapter = new ActionBarMenuAdapter(this, 2130968660, arrayOfString);
      this.mActionBarMenuAdapter = localActionBarMenuAdapter;
      if (!bool2)
        break label402;
      loadTVSideDrawer(2130968680);
      ArrayList localArrayList3 = this.mContentViews;
      ViewGroup localViewGroup2 = this.mTVSideDrawerContainer;
      boolean bool4 = localArrayList3.add(localViewGroup2);
      ListView localListView = (ListView)findViewById(2131296534);
      setupRootMenu(localListView);
      if ((!getDrawerStateOpenDefault()) && (getPreferences().wasSidePannelClosedOnce()))
        break label463;
      bool5 = true;
      label265: this.mDrawerRestoredStateOpen = bool5;
      boolean bool6 = getDrawerAutoHideDefault();
      this.mDrawerAutoHide = bool6;
      if (paramBundle == null)
        break label469;
    }
    while (true)
    {
      initializeDrawerState(paramBundle);
      if (usesActionBarOverlay())
      {
        View localView = findViewById(2131296358);
        int i = localView.getPaddingLeft();
        int j = localView.getPaddingRight();
        int k = localView.getPaddingBottom();
        localView.setPadding(i, 0, j, k);
      }
      if ((getPreferences().isMediaRouteSupportEnabled()) && (getPreferences().hasStreamingAccount()))
      {
        MediaRouteManager localMediaRouteManager = new MediaRouteManager(this);
        this.mMediaRouteManager = localMediaRouteManager;
        this.mMediaRouteManager.onCreate();
      }
      setActionBarAlpha(255);
      return;
      setContentView(2130968606);
      break;
      label402: DrawerLayout localDrawerLayout1 = (DrawerLayout)findViewById(2131296357);
      this.mSideDrawerContainer = localDrawerLayout1;
      this.mSideDrawerContainer.setDrawerShadow(2130837679, 3);
      DrawerLayout localDrawerLayout2 = this.mSideDrawerContainer;
      BaseActivity.6 local6 = new BaseActivity.6(this);
      localDrawerLayout2.setDrawerListener(local6);
      loadSideDrawer(2130968680);
      break label227;
      label463: bool5 = false;
      break label265;
      label469: paramBundle = getIntent().getExtras();
    }
  }

  public Dialog onCreateDialog(final int paramInt, Bundle paramBundle)
  {
    Object localObject;
    if (paramInt == 103)
    {
      AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
      AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(2131231047);
      AlertDialog.Builder localBuilder3 = localBuilder1.setMessage(2131231048);
      AlertDialog.Builder localBuilder4 = localBuilder1.setCancelable(true);
      AlertDialog.Builder localBuilder5 = localBuilder1.setPositiveButton(2131230741, null);
      localObject = localBuilder1.create();
    }
    while (true)
    {
      if (localObject != null)
      {
        DialogInterface.OnDismissListener local9 = new DialogInterface.OnDismissListener()
        {
          public void onDismiss(DialogInterface paramAnonymousDialogInterface)
          {
            BaseActivity localBaseActivity = BaseActivity.this;
            int i = paramInt;
            localBaseActivity.removeDialog(i);
          }
        };
        ((AlertDialog)localObject).setOnDismissListener(local9);
      }
      return localObject;
      if (paramInt == 107)
      {
        AlertDialog.Builder localBuilder6 = new AlertDialog.Builder(this);
        AlertDialog.Builder localBuilder7 = localBuilder6.setMessage(2131230745);
        AlertDialog.Builder localBuilder8 = localBuilder6.setNegativeButton(2131231239, null);
        localObject = localBuilder6.create();
      }
      else if (paramInt == 108)
      {
        int i = paramBundle.getInt("errorType");
        ContentIdentifier localContentIdentifier = (ContentIdentifier)paramBundle.getParcelable("musicId");
        MusicPreferences localMusicPreferences = getPreferences();
        ErrorInfo localErrorInfo = ErrorInfo.createErrorInfo(i, localMusicPreferences, null);
        if (localErrorInfo.canShowAlert())
        {
          this.mCurrentFailureDialogsMusicId = localContentIdentifier;
          this.mShowingFailureAlert = true;
          ErrorInfo.OnErrorAlertDismissed local8 = new ErrorInfo.OnErrorAlertDismissed()
          {
            public void onErrorAlertDismissed()
            {
              boolean bool = BaseActivity.access$1002(BaseActivity.this, false);
              ContentIdentifier localContentIdentifier = BaseActivity.access$1102(BaseActivity.this, null);
            }
          };
          localObject = localErrorInfo.createAlert(this, local8);
          if (localObject != null)
            continue;
          NullPointerException localNullPointerException = new NullPointerException();
          int j = Log.wtf("MusicBaseActivity", "Unexpected null alert", localNullPointerException);
        }
      }
      else if (paramInt == 109)
      {
        long l = paramBundle.getLong("deleteId");
        String str1 = paramBundle.getString("deleteName");
        String str2 = paramBundle.getString("deleteArtistName");
        boolean bool = paramBundle.getBoolean("deleteHasRemote");
        DeleteConfirmationDialog.DeletionType[] arrayOfDeletionType = DeleteConfirmationDialog.DeletionType.values();
        int k = paramBundle.getInt("deleteType");
        DeleteConfirmationDialog.DeletionType localDeletionType = arrayOfDeletionType[k];
        BaseActivity localBaseActivity = this;
        localObject = new DeleteConfirmationDialog(localBaseActivity, localDeletionType, l, str1, str2, bool);
      }
      else
      {
        localObject = null;
        continue;
        localObject = null;
      }
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131820546, paramMenu);
    if (this.mMediaRouteManager != null)
      this.mMediaRouteManager.onCreateOptionsMenu(paramMenu);
    return true;
  }

  protected void onDestroy()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.UIStateChangeListener localUIStateChangeListener = this.mUIStateChangeListener;
    localUIStateManager.unregisterUIStateChangeListener(localUIStateChangeListener);
    this.mSideDrawerContainer = null;
    this.mIsDestroyed = true;
    removeUpIntent();
    if (this.mMediaRouteManager != null)
      this.mMediaRouteManager.onDestroy();
    super.onDestroy();
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((this.mMediaRouteManager != null) && (this.mMediaRouteManager.onKeyDown(paramInt, paramKeyEvent)));
    for (boolean bool = true; ; bool = super.onKeyDown(paramInt, paramKeyEvent))
      return bool;
  }

  public void onNewIntent(Intent paramIntent)
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    super.onNewIntent(paramIntent);
    setIntent(paramIntent);
    Bundle localBundle = paramIntent.getExtras();
    initializeDrawerState(localBundle);
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
    String str1;
    StringBuilder localStringBuilder;
    if (Log.isLoggable("MusicBaseActivity", 4))
    {
      str1 = "MusicBaseActivity";
      localStringBuilder = new StringBuilder().append("onNewNautilusStatus status: ").append(paramNautilusStatus).append(" has menu list: ");
      if (this.mRootMenuList == null)
        break label89;
    }
    label89: for (boolean bool = true; ; bool = false)
    {
      String str2 = bool;
      int i = Log.i(str1, str2);
      if (this.mRootMenuList != null)
      {
        ListView localListView = this.mRootMenuList;
        setupRootMenu(localListView);
      }
      supportInvalidateOptionsMenu();
      return;
    }
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    boolean bool;
    switch (paramMenuItem.getItemId())
    {
    default:
      bool = super.onOptionsItemSelected(paramMenuItem);
    case 16908332:
    case 2131296583:
    case 2131296585:
    case 2131296586:
    case 2131296584:
    case 2131296587:
    }
    while (true)
    {
      return bool;
      int i;
      label83: Intent localIntent1;
      if (!sUpList.isEmpty())
      {
        i = 1;
        if (i == 0)
          break label138;
        localIntent1 = (Intent)sUpList.getLast();
        label98: if ((!isTaskRoot()) || ((this instanceof HomeActivity)))
          break label144;
        Intent localIntent2 = HomeActivity.createHomeScreenIntent(this);
        startActivity(localIntent2);
        finish();
      }
      while (true)
      {
        bool = true;
        break;
        i = 0;
        break label83;
        label138: localIntent1 = null;
        break label98;
        label144: if (localIntent1 != null)
        {
          Intent localIntent3 = this.mUpIntent;
          if (localIntent1 != localIntent3)
            startActivity(localIntent1);
        }
        else
        {
          onBackPressed();
        }
      }
      SearchActivity.showSearch(this);
      bool = true;
      continue;
      Intent localIntent4 = new Intent(this, MusicSettingsActivity.class);
      startActivity(localIntent4);
      bool = true;
      continue;
      Resources localResources = getResources();
      StringBuilder localStringBuilder1 = new StringBuilder();
      int j = getHelpLinkResId();
      String str1 = localResources.getString(j);
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("&hl=");
      String str2 = localResources.getConfiguration().locale.getLanguage();
      String str3 = str2;
      AppNavigation.openHelpLink(this, str3);
      bool = true;
      continue;
      MusicUtils.requestSync(getPreferences(), true);
      Toast.makeText(this, 2131230756, 1).show();
      bool = true;
      continue;
      BugReporter.launchGoogleFeedback(this);
      bool = true;
    }
  }

  protected void onPause()
  {
    super.onPause();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    UIStateManager.getInstance().onPause();
    BroadcastReceiver localBroadcastReceiver = this.mMediaPlayerBroadcastReceiver;
    unregisterReceiver(localBroadcastReceiver);
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onPause();
  }

  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    MusicPreferences localMusicPreferences = UIStateManager.getInstance(this).getPrefs();
    if (this.mEmptyScreenShowing)
      setMenuVisibility(paramMenu, 2131296583, false, false);
    if (localMusicPreferences.getSyncAccount() == null)
      setMenuVisibility(paramMenu, 2131296584, false, false);
    if (!BugReporter.isGoogleFeedbackInstalled(this))
      setMenuVisibility(paramMenu, 2131296587, false, false);
    return super.onPrepareOptionsMenu(paramMenu);
  }

  protected void onRestart()
  {
    super.onRestart();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onRestart();
  }

  protected void onResume()
  {
    super.onResume();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    UIStateManager.getInstance().onResume();
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("com.android.music.playbackfailed");
    BroadcastReceiver localBroadcastReceiver = this.mMediaPlayerBroadcastReceiver;
    Intent localIntent = registerReceiver(localBroadcastReceiver, localIntentFilter);
    syncActionBarIconState();
    if (isSideDrawerOpen())
      setActionBarTitleInternal(2131230928, false, true);
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onResume();
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    boolean bool1 = isSideDrawerOpen();
    this.mDrawerRestoredStateOpen = bool1;
    boolean bool2 = this.mDrawerRestoredStateOpen;
    paramBundle.putBoolean("music:base:drawer_state_open", bool2);
    boolean bool3 = this.mDrawerAutoHide;
    paramBundle.putBoolean("music:base:drawer_auto_hide", bool3);
  }

  protected void onStart()
  {
    super.onStart();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    if (this.mMediaRouteManager != null)
    {
      this.mMediaRouteManager.onStart();
      return;
    }
    if (!getPreferences().isMediaRouteSupportEnabled())
      return;
    if (!getPreferences().hasStreamingAccount())
      return;
    MediaRouteManager localMediaRouteManager = new MediaRouteManager(this);
    this.mMediaRouteManager = localMediaRouteManager;
    this.mMediaRouteManager.onCreate();
    this.mMediaRouteManager.onStart();
  }

  public void onStop()
  {
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    super.onStop();
    if (this.mMediaRouteManager == null)
      return;
    this.mMediaRouteManager.onStop();
  }

  protected void openSideDrawer()
  {
    if (this.mSideDrawerContainer == null)
      return;
    if (!this.mSideDrawerEnabled)
      return;
    DrawerLayout localDrawerLayout = this.mSideDrawerContainer;
    View localView = this.mSideDrawer;
    localDrawerLayout.openDrawer(localView);
  }

  protected void removeUpIntent()
  {
    if (this.mUpIntent == null)
      return;
    if (LOGV)
    {
      LinkedList localLinkedList1 = sUpList;
      Intent localIntent1 = this.mUpIntent;
      if (!localLinkedList1.contains(localIntent1))
        int i = Log.wtf("MusicBaseActivity", "RemoveUpIntent with intent not in up stack");
    }
    LinkedList localLinkedList2 = sUpList;
    Intent localIntent2 = this.mUpIntent;
    if (!localLinkedList2.contains(localIntent2))
      return;
    LinkedList localLinkedList3 = sUpList;
    Intent localIntent3 = this.mUpIntent;
    boolean bool = localLinkedList3.remove(localIntent3);
    this.mUpIntent = null;
  }

  protected void replaceContent(Fragment paramFragment, boolean paramBoolean)
  {
    BaseActivity localBaseActivity = this;
    Fragment localFragment = paramFragment;
    boolean bool = paramBoolean;
    int i = -1;
    localBaseActivity.replaceContent(localFragment, bool, null, -1, i);
  }

  protected void replaceContent(Fragment paramFragment, boolean paramBoolean, String paramString)
  {
    BaseActivity localBaseActivity = this;
    Fragment localFragment = paramFragment;
    boolean bool = paramBoolean;
    String str = paramString;
    int i = -1;
    localBaseActivity.replaceContent(localFragment, bool, str, -1, i);
  }

  protected void replaceContent(Fragment paramFragment, boolean paramBoolean, String paramString, int paramInt1, int paramInt2)
  {
    clearMainView();
    FragmentTransaction localFragmentTransaction1 = getSupportFragmentManager().beginTransaction();
    if ((paramInt1 != -1) && (paramInt2 != -1))
      FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.setCustomAnimations(paramInt1, paramInt2);
    if (paramString != null)
      FragmentTransaction localFragmentTransaction3 = localFragmentTransaction1.replace(2131296358, paramFragment, paramString);
    while (true)
    {
      if (paramBoolean)
      {
        String str = paramFragment.getClass().getSimpleName();
        FragmentTransaction localFragmentTransaction4 = localFragmentTransaction1.addToBackStack(str);
      }
      int i = localFragmentTransaction1.commitAllowingStateLoss();
      this.mContentFragment = paramFragment;
      return;
      FragmentTransaction localFragmentTransaction5 = localFragmentTransaction1.replace(2131296358, paramFragment);
    }
  }

  public void setActionBarAlpha(int paramInt)
  {
    if (getPreferences().isTvMusic())
      return;
    this.mActionBarAlpha = paramInt;
    if (isSideDrawerOpen())
      return;
    setActionBarAlphaInternal(paramInt);
  }

  public void setActionBarTitle(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.mActionBarTitleResId = paramInt;
    this.mOnDeviceSelector = paramBoolean1;
    this.mShowHome = paramBoolean2;
    this.mActionBarTitle = null;
    if (isSideDrawerOpen())
    {
      setActionBarTitleInternal(2131230928, false, paramBoolean2);
      return;
    }
    setActionBarTitleInternal(paramInt, paramBoolean1, paramBoolean2);
  }

  public void setActionBarTitle(String paramString)
  {
    this.mActionBarTitle = paramString;
    this.mActionBarTitleResId = -1;
    if (isSideDrawerOpen())
    {
      String str = getResources().getString(2131230928);
      setActionBarTitleInternal(str);
      return;
    }
    setActionBarTitleInternal(paramString);
  }

  public void setEmptyScreenShowing(boolean paramBoolean)
  {
    this.mEmptyScreenShowing = paramBoolean;
  }

  protected void setMenuVisibility(Menu paramMenu, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    MenuItem localMenuItem1 = paramMenu.findItem(paramInt);
    if (localMenuItem1 == null)
      return;
    MenuItem localMenuItem2 = localMenuItem1.setVisible(paramBoolean1);
    MenuItem localMenuItem3 = localMenuItem1.setEnabled(paramBoolean2);
  }

  protected void setupNowPlayingBar()
  {
    if (showNowPlayingBar())
    {
      FragmentManager localFragmentManager = getSupportFragmentManager();
      if (localFragmentManager.findFragmentByTag("nowplaying") == null)
      {
        FragmentTransaction localFragmentTransaction1 = localFragmentManager.beginTransaction();
        if (this.mNowPlayingFragment == null)
        {
          NowPlayingScreenFragment localNowPlayingScreenFragment1 = new NowPlayingScreenFragment();
          this.mNowPlayingFragment = localNowPlayingScreenFragment1;
        }
        NowPlayingScreenFragment localNowPlayingScreenFragment2 = this.mNowPlayingFragment;
        FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.add(localNowPlayingScreenFragment2, "nowplaying");
        int i = localFragmentTransaction1.commit();
      }
      ExpandingScrollView localExpandingScrollView = getBottomDrawer();
      localExpandingScrollView.getBackground().setAlpha(0);
      ExpandingScrollView.ExpandingScrollViewListener local4 = new ExpandingScrollView.ExpandingScrollViewListener()
      {
        public void onDragEnded(ExpandingScrollView paramAnonymousExpandingScrollView, ExpandingScrollView.ExpandingState paramAnonymousExpandingState)
        {
        }

        public void onDragStarted(ExpandingScrollView paramAnonymousExpandingScrollView, ExpandingScrollView.ExpandingState paramAnonymousExpandingState)
        {
        }

        public void onExpandingStateChanged(ExpandingScrollView paramAnonymousExpandingScrollView, ExpandingScrollView.ExpandingState paramAnonymousExpandingState1, ExpandingScrollView.ExpandingState paramAnonymousExpandingState2)
        {
          if (BaseActivity.LOGV)
          {
            String str = "Bottom state changed: " + paramAnonymousExpandingState2;
            int i = Log.d("MusicBaseActivity", str);
          }
          ViewGroup localViewGroup = BaseActivity.this.getMainContainer();
          int j = 262144;
          int k = 262144;
          ExpandingScrollView.ExpandingState localExpandingState1 = ExpandingScrollView.ExpandingState.HIDDEN;
          if (paramAnonymousExpandingState2 == localExpandingState1)
            k = 393216;
          ExpandingScrollView.ExpandingState localExpandingState2 = ExpandingScrollView.ExpandingState.FULLY_EXPANDED;
          if (paramAnonymousExpandingState2 == localExpandingState2)
            j = 393216;
          Iterator localIterator = BaseActivity.this.mContentViews.iterator();
          while (localIterator.hasNext())
            ((ViewGroup)localIterator.next()).setDescendantFocusability(j);
          paramAnonymousExpandingScrollView.setDescendantFocusability(k);
          ExpandingScrollView.ExpandingState localExpandingState3 = ExpandingScrollView.ExpandingState.HIDDEN;
          if (paramAnonymousExpandingState2 == localExpandingState3);
          for (int m = 0; ; m = BaseActivity.this.getResources().getDimensionPixelSize(2131558461))
          {
            int n = localViewGroup.getPaddingBottom();
            if (m != n)
              return;
            int i1 = localViewGroup.getPaddingLeft();
            int i2 = localViewGroup.getPaddingTop();
            int i3 = localViewGroup.getPaddingRight();
            localViewGroup.setPadding(i1, i2, i3, m);
            localViewGroup.requestLayout();
            return;
          }
        }

        public void onMoving(ExpandingScrollView paramAnonymousExpandingScrollView, ExpandingScrollView.ExpandingState paramAnonymousExpandingState, float paramAnonymousFloat)
        {
        }
      };
      localExpandingScrollView.addListener(local4);
      return;
    }
    ViewGroup localViewGroup = getMainContainer();
    int j = localViewGroup.getPaddingLeft();
    int k = localViewGroup.getPaddingTop();
    int m = localViewGroup.getPaddingRight();
    localViewGroup.setPadding(j, k, m, 0);
  }

  protected void setupRootMenu(ListView paramListView)
  {
    HomeMenu.HomeMenuAdapter localHomeMenuAdapter = HomeMenu.configureListView(paramListView, this);
    this.mRootMenuList = paramListView;
  }

  public void showActionBar()
  {
    getSupportActionBar().show();
  }

  protected void showDrawerIfRequested()
  {
    if (!this.mDrawerRestoredStateOpen)
      return;
    openSideDrawer();
  }

  protected boolean showNowPlayingBar()
  {
    return true;
  }

  protected boolean useActionBarHamburger()
  {
    return false;
  }

  protected boolean usesActionBarOverlay()
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseActivity
 * JD-Core Version:    0.6.2
 */