package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocalBroadcastManager
{
  private static LocalBroadcastManager mInstance;
  private static final Object mLock = new Object();
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
  private final Context mAppContext;
  private final Handler mHandler;
  private final ArrayList<BroadcastRecord> mPendingBroadcasts;
  private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers;

  private LocalBroadcastManager(Context paramContext)
  {
    HashMap localHashMap1 = new HashMap();
    this.mReceivers = localHashMap1;
    HashMap localHashMap2 = new HashMap();
    this.mActions = localHashMap2;
    ArrayList localArrayList = new ArrayList();
    this.mPendingBroadcasts = localArrayList;
    this.mAppContext = paramContext;
    Looper localLooper = paramContext.getMainLooper();
    Handler local1 = new Handler(localLooper)
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        switch (paramAnonymousMessage.what)
        {
        default:
          super.handleMessage(paramAnonymousMessage);
          return;
        case 1:
        }
        LocalBroadcastManager.this.executePendingBroadcasts();
      }
    };
    this.mHandler = local1;
  }

  private void executePendingBroadcasts()
  {
    while (true)
    {
      int j;
      synchronized (this.mReceivers)
      {
        int i = this.mPendingBroadcasts.size();
        if (i <= 0)
          return;
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        Object[] arrayOfObject = this.mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
        this.mPendingBroadcasts.clear();
        j = 0;
        int k = arrayOfBroadcastRecord.length;
        if (j >= k)
          continue;
        BroadcastRecord localBroadcastRecord = arrayOfBroadcastRecord[j];
        int m = 0;
        int n = localBroadcastRecord.receivers.size();
        if (m < n)
        {
          BroadcastReceiver localBroadcastReceiver = ((ReceiverRecord)localBroadcastRecord.receivers.get(m)).receiver;
          Context localContext = this.mAppContext;
          Intent localIntent = localBroadcastRecord.intent;
          localBroadcastReceiver.onReceive(localContext, localIntent);
          m += 1;
        }
      }
      j += 1;
    }
  }

  public static LocalBroadcastManager getInstance(Context paramContext)
  {
    synchronized (mLock)
    {
      if (mInstance == null)
      {
        Context localContext = paramContext.getApplicationContext();
        mInstance = new LocalBroadcastManager(localContext);
      }
      LocalBroadcastManager localLocalBroadcastManager = mInstance;
      return localLocalBroadcastManager;
    }
  }

  public void registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    synchronized (this.mReceivers)
    {
      ReceiverRecord localReceiverRecord = new ReceiverRecord(paramIntentFilter, paramBroadcastReceiver);
      ArrayList localArrayList1 = (ArrayList)this.mReceivers.get(paramBroadcastReceiver);
      if (localArrayList1 == null)
      {
        localArrayList1 = new ArrayList(1);
        Object localObject1 = this.mReceivers.put(paramBroadcastReceiver, localArrayList1);
      }
      boolean bool1 = localArrayList1.add(paramIntentFilter);
      int i = 0;
      while (true)
      {
        int j = paramIntentFilter.countActions();
        if (i >= j)
          break;
        String str = paramIntentFilter.getAction(i);
        ArrayList localArrayList2 = (ArrayList)this.mActions.get(str);
        if (localArrayList2 == null)
        {
          localArrayList2 = new ArrayList(1);
          Object localObject2 = this.mActions.put(str, localArrayList2);
        }
        boolean bool2 = localArrayList2.add(localReceiverRecord);
        i += 1;
      }
      return;
    }
  }

  public boolean sendBroadcast(Intent paramIntent)
  {
    ArrayList localArrayList2;
    int m;
    while (true)
    {
      int i;
      int i3;
      synchronized (this.mReceivers)
      {
        String str1 = paramIntent.getAction();
        ContentResolver localContentResolver = this.mAppContext.getContentResolver();
        String str2 = paramIntent.resolveTypeIfNeeded(localContentResolver);
        Uri localUri = paramIntent.getData();
        String str3 = paramIntent.getScheme();
        Set localSet = paramIntent.getCategories();
        ReceiverRecord localReceiverRecord;
        if ((paramIntent.getFlags() & 0x8) != 0)
        {
          i = 1;
          if (i != 0)
          {
            StringBuilder localStringBuilder1 = new StringBuilder();
            String str4 = "Resolving type ";
            StringBuilder localStringBuilder2 = localStringBuilder1.append(str4).append(str2);
            String str5 = " scheme ";
            StringBuilder localStringBuilder3 = localStringBuilder2.append(str5).append(str3);
            String str6 = " of intent ";
            StringBuilder localStringBuilder4 = localStringBuilder3.append(str6);
            Intent localIntent1 = paramIntent;
            String str7 = localIntent1;
            int j = Log.v("LocalBroadcastManager", str7);
          }
          HashMap localHashMap2 = this.mActions;
          String str8 = paramIntent.getAction();
          ArrayList localArrayList1 = (ArrayList)localHashMap2.get(str8);
          if (localArrayList1 == null)
            break label679;
          if (i != 0)
          {
            StringBuilder localStringBuilder5 = new StringBuilder();
            String str9 = "Action list: ";
            String str10 = str9 + localArrayList1;
            int k = Log.v("LocalBroadcastManager", str10);
          }
          localArrayList2 = null;
          m = 0;
          int n = localArrayList1.size();
          if (m >= n)
            break;
          localReceiverRecord = (ReceiverRecord)localArrayList1.get(m);
          if (i != 0)
          {
            StringBuilder localStringBuilder6 = new StringBuilder();
            String str11 = "Matching against filter ";
            StringBuilder localStringBuilder7 = localStringBuilder6.append(str11);
            IntentFilter localIntentFilter = localReceiverRecord.filter;
            String str12 = localIntentFilter;
            int i1 = Log.v("LocalBroadcastManager", str12);
          }
          if (localReceiverRecord.broadcasting)
          {
            if (i != 0)
              int i2 = Log.v("LocalBroadcastManager", "  Filter's target already added");
            m += 1;
            continue;
          }
        }
        else
        {
          i = 0;
          continue;
        }
        i3 = localReceiverRecord.filter.match(str1, str2, str3, localUri, localSet, "LocalBroadcastManager");
        if (i3 >= 0)
        {
          if (i != 0)
          {
            StringBuilder localStringBuilder8 = new StringBuilder();
            String str13 = "  Filter matched!  match=0x";
            StringBuilder localStringBuilder9 = localStringBuilder8.append(str13);
            String str14 = Integer.toHexString(i3);
            String str15 = str14;
            int i4 = Log.v("LocalBroadcastManager", str15);
          }
          if (localArrayList2 == null)
            localArrayList2 = new ArrayList();
          boolean bool1 = localArrayList2.add(localReceiverRecord);
          localReceiverRecord.broadcasting = true;
        }
      }
      if (i != 0)
      {
        String str16;
        switch (i3)
        {
        default:
          str16 = "unknown reason";
        case -3:
        case -4:
        case -2:
        case -1:
        }
        while (true)
        {
          StringBuilder localStringBuilder10 = new StringBuilder();
          String str17 = "  Filter did not match: ";
          String str18 = str17 + str16;
          int i5 = Log.v("LocalBroadcastManager", str18);
          break;
          str16 = "action";
          continue;
          str16 = "category";
          continue;
          str16 = "data";
          continue;
          str16 = "type";
        }
      }
    }
    boolean bool4;
    if (localArrayList2 != null)
    {
      m = 0;
      while (true)
      {
        int i6 = localArrayList2.size();
        if (m >= i6)
          break;
        ((ReceiverRecord)localArrayList2.get(m)).broadcasting = false;
        m += 1;
      }
      ArrayList localArrayList3 = this.mPendingBroadcasts;
      Intent localIntent2 = paramIntent;
      BroadcastRecord localBroadcastRecord = new BroadcastRecord(localIntent2, localArrayList2);
      boolean bool2 = localArrayList3.add(localBroadcastRecord);
      if (!this.mHandler.hasMessages(1))
        boolean bool3 = this.mHandler.sendEmptyMessage(1);
      bool4 = true;
    }
    while (true)
    {
      return bool4;
      label679: bool4 = false;
    }
  }

  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    synchronized (this.mReceivers)
    {
      ArrayList localArrayList1 = (ArrayList)this.mReceivers.remove(paramBroadcastReceiver);
      if (localArrayList1 == null)
        return;
      int i = 0;
      while (true)
      {
        int j = localArrayList1.size();
        if (i >= j)
          break;
        IntentFilter localIntentFilter = (IntentFilter)localArrayList1.get(i);
        int k = 0;
        while (true)
        {
          int m = localIntentFilter.countActions();
          if (k >= m)
            break;
          String str = localIntentFilter.getAction(k);
          ArrayList localArrayList2 = (ArrayList)this.mActions.get(str);
          if (localArrayList2 != null)
          {
            int n = 0;
            while (true)
            {
              int i1 = localArrayList2.size();
              if (n >= i1)
                break;
              if (((ReceiverRecord)localArrayList2.get(n)).receiver == paramBroadcastReceiver)
              {
                Object localObject1 = localArrayList2.remove(n);
                n += -1;
              }
              n += 1;
            }
            if (localArrayList2.size() <= 0)
              Object localObject2 = this.mActions.remove(str);
          }
          k += 1;
        }
        i += 1;
      }
      return;
    }
  }

  private static class BroadcastRecord
  {
    final Intent intent;
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;

    BroadcastRecord(Intent paramIntent, ArrayList<LocalBroadcastManager.ReceiverRecord> paramArrayList)
    {
      this.intent = paramIntent;
      this.receivers = paramArrayList;
    }
  }

  private static class ReceiverRecord
  {
    boolean broadcasting;
    final IntentFilter filter;
    final BroadcastReceiver receiver;

    ReceiverRecord(IntentFilter paramIntentFilter, BroadcastReceiver paramBroadcastReceiver)
    {
      this.filter = paramIntentFilter;
      this.receiver = paramBroadcastReceiver;
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder(128);
      StringBuilder localStringBuilder2 = localStringBuilder1.append("Receiver{");
      BroadcastReceiver localBroadcastReceiver = this.receiver;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(localBroadcastReceiver);
      StringBuilder localStringBuilder4 = localStringBuilder1.append(" filter=");
      IntentFilter localIntentFilter = this.filter;
      StringBuilder localStringBuilder5 = localStringBuilder1.append(localIntentFilter);
      StringBuilder localStringBuilder6 = localStringBuilder1.append("}");
      return localStringBuilder1.toString();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.LocalBroadcastManager
 * JD-Core Version:    0.6.2
 */