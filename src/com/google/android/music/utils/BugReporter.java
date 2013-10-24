package com.google.android.music.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import java.util.List;

public class BugReporter
{
  private static final Intent BUG_REPORT_INTENT = new Intent("android.intent.action.BUG_REPORT");

  private static Bitmap getCurrentScreenshot(Activity paramActivity)
  {
    try
    {
      View localView = paramActivity.getWindow().getDecorView().getRootView();
      boolean bool = localView.isDrawingCacheEnabled();
      localView.setDrawingCacheEnabled(true);
      localBitmap = localView.getDrawingCache();
      if (localBitmap != null)
        localBitmap = resizeBitmap(localBitmap);
      if (!bool)
      {
        localView.setDrawingCacheEnabled(false);
        localView.destroyDrawingCache();
      }
      return localBitmap;
    }
    catch (Exception localException)
    {
      while (true)
        Bitmap localBitmap = null;
    }
  }

  public static boolean isGoogleFeedbackInstalled(Context paramContext)
  {
    Intent localIntent = BUG_REPORT_INTENT;
    return isSupportingServiceInstalled(paramContext, localIntent);
  }

  private static boolean isSupportingServiceInstalled(Context paramContext, Intent paramIntent)
  {
    if (!paramContext.getPackageManager().queryIntentServices(paramIntent, 65536).isEmpty());
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public static void launchGoogleFeedback(Activity paramActivity)
  {
    if (!isGoogleFeedbackInstalled(paramActivity))
    {
      int i = Log.w("BugReporter", "GoogleFeedback is not installed");
      return;
    }
    ServiceConnection local1 = new ServiceConnection()
    {
      public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
      {
        try
        {
          Parcel localParcel = Parcel.obtain();
          Bitmap localBitmap = BugReporter.getCurrentScreenshot(BugReporter.this);
          if (localBitmap != null)
            localBitmap.writeToParcel(localParcel, 0);
          boolean bool = paramAnonymousIBinder.transact(1, localParcel, null, 0);
          return;
        }
        catch (RemoteException localRemoteException)
        {
          int i = Log.e("BugReporter", "Error connecting to bug report service", localRemoteException);
          return;
        }
        finally
        {
          BugReporter.this.unbindService(this);
        }
      }

      public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
      {
      }
    };
    Intent localIntent = BUG_REPORT_INTENT;
    boolean bool = paramActivity.bindService(localIntent, local1, 1);
  }

  private static Bitmap resizeBitmap(Bitmap paramBitmap)
  {
    Bitmap.Config localConfig = Bitmap.Config.RGB_565;
    Bitmap localBitmap = paramBitmap.copy(localConfig, false);
    int i = localBitmap.getWidth();
    int j = localBitmap.getHeight();
    while (i * j * 2 > 1048576)
    {
      i /= 2;
      j /= 2;
    }
    int k = localBitmap.getWidth();
    if (i != k)
      localBitmap = Bitmap.createScaledBitmap(localBitmap, i, j, true);
    return localBitmap;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.utils.BugReporter
 * JD-Core Version:    0.6.2
 */