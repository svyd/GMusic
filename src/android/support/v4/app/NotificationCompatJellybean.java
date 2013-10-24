package android.support.v4.app;

import android.app.Notification;
import android.app.Notification.BigPictureStyle;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.Notification.InboxStyle;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Iterator;

class NotificationCompatJellybean
{
  private Notification.Builder b;

  public NotificationCompatJellybean(Context paramContext, Notification paramNotification, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, RemoteViews paramRemoteViews, int paramInt1, PendingIntent paramPendingIntent1, PendingIntent paramPendingIntent2, Bitmap paramBitmap, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4, CharSequence paramCharSequence4)
  {
    Notification.Builder localBuilder1 = new Notification.Builder(paramContext);
    long l = paramNotification.when;
    Notification.Builder localBuilder2 = localBuilder1.setWhen(l);
    int i = paramNotification.icon;
    int j = paramNotification.iconLevel;
    Notification.Builder localBuilder3 = localBuilder2.setSmallIcon(i, j);
    RemoteViews localRemoteViews = paramNotification.contentView;
    Notification.Builder localBuilder4 = localBuilder3.setContent(localRemoteViews);
    CharSequence localCharSequence1 = paramNotification.tickerText;
    Notification.Builder localBuilder5 = localBuilder4.setTicker(localCharSequence1, paramRemoteViews);
    Uri localUri = paramNotification.sound;
    int k = paramNotification.audioStreamType;
    Notification.Builder localBuilder6 = localBuilder5.setSound(localUri, k);
    long[] arrayOfLong = paramNotification.vibrate;
    Notification.Builder localBuilder7 = localBuilder6.setVibrate(arrayOfLong);
    int m = paramNotification.ledARGB;
    int n = paramNotification.ledOnMS;
    int i1 = paramNotification.ledOffMS;
    Notification.Builder localBuilder8 = localBuilder7.setLights(m, n, i1);
    boolean bool1;
    boolean bool2;
    label187: boolean bool3;
    label209: Notification.Builder localBuilder14;
    if ((paramNotification.flags & 0x2) != 0)
    {
      bool1 = true;
      Notification.Builder localBuilder9 = localBuilder8.setOngoing(bool1);
      if ((paramNotification.flags & 0x8) == 0)
        break label391;
      bool2 = true;
      Notification.Builder localBuilder10 = localBuilder9.setOnlyAlertOnce(bool2);
      if ((paramNotification.flags & 0x10) == 0)
        break label397;
      bool3 = true;
      Notification.Builder localBuilder11 = localBuilder10.setAutoCancel(bool3);
      int i2 = paramNotification.defaults;
      Notification.Builder localBuilder12 = localBuilder11.setDefaults(i2).setContentTitle(paramCharSequence1).setContentText(paramCharSequence2);
      CharSequence localCharSequence2 = paramCharSequence4;
      Notification.Builder localBuilder13 = localBuilder12.setSubText(localCharSequence2).setContentInfo(paramCharSequence3).setContentIntent(paramPendingIntent1);
      PendingIntent localPendingIntent1 = paramNotification.deleteIntent;
      localBuilder14 = localBuilder13.setDeleteIntent(localPendingIntent1);
      if ((paramNotification.flags & 0x80) == 0)
        break label403;
    }
    label391: label397: label403: for (boolean bool4 = true; ; bool4 = false)
    {
      PendingIntent localPendingIntent2 = paramPendingIntent2;
      Notification.Builder localBuilder15 = localBuilder14.setFullScreenIntent(localPendingIntent2, bool4);
      Bitmap localBitmap = paramBitmap;
      Notification.Builder localBuilder16 = localBuilder15.setLargeIcon(localBitmap).setNumber(paramInt1);
      boolean bool5 = paramBoolean2;
      Notification.Builder localBuilder17 = localBuilder16.setUsesChronometer(bool5);
      int i3 = paramInt4;
      Notification.Builder localBuilder18 = localBuilder17.setPriority(i3);
      int i4 = paramInt2;
      int i5 = paramInt3;
      boolean bool6 = paramBoolean1;
      Notification.Builder localBuilder19 = localBuilder18.setProgress(i4, i5, bool6);
      this.b = localBuilder19;
      return;
      bool1 = false;
      break;
      bool2 = false;
      break label187;
      bool3 = false;
      break label209;
    }
  }

  public void addAction(int paramInt, CharSequence paramCharSequence, PendingIntent paramPendingIntent)
  {
    Notification.Builder localBuilder = this.b.addAction(paramInt, paramCharSequence, paramPendingIntent);
  }

  public void addBigPictureStyle(CharSequence paramCharSequence1, boolean paramBoolean1, CharSequence paramCharSequence2, Bitmap paramBitmap1, Bitmap paramBitmap2, boolean paramBoolean2)
  {
    Notification.Builder localBuilder = this.b;
    Notification.BigPictureStyle localBigPictureStyle1 = new Notification.BigPictureStyle(localBuilder).setBigContentTitle(paramCharSequence1).bigPicture(paramBitmap1);
    if (paramBoolean2)
      Notification.BigPictureStyle localBigPictureStyle2 = localBigPictureStyle1.bigLargeIcon(paramBitmap2);
    if (!paramBoolean1)
      return;
    Notification.BigPictureStyle localBigPictureStyle3 = localBigPictureStyle1.setSummaryText(paramCharSequence2);
  }

  public void addBigTextStyle(CharSequence paramCharSequence1, boolean paramBoolean, CharSequence paramCharSequence2, CharSequence paramCharSequence3)
  {
    Notification.Builder localBuilder = this.b;
    Notification.BigTextStyle localBigTextStyle1 = new Notification.BigTextStyle(localBuilder).setBigContentTitle(paramCharSequence1).bigText(paramCharSequence3);
    if (!paramBoolean)
      return;
    Notification.BigTextStyle localBigTextStyle2 = localBigTextStyle1.setSummaryText(paramCharSequence2);
  }

  public void addInboxStyle(CharSequence paramCharSequence1, boolean paramBoolean, CharSequence paramCharSequence2, ArrayList<CharSequence> paramArrayList)
  {
    Notification.Builder localBuilder = this.b;
    Notification.InboxStyle localInboxStyle1 = new Notification.InboxStyle(localBuilder).setBigContentTitle(paramCharSequence1);
    if (paramBoolean)
      Notification.InboxStyle localInboxStyle2 = localInboxStyle1.setSummaryText(paramCharSequence2);
    Iterator localIterator = paramArrayList.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      CharSequence localCharSequence = (CharSequence)localIterator.next();
      Notification.InboxStyle localInboxStyle3 = localInboxStyle1.addLine(localCharSequence);
    }
  }

  public Notification build()
  {
    return this.b.build();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.NotificationCompatJellybean
 * JD-Core Version:    0.6.2
 */