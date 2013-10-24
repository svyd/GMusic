package com.google.cast;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.media.MediaItemStatus;
import android.support.v7.media.MediaItemStatus.Builder;
import android.support.v7.media.MediaRouter.ControlRequestCallback;
import android.text.TextUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

final class c extends b
{
  private static final MimeData b = null;
  private static final String[] c;
  private static final String[] d;
  private static final Set<String> e;
  private static final Logger f = new Logger("CastMediaRouteProvider");
  private String g = "GoogleCastPlayer";
  private MimeData h;
  private int i;
  private boolean j;

  static
  {
    String[] arrayOfString1 = new String[3];
    arrayOfString1[0] = "audio/*";
    arrayOfString1[1] = "image/*";
    arrayOfString1[2] = "video/*";
    c = arrayOfString1;
    String[] arrayOfString2 = new String[5];
    arrayOfString2[0] = "android.media.intent.action.PAUSE";
    arrayOfString2[1] = "android.media.intent.action.RESUME";
    arrayOfString2[2] = "android.media.intent.action.STOP";
    arrayOfString2[3] = "android.media.intent.action.SEEK";
    arrayOfString2[4] = "android.media.intent.action.GET_STATUS";
    d = arrayOfString2;
    e = new HashSet();
    boolean bool1 = e.add("android.media.intent.extra.ITEM_ID");
    boolean bool2 = e.add("android.media.intent.extra.ITEM_STATUS");
    boolean bool3 = e.add("android.media.intent.extra.ITEM_POSITION");
    boolean bool4 = e.add("android.media.intent.extra.ITEM_METADATA");
    boolean bool5 = e.add("android.media.intent.extra.ITEM_STATUS_UPDATE_RECEIVER");
  }

  public c(CastContext paramCastContext, String paramString, MimeData paramMimeData, int paramInt, boolean paramBoolean)
  {
    super(paramCastContext);
    MimeData localMimeData = b;
    this.h = localMimeData;
    if (!TextUtils.isEmpty(paramString))
    {
      this.g = paramString;
      this.h = paramMimeData;
    }
    this.i = paramInt;
    this.j = paramBoolean;
    IntentFilter localIntentFilter1 = new IntentFilter();
    localIntentFilter1.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
    localIntentFilter1.addAction("android.media.intent.action.PLAY");
    localIntentFilter1.addDataScheme("http");
    localIntentFilter1.addDataScheme("https");
    String[] arrayOfString1 = c;
    int m = arrayOfString1.length;
    int n = 0;
    while (n < m)
    {
      String str1 = arrayOfString1[n];
      a(localIntentFilter1, str1);
      n += 1;
    }
    a(localIntentFilter1);
    String[] arrayOfString2 = d;
    int i1 = arrayOfString2.length;
    while (k < i1)
    {
      String str2 = arrayOfString2[k];
      IntentFilter localIntentFilter2 = new IntentFilter();
      localIntentFilter2.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
      localIntentFilter2.addAction(str2);
      a(localIntentFilter2);
      k += 1;
    }
    IntentFilter localIntentFilter3 = new IntentFilter();
    localIntentFilter3.addCategory("com.google.cast.CATEGORY_REMOTE_PLAYBACK_CAST_EXTENSIONS");
    localIntentFilter3.addAction("com.google.cast.ACTION_SYNC_STATUS");
    a(localIntentFilter3);
  }

  private Bundle a(int paramInt)
  {
    Bundle localBundle = new Bundle();
    localBundle.putInt("com.google.cast.EXTRA_ERROR_CODE", paramInt);
    return localBundle;
  }

  private Bundle a(JSONObject paramJSONObject)
  {
    Bundle localBundle1 = new Bundle();
    Iterator localIterator = paramJSONObject.keys();
    while (localIterator.hasNext())
    {
      String str1;
      Object localObject1;
      try
      {
        str1 = (String)localIterator.next();
        localObject1 = paramJSONObject.get(str1);
        Object localObject2 = JSONObject.NULL;
        if (localObject1 != localObject2)
          break label68;
        localBundle1.putParcelable(str1, null);
      }
      catch (JSONException localJSONException)
      {
      }
      continue;
      label68: if ((localObject1 instanceof String))
      {
        String str2 = (String)localObject1;
        localBundle1.putString(str1, str2);
      }
      else if ((localObject1 instanceof Boolean))
      {
        boolean bool = ((Boolean)localObject1).booleanValue();
        localBundle1.putBoolean(str1, bool);
      }
      else if ((localObject1 instanceof Integer))
      {
        int k = ((Integer)localObject1).intValue();
        localBundle1.putInt(str1, k);
      }
      else if ((localObject1 instanceof Long))
      {
        long l = ((Long)localObject1).longValue();
        localBundle1.putLong(str1, l);
      }
      else if ((localObject1 instanceof Double))
      {
        double d1 = ((Double)localObject1).doubleValue();
        localBundle1.putDouble(str1, d1);
      }
      else if ((localObject1 instanceof JSONObject))
      {
        JSONObject localJSONObject = (JSONObject)localObject1;
        Bundle localBundle2 = a(localJSONObject);
        localBundle1.putBundle(str1, localBundle2);
      }
    }
    return localBundle1;
  }

