package com.google.android.music.download.stream;

import android.content.Context;
import com.google.android.music.download.DownloadRequest;
import com.google.android.music.log.Log;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class StreamingHttpServer
{
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
  private RequestAcceptorThread mAcceptor;
  private final AllowedStreams mAllowedStreams;
  protected HttpParams mParams;
  protected ServerSocket mServerSocket;
  private boolean mShutdown = false;
  private LinkedList<WorkerThread> mWorkers;

  public StreamingHttpServer(Context paramContext)
    throws IOException
  {
    AllowedStreams localAllowedStreams = new AllowedStreams();
    this.mAllowedStreams = localAllowedStreams;
    LinkedList localLinkedList = new LinkedList();
    this.mWorkers = localLinkedList;
    BasicHttpParams localBasicHttpParams = new BasicHttpParams();
    this.mParams = localBasicHttpParams;
    HttpParams localHttpParams = this.mParams.setBooleanParameter("http.connection.stalecheck", false).setBooleanParameter("http.tcp.nodelay", true).setIntParameter("http.socket.timeout", 10000).setIntParameter("http.socket.buffer-size", 8192);
    bind(0);
  }

  private void bind(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = { 127, 0, 0, 1 };
    InetAddress localInetAddress = InetAddress.getByAddress("localhost", arrayOfByte);
    InetSocketAddress localInetSocketAddress = new InetSocketAddress(localInetAddress, paramInt);
    bind(localInetSocketAddress);
  }

  private void bind(InetSocketAddress paramInetSocketAddress)
    throws IOException
  {
    ServerSocket localServerSocket = new ServerSocket();
    this.mServerSocket = localServerSocket;
    this.mServerSocket.bind(paramInetSocketAddress);
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Bound to port: ");
      int i = this.mServerSocket.getLocalPort();
      String str = i;
      Log.i("StreamingHttpServer", str);
    }
    if (this.mAcceptor != null)
      throw new RuntimeException("Should never bind to a socket twice");
    RequestAcceptorThread localRequestAcceptorThread = new RequestAcceptorThread();
    this.mAcceptor = localRequestAcceptorThread;
    this.mAcceptor.start();
  }

  private String generateUri(StreamingContent paramStreamingContent)
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    StringBuilder localStringBuilder2 = localStringBuilder1.append("http://127.0.0.1:");
    int i = getPort();
    StringBuilder localStringBuilder3 = localStringBuilder2.append(i).append("/");
    long l = paramStreamingContent.getSecureId();
    StringBuilder localStringBuilder4 = localStringBuilder3.append(l);
    return localStringBuilder1.toString();
  }

  private int getPort()
  {
    if (this.mServerSocket == null)
      throw new IllegalStateException("Socket not bound");
    return this.mServerSocket.getLocalPort();
  }

  public String serveStream(StreamingContent paramStreamingContent1, StreamingContent paramStreamingContent2, StreamingContent paramStreamingContent3)
  {
    this.mAllowedStreams.setAllowedStreams(paramStreamingContent2, paramStreamingContent3);
    this.mAcceptor.shutdownOldWorkers();
    return generateUri(paramStreamingContent1);
  }

  public void shutdown()
  {
    this.mShutdown = true;
    try
    {
      this.mServerSocket.close();
      label12: synchronized (this.mWorkers)
      {
        Iterator localIterator = this.mWorkers.iterator();
        if (localIterator.hasNext())
          ((WorkerThread)localIterator.next()).shutdown();
      }
      return;
    }
    catch (IOException localIOException)
    {
      break label12;
    }
  }

  private class WorkerThread extends Thread
  {
    private StreamRequestHandler mHandler;
    private final Socket mSocket;

    public WorkerThread(Socket arg2)
    {
      super();
      setDaemon(true);
      Object localObject;
      this.mSocket = localObject;
    }

    public DownloadRequest getDownloadRequest()
    {
      if (this.mHandler != null);
      for (DownloadRequest localDownloadRequest = this.mHandler.getDownloadRequest(); ; localDownloadRequest = null)
        return localDownloadRequest;
    }

    // ERROR //
    public void run()
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_1
      //   2: aload_0
      //   3: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   6: invokestatic 47	com/google/android/music/download/stream/StreamingHttpServer:access$400	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Lcom/google/android/music/download/stream/AllowedStreams;
      //   9: astore_2
      //   10: new 35	com/google/android/music/download/stream/StreamRequestHandler
      //   13: dup
      //   14: aload_2
      //   15: invokespecial 50	com/google/android/music/download/stream/StreamRequestHandler:<init>	(Lcom/google/android/music/download/stream/AllowedStreams;)V
      //   18: astore_3
      //   19: aload_0
      //   20: aload_3
      //   21: putfield 33	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mHandler	Lcom/google/android/music/download/stream/StreamRequestHandler;
      //   24: new 52	org/apache/http/impl/DefaultHttpServerConnection
      //   27: dup
      //   28: invokespecial 54	org/apache/http/impl/DefaultHttpServerConnection:<init>	()V
      //   31: astore 4
      //   33: aload_0
      //   34: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   37: astore 5
      //   39: aload_0
      //   40: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   43: getfield 58	com/google/android/music/download/stream/StreamingHttpServer:mParams	Lorg/apache/http/params/HttpParams;
      //   46: astore 6
      //   48: aload 4
      //   50: aload 5
      //   52: aload 6
      //   54: invokevirtual 62	org/apache/http/impl/DefaultHttpServerConnection:bind	(Ljava/net/Socket;Lorg/apache/http/params/HttpParams;)V
      //   57: new 64	org/apache/http/protocol/BasicHttpProcessor
      //   60: dup
      //   61: invokespecial 65	org/apache/http/protocol/BasicHttpProcessor:<init>	()V
      //   64: astore 7
      //   66: new 67	org/apache/http/protocol/ResponseContent
      //   69: dup
      //   70: invokespecial 68	org/apache/http/protocol/ResponseContent:<init>	()V
      //   73: astore 8
      //   75: aload 7
      //   77: aload 8
      //   79: invokevirtual 72	org/apache/http/protocol/BasicHttpProcessor:addInterceptor	(Lorg/apache/http/HttpResponseInterceptor;)V
      //   82: new 74	org/apache/http/protocol/ResponseConnControl
      //   85: dup
      //   86: invokespecial 75	org/apache/http/protocol/ResponseConnControl:<init>	()V
      //   89: astore 9
      //   91: aload 7
      //   93: aload 9
      //   95: invokevirtual 72	org/apache/http/protocol/BasicHttpProcessor:addInterceptor	(Lorg/apache/http/HttpResponseInterceptor;)V
      //   98: new 77	org/apache/http/protocol/HttpRequestHandlerRegistry
      //   101: dup
      //   102: invokespecial 78	org/apache/http/protocol/HttpRequestHandlerRegistry:<init>	()V
      //   105: astore 10
      //   107: aload_0
      //   108: getfield 33	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mHandler	Lcom/google/android/music/download/stream/StreamRequestHandler;
      //   111: astore 11
      //   113: aload 10
      //   115: ldc 80
      //   117: aload 11
      //   119: invokevirtual 84	org/apache/http/protocol/HttpRequestHandlerRegistry:register	(Ljava/lang/String;Lorg/apache/http/protocol/HttpRequestHandler;)V
      //   122: new 86	org/apache/http/impl/DefaultConnectionReuseStrategy
      //   125: dup
      //   126: invokespecial 87	org/apache/http/impl/DefaultConnectionReuseStrategy:<init>	()V
      //   129: astore 12
      //   131: new 89	org/apache/http/impl/DefaultHttpResponseFactory
      //   134: dup
      //   135: invokespecial 90	org/apache/http/impl/DefaultHttpResponseFactory:<init>	()V
      //   138: astore 13
      //   140: new 92	org/apache/http/protocol/HttpService
      //   143: dup
      //   144: aload 7
      //   146: aload 12
      //   148: aload 13
      //   150: invokespecial 95	org/apache/http/protocol/HttpService:<init>	(Lorg/apache/http/protocol/HttpProcessor;Lorg/apache/http/ConnectionReuseStrategy;Lorg/apache/http/HttpResponseFactory;)V
      //   153: astore 14
      //   155: aload_0
      //   156: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   159: getfield 58	com/google/android/music/download/stream/StreamingHttpServer:mParams	Lorg/apache/http/params/HttpParams;
      //   162: astore 15
      //   164: aload 14
      //   166: aload 15
      //   168: invokevirtual 99	org/apache/http/protocol/HttpService:setParams	(Lorg/apache/http/params/HttpParams;)V
      //   171: aload 14
      //   173: aload 10
      //   175: invokevirtual 103	org/apache/http/protocol/HttpService:setHandlerResolver	(Lorg/apache/http/protocol/HttpRequestHandlerResolver;)V
      //   178: new 105	org/apache/http/protocol/BasicHttpContext
      //   181: dup
      //   182: aconst_null
      //   183: invokespecial 108	org/apache/http/protocol/BasicHttpContext:<init>	(Lorg/apache/http/protocol/HttpContext;)V
      //   186: astore 16
      //   188: aload 14
      //   190: aload 4
      //   192: aload 16
      //   194: invokevirtual 112	org/apache/http/protocol/HttpService:handleRequest	(Lorg/apache/http/HttpServerConnection;Lorg/apache/http/protocol/HttpContext;)V
      //   197: aload 4
      //   199: ifnull +41 -> 240
      //   202: aload 4
      //   204: invokevirtual 115	org/apache/http/impl/DefaultHttpServerConnection:shutdown	()V
      //   207: aload_0
      //   208: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   211: invokestatic 119	com/google/android/music/download/stream/StreamingHttpServer:access$200	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Ljava/util/LinkedList;
      //   214: astore 17
      //   216: aload 17
      //   218: monitorenter
      //   219: aload_0
      //   220: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   223: invokestatic 119	com/google/android/music/download/stream/StreamingHttpServer:access$200	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Ljava/util/LinkedList;
      //   226: aload_0
      //   227: invokevirtual 125	java/util/LinkedList:remove	(Ljava/lang/Object;)Z
      //   230: istore 18
      //   232: aload 17
      //   234: monitorexit
      //   235: aload 4
      //   237: astore 19
      //   239: return
      //   240: aload_0
      //   241: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   244: ifnull -37 -> 207
      //   247: aload_0
      //   248: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   251: invokevirtual 130	java/net/Socket:close	()V
      //   254: goto -47 -> 207
      //   257: astore 20
      //   259: goto -52 -> 207
      //   262: astore 21
      //   264: aload 17
      //   266: monitorexit
      //   267: aload 21
      //   269: athrow
      //   270: astore 22
      //   272: aload 22
      //   274: instanceof 132
      //   277: ifne +24 -> 301
      //   280: aload 22
      //   282: instanceof 134
      //   285: ifeq +74 -> 359
      //   288: aload 22
      //   290: invokevirtual 138	java/lang/Exception:getMessage	()Ljava/lang/String;
      //   293: ldc 140
      //   295: invokevirtual 145	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   298: ifeq +61 -> 359
      //   301: invokestatic 149	com/google/android/music/download/stream/StreamingHttpServer:access$300	()Z
      //   304: ifeq +10 -> 314
      //   307: ldc 151
      //   309: ldc 153
      //   311: invokestatic 159	com/google/android/music/log/Log:i	(Ljava/lang/String;Ljava/lang/String;)V
      //   314: aload_1
      //   315: ifnull +130 -> 445
      //   318: aload_1
      //   319: invokevirtual 115	org/apache/http/impl/DefaultHttpServerConnection:shutdown	()V
      //   322: aload_0
      //   323: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   326: invokestatic 119	com/google/android/music/download/stream/StreamingHttpServer:access$200	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Ljava/util/LinkedList;
      //   329: astore 17
      //   331: aload 17
      //   333: monitorenter
      //   334: aload_0
      //   335: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   338: invokestatic 119	com/google/android/music/download/stream/StreamingHttpServer:access$200	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Ljava/util/LinkedList;
      //   341: aload_0
      //   342: invokevirtual 125	java/util/LinkedList:remove	(Ljava/lang/Object;)Z
      //   345: istore 23
      //   347: aload 17
      //   349: monitorexit
      //   350: return
      //   351: astore 24
      //   353: aload 17
      //   355: monitorexit
      //   356: aload 24
      //   358: athrow
      //   359: new 161	java/lang/StringBuilder
      //   362: dup
      //   363: invokespecial 162	java/lang/StringBuilder:<init>	()V
      //   366: ldc 164
      //   368: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   371: astore 25
      //   373: aload 22
      //   375: invokevirtual 171	java/lang/Exception:toString	()Ljava/lang/String;
      //   378: astore 26
      //   380: aload 25
      //   382: aload 26
      //   384: invokevirtual 168	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   387: invokevirtual 172	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   390: astore 27
      //   392: ldc 151
      //   394: aload 27
      //   396: aload 22
      //   398: invokestatic 176	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
      //   401: goto -87 -> 314
      //   404: astore 28
      //   406: aload_1
      //   407: ifnull +60 -> 467
      //   410: aload_1
      //   411: invokevirtual 115	org/apache/http/impl/DefaultHttpServerConnection:shutdown	()V
      //   414: aload_0
      //   415: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   418: invokestatic 119	com/google/android/music/download/stream/StreamingHttpServer:access$200	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Ljava/util/LinkedList;
      //   421: astore 29
      //   423: aload 29
      //   425: monitorenter
      //   426: aload_0
      //   427: getfield 18	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:this$0	Lcom/google/android/music/download/stream/StreamingHttpServer;
      //   430: invokestatic 119	com/google/android/music/download/stream/StreamingHttpServer:access$200	(Lcom/google/android/music/download/stream/StreamingHttpServer;)Ljava/util/LinkedList;
      //   433: aload_0
      //   434: invokevirtual 125	java/util/LinkedList:remove	(Ljava/lang/Object;)Z
      //   437: istore 30
      //   439: aload 29
      //   441: monitorexit
      //   442: aload 28
      //   444: athrow
      //   445: aload_0
      //   446: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   449: ifnull -127 -> 322
      //   452: aload_0
      //   453: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   456: invokevirtual 130	java/net/Socket:close	()V
      //   459: goto -137 -> 322
      //   462: astore 31
      //   464: goto -142 -> 322
      //   467: aload_0
      //   468: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   471: ifnull -57 -> 414
      //   474: aload_0
      //   475: getfield 29	com/google/android/music/download/stream/StreamingHttpServer$WorkerThread:mSocket	Ljava/net/Socket;
      //   478: invokevirtual 130	java/net/Socket:close	()V
      //   481: goto -67 -> 414
      //   484: astore 32
      //   486: goto -72 -> 414
      //   489: astore 28
      //   491: aload 29
      //   493: monitorexit
      //   494: aload 28
      //   496: athrow
      //   497: astore 28
      //   499: aload 4
      //   501: astore_1
      //   502: goto -96 -> 406
      //   505: astore 22
      //   507: aload 4
      //   509: astore_1
      //   510: goto -238 -> 272
      //
      // Exception table:
      //   from	to	target	type
      //   202	207	257	java/io/IOException
      //   240	254	257	java/io/IOException
      //   219	235	262	finally
      //   264	267	262	finally
      //   24	33	270	java/lang/Exception
      //   334	351	351	finally
      //   24	33	404	finally
      //   272	314	404	finally
      //   359	401	404	finally
      //   318	322	462	java/io/IOException
      //   445	459	462	java/io/IOException
      //   410	414	484	java/io/IOException
      //   467	481	484	java/io/IOException
      //   426	442	489	finally
      //   491	494	489	finally
      //   33	197	497	finally
      //   33	197	505	java/lang/Exception
    }

    public void shutdown()
    {
      if (StreamingHttpServer.LOGV)
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Worker.shutdown() for request: ");
        DownloadRequest localDownloadRequest = getDownloadRequest();
        String str = localDownloadRequest;
        Log.d("StreamingHttpServer", str);
      }
      interrupt();
      try
      {
        this.mSocket.close();
        return;
      }
      catch (Exception localException)
      {
      }
    }
  }

  private class RequestAcceptorThread extends Thread
  {
    RequestAcceptorThread()
    {
      super();
    }

    private void shutdownOldWorkers()
    {
      synchronized (StreamingHttpServer.this.mWorkers)
      {
        Iterator localIterator = StreamingHttpServer.this.mWorkers.iterator();
        while (localIterator.hasNext())
        {
          StreamingHttpServer.WorkerThread localWorkerThread = (StreamingHttpServer.WorkerThread)localIterator.next();
          DownloadRequest localDownloadRequest = localWorkerThread.getDownloadRequest();
          if ((localDownloadRequest != null) && (StreamingHttpServer.this.mAllowedStreams.findStreamByRequest(localDownloadRequest) == null))
          {
            localWorkerThread.shutdown();
            localIterator.remove();
          }
        }
      }
    }

    public void run()
    {
      label231: 
      while (true)
      {
        StringBuilder localStringBuilder1;
        try
        {
          if (StreamingHttpServer.this.mShutdown)
            return;
          Socket localSocket = StreamingHttpServer.this.mServerSocket.accept();
          synchronized (StreamingHttpServer.this.mWorkers)
          {
            StreamingHttpServer localStreamingHttpServer = StreamingHttpServer.this;
            StreamingHttpServer.WorkerThread localWorkerThread = new StreamingHttpServer.WorkerThread(localStreamingHttpServer, localSocket);
            boolean bool = StreamingHttpServer.this.mWorkers.add(localWorkerThread);
            localWorkerThread.start();
            if ((!StreamingHttpServer.LOGV) || (StreamingHttpServer.this.mWorkers.size() <= 2))
              break label231;
            localStringBuilder1 = new StringBuilder();
            StringBuilder localStringBuilder2 = localStringBuilder1.append("More than 2 worker running: ");
            Iterator localIterator = StreamingHttpServer.this.mWorkers.iterator();
            if (localIterator.hasNext())
            {
              DownloadRequest localDownloadRequest = ((StreamingHttpServer.WorkerThread)localIterator.next()).getDownloadRequest();
              StringBuilder localStringBuilder3 = localStringBuilder1.append(localDownloadRequest);
            }
          }
        }
        catch (IOException localIOException)
        {
          if (StreamingHttpServer.this.mShutdown)
            return;
          StringBuilder localStringBuilder4 = new StringBuilder().append("RequestAcceptorThread exited abnormally: ");
          String str1 = localIOException.getMessage();
          String str2 = str1;
          Log.w("StreamingHttpServer", str2, localIOException);
          return;
        }
        String str3 = localStringBuilder1.toString();
        Log.w("StreamingHttpServer", str3);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.stream.StreamingHttpServer
 * JD-Core Version:    0.6.2
 */