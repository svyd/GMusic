package com.google.android.music.sync.google;

import android.content.Context;
import com.google.android.music.store.Store;
import com.google.android.music.sync.common.AbstractSyncAdapter.DownstreamFetchQueue;
import com.google.android.music.sync.common.DownstreamReader;
import java.util.Map;

class MusicDownstreamReader extends DownstreamReader
{
  private Map<String, Object> mProtocolState;
  private String mRemoteAccount;
  private final Store mStore;

  public MusicDownstreamReader(AbstractSyncAdapter.DownstreamFetchQueue paramDownstreamFetchQueue, int paramInt, Context paramContext, Map<String, Object> paramMap, String paramString)
  {
    super(paramDownstreamFetchQueue, paramInt, paramString);
    Store localStore = Store.getInstance(paramContext);
    this.mStore = localStore;
    Object localObject = paramMap.get("remote_account");
    String str = ((Integer)Integer.class.cast(localObject)).toString();
    this.mRemoteAccount = str;
    this.mProtocolState = paramMap;
  }

  // ERROR //
  public void processServerEntity(com.google.android.music.sync.common.QueueableSyncEntity paramQueueableSyncEntity)
    throws com.google.android.music.sync.common.HardSyncException, com.google.android.music.sync.common.SoftSyncException
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aload_0
    //   3: getfield 25	com/google/android/music/sync/google/MusicDownstreamReader:mStore	Lcom/google/android/music/store/Store;
    //   6: invokevirtual 62	com/google/android/music/store/Store:beginRead	()Landroid/database/sqlite/SQLiteDatabase;
    //   9: astore_3
    //   10: aload_1
    //   11: instanceof 64
    //   14: ifeq +151 -> 165
    //   17: ldc 64
    //   19: aload_1
    //   20: invokevirtual 40	java/lang/Class:cast	(Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast 64	com/google/android/music/sync/google/model/Track
    //   26: getfield 67	com/google/android/music/sync/google/model/Track:mRemoteId	Ljava/lang/String;
    //   29: astore 4
    //   31: ldc 69
    //   33: iconst_2
    //   34: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   37: ifeq +33 -> 70
    //   40: iconst_1
    //   41: anewarray 77	java/lang/Object
    //   44: astore 5
    //   46: aload 5
    //   48: iconst_0
    //   49: aload 4
    //   51: aastore
    //   52: ldc 79
    //   54: aload 5
    //   56: invokestatic 85	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   59: astore 6
    //   61: ldc 69
    //   63: aload 6
    //   65: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   68: istore 7
    //   70: aload_0
    //   71: getfield 46	com/google/android/music/sync/google/MusicDownstreamReader:mRemoteAccount	Ljava/lang/String;
    //   74: astore 8
    //   76: aload_3
    //   77: aload 8
    //   79: aload 4
    //   81: aconst_null
    //   82: invokestatic 95	com/google/android/music/store/MusicFile:readMusicFile	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/music/store/MusicFile;)Lcom/google/android/music/store/MusicFile;
    //   85: astore 4
    //   87: aload 4
    //   89: ifnull +564 -> 653
    //   92: aload 4
    //   94: invokestatic 99	com/google/android/music/sync/google/model/Track:parse	(Lcom/google/android/music/store/MusicFile;)Lcom/google/android/music/sync/google/model/Track;
    //   97: astore 9
    //   99: aload 9
    //   101: astore 10
    //   103: aload_0
    //   104: getfield 25	com/google/android/music/sync/google/MusicDownstreamReader:mStore	Lcom/google/android/music/store/Store;
    //   107: aload_3
    //   108: invokevirtual 103	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   111: aload_2
    //   112: ifnull +27 -> 139
    //   115: aload_0
    //   116: getfield 107	com/google/android/music/sync/google/MusicDownstreamReader:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   119: astore 11
    //   121: new 109	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry
    //   124: dup
    //   125: aload_2
    //   126: aconst_null
    //   127: invokespecial 112	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry:<init>	(Lcom/google/android/music/sync/common/QueueableSyncEntity;Lcom/google/android/music/sync/common/QueueableSyncEntity;)V
    //   130: astore 12
    //   132: aload 11
    //   134: aload 12
    //   136: invokevirtual 118	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:put	(Ljava/lang/Object;)V
    //   139: aload_0
    //   140: getfield 107	com/google/android/music/sync/google/MusicDownstreamReader:mMergeQueue	Lcom/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue;
    //   143: astore 13
    //   145: new 109	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry
    //   148: dup
    //   149: aload_1
    //   150: aload 10
    //   152: invokespecial 112	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueueEntry:<init>	(Lcom/google/android/music/sync/common/QueueableSyncEntity;Lcom/google/android/music/sync/common/QueueableSyncEntity;)V
    //   155: astore 14
    //   157: aload 13
    //   159: aload 14
    //   161: invokevirtual 118	com/google/android/music/sync/common/AbstractSyncAdapter$DownstreamMergeQueue:put	(Ljava/lang/Object;)V
    //   164: return
    //   165: aload_1
    //   166: instanceof 120
    //   169: ifeq +89 -> 258
    //   172: ldc 120
    //   174: aload_1
    //   175: invokevirtual 40	java/lang/Class:cast	(Ljava/lang/Object;)Ljava/lang/Object;
    //   178: checkcast 120	com/google/android/music/sync/google/model/SyncablePlaylist
    //   181: getfield 121	com/google/android/music/sync/google/model/SyncablePlaylist:mRemoteId	Ljava/lang/String;
    //   184: astore 4
    //   186: ldc 69
    //   188: iconst_2
    //   189: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   192: ifeq +34 -> 226
    //   195: new 123	java/lang/StringBuilder
    //   198: dup
    //   199: invokespecial 126	java/lang/StringBuilder:<init>	()V
    //   202: ldc 128
    //   204: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: aload 4
    //   209: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   215: astore 15
    //   217: ldc 69
    //   219: aload 15
    //   221: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   224: istore 16
    //   226: aload_0
    //   227: getfield 46	com/google/android/music/sync/google/MusicDownstreamReader:mRemoteAccount	Ljava/lang/String;
    //   230: astore 17
    //   232: aload_3
    //   233: aload 17
    //   235: aload 4
    //   237: aconst_null
    //   238: invokestatic 139	com/google/android/music/store/PlayList:readPlayList	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/music/store/PlayList;)Lcom/google/android/music/store/PlayList;
    //   241: astore 4
    //   243: aload 4
    //   245: ifnull +402 -> 647
    //   248: aload 4
    //   250: invokestatic 142	com/google/android/music/sync/google/model/SyncablePlaylist:parse	(Lcom/google/android/music/store/PlayList;)Lcom/google/android/music/sync/google/model/SyncablePlaylist;
    //   253: astore 10
    //   255: goto -152 -> 103
    //   258: aload_1
    //   259: instanceof 144
    //   262: ifeq +221 -> 483
    //   265: aload_1
    //   266: checkcast 144	com/google/android/music/sync/google/model/SyncablePlaylistEntry
    //   269: astore 4
    //   271: aload 4
    //   273: getfield 145	com/google/android/music/sync/google/model/SyncablePlaylistEntry:mRemoteId	Ljava/lang/String;
    //   276: astore 18
    //   278: ldc 69
    //   280: iconst_2
    //   281: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   284: ifeq +34 -> 318
    //   287: new 123	java/lang/StringBuilder
    //   290: dup
    //   291: invokespecial 126	java/lang/StringBuilder:<init>	()V
    //   294: ldc 147
    //   296: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   299: aload 18
    //   301: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   304: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   307: astore 19
    //   309: ldc 69
    //   311: aload 19
    //   313: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   316: istore 20
    //   318: aload_0
    //   319: getfield 46	com/google/android/music/sync/google/MusicDownstreamReader:mRemoteAccount	Ljava/lang/String;
    //   322: astore 21
    //   324: aload_3
    //   325: aload 21
    //   327: aload 18
    //   329: aconst_null
    //   330: invokestatic 153	com/google/android/music/store/PlayList$Item:readItem	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/music/store/PlayList$Item;)Lcom/google/android/music/store/PlayList$Item;
    //   333: astore 18
    //   335: aload 18
    //   337: ifnull +304 -> 641
    //   340: aload 4
    //   342: invokevirtual 157	com/google/android/music/sync/google/model/SyncablePlaylistEntry:getSource	()I
    //   345: invokestatic 161	com/google/android/music/sync/google/model/SyncablePlaylistEntry:convertServerSourceToClientSourceType	(I)I
    //   348: istore 22
    //   350: aload 4
    //   352: getfield 164	com/google/android/music/sync/google/model/SyncablePlaylistEntry:mTrackId	Ljava/lang/String;
    //   355: iload 22
    //   357: aload 18
    //   359: invokestatic 167	com/google/android/music/sync/google/model/SyncablePlaylistEntry:parse	(Ljava/lang/String;ILcom/google/android/music/store/PlayList$Item;)Lcom/google/android/music/sync/google/model/SyncablePlaylistEntry;
    //   362: astore 18
    //   364: aload 4
    //   366: invokevirtual 171	com/google/android/music/sync/google/model/SyncablePlaylistEntry:isDeleted	()Z
    //   369: ifne +9 -> 378
    //   372: aload 4
    //   374: invokevirtual 175	com/google/android/music/sync/google/model/SyncablePlaylistEntry:getTrack	()Lcom/google/android/music/sync/google/model/Track;
    //   377: astore_2
    //   378: aload 4
    //   380: invokevirtual 157	com/google/android/music/sync/google/model/SyncablePlaylistEntry:getSource	()I
    //   383: iconst_2
    //   384: if_icmpne +92 -> 476
    //   387: aload_2
    //   388: ifnull +88 -> 476
    //   391: aload_2
    //   392: invokevirtual 178	com/google/android/music/sync/google/model/Track:getNormalizedNautilusId	()Ljava/lang/String;
    //   395: astore 4
    //   397: ldc 69
    //   399: iconst_2
    //   400: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   403: ifeq +33 -> 436
    //   406: new 123	java/lang/StringBuilder
    //   409: dup
    //   410: invokespecial 126	java/lang/StringBuilder:<init>	()V
    //   413: ldc 180
    //   415: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   418: aload_2
    //   419: invokevirtual 183	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   422: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   425: astore 23
    //   427: ldc 69
    //   429: aload 23
    //   431: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   434: istore 24
    //   436: ldc 69
    //   438: iconst_2
    //   439: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   442: ifeq +34 -> 476
    //   445: new 123	java/lang/StringBuilder
    //   448: dup
    //   449: invokespecial 126	java/lang/StringBuilder:<init>	()V
    //   452: ldc 185
    //   454: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   457: aload 4
    //   459: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   462: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   465: astore 25
    //   467: ldc 69
    //   469: aload 25
    //   471: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   474: istore 26
    //   476: aload 18
    //   478: astore 10
    //   480: goto -377 -> 103
    //   483: aload_1
    //   484: instanceof 187
    //   487: ifeq +89 -> 576
    //   490: ldc 187
    //   492: aload_1
    //   493: invokevirtual 40	java/lang/Class:cast	(Ljava/lang/Object;)Ljava/lang/Object;
    //   496: checkcast 187	com/google/android/music/sync/google/model/SyncableRadioStation
    //   499: getfield 188	com/google/android/music/sync/google/model/SyncableRadioStation:mRemoteId	Ljava/lang/String;
    //   502: astore 4
    //   504: ldc 69
    //   506: iconst_2
    //   507: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   510: ifeq +34 -> 544
    //   513: new 123	java/lang/StringBuilder
    //   516: dup
    //   517: invokespecial 126	java/lang/StringBuilder:<init>	()V
    //   520: ldc 190
    //   522: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   525: aload 4
    //   527: invokevirtual 132	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   530: invokevirtual 133	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   533: astore 27
    //   535: ldc 69
    //   537: aload 27
    //   539: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   542: istore 28
    //   544: aload_0
    //   545: getfield 46	com/google/android/music/sync/google/MusicDownstreamReader:mRemoteAccount	Ljava/lang/String;
    //   548: astore 29
    //   550: aload_3
    //   551: aload 29
    //   553: aload 4
    //   555: aconst_null
    //   556: invokestatic 196	com/google/android/music/store/RadioStation:read	(Landroid/database/sqlite/SQLiteDatabase;Ljava/lang/String;Ljava/lang/String;Lcom/google/android/music/store/RadioStation;)Lcom/google/android/music/store/RadioStation;
    //   559: astore 4
    //   561: aload 4
    //   563: ifnull +72 -> 635
    //   566: aload 4
    //   568: invokestatic 199	com/google/android/music/sync/google/model/SyncableRadioStation:parse	(Lcom/google/android/music/store/RadioStation;)Lcom/google/android/music/sync/google/model/SyncableRadioStation;
    //   571: astore 10
    //   573: goto -470 -> 103
    //   576: new 52	com/google/android/music/sync/common/HardSyncException
    //   579: dup
    //   580: ldc 201
    //   582: invokespecial 204	com/google/android/music/sync/common/HardSyncException:<init>	(Ljava/lang/String;)V
    //   585: athrow
    //   586: astore 30
    //   588: aload_0
    //   589: getfield 25	com/google/android/music/sync/google/MusicDownstreamReader:mStore	Lcom/google/android/music/store/Store;
    //   592: aload_3
    //   593: invokevirtual 103	com/google/android/music/store/Store:endRead	(Landroid/database/sqlite/SQLiteDatabase;)V
    //   596: aload 30
    //   598: athrow
    //   599: astore 31
    //   601: ldc 69
    //   603: iconst_2
    //   604: invokestatic 75	android/util/Log:isLoggable	(Ljava/lang/String;I)Z
    //   607: ifne +4 -> 611
    //   610: return
    //   611: ldc 69
    //   613: ldc 206
    //   615: invokestatic 89	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
    //   618: istore 32
    //   620: return
    //   621: astore 33
    //   623: new 54	com/google/android/music/sync/common/SoftSyncException
    //   626: dup
    //   627: ldc 208
    //   629: aload 33
    //   631: invokespecial 211	com/google/android/music/sync/common/SoftSyncException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   634: athrow
    //   635: aconst_null
    //   636: astore 10
    //   638: goto -535 -> 103
    //   641: aconst_null
    //   642: astore 18
    //   644: goto -280 -> 364
    //   647: aconst_null
    //   648: astore 10
    //   650: goto -547 -> 103
    //   653: aconst_null
    //   654: astore 10
    //   656: goto -553 -> 103
    //
    // Exception table:
    //   from	to	target	type
    //   10	99	586	finally
    //   165	586	586	finally
    //   115	164	599	com/google/android/music/sync/common/ClosableBlockingQueue$QueueClosedException
    //   115	164	621	java/lang/InterruptedException
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sync.google.MusicDownstreamReader
 * JD-Core Version:    0.6.2
 */