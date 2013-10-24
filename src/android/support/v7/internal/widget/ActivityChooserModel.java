package android.support.v7.internal.widget;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.DataSetObservable;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.text.TextUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class ActivityChooserModel extends DataSetObservable
{
  private static final String LOG_TAG = ActivityChooserModel.class.getSimpleName();
  private static final Map<String, ActivityChooserModel> sDataModelRegistry = new HashMap();
  private static final Object sRegistryLock = new Object();
  private final List<ActivityResolveInfo> mActivities;
  private OnChooseActivityListener mActivityChoserModelPolicy;
  private ActivitySorter mActivitySorter;
  private boolean mCanReadHistoricalData;
  private final Context mContext;
  private final List<HistoricalRecord> mHistoricalRecords;
  private boolean mHistoricalRecordsChanged;
  private final String mHistoryFileName;
  private int mHistoryMaxSize;
  private final Object mInstanceLock;
  private Intent mIntent;
  private boolean mReadShareHistoryCalled;
  private boolean mReloadActivities;

  private boolean addHisoricalRecord(HistoricalRecord paramHistoricalRecord)
  {
    boolean bool1 = this.mHistoricalRecords.add(paramHistoricalRecord);
    if (bool1)
    {
      this.mHistoricalRecordsChanged = true;
      pruneExcessiveHistoricalRecordsIfNeeded();
      persistHistoricalDataIfNeeded();
      boolean bool2 = sortActivitiesIfNeeded();
      notifyChanged();
    }
    return bool1;
  }

  private void ensureConsistentState()
  {
    boolean bool1 = loadActivitiesIfNeeded();
    boolean bool2 = readHistoricalDataIfNeeded();
    int i = bool1 | bool2;
    pruneExcessiveHistoricalRecordsIfNeeded();
    if (i == 0)
      return;
    boolean bool3 = sortActivitiesIfNeeded();
    notifyChanged();
  }

  private void executePersistHistoryAsyncTaskBase()
  {
    PersistHistoryAsyncTask localPersistHistoryAsyncTask = new PersistHistoryAsyncTask(null);
    Object[] arrayOfObject = new Object[2];
    List localList = this.mHistoricalRecords;
    ArrayList localArrayList = new ArrayList(localList);
    arrayOfObject[0] = localArrayList;
    String str = this.mHistoryFileName;
    arrayOfObject[1] = str;
    AsyncTask localAsyncTask = localPersistHistoryAsyncTask.execute(arrayOfObject);
  }

  private void executePersistHistoryAsyncTaskSDK11()
  {
    PersistHistoryAsyncTask localPersistHistoryAsyncTask = new PersistHistoryAsyncTask(null);
    Executor localExecutor = AsyncTask.SERIAL_EXECUTOR;
    Object[] arrayOfObject = new Object[2];
    List localList = this.mHistoricalRecords;
    ArrayList localArrayList = new ArrayList(localList);
    arrayOfObject[0] = localArrayList;
    String str = this.mHistoryFileName;
    arrayOfObject[1] = str;
    AsyncTask localAsyncTask = localPersistHistoryAsyncTask.executeOnExecutor(localExecutor, arrayOfObject);
  }

  private boolean loadActivitiesIfNeeded()
  {
    boolean bool1 = false;
    if ((this.mReloadActivities) && (this.mIntent != null))
    {
      this.mReloadActivities = false;
      this.mActivities.clear();
      PackageManager localPackageManager = this.mContext.getPackageManager();
      Intent localIntent = this.mIntent;
      List localList1 = localPackageManager.queryIntentActivities(localIntent, 0);
      int i = localList1.size();
      int j = 0;
      while (j < i)
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localList1.get(j);
        List localList2 = this.mActivities;
        ActivityResolveInfo localActivityResolveInfo = new ActivityResolveInfo(localResolveInfo);
        boolean bool2 = localList2.add(localActivityResolveInfo);
        j += 1;
      }
      bool1 = true;
    }
    return bool1;
  }

  private void persistHistoricalDataIfNeeded()
  {
    if (!this.mReadShareHistoryCalled)
      throw new IllegalStateException("No preceding call to #readHistoricalData");
    if (!this.mHistoricalRecordsChanged)
      return;
    this.mHistoricalRecordsChanged = false;
    if (TextUtils.isEmpty(this.mHistoryFileName))
      return;
    if (Build.VERSION.SDK_INT >= 11)
    {
      executePersistHistoryAsyncTaskSDK11();
      return;
    }
    executePersistHistoryAsyncTaskBase();
  }

  private void pruneExcessiveHistoricalRecordsIfNeeded()
  {
    int i = this.mHistoricalRecords.size();
    int j = this.mHistoryMaxSize;
    int k = i - j;
    if (k <= 0)
      return;
    this.mHistoricalRecordsChanged = true;
    int m = 0;
    while (true)
    {
      if (m >= k)
        return;
      HistoricalRecord localHistoricalRecord = (HistoricalRecord)this.mHistoricalRecords.remove(0);
      m += 1;
    }
  }

  private boolean readHistoricalDataIfNeeded()
  {
    boolean bool = true;
    if ((this.mCanReadHistoricalData) && (this.mHistoricalRecordsChanged) && (!TextUtils.isEmpty(this.mHistoryFileName)))
    {
      this.mCanReadHistoricalData = false;
      this.mReadShareHistoryCalled = true;
      readHistoricalDataImpl();
    }
    while (true)
    {
      return bool;
      bool = false;
    }
  }

  // ERROR //
  private void readHistoricalDataImpl()
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore_1
    //   2: aload_0
    //   3: getfield 77	android/support/v7/internal/widget/ActivityChooserModel:mContext	Landroid/content/Context;
    //   6: astore_2
    //   7: aload_0
    //   8: getfield 82	android/support/v7/internal/widget/ActivityChooserModel:mHistoryFileName	Ljava/lang/String;
    //   11: astore_3
    //   12: aload_2
    //   13: aload_3
    //   14: invokevirtual 218	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   17: istore 4
    //   19: iload 4
    //   21: istore_1
    //   22: invokestatic 224	android/util/Xml:newPullParser	()Lorg/xmlpull/v1/XmlPullParser;
    //   25: astore 5
    //   27: aload 5
    //   29: iload_1
    //   30: aconst_null
    //   31: invokeinterface 230 3 0
    //   36: iconst_0
    //   37: istore 6
    //   39: iload 6
    //   41: iconst_1
    //   42: if_icmpeq +24 -> 66
    //   45: iload 6
    //   47: iconst_2
    //   48: if_icmpeq +18 -> 66
    //   51: aload 5
    //   53: invokeinterface 233 1 0
    //   58: istore 6
    //   60: goto -21 -> 39
    //   63: astore 7
    //   65: return
    //   66: aload 5
    //   68: invokeinterface 236 1 0
    //   73: astore 8
    //   75: ldc 238
    //   77: aload 8
    //   79: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   82: ifne +76 -> 158
    //   85: new 212	org/xmlpull/v1/XmlPullParserException
    //   88: dup
    //   89: ldc 245
    //   91: invokespecial 246	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   94: athrow
    //   95: astore 9
    //   97: getstatic 61	android/support/v7/internal/widget/ActivityChooserModel:LOG_TAG	Ljava/lang/String;
    //   100: astore 10
    //   102: new 248	java/lang/StringBuilder
    //   105: dup
    //   106: invokespecial 249	java/lang/StringBuilder:<init>	()V
    //   109: ldc 251
    //   111: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: astore 11
    //   116: aload_0
    //   117: getfield 82	android/support/v7/internal/widget/ActivityChooserModel:mHistoryFileName	Ljava/lang/String;
    //   120: astore 12
    //   122: aload 11
    //   124: aload 12
    //   126: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: invokevirtual 258	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   132: astore 13
    //   134: aload 10
    //   136: aload 13
    //   138: aload 9
    //   140: invokestatic 264	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   143: istore 14
    //   145: iload_1
    //   146: ifne +4 -> 150
    //   149: return
    //   150: iload_1
    //   151: invokevirtual 269	java/io/FileInputStream:close	()V
    //   154: return
    //   155: astore 15
    //   157: return
    //   158: aload_0
    //   159: getfield 90	android/support/v7/internal/widget/ActivityChooserModel:mHistoricalRecords	Ljava/util/List;
    //   162: astore 16
    //   164: aload 16
    //   166: invokeinterface 151 1 0
    //   171: aload 5
    //   173: invokeinterface 233 1 0
    //   178: istore 4
    //   180: iload 4
    //   182: istore 6
    //   184: iload 6
    //   186: iconst_1
    //   187: if_icmpne +16 -> 203
    //   190: iload_1
    //   191: ifne +4 -> 195
    //   194: return
    //   195: iload_1
    //   196: invokevirtual 269	java/io/FileInputStream:close	()V
    //   199: return
    //   200: astore 17
    //   202: return
    //   203: iload 6
    //   205: iconst_3
    //   206: if_icmpeq -35 -> 171
    //   209: iload 6
    //   211: iconst_4
    //   212: if_icmpeq -41 -> 171
    //   215: aload 5
    //   217: invokeinterface 236 1 0
    //   222: astore 18
    //   224: ldc_w 271
    //   227: aload 18
    //   229: invokevirtual 243	java/lang/String:equals	(Ljava/lang/Object;)Z
    //   232: ifne +77 -> 309
    //   235: new 212	org/xmlpull/v1/XmlPullParserException
    //   238: dup
    //   239: ldc_w 273
    //   242: invokespecial 246	org/xmlpull/v1/XmlPullParserException:<init>	(Ljava/lang/String;)V
    //   245: athrow
    //   246: astore 19
    //   248: getstatic 61	android/support/v7/internal/widget/ActivityChooserModel:LOG_TAG	Ljava/lang/String;
    //   251: astore 20
    //   253: new 248	java/lang/StringBuilder
    //   256: dup
    //   257: invokespecial 249	java/lang/StringBuilder:<init>	()V
    //   260: ldc 251
    //   262: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   265: astore 21
    //   267: aload_0
    //   268: getfield 82	android/support/v7/internal/widget/ActivityChooserModel:mHistoryFileName	Ljava/lang/String;
    //   271: astore 22
    //   273: aload 21
    //   275: aload 22
    //   277: invokevirtual 255	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   280: invokevirtual 258	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   283: astore 23
    //   285: aload 20
    //   287: aload 23
    //   289: aload 19
    //   291: invokestatic 264	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   294: istore 24
    //   296: iload_1
    //   297: ifne +4 -> 301
    //   300: return
    //   301: iload_1
    //   302: invokevirtual 269	java/io/FileInputStream:close	()V
    //   305: return
    //   306: astore 25
    //   308: return
    //   309: aconst_null
    //   310: astore 26
    //   312: aload 5
    //   314: aload 26
    //   316: ldc_w 275
    //   319: invokeinterface 279 3 0
    //   324: astore 27
    //   326: aload 5
    //   328: aconst_null
    //   329: ldc_w 281
    //   332: invokeinterface 279 3 0
    //   337: invokestatic 287	java/lang/Long:parseLong	(Ljava/lang/String;)J
    //   340: lstore 28
    //   342: aload 5
    //   344: aconst_null
    //   345: ldc_w 289
    //   348: invokeinterface 279 3 0
    //   353: invokestatic 295	java/lang/Float:parseFloat	(Ljava/lang/String;)F
    //   356: fstore 30
    //   358: new 14	android/support/v7/internal/widget/ActivityChooserModel$HistoricalRecord
    //   361: dup
    //   362: aload 27
    //   364: lload 28
    //   366: fload 30
    //   368: invokespecial 298	android/support/v7/internal/widget/ActivityChooserModel$HistoricalRecord:<init>	(Ljava/lang/String;JF)V
    //   371: astore 31
    //   373: aload 16
    //   375: aload 31
    //   377: invokeinterface 96 2 0
    //   382: istore 32
    //   384: goto -213 -> 171
    //   387: astore 33
    //   389: iload_1
    //   390: ifeq +7 -> 397
    //   393: iload_1
    //   394: invokevirtual 269	java/io/FileInputStream:close	()V
    //   397: aload 33
    //   399: athrow
    //   400: astore 34
    //   402: goto -5 -> 397
    //
    // Exception table:
    //   from	to	target	type
    //   2	19	63	java/io/FileNotFoundException
    //   22	95	95	org/xmlpull/v1/XmlPullParserException
    //   158	180	95	org/xmlpull/v1/XmlPullParserException
    //   215	246	95	org/xmlpull/v1/XmlPullParserException
    //   312	384	95	org/xmlpull/v1/XmlPullParserException
    //   150	154	155	java/io/IOException
    //   195	199	200	java/io/IOException
    //   22	95	246	java/io/IOException
    //   158	180	246	java/io/IOException
    //   215	246	246	java/io/IOException
    //   312	384	246	java/io/IOException
    //   301	305	306	java/io/IOException
    //   22	95	387	finally
    //   97	145	387	finally
    //   158	180	387	finally
    //   215	246	387	finally
    //   248	296	387	finally
    //   312	384	387	finally
    //   393	397	400	java/io/IOException
  }

  private boolean sortActivitiesIfNeeded()
  {
    if ((this.mActivitySorter != null) && (this.mIntent != null) && (!this.mActivities.isEmpty()) && (!this.mHistoricalRecords.isEmpty()))
    {
      ActivitySorter localActivitySorter = this.mActivitySorter;
      Intent localIntent = this.mIntent;
      List localList1 = this.mActivities;
      List localList2 = Collections.unmodifiableList(this.mHistoricalRecords);
      localActivitySorter.sort(localIntent, localList1, localList2);
    }
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public Intent chooseActivity(int paramInt)
  {
    synchronized (this.mInstanceLock)
    {
      if (this.mIntent == null);
      ComponentName localComponentName;
      for (Intent localIntent1 = null; ; localIntent1 = null)
      {
        return localIntent1;
        ensureConsistentState();
        ActivityResolveInfo localActivityResolveInfo = (ActivityResolveInfo)this.mActivities.get(paramInt);
        String str1 = localActivityResolveInfo.resolveInfo.activityInfo.packageName;
        String str2 = localActivityResolveInfo.resolveInfo.activityInfo.name;
        localComponentName = new ComponentName(str1, str2);
        Intent localIntent2 = this.mIntent;
        localIntent1 = new Intent(localIntent2);
        Intent localIntent3 = localIntent1.setComponent(localComponentName);
        if (this.mActivityChoserModelPolicy == null)
          break;
        Intent localIntent4 = new Intent(localIntent1);
        if (!this.mActivityChoserModelPolicy.onChooseActivity(this, localIntent4))
          break;
      }
      long l = System.currentTimeMillis();
      HistoricalRecord localHistoricalRecord = new HistoricalRecord(localComponentName, l, 1.0F);
      boolean bool = addHisoricalRecord(localHistoricalRecord);
    }
  }

  public ResolveInfo getActivity(int paramInt)
  {
    synchronized (this.mInstanceLock)
    {
      ensureConsistentState();
      ResolveInfo localResolveInfo = ((ActivityResolveInfo)this.mActivities.get(paramInt)).resolveInfo;
      return localResolveInfo;
    }
  }

  public int getActivityCount()
  {
    synchronized (this.mInstanceLock)
    {
      ensureConsistentState();
      int i = this.mActivities.size();
      return i;
    }
  }

  public int getActivityIndex(ResolveInfo paramResolveInfo)
  {
    synchronized (this.mInstanceLock)
    {
      ensureConsistentState();
      List localList = this.mActivities;
      int i = localList.size();
      int j = 0;
      while (j < i)
      {
        if (((ActivityResolveInfo)localList.get(j)).resolveInfo == paramResolveInfo)
          return j;
        j += 1;
      }
      j = -1;
    }
  }

  public ResolveInfo getDefaultActivity()
  {
    synchronized (this.mInstanceLock)
    {
      ensureConsistentState();
      if (!this.mActivities.isEmpty())
      {
        localResolveInfo = ((ActivityResolveInfo)this.mActivities.get(0)).resolveInfo;
        return localResolveInfo;
      }
      ResolveInfo localResolveInfo = null;
    }
  }

  public int getHistorySize()
  {
    synchronized (this.mInstanceLock)
    {
      ensureConsistentState();
      int i = this.mHistoricalRecords.size();
      return i;
    }
  }

  public void setDefaultActivity(int paramInt)
  {
    synchronized (this.mInstanceLock)
    {
      ensureConsistentState();
      ActivityResolveInfo localActivityResolveInfo1 = (ActivityResolveInfo)this.mActivities.get(paramInt);
      ActivityResolveInfo localActivityResolveInfo2 = (ActivityResolveInfo)this.mActivities.get(0);
      if (localActivityResolveInfo2 != null)
      {
        float f1 = localActivityResolveInfo2.weight;
        float f2 = localActivityResolveInfo1.weight;
        f3 = f1 - f2 + 5.0F;
        String str1 = localActivityResolveInfo1.resolveInfo.activityInfo.packageName;
        String str2 = localActivityResolveInfo1.resolveInfo.activityInfo.name;
        ComponentName localComponentName = new ComponentName(str1, str2);
        long l = System.currentTimeMillis();
        HistoricalRecord localHistoricalRecord = new HistoricalRecord(localComponentName, l, f3);
        boolean bool = addHisoricalRecord(localHistoricalRecord);
        return;
      }
      float f3 = 1.0F;
    }
  }

  private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
  {
    private PersistHistoryAsyncTask()
    {
    }

    // ERROR //
    public Void doInBackground(Object[] paramArrayOfObject)
    {
      // Byte code:
      //   0: aload_1
      //   1: iconst_0
      //   2: aaload
      //   3: checkcast 36	java/util/List
      //   6: astore_2
      //   7: aload_1
      //   8: iconst_1
      //   9: aaload
      //   10: checkcast 38	java/lang/String
      //   13: astore_3
      //   14: aload_0
      //   15: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   18: invokestatic 42	android/support/v7/internal/widget/ActivityChooserModel:access$200	(Landroid/support/v7/internal/widget/ActivityChooserModel;)Landroid/content/Context;
      //   21: aload_3
      //   22: iconst_0
      //   23: invokevirtual 48	android/content/Context:openFileOutput	(Ljava/lang/String;I)Ljava/io/FileOutputStream;
      //   26: astore 4
      //   28: aload 4
      //   30: astore 5
      //   32: invokestatic 54	android/util/Xml:newSerializer	()Lorg/xmlpull/v1/XmlSerializer;
      //   35: astore 6
      //   37: aconst_null
      //   38: astore 7
      //   40: aload 6
      //   42: aload 5
      //   44: aload 7
      //   46: invokeinterface 60 3 0
      //   51: iconst_1
      //   52: invokestatic 66	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
      //   55: astore 8
      //   57: aload 6
      //   59: ldc 68
      //   61: aload 8
      //   63: invokeinterface 72 3 0
      //   68: aload 6
      //   70: aconst_null
      //   71: ldc 74
      //   73: invokeinterface 78 3 0
      //   78: astore 9
      //   80: aload_2
      //   81: invokeinterface 82 1 0
      //   86: istore 10
      //   88: iconst_0
      //   89: istore 11
      //   91: iload 11
      //   93: iload 10
      //   95: if_icmpge +165 -> 260
      //   98: aload_2
      //   99: iconst_0
      //   100: invokeinterface 86 2 0
      //   105: checkcast 88	android/support/v7/internal/widget/ActivityChooserModel$HistoricalRecord
      //   108: astore 12
      //   110: aload 6
      //   112: aconst_null
      //   113: ldc 90
      //   115: invokeinterface 78 3 0
      //   120: astore 13
      //   122: aload 12
      //   124: getfield 94	android/support/v7/internal/widget/ActivityChooserModel$HistoricalRecord:activity	Landroid/content/ComponentName;
      //   127: invokevirtual 100	android/content/ComponentName:flattenToString	()Ljava/lang/String;
      //   130: astore 14
      //   132: aload 6
      //   134: aconst_null
      //   135: ldc 101
      //   137: aload 14
      //   139: invokeinterface 105 4 0
      //   144: astore 15
      //   146: aload 12
      //   148: getfield 109	android/support/v7/internal/widget/ActivityChooserModel$HistoricalRecord:time	J
      //   151: invokestatic 112	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   154: astore 16
      //   156: aload 6
      //   158: aconst_null
      //   159: ldc 113
      //   161: aload 16
      //   163: invokeinterface 105 4 0
      //   168: astore 17
      //   170: aload 12
      //   172: getfield 117	android/support/v7/internal/widget/ActivityChooserModel$HistoricalRecord:weight	F
      //   175: invokestatic 120	java/lang/String:valueOf	(F)Ljava/lang/String;
      //   178: astore 18
      //   180: aload 6
      //   182: aconst_null
      //   183: ldc 121
      //   185: aload 18
      //   187: invokeinterface 105 4 0
      //   192: astore 19
      //   194: aload 6
      //   196: aconst_null
      //   197: ldc 90
      //   199: invokeinterface 124 3 0
      //   204: astore 20
      //   206: iload 11
      //   208: iconst_1
      //   209: iadd
      //   210: istore 11
      //   212: goto -121 -> 91
      //   215: astore 21
      //   217: invokestatic 127	android/support/v7/internal/widget/ActivityChooserModel:access$300	()Ljava/lang/String;
      //   220: astore 22
      //   222: new 129	java/lang/StringBuilder
      //   225: dup
      //   226: invokespecial 130	java/lang/StringBuilder:<init>	()V
      //   229: ldc 132
      //   231: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   234: aload_3
      //   235: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   238: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   241: astore 23
      //   243: aload 22
      //   245: aload 23
      //   247: aload 21
      //   249: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   252: istore 24
      //   254: aconst_null
      //   255: astore 25
      //   257: aload 25
      //   259: areturn
      //   260: aconst_null
      //   261: astore 7
      //   263: aload 6
      //   265: aload 7
      //   267: ldc 74
      //   269: invokeinterface 124 3 0
      //   274: astore 26
      //   276: aload 6
      //   278: invokeinterface 148 1 0
      //   283: aload_0
      //   284: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   287: iconst_1
      //   288: invokestatic 152	android/support/v7/internal/widget/ActivityChooserModel:access$502	(Landroid/support/v7/internal/widget/ActivityChooserModel;Z)Z
      //   291: istore 27
      //   293: aload 5
      //   295: ifnull +8 -> 303
      //   298: aload 5
      //   300: invokevirtual 157	java/io/FileOutputStream:close	()V
      //   303: aconst_null
      //   304: astore 25
      //   306: goto -49 -> 257
      //   309: astore 28
      //   311: invokestatic 127	android/support/v7/internal/widget/ActivityChooserModel:access$300	()Ljava/lang/String;
      //   314: astore 29
      //   316: new 129	java/lang/StringBuilder
      //   319: dup
      //   320: invokespecial 130	java/lang/StringBuilder:<init>	()V
      //   323: ldc 132
      //   325: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   328: astore 30
      //   330: aload_0
      //   331: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   334: invokestatic 161	android/support/v7/internal/widget/ActivityChooserModel:access$400	(Landroid/support/v7/internal/widget/ActivityChooserModel;)Ljava/lang/String;
      //   337: astore 31
      //   339: aload 30
      //   341: aload 31
      //   343: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   346: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   349: astore 32
      //   351: aload 29
      //   353: aload 32
      //   355: aload 28
      //   357: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   360: istore 33
      //   362: aload_0
      //   363: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   366: iconst_1
      //   367: invokestatic 152	android/support/v7/internal/widget/ActivityChooserModel:access$502	(Landroid/support/v7/internal/widget/ActivityChooserModel;Z)Z
      //   370: istore 34
      //   372: aload 5
      //   374: ifnull -71 -> 303
      //   377: aload 5
      //   379: invokevirtual 157	java/io/FileOutputStream:close	()V
      //   382: goto -79 -> 303
      //   385: astore 35
      //   387: goto -84 -> 303
      //   390: astore 36
      //   392: invokestatic 127	android/support/v7/internal/widget/ActivityChooserModel:access$300	()Ljava/lang/String;
      //   395: astore 37
      //   397: new 129	java/lang/StringBuilder
      //   400: dup
      //   401: invokespecial 130	java/lang/StringBuilder:<init>	()V
      //   404: ldc 132
      //   406: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   409: astore 38
      //   411: aload_0
      //   412: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   415: invokestatic 161	android/support/v7/internal/widget/ActivityChooserModel:access$400	(Landroid/support/v7/internal/widget/ActivityChooserModel;)Ljava/lang/String;
      //   418: astore 39
      //   420: aload 38
      //   422: aload 39
      //   424: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   427: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   430: astore 40
      //   432: aload 37
      //   434: aload 40
      //   436: aload 36
      //   438: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   441: istore 41
      //   443: aload_0
      //   444: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   447: iconst_1
      //   448: invokestatic 152	android/support/v7/internal/widget/ActivityChooserModel:access$502	(Landroid/support/v7/internal/widget/ActivityChooserModel;Z)Z
      //   451: istore 42
      //   453: aload 5
      //   455: ifnull -152 -> 303
      //   458: aload 5
      //   460: invokevirtual 157	java/io/FileOutputStream:close	()V
      //   463: goto -160 -> 303
      //   466: astore 43
      //   468: goto -165 -> 303
      //   471: astore 44
      //   473: invokestatic 127	android/support/v7/internal/widget/ActivityChooserModel:access$300	()Ljava/lang/String;
      //   476: astore 45
      //   478: new 129	java/lang/StringBuilder
      //   481: dup
      //   482: invokespecial 130	java/lang/StringBuilder:<init>	()V
      //   485: ldc 132
      //   487: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   490: astore 46
      //   492: aload_0
      //   493: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   496: invokestatic 161	android/support/v7/internal/widget/ActivityChooserModel:access$400	(Landroid/support/v7/internal/widget/ActivityChooserModel;)Ljava/lang/String;
      //   499: astore 47
      //   501: aload 46
      //   503: aload 47
      //   505: invokevirtual 136	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   508: invokevirtual 139	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   511: astore 48
      //   513: aload 45
      //   515: aload 48
      //   517: aload 44
      //   519: invokestatic 145	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
      //   522: istore 49
      //   524: aload_0
      //   525: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   528: iconst_1
      //   529: invokestatic 152	android/support/v7/internal/widget/ActivityChooserModel:access$502	(Landroid/support/v7/internal/widget/ActivityChooserModel;Z)Z
      //   532: istore 50
      //   534: aload 5
      //   536: ifnull -233 -> 303
      //   539: aload 5
      //   541: invokevirtual 157	java/io/FileOutputStream:close	()V
      //   544: goto -241 -> 303
      //   547: astore 51
      //   549: goto -246 -> 303
      //   552: astore 52
      //   554: aload_0
      //   555: getfield 15	android/support/v7/internal/widget/ActivityChooserModel$PersistHistoryAsyncTask:this$0	Landroid/support/v7/internal/widget/ActivityChooserModel;
      //   558: iconst_1
      //   559: invokestatic 152	android/support/v7/internal/widget/ActivityChooserModel:access$502	(Landroid/support/v7/internal/widget/ActivityChooserModel;Z)Z
      //   562: istore 53
      //   564: aload 5
      //   566: ifnull +8 -> 574
      //   569: aload 5
      //   571: invokevirtual 157	java/io/FileOutputStream:close	()V
      //   574: aload 52
      //   576: athrow
      //   577: astore 54
      //   579: goto -276 -> 303
      //   582: astore 55
      //   584: goto -10 -> 574
      //
      // Exception table:
      //   from	to	target	type
      //   14	28	215	java/io/FileNotFoundException
      //   40	206	309	java/lang/IllegalArgumentException
      //   263	283	309	java/lang/IllegalArgumentException
      //   377	382	385	java/io/IOException
      //   40	206	390	java/lang/IllegalStateException
      //   263	283	390	java/lang/IllegalStateException
      //   458	463	466	java/io/IOException
      //   40	206	471	java/io/IOException
      //   263	283	471	java/io/IOException
      //   539	544	547	java/io/IOException
      //   40	206	552	finally
      //   263	283	552	finally
      //   311	362	552	finally
      //   392	443	552	finally
      //   473	524	552	finally
      //   298	303	577	java/io/IOException
      //   569	574	582	java/io/IOException
    }
  }

  public final class ActivityResolveInfo
    implements Comparable<ActivityResolveInfo>
  {
    public final ResolveInfo resolveInfo;
    public float weight;

    public ActivityResolveInfo(ResolveInfo arg2)
    {
      Object localObject;
      this.resolveInfo = localObject;
    }

    public int compareTo(ActivityResolveInfo paramActivityResolveInfo)
    {
      int i = Float.floatToIntBits(paramActivityResolveInfo.weight);
      int j = Float.floatToIntBits(this.weight);
      return i - j;
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject);
      while (true)
      {
        return bool;
        if (paramObject == null)
        {
          bool = false;
        }
        else
        {
          Class localClass1 = getClass();
          Class localClass2 = paramObject.getClass();
          if (localClass1 != localClass2)
          {
            bool = false;
          }
          else
          {
            ActivityResolveInfo localActivityResolveInfo = (ActivityResolveInfo)paramObject;
            int i = Float.floatToIntBits(this.weight);
            int j = Float.floatToIntBits(localActivityResolveInfo.weight);
            if (i != j)
              bool = false;
          }
        }
      }
    }

    public int hashCode()
    {
      return Float.floatToIntBits(this.weight) + 31;
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
      StringBuilder localStringBuilder3 = localStringBuilder1.append("resolveInfo:");
      String str = this.resolveInfo.toString();
      StringBuilder localStringBuilder4 = localStringBuilder3.append(str);
      StringBuilder localStringBuilder5 = localStringBuilder1.append("; weight:");
      double d = this.weight;
      BigDecimal localBigDecimal = new BigDecimal(d);
      StringBuilder localStringBuilder6 = localStringBuilder5.append(localBigDecimal);
      StringBuilder localStringBuilder7 = localStringBuilder1.append("]");
      return localStringBuilder1.toString();
    }
  }

  public static final class HistoricalRecord
  {
    public final ComponentName activity;
    public final long time;
    public final float weight;

    public HistoricalRecord(ComponentName paramComponentName, long paramLong, float paramFloat)
    {
      this.activity = paramComponentName;
      this.time = paramLong;
      this.weight = paramFloat;
    }

    public HistoricalRecord(String paramString, long paramLong, float paramFloat)
    {
      this(localComponentName, paramLong, paramFloat);
    }

    public boolean equals(Object paramObject)
    {
      boolean bool = true;
      if (this == paramObject);
      while (true)
      {
        return bool;
        if (paramObject == null)
        {
          bool = false;
        }
        else
        {
          Class localClass1 = getClass();
          Class localClass2 = paramObject.getClass();
          if (localClass1 != localClass2)
          {
            bool = false;
          }
          else
          {
            HistoricalRecord localHistoricalRecord = (HistoricalRecord)paramObject;
            if (this.activity == null)
            {
              if (localHistoricalRecord.activity != null)
                bool = false;
            }
            else
            {
              ComponentName localComponentName1 = this.activity;
              ComponentName localComponentName2 = localHistoricalRecord.activity;
              if (!localComponentName1.equals(localComponentName2))
              {
                bool = false;
              }
              else
              {
                long l1 = this.time;
                long l2 = localHistoricalRecord.time;
                if (l1 != l2)
                {
                  bool = false;
                }
                else
                {
                  int i = Float.floatToIntBits(this.weight);
                  int j = Float.floatToIntBits(localHistoricalRecord.weight);
                  if (i != j)
                    bool = false;
                }
              }
            }
          }
        }
      }
    }

    public int hashCode()
    {
      if (this.activity == null);
      for (int i = 0; ; i = this.activity.hashCode())
      {
        int j = (i + 31) * 31;
        long l1 = this.time;
        long l2 = this.time >>> 32;
        int k = (int)(l1 ^ l2);
        int m = (j + k) * 31;
        int n = Float.floatToIntBits(this.weight);
        return m + n;
      }
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder();
      StringBuilder localStringBuilder2 = localStringBuilder1.append("[");
      StringBuilder localStringBuilder3 = localStringBuilder1.append("; activity:");
      ComponentName localComponentName = this.activity;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(localComponentName);
      StringBuilder localStringBuilder5 = localStringBuilder1.append("; time:");
      long l = this.time;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(l);
      StringBuilder localStringBuilder7 = localStringBuilder1.append("; weight:");
      double d = this.weight;
      BigDecimal localBigDecimal = new BigDecimal(d);
      StringBuilder localStringBuilder8 = localStringBuilder7.append(localBigDecimal);
      StringBuilder localStringBuilder9 = localStringBuilder1.append("]");
      return localStringBuilder1.toString();
    }
  }

  public static abstract interface OnChooseActivityListener
  {
    public abstract boolean onChooseActivity(ActivityChooserModel paramActivityChooserModel, Intent paramIntent);
  }

  public static abstract interface ActivitySorter
  {
    public abstract void sort(Intent paramIntent, List<ActivityChooserModel.ActivityResolveInfo> paramList, List<ActivityChooserModel.HistoricalRecord> paramList1);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActivityChooserModel
 * JD-Core Version:    0.6.2
 */