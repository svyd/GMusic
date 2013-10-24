package com.google.android.music.playback;

import android.accounts.Account;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.google.android.music.net.INetworkMonitor;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.utils.MusicUtils;

public class ErrorInfo
{
  private int mAlertMessageResId = -1;
  private int mAlertTitleResId = -1;
  private int mHelpUriResId = -1;
  private String mHttpParamAccountName;
  private int mInlineResId = -1;
  private int mNeutralButtonActionUriId = -1;
  private int mNeutralButtonResId = -1;
  private int mNotificationMessageResId = -1;
  private int mNotificationTitleResId = -1;
  private int mPositiveButtonResId = 2131231013;
  private int mPositivetButtonActionUriId = -1;

  public static ErrorInfo createErrorInfo(int paramInt, MusicPreferences paramMusicPreferences, INetworkMonitor paramINetworkMonitor)
  {
    ErrorInfo localErrorInfo = new ErrorInfo();
    switch (paramInt)
    {
    case 9:
    case 10:
    case 11:
    case 13:
    default:
      if (MusicUtils.isStreaming())
      {
        localErrorInfo.setAlertText(2131231058, 2131230946);
        localErrorInfo.setNotificationText(2131231058, 2131230946);
        localErrorInfo.setInlineText(2131230946);
      }
      break;
    case 2:
    case 3:
    case 4:
    case 6:
    case 5:
    case 7:
    case 8:
    case 12:
    case 14:
    case 15:
    }
    while (true)
    {
      return localErrorInfo;
      localErrorInfo.setAlertText(2131231058, 2131231059);
      localErrorInfo.setNotificationText(2131231058, 2131231059);
      localErrorInfo.setInlineText(2131231059);
      continue;
      int i = 0;
      if (paramMusicPreferences != null);
      try
      {
        if ((paramMusicPreferences.isStreamOnlyOnWifi()) && (paramINetworkMonitor != null))
        {
          boolean bool = paramINetworkMonitor.hasHighSpeedConnection();
          if (!bool)
            i = 1;
        }
        if (i != 0)
        {
          localErrorInfo.setAlertText(2131231058, 2131231060);
          localErrorInfo.setNotificationText(2131231058, 2131231060);
          localErrorInfo.setInlineText(2131231060);
        }
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
        {
          String str1 = localRemoteException.getMessage();
          int j = Log.e("ErrorInfo", str1, localRemoteException);
        }
        localErrorInfo.setAlertText(2131231058, 2131231061);
        localErrorInfo.setNotificationText(2131231058, 2131231061);
        localErrorInfo.setInlineText(2131231061);
      }
      continue;
      localErrorInfo.setAlertText(2131231062, 2131231063);
      localErrorInfo.setNotificationText(2131231062, 2131231063);
      localErrorInfo.setInlineText(2131231063);
      continue;
      localErrorInfo.setAlertText(2131231073, 2131231064);
      localErrorInfo.setNotificationText(2131231079, 2131231076);
      localErrorInfo.setHelp(2131231072);
      localErrorInfo.setInlineText(2131231073);
      continue;
      localErrorInfo.setAlertText(2131231075, 2131231066);
      localErrorInfo.setNotificationText(2131231081, 2131231078);
      localErrorInfo.setNeutralButtonAction(-1, 2131231069);
      localErrorInfo.setPositiveButtonAction(2131231067, 2131231070);
      localErrorInfo.setInlineText(2131231075);
      if (paramMusicPreferences != null)
      {
        String str2 = paramMusicPreferences.getSyncAccount().name;
        localErrorInfo.setHttpParamAccountName(str2);
      }
      else
      {
        int k = Log.w("ErrorInfo", "Failed to retrieve account name");
        continue;
        localErrorInfo.setAlertText(2131231074, 2131231065);
        localErrorInfo.setNotificationText(2131231080, 2131231077);
        localErrorInfo.setHelp(2131231072);
        localErrorInfo.setInlineText(2131231074);
        continue;
        localErrorInfo.setAlertText(2131231058, 2131230946);
        localErrorInfo.setNotificationText(2131231058, 2131230946);
        localErrorInfo.setInlineText(2131230946);
        continue;
        localErrorInfo.setAlertText(2131231083, 2131231082);
        localErrorInfo.setNotificationText(2131231085, 2131231084);
        localErrorInfo.setInlineText(2131231083);
        continue;
        localErrorInfo.setAlertText(2131231087, 2131231086);
        localErrorInfo.setNotificationText(2131231089, 2131231088);
        localErrorInfo.setInlineText(2131231087);
        continue;
        localErrorInfo.setAlertText(2131231091, 2131231090);
        localErrorInfo.setNotificationText(2131231093, 2131231092);
        localErrorInfo.setInlineText(2131231091);
        continue;
        localErrorInfo.setAlertText(2131230931, 2131230932);
        localErrorInfo.setNotificationText(2131230931, 2131230932);
        localErrorInfo.setInlineText(2131230932);
      }
    }
  }

