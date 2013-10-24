package com.google.android.music.sync.common;

import android.accounts.AuthenticatorException;
import com.google.android.music.sync.api.ServiceUnavailableException;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.List;
import java.util.concurrent.Callable;

public abstract class UpstreamSender
  implements Callable<UpstreamSenderResult>
{
  protected final AbstractSyncAdapter.UpstreamQueue mQueue;
  protected final String mTag;
  private final int mUpstreamPageSize;
  private final boolean mUseVerboseLogging;

  public UpstreamSender(AbstractSyncAdapter.UpstreamQueue paramUpstreamQueue, int paramInt, String paramString)
  {
    this.mQueue = paramUpstreamQueue;
    this.mUpstreamPageSize = paramInt;
    this.mTag = paramString;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.mUseVerboseLogging = bool;
  }

  // ERROR //
  public UpstreamSenderResult call()
    throws AuthenticatorException, HardSyncException, SoftSyncException, ServiceUnavailableException
  {
    // Byte code:
    //   0: new 9	com/google/android/music/sync/common/UpstreamSender$UpstreamSenderResult
    //   3: dup
    //   4: invokespecial 61	com/google/android/music/sync/common/UpstreamSender$UpstreamSenderResult:<init>	()V
    //   7: astore_1
    //   8: iconst_0
    //   9: istore_2
    //   10: aload_0
    //   11: getfield 26	com/google/android/music/sync/common/UpstreamSender:mQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue;
    //   14: astore_3
    //   15: aload_0
    //   16: getfield 28	com/google/android/music/sync/common/UpstreamSender:mUpstreamPageSize	I
    //   19: istore 4
    //   21: aload_3
    //   22: iload 4
    //   24: invokevirtual 67	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue:take	(I)Ljava/util/ArrayList;
    //   27: astore 5
    //   29: aload 5
    //   31: invokeinterface 73 1 0
    //   36: ifeq +34 -> 70
    //   39: aload_0
    //   40: getfield 44	com/google/android/music/sync/common/UpstreamSender:mUseVerboseLogging	Z
    //   43: ifeq +14 -> 57
    //   46: aload_0
    //   47: getfield 30	com/google/android/music/sync/common/UpstreamSender:mTag	Ljava/lang/String;
    //   50: ldc 75
    //   52: invokestatic 81	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   55: istore 6
    //   57: iconst_0
    //   58: ifeq +10 -> 68
    //   61: aload_0
    //   62: getfield 26	com/google/android/music/sync/common/UpstreamSender:mQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue;
    //   65: invokevirtual 84	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue:kill	()V
    //   68: aload_1
    //   69: areturn
    //   70: aload_0
    //   71: aload 5
    //   73: invokevirtual 88	com/google/android/music/sync/common/UpstreamSender:processUpstreamEntityBlock	(Ljava/util/List;)V
    //   76: iload_2
    //   77: iconst_1
    //   78: iadd
    //   79: istore_2
    //   80: aload_0
    //   81: getfield 44	com/google/android/music/sync/common/UpstreamSender:mUseVerboseLogging	Z
    //   84: ifeq -74 -> 10
    //   87: aload_0
    //   88: getfield 30	com/google/android/music/sync/common/UpstreamSender:mTag	Ljava/lang/String;
    //   91: astore 7
    //   93: new 90	java/lang/StringBuilder
    //   96: dup
    //   97: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   100: ldc 93
    //   102: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: iload_2
    //   106: invokevirtual 100	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   109: ldc 102
    //   111: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   117: astore 8
    //   119: aload 7
    //   121: aload 8
    //   123: invokestatic 81	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   126: istore 9
    //   128: goto -118 -> 10
    //   131: astore 10
    //   133: new 52	com/google/android/music/sync/common/SoftSyncException
    //   136: dup
    //   137: ldc 108
    //   139: aload 10
    //   141: invokespecial 111	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   144: athrow
    //   145: astore 11
    //   147: iconst_1
    //   148: ifeq +10 -> 158
    //   151: aload_0
    //   152: getfield 26	com/google/android/music/sync/common/UpstreamSender:mQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue;
    //   155: invokevirtual 84	com/google/android/music/sync/common/AbstractSyncAdapter$UpstreamQueue:kill	()V
    //   158: aload 11
    //   160: athrow
    //   161: astore 10
    //   163: aload 10
    //   165: invokevirtual 115	com/google/android/music/sync/common/ConflictDetectedException:getConflictCount	()I
    //   168: istore 12
    //   170: aload_1
    //   171: getfield 118	com/google/android/music/sync/common/UpstreamSender$UpstreamSenderResult:mNumConflicts	I
    //   174: iload 12
    //   176: iadd
    //   177: istore 13
    //   179: aload_1
    //   180: iload 13
    //   182: putfield 118	com/google/android/music/sync/common/UpstreamSender$UpstreamSenderResult:mNumConflicts	I
    //   185: aload_0
    //   186: getfield 44	com/google/android/music/sync/common/UpstreamSender:mUseVerboseLogging	Z
    //   189: ifeq -113 -> 76
    //   192: aload_0
    //   193: getfield 30	com/google/android/music/sync/common/UpstreamSender:mTag	Ljava/lang/String;
    //   196: astore 14
    //   198: new 90	java/lang/StringBuilder
    //   201: dup
    //   202: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   205: ldc 120
    //   207: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: iload 12
    //   212: invokevirtual 100	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   215: ldc 122
    //   217: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   220: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   223: astore 15
    //   225: aload 14
    //   227: aload 15
    //   229: aload 10
    //   231: invokestatic 125	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   234: istore 16
    //   236: goto -160 -> 76
    //   239: astore 10
    //   241: new 90	java/lang/StringBuilder
    //   244: dup
    //   245: invokespecial 91	java/lang/StringBuilder:<init>	()V
    //   248: ldc 127
    //   250: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: astore 17
    //   255: aload 10
    //   257: invokevirtual 130	java/lang/RuntimeException:getLocalizedMessage	()Ljava/lang/String;
    //   260: astore 18
    //   262: aload 17
    //   264: aload 18
    //   266: invokevirtual 97	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   269: invokevirtual 106	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   272: astore 19
    //   274: aload_0
    //   275: getfield 30	com/google/android/music/sync/common/UpstreamSender:mTag	Ljava/lang/String;
    //   278: aload 19
    //   280: aload 10
    //   282: invokestatic 133	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   285: istore 20
    //   287: new 50	com/google/android/music/sync/common/HardSyncException
    //   290: dup
    //   291: aload 19
    //   293: aload 10
    //   295: invokespecial 134	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   298: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   10	57	131	java/lang/InterruptedException
    //   70	76	131	java/lang/InterruptedException
    //   80	128	131	java/lang/InterruptedException
    //   163	236	131	java/lang/InterruptedException
    //   10	57	145	finally
    //   70	76	145	finally
    //   80	128	145	finally
    //   133	145	145	finally
    //   163	236	145	finally
    //   241	299	145	finally
    //   70	76	161	com/google/android/music/sync/common/ConflictDetectedException
    //   10	57	239	java/lang/RuntimeException
    //   70	76	239	java/lang/RuntimeException
    //   80	128	239	java/lang/RuntimeException
    //   163	236	239	java/lang/RuntimeException
  }

  protected abstract void processUpstreamEntityBlock(List<QueueableSyncEntity> paramList)
    throws AuthenticatorException, HardSyncException, SoftSyncException, ServiceUnavailableException, ConflictDetectedException;

  public static class UpstreamSenderResult
  {
    public int mNumConflicts = 0;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.UpstreamSender
 * JD-Core Version:    0.6.2
 */