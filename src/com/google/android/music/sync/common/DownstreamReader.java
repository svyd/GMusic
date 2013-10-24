package com.google.android.music.sync.common;

import android.accounts.AuthenticatorException;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.concurrent.Callable;

public abstract class DownstreamReader
  implements Callable<Void>
{
  protected final AbstractSyncAdapter.DownstreamMergeQueue mMergeQueue;
  private final String mTag;
  protected final AbstractSyncAdapter.DownstreamFetchQueue mUpdateQueue;
  private final boolean mUseVerboseLogging;

  public DownstreamReader(AbstractSyncAdapter.DownstreamFetchQueue paramDownstreamFetchQueue, int paramInt, String paramString)
  {
    this.mUpdateQueue = paramDownstreamFetchQueue;
    AbstractSyncAdapter.DownstreamMergeQueue localDownstreamMergeQueue = new AbstractSyncAdapter.DownstreamMergeQueue(paramInt);
    this.mMergeQueue = localDownstreamMergeQueue;
    this.mTag = paramString;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.mUseVerboseLogging = bool;
  }

  // ERROR //
  public Void call()
    throws AuthenticatorException, HardSyncException, SoftSyncException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield 23	com/google/android/music/sync/common/DownstreamReader:mUpdateQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamFetchQueue;
    //   6: invokevirtual 68	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamFetchQueue:take	()Ljava/lang/Object;
    //   9: checkcast 70	com/google/android/music/sync/common/QueueableSyncEntity
    //   12: astore_2
    //   13: aload_2
    //   14: ifnonnull +58 -> 72
    //   17: aload_0
    //   18: getfield 46	com/google/android/music/sync/common/DownstreamReader:mUseVerboseLogging	Z
    //   21: ifeq +13 -> 34
    //   24: aload_0
    //   25: getfield 32	com/google/android/music/sync/common/DownstreamReader:mTag	Ljava/lang/String;
    //   28: ldc 72
    //   30: invokestatic 78	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   33: istore_3
    //   34: iconst_0
    //   35: ifeq +10 -> 45
    //   38: aload_0
    //   39: getfield 23	com/google/android/music/sync/common/DownstreamReader:mUpdateQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamFetchQueue;
    //   42: invokevirtual 81	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamFetchQueue:kill	()V
    //   45: aload_0
    //   46: getfield 30	com/google/android/music/sync/common/DownstreamReader:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   49: invokevirtual 84	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:close	()V
    //   52: aload_0
    //   53: getfield 46	com/google/android/music/sync/common/DownstreamReader:mUseVerboseLogging	Z
    //   56: ifeq +14 -> 70
    //   59: aload_0
    //   60: getfield 32	com/google/android/music/sync/common/DownstreamReader:mTag	Ljava/lang/String;
    //   63: ldc 86
    //   65: invokestatic 78	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   68: istore 4
    //   70: aconst_null
    //   71: areturn
    //   72: aload_0
    //   73: getfield 46	com/google/android/music/sync/common/DownstreamReader:mUseVerboseLogging	Z
    //   76: ifeq +54 -> 130
    //   79: aload_0
    //   80: getfield 32	com/google/android/music/sync/common/DownstreamReader:mTag	Ljava/lang/String;
    //   83: astore 5
    //   85: new 88	java/lang/StringBuilder
    //   88: dup
    //   89: invokespecial 89	java/lang/StringBuilder:<init>	()V
    //   92: ldc 91
    //   94: invokevirtual 95	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: astore 6
    //   99: iconst_0
    //   100: iconst_1
    //   101: iadd
    //   102: istore 7
    //   104: aload 6
    //   106: iload 7
    //   108: invokevirtual 98	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   111: ldc 100
    //   113: invokevirtual 95	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: invokevirtual 104	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   119: astore 8
    //   121: aload 5
    //   123: aload 8
    //   125: invokestatic 78	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   128: istore 9
    //   130: aload_0
    //   131: aload_2
    //   132: invokevirtual 108	com/google/android/music/sync/common/DownstreamReader:processServerEntity	(Lcom/google/android/music/sync/common/QueueableSyncEntity;)V
    //   135: goto -133 -> 2
    //   138: astore 10
    //   140: iconst_1
    //   141: istore_1
    //   142: new 59	com/google/android/music/sync/common/SoftSyncException
    //   145: dup
    //   146: ldc 110
    //   148: aload 10
    //   150: invokespecial 113	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   153: athrow
    //   154: astore 11
    //   156: iload_1
    //   157: ifeq +10 -> 167
    //   160: aload_0
    //   161: getfield 23	com/google/android/music/sync/common/DownstreamReader:mUpdateQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamFetchQueue;
    //   164: invokevirtual 81	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamFetchQueue:kill	()V
    //   167: aload_0
    //   168: getfield 30	com/google/android/music/sync/common/DownstreamReader:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   171: invokevirtual 84	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:close	()V
    //   174: aload_0
    //   175: getfield 46	com/google/android/music/sync/common/DownstreamReader:mUseVerboseLogging	Z
    //   178: ifeq +14 -> 192
    //   181: aload_0
    //   182: getfield 32	com/google/android/music/sync/common/DownstreamReader:mTag	Ljava/lang/String;
    //   185: ldc 86
    //   187: invokestatic 78	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   190: istore 12
    //   192: aload 11
    //   194: athrow
    //   195: astore 10
    //   197: iconst_1
    //   198: istore_1
    //   199: new 88	java/lang/StringBuilder
    //   202: dup
    //   203: invokespecial 89	java/lang/StringBuilder:<init>	()V
    //   206: ldc 115
    //   208: invokevirtual 95	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: astore 13
    //   213: aload 10
    //   215: invokevirtual 118	java/lang/RuntimeException:getLocalizedMessage	()Ljava/lang/String;
    //   218: astore 14
    //   220: aload 13
    //   222: aload 14
    //   224: invokevirtual 95	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   227: invokevirtual 104	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   230: astore 15
    //   232: aload_0
    //   233: getfield 32	com/google/android/music/sync/common/DownstreamReader:mTag	Ljava/lang/String;
    //   236: aload 15
    //   238: aload 10
    //   240: invokestatic 122	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   243: istore 16
    //   245: new 57	com/google/android/music/sync/common/HardSyncException
    //   248: dup
    //   249: aload 15
    //   251: aload 10
    //   253: invokespecial 123	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   256: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   2	34	138	java/lang/InterruptedException
    //   72	135	138	java/lang/InterruptedException
    //   2	34	154	finally
    //   72	135	154	finally
    //   142	154	154	finally
    //   199	257	154	finally
    //   2	34	195	java/lang/RuntimeException
    //   72	135	195	java/lang/RuntimeException
  }

  protected AbstractSyncAdapter.DownstreamMergeQueue getMergeQueue()
  {
    return this.mMergeQueue;
  }

  public abstract void processServerEntity(QueueableSyncEntity paramQueueableSyncEntity)
    throws AuthenticatorException, HardSyncException, SoftSyncException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.DownstreamReader
 * JD-Core Version:    0.6.2
 */