  private void startManageDevicesActivity(Activity paramActivity, Intent paramIntent)
  {
    if (paramActivity.getPackageManager().resolveActivity(paramIntent, 0) == null)
    {
      String str = paramActivity.getResources().getString(2131231376);
      Toast.makeText(paramActivity, str, 0).show();
      return;
    }
    paramActivity.startActivity(paramIntent);
  }

  public boolean canShowAlert()
  {
    if ((this.mAlertMessageResId != -1) && (this.mAlertTitleResId != -1));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean canShowNotification()
  {
    if ((this.mNotificationTitleResId != -1) && (this.mNotificationMessageResId != -1));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public AlertDialog createAlert(final Activity paramActivity, final OnErrorAlertDismissed paramOnErrorAlertDismissed)
  {
    AlertDialog localAlertDialog;
    if (!canShowAlert())
    {
      localAlertDialog = null;
      return localAlertDialog;
    }
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(paramActivity);
    int i = getAlertMessageResId();
    AlertDialog.Builder localBuilder2 = localBuilder1.setMessage(i);
    int j = getAlertTitleResId();
    AlertDialog.Builder localBuilder3 = localBuilder1.setTitle(j);
    int k = getPositiveButtonResId();
    final int m = getPositiveButtonActionUri();
    if (k != -1)
    {
      DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (m != -1)
          {
            Intent localIntent1 = new Intent("android.intent.action.VIEW");
            Activity localActivity1 = paramActivity;
            int i = m;
            String str1 = localActivity1.getString(i);
            String str2 = ErrorInfo.this.geHttpParamAccountName();
            Uri localUri = MusicUtils.buildUriWithAccountName(str1, str2);
            Intent localIntent2 = localIntent1.setData(localUri);
            Intent localIntent3 = localIntent1.addFlags(268435456);
            Intent localIntent4 = localIntent1.addFlags(32768);
            Intent localIntent5 = localIntent1.addFlags(524288);
            ErrorInfo localErrorInfo = ErrorInfo.this;
            Activity localActivity2 = paramActivity;
            localErrorInfo.startManageDevicesActivity(localActivity2, localIntent1);
          }
          if (paramOnErrorAlertDismissed == null)
            return;
          paramOnErrorAlertDismissed.onErrorAlertDismissed();
        }
      };
      AlertDialog.Builder localBuilder4 = localBuilder1.setPositiveButton(k, local1);
    }
    int n;
    final int i1;
    if (hasNeutrualAction())
    {
      n = getNeutralButtonResId();
      i1 = getNeutralButtonActionUri();
    }
    while (true)
    {
      if (n != -1)
      {
        DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            if (i1 != -1)
            {
              Intent localIntent1 = new Intent("android.intent.action.VIEW");
              Activity localActivity1 = paramActivity;
              int i = i1;
              String str1 = localActivity1.getString(i);
              String str2 = ErrorInfo.this.geHttpParamAccountName();
              Uri localUri = MusicUtils.buildUriWithAccountName(str1, str2);
              Intent localIntent2 = localIntent1.setData(localUri);
              Intent localIntent3 = localIntent1.addFlags(268435456);
              Intent localIntent4 = localIntent1.addFlags(32768);
              Intent localIntent5 = localIntent1.addFlags(524288);
              ErrorInfo localErrorInfo = ErrorInfo.this;
              Activity localActivity2 = paramActivity;
              localErrorInfo.startManageDevicesActivity(localActivity2, localIntent1);
            }
            if (paramOnErrorAlertDismissed == null)
              return;
            paramOnErrorAlertDismissed.onErrorAlertDismissed();
          }
        };
        AlertDialog.Builder localBuilder5 = localBuilder1.setNeutralButton(n, local2);
      }
      DialogInterface.OnCancelListener local3 = new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          if (paramOnErrorAlertDismissed == null)
            return;
          paramOnErrorAlertDismissed.onErrorAlertDismissed();
        }
      };
      AlertDialog.Builder localBuilder6 = localBuilder1.setOnCancelListener(local3);
      localAlertDialog = localBuilder1.create();
      break;
      if (hasHelp())
      {
        n = getHelpButtonResId();
        i1 = getHelpUriResId();
      }
      else
      {
        n = getNeutralButtonResId();
        i1 = -1;
      }
    }
  }

  public Notification createNotification(ContextWrapper paramContextWrapper)
  {
    Notification localNotification;
    if (!canShowNotification())
    {
      localNotification = null;
      return localNotification;
    }
    String str1 = null;
    label33: Intent localIntent1;
    if (hasNeutrualAction())
    {
      int i = getNeutralButtonActionUri();
      str1 = paramContextWrapper.getString(i);
      if ((TextUtils.isEmpty(str1)) && (hasHelp()))
      {
        int j = getHelpUriResId();
        str1 = paramContextWrapper.getString(j);
      }
      if (str1 == null)
        break label235;
      localIntent1 = new Intent("android.intent.action.VIEW");
      String str2 = geHttpParamAccountName();
      Uri localUri = MusicUtils.buildUriWithAccountName(str1, str2);
      Intent localIntent2 = localIntent1.setData(localUri);
    }
    label235: for (PendingIntent localPendingIntent = PendingIntent.getActivity(paramContextWrapper, 0, localIntent1, 268435456); ; localPendingIntent = AppNavigation.getIntentToOpenApp(paramContextWrapper))
    {
      localNotification = new Notification();
      long l = System.currentTimeMillis();
      localNotification.when = l;
      localNotification.flags = 24;
      localNotification.icon = 17301624;
      int k = getNotificationTitleResId();
      String str3 = paramContextWrapper.getString(k);
      localNotification.tickerText = str3;
      Context localContext = paramContextWrapper.getBaseContext();
      int m = getNotificationTitleResId();
      String str4 = paramContextWrapper.getString(m);
      int n = getNotificationMessageResId();
      String str5 = paramContextWrapper.getString(n);
      localNotification.setLatestEventInfo(localContext, str4, str5, localPendingIntent);
      break;
      if (!hasPositiveAction())
        break label33;
      int i1 = getPositiveButtonActionUri();
      str1 = paramContextWrapper.getString(i1);
      break label33;
    }
  }

  public String geHttpParamAccountName()
  {
    return this.mHttpParamAccountName;
  }

  public int getAlertMessageResId()
  {
    return this.mAlertMessageResId;
  }

  public int getAlertTitleResId()
  {
    return this.mAlertTitleResId;
  }

  public int getHelpButtonResId()
  {
    return 2131231071;
  }

  public int getHelpUriResId()
  {
    return this.mHelpUriResId;
  }

  public int getNeutralButtonActionUri()
  {
    return this.mNeutralButtonActionUriId;
  }

  public int getNeutralButtonResId()
  {
    return this.mNeutralButtonResId;
  }

  public int getNotificationMessageResId()
  {
    return this.mNotificationMessageResId;
  }

  public int getNotificationTitleResId()
  {
    return this.mNotificationTitleResId;
  }

  public int getPositiveButtonActionUri()
  {
    return this.mPositivetButtonActionUriId;
  }

  public int getPositiveButtonResId()
  {
    return this.mPositiveButtonResId;
  }

  public boolean hasHelp()
  {
    if (this.mHelpUriResId != -1);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean hasNeutrualAction()
  {
    if (this.mNeutralButtonActionUriId != -1);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean hasPositiveAction()
  {
    if (this.mPositivetButtonActionUriId != -1);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void setAlertText(int paramInt1, int paramInt2)
  {
    this.mAlertTitleResId = paramInt1;
    this.mAlertMessageResId = paramInt2;
  }

  public void setHelp(int paramInt)
  {
    this.mHelpUriResId = paramInt;
  }

  public void setHttpParamAccountName(String paramString)
  {
    this.mHttpParamAccountName = paramString;
  }

  public void setInlineText(int paramInt)
  {
    this.mInlineResId = paramInt;
  }

  public void setNeutralButtonAction(int paramInt1, int paramInt2)
  {
    this.mNeutralButtonActionUriId = paramInt1;
    this.mNeutralButtonResId = paramInt2;
  }

  public void setNotificationText(int paramInt1, int paramInt2)
  {
    this.mNotificationTitleResId = paramInt1;
    this.mNotificationMessageResId = paramInt2;
  }

  public void setPositiveButtonAction(int paramInt1, int paramInt2)
  {
    this.mPositivetButtonActionUriId = paramInt1;
    this.mPositiveButtonResId = paramInt2;
  }

  public static abstract interface OnErrorAlertDismissed
  {
    public abstract void onErrorAlertDismissed();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.ErrorInfo
 * JD-Core Version:    0.6.2
 */