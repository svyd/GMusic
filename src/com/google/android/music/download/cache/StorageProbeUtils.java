package com.google.android.music.download.cache;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore.Files;
import android.text.TextUtils;
import com.google.android.music.log.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.Store;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class StorageProbeUtils
{
  private static final EnumSet<ProbeMethod> DEFAULT_PROBE_METHODS = EnumSet.of(localProbeMethod1, localProbeMethod2);
  private static final boolean LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.DOWNLOAD);

  static
  {
    ProbeMethod localProbeMethod1 = ProbeMethod.MOUNT_SERVICE;
    ProbeMethod localProbeMethod2 = ProbeMethod.MEDIA_PROVIDER;
  }

  @SuppressLint({"NewApi"})
  private static Set<StorageLocation> getMountPointsUsingMediaProvider(Context paramContext)
  {
    HashSet localHashSet = new HashSet();
    if (!MusicPreferences.isHoneycombOrGreater());
    while (true)
    {
      return localHashSet;
      Uri localUri = MediaStore.Files.getContentUri("external");
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "_data";
      Cursor localCursor;
      while (true)
      {
        try
        {
          localCursor = paramContext.getContentResolver().query(localUri, arrayOfString, "parent=0", null, null);
          if (localCursor == null)
            break;
          if (!localCursor.moveToNext())
            break;
          if (localCursor.isNull(0))
          {
            localObject1 = "null";
            if (LOGV)
            {
              String str1 = "c: " + (String)localObject1;
              Log.v("StorageProbeUtils", str1);
            }
            String str2 = new File((String)localObject1).getParent();
            ProbeMethod localProbeMethod = ProbeMethod.MEDIA_PROVIDER;
            StorageLocation localStorageLocation = StorageLocation.get(str2, localProbeMethod);
            boolean bool = localHashSet.add(localStorageLocation);
            continue;
          }
        }
        finally
        {
          Store.safeClose(localCursor);
        }
        int i = 0;
        String str3 = localCursor.getString(i);
        Object localObject1 = str3;
      }
      Store.safeClose(localCursor);
    }
  }

  private static Set<StorageLocation> getMountPointsUsingMountService(Context paramContext)
  {
    HashSet localHashSet = new HashSet();
    try
    {
      Class localClass = Class.forName("android.os.storage.IMountService");
      Class[] arrayOfClass1 = new Class[0];
      int i = localClass.getMethod("getVolumeList", arrayOfClass1);
      Object localObject1 = getMountService();
      localObject2 = Class.forName("android.os.storage.StorageVolume");
      Class[] arrayOfClass2 = new Class[0];
      Method localMethod1 = ((Class)localObject2).getMethod("getPath", arrayOfClass2);
      Class[] arrayOfClass3 = new Class[0];
      Method localMethod2 = ((Class)localObject2).getMethod("isRemovable", arrayOfClass3);
      Class[] arrayOfClass4 = new Class[0];
      Method localMethod3 = ((Class)localObject2).getMethod("isEmulated", arrayOfClass4);
      Method localMethod4 = localMethod3;
      String str1 = null;
      localObject3 = null;
      Object localObject4;
      Object localObject6;
      try
      {
        Class[] arrayOfClass5 = new Class[1];
        arrayOfClass5[0] = Context.class;
        localMethod3 = ((Class)localObject2).getMethod("getDescription", arrayOfClass5);
        Method localMethod5 = localMethod3;
        localObject4 = null;
        localObject5 = localMethod5;
        if (localObject5 != null)
          break label623;
      }
      catch (NoSuchMethodException localNoSuchMethodException1)
      {
        try
        {
          Class[] arrayOfClass6 = new Class[0];
          localMethod3 = ((Class)localObject2).getMethod("getDescription", arrayOfClass6);
          localObject2 = localMethod3;
          localObject3 = null;
          Object[] arrayOfObject1 = new Object[localObject3];
          localObject6 = invoke(i, localObject1, arrayOfObject1);
          if (LOGV)
          {
            String str2 = "res=" + localObject6;
            Log.d("StorageProbeUtils", str2);
          }
          if (localObject6 == null)
          {
            localObject3 = localHashSet;
            return localObject3;
            localNoSuchMethodException1 = localNoSuchMethodException1;
            localObject4 = localObject3;
            localObject5 = str1;
          }
        }
        catch (NoSuchMethodException localNoSuchMethodException2)
        {
          while (true)
            localObject3 = localHashSet;
        }
      }
      if (localObject6.getClass().isArray())
      {
        int k = Array.getLength(localObject6);
        int j = 0;
        if (j < k)
        {
          Object localObject7 = Array.get(localObject6, j);
          Object[] arrayOfObject2 = new Object[0];
          localObject3 = (String)invoke(localMethod1, localObject7, arrayOfObject2);
          Object[] arrayOfObject3 = new Object[0];
          boolean bool1 = ((Boolean)invoke(localMethod2, localObject7, arrayOfObject3)).booleanValue();
          Object[] arrayOfObject4 = new Object[0];
          boolean bool2 = ((Boolean)invoke(localMethod4, localObject7, arrayOfObject4)).booleanValue();
          Object[] arrayOfObject5;
          if (localObject4 != null)
          {
            arrayOfObject5 = new Object[1];
            arrayOfObject5[0] = paramContext;
          }
          Object[] arrayOfObject6;
          for (str1 = (String)invoke((Method)localObject2, localObject7, arrayOfObject5); ; str1 = (String)invoke((Method)localObject2, localObject7, arrayOfObject6))
          {
            if (LOGV)
            {
              String str3 = "arrayElement=" + localObject7;
              Log.v("StorageProbeUtils", str3);
              String str4 = "path=" + (String)localObject3;
              Log.v("StorageProbeUtils", str4);
              StringBuilder localStringBuilder = new StringBuilder().append("state: ");
              String str5 = getVolumeState(localObject1, (String)localObject3);
              String str6 = str5;
              Log.v("StorageProbeUtils", str6);
            }
            ProbeMethod localProbeMethod = ProbeMethod.MOUNT_SERVICE;
            StorageLocation localStorageLocation = StorageLocation.get((String)localObject3, localProbeMethod);
            localStorageLocation.mIsRemovable = bool1;
            localStorageLocation.mIsEmulated = bool2;
            String str7 = getVolumeState(localObject1, (String)localObject3);
            boolean bool3 = "mounted".equals(str7);
            localStorageLocation.mIsMounted = bool3;
            localStorageLocation.mDescription = str1;
            boolean bool4 = localHashSet.add(localStorageLocation);
            j += 1;
            break;
            arrayOfObject6 = new Object[0];
          }
        }
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException3)
    {
      while (true)
      {
        Log.e("StorageProbeUtils", "Exception: ", localNoSuchMethodException3);
        Object localObject3 = localHashSet;
      }
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      while (true)
      {
        Object localObject5;
        Log.e("StorageProbeUtils", "Exception: ", localClassNotFoundException);
        continue;
        label623: Object localObject2 = localObject5;
      }
    }
  }

  // ERROR //
  private static Set<StorageLocation> getMountPointsUsingProcMountinfo()
  {
    // Byte code:
    //   0: new 139	java/io/File
    //   3: dup
    //   4: ldc_w 272
    //   7: invokespecial 142	java/io/File:<init>	(Ljava/lang/String;)V
    //   10: astore_0
    //   11: aconst_null
    //   12: astore_1
    //   13: new 72	java/util/HashSet
    //   16: dup
    //   17: invokespecial 73	java/util/HashSet:<init>	()V
    //   20: astore_2
    //   21: new 274	java/io/FileInputStream
    //   24: dup
    //   25: aload_0
    //   26: invokespecial 277	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   29: astore_3
    //   30: new 279	java/io/InputStreamReader
    //   33: dup
    //   34: aload_3
    //   35: ldc_w 281
    //   38: invokespecial 284	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   41: astore 4
    //   43: new 286	java/io/BufferedReader
    //   46: dup
    //   47: aload 4
    //   49: invokespecial 289	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   52: astore 5
    //   54: aload 5
    //   56: invokevirtual 292	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   59: astore 6
    //   61: aload 6
    //   63: ifnull +192 -> 255
    //   66: getstatic 31	com/google/android/music/download/cache/StorageProbeUtils:LOGV	Z
    //   69: ifeq +33 -> 102
    //   72: new 118	java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   79: ldc_w 294
    //   82: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: aload 6
    //   87: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   93: astore 7
    //   95: ldc 131
    //   97: aload 7
    //   99: invokestatic 137	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   102: aload 6
    //   104: ldc_w 296
    //   107: invokevirtual 300	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   110: astore 8
    //   112: aload 8
    //   114: arraylength
    //   115: bipush 10
    //   117: if_icmple -63 -> 54
    //   120: aload 8
    //   122: bipush 8
    //   124: aaload
    //   125: astore 9
    //   127: ldc_w 302
    //   130: aload 9
    //   132: invokevirtual 253	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   135: ifne +21 -> 156
    //   138: aload 8
    //   140: bipush 8
    //   142: aaload
    //   143: astore 10
    //   145: ldc_w 304
    //   148: aload 10
    //   150: invokevirtual 253	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   153: ifeq -99 -> 54
    //   156: getstatic 31	com/google/android/music/download/cache/StorageProbeUtils:LOGV	Z
    //   159: ifeq +42 -> 201
    //   162: new 118	java/lang/StringBuilder
    //   165: dup
    //   166: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   169: ldc 205
    //   171: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: astore 11
    //   176: aload 8
    //   178: iconst_4
    //   179: aaload
    //   180: astore 12
    //   182: aload 11
    //   184: aload 12
    //   186: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   192: astore 13
    //   194: ldc 131
    //   196: aload 13
    //   198: invokestatic 137	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   201: aload 8
    //   203: iconst_4
    //   204: aaload
    //   205: astore 14
    //   207: getstatic 307	com/google/android/music/download/cache/StorageProbeUtils$ProbeMethod:PROC_MOUNTINFO	Lcom/google/android/music/download/cache/StorageProbeUtils$ProbeMethod;
    //   210: astore 15
    //   212: aload 14
    //   214: aload 15
    //   216: invokestatic 151	com/google/android/music/download/cache/StorageLocation:get	(Ljava/lang/String;Lcom/google/android/music/download/cache/StorageProbeUtils$ProbeMethod;)Lcom/google/android/music/download/cache/StorageLocation;
    //   219: astore 16
    //   221: aload_2
    //   222: aload 16
    //   224: invokeinterface 157 2 0
    //   229: istore 17
    //   231: goto -177 -> 54
    //   234: astore 18
    //   236: aload 5
    //   238: astore_1
    //   239: ldc 131
    //   241: ldc_w 309
    //   244: aload 18
    //   246: invokestatic 266	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   249: aload_1
    //   250: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   253: aload_2
    //   254: areturn
    //   255: aload 5
    //   257: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   260: aload 5
    //   262: astore 19
    //   264: goto -11 -> 253
    //   267: astore 18
    //   269: ldc 131
    //   271: ldc_w 317
    //   274: aload 18
    //   276: invokestatic 266	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   279: aload_1
    //   280: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   283: goto -30 -> 253
    //   286: astore 20
    //   288: aload_1
    //   289: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   292: aload 20
    //   294: athrow
    //   295: astore 20
    //   297: aload 5
    //   299: astore_1
    //   300: goto -12 -> 288
    //   303: astore 18
    //   305: aload 5
    //   307: astore_1
    //   308: goto -39 -> 269
    //   311: astore 18
    //   313: goto -74 -> 239
    //
    // Exception table:
    //   from	to	target	type
    //   54	231	234	java/io/FileNotFoundException
    //   21	54	267	java/io/IOException
    //   21	54	286	finally
    //   239	249	286	finally
    //   269	279	286	finally
    //   54	231	295	finally
    //   54	231	303	java/io/IOException
    //   21	54	311	java/io/FileNotFoundException
  }

  // ERROR //
  private static Set<StorageLocation> getMountPointsUsingProcMounts()
  {
    // Byte code:
    //   0: new 139	java/io/File
    //   3: dup
    //   4: ldc_w 319
    //   7: invokespecial 142	java/io/File:<init>	(Ljava/lang/String;)V
    //   10: astore_0
    //   11: aconst_null
    //   12: astore_1
    //   13: new 72	java/util/HashSet
    //   16: dup
    //   17: invokespecial 73	java/util/HashSet:<init>	()V
    //   20: astore_2
    //   21: new 274	java/io/FileInputStream
    //   24: dup
    //   25: aload_0
    //   26: invokespecial 277	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   29: astore_3
    //   30: new 279	java/io/InputStreamReader
    //   33: dup
    //   34: aload_3
    //   35: ldc_w 281
    //   38: invokespecial 284	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   41: astore 4
    //   43: new 286	java/io/BufferedReader
    //   46: dup
    //   47: aload 4
    //   49: invokespecial 289	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   52: astore 5
    //   54: aload 5
    //   56: invokevirtual 292	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   59: astore 6
    //   61: aload 6
    //   63: ifnull +177 -> 240
    //   66: getstatic 31	com/google/android/music/download/cache/StorageProbeUtils:LOGV	Z
    //   69: ifeq +33 -> 102
    //   72: new 118	java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   79: ldc_w 294
    //   82: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: aload 6
    //   87: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   93: astore 7
    //   95: ldc 131
    //   97: aload 7
    //   99: invokestatic 137	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   102: aload 6
    //   104: ldc_w 302
    //   107: invokevirtual 323	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   110: ifne +14 -> 124
    //   113: aload 6
    //   115: ldc_w 304
    //   118: invokevirtual 323	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   121: ifeq -67 -> 54
    //   124: aload 6
    //   126: ldc_w 296
    //   129: invokevirtual 300	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   132: astore 8
    //   134: aload 8
    //   136: arraylength
    //   137: iconst_1
    //   138: if_icmple -84 -> 54
    //   141: getstatic 31	com/google/android/music/download/cache/StorageProbeUtils:LOGV	Z
    //   144: ifeq +42 -> 186
    //   147: new 118	java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial 119	java/lang/StringBuilder:<init>	()V
    //   154: ldc 205
    //   156: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: astore 9
    //   161: aload 8
    //   163: iconst_1
    //   164: aaload
    //   165: astore 10
    //   167: aload 9
    //   169: aload 10
    //   171: invokevirtual 125	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: invokevirtual 129	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   177: astore 11
    //   179: ldc 131
    //   181: aload 11
    //   183: invokestatic 137	com/google/android/music/log/Log:v	(Ljava/lang/String;Ljava/lang/String;)V
    //   186: aload 8
    //   188: iconst_1
    //   189: aaload
    //   190: astore 12
    //   192: getstatic 326	com/google/android/music/download/cache/StorageProbeUtils$ProbeMethod:PROC_MOUNTS	Lcom/google/android/music/download/cache/StorageProbeUtils$ProbeMethod;
    //   195: astore 13
    //   197: aload 12
    //   199: aload 13
    //   201: invokestatic 151	com/google/android/music/download/cache/StorageLocation:get	(Ljava/lang/String;Lcom/google/android/music/download/cache/StorageProbeUtils$ProbeMethod;)Lcom/google/android/music/download/cache/StorageLocation;
    //   204: astore 14
    //   206: aload_2
    //   207: aload 14
    //   209: invokeinterface 157 2 0
    //   214: istore 15
    //   216: goto -162 -> 54
    //   219: astore 16
    //   221: aload 5
    //   223: astore_1
    //   224: ldc 131
    //   226: ldc_w 309
    //   229: aload 16
    //   231: invokestatic 266	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   234: aload_1
    //   235: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   238: aload_2
    //   239: areturn
    //   240: aload 5
    //   242: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   245: aload 5
    //   247: astore 17
    //   249: goto -11 -> 238
    //   252: astore 16
    //   254: ldc 131
    //   256: ldc_w 317
    //   259: aload 16
    //   261: invokestatic 266	com/google/android/music/log/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)V
    //   264: aload_1
    //   265: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   268: goto -30 -> 238
    //   271: astore 18
    //   273: aload_1
    //   274: invokestatic 315	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   277: aload 18
    //   279: athrow
    //   280: astore 18
    //   282: aload 5
    //   284: astore_1
    //   285: goto -12 -> 273
    //   288: astore 16
    //   290: aload 5
    //   292: astore_1
    //   293: goto -39 -> 254
    //   296: astore 16
    //   298: goto -74 -> 224
    //
    // Exception table:
    //   from	to	target	type
    //   54	216	219	java/io/FileNotFoundException
    //   21	54	252	java/io/IOException
    //   21	54	271	finally
    //   224	234	271	finally
    //   254	264	271	finally
    //   54	216	280	finally
    //   54	216	288	java/io/IOException
    //   21	54	296	java/io/FileNotFoundException
  }

  private static Object getMountService()
  {
    Object localObject1 = null;
    try
    {
      Class localClass1 = Class.forName("android.os.ServiceManager");
      Class[] arrayOfClass1 = new Class[1];
      arrayOfClass1[0] = String.class;
      Method localMethod1 = localClass1.getMethod("getService", arrayOfClass1);
      Class localClass2 = Class.forName("android.os.storage.IMountService$Stub");
      Class[] arrayOfClass2 = new Class[1];
      arrayOfClass2[0] = IBinder.class;
      Method localMethod2 = localClass2.getMethod("asInterface", arrayOfClass2);
      Method localMethod3 = localMethod2;
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = "mount";
      Object localObject2 = invoke(localMethod1, null, arrayOfObject1);
      if (localObject2 != null)
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = localObject2;
        localObject1 = invoke(localMethod3, null, arrayOfObject2);
        return localObject1;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.e("StorageProbeUtils", "Exception: ", localException);
        continue;
        Log.e("StorageProbeUtils", "Failed to get mount service");
      }
    }
  }

  public static Set<StorageLocation> getPossibleSDCardLocations(Context paramContext)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = DEFAULT_PROBE_METHODS.iterator();
    while (localIterator.hasNext())
    {
      ProbeMethod localProbeMethod = (ProbeMethod)localIterator.next();
      try
      {
        Set localSet = localProbeMethod.getMountPoints(paramContext);
        if (LOGV)
        {
          Object[] arrayOfObject = new Object[2];
          arrayOfObject[0] = localProbeMethod;
          Integer localInteger = Integer.valueOf(localSet.size());
          arrayOfObject[1] = localInteger;
          String str1 = String.format("Method %s: %d locations found", arrayOfObject);
          Log.v("StorageProbeUtils", str1);
        }
        boolean bool = localHashSet.addAll(localSet);
      }
      catch (RuntimeException localRuntimeException)
      {
        String str2 = "Error getting mount points using method: " + localProbeMethod;
        Log.e("StorageProbeUtils", str2, localRuntimeException);
      }
    }
    if (LOGV)
    {
      localIterator = localHashSet.iterator();
      while (localIterator.hasNext())
      {
        StorageLocation localStorageLocation = (StorageLocation)localIterator.next();
        String str3 = "found location: " + localStorageLocation;
        Log.d("StorageProbeUtils", str3);
      }
    }
    return localHashSet;
  }

  public static StorageLocation getSecondaryExternalStorageLocation(Context paramContext, MusicPreferences paramMusicPreferences)
  {
    String str = paramMusicPreferences.getSecondaryExternalStorageMountPoint();
    StorageLocation localStorageLocation;
    if (TextUtils.isEmpty(str))
      localStorageLocation = null;
    while (true)
    {
      return localStorageLocation;
      Iterator localIterator = getMountPointsUsingMountService(paramContext).iterator();
      while (true)
        if (localIterator.hasNext())
        {
          localStorageLocation = (StorageLocation)localIterator.next();
          if (localStorageLocation.mMountPoint.equals(str))
            break;
        }
      localStorageLocation = null;
    }
  }

  private static String getVolumeState(Object paramObject, String paramString)
  {
    Object localObject = null;
    try
    {
      Class localClass = Class.forName("android.os.storage.IMountService");
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = String.class;
      Method localMethod = localClass.getMethod("getVolumeState", arrayOfClass);
      localObject = localMethod;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramString;
      return (String)invoke(localObject, paramObject, arrayOfObject);
    }
    catch (Exception localException)
    {
      while (true)
        Log.e("StorageProbeUtils", "Exception: ", localException);
    }
  }

  static String getVolumeState(String paramString)
  {
    return getVolumeState(getMountService(), paramString);
  }

  private static Object invoke(Method paramMethod, Object paramObject, Object[] paramArrayOfObject)
  {
    try
    {
      Object localObject1 = paramMethod.invoke(paramObject, paramArrayOfObject);
      localObject2 = localObject1;
      return localObject2;
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.e("StorageProbeUtils", "Exception", localException);
        Object localObject2 = null;
      }
    }
  }

  static enum ProbeMethod
  {
    static
    {
      PROC_MOUNTINFO = new ProbeMethod("PROC_MOUNTINFO", 1);
      MEDIA_PROVIDER = new ProbeMethod("MEDIA_PROVIDER", 2);
      MOUNT_SERVICE = new ProbeMethod("MOUNT_SERVICE", 3);
      ProbeMethod[] arrayOfProbeMethod = new ProbeMethod[4];
      ProbeMethod localProbeMethod1 = PROC_MOUNTS;
      arrayOfProbeMethod[0] = localProbeMethod1;
      ProbeMethod localProbeMethod2 = PROC_MOUNTINFO;
      arrayOfProbeMethod[1] = localProbeMethod2;
      ProbeMethod localProbeMethod3 = MEDIA_PROVIDER;
      arrayOfProbeMethod[2] = localProbeMethod3;
      ProbeMethod localProbeMethod4 = MOUNT_SERVICE;
      arrayOfProbeMethod[3] = localProbeMethod4;
    }

    public Set<StorageLocation> getMountPoints(Context paramContext)
    {
      int[] arrayOfInt = StorageProbeUtils.1.$SwitchMap$com$google$android$music$download$cache$StorageProbeUtils$ProbeMethod;
      int i = ordinal();
      Set localSet;
      switch (arrayOfInt[i])
      {
      default:
        localSet = StorageProbeUtils.getMountPointsUsingMountService(paramContext);
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        return localSet;
        localSet = StorageProbeUtils.access$000();
        continue;
        localSet = StorageProbeUtils.access$100();
        continue;
        localSet = StorageProbeUtils.getMountPointsUsingMediaProvider(paramContext);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.download.cache.StorageProbeUtils
 * JD-Core Version:    0.6.2
 */