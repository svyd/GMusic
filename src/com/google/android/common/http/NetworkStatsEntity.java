package com.google.android.common.http;

import android.net.TrafficStats;
import android.os.SystemClock;
import android.util.EventLog;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class NetworkStatsEntity extends HttpEntityWrapper
{
  private final long mProcessingStartTime;
  private final long mResponseLatency;
  private final long mStartRx;
  private final long mStartTx;
  private final String mUa;
  private final int mUid;

  public NetworkStatsEntity(HttpEntity paramHttpEntity, String paramString, int paramInt, long paramLong1, long paramLong2, long paramLong3, long paramLong4)
  {
    super(paramHttpEntity);
    this.mUa = paramString;
    this.mUid = paramInt;
    this.mStartTx = paramLong1;
    this.mStartRx = paramLong2;
    this.mResponseLatency = paramLong3;
    this.mProcessingStartTime = paramLong4;
  }

  public InputStream getContent()
    throws IOException
  {
    InputStream localInputStream = super.getContent();
    return new NetworkStatsInputStream(localInputStream);
  }

  private class NetworkStatsInputStream extends FilterInputStream
  {
    public NetworkStatsInputStream(InputStream arg2)
    {
      super();
    }

    public void close()
      throws IOException
    {
      try
      {
        super.close();
        long l1;
        long l2;
        long l3;
        long l4;
        long l5;
        Object[] arrayOfObject1;
        String str1;
        Long localLong1;
        Long localLong2;
        long l6;
        Long localLong3;
        long l7;
        Long localLong4;
        int i;
        return;
      }
      finally
      {
        long l8 = SystemClock.elapsedRealtime();
        long l9 = NetworkStatsEntity.this.mProcessingStartTime;
        long l10 = l8 - l9;
        long l11 = TrafficStats.getUidTxBytes(NetworkStatsEntity.this.mUid);
        long l12 = TrafficStats.getUidRxBytes(NetworkStatsEntity.this.mUid);
        Object[] arrayOfObject2 = new Object[5];
        String str2 = NetworkStatsEntity.this.mUa;
        arrayOfObject2[0] = str2;
        Long localLong5 = Long.valueOf(NetworkStatsEntity.this.mResponseLatency);
        arrayOfObject2[1] = localLong5;
        Long localLong6 = Long.valueOf(l10);
        arrayOfObject2[2] = localLong6;
        long l13 = NetworkStatsEntity.this.mStartTx;
        Long localLong7 = Long.valueOf(l11 - l13);
        arrayOfObject2[3] = localLong7;
        long l14 = NetworkStatsEntity.this.mStartRx;
        Long localLong8 = Long.valueOf(l12 - l14);
        arrayOfObject2[4] = localLong8;
        int j = EventLog.writeEvent(52001, arrayOfObject2);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.http.NetworkStatsEntity
 * JD-Core Version:    0.6.2
 */