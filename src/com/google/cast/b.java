package com.google.cast;

import android.content.Context;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.support.v7.media.MediaRouteDescriptor;
import android.support.v7.media.MediaRouteDescriptor.Builder;
import android.support.v7.media.MediaRouteDiscoveryRequest;
import android.support.v7.media.MediaRouteProvider;
import android.support.v7.media.MediaRouteProvider.RouteController;
import android.support.v7.media.MediaRouteProviderDescriptor;
import android.support.v7.media.MediaRouteProviderDescriptor.Builder;
import android.support.v7.media.MediaRouteSelector;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

abstract class b extends MediaRouteProvider
{
  private static final Logger h = new Logger("BaseCastMediaRouteProvider");
  protected CastContext a;
  private DeviceManager b;
  private DeviceManager.Listener c;
  private a d;
  private List<IntentFilter> e;
  private Map<String, CastDevice> f;
  private Map<String, a> g;

  protected b(CastContext paramCastContext)
  {
    super(localContext);
    this.a = paramCastContext;
    CastContext localCastContext1 = this.a;
    DeviceManager localDeviceManager1 = new DeviceManager(localCastContext1);
    this.b = localDeviceManager1;
    DeviceManager.Listener local1 = new DeviceManager.Listener()
    {
      public void onDeviceOffline(CastDevice paramAnonymousCastDevice)
      {
        Map localMap = b.a(b.this);
        String str = paramAnonymousCastDevice.getDeviceId();
        b.a locala = (b.a)localMap.get(str);
        if (locala != null)
        {
          b.a.a(locala, false);
          return;
        }
        b.b(b.this, paramAnonymousCastDevice);
        b.b(b.this);
      }

      public void onDeviceOnline(CastDevice paramAnonymousCastDevice)
      {
        Map localMap = b.a(b.this);
        String str = paramAnonymousCastDevice.getDeviceId();
        b.a locala = (b.a)localMap.get(str);
        if (locala != null)
        {
          b.a.a(locala, true);
          return;
        }
        b.a(b.this, paramAnonymousCastDevice);
        b.b(b.this);
      }

      public void onScanStateChanged(int paramAnonymousInt)
      {
      }
    };
    this.c = local1;
    CastContext localCastContext2 = this.a;
    DeviceManager.Listener localListener = this.c;
    a locala1 = new a(localCastContext2, null, null, localListener);
    this.d = locala1;
    DeviceManager localDeviceManager2 = this.b;
    a locala2 = this.d;
    localDeviceManager2.a(locala2);
    HashMap localHashMap1 = new HashMap();
    this.f = localHashMap1;
    HashMap localHashMap2 = new HashMap();
    this.g = localHashMap2;
    ArrayList localArrayList = new ArrayList();
    this.e = localArrayList;
    b();
  }

  protected static final void a(IntentFilter paramIntentFilter, String paramString)
  {
    try
    {
      paramIntentFilter.addDataType(paramString);
      return;
    }
    catch (IntentFilter.MalformedMimeTypeException localMalformedMimeTypeException)
    {
      throw new RuntimeException(localMalformedMimeTypeException);
    }
  }

  private final void a(CastDevice paramCastDevice)
  {
    Map localMap = this.f;
    String str = paramCastDevice.getDeviceId();
    Object localObject = localMap.put(str, paramCastDevice);
  }

