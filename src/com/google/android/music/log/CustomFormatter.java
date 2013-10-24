package com.google.android.music.log;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.logging.Formatter;
import java.util.logging.Level;

public class CustomFormatter extends Formatter
{
  private static final ImmutableMap<Level, String> sLevelMap = localBuilder5.put(localLevel5, "E").build();

  static
  {
    ImmutableMap.Builder localBuilder1 = new ImmutableMap.Builder();
    Level localLevel1 = Level.FINER;
    ImmutableMap.Builder localBuilder2 = localBuilder1.put(localLevel1, "V");
    Level localLevel2 = Level.FINE;
    ImmutableMap.Builder localBuilder3 = localBuilder2.put(localLevel2, "D");
    Level localLevel3 = Level.INFO;
    ImmutableMap.Builder localBuilder4 = localBuilder3.put(localLevel3, "I");
    Level localLevel4 = Level.WARNING;
    ImmutableMap.Builder localBuilder5 = localBuilder4.put(localLevel4, "W");
    Level localLevel5 = Level.SEVERE;
  }

  // ERROR //
  public String format(java.util.logging.LogRecord paramLogRecord)
  {
    // Byte code:
    //   0: getstatic 53	com/google/android/music/log/CustomFormatter:sLevelMap	Lcom/google/common/collect/ImmutableMap;
    //   3: astore_2
    //   4: aload_1
    //   5: invokevirtual 62	java/util/logging/LogRecord:getLevel	()Ljava/util/logging/Level;
    //   8: astore_3
    //   9: aload_2
    //   10: aload_3
    //   11: invokevirtual 68	com/google/common/collect/ImmutableMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast 70	java/lang/String
    //   17: astore 4
    //   19: aload 4
    //   21: ifnonnull +7 -> 28
    //   24: ldc 72
    //   26: astore 4
    //   28: new 74	java/lang/StringBuilder
    //   31: dup
    //   32: invokespecial 75	java/lang/StringBuilder:<init>	()V
    //   35: astore 5
    //   37: new 77	java/text/SimpleDateFormat
    //   40: dup
    //   41: ldc 79
    //   43: invokespecial 82	java/text/SimpleDateFormat:<init>	(Ljava/lang/String;)V
    //   46: astore 6
    //   48: aload_1
    //   49: invokevirtual 86	java/util/logging/LogRecord:getMillis	()J
    //   52: lstore 7
    //   54: new 88	java/util/Date
    //   57: dup
    //   58: lload 7
    //   60: invokespecial 91	java/util/Date:<init>	(J)V
    //   63: astore 9
    //   65: aload 6
    //   67: aload 9
    //   69: invokevirtual 94	java/text/SimpleDateFormat:format	(Ljava/util/Date;)Ljava/lang/String;
    //   72: astore 10
    //   74: aload 5
    //   76: aload 10
    //   78: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: astore 11
    //   83: aload 5
    //   85: aload 4
    //   87: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: astore 12
    //   92: aload 5
    //   94: ldc 100
    //   96: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: astore 13
    //   101: aload_0
    //   102: aload_1
    //   103: invokevirtual 103	com/google/android/music/log/CustomFormatter:formatMessage	(Ljava/util/logging/LogRecord;)Ljava/lang/String;
    //   106: astore 14
    //   108: aload 5
    //   110: aload 14
    //   112: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: ldc 105
    //   117: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: astore 15
    //   122: aload_1
    //   123: invokevirtual 109	java/util/logging/LogRecord:getThrown	()Ljava/lang/Throwable;
    //   126: ifnull +69 -> 195
    //   129: aload 5
    //   131: ldc 111
    //   133: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: astore 16
    //   138: aload_1
    //   139: invokevirtual 109	java/util/logging/LogRecord:getThrown	()Ljava/lang/Throwable;
    //   142: astore 17
    //   144: aconst_null
    //   145: astore 18
    //   147: new 113	java/io/StringWriter
    //   150: dup
    //   151: invokespecial 114	java/io/StringWriter:<init>	()V
    //   154: astore 19
    //   156: new 116	java/io/PrintWriter
    //   159: dup
    //   160: aload 19
    //   162: invokespecial 119	java/io/PrintWriter:<init>	(Ljava/io/Writer;)V
    //   165: astore 20
    //   167: aload 17
    //   169: aload 20
    //   171: invokevirtual 125	java/lang/Throwable:printStackTrace	(Ljava/io/PrintWriter;)V
    //   174: aload 19
    //   176: invokevirtual 129	java/io/StringWriter:toString	()Ljava/lang/String;
    //   179: astore 21
    //   181: aload 5
    //   183: aload 21
    //   185: invokevirtual 98	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   188: astore 22
    //   190: aload 20
    //   192: invokestatic 135	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   195: aload 5
    //   197: invokevirtual 136	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   200: areturn
    //   201: astore 23
    //   203: aload 18
    //   205: invokestatic 135	com/google/common/io/Closeables:closeQuietly	(Ljava/io/Closeable;)V
    //   208: aload 23
    //   210: athrow
    //   211: astore 23
    //   213: aload 20
    //   215: astore 18
    //   217: goto -14 -> 203
    //
    // Exception table:
    //   from	to	target	type
    //   147	167	201	finally
    //   167	190	211	finally
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.log.CustomFormatter
 * JD-Core Version:    0.6.2
 */