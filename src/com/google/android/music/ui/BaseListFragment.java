package com.google.android.music.ui;

import android.accounts.Account;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.music.NautilusStatus;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.TypefaceUtil;
import java.util.Locale;

public abstract class BaseListFragment extends ListFragment
  implements MusicFragment
{
  private ImageView mEmptyImageView;
  private View mEmptyScreen;
  private TextView mEmptyTextView;
  private boolean mIsCardHeaderShowing = false;
  private TextView mLearnMore;
  private ViewGroup mListContainer;
  private View mPaddingView;
  private boolean mShouldShowEmptyScreen = false;
  private final UIStateManager.UIStateChangeListener mUIStateChangeListener;

  public BaseListFragment()
  {
    UIStateManager.UIStateChangeListener local1 = new UIStateManager.UIStateChangeListener()
    {
      public void onAccountStatusUpdate(Account paramAnonymousAccount, NautilusStatus paramAnonymousNautilusStatus)
      {
        BaseListFragment.this.onNewNautilusStatus(paramAnonymousNautilusStatus);
      }
    };
    this.mUIStateChangeListener = local1;
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

  protected void clearEmptyScreenText()
  {
    this.mEmptyTextView.setText("");
  }

  protected ActionBarController getActionBarController()
  {
    Object localObject = getBaseActivity();
    if (localObject == null)
      localObject = new NoOpActionBarControllerImpl();
    return localObject;
  }

  public long getAlbumId()
  {
    return 65535L;
  }

  public String getAlbumMetajamId()
  {
    return null;
  }

  public long getArtistId()
  {
    return 65535L;
  }

  public String getArtistMetajamId()
  {
    return null;
  }

  BaseActivity getBaseActivity()
  {
    return (BaseActivity)getActivity();
  }

  public final Fragment getFragment()
  {
    return this;
  }

  public MediaList getFragmentMediaList()
  {
    return null;
  }

  MusicPreferences getPreferences()
  {
    return getBaseActivity().getPreferences();
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
    View localView = this.mEmptyScreen.findViewById(2131296405);
    this.mPaddingView = localView;
    TextView localTextView2 = (TextView)this.mEmptyScreen.findViewById(2131296404);
    this.mLearnMore = localTextView2;
    this.mLearnMore.setText(2131231361);
    TypefaceUtil.setTypeface(this.mEmptyTextView, 3);
    TypefaceUtil.setTypeface(this.mLearnMore, 3);
    TextView localTextView3 = this.mLearnMore;
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        BaseListFragment localBaseListFragment = BaseListFragment.this;
        Object[] arrayOfObject = new Object[1];
        String str1 = Locale.getDefault().getLanguage().toLowerCase();
        arrayOfObject[0] = str1;
        String str2 = localBaseListFragment.getString(2131230828, arrayOfObject);
        AppNavigation.openHelpLink(BaseListFragment.this.getActivity(), str2);
      }
    };
    localTextView3.setOnClickListener(local2);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    initEmptyScreen();
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = super.onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.UIStateChangeListener localUIStateChangeListener = this.mUIStateChangeListener;
    localUIStateManager.registerUIStateChangeListener(localUIStateChangeListener);
    return localView;
  }

  public void onDestroy()
  {
    super.onDestroy();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onDestroyView()
  {
    UIStateManager localUIStateManager = UIStateManager.getInstance();
    UIStateManager.UIStateChangeListener localUIStateChangeListener = this.mUIStateChangeListener;
    localUIStateManager.unregisterUIStateChangeListener(localUIStateChangeListener);
    super.onDestroyView();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    this.mEmptyScreen = null;
  }

  public void onDetach()
  {
    super.onDetach();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  protected void onNewNautilusStatus(NautilusStatus paramNautilusStatus)
  {
  }

  public void onPause()
  {
    super.onPause();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onResume()
  {
    super.onResume();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onStart()
  {
    super.onStart();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onStop()
  {
    super.onStop();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onTutorialCardClosed()
  {
    setIsCardHeaderShowing(false);
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    getListView().setItemsCanFocus(true);
  }

  protected void setEmptyImageView(int paramInt)
  {
    this.mEmptyImageView.setBackgroundResource(paramInt);
  }

  protected void setEmptyScreen(int paramInt)
  {
    ViewGroup localViewGroup1 = (ViewGroup)getListView().getParent();
    this.mListContainer = localViewGroup1;
    View localView1 = View.inflate(getActivity(), paramInt, null);
    this.mEmptyScreen = localView1;
    ViewGroup localViewGroup2 = this.mListContainer;
    View localView2 = this.mEmptyScreen;
    localViewGroup2.addView(localView2);
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

  protected void setEmptyScreenPadding(boolean paramBoolean)
  {
    View localView = this.mPaddingView;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localView.setVisibility(i);
      return;
    }
  }

  protected void setEmptyScreenText(int paramInt)
  {
    this.mEmptyTextView.setText(paramInt);
  }

  protected void setEmptyScreenVisible(boolean paramBoolean)
  {
    this.mShouldShowEmptyScreen = paramBoolean;
    refreshEmptyScreen();
  }

  protected void setIsCardHeaderShowing(boolean paramBoolean)
  {
    this.mIsCardHeaderShowing = paramBoolean;
    refreshEmptyScreen();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseListFragment
 * JD-Core Version:    0.6.2
 */