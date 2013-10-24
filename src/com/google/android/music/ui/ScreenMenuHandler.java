package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.SongList;
import com.google.android.music.playback.IMusicPlaybackService;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler.MenuEntry;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler.UIThreadMenuEntry;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler;
import com.google.android.music.utils.MusicUtils;
import java.util.List;

public class ScreenMenuHandler extends DocumentMenuHandler
{
  private final ScreenMenuType mScreenMenuType;

  public ScreenMenuHandler(MusicFragment paramMusicFragment, Document paramDocument, ScreenMenuType paramScreenMenuType)
  {
    super(paramMusicFragment, paramDocument);
    this.mScreenMenuType = paramScreenMenuType;
  }

  private void addClearQueue(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    ClearQueue localClearQueue = new ClearQueue(paramResources);
    boolean bool = paramList.add(localClearQueue);
  }

  private void addSaveQueue(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    SaveQueue localSaveQueue = new SaveQueue(paramResources);
    boolean bool = paramList.add(localSaveQueue);
  }

  public void addMenuEntries(List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    Resources localResources = this.mContext.getResources();
    int[] arrayOfInt1 = 1.$SwitchMap$com$google$android$music$ui$ScreenMenuHandler$ScreenMenuType;
    int i = this.mScreenMenuType.ordinal();
    switch (arrayOfInt1[i])
    {
    default:
      Log.wtf("ScreenMenuHandler", "Unsupported screen type");
      return;
    case 1:
      addStartRadio(localResources, paramList);
      addAddToMyLibraryEntry(localResources, paramList);
      addAddToPlaylist(localResources, paramList);
      addGoToArtist(localResources, paramList);
      addGoToAlbum(localResources, paramList);
      addShareEntry(localResources, paramList);
      addClearQueue(localResources, paramList);
      addSaveQueue(localResources, paramList);
      if (!UIStateManager.getInstance().getPrefs().isFindVideoEnabled())
        return;
      addFindVideo(localResources, paramList);
      return;
    case 2:
      addClearQueue(localResources, paramList);
      return;
    case 3:
    }
    boolean bool = UIStateManager.getInstance().getPrefs().isXLargeScreen();
    int[] arrayOfInt2 = 1.$SwitchMap$com$google$android$music$ui$cardlib$model$Document$Type;
    int j = this.mDoc.getType().ordinal();
    switch (arrayOfInt2[j])
    {
    default:
      StringBuilder localStringBuilder = new StringBuilder().append("Unhandled document type: ");
      Document.Type localType = this.mDoc.getType();
      String str = localType;
      Log.wtf("ScreenMenuHandler", str);
      return;
    case 1:
      if (!bool)
      {
        addStartRadio(localResources, paramList);
        addShuffleEntry(localResources, paramList);
      }
      addAddToQueueEntry(localResources, paramList);
      addAddToPlaylist(localResources, paramList);
      addRemoveFromLibraryEntry(localResources, paramList);
      addGoToArtist(localResources, paramList);
      addBuyEntry(localResources, paramList);
      addShopArtistEntry(localResources, paramList);
      return;
    case 2:
      if (!bool)
        addShuffleEntry(localResources, paramList);
      addAddToQueueEntry(localResources, paramList);
      addAddToPlaylist(localResources, paramList);
      addShopArtistEntry(localResources, paramList);
      return;
    case 3:
      if (!bool)
        addShuffleEntry(localResources, paramList);
      if (this.mDoc.getPlaylistType() != 100)
        addAddToPlaylist(localResources, paramList);
      addAddToQueueEntry(localResources, paramList);
      addDeleteEntry(localResources, paramList);
      return;
    case 4:
    }
    if (!bool)
      addShuffleEntry(localResources, paramList);
    addAddToQueueEntry(localResources, paramList);
    addAddToPlaylist(localResources, paramList);
  }

  private class SaveQueue extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    SaveQueue(Resources arg2)
    {
      super(2131296275, 2131230904);
    }

    public void onActionSelected()
    {
      FragmentActivity localFragmentActivity = ScreenMenuHandler.this.mMusicFragment.getFragment().getActivity();
      if (localFragmentActivity == null)
        return;
      try
      {
        SongList localSongList = MusicUtils.sService.getMediaList();
        AddToPlaylistFragment localAddToPlaylistFragment = new AddToPlaylistFragment();
        Bundle localBundle = AddToPlaylistFragment.createArgs(localSongList, 65535L);
        FragmentUtils.addFragment(localFragmentActivity, localAddToPlaylistFragment, localBundle);
        return;
      }
      catch (RemoteException localRemoteException)
      {
        String str = localRemoteException.getMessage();
        Log.w("ScreenMenuHandler", str, localRemoteException);
      }
    }
  }

  private class ClearQueue extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    ClearQueue(Resources arg2)
    {
      super(2131296274, 2131230760);
    }

    public void onActionSelected()
    {
      MusicUtils.clearPlayQueue();
    }
  }

  public static enum ScreenMenuType
  {
    static
    {
      ScreenMenuType[] arrayOfScreenMenuType = new ScreenMenuType[3];
      ScreenMenuType localScreenMenuType1 = NOW_PLAYING;
      arrayOfScreenMenuType[0] = localScreenMenuType1;
      ScreenMenuType localScreenMenuType2 = NOW_PLAYING_RESTRICTED;
      arrayOfScreenMenuType[1] = localScreenMenuType2;
      ScreenMenuType localScreenMenuType3 = TRACK_CONTAINER;
      arrayOfScreenMenuType[2] = localScreenMenuType3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ScreenMenuHandler
 * JD-Core Version:    0.6.2
 */