  private void b()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.f.values().iterator();
    while (localIterator.hasNext())
    {
      CastDevice localCastDevice = (CastDevice)localIterator.next();
      MediaRouteDescriptor localMediaRouteDescriptor = c(localCastDevice);
      boolean bool = localArrayList.add(localMediaRouteDescriptor);
    }
    MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = new MediaRouteProviderDescriptor.Builder().addRoutes(localArrayList).build();
    setDescriptor(localMediaRouteProviderDescriptor);
    Logger localLogger = h;
    Object[] arrayOfObject = new Object[1];
    Integer localInteger = Integer.valueOf(localArrayList.size());
    arrayOfObject[0] = localInteger;
    localLogger.d("published %d routes", arrayOfObject);
  }

  private final void b(CastDevice paramCastDevice)
  {
    Map localMap = this.f;
    String str = paramCastDevice.getDeviceId();
    Object localObject = localMap.remove(str);
  }

  private MediaRouteDescriptor c(CastDevice paramCastDevice)
  {
    int i = 0;
    Map localMap = this.g;
    String str1 = paramCastDevice.getDeviceId();
    a locala = (a)localMap.get(str1);
    if (locala != null)
      i = locala.a();
    for (boolean bool = locala.b(); ; bool = false)
    {
      String str2 = paramCastDevice.getModelName().replaceAll("(Eureka|Chromekey)( Dongle)?", "Chromecast");
      String str3 = paramCastDevice.getDeviceId();
      String str4 = paramCastDevice.getFriendlyName();
      MediaRouteDescriptor.Builder localBuilder = new MediaRouteDescriptor.Builder(str3, str4).setDescription(str2).setConnecting(bool).setVolumeHandling(1).setVolume(i).setVolumeMax(20).setPlaybackType(1);
      List localList = this.e;
      return localBuilder.addControlFilters(localList).build();
    }
  }

  protected abstract a a(CastDevice paramCastDevice, String paramString);

  protected final void a(IntentFilter paramIntentFilter)
  {
    boolean bool = this.e.add(paramIntentFilter);
  }

  public MediaRouteProvider.RouteController onCreateRouteController(String paramString)
  {
    CastDevice localCastDevice = (CastDevice)this.f.get(paramString);
    if (localCastDevice == null)
    {
      String str = "Unknown route ID: " + paramString;
      throw new IllegalArgumentException(str);
    }
    a locala = (a)this.g.get(paramString);
    if (locala == null)
    {
      locala = a(localCastDevice, paramString);
      Object localObject = this.g.put(paramString, locala);
    }
    return locala;
  }

  public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
  {
    Logger localLogger1 = h;
    Object[] arrayOfObject1 = new Object[1];
    arrayOfObject1[0] = paramMediaRouteDiscoveryRequest;
    localLogger1.d("in onDiscoveryRequestChanged: request=%s", arrayOfObject1);
    int i;
    int j;
    int n;
    Object localObject1;
    Object localObject2;
    Object localObject3;
    Object localObject4;
    label118: ArrayList localArrayList1;
    if (paramMediaRouteDiscoveryRequest != null)
    {
      List localList = paramMediaRouteDiscoveryRequest.getSelector().getControlCategories();
      if (localList.contains("com.google.cast.CATEGORY_CAST"))
      {
        i = 1;
        j = 1;
        int k = localList.size();
        int m = 0;
        n = i;
        localObject1 = null;
        localObject2 = null;
        if (m < k)
        {
          String str1 = (String)localList.get(m);
          if (str1.equals("android.media.intent.category.REMOTE_PLAYBACK"))
          {
            i = 1;
            localObject3 = localObject1;
            localObject4 = localObject2;
          }
          while (true)
          {
            m += 1;
            localObject2 = localObject4;
            localObject1 = localObject3;
            n = i;
            break;
            if ((str1.startsWith("com.google.cast.CATEGORY_CAST_APP_NAME:")) && (j != 0))
            {
              if (localObject2 != null)
                break label454;
              int i1 = "com.google.cast.CATEGORY_CAST_APP_NAME:".length();
              String str2 = str1.substring(i1);
              int i2 = n;
              localObject3 = localObject1;
              localObject4 = str2;
              i = i2;
            }
            else
            {
              if ((!str1.startsWith("com.google.cast.CATEGORY_CAST_APP_PROTOCOLS:")) || (j == 0) || (localObject1 != null))
                break label454;
              int i3 = "com.google.cast.CATEGORY_CAST_APP_PROTOCOLS:".length();
              String[] arrayOfString = TextUtils.split(str1.substring(i3), ",");
              int i4 = arrayOfString.length;
              localArrayList1 = new ArrayList(i4);
              int i5 = 0;
              while (true)
              {
                int i6 = arrayOfString.length;
                if (i5 >= i6)
                  break;
                if (!TextUtils.isEmpty(arrayOfString[i5]))
                {
                  String str3 = arrayOfString[i5];
                  boolean bool = localArrayList1.add(str3);
                }
                i5 += 1;
              }
              if (!localArrayList1.isEmpty())
                break label435;
              i = n;
              localObject4 = localObject2;
              localObject3 = 0;
            }
          }
        }
        if (this.d.a(localObject2, localObject1))
        {
          Logger localLogger2 = h;
          Object[] arrayOfObject2 = new Object[2];
          arrayOfObject2[0] = localObject2;
          arrayOfObject2[1] = localObject1;
          localLogger2.d("filter criteria changed (name: %s, protocols: %s); flushing routes", arrayOfObject2);
          this.f.clear();
          b();
        }
      }
    }
    while (true)
    {
      if (n != 0)
      {
        Logger localLogger3 = h;
        Object[] arrayOfObject3 = new Object[0];
        localLogger3.d("starting a scan", arrayOfObject3);
        this.b.a();
        return;
      }
      this.b.b();
      return;
      label435: localObject4 = localObject2;
      ArrayList localArrayList2 = localArrayList1;
      i = n;
      localObject3 = localArrayList2;
      break label118;
      label454: i = n;
      localObject3 = localObject1;
      localObject4 = localObject2;
      break label118;
      i = 0;
      j = 0;
      break;
      n = 0;
    }
  }

  abstract class a extends MediaRouteProvider.RouteController
  {
    protected final CastDevice a;
    protected final String b;
    private int d;
    private boolean e;
    private boolean f;
    private boolean g;

    protected a(CastDevice paramString, String arg3)
    {
      this.a = paramString;
      Object localObject;
      this.b = localObject;
      this.d = 0;
      this.e = false;
      this.f = true;
      this.g = false;
    }

    private void a(boolean paramBoolean)
    {
      if (this.f != paramBoolean)
        return;
      this.f = paramBoolean;
      if (!c())
        return;
      b.b(b.this);
    }

    private boolean b(boolean paramBoolean)
    {
      if (this.g != paramBoolean)
        this.g = paramBoolean;
      for (boolean bool = c(); ; bool = false)
        return bool;
    }

    private boolean c()
    {
      boolean bool1 = true;
      boolean bool2;
      if ((this.f) || (this.g))
      {
        bool2 = true;
        Map localMap = b.c(b.this);
        String str = this.a.getDeviceId();
        boolean bool3 = localMap.containsKey(str);
        if (bool2 == bool3)
          break label103;
        if (!bool2)
          break label81;
        b localb1 = b.this;
        CastDevice localCastDevice1 = this.a;
        b.a(localb1, localCastDevice1);
      }
      while (true)
      {
        return bool1;
        bool2 = false;
        break;
        label81: b localb2 = b.this;
        CastDevice localCastDevice2 = this.a;
        b.b(localb2, localCastDevice2);
        continue;
        label103: bool1 = false;
      }
    }

    final int a()
    {
      return this.d;
    }

    protected final void a(double paramDouble)
    {
      int i = (int)Math.round(20.0D * paramDouble);
      Logger localLogger = b.a();
      Object[] arrayOfObject = new Object[2];
      Integer localInteger1 = Integer.valueOf(i);
      arrayOfObject[0] = localInteger1;
      Integer localInteger2 = Integer.valueOf(this.d);
      arrayOfObject[1] = localInteger2;
      localLogger.d("setCurrentVolume to %d, was %d", arrayOfObject);
      int j = this.d;
      if (i != j)
        return;
      this.d = i;
      b.b(b.this);
    }

    protected final void a(int paramInt)
    {
      boolean bool1 = false;
      int i = 1;
      boolean bool2;
      if (paramInt == i)
      {
        bool2 = true;
        boolean bool3 = this.e;
        if (bool2 == bool3)
          break label79;
        this.e = bool2;
      }
      label79: for (int j = 1; ; j = 0)
      {
        if ((paramInt == 1) || (paramInt == 2))
          bool1 = true;
        if (b(bool1));
        while (true)
        {
          if (i == 0)
            return;
          b.b(b.this);
          return;
          bool2 = false;
          break;
          i = j;
        }
      }
    }

    final boolean b()
    {
      return this.e;
    }

    public void onRelease()
    {
      Logger localLogger = b.a();
      Object[] arrayOfObject = new Object[0];
      localLogger.d("Controller released", arrayOfObject);
      if (b(false))
        b.b(b.this);
      Map localMap = b.a(b.this);
      String str = this.a.getDeviceId();
      Object localObject = localMap.remove(str);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.b
 * JD-Core Version:    0.6.2
 */