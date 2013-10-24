package com.google.cast;

import android.net.Uri;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

class a
  implements DeviceManager.Listener
{
  private static Logger a = new Logger("ApplicationSupportFilterListener");
  private CastContext b;
  private DeviceManager.Listener c;
  private String d;
  private List<String> e;
  private LinkedList<CastDevice> f;
  private LinkedList<NetworkTask> g;

  public a(CastContext paramCastContext, String paramString, List<String> paramList, DeviceManager.Listener paramListener)
  {
    this.b = paramCastContext;
    this.d = paramString;
    this.c = paramListener;
    if ((paramList == null) || (paramList.isEmpty()))
      paramList = null;
    this.e = paramList;
    LinkedList localLinkedList1 = new LinkedList();
    this.f = localLinkedList1;
    LinkedList localLinkedList2 = new LinkedList();
    this.g = localLinkedList2;
  }

  private void a(final CastDevice paramCastDevice)
  {
    Uri localUri = paramCastDevice.getApplicationUrl();
    if (localUri == null)
      return;
    CastContext localCastContext = this.b;
    String str = this.d;
    final g localg = new g(localCastContext, localUri, str);
    NetworkRequest[] arrayOfNetworkRequest = new NetworkRequest[1];
    arrayOfNetworkRequest[0] = localg;
    final NetworkTask localNetworkTask = new NetworkTask(arrayOfNetworkRequest);
    NetworkTask.Listener local1 = new NetworkTask.Listener()
    {
      public void onTaskCancelled()
      {
        LinkedList localLinkedList = a.d(a.this);
        NetworkTask localNetworkTask = localNetworkTask;
        boolean bool = localLinkedList.remove(localNetworkTask);
      }

      public void onTaskCompleted()
      {
        boolean bool1 = true;
        ApplicationMetadata localApplicationMetadata;
        List localList;
        if (a.a(a.this) != null)
        {
          localApplicationMetadata = localg.a();
          if (localApplicationMetadata == null)
            break label125;
          localList = a.a(a.this);
        }
        label125: for (bool1 = localApplicationMetadata.areProtocolsSupported(localList); ; bool1 = false)
        {
          if (bool1)
          {
            LinkedList localLinkedList1 = a.b(a.this);
            CastDevice localCastDevice1 = paramCastDevice;
            boolean bool2 = localLinkedList1.add(localCastDevice1);
            if (a.c(a.this) != null)
            {
              DeviceManager.Listener localListener = a.c(a.this);
              CastDevice localCastDevice2 = paramCastDevice;
              localListener.onDeviceOnline(localCastDevice2);
            }
          }
          LinkedList localLinkedList2 = a.d(a.this);
          NetworkTask localNetworkTask = localNetworkTask;
          boolean bool3 = localLinkedList2.remove(localNetworkTask);
          return;
        }
      }

      public void onTaskFailed(int paramAnonymousInt)
      {
        Logger localLogger = a.a();
        Object[] arrayOfObject = new Object[1];
        String str = a.e(a.this);
        arrayOfObject[0] = str;
        localLogger.d("Device does not support app %s", arrayOfObject);
        LinkedList localLinkedList = a.d(a.this);
        NetworkTask localNetworkTask = localNetworkTask;
        boolean bool = localLinkedList.remove(localNetworkTask);
      }
    };
    localNetworkTask.setListener(local1);
    boolean bool = this.g.add(localNetworkTask);
    localNetworkTask.execute();
  }

  public boolean a(String paramString, List<String> paramList)
  {
    Object localObject = null;
    boolean bool1 = true;
    int j;
    int k;
    if (paramString == null)
    {
      j = 1;
      if (this.d != null)
        break label140;
      k = 1;
      label22: if ((j == k) && ((this.d == null) || (this.d.equals(paramString))))
        break label256;
      this.d = paramString;
    }
    label256: for (boolean bool2 = true; ; bool2 = false)
    {
      if ((paramList != null) && (paramList.isEmpty()))
        paramList = null;
      int m;
      if (paramList == null)
      {
        m = 1;
        label77: int i;
        if (this.e == null)
          i = 1;
        if (m == i)
          break label152;
        this.e = paramList;
      }
      while (true)
      {
        label97: if (bool1)
        {
          Iterator localIterator = this.g.iterator();
          while (true)
            if (localIterator.hasNext())
            {
              ((NetworkTask)localIterator.next()).cancel();
              continue;
              j = 0;
              break;
              k = 0;
              break label22;
              m = 0;
              break label77;
              if (paramList == null)
                break label249;
              int n = paramList.size();
              int i1 = this.e.size();
              if (n != i1)
              {
                this.e = paramList;
                break label97;
              }
              localIterator = paramList.iterator();
              String str;
              do
              {
                if (!localIterator.hasNext())
                  break;
                str = (String)localIterator.next();
              }
              while (this.e.contains(str));
              this.e = paramList;
              break label97;
            }
          this.f.clear();
        }
        label140: label152: return bool1;
        label249: bool1 = bool2;
      }
    }
  }

  public void onDeviceOffline(CastDevice paramCastDevice)
  {
    if (!this.f.remove(paramCastDevice))
      return;
    if (this.c == null)
      return;
    this.c.onDeviceOffline(paramCastDevice);
  }

  public void onDeviceOnline(CastDevice paramCastDevice)
  {
    a(paramCastDevice);
  }

  public void onScanStateChanged(int paramInt)
  {
    if ((paramInt == 0) || (paramInt == 2))
    {
      Iterator localIterator = this.g.iterator();
      while (localIterator.hasNext())
        ((NetworkTask)localIterator.next()).cancel();
    }
    if (this.c == null)
      return;
    this.c.onScanStateChanged(paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.a
 * JD-Core Version:    0.6.2
 */