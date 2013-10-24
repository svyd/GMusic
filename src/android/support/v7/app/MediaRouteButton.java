package android.support.v7.app;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.media.MediaRouteSelector;
import android.support.v7.media.MediaRouter;
import android.support.v7.media.MediaRouter.Callback;
import android.support.v7.media.MediaRouter.ProviderInfo;
import android.support.v7.media.MediaRouter.RouteInfo;
import android.support.v7.mediarouter.R.styleable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.Toast;

public class MediaRouteButton extends View
{
  private static final int[] CHECKABLE_STATE_SET = arrayOfInt2;
  private static final int[] CHECKED_STATE_SET;
  private boolean mAttachedToWindow;
  private final MediaRouterCallback mCallback;
  private boolean mCheatSheetEnabled;
  private MediaRouteDialogFactory mDialogFactory;
  private boolean mIsConnecting;
  private int mMinHeight;
  private int mMinWidth;
  private boolean mRemoteActive;
  private Drawable mRemoteIndicator;
  private final MediaRouter mRouter;
  private MediaRouteSelector mSelector;

  static
  {
    int[] arrayOfInt1 = new int[1];
    arrayOfInt1[0] = 16842912;
    CHECKED_STATE_SET = arrayOfInt1;
    int[] arrayOfInt2 = new int[1];
    arrayOfInt2[0] = 16842911;
  }

  public MediaRouteButton(Context paramContext)
  {
    this(paramContext, null);
  }

