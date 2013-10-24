package com.google.android.music.playback;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class RemoteControlClientCompat
{
  public static int FLAG_KEY_MEDIA_FAST_FORWARD;
  public static int FLAG_KEY_MEDIA_NEXT;
  public static int FLAG_KEY_MEDIA_PAUSE;
  public static int FLAG_KEY_MEDIA_PLAY;
  public static int FLAG_KEY_MEDIA_PLAY_PAUSE;
  public static int FLAG_KEY_MEDIA_POSITION_UPDATE;
  public static int FLAG_KEY_MEDIA_PREVIOUS;
  public static int FLAG_KEY_MEDIA_REWIND;
  public static int FLAG_KEY_MEDIA_STOP;
  public static int PLAYSTATE_BUFFERING;
  public static int PLAYSTATE_ERROR;
  public static int PLAYSTATE_FAST_FORWARDING;
  public static int PLAYSTATE_PAUSED;
  public static int PLAYSTATE_PLAYING;
  public static int PLAYSTATE_REWINDING;
  public static int PLAYSTATE_SKIPPING_BACKWARDS;
  public static int PLAYSTATE_SKIPPING_FORWARDS;
  public static int PLAYSTATE_STOPPED;
  private static boolean sHasRemoteControlAPIs = true;
  private static Class sOnGetPlaybackPositionListenerInterface;
  private static Class sOnPlaybackPositionUpdateListenerInterface;
  private static Method sRCCEditMetadataMethod;
  private static Method sRCCSetOnGetPlaybackPositionListener;
  private static Method sRCCSetPlayStateMethodJBMR2Plus;
  private static Method sRCCSetPlayStateMethodPreJBMR2;
  private static Method sRCCSetPlaybackPositionUpdateListener;
  private static Method sRCCSetTransportControlFlags;
  private static Class sRemoteControlClientClass;
  private Object mActualRemoteControlClient;

  // ERROR //
  static
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_0
    //   2: iconst_1
    //   3: putstatic 68	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_STOPPED	I
    //   6: iconst_2
    //   7: putstatic 70	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_PAUSED	I
    //   10: iconst_3
    //   11: putstatic 72	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_PLAYING	I
    //   14: iconst_4
    //   15: putstatic 74	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_FAST_FORWARDING	I
    //   18: iconst_5
    //   19: putstatic 76	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_REWINDING	I
    //   22: bipush 6
    //   24: putstatic 78	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_SKIPPING_FORWARDS	I
    //   27: bipush 7
    //   29: putstatic 80	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_SKIPPING_BACKWARDS	I
    //   32: bipush 8
    //   34: putstatic 82	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_BUFFERING	I
    //   37: bipush 9
    //   39: putstatic 84	com/google/android/music/playback/RemoteControlClientCompat:PLAYSTATE_ERROR	I
    //   42: iconst_1
    //   43: putstatic 86	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_PREVIOUS	I
    //   46: iconst_2
    //   47: putstatic 88	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_REWIND	I
    //   50: iconst_4
    //   51: putstatic 90	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_PLAY	I
    //   54: bipush 8
    //   56: putstatic 92	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_PLAY_PAUSE	I
    //   59: bipush 16
    //   61: putstatic 94	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_PAUSE	I
    //   64: bipush 32
    //   66: putstatic 96	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_STOP	I
    //   69: bipush 64
    //   71: putstatic 98	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_FAST_FORWARD	I
    //   74: sipush 128
    //   77: putstatic 100	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_NEXT	I
    //   80: sipush 256
    //   83: putstatic 102	com/google/android/music/playback/RemoteControlClientCompat:FLAG_KEY_MEDIA_POSITION_UPDATE	I
    //   86: aconst_null
    //   87: putstatic 104	com/google/android/music/playback/RemoteControlClientCompat:sOnPlaybackPositionUpdateListenerInterface	Ljava/lang/Class;
    //   90: aconst_null
    //   91: putstatic 106	com/google/android/music/playback/RemoteControlClientCompat:sOnGetPlaybackPositionListenerInterface	Ljava/lang/Class;
    //   94: aconst_null
    //   95: putstatic 108	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetPlayStateMethodJBMR2Plus	Ljava/lang/reflect/Method;
    //   98: aconst_null
    //   99: putstatic 110	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetPlaybackPositionUpdateListener	Ljava/lang/reflect/Method;
    //   102: aconst_null
    //   103: putstatic 112	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetOnGetPlaybackPositionListener	Ljava/lang/reflect/Method;
    //   106: aload_0
    //   107: putstatic 114	com/google/android/music/playback/RemoteControlClientCompat:sHasRemoteControlAPIs	Z
    //   110: invokestatic 120	com/google/android/music/preferences/MusicPreferences:isICSOrGreater	()Z
    //   113: ifne +4 -> 117
    //   116: return
    //   117: ldc 2
    //   119: invokevirtual 126	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
    //   122: invokestatic 130	com/google/android/music/playback/RemoteControlClientCompat:getActualRemoteControlClientClass	(Ljava/lang/ClassLoader;)Ljava/lang/Class;
    //   125: putstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   128: ldc 2
    //   130: invokevirtual 136	java/lang/Class:getFields	()[Ljava/lang/reflect/Field;
    //   133: astore_1
    //   134: aload_1
    //   135: arraylength
    //   136: istore_2
    //   137: iconst_0
    //   138: istore_3
    //   139: iload_3
    //   140: iload_2
    //   141: if_icmpge +366 -> 507
    //   144: aload_1
    //   145: iload_3
    //   146: aaload
    //   147: astore 4
    //   149: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   152: astore 5
    //   154: aload 4
    //   156: invokevirtual 142	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   159: astore 6
    //   161: aload 5
    //   163: aload 6
    //   165: invokevirtual 146	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   168: aconst_null
    //   169: invokevirtual 150	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   172: astore 7
    //   174: aload 4
    //   176: aconst_null
    //   177: aload 7
    //   179: invokevirtual 154	java/lang/reflect/Field:set	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   182: iload_3
    //   183: iconst_1
    //   184: iadd
    //   185: istore_3
    //   186: goto -47 -> 139
    //   189: astore 8
    //   191: new 156	java/lang/StringBuilder
    //   194: dup
    //   195: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   198: ldc 161
    //   200: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: astore 9
    //   205: aload 4
    //   207: invokevirtual 142	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   210: astore 10
    //   212: aload 9
    //   214: aload 10
    //   216: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   219: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   222: astore 11
    //   224: ldc 170
    //   226: aload 11
    //   228: invokestatic 176	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   231: istore 12
    //   233: goto -51 -> 182
    //   236: astore 13
    //   238: new 156	java/lang/StringBuilder
    //   241: dup
    //   242: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   245: ldc 178
    //   247: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   250: astore 14
    //   252: aload 13
    //   254: invokevirtual 181	java/lang/ClassNotFoundException:getMessage	()Ljava/lang/String;
    //   257: astore 15
    //   259: aload 14
    //   261: aload 15
    //   263: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   266: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   269: astore 16
    //   271: ldc 170
    //   273: aload 16
    //   275: invokestatic 176	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   278: istore 17
    //   280: return
    //   281: astore_0
    //   282: new 156	java/lang/StringBuilder
    //   285: dup
    //   286: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   289: ldc 183
    //   291: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   294: astore 18
    //   296: aload 4
    //   298: invokevirtual 142	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   301: astore 19
    //   303: aload 18
    //   305: aload 19
    //   307: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   310: ldc 185
    //   312: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: astore 20
    //   317: aload_0
    //   318: invokevirtual 186	java/lang/IllegalArgumentException:getMessage	()Ljava/lang/String;
    //   321: astore 21
    //   323: aload 20
    //   325: aload 21
    //   327: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   330: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   333: astore 22
    //   335: ldc 170
    //   337: aload 22
    //   339: invokestatic 176	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   342: istore 23
    //   344: goto -162 -> 182
    //   347: astore 24
    //   349: new 156	java/lang/StringBuilder
    //   352: dup
    //   353: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   356: ldc 188
    //   358: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: astore 25
    //   363: aload 24
    //   365: invokevirtual 189	java/lang/SecurityException:getMessage	()Ljava/lang/String;
    //   368: astore 26
    //   370: aload 25
    //   372: aload 26
    //   374: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   377: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   380: astore 27
    //   382: ldc 170
    //   384: aload 27
    //   386: aload 24
    //   388: invokestatic 192	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   391: istore 28
    //   393: return
    //   394: astore_0
    //   395: new 156	java/lang/StringBuilder
    //   398: dup
    //   399: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   402: ldc 183
    //   404: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   407: astore 29
    //   409: aload 4
    //   411: invokevirtual 142	java/lang/reflect/Field:getName	()Ljava/lang/String;
    //   414: astore 30
    //   416: aload 29
    //   418: aload 30
    //   420: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   423: ldc 185
    //   425: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   428: astore 31
    //   430: aload_0
    //   431: invokevirtual 193	java/lang/IllegalAccessException:getMessage	()Ljava/lang/String;
    //   434: astore 32
    //   436: aload 31
    //   438: aload 32
    //   440: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   443: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   446: astore 33
    //   448: ldc 170
    //   450: aload 33
    //   452: invokestatic 176	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;)I
    //   455: istore 34
    //   457: goto -275 -> 182
    //   460: astore 35
    //   462: new 156	java/lang/StringBuilder
    //   465: dup
    //   466: invokespecial 159	java/lang/StringBuilder:<init>	()V
    //   469: ldc 195
    //   471: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   474: astore 36
    //   476: aload 35
    //   478: invokevirtual 196	java/lang/NoSuchMethodException:getMessage	()Ljava/lang/String;
    //   481: astore 37
    //   483: aload 36
    //   485: aload 37
    //   487: invokevirtual 165	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   490: invokevirtual 168	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   493: astore 38
    //   495: ldc 170
    //   497: aload 38
    //   499: aload 35
    //   501: invokestatic 192	android/util/Log:w	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   504: istore 39
    //   506: return
    //   507: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   510: astore 40
    //   512: iconst_1
    //   513: anewarray 122	java/lang/Class
    //   516: astore 41
    //   518: getstatic 201	java/lang/Boolean:TYPE	Ljava/lang/Class;
    //   521: astore 42
    //   523: aload 41
    //   525: iconst_0
    //   526: aload 42
    //   528: aastore
    //   529: aload 40
    //   531: ldc 203
    //   533: aload 41
    //   535: invokevirtual 207	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   538: putstatic 209	com/google/android/music/playback/RemoteControlClientCompat:sRCCEditMetadataMethod	Ljava/lang/reflect/Method;
    //   541: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   544: astore 43
    //   546: iconst_1
    //   547: anewarray 122	java/lang/Class
    //   550: astore 44
    //   552: getstatic 212	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   555: astore 45
    //   557: aload 44
    //   559: iconst_0
    //   560: aload 45
    //   562: aastore
    //   563: aload 43
    //   565: ldc 214
    //   567: aload 44
    //   569: invokevirtual 207	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   572: putstatic 216	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetPlayStateMethodPreJBMR2	Ljava/lang/reflect/Method;
    //   575: invokestatic 219	com/google/android/music/preferences/MusicPreferences:isJellyBeanMR2OrGreater	()Z
    //   578: ifne +9 -> 587
    //   581: invokestatic 222	com/google/android/music/preferences/MusicPreferences:isGlass	()Z
    //   584: ifeq +143 -> 727
    //   587: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   590: astore 46
    //   592: iconst_3
    //   593: anewarray 122	java/lang/Class
    //   596: astore 47
    //   598: getstatic 212	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   601: astore 48
    //   603: aload 47
    //   605: iconst_0
    //   606: aload 48
    //   608: aastore
    //   609: getstatic 225	java/lang/Long:TYPE	Ljava/lang/Class;
    //   612: astore 49
    //   614: aload 47
    //   616: iconst_1
    //   617: aload 49
    //   619: aastore
    //   620: getstatic 228	java/lang/Float:TYPE	Ljava/lang/Class;
    //   623: astore 50
    //   625: aload 47
    //   627: iconst_2
    //   628: aload 50
    //   630: aastore
    //   631: aload 46
    //   633: ldc 214
    //   635: aload 47
    //   637: invokevirtual 207	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   640: putstatic 108	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetPlayStateMethodJBMR2Plus	Ljava/lang/reflect/Method;
    //   643: ldc 230
    //   645: invokestatic 234	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   648: putstatic 104	com/google/android/music/playback/RemoteControlClientCompat:sOnPlaybackPositionUpdateListenerInterface	Ljava/lang/Class;
    //   651: ldc 236
    //   653: invokestatic 234	java/lang/Class:forName	(Ljava/lang/String;)Ljava/lang/Class;
    //   656: putstatic 106	com/google/android/music/playback/RemoteControlClientCompat:sOnGetPlaybackPositionListenerInterface	Ljava/lang/Class;
    //   659: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   662: astore 51
    //   664: iconst_1
    //   665: anewarray 122	java/lang/Class
    //   668: astore 52
    //   670: getstatic 104	com/google/android/music/playback/RemoteControlClientCompat:sOnPlaybackPositionUpdateListenerInterface	Ljava/lang/Class;
    //   673: astore 53
    //   675: aload 52
    //   677: iconst_0
    //   678: aload 53
    //   680: aastore
    //   681: aload 51
    //   683: ldc 238
    //   685: aload 52
    //   687: invokevirtual 207	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   690: putstatic 110	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetPlaybackPositionUpdateListener	Ljava/lang/reflect/Method;
    //   693: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   696: astore 54
    //   698: iconst_1
    //   699: anewarray 122	java/lang/Class
    //   702: astore 55
    //   704: getstatic 106	com/google/android/music/playback/RemoteControlClientCompat:sOnGetPlaybackPositionListenerInterface	Ljava/lang/Class;
    //   707: astore 56
    //   709: aload 55
    //   711: iconst_0
    //   712: aload 56
    //   714: aastore
    //   715: aload 54
    //   717: ldc 240
    //   719: aload 55
    //   721: invokevirtual 207	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   724: putstatic 112	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetOnGetPlaybackPositionListener	Ljava/lang/reflect/Method;
    //   727: getstatic 132	com/google/android/music/playback/RemoteControlClientCompat:sRemoteControlClientClass	Ljava/lang/Class;
    //   730: astore 57
    //   732: iconst_1
    //   733: anewarray 122	java/lang/Class
    //   736: astore 58
    //   738: getstatic 212	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   741: astore 59
    //   743: aload 58
    //   745: iconst_0
    //   746: aload 59
    //   748: aastore
    //   749: aload 57
    //   751: ldc 242
    //   753: aload 58
    //   755: invokevirtual 207	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   758: putstatic 244	com/google/android/music/playback/RemoteControlClientCompat:sRCCSetTransportControlFlags	Ljava/lang/reflect/Method;
    //   761: iconst_1
    //   762: putstatic 114	com/google/android/music/playback/RemoteControlClientCompat:sHasRemoteControlAPIs	Z
    //   765: return
    //
    // Exception table:
    //   from	to	target	type
    //   149	182	189	java/lang/NoSuchFieldException
    //   117	149	236	java/lang/ClassNotFoundException
    //   149	182	236	java/lang/ClassNotFoundException
    //   191	233	236	java/lang/ClassNotFoundException
    //   282	344	236	java/lang/ClassNotFoundException
    //   395	457	236	java/lang/ClassNotFoundException
    //   507	765	236	java/lang/ClassNotFoundException
    //   149	182	281	java/lang/IllegalArgumentException
    //   117	149	347	java/lang/SecurityException
    //   149	182	347	java/lang/SecurityException
    //   191	233	347	java/lang/SecurityException
    //   282	344	347	java/lang/SecurityException
    //   395	457	347	java/lang/SecurityException
    //   507	765	347	java/lang/SecurityException
    //   149	182	394	java/lang/IllegalAccessException
    //   117	149	460	java/lang/NoSuchMethodException
    //   149	182	460	java/lang/NoSuchMethodException
    //   191	233	460	java/lang/NoSuchMethodException
    //   282	344	460	java/lang/NoSuchMethodException
    //   395	457	460	java/lang/NoSuchMethodException
    //   507	765	460	java/lang/NoSuchMethodException
  }

  public RemoteControlClientCompat(PendingIntent paramPendingIntent, Looper paramLooper)
  {
    if (!sHasRemoteControlAPIs)
      return;
    try
    {
      Class localClass = sRemoteControlClientClass;
      Class[] arrayOfClass = new Class[2];
      arrayOfClass[0] = PendingIntent.class;
      arrayOfClass[1] = Looper.class;
      Constructor localConstructor = localClass.getConstructor(arrayOfClass);
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = paramPendingIntent;
      arrayOfObject[1] = paramLooper;
      Object localObject = localConstructor.newInstance(arrayOfObject);
      this.mActualRemoteControlClient = localObject;
      return;
    }
    catch (Exception localException)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Error creating new instance of ");
      String str1 = sRemoteControlClientClass.getName();
      String str2 = str1;
      int i = Log.e("RemoteControlCompat", str2, localException);
    }
  }

  public static Class getActualRemoteControlClientClass(ClassLoader paramClassLoader)
    throws ClassNotFoundException
  {
    return paramClassLoader.loadClass("android.media.RemoteControlClient");
  }

  public MetadataEditorCompat editMetadata(boolean paramBoolean)
  {
    if (sHasRemoteControlAPIs);
    while (true)
    {
      try
      {
        Method localMethod = sRCCEditMetadataMethod;
        Object localObject1 = this.mActualRemoteControlClient;
        Object[] arrayOfObject = new Object[1];
        Boolean localBoolean = Boolean.valueOf(paramBoolean);
        arrayOfObject[0] = localBoolean;
        Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
        localObject3 = localObject2;
        return new MetadataEditorCompat(localObject3, null);
      }
      catch (Exception localException)
      {
        throw new RuntimeException(localException);
      }
      Object localObject3 = null;
    }
  }

  public final Object getActualRemoteControlClientObject()
  {
    return this.mActualRemoteControlClient;
  }

  public void setOnGetPlaybackPositionListener(final OnGetPlaybackPositionListener paramOnGetPlaybackPositionListener)
  {
    if (sRCCSetPlayStateMethodJBMR2Plus == null)
      return;
    if (sOnGetPlaybackPositionListenerInterface == null)
      return;
    ClassLoader localClassLoader = sOnGetPlaybackPositionListenerInterface.getClassLoader();
    Class[] arrayOfClass = new Class[1];
    Class localClass = sOnGetPlaybackPositionListenerInterface;
    arrayOfClass[0] = localClass;
    InvocationHandler local2 = new InvocationHandler()
    {
      public Object invoke(Object paramAnonymousObject, Method paramAnonymousMethod, Object[] paramAnonymousArrayOfObject)
        throws Throwable
      {
        if (paramAnonymousMethod.getName().equals("onGetPlaybackPosition"));
        for (Object localObject = Long.valueOf(paramOnGetPlaybackPositionListener.onGetPlaybackPosition()); ; localObject = Integer.valueOf(-1))
        {
          return localObject;
          StringBuilder localStringBuilder = new StringBuilder().append("Unexpected interface method call: ");
          String str1 = paramAnonymousMethod.getName();
          String str2 = str1;
          int i = Log.wtf("RemoteControlCompat", str2);
        }
      }
    };
    Object localObject1 = Proxy.newProxyInstance(localClassLoader, arrayOfClass, local2);
    try
    {
      Method localMethod = sRCCSetOnGetPlaybackPositionListener;
      Object localObject2 = this.mActualRemoteControlClient;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localObject1;
      Object localObject3 = localMethod.invoke(localObject2, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  public void setPlaybackPositionUpdateListener(final OnPlaybackPositionUpdateListener paramOnPlaybackPositionUpdateListener)
  {
    if (sRCCSetPlayStateMethodJBMR2Plus == null)
      return;
    if (sOnPlaybackPositionUpdateListenerInterface == null)
      return;
    ClassLoader localClassLoader = sOnPlaybackPositionUpdateListenerInterface.getClassLoader();
    Class[] arrayOfClass = new Class[1];
    Class localClass = sOnPlaybackPositionUpdateListenerInterface;
    arrayOfClass[0] = localClass;
    InvocationHandler local1 = new InvocationHandler()
    {
      public Object invoke(Object paramAnonymousObject, Method paramAnonymousMethod, Object[] paramAnonymousArrayOfObject)
        throws Throwable
      {
        if (paramAnonymousMethod.getName().equals("onPlaybackPositionUpdate"))
        {
          RemoteControlClientCompat.OnPlaybackPositionUpdateListener localOnPlaybackPositionUpdateListener = paramOnPlaybackPositionUpdateListener;
          long l = ((Long)paramAnonymousArrayOfObject[0]).longValue();
          localOnPlaybackPositionUpdateListener.onPlaybackPositionUpdate(l);
        }
        while (true)
        {
          return null;
          StringBuilder localStringBuilder = new StringBuilder().append("Unexpected interface method call: ");
          String str1 = paramAnonymousMethod.getName();
          String str2 = str1;
          int i = Log.wtf("RemoteControlCompat", str2);
        }
      }
    };
    Object localObject1 = Proxy.newProxyInstance(localClassLoader, arrayOfClass, local1);
    try
    {
      Method localMethod = sRCCSetPlaybackPositionUpdateListener;
      Object localObject2 = this.mActualRemoteControlClient;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = localObject1;
      Object localObject3 = localMethod.invoke(localObject2, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  public void setPlaybackState(int paramInt)
  {
    if (!sHasRemoteControlAPIs)
      return;
    try
    {
      Method localMethod = sRCCSetPlayStateMethodPreJBMR2;
      Object localObject1 = this.mActualRemoteControlClient;
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  public void setPlaybackState(int paramInt, long paramLong, float paramFloat)
  {
    if (!sHasRemoteControlAPIs)
      return;
    try
    {
      if (sRCCSetPlayStateMethodJBMR2Plus != null)
      {
        Method localMethod1 = sRCCSetPlayStateMethodJBMR2Plus;
        Object localObject1 = this.mActualRemoteControlClient;
        Object[] arrayOfObject1 = new Object[3];
        Integer localInteger1 = Integer.valueOf(paramInt);
        arrayOfObject1[0] = localInteger1;
        Long localLong = Long.valueOf(paramLong);
        arrayOfObject1[1] = localLong;
        Float localFloat = Float.valueOf(paramFloat);
        arrayOfObject1[2] = localFloat;
        Object localObject2 = localMethod1.invoke(localObject1, arrayOfObject1);
        return;
      }
      Method localMethod2 = sRCCSetPlayStateMethodPreJBMR2;
      Object localObject3 = this.mActualRemoteControlClient;
      Object[] arrayOfObject2 = new Object[1];
      Integer localInteger2 = Integer.valueOf(paramInt);
      arrayOfObject2[0] = localInteger2;
      Object localObject4 = localMethod2.invoke(localObject3, arrayOfObject2);
      return;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  public void setTransportControlFlags(int paramInt)
  {
    if (!sHasRemoteControlAPIs)
      return;
    try
    {
      Method localMethod = sRCCSetTransportControlFlags;
      Object localObject1 = this.mActualRemoteControlClient;
      Object[] arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(paramInt);
      arrayOfObject[0] = localInteger;
      Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
      return;
    }
    catch (Exception localException)
    {
      throw new RuntimeException(localException);
    }
  }

  public class MetadataEditorCompat
  {
    private Object mActualMetadataEditor;
    private Method mApplyMethod;
    private Method mClearMethod;
    private Method mPutBitmapMethod;
    private Method mPutLongMethod;
    private Method mPutStringMethod;

    private MetadataEditorCompat(Object arg2)
    {
      Object localObject;
      if ((RemoteControlClientCompat.sHasRemoteControlAPIs) && (localObject == null))
        throw new IllegalArgumentException("Remote Control API's exist, should not be given a null MetadataEditor");
      Class localClass1;
      if (RemoteControlClientCompat.sHasRemoteControlAPIs)
        localClass1 = localObject.getClass();
      try
      {
        Class[] arrayOfClass1 = new Class[2];
        Class localClass2 = Integer.TYPE;
        arrayOfClass1[0] = localClass2;
        arrayOfClass1[1] = String.class;
        Method localMethod1 = localClass1.getMethod("putString", arrayOfClass1);
        this.mPutStringMethod = localMethod1;
        Class[] arrayOfClass2 = new Class[2];
        Class localClass3 = Integer.TYPE;
        arrayOfClass2[0] = localClass3;
        arrayOfClass2[1] = Bitmap.class;
        Method localMethod2 = localClass1.getMethod("putBitmap", arrayOfClass2);
        this.mPutBitmapMethod = localMethod2;
        Class[] arrayOfClass3 = new Class[2];
        Class localClass4 = Integer.TYPE;
        arrayOfClass3[0] = localClass4;
        Class localClass5 = Long.TYPE;
        arrayOfClass3[1] = localClass5;
        Method localMethod3 = localClass1.getMethod("putLong", arrayOfClass3);
        this.mPutLongMethod = localMethod3;
        Class[] arrayOfClass4 = new Class[0];
        Method localMethod4 = localClass1.getMethod("clear", arrayOfClass4);
        this.mClearMethod = localMethod4;
        Class[] arrayOfClass5 = new Class[0];
        Method localMethod5 = localClass1.getMethod("apply", arrayOfClass5);
        this.mApplyMethod = localMethod5;
        this.mActualMetadataEditor = localObject;
        return;
      }
      catch (Exception localException)
      {
        String str = localException.getMessage();
        throw new RuntimeException(str, localException);
      }
    }

    public void apply()
    {
      if (!RemoteControlClientCompat.sHasRemoteControlAPIs)
        return;
      try
      {
        Method localMethod = this.mApplyMethod;
        Object localObject1 = this.mActualMetadataEditor;
        Object[] arrayOfObject = (Object[])null;
        Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
        return;
      }
      catch (Exception localException)
      {
        String str = localException.getMessage();
        throw new RuntimeException(str, localException);
      }
    }

    public MetadataEditorCompat putBitmap(int paramInt, Bitmap paramBitmap)
    {
      if (RemoteControlClientCompat.sHasRemoteControlAPIs);
      try
      {
        Method localMethod = this.mPutBitmapMethod;
        Object localObject1 = this.mActualMetadataEditor;
        Object[] arrayOfObject = new Object[2];
        Integer localInteger = Integer.valueOf(paramInt);
        arrayOfObject[0] = localInteger;
        arrayOfObject[1] = paramBitmap;
        Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
        return this;
      }
      catch (Exception localException)
      {
        String str = localException.getMessage();
        throw new RuntimeException(str, localException);
      }
    }

    public MetadataEditorCompat putLong(int paramInt, long paramLong)
    {
      if (RemoteControlClientCompat.sHasRemoteControlAPIs);
      try
      {
        Method localMethod = this.mPutLongMethod;
        Object localObject1 = this.mActualMetadataEditor;
        Object[] arrayOfObject = new Object[2];
        Integer localInteger = Integer.valueOf(paramInt);
        arrayOfObject[0] = localInteger;
        Long localLong = Long.valueOf(paramLong);
        arrayOfObject[1] = localLong;
        Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
        return this;
      }
      catch (Exception localException)
      {
        String str = localException.getMessage();
        throw new RuntimeException(str, localException);
      }
    }

    public MetadataEditorCompat putString(int paramInt, String paramString)
    {
      if (RemoteControlClientCompat.sHasRemoteControlAPIs);
      try
      {
        Method localMethod = this.mPutStringMethod;
        Object localObject1 = this.mActualMetadataEditor;
        Object[] arrayOfObject = new Object[2];
        Integer localInteger = Integer.valueOf(paramInt);
        arrayOfObject[0] = localInteger;
        arrayOfObject[1] = paramString;
        Object localObject2 = localMethod.invoke(localObject1, arrayOfObject);
        return this;
      }
      catch (Exception localException)
      {
        String str = localException.getMessage();
        throw new RuntimeException(str, localException);
      }
    }
  }

  public static abstract interface OnGetPlaybackPositionListener
  {
    public abstract long onGetPlaybackPosition();
  }

  public static abstract interface OnPlaybackPositionUpdateListener
  {
    public abstract void onPlaybackPositionUpdate(long paramLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.playback.RemoteControlClientCompat
 * JD-Core Version:    0.6.2
 */