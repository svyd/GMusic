package android.support.v4.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

public class NotificationCompat
{
  public static final int FLAG_HIGH_PRIORITY = 128;
  private static final NotificationCompatImpl IMPL = new NotificationCompatImplBase();
  public static final int PRIORITY_DEFAULT = 0;
  public static final int PRIORITY_HIGH = 1;
  public static final int PRIORITY_LOW = 255;
  public static final int PRIORITY_MAX = 2;
  public static final int PRIORITY_MIN = 254;

  static
  {
    if (Build.VERSION.SDK_INT >= 16)
    {
      IMPL = new NotificationCompatImplJellybean();
      return;
    }
    if (Build.VERSION.SDK_INT >= 14)
    {
      IMPL = new NotificationCompatImplIceCreamSandwich();
      return;
    }
    if (Build.VERSION.SDK_INT >= 11)
    {
      IMPL = new NotificationCompatImplHoneycomb();
      return;
    }
  }

  public static class Action
  {
    public PendingIntent actionIntent;
    public int icon;
    public CharSequence title;

    public Action(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
    {
      this.icon = paramInt;
      this.title = paramCharSequence;
      this.actionIntent = paramPendingIntent;
    }
  }

  public static class InboxStyle extends NotificationCompat.Style
  {
    ArrayList<CharSequence> mTexts;

    public InboxStyle()
    {
      ArrayList localArrayList = new ArrayList();
      this.mTexts = localArrayList;
    }

    public InboxStyle(NotificationCompat.Builder paramBuilder)
    {
      ArrayList localArrayList = new ArrayList();
      this.mTexts = localArrayList;
      setBuilder(paramBuilder);
    }

    public InboxStyle addLine(CharSequence paramCharSequence)
    {
      boolean bool = this.mTexts.add(paramCharSequence);
      return this;
    }

    public InboxStyle setBigContentTitle(CharSequence paramCharSequence)
    {
      this.mBigContentTitle = paramCharSequence;
      return this;
    }

    public InboxStyle setSummaryText(CharSequence paramCharSequence)
    {
      this.mSummaryText = paramCharSequence;
      this.mSummaryTextSet = true;
      return this;
    }
  }

  public static class BigTextStyle extends NotificationCompat.Style
  {
    CharSequence mBigText;

    public BigTextStyle()
    {
    }

    public BigTextStyle(NotificationCompat.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }

    public BigTextStyle bigText(CharSequence paramCharSequence)
    {
      this.mBigText = paramCharSequence;
      return this;
    }

    public BigTextStyle setBigContentTitle(CharSequence paramCharSequence)
    {
      this.mBigContentTitle = paramCharSequence;
      return this;
    }

    public BigTextStyle setSummaryText(CharSequence paramCharSequence)
    {
      this.mSummaryText = paramCharSequence;
      this.mSummaryTextSet = true;
      return this;
    }
  }

  public static class BigPictureStyle extends NotificationCompat.Style
  {
    Bitmap mBigLargeIcon;
    boolean mBigLargeIconSet;
    Bitmap mPicture;

    public BigPictureStyle()
    {
    }

    public BigPictureStyle(NotificationCompat.Builder paramBuilder)
    {
      setBuilder(paramBuilder);
    }

    public BigPictureStyle bigLargeIcon(Bitmap paramBitmap)
    {
      this.mBigLargeIcon = paramBitmap;
      this.mBigLargeIconSet = true;
      return this;
    }

    public BigPictureStyle bigPicture(Bitmap paramBitmap)
    {
      this.mPicture = paramBitmap;
      return this;
    }

    public BigPictureStyle setBigContentTitle(CharSequence paramCharSequence)
    {
      this.mBigContentTitle = paramCharSequence;
      return this;
    }

    public BigPictureStyle setSummaryText(CharSequence paramCharSequence)
    {
      this.mSummaryText = paramCharSequence;
      this.mSummaryTextSet = true;
      return this;
    }
  }

