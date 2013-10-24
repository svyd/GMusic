package com.google.android.music.download;

import android.content.Context;
import android.net.NetworkInfo;
import android.os.RemoteException;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.music.download.cache.FileLocation;
import com.google.android.music.download.cp.CpOutputStream;
import com.google.android.music.eventlog.MusicEventLogger;
import com.google.android.music.io.ChunkedOutputStreamAdapter;
import com.google.android.music.log.Log;
import com.google.android.music.net.IStreamabilityChangeListener;
import com.google.android.music.net.IStreamabilityChangeListener.Stub;
import com.google.android.music.net.NetworkMonitorServiceConnection;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloadTaskImpl
  implements DownloadTask
{
  private final boolean LOGV;
  private BufferProgressOutputStream mBufferOut;
  private final Context mContext;
  private final IDownloadProgressListener mDownloadProgressListener;
  private final DownloadRequest mDownloadRequest;
  private final DownloadState mDownloadState;
  private final MusicEventLogger mEventLogger;
  private final MplayHandler mMplayHandler;
  private final MusicPreferences mMusicPreferences;
  private NetworkMonitorServiceConnection mNetworkMonitorServiceConnection;
  private long mServerSpecifiedRetryTime;
  private boolean mStarted;
  private volatile boolean mStopDownload;
  private IStreamabilityChangeListener mStreamabilityChangeListener;
  private AtomicBoolean mStreamingEnabled;
  private long mStreamingEnabledChangedTime;

  public DownloadTaskImpl(Context paramContext, DownloadRequest paramDownloadRequest, IDownloadProgressListener paramIDownloadProgressListener, MusicPreferences paramMusicPreferences, GoogleHttpClient paramGoogleHttpClient, NetworkMonitorServiceConnection paramNetworkMonitorServiceConnection)
  {
    boolean bool = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
    this.LOGV = bool;
    this.mStopDownload = false;
    this.mStarted = false;
    AtomicBoolean localAtomicBoolean = new AtomicBoolean(true);
    this.mStreamingEnabled = localAtomicBoolean;
    this.mStreamingEnabledChangedTime = 0L;
    this.mServerSpecifiedRetryTime = 0L;
    IStreamabilityChangeListener.Stub local1 = new IStreamabilityChangeListener.Stub()
    {
      public void onStreamabilityChanged(boolean paramAnonymousBoolean)
        throws RemoteException
      {
        if (DownloadTaskImpl.this.mStreamingEnabled.get() != paramAnonymousBoolean)
          return;
        synchronized (DownloadTaskImpl.this.mStreamingEnabled)
        {
          DownloadTaskImpl localDownloadTaskImpl = DownloadTaskImpl.this;
          long l1 = System.currentTimeMillis();
          long l2 = DownloadTaskImpl.access$102(localDownloadTaskImpl, l1);
          DownloadTaskImpl.this.mStreamingEnabled.set(paramAnonymousBoolean);
          DownloadTaskImpl.this.mStreamingEnabled.notifyAll();
          return;
        }
      }
    };
    this.mStreamabilityChangeListener = local1;
    this.mContext = paramContext;
    MusicEventLogger localMusicEventLogger = MusicEventLogger.getInstance(paramContext);
    this.mEventLogger = localMusicEventLogger;
    this.mDownloadRequest = paramDownloadRequest;
    DownloadState localDownloadState1 = new DownloadState();
    this.mDownloadState = localDownloadState1;
    this.mDownloadProgressListener = paramIDownloadProgressListener;
    Context localContext = this.mContext;
    DownloadRequest localDownloadRequest = this.mDownloadRequest;
    DownloadState localDownloadState2 = this.mDownloadState;
    MusicPreferences localMusicPreferences = paramMusicPreferences;
    GoogleHttpClient localGoogleHttpClient = paramGoogleHttpClient;
    MplayHandler localMplayHandler = new MplayHandler(localContext, localMusicPreferences, localGoogleHttpClient, localDownloadRequest, localDownloadState2);
    this.mMplayHandler = localMplayHandler;
    this.mNetworkMonitorServiceConnection = paramNetworkMonitorServiceConnection;
    this.mMusicPreferences = paramMusicPreferences;
  }

  // ERROR //
  private void handleRun()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 63	com/google/android/music/download/DownloadTaskImpl:mStarted	Z
    //   6: ifeq +18 -> 24
    //   9: new 124	java/lang/RuntimeException
    //   12: dup
    //   13: ldc 126
    //   15: invokespecial 129	java/lang/RuntimeException:<init>	(Ljava/lang/String;)V
    //   18: athrow
    //   19: astore_1
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_1
    //   23: athrow
    //   24: iconst_1
    //   25: istore_2
    //   26: iload_2
    //   27: istore_3
    //   28: aload_0
    //   29: iload_3
    //   30: putfield 63	com/google/android/music/download/DownloadTaskImpl:mStarted	Z
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_0
    //   36: getfield 83	com/google/android/music/download/DownloadTaskImpl:mContext	Landroid/content/Context;
    //   39: invokestatic 134	com/google/android/music/store/Store:getInstance	(Landroid/content/Context;)Lcom/google/android/music/store/Store;
    //   42: astore 4
    //   44: aload_0
    //   45: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   48: astore 5
    //   50: aload 4
    //   52: aload 5
    //   54: invokevirtual 138	com/google/android/music/store/Store:requiresDownload	(Lcom/google/android/music/download/DownloadRequest;)Z
    //   57: ifne +54 -> 111
    //   60: aload_0
    //   61: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   64: ifeq +42 -> 106
    //   67: new 140	java/lang/StringBuilder
    //   70: dup
    //   71: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   74: ldc 143
    //   76: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   79: astore 6
    //   81: aload_0
    //   82: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   85: astore 7
    //   87: aload 6
    //   89: aload 7
    //   91: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   94: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   97: astore 8
    //   99: ldc 156
    //   101: aload 8
    //   103: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   106: aload_0
    //   107: invokespecial 165	com/google/android/music/download/DownloadTaskImpl:updateCompleted	()V
    //   110: return
    //   111: aload_0
    //   112: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   115: astore 9
    //   117: invokestatic 171	com/google/android/music/net/NetworkMonitor:getRecommendedBitrate	()I
    //   120: istore 10
    //   122: aload 9
    //   124: iload 10
    //   126: invokevirtual 175	com/google/android/music/download/DownloadState:setRecommendedBitrate	(I)V
    //   129: aload_0
    //   130: invokespecial 178	com/google/android/music/download/DownloadTaskImpl:updateDownloading	()V
    //   133: iconst_0
    //   134: istore 11
    //   136: iconst_1
    //   137: istore 12
    //   139: aload_0
    //   140: getfield 109	com/google/android/music/download/DownloadTaskImpl:mNetworkMonitorServiceConnection	Lcom/google/android/music/net/NetworkMonitorServiceConnection;
    //   143: invokevirtual 184	com/google/android/music/net/NetworkMonitorServiceConnection:getNetworkMonitor	()Lcom/google/android/music/net/INetworkMonitor;
    //   146: astore 13
    //   148: aload 13
    //   150: ifnull +18 -> 168
    //   153: aload_0
    //   154: getfield 81	com/google/android/music/download/DownloadTaskImpl:mStreamabilityChangeListener	Lcom/google/android/music/net/IStreamabilityChangeListener;
    //   157: astore 14
    //   159: aload 13
    //   161: aload 14
    //   163: invokeinterface 190 2 0
    //   168: iconst_1
    //   169: istore 15
    //   171: aload_0
    //   172: getfield 61	com/google/android/music/download/DownloadTaskImpl:mStopDownload	Z
    //   175: astore 16
    //   177: aload 16
    //   179: ifnonnull +1354 -> 1533
    //   182: iload 12
    //   184: ifeq +1349 -> 1533
    //   187: aload_0
    //   188: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   191: ifeq +1335 -> 1526
    //   194: ldc 156
    //   196: astore 16
    //   198: new 140	java/lang/StringBuilder
    //   201: dup
    //   202: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   205: ldc 192
    //   207: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   210: istore 17
    //   212: iload 17
    //   214: istore 18
    //   216: iload 15
    //   218: iconst_1
    //   219: iadd
    //   220: istore 19
    //   222: iload 18
    //   224: iload 15
    //   226: invokevirtual 195	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   229: ldc 197
    //   231: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: astore 20
    //   236: aload_0
    //   237: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   240: astore 21
    //   242: aload 20
    //   244: aload 21
    //   246: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   249: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   252: astore 22
    //   254: aload 16
    //   256: aload 22
    //   258: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   261: invokestatic 203	java/lang/System:currentTimeMillis	()J
    //   264: lstore 23
    //   266: aload_0
    //   267: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   270: invokevirtual 206	com/google/android/music/download/DownloadState:getCompletedBytes	()J
    //   273: istore 17
    //   275: iload 17
    //   277: istore 25
    //   279: aload_0
    //   280: getfield 70	com/google/android/music/download/DownloadTaskImpl:mStreamingEnabled	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   283: invokevirtual 210	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   286: ifeq +324 -> 610
    //   289: aload_0
    //   290: invokevirtual 213	com/google/android/music/download/DownloadTaskImpl:download	()I
    //   293: istore 17
    //   295: iload 17
    //   297: istore 26
    //   299: aload_0
    //   300: getfield 107	com/google/android/music/download/DownloadTaskImpl:mMplayHandler	Lcom/google/android/music/download/MplayHandler;
    //   303: invokevirtual 216	com/google/android/music/download/MplayHandler:releaseConnection	()V
    //   306: invokestatic 203	java/lang/System:currentTimeMillis	()J
    //   309: lload 23
    //   311: lsub
    //   312: lstore 27
    //   314: aload_0
    //   315: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   318: invokevirtual 206	com/google/android/music/download/DownloadState:getCompletedBytes	()J
    //   321: iload 25
    //   323: lsub
    //   324: lstore 29
    //   326: lload 29
    //   328: ldc2_w 71
    //   331: lcmp
    //   332: ifle +22 -> 354
    //   335: aload_0
    //   336: getfield 83	com/google/android/music/download/DownloadTaskImpl:mContext	Landroid/content/Context;
    //   339: astore 31
    //   341: lload 27
    //   343: lstore 32
    //   345: aload 31
    //   347: lload 29
    //   349: lload 32
    //   351: invokestatic 220	com/google/android/music/net/NetworkMonitor:reportBitrate	(Landroid/content/Context;JJ)V
    //   354: iconst_0
    //   355: istore 30
    //   357: iconst_4
    //   358: istore 31
    //   360: iload 26
    //   362: iload 31
    //   364: if_icmpeq +16 -> 380
    //   367: aload_0
    //   368: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   371: invokevirtual 225	com/google/android/music/download/DownloadRequest:isRetryAllowed	()Z
    //   374: ifne +365 -> 739
    //   377: iconst_1
    //   378: istore 30
    //   380: iload 30
    //   382: ifeq +363 -> 745
    //   385: aload_0
    //   386: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   389: invokevirtual 229	com/google/android/music/download/DownloadState:getState	()Lcom/google/android/music/download/DownloadState$State;
    //   392: astore 36
    //   394: getstatic 235	com/google/android/music/download/DownloadState$State:FAILED	Lcom/google/android/music/download/DownloadState$State;
    //   397: astore 37
    //   399: aload 36
    //   401: astore 38
    //   403: aload 37
    //   405: astore 39
    //   407: aload 38
    //   409: aload 39
    //   411: if_acmpeq +16 -> 427
    //   414: aload_0
    //   415: astore 40
    //   417: iconst_1
    //   418: istore 41
    //   420: aload 40
    //   422: iload 41
    //   424: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   427: iconst_0
    //   428: istore 12
    //   430: iload 12
    //   432: ifeq +982 -> 1414
    //   435: invokestatic 243	java/lang/Thread:interrupted	()Z
    //   438: ifeq +976 -> 1414
    //   441: new 120	java/lang/InterruptedException
    //   444: dup
    //   445: invokespecial 244	java/lang/InterruptedException:<init>	()V
    //   448: athrow
    //   449: astore 42
    //   451: aload_0
    //   452: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   455: ifeq +47 -> 502
    //   458: new 140	java/lang/StringBuilder
    //   461: dup
    //   462: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   465: ldc 246
    //   467: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   470: astore 43
    //   472: aload_0
    //   473: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   476: astore 44
    //   478: aload 43
    //   480: aload 44
    //   482: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   485: ldc 248
    //   487: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   490: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   493: astore 45
    //   495: ldc 156
    //   497: aload 45
    //   499: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   502: aload_0
    //   503: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   506: invokevirtual 251	com/google/android/music/download/DownloadState:setExperiencedGlitch	()V
    //   509: aload_0
    //   510: getfield 109	com/google/android/music/download/DownloadTaskImpl:mNetworkMonitorServiceConnection	Lcom/google/android/music/net/NetworkMonitorServiceConnection;
    //   513: invokevirtual 184	com/google/android/music/net/NetworkMonitorServiceConnection:getNetworkMonitor	()Lcom/google/android/music/net/INetworkMonitor;
    //   516: astore 13
    //   518: aload 13
    //   520: ifnull +18 -> 538
    //   523: aload_0
    //   524: getfield 81	com/google/android/music/download/DownloadTaskImpl:mStreamabilityChangeListener	Lcom/google/android/music/net/IStreamabilityChangeListener;
    //   527: astore 46
    //   529: aload 13
    //   531: aload 46
    //   533: invokeinterface 254 2 0
    //   538: aload_0
    //   539: getfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   542: ifnonnull +4 -> 546
    //   545: return
    //   546: aload_0
    //   547: getfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   550: invokevirtual 261	com/google/android/music/download/BufferProgressOutputStream:close	()V
    //   553: return
    //   554: astore 47
    //   556: aload 47
    //   558: invokevirtual 264	java/io/IOException:getMessage	()Ljava/lang/String;
    //   561: astore 48
    //   563: ldc 156
    //   565: astore 49
    //   567: aload 48
    //   569: astore 50
    //   571: aload 49
    //   573: aload 50
    //   575: aload 47
    //   577: invokestatic 268	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   580: return
    //   581: astore 51
    //   583: aload 51
    //   585: invokevirtual 269	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   588: astore 52
    //   590: ldc 156
    //   592: astore 53
    //   594: aload 52
    //   596: astore 54
    //   598: aload 53
    //   600: aload 54
    //   602: aload 51
    //   604: invokestatic 272	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   607: goto -439 -> 168
    //   610: aload_0
    //   611: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   614: ifeq +11 -> 625
    //   617: ldc 156
    //   619: ldc_w 274
    //   622: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   625: iconst_1
    //   626: istore 26
    //   628: goto -329 -> 299
    //   631: astore 16
    //   633: aload_0
    //   634: getfield 107	com/google/android/music/download/DownloadTaskImpl:mMplayHandler	Lcom/google/android/music/download/MplayHandler;
    //   637: invokevirtual 216	com/google/android/music/download/MplayHandler:releaseConnection	()V
    //   640: invokestatic 203	java/lang/System:currentTimeMillis	()J
    //   643: lload 23
    //   645: lsub
    //   646: lstore 27
    //   648: aload_0
    //   649: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   652: invokevirtual 206	com/google/android/music/download/DownloadState:getCompletedBytes	()J
    //   655: iload 25
    //   657: lsub
    //   658: lstore 29
    //   660: lload 29
    //   662: ldc2_w 71
    //   665: lcmp
    //   666: ifle +22 -> 688
    //   669: aload_0
    //   670: getfield 83	com/google/android/music/download/DownloadTaskImpl:mContext	Landroid/content/Context;
    //   673: astore 55
    //   675: lload 27
    //   677: lstore 56
    //   679: aload 55
    //   681: lload 29
    //   683: lload 56
    //   685: invokestatic 220	com/google/android/music/net/NetworkMonitor:reportBitrate	(Landroid/content/Context;JJ)V
    //   688: aload 16
    //   690: athrow
    //   691: astore 58
    //   693: aload_0
    //   694: getfield 109	com/google/android/music/download/DownloadTaskImpl:mNetworkMonitorServiceConnection	Lcom/google/android/music/net/NetworkMonitorServiceConnection;
    //   697: invokevirtual 184	com/google/android/music/net/NetworkMonitorServiceConnection:getNetworkMonitor	()Lcom/google/android/music/net/INetworkMonitor;
    //   700: astore 13
    //   702: aload 13
    //   704: ifnull +18 -> 722
    //   707: aload_0
    //   708: getfield 81	com/google/android/music/download/DownloadTaskImpl:mStreamabilityChangeListener	Lcom/google/android/music/net/IStreamabilityChangeListener;
    //   711: astore 59
    //   713: aload 13
    //   715: aload 59
    //   717: invokeinterface 254 2 0
    //   722: aload_0
    //   723: getfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   726: ifnull +10 -> 736
    //   729: aload_0
    //   730: getfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   733: invokevirtual 261	com/google/android/music/download/BufferProgressOutputStream:close	()V
    //   736: aload 58
    //   738: athrow
    //   739: iconst_0
    //   740: istore 30
    //   742: goto -362 -> 380
    //   745: ldc2_w 275
    //   748: lstore 60
    //   750: iload 26
    //   752: tableswitch	default:+36 -> 788, 1:+391->1143, 2:+274->1026, 3:+365->1117, 4:+69->821, 5:+228->980
    //   789: nop
    //   790: f2l
    //   791: dup
    //   792: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   795: ldc_w 278
    //   798: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   801: iload 26
    //   803: invokevirtual 195	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   806: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   809: astore 62
    //   811: new 280	java/lang/IllegalStateException
    //   814: dup
    //   815: aload 62
    //   817: invokespecial 281	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
    //   820: athrow
    //   821: aload_0
    //   822: invokespecial 165	com/google/android/music/download/DownloadTaskImpl:updateCompleted	()V
    //   825: aload_0
    //   826: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   829: ifeq +93 -> 922
    //   832: new 140	java/lang/StringBuilder
    //   835: dup
    //   836: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   839: ldc_w 283
    //   842: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   845: astore 63
    //   847: aload_0
    //   848: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   851: astore 64
    //   853: aload 63
    //   855: aload 64
    //   857: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   860: ldc_w 285
    //   863: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   866: astore 65
    //   868: ldc2_w 286
    //   871: lload 29
    //   873: lmul
    //   874: l2d
    //   875: ldc2_w 288
    //   878: ddiv
    //   879: dstore 66
    //   881: lload 27
    //   883: l2d
    //   884: ldc2_w 288
    //   887: ddiv
    //   888: dstore 68
    //   890: dload 66
    //   892: dload 68
    //   894: ddiv
    //   895: dstore 70
    //   897: aload 65
    //   899: dload 70
    //   901: invokevirtual 292	java/lang/StringBuilder:append	(D)Ljava/lang/StringBuilder;
    //   904: ldc_w 294
    //   907: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   910: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   913: astore 72
    //   915: ldc 156
    //   917: aload 72
    //   919: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   922: iconst_0
    //   923: istore 12
    //   925: iload 12
    //   927: ifeq -497 -> 430
    //   930: lload 60
    //   932: ldc2_w 71
    //   935: lcmp
    //   936: ifgt +215 -> 1151
    //   939: new 140	java/lang/StringBuilder
    //   942: dup
    //   943: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   946: ldc_w 296
    //   949: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   952: astore 73
    //   954: lload 60
    //   956: lstore 74
    //   958: aload 73
    //   960: lload 74
    //   962: invokevirtual 299	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   965: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   968: astore 76
    //   970: new 301	java/lang/IllegalArgumentException
    //   973: dup
    //   974: aload 76
    //   976: invokespecial 302	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   979: athrow
    //   980: new 140	java/lang/StringBuilder
    //   983: dup
    //   984: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   987: ldc_w 304
    //   990: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   993: astore 77
    //   995: aload_0
    //   996: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   999: astore 78
    //   1001: aload 77
    //   1003: aload 78
    //   1005: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1008: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1011: astore 79
    //   1013: ldc 156
    //   1015: aload 79
    //   1017: invokestatic 306	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   1020: iconst_0
    //   1021: istore 12
    //   1023: goto -98 -> 925
    //   1026: aload_0
    //   1027: getfield 76	com/google/android/music/download/DownloadTaskImpl:mServerSpecifiedRetryTime	J
    //   1030: ldc2_w 71
    //   1033: lcmp
    //   1034: ifle +14 -> 1048
    //   1037: aload_0
    //   1038: getfield 76	com/google/android/music/download/DownloadTaskImpl:mServerSpecifiedRetryTime	J
    //   1041: ldc2_w 307
    //   1044: lcmp
    //   1045: ifle +63 -> 1108
    //   1048: new 140	java/lang/StringBuilder
    //   1051: dup
    //   1052: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   1055: ldc_w 310
    //   1058: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1061: astore 80
    //   1063: aload_0
    //   1064: getfield 76	com/google/android/music/download/DownloadTaskImpl:mServerSpecifiedRetryTime	J
    //   1067: lstore 81
    //   1069: aload 80
    //   1071: lload 81
    //   1073: invokevirtual 299	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   1076: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1079: astore 83
    //   1081: ldc 156
    //   1083: aload 83
    //   1085: invokestatic 312	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   1088: aload_0
    //   1089: astore 84
    //   1091: bipush 10
    //   1093: istore 85
    //   1095: aload 84
    //   1097: iload 85
    //   1099: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   1102: iconst_0
    //   1103: istore 12
    //   1105: goto -180 -> 925
    //   1108: aload_0
    //   1109: getfield 76	com/google/android/music/download/DownloadTaskImpl:mServerSpecifiedRetryTime	J
    //   1112: lstore 60
    //   1114: goto -189 -> 925
    //   1117: aload_0
    //   1118: getfield 74	com/google/android/music/download/DownloadTaskImpl:mStreamingEnabledChangedTime	J
    //   1121: lload 23
    //   1123: lcmp
    //   1124: ifle +11 -> 1135
    //   1127: ldc2_w 313
    //   1130: lstore 60
    //   1132: goto -207 -> 925
    //   1135: ldc2_w 275
    //   1138: lstore 60
    //   1140: goto -215 -> 925
    //   1143: ldc2_w 275
    //   1146: lstore 60
    //   1148: goto -223 -> 925
    //   1151: aload_0
    //   1152: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   1155: invokevirtual 251	com/google/android/music/download/DownloadState:setExperiencedGlitch	()V
    //   1158: new 140	java/lang/StringBuilder
    //   1161: dup
    //   1162: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   1165: ldc_w 316
    //   1168: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1171: astore 86
    //   1173: aload_0
    //   1174: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   1177: astore 87
    //   1179: aload 86
    //   1181: aload 87
    //   1183: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1186: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1189: astore 88
    //   1191: ldc 156
    //   1193: aload 88
    //   1195: invokestatic 312	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   1198: lload 29
    //   1200: ldc2_w 317
    //   1203: lcmp
    //   1204: ifge +44 -> 1248
    //   1207: iload 11
    //   1209: iconst_1
    //   1210: iadd
    //   1211: istore 89
    //   1213: iconst_5
    //   1214: istore 90
    //   1216: iload 89
    //   1218: iload 90
    //   1220: if_icmplt +28 -> 1248
    //   1223: ldc 156
    //   1225: ldc_w 320
    //   1228: invokestatic 312	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;)V
    //   1231: aload_0
    //   1232: astore 91
    //   1234: bipush 9
    //   1236: istore 92
    //   1238: aload 91
    //   1240: iload 92
    //   1242: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   1245: goto -736 -> 509
    //   1248: aload_0
    //   1249: getfield 70	com/google/android/music/download/DownloadTaskImpl:mStreamingEnabled	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   1252: astore 18
    //   1254: aload 18
    //   1256: monitorenter
    //   1257: aload_0
    //   1258: getfield 70	com/google/android/music/download/DownloadTaskImpl:mStreamingEnabled	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   1261: invokevirtual 210	java/util/concurrent/atomic/AtomicBoolean:get	()Z
    //   1264: ifeq +106 -> 1370
    //   1267: aload_0
    //   1268: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   1271: ifeq +68 -> 1339
    //   1274: lload 60
    //   1276: ldc2_w 275
    //   1279: lcmp
    //   1280: ifle +59 -> 1339
    //   1283: new 140	java/lang/StringBuilder
    //   1286: dup
    //   1287: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   1290: ldc_w 322
    //   1293: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1296: astore 93
    //   1298: lload 60
    //   1300: ldc2_w 275
    //   1303: ldiv
    //   1304: lstore 94
    //   1306: aload 93
    //   1308: lload 94
    //   1310: invokevirtual 299	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   1313: ldc_w 324
    //   1316: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1319: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1322: astore 96
    //   1324: ldc 156
    //   1326: astore 97
    //   1328: aload 96
    //   1330: astore 98
    //   1332: aload 97
    //   1334: aload 98
    //   1336: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   1339: aload_0
    //   1340: getfield 70	com/google/android/music/download/DownloadTaskImpl:mStreamingEnabled	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   1343: astore 99
    //   1345: lload 60
    //   1347: lstore 100
    //   1349: aload 99
    //   1351: lload 100
    //   1353: invokevirtual 328	java/lang/Object:wait	(J)V
    //   1356: aload 18
    //   1358: monitorexit
    //   1359: goto -929 -> 430
    //   1362: astore 16
    //   1364: aload 18
    //   1366: monitorexit
    //   1367: aload 16
    //   1369: athrow
    //   1370: aload_0
    //   1371: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   1374: ifeq +19 -> 1393
    //   1377: ldc 156
    //   1379: astore 102
    //   1381: ldc_w 330
    //   1384: astore 103
    //   1386: aload 102
    //   1388: aload 103
    //   1390: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   1393: aload_0
    //   1394: getfield 70	com/google/android/music/download/DownloadTaskImpl:mStreamingEnabled	Ljava/util/concurrent/atomic/AtomicBoolean;
    //   1397: astore 104
    //   1399: ldc2_w 331
    //   1402: lstore 105
    //   1404: aload 104
    //   1406: lload 105
    //   1408: invokevirtual 328	java/lang/Object:wait	(J)V
    //   1411: goto -55 -> 1356
    //   1414: iload 19
    //   1416: istore 15
    //   1418: goto -1247 -> 171
    //   1421: astore 107
    //   1423: aload 107
    //   1425: invokevirtual 269	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   1428: astore 108
    //   1430: ldc 156
    //   1432: astore 109
    //   1434: aload 108
    //   1436: astore 110
    //   1438: aload 109
    //   1440: aload 110
    //   1442: aload 107
    //   1444: invokestatic 272	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1447: goto -909 -> 538
    //   1450: astore 111
    //   1452: aload 111
    //   1454: invokevirtual 269	android/os/RemoteException:getMessage	()Ljava/lang/String;
    //   1457: astore 112
    //   1459: ldc 156
    //   1461: astore 113
    //   1463: aload 112
    //   1465: astore 114
    //   1467: aload 113
    //   1469: aload 114
    //   1471: aload 111
    //   1473: invokestatic 272	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1476: goto -754 -> 722
    //   1479: astore 115
    //   1481: aload 115
    //   1483: invokevirtual 264	java/io/IOException:getMessage	()Ljava/lang/String;
    //   1486: astore 116
    //   1488: ldc 156
    //   1490: astore 117
    //   1492: aload 116
    //   1494: astore 118
    //   1496: aload 117
    //   1498: aload 118
    //   1500: aload 115
    //   1502: invokestatic 268	com/google/android/music/log/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1505: goto -769 -> 736
    //   1508: astore 58
    //   1510: iload 15
    //   1512: istore 119
    //   1514: goto -821 -> 693
    //   1517: astore 120
    //   1519: iload 15
    //   1521: istore 121
    //   1523: goto -1072 -> 451
    //   1526: iload 15
    //   1528: istore 19
    //   1530: goto -1269 -> 261
    //   1533: iload 15
    //   1535: istore 122
    //   1537: goto -1028 -> 509
    //
    // Exception table:
    //   from	to	target	type
    //   2	19	19	finally
    //   26	35	19	finally
    //   222	275	449	java/lang/InterruptedException
    //   299	449	449	java/lang/InterruptedException
    //   633	691	449	java/lang/InterruptedException
    //   788	1257	449	java/lang/InterruptedException
    //   1367	1370	449	java/lang/InterruptedException
    //   538	553	554	java/io/IOException
    //   139	168	581	android/os/RemoteException
    //   279	295	631	finally
    //   610	625	631	finally
    //   222	275	691	finally
    //   299	449	691	finally
    //   451	509	691	finally
    //   633	691	691	finally
    //   788	1257	691	finally
    //   1367	1370	691	finally
    //   1257	1362	1362	finally
    //   1370	1411	1362	finally
    //   509	538	1421	android/os/RemoteException
    //   693	722	1450	android/os/RemoteException
    //   722	736	1479	java/io/IOException
    //   171	177	1508	finally
    //   187	212	1508	finally
    //   187	212	1517	java/lang/InterruptedException
  }

  private void logDownloadSucceededEvent()
  {
    NetworkInfo localNetworkInfo = DownloadUtils.getActiveNetworkInfo(this.mContext);
    int i;
    if (localNetworkInfo != null)
    {
      i = localNetworkInfo.getType();
      if (localNetworkInfo == null)
        break label354;
    }
    label354: for (int j = localNetworkInfo.getSubtype(); ; j = 0)
    {
      long l = this.mDownloadState.calculateLatency();
      Object[] arrayOfObject1 = new Object[5];
      ContentIdentifier localContentIdentifier1 = this.mDownloadRequest.getId();
      arrayOfObject1[0] = localContentIdentifier1;
      String str1 = this.mDownloadRequest.getRemoteId();
      arrayOfObject1[1] = str1;
      DownloadRequest.Owner localOwner1 = this.mDownloadRequest.getOwner();
      arrayOfObject1[2] = localOwner1;
      Integer localInteger1 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject1[3] = localInteger1;
      Long localLong1 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject1[4] = localLong1;
      String str2 = String.format("downloadCompleted: id=%s, remoteId=%s, owner=%s, priority=%s, seek=%s", arrayOfObject1);
      Log.i("DownloadTaskImpl", str2);
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject2 = new Object[16];
      arrayOfObject2[0] = "id";
      ContentIdentifier localContentIdentifier2 = this.mDownloadRequest.getId();
      arrayOfObject2[1] = localContentIdentifier2;
      arrayOfObject2[2] = "retmoteId";
      String str3 = this.mDownloadRequest.getRemoteId();
      arrayOfObject2[3] = str3;
      arrayOfObject2[4] = "downloadOwner";
      DownloadRequest.Owner localOwner2 = this.mDownloadRequest.getOwner();
      arrayOfObject2[5] = localOwner2;
      arrayOfObject2[6] = "downloadPriority";
      Integer localInteger2 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject2[7] = localInteger2;
      arrayOfObject2[8] = "downloadSeekMs";
      Long localLong2 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject2[9] = localLong2;
      arrayOfObject2[10] = "latency";
      Long localLong3 = Long.valueOf(l);
      arrayOfObject2[11] = localLong3;
      arrayOfObject2[12] = "networkType";
      Integer localInteger3 = Integer.valueOf(i);
      arrayOfObject2[13] = localInteger3;
      arrayOfObject2[14] = "networkSubType";
      Integer localInteger4 = Integer.valueOf(j);
      arrayOfObject2[15] = localInteger4;
      localMusicEventLogger.trackEvent("downloadSucceeded", arrayOfObject2);
      return;
      i = 10000;
      break;
    }
  }

  private void logHttpErrorEvent(int paramInt)
  {
    NetworkInfo localNetworkInfo = DownloadUtils.getActiveNetworkInfo(this.mContext);
    int i;
    if (localNetworkInfo != null)
    {
      i = localNetworkInfo.getType();
      if (localNetworkInfo == null)
        break label372;
    }
    label372: for (int j = localNetworkInfo.getSubtype(); ; j = 0)
    {
      Object[] arrayOfObject1 = new Object[7];
      ContentIdentifier localContentIdentifier1 = this.mDownloadRequest.getId();
      arrayOfObject1[0] = localContentIdentifier1;
      String str1 = this.mDownloadRequest.getRemoteId();
      arrayOfObject1[1] = str1;
      DownloadRequest.Owner localOwner1 = this.mDownloadRequest.getOwner();
      arrayOfObject1[2] = localOwner1;
      Integer localInteger1 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject1[3] = localInteger1;
      Long localLong1 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject1[4] = localLong1;
      Integer localInteger2 = Integer.valueOf(paramInt);
      arrayOfObject1[5] = localInteger2;
      Integer localInteger3 = Integer.valueOf(i);
      arrayOfObject1[6] = localInteger3;
      String str2 = String.format("httpError: id=%s, remoteId=%s, owner=%s, priority=%s, seek=%s, error=%s, network=%s", arrayOfObject1);
      Log.e("DownloadTaskImpl", str2);
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject2 = new Object[16];
      arrayOfObject2[0] = "id";
      ContentIdentifier localContentIdentifier2 = this.mDownloadRequest.getId();
      arrayOfObject2[1] = localContentIdentifier2;
      arrayOfObject2[2] = "retmoteId";
      String str3 = this.mDownloadRequest.getRemoteId();
      arrayOfObject2[3] = str3;
      arrayOfObject2[4] = "downloadOwner";
      DownloadRequest.Owner localOwner2 = this.mDownloadRequest.getOwner();
      arrayOfObject2[5] = localOwner2;
      arrayOfObject2[6] = "downloadPriority";
      Integer localInteger4 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject2[7] = localInteger4;
      arrayOfObject2[8] = "downloadSeekMs";
      Long localLong2 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject2[9] = localLong2;
      arrayOfObject2[10] = "httpResponseCode";
      Integer localInteger5 = Integer.valueOf(paramInt);
      arrayOfObject2[11] = localInteger5;
      arrayOfObject2[12] = "networkType";
      Integer localInteger6 = Integer.valueOf(i);
      arrayOfObject2[13] = localInteger6;
      arrayOfObject2[14] = "networkSubType";
      Integer localInteger7 = Integer.valueOf(j);
      arrayOfObject2[15] = localInteger7;
      localMusicEventLogger.trackEvent("downloadHttpError", arrayOfObject2);
      return;
      i = 10000;
      break;
    }
  }

  private void logIOExceptionEvent()
  {
    NetworkInfo localNetworkInfo = DownloadUtils.getActiveNetworkInfo(this.mContext);
    int i;
    if (localNetworkInfo != null)
    {
      i = localNetworkInfo.getType();
      if (localNetworkInfo == null)
        break label323;
    }
    label323: for (int j = localNetworkInfo.getSubtype(); ; j = 0)
    {
      Object[] arrayOfObject1 = new Object[5];
      ContentIdentifier localContentIdentifier1 = this.mDownloadRequest.getId();
      arrayOfObject1[0] = localContentIdentifier1;
      String str1 = this.mDownloadRequest.getRemoteId();
      arrayOfObject1[1] = str1;
      DownloadRequest.Owner localOwner1 = this.mDownloadRequest.getOwner();
      arrayOfObject1[2] = localOwner1;
      Integer localInteger1 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject1[3] = localInteger1;
      Long localLong1 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject1[4] = localLong1;
      String str2 = String.format("IOException: id=%s, remoteId=%s, owner=%s, priority=%s, seek=%s", arrayOfObject1);
      Log.e("DownloadTaskImpl", str2);
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject2 = new Object[14];
      arrayOfObject2[0] = "id";
      ContentIdentifier localContentIdentifier2 = this.mDownloadRequest.getId();
      arrayOfObject2[1] = localContentIdentifier2;
      arrayOfObject2[2] = "retmoteId";
      String str3 = this.mDownloadRequest.getRemoteId();
      arrayOfObject2[3] = str3;
      arrayOfObject2[4] = "downloadOwner";
      DownloadRequest.Owner localOwner2 = this.mDownloadRequest.getOwner();
      arrayOfObject2[5] = localOwner2;
      arrayOfObject2[6] = "downloadPriority";
      Integer localInteger2 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject2[7] = localInteger2;
      arrayOfObject2[8] = "downloadSeekMs";
      Long localLong2 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject2[9] = localLong2;
      arrayOfObject2[10] = "networkType";
      Integer localInteger3 = Integer.valueOf(i);
      arrayOfObject2[11] = localInteger3;
      arrayOfObject2[12] = "networkSubType";
      Integer localInteger4 = Integer.valueOf(j);
      arrayOfObject2[13] = localInteger4;
      localMusicEventLogger.trackEvent("downloadIOException", arrayOfObject2);
      return;
      i = 10000;
      break;
    }
  }

  private void logServiceUnavailableEvent()
  {
    NetworkInfo localNetworkInfo = DownloadUtils.getActiveNetworkInfo(this.mContext);
    int i;
    if (localNetworkInfo != null)
    {
      i = localNetworkInfo.getType();
      if (localNetworkInfo == null)
        break label323;
    }
    label323: for (int j = localNetworkInfo.getSubtype(); ; j = 0)
    {
      Object[] arrayOfObject1 = new Object[5];
      ContentIdentifier localContentIdentifier1 = this.mDownloadRequest.getId();
      arrayOfObject1[0] = localContentIdentifier1;
      String str1 = this.mDownloadRequest.getRemoteId();
      arrayOfObject1[1] = str1;
      DownloadRequest.Owner localOwner1 = this.mDownloadRequest.getOwner();
      arrayOfObject1[2] = localOwner1;
      Integer localInteger1 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject1[3] = localInteger1;
      Long localLong1 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject1[4] = localLong1;
      String str2 = String.format("ServiceUnavailable: id=%s, remoteId=%s, owner=%s, priority=%s, seek=%s", arrayOfObject1);
      Log.e("DownloadTaskImpl", str2);
      MusicEventLogger localMusicEventLogger = this.mEventLogger;
      Object[] arrayOfObject2 = new Object[14];
      arrayOfObject2[0] = "id";
      ContentIdentifier localContentIdentifier2 = this.mDownloadRequest.getId();
      arrayOfObject2[1] = localContentIdentifier2;
      arrayOfObject2[2] = "retmoteId";
      String str3 = this.mDownloadRequest.getRemoteId();
      arrayOfObject2[3] = str3;
      arrayOfObject2[4] = "downloadOwner";
      DownloadRequest.Owner localOwner2 = this.mDownloadRequest.getOwner();
      arrayOfObject2[5] = localOwner2;
      arrayOfObject2[6] = "downloadPriority";
      Integer localInteger2 = Integer.valueOf(this.mDownloadRequest.getPriority());
      arrayOfObject2[7] = localInteger2;
      arrayOfObject2[8] = "downloadSeekMs";
      Long localLong2 = Long.valueOf(this.mDownloadRequest.getSeekMs());
      arrayOfObject2[9] = localLong2;
      arrayOfObject2[10] = "networkType";
      Integer localInteger3 = Integer.valueOf(i);
      arrayOfObject2[11] = localInteger3;
      arrayOfObject2[12] = "networkSubType";
      Integer localInteger4 = Integer.valueOf(j);
      arrayOfObject2[13] = localInteger4;
      localMusicEventLogger.trackEvent("downloadServiceUnavailable", arrayOfObject2);
      return;
      i = 10000;
      break;
    }
  }

  private void updateCanceled()
  {
    this.mDownloadState.setCanceledState();
  }

  private void updateCompleted()
  {
    this.mDownloadState.setCompletedState();
  }

  private void updateDownloading()
  {
    this.mDownloadState.setDownloadingState();
    updateProgress();
  }

  private void updateFailed(int paramInt)
  {
    this.mDownloadState.setFailedState(paramInt);
  }

  private void updateProgress()
  {
    try
    {
      DownloadRequest localDownloadRequest1 = this.mDownloadRequest;
      DownloadState localDownloadState = this.mDownloadState;
      DownloadProgress localDownloadProgress = new DownloadProgress(localDownloadRequest1, localDownloadState);
      this.mDownloadProgressListener.onDownloadProgress(localDownloadProgress);
      if (!this.mDownloadState.getState().isFinished())
        return;
      if (!this.LOGV)
        return;
      Object[] arrayOfObject = new Object[2];
      DownloadRequest localDownloadRequest2 = this.mDownloadRequest;
      arrayOfObject[0] = localDownloadRequest2;
      arrayOfObject[1] = localDownloadProgress;
      String str = String.format("Download finished: %s %s", arrayOfObject);
      Log.d("DownloadTaskImpl", str);
      return;
    }
    catch (RemoteException localRemoteException)
    {
      Log.e("DownloadTaskImpl", "Failed to call download progress callback", localRemoteException);
    }
  }

  public void cancel()
  {
    this.mStopDownload = true;
    this.mMplayHandler.cancel();
    updateCanceled();
  }

  // ERROR //
  protected int download()
    throws java.lang.InterruptedException
  {
    // Byte code:
    //   0: iconst_4
    //   1: istore_1
    //   2: aconst_null
    //   3: astore_2
    //   4: aconst_null
    //   5: astore_3
    //   6: aconst_null
    //   7: astore 4
    //   9: aload_0
    //   10: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   13: ifeq +43 -> 56
    //   16: new 140	java/lang/StringBuilder
    //   19: dup
    //   20: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   23: ldc_w 485
    //   26: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: astore 5
    //   31: aload_0
    //   32: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   35: astore 6
    //   37: aload 5
    //   39: aload 6
    //   41: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   44: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   47: astore 7
    //   49: ldc 156
    //   51: aload 7
    //   53: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   56: aload_0
    //   57: getfield 107	com/google/android/music/download/DownloadTaskImpl:mMplayHandler	Lcom/google/android/music/download/MplayHandler;
    //   60: invokevirtual 488	com/google/android/music/download/MplayHandler:prepareInputStream	()V
    //   63: aload_0
    //   64: getfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   67: ifnonnull +127 -> 194
    //   70: aload_0
    //   71: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   74: invokevirtual 492	com/google/android/music/download/DownloadRequest:getFileLocation	()Lcom/google/android/music/download/cache/FileLocation;
    //   77: invokevirtual 498	com/google/android/music/download/cache/FileLocation:getFullPath	()Ljava/io/File;
    //   80: astore 8
    //   82: aload 8
    //   84: invokevirtual 503	java/io/File:exists	()Z
    //   87: astore 9
    //   89: aload 9
    //   91: ifnonnull +52 -> 143
    //   94: aload 8
    //   96: invokevirtual 506	java/io/File:createNewFile	()Z
    //   99: ifne +44 -> 143
    //   102: new 140	java/lang/StringBuilder
    //   105: dup
    //   106: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   109: ldc_w 508
    //   112: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: astore 10
    //   117: aload 8
    //   119: invokevirtual 511	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   122: astore 11
    //   124: aload 10
    //   126: aload 11
    //   128: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   131: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   134: astore 12
    //   136: ldc 156
    //   138: aload 12
    //   140: invokestatic 514	com/google/android/music/log/Log:wtf	(Ljava/lang/String;Ljava/lang/String;)V
    //   143: aload_0
    //   144: invokevirtual 518	com/google/android/music/download/DownloadTaskImpl:getWriteToStream	()Ljava/io/OutputStream;
    //   147: astore 9
    //   149: aload 9
    //   151: astore 13
    //   153: aload_0
    //   154: getfield 100	com/google/android/music/download/DownloadTaskImpl:mDownloadProgressListener	Lcom/google/android/music/download/IDownloadProgressListener;
    //   157: astore 14
    //   159: aload_0
    //   160: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   163: astore 15
    //   165: aload_0
    //   166: getfield 98	com/google/android/music/download/DownloadTaskImpl:mDownloadState	Lcom/google/android/music/download/DownloadState;
    //   169: astore 16
    //   171: new 258	com/google/android/music/download/BufferProgressOutputStream
    //   174: dup
    //   175: aload 14
    //   177: aload 13
    //   179: aload 15
    //   181: aload 16
    //   183: invokespecial 521	com/google/android/music/download/BufferProgressOutputStream:<init>	(Lcom/google/android/music/download/IDownloadProgressListener;Ljava/io/OutputStream;Lcom/google/android/music/download/DownloadRequest;Lcom/google/android/music/download/DownloadState;)V
    //   186: astore 17
    //   188: aload_0
    //   189: aload 17
    //   191: putfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   194: aload_0
    //   195: getfield 107	com/google/android/music/download/DownloadTaskImpl:mMplayHandler	Lcom/google/android/music/download/MplayHandler;
    //   198: astore 18
    //   200: aload_0
    //   201: getfield 256	com/google/android/music/download/DownloadTaskImpl:mBufferOut	Lcom/google/android/music/download/BufferProgressOutputStream;
    //   204: astore 19
    //   206: aload 18
    //   208: aload 19
    //   210: invokevirtual 525	com/google/android/music/download/MplayHandler:downloadTo	(Ljava/io/OutputStream;)V
    //   213: aload_0
    //   214: getfield 107	com/google/android/music/download/DownloadTaskImpl:mMplayHandler	Lcom/google/android/music/download/MplayHandler;
    //   217: invokevirtual 527	com/google/android/music/download/MplayHandler:downloadSucceeded	()Z
    //   220: ifeq +553 -> 773
    //   223: aload_0
    //   224: invokespecial 529	com/google/android/music/download/DownloadTaskImpl:logDownloadSucceededEvent	()V
    //   227: aconst_null
    //   228: astore 4
    //   230: aload 4
    //   232: ireturn
    //   233: astore 20
    //   235: new 140	java/lang/StringBuilder
    //   238: dup
    //   239: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   242: ldc_w 508
    //   245: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   248: astore 21
    //   250: aload 8
    //   252: invokevirtual 511	java/io/File:getAbsolutePath	()Ljava/lang/String;
    //   255: astore 22
    //   257: aload 21
    //   259: aload 22
    //   261: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   264: ldc_w 531
    //   267: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: astore 23
    //   272: aload 20
    //   274: invokevirtual 264	java/io/IOException:getMessage	()Ljava/lang/String;
    //   277: astore 24
    //   279: aload 23
    //   281: aload 24
    //   283: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   286: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   289: astore 25
    //   291: ldc 156
    //   293: aload 25
    //   295: aload 20
    //   297: invokestatic 533	com/google/android/music/log/Log:wtf	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   300: goto -157 -> 143
    //   303: astore 26
    //   305: aload_0
    //   306: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   309: ifeq +53 -> 362
    //   312: new 140	java/lang/StringBuilder
    //   315: dup
    //   316: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   319: ldc_w 535
    //   322: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   325: astore 27
    //   327: aload_0
    //   328: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   331: astore 28
    //   333: aload 27
    //   335: aload 28
    //   337: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   340: astore 29
    //   342: ldc_w 537
    //   345: astore_3
    //   346: aload 29
    //   348: aload_3
    //   349: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   355: astore_1
    //   356: ldc 156
    //   358: aload_1
    //   359: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   362: aload_2
    //   363: astore 4
    //   365: goto -135 -> 230
    //   368: astore 30
    //   370: aload 30
    //   372: invokevirtual 538	java/io/FileNotFoundException:getMessage	()Ljava/lang/String;
    //   375: astore 31
    //   377: ldc 156
    //   379: aload 31
    //   381: aload 30
    //   383: invokestatic 272	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   386: aload_0
    //   387: iconst_2
    //   388: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   391: goto -161 -> 230
    //   394: astore 32
    //   396: aload_0
    //   397: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   400: ifeq +49 -> 449
    //   403: new 140	java/lang/StringBuilder
    //   406: dup
    //   407: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   410: ldc_w 535
    //   413: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   416: astore 33
    //   418: aload_0
    //   419: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   422: astore 34
    //   424: aload 33
    //   426: aload 34
    //   428: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   431: ldc_w 540
    //   434: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   437: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   440: astore 35
    //   442: ldc 156
    //   444: aload 35
    //   446: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   449: aload_2
    //   450: astore 4
    //   452: goto -222 -> 230
    //   455: astore 36
    //   457: new 120	java/lang/InterruptedException
    //   460: dup
    //   461: invokespecial 244	java/lang/InterruptedException:<init>	()V
    //   464: astore 37
    //   466: aload 37
    //   468: aload 36
    //   470: invokevirtual 544	java/lang/InterruptedException:initCause	(Ljava/lang/Throwable;)Ljava/lang/Throwable;
    //   473: astore 38
    //   475: aload 37
    //   477: athrow
    //   478: astore 39
    //   480: aload_0
    //   481: invokespecial 546	com/google/android/music/download/DownloadTaskImpl:logServiceUnavailableEvent	()V
    //   484: aload 39
    //   486: invokevirtual 549	com/google/android/music/download/ServiceUnavailableException:getRetryAfterInSeconds	()J
    //   489: lstore 40
    //   491: aload_0
    //   492: lload 40
    //   494: putfield 76	com/google/android/music/download/DownloadTaskImpl:mServerSpecifiedRetryTime	J
    //   497: aconst_null
    //   498: astore 4
    //   500: goto -270 -> 230
    //   503: astore 42
    //   505: getstatic 553	com/google/android/music/download/DownloadTaskImpl$2:$SwitchMap$com$google$android$music$download$ServerRejectionException$RejectionReason	[I
    //   508: astore 43
    //   510: aload 42
    //   512: invokevirtual 557	com/google/android/music/download/ServerRejectionException:getRejectionReason	()Lcom/google/android/music/download/ServerRejectionException$RejectionReason;
    //   515: invokevirtual 562	com/google/android/music/download/ServerRejectionException$RejectionReason:ordinal	()I
    //   518: istore_2
    //   519: aload 43
    //   521: iload_2
    //   522: iaload
    //   523: istore_1
    //   524: iload_1
    //   525: tableswitch	default:+31 -> 556, 1:+39->564, 2:+50->575, 3:+59->584, 4:+70->595
    //   557: aload_3
    //   558: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   561: goto -331 -> 230
    //   564: bipush 6
    //   566: istore_1
    //   567: aload_0
    //   568: iload_1
    //   569: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   572: goto -342 -> 230
    //   575: aload_0
    //   576: aload 4
    //   578: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   581: goto -351 -> 230
    //   584: bipush 7
    //   586: istore_1
    //   587: aload_0
    //   588: iload_1
    //   589: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   592: goto -362 -> 230
    //   595: bipush 13
    //   597: istore_1
    //   598: aload_0
    //   599: iload_1
    //   600: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   603: goto -373 -> 230
    //   606: invokevirtual 565	org/apache/http/client/HttpResponseException:getStatusCode	()I
    //   609: istore 44
    //   611: aload_0
    //   612: iload 44
    //   614: invokespecial 567	com/google/android/music/download/DownloadTaskImpl:logHttpErrorEvent	(I)V
    //   617: aconst_null
    //   618: astore_2
    //   619: iload 44
    //   621: aload_2
    //   622: if_icmplt +151 -> 773
    //   625: aconst_null
    //   626: astore_2
    //   627: iload 44
    //   629: aload_2
    //   630: if_icmpge +143 -> 773
    //   633: aconst_null
    //   634: astore_2
    //   635: iload 44
    //   637: aload_2
    //   638: if_acmpne +11 -> 649
    //   641: aload_0
    //   642: iload_1
    //   643: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   646: goto -416 -> 230
    //   649: sipush 404
    //   652: istore_1
    //   653: iload 44
    //   655: iload_1
    //   656: if_icmpeq +14 -> 670
    //   659: bipush 12
    //   661: istore_1
    //   662: aload_0
    //   663: iload_1
    //   664: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   667: goto -437 -> 230
    //   670: aload_0
    //   671: aload_3
    //   672: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   675: goto -445 -> 230
    //   678: astore 45
    //   680: new 140	java/lang/StringBuilder
    //   683: dup
    //   684: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   687: ldc_w 569
    //   690: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   693: astore 46
    //   695: aload 45
    //   697: invokevirtual 572	com/google/android/music/download/UnsupportedAudioTypeException:getAudioType	()Ljava/lang/String;
    //   700: astore_2
    //   701: aload 46
    //   703: aload_2
    //   704: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   707: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   710: astore_3
    //   711: ldc 156
    //   713: aload_3
    //   714: invokestatic 306	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   717: bipush 8
    //   719: istore_1
    //   720: aload_0
    //   721: iload_1
    //   722: invokespecial 238	com/google/android/music/download/DownloadTaskImpl:updateFailed	(I)V
    //   725: goto -495 -> 230
    //   728: astore 47
    //   730: aload_0
    //   731: invokespecial 574	com/google/android/music/download/DownloadTaskImpl:logIOExceptionEvent	()V
    //   734: new 140	java/lang/StringBuilder
    //   737: dup
    //   738: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   741: ldc_w 576
    //   744: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   747: astore 48
    //   749: aload 47
    //   751: invokevirtual 264	java/io/IOException:getMessage	()Ljava/lang/String;
    //   754: astore_2
    //   755: aload 48
    //   757: aload_2
    //   758: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   761: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   764: astore 49
    //   766: ldc 156
    //   768: aload 49
    //   770: invokestatic 306	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;)V
    //   773: aload_0
    //   774: getfield 59	com/google/android/music/download/DownloadTaskImpl:LOGV	Z
    //   777: ifeq +62 -> 839
    //   780: aload_0
    //   781: getfield 61	com/google/android/music/download/DownloadTaskImpl:mStopDownload	Z
    //   784: ifeq +55 -> 839
    //   787: new 140	java/lang/StringBuilder
    //   790: dup
    //   791: invokespecial 141	java/lang/StringBuilder:<init>	()V
    //   794: ldc_w 578
    //   797: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   800: astore 50
    //   802: aload_0
    //   803: getfield 93	com/google/android/music/download/DownloadTaskImpl:mDownloadRequest	Lcom/google/android/music/download/DownloadRequest;
    //   806: astore 51
    //   808: aload 50
    //   810: aload 51
    //   812: invokevirtual 150	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   815: astore 52
    //   817: ldc_w 580
    //   820: astore_2
    //   821: aload 52
    //   823: aload_2
    //   824: invokevirtual 147	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   827: invokevirtual 154	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   830: astore 53
    //   832: ldc 156
    //   834: aload 53
    //   836: invokestatic 162	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
    //   839: aload_3
    //   840: astore 4
    //   842: goto -612 -> 230
    //
    // Exception table:
    //   from	to	target	type
    //   94	143	233	java/io/IOException
    //   9	89	303	java/net/SocketTimeoutException
    //   94	143	303	java/net/SocketTimeoutException
    //   143	149	303	java/net/SocketTimeoutException
    //   153	300	303	java/net/SocketTimeoutException
    //   370	391	303	java/net/SocketTimeoutException
    //   143	149	368	java/io/FileNotFoundException
    //   9	89	394	org/apache/http/conn/ConnectTimeoutException
    //   94	143	394	org/apache/http/conn/ConnectTimeoutException
    //   143	149	394	org/apache/http/conn/ConnectTimeoutException
    //   153	300	394	org/apache/http/conn/ConnectTimeoutException
    //   370	391	394	org/apache/http/conn/ConnectTimeoutException
    //   9	89	455	java/io/InterruptedIOException
    //   94	143	455	java/io/InterruptedIOException
    //   143	149	455	java/io/InterruptedIOException
    //   153	300	455	java/io/InterruptedIOException
    //   370	391	455	java/io/InterruptedIOException
    //   9	89	478	com/google/android/music/download/ServiceUnavailableException
    //   94	143	478	com/google/android/music/download/ServiceUnavailableException
    //   143	149	478	com/google/android/music/download/ServiceUnavailableException
    //   153	300	478	com/google/android/music/download/ServiceUnavailableException
    //   370	391	478	com/google/android/music/download/ServiceUnavailableException
    //   9	89	503	com/google/android/music/download/ServerRejectionException
    //   94	143	503	com/google/android/music/download/ServerRejectionException
    //   143	149	503	com/google/android/music/download/ServerRejectionException
    //   153	300	503	com/google/android/music/download/ServerRejectionException
    //   370	391	503	com/google/android/music/download/ServerRejectionException
    //   9	89	606	org/apache/http/client/HttpResponseException
    //   94	143	606	org/apache/http/client/HttpResponseException
    //   143	149	606	org/apache/http/client/HttpResponseException
    //   153	300	606	org/apache/http/client/HttpResponseException
    //   370	391	606	org/apache/http/client/HttpResponseException
    //   9	89	678	com/google/android/music/download/UnsupportedAudioTypeException
    //   94	143	678	com/google/android/music/download/UnsupportedAudioTypeException
    //   143	149	678	com/google/android/music/download/UnsupportedAudioTypeException
    //   153	300	678	com/google/android/music/download/UnsupportedAudioTypeException
    //   370	391	678	com/google/android/music/download/UnsupportedAudioTypeException
    //   9	89	728	java/io/IOException
    //   143	149	728	java/io/IOException
    //   153	300	728	java/io/IOException
    //   370	391	728	java/io/IOException
  }

  public DownloadRequest getDownloadRequest()
  {
    return this.mDownloadRequest;
  }

  protected OutputStream getWriteToStream()
    throws FileNotFoundException, IOException
  {
    this.mDownloadState.resetCompletedBytes();
    if (this.LOGV)
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("Opening file for download:");
      DownloadRequest localDownloadRequest = this.mDownloadRequest;
      String str1 = localDownloadRequest;
      Log.d("DownloadTaskImpl", str1);
    }
    if (this.LOGV)
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append("Opening file for download:");
      FileLocation localFileLocation = this.mDownloadRequest.getFileLocation();
      StringBuilder localStringBuilder3 = localStringBuilder2.append(localFileLocation).append(" ");
      String str2 = this.mDownloadRequest.toString();
      String str3 = str2;
      Log.f("DownloadTaskImpl", str3);
    }
    File localFile = this.mDownloadRequest.getFileLocation().getFullPath();
    Object localObject = new FileOutputStream(localFile);
    if (this.mDownloadRequest.getId().isCacheable())
    {
      Store localStore = Store.getInstance(this.mContext);
      long l = this.mDownloadRequest.getId().getId();
      byte[] arrayOfByte = localStore.getCpData(l, true);
      if (arrayOfByte != null)
      {
        CpOutputStream localCpOutputStream = new CpOutputStream((OutputStream)localObject, arrayOfByte);
        localObject = new ChunkedOutputStreamAdapter(localCpOutputStream);
        this.mDownloadState.setCp();
      }
    }
    return localObject;
  }

  public void run()
  {
    try
    {
      handleRun();
      if ((1 == 0) || (!this.mDownloadState.getState().isFinished()))
        updateFailed(11);
      updateProgress();
      return;
    }
    finally
    {
      if ((0 == 0) || (!this.mDownloadState.getState().isFinished()))
        updateFailed(11);
      updateProgress();
    }
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("task(");
    DownloadRequest localDownloadRequest = this.mDownloadRequest;
    StringBuilder localStringBuilder3 = localStringBuilder1.append(localDownloadRequest);
    StringBuilder localStringBuilder4 = localStringBuilder1.append(")");
    return localStringBuilder1.toString();
  }

  public boolean upgrade(DownloadTask paramDownloadTask)
  {
    DownloadRequest localDownloadRequest1 = this.mDownloadRequest;
    DownloadRequest localDownloadRequest2 = paramDownloadTask.getDownloadRequest();
    return localDownloadRequest1.upgrade(localDownloadRequest2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.DownloadTaskImpl
 * JD-Core Version:    0.6.2
 */