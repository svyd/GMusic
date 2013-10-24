package android.support.v7.internal.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.SpinnerAdapter;

abstract class AbsSpinnerICS extends AdapterViewICS<SpinnerAdapter>
{
  SpinnerAdapter mAdapter;
  boolean mBlockLayoutRequests;
  private DataSetObserver mDataSetObserver;
  int mHeightMeasureSpec;
  final RecycleBin mRecycler;
  int mSelectionBottomPadding = 0;
  int mSelectionLeftPadding = 0;
  int mSelectionRightPadding = 0;
  int mSelectionTopPadding = 0;
  final Rect mSpinnerPadding;
  int mWidthMeasureSpec;

  AbsSpinnerICS(Context paramContext)
  {
    super(paramContext);
    Rect localRect = new Rect();
    this.mSpinnerPadding = localRect;
    RecycleBin localRecycleBin = new RecycleBin();
    this.mRecycler = localRecycleBin;
    initAbsSpinner();
  }

  AbsSpinnerICS(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  AbsSpinnerICS(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    Rect localRect = new Rect();
    this.mSpinnerPadding = localRect;
    RecycleBin localRecycleBin = new RecycleBin();
    this.mRecycler = localRecycleBin;
    initAbsSpinner();
  }

  private void initAbsSpinner()
  {
    setFocusable(true);
    setWillNotDraw(false);
  }

  protected ViewGroup.LayoutParams generateDefaultLayoutParams()
  {
    return new ViewGroup.LayoutParams(-1, -1);
  }

  public SpinnerAdapter getAdapter()
  {
    return this.mAdapter;
  }

  int getChildHeight(View paramView)
  {
    return paramView.getMeasuredHeight();
  }

  int getChildWidth(View paramView)
  {
    return paramView.getMeasuredWidth();
  }

  public View getSelectedView()
  {
    int k;
    if ((this.mItemCount > 0) && (this.mSelectedPosition >= 0))
    {
      int i = this.mSelectedPosition;
      int j = this.mFirstPosition;
      k = i - j;
    }
    for (View localView = getChildAt(k); ; localView = null)
      return localView;
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = getPaddingLeft();
    int k = getPaddingTop();
    int m = getPaddingRight();
    int n = getPaddingBottom();
    Rect localRect1 = this.mSpinnerPadding;
    int i1 = this.mSelectionLeftPadding;
    label74: label100: Rect localRect4;
    if (j > i1)
    {
      localRect1.left = j;
      Rect localRect2 = this.mSpinnerPadding;
      int i2 = this.mSelectionTopPadding;
      if (k <= i2)
        break label536;
      localRect2.top = k;
      Rect localRect3 = this.mSpinnerPadding;
      int i3 = this.mSelectionRightPadding;
      if (m <= i3)
        break label545;
      localRect3.right = m;
      localRect4 = this.mSpinnerPadding;
      int i4 = this.mSelectionBottomPadding;
      if (n <= i4)
        break label554;
    }
    while (true)
    {
      localRect4.bottom = n;
      if (this.mDataChanged)
        handleDataChanged();
      int i5 = 0;
      int i6 = 0;
      int i7 = 1;
      int i8 = getSelectedItemPosition();
      if ((i8 >= 0) && (this.mAdapter != null))
      {
        int i9 = this.mAdapter.getCount();
        if (i8 < i9)
        {
          View localView1 = this.mRecycler.get(i8);
          if (localView1 == null)
          {
            SpinnerAdapter localSpinnerAdapter = this.mAdapter;
            View localView2 = null;
            AbsSpinnerICS localAbsSpinnerICS1 = this;
            localView1 = localSpinnerAdapter.getView(i8, localView2, localAbsSpinnerICS1);
          }
          if (localView1 != null)
            this.mRecycler.put(i8, localView1);
          if (localView1 != null)
          {
            if (localView1.getLayoutParams() == null)
            {
              this.mBlockLayoutRequests = true;
              ViewGroup.LayoutParams localLayoutParams = generateDefaultLayoutParams();
              localView1.setLayoutParams(localLayoutParams);
              this.mBlockLayoutRequests = false;
            }
            AbsSpinnerICS localAbsSpinnerICS2 = this;
            int i10 = paramInt1;
            int i11 = paramInt2;
            localAbsSpinnerICS2.measureChild(localView1, i10, i11);
            int i12 = getChildHeight(localView1);
            int i13 = this.mSpinnerPadding.top;
            int i14 = i12 + i13;
            int i15 = this.mSpinnerPadding.bottom;
            i5 = i14 + i15;
            int i16 = getChildWidth(localView1);
            int i17 = this.mSpinnerPadding.left;
            int i18 = i16 + i17;
            int i19 = this.mSpinnerPadding.right;
            i6 = i18 + i19;
            i7 = 0;
          }
        }
      }
      if (i7 != 0)
      {
        int i20 = this.mSpinnerPadding.top;
        int i21 = this.mSpinnerPadding.bottom;
        i5 = i20 + i21;
        if (i == 0)
        {
          int i22 = this.mSpinnerPadding.left;
          int i23 = this.mSpinnerPadding.right;
          i6 = i22 + i23;
        }
      }
      int i24 = getSuggestedMinimumHeight();
      int i25 = Math.max(i5, i24);
      int i26 = getSuggestedMinimumWidth();
      int i27 = Math.max(i6, i26);
      int i28 = paramInt2;
      int i29 = resolveSize(i25, i28);
      int i30 = paramInt1;
      int i31 = resolveSize(i27, i30);
      setMeasuredDimension(i31, i29);
      int i32 = paramInt2;
      this.mHeightMeasureSpec = i32;
      int i33 = paramInt1;
      this.mWidthMeasureSpec = i33;
      return;
      j = this.mSelectionLeftPadding;
      break;
      label536: k = this.mSelectionTopPadding;
      break label74;
      label545: m = this.mSelectionRightPadding;
      break label100;
      label554: n = this.mSelectionBottomPadding;
    }
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    if (localSavedState.selectedId < 0L)
      return;
    this.mDataChanged = true;
    this.mNeedSync = true;
    long l = localSavedState.selectedId;
    this.mSyncRowId = l;
    int i = localSavedState.position;
    this.mSyncPosition = i;
    this.mSyncMode = 0;
    requestLayout();
  }

  public Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    long l = getSelectedItemId();
    localSavedState.selectedId = l;
    int i;
    if (localSavedState.selectedId >= 0L)
      i = getSelectedItemPosition();
    for (localSavedState.position = i; ; localSavedState.position = -1)
      return localSavedState;
  }

