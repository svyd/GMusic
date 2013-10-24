package com.google.android.music.tutorial;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.finsky.services.IMarketCatalogService;
import com.google.android.finsky.services.IMarketCatalogService.Stub;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.SafeServiceConnection;
import com.google.common.collect.Lists;
import java.util.ArrayList;

public class SignupStatus
{
  public static Account[] getAllAccounts(Context paramContext)
  {
    Account[] arrayOfAccount1 = AccountManager.get(paramContext).getAccountsByType("com.google");
    ArrayList localArrayList;
    Account[] arrayOfAccount3;
    if ((arrayOfAccount1 != null) && (arrayOfAccount1.length != 0))
    {
      localArrayList = Lists.newArrayList();
      Account[] arrayOfAccount2 = arrayOfAccount1;
      int i = arrayOfAccount2.length;
      int j = 0;
      while (j < i)
      {
        Account localAccount = arrayOfAccount2[j];
        if (!localAccount.name.endsWith("@youtube.com"))
          boolean bool = localArrayList.add(localAccount);
        j += 1;
      }
      int k = localArrayList.size();
      int m = arrayOfAccount1.length;
      if (k != m)
      {
        if (localArrayList.size() <= 0)
          break label120;
        arrayOfAccount3 = new Account[localArrayList.size()];
      }
    }
    label120: for (arrayOfAccount1 = (Account[])localArrayList.toArray(arrayOfAccount3); ; arrayOfAccount1 = null)
      return arrayOfAccount1;
  }

  private static SharedPreferences getSharedPreferences(Context paramContext)
  {
    int i = 0;
    if (Build.VERSION.SDK_INT >= 11)
      i = 0x0 | 0x4;
    return paramContext.getSharedPreferences("signup.pref", i);
  }

  private static Account[] getVerifiedAccounts(Context paramContext, SharedPreferences paramSharedPreferences)
  {
    if (getVerifiedStatus(paramSharedPreferences) == 2);
    for (Account[] arrayOfAccount = getAllAccounts(paramContext); ; arrayOfAccount = null)
      return arrayOfAccount;
  }

  private static int getVerifiedStatus(SharedPreferences paramSharedPreferences)
  {
    return paramSharedPreferences.getInt("status", 0);
  }

  private static boolean isAccountVerified(Context paramContext, SharedPreferences paramSharedPreferences, Account paramAccount)
  {
    boolean bool = false;
    Account[] arrayOfAccount1 = getVerifiedAccounts(paramContext, paramSharedPreferences);
    if (arrayOfAccount1 == null);
    label63: 
    while (true)
    {
      return bool;
      Account[] arrayOfAccount2 = arrayOfAccount1;
      int i = arrayOfAccount2.length;
      int j = 0;
      while (true)
      {
        if (j >= i)
          break label63;
        Account localAccount = arrayOfAccount2[j];
        if (paramAccount.equals(localAccount))
        {
          bool = true;
          break;
        }
        j += 1;
      }
    }
  }

  private static boolean isBackgroundDataEnabled(Context paramContext)
  {
    ConnectivityManager localConnectivityManager = (ConnectivityManager)paramContext.getSystemService("connectivity");
    if (localConnectivityManager != null);
    for (boolean bool = localConnectivityManager.getBackgroundDataSetting(); ; bool = true)
      return bool;
  }

  public static void launchVerificationCheck(Context paramContext)
  {
    Intent localIntent = new Intent(paramContext, SignupCheckService.class);
    ComponentName localComponentName = paramContext.startService(localIntent);
  }

  public static final class NetworkChangedReceiver extends BroadcastReceiver
  {
    public void onReceive(Context paramContext, Intent paramIntent)
    {
      String str1 = paramIntent.getAction();
      if ("android.net.conn.CONNECTIVITY_CHANGE".equals(str1))
      {
        if (paramIntent.getBooleanExtra("noConnectivity", true))
          return;
        SignupStatus.launchVerificationCheck(paramContext);
        return;
      }
      StringBuilder localStringBuilder = new StringBuilder().append("Unknown action received: ");
      String str2 = paramIntent.getAction();
      String str3 = str2;
      Throwable localThrowable = new Throwable();
      int i = Log.wtf("SignupStatus", str3, localThrowable);
    }
  }

  public static class SignupCheckService extends IntentService
  {
    private IMarketCatalogService mMarketCatalogService = null;
    private boolean mMarketCatalogServiceBound = false;
    private SafeServiceConnection mMarketServiceSafeConnection;
    private MusicPreferences mMusicPreferences;
    private NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;

