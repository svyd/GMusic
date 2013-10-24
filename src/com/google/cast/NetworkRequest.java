package com.google.cast;

import java.util.concurrent.TimeUnit;

public abstract class NetworkRequest
{
  public static final int DEFAULT_TIMEOUT = (int)TimeUnit.SECONDS.toMillis(10L);
  private SimpleHttpRequest a;
  private Object b;
  private boolean c;
  private Logger d;
  protected CastContext mCastContext;

  protected NetworkRequest(CastContext paramCastContext)
    throws IllegalArgumentException
  {
    Object localObject = new Object();
    this.b = localObject;
    if (paramCastContext == null)
      throw new IllegalArgumentException("cast context cannot be null");
    this.mCastContext = paramCastContext;
    Logger localLogger = new Logger("NetworkRequest");
    this.d = localLogger;
  }

  public final void cancel()
  {
    synchronized (this.b)
    {
      this.c = true;
      if (this.a != null)
        this.a.cancel();
      return;
    }
  }

  protected SimpleHttpRequest constructHttpRequest(int paramInt)
  {
    CastContext localCastContext = this.mCastContext;
    return new l(localCastContext, paramInt, paramInt);
  }

  public abstract int execute();

  protected final boolean isCancelled()
  {
    return this.c;
  }

  // ERROR //
  protected final SimpleHttpRequest performHttpDelete(android.net.Uri paramUri, int paramInt)
    throws java.io.IOException, java.util.concurrent.TimeoutException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   4: astore_3
    //   5: iconst_1
    //   6: anewarray 4	java/lang/Object
    //   9: astore 4
    //   11: aload 4
    //   13: iconst_0
    //   14: aload_1
    //   15: aastore
    //   16: aload_3
    //   17: ldc 83
    //   19: aload 4
    //   21: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   24: aload_0
    //   25: iload_2
    //   26: invokevirtual 88	com/google/cast/NetworkRequest:constructHttpRequest	(I)Lcom/google/cast/SimpleHttpRequest;
    //   29: astore 5
    //   31: aload 5
    //   33: ldc 90
    //   35: ldc 92
    //   37: invokeinterface 96 3 0
    //   42: aload_0
    //   43: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   46: astore 6
    //   48: aload 6
    //   50: monitorenter
    //   51: aload_0
    //   52: aload 5
    //   54: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   57: aload 6
    //   59: monitorexit
    //   60: aload 5
    //   62: aload_1
    //   63: invokeinterface 100 2 0
    //   68: aload_0
    //   69: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   72: astore 7
    //   74: aload 7
    //   76: monitorenter
    //   77: aconst_null
    //   78: astore 8
    //   80: aload_0
    //   81: aload 8
    //   83: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   86: aload 7
    //   88: monitorexit
    //   89: aload 5
    //   91: areturn
    //   92: astore 5
    //   94: aload 6
    //   96: monitorexit
    //   97: aload 5
    //   99: athrow
    //   100: astore 5
    //   102: aload_0
    //   103: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   106: astore 9
    //   108: iconst_1
    //   109: anewarray 4	java/lang/Object
    //   112: astore 10
    //   114: aload 10
    //   116: iconst_0
    //   117: aload 5
    //   119: aastore
    //   120: aload 9
    //   122: ldc 102
    //   124: aload 10
    //   126: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   129: aload 5
    //   131: athrow
    //   132: astore 11
    //   134: aload_0
    //   135: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   138: astore 12
    //   140: aload 12
    //   142: monitorenter
    //   143: aconst_null
    //   144: astore 13
    //   146: aload_0
    //   147: aload 13
    //   149: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   152: aload 12
    //   154: monitorexit
    //   155: aload 11
    //   157: athrow
    //   158: astore 5
    //   160: aload 7
    //   162: monitorexit
    //   163: aload 5
    //   165: athrow
    //   166: astore 5
    //   168: aload_0
    //   169: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   172: astore 14
    //   174: iconst_1
    //   175: anewarray 4	java/lang/Object
    //   178: astore 15
    //   180: aload 15
    //   182: iconst_0
    //   183: aload 5
    //   185: aastore
    //   186: aload 14
    //   188: ldc 104
    //   190: aload 15
    //   192: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   195: aload 5
    //   197: athrow
    //   198: astore 11
    //   200: aload 12
    //   202: monitorexit
    //   203: aload 11
    //   205: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   51	60	92	finally
    //   94	97	92	finally
    //   42	51	100	java/io/IOException
    //   60	68	100	java/io/IOException
    //   97	100	100	java/io/IOException
    //   42	51	132	finally
    //   60	68	132	finally
    //   97	100	132	finally
    //   102	132	132	finally
    //   168	198	132	finally
    //   80	89	158	finally
    //   160	163	158	finally
    //   42	51	166	java/util/concurrent/TimeoutException
    //   60	68	166	java/util/concurrent/TimeoutException
    //   97	100	166	java/util/concurrent/TimeoutException
    //   146	155	198	finally
    //   200	203	198	finally
  }

  // ERROR //
  protected final SimpleHttpRequest performHttpGet(android.net.Uri paramUri, int paramInt)
    throws java.io.IOException, java.util.concurrent.TimeoutException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   4: astore_3
    //   5: iconst_1
    //   6: anewarray 4	java/lang/Object
    //   9: astore 4
    //   11: aload 4
    //   13: iconst_0
    //   14: aload_1
    //   15: aastore
    //   16: aload_3
    //   17: ldc 107
    //   19: aload 4
    //   21: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   24: aload_0
    //   25: iload_2
    //   26: invokevirtual 88	com/google/cast/NetworkRequest:constructHttpRequest	(I)Lcom/google/cast/SimpleHttpRequest;
    //   29: astore 5
    //   31: aload 5
    //   33: ldc 90
    //   35: ldc 92
    //   37: invokeinterface 96 3 0
    //   42: aload_0
    //   43: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   46: astore 6
    //   48: aload 6
    //   50: monitorenter
    //   51: aload_0
    //   52: aload 5
    //   54: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   57: aload 6
    //   59: monitorexit
    //   60: aload 5
    //   62: aload_1
    //   63: invokeinterface 110 2 0
    //   68: aload_0
    //   69: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   72: astore 7
    //   74: aload 7
    //   76: monitorenter
    //   77: aconst_null
    //   78: astore 8
    //   80: aload_0
    //   81: aload 8
    //   83: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   86: aload 7
    //   88: monitorexit
    //   89: aload 5
    //   91: areturn
    //   92: astore 5
    //   94: aload 6
    //   96: monitorexit
    //   97: aload 5
    //   99: athrow
    //   100: astore 5
    //   102: aload_0
    //   103: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   106: astore 9
    //   108: iconst_1
    //   109: anewarray 4	java/lang/Object
    //   112: astore 10
    //   114: aload 10
    //   116: iconst_0
    //   117: aload 5
    //   119: aastore
    //   120: aload 9
    //   122: ldc 112
    //   124: aload 10
    //   126: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   129: aload 5
    //   131: athrow
    //   132: astore 11
    //   134: aload_0
    //   135: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   138: astore 12
    //   140: aload 12
    //   142: monitorenter
    //   143: aconst_null
    //   144: astore 13
    //   146: aload_0
    //   147: aload 13
    //   149: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   152: aload 12
    //   154: monitorexit
    //   155: aload 11
    //   157: athrow
    //   158: astore 5
    //   160: aload 7
    //   162: monitorexit
    //   163: aload 5
    //   165: athrow
    //   166: astore 5
    //   168: aload_0
    //   169: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   172: astore 14
    //   174: iconst_1
    //   175: anewarray 4	java/lang/Object
    //   178: astore 15
    //   180: aload 15
    //   182: iconst_0
    //   183: aload 5
    //   185: aastore
    //   186: aload 14
    //   188: ldc 114
    //   190: aload 15
    //   192: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   195: aload 5
    //   197: athrow
    //   198: astore 11
    //   200: aload 12
    //   202: monitorexit
    //   203: aload 11
    //   205: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   51	60	92	finally
    //   94	97	92	finally
    //   42	51	100	java/io/IOException
    //   60	68	100	java/io/IOException
    //   97	100	100	java/io/IOException
    //   42	51	132	finally
    //   60	68	132	finally
    //   97	100	132	finally
    //   102	132	132	finally
    //   168	198	132	finally
    //   80	89	158	finally
    //   160	163	158	finally
    //   42	51	166	java/util/concurrent/TimeoutException
    //   60	68	166	java/util/concurrent/TimeoutException
    //   97	100	166	java/util/concurrent/TimeoutException
    //   146	155	198	finally
    //   200	203	198	finally
  }

  // ERROR //
  protected final SimpleHttpRequest performHttpPost(android.net.Uri paramUri, MimeData paramMimeData, int paramInt)
    throws java.io.IOException, java.util.concurrent.TimeoutException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   4: astore 4
    //   6: iconst_1
    //   7: anewarray 4	java/lang/Object
    //   10: astore 5
    //   12: aload 5
    //   14: iconst_0
    //   15: aload_1
    //   16: aastore
    //   17: aload 4
    //   19: ldc 118
    //   21: aload 5
    //   23: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   26: aload_0
    //   27: iload_3
    //   28: invokevirtual 88	com/google/cast/NetworkRequest:constructHttpRequest	(I)Lcom/google/cast/SimpleHttpRequest;
    //   31: astore 6
    //   33: aload 6
    //   35: ldc 90
    //   37: ldc 92
    //   39: invokeinterface 96 3 0
    //   44: aload_0
    //   45: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   48: astore 7
    //   50: aload 7
    //   52: monitorenter
    //   53: aload_0
    //   54: aload 6
    //   56: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   59: aload 7
    //   61: monitorexit
    //   62: aload 6
    //   64: aload_1
    //   65: aload_2
    //   66: invokeinterface 122 3 0
    //   71: aload_0
    //   72: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   75: astore 8
    //   77: aload 8
    //   79: monitorenter
    //   80: aconst_null
    //   81: astore 9
    //   83: aload_0
    //   84: aload 9
    //   86: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   89: aload 8
    //   91: monitorexit
    //   92: aload 6
    //   94: areturn
    //   95: astore 6
    //   97: aload 7
    //   99: monitorexit
    //   100: aload 6
    //   102: athrow
    //   103: astore 6
    //   105: aload_0
    //   106: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   109: astore 10
    //   111: iconst_1
    //   112: anewarray 4	java/lang/Object
    //   115: astore 11
    //   117: aload 11
    //   119: iconst_0
    //   120: aload 6
    //   122: aastore
    //   123: aload 10
    //   125: ldc 124
    //   127: aload 11
    //   129: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   132: aload 6
    //   134: athrow
    //   135: astore 12
    //   137: aload_0
    //   138: getfield 41	com/google/cast/NetworkRequest:b	Ljava/lang/Object;
    //   141: astore 13
    //   143: aload 13
    //   145: monitorenter
    //   146: aconst_null
    //   147: astore 14
    //   149: aload_0
    //   150: aload 14
    //   152: putfield 60	com/google/cast/NetworkRequest:a	Lcom/google/cast/SimpleHttpRequest;
    //   155: aload 13
    //   157: monitorexit
    //   158: aload 12
    //   160: athrow
    //   161: astore 6
    //   163: aload 8
    //   165: monitorexit
    //   166: aload 6
    //   168: athrow
    //   169: astore 6
    //   171: aload_0
    //   172: getfield 55	com/google/cast/NetworkRequest:d	Lcom/google/cast/Logger;
    //   175: astore 15
    //   177: iconst_1
    //   178: anewarray 4	java/lang/Object
    //   181: astore 16
    //   183: aload 16
    //   185: iconst_0
    //   186: aload 6
    //   188: aastore
    //   189: aload 15
    //   191: ldc 126
    //   193: aload 16
    //   195: invokevirtual 86	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
    //   198: aload 6
    //   200: athrow
    //   201: astore 12
    //   203: aload 13
    //   205: monitorexit
    //   206: aload 12
    //   208: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   53	62	95	finally
    //   97	100	95	finally
    //   44	53	103	java/io/IOException
    //   62	71	103	java/io/IOException
    //   100	103	103	java/io/IOException
    //   44	53	135	finally
    //   62	71	135	finally
    //   100	103	135	finally
    //   105	135	135	finally
    //   171	201	135	finally
    //   83	92	161	finally
    //   163	166	161	finally
    //   44	53	169	java/util/concurrent/TimeoutException
    //   62	71	169	java/util/concurrent/TimeoutException
    //   100	103	169	java/util/concurrent/TimeoutException
    //   149	158	201	finally
    //   203	206	201	finally
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.NetworkRequest
 * JD-Core Version:    0.6.2
 */