  public static abstract class Style
  {
    CharSequence mBigContentTitle;
    NotificationCompat.Builder mBuilder;
    CharSequence mSummaryText;
    boolean mSummaryTextSet = false;

    public Notification build()
    {
      Notification localNotification = null;
      if (this.mBuilder != null)
        localNotification = this.mBuilder.build();
      return localNotification;
    }

    public void setBuilder(NotificationCompat.Builder paramBuilder)
    {
      if (this.mBuilder == paramBuilder)
        return;
      this.mBuilder = paramBuilder;
      if (this.mBuilder == null)
        return;
      NotificationCompat.Builder localBuilder = this.mBuilder.setStyle(this);
    }
  }

  public static class Builder
  {
    ArrayList<NotificationCompat.Action> mActions;
    CharSequence mContentInfo;
    PendingIntent mContentIntent;
    CharSequence mContentText;
    CharSequence mContentTitle;
    Context mContext;
    PendingIntent mFullScreenIntent;
    Bitmap mLargeIcon;
    Notification mNotification;
    int mNumber;
    int mPriority;
    int mProgress;
    boolean mProgressIndeterminate;
    int mProgressMax;
    NotificationCompat.Style mStyle;
    CharSequence mSubText;
    RemoteViews mTickerView;
    boolean mUseChronometer;

    public Builder(Context paramContext)
    {
      ArrayList localArrayList = new ArrayList();
      this.mActions = localArrayList;
      Notification localNotification1 = new Notification();
      this.mNotification = localNotification1;
      this.mContext = paramContext;
      Notification localNotification2 = this.mNotification;
      long l = System.currentTimeMillis();
      localNotification2.when = l;
      this.mNotification.audioStreamType = -1;
      this.mPriority = 0;
    }

    private void setFlag(int paramInt, boolean paramBoolean)
    {
      if (paramBoolean)
      {
        Notification localNotification1 = this.mNotification;
        int i = localNotification1.flags | paramInt;
        localNotification1.flags = i;
        return;
      }
      Notification localNotification2 = this.mNotification;
      int j = localNotification2.flags;
      int k = paramInt ^ 0xFFFFFFFF;
      int m = j & k;
      localNotification2.flags = m;
    }

