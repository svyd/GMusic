package android.support.v4.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import java.util.ArrayList;

public class FragmentTabHost extends TabHost
  implements TabHost.OnTabChangeListener
{
  private boolean mAttached;
  private int mContainerId;
  private Context mContext;
  private FragmentManager mFragmentManager;
  private TabInfo mLastTab;
  private TabHost.OnTabChangeListener mOnTabChangeListener;
  private FrameLayout mRealTabContent;
  private final ArrayList<TabInfo> mTabs;

  public FragmentTabHost(Context paramContext)
  {
    super(paramContext, null);
    ArrayList localArrayList = new ArrayList();
    this.mTabs = localArrayList;
    initFragmentTabHost(paramContext, null);
  }

  public FragmentTabHost(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    ArrayList localArrayList = new ArrayList();
    this.mTabs = localArrayList;
    initFragmentTabHost(paramContext, paramAttributeSet);
  }

  private FragmentTransaction doTabChanged(String paramString, FragmentTransaction paramFragmentTransaction)
  {
    Object localObject = null;
    int i = 0;
    while (true)
    {
      int j = this.mTabs.size();
      if (i >= j)
        break;
      TabInfo localTabInfo = (TabInfo)this.mTabs.get(i);
      if (localTabInfo.tag.equals(paramString))
        localObject = localTabInfo;
      i += 1;
    }
    if (localObject == null)
    {
      String str1 = "No tab known for tag " + paramString;
      throw new IllegalStateException(str1);
    }
    if (this.mLastTab != localObject)
    {
      if (paramFragmentTransaction == null)
        paramFragmentTransaction = this.mFragmentManager.beginTransaction();
      if ((this.mLastTab != null) && (this.mLastTab.fragment != null))
      {
        Fragment localFragment1 = this.mLastTab.fragment;
        FragmentTransaction localFragmentTransaction1 = paramFragmentTransaction.detach(localFragment1);
      }
      if (localObject != null)
      {
        if (localObject.fragment != null)
          break label236;
        Context localContext = this.mContext;
        String str2 = localObject.clss.getName();
        Bundle localBundle = localObject.args;
        Fragment localFragment2 = Fragment.instantiate(localContext, str2, localBundle);
        Fragment localFragment3 = TabInfo.access$102(localObject, localFragment2);
        int k = this.mContainerId;
        Fragment localFragment4 = localObject.fragment;
        String str3 = localObject.tag;
        FragmentTransaction localFragmentTransaction2 = paramFragmentTransaction.add(k, localFragment4, str3);
      }
    }
    while (true)
    {
      this.mLastTab = localObject;
      return paramFragmentTransaction;
      label236: Fragment localFragment5 = localObject.fragment;
      FragmentTransaction localFragmentTransaction3 = paramFragmentTransaction.attach(localFragment5);
    }
  }

  private void ensureContent()
  {
    if (this.mRealTabContent != null)
      return;
    int i = this.mContainerId;
    FrameLayout localFrameLayout = (FrameLayout)findViewById(i);
    this.mRealTabContent = localFrameLayout;
    if (this.mRealTabContent != null)
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("No tab content FrameLayout found for id ");
    int j = this.mContainerId;
    String str = j;
    throw new IllegalStateException(str);
  }

  private void initFragmentTabHost(Context paramContext, AttributeSet paramAttributeSet)
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16842995;
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, arrayOfInt, 0, 0);
    int i = localTypedArray.getResourceId(0, 0);
    this.mContainerId = i;
    localTypedArray.recycle();
    super.setOnTabChangedListener(this);
    if (findViewById(16908307) != null)
      return;
    LinearLayout localLinearLayout = new LinearLayout(paramContext);
    localLinearLayout.setOrientation(1);
    FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-1, -1);
    addView(localLinearLayout, localLayoutParams);
    TabWidget localTabWidget = new TabWidget(paramContext);
    localTabWidget.setId(16908307);
    localTabWidget.setOrientation(0);
    LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-1, -1, 0.0F);
    localLinearLayout.addView(localTabWidget, localLayoutParams1);
    FrameLayout localFrameLayout1 = new FrameLayout(paramContext);
    localFrameLayout1.setId(16908305);
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(0, 0, 0.0F);
    localLinearLayout.addView(localFrameLayout1, localLayoutParams2);
    FrameLayout localFrameLayout2 = new FrameLayout(paramContext);
    this.mRealTabContent = localFrameLayout2;
    FrameLayout localFrameLayout3 = this.mRealTabContent;
    int j = this.mContainerId;
    localFrameLayout3.setId(j);
    LinearLayout.LayoutParams localLayoutParams3 = new LinearLayout.LayoutParams(-1, 0, 1.0F);
    localLinearLayout.addView(localFrameLayout2, localLayoutParams3);
  }

  public void addTab(TabHost.TabSpec paramTabSpec, Class<?> paramClass, Bundle paramBundle)
  {
    Context localContext = this.mContext;
    DummyTabFactory localDummyTabFactory = new DummyTabFactory(localContext);
    TabHost.TabSpec localTabSpec = paramTabSpec.setContent(localDummyTabFactory);
    String str = paramTabSpec.getTag();
    TabInfo localTabInfo = new TabInfo(str, paramClass, paramBundle);
    if (this.mAttached)
    {
      Fragment localFragment1 = this.mFragmentManager.findFragmentByTag(str);
      Fragment localFragment2 = TabInfo.access$102(localTabInfo, localFragment1);
      if ((localTabInfo.fragment != null) && (!localTabInfo.fragment.isDetached()))
      {
        FragmentTransaction localFragmentTransaction1 = this.mFragmentManager.beginTransaction();
        Fragment localFragment3 = localTabInfo.fragment;
        FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.detach(localFragment3);
        int i = localFragmentTransaction1.commit();
      }
    }
    boolean bool = this.mTabs.add(localTabInfo);
    addTab(paramTabSpec);
  }

  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    String str1 = getCurrentTabTag();
    FragmentTransaction localFragmentTransaction1 = null;
    int i = 0;
    int j = this.mTabs.size();
    if (i < j)
    {
      TabInfo localTabInfo = (TabInfo)this.mTabs.get(i);
      FragmentManager localFragmentManager = this.mFragmentManager;
      String str2 = localTabInfo.tag;
      Fragment localFragment1 = localFragmentManager.findFragmentByTag(str2);
      Fragment localFragment2 = TabInfo.access$102(localTabInfo, localFragment1);
      if ((localTabInfo.fragment != null) && (!localTabInfo.fragment.isDetached()))
      {
        if (!localTabInfo.tag.equals(str1))
          break label116;
        this.mLastTab = localTabInfo;
      }
      while (true)
      {
        i += 1;
        break;
        label116: if (localFragmentTransaction1 == null)
          localFragmentTransaction1 = this.mFragmentManager.beginTransaction();
        Fragment localFragment3 = localTabInfo.fragment;
        FragmentTransaction localFragmentTransaction2 = localFragmentTransaction1.detach(localFragment3);
      }
    }
    this.mAttached = true;
    FragmentTransaction localFragmentTransaction3 = doTabChanged(str1, localFragmentTransaction1);
    if (localFragmentTransaction3 == null)
      return;
    int k = localFragmentTransaction3.commit();
    boolean bool = this.mFragmentManager.executePendingTransactions();
  }

  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mAttached = false;
  }

  protected void onRestoreInstanceState(Parcelable paramParcelable)
  {
    SavedState localSavedState = (SavedState)paramParcelable;
    Parcelable localParcelable = localSavedState.getSuperState();
    super.onRestoreInstanceState(localParcelable);
    String str = localSavedState.curTab;
    setCurrentTabByTag(str);
  }

  protected Parcelable onSaveInstanceState()
  {
    Parcelable localParcelable = super.onSaveInstanceState();
    SavedState localSavedState = new SavedState(localParcelable);
    String str = getCurrentTabTag();
    localSavedState.curTab = str;
    return localSavedState;
  }

  public void onTabChanged(String paramString)
  {
    if (this.mAttached)
    {
      FragmentTransaction localFragmentTransaction = doTabChanged(paramString, null);
      if (localFragmentTransaction != null)
        int i = localFragmentTransaction.commit();
    }
    if (this.mOnTabChangeListener == null)
      return;
    this.mOnTabChangeListener.onTabChanged(paramString);
  }

  public void setOnTabChangedListener(TabHost.OnTabChangeListener paramOnTabChangeListener)
  {
    this.mOnTabChangeListener = paramOnTabChangeListener;
  }

  @Deprecated
  public void setup()
  {
    throw new IllegalStateException("Must call setup() that takes a Context and FragmentManager");
  }

  public void setup(Context paramContext, FragmentManager paramFragmentManager)
  {
    super.setup();
    this.mContext = paramContext;
    this.mFragmentManager = paramFragmentManager;
    ensureContent();
  }

  public void setup(Context paramContext, FragmentManager paramFragmentManager, int paramInt)
  {
    super.setup();
    this.mContext = paramContext;
    this.mFragmentManager = paramFragmentManager;
    this.mContainerId = paramInt;
    ensureContent();
    this.mRealTabContent.setId(paramInt);
    if (getId() != -1)
      return;
    setId(16908306);
  }

  static class SavedState extends View.BaseSavedState
  {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator()
    {
      public FragmentTabHost.SavedState createFromParcel(Parcel paramAnonymousParcel)
      {
        return new FragmentTabHost.SavedState(paramAnonymousParcel, null);
      }

      public FragmentTabHost.SavedState[] newArray(int paramAnonymousInt)
      {
        return new FragmentTabHost.SavedState[paramAnonymousInt];
      }
    };
    String curTab;

    private SavedState(Parcel paramParcel)
    {
      super();
      String str = paramParcel.readString();
      this.curTab = str;
    }

    SavedState(Parcelable paramParcelable)
    {
      super();
    }

    public String toString()
    {
      StringBuilder localStringBuilder1 = new StringBuilder().append("FragmentTabHost.SavedState{");
      String str1 = Integer.toHexString(System.identityHashCode(this));
      StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" curTab=");
      String str2 = this.curTab;
      return str2 + "}";
    }

    public void writeToParcel(Parcel paramParcel, int paramInt)
    {
      super.writeToParcel(paramParcel, paramInt);
      String str = this.curTab;
      paramParcel.writeString(str);
    }
  }

  static class DummyTabFactory
    implements TabHost.TabContentFactory
  {
    private final Context mContext;

    public DummyTabFactory(Context paramContext)
    {
      this.mContext = paramContext;
    }

    public View createTabContent(String paramString)
    {
      Context localContext = this.mContext;
      View localView = new View(localContext);
      localView.setMinimumWidth(0);
      localView.setMinimumHeight(0);
      return localView;
    }
  }

  static final class TabInfo
  {
    private final Bundle args;
    private final Class<?> clss;
    private Fragment fragment;
    private final String tag;

    TabInfo(String paramString, Class<?> paramClass, Bundle paramBundle)
    {
      this.tag = paramString;
      this.clss = paramClass;
      this.args = paramBundle;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.FragmentTabHost
 * JD-Core Version:    0.6.2
 */