package com.google.android.music.activities;

import android.accounts.Account;
import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Process;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.view.MenuItem;
import com.google.android.gsf.Gservices;
import com.google.android.music.DownloadQueueActivity;
import com.google.android.music.NautilusStatus;
import com.google.android.music.download.cache.CacheService;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.download.cache.StorageLocation;
import com.google.android.music.download.cache.StorageProbeUtils;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.purchase.Finsky;
import com.google.android.music.tutorial.TutorialUtils;
import com.google.android.music.ui.DownloadContainerActivity;
import com.google.android.music.ui.SDCardSelectorActivity;
import com.google.android.music.ui.StreamQualityListAdapter;
import com.google.android.music.ui.StreamQualityListAdapter.StreamQualityListItem;
import com.google.android.music.utils.ConfigUtils;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.PostFroyoUtils.EnvironmentCompat;
import com.google.android.music.utils.async.AsyncWorkers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MusicSettingsActivity extends PreferenceActivity
  implements Preference.OnPreferenceChangeListener
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
  private PreferenceScreen mAccountSettingsScreen;
  private PreferenceScreen mAccountTypeScreen;
  private CheckBoxPreference mAutoCache;
  private CheckBoxPreference mCachedStreamed;
  private PreferenceScreen mClearCache;
  private CheckBoxPreference mContentFilter;
  private CheckBoxPreference mDebugLogs;
  private PreferenceCategory mDeveloperCategory;
  private PreferenceScreen mDownloadLocationScreen;
  private CheckBoxPreference mDownloadOnlyOnWifi;
  private PreferenceScreen mDownloadQueueScreen;
  private PreferenceCategory mDownloadingCategory;
  private PreferenceScreen mEqualizerScreen;
  private PreferenceCategory mGeneralCategory;
  private boolean mIsExternalEmulated;
  private boolean mIsWifiOnly = false;
  private PreferenceScreen mManageNautilusScreen;
  private MusicPreferences mMusicPreferences;
  private NautilusStatus mNautilusStatus;
  private boolean mSecondaryExternalEnabled;
  private Account mSelectedAccount;
  private CheckBoxPreference mStreamOnlyOnWifi;
  private PreferenceScreen mStreamQuality;
  private List<StreamQualityListAdapter.StreamQualityListItem> mStreamQualityList;
  private PreferenceCategory mStreamingCategory;
  private MusicEventLogger mTracker;
  private PreferenceScreen mVersion;

  private boolean getIsWifiOnly()
  {
    boolean bool1 = false;
    if (!this.mMusicPreferences.isTvMusic());
    while (true)
    {
      try
      {
        Class localClass = Class.forName("android.os.SystemProperties");
        Class[] arrayOfClass = new Class[1];
        arrayOfClass[0] = String.class;
        Method localMethod = localClass.getMethod("get", arrayOfClass);
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = "ro.carrier";
        Object localObject = localMethod.invoke(null, arrayOfObject);
        boolean bool2 = "wifi-only".equals(localObject);
        bool1 = bool2;
        return bool1;
      }
      catch (Exception localException)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Error trying to access SystemProperties to check if wifi-only: ");
        String str1 = localException.getMessage();
        String str2 = str1;
        Log.w("MusicSettings", str2, localException);
        continue;
      }
      bool1 = true;
    }
  }

  private void handleEnableDebugLogs(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      boolean bool1 = this.mDebugLogs.isChecked();
      CheckBoxPreference localCheckBoxPreference = this.mDebugLogs;
      if (!bool1);
      for (boolean bool2 = true; ; bool2 = false)
      {
        localCheckBoxPreference.setChecked(bool2);
        return;
      }
    }
    MusicPreferences localMusicPreferences = this.mMusicPreferences;
    boolean bool3 = this.mDebugLogs.isChecked();
    localMusicPreferences.setLogFilesEnable(bool3);
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        MusicSettingsActivity.this.killOurProcesses();
      }
    };
    boolean bool4 = localLoggableHandler.postDelayed(local2, 3000L);
  }

  private void hideStreamingPreferences()
  {
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    PreferenceCategory localPreferenceCategory1 = this.mDownloadingCategory;
    boolean bool1 = localPreferenceScreen.removePreference(localPreferenceCategory1);
    PreferenceCategory localPreferenceCategory2 = this.mStreamingCategory;
    boolean bool2 = localPreferenceScreen.removePreference(localPreferenceCategory2);
    PreferenceCategory localPreferenceCategory3 = this.mGeneralCategory;
    CheckBoxPreference localCheckBoxPreference = this.mContentFilter;
    boolean bool3 = localPreferenceCategory3.removePreference(localCheckBoxPreference);
  }

  private void initStreamQualityList()
  {
    String str1 = getResources().getString(2131230789);
    String str2 = getResources().getString(2131230792);
    StreamQualityListAdapter.StreamQualityListItem localStreamQualityListItem1 = new StreamQualityListAdapter.StreamQualityListItem(str1, str2);
    String str3 = getResources().getString(2131230790);
    StreamQualityListAdapter.StreamQualityListItem localStreamQualityListItem2 = new StreamQualityListAdapter.StreamQualityListItem(str3, null);
    String str4 = getResources().getString(2131230791);
    String str5 = getResources().getString(2131230793);
    StreamQualityListAdapter.StreamQualityListItem localStreamQualityListItem3 = new StreamQualityListAdapter.StreamQualityListItem(str4, str5);
    ArrayList localArrayList = new ArrayList();
    this.mStreamQualityList = localArrayList;
    boolean bool1 = this.mStreamQualityList.add(localStreamQualityListItem1);
    boolean bool2 = this.mStreamQualityList.add(localStreamQualityListItem2);
    boolean bool3 = this.mStreamQualityList.add(localStreamQualityListItem3);
  }

  private void killOurProcesses()
  {
    String str1 = getPackageName();
    List localList = ((ActivityManager)getSystemService("activity")).getRunningAppProcesses();
    String str2 = str1 + ":main";
    String str3 = str1 + ":ui";
    Iterator localIterator = localList.iterator();
    int i = -1;
    int j = -1;
    ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo;
    int k;
    int m;
    if (localIterator.hasNext())
    {
      localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)localIterator.next();
      if (localRunningAppProcessInfo.processName.equals(str2))
      {
        k = localRunningAppProcessInfo.pid;
        m = j;
        label122: if ((m == -1) || (k == -1))
          break label193;
      }
    }
    while (true)
    {
      if (k != -1)
        Process.killProcess(k);
      if (m == -1)
        return;
      Process.killProcess(m);
      return;
      if (localRunningAppProcessInfo.processName.equals(str3))
      {
        int n = localRunningAppProcessInfo.pid;
        k = i;
        m = n;
        break label122;
        label193: j = m;
        i = k;
        break;
      }
      k = i;
      m = j;
      break label122;
      k = i;
      m = j;
    }
  }

  private void manageNautilusStatus()
  {
    int[] arrayOfInt = 5.$SwitchMap$com$google$android$music$NautilusStatus;
    int i = this.mNautilusStatus.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      Log.i("MusicSettings", "invalid all access status");
      return;
    case 1:
      MusicEventLogger localMusicEventLogger1 = this.mTracker;
      Object[] arrayOfObject1 = new Object[0];
      localMusicEventLogger1.trackEvent("settingsSignupForNautilusFree", arrayOfObject1);
      boolean bool1 = TutorialUtils.launchTutorialOnDemand(this);
      return;
    case 2:
      MusicEventLogger localMusicEventLogger2 = this.mTracker;
      Object[] arrayOfObject2 = new Object[0];
      localMusicEventLogger2.trackEvent("settingsSignupForNautilusPaid", arrayOfObject2);
      boolean bool2 = TutorialUtils.launchTutorialOnDemand(this);
      return;
    case 3:
    }
    MusicEventLogger localMusicEventLogger3 = this.mTracker;
    Object[] arrayOfObject3 = new Object[0];
    localMusicEventLogger3.trackEvent("settingsCancelNautilusPaid", arrayOfObject3);
    if (!Finsky.startCancelNautilusActivity(this))
      return;
    finish();
  }

  private void refreshNautilusUI()
  {
    NautilusStatus localNautilusStatus1 = this.mMusicPreferences.getNautilusStatus();
    this.mNautilusStatus = localNautilusStatus1;
    if (this.mSelectedAccount != null)
    {
      NautilusStatus localNautilusStatus2 = this.mNautilusStatus;
      NautilusStatus localNautilusStatus3 = NautilusStatus.UNAVAILABLE;
      if (localNautilusStatus2 != localNautilusStatus3)
      {
        MusicPreferences localMusicPreferences1 = this.mMusicPreferences;
        if (Finsky.isDirectPurchaseAvailable(this, localMusicPreferences1))
          break label92;
      }
    }
    PreferenceCategory localPreferenceCategory1 = this.mGeneralCategory;
    PreferenceScreen localPreferenceScreen1 = this.mAccountTypeScreen;
    boolean bool1 = localPreferenceCategory1.removePreference(localPreferenceScreen1);
    PreferenceCategory localPreferenceCategory2 = this.mGeneralCategory;
    PreferenceScreen localPreferenceScreen2 = this.mManageNautilusScreen;
    boolean bool2 = localPreferenceCategory2.removePreference(localPreferenceScreen2);
    return;
    label92: PreferenceCategory localPreferenceCategory3 = this.mGeneralCategory;
    String str1 = getResources().getString(2131231345);
    if (localPreferenceCategory3.findPreference(str1) == null)
    {
      PreferenceCategory localPreferenceCategory4 = this.mGeneralCategory;
      PreferenceScreen localPreferenceScreen3 = this.mAccountTypeScreen;
      boolean bool3 = localPreferenceCategory4.addPreference(localPreferenceScreen3);
    }
    PreferenceCategory localPreferenceCategory5 = this.mGeneralCategory;
    String str2 = getResources().getString(2131231350);
    if (localPreferenceCategory5.findPreference(str2) == null)
    {
      PreferenceCategory localPreferenceCategory6 = this.mGeneralCategory;
      PreferenceScreen localPreferenceScreen4 = this.mManageNautilusScreen;
      boolean bool4 = localPreferenceCategory6.addPreference(localPreferenceScreen4);
    }
    int[] arrayOfInt = 5.$SwitchMap$com$google$android$music$NautilusStatus;
    int i = this.mNautilusStatus.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("invalid nautilus status: ");
      NautilusStatus localNautilusStatus4 = this.mNautilusStatus;
      String str3 = localNautilusStatus4;
      Log.wtf("MusicSettings", str3);
      return;
    case 1:
      PreferenceCategory localPreferenceCategory7 = this.mGeneralCategory;
      PreferenceScreen localPreferenceScreen5 = this.mAccountTypeScreen;
      boolean bool5 = localPreferenceCategory7.removePreference(localPreferenceScreen5);
      this.mManageNautilusScreen.setTitle(2131231353);
      this.mManageNautilusScreen.setSummary(2131231354);
      return;
    case 2:
      PreferenceCategory localPreferenceCategory8 = this.mGeneralCategory;
      PreferenceScreen localPreferenceScreen6 = this.mAccountTypeScreen;
      boolean bool6 = localPreferenceCategory8.removePreference(localPreferenceScreen6);
      this.mManageNautilusScreen.setTitle(2131231355);
      this.mManageNautilusScreen.setSummary(2131231356);
      return;
    case 3:
    }
    this.mAccountTypeScreen.setTitle(2131231348);
    setBillDate(2131231349);
    MusicPreferences localMusicPreferences2 = this.mMusicPreferences;
    if (Finsky.doesSupportNautilusCancelation(this, localMusicPreferences2))
    {
      this.mManageNautilusScreen.setTitle(2131231359);
      this.mManageNautilusScreen.setSummary(2131231360);
      return;
    }
    PreferenceCategory localPreferenceCategory9 = this.mGeneralCategory;
    PreferenceScreen localPreferenceScreen7 = this.mManageNautilusScreen;
    boolean bool7 = localPreferenceCategory9.removePreference(localPreferenceScreen7);
  }

  private void refreshUI()
  {
    Account localAccount = this.mMusicPreferences.getStreamingAccount();
    this.mSelectedAccount = localAccount;
    String str1;
    label63: boolean bool6;
    label193: boolean bool7;
    label231: PreferenceScreen localPreferenceScreen2;
    if (this.mSelectedAccount != null)
    {
      showStreamingPreferences();
      refreshNautilusUI();
      str1 = this.mMusicPreferences.getSeletectedAccountForDisplay();
      if (str1 != null)
        break label271;
      PreferenceScreen localPreferenceScreen1 = this.mAccountSettingsScreen;
      String str2 = getResources().getString(2131230782);
      localPreferenceScreen1.setSummary(str2);
      CheckBoxPreference localCheckBoxPreference1 = this.mCachedStreamed;
      boolean bool1 = this.mMusicPreferences.isCachedStreamingMusicEnabled();
      localCheckBoxPreference1.setChecked(bool1);
      CheckBoxPreference localCheckBoxPreference2 = this.mAutoCache;
      boolean bool2 = this.mMusicPreferences.isAutoCachingEnabled();
      localCheckBoxPreference2.setChecked(bool2);
      CheckBoxPreference localCheckBoxPreference3 = this.mStreamOnlyOnWifi;
      boolean bool3 = this.mMusicPreferences.isStreamOnlyOnWifi();
      localCheckBoxPreference3.setChecked(bool3);
      CheckBoxPreference localCheckBoxPreference4 = this.mDownloadOnlyOnWifi;
      boolean bool4 = this.mMusicPreferences.isOfflineDLOnlyOnWifi();
      localCheckBoxPreference4.setChecked(bool4);
      CheckBoxPreference localCheckBoxPreference5 = this.mDebugLogs;
      boolean bool5 = this.mMusicPreferences.isLogFilesEnabled();
      localCheckBoxPreference5.setChecked(bool5);
      CheckBoxPreference localCheckBoxPreference6 = this.mContentFilter;
      if (this.mMusicPreferences.getContentFilter() != 2)
        break label316;
      bool6 = true;
      localCheckBoxPreference6.setChecked(bool6);
      updateSecondaryExternalSetting();
      if (!this.mMusicPreferences.isMediaRouteSupportEnabled())
        return;
      if (MediaRouter.getInstance(this).getSelectedRoute().getPlaybackType() != 0)
        break label322;
      bool7 = true;
      this.mEqualizerScreen.setEnabled(bool7);
      localPreferenceScreen2 = this.mEqualizerScreen;
      if (!bool7)
        break label328;
    }
    label271: label316: label322: label328: for (int i = 2131231319; ; i = 2131231320)
    {
      localPreferenceScreen2.setSummary(i);
      return;
      hideStreamingPreferences();
      break;
      PreferenceScreen localPreferenceScreen3 = this.mAccountSettingsScreen;
      Resources localResources = getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = str1;
      String str3 = localResources.getString(2131230783, arrayOfObject);
      localPreferenceScreen3.setSummary(str3);
      break label63;
      bool6 = false;
      break label193;
      bool7 = false;
      break label231;
    }
  }

  private void setBillDate(int paramInt)
  {
    long l = ConfigUtils.getNautilusExpirationTimeInMillisec();
    if (l > 0L)
    {
      java.text.DateFormat localDateFormat = android.text.format.DateFormat.getDateFormat(this);
      Long localLong = Long.valueOf(l);
      String str1 = localDateFormat.format(localLong);
      Resources localResources = getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = str1;
      String str2 = localResources.getString(paramInt, arrayOfObject);
      this.mAccountTypeScreen.setSummary(str2);
      return;
    }
    this.mAccountTypeScreen.setSummary("");
  }

  private void showDownloadLocationDialog()
  {
    Context localContext = getApplicationContext();
    Intent localIntent = new Intent(localContext, SDCardSelectorActivity.class);
    startActivity(localIntent);
  }

  private void showStreamQualityDialog()
  {
    int i = this.mMusicPreferences.getStreamQuality();
    List localList = this.mStreamQualityList;
    StreamQualityListAdapter localStreamQualityListAdapter = new StreamQualityListAdapter(this, 2130968681, localList, i);
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
    AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(2131230788);
    DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        if (paramAnonymousInt < 0)
          return;
        int i = MusicSettingsActivity.this.mStreamQualityList.size();
        if (paramAnonymousInt >= i)
          return;
        MusicEventLogger localMusicEventLogger = MusicSettingsActivity.this.mTracker;
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = "setting";
        arrayOfObject[1] = "streamQuality";
        arrayOfObject[2] = "settingValue";
        Integer localInteger = Integer.valueOf(paramAnonymousInt);
        arrayOfObject[3] = localInteger;
        localMusicEventLogger.trackEvent("changeSetting", arrayOfObject);
        MusicSettingsActivity.this.mMusicPreferences.setStreamQuality(paramAnonymousInt);
        MusicSettingsActivity.this.updateStreamQualitySummary();
        paramAnonymousDialogInterface.dismiss();
      }
    };
    AlertDialog.Builder localBuilder3 = localBuilder2.setSingleChoiceItems(localStreamQualityListAdapter, i, local1);
    AlertDialog localAlertDialog = localBuilder1.create();
    localAlertDialog.setCanceledOnTouchOutside(true);
    localAlertDialog.show();
  }

  private void showStreamingPreferences()
  {
    PreferenceScreen localPreferenceScreen = getPreferenceScreen();
    PreferenceCategory localPreferenceCategory1 = this.mDownloadingCategory;
    boolean bool1 = localPreferenceScreen.addPreference(localPreferenceCategory1);
    if (!this.mIsWifiOnly)
    {
      PreferenceCategory localPreferenceCategory2 = this.mStreamingCategory;
      boolean bool2 = localPreferenceScreen.addPreference(localPreferenceCategory2);
    }
    PreferenceCategory localPreferenceCategory3 = this.mGeneralCategory;
    CheckBoxPreference localCheckBoxPreference = this.mContentFilter;
    boolean bool3 = localPreferenceCategory3.addPreference(localCheckBoxPreference);
  }

  private void updateSecondaryExternalSetting()
  {
    if (!this.mSecondaryExternalEnabled)
      return;
    if (!this.mIsExternalEmulated)
      return;
    MusicPreferences localMusicPreferences = this.mMusicPreferences;
    StorageLocation localStorageLocation = StorageProbeUtils.getSecondaryExternalStorageLocation(this, localMusicPreferences);
    if (localStorageLocation != null)
    {
      if (CacheUtils.isSecondaryExternalStorageMounted(this))
      {
        PreferenceScreen localPreferenceScreen = this.mDownloadLocationScreen;
        String str = localStorageLocation.mDescription;
        localPreferenceScreen.setSummary(str);
        return;
      }
      this.mDownloadLocationScreen.setSummary(2131231389);
      return;
    }
    this.mDownloadLocationScreen.setSummary(2131231379);
  }

  private void updateStreamQualitySummary()
  {
    if (this.mMusicPreferences.isLowStreamQuality())
    {
      this.mStreamQuality.setSummary(2131230789);
      return;
    }
    if (this.mMusicPreferences.isHighStreamQuality())
    {
      this.mStreamQuality.setSummary(2131230791);
      return;
    }
    this.mStreamQuality.setSummary(2131230790);
  }

  private void updateVersion()
  {
    try
    {
      PackageManager localPackageManager = getPackageManager();
      String str1 = getPackageName();
      PackageInfo localPackageInfo = localPackageManager.getPackageInfo(str1, 0);
      PreferenceScreen localPreferenceScreen = this.mVersion;
      StringBuilder localStringBuilder = new StringBuilder().append("v");
      String str2 = localPackageInfo.versionName;
      String str3 = str2;
      localPreferenceScreen.setSummary(str3);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      String str4 = "Package not found (to retrieve version number)" + localNameNotFoundException;
      Log.e("MusicSettings", str4);
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    Intent localIntent1 = getIntent();
    if (localIntent1.hasExtra(":android:show_fragment"))
      localIntent1.removeExtra(":android:show_fragment");
    super.onCreate(paramBundle);
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(this);
    this.mTracker = localMusicEventLogger;
    addPreferencesFromResource(2131099648);
    String str1 = getResources().getString(2131231309);
    PreferenceCategory localPreferenceCategory1 = (PreferenceCategory)findPreference(str1);
    this.mGeneralCategory = localPreferenceCategory1;
    String str2 = getResources().getString(2131231310);
    PreferenceCategory localPreferenceCategory2 = (PreferenceCategory)findPreference(str2);
    this.mDownloadingCategory = localPreferenceCategory2;
    String str3 = getResources().getString(2131231311);
    PreferenceCategory localPreferenceCategory3 = (PreferenceCategory)findPreference(str3);
    this.mStreamingCategory = localPreferenceCategory3;
    String str4 = getResources().getString(2131231312);
    PreferenceCategory localPreferenceCategory4 = (PreferenceCategory)findPreference(str4);
    this.mDeveloperCategory = localPreferenceCategory4;
    String str5 = getResources().getString(2131231345);
    PreferenceScreen localPreferenceScreen1 = (PreferenceScreen)findPreference(str5);
    this.mAccountTypeScreen = localPreferenceScreen1;
    String str6 = getResources().getString(2131231350);
    PreferenceScreen localPreferenceScreen2 = (PreferenceScreen)findPreference(str6);
    this.mManageNautilusScreen = localPreferenceScreen2;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
    this.mMusicPreferences = localMusicPreferences;
    String str7 = getResources().getString(2131230784);
    CheckBoxPreference localCheckBoxPreference1 = (CheckBoxPreference)findPreference(str7);
    this.mStreamOnlyOnWifi = localCheckBoxPreference1;
    String str8 = getResources().getString(2131230787);
    PreferenceScreen localPreferenceScreen3 = (PreferenceScreen)findPreference(str8);
    this.mStreamQuality = localPreferenceScreen3;
    updateStreamQualitySummary();
    initStreamQualityList();
    String str9 = getResources().getString(2131230815);
    CheckBoxPreference localCheckBoxPreference2 = (CheckBoxPreference)findPreference(str9);
    this.mCachedStreamed = localCheckBoxPreference2;
    String str10 = getResources().getString(2131230818);
    CheckBoxPreference localCheckBoxPreference3 = (CheckBoxPreference)findPreference(str10);
    this.mAutoCache = localCheckBoxPreference3;
    String str11 = getResources().getString(2131231342);
    PreferenceScreen localPreferenceScreen4 = (PreferenceScreen)findPreference(str11);
    this.mClearCache = localPreferenceScreen4;
    String str12 = getResources().getString(2131230812);
    CheckBoxPreference localCheckBoxPreference4 = (CheckBoxPreference)findPreference(str12);
    this.mDownloadOnlyOnWifi = localCheckBoxPreference4;
    String str13 = getResources().getString(2131230780);
    PreferenceScreen localPreferenceScreen5 = (PreferenceScreen)findPreference(str13);
    this.mAccountSettingsScreen = localPreferenceScreen5;
    String str14 = getResources().getString(2131230821);
    PreferenceScreen localPreferenceScreen6 = (PreferenceScreen)findPreference(str14);
    this.mDownloadQueueScreen = localPreferenceScreen6;
    String str15 = getResources().getString(2131230824);
    PreferenceScreen localPreferenceScreen7 = (PreferenceScreen)findPreference(str15);
    this.mDownloadLocationScreen = localPreferenceScreen7;
    String str16 = getResources().getString(2131230803);
    CheckBoxPreference localCheckBoxPreference5 = (CheckBoxPreference)findPreference(str16);
    this.mDebugLogs = localCheckBoxPreference5;
    String str17 = getResources().getString(2131230808);
    CheckBoxPreference localCheckBoxPreference6 = (CheckBoxPreference)findPreference(str17);
    this.mContentFilter = localCheckBoxPreference6;
    if (!this.mMusicPreferences.isNautilusEnabled())
      this.mContentFilter.setTitle(2131230810);
    String str18 = getResources().getString(2131231317);
    PreferenceScreen localPreferenceScreen8 = (PreferenceScreen)findPreference(str18);
    this.mEqualizerScreen = localPreferenceScreen8;
    Intent localIntent2 = new Intent("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
    ResolveInfo localResolveInfo = getPackageManager().resolveActivity(localIntent2, 0);
    if ((localResolveInfo == null) || (!localResolveInfo.activityInfo.exported))
    {
      PreferenceCategory localPreferenceCategory5 = this.mGeneralCategory;
      PreferenceScreen localPreferenceScreen9 = this.mEqualizerScreen;
      boolean bool1 = localPreferenceCategory5.removePreference(localPreferenceScreen9);
    }
    if ((this.mStreamOnlyOnWifi == null) || (this.mStreamQuality == null) || (this.mCachedStreamed == null) || (this.mAutoCache == null) || (this.mDownloadOnlyOnWifi == null) || (this.mAccountSettingsScreen == null) || (this.mDownloadQueueScreen == null) || (this.mDebugLogs == null))
      throw new IllegalStateException("Could not find the preference screens");
    PreferenceScreen localPreferenceScreen10 = (PreferenceScreen)findPreference("music_version_key");
    this.mVersion = localPreferenceScreen10;
    updateVersion();
    if (MusicPreferences.isHoneycombOrGreater())
    {
      ActionBar localActionBar = getActionBar();
      if (localActionBar != null)
        localActionBar.setDisplayHomeAsUpEnabled(true);
    }
    boolean bool2 = getIsWifiOnly();
    this.mIsWifiOnly = bool2;
    if (this.mIsWifiOnly)
    {
      PreferenceScreen localPreferenceScreen11 = getPreferenceScreen();
      PreferenceCategory localPreferenceCategory6 = this.mStreamingCategory;
      boolean bool3 = localPreferenceScreen11.removePreference(localPreferenceCategory6);
      PreferenceCategory localPreferenceCategory7 = this.mDownloadingCategory;
      CheckBoxPreference localCheckBoxPreference7 = this.mDownloadOnlyOnWifi;
      if (!localPreferenceCategory7.removePreference(localCheckBoxPreference7))
        throw new IllegalStateException("Could not remove the streaming over wifi preferences");
    }
    if (!this.mMusicPreferences.isCachingFeatureAvailable())
    {
      PreferenceCategory localPreferenceCategory8 = this.mDownloadingCategory;
      CheckBoxPreference localCheckBoxPreference8 = this.mCachedStreamed;
      if (!localPreferenceCategory8.removePreference(localCheckBoxPreference8))
        throw new IllegalStateException("Could not remove the caching preferences");
      PreferenceCategory localPreferenceCategory9 = this.mDownloadingCategory;
      CheckBoxPreference localCheckBoxPreference9 = this.mAutoCache;
      if (!localPreferenceCategory9.removePreference(localCheckBoxPreference9))
        throw new IllegalStateException("Could not remove the autocaching preference");
    }
    if (!this.mMusicPreferences.isAutoCachingAvailable())
    {
      PreferenceCategory localPreferenceCategory10 = this.mDownloadingCategory;
      CheckBoxPreference localCheckBoxPreference10 = this.mAutoCache;
      if (!localPreferenceCategory10.removePreference(localCheckBoxPreference10))
        throw new IllegalStateException("Could not remove the autocaching preference");
    }
    if (!this.mMusicPreferences.isOfflineFeatureAvailable())
    {
      PreferenceCategory localPreferenceCategory11 = this.mDownloadingCategory;
      PreferenceScreen localPreferenceScreen12 = this.mDownloadQueueScreen;
      if (!localPreferenceCategory11.removePreference(localPreferenceScreen12))
        throw new IllegalStateException("Could not remove the offline preferences");
    }
    ContentResolver localContentResolver = getApplicationContext().getContentResolver();
    if (!Gservices.getBoolean(localContentResolver, "music_debug_logs_enabled", false))
    {
      PreferenceCategory localPreferenceCategory12 = this.mDeveloperCategory;
      CheckBoxPreference localCheckBoxPreference11 = this.mDebugLogs;
      if (!localPreferenceCategory12.removePreference(localCheckBoxPreference11))
        throw new IllegalStateException("Could not remove the debug logs preferences");
      this.mMusicPreferences.setLogFilesEnable(false);
    }
    boolean bool4 = Gservices.getBoolean(localContentResolver, "music_enable_secondary_sdcards", false);
    this.mSecondaryExternalEnabled = bool4;
    boolean bool5 = PostFroyoUtils.EnvironmentCompat.isExternalStorageEmulated();
    this.mIsExternalEmulated = bool5;
    if ((this.mSecondaryExternalEnabled) && (this.mIsExternalEmulated))
      return;
    PreferenceCategory localPreferenceCategory13 = this.mDownloadingCategory;
    PreferenceScreen localPreferenceScreen13 = this.mDownloadLocationScreen;
    if (localPreferenceCategory13.removePreference(localPreferenceScreen13))
      return;
    throw new IllegalStateException("Could not remove the download location preference");
  }

  protected Dialog onCreateDialog(int paramInt)
  {
    Object localObject;
    if (paramInt != 0)
    {
      localObject = null;
      return localObject;
    }
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
    if (this.mDebugLogs.isChecked())
      AlertDialog.Builder localBuilder2 = localBuilder1.setMessage(2131230806);
    while (true)
    {
      DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          MusicSettingsActivity.this.handleEnableDebugLogs(false);
        }
      };
      AlertDialog.Builder localBuilder3 = localBuilder1.setNegativeButton(2131230742, local3);
      DialogInterface.OnClickListener local4 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          MusicSettingsActivity.this.handleEnableDebugLogs(true);
        }
      };
      AlertDialog.Builder localBuilder4 = localBuilder1.setPositiveButton(2131230741, local4);
      localObject = localBuilder1.create();
      break;
      AlertDialog.Builder localBuilder5 = localBuilder1.setMessage(2131230807);
    }
  }

  public void onDestroy()
  {
    super.onDestroy();
    MusicPreferences.releaseMusicPreferences(this);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if ((MusicPreferences.isHoneycombOrGreater()) && (paramMenuItem.getItemId() == 16908332))
      finish();
    for (boolean bool = true; ; bool = super.onOptionsItemSelected(paramMenuItem))
      return bool;
  }

  public boolean onPreferenceChange(Preference paramPreference, Object paramObject)
  {
    refreshUI();
    return true;
  }

  public boolean onPreferenceTreeClick(PreferenceScreen paramPreferenceScreen, Preference paramPreference)
  {
    Object[] arrayOfObject1 = null;
    boolean bool1 = false;
    PreferenceScreen localPreferenceScreen1 = this.mAccountSettingsScreen;
    if (paramPreference == localPreferenceScreen1)
    {
      MusicEventLogger localMusicEventLogger1 = this.mTracker;
      Object[] arrayOfObject2 = new Object[0];
      localMusicEventLogger1.trackEvent("settingsSelectAccount", arrayOfObject2);
      if (TutorialUtils.launchTutorialToChooseAccount(this, false))
        finish();
    }
    while (true)
    {
      bool1 = true;
      label54: return bool1;
      CheckBoxPreference localCheckBoxPreference1 = this.mCachedStreamed;
      MusicEventLogger localMusicEventLogger2;
      String str;
      if (paramPreference == localCheckBoxPreference1)
      {
        localMusicEventLogger2 = this.mTracker;
        str = "changeSetting";
        arrayOfObject1 = new Object[4];
        arrayOfObject1[0] = "setting";
        arrayOfObject1[1] = "cacheStreamed";
        arrayOfObject1[2] = "settingValue";
        if (this.mCachedStreamed.isChecked())
          int i = 1;
        Integer localInteger1 = Integer.valueOf(0);
        arrayOfObject1[3] = localInteger1;
        localMusicEventLogger2.trackEvent(str, arrayOfObject1);
        MusicPreferences localMusicPreferences1 = this.mMusicPreferences;
        boolean bool2 = this.mCachedStreamed.isChecked();
        localMusicPreferences1.setCachedStreamingMusicEnabled(bool2);
      }
      else
      {
        CheckBoxPreference localCheckBoxPreference2 = this.mAutoCache;
        if (paramPreference == localCheckBoxPreference2)
        {
          localMusicEventLogger2 = this.mTracker;
          str = "changeSetting";
          arrayOfObject1 = new Object[4];
          arrayOfObject1[0] = "setting";
          arrayOfObject1[1] = "autocache";
          arrayOfObject1[2] = "settingValue";
          if (this.mAutoCache.isChecked())
            int j = 1;
          Integer localInteger2 = Integer.valueOf(0);
          arrayOfObject1[3] = localInteger2;
          localMusicEventLogger2.trackEvent(str, arrayOfObject1);
          MusicPreferences localMusicPreferences2 = this.mMusicPreferences;
          boolean bool3 = this.mAutoCache.isChecked();
          localMusicPreferences2.setAutoCachingEnabled(bool3);
        }
        else
        {
          CheckBoxPreference localCheckBoxPreference3 = this.mStreamOnlyOnWifi;
          if (paramPreference == localCheckBoxPreference3)
          {
            localMusicEventLogger2 = this.mTracker;
            str = "changeSetting";
            arrayOfObject1 = new Object[4];
            arrayOfObject1[0] = "setting";
            arrayOfObject1[1] = "streamOnlyWifi";
            arrayOfObject1[2] = "settingValue";
            if (this.mStreamOnlyOnWifi.isChecked())
              int k = 1;
            Integer localInteger3 = Integer.valueOf(0);
            arrayOfObject1[3] = localInteger3;
            localMusicEventLogger2.trackEvent(str, arrayOfObject1);
            MusicPreferences localMusicPreferences3 = this.mMusicPreferences;
            boolean bool4 = this.mStreamOnlyOnWifi.isChecked();
            localMusicPreferences3.setStreamOnlyOnWifi(bool4);
          }
          else
          {
            PreferenceScreen localPreferenceScreen2 = this.mStreamQuality;
            if (paramPreference == localPreferenceScreen2)
            {
              showStreamQualityDialog();
            }
            else
            {
              PreferenceScreen localPreferenceScreen3 = this.mDownloadLocationScreen;
              if (paramPreference == localPreferenceScreen3)
              {
                showDownloadLocationDialog();
              }
              else
              {
                CheckBoxPreference localCheckBoxPreference4 = this.mDownloadOnlyOnWifi;
                if (paramPreference == localCheckBoxPreference4)
                {
                  localMusicEventLogger2 = this.mTracker;
                  str = "changeSetting";
                  arrayOfObject1 = new Object[4];
                  arrayOfObject1[0] = "setting";
                  arrayOfObject1[1] = "pinOnlyWifi";
                  arrayOfObject1[2] = "settingValue";
                  if (this.mDownloadOnlyOnWifi.isChecked())
                    int m = 1;
                  Integer localInteger4 = Integer.valueOf(0);
                  arrayOfObject1[3] = localInteger4;
                  localMusicEventLogger2.trackEvent(str, arrayOfObject1);
                  MusicPreferences localMusicPreferences4 = this.mMusicPreferences;
                  boolean bool5 = this.mDownloadOnlyOnWifi.isChecked();
                  localMusicPreferences4.setOffineDLOnlyOnWifi(bool5);
                }
                else
                {
                  CheckBoxPreference localCheckBoxPreference5 = this.mDebugLogs;
                  if (paramPreference != localCheckBoxPreference5)
                    break;
                  showDialog(0);
                }
              }
            }
          }
        }
      }
    }
    PreferenceScreen localPreferenceScreen4 = this.mEqualizerScreen;
    if (paramPreference == localPreferenceScreen4)
    {
      localIntent1 = new Intent("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL");
      int n = MusicUtils.getAudioSessionId();
      if (n != -1)
        Intent localIntent2 = localIntent1.putExtra("android.media.extra.AUDIO_SESSION", n);
      while (true)
      {
        startActivityForResult(localIntent1, 26);
        break;
        Log.w("MusicSettings", "Failed to get valid audio session id");
      }
    }
    CheckBoxPreference localCheckBoxPreference6 = this.mContentFilter;
    if (paramPreference == localCheckBoxPreference6)
      if (!this.mContentFilter.isChecked())
        break label756;
    label756: for (Intent localIntent1 = null; ; localIntent1 = null)
    {
      this.mMusicPreferences.setContentFilter(localIntent1);
      break;
      PreferenceScreen localPreferenceScreen5 = this.mManageNautilusScreen;
      if (paramPreference == localPreferenceScreen5)
      {
        manageNautilusStatus();
        break;
      }
      PreferenceScreen localPreferenceScreen6 = this.mClearCache;
      if (paramPreference == localPreferenceScreen6)
      {
        Intent localIntent3 = new Intent(this, CacheService.class);
        Intent localIntent4 = localIntent3.setAction("com.google.android.music.download.cache.CacheService.CLEAR_CACHE");
        ComponentName localComponentName = startService(localIntent3);
        break;
      }
      PreferenceScreen localPreferenceScreen7 = this.mDownloadQueueScreen;
      if (paramPreference != localPreferenceScreen7)
        break label54;
      if (MusicPreferences.usingNewDownloadUI(this));
      for (localIntent1 = new Intent(this, DownloadContainerActivity.class); ; localIntent1 = new Intent(this, DownloadQueueActivity.class))
      {
        startActivity(localIntent1);
        break;
      }
    }
  }

  protected void onResume()
  {
    super.onResume();
    refreshUI();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.activities.MusicSettingsActivity
 * JD-Core Version:    0.6.2
 */