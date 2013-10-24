package com.google.android.music.store;

import android.content.Context;
import android.content.Intent;

public class PackageReplacedBroadcastReceiverPost12 extends PackageReplacedBroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    startUpgrade(paramContext);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.PackageReplacedBroadcastReceiverPost12
 * JD-Core Version:    0.6.2
 */