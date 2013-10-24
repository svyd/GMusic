package com.google.android.music.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public abstract class SafeServiceConnection
  implements ServiceConnection
{
  private ServiceConnectionImp mServiceConnectionImp;

  public SafeServiceConnection()
  {
    ServiceConnectionImp localServiceConnectionImp = new ServiceConnectionImp();
    this.mServiceConnectionImp = localServiceConnectionImp;
  }

  public boolean bindService(Context paramContext, Intent paramIntent, int paramInt)
  {
    return this.mServiceConnectionImp.bindService(paramContext, paramIntent, paramInt);
  }

  public void unbindService(Context paramContext)
  {
    this.mServiceConnectionImp.unbindService(paramContext);
  }

  private class ServiceConnectionImp
    implements ServiceConnection
  {
    private Context mContextForDelayedUnbind;
    private int mFlags;
    private Intent mService;
    private int mState = 0;

    public ServiceConnectionImp()
    {
    }

    private void reportBadState()
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Unexpected method for state ");
      int i = this.mState;
      String str = i;
      throw new IllegalStateException(str);
    }

    public boolean bindService(Context paramContext, Intent paramIntent, int paramInt)
    {
      boolean bool = true;
      while (true)
      {
        try
        {
          switch (this.mState)
          {
          case 1:
          case 2:
          default:
            reportBadState();
            bool = false;
            return bool;
          case 0:
            if (this.mService == null)
            {
              this.mService = paramIntent;
              this.mFlags = paramInt;
              this.mState = 1;
              bool = paramContext.bindService(paramIntent, this, paramInt);
              if (!bool)
                this.mState = 0;
              continue;
            }
            break;
          case 3:
          }
        }
        finally
        {
        }
        if (!this.mService.filterEquals(paramIntent))
          throw new RuntimeException("service must be equivalent for every call to bindService");
        if (this.mFlags != paramInt)
        {
          throw new RuntimeException("flags must be equivalent for every call to bindService");
          this.mState = 1;
          this.mContextForDelayedUnbind = null;
        }
      }
    }

    public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
    {
      while (true)
      {
        Object localObject1;
        try
        {
          switch (this.mState)
          {
          default:
            reportBadState();
          case 2:
            SafeServiceConnection.this.onServiceConnected(paramComponentName, paramIBinder);
            return;
          case 1:
            localObject1 = null;
            this.mState = localObject1;
            continue;
          case 3:
          }
        }
        finally
        {
        }
        try
        {
          this.mContextForDelayedUnbind.unbindService(this);
          localObject1 = null;
          this.mContextForDelayedUnbind = localObject1;
          this.mState = 0;
        }
        catch (RuntimeException localRuntimeException)
        {
          while (true)
            int i = Log.w("SafeServiceConnection", "Unable to perform delayed unbind.", localRuntimeException);
        }
      }
    }

    public void onServiceDisconnected(ComponentName paramComponentName)
    {
      SafeServiceConnection.this.onServiceDisconnected(paramComponentName);
    }

    public void unbindService(Context paramContext)
    {
      while (true)
      {
        try
        {
          switch (this.mState)
          {
          default:
            reportBadState();
            return;
          case 1:
            this.mState = 3;
            this.mContextForDelayedUnbind = paramContext;
            continue;
          case 2:
          }
        }
        finally
        {
        }
        int i = 0;
        this.mState = i;
        paramContext.unbindService(this);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.SafeServiceConnection
 * JD-Core Version:    0.6.2
 */