    public Builder addAction(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
    {
      ArrayList localArrayList = this.mActions;
      NotificationCompat.Action localAction = new NotificationCompat.Action(paramInt, paramCharSequence, paramPendingIntent);
      boolean bool = localArrayList.add(localAction);
      return this;
    }

    public Notification build()
    {
      return NotificationCompat.IMPL.build(this);
    }

    @Deprecated
    public Notification getNotification()
    {
      return NotificationCompat.IMPL.build(this);
    }

    public Builder setAutoCancel(boolean paramBoolean)
    {
      setFlag(16, paramBoolean);
      return this;
    }

    public Builder setContent(RemoteViews paramRemoteViews)
    {
      this.mNotification.contentView = paramRemoteViews;
      return this;
    }

    public Builder setContentInfo(CharSequence paramCharSequence)
    {
      this.mContentInfo = paramCharSequence;
      return this;
    }

    public Builder setContentIntent(PendingIntent paramPendingIntent)
    {
      this.mContentIntent = paramPendingIntent;
      return this;
    }

    public Builder setContentText(CharSequence paramCharSequence)
    {
      this.mContentText = paramCharSequence;
      return this;
    }

    public Builder setContentTitle(CharSequence paramCharSequence)
    {
      this.mContentTitle = paramCharSequence;
      return this;
    }

    public Builder setDefaults(int paramInt)
    {
      this.mNotification.defaults = paramInt;
      if ((paramInt & 0x4) != 0)
      {
        Notification localNotification = this.mNotification;
        int i = localNotification.flags | 0x1;
        localNotification.flags = i;
      }
      return this;
    }

    public Builder setDeleteIntent(PendingIntent paramPendingIntent)
    {
      this.mNotification.deleteIntent = paramPendingIntent;
      return this;
    }

    public Builder setFullScreenIntent(PendingIntent paramPendingIntent, boolean paramBoolean)
    {
      this.mFullScreenIntent = paramPendingIntent;
      setFlag(128, paramBoolean);
      return this;
    }

    public Builder setLargeIcon(Bitmap paramBitmap)
    {
      this.mLargeIcon = paramBitmap;
      return this;
    }

    public Builder setLights(int paramInt1, int paramInt2, int paramInt3)
    {
      int i = 1;
      this.mNotification.ledARGB = paramInt1;
      this.mNotification.ledOnMS = paramInt2;
      this.mNotification.ledOffMS = paramInt3;
      int j;
      Notification localNotification;
      int k;
      if ((this.mNotification.ledOnMS != 0) && (this.mNotification.ledOffMS != 0))
      {
        j = 1;
        localNotification = this.mNotification;
        k = this.mNotification.flags & 0xFFFFFFFE;
        if (j == 0)
          break label95;
      }
      while (true)
      {
        int m = i | k;
        localNotification.flags = m;
        return this;
        j = 0;
        break;
        label95: i = 0;
      }
    }

    public Builder setNumber(int paramInt)
    {
      this.mNumber = paramInt;
      return this;
    }

    public Builder setOngoing(boolean paramBoolean)
    {
      setFlag(2, paramBoolean);
      return this;
    }

    public Builder setOnlyAlertOnce(boolean paramBoolean)
    {
      setFlag(8, paramBoolean);
      return this;
    }

    public Builder setPriority(int paramInt)
    {
      this.mPriority = paramInt;
      return this;
    }

    public Builder setProgress(int paramInt1, int paramInt2, boolean paramBoolean)
    {
      this.mProgressMax = paramInt1;
      this.mProgress = paramInt2;
      this.mProgressIndeterminate = paramBoolean;
      return this;
    }

    public Builder setSmallIcon(int paramInt)
    {
      this.mNotification.icon = paramInt;
      return this;
    }

    public Builder setSmallIcon(int paramInt1, int paramInt2)
    {
      this.mNotification.icon = paramInt1;
      this.mNotification.iconLevel = paramInt2;
      return this;
    }

    public Builder setSound(Uri paramUri)
    {
      this.mNotification.sound = paramUri;
      this.mNotification.audioStreamType = -1;
      return this;
    }

    public Builder setSound(Uri paramUri, int paramInt)
    {
      this.mNotification.sound = paramUri;
      this.mNotification.audioStreamType = paramInt;
      return this;
    }

    public Builder setStyle(NotificationCompat.Style paramStyle)
    {
      if (this.mStyle != paramStyle)
      {
        this.mStyle = paramStyle;
        if (this.mStyle != null)
          this.mStyle.setBuilder(this);
      }
      return this;
    }

    public Builder setSubText(CharSequence paramCharSequence)
    {
      this.mSubText = paramCharSequence;
      return this;
    }

    public Builder setTicker(CharSequence paramCharSequence)
    {
      this.mNotification.tickerText = paramCharSequence;
      return this;
    }

    public Builder setTicker(CharSequence paramCharSequence, RemoteViews paramRemoteViews)
    {
      this.mNotification.tickerText = paramCharSequence;
      this.mTickerView = paramRemoteViews;
      return this;
    }

    public Builder setUsesChronometer(boolean paramBoolean)
    {
      this.mUseChronometer = paramBoolean;
      return this;
    }

    public Builder setVibrate(long[] paramArrayOfLong)
    {
      this.mNotification.vibrate = paramArrayOfLong;
      return this;
    }

    public Builder setWhen(long paramLong)
    {
      this.mNotification.when = paramLong;
      return this;
    }
  }

