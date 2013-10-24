package com.google.android.music.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.widgets.ExpandingScrollView;

public class BaseFragment extends Fragment
  implements MusicFragment
{
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

  protected BaseActivity getBaseActivity()
  {
    return (BaseActivity)getActivity();
  }

  protected ExpandingScrollView getBottomDrawer()
  {
    BaseActivity localBaseActivity = getBaseActivity();
    if (localBaseActivity != null);
    for (ExpandingScrollView localExpandingScrollView = localBaseActivity.getBottomDrawer(); ; localExpandingScrollView = null)
      return localExpandingScrollView;
  }

  public final Fragment getFragment()
  {
    return this;
  }

  public MediaList getFragmentMediaList()
  {
    return null;
  }

  protected MusicPreferences getPreferences()
  {
    return getUIStateManager().getPrefs();
  }

  protected UIStateManager getUIStateManager()
  {
    return UIStateManager.getInstance();
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
    if ((paramActivity instanceof BaseActivity))
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("BaseFragment is hosted by ");
    String str1 = paramActivity.getClass().getCanonicalName();
    String str2 = str1 + " which is not a BaseActivity";
    throw new IllegalStateException(str2);
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
    return localView;
  }

  public void onDestroy()
  {
    super.onDestroy();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onDestroyView()
  {
    super.onDestroyView();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
  }

  public void onDetach()
  {
    super.onDetach();
    DebugUtils.maybeLogMethodName(DebugUtils.MusicTag.CALLS, this);
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
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.BaseFragment
 * JD-Core Version:    0.6.2
 */