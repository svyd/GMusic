package com.google.android.music.sync.google;

import android.accounts.Account;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.gsf.Gservices;
import com.google.android.music.MusicGservicesKeys;
import com.google.android.music.preferences.MusicPreferences;

public class TickleReceiver extends BroadcastReceiver
{
  private Context mContext;

  private void scheduleConfigResync()
  {
    AlarmManager localAlarmManager = (AlarmManager)this.mContext.getSystemService("alarm");
    Context localContext = this.mContext;
    Intent localIntent = new Intent("com.google.android.music.sync.TICKLE_CONFIG_ALARM");
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(localContext, 0, localIntent, 0);
    ContentResolver localContentResolver = this.mContext.getContentResolver();
    String str = MusicGservicesKeys.MUSIC_CONFIG_SYNC_ALARM_MIN;
    long l1 = Gservices.getLong(localContentResolver, str, 15L) * 60L * 1000L;
    if (l1 == 0L)
      return;
    long l2 = System.currentTimeMillis() + l1;
    localAlarmManager.set(1, l2, localPendingIntent);
  }

  public void onReceive(Context paramContext, Intent paramIntent)
  {
    this.mContext = paramContext;
    String str1 = paramIntent.getStringExtra("account");
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, this);
    Account localAccount2;
    try
    {
      Account localAccount1 = localMusicPreferences.getSyncAccount();
      localAccount2 = localAccount1;
      MusicPreferences.releaseMusicPreferences(this);
      if ((localAccount2 == null) || (TextUtils.isEmpty(str1)) || (!localAccount2.name.equalsIgnoreCase(str1)))
      {
        if (!Log.isLoggable("MusicSyncAdapter", 2))
          return;
        String str2 = "Not doing sync.  Given account: " + str1 + " is not the selected account: " + localAccount2 + " actual: ";
        int i = Log.i("MusicSyncAdapter", str2);
        return;
      }
    }
    finally
    {
      MusicPreferences.releaseMusicPreferences(this);
    }
    if (ContentResolver.getIsSyncable(localAccount2, "com.google.android.music.MusicContent") > 0)
    {
      String str3 = "Running sync for account: " + str1;
      int j = Log.i("MusicSyncAdapter", str3);
      Bundle localBundle = new Bundle();
      ContentResolver.requestSync(localAccount2, "com.google.android.music.MusicContent", localBundle);
    }
    while (true)
    {
      scheduleConfigResync();
      return;
      String str4 = "Account: " + str1 + " not syncable";
      int k = Log.i("MusicSyncAdapter", str4);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.TickleReceiver
 * JD-Core Version:    0.6.2
 */