  static class NotificationCompatImplJellybean
    implements NotificationCompat.NotificationCompatImpl
  {
    public Notification build(NotificationCompat.Builder paramBuilder)
    {
      Context localContext = paramBuilder.mContext;
      Notification localNotification = paramBuilder.mNotification;
      CharSequence localCharSequence1 = paramBuilder.mContentTitle;
      CharSequence localCharSequence2 = paramBuilder.mContentText;
      CharSequence localCharSequence3 = paramBuilder.mContentInfo;
      RemoteViews localRemoteViews = paramBuilder.mTickerView;
      int i = paramBuilder.mNumber;
      PendingIntent localPendingIntent1 = paramBuilder.mContentIntent;
      PendingIntent localPendingIntent2 = paramBuilder.mFullScreenIntent;
      Bitmap localBitmap1 = paramBuilder.mLargeIcon;
      int j = paramBuilder.mProgressMax;
      int k = paramBuilder.mProgress;
      boolean bool1 = paramBuilder.mProgressIndeterminate;
      boolean bool2 = paramBuilder.mUseChronometer;
      int m = paramBuilder.mPriority;
      CharSequence localCharSequence4 = paramBuilder.mSubText;
      NotificationCompatJellybean localNotificationCompatJellybean = new NotificationCompatJellybean(localContext, localNotification, localCharSequence1, localCharSequence2, localCharSequence3, localRemoteViews, i, localPendingIntent1, localPendingIntent2, localBitmap1, j, k, bool1, bool2, m, localCharSequence4);
      Iterator localIterator = paramBuilder.mActions.iterator();
      while (localIterator.hasNext())
      {
        NotificationCompat.Action localAction = (NotificationCompat.Action)localIterator.next();
        int n = localAction.icon;
        CharSequence localCharSequence5 = localAction.title;
        PendingIntent localPendingIntent3 = localAction.actionIntent;
        localNotificationCompatJellybean.addAction(n, localCharSequence5, localPendingIntent3);
      }
      if (paramBuilder.mStyle != null)
      {
        if (!(paramBuilder.mStyle instanceof NotificationCompat.BigTextStyle))
          break label272;
        NotificationCompat.BigTextStyle localBigTextStyle = (NotificationCompat.BigTextStyle)paramBuilder.mStyle;
        CharSequence localCharSequence6 = localBigTextStyle.mBigContentTitle;
        boolean bool3 = localBigTextStyle.mSummaryTextSet;
        CharSequence localCharSequence7 = localBigTextStyle.mSummaryText;
        CharSequence localCharSequence8 = localBigTextStyle.mBigText;
        localNotificationCompatJellybean.addBigTextStyle(localCharSequence6, bool3, localCharSequence7, localCharSequence8);
      }
      while (true)
      {
        return localNotificationCompatJellybean.build();
        label272: if ((paramBuilder.mStyle instanceof NotificationCompat.InboxStyle))
        {
          NotificationCompat.InboxStyle localInboxStyle = (NotificationCompat.InboxStyle)paramBuilder.mStyle;
          CharSequence localCharSequence9 = localInboxStyle.mBigContentTitle;
          boolean bool4 = localInboxStyle.mSummaryTextSet;
          CharSequence localCharSequence10 = localInboxStyle.mSummaryText;
          ArrayList localArrayList = localInboxStyle.mTexts;
          localNotificationCompatJellybean.addInboxStyle(localCharSequence9, bool4, localCharSequence10, localArrayList);
        }
        else if ((paramBuilder.mStyle instanceof NotificationCompat.BigPictureStyle))
        {
          NotificationCompat.BigPictureStyle localBigPictureStyle = (NotificationCompat.BigPictureStyle)paramBuilder.mStyle;
          CharSequence localCharSequence11 = localBigPictureStyle.mBigContentTitle;
          boolean bool5 = localBigPictureStyle.mSummaryTextSet;
          CharSequence localCharSequence12 = localBigPictureStyle.mSummaryText;
          Bitmap localBitmap2 = localBigPictureStyle.mPicture;
          Bitmap localBitmap3 = localBigPictureStyle.mBigLargeIcon;
          boolean bool6 = localBigPictureStyle.mBigLargeIconSet;
          localNotificationCompatJellybean.addBigPictureStyle(localCharSequence11, bool5, localCharSequence12, localBitmap2, localBitmap3, bool6);
        }
      }
    }
  }

