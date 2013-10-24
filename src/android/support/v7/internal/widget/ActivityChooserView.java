package android.support.v7.internal.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R.dimen;
import android.support.v7.appcompat.R.id;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.string;
import android.support.v7.appcompat.R.styleable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class ActivityChooserView extends ViewGroup
{
  private final LinearLayout mActivityChooserContent;
  private final Drawable mActivityChooserContentBackground;
  private final ActivityChooserViewAdapter mAdapter;
  private final Callbacks mCallbacks;
  private int mDefaultActionButtonContentDescription;
  private final FrameLayout mDefaultActivityButton;
  private final ImageView mDefaultActivityButtonImage;
  private final FrameLayout mExpandActivityOverflowButton;
  private final ImageView mExpandActivityOverflowButtonImage;
  private int mInitialActivityCount;
  private boolean mIsAttachedToWindow;
  private boolean mIsSelectingDefaultActivity;
  private final int mListPopupMaxWidth;
  private ListPopupWindow mListPopupWindow;
  private final DataSetObserver mModelDataSetOberver;
  private PopupWindow.OnDismissListener mOnDismissListener;
  private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
  ActionProvider mProvider;

  public ActivityChooserView(Context paramContext)
  {
    this(paramContext, null);
  }

  public ActivityChooserView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
  }

  public ActivityChooserView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        super.onChanged();
        ActivityChooserView.this.mAdapter.notifyDataSetChanged();
      }

      public void onInvalidated()
      {
        super.onInvalidated();
        ActivityChooserView.this.mAdapter.notifyDataSetInvalidated();
      }
    };
    this.mModelDataSetOberver = local1;
    ViewTreeObserver.OnGlobalLayoutListener local2 = new ViewTreeObserver.OnGlobalLayoutListener()
    {
      public void onGlobalLayout()
      {
        if (!ActivityChooserView.this.isShowingPopup())
          return;
        if (!ActivityChooserView.this.isShown())
        {
          ActivityChooserView.this.getListPopupWindow().dismiss();
          return;
        }
        ActivityChooserView.this.getListPopupWindow().show();
        if (ActivityChooserView.this.mProvider == null)
          return;
        ActivityChooserView.this.mProvider.subUiVisibilityChanged(true);
      }
    };
    this.mOnGlobalLayoutListener = local2;
    this.mInitialActivityCount = 4;
    int[] arrayOfInt = R.styleable.ActivityChooserView;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, paramInt, 0);
    int i = localTypedArray.getInt(0, 4);
    this.mInitialActivityCount = i;
    Drawable localDrawable1 = localTypedArray.getDrawable(1);
    localTypedArray.recycle();
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    int j = R.layout.abc_activity_chooser_view;
    View localView = localLayoutInflater.inflate(j, this, true);
    Callbacks localCallbacks1 = new Callbacks(null);
    this.mCallbacks = localCallbacks1;
    int k = R.id.activity_chooser_view_content;
    LinearLayout localLinearLayout = (LinearLayout)findViewById(k);
    this.mActivityChooserContent = localLinearLayout;
    Drawable localDrawable2 = this.mActivityChooserContent.getBackground();
    this.mActivityChooserContentBackground = localDrawable2;
    int m = R.id.default_activity_button;
    FrameLayout localFrameLayout1 = (FrameLayout)findViewById(m);
    this.mDefaultActivityButton = localFrameLayout1;
    FrameLayout localFrameLayout2 = this.mDefaultActivityButton;
    Callbacks localCallbacks2 = this.mCallbacks;
    localFrameLayout2.setOnClickListener(localCallbacks2);
    FrameLayout localFrameLayout3 = this.mDefaultActivityButton;
    Callbacks localCallbacks3 = this.mCallbacks;
    localFrameLayout3.setOnLongClickListener(localCallbacks3);
    FrameLayout localFrameLayout4 = this.mDefaultActivityButton;
    int n = R.id.image;
    ImageView localImageView1 = (ImageView)localFrameLayout4.findViewById(n);
    this.mDefaultActivityButtonImage = localImageView1;
    int i1 = R.id.expand_activities_button;
    FrameLayout localFrameLayout5 = (FrameLayout)findViewById(i1);
    this.mExpandActivityOverflowButton = localFrameLayout5;
    FrameLayout localFrameLayout6 = this.mExpandActivityOverflowButton;
    Callbacks localCallbacks4 = this.mCallbacks;
    localFrameLayout6.setOnClickListener(localCallbacks4);
    FrameLayout localFrameLayout7 = this.mExpandActivityOverflowButton;
    int i2 = R.id.image;
    ImageView localImageView2 = (ImageView)localFrameLayout7.findViewById(i2);
    this.mExpandActivityOverflowButtonImage = localImageView2;
    this.mExpandActivityOverflowButtonImage.setImageDrawable(localDrawable1);
    ActivityChooserViewAdapter localActivityChooserViewAdapter1 = new ActivityChooserViewAdapter(null);
    this.mAdapter = localActivityChooserViewAdapter1;
    ActivityChooserViewAdapter localActivityChooserViewAdapter2 = this.mAdapter;
    DataSetObserver local3 = new DataSetObserver()
    {
      public void onChanged()
      {
        super.onChanged();
        ActivityChooserView.this.updateAppearance();
      }
    };
    localActivityChooserViewAdapter2.registerDataSetObserver(local3);
    Resources localResources = paramContext.getResources();
    int i3 = localResources.getDisplayMetrics().widthPixels / 2;
    int i4 = R.dimen.abc_config_prefDialogWidth;
    int i5 = localResources.getDimensionPixelSize(i4);
    int i6 = Math.max(i3, i5);
    this.mListPopupMaxWidth = i6;
  }

  private ListPopupWindow getListPopupWindow()
  {
    if (this.mListPopupWindow == null)
    {
      Context localContext = getContext();
      ListPopupWindow localListPopupWindow1 = new ListPopupWindow(localContext);
      this.mListPopupWindow = localListPopupWindow1;
      ListPopupWindow localListPopupWindow2 = this.mListPopupWindow;
      ActivityChooserViewAdapter localActivityChooserViewAdapter = this.mAdapter;
      localListPopupWindow2.setAdapter(localActivityChooserViewAdapter);
      this.mListPopupWindow.setAnchorView(this);
      this.mListPopupWindow.setModal(true);
      ListPopupWindow localListPopupWindow3 = this.mListPopupWindow;
      Callbacks localCallbacks1 = this.mCallbacks;
      localListPopupWindow3.setOnItemClickListener(localCallbacks1);
      ListPopupWindow localListPopupWindow4 = this.mListPopupWindow;
      Callbacks localCallbacks2 = this.mCallbacks;
      localListPopupWindow4.setOnDismissListener(localCallbacks2);
    }
    return this.mListPopupWindow;
  }

  private void showPopupUnchecked(int paramInt)
  {
    if (this.mAdapter.getDataModel() == null)
      throw new IllegalStateException("No data model. Did you call #setDataModel?");
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    ViewTreeObserver.OnGlobalLayoutListener localOnGlobalLayoutListener = this.mOnGlobalLayoutListener;
    localViewTreeObserver.addOnGlobalLayoutListener(localOnGlobalLayoutListener);
    boolean bool;
    int j;
    label66: label113: ListPopupWindow localListPopupWindow;
    if (this.mDefaultActivityButton.getVisibility() == 0)
    {
      bool = true;
      int i = this.mAdapter.getActivityCount();
      if (!bool)
        break label242;
      j = 1;
      if (paramInt == 2147483647)
        break label248;
      int k = paramInt + j;
      if (i <= k)
        break label248;
      this.mAdapter.setShowFooterView(true);
      ActivityChooserViewAdapter localActivityChooserViewAdapter = this.mAdapter;
      int m = paramInt + -1;
      localActivityChooserViewAdapter.setMaxActivityCount(m);
      localListPopupWindow = getListPopupWindow();
      if (localListPopupWindow.isShowing())
        return;
      if ((!this.mIsSelectingDefaultActivity) && (bool))
        break label267;
      this.mAdapter.setShowDefaultActivity(true, bool);
    }
    while (true)
    {
      int n = this.mAdapter.measureContentWidth();
      int i1 = this.mListPopupMaxWidth;
      int i2 = Math.min(n, i1);
      localListPopupWindow.setContentWidth(i2);
      localListPopupWindow.show();
      if (this.mProvider != null)
        this.mProvider.subUiVisibilityChanged(true);
      ListView localListView = localListPopupWindow.getListView();
      Context localContext = getContext();
      int i3 = R.string.abc_activitychooserview_choose_application;
      String str = localContext.getString(i3);
      localListView.setContentDescription(str);
      return;
      bool = false;
      break;
      label242: j = 0;
      break label66;
      label248: this.mAdapter.setShowFooterView(false);
      this.mAdapter.setMaxActivityCount(paramInt);
      break label113;
      label267: this.mAdapter.setShowDefaultActivity(false, false);
    }
  }

  private void updateAppearance()
  {
    if (this.mAdapter.getCount() > 0)
    {
      this.mExpandActivityOverflowButton.setEnabled(true);
      int i = this.mAdapter.getActivityCount();
      int j = this.mAdapter.getHistorySize();
      if ((i != 1) && ((i <= 1) || (j <= 0)))
        break label194;
      this.mDefaultActivityButton.setVisibility(0);
      ResolveInfo localResolveInfo = this.mAdapter.getDefaultActivity();
      PackageManager localPackageManager = getContext().getPackageManager();
      ImageView localImageView = this.mDefaultActivityButtonImage;
      Drawable localDrawable1 = localResolveInfo.loadIcon(localPackageManager);
      localImageView.setImageDrawable(localDrawable1);
      if (this.mDefaultActionButtonContentDescription != 0)
      {
        CharSequence localCharSequence = localResolveInfo.loadLabel(localPackageManager);
        Context localContext = getContext();
        int k = this.mDefaultActionButtonContentDescription;
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = localCharSequence;
        String str = localContext.getString(k, arrayOfObject);
        this.mDefaultActivityButton.setContentDescription(str);
      }
    }
    while (true)
    {
      if (this.mDefaultActivityButton.getVisibility() != 0)
        break label206;
      LinearLayout localLinearLayout = this.mActivityChooserContent;
      Drawable localDrawable2 = this.mActivityChooserContentBackground;
      localLinearLayout.setBackgroundDrawable(localDrawable2);
      return;
      this.mExpandActivityOverflowButton.setEnabled(false);
      break;
      label194: this.mDefaultActivityButton.setVisibility(8);
    }
    label206: this.mActivityChooserContent.setBackgroundDrawable(null);
  }

  public boolean dismissPopup()
  {
    if (isShowingPopup())
    {
      getListPopupWindow().dismiss();
      ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
      if (localViewTreeObserver.isAlive())
      {
        ViewTreeObserver.OnGlobalLayoutListener localOnGlobalLayoutListener = this.mOnGlobalLayoutListener;
        localViewTreeObserver.removeGlobalOnLayoutListener(localOnGlobalLayoutListener);
      }
    }
    return true;
  }

  public boolean isShowingPopup()
  {
    return getListPopupWindow().isShowing();
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    ActivityChooserModel localActivityChooserModel = this.mAdapter.getDataModel();
    if (localActivityChooserModel != null)
    {
      DataSetObserver localDataSetObserver = this.mModelDataSetOberver;
      localActivityChooserModel.registerObserver(localDataSetObserver);
    }
    this.mIsAttachedToWindow = true;
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    ActivityChooserModel localActivityChooserModel = this.mAdapter.getDataModel();
    if (localActivityChooserModel != null)
    {
      DataSetObserver localDataSetObserver = this.mModelDataSetOberver;
      localActivityChooserModel.unregisterObserver(localDataSetObserver);
    }
    ViewTreeObserver localViewTreeObserver = getViewTreeObserver();
    if (localViewTreeObserver.isAlive())
    {
      ViewTreeObserver.OnGlobalLayoutListener localOnGlobalLayoutListener = this.mOnGlobalLayoutListener;
      localViewTreeObserver.removeGlobalOnLayoutListener(localOnGlobalLayoutListener);
    }
    if (isShowingPopup())
      boolean bool = dismissPopup();
    this.mIsAttachedToWindow = false;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    LinearLayout localLinearLayout = this.mActivityChooserContent;
    int i = paramInt3 - paramInt1;
    int j = paramInt4 - paramInt2;
    localLinearLayout.layout(0, 0, i, j);
    if (isShowingPopup())
      return;
    boolean bool = dismissPopup();
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    LinearLayout localLinearLayout = this.mActivityChooserContent;
    if (this.mDefaultActivityButton.getVisibility() != 0)
      paramInt2 = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(paramInt2), 1073741824);
    measureChild(localLinearLayout, paramInt1, paramInt2);
    int i = localLinearLayout.getMeasuredWidth();
    int j = localLinearLayout.getMeasuredHeight();
    setMeasuredDimension(i, j);
  }

  public void setActivityChooserModel(ActivityChooserModel paramActivityChooserModel)
  {
    this.mAdapter.setDataModel(paramActivityChooserModel);
    if (!isShowingPopup())
      return;
    boolean bool1 = dismissPopup();
    boolean bool2 = showPopup();
  }

  public void setDefaultActionButtonContentDescription(int paramInt)
  {
    this.mDefaultActionButtonContentDescription = paramInt;
  }

  public void setExpandActivityOverflowButtonContentDescription(int paramInt)
  {
    String str = getContext().getString(paramInt);
    this.mExpandActivityOverflowButtonImage.setContentDescription(str);
  }

  public void setExpandActivityOverflowButtonDrawable(Drawable paramDrawable)
  {
    this.mExpandActivityOverflowButtonImage.setImageDrawable(paramDrawable);
  }

  public void setInitialActivityCount(int paramInt)
  {
    this.mInitialActivityCount = paramInt;
  }

  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener)
  {
    this.mOnDismissListener = paramOnDismissListener;
  }

  public void setProvider(ActionProvider paramActionProvider)
  {
    this.mProvider = paramActionProvider;
  }

  public boolean showPopup()
  {
    boolean bool = false;
    if ((isShowingPopup()) || (!this.mIsAttachedToWindow));
    while (true)
    {
      return bool;
      this.mIsSelectingDefaultActivity = false;
      int i = this.mInitialActivityCount;
      showPopupUnchecked(i);
      bool = true;
    }
  }

  private class ActivityChooserViewAdapter extends BaseAdapter
  {
    private ActivityChooserModel mDataModel;
    private boolean mHighlightDefaultActivity;
    private int mMaxActivityCount = 4;
    private boolean mShowDefaultActivity;
    private boolean mShowFooterView;

    private ActivityChooserViewAdapter()
    {
    }

    public int getActivityCount()
    {
      return this.mDataModel.getActivityCount();
    }

    public int getCount()
    {
      int i = this.mDataModel.getActivityCount();
      if ((!this.mShowDefaultActivity) && (this.mDataModel.getDefaultActivity() != null))
        i += -1;
      int j = this.mMaxActivityCount;
      int k = Math.min(i, j);
      if (this.mShowFooterView)
        k += 1;
      return k;
    }

    public ActivityChooserModel getDataModel()
    {
      return this.mDataModel;
    }

    public ResolveInfo getDefaultActivity()
    {
      return this.mDataModel.getDefaultActivity();
    }

    public int getHistorySize()
    {
      return this.mDataModel.getHistorySize();
    }

    public Object getItem(int paramInt)
    {
      switch (getItemViewType(paramInt))
      {
      default:
        throw new IllegalArgumentException();
      case 1:
      case 0:
      }
      for (Object localObject = null; ; localObject = this.mDataModel.getActivity(paramInt))
      {
        return localObject;
        if ((!this.mShowDefaultActivity) && (this.mDataModel.getDefaultActivity() != null))
          paramInt += 1;
      }
    }

    public long getItemId(int paramInt)
    {
      return paramInt;
    }

    public int getItemViewType(int paramInt)
    {
      if (this.mShowFooterView)
      {
        int i = getCount() + -1;
        if (paramInt == i);
      }
      for (int j = 1; ; j = 0)
        return j;
    }

    public boolean getShowDefaultActivity()
    {
      return this.mShowDefaultActivity;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      switch (getItemViewType(paramInt))
      {
      default:
        throw new IllegalArgumentException();
      case 1:
        if ((paramView == null) || (paramView.getId() != 1))
        {
          LayoutInflater localLayoutInflater1 = LayoutInflater.from(ActivityChooserView.this.getContext());
          int i = R.layout.abc_activity_chooser_view_list_item;
          paramView = localLayoutInflater1.inflate(i, paramViewGroup, false);
          paramView.setId(1);
          int j = R.id.title;
          TextView localTextView1 = (TextView)paramView.findViewById(j);
          Context localContext = ActivityChooserView.this.getContext();
          int k = R.string.abc_activity_chooser_view_see_all;
          String str = localContext.getString(k);
          localTextView1.setText(str);
        }
        break;
      case 0:
      }
      for (View localView = paramView; ; localView = paramView)
      {
        return localView;
        if (paramView != null)
        {
          int m = paramView.getId();
          int n = R.id.list_item;
          if (m == n);
        }
        else
        {
          LayoutInflater localLayoutInflater2 = LayoutInflater.from(ActivityChooserView.this.getContext());
          int i1 = R.layout.abc_activity_chooser_view_list_item;
          paramView = localLayoutInflater2.inflate(i1, paramViewGroup, false);
        }
        PackageManager localPackageManager = ActivityChooserView.this.getContext().getPackageManager();
        int i2 = R.id.icon;
        ImageView localImageView = (ImageView)paramView.findViewById(i2);
        ResolveInfo localResolveInfo = (ResolveInfo)getItem(paramInt);
        Drawable localDrawable = localResolveInfo.loadIcon(localPackageManager);
        localImageView.setImageDrawable(localDrawable);
        int i3 = R.id.title;
        TextView localTextView2 = (TextView)paramView.findViewById(i3);
        CharSequence localCharSequence = localResolveInfo.loadLabel(localPackageManager);
        localTextView2.setText(localCharSequence);
        if ((!this.mShowDefaultActivity) || (paramInt != 0) || (!this.mHighlightDefaultActivity));
      }
    }

    public int getViewTypeCount()
    {
      return 3;
    }

    public int measureContentWidth()
    {
      int i = this.mMaxActivityCount;
      this.mMaxActivityCount = 2147483647;
      int j = 0;
      View localView = null;
      int k = View.MeasureSpec.makeMeasureSpec(0, 0);
      int m = View.MeasureSpec.makeMeasureSpec(0, 0);
      int n = getCount();
      int i1 = 0;
      while (i1 < n)
      {
        localView = getView(i1, localView, null);
        localView.measure(k, m);
        int i2 = localView.getMeasuredWidth();
        j = Math.max(j, i2);
        i1 += 1;
      }
      this.mMaxActivityCount = i;
      return j;
    }

    public void setDataModel(ActivityChooserModel paramActivityChooserModel)
    {
      ActivityChooserModel localActivityChooserModel = ActivityChooserView.this.mAdapter.getDataModel();
      if ((localActivityChooserModel != null) && (ActivityChooserView.this.isShown()))
      {
        DataSetObserver localDataSetObserver1 = ActivityChooserView.this.mModelDataSetOberver;
        localActivityChooserModel.unregisterObserver(localDataSetObserver1);
      }
      this.mDataModel = paramActivityChooserModel;
      if ((paramActivityChooserModel != null) && (ActivityChooserView.this.isShown()))
      {
        DataSetObserver localDataSetObserver2 = ActivityChooserView.this.mModelDataSetOberver;
        paramActivityChooserModel.registerObserver(localDataSetObserver2);
      }
      notifyDataSetChanged();
    }

    public void setMaxActivityCount(int paramInt)
    {
      if (this.mMaxActivityCount != paramInt)
        return;
      this.mMaxActivityCount = paramInt;
      notifyDataSetChanged();
    }

    public void setShowDefaultActivity(boolean paramBoolean1, boolean paramBoolean2)
    {
      if ((this.mShowDefaultActivity != paramBoolean1) && (this.mHighlightDefaultActivity != paramBoolean2))
        return;
      this.mShowDefaultActivity = paramBoolean1;
      this.mHighlightDefaultActivity = paramBoolean2;
      notifyDataSetChanged();
    }

    public void setShowFooterView(boolean paramBoolean)
    {
      if (this.mShowFooterView != paramBoolean)
        return;
      this.mShowFooterView = paramBoolean;
      notifyDataSetChanged();
    }
  }

  private class Callbacks
    implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemClickListener, PopupWindow.OnDismissListener
  {
    private Callbacks()
    {
    }

    private void notifyOnDismissListener()
    {
      if (ActivityChooserView.this.mOnDismissListener == null)
        return;
      ActivityChooserView.this.mOnDismissListener.onDismiss();
    }

    public void onClick(View paramView)
    {
      FrameLayout localFrameLayout1 = ActivityChooserView.this.mDefaultActivityButton;
      if (paramView == localFrameLayout1)
      {
        boolean bool1 = ActivityChooserView.this.dismissPopup();
        ResolveInfo localResolveInfo = ActivityChooserView.this.mAdapter.getDefaultActivity();
        int i = ActivityChooserView.this.mAdapter.getDataModel().getActivityIndex(localResolveInfo);
        Intent localIntent1 = ActivityChooserView.this.mAdapter.getDataModel().chooseActivity(i);
        if (localIntent1 == null)
          return;
        Intent localIntent2 = localIntent1.addFlags(524288);
        ActivityChooserView.this.getContext().startActivity(localIntent1);
        return;
      }
      FrameLayout localFrameLayout2 = ActivityChooserView.this.mExpandActivityOverflowButton;
      if (paramView == localFrameLayout2)
      {
        boolean bool2 = ActivityChooserView.access$602(ActivityChooserView.this, false);
        ActivityChooserView localActivityChooserView = ActivityChooserView.this;
        int j = ActivityChooserView.this.mInitialActivityCount;
        localActivityChooserView.showPopupUnchecked(j);
        return;
      }
      throw new IllegalArgumentException();
    }

    public void onDismiss()
    {
      notifyOnDismissListener();
      if (ActivityChooserView.this.mProvider == null)
        return;
      ActivityChooserView.this.mProvider.subUiVisibilityChanged(false);
    }

    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
    {
      switch (((ActivityChooserView.ActivityChooserViewAdapter)paramAdapterView.getAdapter()).getItemViewType(paramInt))
      {
      default:
        throw new IllegalArgumentException();
      case 1:
        ActivityChooserView.this.showPopupUnchecked(2147483647);
        return;
      case 0:
      }
      boolean bool = ActivityChooserView.this.dismissPopup();
      if (ActivityChooserView.this.mIsSelectingDefaultActivity)
      {
        if (paramInt <= 0)
          return;
        ActivityChooserView.this.mAdapter.getDataModel().setDefaultActivity(paramInt);
        return;
      }
      if (ActivityChooserView.this.mAdapter.getShowDefaultActivity());
      while (true)
      {
        Intent localIntent1 = ActivityChooserView.this.mAdapter.getDataModel().chooseActivity(paramInt);
        if (localIntent1 == null)
          return;
        Intent localIntent2 = localIntent1.addFlags(524288);
        ActivityChooserView.this.getContext().startActivity(localIntent1);
        return;
        paramInt += 1;
      }
    }

    public boolean onLongClick(View paramView)
    {
      FrameLayout localFrameLayout = ActivityChooserView.this.mDefaultActivityButton;
      if (paramView == localFrameLayout)
      {
        if (ActivityChooserView.this.mAdapter.getCount() > 0)
        {
          boolean bool = ActivityChooserView.access$602(ActivityChooserView.this, true);
          ActivityChooserView localActivityChooserView = ActivityChooserView.this;
          int i = ActivityChooserView.this.mInitialActivityCount;
          localActivityChooserView.showPopupUnchecked(i);
        }
        return true;
      }
      throw new IllegalArgumentException();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.widget.ActivityChooserView
 * JD-Core Version:    0.6.2
 */