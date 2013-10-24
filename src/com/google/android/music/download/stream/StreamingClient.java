package com.google.android.music.download.stream;

import android.content.Context;
import android.database.Cursor;
import android.os.RemoteException;
import com.google.android.music.download.ContentIdentifier;
import com.google.android.music.download.DownloadProgress;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.download.DownloadRequest.Owner;
import com.google.android.music.download.IDownloadProgressListener;
import com.google.android.music.download.IDownloadQueueManager;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.download.cache.ICacheManager;
import com.google.android.music.download.cache.OutOfSpaceException;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.SongList;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncWorkers;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class StreamingClient
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private final List<StreamingContent> mAllStreamingContents;
  private final ICacheManager mCacheManager;
  private final Context mContext;
  private StreamingContent mCurrentStreamingContent;
  private final IDownloadQueueManager mDownloadQueueManager;
  private StreamingContent mNextStreamingContent;
  private final PrefetchedContentCache mPrefetchedContentCache;
  private final Object mRequestsLock;
  private volatile StreamingHttpServer mStreamingHttpServer;

  public StreamingClient(Context paramContext, IDownloadQueueManager paramIDownloadQueueManager, ICacheManager paramICacheManager)
  {
    Object localObject = new Object();
    this.mRequestsLock = localObject;
    LinkedList localLinkedList = new LinkedList();
    this.mAllStreamingContents = localLinkedList;
    PrefetchedContentCache localPrefetchedContentCache = new PrefetchedContentCache();
    this.mPrefetchedContentCache = localPrefetchedContentCache;
    this.mContext = paramContext;
    if (paramIDownloadQueueManager == null)
      throw new IllegalArgumentException("IDownloadQueueManager is null ");
    this.mDownloadQueueManager = paramIDownloadQueueManager;
    if (paramICacheManager == null)
      throw new IllegalArgumentException("ICacheManager is null ");
    this.mCacheManager = paramICacheManager;
  }

  private void clearContents(List<StreamingContent> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      StreamingContent localStreamingContent = (StreamingContent)localIterator.next();
      if (LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("clearContents: ");
        ContentIdentifier localContentIdentifier = localStreamingContent.getId();
        String str = localContentIdentifier;
        Log.d("StreamingClient", str);
      }
      ICacheManager localICacheManager = this.mCacheManager;
      localStreamingContent.clearFileIfNotSavable(localICacheManager);
    }
  }

  private DownloadRequest createDownloadRequest(ContentIdentifier paramContentIdentifier, int paramInt, long paramLong, boolean paramBoolean, SongList paramSongList, String[] paramArrayOfString)
    throws DownloadRequestException, OutOfSpaceException
  {
    Context localContext = this.mContext;
    SongList localSongList = paramSongList;
    ContentIdentifier localContentIdentifier1 = paramContentIdentifier;
    String[] arrayOfString = paramArrayOfString;
    Cursor localCursor = localSongList.getSongCursor(localContext, localContentIdentifier1, arrayOfString);
    if (localCursor == null)
    {
      Log.w("StreamingClient", "createDownloadRequest: song cursor is null");
      throw new DownloadRequestException("null song cursor");
    }
    try
    {
      if (!localCursor.moveToFirst())
      {
        Log.w("StreamingClient", "createDownloadRequest: failed to move cursor to first");
        throw new DownloadRequestException("empty song cursor");
      }
    }
    finally
    {
      Store.safeClose(localCursor);
    }
    int i = localCursor.getColumnIndexOrThrow("SourceId");
    String str1 = localCursor.getString(i);
    int j = localCursor.getColumnIndexOrThrow("domainParam");
    String str2 = localCursor.getString(j);
    int k = localCursor.getColumnIndexOrThrow("SourceAccount");
    int m = localCursor.getInt(k);
    int n = localCursor.getColumnIndexOrThrow("Size");
    long l1 = localCursor.getLong(n);
    int i1 = localCursor.getColumnIndexOrThrow("title");
    String str3 = localCursor.getString(i1);
    String str4 = str3;
    int i2 = 2;
    FileLocation localFileLocation;
    try
    {
      if (!paramContentIdentifier.isCacheable())
        i2 = 1;
      ICacheManager localICacheManager = this.mCacheManager;
      int i3 = DownloadRequest.Owner.MUSIC_PLAYBACK.ordinal();
      ContentIdentifier localContentIdentifier2 = paramContentIdentifier;
      localFileLocation = localICacheManager.getTempFileLocation(localContentIdentifier2, i3, l1, i2);
      if (localFileLocation == null)
      {
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = " file location is null";
        String str5 = String.format("createDownloadRequest:%s", arrayOfObject);
        Log.w("StreamingClient", str5);
        String str6 = " file location is null";
        throw new OutOfSpaceException(str6);
      }
    }
    catch (RemoteException localRemoteException1)
    {
      RemoteException localRemoteException2 = localRemoteException1;
      Log.e("StreamingClient", "Failed to get temp file location", localRemoteException2);
      RemoteException localRemoteException3 = localRemoteException1;
      throw new DownloadRequestException(localRemoteException3);
    }
    DownloadRequest.Owner localOwner = DownloadRequest.Owner.MUSIC_PLAYBACK;
    ContentIdentifier localContentIdentifier3 = paramContentIdentifier;
    int i4 = paramInt;
    long l2 = paramLong;
    boolean bool = paramBoolean;
    DownloadRequest localDownloadRequest = new DownloadRequest(localContentIdentifier3, str4, str1, m, i4, localOwner, l2, true, localFileLocation, str2, bool);
    Store.safeClose(localCursor);
    return localDownloadRequest;
  }

  private void handleCancelCurrentStream()
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      StreamingContent localStreamingContent1 = this.mCurrentStreamingContent;
      arrayOfObject[0] = localStreamingContent1;
      StreamingContent localStreamingContent2 = this.mNextStreamingContent;
      arrayOfObject[1] = localStreamingContent2;
      String str = String.format("handleCancelCurrentStream: current=%s next=%s", arrayOfObject);
      Log.d("StreamingClient", str);
    }
    if (this.mCurrentStreamingContent == null)
      return;
    this.mCurrentStreamingContent.setWaitingContentTypeAllowed(false);
    this.mCurrentStreamingContent = null;
  }

  private void handleCancelNextStreamIfMatching(StreamingContent paramStreamingContent)
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      StreamingContent localStreamingContent1 = this.mCurrentStreamingContent;
      arrayOfObject[0] = localStreamingContent1;
      StreamingContent localStreamingContent2 = this.mNextStreamingContent;
      arrayOfObject[1] = localStreamingContent2;
      String str = String.format("handleCancelNextStreamIfMatching: current=%s next=%s", arrayOfObject);
      Log.d("StreamingClient", str);
    }
    if (this.mNextStreamingContent == null)
      return;
    if (this.mNextStreamingContent != paramStreamingContent)
      return;
    this.mNextStreamingContent.setWaitingContentTypeAllowed(false);
    this.mNextStreamingContent = null;
  }

  private int prefetchIndexToPriority(int paramInt)
  {
    int i;
    switch (paramInt)
    {
    default:
      i = DownloadRequest.PRIORITY_PREFETCH4;
    case 0:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return i;
      i = DownloadRequest.PRIORITY_PREFETCH1;
      continue;
      i = DownloadRequest.PRIORITY_PREFETCH2;
      continue;
      i = DownloadRequest.PRIORITY_PREFETCH3;
      continue;
      i = DownloadRequest.PRIORITY_PREFETCH4;
    }
  }

  /** @deprecated */
  // ERROR //
  private String streamTrackSynchronized(ContentIdentifier paramContentIdentifier, long paramLong, IDownloadProgressListener paramIDownloadProgressListener, boolean paramBoolean1, boolean paramBoolean2, SongList paramSongList, String[] paramArrayOfString, ContentIdentifier[] paramArrayOfContentIdentifier)
    throws DownloadRequestException, OutOfSpaceException
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic 43	com/google/android/music/download/stream/StreamingClient:LOGV	Z
    //   5: ifeq +96 -> 101
    //   8: aload_0
    //   9: getfield 49	com/google/android/music/download/stream/StreamingClient:mRequestsLock	Ljava/lang/Object;
    //   12: astore 10
    //   14: aload 10
    //   16: monitorenter
    //   17: iconst_4
    //   18: anewarray 4	java/lang/Object
    //   21: astore 11
    //   23: aload 11
    //   25: iconst_0
    //   26: aload_1
    //   27: aastore
    //   28: aload_0
    //   29: getfield 54	com/google/android/music/download/stream/StreamingClient:mAllStreamingContents	Ljava/util/List;
    //   32: astore 12
    //   34: ldc_w 278
    //   37: aload 12
    //   39: invokestatic 282	com/google/android/music/download/stream/StreamingContent:contentListToString	(Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   42: astore 13
    //   44: aload 11
    //   46: iconst_1
    //   47: aload 13
    //   49: aastore
    //   50: aload_0
    //   51: getfield 59	com/google/android/music/download/stream/StreamingClient:mPrefetchedContentCache	Lcom/google/android/music/download/stream/PrefetchedContentCache;
    //   54: invokevirtual 285	com/google/android/music/download/stream/PrefetchedContentCache:size	()I
    //   57: invokestatic 291	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   60: astore 14
    //   62: aload 11
    //   64: iconst_2
    //   65: aload 14
    //   67: aastore
    //   68: iload 5
    //   70: invokestatic 296	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   73: astore 15
    //   75: aload 11
    //   77: iconst_3
    //   78: aload 15
    //   80: aastore
    //   81: ldc_w 298
    //   84: aload 11
    //   86: invokestatic 228	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   89: astore 16
    //   91: ldc 124
    //   93: aload 16
    //   95: invokestatic 130	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   98: aload 10
    //   100: monitorexit
    //   101: getstatic 301	com/google/android/music/download/DownloadRequest:PRIORITY_STREAM	I
    //   104: astore 17
    //   106: iconst_2
    //   107: istore 18
    //   109: iload 5
    //   111: ifne +11 -> 122
    //   114: getstatic 264	com/google/android/music/download/DownloadRequest:PRIORITY_PREFETCH1	I
    //   117: istore 17
    //   119: iconst_3
    //   120: istore 18
    //   122: new 51	java/util/LinkedList
    //   125: dup
    //   126: invokespecial 52	java/util/LinkedList:<init>	()V
    //   129: astore 19
    //   131: aload_0
    //   132: astore 20
    //   134: aload_1
    //   135: astore 21
    //   137: lload_2
    //   138: lstore 22
    //   140: iload 6
    //   142: istore 24
    //   144: aload 7
    //   146: astore 25
    //   148: aload 8
    //   150: astore 26
    //   152: aload 20
    //   154: aload 21
    //   156: aload 17
    //   158: lload 22
    //   160: iload 24
    //   162: aload 25
    //   164: aload 26
    //   166: invokespecial 303	com/google/android/music/download/stream/StreamingClient:createDownloadRequest	(Lcom/google/android/music/download/ContentIdentifier;IJZLcom/google/android/music/medialist/SongList;[Ljava/lang/String;)Lcom/google/android/music/download/DownloadRequest;
    //   169: astore 27
    //   171: aload_0
    //   172: getfield 49	com/google/android/music/download/stream/StreamingClient:mRequestsLock	Ljava/lang/Object;
    //   175: astore 10
    //   177: aload 10
    //   179: monitorenter
    //   180: aload_0
    //   181: getfield 59	com/google/android/music/download/stream/StreamingClient:mPrefetchedContentCache	Lcom/google/android/music/download/stream/PrefetchedContentCache;
    //   184: astore 28
    //   186: aload 27
    //   188: astore 29
    //   190: aload 28
    //   192: aload 29
    //   194: invokevirtual 307	com/google/android/music/download/stream/PrefetchedContentCache:findContent	(Lcom/google/android/music/download/DownloadRequest;)Lcom/google/android/music/download/stream/StreamingContent;
    //   197: astore 30
    //   199: iload 5
    //   201: ifne +39 -> 240
    //   204: aload_0
    //   205: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   208: ifnull +32 -> 240
    //   211: aload_0
    //   212: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   215: invokevirtual 311	com/google/android/music/download/stream/StreamingContent:getDownloadRequest	()Lcom/google/android/music/download/DownloadRequest;
    //   218: astore 31
    //   220: aload 27
    //   222: astore 32
    //   224: aload 31
    //   226: aload 32
    //   228: invokevirtual 315	com/google/android/music/download/DownloadRequest:equals	(Ljava/lang/Object;)Z
    //   231: ifeq +9 -> 240
    //   234: aload_0
    //   235: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   238: astore 30
    //   240: aload 10
    //   242: monitorexit
    //   243: aload 30
    //   245: ifnonnull +50 -> 295
    //   248: aload 19
    //   250: astore 33
    //   252: aload 27
    //   254: astore 34
    //   256: aload 33
    //   258: aload 34
    //   260: invokeinterface 318 2 0
    //   265: istore 35
    //   267: new 102	com/google/android/music/download/stream/StreamingContent
    //   270: astore 30
    //   272: aload_0
    //   273: getfield 61	com/google/android/music/download/stream/StreamingClient:mContext	Landroid/content/Context;
    //   276: astore 36
    //   278: aload 30
    //   280: astore 37
    //   282: aload 27
    //   284: astore 38
    //   286: aload 37
    //   288: aload 36
    //   290: aload 38
    //   292: invokespecial 321	com/google/android/music/download/stream/StreamingContent:<init>	(Landroid/content/Context;Lcom/google/android/music/download/DownloadRequest;)V
    //   295: new 51	java/util/LinkedList
    //   298: dup
    //   299: invokespecial 52	java/util/LinkedList:<init>	()V
    //   302: astore 39
    //   304: aload_0
    //   305: getfield 49	com/google/android/music/download/stream/StreamingClient:mRequestsLock	Ljava/lang/Object;
    //   308: astore 10
    //   310: aload 10
    //   312: monitorenter
    //   313: iload 5
    //   315: ifeq +408 -> 723
    //   318: aload_0
    //   319: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   322: astore 40
    //   324: aload 30
    //   326: astore 41
    //   328: aload 40
    //   330: aload 41
    //   332: if_acmpeq +7 -> 339
    //   335: aload_0
    //   336: invokespecial 323	com/google/android/music/download/stream/StreamingClient:handleCancelCurrentStream	()V
    //   339: aload 30
    //   341: astore 42
    //   343: aload_0
    //   344: aload 42
    //   346: putfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   349: aload_0
    //   350: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   353: iconst_1
    //   354: invokevirtual 254	com/google/android/music/download/stream/StreamingContent:setWaitingContentTypeAllowed	(Z)V
    //   357: aload_0
    //   358: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   361: ifnull +20 -> 381
    //   364: aload_0
    //   365: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   368: astore 43
    //   370: aload 39
    //   372: aload 43
    //   374: invokeinterface 318 2 0
    //   379: istore 44
    //   381: aload_0
    //   382: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   385: ifnull +20 -> 405
    //   388: aload_0
    //   389: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   392: astore 45
    //   394: aload 39
    //   396: aload 45
    //   398: invokeinterface 318 2 0
    //   403: istore 46
    //   405: aload_0
    //   406: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   409: astore 47
    //   411: aload_0
    //   412: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   415: astore 48
    //   417: aload 10
    //   419: monitorexit
    //   420: aload_0
    //   421: getfield 325	com/google/android/music/download/stream/StreamingClient:mStreamingHttpServer	Lcom/google/android/music/download/stream/StreamingHttpServer;
    //   424: astore 49
    //   426: aload 49
    //   428: ifnonnull +32 -> 460
    //   431: aload_0
    //   432: getfield 61	com/google/android/music/download/stream/StreamingClient:mContext	Landroid/content/Context;
    //   435: astore 50
    //   437: new 327	com/google/android/music/download/stream/StreamingHttpServer
    //   440: dup
    //   441: aload 50
    //   443: invokespecial 330	com/google/android/music/download/stream/StreamingHttpServer:<init>	(Landroid/content/Context;)V
    //   446: astore 51
    //   448: aload_0
    //   449: aload 51
    //   451: putfield 325	com/google/android/music/download/stream/StreamingClient:mStreamingHttpServer	Lcom/google/android/music/download/stream/StreamingHttpServer;
    //   454: aload_0
    //   455: getfield 325	com/google/android/music/download/stream/StreamingClient:mStreamingHttpServer	Lcom/google/android/music/download/stream/StreamingHttpServer;
    //   458: astore 49
    //   460: aconst_null
    //   461: astore 52
    //   463: aload 49
    //   465: ifnull +336 -> 801
    //   468: aload 49
    //   470: astore 53
    //   472: aload 30
    //   474: astore 54
    //   476: aload 48
    //   478: astore 55
    //   480: aload 53
    //   482: aload 54
    //   484: aload 47
    //   486: aload 55
    //   488: invokevirtual 334	com/google/android/music/download/stream/StreamingHttpServer:serveStream	(Lcom/google/android/music/download/stream/StreamingContent;Lcom/google/android/music/download/stream/StreamingContent;Lcom/google/android/music/download/stream/StreamingContent;)Ljava/lang/String;
    //   491: astore 52
    //   493: new 51	java/util/LinkedList
    //   496: dup
    //   497: invokespecial 52	java/util/LinkedList:<init>	()V
    //   500: astore 56
    //   502: iconst_0
    //   503: istore 57
    //   505: aload 9
    //   507: ifnull +480 -> 987
    //   510: aload_0
    //   511: getfield 49	com/google/android/music/download/stream/StreamingClient:mRequestsLock	Ljava/lang/Object;
    //   514: astore 10
    //   516: aload 10
    //   518: monitorenter
    //   519: aload_0
    //   520: getfield 59	com/google/android/music/download/stream/StreamingClient:mPrefetchedContentCache	Lcom/google/android/music/download/stream/PrefetchedContentCache;
    //   523: astore 58
    //   525: aload 9
    //   527: astore 59
    //   529: aload 58
    //   531: aload 59
    //   533: invokevirtual 338	com/google/android/music/download/stream/PrefetchedContentCache:pruneNotMatching	([Lcom/google/android/music/download/ContentIdentifier;)Ljava/util/List;
    //   536: astore 60
    //   538: aload 56
    //   540: aload 60
    //   542: invokeinterface 342 2 0
    //   547: istore 61
    //   549: aload 10
    //   551: monitorexit
    //   552: aload 9
    //   554: astore 62
    //   556: aload 62
    //   558: arraylength
    //   559: istore 63
    //   561: iconst_0
    //   562: istore 64
    //   564: iload 57
    //   566: istore 65
    //   568: iload 64
    //   570: istore 66
    //   572: iload 63
    //   574: istore 67
    //   576: iload 66
    //   578: iload 67
    //   580: if_icmpge +403 -> 983
    //   583: aload 62
    //   585: iload 64
    //   587: aaload
    //   588: astore 68
    //   590: iload 65
    //   592: iconst_1
    //   593: iadd
    //   594: istore 57
    //   596: aload_0
    //   597: astore 69
    //   599: iload 65
    //   601: istore 70
    //   603: aload 69
    //   605: iload 70
    //   607: invokespecial 344	com/google/android/music/download/stream/StreamingClient:prefetchIndexToPriority	(I)I
    //   610: astore 71
    //   612: aload 71
    //   614: astore 17
    //   616: aconst_null
    //   617: astore 10
    //   619: iconst_0
    //   620: istore 72
    //   622: aload_0
    //   623: astore 73
    //   625: aload 7
    //   627: astore 74
    //   629: aload 8
    //   631: astore 75
    //   633: aload 73
    //   635: aload 68
    //   637: aload 17
    //   639: aload 10
    //   641: iload 72
    //   643: aload 74
    //   645: aload 75
    //   647: invokespecial 303	com/google/android/music/download/stream/StreamingClient:createDownloadRequest	(Lcom/google/android/music/download/ContentIdentifier;IJZLcom/google/android/music/medialist/SongList;[Ljava/lang/String;)Lcom/google/android/music/download/DownloadRequest;
    //   650: astore 71
    //   652: aload 71
    //   654: astore 76
    //   656: aload_0
    //   657: getfield 49	com/google/android/music/download/stream/StreamingClient:mRequestsLock	Ljava/lang/Object;
    //   660: astore 10
    //   662: aload 10
    //   664: monitorenter
    //   665: aload_0
    //   666: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   669: ifnull +198 -> 867
    //   672: aload_0
    //   673: getfield 246	com/google/android/music/download/stream/StreamingClient:mCurrentStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   676: aload 68
    //   678: invokevirtual 348	com/google/android/music/download/stream/StreamingContent:hasId	(Lcom/google/android/music/download/ContentIdentifier;)Z
    //   681: ifeq +186 -> 867
    //   684: aload 10
    //   686: monitorexit
    //   687: iload 64
    //   689: iconst_1
    //   690: iadd
    //   691: istore 64
    //   693: iload 57
    //   695: istore 65
    //   697: goto -129 -> 568
    //   700: astore 73
    //   702: aload 10
    //   704: monitorexit
    //   705: aload 73
    //   707: athrow
    //   708: astore 77
    //   710: aload_0
    //   711: monitorexit
    //   712: aload 77
    //   714: athrow
    //   715: astore 73
    //   717: aload 10
    //   719: monitorexit
    //   720: aload 73
    //   722: athrow
    //   723: aload_0
    //   724: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   727: astore 78
    //   729: aload 30
    //   731: astore 79
    //   733: aload 78
    //   735: aload 79
    //   737: if_acmpeq +15 -> 752
    //   740: aload_0
    //   741: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   744: astore 80
    //   746: aload_0
    //   747: aload 80
    //   749: invokespecial 82	com/google/android/music/download/stream/StreamingClient:handleCancelNextStreamIfMatching	(Lcom/google/android/music/download/stream/StreamingContent;)V
    //   752: aload 30
    //   754: astore 81
    //   756: aload_0
    //   757: aload 81
    //   759: putfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   762: aload_0
    //   763: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   766: iconst_1
    //   767: invokevirtual 254	com/google/android/music/download/stream/StreamingContent:setWaitingContentTypeAllowed	(Z)V
    //   770: goto -413 -> 357
    //   773: astore 73
    //   775: aload 10
    //   777: monitorexit
    //   778: aload 73
    //   780: athrow
    //   781: astore 82
    //   783: ldc 124
    //   785: ldc_w 350
    //   788: aload 82
    //   790: invokestatic 235	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   793: aconst_null
    //   794: astore 52
    //   796: aload_0
    //   797: monitorexit
    //   798: aload 52
    //   800: areturn
    //   801: ldc 124
    //   803: ldc_w 352
    //   806: invokestatic 153	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   809: goto -316 -> 493
    //   812: astore 73
    //   814: aload 10
    //   816: monitorexit
    //   817: aload 73
    //   819: athrow
    //   820: astore 83
    //   822: iconst_2
    //   823: anewarray 4	java/lang/Object
    //   826: astore 84
    //   828: aload 84
    //   830: iconst_0
    //   831: aload 68
    //   833: aastore
    //   834: aload 83
    //   836: invokevirtual 355	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   839: astore 85
    //   841: aload 84
    //   843: iconst_1
    //   844: aload 85
    //   846: aastore
    //   847: ldc_w 357
    //   850: aload 84
    //   852: invokestatic 228	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   855: astore 86
    //   857: ldc 124
    //   859: aload 86
    //   861: invokestatic 153	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   864: goto -177 -> 687
    //   867: aload_0
    //   868: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   871: ifnull +29 -> 900
    //   874: aload_0
    //   875: getfield 248	com/google/android/music/download/stream/StreamingClient:mNextStreamingContent	Lcom/google/android/music/download/stream/StreamingContent;
    //   878: aload 68
    //   880: invokevirtual 348	com/google/android/music/download/stream/StreamingContent:hasId	(Lcom/google/android/music/download/ContentIdentifier;)Z
    //   883: ifeq +17 -> 900
    //   886: aload 10
    //   888: monitorexit
    //   889: goto -202 -> 687
    //   892: astore 73
    //   894: aload 10
    //   896: monitorexit
    //   897: aload 73
    //   899: athrow
    //   900: aload_0
    //   901: getfield 59	com/google/android/music/download/stream/StreamingClient:mPrefetchedContentCache	Lcom/google/android/music/download/stream/PrefetchedContentCache;
    //   904: astore 87
    //   906: aload 76
    //   908: astore 88
    //   910: aload 87
    //   912: aload 88
    //   914: invokevirtual 307	com/google/android/music/download/stream/PrefetchedContentCache:findContent	(Lcom/google/android/music/download/DownloadRequest;)Lcom/google/android/music/download/stream/StreamingContent;
    //   917: astore 89
    //   919: aload 10
    //   921: monitorexit
    //   922: aload 89
    //   924: ifnonnull +45 -> 969
    //   927: aload 19
    //   929: astore 90
    //   931: aload 76
    //   933: astore 91
    //   935: aload 90
    //   937: aload 91
    //   939: invokeinterface 318 2 0
    //   944: istore 92
    //   946: aload_0
    //   947: getfield 61	com/google/android/music/download/stream/StreamingClient:mContext	Landroid/content/Context;
    //   950: astore 93
    //   952: aload 76
    //   954: astore 94
    //   956: new 102	com/google/android/music/download/stream/StreamingContent
    //   959: dup
    //   960: aload 93
    //   962: aload 94
    //   964: invokespecial 321	com/google/android/music/download/stream/StreamingContent:<init>	(Landroid/content/Context;Lcom/google/android/music/download/DownloadRequest;)V
    //   967: astore 89
    //   969: aload 39
    //   971: aload 89
    //   973: invokeinterface 318 2 0
    //   978: istore 95
    //   980: goto -293 -> 687
    //   983: iload 65
    //   985: istore 96
    //   987: aload_0
    //   988: getfield 49	com/google/android/music/download/stream/StreamingClient:mRequestsLock	Ljava/lang/Object;
    //   991: astore 10
    //   993: aload 10
    //   995: monitorenter
    //   996: aload_0
    //   997: getfield 54	com/google/android/music/download/stream/StreamingClient:mAllStreamingContents	Ljava/util/List;
    //   1000: astore 97
    //   1002: aload 56
    //   1004: aload 97
    //   1006: invokeinterface 342 2 0
    //   1011: istore 98
    //   1013: aload_0
    //   1014: getfield 54	com/google/android/music/download/stream/StreamingClient:mAllStreamingContents	Ljava/util/List;
    //   1017: invokeinterface 360 1 0
    //   1022: aload_0
    //   1023: getfield 54	com/google/android/music/download/stream/StreamingClient:mAllStreamingContents	Ljava/util/List;
    //   1026: aload 39
    //   1028: invokeinterface 342 2 0
    //   1033: istore 99
    //   1035: getstatic 43	com/google/android/music/download/stream/StreamingClient:LOGV	Z
    //   1038: ifeq +48 -> 1086
    //   1041: iconst_1
    //   1042: anewarray 4	java/lang/Object
    //   1045: astore 100
    //   1047: aload_0
    //   1048: getfield 54	com/google/android/music/download/stream/StreamingClient:mAllStreamingContents	Ljava/util/List;
    //   1051: astore 101
    //   1053: ldc_w 278
    //   1056: aload 101
    //   1058: invokestatic 282	com/google/android/music/download/stream/StreamingContent:contentListToString	(Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   1061: astore 102
    //   1063: aload 100
    //   1065: iconst_0
    //   1066: aload 102
    //   1068: aastore
    //   1069: ldc_w 362
    //   1072: aload 100
    //   1074: invokestatic 228	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   1077: astore 103
    //   1079: ldc 124
    //   1081: aload 103
    //   1083: invokestatic 130	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   1086: aload 10
    //   1088: monitorexit
    //   1089: getstatic 43	com/google/android/music/download/stream/StreamingClient:LOGV	Z
    //   1092: ifeq +24 -> 1116
    //   1095: aload 56
    //   1097: astore 104
    //   1099: ldc_w 364
    //   1102: aload 104
    //   1104: invokestatic 282	com/google/android/music/download/stream/StreamingContent:contentListToString	(Ljava/lang/String;Ljava/util/List;)Ljava/lang/String;
    //   1107: astore 105
    //   1109: ldc 124
    //   1111: aload 105
    //   1113: invokestatic 130	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   1116: aload_0
    //   1117: astore 106
    //   1119: aload 56
    //   1121: astore 107
    //   1123: aload 106
    //   1125: aload 107
    //   1127: invokespecial 366	com/google/android/music/download/stream/StreamingClient:clearContents	(Ljava/util/List;)V
    //   1130: aload_0
    //   1131: getfield 70	com/google/android/music/download/stream/StreamingClient:mDownloadQueueManager	Lcom/google/android/music/download/IDownloadQueueManager;
    //   1134: astore 108
    //   1136: aload 19
    //   1138: astore 109
    //   1140: aload 4
    //   1142: astore 110
    //   1144: iload 18
    //   1146: istore 111
    //   1148: aload 108
    //   1150: aload 109
    //   1152: aload 110
    //   1154: iload 111
    //   1156: invokeinterface 372 4 0
    //   1161: goto -365 -> 796
    //   1164: astore 112
    //   1166: ldc 124
    //   1168: ldc_w 374
    //   1171: invokestatic 153	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   1174: new 138	com/google/android/music/download/stream/DownloadRequestException
    //   1177: dup
    //   1178: aload 112
    //   1180: invokespecial 238	com/google/android/music/download/stream/DownloadRequestException:<init>	(Ljava/lang/Exception;)V
    //   1183: athrow
    //   1184: astore 73
    //   1186: aload 10
    //   1188: monitorexit
    //   1189: aload 73
    //   1191: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   17	101	700	finally
    //   702	705	700	finally
    //   2	17	708	finally
    //   101	180	708	finally
    //   248	313	708	finally
    //   420	426	708	finally
    //   431	460	708	finally
    //   468	519	708	finally
    //   556	612	708	finally
    //   633	652	708	finally
    //   656	665	708	finally
    //   705	708	708	finally
    //   720	723	708	finally
    //   778	793	708	finally
    //   801	809	708	finally
    //   817	864	708	finally
    //   897	900	708	finally
    //   927	996	708	finally
    //   1089	1130	708	finally
    //   1130	1161	708	finally
    //   1166	1184	708	finally
    //   1189	1192	708	finally
    //   180	243	715	finally
    //   717	720	715	finally
    //   318	420	773	finally
    //   723	773	773	finally
    //   431	460	781	java/io/IOException
    //   519	552	812	finally
    //   814	817	812	finally
    //   633	652	820	java/lang/Exception
    //   665	687	892	finally
    //   867	892	892	finally
    //   900	922	892	finally
    //   1130	1161	1164	android/os/RemoteException
    //   996	1089	1184	finally
    //   1186	1189	1184	finally
  }

  public void cancelAndPurgeAllStreamingTracks()
  {
    try
    {
      IDownloadQueueManager localIDownloadQueueManager = this.mDownloadQueueManager;
      int i = DownloadRequest.Owner.MUSIC_PLAYBACK.ordinal();
      localIDownloadQueueManager.cancelAndPurge(i, 2);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.w("StreamingClient", "Failed to call the download queue manager");
    }
  }

  public void cancelNextStream()
  {
    final StreamingContent localStreamingContent = this.mNextStreamingContent;
    LoggableHandler localLoggableHandler = AsyncWorkers.sBackendServiceWorker;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        synchronized (StreamingClient.this.mRequestsLock)
        {
          StreamingClient localStreamingClient = StreamingClient.this;
          StreamingContent localStreamingContent = localStreamingContent;
          localStreamingClient.handleCancelNextStreamIfMatching(localStreamingContent);
          return;
        }
      }
    };
    AsyncWorkers.runAsync(localLoggableHandler, local1);
  }

  public void clearPrefetchedCache()
  {
    synchronized (this.mRequestsLock)
    {
      List localList = this.mPrefetchedContentCache.pruneNotMatching(null);
      clearContents(localList);
      return;
    }
  }

  public void currenStreamingPlayEnded()
  {
    if (LOGV)
    {
      Object[] arrayOfObject = new Object[2];
      StreamingContent localStreamingContent1 = this.mCurrentStreamingContent;
      arrayOfObject[0] = localStreamingContent1;
      StreamingContent localStreamingContent2 = this.mNextStreamingContent;
      arrayOfObject[1] = localStreamingContent2;
      String str = String.format("currenStreamingPlayEnded: current=%s next=%s", arrayOfObject);
      Log.d("StreamingClient", str);
    }
    StreamingContent localStreamingContent3;
    synchronized (this.mRequestsLock)
    {
      if (this.mCurrentStreamingContent == null)
        break label149;
      localStreamingContent3 = this.mCurrentStreamingContent;
      this.mCurrentStreamingContent = null;
      Iterator localIterator = this.mAllStreamingContents.iterator();
      while (localIterator.hasNext())
      {
        DownloadRequest localDownloadRequest = ((StreamingContent)localIterator.next()).getDownloadRequest();
        if (localStreamingContent3.hasRequest(localDownloadRequest))
          localIterator.remove();
      }
    }
    ICacheManager localICacheManager = this.mCacheManager;
    localStreamingContent3.clearFileIfNotSavable(localICacheManager);
    label149: StreamingContent localStreamingContent4 = this.mNextStreamingContent;
    this.mCurrentStreamingContent = localStreamingContent4;
    this.mNextStreamingContent = null;
  }

  public void destroy()
  {
    StreamingHttpServer localStreamingHttpServer = this.mStreamingHttpServer;
    if (localStreamingHttpServer == null)
      return;
    localStreamingHttpServer.shutdown();
  }

  public ContentIdentifier[] getFilteredIds()
  {
    LinkedList localLinkedList;
    synchronized (this.mRequestsLock)
    {
      localLinkedList = new LinkedList();
      if (this.mCurrentStreamingContent != null)
      {
        ContentIdentifier localContentIdentifier1 = this.mCurrentStreamingContent.getId();
        boolean bool1 = localLinkedList.add(localContentIdentifier1);
      }
      if (this.mNextStreamingContent != null)
      {
        ContentIdentifier localContentIdentifier2 = this.mNextStreamingContent.getId();
        boolean bool2 = localLinkedList.add(localContentIdentifier2);
      }
      Iterator localIterator = this.mAllStreamingContents.iterator();
      if (localIterator.hasNext())
      {
        ContentIdentifier localContentIdentifier3 = ((StreamingContent)localIterator.next()).getId();
        boolean bool3 = localLinkedList.add(localContentIdentifier3);
      }
    }
    ContentIdentifier[] arrayOfContentIdentifier1;
    if (localLinkedList.size() == 0)
      arrayOfContentIdentifier1 = null;
    while (true)
    {
      return arrayOfContentIdentifier1;
      ContentIdentifier[] arrayOfContentIdentifier2 = new ContentIdentifier[localLinkedList.size()];
      arrayOfContentIdentifier1 = (ContentIdentifier[])localLinkedList.toArray(arrayOfContentIdentifier2);
    }
  }

  public void handleDownloadProgress(DownloadProgress paramDownloadProgress)
  {
    synchronized (this.mRequestsLock)
    {
      Iterator localIterator = this.mAllStreamingContents.iterator();
      while (localIterator.hasNext())
      {
        StreamingContent localStreamingContent = (StreamingContent)localIterator.next();
        if (localStreamingContent.isMyProgress(paramDownloadProgress))
        {
          ICacheManager localICacheManager = this.mCacheManager;
          localStreamingContent.notifyDownloadProgress(paramDownloadProgress, localICacheManager);
          if (paramDownloadProgress.isFullCopy())
            this.mPrefetchedContentCache.store(localStreamingContent);
        }
      }
    }
  }

  public boolean isCurrentStreamingFullyBuffered()
  {
    synchronized (this.mRequestsLock)
    {
      if (this.mCurrentStreamingContent != null)
      {
        bool = this.mCurrentStreamingContent.isCompleted();
        return bool;
      }
      boolean bool = false;
    }
  }

  public boolean shouldFilter(String paramString)
  {
    boolean bool = true;
    synchronized (this.mRequestsLock)
    {
      if (LOGV)
      {
        Object[] arrayOfObject = new Object[2];
        arrayOfObject[0] = paramString;
        List localList = this.mAllStreamingContents;
        String str1 = StreamingContent.contentListToString("all", localList);
        arrayOfObject[1] = str1;
        String str2 = String.format("shouldFilter %s %s", arrayOfObject);
        Log.d("StreamingClient", str2);
      }
      Iterator localIterator = this.mAllStreamingContents.iterator();
      while (localIterator.hasNext())
        if (((StreamingContent)localIterator.next()).shouldFilter(paramString))
          return bool;
      bool = this.mPrefetchedContentCache.shouldFilter(paramString);
    }
  }

  public String streamTrack(ContentIdentifier paramContentIdentifier, long paramLong, IDownloadProgressListener paramIDownloadProgressListener, boolean paramBoolean1, boolean paramBoolean2, SongList paramSongList, String[] paramArrayOfString, ContentIdentifier[] paramArrayOfContentIdentifier)
    throws DownloadRequestException, OutOfSpaceException
  {
    return streamTrackSynchronized(paramContentIdentifier, paramLong, paramIDownloadProgressListener, paramBoolean1, paramBoolean2, paramSongList, paramArrayOfString, paramArrayOfContentIdentifier);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.StreamingClient
 * JD-Core Version:    0.6.2
 */