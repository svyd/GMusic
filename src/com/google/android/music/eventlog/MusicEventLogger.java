package com.google.android.music.eventlog;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.play.analytics.EventLogger;
import com.google.android.play.analytics.EventLogger.Configuration;
import com.google.android.play.analytics.EventLogger.LogSource;
import com.google.android.play.utils.PlayUtils;
import java.util.HashMap;

public class MusicEventLogger
{
  private static MusicEventLogger sInstance = null;
  private final BroadcastReceiver mAccountReceiver;
  private final long mAndroidId;
  private final String mApplicationVersion;
  private final String mAuthTokenType;
  private final Context mContext;
  private EventLogger mEventLogger;
  private final HashMap<Account, EventLogger> mEventLoggers;
  private final String mLoggingId;
  private final String mMccMnc;
  private final boolean mPlayLoggingEnabled;

  // ERROR //
  private MusicEventLogger(Context paramContext)
  {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial 37	java/lang/Object:<init>	()V
    //   4: new 6	com/google/android/music/eventlog/MusicEventLogger$1
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 40	com/google/android/music/eventlog/MusicEventLogger$1:<init>	(Lcom/google/android/music/eventlog/MusicEventLogger;)V
    //   12: astore_2
    //   13: aload_0
    //   14: aload_2
    //   15: putfield 42	com/google/android/music/eventlog/MusicEventLogger:mAccountReceiver	Landroid/content/BroadcastReceiver;
    //   18: aload_1
    //   19: invokevirtual 48	android/content/Context:getApplicationContext	()Landroid/content/Context;
    //   22: astore_3
    //   23: aload_0
    //   24: aload_3
    //   25: putfield 50	com/google/android/music/eventlog/MusicEventLogger:mContext	Landroid/content/Context;
    //   28: new 4	java/lang/Object
    //   31: dup
    //   32: invokespecial 37	java/lang/Object:<init>	()V
    //   35: astore 4
    //   37: aload_0
    //   38: getfield 50	com/google/android/music/eventlog/MusicEventLogger:mContext	Landroid/content/Context;
    //   41: aload 4
    //   43: invokestatic 56	com/google/android/music/preferences/MusicPreferences:getMusicPreferences	(Landroid/content/Context;Ljava/lang/Object;)Lcom/google/android/music/preferences/MusicPreferences;
    //   46: astore 5
    //   48: aload 5
    //   50: invokevirtual 60	com/google/android/music/preferences/MusicPreferences:getLoggingId	()Ljava/lang/String;
    //   53: astore 6
    //   55: aload_0
    //   56: aload 6
    //   58: putfield 62	com/google/android/music/eventlog/MusicEventLogger:mLoggingId	Ljava/lang/String;
    //   61: aload 4
    //   63: invokestatic 66	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   66: new 68	java/util/HashMap
    //   69: dup
    //   70: invokespecial 69	java/util/HashMap:<init>	()V
    //   73: astore 7
    //   75: aload_0
    //   76: aload 7
    //   78: putfield 71	com/google/android/music/eventlog/MusicEventLogger:mEventLoggers	Ljava/util/HashMap;
    //   81: aload_1
    //   82: invokevirtual 75	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   85: ldc 77
    //   87: iconst_0
    //   88: invokestatic 83	com/google/android/gsf/Gservices:getBoolean	(Landroid/content/ContentResolver;Ljava/lang/String;Z)Z
    //   91: istore 8
    //   93: aload_0
    //   94: iload 8
    //   96: putfield 85	com/google/android/music/eventlog/MusicEventLogger:mPlayLoggingEnabled	Z
    //   99: aload_1
    //   100: invokevirtual 75	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   103: ldc 87
    //   105: ldc2_w 88
    //   108: invokestatic 93	com/google/android/gsf/Gservices:getLong	(Landroid/content/ContentResolver;Ljava/lang/String;J)J
    //   111: lstore 9
    //   113: aload_0
    //   114: lload 9
    //   116: putfield 95	com/google/android/music/eventlog/MusicEventLogger:mAndroidId	J
    //   119: aload_0
    //   120: getfield 50	com/google/android/music/eventlog/MusicEventLogger:mContext	Landroid/content/Context;
    //   123: invokestatic 101	com/google/android/music/sync/google/MusicAuthInfo:getAuthTokenType	(Landroid/content/Context;)Ljava/lang/String;
    //   126: astore 11
    //   128: aload_0
    //   129: aload 11
    //   131: putfield 103	com/google/android/music/eventlog/MusicEventLogger:mAuthTokenType	Ljava/lang/String;
    //   134: aload_0
    //   135: getfield 50	com/google/android/music/eventlog/MusicEventLogger:mContext	Landroid/content/Context;
    //   138: ldc 105
    //   140: invokevirtual 109	android/content/Context:getSystemService	(Ljava/lang/String;)Ljava/lang/Object;
    //   143: checkcast 111	android/telephony/TelephonyManager
    //   146: invokevirtual 114	android/telephony/TelephonyManager:getSimOperator	()Ljava/lang/String;
    //   149: astore 12
    //   151: aload_0
    //   152: aload 12
    //   154: putfield 116	com/google/android/music/eventlog/MusicEventLogger:mMccMnc	Ljava/lang/String;
    //   157: aload_0
    //   158: getfield 50	com/google/android/music/eventlog/MusicEventLogger:mContext	Landroid/content/Context;
    //   161: invokevirtual 120	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   164: astore 13
    //   166: aload_0
    //   167: getfield 50	com/google/android/music/eventlog/MusicEventLogger:mContext	Landroid/content/Context;
    //   170: invokevirtual 123	android/content/Context:getPackageName	()Ljava/lang/String;
    //   173: astore 14
    //   175: aload 13
    //   177: aload 14
    //   179: iconst_0
    //   180: invokevirtual 129	android/content/pm/PackageManager:getPackageInfo	(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
    //   183: getfield 134	android/content/pm/PackageInfo:versionName	Ljava/lang/String;
    //   186: astore 15
    //   188: aload_0
    //   189: aload 15
    //   191: putfield 136	com/google/android/music/eventlog/MusicEventLogger:mApplicationVersion	Ljava/lang/String;
    //   194: new 138	android/content/IntentFilter
    //   197: dup
    //   198: invokespecial 139	android/content/IntentFilter:<init>	()V
    //   201: astore 16
    //   203: aload 16
    //   205: ldc 141
    //   207: invokevirtual 145	android/content/IntentFilter:addAction	(Ljava/lang/String;)V
    //   210: aload 16
    //   212: ldc 147
    //   214: invokevirtual 145	android/content/IntentFilter:addAction	(Ljava/lang/String;)V
    //   217: aload_0
    //   218: getfield 42	com/google/android/music/eventlog/MusicEventLogger:mAccountReceiver	Landroid/content/BroadcastReceiver;
    //   221: astore 17
    //   223: aload_1
    //   224: aload 17
    //   226: aload 16
    //   228: invokevirtual 151	android/content/Context:registerReceiver	(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
    //   231: astore 18
    //   233: aload_0
    //   234: invokespecial 154	com/google/android/music/eventlog/MusicEventLogger:maybeUpdateEventLogger	()V
    //   237: return
    //   238: astore 19
    //   240: aload 4
    //   242: invokestatic 66	com/google/android/music/preferences/MusicPreferences:releaseMusicPreferences	(Ljava/lang/Object;)V
    //   245: aload 19
    //   247: athrow
    //   248: astore 20
    //   250: new 156	java/lang/StringBuilder
    //   253: dup
    //   254: invokespecial 157	java/lang/StringBuilder:<init>	()V
    //   257: ldc 159
    //   259: invokevirtual 163	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: aload 20
    //   264: invokevirtual 166	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   267: invokevirtual 169	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   270: astore 21
    //   272: ldc 171
    //   274: aload 21
    //   276: invokestatic 177	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
    //   279: istore 22
    //   281: goto -93 -> 188
    //
    // Exception table:
    //   from	to	target	type
    //   48	61	238	finally
    //   157	188	248	android/content/pm/PackageManager$NameNotFoundException
  }

