package com.google.android.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.google.android.music.tutorial.SignupStatus;

public class AccountsBroadcastReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    SignupStatus.launchVerificationCheck(paramContext);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.AccountsBroadcastReceiver
 * JD-Core Version:    0.6.2
 */