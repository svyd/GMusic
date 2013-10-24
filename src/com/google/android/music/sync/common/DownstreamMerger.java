package com.google.android.music.sync.common;

import android.accounts.AuthenticatorException;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.util.concurrent.Callable;

public abstract class DownstreamMerger
  implements Callable<Void>
{
  protected int mMaxBlockSize;
  protected final AbstractSyncAdapter.DownstreamMergeQueue mMergeQueue;
  protected final String mTag;
  private final boolean useVerboseLogging;

  public DownstreamMerger(AbstractSyncAdapter.DownstreamMergeQueue paramDownstreamMergeQueue, int paramInt, String paramString)
  {
    this.mMergeQueue = paramDownstreamMergeQueue;
    this.mMaxBlockSize = paramInt;
    this.mTag = paramString;
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.SYNC);
    this.useVerboseLogging = bool;
  }

  // ERROR //
  public Void call()
    throws HardSyncException, SoftSyncException, AuthenticatorException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield 23	com/google/android/music/sync/common/DownstreamMerger:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   6: astore_2
    //   7: aload_0
    //   8: getfield 25	com/google/android/music/sync/common/DownstreamMerger:mMaxBlockSize	I
    //   11: istore_3
    //   12: aload_2
    //   13: iload_3
    //   14: invokevirtual 64	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:take	(I)Ljava/util/ArrayList;
    //   17: astore 4
    //   19: aload 4
    //   21: invokeinterface 70 1 0
    //   26: istore 5
    //   28: aload_0
    //   29: getfield 41	com/google/android/music/sync/common/DownstreamMerger:useVerboseLogging	Z
    //   32: ifeq +45 -> 77
    //   35: aload_0
    //   36: getfield 27	com/google/android/music/sync/common/DownstreamMerger:mTag	Ljava/lang/String;
    //   39: astore 6
    //   41: new 72	java/lang/StringBuilder
    //   44: dup
    //   45: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   48: ldc 75
    //   50: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: iload 5
    //   55: invokevirtual 82	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   58: ldc 84
    //   60: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   66: astore 7
    //   68: aload 6
    //   70: aload 7
    //   72: invokestatic 94	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   75: istore 8
    //   77: iload 5
    //   79: ifne +52 -> 131
    //   82: aload_0
    //   83: getfield 41	com/google/android/music/sync/common/DownstreamMerger:useVerboseLogging	Z
    //   86: ifeq +14 -> 100
    //   89: aload_0
    //   90: getfield 27	com/google/android/music/sync/common/DownstreamMerger:mTag	Ljava/lang/String;
    //   93: ldc 96
    //   95: invokestatic 94	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   98: istore 9
    //   100: iconst_0
    //   101: ifeq +10 -> 111
    //   104: aload_0
    //   105: getfield 23	com/google/android/music/sync/common/DownstreamMerger:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   108: invokevirtual 99	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:kill	()V
    //   111: aload_0
    //   112: getfield 41	com/google/android/music/sync/common/DownstreamMerger:useVerboseLogging	Z
    //   115: ifeq +14 -> 129
    //   118: aload_0
    //   119: getfield 27	com/google/android/music/sync/common/DownstreamMerger:mTag	Ljava/lang/String;
    //   122: ldc 101
    //   124: invokestatic 94	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   127: istore 10
    //   129: aconst_null
    //   130: areturn
    //   131: iconst_0
    //   132: istore 11
    //   134: aload_0
    //   135: invokevirtual 104	com/google/android/music/sync/common/DownstreamMerger:onStartMergeBlock	()V
    //   138: aload 4
    //   140: invokeinterface 108 1 0
    //   145: astore 12
    //   147: aload 12
    //   149: invokeinterface 114 1 0
    //   154: ifeq +107 -> 261
    //   157: aload 12
    //   159: invokeinterface 117 1 0
    //   164: checkcast 119	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry
    //   167: astore 13
    //   169: aload 13
    //   171: getfield 123	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry:first	Ljava/lang/Object;
    //   174: checkcast 125	com/google/android/music/sync/common/QueueableSyncEntity
    //   177: astore 14
    //   179: aload 13
    //   181: getfield 128	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry:second	Ljava/lang/Object;
    //   184: checkcast 125	com/google/android/music/sync/common/QueueableSyncEntity
    //   187: astore 15
    //   189: aload_0
    //   190: aload 14
    //   192: aload 15
    //   194: invokevirtual 132	com/google/android/music/sync/common/DownstreamMerger:processMergeItem	(Lcom/google/android/music/sync/common/QueueableSyncEntity;Lcom/google/android/music/sync/common/QueueableSyncEntity;)V
    //   197: goto -50 -> 147
    //   200: astore 16
    //   202: aload_0
    //   203: iload 11
    //   205: invokevirtual 136	com/google/android/music/sync/common/DownstreamMerger:onEndMergeBlock	(Z)V
    //   208: aload 16
    //   210: athrow
    //   211: astore 17
    //   213: iconst_1
    //   214: istore_1
    //   215: new 52	com/google/android/music/sync/common/SoftSyncException
    //   218: dup
    //   219: ldc 138
    //   221: aload 17
    //   223: invokespecial 141	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   226: athrow
    //   227: astore 18
    //   229: iload_1
    //   230: ifeq +10 -> 240
    //   233: aload_0
    //   234: getfield 23	com/google/android/music/sync/common/DownstreamMerger:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   237: invokevirtual 99	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:kill	()V
    //   240: aload_0
    //   241: getfield 41	com/google/android/music/sync/common/DownstreamMerger:useVerboseLogging	Z
    //   244: ifeq +14 -> 258
    //   247: aload_0
    //   248: getfield 27	com/google/android/music/sync/common/DownstreamMerger:mTag	Ljava/lang/String;
    //   251: ldc 101
    //   253: invokestatic 94	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   256: istore 19
    //   258: aload 18
    //   260: athrow
    //   261: iconst_1
    //   262: istore 11
    //   264: aload_0
    //   265: iload 11
    //   267: invokevirtual 136	com/google/android/music/sync/common/DownstreamMerger:onEndMergeBlock	(Z)V
    //   270: goto -268 -> 2
    //   273: astore 17
    //   275: iconst_1
    //   276: istore_1
    //   277: new 72	java/lang/StringBuilder
    //   280: dup
    //   281: invokespecial 73	java/lang/StringBuilder:<init>	()V
    //   284: ldc 143
    //   286: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: astore 20
    //   291: aload 17
    //   293: invokevirtual 146	java/lang/RuntimeException:getLocalizedMessage	()Ljava/lang/String;
    //   296: astore 21
    //   298: aload 20
    //   300: aload 21
    //   302: invokevirtual 79	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: invokevirtual 88	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   308: astore 22
    //   310: aload_0
    //   311: getfield 27	com/google/android/music/sync/common/DownstreamMerger:mTag	Ljava/lang/String;
    //   314: aload 22
    //   316: aload 17
    //   318: invokestatic 150	android/util/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   321: istore 23
    //   323: new 50	com/google/android/music/sync/common/HardSyncException
    //   326: dup
    //   327: aload 22
    //   329: aload 17
    //   331: invokespecial 151	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   334: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   134	197	200	finally
    //   2	100	211	java/lang/InterruptedException
    //   202	211	211	java/lang/InterruptedException
    //   264	270	211	java/lang/InterruptedException
    //   2	100	227	finally
    //   202	211	227	finally
    //   215	227	227	finally
    //   264	270	227	finally
    //   277	335	227	finally
    //   2	100	273	java/lang/RuntimeException
    //   202	211	273	java/lang/RuntimeException
    //   264	270	273	java/lang/RuntimeException
  }

  public abstract void onEndMergeBlock(boolean paramBoolean)
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  public abstract void onStartMergeBlock()
    throws AuthenticatorException, HardSyncException, SoftSyncException;

  public abstract void processMergeItem(QueueableSyncEntity paramQueueableSyncEntity1, QueueableSyncEntity paramQueueableSyncEntity2)
    throws AuthenticatorException, HardSyncException, SoftSyncException;
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.common.DownstreamMerger
 * JD-Core Version:    0.6.2
 */