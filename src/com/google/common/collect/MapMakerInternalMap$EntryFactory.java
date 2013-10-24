package com.google.common.collect;

import java.lang.ref.ReferenceQueue;

 enum MapMakerInternalMap$EntryFactory
{
  static final EntryFactory[][] factories = arrayOfEntryFactory2;

  static
  {
    // Byte code:
    //   0: new 13	com/google/common/collect/MapMakerInternalMap$EntryFactory$1
    //   3: dup
    //   4: ldc 53
    //   6: iconst_0
    //   7: invokespecial 57	com/google/common/collect/MapMakerInternalMap$EntryFactory$1:<init>	(Ljava/lang/String;I)V
    //   10: putstatic 59	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   13: new 15	com/google/common/collect/MapMakerInternalMap$EntryFactory$2
    //   16: dup
    //   17: ldc 60
    //   19: iconst_1
    //   20: invokespecial 61	com/google/common/collect/MapMakerInternalMap$EntryFactory$2:<init>	(Ljava/lang/String;I)V
    //   23: putstatic 63	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   26: new 17	com/google/common/collect/MapMakerInternalMap$EntryFactory$3
    //   29: dup
    //   30: ldc 64
    //   32: iconst_2
    //   33: invokespecial 65	com/google/common/collect/MapMakerInternalMap$EntryFactory$3:<init>	(Ljava/lang/String;I)V
    //   36: putstatic 67	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   39: new 19	com/google/common/collect/MapMakerInternalMap$EntryFactory$4
    //   42: dup
    //   43: ldc 68
    //   45: iconst_3
    //   46: invokespecial 69	com/google/common/collect/MapMakerInternalMap$EntryFactory$4:<init>	(Ljava/lang/String;I)V
    //   49: putstatic 71	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   52: new 21	com/google/common/collect/MapMakerInternalMap$EntryFactory$5
    //   55: dup
    //   56: ldc 72
    //   58: iconst_4
    //   59: invokespecial 73	com/google/common/collect/MapMakerInternalMap$EntryFactory$5:<init>	(Ljava/lang/String;I)V
    //   62: putstatic 75	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   65: new 23	com/google/common/collect/MapMakerInternalMap$EntryFactory$6
    //   68: dup
    //   69: ldc 76
    //   71: iconst_5
    //   72: invokespecial 77	com/google/common/collect/MapMakerInternalMap$EntryFactory$6:<init>	(Ljava/lang/String;I)V
    //   75: putstatic 79	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   78: new 25	com/google/common/collect/MapMakerInternalMap$EntryFactory$7
    //   81: dup
    //   82: ldc 80
    //   84: bipush 6
    //   86: invokespecial 81	com/google/common/collect/MapMakerInternalMap$EntryFactory$7:<init>	(Ljava/lang/String;I)V
    //   89: putstatic 83	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   92: new 27	com/google/common/collect/MapMakerInternalMap$EntryFactory$8
    //   95: dup
    //   96: ldc 84
    //   98: bipush 7
    //   100: invokespecial 85	com/google/common/collect/MapMakerInternalMap$EntryFactory$8:<init>	(Ljava/lang/String;I)V
    //   103: putstatic 87	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   106: new 29	com/google/common/collect/MapMakerInternalMap$EntryFactory$9
    //   109: dup
    //   110: ldc 88
    //   112: bipush 8
    //   114: invokespecial 89	com/google/common/collect/MapMakerInternalMap$EntryFactory$9:<init>	(Ljava/lang/String;I)V
    //   117: putstatic 91	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   120: new 7	com/google/common/collect/MapMakerInternalMap$EntryFactory$10
    //   123: dup
    //   124: ldc 92
    //   126: bipush 9
    //   128: invokespecial 93	com/google/common/collect/MapMakerInternalMap$EntryFactory$10:<init>	(Ljava/lang/String;I)V
    //   131: putstatic 95	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   134: new 9	com/google/common/collect/MapMakerInternalMap$EntryFactory$11
    //   137: dup
    //   138: ldc 96
    //   140: bipush 10
    //   142: invokespecial 97	com/google/common/collect/MapMakerInternalMap$EntryFactory$11:<init>	(Ljava/lang/String;I)V
    //   145: putstatic 99	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   148: new 11	com/google/common/collect/MapMakerInternalMap$EntryFactory$12
    //   151: dup
    //   152: ldc 100
    //   154: bipush 11
    //   156: invokespecial 101	com/google/common/collect/MapMakerInternalMap$EntryFactory$12:<init>	(Ljava/lang/String;I)V
    //   159: putstatic 103	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   162: bipush 12
    //   164: anewarray 2	com/google/common/collect/MapMakerInternalMap$EntryFactory
    //   167: astore_0
    //   168: getstatic 59	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   171: astore_1
    //   172: aload_0
    //   173: iconst_0
    //   174: aload_1
    //   175: aastore
    //   176: getstatic 63	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   179: astore_2
    //   180: aload_0
    //   181: iconst_1
    //   182: aload_2
    //   183: aastore
    //   184: getstatic 67	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   187: astore_3
    //   188: aload_0
    //   189: iconst_2
    //   190: aload_3
    //   191: aastore
    //   192: getstatic 71	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   195: astore 4
    //   197: aload_0
    //   198: iconst_3
    //   199: aload 4
    //   201: aastore
    //   202: getstatic 75	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   205: astore 5
    //   207: aload_0
    //   208: iconst_4
    //   209: aload 5
    //   211: aastore
    //   212: getstatic 79	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   215: astore 6
    //   217: aload_0
    //   218: iconst_5
    //   219: aload 6
    //   221: aastore
    //   222: getstatic 83	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   225: astore 7
    //   227: aload_0
    //   228: bipush 6
    //   230: aload 7
    //   232: aastore
    //   233: getstatic 87	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   236: astore 8
    //   238: aload_0
    //   239: bipush 7
    //   241: aload 8
    //   243: aastore
    //   244: getstatic 91	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   247: astore 9
    //   249: aload_0
    //   250: bipush 8
    //   252: aload 9
    //   254: aastore
    //   255: getstatic 95	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   258: astore 10
    //   260: aload_0
    //   261: bipush 9
    //   263: aload 10
    //   265: aastore
    //   266: getstatic 99	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   269: astore 11
    //   271: aload_0
    //   272: bipush 10
    //   274: aload 11
    //   276: aastore
    //   277: getstatic 103	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   280: astore 12
    //   282: aload_0
    //   283: bipush 11
    //   285: aload 12
    //   287: aastore
    //   288: aload_0
    //   289: putstatic 105	com/google/common/collect/MapMakerInternalMap$EntryFactory:$VALUES	[Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   292: iconst_3
    //   293: anewarray 2	com/google/common/collect/MapMakerInternalMap$EntryFactory
    //   296: astore 13
    //   298: iconst_4
    //   299: anewarray 2	com/google/common/collect/MapMakerInternalMap$EntryFactory
    //   302: astore 14
    //   304: getstatic 59	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   307: astore 15
    //   309: aload 14
    //   311: iconst_0
    //   312: aload 15
    //   314: aastore
    //   315: getstatic 63	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   318: astore 16
    //   320: aload 14
    //   322: iconst_1
    //   323: aload 16
    //   325: aastore
    //   326: getstatic 67	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   329: astore 17
    //   331: aload 14
    //   333: iconst_2
    //   334: aload 17
    //   336: aastore
    //   337: getstatic 71	com/google/common/collect/MapMakerInternalMap$EntryFactory:STRONG_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   340: astore 18
    //   342: aload 14
    //   344: iconst_3
    //   345: aload 18
    //   347: aastore
    //   348: aload 13
    //   350: iconst_0
    //   351: aload 14
    //   353: aastore
    //   354: iconst_4
    //   355: anewarray 2	com/google/common/collect/MapMakerInternalMap$EntryFactory
    //   358: astore 19
    //   360: getstatic 75	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   363: astore 20
    //   365: aload 19
    //   367: iconst_0
    //   368: aload 20
    //   370: aastore
    //   371: getstatic 79	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   374: astore 21
    //   376: aload 19
    //   378: iconst_1
    //   379: aload 21
    //   381: aastore
    //   382: getstatic 83	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   385: astore 22
    //   387: aload 19
    //   389: iconst_2
    //   390: aload 22
    //   392: aastore
    //   393: getstatic 87	com/google/common/collect/MapMakerInternalMap$EntryFactory:SOFT_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   396: astore 23
    //   398: aload 19
    //   400: iconst_3
    //   401: aload 23
    //   403: aastore
    //   404: aload 13
    //   406: iconst_1
    //   407: aload 19
    //   409: aastore
    //   410: iconst_4
    //   411: anewarray 2	com/google/common/collect/MapMakerInternalMap$EntryFactory
    //   414: astore 24
    //   416: getstatic 91	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   419: astore 25
    //   421: aload 24
    //   423: iconst_0
    //   424: aload 25
    //   426: aastore
    //   427: getstatic 95	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EXPIRABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   430: astore 26
    //   432: aload 24
    //   434: iconst_1
    //   435: aload 26
    //   437: aastore
    //   438: getstatic 99	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   441: astore 27
    //   443: aload 24
    //   445: iconst_2
    //   446: aload 27
    //   448: aastore
    //   449: getstatic 103	com/google/common/collect/MapMakerInternalMap$EntryFactory:WEAK_EXPIRABLE_EVICTABLE	Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   452: astore 28
    //   454: aload 24
    //   456: iconst_3
    //   457: aload 28
    //   459: aastore
    //   460: aload 13
    //   462: iconst_2
    //   463: aload 24
    //   465: aastore
    //   466: aload 13
    //   468: putstatic 107	com/google/common/collect/MapMakerInternalMap$EntryFactory:factories	[[Lcom/google/common/collect/MapMakerInternalMap$EntryFactory;
    //   471: return
  }

