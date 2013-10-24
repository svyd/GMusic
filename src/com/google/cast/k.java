package com.google.cast;

import android.net.Uri;

final class k extends NetworkRequest
{
  private Uri a;
  private Uri b;
  private long c;

  public k(CastContext paramCastContext, Uri paramUri)
  {
    super(paramCastContext);
    this.a = paramUri;
  }

  public Uri a()
  {
    return this.b;
  }

  public long b()
  {
    return this.c;
  }

  // ERROR //
  public final int execute()
  {
    // Byte code:
    //   0: bipush 255
    //   2: istore_1
    //   3: new 33	org/json/JSONObject
    //   6: dup
    //   7: invokespecial 36	org/json/JSONObject:<init>	()V
    //   10: astore_2
    //   11: aload_2
    //   12: ldc 38
    //   14: iconst_0
    //   15: invokevirtual 42	org/json/JSONObject:put	(Ljava/lang/String;I)Lorg/json/JSONObject;
    //   18: astore_3
    //   19: aload_0
    //   20: getfield 17	com/google/cast/k:a	Landroid/net/Uri;
    //   23: astore 4
    //   25: aload_2
    //   26: invokestatic 48	com/google/cast/MimeData:createJsonData	(Lorg/json/JSONObject;)Lcom/google/cast/MimeData;
    //   29: astore 5
    //   31: getstatic 52	com/google/cast/k:DEFAULT_TIMEOUT	I
    //   34: istore 6
    //   36: aload_0
    //   37: aload 4
    //   39: aload 5
    //   41: iload 6
    //   43: invokevirtual 56	com/google/cast/k:performHttpPost	(Landroid/net/Uri;Lcom/google/cast/MimeData;I)Lcom/google/cast/SimpleHttpRequest;
    //   46: astore_2
    //   47: aload_2
    //   48: invokeinterface 61 1 0
    //   53: istore 7
    //   55: iload 7
    //   57: sipush 404
    //   60: if_icmpne +8 -> 68
    //   63: bipush 255
    //   65: istore_1
    //   66: iload_1
    //   67: ireturn
    //   68: iload 7
    //   70: sipush 403
    //   73: if_icmpne +9 -> 82
    //   76: bipush 255
    //   78: istore_1
    //   79: goto -13 -> 66
    //   82: iload 7
    //   84: sipush 200
    //   87: if_icmpne -21 -> 66
    //   90: aload_2
    //   91: invokeinterface 65 1 0
    //   96: astore 8
    //   98: aload 8
    //   100: astore 9
    //   102: aload 9
    //   104: ifnull +20 -> 124
    //   107: aload 9
    //   109: invokevirtual 69	com/google/cast/MimeData:getType	()Ljava/lang/String;
    //   112: astore 10
    //   114: ldc 71
    //   116: aload 10
    //   118: invokevirtual 77	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   121: ifne +17 -> 138
    //   124: bipush 255
    //   126: istore_1
    //   127: goto -61 -> 66
    //   130: astore 11
    //   132: bipush 255
    //   134: istore_1
    //   135: goto -69 -> 66
    //   138: aload 9
    //   140: invokevirtual 80	com/google/cast/MimeData:getTextData	()Ljava/lang/String;
    //   143: astore 12
    //   145: aload 12
    //   147: ifnonnull +9 -> 156
    //   150: bipush 255
    //   152: istore_1
    //   153: goto -87 -> 66
    //   156: new 33	org/json/JSONObject
    //   159: dup
    //   160: aload 12
    //   162: invokespecial 83	org/json/JSONObject:<init>	(Ljava/lang/String;)V
    //   165: astore_2
    //   166: aload_2
    //   167: ldc 85
    //   169: invokevirtual 89	org/json/JSONObject:getString	(Ljava/lang/String;)Ljava/lang/String;
    //   172: astore 12
    //   174: aload 12
    //   176: ifnonnull +9 -> 185
    //   179: bipush 255
    //   181: istore_1
    //   182: goto -116 -> 66
    //   185: aload 12
    //   187: invokestatic 95	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
    //   190: astore 13
    //   192: aload_0
    //   193: aload 13
    //   195: putfield 20	com/google/cast/k:b	Landroid/net/Uri;
    //   198: aload_2
    //   199: ldc 97
    //   201: ldc2_w 98
    //   204: invokevirtual 103	org/json/JSONObject:optLong	(Ljava/lang/String;J)J
    //   207: ldc2_w 104
    //   210: lmul
    //   211: lstore 14
    //   213: aload_0
    //   214: lload 14
    //   216: putfield 23	com/google/cast/k:c	J
    //   219: iconst_0
    //   220: istore_1
    //   221: goto -155 -> 66
    //   224: astore 16
    //   226: bipush 255
    //   228: istore_1
    //   229: goto -163 -> 66
    //   232: astore 17
    //   234: goto -168 -> 66
    //   237: astore 18
    //   239: goto -220 -> 19
    //
    // Exception table:
    //   from	to	target	type
    //   3	11	130	java/util/concurrent/TimeoutException
    //   11	19	130	java/util/concurrent/TimeoutException
    //   19	98	130	java/util/concurrent/TimeoutException
    //   156	219	224	org/json/JSONException
    //   3	11	232	java/io/IOException
    //   11	19	232	java/io/IOException
    //   19	98	232	java/io/IOException
    //   11	19	237	org/json/JSONException
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.k
 * JD-Core Version:    0.6.2
 */