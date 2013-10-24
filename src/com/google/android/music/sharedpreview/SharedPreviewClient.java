package com.google.android.music.sharedpreview;

import android.content.Context;
import com.google.android.common.http.GoogleHttpClient;
import com.google.android.music.cloudclient.MusicRequest;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;

public class SharedPreviewClient
{
  private final boolean LOGV;
  private final Context mContext;
  private final GoogleHttpClient mHttpClient;
  private final boolean mLogHttp;
  private final MusicPreferences mMusicPreferences;

  public SharedPreviewClient(Context paramContext)
  {
    boolean bool1 = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);
    this.LOGV = bool1;
    boolean bool2 = DebugUtils.isLoggable(DebugUtils.MusicTag.HTTP);
    this.mLogHttp = bool2;
    this.mContext = paramContext;
    GoogleHttpClient localGoogleHttpClient = MusicRequest.getSharedHttpClient(this.mContext);
    this.mHttpClient = localGoogleHttpClient;
    MusicPreferences localMusicPreferences = MusicPreferences.getMusicPreferences(paramContext, this);
    this.mMusicPreferences = localMusicPreferences;
  }

  public JsonResponse getMetaDataResponse(String paramString)
  {
    return getResponse(paramString, false);
  }

  public PreviewResponse getPreviewResponse(String paramString)
  {
    PreviewResponse localPreviewResponse = null;
    JsonResponse localJsonResponse = getResponse(paramString, true);
    if (localJsonResponse == null)
      Log.w("SharedPreviewClient", "Failed to gret preview response");
    while (true)
    {
      return localPreviewResponse;
      if (!(localJsonResponse instanceof PreviewResponse))
        Log.w("SharedPreviewClient", "Received wrong response");
      else
        localPreviewResponse = (PreviewResponse)localJsonResponse;
    }
  }

  // ERROR //
  public JsonResponse getResponse(String paramString, boolean paramBoolean)
  {
    // Byte code:
    //   0: new 84	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   7: aload_1
    //   8: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11: ldc 91
    //   13: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: invokevirtual 95	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   19: astore_3
    //   20: new 97	org/apache/http/client/methods/HttpGet
    //   23: dup
    //   24: aload_3
    //   25: invokespecial 100	org/apache/http/client/methods/HttpGet:<init>	(Ljava/lang/String;)V
    //   28: astore 4
    //   30: aload_0
    //   31: getfield 40	com/google/android/music/sharedpreview/SharedPreviewClient:mContext	Landroid/content/Context;
    //   34: astore 5
    //   36: aload_0
    //   37: getfield 56	com/google/android/music/sharedpreview/SharedPreviewClient:mMusicPreferences	Lcom/google/android/music/preferences/MusicPreferences;
    //   40: astore 6
    //   42: new 42	com/google/android/music/cloudclient/MusicRequest
    //   45: dup
    //   46: aload 5
    //   48: aload 6
    //   50: invokespecial 103	com/google/android/music/cloudclient/MusicRequest:<init>	(Landroid/content/Context;Lcom/google/android/music/preferences/MusicPreferences;)V
    //   53: astore 7
    //   55: aload_0
    //   56: getfield 48	com/google/android/music/sharedpreview/SharedPreviewClient:mHttpClient	Lcom/google/android/common/http/GoogleHttpClient;
    //   59: astore 8
    //   61: aload 7
    //   63: aload 4
    //   65: aload 8
    //   67: invokevirtual 107	com/google/android/music/cloudclient/MusicRequest:sendRequest	(Lorg/apache/http/client/methods/HttpRequestBase;Lcom/google/android/common/http/GoogleHttpClient;)Lorg/apache/http/HttpResponse;
    //   70: astore 9
    //   72: aload 9
    //   74: invokeinterface 113 1 0
    //   79: invokeinterface 119 1 0
    //   84: astore 10
    //   86: aload 10
    //   88: astore 11
    //   90: iload_2
    //   91: ifeq +61 -> 152
    //   94: aload 11
    //   96: invokestatic 123	com/google/android/music/sharedpreview/PreviewResponse:parseFromJsonInputStream	(Ljava/io/InputStream;)Lcom/google/android/music/sharedpreview/PreviewResponse;
    //   99: astore 12
    //   101: aload_0
    //   102: getfield 33	com/google/android/music/sharedpreview/SharedPreviewClient:LOGV	Z
    //   105: ifeq +32 -> 137
    //   108: new 84	java/lang/StringBuilder
    //   111: dup
    //   112: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   115: ldc 125
    //   117: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: aload 12
    //   122: invokevirtual 128	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   125: invokevirtual 95	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   128: astore 13
    //   130: ldc 66
    //   132: aload 13
    //   134: invokestatic 131	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   137: aload 11
    //   139: invokestatic 137	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   142: aload 4
    //   144: aload 9
    //   146: invokestatic 141	com/google/android/music/cloudclient/MusicRequest:releaseResponse	(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/HttpResponse;)V
    //   149: aload 12
    //   151: areturn
    //   152: aload 11
    //   154: invokestatic 147	com/google/common/io/ByteStreams:toByteArray	(Ljava/io/InputStream;)[B
    //   157: astore 14
    //   159: aload_0
    //   160: getfield 38	com/google/android/music/sharedpreview/SharedPreviewClient:mLogHttp	Z
    //   163: ifeq +52 -> 215
    //   166: getstatic 151	com/google/android/music/utils/DebugUtils:HTTP_TAG	Ljava/lang/String;
    //   169: astore 15
    //   171: new 84	java/lang/StringBuilder
    //   174: dup
    //   175: invokespecial 85	java/lang/StringBuilder:<init>	()V
    //   178: ldc 153
    //   180: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: astore 16
    //   185: new 155	java/lang/String
    //   188: dup
    //   189: aload 14
    //   191: invokespecial 158	java/lang/String:<init>	([B)V
    //   194: astore 17
    //   196: aload 16
    //   198: aload 17
    //   200: invokevirtual 89	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: invokevirtual 95	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   206: astore 18
    //   208: aload 15
    //   210: aload 18
    //   212: invokestatic 131	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   215: aload 11
    //   217: invokestatic 137	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   220: aconst_null
    //   221: astore 11
    //   223: new 160	java/io/ByteArrayInputStream
    //   226: dup
    //   227: aload 14
    //   229: invokespecial 161	java/io/ByteArrayInputStream:<init>	([B)V
    //   232: astore 19
    //   234: aload 19
    //   236: invokestatic 166	com/google/android/music/sharedpreview/SharedAlbumResponse:parseFromJsonInputStream	(Ljava/io/InputStream;)Lcom/google/android/music/sharedpreview/SharedAlbumResponse;
    //   239: astore 12
    //   241: aload 12
    //   243: checkcast 163	com/google/android/music/sharedpreview/SharedAlbumResponse
    //   246: getfield 169	com/google/android/music/sharedpreview/SharedAlbumResponse:mAlbumArtist	Ljava/lang/String;
    //   249: ifnonnull +111 -> 360
    //   252: aload_0
    //   253: getfield 33	com/google/android/music/sharedpreview/SharedPreviewClient:LOGV	Z
    //   256: ifeq +10 -> 266
    //   259: ldc 66
    //   261: ldc 171
    //   263: invokestatic 131	com/google/android/music/log/Log:d	(Ljava/lang/String;Ljava/lang/String;)V
    //   266: aload 19
    //   268: invokestatic 137	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   271: aconst_null
    //   272: astore 11
    //   274: new 160	java/io/ByteArrayInputStream
    //   277: dup
    //   278: aload 14
    //   280: invokespecial 161	java/io/ByteArrayInputStream:<init>	([B)V
    //   283: astore 19
    //   285: aload 19
    //   287: invokestatic 176	com/google/android/music/sharedpreview/SharedSongResponse:parseFromJsonInputStream	(Ljava/io/InputStream;)Lcom/google/android/music/sharedpreview/SharedSongResponse;
    //   290: astore 10
    //   292: aload 10
    //   294: astore 12
    //   296: aload 19
    //   298: astore 11
    //   300: goto -199 -> 101
    //   303: astore 20
    //   305: aload 11
    //   307: invokestatic 137	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   310: aload 4
    //   312: aload 9
    //   314: invokestatic 141	com/google/android/music/cloudclient/MusicRequest:releaseResponse	(Lorg/apache/http/client/methods/HttpUriRequest;Lorg/apache/http/HttpResponse;)V
    //   317: aload 20
    //   319: athrow
    //   320: astore 21
    //   322: ldc 66
    //   324: ldc 178
    //   326: aload 21
    //   328: invokestatic 182	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   331: aconst_null
    //   332: astore 12
    //   334: goto -185 -> 149
    //   337: astore 22
    //   339: ldc 66
    //   341: ldc 178
    //   343: aload 22
    //   345: invokestatic 182	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   348: goto -17 -> 331
    //   351: astore 20
    //   353: aload 19
    //   355: astore 11
    //   357: goto -52 -> 305
    //   360: aload 19
    //   362: astore 11
    //   364: goto -263 -> 101
    //
    // Exception table:
    //   from	to	target	type
    //   94	137	303	finally
    //   152	234	303	finally
    //   274	285	303	finally
    //   0	86	320	java/lang/InterruptedException
    //   137	149	320	java/lang/InterruptedException
    //   305	320	320	java/lang/InterruptedException
    //   0	86	337	java/io/IOException
    //   137	149	337	java/io/IOException
    //   305	320	337	java/io/IOException
    //   234	271	351	finally
    //   285	292	351	finally
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.sharedpreview.SharedPreviewClient
 * JD-Core Version:    0.6.2
 */