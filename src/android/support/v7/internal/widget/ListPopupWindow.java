package android.support.v7.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import java.util.Locale;

public class ListPopupWindow
{
  private ListAdapter mAdapter;
  private Context mContext;
  private boolean mDropDownAlwaysVisible = false;
  private View mDropDownAnchorView;
  private int mDropDownHeight = -1;
  private int mDropDownHorizontalOffset;
  private DropDownListView mDropDownList;
  private Drawable mDropDownListHighlight;
  private int mDropDownVerticalOffset;
  private boolean mDropDownVerticalOffsetSet;
  private int mDropDownWidth = -1;
  private boolean mForceIgnoreOutsideTouch = false;
  private Handler mHandler;
  private final ListSelectorHider mHideSelector;
  private AdapterView.OnItemClickListener mItemClickListener;
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  int mListItemExpandMaximum = 2147483647;
  private boolean mModal;
  private DataSetObserver mObserver;
  private PopupWindow mPopup;
  private int mPromptPosition = 0;
  private View mPromptView;
  private final ResizePopupRunnable mResizePopupRunnable;
  private final PopupScrollListener mScrollListener;
  private Runnable mShowDropDownRunnable;
  private Rect mTempRect;
  private final PopupTouchInterceptor mTouchInterceptor;

  public ListPopupWindow(Context paramContext)
  {
    this(paramContext, null, i);
  }

  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    ResizePopupRunnable localResizePopupRunnable = new ResizePopupRunnable(null);
    this.mResizePopupRunnable = localResizePopupRunnable;
    PopupTouchInterceptor localPopupTouchInterceptor = new PopupTouchInterceptor(null);
    this.mTouchInterceptor = localPopupTouchInterceptor;
    PopupScrollListener localPopupScrollListener = new PopupScrollListener(null);
    this.mScrollListener = localPopupScrollListener;
    ListSelectorHider localListSelectorHider = new ListSelectorHider(null);
    this.mHideSelector = localListSelectorHider;
    Handler localHandler = new Handler();
    this.mHandler = localHandler;
    Rect localRect = new Rect();
    this.mTempRect = localRect;
    this.mContext = paramContext;
    PopupWindow localPopupWindow = new PopupWindow(paramContext, paramAttributeSet, paramInt);
    this.mPopup = localPopupWindow;
    this.mPopup.setInputMethodMode(1);
    Locale localLocale = this.mContext.getResources().getConfiguration().locale;
  }

  private int buildDropDown()
  {
    int i = 0;
    boolean bool1;
    Object localObject;
    View localView1;
    LinearLayout localLinearLayout;
    LinearLayout.LayoutParams localLayoutParams1;
    label329: int i4;
    if (this.mDropDownList == null)
    {
      Context localContext = this.mContext;
      ListPopupWindow localListPopupWindow1 = this;
      Runnable local1 = new Runnable()
      {
        public void run()
        {
          View localView = ListPopupWindow.this.getAnchorView();
          if (localView == null)
            return;
          if (localView.getWindowToken() == null)
            return;
          ListPopupWindow.this.show();
        }
      };
      this.mShowDropDownRunnable = local1;
      DropDownListView localDropDownListView1 = new android/support/v7/internal/widget/ListPopupWindow$DropDownListView;
      if (!this.mModal)
      {
        bool1 = true;
        localDropDownListView1.<init>(localContext, bool1);
        this.mDropDownList = localDropDownListView1;
        if (this.mDropDownListHighlight != null)
        {
          DropDownListView localDropDownListView2 = this.mDropDownList;
          Drawable localDrawable1 = this.mDropDownListHighlight;
          localDropDownListView2.setSelector(localDrawable1);
        }
        DropDownListView localDropDownListView3 = this.mDropDownList;
        ListAdapter localListAdapter = this.mAdapter;
        localDropDownListView3.setAdapter(localListAdapter);
        DropDownListView localDropDownListView4 = this.mDropDownList;
        AdapterView.OnItemClickListener localOnItemClickListener = this.mItemClickListener;
        localDropDownListView4.setOnItemClickListener(localOnItemClickListener);
        this.mDropDownList.setFocusable(true);
        this.mDropDownList.setFocusableInTouchMode(true);
        DropDownListView localDropDownListView5 = this.mDropDownList;
        ListPopupWindow localListPopupWindow2 = this;
        AdapterView.OnItemSelectedListener local2 = new AdapterView.OnItemSelectedListener()
        {
          public void onItemSelected(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
          {
            if (paramAnonymousInt == -1)
              return;
            ListPopupWindow.DropDownListView localDropDownListView = ListPopupWindow.this.mDropDownList;
            if (localDropDownListView == null)
              return;
            boolean bool = ListPopupWindow.DropDownListView.access$502(localDropDownListView, false);
          }

          public void onNothingSelected(AdapterView<?> paramAnonymousAdapterView)
          {
          }
        };
        localDropDownListView5.setOnItemSelectedListener(local2);
        DropDownListView localDropDownListView6 = this.mDropDownList;
        PopupScrollListener localPopupScrollListener = this.mScrollListener;
        localDropDownListView6.setOnScrollListener(localPopupScrollListener);
        if (this.mItemSelectedListener != null)
        {
          DropDownListView localDropDownListView7 = this.mDropDownList;
          AdapterView.OnItemSelectedListener localOnItemSelectedListener = this.mItemSelectedListener;
          localDropDownListView7.setOnItemSelectedListener(localOnItemSelectedListener);
        }
        localObject = this.mDropDownList;
        localView1 = this.mPromptView;
        if (localView1 != null)
        {
          localLinearLayout = new LinearLayout(localContext);
          localLinearLayout.setOrientation(1);
          localLayoutParams1 = new LinearLayout.LayoutParams(-1, 0, 1.0F);
        }
        switch (this.mPromptPosition)
        {
        default:
          StringBuilder localStringBuilder = new StringBuilder().append("Invalid hint position ");
          int j = this.mPromptPosition;
          String str = j;
          int k = Log.e("ListPopupWindow", str);
          int m = View.MeasureSpec.makeMeasureSpec(this.mDropDownWidth, -2147483648);
          localView1.measure(m, 0);
          LinearLayout.LayoutParams localLayoutParams2 = (LinearLayout.LayoutParams)localView1.getLayoutParams();
          int n = localView1.getMeasuredHeight();
          int i1 = localLayoutParams2.topMargin;
          int i2 = n + i1;
          int i3 = localLayoutParams2.bottomMargin;
          i = i2 + i3;
          localObject = localLinearLayout;
          this.mPopup.setContentView((View)localObject);
          label405: i4 = 0;
          Drawable localDrawable2 = this.mPopup.getBackground();
          if (localDrawable2 != null)
          {
            Rect localRect = this.mTempRect;
            boolean bool2 = localDrawable2.getPadding(localRect);
            int i5 = this.mTempRect.top;
            int i6 = this.mTempRect.bottom;
            i4 = i5 + i6;
            if (!this.mDropDownVerticalOffsetSet)
            {
              int i7 = -this.mTempRect.top;
              this.mDropDownVerticalOffset = i7;
            }
            label485: if (this.mPopup.getInputMethodMode() != 2)
              break label673;
          }
          break;
        case 1:
        case 0:
        }
      }
    }
    int i9;
    int i10;
    label673: for (boolean bool3 = true; ; bool3 = false)
    {
      View localView2 = getAnchorView();
      int i8 = this.mDropDownVerticalOffset;
      i9 = getMaxAvailableHeight(localView2, i8, bool3);
      if ((!this.mDropDownAlwaysVisible) && (this.mDropDownHeight != -1))
        break label679;
      i10 = i9 + i4;
      return i10;
      bool1 = false;
      break;
      localLinearLayout.addView((View)localObject, localLayoutParams1);
      localLinearLayout.addView(localView1);
      break label329;
      localLinearLayout.addView(localView1);
      localLinearLayout.addView((View)localObject, localLayoutParams1);
      break label329;
      ViewGroup localViewGroup = (ViewGroup)this.mPopup.getContentView();
      View localView3 = this.mPromptView;
      if (localView3 == null)
        break label405;
      LinearLayout.LayoutParams localLayoutParams3 = (LinearLayout.LayoutParams)localView3.getLayoutParams();
      int i11 = localView3.getMeasuredHeight();
      int i12 = localLayoutParams3.topMargin;
      int i13 = i11 + i12;
      int i14 = localLayoutParams3.bottomMargin;
      i = i13 + i14;
      break label405;
      this.mTempRect.setEmpty();
      break label485;
    }
    label679: int i15;
    switch (this.mDropDownWidth)
    {
    default:
      i15 = View.MeasureSpec.makeMeasureSpec(this.mDropDownWidth, 1073741824);
    case -2:
    case -1:
    }
    while (true)
    {
      DropDownListView localDropDownListView8 = this.mDropDownList;
      int i16 = i9 - i;
      int i17 = localDropDownListView8.measureHeightOfChildrenCompat(i15, 0, -1, i16, -1);
      if (i17 > 0)
        i += i4;
      i10 = i17 + i;
      break;
      int i18 = this.mContext.getResources().getDisplayMetrics().widthPixels;
      int i19 = this.mTempRect.left;
      int i20 = this.mTempRect.right;
      int i21 = i19 + i20;
      i15 = View.MeasureSpec.makeMeasureSpec(i18 - i21, -2147483648);
      continue;
      int i22 = this.mContext.getResources().getDisplayMetrics().widthPixels;
      int i23 = this.mTempRect.left;
      int i24 = this.mTempRect.right;
      int i25 = i23 + i24;
      i15 = View.MeasureSpec.makeMeasureSpec(i22 - i25, 1073741824);
    }
  }

  private void removePromptView()
  {
    if (this.mPromptView == null)
      return;
    ViewParent localViewParent = this.mPromptView.getParent();
    if (!(localViewParent instanceof ViewGroup))
      return;
    ViewGroup localViewGroup = (ViewGroup)localViewParent;
    View localView = this.mPromptView;
    localViewGroup.removeView(localView);
  }

  public void clearListSelection()
  {
    DropDownListView localDropDownListView = this.mDropDownList;
    if (localDropDownListView == null)
      return;
    boolean bool = DropDownListView.access$502(localDropDownListView, true);
    localDropDownListView.requestLayout();
  }

  public void dismiss()
  {
    this.mPopup.dismiss();
    removePromptView();
    this.mPopup.setContentView(null);
    this.mDropDownList = null;
    Handler localHandler = this.mHandler;
    ResizePopupRunnable localResizePopupRunnable = this.mResizePopupRunnable;
    localHandler.removeCallbacks(localResizePopupRunnable);
  }

  public View getAnchorView()
  {
    return this.mDropDownAnchorView;
  }

  public Drawable getBackground()
  {
    return this.mPopup.getBackground();
  }

  public ListView getListView()
  {
    return this.mDropDownList;
  }

  public int getMaxAvailableHeight(View paramView, int paramInt, boolean paramBoolean)
  {
    Rect localRect1 = new Rect();
    paramView.getWindowVisibleDisplayFrame(localRect1);
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    int i = localRect1.bottom;
    if (paramBoolean)
      i = paramView.getContext().getResources().getDisplayMetrics().heightPixels;
    int j = arrayOfInt[1];
    int k = paramView.getHeight();
    int m = j + k;
    int n = i - m - paramInt;
    int i1 = arrayOfInt[1];
    int i2 = localRect1.top;
    int i3 = i1 - i2 + paramInt;
    int i4 = Math.max(n, i3);
    if (this.mPopup.getBackground() != null)
    {
      Drawable localDrawable = this.mPopup.getBackground();
      Rect localRect2 = this.mTempRect;
      boolean bool = localDrawable.getPadding(localRect2);
      int i5 = this.mTempRect.top;
      int i6 = this.mTempRect.bottom;
      int i7 = i5 + i6;
      i4 -= i7;
    }
    return i4;
  }

  public boolean isInputMethodNotNeeded()
  {
    if (this.mPopup.getInputMethodMode() == 2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isShowing()
  {
    return this.mPopup.isShowing();
  }

  public void setAdapter(ListAdapter paramListAdapter)
  {
    if (this.mObserver == null)
    {
      PopupDataSetObserver localPopupDataSetObserver = new PopupDataSetObserver(null);
      this.mObserver = localPopupDataSetObserver;
    }
    while (true)
    {
      this.mAdapter = paramListAdapter;
      if (this.mAdapter != null)
      {
        DataSetObserver localDataSetObserver1 = this.mObserver;
        paramListAdapter.registerDataSetObserver(localDataSetObserver1);
      }
      if (this.mDropDownList == null)
        return;
      DropDownListView localDropDownListView = this.mDropDownList;
      ListAdapter localListAdapter1 = this.mAdapter;
      localDropDownListView.setAdapter(localListAdapter1);
      return;
      if (this.mAdapter != null)
      {
        ListAdapter localListAdapter2 = this.mAdapter;
        DataSetObserver localDataSetObserver2 = this.mObserver;
        localListAdapter2.unregisterDataSetObserver(localDataSetObserver2);
      }
    }
  }

  public void setAnchorView(View paramView)
  {
    this.mDropDownAnchorView = paramView;
  }

  public void setBackgroundDrawable(Drawable paramDrawable)
  {
    this.mPopup.setBackgroundDrawable(paramDrawable);
  }

  public void setContentWidth(int paramInt)
  {
    Drawable localDrawable = this.mPopup.getBackground();
    if (localDrawable != null)
    {
      Rect localRect = this.mTempRect;
      boolean bool = localDrawable.getPadding(localRect);
      int i = this.mTempRect.left;
      int j = this.mTempRect.right;
      int k = i + j + paramInt;
      this.mDropDownWidth = k;
      return;
    }
    setWidth(paramInt);
  }

  public void setHorizontalOffset(int paramInt)
  {
    this.mDropDownHorizontalOffset = paramInt;
  }

  public void setInputMethodMode(int paramInt)
  {
    this.mPopup.setInputMethodMode(paramInt);
  }

  public void setModal(boolean paramBoolean)
  {
    this.mModal = true;
    this.mPopup.setFocusable(paramBoolean);
  }

  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    this.mPopup.setOnDismissListener(paramOnDismissListener);
  }

  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener)
  {
    this.mItemClickListener = paramOnItemClickListener;
  }

  public void setPromptPosition(int paramInt)
  {
    this.mPromptPosition = paramInt;
  }

  public void setSelection(int paramInt)
  {
    DropDownListView localDropDownListView = this.mDropDownList;
    if (!isShowing())
      return;
    if (localDropDownListView == null)
      return;
    boolean bool = DropDownListView.access$502(localDropDownListView, false);
    localDropDownListView.setSelection(paramInt);
    if (localDropDownListView.getChoiceMode() == 0)
      return;
    localDropDownListView.setItemChecked(paramInt, true);
  }

  public void setVerticalOffset(int paramInt)
  {
    this.mDropDownVerticalOffset = paramInt;
    this.mDropDownVerticalOffsetSet = true;
  }

  public void setWidth(int paramInt)
  {
    this.mDropDownWidth = paramInt;
  }

  public void show()
  {
    boolean bool1 = true;
    boolean bool2 = false;
    int i = -1;
    int j = buildDropDown();
    int k = 0;
    int m = 0;
    boolean bool3 = isInputMethodNotNeeded();
    if (this.mPopup.isShowing())
    {
      label66: PopupWindow localPopupWindow1;
      if (this.mDropDownWidth == -1)
      {
        k = -1;
        if (this.mDropDownHeight != -1)
          break label239;
        if (!bool3)
          break label191;
        m = j;
        if (!bool3)
          break label203;
        localPopupWindow1 = this.mPopup;
        if (this.mDropDownWidth != -1)
          break label198;
        label86: localPopupWindow1.setWindowLayoutMode(i, 0);
      }
      while (true)
      {
        PopupWindow localPopupWindow2 = this.mPopup;
        if ((!this.mForceIgnoreOutsideTouch) && (!this.mDropDownAlwaysVisible))
          bool2 = true;
        localPopupWindow2.setOutsideTouchable(bool2);
        PopupWindow localPopupWindow3 = this.mPopup;
        View localView1 = getAnchorView();
        int n = this.mDropDownHorizontalOffset;
        int i1 = this.mDropDownVerticalOffset;
        localPopupWindow3.update(localView1, n, i1, k, m);
        return;
        if (this.mDropDownWidth == -1)
        {
          k = getAnchorView().getWidth();
          break;
        }
        k = this.mDropDownWidth;
        break;
        label191: m = -1;
        break label66;
        label198: i = 0;
        break label86;
        label203: PopupWindow localPopupWindow4 = this.mPopup;
        if (this.mDropDownWidth == -1);
        for (localPopupWindow1 = null; ; localPopupWindow1 = null)
        {
          localPopupWindow4.setWindowLayoutMode(localPopupWindow1, -1);
          break;
        }
        label239: if (this.mDropDownHeight == -1)
          m = j;
        else
          m = this.mDropDownHeight;
      }
    }
    label290: PopupWindow localPopupWindow5;
    if (this.mDropDownWidth == -1)
    {
      k = -1;
      if (this.mDropDownHeight != -1)
        break label497;
      m = -1;
      this.mPopup.setWindowLayoutMode(k, m);
      localPopupWindow5 = this.mPopup;
      if ((this.mForceIgnoreOutsideTouch) || (this.mDropDownAlwaysVisible))
        break label540;
    }
    while (true)
    {
      localPopupWindow5.setOutsideTouchable(bool1);
      PopupWindow localPopupWindow6 = this.mPopup;
      PopupTouchInterceptor localPopupTouchInterceptor = this.mTouchInterceptor;
      localPopupWindow6.setTouchInterceptor(localPopupTouchInterceptor);
      PopupWindow localPopupWindow7 = this.mPopup;
      View localView2 = getAnchorView();
      int i2 = this.mDropDownHorizontalOffset;
      int i3 = this.mDropDownVerticalOffset;
      localPopupWindow7.showAsDropDown(localView2, i2, i3);
      this.mDropDownList.setSelection(-1);
      if ((!this.mModal) || (this.mDropDownList.isInTouchMode()))
        clearListSelection();
      if (this.mModal)
        return;
      Handler localHandler = this.mHandler;
      ListSelectorHider localListSelectorHider = this.mHideSelector;
      boolean bool4 = localHandler.post(localListSelectorHider);
      return;
      if (this.mDropDownWidth == -1)
      {
        PopupWindow localPopupWindow8 = this.mPopup;
        int i4 = getAnchorView().getWidth();
        localPopupWindow8.setWidth(i4);
        break;
      }
      PopupWindow localPopupWindow9 = this.mPopup;
      int i5 = this.mDropDownWidth;
      localPopupWindow9.setWidth(i5);
      break;
      label497: if (this.mDropDownHeight == -1)
      {
        this.mPopup.setHeight(j);
        break label290;
      }
      PopupWindow localPopupWindow10 = this.mPopup;
      int i6 = this.mDropDownHeight;
      localPopupWindow10.setHeight(i6);
      break label290;
      label540: bool1 = false;
    }
  }

  private class PopupScrollListener
    implements AbsListView.OnScrollListener
  {
    private PopupScrollListener()
    {
    }

    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt)
    {
      if (paramInt != 1)
        return;
      if (ListPopupWindow.this.isInputMethodNotNeeded())
        return;
      if (ListPopupWindow.this.mPopup.getContentView() == null)
        return;
      Handler localHandler = ListPopupWindow.this.mHandler;
      ListPopupWindow.ResizePopupRunnable localResizePopupRunnable = ListPopupWindow.this.mResizePopupRunnable;
      localHandler.removeCallbacks(localResizePopupRunnable);
      ListPopupWindow.this.mResizePopupRunnable.run();
    }
  }

  private class PopupTouchInterceptor
    implements View.OnTouchListener
  {
    private PopupTouchInterceptor()
    {
    }

    public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
    {
      int i = paramMotionEvent.getAction();
      int j = (int)paramMotionEvent.getX();
      int k = (int)paramMotionEvent.getY();
      if ((i == 0) && (ListPopupWindow.this.mPopup != null) && (ListPopupWindow.this.mPopup.isShowing()) && (j >= 0))
      {
        int m = ListPopupWindow.this.mPopup.getWidth();
        if ((j < m) && (k >= 0))
        {
          int n = ListPopupWindow.this.mPopup.getHeight();
          if (k < n)
          {
            Handler localHandler1 = ListPopupWindow.this.mHandler;
            ListPopupWindow.ResizePopupRunnable localResizePopupRunnable1 = ListPopupWindow.this.mResizePopupRunnable;
            boolean bool = localHandler1.postDelayed(localResizePopupRunnable1, 250L);
          }
        }
      }
      while (true)
      {
        return false;
        if (i == 1)
        {
          Handler localHandler2 = ListPopupWindow.this.mHandler;
          ListPopupWindow.ResizePopupRunnable localResizePopupRunnable2 = ListPopupWindow.this.mResizePopupRunnable;
          localHandler2.removeCallbacks(localResizePopupRunnable2);
        }
      }
    }
  }

  private class ResizePopupRunnable
    implements Runnable
  {
    private ResizePopupRunnable()
    {
    }

    public void run()
    {
      if (ListPopupWindow.this.mDropDownList == null)
        return;
      int i = ListPopupWindow.this.mDropDownList.getCount();
      int j = ListPopupWindow.this.mDropDownList.getChildCount();
      if (i <= j)
        return;
      int k = ListPopupWindow.this.mDropDownList.getChildCount();
      int m = ListPopupWindow.this.mListItemExpandMaximum;
      if (k > m)
        return;
      ListPopupWindow.this.mPopup.setInputMethodMode(2);
      ListPopupWindow.this.show();
    }
  }

  private class ListSelectorHider
    implements Runnable
  {
    private ListSelectorHider()
    {
    }

    public void run()
    {
      ListPopupWindow.this.clearListSelection();
    }
  }

  private class PopupDataSetObserver extends DataSetObserver
  {
    private PopupDataSetObserver()
    {
    }

    public void onChanged()
    {
      if (!ListPopupWindow.this.isShowing())
        return;
      ListPopupWindow.this.show();
    }

    public void onInvalidated()
    {
      ListPopupWindow.this.dismiss();
    }
  }

  private static class DropDownListView extends ListView
  {
    private boolean mHijackFocus;
    private boolean mListSelectionHidden;

    public DropDownListView(Context paramContext, boolean paramBoolean)
    {
      super(null, i);
      this.mHijackFocus = paramBoolean;
      setCacheColorHint(0);
    }

    public boolean hasFocus()
    {
      if ((this.mHijackFocus) || (super.hasFocus()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean hasWindowFocus()
    {
      if ((this.mHijackFocus) || (super.hasWindowFocus()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isFocused()
    {
      if ((this.mHijackFocus) || (super.isFocused()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    public boolean isInTouchMode()
    {
      if (((this.mHijackFocus) && (this.mListSelectionHidden)) || (super.isInTouchMode()));
      for (boolean bool = true; ; bool = false)
        return bool;
    }

    final int measureHeightOfChildrenCompat(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
      int i = getListPaddingTop();
      int j = getListPaddingBottom();
      int k = getListPaddingLeft();
      int m = getListPaddingRight();
      int n = getDividerHeight();
      Drawable localDrawable = getDivider();
      ListAdapter localListAdapter = getAdapter();
      int i1;
      if (localListAdapter == null)
        i1 = i + j;
      while (true)
      {
        return i1;
        int i2 = i + j;
        int i3;
        label78: View localView;
        int i4;
        int i5;
        int i6;
        if ((n > 0) && (localDrawable != null))
        {
          i3 = n;
          i1 = 0;
          localView = null;
          i4 = 0;
          i5 = localListAdapter.getCount();
          i6 = 0;
        }
        while (true)
        {
          if (i6 >= i5)
            break label322;
          int i7 = localListAdapter.getItemViewType(i6);
          int i8 = i4;
          if (i7 != i8)
          {
            localView = null;
            i4 = i7;
          }
          DropDownListView localDropDownListView = this;
          localView = localListAdapter.getView(i6, localView, localDropDownListView);
          int i9 = localView.getLayoutParams().height;
          int i10;
          if (i9 > 0)
            i10 = 1073741824;
          for (int i11 = View.MeasureSpec.makeMeasureSpec(i9, i10); ; i11 = View.MeasureSpec.makeMeasureSpec(0, 0))
          {
            int i12 = paramInt1;
            localView.measure(i12, i11);
            if (i6 > 0)
              i2 += i3;
            int i13 = localView.getMeasuredHeight();
            i2 += i13;
            int i14 = i2;
            int i15 = paramInt4;
            if (i14 < i15)
              break label293;
            if (paramInt5 >= 0)
            {
              int i16 = paramInt5;
              if ((i6 > i16) && (i1 > 0))
              {
                int i17 = i2;
                int i18 = paramInt4;
                if (i17 == i18)
                  break;
              }
            }
            i1 = paramInt4;
            break;
            i3 = 0;
            break label78;
          }
          label293: if (paramInt5 >= 0)
          {
            int i19 = paramInt5;
            if (i6 >= i19)
              int i20 = i2;
          }
          i6 += 1;
        }
        label322: i1 = i2;
      }
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ListPopupWindow
 * JD-Core Version:    0.6.2
 */