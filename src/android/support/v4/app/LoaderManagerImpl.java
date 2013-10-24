package android.support.v4.app;

import android.os.Bundle;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Modifier;

class LoaderManagerImpl extends LoaderManager
{
  static boolean DEBUG = false;
  static final String TAG = "LoaderManager";
  FragmentActivity mActivity;
  boolean mCreatingLoader;
  final SparseArrayCompat<LoaderInfo> mInactiveLoaders;
  final SparseArrayCompat<LoaderInfo> mLoaders;
  boolean mRetaining;
  boolean mRetainingStarted;
  boolean mStarted;
  final String mWho;

  LoaderManagerImpl(String paramString, FragmentActivity paramFragmentActivity, boolean paramBoolean)
  {
    SparseArrayCompat localSparseArrayCompat1 = new SparseArrayCompat();
    this.mLoaders = localSparseArrayCompat1;
    SparseArrayCompat localSparseArrayCompat2 = new SparseArrayCompat();
    this.mInactiveLoaders = localSparseArrayCompat2;
    this.mWho = paramString;
    this.mActivity = paramFragmentActivity;
    this.mStarted = paramBoolean;
  }

  private LoaderInfo createAndInstallLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
  {
    try
    {
      this.mCreatingLoader = true;
      LoaderInfo localLoaderInfo = createLoader(paramInt, paramBundle, paramLoaderCallbacks);
      installLoader(localLoaderInfo);
      return localLoaderInfo;
    }
    finally
    {
      this.mCreatingLoader = false;
    }
  }