  public MediaRouteButton(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, i);
  }

  public MediaRouteButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(localContext1, paramAttributeSet, paramInt);
    MediaRouteSelector localMediaRouteSelector = MediaRouteSelector.EMPTY;
    this.mSelector = localMediaRouteSelector;
    MediaRouteDialogFactory localMediaRouteDialogFactory = MediaRouteDialogFactory.getDefault();
    this.mDialogFactory = localMediaRouteDialogFactory;
    Context localContext2 = getContext();
    MediaRouter localMediaRouter = MediaRouter.getInstance(localContext2);
    this.mRouter = localMediaRouter;
    MediaRouterCallback localMediaRouterCallback = new MediaRouterCallback(null);
    this.mCallback = localMediaRouterCallback;
    int[] arrayOfInt = R.styleable.MediaRouteButton;
    TypedArray localTypedArray = localContext2.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt, 0);
    Drawable localDrawable = localTypedArray.getDrawable(2);
    setRemoteIndicatorDrawable(localDrawable);
    int i = localTypedArray.getDimensionPixelSize(0, 0);
    this.mMinWidth = i;
    int j = localTypedArray.getDimensionPixelSize(1, 0);
    this.mMinHeight = j;
    localTypedArray.recycle();
    setClickable(true);
    setLongClickable(true);
  }

  private Activity getActivity()
  {
    Context localContext = getContext();
    if ((localContext instanceof ContextWrapper))
      if (!(localContext instanceof Activity));
    for (Activity localActivity = (Activity)localContext; ; localActivity = null)
    {
      return localActivity;
      localContext = ((ContextWrapper)localContext).getBaseContext();
      break;
    }
  }

  private FragmentManager getFragmentManager()
  {
    Activity localActivity = getActivity();
    if ((localActivity instanceof FragmentActivity));
    for (FragmentManager localFragmentManager = ((FragmentActivity)localActivity).getSupportFragmentManager(); ; localFragmentManager = null)
      return localFragmentManager;
  }

  private void refreshRoute()
  {
    boolean bool1 = false;
    if (!this.mAttachedToWindow)
      return;
    MediaRouter.RouteInfo localRouteInfo = this.mRouter.getSelectedRoute();
    if (!localRouteInfo.isDefault())
    {
      MediaRouteSelector localMediaRouteSelector1 = this.mSelector;
      if (!localRouteInfo.matchesSelector(localMediaRouteSelector1));
    }
    for (boolean bool2 = true; ; bool2 = false)
    {
      if ((bool2) && (localRouteInfo.isConnecting()))
        bool1 = true;
      int i = 0;
      if (this.mRemoteActive != bool2)
      {
        this.mRemoteActive = bool2;
        i = 1;
      }
      if (this.mIsConnecting != bool1)
      {
        this.mIsConnecting = bool1;
        i = 1;
      }
      if (i != 0)
        refreshDrawableState();
      MediaRouter localMediaRouter = this.mRouter;
      MediaRouteSelector localMediaRouteSelector2 = this.mSelector;
      boolean bool3 = localMediaRouter.isRouteAvailable(localMediaRouteSelector2, 1);
      setEnabled(bool3);
      return;
    }
  }

  private void setRemoteIndicatorDrawable(Drawable paramDrawable)
  {
    if (this.mRemoteIndicator != null)
    {
      this.mRemoteIndicator.setCallback(null);
      Drawable localDrawable = this.mRemoteIndicator;
      unscheduleDrawable(localDrawable);
    }
    this.mRemoteIndicator = paramDrawable;
    if (paramDrawable != null)
    {
      paramDrawable.setCallback(this);
      int[] arrayOfInt = getDrawableState();
      boolean bool1 = paramDrawable.setState(arrayOfInt);
      if (getVisibility() != 0)
        break label75;
    }
    label75: for (boolean bool2 = true; ; bool2 = false)
    {
      boolean bool3 = paramDrawable.setVisible(bool2, false);
      refreshDrawableState();
      return;
    }
  }

  protected void drawableStateChanged()
  {
    super.drawableStateChanged();
    if (this.mRemoteIndicator == null)
      return;
    int[] arrayOfInt = getDrawableState();
    boolean bool = this.mRemoteIndicator.setState(arrayOfInt);
    invalidate();
  }

  public void jumpDrawablesToCurrentState()
  {
    if (getBackground() != null)
      DrawableCompat.jumpToCurrentState(getBackground());
    if (this.mRemoteIndicator == null)
      return;
    DrawableCompat.jumpToCurrentState(this.mRemoteIndicator);
  }

  public void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAttachedToWindow = true;
    if (!this.mSelector.isEmpty())
    {
      MediaRouter localMediaRouter = this.mRouter;
      MediaRouteSelector localMediaRouteSelector = this.mSelector;
      MediaRouterCallback localMediaRouterCallback = this.mCallback;
      localMediaRouter.addCallback(localMediaRouteSelector, localMediaRouterCallback);
    }
    refreshRoute();
  }

  protected int[] onCreateDrawableState(int paramInt)
  {
    int i = paramInt + 1;
    int[] arrayOfInt1 = super.onCreateDrawableState(i);
    if (this.mIsConnecting)
    {
      int[] arrayOfInt2 = CHECKABLE_STATE_SET;
      int[] arrayOfInt3 = mergeDrawableStates(arrayOfInt1, arrayOfInt2);
    }
    while (true)
    {
      return arrayOfInt1;
      if (this.mRemoteActive)
      {
        int[] arrayOfInt4 = CHECKED_STATE_SET;
        int[] arrayOfInt5 = mergeDrawableStates(arrayOfInt1, arrayOfInt4);
      }
    }
  }

  public void onDetachedFromWindow()
  {
    this.mAttachedToWindow = false;
    if (!this.mSelector.isEmpty())
    {
      MediaRouter localMediaRouter = this.mRouter;
      MediaRouterCallback localMediaRouterCallback = this.mCallback;
      localMediaRouter.removeCallback(localMediaRouterCallback);
    }
    super.onDetachedFromWindow();
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    if (this.mRemoteIndicator == null)
      return;
    int i = getPaddingLeft();
    int j = getWidth();
    int k = getPaddingRight();
    int m = j - k;
    int n = getPaddingTop();
    int i1 = getHeight();
    int i2 = getPaddingBottom();
    int i3 = i1 - i2;
    int i4 = this.mRemoteIndicator.getIntrinsicWidth();
    int i5 = this.mRemoteIndicator.getIntrinsicHeight();
    int i6 = (m - i - i4) / 2;
    int i7 = i + i6;
    int i8 = (i3 - n - i5) / 2;
    int i9 = n + i8;
    Drawable localDrawable = this.mRemoteIndicator;
    int i10 = i7 + i4;
    int i11 = i9 + i5;
    localDrawable.setBounds(i7, i9, i10, i11);
    this.mRemoteIndicator.draw(paramCanvas);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = View.MeasureSpec.getSize(paramInt2);
    int k = View.MeasureSpec.getMode(paramInt1);
    int m = View.MeasureSpec.getMode(paramInt2);
    int n = this.mMinWidth;
    int i1;
    int i2;
    int i5;
    int i8;
    label134: int i11;
    if (this.mRemoteIndicator != null)
    {
      i1 = this.mRemoteIndicator.getIntrinsicWidth();
      i2 = Math.max(n, i1);
      int i3 = this.mMinHeight;
      if (this.mRemoteIndicator != null)
        int i4 = this.mRemoteIndicator.getIntrinsicHeight();
      i5 = Math.max(i3, 0);
      switch (k)
      {
      default:
        int i6 = getPaddingLeft() + i2;
        int i7 = getPaddingRight();
        i8 = i6 + i7;
        switch (m)
        {
        default:
          int i9 = getPaddingTop() + i5;
          int i10 = getPaddingBottom();
          i11 = i9 + i10;
        case 1073741824:
        case -2147483648:
        }
        break;
      case 1073741824:
      case -2147483648:
      }
    }
    while (true)
    {
      setMeasuredDimension(i8, i11);
      return;
      i1 = 0;
      break;
      i8 = i;
      break label134;
      int i12 = getPaddingLeft() + i2;
      int i13 = getPaddingRight();
      int i14 = i12 + i13;
      i8 = Math.min(i, i14);
      break label134;
      i11 = j;
      continue;
      int i15 = getPaddingTop() + i5;
      int i16 = getPaddingBottom();
      int i17 = i15 + i16;
      i11 = Math.min(j, i17);
    }
  }

  public boolean performClick()
  {
    boolean bool1 = false;
    boolean bool2 = super.performClick();
    if (!bool2)
      playSoundEffect(0);
    if ((showDialog()) || (bool2))
      bool1 = true;
    return bool1;
  }

  public boolean performLongClick()
  {
    boolean bool1 = true;
    if (super.performLongClick());
    CharSequence localCharSequence;
    while (true)
    {
      return bool1;
      if (!this.mCheatSheetEnabled)
      {
        bool1 = false;
      }
      else
      {
        localCharSequence = getContentDescription();
        if (!TextUtils.isEmpty(localCharSequence))
          break;
        bool1 = false;
      }
    }
    int[] arrayOfInt = new int[2];
    Rect localRect = new Rect();
    getLocationOnScreen(arrayOfInt);
    getWindowVisibleDisplayFrame(localRect);
    Context localContext = getContext();
    int i = getWidth();
    int j = getHeight();
    int k = arrayOfInt[1];
    int m = j / 2;
    int n = k + m;
    int i1 = localContext.getResources().getDisplayMetrics().widthPixels;
    Toast localToast = Toast.makeText(localContext, localCharSequence, 0);
    int i2 = localRect.height();
    if (n < i2)
    {
      int i3 = arrayOfInt[0];
      int i4 = i1 - i3;
      int i5 = i / 2;
      int i6 = i4 - i5;
      localToast.setGravity(8388661, i6, j);
    }
    while (true)
    {
      localToast.show();
      boolean bool2 = performHapticFeedback(0);
      break;
      localToast.setGravity(81, 0, j);
    }
  }

  void setCheatSheetEnabled(boolean paramBoolean)
  {
    this.mCheatSheetEnabled = paramBoolean;
  }

  public void setDialogFactory(MediaRouteDialogFactory paramMediaRouteDialogFactory)
  {
    if (paramMediaRouteDialogFactory == null)
      throw new IllegalArgumentException("factory must not be null");
    this.mDialogFactory = paramMediaRouteDialogFactory;
  }

  public void setRouteSelector(MediaRouteSelector paramMediaRouteSelector)
  {
    if (paramMediaRouteSelector == null)
      throw new IllegalArgumentException("selector must not be null");
    if (this.mSelector.equals(paramMediaRouteSelector))
      return;
    if (this.mAttachedToWindow)
    {
      if (!this.mSelector.isEmpty())
      {
        MediaRouter localMediaRouter1 = this.mRouter;
        MediaRouterCallback localMediaRouterCallback1 = this.mCallback;
        localMediaRouter1.removeCallback(localMediaRouterCallback1);
      }
      if (!paramMediaRouteSelector.isEmpty())
      {
        MediaRouter localMediaRouter2 = this.mRouter;
        MediaRouterCallback localMediaRouterCallback2 = this.mCallback;
        localMediaRouter2.addCallback(paramMediaRouteSelector, localMediaRouterCallback2);
      }
    }
    this.mSelector = paramMediaRouteSelector;
    refreshRoute();
  }

  public void setVisibility(int paramInt)
  {
    super.setVisibility(paramInt);
    if (this.mRemoteIndicator == null)
      return;
    Drawable localDrawable = this.mRemoteIndicator;
    if (getVisibility() == 0);
    for (boolean bool1 = true; ; bool1 = false)
    {
      boolean bool2 = localDrawable.setVisible(bool1, false);
      return;
    }
  }

  public boolean showDialog()
  {
    boolean bool = false;
    if (!this.mAttachedToWindow);
    FragmentManager localFragmentManager;
    while (true)
    {
      return bool;
      localFragmentManager = getFragmentManager();
      if (localFragmentManager == null)
        throw new IllegalStateException("The activity must be a subclass of FragmentActivity");
      MediaRouter.RouteInfo localRouteInfo = this.mRouter.getSelectedRoute();
      if (!localRouteInfo.isDefault())
      {
        MediaRouteSelector localMediaRouteSelector1 = this.mSelector;
        if (localRouteInfo.matchesSelector(localMediaRouteSelector1))
          break label121;
      }
      if (localFragmentManager.findFragmentByTag("android.support.v7.mediarouter:MediaRouteChooserDialogFragment") == null)
        break;
      int i = Log.w("MediaRouteButton", "showDialog(): Route chooser dialog already showing!");
    }
    MediaRouteChooserDialogFragment localMediaRouteChooserDialogFragment = this.mDialogFactory.onCreateChooserDialogFragment();
    MediaRouteSelector localMediaRouteSelector2 = this.mSelector;
    localMediaRouteChooserDialogFragment.setRouteSelector(localMediaRouteSelector2);
    localMediaRouteChooserDialogFragment.show(localFragmentManager, "android.support.v7.mediarouter:MediaRouteChooserDialogFragment");
    while (true)
    {
      bool = true;
      break;
      label121: if (localFragmentManager.findFragmentByTag("android.support.v7.mediarouter:MediaRouteControllerDialogFragment") != null)
      {
        int j = Log.w("MediaRouteButton", "showDialog(): Route controller dialog already showing!");
        break;
      }
      this.mDialogFactory.onCreateControllerDialogFragment().show(localFragmentManager, "android.support.v7.mediarouter:MediaRouteControllerDialogFragment");
    }
  }

  protected boolean verifyDrawable(Drawable paramDrawable)
  {
    if (!super.verifyDrawable(paramDrawable))
    {
      Drawable localDrawable = this.mRemoteIndicator;
      if (paramDrawable != localDrawable)
        break label22;
    }
    label22: for (boolean bool = true; ; bool = false)
      return bool;
  }

  private final class MediaRouterCallback extends MediaRouter.Callback
  {
    private MediaRouterCallback()
    {
    }

    public void onProviderAdded(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onProviderChanged(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onProviderRemoved(MediaRouter paramMediaRouter, MediaRouter.ProviderInfo paramProviderInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onRouteAdded(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onRouteChanged(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onRouteRemoved(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onRouteSelected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }

    public void onRouteUnselected(MediaRouter paramMediaRouter, MediaRouter.RouteInfo paramRouteInfo)
    {
      MediaRouteButton.this.refreshRoute();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.app.MediaRouteButton
 * JD-Core Version:    0.6.2
 */