    public SignupCheckService()
    {
      super();
      SafeServiceConnection local1 = new SafeServiceConnection()
      {
        public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
        {
          SignupStatus.SignupCheckService.this.onServiceConnectedImp(paramAnonymousComponentName, paramAnonymousIBinder);
        }

        public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
        {
          SignupStatus.SignupCheckService.this.onServiceDisconnectedImp(paramAnonymousComponentName);
        }
      };
      this.mMarketServiceSafeConnection = local1;
    }

    private void autoSelectAccountWithVerification(SharedPreferences paramSharedPreferences, MusicPreferences paramMusicPreferences)
    {
      Account localAccount = getSoleAccount();
      if (localAccount == null)
        return;
      if (!SignupStatus.isAccountVerified(this, paramSharedPreferences, localAccount))
        return;
      boolean bool = paramMusicPreferences.autoSelectStreamingAccount(localAccount);
    }

    private void autoSelectAccountWithoutVerification(MusicPreferences paramMusicPreferences)
    {
      Account localAccount = getSoleAccount();
      if (localAccount == null)
        return;
      boolean bool = paramMusicPreferences.autoSelectStreamingAccount(localAccount);
    }

    private void checkStoreAvailable()
    {
      long l1 = this.mMusicPreferences.getStoreAvailableLastChecked();
      long l2 = 259200000L + l1;
      long l3 = System.currentTimeMillis();
      if (l2 > l3)
        return;
      try
      {
        if (!connectToMarketService())
        {
          markStoreAvailability(false);
          return;
        }
        try
        {
          Account[] arrayOfAccount1 = SignupStatus.getAllAccounts(this);
          boolean bool = false;
          Account[] arrayOfAccount2 = arrayOfAccount1;
          int i = arrayOfAccount2.length;
          int j = 0;
          while (true)
          {
            if (j < i)
            {
              Account localAccount = arrayOfAccount2[j];
              IMarketCatalogService localIMarketCatalogService = this.mMarketCatalogService;
              String str1 = localAccount.name;
              bool = localIMarketCatalogService.isBackendEnabled(str1, 2);
              if (!bool);
            }
            else
            {
              markStoreAvailability(bool);
              return;
            }
            j += 1;
          }
        }
        catch (RemoteException localRemoteException)
        {
          while (true)
          {
            String str2 = localRemoteException.getMessage();
            int k = Log.w("SignupStatus", str2, localRemoteException);
            markStoreAvailability(false);
          }
        }
      }
      finally
      {
        disconnectFromMarketService();
      }
    }

    private boolean connectToMarketService()
    {
      boolean bool = false;
      SafeServiceConnection localSafeServiceConnection = this.mMarketServiceSafeConnection;
      Intent localIntent = new Intent("com.google.android.finsky.services.IMarketCatalogService.BIND");
      if (!localSafeServiceConnection.bindService(this, localIntent, 1))
        int i = Log.i("SignupStatus", "Could not find market service");
      while (true)
      {
        return bool;
        this.mMarketCatalogServiceBound = true;
        try
        {
          IMarketCatalogService localIMarketCatalogService = this.mMarketCatalogService;
          if (localIMarketCatalogService == null)
            localIMarketCatalogService = null;
          try
          {
            wait(localIMarketCatalogService);
            if (this.mMarketCatalogService == null)
              int j = Log.i("SignupStatus", "Could not connect to market service");
          }
          catch (InterruptedException localInterruptedException)
          {
            while (true)
            {
              String str = localInterruptedException.getMessage();
              int k = Log.w("SignupStatus", str, localInterruptedException);
            }
          }
        }
        finally
        {
        }
        bool = true;
      }
    }

    private void disableNetworkRecevier()
    {
      PackageManager localPackageManager = getPackageManager();
      ComponentName localComponentName = new ComponentName(this, SignupStatus.NetworkChangedReceiver.class);
      localPackageManager.setComponentEnabledSetting(localComponentName, 2, 1);
    }

    private void disconnectFromMarketService()
    {
      if (!this.mMarketCatalogServiceBound)
        return;
      this.mMarketCatalogServiceBound = false;
      this.mMarketServiceSafeConnection.unbindService(this);
    }