  private LoaderInfo createLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<Object> paramLoaderCallbacks)
  {
    LoaderInfo localLoaderInfo = new LoaderInfo(paramInt, paramBundle, paramLoaderCallbacks);
    Loader localLoader = paramLoaderCallbacks.onCreateLoader(paramInt, paramBundle);
    localLoaderInfo.mLoader = localLoader;
    return localLoaderInfo;
  }

  public void destroyLoader(int paramInt)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    if (DEBUG)
    {
      String str = "destroyLoader in " + this + " of " + paramInt;
      int i = Log.v("LoaderManager", str);
    }
    int j = this.mLoaders.indexOfKey(paramInt);
    if (j >= 0)
    {
      LoaderInfo localLoaderInfo1 = (LoaderInfo)this.mLoaders.valueAt(j);
      this.mLoaders.removeAt(j);
      localLoaderInfo1.destroy();
    }
    int k = this.mInactiveLoaders.indexOfKey(paramInt);
    if (k >= 0)
    {
      LoaderInfo localLoaderInfo2 = (LoaderInfo)this.mInactiveLoaders.valueAt(k);
      this.mInactiveLoaders.removeAt(k);
      localLoaderInfo2.destroy();
    }
    if (this.mActivity == null)
      return;
    if (hasRunningLoaders())
      return;
    this.mActivity.mFragments.startPendingDeferredFragments();
  }

  void doDestroy()
  {
    if (!this.mRetaining)
    {
      if (DEBUG)
      {
        String str1 = "Destroying Active in " + this;
        int i = Log.v("LoaderManager", str1);
      }
      int j = this.mLoaders.size() + -1;
      while (j >= 0)
      {
        ((LoaderInfo)this.mLoaders.valueAt(j)).destroy();
        j += -1;
      }
      this.mLoaders.clear();
    }
    if (DEBUG)
    {
      String str2 = "Destroying Inactive in " + this;
      int k = Log.v("LoaderManager", str2);
    }
    int m = this.mInactiveLoaders.size() + -1;
    while (m >= 0)
    {
      ((LoaderInfo)this.mInactiveLoaders.valueAt(m)).destroy();
      m += -1;
    }
    this.mInactiveLoaders.clear();
  }

  void doReportNextStart()
  {
    int i = this.mLoaders.size() + -1;
    while (true)
    {
      if (i < 0)
        return;
      ((LoaderInfo)this.mLoaders.valueAt(i)).mReportNextStart = true;
      i += -1;
    }
  }

  void doReportStart()
  {
    int i = this.mLoaders.size() + -1;
    while (true)
    {
      if (i < 0)
        return;
      ((LoaderInfo)this.mLoaders.valueAt(i)).reportStart();
      i += -1;
    }
  }

  void doRetain()
  {
    if (DEBUG)
    {
      String str1 = "Retaining in " + this;
      int i = Log.v("LoaderManager", str1);
    }
    if (!this.mStarted)
    {
      RuntimeException localRuntimeException = new RuntimeException("here");
      Throwable localThrowable = localRuntimeException.fillInStackTrace();
      String str2 = "Called doRetain when not started: " + this;
      int j = Log.w("LoaderManager", str2, localRuntimeException);
      return;
    }
    this.mRetaining = true;
    this.mStarted = false;
    int k = this.mLoaders.size() + -1;
    while (true)
    {
      if (k < 0)
        return;
      ((LoaderInfo)this.mLoaders.valueAt(k)).retain();
      k += -1;
    }
  }

  void doStart()
  {
    if (DEBUG)
    {
      String str1 = "Starting in " + this;
      int i = Log.v("LoaderManager", str1);
    }
    if (this.mStarted)
    {
      RuntimeException localRuntimeException = new RuntimeException("here");
      Throwable localThrowable = localRuntimeException.fillInStackTrace();
      String str2 = "Called doStart when already started: " + this;
      int j = Log.w("LoaderManager", str2, localRuntimeException);
      return;
    }
    this.mStarted = true;
    int k = this.mLoaders.size() + -1;
    while (true)
    {
      if (k < 0)
        return;
      ((LoaderInfo)this.mLoaders.valueAt(k)).start();
      k += -1;
    }
  }

  void doStop()
  {
    if (DEBUG)
    {
      String str1 = "Stopping in " + this;
      int i = Log.v("LoaderManager", str1);
    }
    if (!this.mStarted)
    {
      RuntimeException localRuntimeException = new RuntimeException("here");
      Throwable localThrowable = localRuntimeException.fillInStackTrace();
      String str2 = "Called doStop when not started: " + this;
      int j = Log.w("LoaderManager", str2, localRuntimeException);
      return;
    }
    int k = this.mLoaders.size() + -1;
    while (k >= 0)
    {
      ((LoaderInfo)this.mLoaders.valueAt(k)).stop();
      k += -1;
    }
    this.mStarted = false;
  }

  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
  {
    if (this.mLoaders.size() > 0)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Active Loaders:");
      str1 = paramString + "    ";
      i = 0;
      while (true)
      {
        int j = this.mLoaders.size();
        if (i >= j)
          break;
        LoaderInfo localLoaderInfo1 = (LoaderInfo)this.mLoaders.valueAt(i);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("  #");
        int k = this.mLoaders.keyAt(i);
        paramPrintWriter.print(k);
        paramPrintWriter.print(": ");
        String str2 = localLoaderInfo1.toString();
        paramPrintWriter.println(str2);
        localLoaderInfo1.dump(str1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
        i += 1;
      }
    }
    if (this.mInactiveLoaders.size() <= 0)
      return;
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("Inactive Loaders:");
    String str1 = paramString + "    ";
    int i = 0;
    while (true)
    {
      int m = this.mInactiveLoaders.size();
      if (i >= m)
        return;
      LoaderInfo localLoaderInfo2 = (LoaderInfo)this.mInactiveLoaders.valueAt(i);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("  #");
      int n = this.mInactiveLoaders.keyAt(i);
      paramPrintWriter.print(n);
      paramPrintWriter.print(": ");
      String str3 = localLoaderInfo2.toString();
      paramPrintWriter.println(str3);
      localLoaderInfo2.dump(str1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      i += 1;
    }
  }

  void finishRetain()
  {
    if (!this.mRetaining)
      return;
    if (DEBUG)
    {
      String str = "Finished Retaining in " + this;
      int i = Log.v("LoaderManager", str);
    }
    this.mRetaining = false;
    int j = this.mLoaders.size() + -1;
    while (true)
    {
      if (j < 0)
        return;
      ((LoaderInfo)this.mLoaders.valueAt(j)).finishRetain();
      j += -1;
    }
  }

  public <D> Loader<D> getLoader(int paramInt)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    LoaderInfo localLoaderInfo = (LoaderInfo)this.mLoaders.get(paramInt);
    Loader localLoader;
    if (localLoaderInfo != null)
      if (localLoaderInfo.mPendingLoader != null)
        localLoader = localLoaderInfo.mPendingLoader.mLoader;
    while (true)
    {
      return localLoader;
      localLoader = localLoaderInfo.mLoader;
      continue;
      localLoader = null;
    }
  }

  public boolean hasRunningLoaders()
  {
    boolean bool1 = false;
    int i = this.mLoaders.size();
    int j = 0;
    if (j < i)
    {
      LoaderInfo localLoaderInfo = (LoaderInfo)this.mLoaders.valueAt(j);
      if ((localLoaderInfo.mStarted) && (!localLoaderInfo.mDeliveredData));
      for (boolean bool2 = true; ; bool2 = false)
      {
        bool1 |= bool2;
        j += 1;
        break;
      }
    }
    return bool1;
  }

  public <D> Loader<D> initLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<D> paramLoaderCallbacks)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    LoaderInfo localLoaderInfo = (LoaderInfo)this.mLoaders.get(paramInt);
    if (DEBUG)
    {
      String str1 = "initLoader in " + this + ": args=" + paramBundle;
      int i = Log.v("LoaderManager", str1);
    }
    if (localLoaderInfo == null)
    {
      localLoaderInfo = createAndInstallLoader(paramInt, paramBundle, paramLoaderCallbacks);
      if (DEBUG)
      {
        String str2 = "  Created new loader " + localLoaderInfo;
        int j = Log.v("LoaderManager", str2);
      }
    }
    while (true)
    {
      if ((localLoaderInfo.mHaveData) && (this.mStarted))
      {
        Loader localLoader = localLoaderInfo.mLoader;
        Object localObject = localLoaderInfo.mData;
        localLoaderInfo.callOnLoadFinished(localLoader, localObject);
      }
      return localLoaderInfo.mLoader;
      if (DEBUG)
      {
        String str3 = "  Re-using existing loader " + localLoaderInfo;
        int k = Log.v("LoaderManager", str3);
      }
      localLoaderInfo.mCallbacks = paramLoaderCallbacks;
    }
  }

  void installLoader(LoaderInfo paramLoaderInfo)
  {
    SparseArrayCompat localSparseArrayCompat = this.mLoaders;
    int i = paramLoaderInfo.mId;
    localSparseArrayCompat.put(i, paramLoaderInfo);
    if (!this.mStarted)
      return;
    paramLoaderInfo.start();
  }

  public <D> Loader<D> restartLoader(int paramInt, Bundle paramBundle, LoaderManager.LoaderCallbacks<D> paramLoaderCallbacks)
  {
    if (this.mCreatingLoader)
      throw new IllegalStateException("Called while creating a loader");
    LoaderInfo localLoaderInfo1 = (LoaderInfo)this.mLoaders.get(paramInt);
    if (DEBUG)
    {
      String str1 = "restartLoader in " + this + ": args=" + paramBundle;
      int i = Log.v("LoaderManager", str1);
    }
    if (localLoaderInfo1 != null)
    {
      LoaderInfo localLoaderInfo2 = (LoaderInfo)this.mInactiveLoaders.get(paramInt);
      if (localLoaderInfo2 == null)
        break label346;
      if (!localLoaderInfo1.mHaveData)
        break label189;
      if (DEBUG)
      {
        String str2 = "  Removing last inactive loader: " + localLoaderInfo1;
        int j = Log.v("LoaderManager", str2);
      }
      localLoaderInfo2.mDeliveredData = false;
      localLoaderInfo2.destroy();
      localLoaderInfo1.mLoader.abandon();
      this.mInactiveLoaders.put(paramInt, localLoaderInfo1);
    }
    while (true)
    {
      for (Loader localLoader = createAndInstallLoader(paramInt, paramBundle, paramLoaderCallbacks).mLoader; ; localLoader = localLoaderInfo1.mPendingLoader.mLoader)
      {
        return localLoader;
        label189: if (!localLoaderInfo1.mStarted)
        {
          if (DEBUG)
            int k = Log.v("LoaderManager", "  Current loader is stopped; replacing");
          this.mLoaders.put(paramInt, null);
          localLoaderInfo1.destroy();
          break;
        }
        if (localLoaderInfo1.mPendingLoader != null)
        {
          if (DEBUG)
          {
            StringBuilder localStringBuilder = new StringBuilder().append("  Removing pending loader: ");
            LoaderInfo localLoaderInfo3 = localLoaderInfo1.mPendingLoader;
            String str3 = localLoaderInfo3;
            int m = Log.v("LoaderManager", str3);
          }
          localLoaderInfo1.mPendingLoader.destroy();
          localLoaderInfo1.mPendingLoader = null;
        }
        if (DEBUG)
          int n = Log.v("LoaderManager", "  Enqueuing as new pending loader");
        LoaderInfo localLoaderInfo4 = createLoader(paramInt, paramBundle, paramLoaderCallbacks);
        localLoaderInfo1.mPendingLoader = localLoaderInfo4;
      }
      label346: if (DEBUG)
      {
        String str4 = "  Making last loader inactive: " + localLoaderInfo1;
        int i1 = Log.v("LoaderManager", str4);
      }
      localLoaderInfo1.mLoader.abandon();
      this.mInactiveLoaders.put(paramInt, localLoaderInfo1);
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    StringBuilder localStringBuilder2 = localStringBuilder1.append("LoaderManager{");
    String str = Integer.toHexString(System.identityHashCode(this));
    StringBuilder localStringBuilder3 = localStringBuilder1.append(str);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(" in ");
    DebugUtils.buildShortClassTag(this.mActivity, localStringBuilder1);
    StringBuilder localStringBuilder5 = localStringBuilder1.append("}}");
    return localStringBuilder1.toString();
  }

  void updateActivity(FragmentActivity paramFragmentActivity)
  {
    this.mActivity = paramFragmentActivity;
  }

  final class LoaderInfo
    implements Loader.OnLoadCompleteListener<Object>
  {
    final Bundle mArgs;
    LoaderManager.LoaderCallbacks<Object> mCallbacks;
    Object mData;
    boolean mDeliveredData;
    boolean mDestroyed;
    boolean mHaveData;
    final int mId;
    boolean mListenerRegistered;
    Loader<Object> mLoader;
    LoaderInfo mPendingLoader;
    boolean mReportNextStart;
    boolean mRetaining;
    boolean mRetainingStarted;
    boolean mStarted;

    public LoaderInfo(Bundle paramLoaderCallbacks, LoaderManager.LoaderCallbacks<Object> arg3)
    {
      this.mId = paramLoaderCallbacks;
      Object localObject1;
      this.mArgs = localObject1;
      Object localObject2;
      this.mCallbacks = localObject2;
    }

    void callOnLoadFinished(Loader<Object> paramLoader, Object paramObject)
    {
      if (this.mCallbacks == null)
        return;
      String str1 = null;
      if (LoaderManagerImpl.this.mActivity != null)
      {
        str1 = LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause;
        LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = "onLoadFinished";
      }
      try
      {
        if (LoaderManagerImpl.DEBUG)
        {
          StringBuilder localStringBuilder = new StringBuilder().append("  onLoadFinished in ").append(paramLoader).append(": ");
          String str2 = paramLoader.dataToString(paramObject);
          String str3 = str2;
          int i = Log.v("LoaderManager", str3);
        }
        this.mCallbacks.onLoadFinished(paramLoader, paramObject);
        if (LoaderManagerImpl.this.mActivity != null)
          LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = str1;
        this.mDeliveredData = true;
        return;
      }
      finally
      {
        if (LoaderManagerImpl.this.mActivity != null)
          LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = str1;
      }
    }

    void destroy()
    {
      if (LoaderManagerImpl.DEBUG)
      {
        String str1 = "  Destroying: " + this;
        int i = Log.v("LoaderManager", str1);
      }
      this.mDestroyed = true;
      boolean bool = this.mDeliveredData;
      this.mDeliveredData = false;
      String str3;
      if ((this.mCallbacks != null) && (this.mLoader != null) && (this.mHaveData) && (bool))
      {
        if (LoaderManagerImpl.DEBUG)
        {
          String str2 = "  Reseting: " + this;
          int j = Log.v("LoaderManager", str2);
        }
        str3 = null;
        if (LoaderManagerImpl.this.mActivity != null)
        {
          str3 = LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause;
          LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = "onLoaderReset";
        }
      }
      try
      {
        LoaderManager.LoaderCallbacks localLoaderCallbacks = this.mCallbacks;
        Loader localLoader = this.mLoader;
        localLoaderCallbacks.onLoaderReset(localLoader);
        if (LoaderManagerImpl.this.mActivity != null)
          LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = str3;
        this.mCallbacks = null;
        this.mData = null;
        this.mHaveData = false;
        if (this.mLoader != null)
        {
          if (this.mListenerRegistered)
          {
            this.mListenerRegistered = false;
            this.mLoader.unregisterListener(this);
          }
          this.mLoader.reset();
        }
        if (this.mPendingLoader == null)
          return;
        this.mPendingLoader.destroy();
        return;
      }
      finally
      {
        if (LoaderManagerImpl.this.mActivity != null)
          LoaderManagerImpl.this.mActivity.mFragments.mNoTransactionsBecause = str3;
      }
    }

    public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString)
    {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mId=");
      int i = this.mId;
      paramPrintWriter.print(i);
      paramPrintWriter.print(" mArgs=");
      Bundle localBundle = this.mArgs;
      paramPrintWriter.println(localBundle);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mCallbacks=");
      LoaderManager.LoaderCallbacks localLoaderCallbacks = this.mCallbacks;
      paramPrintWriter.println(localLoaderCallbacks);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mLoader=");
      Loader localLoader1 = this.mLoader;
      paramPrintWriter.println(localLoader1);
      if (this.mLoader != null)
      {
        Loader localLoader2 = this.mLoader;
        String str1 = paramString + "  ";
        localLoader2.dump(str1, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
      }
      if ((this.mHaveData) || (this.mDeliveredData))
      {
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mHaveData=");
        boolean bool1 = this.mHaveData;
        paramPrintWriter.print(bool1);
        paramPrintWriter.print("  mDeliveredData=");
        boolean bool2 = this.mDeliveredData;
        paramPrintWriter.println(bool2);
        paramPrintWriter.print(paramString);
        paramPrintWriter.print("mData=");
        Object localObject = this.mData;
        paramPrintWriter.println(localObject);
      }
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStarted=");
      boolean bool3 = this.mStarted;
      paramPrintWriter.print(bool3);
      paramPrintWriter.print(" mReportNextStart=");
      boolean bool4 = this.mReportNextStart;
      paramPrintWriter.print(bool4);
      paramPrintWriter.print(" mDestroyed=");
      boolean bool5 = this.mDestroyed;
      paramPrintWriter.println(bool5);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mRetaining=");
      boolean bool6 = this.mRetaining;
      paramPrintWriter.print(bool6);
      paramPrintWriter.print(" mRetainingStarted=");
      boolean bool7 = this.mRetainingStarted;
      paramPrintWriter.print(bool7);
      paramPrintWriter.print(" mListenerRegistered=");
      boolean bool8 = this.mListenerRegistered;
      paramPrintWriter.println(bool8);
      if (this.mPendingLoader == null)
        return;
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Pending Loader ");
      LoaderInfo localLoaderInfo1 = this.mPendingLoader;
      paramPrintWriter.print(localLoaderInfo1);
      paramPrintWriter.println(":");
      LoaderInfo localLoaderInfo2 = this.mPendingLoader;
      String str2 = paramString + "  ";
      localLoaderInfo2.dump(str2, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    }

    void finishRetain()
    {
      if (this.mRetaining)
      {
        if (LoaderManagerImpl.DEBUG)
        {
          String str = "  Finished Retaining: " + this;
          int i = Log.v("LoaderManager", str);
        }
        this.mRetaining = false;
        boolean bool1 = this.mStarted;
        boolean bool2 = this.mRetainingStarted;
        if ((bool1 != bool2) && (!this.mStarted))
          stop();
      }
      if (!this.mStarted)
        return;
      if (!this.mHaveData)
        return;
      if (this.mReportNextStart)
        return;
      Loader localLoader = this.mLoader;
      Object localObject = this.mData;
      callOnLoadFinished(localLoader, localObject);
    }

    public void onLoadComplete(Loader<Object> paramLoader, Object paramObject)
    {
      if (LoaderManagerImpl.DEBUG)
      {
        String str1 = "onLoadComplete: " + this;
        int i = Log.v("LoaderManager", str1);
      }
      if (this.mDestroyed)
      {
        if (!LoaderManagerImpl.DEBUG)
          return;
        int j = Log.v("LoaderManager", "  Ignoring load complete -- destroyed");
        return;
      }
      SparseArrayCompat localSparseArrayCompat1 = LoaderManagerImpl.this.mLoaders;
      int k = this.mId;
      if (localSparseArrayCompat1.get(k) != this)
      {
        if (!LoaderManagerImpl.DEBUG)
          return;
        int m = Log.v("LoaderManager", "  Ignoring load complete -- not active");
        return;
      }
      LoaderInfo localLoaderInfo1 = this.mPendingLoader;
      if (localLoaderInfo1 != null)
      {
        if (LoaderManagerImpl.DEBUG)
        {
          String str2 = "  Switching to pending loader: " + localLoaderInfo1;
          int n = Log.v("LoaderManager", str2);
        }
        this.mPendingLoader = null;
        SparseArrayCompat localSparseArrayCompat2 = LoaderManagerImpl.this.mLoaders;
        int i1 = this.mId;
        localSparseArrayCompat2.put(i1, null);
        destroy();
        LoaderManagerImpl.this.installLoader(localLoaderInfo1);
        return;
      }
      if ((this.mData != paramObject) || (!this.mHaveData))
      {
        this.mData = paramObject;
        this.mHaveData = true;
        if (this.mStarted)
          callOnLoadFinished(paramLoader, paramObject);
      }
      SparseArrayCompat localSparseArrayCompat3 = LoaderManagerImpl.this.mInactiveLoaders;
      int i2 = this.mId;
      LoaderInfo localLoaderInfo2 = (LoaderInfo)localSparseArrayCompat3.get(i2);
      if ((localLoaderInfo2 != null) && (localLoaderInfo2 != this))
      {
        localLoaderInfo2.mDeliveredData = false;
        localLoaderInfo2.destroy();
        SparseArrayCompat localSparseArrayCompat4 = LoaderManagerImpl.this.mInactiveLoaders;
        int i3 = this.mId;
        localSparseArrayCompat4.remove(i3);
      }
      if (LoaderManagerImpl.this.mActivity == null)
        return;
      if (LoaderManagerImpl.this.hasRunningLoaders())
        return;
      LoaderManagerImpl.this.mActivity.mFragments.startPendingDeferredFragments();
    }

    void reportStart()
    {
      if (!this.mStarted)
        return;
      if (!this.mReportNextStart)
        return;
      this.mReportNextStart = false;
      if (!this.mHaveData)
        return;
      Loader localLoader = this.mLoader;
      Object localObject = this.mData;
      callOnLoadFinished(localLoader, localObject);
    }

    void retain()
    {
      if (LoaderManagerImpl.DEBUG)
      {
        String str = "  Retaining: " + this;
        int i = Log.v("LoaderManager", str);
      }
      this.mRetaining = true;
      boolean bool = this.mStarted;
      this.mRetainingStarted = bool;
      this.mStarted = false;
      this.mCallbacks = null;
    }

    void start()
    {
      if ((this.mRetaining) && (this.mRetainingStarted))
      {
        this.mStarted = true;
        return;
      }
      if (this.mStarted)
        return;
      this.mStarted = true;
      if (LoaderManagerImpl.DEBUG)
      {
        String str1 = "  Starting: " + this;
        int i = Log.v("LoaderManager", str1);
      }
      if ((this.mLoader == null) && (this.mCallbacks != null))
      {
        LoaderManager.LoaderCallbacks localLoaderCallbacks = this.mCallbacks;
        int j = this.mId;
        Bundle localBundle = this.mArgs;
        Loader localLoader1 = localLoaderCallbacks.onCreateLoader(j, localBundle);
        this.mLoader = localLoader1;
      }
      if (this.mLoader == null)
        return;
      if ((this.mLoader.getClass().isMemberClass()) && (!Modifier.isStatic(this.mLoader.getClass().getModifiers())))
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Object returned from onCreateLoader must not be a non-static inner member class: ");
        Loader localLoader2 = this.mLoader;
        String str2 = localLoader2;
        throw new IllegalArgumentException(str2);
      }
      if (!this.mListenerRegistered)
      {
        Loader localLoader3 = this.mLoader;
        int k = this.mId;
        localLoader3.registerListener(k, this);
        this.mListenerRegistered = true;
      }
      this.mLoader.startLoading();
    }

    void stop()
    {
      if (LoaderManagerImpl.DEBUG)
      {
        String str = "  Stopping: " + this;
        int i = Log.v("LoaderManager", str);
      }
      this.mStarted = false;
      if (this.mRetaining)
        return;
      if (this.mLoader == null)
        return;
      if (!this.mListenerRegistered)
        return;
      this.mListenerRegistered = false;
      this.mLoader.unregisterListener(this);
      this.mLoader.stopLoading();
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder(64);
      StringBuilder localStringBuilder2 = localStringBuilder1.append("LoaderInfo{");
      String str = Integer.toHexString(System.identityHashCode(this));
      StringBuilder localStringBuilder3 = localStringBuilder1.append(str);
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" #");
      int i = this.mId;
      StringBuilder localStringBuilder5 = localStringBuilder1.append(i);
      StringBuilder localStringBuilder6 = localStringBuilder1.append(" : ");
      DebugUtils.buildShortClassTag(this.mLoader, localStringBuilder1);
      StringBuilder localStringBuilder7 = localStringBuilder1.append("}}");
      return localStringBuilder1.toString();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.LoaderManagerImpl
 * JD-Core Version:    0.6.2
 */