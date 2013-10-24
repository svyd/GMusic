package com.google.android.music;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.google.android.music.utils.LoggableHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncWorkers;

public class TouchInterceptor extends MusicListView
{
  private static final int MSG_RESET_ITEM_VIEWS = AsyncWorkers.getUniqueMessageType(AsyncWorkers.sUIBackgroundWorker);
  private transient boolean mAwaitingChangeToUpdate;
  private DataSetObserver mDataSetObserver;
  private Bitmap mDragBitmap;
  private DragListener mDragListener;
  private int mDragPointX;
  private int mDragPointY;
  private int mDragPos;
  private ImageView mDragView;
  private DropListener mDropListener;
  private GestureDetector mGestureDetector;
  private int mGrabHandleWidth;
  private int mHeight;
  private int mItemHeightDouble;
  private int mItemHeightHalf;
  private int mItemHeightNormal;
  private int mLowerBound;
  private Runnable mPostResetItemViewSizesRunnable;
  private RemoveListener mRemoveListener;
  private int mRemoveMode = -1;
  private Runnable mResetItemViewSizesRunnable;
  private int mSrcDragPos;
  private Rect mTempRect;
  private final int mTouchSlop;
  private Drawable mTrashcan;
  private int mUpperBound;
  private WindowManager mWindowManager;
  private WindowManager.LayoutParams mWindowParams;
  private int mXOffset;
  private int mYOffset;

