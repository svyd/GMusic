package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.CapturedViewProperty;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

abstract class AdapterViewICS<T extends Adapter> extends ViewGroup
{
  boolean mBlockLayoutRequests = false;
  boolean mDataChanged;
  private boolean mDesiredFocusableInTouchModeState;
  private boolean mDesiredFocusableState;
  private View mEmptyView;

  @ViewDebug.ExportedProperty(category="scrolling")
  int mFirstPosition = 0;
  boolean mInLayout = false;

  @ViewDebug.ExportedProperty(category="list")
  int mItemCount;
  private int mLayoutHeight;
  boolean mNeedSync = false;

  @ViewDebug.ExportedProperty(category="list")
  int mNextSelectedPosition = -1;
  long mNextSelectedRowId = -9223372036854775808L;
  int mOldItemCount;
  int mOldSelectedPosition = -1;
  long mOldSelectedRowId = -9223372036854775808L;
  OnItemClickListener mOnItemClickListener;
  OnItemSelectedListener mOnItemSelectedListener;

  @ViewDebug.ExportedProperty(category="list")
  int mSelectedPosition = -1;
  long mSelectedRowId = -9223372036854775808L;
  private AdapterViewICS<T>.SelectionNotifier mSelectionNotifier;
  int mSpecificTop;
  long mSyncHeight;
  int mSyncMode;
  int mSyncPosition;
  long mSyncRowId = -9223372036854775808L;

  AdapterViewICS(Context paramContext)
  {
    super(paramContext);
  }

