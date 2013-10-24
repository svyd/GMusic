package android.support.v7.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
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
import java.util.List;

final class RegisteredMediaRouteProvider extends MediaRouteProvider
  implements ServiceConnection
{
  private static final boolean DEBUG = Log.isLoggable("MediaRouteProviderProxy", 3);
  private Connection mActiveConnection;
  private boolean mBound;
  private final ComponentName mComponentName;
  private boolean mConnectionReady;
  private final ArrayList<Controller> mControllers;
  private final PrivateHandler mPrivateHandler;
  private boolean mStarted;

  public RegisteredMediaRouteProvider(Context paramContext, ComponentName paramComponentName)
  {
    super(paramContext, localProviderMetadata);
    ArrayList localArrayList = new ArrayList();
    this.mControllers = localArrayList;
    this.mComponentName = paramComponentName;
    PrivateHandler localPrivateHandler = new PrivateHandler(null);
    this.mPrivateHandler = localPrivateHandler;
  }

  private void attachControllersToConnection()
  {
    int i = this.mControllers.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      Controller localController = (Controller)this.mControllers.get(j);
      Connection localConnection = this.mActiveConnection;
      localController.attachConnection(localConnection);
      j += 1;
    }
  }

  private void bind()
  {
    if (this.mBound)
      return;
    if (DEBUG)
    {
      String str1 = this + ": Binding";
      int i = Log.d("MediaRouteProviderProxy", str1);
    }
    Intent localIntent1 = new Intent("android.media.MediaRouteProviderService");
    ComponentName localComponentName = this.mComponentName;
    Intent localIntent2 = localIntent1.setComponent(localComponentName);
    try
    {
      boolean bool = getContext().bindService(localIntent1, this, 1);
      this.mBound = bool;
      if (this.mBound)
        return;
      if (!DEBUG)
        return;
      String str2 = this + ": Bind failed";
      int j = Log.d("MediaRouteProviderProxy", str2);
      return;
    }
    catch (SecurityException localSecurityException)
    {
      if (!DEBUG)
        return;
      String str3 = this + ": Bind failed";
      int k = Log.d("MediaRouteProviderProxy", str3, localSecurityException);
    }
  }

  private void detachControllersFromConnection()
  {
    int i = this.mControllers.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      ((Controller)this.mControllers.get(j)).detachConnection();
      j += 1;
    }
  }

  private void disconnect()
  {
    if (this.mActiveConnection == null)
      return;
    setDescriptor(null);
    this.mConnectionReady = false;
    detachControllersFromConnection();
    this.mActiveConnection.dispose();
    this.mActiveConnection = null;
  }

  private void onConnectionDescriptorChanged(Connection paramConnection, MediaRouteProviderDescriptor paramMediaRouteProviderDescriptor)
  {
    if (this.mActiveConnection != paramConnection)
      return;
    if (DEBUG)
    {
      String str = this + ": Descriptor changed, descriptor=" + paramMediaRouteProviderDescriptor;
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    setDescriptor(paramMediaRouteProviderDescriptor);
  }

  private void onConnectionDied(Connection paramConnection)
  {
    if (this.mActiveConnection != paramConnection)
      return;
    if (DEBUG)
    {
      String str = this + ": Service connection died";
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    disconnect();
  }

  private void onConnectionError(Connection paramConnection, String paramString)
  {
    if (this.mActiveConnection != paramConnection)
      return;
    if (DEBUG)
    {
      String str = this + ": Service connection error - " + paramString;
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    unbind();
  }

  private void onConnectionReady(Connection paramConnection)
  {
    if (this.mActiveConnection != paramConnection)
      return;
    this.mConnectionReady = true;
    attachControllersToConnection();
    MediaRouteDiscoveryRequest localMediaRouteDiscoveryRequest = getDiscoveryRequest();
    if (localMediaRouteDiscoveryRequest == null)
      return;
    this.mActiveConnection.setDiscoveryRequest(localMediaRouteDiscoveryRequest);
  }

  private void onControllerReleased(Controller paramController)
  {
    boolean bool = this.mControllers.remove(paramController);
    paramController.detachConnection();
    updateBinding();
  }

  private boolean shouldBind()
  {
    boolean bool = true;
    if (this.mStarted)
      if (getDiscoveryRequest() == null);
    while (true)
    {
      return bool;
      if (this.mControllers.isEmpty())
        bool = false;
    }
  }

  private void unbind()
  {
    if (!this.mBound)
      return;
    if (DEBUG)
    {
      String str = this + ": Unbinding";
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    this.mBound = false;
    disconnect();
    getContext().unbindService(this);
  }

  private void updateBinding()
  {
    if (shouldBind())
    {
      bind();
      return;
    }
    unbind();
  }

  public boolean hasComponentName(String paramString1, String paramString2)
  {
    if ((this.mComponentName.getPackageName().equals(paramString1)) && (this.mComponentName.getClassName().equals(paramString2)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public MediaRouteProvider.RouteController onCreateRouteController(String paramString)
  {
    MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = getDescriptor();
    int j;
    Controller localController;
    if (localMediaRouteProviderDescriptor != null)
    {
      List localList = localMediaRouteProviderDescriptor.getRoutes();
      int i = localList.size();
      j = 0;
      if (j < i)
        if (((MediaRouteDescriptor)localList.get(j)).getId().equals(paramString))
        {
          localController = new Controller(paramString);
          boolean bool = this.mControllers.add(localController);
          if (this.mConnectionReady)
          {
            Connection localConnection = this.mActiveConnection;
            localController.attachConnection(localConnection);
          }
          updateBinding();
        }
    }
    while (true)
    {
      return localController;
      j += 1;
      break;
      localController = null;
    }
  }

  public void onDiscoveryRequestChanged(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
  {
    if (this.mConnectionReady)
      this.mActiveConnection.setDiscoveryRequest(paramMediaRouteDiscoveryRequest);
    updateBinding();
  }

  public void onServiceConnected(ComponentName paramComponentName, IBinder paramIBinder)
  {
    if (DEBUG)
    {
      String str1 = this + ": Connected";
      int i = Log.d("MediaRouteProviderProxy", str1);
    }
    if (!this.mBound)
      return;
    disconnect();
    Messenger localMessenger;
    if (paramIBinder != null)
      localMessenger = new Messenger(paramIBinder);
    while (MediaRouteProviderService.isValidRemoteMessenger(localMessenger))
    {
      Connection localConnection = new Connection(localMessenger);
      if (localConnection.register())
      {
        this.mActiveConnection = localConnection;
        return;
        localMessenger = null;
      }
      else
      {
        if (!DEBUG)
          return;
        String str2 = this + ": Registration failed";
        int j = Log.d("MediaRouteProviderProxy", str2);
        return;
      }
    }
    String str3 = this + ": Service returned invalid messenger binder";
    int k = Log.e("MediaRouteProviderProxy", str3);
  }

  public void onServiceDisconnected(ComponentName paramComponentName)
  {
    if (DEBUG)
    {
      String str = this + ": Service disconnected";
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    disconnect();
  }

  public void rebindIfDisconnected()
  {
    if (this.mActiveConnection != null)
      return;
    if (!shouldBind())
      return;
    unbind();
    bind();
  }

  public void start()
  {
    if (this.mStarted)
      return;
    if (DEBUG)
    {
      String str = this + ": Starting";
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    this.mStarted = true;
    updateBinding();
  }

  public void stop()
  {
    if (!this.mStarted)
      return;
    if (DEBUG)
    {
      String str = this + ": Stopping";
      int i = Log.d("MediaRouteProviderProxy", str);
    }
    this.mStarted = false;
    updateBinding();
  }

  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder().append("Service connection ");
    String str = this.mComponentName.flattenToShortString();
    return str;
  }

  private static final class ReceiveHandler extends Handler
  {
    private final WeakReference<RegisteredMediaRouteProvider.Connection> mConnectionRef;

    public ReceiveHandler(RegisteredMediaRouteProvider.Connection paramConnection)
    {
      WeakReference localWeakReference = new WeakReference(paramConnection);
      this.mConnectionRef = localWeakReference;
    }

    private boolean processMessage(RegisteredMediaRouteProvider.Connection paramConnection, int paramInt1, int paramInt2, int paramInt3, Object paramObject, Bundle paramBundle)
    {
      boolean bool1 = true;
      switch (paramInt1)
      {
      default:
      case 0:
      case 1:
      case 2:
      case 5:
      case 3:
      case 4:
      }
      do
      {
        bool1 = false;
        while (true)
        {
          return bool1;
          boolean bool2 = paramConnection.onGenericFailure(paramInt2);
          continue;
          boolean bool3 = paramConnection.onGenericSuccess(paramInt2);
          continue;
          if ((paramObject != null) && (!(paramObject instanceof Bundle)))
            break;
          Bundle localBundle1 = (Bundle)paramObject;
          bool1 = paramConnection.onRegistered(paramInt2, paramInt3, localBundle1);
          continue;
          if ((paramObject != null) && (!(paramObject instanceof Bundle)))
            break;
          Bundle localBundle2 = (Bundle)paramObject;
          bool1 = paramConnection.onDescriptorChanged(localBundle2);
          continue;
          if ((paramObject != null) && (!(paramObject instanceof Bundle)))
            break;
          Bundle localBundle3 = (Bundle)paramObject;
          bool1 = paramConnection.onControlRequestSucceeded(paramInt2, localBundle3);
        }
      }
      while ((paramObject != null) && (!(paramObject instanceof Bundle)));
      if (paramBundle == null);
      for (String str = null; ; str = paramBundle.getString("error"))
      {
        Bundle localBundle4 = (Bundle)paramObject;
        bool1 = paramConnection.onControlRequestFailed(paramInt2, str, localBundle4);
        break;
      }
    }

    public void dispose()
    {
      this.mConnectionRef.clear();
    }

    public void handleMessage(Message paramMessage)
    {
      RegisteredMediaRouteProvider.Connection localConnection = (RegisteredMediaRouteProvider.Connection)this.mConnectionRef.get();
      if (localConnection == null)
        return;
      int i = paramMessage.what;
      int j = paramMessage.arg1;
      int k = paramMessage.arg2;
      Object localObject = paramMessage.obj;
      Bundle localBundle = paramMessage.peekData();
      if (processMessage(localConnection, i, j, k, localObject, localBundle))
        return;
      if (!RegisteredMediaRouteProvider.DEBUG)
        return;
      String str = "Unhandled message from server: " + paramMessage;
      int m = Log.d("MediaRouteProviderProxy", str);
    }
  }

  private final class PrivateHandler extends Handler
  {
    private PrivateHandler()
    {
    }
  }

  private final class Connection
    implements IBinder.DeathRecipient
  {
    private int mNextControllerId = 1;
    private int mNextRequestId = 1;
    private final SparseArray<MediaRouter.ControlRequestCallback> mPendingCallbacks;
    private int mPendingRegisterRequestId;
    private final RegisteredMediaRouteProvider.ReceiveHandler mReceiveHandler;
    private final Messenger mReceiveMessenger;
    private final Messenger mServiceMessenger;
    private int mServiceVersion;

    public Connection(Messenger arg2)
    {
      SparseArray localSparseArray = new SparseArray();
      this.mPendingCallbacks = localSparseArray;
      Object localObject;
      this.mServiceMessenger = localObject;
      RegisteredMediaRouteProvider.ReceiveHandler localReceiveHandler1 = new RegisteredMediaRouteProvider.ReceiveHandler(this);
      this.mReceiveHandler = localReceiveHandler1;
      RegisteredMediaRouteProvider.ReceiveHandler localReceiveHandler2 = this.mReceiveHandler;
      Messenger localMessenger = new Messenger(localReceiveHandler2);
      this.mReceiveMessenger = localMessenger;
    }

    private void failPendingCallbacks()
    {
      int i = 0;
      while (true)
      {
        int j = this.mPendingCallbacks.size();
        if (i >= j)
          break;
        ((MediaRouter.ControlRequestCallback)this.mPendingCallbacks.valueAt(i)).onError(null, null);
        i += 1;
      }
      this.mPendingCallbacks.clear();
    }

    private boolean sendRequest(int paramInt1, int paramInt2, int paramInt3, Object paramObject, Bundle paramBundle)
    {
      Message localMessage = Message.obtain();
      localMessage.what = paramInt1;
      localMessage.arg1 = paramInt2;
      localMessage.arg2 = paramInt3;
      localMessage.obj = paramObject;
      localMessage.setData(paramBundle);
      Messenger localMessenger = this.mReceiveMessenger;
      localMessage.replyTo = localMessenger;
      try
      {
        this.mServiceMessenger.send(localMessage);
        bool = true;
        return bool;
      }
      catch (RemoteException localRemoteException)
      {
        while (true)
        {
          if (paramInt1 != 2)
            int i = Log.e("MediaRouteProviderProxy", "Could not send message to service.", localRemoteException);
          boolean bool = false;
        }
      }
      catch (DeadObjectException localDeadObjectException)
      {
        label83: break label83;
      }
    }

    public void binderDied()
    {
      RegisteredMediaRouteProvider.PrivateHandler localPrivateHandler = RegisteredMediaRouteProvider.this.mPrivateHandler;
      Runnable local2 = new Runnable()
      {
        public void run()
        {
          RegisteredMediaRouteProvider localRegisteredMediaRouteProvider = RegisteredMediaRouteProvider.this;
          RegisteredMediaRouteProvider.Connection localConnection = RegisteredMediaRouteProvider.Connection.this;
          localRegisteredMediaRouteProvider.onConnectionDied(localConnection);
        }
      };
      boolean bool = localPrivateHandler.post(local2);
    }

    public int createRouteController(String paramString)
    {
      int i = this.mNextControllerId;
      int j = i + 1;
      this.mNextControllerId = j;
      Bundle localBundle = new Bundle();
      localBundle.putString("routeId", paramString);
      int k = this.mNextRequestId;
      int m = k + 1;
      this.mNextRequestId = m;
      boolean bool = sendRequest(3, k, i, null, localBundle);
      return i;
    }

    public void dispose()
    {
      Connection localConnection = this;
      int i = 0;
      Bundle localBundle = null;
      boolean bool1 = localConnection.sendRequest(2, 0, i, null, localBundle);
      this.mReceiveHandler.dispose();
      boolean bool2 = this.mServiceMessenger.getBinder().unlinkToDeath(this, 0);
      RegisteredMediaRouteProvider.PrivateHandler localPrivateHandler = RegisteredMediaRouteProvider.this.mPrivateHandler;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          RegisteredMediaRouteProvider.Connection.this.failPendingCallbacks();
        }
      };
      boolean bool3 = localPrivateHandler.post(local1);
    }

    public boolean onControlRequestFailed(int paramInt, String paramString, Bundle paramBundle)
    {
      MediaRouter.ControlRequestCallback localControlRequestCallback = (MediaRouter.ControlRequestCallback)this.mPendingCallbacks.get(paramInt);
      if (localControlRequestCallback != null)
      {
        this.mPendingCallbacks.remove(paramInt);
        localControlRequestCallback.onError(paramString, paramBundle);
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean onControlRequestSucceeded(int paramInt, Bundle paramBundle)
    {
      MediaRouter.ControlRequestCallback localControlRequestCallback = (MediaRouter.ControlRequestCallback)this.mPendingCallbacks.get(paramInt);
      if (localControlRequestCallback != null)
      {
        this.mPendingCallbacks.remove(paramInt);
        localControlRequestCallback.onResult(paramBundle);
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean onDescriptorChanged(Bundle paramBundle)
    {
      if (this.mServiceVersion != 0)
      {
        RegisteredMediaRouteProvider localRegisteredMediaRouteProvider = RegisteredMediaRouteProvider.this;
        MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = MediaRouteProviderDescriptor.fromBundle(paramBundle);
        localRegisteredMediaRouteProvider.onConnectionDescriptorChanged(this, localMediaRouteProviderDescriptor);
      }
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean onGenericFailure(int paramInt)
    {
      int i = this.mPendingRegisterRequestId;
      if (paramInt != i)
      {
        this.mPendingRegisterRequestId = 0;
        RegisteredMediaRouteProvider.this.onConnectionError(this, "Registation failed");
      }
      MediaRouter.ControlRequestCallback localControlRequestCallback = (MediaRouter.ControlRequestCallback)this.mPendingCallbacks.get(paramInt);
      if (localControlRequestCallback != null)
      {
        this.mPendingCallbacks.remove(paramInt);
        localControlRequestCallback.onError(null, null);
      }
      return true;
    }

    public boolean onGenericSuccess(int paramInt)
    {
      return true;
    }

    public boolean onRegistered(int paramInt1, int paramInt2, Bundle paramBundle)
    {
      boolean bool = true;
      if (this.mServiceVersion == 0)
      {
        int i = this.mPendingRegisterRequestId;
        if ((paramInt1 != i) && (paramInt2 >= 1))
        {
          this.mPendingRegisterRequestId = 0;
          this.mServiceVersion = paramInt2;
          RegisteredMediaRouteProvider localRegisteredMediaRouteProvider = RegisteredMediaRouteProvider.this;
          MediaRouteProviderDescriptor localMediaRouteProviderDescriptor = MediaRouteProviderDescriptor.fromBundle(paramBundle);
          localRegisteredMediaRouteProvider.onConnectionDescriptorChanged(this, localMediaRouteProviderDescriptor);
          RegisteredMediaRouteProvider.this.onConnectionReady(this);
        }
      }
      while (true)
      {
        return bool;
        bool = false;
      }
    }

    public boolean register()
    {
      int i = 1;
      int k = this.mNextRequestId;
      int m = k + 1;
      this.mNextRequestId = m;
      this.mPendingRegisterRequestId = k;
      int n = this.mPendingRegisterRequestId;
      Connection localConnection = this;
      int i1 = i;
      Bundle localBundle = null;
      if (!localConnection.sendRequest(i, n, i1, null, localBundle))
        i = 0;
      while (true)
      {
        return i;
        try
        {
          this.mServiceMessenger.getBinder().linkToDeath(this, 0);
        }
        catch (RemoteException localRemoteException)
        {
          binderDied();
          int j = 0;
        }
      }
    }

    public void releaseRouteController(int paramInt)
    {
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      Connection localConnection = this;
      int k = paramInt;
      Bundle localBundle = null;
      boolean bool = localConnection.sendRequest(4, i, k, null, localBundle);
    }

    public void selectRoute(int paramInt)
    {
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      Connection localConnection = this;
      int k = paramInt;
      Bundle localBundle = null;
      boolean bool = localConnection.sendRequest(5, i, k, null, localBundle);
    }

    public boolean sendControlRequest(int paramInt, Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      Connection localConnection = this;
      int k = paramInt;
      Intent localIntent = paramIntent;
      if (localConnection.sendRequest(9, i, k, localIntent, null))
        if (paramControlRequestCallback != null)
          this.mPendingCallbacks.put(i, paramControlRequestCallback);
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public void setDiscoveryRequest(MediaRouteDiscoveryRequest paramMediaRouteDiscoveryRequest)
    {
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      if (paramMediaRouteDiscoveryRequest != null);
      for (Bundle localBundle = paramMediaRouteDiscoveryRequest.asBundle(); ; localBundle = null)
      {
        boolean bool = sendRequest(10, i, 0, localBundle, null);
        return;
      }
    }

    public void setVolume(int paramInt1, int paramInt2)
    {
      Bundle localBundle = new Bundle();
      localBundle.putInt("volume", paramInt2);
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      Connection localConnection = this;
      int k = paramInt1;
      boolean bool = localConnection.sendRequest(7, i, k, null, localBundle);
    }

    public void unselectRoute(int paramInt)
    {
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      Connection localConnection = this;
      int k = paramInt;
      Bundle localBundle = null;
      boolean bool = localConnection.sendRequest(6, i, k, null, localBundle);
    }

    public void updateVolume(int paramInt1, int paramInt2)
    {
      Bundle localBundle = new Bundle();
      localBundle.putInt("volume", paramInt2);
      int i = this.mNextRequestId;
      int j = i + 1;
      this.mNextRequestId = j;
      Connection localConnection = this;
      int k = paramInt1;
      boolean bool = localConnection.sendRequest(8, i, k, null, localBundle);
    }
  }

  private final class Controller extends MediaRouteProvider.RouteController
  {
    private RegisteredMediaRouteProvider.Connection mConnection;
    private int mControllerId;
    private int mPendingSetVolume = -1;
    private int mPendingUpdateVolumeDelta;
    private final String mRouteId;
    private boolean mSelected;

    public Controller(String arg2)
    {
      Object localObject;
      this.mRouteId = localObject;
    }

    public void attachConnection(RegisteredMediaRouteProvider.Connection paramConnection)
    {
      this.mConnection = paramConnection;
      String str = this.mRouteId;
      int i = paramConnection.createRouteController(str);
      this.mControllerId = i;
      if (!this.mSelected)
        return;
      int j = this.mControllerId;
      paramConnection.selectRoute(j);
      if (this.mPendingSetVolume >= 0)
      {
        int k = this.mControllerId;
        int m = this.mPendingSetVolume;
        paramConnection.setVolume(k, m);
        this.mPendingSetVolume = -1;
      }
      if (this.mPendingUpdateVolumeDelta == 0)
        return;
      int n = this.mControllerId;
      int i1 = this.mPendingUpdateVolumeDelta;
      paramConnection.updateVolume(n, i1);
      this.mPendingUpdateVolumeDelta = 0;
    }

    public void detachConnection()
    {
      if (this.mConnection == null)
        return;
      RegisteredMediaRouteProvider.Connection localConnection = this.mConnection;
      int i = this.mControllerId;
      localConnection.releaseRouteController(i);
      this.mConnection = null;
      this.mControllerId = 0;
    }

    public boolean onControlRequest(Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      RegisteredMediaRouteProvider.Connection localConnection;
      int i;
      if (this.mConnection != null)
      {
        localConnection = this.mConnection;
        i = this.mControllerId;
      }
      for (boolean bool = localConnection.sendControlRequest(i, paramIntent, paramControlRequestCallback); ; bool = false)
        return bool;
    }

    public void onRelease()
    {
      RegisteredMediaRouteProvider.this.onControllerReleased(this);
    }

    public void onSelect()
    {
      this.mSelected = true;
      if (this.mConnection == null)
        return;
      RegisteredMediaRouteProvider.Connection localConnection = this.mConnection;
      int i = this.mControllerId;
      localConnection.selectRoute(i);
    }

    public void onSetVolume(int paramInt)
    {
      if (this.mConnection != null)
      {
        RegisteredMediaRouteProvider.Connection localConnection = this.mConnection;
        int i = this.mControllerId;
        localConnection.setVolume(i, paramInt);
        return;
      }
      this.mPendingSetVolume = paramInt;
      this.mPendingUpdateVolumeDelta = 0;
    }

    public void onUnselect()
    {
      this.mSelected = false;
      if (this.mConnection == null)
        return;
      RegisteredMediaRouteProvider.Connection localConnection = this.mConnection;
      int i = this.mControllerId;
      localConnection.unselectRoute(i);
    }

    public void onUpdateVolume(int paramInt)
    {
      if (this.mConnection != null)
      {
        RegisteredMediaRouteProvider.Connection localConnection = this.mConnection;
        int i = this.mControllerId;
        localConnection.updateVolume(i, paramInt);
        return;
      }
      int j = this.mPendingUpdateVolumeDelta + paramInt;
      this.mPendingUpdateVolumeDelta = j;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.media.RegisteredMediaRouteProvider
 * JD-Core Version:    0.6.2
 */