  public TouchInterceptor(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    Rect localRect = new Rect();
    this.mTempRect = localRect;
    this.mAwaitingChangeToUpdate = false;
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        if (!TouchInterceptor.this.mAwaitingChangeToUpdate)
          return;
        boolean bool = TouchInterceptor.access$002(TouchInterceptor.this, false);
        TouchInterceptor.this.mResetItemViewSizesRunnable.run();
      }
    };
    this.mDataSetObserver = local1;
    Runnable local2 = new Runnable()
    {
      public void run()
      {
        TouchInterceptor localTouchInterceptor = TouchInterceptor.this;
        Runnable localRunnable = TouchInterceptor.this.mResetItemViewSizesRunnable;
        boolean bool = localTouchInterceptor.post(localRunnable);
      }
    };
    this.mPostResetItemViewSizesRunnable = local2;
    Runnable local3 = new Runnable()
    {
      public void run()
      {
        MusicUtils.assertUiThread();
        LoggableHandler localLoggableHandler = AsyncWorkers.sUIBackgroundWorker;
        int i = TouchInterceptor.MSG_RESET_ITEM_VIEWS;
        localLoggableHandler.removeMessages(i);
        int j = 0;
        View localView = TouchInterceptor.this.getChildAt(j);
        if (TouchInterceptor.this.isHeaderView(localView));
        while (true)
        {
          j += 1;
          break;
          if (localView == null)
          {
            TouchInterceptor.this.layoutChildren();
            localView = TouchInterceptor.this.getChildAt(j);
            if (localView == null)
            {
              TouchInterceptor.this.requestLayout();
              return;
            }
          }
          ViewGroup.LayoutParams localLayoutParams = localView.getLayoutParams();
          int k = TouchInterceptor.this.mItemHeightNormal;
          localLayoutParams.height = k;
          localView.setLayoutParams(localLayoutParams);
          localView.setVisibility(0);
        }
      }
    };
    this.mResetItemViewSizesRunnable = local3;
    int i = ViewConfiguration.get(paramContext).getScaledTouchSlop();
    this.mTouchSlop = i;
    Resources localResources = getResources();
    int j = localResources.getDimensionPixelSize(2131558401);
    this.mItemHeightNormal = j;
    int k = this.mItemHeightNormal / 2;
    this.mItemHeightHalf = k;
    int m = this.mItemHeightNormal * 2;
    this.mItemHeightDouble = m;
    int n = localResources.getDimensionPixelSize(2131558411);
    this.mGrabHandleWidth = n;
  }

  private void adjustScrollBounds(int paramInt)
  {
    int i = this.mHeight / 3;
    if (paramInt >= i)
    {
      int j = this.mHeight / 3;
      this.mUpperBound = j;
    }
    int k = this.mHeight * 2 / 3;
    if (paramInt > k)
      return;
    int m = this.mHeight * 2 / 3;
    this.mLowerBound = m;
  }

  private void doExpansion()
  {
    int i = this.mDragPos;
    int j = getFirstVisiblePosition();
    int k = i - j;
    int m = this.mDragPos;
    int n = this.mSrcDragPos;
    if (m > n)
      k += 1;
    int i1 = getHeaderViewsCount();
    int i2 = this.mSrcDragPos;
    int i3 = getFirstVisiblePosition();
    int i4 = i2 - i3;
    View localView1 = getChildAt(i4);
    int i5 = 0;
    View localView2;
    while (true)
    {
      localView2 = getChildAt(i5);
      if (localView2 == null)
        return;
      if (!isHeaderView(localView2))
        break;
      i5 += 1;
    }
    int i6 = this.mItemHeightNormal;
    int i7 = 0;
    if ((this.mDragPos < i1) && (i5 != i1))
      if (localView2.equals(localView1))
        i7 = 4;
    while (true)
    {
      ViewGroup.LayoutParams localLayoutParams = localView2.getLayoutParams();
      localLayoutParams.height = i6;
      localView2.setLayoutParams(localLayoutParams);
      localView2.setVisibility(i7);
      break;
      i6 = this.mItemHeightDouble;
      continue;
      if (localView2.equals(localView1))
      {
        int i8 = this.mDragPos;
        int i9 = this.mSrcDragPos;
        if (i8 != i9)
        {
          int i10 = getPositionForView(localView2);
          int i11 = getCount() + -1;
          if (i10 == i11);
        }
        else
        {
          i7 = 4;
          continue;
        }
        i6 = 1;
      }
      else if ((i5 != k) && (this.mDragPos >= i1))
      {
        int i12 = this.mDragPos;
        int i13 = getCount() + -1;
        if (i12 < i13)
          i6 = this.mItemHeightDouble;
      }
    }
  }

  private void dragView(int paramInt1, int paramInt2)
  {
    int i;
    if (this.mRemoveMode == 1)
    {
      float f1 = 1.0F;
      i = this.mDragView.getWidth();
      int j = i / 2;
      if (paramInt1 > j)
      {
        float f2 = i - paramInt1;
        float f3 = i / 2;
        f1 = f2 / f3;
      }
      this.mWindowParams.alpha = f1;
    }
    WindowManager.LayoutParams localLayoutParams1;
    int i1;
    if ((this.mRemoveMode == 0) || (this.mRemoveMode == 2))
    {
      localLayoutParams1 = this.mWindowParams;
      int k = this.mDragPointX;
      int m = paramInt1 - k;
      int n = this.mXOffset;
      i1 = m + n;
    }
    WindowManager.LayoutParams localLayoutParams4;
    int i7;
    for (localLayoutParams1.x = i1; ; localLayoutParams4.x = i7)
    {
      WindowManager.LayoutParams localLayoutParams2 = this.mWindowParams;
      int i2 = this.mDragPointY;
      int i3 = paramInt2 - i2;
      int i4 = this.mYOffset;
      int i5 = i3 + i4;
      localLayoutParams2.y = i5;
      WindowManager localWindowManager = this.mWindowManager;
      ImageView localImageView = this.mDragView;
      WindowManager.LayoutParams localLayoutParams3 = this.mWindowParams;
      localWindowManager.updateViewLayout(localImageView, localLayoutParams3);
      if (this.mTrashcan == null)
        return;
      i = this.mDragView.getWidth();
      int i6 = getHeight() * 3 / 4;
      if (paramInt2 <= i6)
        break;
      boolean bool1 = this.mTrashcan.setLevel(2);
      return;
      localLayoutParams4 = this.mWindowParams;
      i7 = this.mXOffset;
    }
    if (i > 0)
    {
      int i8 = i / 4;
      if (paramInt1 > i8)
      {
        boolean bool2 = this.mTrashcan.setLevel(1);
        return;
      }
    }
    boolean bool3 = this.mTrashcan.setLevel(0);
  }

  private int getItemForPosition(int paramInt)
  {
    int i = this.mDragPointY;
    int j = paramInt - i;
    int k = this.mItemHeightHalf;
    int m = j - k;
    int n = myPointToPosition(0, m);
    if (n >= 0)
    {
      int i1 = this.mSrcDragPos;
      if (n <= i1)
        n += 1;
    }
    while (true)
    {
      return n;
      if (m < 0)
        n = 0;
    }
  }

  private int myPointToPosition(int paramInt1, int paramInt2)
  {
    int k;
    if (paramInt2 < 0)
    {
      int i = this.mItemHeightNormal + paramInt2;
      int j = myPointToPosition(paramInt1, i);
      if (j > 0)
        k = j + -1;
    }
    while (true)
    {
      return k;
      Rect localRect = this.mTempRect;
      int m = getChildCount() + -1;
      while (true)
      {
        if (m < 0)
          break label97;
        getChildAt(m).getHitRect(localRect);
        if (localRect.contains(paramInt1, paramInt2))
        {
          k = getFirstVisiblePosition() + m;
          break;
        }
        m += -1;
      }
      label97: k = -1;
    }
  }

  private void onDragEnd()
  {
    this.mAwaitingChangeToUpdate = true;
    LoggableHandler localLoggableHandler = AsyncWorkers.sUIBackgroundWorker;
    Runnable localRunnable = this.mPostResetItemViewSizesRunnable;
    Message localMessage = Message.obtain(localLoggableHandler, localRunnable);
    int i = MSG_RESET_ITEM_VIEWS;
    localMessage.what = i;
    boolean bool = AsyncWorkers.sUIBackgroundWorker.sendMessageDelayed(localMessage, 100L);
  }

  private void startDragging(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    stopDragging();
    WindowManager.LayoutParams localLayoutParams1 = new WindowManager.LayoutParams();
    this.mWindowParams = localLayoutParams1;
    this.mWindowParams.gravity = 51;
    WindowManager.LayoutParams localLayoutParams2 = this.mWindowParams;
    int i = this.mDragPointX;
    int j = paramInt1 - i;
    int k = this.mXOffset;
    int m = j + k;
    localLayoutParams2.x = m;
    WindowManager.LayoutParams localLayoutParams3 = this.mWindowParams;
    int n = this.mDragPointY;
    int i1 = paramInt2 - n;
    int i2 = this.mYOffset;
    int i3 = i1 + i2;
    localLayoutParams3.y = i3;
    this.mWindowParams.height = -1;
    WindowManager.LayoutParams localLayoutParams4 = this.mWindowParams;
    int i4 = getWidth();
    int i5 = getPaddingLeft();
    int i6 = i4 - i5;
    int i7 = getPaddingRight();
    int i8 = i6 - i7;
    localLayoutParams4.width = i8;
    this.mWindowParams.flags = 920;
    this.mWindowParams.format = -1;
    this.mWindowParams.windowAnimations = 0;
    Context localContext = getContext();
    ImageView localImageView = new ImageView(localContext);
    localImageView.setBackgroundResource(2130837862);
    localImageView.setPadding(0, 0, 0, 0);
    localImageView.setImageBitmap(paramBitmap);
    this.mDragBitmap = paramBitmap;
    WindowManager localWindowManager1 = (WindowManager)localContext.getSystemService("window");
    this.mWindowManager = localWindowManager1;
    WindowManager localWindowManager2 = this.mWindowManager;
    WindowManager.LayoutParams localLayoutParams5 = this.mWindowParams;
    localWindowManager2.addView(localImageView, localLayoutParams5);
    this.mDragView = localImageView;
  }

  private void stopDragging()
  {
    if (this.mDragView != null)
    {
      this.mDragView.setVisibility(8);
      WindowManager localWindowManager = (WindowManager)getContext().getSystemService("window");
      ImageView localImageView = this.mDragView;
      localWindowManager.removeView(localImageView);
      this.mDragView.setImageDrawable(null);
      this.mDragView = null;
    }
    if (this.mDragBitmap != null)
    {
      this.mDragBitmap.recycle();
      this.mDragBitmap = null;
    }
    if (this.mTrashcan == null)
      return;
    boolean bool = this.mTrashcan.setLevel(0);
  }

  protected void handleDataChanged()
  {
    if (this.mAwaitingChangeToUpdate)
      return;
    super.handleDataChanged();
  }

  protected void layoutChildren()
  {
    if (this.mAwaitingChangeToUpdate)
      return;
    super.layoutChildren();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    clearDisappearingChildren();
  }

  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    if ((this.mRemoveListener != null) && (this.mGestureDetector == null) && (this.mRemoveMode == 0))
    {
      Context localContext = getContext();
      GestureDetector.SimpleOnGestureListener local4 = new GestureDetector.SimpleOnGestureListener()
      {
        public boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
        {
          if (TouchInterceptor.this.mDragView != null)
            if (paramAnonymousFloat1 > 1000.0F)
            {
              Rect localRect = TouchInterceptor.this.mTempRect;
              TouchInterceptor.this.mDragView.getDrawingRect(localRect);
              float f1 = paramAnonymousMotionEvent2.getX();
              float f2 = localRect.right * 2 / 3;
              if (f1 > f2)
              {
                TouchInterceptor.this.stopDragging();
                TouchInterceptor.RemoveListener localRemoveListener = TouchInterceptor.this.mRemoveListener;
                int i = TouchInterceptor.this.mSrcDragPos;
                localRemoveListener.remove(i);
                TouchInterceptor.this.onDragEnd();
              }
            }
          for (boolean bool = true; ; bool = false)
            return bool;
        }
      };
      GestureDetector localGestureDetector = new GestureDetector(localContext, local4);
      this.mGestureDetector = localGestureDetector;
    }
    if ((this.mDragListener != null) || (this.mDropListener != null))
      switch (paramMotionEvent.getAction())
      {
      default:
      case 0:
      }
    while (true)
    {
      for (boolean bool = super.onInterceptTouchEvent(paramMotionEvent); ; bool = false)
      {
        return bool;
        int i = (int)paramMotionEvent.getX();
        int j = (int)paramMotionEvent.getY();
        int k = pointToPosition(i, j);
        if (k == -1)
          break;
        int m = getHeaderViewsCount();
        if (k < m)
          break;
        int n = getFirstVisiblePosition();
        int i1 = k - n;
        View localView = getChildAt(i1);
        int i2 = localView.getLeft();
        int i3 = i - i2;
        this.mDragPointX = i3;
        int i4 = localView.getTop();
        int i5 = j - i4;
        this.mDragPointY = i5;
        int i6 = (int)paramMotionEvent.getRawX() - i;
        this.mXOffset = i6;
        int i7 = (int)paramMotionEvent.getRawY() - j;
        this.mYOffset = i7;
        int i8 = this.mGrabHandleWidth;
        if (i >= i8)
          break label383;
        localView.setDrawingCacheEnabled(true);
        localView.invalidate();
        Bitmap localBitmap = Bitmap.createBitmap(localView.getDrawingCache());
        startDragging(localBitmap, i, j);
        this.mDragPos = k;
        int i9 = this.mDragPos;
        this.mSrcDragPos = i9;
        int i10 = getHeight();
        this.mHeight = i10;
        int i11 = this.mTouchSlop;
        int i12 = j - i11;
        int i13 = this.mHeight / 3;
        int i14 = Math.min(i12, i13);
        this.mUpperBound = i14;
        int i15 = j + i11;
        int i16 = this.mHeight * 2 / 3;
        int i17 = Math.max(i15, i16);
        this.mLowerBound = i17;
      }
      label383: stopDragging();
    }
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1 = true;
    if (this.mGestureDetector != null)
      boolean bool2 = this.mGestureDetector.onTouchEvent(paramMotionEvent);
    int i;
    int j;
    int k;
    int m;
    if (((this.mDragListener != null) || (this.mDropListener != null)) && (this.mDragView != null))
    {
      i = paramMotionEvent.getAction();
      j = getCount();
      k = getHeaderViewsCount();
      m = getFooterViewsCount();
      switch (i)
      {
      default:
      case 1:
      case 3:
      case 0:
      case 2:
      }
    }
    while (true)
    {
      return bool1;
      Rect localRect = this.mTempRect;
      this.mDragView.getDrawingRect(localRect);
      stopDragging();
      if (this.mRemoveMode == 1)
      {
        float f1 = paramMotionEvent.getX();
        float f2 = localRect.right * 3 / 4;
        if (f1 > f2)
        {
          if (this.mRemoveListener != null)
          {
            RemoveListener localRemoveListener = this.mRemoveListener;
            int n = this.mSrcDragPos - k;
            localRemoveListener.remove(n);
          }
          onDragEnd();
        }
      }
      else
      {
        if (this.mDropListener != null)
        {
          if (this.mDragPos < k)
            this.mDragPos = k;
          int i1 = this.mDragPos;
          int i2 = j - m;
          if (i1 >= i2)
          {
            int i3 = j - m + -1;
            this.mDragPos = i3;
          }
          DropListener localDropListener = this.mDropListener;
          int i4 = this.mSrcDragPos - k;
          int i5 = this.mDragPos - k;
          localDropListener.drop(i4, i5);
        }
        onDragEnd();
        continue;
        int i6 = (int)paramMotionEvent.getX();
        int i7 = (int)paramMotionEvent.getY();
        dragView(i6, i7);
        int i8 = getItemForPosition(i7);
        if (i8 >= 0)
        {
          if (i != 0)
          {
            int i9 = this.mDragPos;
            if (i8 == i9);
          }
          else
          {
            if (this.mDragListener != null)
            {
              DragListener localDragListener = this.mDragListener;
              int i10 = this.mDragPos - k;
              int i11 = i8 - k;
              localDragListener.drag(i10, i11);
            }
            this.mDragPos = i8;
            doExpansion();
          }
          int i12 = 0;
          adjustScrollBounds(i7);
          int i13 = this.mLowerBound;
          if (i7 > i13)
          {
            int i14 = getLastVisiblePosition();
            int i15 = getCount() + -1;
            if (i14 < i15)
            {
              int i16 = this.mHeight;
              int i17 = this.mLowerBound;
              int i18 = (i16 + i17) / 2;
              if (i7 > i18)
                i12 = 16;
            }
          }
          label565: label570: 
          while (true)
          {
            label468: if (i12 != 0)
            {
              smoothScrollBy(i12, 30);
              break;
              i12 = 4;
              continue;
              i12 = 1;
              continue;
              int i19 = this.mUpperBound;
              if (i7 >= i19)
                continue;
              int i20 = this.mUpperBound / 2;
              if (i7 >= i20)
                break label565;
            }
            for (i12 = 65520; ; i12 = -1)
            {
              if (getFirstVisiblePosition() != 0)
                break label570;
              int i21 = getChildAt(0).getTop();
              int i22 = getPaddingTop();
              if (i21 < i22)
                break label468;
              i12 = 0;
              break label468;
              break;
            }
          }
          bool1 = super.onTouchEvent(paramMotionEvent);
        }
      }
    }
  }

  public void requestLayout()
  {
    if (this.mAwaitingChangeToUpdate)
      return;
    super.requestLayout();
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    ListAdapter localListAdapter = getAdapter();
    if (localListAdapter != null)
    {
      DataSetObserver localDataSetObserver1 = this.mDataSetObserver;
      localListAdapter.unregisterDataSetObserver(localDataSetObserver1);
    }
    super.setAdapter(paramListAdapter);
    if (paramListAdapter == null)
      return;
    DataSetObserver localDataSetObserver2 = this.mDataSetObserver;
    paramListAdapter.registerDataSetObserver(localDataSetObserver2);
  }

  public void setDragListener(DragListener paramDragListener)
  {
    this.mDragListener = paramDragListener;
  }

  public void setDropListener(DropListener paramDropListener)
  {
    this.mDropListener = paramDropListener;
  }

  public void setRemoveListener(RemoveListener paramRemoveListener)
  {
    this.mRemoveListener = paramRemoveListener;
  }

  public void setTrashcan(Drawable paramDrawable)
  {
    this.mTrashcan = paramDrawable;
    this.mRemoveMode = 2;
  }

  public static abstract interface RemoveListener
  {
    public abstract void remove(int paramInt);
  }

  public static abstract interface DropListener
  {
    public abstract void drop(int paramInt1, int paramInt2);
  }

  public static abstract interface DragListener
  {
    public abstract void drag(int paramInt1, int paramInt2);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.TouchInterceptor
 * JD-Core Version:    0.6.2
 */