  private JSONObject a(Bundle paramBundle, Set<String> paramSet)
  {
    JSONObject localJSONObject1 = new JSONObject();
    Iterator localIterator = paramBundle.keySet().iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (!paramSet.contains(str))
      {
        Object localObject = paramBundle.get(str);
        try
        {
          if (!(localObject instanceof Bundle))
            break label102;
          Bundle localBundle = (Bundle)localObject;
          JSONObject localJSONObject2 = a(localBundle, paramSet);
          JSONObject localJSONObject3 = localJSONObject1.put(str, localJSONObject2);
        }
        catch (JSONException localJSONException)
        {
        }
        continue;
        label102: JSONObject localJSONObject4 = localJSONObject1.put(str, localObject);
      }
    }
    return localJSONObject1;
  }

  protected b.a a(CastDevice paramCastDevice, String paramString)
  {
    return new a(paramCastDevice, paramString);
  }

  private class b
  {
    public Intent a;
    public MediaRouter.ControlRequestCallback b;

    public b(Intent paramControlRequestCallback, MediaRouter.ControlRequestCallback arg3)
    {
      this.a = paramControlRequestCallback;
      Object localObject;
      this.b = localObject;
    }
  }

  private final class a extends b.a
    implements ApplicationSession.Listener, MediaProtocolCommand.Listener
  {
    private ApplicationSession e;
    private MediaProtocolMessageStream f;
    private LinkedList<MediaProtocolCommand> g;
    private Map<String, PendingIntent> h;
    private c.b i;
    private String j;
    private double k = -1.0D;
    private boolean l;
    private boolean m;
    private Runnable n;
    private long o;
    private boolean p;

    public a(CastDevice paramString, String arg3)
    {
      super(paramString, str);
      LinkedList localLinkedList = new LinkedList();
      this.g = localLinkedList;
      HashMap localHashMap = new HashMap();
      this.h = localHashMap;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          c.a.a(c.a.this);
        }
      };
      this.n = local1;
      this.o = 0L;
    }

    private double a(long paramLong)
    {
      return paramLong / 1000.0D;
    }

    private void a(MediaProtocolCommand paramMediaProtocolCommand, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      paramMediaProtocolCommand.setListener(this);
      if (paramControlRequestCallback != null)
        Object localObject = paramMediaProtocolCommand.putUserObject("cb", paramControlRequestCallback);
      boolean bool = this.g.add(paramMediaProtocolCommand);
    }

    private void a(String paramString)
      throws IllegalStateException
    {
      if (this.j == null)
        return;
      if (this.j.equals(paramString))
        return;
      throw new IllegalStateException("item ID does not match");
    }

    // ERROR //
    private boolean a(c.b paramb)
    {
      // Byte code:
      //   0: aconst_null
      //   1: astore_2
      //   2: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   5: astore_3
      //   6: iconst_1
      //   7: anewarray 127	java/lang/Object
      //   10: astore 4
      //   12: aload_0
      //   13: getfield 129	com/google/cast/c$a:e	Lcom/google/cast/ApplicationSession;
      //   16: astore 5
      //   18: aload 4
      //   20: iconst_0
      //   21: aload 5
      //   23: aastore
      //   24: aload_3
      //   25: ldc 131
      //   27: aload 4
      //   29: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   32: aload_0
      //   33: getfield 129	com/google/cast/c$a:e	Lcom/google/cast/ApplicationSession;
      //   36: ifnonnull +18 -> 54
      //   39: aload_0
      //   40: aload_1
      //   41: putfield 138	com/google/cast/c$a:i	Lcom/google/cast/c$b;
      //   44: aload_0
      //   45: invokespecial 101	com/google/cast/c$a:c	()V
      //   48: iconst_1
      //   49: istore 6
      //   51: iload 6
      //   53: ireturn
      //   54: aload_0
      //   55: getfield 129	com/google/cast/c$a:e	Lcom/google/cast/ApplicationSession;
      //   58: invokevirtual 144	com/google/cast/ApplicationSession:isStarting	()Z
      //   61: ifeq +34 -> 95
      //   64: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   67: astore 7
      //   69: iconst_0
      //   70: anewarray 127	java/lang/Object
      //   73: astore 8
      //   75: aload 7
      //   77: ldc 146
      //   79: aload 8
      //   81: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   84: aload_0
      //   85: aload_1
      //   86: putfield 138	com/google/cast/c$a:i	Lcom/google/cast/c$b;
      //   89: iconst_1
      //   90: istore 6
      //   92: goto -41 -> 51
      //   95: aload_1
      //   96: getfield 151	com/google/cast/c$b:b	Landroid/support/v7/media/MediaRouter$ControlRequestCallback;
      //   99: astore 9
      //   101: aload_0
      //   102: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   105: ifnonnull +71 -> 176
      //   108: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   111: astore 10
      //   113: iconst_0
      //   114: anewarray 127	java/lang/Object
      //   117: astore 11
      //   119: aload 10
      //   121: ldc 155
      //   123: aload 11
      //   125: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   128: aload_0
      //   129: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   132: invokevirtual 159	com/google/cast/c:getContext	()Landroid/content/Context;
      //   135: astore 12
      //   137: getstatic 165	com/google/cast/R$string:error_no_session	I
      //   140: istore 13
      //   142: aload 12
      //   144: iload 13
      //   146: invokevirtual 171	android/content/Context:getString	(I)Ljava/lang/String;
      //   149: astore 14
      //   151: aload_0
      //   152: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   155: iconst_3
      //   156: invokestatic 174	com/google/cast/c:a	(Lcom/google/cast/c;I)Landroid/os/Bundle;
      //   159: astore 15
      //   161: aload 9
      //   163: aload 14
      //   165: aload 15
      //   167: invokevirtual 180	android/support/v7/media/MediaRouter$ControlRequestCallback:onError	(Ljava/lang/String;Landroid/os/Bundle;)V
      //   170: iconst_0
      //   171: istore 6
      //   173: goto -122 -> 51
      //   176: aload_1
      //   177: getfield 183	com/google/cast/c$b:a	Landroid/content/Intent;
      //   180: astore 16
      //   182: aload 16
      //   184: invokevirtual 189	android/content/Intent:getAction	()Ljava/lang/String;
      //   187: astore 17
      //   189: aload 17
      //   191: ldc 191
      //   193: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   196: ifeq +608 -> 804
      //   199: aload 16
      //   201: invokevirtual 195	android/content/Intent:getData	()Landroid/net/Uri;
      //   204: ifnull +600 -> 804
      //   207: aload_0
      //   208: invokespecial 197	com/google/cast/c$a:d	()V
      //   211: aload 16
      //   213: invokevirtual 195	android/content/Intent:getData	()Landroid/net/Uri;
      //   216: lstore 18
      //   218: lload 18
      //   220: ifnonnull +9 -> 229
      //   223: iconst_0
      //   224: istore 6
      //   226: goto -175 -> 51
      //   229: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   232: astore 20
      //   234: iconst_1
      //   235: anewarray 127	java/lang/Object
      //   238: astore 21
      //   240: aload 21
      //   242: iconst_0
      //   243: lload 18
      //   245: aastore
      //   246: aload 20
      //   248: ldc 199
      //   250: aload 21
      //   252: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   255: invokestatic 205	java/util/UUID:randomUUID	()Ljava/util/UUID;
      //   258: invokevirtual 208	java/util/UUID:toString	()Ljava/lang/String;
      //   261: astore_2
      //   262: new 210	com/google/cast/ContentMetadata
      //   265: dup
      //   266: invokespecial 211	com/google/cast/ContentMetadata:<init>	()V
      //   269: astore 22
      //   271: aload_0
      //   272: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   275: astore 23
      //   277: aload 16
      //   279: invokevirtual 215	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   282: astore 24
      //   284: invokestatic 218	com/google/cast/c:c	()Ljava/util/Set;
      //   287: astore 25
      //   289: aload 23
      //   291: aload 24
      //   293: aload 25
      //   295: invokestatic 221	com/google/cast/c:a	(Lcom/google/cast/c;Landroid/os/Bundle;Ljava/util/Set;)Lorg/json/JSONObject;
      //   298: astore 17
      //   300: aload 22
      //   302: aload 17
      //   304: invokevirtual 225	com/google/cast/ContentMetadata:setContentInfo	(Lorg/json/JSONObject;)V
      //   307: aload 16
      //   309: ldc 227
      //   311: invokevirtual 231	android/content/Intent:getBundleExtra	(Ljava/lang/String;)Landroid/os/Bundle;
      //   314: astore 26
      //   316: aload 26
      //   318: ifnull +403 -> 721
      //   321: aload 26
      //   323: ldc 233
      //   325: invokevirtual 239	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
      //   328: ifeq +24 -> 352
      //   331: aload 26
      //   333: ldc 233
      //   335: invokevirtual 242	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   338: astore 28
      //   340: aload 28
      //   342: ifnull +10 -> 352
      //   345: aload 22
      //   347: aload 28
      //   349: invokevirtual 245	com/google/cast/ContentMetadata:setTitle	(Ljava/lang/String;)V
      //   352: aload 26
      //   354: ldc 247
      //   356: invokevirtual 239	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
      //   359: ifeq +34 -> 393
      //   362: aload 26
      //   364: ldc 247
      //   366: invokevirtual 242	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   369: astore 28
      //   371: aload 28
      //   373: invokestatic 253	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
      //   376: ifne +17 -> 393
      //   379: aload 28
      //   381: invokestatic 259	android/net/Uri:parse	(Ljava/lang/String;)Landroid/net/Uri;
      //   384: astore 29
      //   386: aload 22
      //   388: aload 29
      //   390: invokevirtual 263	com/google/cast/ContentMetadata:setImageUrl	(Landroid/net/Uri;)V
      //   393: aload 26
      //   395: ldc_w 265
      //   398: invokevirtual 239	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
      //   401: ifeq +34 -> 435
      //   404: aload 26
      //   406: ldc_w 265
      //   409: invokevirtual 242	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   412: astore 30
      //   414: aload 30
      //   416: astore 28
      //   418: aload 28
      //   420: ifnull +15 -> 435
      //   423: aload 17
      //   425: ldc_w 267
      //   428: aload 28
      //   430: invokevirtual 273	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
      //   433: astore 31
      //   435: aload 26
      //   437: ldc_w 275
      //   440: invokevirtual 239	android/os/Bundle:containsKey	(Ljava/lang/String;)Z
      //   443: ifeq +34 -> 477
      //   446: aload 26
      //   448: ldc_w 275
      //   451: invokevirtual 242	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   454: astore 30
      //   456: aload 30
      //   458: astore 26
      //   460: aload 26
      //   462: ifnull +15 -> 477
      //   465: aload 17
      //   467: ldc_w 277
      //   470: aload 26
      //   472: invokevirtual 273	org/json/JSONObject:put	(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
      //   475: astore 32
      //   477: aload 16
      //   479: ldc_w 279
      //   482: ldc2_w 71
      //   485: invokevirtual 283	android/content/Intent:getLongExtra	(Ljava/lang/String;J)J
      //   488: lstore 26
      //   490: lload 26
      //   492: ldc2_w 71
      //   495: lcmp
      //   496: ifle +291 -> 787
      //   499: aload_0
      //   500: lload 26
      //   502: invokespecial 285	com/google/cast/c$a:a	(J)D
      //   505: dstore 33
      //   507: aload_0
      //   508: dload 33
      //   510: putfield 53	com/google/cast/c$a:k	D
      //   513: aconst_null
      //   514: astore 17
      //   516: aload 16
      //   518: ldc_w 287
      //   521: invokevirtual 291	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
      //   524: checkcast 293	android/app/PendingIntent
      //   527: astore 16
      //   529: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   532: astore 35
      //   534: iconst_1
      //   535: anewarray 127	java/lang/Object
      //   538: astore 36
      //   540: aload 36
      //   542: iconst_0
      //   543: aload 16
      //   545: aastore
      //   546: aload 35
      //   548: ldc_w 295
      //   551: aload 36
      //   553: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   556: aload 16
      //   558: ifnull +17 -> 575
      //   561: aload_0
      //   562: getfield 65	com/google/cast/c$a:h	Ljava/util/Map;
      //   565: aload_2
      //   566: aload 16
      //   568: invokeinterface 300 3 0
      //   573: astore 37
      //   575: aload_0
      //   576: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   579: astore 38
      //   581: lload 18
      //   583: invokevirtual 301	android/net/Uri:toString	()Ljava/lang/String;
      //   586: astore 39
      //   588: aload 38
      //   590: aload 39
      //   592: aload 22
      //   594: aload 17
      //   596: invokevirtual 307	com/google/cast/MediaProtocolMessageStream:loadMedia	(Ljava/lang/String;Lcom/google/cast/ContentMetadata;Z)Lcom/google/cast/MediaProtocolCommand;
      //   599: astore 40
      //   601: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   604: astore 41
      //   606: iconst_1
      //   607: anewarray 127	java/lang/Object
      //   610: astore 42
      //   612: aload 42
      //   614: iconst_0
      //   615: aload_2
      //   616: aastore
      //   617: aload 41
      //   619: ldc_w 309
      //   622: aload 42
      //   624: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   627: aload 40
      //   629: ldc_w 311
      //   632: aload_2
      //   633: invokevirtual 93	com/google/cast/MediaProtocolCommand:putUserObject	(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
      //   636: astore 43
      //   638: aload_0
      //   639: iconst_1
      //   640: putfield 313	com/google/cast/c$a:m	Z
      //   643: aload_0
      //   644: aload 40
      //   646: aconst_null
      //   647: invokespecial 315	com/google/cast/c$a:a	(Lcom/google/cast/MediaProtocolCommand;Landroid/support/v7/media/MediaRouter$ControlRequestCallback;)V
      //   650: new 235	android/os/Bundle
      //   653: dup
      //   654: invokespecial 316	android/os/Bundle:<init>	()V
      //   657: astore 44
      //   659: aload 44
      //   661: ldc_w 318
      //   664: aload_2
      //   665: invokevirtual 322	android/os/Bundle:putString	(Ljava/lang/String;Ljava/lang/String;)V
      //   668: new 324	android/support/v7/media/MediaItemStatus$Builder
      //   671: dup
      //   672: iconst_3
      //   673: invokespecial 327	android/support/v7/media/MediaItemStatus$Builder:<init>	(I)V
      //   676: astore 45
      //   678: invokestatic 333	android/os/SystemClock:uptimeMillis	()J
      //   681: lstore 46
      //   683: aload 45
      //   685: lload 46
      //   687: invokevirtual 337	android/support/v7/media/MediaItemStatus$Builder:setTimestamp	(J)Landroid/support/v7/media/MediaItemStatus$Builder;
      //   690: invokevirtual 341	android/support/v7/media/MediaItemStatus$Builder:build	()Landroid/support/v7/media/MediaItemStatus;
      //   693: invokevirtual 346	android/support/v7/media/MediaItemStatus:asBundle	()Landroid/os/Bundle;
      //   696: astore 48
      //   698: aload 44
      //   700: ldc_w 348
      //   703: aload 48
      //   705: invokevirtual 351	android/os/Bundle:putBundle	(Ljava/lang/String;Landroid/os/Bundle;)V
      //   708: aload 9
      //   710: aload 44
      //   712: invokevirtual 355	android/support/v7/media/MediaRouter$ControlRequestCallback:onResult	(Landroid/os/Bundle;)V
      //   715: iconst_1
      //   716: istore 6
      //   718: goto -667 -> 51
      //   721: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   724: astore 49
      //   726: iconst_0
      //   727: anewarray 127	java/lang/Object
      //   730: astore 50
      //   732: aload 49
      //   734: ldc_w 357
      //   737: aload 50
      //   739: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   742: goto -265 -> 477
      //   745: astore 51
      //   747: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   750: astore 52
      //   752: iconst_1
      //   753: anewarray 127	java/lang/Object
      //   756: astore 53
      //   758: aload 51
      //   760: invokevirtual 360	java/lang/IllegalStateException:getMessage	()Ljava/lang/String;
      //   763: astore 54
      //   765: aload 53
      //   767: iconst_0
      //   768: aload 54
      //   770: aastore
      //   771: aload 52
      //   773: ldc_w 362
      //   776: aload 53
      //   778: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   781: iconst_0
      //   782: istore 6
      //   784: goto -733 -> 51
      //   787: ldc2_w 363
      //   790: lstore 26
      //   792: aload_0
      //   793: lload 26
      //   795: putfield 53	com/google/cast/c$a:k	D
      //   798: aconst_null
      //   799: astore 17
      //   801: goto -285 -> 516
      //   804: aload 17
      //   806: ldc_w 366
      //   809: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   812: ifeq +91 -> 903
      //   815: aload_0
      //   816: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   819: invokevirtual 369	com/google/cast/MediaProtocolMessageStream:stop	()V
      //   822: aload_0
      //   823: iconst_1
      //   824: putfield 116	com/google/cast/c$a:l	Z
      //   827: goto -112 -> 715
      //   830: astore 55
      //   832: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   835: astore 56
      //   837: iconst_0
      //   838: anewarray 127	java/lang/Object
      //   841: astore 57
      //   843: aload 56
      //   845: aload 55
      //   847: ldc_w 371
      //   850: aload 57
      //   852: invokevirtual 374	com/google/cast/Logger:d	(Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
      //   855: aload_0
      //   856: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   859: invokevirtual 159	com/google/cast/c:getContext	()Landroid/content/Context;
      //   862: astore 58
      //   864: getstatic 377	com/google/cast/R$string:error_ramp_command_failed	I
      //   867: istore 59
      //   869: aload 58
      //   871: iload 59
      //   873: invokevirtual 171	android/content/Context:getString	(I)Ljava/lang/String;
      //   876: astore 60
      //   878: aload_0
      //   879: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   882: iconst_4
      //   883: invokestatic 174	com/google/cast/c:a	(Lcom/google/cast/c;I)Landroid/os/Bundle;
      //   886: astore 61
      //   888: aload 9
      //   890: aload 60
      //   892: aload 61
      //   894: invokevirtual 180	android/support/v7/media/MediaRouter$ControlRequestCallback:onError	(Ljava/lang/String;Landroid/os/Bundle;)V
      //   897: iconst_0
      //   898: istore 6
      //   900: goto -849 -> 51
      //   903: aload 17
      //   905: ldc_w 379
      //   908: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   911: ifeq +109 -> 1020
      //   914: aload_0
      //   915: invokespecial 197	com/google/cast/c$a:d	()V
      //   918: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   921: astore 62
      //   923: iconst_1
      //   924: anewarray 127	java/lang/Object
      //   927: astore 63
      //   929: aload_0
      //   930: getfield 53	com/google/cast/c$a:k	D
      //   933: invokestatic 385	java/lang/Double:valueOf	(D)Ljava/lang/Double;
      //   936: astore 64
      //   938: aload 63
      //   940: iconst_0
      //   941: aload 64
      //   943: aastore
      //   944: aload 62
      //   946: ldc_w 387
      //   949: aload 63
      //   951: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   954: aload_0
      //   955: getfield 53	com/google/cast/c$a:k	D
      //   958: ldc2_w 388
      //   961: dcmpl
      //   962: iflt +38 -> 1000
      //   965: aload_0
      //   966: getfield 53	com/google/cast/c$a:k	D
      //   969: dstore 65
      //   971: aload_0
      //   972: ldc2_w 50
      //   975: putfield 53	com/google/cast/c$a:k	D
      //   978: aload_0
      //   979: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   982: dload 65
      //   984: invokevirtual 393	com/google/cast/MediaProtocolMessageStream:playFrom	(D)Lcom/google/cast/MediaProtocolCommand;
      //   987: astore 67
      //   989: aload_0
      //   990: aload 67
      //   992: aload 9
      //   994: invokespecial 315	com/google/cast/c$a:a	(Lcom/google/cast/MediaProtocolCommand;Landroid/support/v7/media/MediaRouter$ControlRequestCallback;)V
      //   997: goto -282 -> 715
      //   1000: aload_0
      //   1001: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1004: invokevirtual 397	com/google/cast/MediaProtocolMessageStream:resume	()Lcom/google/cast/MediaProtocolCommand;
      //   1007: astore 68
      //   1009: aload_0
      //   1010: aload 68
      //   1012: aload 9
      //   1014: invokespecial 315	com/google/cast/c$a:a	(Lcom/google/cast/MediaProtocolCommand;Landroid/support/v7/media/MediaRouter$ControlRequestCallback;)V
      //   1017: goto -302 -> 715
      //   1020: aload 17
      //   1022: ldc_w 399
      //   1025: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   1028: ifeq +13 -> 1041
      //   1031: aload_0
      //   1032: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1035: invokevirtual 369	com/google/cast/MediaProtocolMessageStream:stop	()V
      //   1038: goto -323 -> 715
      //   1041: aload 17
      //   1043: ldc_w 401
      //   1046: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   1049: ifeq +176 -> 1225
      //   1052: aload_0
      //   1053: invokespecial 197	com/google/cast/c$a:d	()V
      //   1056: aload 16
      //   1058: ldc_w 318
      //   1061: invokevirtual 404	android/content/Intent:getStringExtra	(Ljava/lang/String;)Ljava/lang/String;
      //   1064: astore 69
      //   1066: aload_0
      //   1067: aload 69
      //   1069: invokespecial 406	com/google/cast/c$a:a	(Ljava/lang/String;)V
      //   1072: aload 16
      //   1074: ldc_w 279
      //   1077: ldc2_w 71
      //   1080: invokevirtual 283	android/content/Intent:getLongExtra	(Ljava/lang/String;J)J
      //   1083: lstore 18
      //   1085: aload_0
      //   1086: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1089: invokevirtual 410	com/google/cast/MediaProtocolMessageStream:getPlayerState	()Lcom/google/cast/MediaProtocolMessageStream$PlayerState;
      //   1092: astore 70
      //   1094: getstatic 416	com/google/cast/MediaProtocolMessageStream$PlayerState:PLAYING	Lcom/google/cast/MediaProtocolMessageStream$PlayerState;
      //   1097: astore 71
      //   1099: aload 70
      //   1101: aload 71
      //   1103: if_acmpeq +54 -> 1157
      //   1106: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   1109: astore 72
      //   1111: iconst_1
      //   1112: anewarray 127	java/lang/Object
      //   1115: astore 73
      //   1117: lload 18
      //   1119: invokestatic 421	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   1122: astore 74
      //   1124: aload 73
      //   1126: iconst_0
      //   1127: aload 74
      //   1129: aastore
      //   1130: aload 72
      //   1132: ldc_w 423
      //   1135: aload 73
      //   1137: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   1140: aload_0
      //   1141: lload 18
      //   1143: invokespecial 285	com/google/cast/c$a:a	(J)D
      //   1146: dstore 75
      //   1148: aload_0
      //   1149: dload 75
      //   1151: putfield 53	com/google/cast/c$a:k	D
      //   1154: goto -439 -> 715
      //   1157: invokestatic 125	com/google/cast/c:b	()Lcom/google/cast/Logger;
      //   1160: astore 77
      //   1162: iconst_1
      //   1163: anewarray 127	java/lang/Object
      //   1166: astore 78
      //   1168: lload 18
      //   1170: invokestatic 421	java/lang/Long:valueOf	(J)Ljava/lang/Long;
      //   1173: astore 79
      //   1175: aload 78
      //   1177: iconst_0
      //   1178: aload 79
      //   1180: aastore
      //   1181: aload 77
      //   1183: ldc_w 425
      //   1186: aload 78
      //   1188: invokevirtual 136	com/google/cast/Logger:d	(Ljava/lang/String;[Ljava/lang/Object;)V
      //   1191: aload_0
      //   1192: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1195: astore 80
      //   1197: aload_0
      //   1198: lload 18
      //   1200: invokespecial 285	com/google/cast/c$a:a	(J)D
      //   1203: dstore 81
      //   1205: aload 80
      //   1207: dload 81
      //   1209: invokevirtual 393	com/google/cast/MediaProtocolMessageStream:playFrom	(D)Lcom/google/cast/MediaProtocolCommand;
      //   1212: astore 83
      //   1214: aload_0
      //   1215: aload 83
      //   1217: aload 9
      //   1219: invokespecial 315	com/google/cast/c$a:a	(Lcom/google/cast/MediaProtocolCommand;Landroid/support/v7/media/MediaRouter$ControlRequestCallback;)V
      //   1222: goto -507 -> 715
      //   1225: aload 17
      //   1227: ldc_w 427
      //   1230: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   1233: ifeq +106 -> 1339
      //   1236: aload 16
      //   1238: ldc_w 318
      //   1241: invokevirtual 404	android/content/Intent:getStringExtra	(Ljava/lang/String;)Ljava/lang/String;
      //   1244: astore 84
      //   1246: aload_0
      //   1247: aload 84
      //   1249: invokespecial 406	com/google/cast/c$a:a	(Ljava/lang/String;)V
      //   1252: aload_0
      //   1253: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1256: ifnull +38 -> 1294
      //   1259: new 235	android/os/Bundle
      //   1262: dup
      //   1263: invokespecial 316	android/os/Bundle:<init>	()V
      //   1266: astore 85
      //   1268: aload_0
      //   1269: invokespecial 429	com/google/cast/c$a:g	()Landroid/os/Bundle;
      //   1272: astore 86
      //   1274: aload 85
      //   1276: ldc_w 348
      //   1279: aload 86
      //   1281: invokevirtual 433	android/os/Bundle:putParcelable	(Ljava/lang/String;Landroid/os/Parcelable;)V
      //   1284: aload 9
      //   1286: aload 85
      //   1288: invokevirtual 355	android/support/v7/media/MediaRouter$ControlRequestCallback:onResult	(Landroid/os/Bundle;)V
      //   1291: goto -576 -> 715
      //   1294: aload_0
      //   1295: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   1298: invokevirtual 159	com/google/cast/c:getContext	()Landroid/content/Context;
      //   1301: astore 87
      //   1303: getstatic 165	com/google/cast/R$string:error_no_session	I
      //   1306: istore 88
      //   1308: aload 87
      //   1310: iload 88
      //   1312: invokevirtual 171	android/content/Context:getString	(I)Ljava/lang/String;
      //   1315: astore 89
      //   1317: aload_0
      //   1318: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   1321: iconst_3
      //   1322: invokestatic 174	com/google/cast/c:a	(Lcom/google/cast/c;I)Landroid/os/Bundle;
      //   1325: astore 90
      //   1327: aload 9
      //   1329: aload 89
      //   1331: aload 90
      //   1333: invokevirtual 180	android/support/v7/media/MediaRouter$ControlRequestCallback:onError	(Ljava/lang/String;Landroid/os/Bundle;)V
      //   1336: goto -621 -> 715
      //   1339: aload 17
      //   1341: ldc_w 435
      //   1344: invokevirtual 109	java/lang/String:equals	(Ljava/lang/Object;)Z
      //   1347: ifeq +111 -> 1458
      //   1350: aload 16
      //   1352: ldc_w 287
      //   1355: invokevirtual 291	android/content/Intent:getParcelableExtra	(Ljava/lang/String;)Landroid/os/Parcelable;
      //   1358: checkcast 293	android/app/PendingIntent
      //   1361: astore 16
      //   1363: aload 16
      //   1365: ifnonnull +9 -> 1374
      //   1368: iconst_0
      //   1369: istore 6
      //   1371: goto -1320 -> 51
      //   1374: aload_0
      //   1375: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1378: ifnull +35 -> 1413
      //   1381: aload_0
      //   1382: getfield 153	com/google/cast/c$a:f	Lcom/google/cast/MediaProtocolMessageStream;
      //   1385: invokevirtual 438	com/google/cast/MediaProtocolMessageStream:requestStatus	()Lcom/google/cast/MediaProtocolCommand;
      //   1388: astore 91
      //   1390: aload 91
      //   1392: ldc_w 440
      //   1395: aload 16
      //   1397: invokevirtual 93	com/google/cast/MediaProtocolCommand:putUserObject	(Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;
      //   1400: astore 92
      //   1402: aload_0
      //   1403: aload 91
      //   1405: aload 9
      //   1407: invokespecial 315	com/google/cast/c$a:a	(Lcom/google/cast/MediaProtocolCommand;Landroid/support/v7/media/MediaRouter$ControlRequestCallback;)V
      //   1410: goto -695 -> 715
      //   1413: aload_0
      //   1414: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   1417: invokevirtual 159	com/google/cast/c:getContext	()Landroid/content/Context;
      //   1420: astore 93
      //   1422: getstatic 165	com/google/cast/R$string:error_no_session	I
      //   1425: istore 94
      //   1427: aload 93
      //   1429: iload 94
      //   1431: invokevirtual 171	android/content/Context:getString	(I)Ljava/lang/String;
      //   1434: astore 95
      //   1436: aload_0
      //   1437: getfield 46	com/google/cast/c$a:d	Lcom/google/cast/c;
      //   1440: iconst_3
      //   1441: invokestatic 174	com/google/cast/c:a	(Lcom/google/cast/c;I)Landroid/os/Bundle;
      //   1444: astore 96
      //   1446: aload 9
      //   1448: aload 95
      //   1450: aload 96
      //   1452: invokevirtual 180	android/support/v7/media/MediaRouter$ControlRequestCallback:onError	(Ljava/lang/String;Landroid/os/Bundle;)V
      //   1455: goto -740 -> 715
      //   1458: iconst_0
      //   1459: istore 6
      //   1461: goto -1410 -> 51
      //   1464: astore 97
      //   1466: goto -989 -> 477
      //   1469: astore 98
      //   1471: goto -1036 -> 435
      //
      // Exception table:
      //   from	to	target	type
      //   189	414	745	java/lang/IllegalStateException
      //   423	435	745	java/lang/IllegalStateException
      //   435	456	745	java/lang/IllegalStateException
      //   465	477	745	java/lang/IllegalStateException
      //   477	742	745	java/lang/IllegalStateException
      //   792	827	745	java/lang/IllegalStateException
      //   903	1455	745	java/lang/IllegalStateException
      //   189	414	830	java/io/IOException
      //   423	435	830	java/io/IOException
      //   435	456	830	java/io/IOException
      //   465	477	830	java/io/IOException
      //   477	742	830	java/io/IOException
      //   792	827	830	java/io/IOException
      //   903	1455	830	java/io/IOException
      //   465	477	1464	org/json/JSONException
      //   423	435	1469	org/json/JSONException
    }

    private long b(double paramDouble)
    {
      return ()(1000.0D * paramDouble);
    }

    private void c()
    {
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[1];
      ApplicationSession localApplicationSession1 = this.e;
      arrayOfObject1[0] = localApplicationSession1;
      localLogger1.d("startSession(); mSession=%s", arrayOfObject1);
      if ((this.e == null) || (!this.e.isResumable()))
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[0];
        localLogger2.d("starting a new session", arrayOfObject2);
        CastContext localCastContext = c.this.a;
        CastDevice localCastDevice = this.a;
        ApplicationSession localApplicationSession2 = new ApplicationSession(localCastContext, localCastDevice);
        this.e = localApplicationSession2;
        this.e.setListener(this);
        ApplicationSession localApplicationSession3 = this.e;
        int i1 = c.b(c.this);
        localApplicationSession3.setApplicationOptions(i1);
        try
        {
          ApplicationSession localApplicationSession4 = this.e;
          String str = c.c(c.this);
          MimeData localMimeData = c.d(c.this);
          localApplicationSession4.startSession(str, localMimeData);
          a(1);
          return;
        }
        catch (IOException localIOException1)
        {
          this.e = null;
          boolean bool = f();
          return;
        }
      }
      Logger localLogger3 = c.b();
      Object[] arrayOfObject3 = new Object[0];
      localLogger3.d("resuming an existing session", arrayOfObject3);
      try
      {
        this.e.resumeSession();
        a(1);
        return;
      }
      catch (IOException localIOException2)
      {
        if (f())
          return;
        e();
        this.e = null;
      }
    }

    private void d()
      throws IllegalStateException
    {
      if (this.g.size() < 50)
        return;
      throw new IllegalStateException("command backlog is full");
    }

    private void e()
    {
      if (this.j == null)
        return;
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[1];
      String str1 = this.j;
      arrayOfObject1[0] = str1;
      localLogger1.d("invalidating item %s", arrayOfObject1);
      Map localMap1 = this.h;
      String str2 = this.j;
      PendingIntent localPendingIntent = (PendingIntent)localMap1.get(str2);
      Intent localIntent1;
      if (localPendingIntent != null)
      {
        localIntent1 = new Intent();
        String str3 = this.j;
        Intent localIntent2 = localIntent1.putExtra("android.media.intent.extra.ITEM_ID", str3);
        MediaItemStatus.Builder localBuilder = new MediaItemStatus.Builder(6);
        long l1 = SystemClock.uptimeMillis();
        Bundle localBundle = localBuilder.setTimestamp(l1).build().asBundle();
        Intent localIntent3 = localIntent1.putExtra("android.media.intent.extra.ITEM_STATUS", localBundle);
      }
      try
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = localIntent1;
        localLogger2.d("Invoking the PendingIntent with: %s", arrayOfObject2);
        Context localContext = c.this.getContext();
        localPendingIntent.send(localContext, 0, localIntent1);
        Map localMap2 = this.h;
        String str4 = this.j;
        Object localObject = localMap2.remove(str4);
        this.j = null;
        return;
      }
      catch (PendingIntent.CanceledException localCanceledException)
      {
        while (true)
        {
          Logger localLogger3 = c.b();
          Object[] arrayOfObject3 = new Object[0];
          localLogger3.d(localCanceledException, "exception while sending PendingIntent", arrayOfObject3);
        }
      }
    }

    private boolean f()
    {
      boolean bool1 = true;
      long l1 = SystemClock.uptimeMillis();
      long l2 = this.o;
      if (l1 - l2 < 10000L)
      {
        Logger localLogger1 = c.b();
        Object[] arrayOfObject1 = new Object[0];
        localLogger1.d("Scheduling a reconnect attempt", arrayOfObject1);
        a(5);
        Handler localHandler = c.this.getHandler();
        Runnable localRunnable = this.n;
        boolean bool2 = localHandler.postDelayed(localRunnable, 3000L);
        this.p = true;
      }
      while (true)
      {
        return bool1;
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[0];
        localLogger2.d("Giving up on reconnecting route's session.", arrayOfObject2);
        a(4);
        this.p = false;
        bool1 = false;
      }
    }

    private Bundle g()
    {
      int i1 = 2;
      MediaProtocolMessageStream.PlayerState localPlayerState = this.f.getPlayerState();
      Bundle localBundle = new Bundle();
      if (this.l);
      while (true)
      {
        double d1 = this.f.getStreamDuration();
        long l1 = b(d1);
        double d2 = this.f.getStreamPosition();
        long l2 = b(d2);
        MediaItemStatus.Builder localBuilder = new MediaItemStatus.Builder(i1).setContentDuration(l1).setContentPosition(l2).setExtras(localBundle);
        long l3 = SystemClock.uptimeMillis();
        return localBuilder.setTimestamp(l3).build().asBundle();
        int[] arrayOfInt = c.1.a;
        int i2 = localPlayerState.ordinal();
        switch (arrayOfInt[i2])
        {
        case 2:
        default:
          i1 = 0;
          break;
        case 1:
          Logger localLogger = c.b();
          Object[] arrayOfObject = new Object[0];
          localLogger.d("buildCurrentItemStatusBundle; player is IDLE, so FINISHED", arrayOfObject);
          i1 = 4;
          break;
        case 3:
          if (this.f.isStreamProgressing())
            i1 = 1;
          else
            i1 = 3;
          break;
        }
      }
    }

    private Bundle h()
    {
      Bundle localBundle = new Bundle();
      if (this.f != null)
      {
        String str1 = this.f.getTitle();
        if (str1 != null)
          localBundle.putString("android.media.metadata.TITLE", str1);
        Uri localUri = this.f.getImageUrl();
        if (localUri != null)
        {
          String str2 = localUri.toString();
          localBundle.putString("android.media.metadata.ARTWORK_URI", str2);
        }
        Object localObject = this.f.getContentInfo();
        if (localObject != null)
        {
          localObject = ((JSONObject)localObject).optString("artist");
          if (localObject != null)
            localBundle.putString("android.media.metadata.ARTIST", (String)localObject);
        }
      }
      return localBundle;
    }

    public void onCancelled(MediaProtocolCommand paramMediaProtocolCommand)
    {
      boolean bool = this.g.remove(paramMediaProtocolCommand);
    }

    public void onCompleted(MediaProtocolCommand paramMediaProtocolCommand)
    {
      boolean bool = this.g.remove(paramMediaProtocolCommand);
      Object localObject1 = paramMediaProtocolCommand.getType();
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = localObject1;
      localLogger1.d("onCompleted; RAMP cmd is %s", arrayOfObject1);
      Object localObject2 = (MediaRouter.ControlRequestCallback)paramMediaProtocolCommand.getUserObject("cb");
      if ("LOAD".equals(localObject1))
        this.m = false;
      if (!paramMediaProtocolCommand.hasError())
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = localObject1;
        localLogger2.d("RAMP command %s completed successfully", arrayOfObject2);
        MediaProtocolMessageStream.PlayerState localPlayerState1 = this.f.getPlayerState();
        MediaProtocolMessageStream.PlayerState localPlayerState2 = MediaProtocolMessageStream.PlayerState.PLAYING;
        if (localPlayerState1 == localPlayerState2)
          this.l = false;
        if ("LOAD".equals(localObject1))
        {
          String str1 = (String)paramMediaProtocolCommand.getUserObject("itemid");
          this.j = str1;
          Logger localLogger3 = c.b();
          Object[] arrayOfObject3 = new Object[1];
          String str2 = this.j;
          arrayOfObject3[0] = str2;
          localLogger3.d("mCurrentItemId is now %s", arrayOfObject3);
        }
        while (true)
        {
          if (this.k < 0.0D)
            return;
          double d1 = this.k;
          this.k = -1.0D;
          try
          {
            d();
            MediaProtocolCommand localMediaProtocolCommand = this.f.playFrom(d1);
            a(localMediaProtocolCommand, (MediaRouter.ControlRequestCallback)localObject2);
            return;
            if ("INFO".equals(localObject1))
            {
              PendingIntent localPendingIntent = (PendingIntent)paramMediaProtocolCommand.getUserObject("sync");
              if (localPendingIntent != null)
              {
                Logger localLogger4 = c.b();
                Object[] arrayOfObject4 = new Object[0];
                localLogger4.d("GET_STATUS completed for a sync-up request", arrayOfObject4);
                Bundle localBundle1 = new Bundle();
                if (this.f.getContentId() != null)
                {
                  String str3 = UUID.randomUUID().toString();
                  this.j = str3;
                  String str4 = this.j;
                  localBundle1.putString("android.media.intent.extra.ITEM_ID", str4);
                  Map localMap = this.h;
                  String str5 = this.j;
                  Object localObject3 = localMap.put(str5, localPendingIntent);
                  Bundle localBundle2 = g();
                  localBundle1.putBundle("android.media.intent.extra.ITEM_STATUS", localBundle2);
                  Bundle localBundle3 = h();
                  localBundle1.putBundle("android.media.intent.extra.ITEM_METADATA", localBundle3);
                }
                ((MediaRouter.ControlRequestCallback)localObject2).onResult(localBundle1);
              }
            }
          }
          catch (IllegalStateException localIllegalStateException)
          {
            Logger localLogger5 = c.b();
            Object[] arrayOfObject5 = new Object[0];
            localLogger5.d("Cannot enqueue a pending seek; backlog full", arrayOfObject5);
            return;
          }
          catch (IOException localIOException)
          {
            Logger localLogger6 = c.b();
            Object[] arrayOfObject6 = new Object[0];
            localLogger6.d(localIOException, "Cannot enqueue a pending seek", arrayOfObject6);
            return;
          }
        }
      }
      Logger localLogger7 = c.b();
      Object[] arrayOfObject7 = new Object[1];
      arrayOfObject7[0] = localObject1;
      localLogger7.d("RAMP command %s failed", arrayOfObject7);
      if (localObject2 != null)
      {
        Context localContext1 = c.this.getContext();
        int i1 = R.string.error_ramp_command_failed;
        String str6 = localContext1.getString(i1);
        Bundle localBundle4 = c.a(c.this, 4);
        ((MediaRouter.ControlRequestCallback)localObject2).onError(str6, localBundle4);
      }
      if (!"LOAD".equals(localObject1))
        return;
      if (!paramMediaProtocolCommand.hasError())
        return;
      String str7 = paramMediaProtocolCommand.getErrorDomain();
      if (!"ramp".equals(str7))
        return;
      localObject2 = (String)paramMediaProtocolCommand.getUserObject("itemid");
      localObject1 = (PendingIntent)this.h.get(localObject2);
      if (localObject1 == null)
        return;
      MediaItemStatus.Builder localBuilder1 = new MediaItemStatus.Builder(7);
      long l1 = SystemClock.uptimeMillis();
      MediaItemStatus.Builder localBuilder2 = localBuilder1.setTimestamp(l1);
      JSONObject localJSONObject = paramMediaProtocolCommand.getErrorInfo();
      if (localJSONObject != null)
      {
        Bundle localBundle5 = c.a(c.this, localJSONObject);
        MediaItemStatus.Builder localBuilder3 = localBuilder2.setExtras(localBundle5);
      }
      Intent localIntent1 = new Intent();
      Intent localIntent2 = localIntent1.putExtra("android.media.intent.extra.ITEM_ID", (String)localObject2);
      Bundle localBundle6 = localBuilder2.build().asBundle();
      Intent localIntent3 = localIntent1.putExtra("android.media.intent.extra.ITEM_STATUS", localBundle6);
      try
      {
        Context localContext2 = c.this.getContext();
        ((PendingIntent)localObject1).send(localContext2, 0, localIntent1);
        return;
      }
      catch (PendingIntent.CanceledException localCanceledException)
      {
        Logger localLogger8 = c.b();
        Object[] arrayOfObject8 = new Object[0];
        localLogger8.d(localCanceledException, "exception while sending PendingIntent", arrayOfObject8);
      }
    }

    public boolean onControlRequest(Intent paramIntent, MediaRouter.ControlRequestCallback paramControlRequestCallback)
    {
      boolean bool = false;
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[bool] = paramIntent;
      localLogger1.d("Received control request %s", arrayOfObject1);
      if (this.p)
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[0];
        localLogger2.d("Got a request while reconnecting, so rejecting it", arrayOfObject2);
        Context localContext = c.this.getContext();
        int i1 = R.string.error_no_session;
        String str = localContext.getString(i1);
        Bundle localBundle = c.a(c.this, 3);
        paramControlRequestCallback.onError(str, localBundle);
      }
      while (true)
      {
        return bool;
        if ((paramIntent.hasCategory("android.media.intent.category.REMOTE_PLAYBACK")) || (paramIntent.hasCategory("com.google.cast.CATEGORY_REMOTE_PLAYBACK_CAST_EXTENSIONS")))
        {
          c localc = c.this;
          c.b localb = new c.b(localc, paramIntent, paramControlRequestCallback);
          bool = a(localb);
        }
      }
    }

    public void onSelect()
    {
      Logger localLogger = c.b();
      Object[] arrayOfObject = new Object[0];
      localLogger.d("Selected device", arrayOfObject);
      c();
    }

    public void onSessionEnded(SessionError paramSessionError)
    {
      Logger localLogger = c.b();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramSessionError;
      localLogger.d("onSessionEnded; error=%s", arrayOfObject);
      if (this.i != null)
      {
        MediaRouter.ControlRequestCallback localControlRequestCallback = this.i.b;
        Context localContext = c.this.getContext();
        int i1 = R.string.error_session_ended;
        String str = localContext.getString(i1);
        Bundle localBundle = c.a(c.this, 1);
        localControlRequestCallback.onError(str, localBundle);
        this.i = null;
      }
      if (paramSessionError != null)
      {
        long l1 = SystemClock.uptimeMillis();
        this.o = l1;
        if (!f())
        {
          e();
          this.h.clear();
          this.e = null;
        }
      }
      while (true)
      {
        this.g.clear();
        this.f = null;
        this.k = -1.0D;
        this.l = false;
        this.m = false;
        return;
        this.e = null;
        this.j = null;
        this.h.clear();
        a(4);
      }
    }

    public void onSessionStartFailed(SessionError paramSessionError)
    {
      Logger localLogger = c.b();
      Object[] arrayOfObject = new Object[0];
      localLogger.d("onSessionStartFailed", arrayOfObject);
      if ((this.e != null) && (!this.e.isResumable()))
        this.e = null;
      this.f = null;
      if (this.i != null)
      {
        MediaRouter.ControlRequestCallback localControlRequestCallback = this.i.b;
        Context localContext = c.this.getContext();
        int i1 = R.string.error_start_session_failed;
        String str = localContext.getString(i1);
        Bundle localBundle = c.a(c.this, 2);
        localControlRequestCallback.onError(str, localBundle);
        this.i = null;
      }
      if (this.o == 0L)
      {
        long l1 = SystemClock.uptimeMillis();
        this.o = l1;
      }
      if (f())
        return;
      e();
      this.e = null;
    }

    public void onSessionStarted(ApplicationMetadata paramApplicationMetadata)
    {
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[0];
      localLogger1.d("onSessionStarted", arrayOfObject1);
      this.o = 0L;
      this.p = false;
      MediaProtocolMessageStream local2 = new MediaProtocolMessageStream()
      {
        protected void onError(String paramAnonymousString, long paramAnonymousLong, JSONObject paramAnonymousJSONObject)
        {
          PendingIntent localPendingIntent = null;
          if (c.a.c(c.a.this) != null)
          {
            Map localMap1 = c.a.d(c.a.this);
            String str1 = c.a.c(c.a.this);
            localPendingIntent = (PendingIntent)localMap1.get(str1);
          }
          if (localPendingIntent == null)
            return;
          MediaItemStatus.Builder localBuilder1 = new MediaItemStatus.Builder(7);
          long l = SystemClock.uptimeMillis();
          MediaItemStatus.Builder localBuilder2 = localBuilder1.setTimestamp(l);
          if (paramAnonymousJSONObject != null)
          {
            Bundle localBundle1 = c.a(c.this, paramAnonymousJSONObject);
            MediaItemStatus.Builder localBuilder3 = localBuilder2.setExtras(localBundle1);
          }
          Intent localIntent1 = new Intent();
          String str2 = c.a.c(c.a.this);
          Intent localIntent2 = localIntent1.putExtra("android.media.intent.extra.ITEM_ID", str2);
          Bundle localBundle2 = localBuilder2.build().asBundle();
          Intent localIntent3 = localIntent1.putExtra("android.media.intent.extra.ITEM_STATUS", localBundle2);
          try
          {
            Context localContext = c.this.getContext();
            localPendingIntent.send(localContext, 0, localIntent1);
            Map localMap2 = c.a.d(c.a.this);
            String str3 = c.a.c(c.a.this);
            Object localObject = localMap2.remove(str3);
            return;
          }
          catch (PendingIntent.CanceledException localCanceledException)
          {
            while (true)
            {
              Logger localLogger = c.b();
              Object[] arrayOfObject = new Object[0];
              localLogger.d(localCanceledException, "exception while sending PendingIntent", arrayOfObject);
            }
          }
        }

        protected void onStatusUpdated()
        {
          Logger localLogger1 = c.b();
          Object[] arrayOfObject1 = new Object[3];
          Boolean localBoolean = Boolean.valueOf(c.a.b(c.a.this));
          arrayOfObject1[0] = localBoolean;
          String str1 = c.a.c(c.a.this);
          arrayOfObject1[1] = str1;
          MediaProtocolMessageStream.PlayerState localPlayerState1 = getPlayerState();
          arrayOfObject1[2] = localPlayerState1;
          localLogger1.d("onStatusUpdated; mLoadInProgress=%b, mCurrentItemId=%s, player state: %s", arrayOfObject1);
          if (c.a.b(c.a.this))
            return;
          MediaProtocolMessageStream.PlayerState localPlayerState2 = getPlayerState();
          MediaProtocolMessageStream.PlayerState localPlayerState3 = MediaProtocolMessageStream.PlayerState.PLAYING;
          if (localPlayerState2 == localPlayerState3)
            boolean bool = c.a.a(c.a.this, false);
          c.a locala = c.a.this;
          double d = getVolume();
          locala.a(d);
          PendingIntent localPendingIntent;
          if (c.a.c(c.a.this) != null)
          {
            Map localMap1 = c.a.d(c.a.this);
            String str2 = c.a.c(c.a.this);
            localPendingIntent = (PendingIntent)localMap1.get(str2);
            Logger localLogger2 = c.b();
            Object[] arrayOfObject2 = new Object[2];
            String str3 = c.a.c(c.a.this);
            arrayOfObject2[0] = str3;
            arrayOfObject2[1] = localPendingIntent;
            localLogger2.d("found a PendingIntent for item %s: %s", arrayOfObject2);
          }
          while (true)
          {
            Intent localIntent1;
            if (localPendingIntent != null)
            {
              localIntent1 = new Intent();
              String str4 = c.a.c(c.a.this);
              Intent localIntent2 = localIntent1.putExtra("android.media.intent.extra.ITEM_ID", str4);
              Bundle localBundle1 = c.a.e(c.a.this);
              Intent localIntent3 = localIntent1.putExtra("android.media.intent.extra.ITEM_STATUS", localBundle1);
              Bundle localBundle2 = c.a.f(c.a.this);
              Intent localIntent4 = localIntent1.putExtra("android.media.intent.extra.ITEM_METADATA", localBundle2);
            }
            try
            {
              Logger localLogger3 = c.b();
              Object[] arrayOfObject3 = new Object[1];
              arrayOfObject3[0] = localIntent1;
              localLogger3.d("Invoking the PendingIntent with: %s", arrayOfObject3);
              Context localContext = c.this.getContext();
              localPendingIntent.send(localContext, 0, localIntent1);
              MediaProtocolMessageStream.PlayerState localPlayerState4 = c.a.g(c.a.this).getPlayerState();
              MediaProtocolMessageStream.PlayerState localPlayerState5 = MediaProtocolMessageStream.PlayerState.IDLE;
              if (localPlayerState4 != localPlayerState5)
                return;
              Map localMap2 = c.a.d(c.a.this);
              String str5 = c.a.c(c.a.this);
              Object localObject = localMap2.remove(str5);
              Logger localLogger4 = c.b();
              Object[] arrayOfObject4 = new Object[0];
              localLogger4.d("player state is now IDLE; clearing mCurrentItemId", arrayOfObject4);
              String str6 = c.a.a(c.a.this, null);
              return;
            }
            catch (PendingIntent.CanceledException localCanceledException)
            {
              while (true)
              {
                Logger localLogger5 = c.b();
                Object[] arrayOfObject5 = new Object[0];
                localLogger5.d(localCanceledException, "exception while sending PendingIntent", arrayOfObject5);
              }
            }
            localPendingIntent = null;
          }
        }
      };
      this.f = local2;
      ApplicationChannel localApplicationChannel = this.e.getChannel();
      if (localApplicationChannel != null)
      {
        MediaProtocolMessageStream localMediaProtocolMessageStream = this.f;
        localApplicationChannel.attachMessageStream(localMediaProtocolMessageStream);
        if (this.i == null)
          break label128;
        c.b localb = this.i;
        boolean bool = a(localb);
        this.i = null;
      }
      while (true)
      {
        a(2);
        return;
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[0];
        localLogger2.e("No channel in session!", arrayOfObject2);
        break;
        label128: Logger localLogger3 = c.b();
        Object[] arrayOfObject3 = new Object[0];
        localLogger3.d("requesting RAMP status", arrayOfObject3);
        try
        {
          MediaProtocolCommand localMediaProtocolCommand = this.f.requestStatus();
          a(localMediaProtocolCommand, null);
        }
        catch (IOException localIOException)
        {
          Logger localLogger4 = c.b();
          Object[] arrayOfObject4 = new Object[0];
          localLogger4.e(localIOException, "Error while requesting RAMP status", arrayOfObject4);
        }
      }
    }

    public void onSetVolume(int paramInt)
    {
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[2];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject1[0] = localInteger;
      MediaProtocolMessageStream localMediaProtocolMessageStream = this.f;
      arrayOfObject1[1] = localMediaProtocolMessageStream;
      localLogger1.d("onSetVolume() volume=%d, mRampStream=%s", arrayOfObject1);
      if (this.f == null)
        return;
      double d1 = paramInt / 20.0D;
      try
      {
        d();
        MediaProtocolCommand localMediaProtocolCommand = this.f.setVolume(d1);
        a(localMediaProtocolCommand, null);
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[1];
        String str1 = localIllegalStateException.getMessage();
        arrayOfObject2[0] = str1;
        localLogger2.d("can't process command; %s", arrayOfObject2);
        return;
      }
      catch (IOException localIOException)
      {
        Logger localLogger3 = c.b();
        Object[] arrayOfObject3 = new Object[1];
        String str2 = localIOException.getMessage();
        arrayOfObject3[0] = str2;
        localLogger3.d("can't process command; %s", arrayOfObject3);
      }
    }

    public void onUnselect()
    {
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[0];
      localLogger1.d("Unselected device", arrayOfObject1);
      Handler localHandler = c.this.getHandler();
      Runnable localRunnable = this.n;
      localHandler.removeCallbacks(localRunnable);
      if (this.e == null)
        return;
      try
      {
        ApplicationSession localApplicationSession = this.e;
        boolean bool = c.a(c.this);
        localApplicationSession.setStopApplicationWhenEnding(bool);
        if (!this.e.endSession())
          return;
        a(3);
        return;
      }
      catch (IOException localIOException)
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[0];
        localLogger2.e(localIOException, "Exception while ending session", arrayOfObject2);
        this.e = null;
      }
    }

    public void onUpdateVolume(int paramInt)
    {
      Logger localLogger1 = c.b();
      Object[] arrayOfObject1 = new Object[2];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject1[0] = localInteger;
      MediaProtocolMessageStream localMediaProtocolMessageStream = this.f;
      arrayOfObject1[1] = localMediaProtocolMessageStream;
      localLogger1.d("onUpdateVolume() delta=%d, mRampStream=%s", arrayOfObject1);
      if (this.f == null)
        return;
      try
      {
        d();
        double d1 = (a() + paramInt) / 20.0D;
        MediaProtocolCommand localMediaProtocolCommand = this.f.setVolume(d1);
        a(localMediaProtocolCommand, null);
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        Logger localLogger2 = c.b();
        Object[] arrayOfObject2 = new Object[1];
        String str1 = localIllegalStateException.getMessage();
        arrayOfObject2[0] = str1;
        localLogger2.d("can't process command; %s", arrayOfObject2);
        return;
      }
      catch (IOException localIOException)
      {
        Logger localLogger3 = c.b();
        Object[] arrayOfObject3 = new Object[1];
        String str2 = localIOException.getMessage();
        arrayOfObject3[0] = str2;
        localLogger3.d("can't process command; %s", arrayOfObject3);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.cast.c
 * JD-Core Version:    0.6.2
 */