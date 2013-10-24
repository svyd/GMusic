package com.google.android.music;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.google.android.music.activitymanagement.KeepOnManager;
import com.google.android.music.activitymanagement.KeepOnManager.KeepOnState;
import com.google.android.music.activitymanagement.KeepOnManager.UpdateKeepOnStatusTask;
import com.google.android.music.animator.Animator;
import com.google.android.music.animator.AnimatorUpdateListener;
import com.google.android.music.download.cache.CacheUtils;
import com.google.android.music.medialist.NautilusAlbumSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent;
import com.google.android.music.ui.AddToLibraryButton;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.ui.UIStateManager.NetworkChangeListener;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;

public abstract class KeepOnView extends ImageView
  implements View.OnClickListener, KeepOnManager.KeepOnState, AnimatorUpdateListener
{
  protected static int sArcColor = -1;
  protected static int sArcColorPaused = -1;
  protected static int sProgressArcPadding = -1;
  private Paint mArcPaint;
  private RectF mArcRect;
  private boolean mCached;
  private Document.Type mDocumentType;
  private float mDownloadFraction;
  private boolean mEnabled;
  private ContentObserver mKeepOnObserver;
  private final UIStateManager.NetworkChangeListener mNetworkChangeListener;
  private boolean mPinned;
  private SongList mSongList;
  private String mSongListName;
  private boolean mViewInitialized;

  protected KeepOnView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    UIStateManager.NetworkChangeListener local1 = new UIStateManager.NetworkChangeListener()
    {
      public void onNetworkChanged(boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2)
      {
        boolean bool = false;
        if ((paramAnonymousBoolean1) && (!UIStateManager.getInstance().getPrefs().isOfflineDLOnlyOnWifi()));
        for (int i = 1; ; i = 0)
        {
          KeepOnView localKeepOnView = KeepOnView.this;
          if ((i != 0) || (paramAnonymousBoolean2))
            bool = true;
          localKeepOnView.setArtPaintColor(bool);
          return;
        }
      }
    };
    this.mNetworkChangeListener = local1;
    initResourceIds(paramContext);
    RectF localRectF = new RectF();
    this.mArcRect = localRectF;
    Paint localPaint = new Paint();
    this.mArcPaint = localPaint;
    boolean bool = isAbleToDownload();
    setArtPaintColor(bool);
    this.mArcPaint.setAntiAlias(true);
    this.mPinned = false;
    this.mEnabled = false;
    this.mCached = false;
    this.mDownloadFraction = 0.0F;
    setImageDrawable(null);
    this.mViewInitialized = false;
  }

  private void drawKeepOn(Canvas paramCanvas, int paramInt1, int paramInt2, Paint paramPaint, RectF paramRectF, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, float paramFloat)
  {
    SharedResources localSharedResources = getKeepOnResources();
    Bitmap localBitmap1;
    if (paramBoolean1)
      if (paramBoolean2)
        localBitmap1 = localSharedResources.sPinnedOverlay;
    while (paramBoolean1)
    {
      Bitmap localBitmap2 = localSharedResources.sGreyCircleBackground;
      float f1 = paramInt1;
      float f2 = paramInt2;
      paramCanvas.drawBitmap(localBitmap2, f1, f2, null);
      float f3 = paramFloat * 360.0F;
      Canvas localCanvas = paramCanvas;
      RectF localRectF = paramRectF;
      Paint localPaint = paramPaint;
      localCanvas.drawArc(localRectF, 270.0F, f3, true, localPaint);
      float f4 = paramInt1;
      float f5 = paramInt2;
      paramCanvas.drawBitmap(localBitmap1, f4, f5, null);
      return;
      if (paramBoolean3)
      {
        localBitmap1 = localSharedResources.sCachedOverlay;
      }
      else
      {
        localBitmap1 = localSharedResources.sUnpinnedOverlay;
        continue;
        localBitmap1 = localSharedResources.sSideloadedOverlay;
      }
    }
    float f6 = paramInt1;
    float f7 = paramInt2;
    paramCanvas.drawBitmap(localBitmap1, f6, f7, null);
  }

  private CharSequence getBodyText()
  {
    Object localObject = null;
    if (this.mDocumentType == null);
    while (true)
    {
      return localObject;
      int i = -2147483648;
      Document.Type localType1 = this.mDocumentType;
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 == localType2)
        i = 2131231415;
      while (true)
      {
        if (i == -2147483648)
          break label122;
        if (TextUtils.isEmpty(this.mSongListName))
          break label124;
        Object[] arrayOfObject1 = new Object[2];
        String str1 = this.mSongListName;
        arrayOfObject1[0] = str1;
        String str2 = getContext().getResources().getString(i);
        arrayOfObject1[1] = str2;
        localObject = Html.fromHtml(String.format("<b><br/>%s</b><br/><br/>%s", arrayOfObject1));
        break;
        Document.Type localType3 = this.mDocumentType;
        Document.Type localType4 = Document.Type.PLAYLIST;
        if (localType3 == localType4)
          i = 2131231416;
      }
      label122: continue;
      label124: Object[] arrayOfObject2 = new Object[1];
      String str3 = getContext().getResources().getString(i);
      arrayOfObject2[0] = str3;
      localObject = Html.fromHtml(String.format("%s", arrayOfObject2));
    }
  }

  private void handleOnKeepOnClicked()
  {
    if ((this.mSongList instanceof NautilusAlbumSongList))
    {
      MusicUtils.runAsyncWithCallback(new AsyncRunner()
      {
        public void backgroundTask()
        {
          NautilusAlbumSongList localNautilusAlbumSongList = (NautilusAlbumSongList)KeepOnView.this.mSongList;
          Context localContext = KeepOnView.this.getContext();
          if (localNautilusAlbumSongList.isAllInLibrary(localContext))
            return;
          AddToLibraryButton.addToLibrary(KeepOnView.this.getContext(), localNautilusAlbumSongList);
        }

        public void taskCompleted()
        {
          Context localContext = KeepOnView.this.getContext();
          SongList localSongList = KeepOnView.this.mSongList;
          KeepOnView localKeepOnView1 = KeepOnView.this;
          KeepOnView localKeepOnView2 = KeepOnView.this;
          KeepOnManager.toggleSonglistKeepOn(localContext, localSongList, localKeepOnView1, localKeepOnView2);
        }
      });
      return;
    }
    Context localContext = getContext();
    SongList localSongList = this.mSongList;
    KeepOnManager.toggleSonglistKeepOn(localContext, localSongList, this, this);
  }

  private boolean isAbleToDownload()
  {
    boolean bool1 = false;
    boolean bool2 = UIStateManager.getInstance().isWifiOrEthernetConnected();
    if ((UIStateManager.getInstance().isMobileConnected()) && (!UIStateManager.getInstance().getPrefs().isOfflineDLOnlyOnWifi()));
    for (int i = 1; ; i = 0)
    {
      if ((bool2) || (i != 0))
        bool1 = true;
      return bool1;
    }
  }

  private void setArtPaintColor(boolean paramBoolean)
  {
    if (getDownloadFraction() == 1.0F)
    {
      Paint localPaint1 = this.mArcPaint;
      int i = sArcColor;
      localPaint1.setColor(i);
      return;
    }
    if ((UIStateManager.getInstance().getPrefs().isKeepOnDownloadingPaused()) || (!paramBoolean))
    {
      Paint localPaint2 = this.mArcPaint;
      int j = sArcColorPaused;
      localPaint2.setColor(j);
      return;
    }
    Paint localPaint3 = this.mArcPaint;
    int k = sArcColor;
    localPaint3.setColor(k);
  }

  private void setDocumentType()
  {
    this.mDocumentType = null;
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private Document mDoc;
      private final SongList mList;

      public void backgroundTask()
      {
        if (this.mList == null)
          return;
        Context localContext = KeepOnView.this.getContext();
        SongList localSongList = this.mList;
        Document localDocument = Document.fromSongList(localContext, localSongList);
        this.mDoc = localDocument;
      }

      public void taskCompleted()
      {
        SongList localSongList1 = this.mList;
        SongList localSongList2 = KeepOnView.this.mSongList;
        if (localSongList1 != localSongList2)
          return;
        if (this.mDoc == null)
          return;
        KeepOnView localKeepOnView = KeepOnView.this;
        Document.Type localType1 = this.mDoc.getType();
        Document.Type localType2 = KeepOnView.access$302(localKeepOnView, localType1);
      }
    });
  }

  private void setSongListName()
  {
    this.mSongListName = "";
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      private final SongList mList;
      private String mListName;

      public void backgroundTask()
      {
        if (this.mList == null)
          return;
        SongList localSongList = this.mList;
        Context localContext = KeepOnView.this.getContext();
        String str = localSongList.getName(localContext);
        this.mListName = str;
      }

      public void taskCompleted()
      {
        SongList localSongList1 = this.mList;
        SongList localSongList2 = KeepOnView.this.mSongList;
        if (localSongList1 != localSongList2)
          return;
        KeepOnView localKeepOnView = KeepOnView.this;
        String str1 = this.mListName;
        String str2 = KeepOnView.access$202(localKeepOnView, str1);
      }
    });
  }

  private void setViewInitilized()
  {
    if (this.mViewInitialized)
      return;
    this.mViewInitialized = true;
  }

  public float getDownloadFraction()
  {
    return this.mDownloadFraction;
  }

  protected abstract SharedResources getKeepOnResources();

  protected void initResourceIds(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    if (sArcColor == -1)
      sArcColor = localResources.getColor(2131427354);
    if (sArcColorPaused == -1)
      sArcColorPaused = localResources.getColor(2131427355);
    if (sProgressArcPadding != -1)
      return;
    sProgressArcPadding = localResources.getDimensionPixelSize(2131558442);
  }

  public boolean isCached()
  {
    return this.mCached;
  }

  public boolean isPinnable()
  {
    return this.mEnabled;
  }

  public boolean isPinned()
  {
    return this.mPinned;
  }

  public void onAnimationUpdate(Animator paramAnimator)
  {
    float f = ((Float)paramAnimator.getAnimatedValue()).floatValue();
    this.mDownloadFraction = f;
    invalidate();
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.NetworkChangeListener localNetworkChangeListener = this.mNetworkChangeListener;
    localUIStateManager.registerNetworkChangeListener(localNetworkChangeListener);
    if (this.mKeepOnObserver != null)
      return;
    Handler localHandler = new Handler();
    ContentObserver local2 = new ContentObserver(localHandler)
    {
      public void onChange(boolean paramAnonymousBoolean)
      {
        if (KeepOnView.this.mSongList == null)
          return;
        KeepOnView.this.updateContainerOfflineStatus();
      }
    };
    this.mKeepOnObserver = local2;
    ContentResolver localContentResolver = getContext().getContentResolver();
    Uri localUri = MusicContent.KEEP_ON_URI;
    ContentObserver localContentObserver = this.mKeepOnObserver;
    localContentResolver.registerContentObserver(localUri, false, localContentObserver);
  }

  public void onClick(View paramView)
  {
    onKeepOnViewClicked();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    if (this.mKeepOnObserver != null)
    {
      ContentResolver localContentResolver = getContext().getContentResolver();
      ContentObserver localContentObserver = this.mKeepOnObserver;
      localContentResolver.unregisterContentObserver(localContentObserver);
      this.mKeepOnObserver = null;
    }
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.NetworkChangeListener localNetworkChangeListener = this.mNetworkChangeListener;
    localUIStateManager.unregisterNetworkChangeListener(localNetworkChangeListener);
  }

  public void onDraw(Canvas paramCanvas)
  {
    if (!this.mViewInitialized)
      return;
    int i = getPaddingLeft();
    int j = getPaddingTop();
    boolean bool1 = isAbleToDownload();
    setArtPaintColor(bool1);
    Paint localPaint = this.mArcPaint;
    RectF localRectF = this.mArcRect;
    boolean bool2 = isPinnable();
    boolean bool3 = isPinned();
    boolean bool4 = isCached();
    float f = getDownloadFraction();
    KeepOnView localKeepOnView = this;
    Canvas localCanvas = paramCanvas;
    localKeepOnView.drawKeepOn(localCanvas, i, j, localPaint, localRectF, bool2, bool3, bool4, f);
  }

  public void onKeepOnViewClicked()
  {
    Context localContext1 = getContext();
    if ((!TextUtils.isEmpty(UIStateManager.getInstance().getPrefs().getSecondaryExternalStorageMountPoint())) && (!CacheUtils.isSecondaryExternalStorageMounted(localContext1)))
    {
      AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localContext1);
      AlertDialog.Builder localBuilder2 = localBuilder1.setTitle(2131231387);
      AlertDialog.Builder localBuilder3 = localBuilder1.setNeutralButton(2131231239, null);
      AlertDialog.Builder localBuilder4 = localBuilder1.setMessage(2131231388);
      AlertDialog localAlertDialog = localBuilder1.show();
      return;
    }
    if ((isPinned()) && (UIStateManager.getInstance().getPrefs().shouldShowUnpinDialog()))
    {
      Context localContext2 = getContext();
      AlertDialog.Builder localBuilder5 = new AlertDialog.Builder(localContext2);
      View localView = ((Activity)getContext()).getLayoutInflater().inflate(2130968639, null);
      final CheckBox localCheckBox = (CheckBox)localView.findViewById(2131296328);
      AlertDialog.Builder localBuilder6 = localBuilder5.setTitle(2131231414);
      CharSequence localCharSequence = getBodyText();
      AlertDialog.Builder localBuilder7 = localBuilder6.setMessage(localCharSequence).setView(localView);
      DialogInterface.OnClickListener local6 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (localCheckBox.isChecked())
            UIStateManager.getInstance().getPrefs().setShowUnpinDialog(false);
          KeepOnView.this.handleOnKeepOnClicked();
        }
      };
      AlertDialog.Builder localBuilder8 = localBuilder7.setPositiveButton(2131231418, local6);
      DialogInterface.OnClickListener local5 = new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          paramAnonymousDialogInterface.dismiss();
        }
      };
      localBuilder8.setNegativeButton(2131231417, local5).create().show();
      return;
    }
    handleOnKeepOnClicked();
  }

  public void onMeasure(int paramInt1, int paramInt2)
  {
    int i = getPaddingLeft();
    int j = getPaddingTop();
    int k = getKeepOnResources().sUnpinnedOverlay.getWidth();
    int m = getKeepOnResources().sUnpinnedOverlay.getHeight();
    float f1 = sProgressArcPadding + i;
    float f2 = sProgressArcPadding + j;
    int n = sProgressArcPadding * 2;
    float f3 = k - n;
    int i1 = sProgressArcPadding * 2;
    float f4 = m - i1;
    RectF localRectF = this.mArcRect;
    float f5 = f1 + f3;
    float f6 = f2 + f4;
    localRectF.set(f1, f2, f5, f6);
    int i2 = i * 2;
    int i3 = k + i2;
    int i4 = j * 2;
    int i5 = m + i4;
    setMeasuredDimension(i3, i5);
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    boolean bool1 = localSavedState.checked;
    setPinned(bool1);
    boolean bool2 = localSavedState.available;
    setPinnable(bool2);
    float f = localSavedState.downloadFraction;
    setDownloadFraction(f);
    requestLayout();
  }

  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    boolean bool1 = isPinned();
    boolean bool2 = SavedState.access$902(localSavedState, bool1);
    boolean bool3 = isPinnable();
    boolean bool4 = SavedState.access$1002(localSavedState, bool3);
    float f1 = getDownloadFraction();
    float f2 = SavedState.access$1102(localSavedState, f1);
    return localSavedState;
  }

  public void setCached(boolean paramBoolean)
  {
    this.mCached = paramBoolean;
    setViewInitilized();
  }

  public void setDownloadFraction(float paramFloat)
  {
    this.mDownloadFraction = paramFloat;
    setViewInitilized();
  }

  public void setOnClick(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setOnClickListener(this);
      return;
    }
    setOnClickListener(null);
  }

  public void setPinnable(boolean paramBoolean)
  {
    this.mEnabled = paramBoolean;
    setViewInitilized();
  }

  public void setPinned(boolean paramBoolean)
  {
    this.mPinned = paramBoolean;
    setViewInitilized();
  }

  public void setSongList(SongList paramSongList)
  {
    this.mSongList = paramSongList;
    setSongListName();
    setDocumentType();
    updateContainerOfflineStatus();
  }

  public void updateContainerOfflineStatus()
  {
    MusicUtils.runAsyncWithCallback(new MyUpdateKeepOnStatusTask());
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public KeepOnView.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new KeepOnView.SavedState(paramAnonymousParcel, null);
      }

      public KeepOnView.SavedState[] newArray(int paramAnonymousInt)
      {
        return new KeepOnView.SavedState[paramAnonymousInt];
      }
    };
    private boolean available;
    private boolean checked;
    private float downloadFraction;

    private SavedState(Parcel paramParcel)
    {
      super();
      boolean bool1 = ((Boolean)paramParcel.readValue(null)).booleanValue();
      this.checked = bool1;
      boolean bool2 = ((Boolean)paramParcel.readValue(null)).booleanValue();
      this.available = bool2;
      float f = paramParcel.readFloat();
      this.downloadFraction = f;
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("SelectionCheckBox.SavedState{");
      String str = Integer.toHexString(System.identityHashCode(this));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(" checked=");
      boolean bool1 = this.checked;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(bool1).append(" available=");
      boolean bool2 = this.available;
      StringBuilder localStringBuilder4 = localStringBuilder3.append(bool2).append(" downloadFraction=");
      float f = this.downloadFraction;
      return f + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      Boolean localBoolean1 = Boolean.valueOf(this.checked);
      paramParcel.writeValue(localBoolean1);
      Boolean localBoolean2 = Boolean.valueOf(this.available);
      paramParcel.writeValue(localBoolean2);
      float f = this.downloadFraction;
      paramParcel.writeFloat(f);
    }
  }

  private class MyUpdateKeepOnStatusTask extends KeepOnManager.UpdateKeepOnStatusTask
  {
    MyUpdateKeepOnStatusTask()
    {
      super(localSongList);
    }

    protected AnimatorUpdateListener getAnimatorUpdateListener()
    {
      return KeepOnView.this;
    }

    protected SongList getCurrentSongList()
    {
      return KeepOnView.this.mSongList;
    }

    protected KeepOnManager.KeepOnState getKeepOnState()
    {
      return KeepOnView.this;
    }
  }

  protected static class SharedResources
  {
    private final Bitmap sCachedOverlay;
    private final Bitmap sGreyCircleBackground;
    private final Bitmap sPinnedOverlay;
    private final Bitmap sSideloadedOverlay;
    private final Bitmap sUnpinnedOverlay;

    protected SharedResources(Bitmap paramBitmap1, Bitmap paramBitmap2, Bitmap paramBitmap3, Bitmap paramBitmap4, Bitmap paramBitmap5)
    {
      this.sUnpinnedOverlay = paramBitmap1;
      this.sPinnedOverlay = paramBitmap2;
      this.sCachedOverlay = paramBitmap3;
      this.sSideloadedOverlay = paramBitmap4;
      this.sGreyCircleBackground = paramBitmap5;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.KeepOnView
 * JD-Core Version:    0.6.2
 */