package android.support.v7.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

final class RegisteredMediaRouteProviderWatcher
{
  private final Context mContext;
  private final PackageManager mPackageManager;
  private final ArrayList<RegisteredMediaRouteProvider> mProviders;
  private final MediaRouter mRouter;

  public RegisteredMediaRouteProviderWatcher(Context paramContext, MediaRouter paramMediaRouter)
  {
    ArrayList localArrayList = new ArrayList();
    this.mProviders = localArrayList;
    this.mContext = paramContext;
    this.mRouter = paramMediaRouter;
    PackageManager localPackageManager = paramContext.getPackageManager();
    this.mPackageManager = localPackageManager;
  }

  private int findProvider(String paramString1, String paramString2)
  {
    int i = this.mProviders.size();
    int j = 0;
    if (j < i)
      if (!((RegisteredMediaRouteProvider)this.mProviders.get(j)).hasComponentName(paramString1, paramString2));
    while (true)
    {
      return j;
      j += 1;
      break;
      j = -1;
    }
  }

  private void scanPackages()
  {
    int i = 0;
    Intent localIntent = new Intent("android.media.MediaRouteProviderService");
    Iterator localIterator = this.mPackageManager.queryIntentServices(localIntent, 0).iterator();
    while (localIterator.hasNext())
    {
      ServiceInfo localServiceInfo = ((ResolveInfo)localIterator.next()).serviceInfo;
      if (localServiceInfo != null)
      {
        String str1 = localServiceInfo.packageName;
        String str2 = localServiceInfo.name;
        int j = findProvider(str1, str2);
        if (j < 0)
        {
          Context localContext = this.mContext;
          String str3 = localServiceInfo.packageName;
          String str4 = localServiceInfo.name;
          ComponentName localComponentName = new ComponentName(str3, str4);
          RegisteredMediaRouteProvider localRegisteredMediaRouteProvider1 = new RegisteredMediaRouteProvider(localContext, localComponentName);
          localRegisteredMediaRouteProvider1.start();
          ArrayList localArrayList1 = this.mProviders;
          int k = i + 1;
          localArrayList1.add(i, localRegisteredMediaRouteProvider1);
          this.mRouter.addProvider(localRegisteredMediaRouteProvider1);
          i = k;
        }
        else if (j >= i)
        {
          ((RegisteredMediaRouteProvider)this.mProviders.get(j)).rebindIfDisconnected();
          ArrayList localArrayList2 = this.mProviders;
          int m = i + 1;
          Collections.swap(localArrayList2, j, i);
          i = m;
        }
      }
    }
    int n = this.mProviders.size();
    if (i >= n)
      return;
    int i1 = this.mProviders.size() + -1;
    while (true)
    {
      if (i1 < i)
        return;
      RegisteredMediaRouteProvider localRegisteredMediaRouteProvider2 = (RegisteredMediaRouteProvider)this.mProviders.get(i1);
      this.mRouter.removeProvider(localRegisteredMediaRouteProvider2);
      boolean bool = this.mProviders.remove(localRegisteredMediaRouteProvider2);
      localRegisteredMediaRouteProvider2.stop();
      i1 += -1;
    }
  }

  public void start()
  {
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("android.intent.action.PACKAGE_ADDED");
    localIntentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
    localIntentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
    Context localContext = this.mContext;
    BroadcastReceiver local1 = new BroadcastReceiver()
    {
      public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
      {
        RegisteredMediaRouteProviderWatcher.this.scanPackages();
      }
    };
    Intent localIntent = localContext.registerReceiver(local1, localIntentFilter);
    scanPackages();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.RegisteredMediaRouteProviderWatcher
 * JD-Core Version:    0.6.2
 */