package com.google.android.music.ui;

import android.accounts.Account;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.TypefaceUtil;
import com.google.android.music.utils.ViewUtils;
import java.util.Locale;

public class GridFragment extends BaseFragment
{
  private ListAdapter mAdapter;
  private ImageView mEmptyImageView;
  private View mEmptyScreen;
  private TextView mEmptyTextView;
  private ListView mGrid;
  private FrameLayout mGridContainer;
  private boolean mGridShown;
  private final Handler mHandler;
  private boolean mIsCardHeaderShowing;
  private TextView mLearnMore;
  private View mProgressContainer;
  private final Runnable mRequestFocus;
  private boolean mShouldShowEmptyScreen;
  private final UIStateManager.UIStateChangeListener mUIStateChangeListener;

  public GridFragment()
  {
    Handler localHandler = new Handler();
    this.mHandler = localHandler;
    Runnable local1 = new Runnable()
    {
      public void run()
      {
        ListView localListView1 = GridFragment.this.mGrid;
        ListView localListView2 = GridFragment.this.mGrid;
        localListView1.focusableViewAvailable(localListView2);
      }
    };
    this.mRequestFocus = local1;
    this.mShouldShowEmptyScreen = false;
    this.mIsCardHeaderShowing = false;
    UIStateManager.UIStateChangeListener local2 = new UIStateManager.UIStateChangeListener()
    {
      public void onAccountStatusUpdate(Account paramAnonymousAccount, NautilusStatus paramAnonymousNautilusStatus)
      {
        GridFragment.this.onNewNautilusStatus(paramAnonymousNautilusStatus);
      }
    };
    this.mUIStateChangeListener = local2;
  }

  private void ensureGrid()
  {
    if (this.mGrid != null)
      return;
    View localView1 = getView();
    if (localView1 == null)
      throw new IllegalStateException("Content view not yet created");
    if ((localView1 instanceof ListView))
    {
      ListView localListView1 = (ListView)localView1;
      this.mGrid = localListView1;
      this.mGridShown = true;
      this.mGrid.setItemsCanFocus(true);
      if (this.mAdapter == null)
        break label188;
      ListAdapter localListAdapter = this.mAdapter;
      this.mAdapter = null;
      setListAdapter(localListAdapter);
    }
    while (true)
    {
      Handler localHandler = this.mHandler;
      Runnable localRunnable = this.mRequestFocus;
      boolean bool = localHandler.post(localRunnable);
      return;
      View localView2 = localView1.findViewById(2131296407);
      this.mProgressContainer = localView2;
      FrameLayout localFrameLayout = (FrameLayout)localView1.findViewById(2131296406);
      this.mGridContainer = localFrameLayout;
      View localView3 = localView1.findViewById(16908298);
      if (!(localView3 instanceof ListView))
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list' that is not a ListView class");
      ListView localListView2 = (ListView)localView3;
      this.mGrid = localListView2;
      if (this.mGrid != null)
        break;
      throw new RuntimeException("Your content must have a ListView whose id attribute is 'android.R.id.list'");
      label188: if (this.mProgressContainer != null)
        setGridShown(false, false);
    }
  }

  private void refreshEmptyScreen()
  {
    if (this.mEmptyScreen == null)
      return;
    View localView = this.mEmptyScreen;
    if ((this.mShouldShowEmptyScreen) && (!this.mIsCardHeaderShowing));
    for (int i = 0; ; i = 8)
    {
      localView.setVisibility(i);
      return;
    }
  }

  private void setGridShown(boolean paramBoolean1, boolean paramBoolean2)
  {
    ensureGrid();
    if (this.mProgressContainer == null)
      throw new IllegalStateException("Can't be used with a custom content view");
    if (this.mGridShown != paramBoolean1)
      return;
    this.mGridShown = paramBoolean1;
    if (paramBoolean1)
    {
      if (paramBoolean2)
      {
        View localView1 = this.mProgressContainer;
        Animation localAnimation1 = AnimationUtils.loadAnimation(getActivity(), 17432577);
        localView1.startAnimation(localAnimation1);
        FrameLayout localFrameLayout1 = this.mGridContainer;
        Animation localAnimation2 = AnimationUtils.loadAnimation(getActivity(), 17432576);
        localFrameLayout1.startAnimation(localAnimation2);
      }
      while (true)
      {
        this.mProgressContainer.setVisibility(8);
        this.mGridContainer.setVisibility(0);
        return;
        this.mProgressContainer.clearAnimation();
        this.mGridContainer.clearAnimation();
      }
    }
    if (paramBoolean2)
    {
      View localView2 = this.mProgressContainer;
      Animation localAnimation3 = AnimationUtils.loadAnimation(getActivity(), 17432576);
      localView2.startAnimation(localAnimation3);
      FrameLayout localFrameLayout2 = this.mGridContainer;
      Animation localAnimation4 = AnimationUtils.loadAnimation(getActivity(), 17432577);
      localFrameLayout2.startAnimation(localAnimation4);
    }
    while (true)
    {
      this.mProgressContainer.setVisibility(0);
      this.mGridContainer.setVisibility(8);
      return;
      this.mProgressContainer.clearAnimation();
      this.mGridContainer.clearAnimation();
    }
  }

