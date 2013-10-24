package android.support.v7.media;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public abstract class MediaRouteProviderService extends Service
{
  private static final boolean DEBUG = Log.isLoggable("MediaRouteProviderSrv", 3);
  private final ArrayList<ClientRecord> mClients;
  private MediaRouteDiscoveryRequest mCompositeDiscoveryRequest;
  private final PrivateHandler mPrivateHandler;
  private MediaRouteProvider mProvider;
  private final ProviderCallback mProviderCallback;
  private final ReceiveHandler mReceiveHandler;
  private final Messenger mReceiveMessenger;

  public MediaRouteProviderService()
  {
    ArrayList localArrayList = new ArrayList();
    this.mClients = localArrayList;
    ReceiveHandler localReceiveHandler1 = new ReceiveHandler(this);
    this.mReceiveHandler = localReceiveHandler1;
    ReceiveHandler localReceiveHandler2 = this.mReceiveHandler;
    Messenger localMessenger = new Messenger(localReceiveHandler2);
    this.mReceiveMessenger = localMessenger;
    PrivateHandler localPrivateHandler = new PrivateHandler(null);
    this.mPrivateHandler = localPrivateHandler;
    ProviderCallback localProviderCallback = new ProviderCallback(null);
    this.mProviderCallback = localProviderCallback;
  }

  private int findClient(Messenger paramMessenger)
  {
    int i = this.mClients.size();
    int j = 0;
    if (j < i)
      if (!((ClientRecord)this.mClients.get(j)).hasMessenger(paramMessenger));
    while (true)
    {
      return j;
      j += 1;
      break;
      j = -1;
    }
  }

  private ClientRecord getClient(Messenger paramMessenger)
  {
    int i = findClient(paramMessenger);
    if (i >= 0);
    for (ClientRecord localClientRecord = (ClientRecord)this.mClients.get(i); ; localClientRecord = null)
      return localClientRecord;
  }

  private static String getClientId(Messenger paramMessenger)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("Client connection ");
    String str = paramMessenger.getBinder().toString();
    return str;
  }

  static boolean isValidRemoteMessenger(Messenger paramMessenger)
  {
    boolean bool = false;
    if (paramMessenger != null);
    try
    {
      IBinder localIBinder = paramMessenger.getBinder();
      if (localIBinder != null)
        bool = true;
      label17: return bool;
    }
    catch (NullPointerException localNullPointerException)
    {
      break label17;
    }
  }

  private void onBinderDied(Messenger paramMessenger)
  {
    int i = findClient(paramMessenger);
    if (i < 0)
      return;
    ClientRecord localClientRecord = (ClientRecord)this.mClients.remove(i);
    if (DEBUG)
    {
      String str = localClientRecord + ": Binder died";
      int j = Log.d("MediaRouteProviderSrv", str);
    }
    localClientRecord.dispose();
  }

  private boolean onCreateRouteController(Messenger paramMessenger, int paramInt1, int paramInt2, String paramString)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if ((localClientRecord != null) && (localClientRecord.createRouteController(paramString, paramInt2)))
    {
      if (DEBUG)
      {
        String str = localClientRecord + ": Route controller created" + ", controllerId=" + paramInt2 + ", routeId=" + paramString;
        int i = Log.d("MediaRouteProviderSrv", str);
      }
      sendGenericSuccess(paramMessenger, paramInt1);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onRegisterClient(Messenger paramMessenger, int paramInt1, int paramInt2)
  {
    int i = 1;
    Bundle localBundle;
    if ((paramInt2 >= i) && (findClient(paramMessenger) < 0))
    {
      ClientRecord localClientRecord = new ClientRecord(paramMessenger, paramInt2);
      if (localClientRecord.register())
      {
        boolean bool = this.mClients.add(localClientRecord);
        if (DEBUG)
        {
          String str = localClientRecord + ": Registered, version=" + paramInt2;
          int j = Log.d("MediaRouteProviderSrv", str);
        }
        if (paramInt1 != 0)
        {
          MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = this.mProvider.getDescriptor();
          int k = 2;
          if (localMediaRouteProviderDescriptor == null)
            break label139;
          localBundle = localMediaRouteProviderDescriptor.asBundle();
          Messenger localMessenger = paramMessenger;
          int m = paramInt1;
          sendReply(localMessenger, k, m, 1, localBundle, null);
        }
      }
    }
    while (true)
    {
      return i;
      label139: localBundle = null;
      break;
      i = 0;
    }
  }

  private boolean onReleaseRouteController(Messenger paramMessenger, int paramInt1, int paramInt2)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if ((localClientRecord != null) && (localClientRecord.releaseRouteController(paramInt2)))
    {
      if (DEBUG)
      {
        String str = localClientRecord + ": Route controller released" + ", controllerId=" + paramInt2;
        int i = Log.d("MediaRouteProviderSrv", str);
      }
      sendGenericSuccess(paramMessenger, paramInt1);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onRouteControlRequest(Messenger paramMessenger, int paramInt1, int paramInt2, Intent paramIntent)
  {
    final ClientRecord localClientRecord = getClient(paramMessenger);
    if (localClientRecord != null)
    {
      MediaRouteProvider.RouteController localRouteController = localClientRecord.getRouteController(paramInt2);
      if (localRouteController != null)
      {
        MediaRouter.ControlRequestCallback local1 = null;
        if (paramInt1 != 0)
        {
          MediaRouteProviderService localMediaRouteProviderService = this;
          final int i = paramInt2;
          final Intent localIntent = paramIntent;
          final Messenger localMessenger = paramMessenger;
          final int j = paramInt1;
          local1 = new MediaRouter.ControlRequestCallback()
          {
            public void onError(String paramAnonymousString, Bundle paramAnonymousBundle)
            {
              if (MediaRouteProviderService.DEBUG)
              {
                StringBuilder localStringBuilder1 = new StringBuilder();
                MediaRouteProviderService.ClientRecord localClientRecord = localClientRecord;
                StringBuilder localStringBuilder2 = localStringBuilder1.append(localClientRecord).append(": Route control request failed").append(", controllerId=");
                int i = i;
                StringBuilder localStringBuilder3 = localStringBuilder2.append(i).append(", intent=");
                Intent localIntent = localIntent;
                String str = localIntent + ", error=" + paramAnonymousString + ", data=" + paramAnonymousBundle;
                int j = Log.d("MediaRouteProviderSrv", str);
              }
              MediaRouteProviderService localMediaRouteProviderService = MediaRouteProviderService.this;
              Messenger localMessenger1 = localMessenger;
              if (localMediaRouteProviderService.findClient(localMessenger1) < 0)
                return;
              if (paramAnonymousString != null)
              {
                Bundle localBundle1 = new Bundle();
                localBundle1.putString("error", paramAnonymousString);
                Messenger localMessenger2 = localMessenger;
                int k = j;
                Bundle localBundle2 = paramAnonymousBundle;
                MediaRouteProviderService.sendReply(localMessenger2, 4, k, 0, localBundle2, localBundle1);
                return;
              }
              Messenger localMessenger3 = localMessenger;
              int m = j;
              int n = 4;
              int i1 = 0;
              Bundle localBundle3 = paramAnonymousBundle;
              MediaRouteProviderService.sendReply(localMessenger3, n, m, i1, localBundle3, null);
            }

            public void onResult(Bundle paramAnonymousBundle)
            {
              if (MediaRouteProviderService.DEBUG)
              {
                StringBuilder localStringBuilder1 = new StringBuilder();
                MediaRouteProviderService.ClientRecord localClientRecord = localClientRecord;
                StringBuilder localStringBuilder2 = localStringBuilder1.append(localClientRecord).append(": Route control request succeeded").append(", controllerId=");
                int i = i;
                StringBuilder localStringBuilder3 = localStringBuilder2.append(i).append(", intent=");
                Intent localIntent = localIntent;
                String str = localIntent + ", data=" + paramAnonymousBundle;
                int j = Log.d("MediaRouteProviderSrv", str);
              }
              MediaRouteProviderService localMediaRouteProviderService = MediaRouteProviderService.this;
              Messenger localMessenger1 = localMessenger;
              if (localMediaRouteProviderService.findClient(localMessenger1) < 0)
                return;
              Messenger localMessenger2 = localMessenger;
              int k = j;
              Bundle localBundle = paramAnonymousBundle;
              MediaRouteProviderService.sendReply(localMessenger2, 3, k, 0, localBundle, null);
            }
          };
        }
        if (localRouteController.onControlRequest(paramIntent, local1))
          if (DEBUG)
          {
            String str = localClientRecord + ": Route control request delivered" + ", controllerId=" + paramInt2 + ", intent=" + paramIntent;
            int k = Log.d("MediaRouteProviderSrv", str);
          }
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onSelectRoute(Messenger paramMessenger, int paramInt1, int paramInt2)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if (localClientRecord != null)
    {
      MediaRouteProvider.RouteController localRouteController = localClientRecord.getRouteController(paramInt2);
      if (localRouteController != null)
      {
        localRouteController.onSelect();
        if (DEBUG)
        {
          String str = localClientRecord + ": Route selected" + ", controllerId=" + paramInt2;
          int i = Log.d("MediaRouteProviderSrv", str);
        }
        sendGenericSuccess(paramMessenger, paramInt1);
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onSetDiscoveryRequest(Messenger paramMessenger, int paramInt, MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if (localClientRecord != null)
    {
      boolean bool1 = localClientRecord.setDiscoveryRequest(paramMediaRouteDiscoveryRequest);
      if (DEBUG)
      {
        StringBuilder localStringBuilder = new StringBuilder().append(localClientRecord).append(": Set discovery request, request=").append(paramMediaRouteDiscoveryRequest).append(", actuallyChanged=").append(bool1).append(", compositeDiscoveryRequest=");
        MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = this.mCompositeDiscoveryRequest;
        String str = localMediaRouteDiscoveryRequest;
        int i = Log.d("MediaRouteProviderSrv", str);
      }
      sendGenericSuccess(paramMessenger, paramInt);
    }
    for (boolean bool2 = true; ; bool2 = false)
      return bool2;
  }

  private boolean onSetRouteVolume(Messenger paramMessenger, int paramInt1, int paramInt2, int paramInt3)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if (localClientRecord != null)
    {
      MediaRouteProvider.RouteController localRouteController = localClientRecord.getRouteController(paramInt2);
      if (localRouteController != null)
      {
        localRouteController.onSetVolume(paramInt3);
        if (DEBUG)
        {
          String str = localClientRecord + ": Route volume changed" + ", controllerId=" + paramInt2 + ", volume=" + paramInt3;
          int i = Log.d("MediaRouteProviderSrv", str);
        }
        sendGenericSuccess(paramMessenger, paramInt1);
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onUnregisterClient(Messenger paramMessenger, int paramInt)
  {
    int i = findClient(paramMessenger);
    if (i >= 0)
    {
      ClientRecord localClientRecord = (ClientRecord)this.mClients.remove(i);
      if (DEBUG)
      {
        String str = localClientRecord + ": Unregistered";
        int j = Log.d("MediaRouteProviderSrv", str);
      }
      localClientRecord.dispose();
      sendGenericSuccess(paramMessenger, paramInt);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onUnselectRoute(Messenger paramMessenger, int paramInt1, int paramInt2)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if (localClientRecord != null)
    {
      MediaRouteProvider.RouteController localRouteController = localClientRecord.getRouteController(paramInt2);
      if (localRouteController != null)
      {
        localRouteController.onUnselect();
        if (DEBUG)
        {
          String str = localClientRecord + ": Route unselected" + ", controllerId=" + paramInt2;
          int i = Log.d("MediaRouteProviderSrv", str);
        }
        sendGenericSuccess(paramMessenger, paramInt1);
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private boolean onUpdateRouteVolume(Messenger paramMessenger, int paramInt1, int paramInt2, int paramInt3)
  {
    ClientRecord localClientRecord = getClient(paramMessenger);
    if (localClientRecord != null)
    {
      MediaRouteProvider.RouteController localRouteController = localClientRecord.getRouteController(paramInt2);
      if (localRouteController != null)
      {
        localRouteController.onUpdateVolume(paramInt3);
        if (DEBUG)
        {
          String str = localClientRecord + ": Route volume updated" + ", controllerId=" + paramInt2 + ", delta=" + paramInt3;
          int i = Log.d("MediaRouteProviderSrv", str);
        }
        sendGenericSuccess(paramMessenger, paramInt1);
      }
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  private void sendDescriptorChanged(MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
  {
    if (paramMediaRouteProviderDescriptor != null);
    for (Bundle localBundle = paramMediaRouteProviderDescriptor.asBundle(); ; localBundle = null)
    {
      int i = this.mClients.size();
      int j = 0;
      while (true)
      {
        if (j >= i)
          return;
        ClientRecord localClientRecord = (ClientRecord)this.mClients.get(j);
        Messenger localMessenger = localClientRecord.mMessenger;
        int k = 0;
        sendReply(localMessenger, 5, 0, k, localBundle, null);
        if (DEBUG)
        {
          String str = localClientRecord + ": Sent descriptor change event, descriptor=" + paramMediaRouteProviderDescriptor;
          int m = Log.d("MediaRouteProviderSrv", str);
        }
        j += 1;
      }
    }
  }

  private static void sendGenericFailure(Messenger paramMessenger, int paramInt)
  {
    if (paramInt == 0)
      return;
    Messenger localMessenger = paramMessenger;
    int i = paramInt;
    int j = 0;
    Bundle localBundle = null;
    sendReply(localMessenger, 0, i, j, null, localBundle);
  }

  private static void sendGenericSuccess(Messenger paramMessenger, int paramInt)
  {
    if (paramInt == 0)
      return;
    Messenger localMessenger = paramMessenger;
    int i = paramInt;
    Bundle localBundle = null;
    sendReply(localMessenger, 1, i, 0, null, localBundle);
  }

  private static void sendReply(Messenger paramMessenger, int paramInt1, int paramInt2, int paramInt3, Object paramObject, Bundle paramBundle)
  {
    Message localMessage = Message.obtain();
    localMessage.what = paramInt1;
    localMessage.arg1 = paramInt2;
    localMessage.arg2 = paramInt3;
    localMessage.obj = paramObject;
    localMessage.setData(paramBundle);
    try
    {
      paramMessenger.send(localMessage);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Could not send message to ");
      String str1 = getClientId(paramMessenger);
      String str2 = str1;
      int i = Log.e("MediaRouteProviderSrv", str2, localRemoteException);
      return;
    }
    catch (DeadObjectException localDeadObjectException)
    {
    }
  }

  private boolean updateCompositeDiscoveryRequest()
  {
    Object localObject = null;
    MediaRouteSelector.Builder localBuilder1 = null;
    boolean bool1 = false;
    int i = this.mClients.size();
    int j = 0;
    if (j < i)
    {
      MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = ((ClientRecord)this.mClients.get(j)).mDiscoveryRequest;
      if ((localMediaRouteDiscoveryRequest != null) && ((!localMediaRouteDiscoveryRequest.getSelector().isEmpty()) || (localMediaRouteDiscoveryRequest.isActiveScan())))
      {
        boolean bool2 = localMediaRouteDiscoveryRequest.isActiveScan();
        bool1 |= bool2;
        if (localObject != null)
          break label94;
        localObject = localMediaRouteDiscoveryRequest;
      }
      while (true)
      {
        j += 1;
        break;
        label94: if (localBuilder1 == null)
        {
          MediaRouteSelector localMediaRouteSelector1 = ((MediaRouteDiscoveryRequest)localObject).getSelector();
          localBuilder1 = new MediaRouteSelector.Builder(localMediaRouteSelector1);
        }
        MediaRouteSelector localMediaRouteSelector2 = localMediaRouteDiscoveryRequest.getSelector();
        MediaRouteSelector.Builder localBuilder2 = localBuilder1.addSelector(localMediaRouteSelector2);
      }
    }
    if (localBuilder1 != null)
    {
      MediaRouteSelector localMediaRouteSelector3 = localBuilder1.build();
      localObject = new MediaRouteDiscoveryRequest(localMediaRouteSelector3, bool1);
    }
    if ((this.mCompositeDiscoveryRequest != localObject) && ((this.mCompositeDiscoveryRequest == null) || (!this.mCompositeDiscoveryRequest.equals(localObject))))
    {
      this.mCompositeDiscoveryRequest = ((MediaRouteDiscoveryRequest)localObject);
      this.mProvider.setDiscoveryRequest((MediaRouteDiscoveryRequest)localObject);
    }
    for (boolean bool3 = true; ; bool3 = false)
      return bool3;
  }

  private static final class ReceiveHandler extends Handler
  {
    private final WeakReference<MediaRouteProviderService> mServiceRef;

    public ReceiveHandler(MediaRouteProviderService paramMediaRouteProviderService)
    {
      WeakReference localWeakReference = new WeakReference(paramMediaRouteProviderService);
      this.mServiceRef = localWeakReference;
    }

    private boolean processMessage(int paramInt1, Messenger paramMessenger, int paramInt2, int paramInt3, Object paramObject, Bundle paramBundle)
    {
      boolean bool = false;
      MediaRouteProviderService localMediaRouteProviderService = (MediaRouteProviderService)this.mServiceRef.get();
      if (localMediaRouteProviderService != null)
        switch (paramInt1)
        {
        default:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        }
      do
        while (true)
        {
          return bool;
          bool = localMediaRouteProviderService.onRegisterClient(paramMessenger, paramInt2, paramInt3);
          continue;
          bool = localMediaRouteProviderService.onUnregisterClient(paramMessenger, paramInt2);
          continue;
          String str = paramBundle.getString("routeId");
          if (str != null)
          {
            bool = localMediaRouteProviderService.onCreateRouteController(paramMessenger, paramInt2, paramInt3, str);
            continue;
            bool = localMediaRouteProviderService.onReleaseRouteController(paramMessenger, paramInt2, paramInt3);
            continue;
            bool = localMediaRouteProviderService.onSelectRoute(paramMessenger, paramInt2, paramInt3);
            continue;
            bool = localMediaRouteProviderService.onUnselectRoute(paramMessenger, paramInt2, paramInt3);
            continue;
            int i = paramBundle.getInt("volume", -1);
            if (i >= 0)
            {
              bool = localMediaRouteProviderService.onSetRouteVolume(paramMessenger, paramInt2, paramInt3, i);
              continue;
              int j = paramBundle.getInt("volume", 0);
              if (j != 0)
              {
                bool = localMediaRouteProviderService.onUpdateRouteVolume(paramMessenger, paramInt2, paramInt3, j);
                continue;
                if ((paramObject instanceof Intent))
                {
                  Intent localIntent = (Intent)paramObject;
                  bool = localMediaRouteProviderService.onRouteControlRequest(paramMessenger, paramInt2, paramInt3, localIntent);
                }
              }
            }
          }
        }
      while ((paramObject != null) && (!(paramObject instanceof Bundle)));
      MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = MediaRouteDiscoveryRequest.fromBundle((Bundle)paramObject);
      if ((localMediaRouteDiscoveryRequest != null) && (localMediaRouteDiscoveryRequest.isValid()));
      while (true)
      {
        bool = localMediaRouteProviderService.onSetDiscoveryRequest(paramMessenger, paramInt2, localMediaRouteDiscoveryRequest);
        break;
        localMediaRouteDiscoveryRequest = null;
      }
    }

    public void handleMessage(Message paramMessage)
    {
      Messenger localMessenger = paramMessage.replyTo;
      if (MediaRouteProviderService.isValidRemoteMessenger(localMessenger))
      {
        int i = paramMessage.what;
        int j = paramMessage.arg1;
        int k = paramMessage.arg2;
        Object localObject = paramMessage.obj;
        Bundle localBundle = paramMessage.peekData();
        if (processMessage(i, localMessenger, j, k, localObject, localBundle))
          return;
        if (MediaRouteProviderService.DEBUG)
        {
          StringBuilder localStringBuilder = new StringBuilder();
          String str1 = MediaRouteProviderService.getClientId(localMessenger);
          String str2 = str1 + ": Message failed, what=" + i + ", requestId=" + j + ", arg=" + k + ", obj=" + localObject + ", data=" + localBundle;
          int m = Log.d("MediaRouteProviderSrv", str2);
        }
        MediaRouteProviderService.sendGenericFailure(localMessenger, j);
        return;
      }
      if (!MediaRouteProviderService.DEBUG)
        return;
      int n = Log.d("MediaRouteProviderSrv", "Ignoring message without valid reply messenger.");
    }
  }

  private final class ClientRecord
    implements IBinder.DeathRecipient
  {
    private final SparseArray<MediaRouteProvider.RouteController> mControllers;
    public MediaRouteDiscoveryRequest mDiscoveryRequest;
    public final Messenger mMessenger;
    public final int mVersion;

    public ClientRecord(Messenger paramInt, int arg3)
    {
      SparseArray localSparseArray = new SparseArray();
      this.mControllers = localSparseArray;
      this.mMessenger = paramInt;
      int i;
      this.mVersion = i;
    }

    public void binderDied()
    {
      MediaRouteProviderService.PrivateHandler localPrivateHandler = MediaRouteProviderService.this.mPrivateHandler;
      Messenger localMessenger = this.mMessenger;
      localPrivateHandler.obtainMessage(1, localMessenger).sendToTarget();
    }

    public boolean createRouteController(String paramString, int paramInt)
    {
      if (this.mControllers.indexOfKey(paramInt) < 0)
      {
        MediaRouteProvider.RouteController localRouteController = MediaRouteProviderService.this.mProvider.onCreateRouteController(paramString);
        if (localRouteController != null)
          this.mControllers.put(paramInt, localRouteController);
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void dispose()
    {
      int i = this.mControllers.size();
      int j = 0;
      while (j < i)
      {
        ((MediaRouteProvider.RouteController)this.mControllers.valueAt(j)).onRelease();
        j += 1;
      }
      this.mControllers.clear();
      boolean bool1 = this.mMessenger.getBinder().unlinkToDeath(this, 0);
      boolean bool2 = setDiscoveryRequest(null);
    }

    public MediaRouteProvider.RouteController getRouteController(int paramInt)
    {
      return (MediaRouteProvider.RouteController)this.mControllers.get(paramInt);
    }

    public boolean hasMessenger(Messenger paramMessenger)
    {
      IBinder localIBinder1 = this.mMessenger.getBinder();
      IBinder localIBinder2 = paramMessenger.getBinder();
      if (localIBinder1 == localIBinder2);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean register()
    {
      boolean bool = false;
      try
      {
        this.mMessenger.getBinder().linkToDeath(this, 0);
        bool = true;
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
          binderDied();
      }
    }

    public boolean releaseRouteController(int paramInt)
    {
      MediaRouteProvider.RouteController localRouteController = (MediaRouteProvider.RouteController)this.mControllers.get(paramInt);
      if (localRouteController != null)
      {
        this.mControllers.remove(paramInt);
        localRouteController.onRelease();
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean setDiscoveryRequest(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
    {
      if ((this.mDiscoveryRequest != paramMediaRouteDiscoveryRequest) && ((this.mDiscoveryRequest == null) || (!this.mDiscoveryRequest.equals(paramMediaRouteDiscoveryRequest))))
        this.mDiscoveryRequest = paramMediaRouteDiscoveryRequest;
      for (boolean bool = MediaRouteProviderService.this.updateCompositeDiscoveryRequest(); ; bool = false)
        return bool;
    }

    public String toString()
    {
      return MediaRouteProviderService.getClientId(this.mMessenger);
    }
  }

  private final class ProviderCallback extends MediaRouteProvider.Callback
  {
    private ProviderCallback()
    {
    }

    public void onDescriptorChanged(MediaRouteProvider paramMediaRouteProvider, MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
    {
      MediaRouteProviderService.this.sendDescriptorChanged(paramMediaRouteProviderDescriptor);
    }
  }

  private final class PrivateHandler extends Handler
  {
    private PrivateHandler()
    {
    }

    public void handleMessage(Message paramMessage)
    {
      switch (paramMessage.what)
      {
      default:
        return;
      case 1:
      }
      MediaRouteProviderService localMediaRouteProviderService = MediaRouteProviderService.this;
      Messenger localMessenger = (Messenger)paramMessage.obj;
      localMediaRouteProviderService.onBinderDied(localMessenger);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.MediaRouteProviderService
 * JD-Core Version:    0.6.2
 */