package com.google.android.music.download.keepon;

import android.content.ComponentName;
import android.os.IBinder;
import com.google.android.music.utils.SafeServiceConnection;

public class KeeponSchedulingServiceConnection extends SafeServiceConnection
{
  private volatile IKeeponCallbackManager mKeeponCallbackManager;

  public IKeeponCallbackManager getKeeponCallbackManager()
  {
    return this.mKeeponCallbackManager;
  }

  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    IKeeponCallbackManager localIKeeponCallbackManager = IKeeponCallbackManager.Stub.asInterface(paramIBinder);
    this.mKeeponCallbackManager = localIKeeponCallbackManager;
  }

  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    this.mKeeponCallbackManager = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.keepon.KeeponSchedulingServiceConnection
 * JD-Core Version:    0.6.2
 */