  static EntryFactory getFactory(MapMakerInternalMap.Strength paramStrength, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = 0;
    if (paramBoolean1);
    for (int j = 1; ; j = 0)
    {
      if (paramBoolean2)
        i = 2;
      int k = j | i;
      EntryFactory[][] arrayOfEntryFactory = factories;
      int m = paramStrength.ordinal();
      return arrayOfEntryFactory[m][k];
    }
  }

  <K, V> MapMakerInternalMap.ReferenceEntry<K, V> copyEntry(MapMakerInternalMap.Segment<K, V> paramSegment, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry1, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry2)
  {
    Object localObject = paramReferenceEntry1.getKey();
    int i = paramReferenceEntry1.getHash();
    return newEntry(paramSegment, localObject, i, paramReferenceEntry2);
  }

  <K, V> void copyEvictableEntry(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry1, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry2)
  {
    MapMakerInternalMap.connectEvictables(paramReferenceEntry1.getPreviousEvictable(), paramReferenceEntry2);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = paramReferenceEntry1.getNextEvictable();
    MapMakerInternalMap.connectEvictables(paramReferenceEntry2, localReferenceEntry);
    MapMakerInternalMap.nullifyEvictable(paramReferenceEntry1);
  }

  <K, V> void copyExpirableEntry(MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry1, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry2)
  {
    long l = paramReferenceEntry1.getExpirationTime();
    paramReferenceEntry2.setExpirationTime(l);
    MapMakerInternalMap.connectExpirables(paramReferenceEntry1.getPreviousExpirable(), paramReferenceEntry2);
    MapMakerInternalMap.ReferenceEntry localReferenceEntry = paramReferenceEntry1.getNextExpirable();
    MapMakerInternalMap.connectExpirables(paramReferenceEntry2, localReferenceEntry);
    MapMakerInternalMap.nullifyExpirable(paramReferenceEntry1);
  }

  abstract <K, V> MapMakerInternalMap.ReferenceEntry<K, V> newEntry(MapMakerInternalMap.Segment<K, V> paramSegment, K paramK, int paramInt, MapMakerInternalMap.ReferenceEntry<K, V> paramReferenceEntry);
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.common.collect.MapMakerInternalMap.EntryFactory
 * JD-Core Version:    0.6.2
 */