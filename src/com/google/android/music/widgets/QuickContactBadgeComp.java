package com.google.android.music.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.QuickContactBadge;
import com.google.android.music.utils.AlbumArtUtils;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.async.AsyncRunner;
import com.google.android.music.utils.async.AsyncWorkers;

public class QuickContactBadgeComp extends QuickContactBadge
{
  private Context mContext;
  private String mCurrentUrl;
  private int mQuickContactBadgeHeight;
  private int mQuickContactBadgeWidth;

  public QuickContactBadgeComp(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    Resources localResources = paramContext.getResources();
    int i = localResources.getDimensionPixelSize(2131558430);
    this.mQuickContactBadgeWidth = i;
    int j = localResources.getDimensionPixelSize(2131558431);
    this.mQuickContactBadgeHeight = j;
  }

  private Bitmap getPhoto(String paramString, boolean paramBoolean)
  {
    Context localContext = this.mContext;
    int i = this.mQuickContactBadgeWidth;
    int j = this.mQuickContactBadgeHeight;
    String str = paramString;
    boolean bool1 = false;
    boolean bool2 = paramBoolean;
    boolean bool3 = false;
    return AlbumArtUtils.getArtworkFromUrl(localContext, str, i, j, false, bool1, bool2, bool3, true);
  }

  public void setImageToDefault()
  {
    if (Build.VERSION.SDK_INT < 11)
    {
      setImageResource(2130837702);
      return;
    }
    super.setImageToDefault();
  }

  /** @deprecated */
  public void setImageUrl(final String paramString)
  {
    if (paramString != null);
    while (true)
    {
      try
      {
        String str = this.mCurrentUrl;
        boolean bool1 = paramString.equals(str);
        if (bool1)
          return;
        this.mCurrentUrl = paramString;
        if (paramString == null)
        {
          setImageToDefault();
          continue;
        }
      }
      finally
      {
      }
      boolean bool2 = false;
      Bitmap localBitmap = getPhoto(paramString, bool2);
      if (localBitmap != null)
      {
        setImageBitmap(localBitmap);
      }
      else
      {
        setImageToDefault();
        LoggableHandler localLoggableHandler = AsyncWorkers.sUIBackgroundWorker;
        AsyncRunner local1 = new AsyncRunner()
        {
          private Bitmap mPhoto;

          public void backgroundTask()
          {
            QuickContactBadgeComp localQuickContactBadgeComp = QuickContactBadgeComp.this;
            String str = paramString;
            Bitmap localBitmap = localQuickContactBadgeComp.getPhoto(str, true);
            this.mPhoto = localBitmap;
          }

          public void taskCompleted()
          {
            String str1 = paramString;
            String str2 = QuickContactBadgeComp.this.mCurrentUrl;
            if (!str1.equals(str2))
              return;
            if (this.mPhoto == null)
            {
              QuickContactBadgeComp.this.setImageToDefault();
              return;
            }
            QuickContactBadgeComp localQuickContactBadgeComp = QuickContactBadgeComp.this;
            Bitmap localBitmap = this.mPhoto;
            localQuickContactBadgeComp.setImageBitmap(localBitmap);
          }
        };
        AsyncWorkers.runAsyncWithCallback(localLoggableHandler, local1);
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.widgets.QuickContactBadgeComp
 * JD-Core Version:    0.6.2
 */