  /** @deprecated */
  public static void destroy()
  {
    try
    {
      if (sInstance != null)
      {
        sInstance.onDestroy();
        sInstance = null;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private EventLogger getEventLogger(Account paramAccount)
  {
    EventLogger localEventLogger = (EventLogger)this.mEventLoggers.get(paramAccount);
    if (localEventLogger == null)
    {
      Context localContext = this.mContext;
      String str1 = this.mLoggingId;
      String str2 = this.mAuthTokenType;
      EventLogger.LogSource localLogSource = EventLogger.LogSource.MUSIC;
      String str3 = PlayUtils.getDefaultUserAgentString(this.mContext);
      long l = this.mAndroidId;
      String str4 = this.mApplicationVersion;
      String str5 = this.mMccMnc;
      EventLogger.Configuration localConfiguration = new EventLogger.Configuration();
      Account localAccount = paramAccount;
      localEventLogger = new EventLogger(localContext, str1, str2, localLogSource, str3, l, str4, str5, localConfiguration, localAccount);
      Object localObject = this.mEventLoggers.put(paramAccount, localEventLogger);
    }
    return localEventLogger;
  }

  /** @deprecated */
  public static MusicEventLogger getInstance(Context paramContext)
  {
    try
    {
      if (sInstance == null)
      {
        Context localContext = paramContext.getApplicationContext();
        sInstance = new MusicEventLogger(localContext);
      }
      MusicEventLogger localMusicEventLogger = sInstance;
      return localMusicEventLogger;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  private Account getUploadAccount()
  {
    Object localObject1 = new Object();
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(this.mContext, localObject1);
    try
    {
      Account localAccount1 = getUploadAccount(localMusicPreferences);
      Account localAccount2 = localAccount1;
      return localAccount2;
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(localObject1);
    }
  }

  private Account getUploadAccount(MusicPreferences paramMusicPreferences)
  {
    Account localAccount = paramMusicPreferences.getSyncAccount();
    if (localAccount != null);
    while (true)
    {
      return localAccount;
      Account[] arrayOfAccount = ((AccountManager)this.mContext.getSystemService("account")).getAccountsByType("com.google");
      if ((arrayOfAccount != null) && (arrayOfAccount.length != 0))
        localAccount = arrayOfAccount[0];
      else
        localAccount = null;
    }
  }

  /** @deprecated */
  private void maybeUpdateEventLogger()
  {
    try
    {
      boolean bool = this.mPlayLoggingEnabled;
      if (!bool);
      while (true)
      {
        return;
        Account localAccount = getUploadAccount();
        EventLogger localEventLogger = getEventLogger(localAccount);
        this.mEventLogger = localEventLogger;
      }
    }
    finally
    {
    }
  }

  private void onDestroy()
  {
    Context localContext = this.mContext;
    BroadcastReceiver localBroadcastReceiver = this.mAccountReceiver;
    localContext.unregisterReceiver(localBroadcastReceiver);
  }

  public void trackEvent(String paramString, Object[] paramArrayOfObject)
  {
    if (this.mEventLogger == null)
      return;
    String[] arrayOfString = new String[paramArrayOfObject.length];
    Object[] arrayOfObject = paramArrayOfObject;
    int i = arrayOfObject.length;
    int j = 0;
    int m;
    for (int k = 0; j < i; k = m)
    {
      Object localObject = arrayOfObject[j];
      m = k + 1;
      String str = localObject.toString();
      arrayOfString[k] = str;
      j += 1;
    }
    this.mEventLogger.logEvent(paramString, null, arrayOfString);
  }

  public void trackScreenView(Context paramContext, String paramString, Object[] paramArrayOfObject)
  {
    if (this.mEventLogger == null)
      return;
    if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0))
    {
      EventLogger localEventLogger = this.mEventLogger;
      String[] arrayOfString1 = new String[2];
      arrayOfString1[0] = "pageName";
      arrayOfString1[1] = paramString;
      localEventLogger.logEvent("pageView", null, arrayOfString1);
      return;
    }
    String[] arrayOfString2 = new String[paramArrayOfObject.length + 2];
    arrayOfString2[0] = "pageName";
    arrayOfString2[1] = paramString;
    Object[] arrayOfObject = paramArrayOfObject;
    int i = arrayOfObject.length;
    int j = 0;
    int m;
    for (int k = 2; j < i; k = m)
    {
      Object localObject = arrayOfObject[j];
      m = k + 1;
      String str = localObject.toString();
      arrayOfString2[k] = str;
      j += 1;
    }
    this.mEventLogger.logEvent("pageView", null, arrayOfString2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.eventlog.MusicEventLogger
 * JD-Core Version:    0.6.2
 */