package com.google.android.music.ringtone;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class SeekTest
{
  private static byte[] SILENCE_MP3_FRAME = { 255, 251, 16, 196, 0, 3, 129, 244, 1, 38, 96, 0, 64, 32, 89, 128, 35, 72, 0, 9, 116, 0, 1, 18, 3, 255, 255, 255, 255, 254, 159, 99, 191, 209, 122, 63, 93, 1, 255, 255, 255, 255, 254, 141, 173, 108, 49, 66, 195, 2, 199, 12, 9, 134, 131, 168, 122, 58, 104, 76, 65, 77, 69, 51, 46, 57, 56, 46, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  static long after;
  static long before;

  // ERROR //
  static boolean CanSeekAccurately(android.content.SharedPreferences paramSharedPreferences)
  {
    // Byte code:
    //   0: ldc 25
    //   2: astore_1
    //   3: ldc 27
    //   5: aload_1
    //   6: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   9: istore_2
    //   10: aload_0
    //   11: astore_3
    //   12: iconst_0
    //   13: istore 4
    //   15: aload_3
    //   16: ldc 35
    //   18: iload 4
    //   20: invokeinterface 41 3 0
    //   25: istore 5
    //   27: aload_0
    //   28: astore 6
    //   30: ldc2_w 42
    //   33: lstore 7
    //   35: aload 6
    //   37: ldc 45
    //   39: lload 7
    //   41: invokeinterface 49 4 0
    //   46: lstore 9
    //   48: new 51	java/util/Date
    //   51: dup
    //   52: invokespecial 52	java/util/Date:<init>	()V
    //   55: invokevirtual 56	java/util/Date:getTime	()J
    //   58: lstore 11
    //   60: lload 11
    //   62: lload 9
    //   64: lsub
    //   65: ldc2_w 57
    //   68: lcmp
    //   69: ifge +49 -> 118
    //   72: new 60	java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   79: ldc 63
    //   81: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: astore 13
    //   86: iload 5
    //   88: istore 14
    //   90: aload 13
    //   92: iload 14
    //   94: invokevirtual 70	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
    //   97: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   100: astore 15
    //   102: ldc 27
    //   104: aload 15
    //   106: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   109: istore 16
    //   111: iload 5
    //   113: istore 17
    //   115: iload 17
    //   117: ireturn
    //   118: new 60	java/lang/StringBuilder
    //   121: dup
    //   122: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   125: astore 18
    //   127: ldc 76
    //   129: astore 19
    //   131: aload 18
    //   133: aload 19
    //   135: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: astore 20
    //   140: new 78	java/util/Random
    //   143: dup
    //   144: invokespecial 79	java/util/Random:<init>	()V
    //   147: invokevirtual 82	java/util/Random:nextLong	()J
    //   150: lstore 21
    //   152: aload 20
    //   154: lload 21
    //   156: invokevirtual 85	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   159: astore 23
    //   161: ldc 87
    //   163: astore 24
    //   165: aload 23
    //   167: aload 24
    //   169: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   175: astore 25
    //   177: new 89	java/io/File
    //   180: dup
    //   181: aload 25
    //   183: invokespecial 92	java/io/File:<init>	(Ljava/lang/String;)V
    //   186: astore 26
    //   188: iconst_0
    //   189: istore 23
    //   191: ldc 94
    //   193: astore 28
    //   195: new 96	java/io/RandomAccessFile
    //   198: dup
    //   199: aload 26
    //   201: aload 28
    //   203: invokespecial 99	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   206: astore 29
    //   208: iload 23
    //   210: ifne +30 -> 240
    //   213: ldc 101
    //   215: astore 30
    //   217: ldc 27
    //   219: aload 30
    //   221: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   224: istore 31
    //   226: iconst_0
    //   227: istore 17
    //   229: goto -114 -> 115
    //   232: astore 32
    //   234: iconst_1
    //   235: istore 23
    //   237: goto -29 -> 208
    //   240: new 60	java/lang/StringBuilder
    //   243: dup
    //   244: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   247: ldc 103
    //   249: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: aload 25
    //   254: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   260: astore 33
    //   262: ldc 27
    //   264: aload 33
    //   266: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   269: istore 34
    //   271: aload 26
    //   273: invokevirtual 107	java/io/File:createNewFile	()Z
    //   276: istore 35
    //   278: new 109	java/io/FileOutputStream
    //   281: astore 36
    //   283: aload 36
    //   285: aload 26
    //   287: invokespecial 112	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   290: iconst_0
    //   291: istore 37
    //   293: iload 37
    //   295: bipush 80
    //   297: if_icmpge +94 -> 391
    //   300: getstatic 16	com/google/android/music/ringtone/SeekTest:SILENCE_MP3_FRAME	[B
    //   303: astore 38
    //   305: getstatic 16	com/google/android/music/ringtone/SeekTest:SILENCE_MP3_FRAME	[B
    //   308: arraylength
    //   309: istore 39
    //   311: aload 36
    //   313: astore 40
    //   315: iconst_0
    //   316: istore 41
    //   318: iload 39
    //   320: istore 42
    //   322: aload 40
    //   324: aload 38
    //   326: iload 41
    //   328: iload 42
    //   330: invokevirtual 116	java/io/FileOutputStream:write	([BII)V
    //   333: iload 37
    //   335: iconst_1
    //   336: iadd
    //   337: istore 37
    //   339: goto -46 -> 293
    //   342: astore 43
    //   344: ldc 118
    //   346: astore 44
    //   348: ldc 27
    //   350: aload 44
    //   352: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   355: istore 45
    //   357: iconst_0
    //   358: istore 17
    //   360: goto -245 -> 115
    //   363: astore 46
    //   365: ldc 120
    //   367: astore 47
    //   369: ldc 27
    //   371: aload 47
    //   373: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   376: istore 48
    //   378: aload 26
    //   380: invokevirtual 123	java/io/File:delete	()Z
    //   383: istore 49
    //   385: iconst_0
    //   386: istore 17
    //   388: goto -273 -> 115
    //   391: ldc 125
    //   393: astore 50
    //   395: ldc 27
    //   397: aload 50
    //   399: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   402: istore 51
    //   404: new 127	android/media/MediaPlayer
    //   407: dup
    //   408: invokespecial 128	android/media/MediaPlayer:<init>	()V
    //   411: astore 52
    //   413: aload 52
    //   415: iconst_3
    //   416: invokevirtual 132	android/media/MediaPlayer:setAudioStreamType	(I)V
    //   419: new 134	java/io/FileInputStream
    //   422: astore 53
    //   424: aload 53
    //   426: aload 25
    //   428: invokespecial 135	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   431: getstatic 16	com/google/android/music/ringtone/SeekTest:SILENCE_MP3_FRAME	[B
    //   434: arraylength
    //   435: bipush 70
    //   437: imul
    //   438: i2l
    //   439: lstore 54
    //   441: getstatic 16	com/google/android/music/ringtone/SeekTest:SILENCE_MP3_FRAME	[B
    //   444: arraylength
    //   445: bipush 10
    //   447: imul
    //   448: i2l
    //   449: lstore 56
    //   451: aload 53
    //   453: invokevirtual 139	java/io/FileInputStream:getFD	()Ljava/io/FileDescriptor;
    //   456: astore 58
    //   458: aload 52
    //   460: aload 58
    //   462: lload 54
    //   464: lload 56
    //   466: invokevirtual 143	android/media/MediaPlayer:setDataSource	(Ljava/io/FileDescriptor;JJ)V
    //   469: ldc 145
    //   471: astore 59
    //   473: ldc 27
    //   475: aload 59
    //   477: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   480: istore 60
    //   482: aload 52
    //   484: invokevirtual 148	android/media/MediaPlayer:prepare	()V
    //   487: ldc2_w 42
    //   490: putstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   493: ldc2_w 42
    //   496: putstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   499: new 6	com/google/android/music/ringtone/SeekTest$1
    //   502: dup
    //   503: invokespecial 153	com/google/android/music/ringtone/SeekTest$1:<init>	()V
    //   506: astore 61
    //   508: aload 52
    //   510: aload 61
    //   512: invokevirtual 157	android/media/MediaPlayer:setOnCompletionListener	(Landroid/media/MediaPlayer$OnCompletionListener;)V
    //   515: ldc 159
    //   517: astore 62
    //   519: ldc 27
    //   521: aload 62
    //   523: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   526: istore 63
    //   528: aload 52
    //   530: invokevirtual 162	android/media/MediaPlayer:start	()V
    //   533: iconst_0
    //   534: istore 37
    //   536: iload 37
    //   538: sipush 200
    //   541: if_icmpge +89 -> 630
    //   544: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   547: ldc2_w 42
    //   550: lcmp
    //   551: ifne +79 -> 630
    //   554: aload 52
    //   556: invokevirtual 166	android/media/MediaPlayer:getCurrentPosition	()I
    //   559: ifle +56 -> 615
    //   562: new 60	java/lang/StringBuilder
    //   565: dup
    //   566: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   569: ldc 168
    //   571: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   574: astore 64
    //   576: iload 37
    //   578: bipush 10
    //   580: imul
    //   581: istore 65
    //   583: aload 64
    //   585: iload 65
    //   587: invokevirtual 171	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   590: ldc 173
    //   592: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   595: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   598: astore 66
    //   600: ldc 27
    //   602: aload 66
    //   604: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   607: istore 67
    //   609: invokestatic 178	java/lang/System:currentTimeMillis	()J
    //   612: putstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   615: ldc2_w 179
    //   618: invokestatic 186	java/lang/Thread:sleep	(J)V
    //   621: iload 37
    //   623: iconst_1
    //   624: iadd
    //   625: istore 37
    //   627: goto -91 -> 536
    //   630: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   633: ldc2_w 42
    //   636: lcmp
    //   637: ifne +101 -> 738
    //   640: ldc 188
    //   642: astore 68
    //   644: ldc 27
    //   646: aload 68
    //   648: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   651: istore 69
    //   653: ldc 190
    //   655: astore 70
    //   657: ldc 27
    //   659: aload 70
    //   661: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   664: istore 71
    //   666: aload 26
    //   668: invokevirtual 123	java/io/File:delete	()Z
    //   671: istore 72
    //   673: aload_0
    //   674: invokeinterface 194 1 0
    //   679: astore 73
    //   681: aload 73
    //   683: astore 74
    //   685: lload 11
    //   687: lstore 75
    //   689: aload 74
    //   691: ldc 45
    //   693: lload 75
    //   695: invokeinterface 200 4 0
    //   700: astore 77
    //   702: aload 73
    //   704: astore 78
    //   706: iload 5
    //   708: istore 79
    //   710: aload 78
    //   712: ldc 35
    //   714: iload 79
    //   716: invokeinterface 204 3 0
    //   721: astore 80
    //   723: aload 73
    //   725: invokeinterface 207 1 0
    //   730: istore 81
    //   732: iconst_0
    //   733: istore 17
    //   735: goto -620 -> 115
    //   738: ldc 209
    //   740: astore 82
    //   742: ldc 27
    //   744: aload 82
    //   746: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   749: istore 83
    //   751: iconst_0
    //   752: istore 37
    //   754: iload 37
    //   756: sipush 300
    //   759: if_icmpge +70 -> 829
    //   762: getstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   765: ldc2_w 42
    //   768: lcmp
    //   769: ifne +60 -> 829
    //   772: new 60	java/lang/StringBuilder
    //   775: dup
    //   776: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   779: ldc 211
    //   781: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   784: astore 84
    //   786: aload 52
    //   788: invokevirtual 166	android/media/MediaPlayer:getCurrentPosition	()I
    //   791: istore 85
    //   793: aload 84
    //   795: iload 85
    //   797: invokevirtual 171	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   800: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   803: astore 86
    //   805: ldc 27
    //   807: aload 86
    //   809: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   812: istore 87
    //   814: ldc2_w 179
    //   817: invokestatic 186	java/lang/Thread:sleep	(J)V
    //   820: iload 37
    //   822: iconst_1
    //   823: iadd
    //   824: istore 37
    //   826: goto -72 -> 754
    //   829: new 60	java/lang/StringBuilder
    //   832: dup
    //   833: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   836: ldc 213
    //   838: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   841: astore 88
    //   843: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   846: lstore 89
    //   848: aload 88
    //   850: lload 89
    //   852: invokevirtual 85	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   855: ldc 215
    //   857: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   860: astore 91
    //   862: getstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   865: lstore 92
    //   867: aload 91
    //   869: lload 92
    //   871: invokevirtual 85	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   874: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   877: astore 94
    //   879: ldc 27
    //   881: aload 94
    //   883: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   886: istore 95
    //   888: getstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   891: lstore 96
    //   893: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   896: lstore 98
    //   898: lload 96
    //   900: lload 98
    //   902: lcmp
    //   903: ifle +175 -> 1078
    //   906: getstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   909: lstore 100
    //   911: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   914: ldc2_w 216
    //   917: ladd
    //   918: lstore 102
    //   920: lload 100
    //   922: lload 102
    //   924: lcmp
    //   925: ifge +153 -> 1078
    //   928: getstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   931: lstore 104
    //   933: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   936: lstore 106
    //   938: lload 104
    //   940: lload 106
    //   942: lcmp
    //   943: ifle +127 -> 1070
    //   946: getstatic 152	com/google/android/music/ringtone/SeekTest:after	J
    //   949: lstore 108
    //   951: getstatic 150	com/google/android/music/ringtone/SeekTest:before	J
    //   954: lstore 110
    //   956: lload 108
    //   958: lload 110
    //   960: lsub
    //   961: lstore 112
    //   963: new 60	java/lang/StringBuilder
    //   966: dup
    //   967: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   970: ldc 219
    //   972: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   975: lload 112
    //   977: invokevirtual 85	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   980: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   983: astore 114
    //   985: ldc 27
    //   987: aload 114
    //   989: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   992: istore 115
    //   994: iconst_1
    //   995: istore 5
    //   997: aload_0
    //   998: invokeinterface 194 1 0
    //   1003: astore 116
    //   1005: aload 116
    //   1007: astore 117
    //   1009: lload 11
    //   1011: lstore 118
    //   1013: aload 117
    //   1015: ldc 45
    //   1017: lload 118
    //   1019: invokeinterface 200 4 0
    //   1024: astore 120
    //   1026: aload 116
    //   1028: astore 121
    //   1030: iload 5
    //   1032: istore 122
    //   1034: aload 121
    //   1036: ldc 35
    //   1038: iload 122
    //   1040: invokeinterface 204 3 0
    //   1045: astore 123
    //   1047: aload 116
    //   1049: invokeinterface 207 1 0
    //   1054: istore 124
    //   1056: aload 26
    //   1058: invokevirtual 123	java/io/File:delete	()Z
    //   1061: istore 125
    //   1063: iload 5
    //   1065: istore 17
    //   1067: goto -952 -> 115
    //   1070: ldc2_w 220
    //   1073: lstore 112
    //   1075: goto -112 -> 963
    //   1078: ldc 223
    //   1080: astore 126
    //   1082: ldc 27
    //   1084: aload 126
    //   1086: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   1089: istore 127
    //   1091: goto -94 -> 997
    //   1094: astore 128
    //   1096: aload 128
    //   1098: invokevirtual 226	java/lang/Exception:printStackTrace	()V
    //   1101: new 60	java/lang/StringBuilder
    //   1104: dup
    //   1105: invokespecial 61	java/lang/StringBuilder:<init>	()V
    //   1108: ldc 228
    //   1110: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1113: astore 129
    //   1115: aload 128
    //   1117: invokevirtual 229	java/lang/Exception:toString	()Ljava/lang/String;
    //   1120: astore 130
    //   1122: aload 129
    //   1124: aload 130
    //   1126: invokevirtual 67	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1129: invokevirtual 74	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1132: astore 131
    //   1134: ldc 27
    //   1136: aload 131
    //   1138: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   1141: istore 132
    //   1143: ldc 190
    //   1145: astore 133
    //   1147: ldc 27
    //   1149: aload 133
    //   1151: invokestatic 33	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
    //   1154: istore 134
    //   1156: aload 26
    //   1158: invokevirtual 123	java/io/File:delete	()Z
    //   1161: istore 135
    //   1163: aload_0
    //   1164: invokeinterface 194 1 0
    //   1169: astore 136
    //   1171: aload 136
    //   1173: astore 137
    //   1175: lload 11
    //   1177: lstore 138
    //   1179: aload 137
    //   1181: ldc 45
    //   1183: lload 138
    //   1185: invokeinterface 200 4 0
    //   1190: astore 140
    //   1192: aload 136
    //   1194: astore 141
    //   1196: iload 5
    //   1198: istore 142
    //   1200: aload 141
    //   1202: ldc 35
    //   1204: iload 142
    //   1206: invokeinterface 204 3 0
    //   1211: astore 143
    //   1213: aload 136
    //   1215: invokeinterface 207 1 0
    //   1220: istore 144
    //   1222: iconst_0
    //   1223: istore 17
    //   1225: goto -1110 -> 115
    //   1228: astore 145
    //   1230: goto -845 -> 385
    //   1233: astore 146
    //   1235: goto -562 -> 673
    //   1238: astore 147
    //   1240: goto -77 -> 1163
    //   1243: astore 148
    //   1245: goto -182 -> 1063
    //
    // Exception table:
    //   from	to	target	type
    //   191	208	232	java/lang/Exception
    //   271	278	342	java/lang/Exception
    //   278	333	363	java/lang/Exception
    //   391	666	1094	java/lang/Exception
    //   673	994	1094	java/lang/Exception
    //   1078	1091	1094	java/lang/Exception
    //   378	385	1228	java/lang/Exception
    //   666	673	1233	java/lang/Exception
    //   1156	1163	1238	java/lang/Exception
    //   1056	1063	1243	java/lang/Exception
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ringtone.SeekTest
 * JD-Core Version:    0.6.2
 */