  public ListAdapter getListAdapter()
  {
    return this.mAdapter;
  }

  public ListView getListView()
  {
    ensureGrid();
    return this.mGrid;
  }

  protected int getScreenColumns()
  {
    Resources localResources = getResources();
    MusicPreferences localMusicPreferences = getPreferences();
    return ViewUtils.getScreenColumnCount(localResources, localMusicPreferences);
  }

  protected void initEmptyScreen()
  {
    if (this.mEmptyScreen != null)
      return;
    setEmptyScreen(2130968617);
    ImageView localImageView = (ImageView)this.mEmptyScreen.findViewById(2131296402);
    this.mEmptyImageView = localImageView;
    TextView localTextView1 = (TextView)this.mEmptyScreen.findViewById(2131296403);
    this.mEmptyTextView = localTextView1;
    TextView localTextView2 = (TextView)this.mEmptyScreen.findViewById(2131296404);
    this.mLearnMore = localTextView2;
    this.mLearnMore.setText(2131231361);
    TypefaceUtil.setTypeface(this.mEmptyTextView, 3);
    TypefaceUtil.setTypeface(this.mLearnMore, 3);
    TextView localTextView3 = this.mLearnMore;
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        GridFragment localGridFragment = GridFragment.this;
        Object[] arrayOfObject = new Object[1];
        String str1 = Locale.getDefault().getLanguage().toLowerCase();
        arrayOfObject[0] = str1;
        String str2 = localGridFragment.getString(2131230828, arrayOfObject);
        AppNavigation.openHelpLink(GridFragment.this.getActivity(), str2);
      }
    };
    localTextView3.setOnClickListener(local3);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    initEmptyScreen();
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968618, paramViewGroup, false);
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.UIStateChangeListener localUIStateChangeListener = this.mUIStateChangeListener;
    localUIStateManager.registerUIStateChangeListener(localUIStateChangeListener);
    return localView;
  }

  public void onDestroyView()
  {
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.UIStateChangeListener localUIStateChangeListener = this.mUIStateChangeListener;
    localUIStateManager.unregisterUIStateChangeListener(localUIStateChangeListener);
    Handler localHandler = this.mHandler;
    Runnable localRunnable = this.mRequestFocus;
    localHandler.removeCallbacks(localRunnable);
    this.mGrid = null;
    this.mGridShown = false;
    this.mGridContainer = null;
    this.mProgressContainer = null;
    this.mEmptyScreen = null;
    super.onDestroyView();
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
  }

  public void onStart()
  {
    super.onStart();
  }

  public void onStop()
  {
    super.onStop();
  }

  public void onTutorialCardClosed()
  {
    setIsCardHeaderShowing(false);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    ensureGrid();
  }

  protected void setEmptyImageView(int paramInt)
  {
    this.mEmptyImageView.setBackgroundResource(paramInt);
  }

  protected void setEmptyScreen(int paramInt)
  {
    ensureGrid();
    View localView1 = View.inflate(getActivity(), paramInt, null);
    this.mEmptyScreen = localView1;
    FrameLayout localFrameLayout = this.mGridContainer;
    View localView2 = this.mEmptyScreen;
    localFrameLayout.addView(localView2);
    setEmptyScreenVisible(false);
  }

  protected void setEmptyScreenLearnMoreVisible(boolean paramBoolean)
  {
    TextView localTextView = this.mLearnMore;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localTextView.setVisibility(i);
      return;
    }
  }

  protected void setEmptyScreenText(int paramInt)
  {
    this.mEmptyTextView.setText(paramInt);
  }

  protected void setEmptyScreenVisible(boolean paramBoolean)
  {
    if (this.mEmptyScreen == null)
      return;
    ensureGrid();
    this.mShouldShowEmptyScreen = paramBoolean;
    refreshEmptyScreen();
  }

  protected void setIsCardHeaderShowing(boolean paramBoolean)
  {
    this.mIsCardHeaderShowing = paramBoolean;
    refreshEmptyScreen();
  }

  public void setListAdapter(ListAdapter paramListAdapter)
  {
    boolean bool = false;
    if (this.mAdapter != null);
    for (int i = 1; ; i = 0)
    {
      this.mAdapter = paramListAdapter;
      if (this.mGrid == null)
        return;
      ListView localListView = this.mGrid;
      ListAdapter localListAdapter = this.mAdapter;
      localListView.setAdapter(localListAdapter);
      if (this.mGridShown)
        return;
      if (i != 0)
        return;
      if (getView().getWindowToken() != null)
        bool = true;
      setGridShown(true, bool);
      return;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GridFragment
 * JD-Core Version:    0.6.2
 */