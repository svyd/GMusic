package com.google.android.music.sync.common;

import android.accounts.AuthenticatorException;
import android.util.Log;
import java.util.concurrent.Callable;

public abstract class UpstreamReader
  implements Callable<Void>
{
  protected final AbstractSyncAdapter.UpstreamQueue mQueue;
  private final String mTag;

  public UpstreamReader(AbstractSyncAdapter.UpstreamQueue paramUpstreamQueue, String paramString)
  {
    this.mTag = paramString;
    this.mQueue = paramUpstreamQueue;
  }

  public Void call()
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    int i = 0;
    try
    {
      fillQueue();
      if (0 != 0)
        this.mQueue.kill();
      this.mQueue.close();
      return null;
    }
    catch (RuntimeException localRuntimeException)
    {
      i = 1;
      StringBuilder localStringBuilder = new StringBuilder().append("Upstream reader thread threw an unknown error.  Bailing. ");
      String str1 = localRuntimeException.getLocalizedMessage();
      String str2 = str1;
      int j = Log.wtf(this.mTag, str2, localRuntimeException);
      throw new HardSyncException(str2, localRuntimeException);
    }
    finally
    {
      if (i != 0)
        this.mQueue.kill();
      this.mQueue.close();
    }
  }

  public abstract void fillQueue()
    throws AuthenticatorException, HardSyncException, SoftSyncException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.UpstreamReader
 * JD-Core Version:    0.6.2
 */