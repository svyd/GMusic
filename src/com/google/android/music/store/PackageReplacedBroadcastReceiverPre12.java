package com.google.android.music.store;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PackageReplacedBroadcastReceiverPre12 extends PackageReplacedBroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramContext.getPackageName();
    if (paramIntent.getData() == null)
      return;
    if (paramIntent.getData().getSchemeSpecificPart() == null)
      return;
    if (!paramIntent.getData().getSchemeSpecificPart().equals(str))
      return;
    startUpgrade(paramContext);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.store.PackageReplacedBroadcastReceiverPre12
 * JD-Core Version:    0.6.2
 */