  void recycleAllViews()
  {
    int i = getChildCount();
    RecycleBin localRecycleBin = this.mRecycler;
    int j = this.mFirstPosition;
    int k = 0;
    while (true)
    {
      if (k >= i)
        return;
      View localView = getChildAt(k);
      int m = j + k;
      localRecycleBin.put(m, localView);
      k += 1;
    }
  }

  public void requestLayout()
  {
    if (this.mBlockLayoutRequests)
      return;
    super.requestLayout();
  }

  void resetList()
  {
    this.mDataChanged = false;
    this.mNeedSync = false;
    removeAllViewsInLayout();
    this.mOldSelectedPosition = -1;
    this.mOldSelectedRowId = -9223372036854775808L;
    setSelectedPositionInt(-1);
    setNextSelectedPositionInt(-1);
    invalidate();
  }

  public void setAdapter(SpinnerAdapter paramSpinnerAdapter)
  {
    int i = -1;
    if (this.mAdapter != null)
    {
      SpinnerAdapter localSpinnerAdapter1 = this.mAdapter;
      DataSetObserver localDataSetObserver1 = this.mDataSetObserver;
      localSpinnerAdapter1.unregisterDataSetObserver(localDataSetObserver1);
      resetList();
    }
    this.mAdapter = paramSpinnerAdapter;
    this.mOldSelectedPosition = -1;
    this.mOldSelectedRowId = -9223372036854775808L;
    if (this.mAdapter != null)
    {
      int j = this.mItemCount;
      this.mOldItemCount = j;
      int k = this.mAdapter.getCount();
      this.mItemCount = k;
      checkFocus();
      AdapterViewICS.AdapterDataSetObserver localAdapterDataSetObserver = new AdapterViewICS.AdapterDataSetObserver(this);
      this.mDataSetObserver = localAdapterDataSetObserver;
      SpinnerAdapter localSpinnerAdapter2 = this.mAdapter;
      DataSetObserver localDataSetObserver2 = this.mDataSetObserver;
      localSpinnerAdapter2.registerDataSetObserver(localDataSetObserver2);
      if (this.mItemCount > 0)
        i = 0;
      setSelectedPositionInt(i);
      setNextSelectedPositionInt(i);
      if (this.mItemCount == 0)
        checkSelectionChanged();
    }
    while (true)
    {
      requestLayout();
      return;
      checkFocus();
      resetList();
      checkSelectionChanged();
    }
  }

  public void setSelection(int paramInt)
  {
    setNextSelectedPositionInt(paramInt);
    requestLayout();
    invalidate();
  }

  class RecycleBin
  {
    private final SparseArray<View> mScrapHeap;

    RecycleBin()
    {
      SparseArray localSparseArray = new SparseArray();
      this.mScrapHeap = localSparseArray;
    }

    void clear()
    {
      SparseArray localSparseArray = this.mScrapHeap;
      int i = localSparseArray.size();
      int j = 0;
      while (j < i)
      {
        View localView = (View)localSparseArray.valueAt(j);
        if (localView != null)
          AbsSpinnerICS.this.removeDetachedView(localView, true);
        j += 1;
      }
      localSparseArray.clear();
    }

    View get(int paramInt)
    {
      View localView = (View)this.mScrapHeap.get(paramInt);
      if (localView != null)
        this.mScrapHeap.delete(paramInt);
      return localView;
    }

    public void put(int paramInt, View paramView)
    {
      this.mScrapHeap.put(paramInt, paramView);
    }
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public AbsSpinnerICS.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new AbsSpinnerICS.SavedState(paramAnonymousParcel, null);
      }

      public AbsSpinnerICS.SavedState[] newArray(int paramAnonymousInt)
      {
        return new AbsSpinnerICS.SavedState[paramAnonymousInt];
      }
    };
    int position;
    long selectedId;

    private SavedState(Parcel paramParcel)
    {
      super();
      long l = paramParcel.readLong();
      this.selectedId = l;
      int i = paramParcel.readInt();
      this.position = i;
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("AbsSpinner.SavedState{");
      String str = Integer.toHexString(System.identityHashCode(this));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str).append(" selectedId=");
      long l = this.selectedId;
      StringBuilder localStringBuilder3 = localStringBuilder2.append(l).append(" position=");
      int i = this.position;
      return i + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      long l = this.selectedId;
      paramParcel.writeLong(l);
      int i = this.position;
      paramParcel.writeInt(i);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.AbsSpinnerICS
 * JD-Core Version:    0.6.2
 */