  AdapterViewICS(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  AdapterViewICS(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }

  private void fireOnSelected()
  {
    if (this.mOnItemSelectedListener == null)
      return;
    int i = getSelectedItemPosition();
    if (i >= 0)
    {
      View localView = getSelectedView();
      OnItemSelectedListener localOnItemSelectedListener = this.mOnItemSelectedListener;
      long l = getAdapter().getItemId(i);
      AdapterViewICS localAdapterViewICS = this;
      localOnItemSelectedListener.onItemSelected(localAdapterViewICS, localView, i, l);
      return;
    }
    this.mOnItemSelectedListener.onNothingSelected(this);
  }

  private void updateEmptyStatus(boolean paramBoolean)
  {
    if (isInFilterMode())
      paramBoolean = false;
    if (paramBoolean)
    {
      if (this.mEmptyView != null)
      {
        this.mEmptyView.setVisibility(0);
        setVisibility(8);
      }
      while (true)
      {
        if (!this.mDataChanged)
          return;
        int i = getLeft();
        int j = getTop();
        int k = getRight();
        int m = getBottom();
        onLayout(false, i, j, k, m);
        return;
        setVisibility(0);
      }
    }
    if (this.mEmptyView != null)
      this.mEmptyView.setVisibility(8);
    setVisibility(0);
  }

  public void addView(View paramView)
  {
    throw new UnsupportedOperationException("addView(View) is not supported in AdapterView");
  }

  public void addView(View paramView, int paramInt)
  {
    throw new UnsupportedOperationException("addView(View, int) is not supported in AdapterView");
  }

  public void addView(View paramView, int paramInt, ViewGroup.LayoutParams paramLayoutParams)
  {
    throw new UnsupportedOperationException("addView(View, int, LayoutParams) is not supported in AdapterView");
  }

  public void addView(View paramView, ViewGroup.LayoutParams paramLayoutParams)
  {
    throw new UnsupportedOperationException("addView(View, LayoutParams) is not supported in AdapterView");
  }

  protected boolean canAnimate()
  {
    if ((super.canAnimate()) && (this.mItemCount > 0));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  void checkFocus()
  {
    boolean bool1 = false;
    Adapter localAdapter = getAdapter();
    int i;
    int j;
    label36: boolean bool2;
    if ((localAdapter == null) || (localAdapter.getCount() == 0))
    {
      i = 1;
      if ((i != 0) && (!isInFilterMode()))
        break label112;
      j = 1;
      if ((j == 0) || (!this.mDesiredFocusableInTouchModeState))
        break label118;
      bool2 = true;
      label51: super.setFocusableInTouchMode(bool2);
      if ((j == 0) || (!this.mDesiredFocusableState))
        break label124;
    }
    label112: label118: label124: for (boolean bool3 = true; ; bool3 = false)
    {
      super.setFocusable(bool3);
      if (this.mEmptyView == null)
        return;
      if ((localAdapter == null) || (localAdapter.isEmpty()))
        bool1 = true;
      updateEmptyStatus(bool1);
      return;
      i = 0;
      break;
      j = 0;
      break label36;
      bool2 = false;
      break label51;
    }
  }

  void checkSelectionChanged()
  {
    int i = this.mSelectedPosition;
    int j = this.mOldSelectedPosition;
    if (i != j)
    {
      long l1 = this.mSelectedRowId;
      long l2 = this.mOldSelectedRowId;
      if (l1 == l2)
        return;
    }
    selectionChanged();
    int k = this.mSelectedPosition;
    this.mOldSelectedPosition = k;
    long l3 = this.mSelectedRowId;
    this.mOldSelectedRowId = l3;
  }

  public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent paramAccessibilityEvent)
  {
    View localView = getSelectedView();
    if ((localView != null) && (localView.getVisibility() == 0) && (localView.dispatchPopulateAccessibilityEvent(paramAccessibilityEvent)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  protected void dispatchRestoreInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchThawSelfOnly(paramSparseArray);
  }

  protected void dispatchSaveInstanceState(SparseArray<Parcelable> paramSparseArray)
  {
    dispatchFreezeSelfOnly(paramSparseArray);
  }

  int findSyncPosition()
  {
    int i = this.mItemCount;
    int j;
    if (i == 0)
      j = -1;
    long l1;
    long l2;
    int i1;
    Adapter localAdapter;
    while (true)
    {
      return j;
      l1 = this.mSyncRowId;
      int k = this.mSyncPosition;
      if (l1 == -9223372036854775808L)
      {
        j = -1;
      }
      else
      {
        int m = Math.max(0, k);
        j = Math.min(i + -1, m);
        l2 = SystemClock.uptimeMillis() + 100L;
        n = j;
        i1 = j;
        n = 0;
        localAdapter = getAdapter();
        if (localAdapter != null)
          break;
        j = -1;
      }
    }
    label92: int i2;
    if ((i2 != 0) || ((n != 0) && (localAdapter == 0)))
    {
      i1 += 1;
      j = i1;
    }
    for (int n = 0; ; n = 1)
    {
      int i3;
      label157: label181: label187: label191: 
      do
      {
        if (SystemClock.uptimeMillis() <= l2)
        {
          if (localAdapter.getItemId(j) == l1)
            break;
          int i4 = i + -1;
          if (i1 == i4)
            break label181;
          i3 = 1;
          if (n != 0)
            break label187;
        }
        for (i2 = 1; ; i2 = 0)
        {
          if ((i3 == 0) || (i2 == 0))
            break label191;
          j = -1;
          break;
          i3 = 0;
          break label157;
        }
        break label92;
      }
      while ((i3 == 0) && ((n != 0) || (i2 != 0)));
      n += -1;
      j = n;
    }
  }

  public abstract T getAdapter();

  public long getItemIdAtPosition(int paramInt)
  {
    Adapter localAdapter = getAdapter();
    if ((localAdapter == null) || (paramInt < 0));
    for (long l = -9223372036854775808L; ; l = localAdapter.getItemId(paramInt))
      return l;
  }

  @ViewDebug.CapturedViewProperty
  public long getSelectedItemId()
  {
    return this.mNextSelectedRowId;
  }

  @ViewDebug.CapturedViewProperty
  public int getSelectedItemPosition()
  {
    return this.mNextSelectedPosition;
  }

  public abstract View getSelectedView();

  void handleDataChanged()
  {
    int i = this.mItemCount;
    int j = 0;
    if (i > 0)
    {
      int k;
      if (this.mNeedSync)
      {
        this.mNeedSync = false;
        k = findSyncPosition();
        if ((k >= 0) && (lookForSelectablePosition(k, true) != k))
        {
          setNextSelectedPositionInt(k);
          j = 1;
        }
      }
      if (j == 0)
      {
        k = getSelectedItemPosition();
        if (k >= i)
          int m = i + -1;
        if (k < 0);
        int n = lookForSelectablePosition(k, true);
        if (n < 0)
          int i1 = lookForSelectablePosition(k, false);
        if (n >= 0)
        {
          setNextSelectedPositionInt(n);
          checkSelectionChanged();
          j = 1;
        }
      }
    }
    if (j != 0)
      return;
    this.mSelectedPosition = -1;
    this.mSelectedRowId = -9223372036854775808L;
    this.mNextSelectedPosition = -1;
    this.mNextSelectedRowId = -9223372036854775808L;
    this.mNeedSync = false;
    checkSelectionChanged();
  }

  boolean isInFilterMode()
  {
    return false;
  }

  int lookForSelectablePosition(int paramInt, boolean paramBoolean)
  {
    return paramInt;
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    SelectionNotifier localSelectionNotifier = this.mSelectionNotifier;
    boolean bool = removeCallbacks(localSelectionNotifier);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getHeight();
    this.mLayoutHeight = i;
  }

  public boolean performItemClick(View paramView, int paramInt, long paramLong)
  {
    boolean bool = false;
    if (this.mOnItemClickListener != null)
    {
      playSoundEffect(0);
      if (paramView != null)
        paramView.sendAccessibilityEvent(1);
      OnItemClickListener localOnItemClickListener = this.mOnItemClickListener;
      AdapterViewICS localAdapterViewICS = this;
      View localView = paramView;
      int i = paramInt;
      long l = paramLong;
      localOnItemClickListener.onItemClick(localAdapterViewICS, localView, i, l);
      bool = true;
    }
    return bool;
  }

  void rememberSyncState()
  {
    if (getChildCount() <= 0)
      return;
    this.mNeedSync = true;
    long l1 = this.mLayoutHeight;
    this.mSyncHeight = l1;
    if (this.mSelectedPosition >= 0)
    {
      int i = this.mSelectedPosition;
      int j = this.mFirstPosition;
      int k = i - j;
      localView = getChildAt(k);
      long l2 = this.mNextSelectedRowId;
      this.mSyncRowId = l2;
      int m = this.mNextSelectedPosition;
      this.mSyncPosition = m;
      if (localView != null)
      {
        int n = localView.getTop();
        this.mSpecificTop = n;
      }
      this.mSyncMode = 0;
      return;
    }
    View localView = getChildAt(0);
    Adapter localAdapter = getAdapter();
    long l3;
    if (this.mFirstPosition >= 0)
    {
      int i1 = this.mFirstPosition;
      int i2 = localAdapter.getCount();
      if (i1 < i2)
      {
        int i3 = this.mFirstPosition;
        l3 = localAdapter.getItemId(i3);
      }
    }
    for (this.mSyncRowId = l3; ; this.mSyncRowId = 65535L)
    {
      int i4 = this.mFirstPosition;
      this.mSyncPosition = i4;
      if (localView != null)
      {
        int i5 = localView.getTop();
        this.mSpecificTop = i5;
      }
      this.mSyncMode = 1;
      return;
    }
  }

  public void removeAllViews()
  {
    throw new UnsupportedOperationException("removeAllViews() is not supported in AdapterView");
  }

  public void removeView(View paramView)
  {
    throw new UnsupportedOperationException("removeView(View) is not supported in AdapterView");
  }

  public void removeViewAt(int paramInt)
  {
    throw new UnsupportedOperationException("removeViewAt(int) is not supported in AdapterView");
  }

  void selectionChanged()
  {
    if (this.mOnItemSelectedListener != null)
    {
      if ((!this.mInLayout) && (!this.mBlockLayoutRequests))
        break label86;
      if (this.mSelectionNotifier == null)
      {
        SelectionNotifier localSelectionNotifier1 = new SelectionNotifier(null);
        this.mSelectionNotifier = localSelectionNotifier1;
      }
      SelectionNotifier localSelectionNotifier2 = this.mSelectionNotifier;
      boolean bool = post(localSelectionNotifier2);
    }
    while (true)
    {
      if (this.mSelectedPosition == -1)
        return;
      if (!isShown())
        return;
      if (isInTouchMode())
        return;
      sendAccessibilityEvent(4);
      return;
      label86: fireOnSelected();
    }
  }

  public void setFocusable(boolean paramBoolean)
  {
    boolean bool = true;
    Adapter localAdapter = getAdapter();
    int i;
    if ((localAdapter == null) || (localAdapter.getCount() == 0))
    {
      i = 1;
      this.mDesiredFocusableState = paramBoolean;
      if (!paramBoolean)
        this.mDesiredFocusableInTouchModeState = false;
      if ((!paramBoolean) || ((i != 0) && (!isInFilterMode())))
        break label65;
    }
    while (true)
    {
      super.setFocusable(bool);
      return;
      i = 0;
      break;
      label65: bool = false;
    }
  }

  public void setFocusableInTouchMode(boolean paramBoolean)
  {
    boolean bool = true;
    Adapter localAdapter = getAdapter();
    int i;
    if ((localAdapter == null) || (localAdapter.getCount() == 0))
    {
      i = 1;
      this.mDesiredFocusableInTouchModeState = paramBoolean;
      if (paramBoolean)
        this.mDesiredFocusableState = true;
      if ((!paramBoolean) || ((i != 0) && (!isInFilterMode())))
        break label65;
    }
    while (true)
    {
      super.setFocusableInTouchMode(bool);
      return;
      i = 0;
      break;
      label65: bool = false;
    }
  }

  void setNextSelectedPositionInt(int paramInt)
  {
    this.mNextSelectedPosition = paramInt;
    long l1 = getItemIdAtPosition(paramInt);
    this.mNextSelectedRowId = l1;
    if (!this.mNeedSync)
      return;
    if (this.mSyncMode != 0)
      return;
    if (paramInt < 0)
      return;
    this.mSyncPosition = paramInt;
    long l2 = this.mNextSelectedRowId;
    this.mSyncRowId = l2;
  }

  public void setOnClickListener(View.OnClickListener paramOnClickListener)
  {
    throw new RuntimeException("Don't call setOnClickListener for an AdapterView. You probably want setOnItemClickListener instead");
  }

  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener)
  {
    this.mOnItemClickListener = paramOnItemClickListener;
  }

  public void setOnItemSelectedListener(OnItemSelectedListener paramOnItemSelectedListener)
  {
    this.mOnItemSelectedListener = paramOnItemSelectedListener;
  }

  void setSelectedPositionInt(int paramInt)
  {
    this.mSelectedPosition = paramInt;
    long l = getItemIdAtPosition(paramInt);
    this.mSelectedRowId = l;
  }

  private class SelectionNotifier
    implements Runnable
  {
    private SelectionNotifier()
    {
    }

    public void run()
    {
      if (AdapterViewICS.this.mDataChanged)
      {
        if (AdapterViewICS.this.getAdapter() == null)
          return;
        boolean bool = AdapterViewICS.this.post(this);
        return;
      }
      AdapterViewICS.this.fireOnSelected();
    }
  }

  class AdapterDataSetObserver extends DataSetObserver
  {
    private Parcelable mInstanceState = null;

    AdapterDataSetObserver()
    {
    }

    public void onChanged()
    {
      AdapterViewICS.this.mDataChanged = true;
      AdapterViewICS localAdapterViewICS1 = AdapterViewICS.this;
      int i = AdapterViewICS.this.mItemCount;
      localAdapterViewICS1.mOldItemCount = i;
      AdapterViewICS localAdapterViewICS2 = AdapterViewICS.this;
      int j = AdapterViewICS.this.getAdapter().getCount();
      localAdapterViewICS2.mItemCount = j;
      if ((AdapterViewICS.this.getAdapter().hasStableIds()) && (this.mInstanceState != null) && (AdapterViewICS.this.mOldItemCount == 0) && (AdapterViewICS.this.mItemCount > 0))
      {
        AdapterViewICS localAdapterViewICS3 = AdapterViewICS.this;
        Parcelable localParcelable = this.mInstanceState;
        localAdapterViewICS3.onRestoreInstanceState(localParcelable);
        this.mInstanceState = null;
      }
      while (true)
      {
        AdapterViewICS.this.checkFocus();
        AdapterViewICS.this.requestLayout();
        return;
        AdapterViewICS.this.rememberSyncState();
      }
    }

    public void onInvalidated()
    {
      AdapterViewICS.this.mDataChanged = true;
      if (AdapterViewICS.this.getAdapter().hasStableIds())
      {
        Parcelable localParcelable = AdapterViewICS.this.onSaveInstanceState();
        this.mInstanceState = localParcelable;
      }
      AdapterViewICS localAdapterViewICS = AdapterViewICS.this;
      int i = AdapterViewICS.this.mItemCount;
      localAdapterViewICS.mOldItemCount = i;
      AdapterViewICS.this.mItemCount = 0;
      AdapterViewICS.this.mSelectedPosition = -1;
      AdapterViewICS.this.mSelectedRowId = -9223372036854775808L;
      AdapterViewICS.this.mNextSelectedPosition = -1;
      AdapterViewICS.this.mNextSelectedRowId = -9223372036854775808L;
      AdapterViewICS.this.mNeedSync = false;
      AdapterViewICS.this.checkFocus();
      AdapterViewICS.this.requestLayout();
    }
  }

  public static abstract interface OnItemSelectedListener
  {
    public abstract void onItemSelected(AdapterViewICS<?> paramAdapterViewICS, View paramView, int paramInt, long paramLong);

    public abstract void onNothingSelected(AdapterViewICS<?> paramAdapterViewICS);
  }

  class OnItemClickListenerWrapper
    implements AdapterView.OnItemClickListener
  {
    private final AdapterViewICS.OnItemClickListener mWrappedListener;

    public OnItemClickListenerWrapper(AdapterViewICS.OnItemClickListener arg2)
    {
      Object localObject;
      this.mWrappedListener = localObject;
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      AdapterViewICS.OnItemClickListener localOnItemClickListener = this.mWrappedListener;
      AdapterViewICS localAdapterViewICS = AdapterViewICS.this;
      View localView = paramView;
      int i = paramInt;
      long l = paramLong;
      localOnItemClickListener.onItemClick(localAdapterViewICS, localView, i, l);
    }
  }

  public static abstract interface OnItemClickListener
  {
    public abstract void onItemClick(AdapterViewICS<?> paramAdapterViewICS, View paramView, int paramInt, long paramLong);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.AdapterViewICS
 * JD-Core Version:    0.6.2
 */