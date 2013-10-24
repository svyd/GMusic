package com.google.android.music.sync.google;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ConfigAlarmBroadcastReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    Bundle localBundle = new Bundle();
    localBundle.putString("configAlarm", "");
    ContentResolver.requestSync(null, "com.google.android.music.MusicContent", localBundle);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.ConfigAlarmBroadcastReceiver
 * JD-Core Version:    0.6.2
 */