    private void enableNetworkReceiver()
    {
      PackageManager localPackageManager = getPackageManager();
      ComponentName localComponentName = new ComponentName(this, SignupStatus.NetworkChangedReceiver.class);
      localPackageManager.setComponentEnabledSetting(localComponentName, 1, 1);
    }

    private Account getSoleAccount()
    {
      Account[] arrayOfAccount = SignupStatus.getAllAccounts(this);
      if ((arrayOfAccount == null) || (arrayOfAccount.length != 1));
      for (Account localAccount = null; ; localAccount = arrayOfAccount[0])
        return localAccount;
    }

    private void markStoreAvailability(boolean paramBoolean)
    {
      this.mMusicPreferences.setStoreAvailable(paramBoolean);
      MusicPreferences localMusicPreferences = this.mMusicPreferences;
      long l = System.currentTimeMillis();
      localMusicPreferences.setStoreAvailableLastChecked(l);
      if (!DebugUtils.IS_DEBUG_BUILD)
        return;
      String str = "store availability: " + paramBoolean;
      int i = Log.i("SignupStatus", str);
    }

    private void onServiceConnectedImp(ComponentName paramComponentName, IBinder paramIBinder)
    {
      try
      {
        IMarketCatalogService localIMarketCatalogService = IMarketCatalogService.Stub.asInterface(paramIBinder);
        this.mMarketCatalogService = localIMarketCatalogService;
        notifyAll();
        return;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }

    private void onServiceDisconnectedImp(ComponentName paramComponentName)
    {
      this.mMarketCatalogService = null;
    }

    public void onCreate()
    {
      super.onCreate();
      MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
      this.mMusicPreferences = localMusicPreferences;
      NetworkMonitorServiceConnection localNetworkMonitorServiceConnection = new NetworkMonitorServiceConnection();
      this.mNetworkMonitorServiceConnection = localNetworkMonitorServiceConnection;
      this.mNetworkMonitorServiceConnection.bindToService(this);
    }

    public void onDestroy()
    {
      super.onDestroy();
      MusicPreferences.releaseMusicPreferences(this);
      this.mNetworkMonitorServiceConnection.unbindFromService(this);
    }

    protected void onHandleIntent(Intent paramIntent)
    {
      SharedPreferences localSharedPreferences = SignupStatus.getSharedPreferences(this);
      if (!this.mMusicPreferences.waitForFullyLoaded())
      {
        int i = Log.w("SignupStatus", "MusicPreferences never loaded.");
        return;
      }
      this.mMusicPreferences.validateStreamingAccount();
      if (!SignupStatus.isBackgroundDataEnabled(this))
      {
        enableNetworkReceiver();
        return;
      }
      if (!this.mNetworkMonitorServiceConnection.waitForServiceConnection(10000L))
      {
        int j = Log.w("SignupStatus", "NetworkMonitor service connection never came up");
        return;
      }
      try
      {
        INetworkMonitor localINetworkMonitor = this.mNetworkMonitorServiceConnection.getNetworkMonitor();
        if ((localINetworkMonitor == null) || (!localINetworkMonitor.isConnected()))
        {
          int k = Log.w("SignupStatus", "No connection available, not continuing signup check");
          enableNetworkReceiver();
          return;
        }
      }
      catch (RemoteException localRemoteException)
      {
        String str = localRemoteException.getMessage();
        int m = Log.e("SignupStatus", str, localRemoteException);
        return;
      }
      disableNetworkRecevier();
      SharedPreferences.Editor localEditor1 = localSharedPreferences.edit();
      checkStoreAvailable();
      boolean bool1 = this.mMusicPreferences.getStoreAvailable();
      if (bool1)
        SharedPreferences.Editor localEditor2 = localEditor1.putInt("status", 2);
      while (true)
      {
        boolean bool2 = localEditor1.commit();
        if (!MusicPreferences.isGlass())
          break;
        MusicPreferences localMusicPreferences1 = this.mMusicPreferences;
        autoSelectAccountWithoutVerification(localMusicPreferences1);
        return;
        if (SignupStatus.getVerifiedStatus(localSharedPreferences) == 2)
          SharedPreferences.Editor localEditor3 = localEditor1.remove("status");
      }
      if (!bool1)
        return;
      MusicPreferences localMusicPreferences2 = this.mMusicPreferences;
      autoSelectAccountWithVerification(localSharedPreferences, localMusicPreferences2);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.tutorial.SignupStatus
 * JD-Core Version:    0.6.2
 */