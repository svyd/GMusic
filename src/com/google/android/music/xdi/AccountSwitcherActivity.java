package com.google.android.music.xdi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.tutorial.SignupStatus;
import java.util.ArrayList;
import java.util.Collections;

public class AccountSwitcherActivity extends Activity
{
  public static String createIntentUri(Context paramContext)
  {
    return new Intent(paramContext, AccountSwitcherActivity.class).toUri(1);
  }

  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    MusicPreferences localMusicPreferences;
    if ((paramInt2 == -1) && (paramInt1 == 1))
      localMusicPreferences = MusicPreferences.getMusicPreferences(this, this);
    try
    {
      Account localAccount1 = localMusicPreferences.getSyncAccount();
      String str1 = paramIntent.getStringExtra("authAccount");
      if (!TextUtils.isEmpty(str1))
        if (localAccount1 != null)
        {
          String str2 = localAccount1.name;
          if (str1.equals(str2));
        }
        else
        {
          Account localAccount2 = localMusicPreferences.resolveAccount(str1);
          if (localAccount2 == null)
            break label104;
          localMusicPreferences.setStreamingAccount(localAccount2);
          XdiContentProvider.notifyAccountChanged(this);
        }
      while (true)
      {
        MusicPreferences.releaseMusicPreferences(this);
        finish();
        return;
        label104: String str3 = "Could not resolve account for " + str1;
        Log.w("MusicXdi", str3);
      }
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(this);
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ArrayList localArrayList = new ArrayList();
    Account[] arrayOfAccount = SignupStatus.getAllAccounts(this);
    boolean bool = Collections.addAll(localArrayList, arrayOfAccount);
    Account localAccount = XdiUtils.getSelectedAccount(this);
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "com.google";
    String[] arrayOfString2 = null;
    Bundle localBundle = null;
    Intent localIntent = AccountManager.newChooseAccountIntent(localAccount, localArrayList, arrayOfString1, true, null, "mail", arrayOfString2, localBundle);
    startActivityForResult(localIntent, 1);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.xdi.AccountSwitcherActivity
 * JD-Core Version:    0.6.2
 */