  static class NotificationCompatImplIceCreamSandwich
    implements NotificationCompat.NotificationCompatImpl
  {
    public Notification build(NotificationCompat.Builder paramBuilder)
    {
      Context localContext = paramBuilder.mContext;
      Notification localNotification = paramBuilder.mNotification;
      CharSequence localCharSequence1 = paramBuilder.mContentTitle;
      CharSequence localCharSequence2 = paramBuilder.mContentText;
      CharSequence localCharSequence3 = paramBuilder.mContentInfo;
      RemoteViews localRemoteViews = paramBuilder.mTickerView;
      int i = paramBuilder.mNumber;
      PendingIntent localPendingIntent1 = paramBuilder.mContentIntent;
      PendingIntent localPendingIntent2 = paramBuilder.mFullScreenIntent;
      Bitmap localBitmap = paramBuilder.mLargeIcon;
      int j = paramBuilder.mProgressMax;
      int k = paramBuilder.mProgress;
      boolean bool = paramBuilder.mProgressIndeterminate;
      return NotificationCompatIceCreamSandwich.add(localContext, localNotification, localCharSequence1, localCharSequence2, localCharSequence3, localRemoteViews, i, localPendingIntent1, localPendingIntent2, localBitmap, j, k, bool);
    }
  }

  static class NotificationCompatImplHoneycomb
    implements NotificationCompat.NotificationCompatImpl
  {
    public Notification build(NotificationCompat.Builder paramBuilder)
    {
      Context localContext = paramBuilder.mContext;
      Notification localNotification = paramBuilder.mNotification;
      CharSequence localCharSequence1 = paramBuilder.mContentTitle;
      CharSequence localCharSequence2 = paramBuilder.mContentText;
      CharSequence localCharSequence3 = paramBuilder.mContentInfo;
      RemoteViews localRemoteViews = paramBuilder.mTickerView;
      int i = paramBuilder.mNumber;
      PendingIntent localPendingIntent1 = paramBuilder.mContentIntent;
      PendingIntent localPendingIntent2 = paramBuilder.mFullScreenIntent;
      Bitmap localBitmap = paramBuilder.mLargeIcon;
      return NotificationCompatHoneycomb.add(localContext, localNotification, localCharSequence1, localCharSequence2, localCharSequence3, localRemoteViews, i, localPendingIntent1, localPendingIntent2, localBitmap);
    }
  }

  static class NotificationCompatImplBase
    implements NotificationCompat.NotificationCompatImpl
  {
    public Notification build(NotificationCompat.Builder paramBuilder)
    {
      Notification localNotification = paramBuilder.mNotification;
      Context localContext = paramBuilder.mContext;
      CharSequence localCharSequence1 = paramBuilder.mContentTitle;
      CharSequence localCharSequence2 = paramBuilder.mContentText;
      PendingIntent localPendingIntent = paramBuilder.mContentIntent;
      localNotification.setLatestEventInfo(localContext, localCharSequence1, localCharSequence2, localPendingIntent);
      if (paramBuilder.mPriority > 0)
      {
        int i = localNotification.flags | 0x80;
        localNotification.flags = i;
      }
      return localNotification;
    }
  }

  static abstract interface NotificationCompatImpl
  {
    public abstract Notification build(NotificationCompat.Builder paramBuilder);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.NotificationCompat
 * JD-Core Version:    0.6.2
 */