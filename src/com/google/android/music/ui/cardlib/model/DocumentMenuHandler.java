package com.google.android.music.ui.cardlib.model;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.google.android.music.DeleteConfirmationDialog.DeletionType;
import com.google.android.music.GPlusShareActivity;
import com.google.android.music.KeepOnView;
import com.google.android.music.KeepOnViewMedium;
import com.google.android.music.download.IntentConstants;
import com.google.android.music.log.Log;
import com.google.android.music.medialist.AlbumSongList;
import com.google.android.music.medialist.MediaList;
import com.google.android.music.medialist.NautilusSongList;
import com.google.android.music.medialist.PlayQueueSongList;
import com.google.android.music.medialist.PlaylistSongList;
import com.google.android.music.medialist.SharedSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.purchase.Finsky;
import com.google.android.music.store.MusicContent.Playlists.Members;
import com.google.android.music.store.MusicContent.XAudio;
import com.google.android.music.store.Store;
import com.google.android.music.ui.AddToLibraryButton;
import com.google.android.music.ui.AddToPlaylistFragment;
import com.google.android.music.ui.ArtistAlbumsGridFragment;
import com.google.android.music.ui.ArtistPageActivity;
import com.google.android.music.ui.FollowPlaylistButton;
import com.google.android.music.ui.FragmentUtils;
import com.google.android.music.ui.MusicFragment;
import com.google.android.music.ui.MyLibraryFragment;
import com.google.android.music.ui.NowPlayingScreenFragment;
import com.google.android.music.ui.RemoveFromLibraryButton;
import com.google.android.music.ui.TrackContainerActivity;
import com.google.android.music.ui.TrackContainerFragment;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler.AsyncMenuEntry;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler.MenuEntry;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler.MenuTask;
import com.google.android.music.ui.cardlib.PlayCardMenuHandler.UIThreadMenuEntry;
import com.google.android.music.ui.cardlib.layout.LegacyPopupMenu;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.layout.PlayPopupMenu.OnActionSelectedListener;
import com.google.android.music.ui.dialogs.DeleteDocumentDialog;
import com.google.android.music.utils.DebugUtils;
import com.google.android.music.utils.DebugUtils.MusicTag;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentMenuHandler
  implements DialogInterface.OnDismissListener, PlayCardMenuHandler
{
  private static final boolean LOGV;
  protected Context mContext;
  protected final Document mDoc;
  protected final MusicFragment mMusicFragment;
  private LegacyPopupMenu mPopupMenu;

  static
  {
    if (!DocumentMenuHandler.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      LOGV = DebugUtils.isLoggable(DebugUtils.MusicTag.UI);
      return;
    }
  }

  public DocumentMenuHandler(MusicFragment paramMusicFragment, Document paramDocument)
  {
    this.mMusicFragment = paramMusicFragment;
    FragmentActivity localFragmentActivity = this.mMusicFragment.getFragment().getActivity();
    this.mContext = localFragmentActivity;
    this.mDoc = paramDocument;
  }

  private void addFollowSharedPlaylist(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    FollowPlaylist localFollowPlaylist = new FollowPlaylist(paramResources);
    boolean bool = paramList.add(localFollowPlaylist);
  }

  private void addKeepOnDeviceEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    Document localDocument = this.mDoc;
    Context localContext = this.mContext;
    SongList localSongList = localDocument.getSongList(localContext);
    if (localSongList == null)
      return;
    if (!localSongList.supportsOfflineCaching())
      return;
    KeepOnDevice localKeepOnDevice = new KeepOnDevice(paramResources);
    boolean bool = paramList.add(localKeepOnDevice);
  }

  private void addPlayNextEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (MusicUtils.sService == null)
      return;
    PlayNext localPlayNext = new PlayNext(paramResources);
  }

  private boolean getIsAllNautilus()
  {
    boolean bool = false;
    assert (!MusicUtils.isMainThread());
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "isAllPersistentNautilus";
    Cursor localCursor;
    if (!this.mDoc.isNautilus())
    {
      Uri localUri = MusicContent.XAudio.getAudioUri(this.mDoc.getId());
      Context localContext = this.mContext;
      String[] arrayOfString2 = null;
      String str1 = null;
      localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
      if (localCursor == null);
    }
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown track id: ");
        long l = this.mDoc.getId();
        String str2 = l;
        Log.e("DocumentMenuHandler", str2);
        return bool;
      }
      int i = 0;
      int j = localCursor.getInt(i);
      if (j != 0);
      for (bool = true; ; bool = false)
        break;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  private boolean getIsSideloaded()
  {
    boolean bool = false;
    assert (!MusicUtils.isMainThread());
    String[] arrayOfString1 = new String[1];
    arrayOfString1[0] = "SourceAccount";
    Uri localUri = MusicContent.XAudio.getAudioUri(this.mDoc.getId());
    Context localContext = this.mContext;
    String[] arrayOfString2 = null;
    String str1 = null;
    Cursor localCursor = MusicUtils.query(localContext, localUri, arrayOfString1, null, arrayOfString2, str1);
    if (localCursor != null);
    try
    {
      if (!localCursor.moveToFirst())
      {
        StringBuilder localStringBuilder = new StringBuilder().append("Unknown track id: ");
        long l = this.mDoc.getId();
        String str2 = l;
        Log.e("DocumentMenuHandler", str2);
        return bool;
      }
      int i = 0;
      int j = localCursor.getInt(i);
      if (j == 0);
      for (bool = true; ; bool = false)
        break;
    }
    finally
    {
      Store.safeClose(localCursor);
    }
  }

  private boolean isShowingMyLibrary()
  {
    return isSubFragmentOf(MyLibraryFragment.class);
  }

  private boolean isShowingPlayQueue()
  {
    return isSubFragmentOf(NowPlayingScreenFragment.class);
  }

  private <T extends Fragment> boolean isSubFragmentOf(Class<T> paramClass)
  {
    Fragment localFragment = this.mMusicFragment.getFragment();
    if (localFragment != null)
      if (!paramClass.isInstance(localFragment));
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      localFragment = localFragment.getParentFragment();
      break;
    }
  }

  private boolean shouldHideGoToAlbum()
  {
    boolean bool1 = false;
    boolean bool2 = isSubFragmentOf(TrackContainerFragment.class);
    long l1;
    if (this.mDoc.isNautilus())
    {
      l1 = this.mMusicFragment.getAlbumMetajamId();
      String str = this.mDoc.getAlbumMetajamId();
      if ((!TextUtils.isEmpty(str)) && ((!bool2) || (l1 == null) || (!l1.equals(str))));
    }
    for (bool1 = true; ; bool1 = true)
    {
      long l2;
      do
      {
        return bool1;
        l1 = this.mMusicFragment.getAlbumId();
        l2 = this.mDoc.getAlbumId();
      }
      while ((l2 != 65535L) && ((!bool2) || (l1 != l2)));
    }
  }

  private boolean shouldHideGoToArtist()
  {
    boolean bool1 = false;
    boolean bool2 = isSubFragmentOf(ArtistAlbumsGridFragment.class);
    long l1;
    if (this.mDoc.isNautilus())
    {
      l1 = this.mMusicFragment.getArtistMetajamId();
      String str = this.mDoc.getArtistMetajamId();
      if ((!TextUtils.isEmpty(str)) && ((!bool2) || (l1 == null) || (!l1.equals(str))));
    }
    for (bool1 = true; ; bool1 = true)
    {
      long l2;
      do
      {
        return bool1;
        l1 = this.mMusicFragment.getArtistId();
        l2 = this.mDoc.getArtistId();
      }
      while ((l2 != 65535L) && ((!bool2) || (l1 != l2)));
    }
  }

  protected void addAddToMyLibraryEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (!this.mDoc.canAddToLibrary())
      return;
    AddToMyLibrary localAddToMyLibrary = new AddToMyLibrary(paramResources);
    boolean bool = paramList.add(localAddToMyLibrary);
  }

  protected void addAddToPlaylist(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    Document localDocument = this.mDoc;
    Context localContext = this.mContext;
    SongList localSongList = localDocument.getSongList(localContext);
    if (localSongList == null)
      return;
    if (!localSongList.supportsAppendToPlaylist())
      return;
    AddToPlaylist localAddToPlaylist = new AddToPlaylist(paramResources);
    boolean bool = paramList.add(localAddToPlaylist);
  }

  protected void addAddToQueueEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (MusicUtils.sService == null)
      return;
    if (isShowingPlayQueue())
      return;
    AddToQueue localAddToQueue = new AddToQueue(paramResources);
    boolean bool = paramList.add(localAddToQueue);
  }

  protected void addBuyEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (!UIStateManager.getInstance().getPrefs().isNautilusEnabled())
      return;
    if (TextUtils.isEmpty(this.mDoc.getAlbumMetajamId()))
      return;
    Buy localBuy = new Buy(paramResources);
    boolean bool = paramList.add(localBuy);
  }

  protected void addDeleteEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    Document.Type localType1 = this.mDoc.getType();
    Document.Type localType2 = Document.Type.TRACK;
    MediaList localMediaList;
    int i;
    if (localType1 == localType2)
    {
      localMediaList = this.mMusicFragment.getFragmentMediaList();
      if ((localMediaList instanceof PlayQueueSongList))
        i = 2131230799;
    }
    while (true)
    {
      if (i == 0)
        return;
      Delete localDelete = new Delete(paramResources, i);
      boolean bool = paramList.add(localDelete);
      return;
      if (((localMediaList instanceof PlaylistSongList)) && (((PlaylistSongList)localMediaList).getPlaylistType() != 71))
      {
        i = 2131230935;
      }
      else if ((localMediaList instanceof SharedSongList))
      {
        i = 0;
      }
      else if (this.mDoc.canRemoveFromLibrary())
      {
        i = 2131231283;
      }
      else if (!this.mDoc.isNautilus())
      {
        i = 2131231287;
      }
      else
      {
        i = 0;
        continue;
        Document.Type localType3 = this.mDoc.getType();
        Document.Type localType4 = Document.Type.PLAYLIST;
        if (localType3 == localType4)
          switch (this.mDoc.getPlaylistType())
          {
          default:
            StringBuilder localStringBuilder = new StringBuilder().append("Unexpected playlist type: ");
            int j = this.mDoc.getPlaylistType();
            String str = j;
            Log.wtf("DocumentMenuHandler", str);
            i = 0;
            break;
          case 0:
          case 1:
          case 71:
            i = 2131231287;
            break;
          case 50:
          case 60:
          case 70:
          case 100:
            i = 0;
            break;
          }
        else
          i = 2131231287;
      }
    }
  }

  protected void addFindVideo(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    FindVideo localFindVideo = new FindVideo(paramResources);
    boolean bool = paramList.add(localFindVideo);
  }

  protected void addGoToAlbum(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (shouldHideGoToAlbum())
      return;
    GoToAlbum localGoToAlbum = new GoToAlbum(paramResources);
    boolean bool = paramList.add(localGoToAlbum);
  }

  protected void addGoToArtist(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (shouldHideGoToArtist())
      return;
    GoToArtist localGoToArtist = new GoToArtist(paramResources);
    boolean bool = paramList.add(localGoToArtist);
  }

  public void addMenuEntries(List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (LOGV)
    {
      StringBuilder localStringBuilder = new StringBuilder().append("Displaying menu for: ");
      Document localDocument = this.mDoc;
      String str = localDocument;
      Log.d("DocumentMenuHandler", str);
    }
    Resources localResources = this.mContext.getResources();
    Document.Type localType1 = this.mDoc.getType();
    Document.Type localType2 = Document.Type.ALBUM;
    if (localType1 == localType2)
    {
      addStartRadio(localResources, paramList);
      addPlayNextEntry(localResources, paramList);
      addAddToQueueEntry(localResources, paramList);
      addAddToMyLibraryEntry(localResources, paramList);
      addRemoveFromLibraryEntry(localResources, paramList);
      addKeepOnDeviceEntry(localResources, paramList);
      addAddToPlaylist(localResources, paramList);
      addGoToArtist(localResources, paramList);
      addShopArtistEntry(localResources, paramList);
      addBuyEntry(localResources, paramList);
      return;
    }
    Document.Type localType3 = this.mDoc.getType();
    Document.Type localType4 = Document.Type.ARTIST;
    if (localType3 == localType4)
    {
      addStartRadio(localResources, paramList);
      addShopArtistEntry(localResources, paramList);
      return;
    }
    Document.Type localType5 = this.mDoc.getType();
    Document.Type localType6 = Document.Type.PLAYLIST;
    if (localType5 == localType6)
    {
      int i = this.mDoc.getPlaylistType();
      addPlayNextEntry(localResources, paramList);
      addAddToQueueEntry(localResources, paramList);
      if ((!this.mDoc.isNautilus()) && ((i == 0) || (i == 100)))
        addKeepOnDeviceEntry(localResources, paramList);
      addAddToPlaylist(localResources, paramList);
      addDeleteEntry(localResources, paramList);
      if (this.mDoc.getPlaylistType() != 70)
        return;
      addFollowSharedPlaylist(localResources, paramList);
      return;
    }
    Document.Type localType7 = this.mDoc.getType();
    Document.Type localType8 = Document.Type.TRACK;
    if (localType7 == localType8)
    {
      addStartRadio(localResources, paramList);
      addPlayNextEntry(localResources, paramList);
      addAddToQueueEntry(localResources, paramList);
      addAddToMyLibraryEntry(localResources, paramList);
      addAddToPlaylist(localResources, paramList);
      addGoToArtist(localResources, paramList);
      addGoToAlbum(localResources, paramList);
      addDeleteEntry(localResources, paramList);
      addShareEntry(localResources, paramList);
      addShopArtistEntry(localResources, paramList);
      addBuyEntry(localResources, paramList);
      if (!UIStateManager.getInstance().getPrefs().isFindVideoEnabled())
        return;
      addFindVideo(localResources, paramList);
      return;
    }
    Document.Type localType9 = this.mDoc.getType();
    Document.Type localType10 = Document.Type.GENRE;
    if (localType9 == localType10)
    {
      if (!isShowingMyLibrary())
        return;
      addPlayNextEntry(localResources, paramList);
      addAddToQueueEntry(localResources, paramList);
      return;
    }
    Document.Type localType11 = this.mDoc.getType();
    Document.Type localType12 = Document.Type.RADIO;
    if (localType11 == localType12)
    {
      addDeleteEntry(localResources, paramList);
      addGoToArtist(localResources, paramList);
      addGoToAlbum(localResources, paramList);
      return;
    }
    Log.wtf("DocumentMenuHandler", "Unexpected doc type");
  }

  protected void addRemoveFromLibraryEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (!this.mDoc.canRemoveFromLibrary())
      return;
    RemoveFromMyLibrary localRemoveFromMyLibrary = new RemoveFromMyLibrary(paramResources);
    boolean bool = paramList.add(localRemoveFromMyLibrary);
  }

  protected void addShareEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    if (this.mDoc.getSongStoreId() == null)
      return;
    if (!GPlusShareActivity.isSharingSupported(this.mContext))
      return;
    Share localShare = new Share(paramResources);
    boolean bool = paramList.add(localShare);
  }

  protected void addShopArtistEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    if ((localMusicPreferences.hasStreamingAccount()) && (!localMusicPreferences.isNautilusEnabled()));
    for (int i = 1; ; i = 0)
    {
      if (i == 0)
        return;
      ShopArtist localShopArtist = new ShopArtist(paramResources);
      boolean bool = paramList.add(localShopArtist);
      return;
    }
  }

  protected void addShuffleEntry(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    Shuffle localShuffle = new Shuffle(paramResources);
    boolean bool = paramList.add(localShuffle);
  }

  protected void addStartRadio(Resources paramResources, List<PlayCardMenuHandler.MenuEntry> paramList)
  {
    MusicPreferences localMusicPreferences = UIStateManager.getInstance().getPrefs();
    if (!localMusicPreferences.hasStreamingAccount())
      return;
    if (localMusicPreferences.isNautilusEnabled());
    for (Object localObject = new StartRadio(paramResources); ; localObject = new StartInstantMix(paramResources))
    {
      boolean bool = paramList.add(localObject);
      return;
    }
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    this.mPopupMenu = null;
  }

  public void showPopupMenu(View paramView)
  {
    Context localContext = this.mContext;
    LegacyPopupMenu localLegacyPopupMenu = new LegacyPopupMenu(localContext, paramView, true);
    this.mPopupMenu = localLegacyPopupMenu;
    this.mPopupMenu.addSpinnerItem();
    this.mPopupMenu.setOnDismissListener(this);
    this.mPopupMenu.show();
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      public void backgroundTask()
      {
        DocumentMenuHandler localDocumentMenuHandler = DocumentMenuHandler.this;
        FragmentActivity localFragmentActivity = DocumentMenuHandler.this.mMusicFragment.getFragment().getActivity();
        localDocumentMenuHandler.mContext = localFragmentActivity;
        if (DocumentMenuHandler.this.mContext == null)
          return;
        Document localDocument1 = DocumentMenuHandler.this.mDoc;
        Context localContext1 = DocumentMenuHandler.this.mContext;
        SongList localSongList = localDocument1.getSongList(localContext1);
        boolean bool2;
        if (DocumentMenuHandler.this.mDoc.isNautilus())
          if ((localSongList instanceof NautilusSongList))
          {
            NautilusSongList localNautilusSongList = (NautilusSongList)localSongList;
            Context localContext2 = DocumentMenuHandler.this.mContext;
            boolean bool1 = localNautilusSongList.isAllInLibrary(localContext2);
            Document localDocument2 = DocumentMenuHandler.this.mDoc;
            if (bool1)
              break label183;
            bool2 = true;
            localDocument2.setCanAddToLibrary(bool2);
          }
        while (true)
        {
          Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
          Document.Type localType2 = Document.Type.TRACK;
          if (localType1 != localType2)
            return;
          Document localDocument3 = DocumentMenuHandler.this.mDoc;
          boolean bool3 = DocumentMenuHandler.this.getIsAllNautilus();
          localDocument3.setCanRemoveFromLibrary(bool3);
          return;
          label183: bool2 = false;
          break;
          if ((localSongList instanceof AlbumSongList))
          {
            AlbumSongList localAlbumSongList = (AlbumSongList)localSongList;
            Context localContext3 = DocumentMenuHandler.this.mContext;
            boolean bool4 = localAlbumSongList.hasPersistentNautilus(localContext3);
            DocumentMenuHandler.this.mDoc.setCanRemoveFromLibrary(bool4);
          }
        }
      }

      public void taskCompleted()
      {
        if (DocumentMenuHandler.this.mPopupMenu == null)
          return;
        DocumentMenuHandler localDocumentMenuHandler = DocumentMenuHandler.this;
        FragmentActivity localFragmentActivity = DocumentMenuHandler.this.mMusicFragment.getFragment().getActivity();
        localDocumentMenuHandler.mContext = localFragmentActivity;
        if (DocumentMenuHandler.this.mContext == null)
          return;
        DocumentMenuHandler.this.mPopupMenu.clearSpinnerItem();
        ArrayList localArrayList = Lists.newArrayList();
        DocumentMenuHandler.this.addMenuEntries(localArrayList);
        if (localArrayList.isEmpty())
          return;
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext())
        {
          final PlayCardMenuHandler.MenuEntry localMenuEntry = (PlayCardMenuHandler.MenuEntry)localIterator.next();
          PlayPopupMenu.OnActionSelectedListener local1 = new PlayPopupMenu.OnActionSelectedListener()
          {
            public void onActionSelected()
            {
              if (localMenuEntry.shouldRunAsync())
              {
                PlayCardMenuHandler.MenuEntry localMenuEntry = localMenuEntry;
                MusicUtils.runAsync(new PlayCardMenuHandler.MenuTask(localMenuEntry));
                return;
              }
              localMenuEntry.onActionSelected();
            }
          };
          LegacyPopupMenu localLegacyPopupMenu = DocumentMenuHandler.this.mPopupMenu;
          String str = localMenuEntry.menuTitle;
          View localView = localMenuEntry.customView;
          localLegacyPopupMenu.addMenuItem(str, true, localView, local1);
        }
        DocumentMenuHandler.this.mPopupMenu.show();
      }
    });
  }

  private class GoToAlbum extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    GoToAlbum(Resources arg2)
    {
      super(2131296272, 2131231288);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.TRACK;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.RADIO;
        if (localType3 != localType4);
      }
      else
      {
        if (DocumentMenuHandler.this.mDoc.isNautilus())
        {
          Context localContext1 = DocumentMenuHandler.this.mContext;
          String str = DocumentMenuHandler.this.mDoc.getAlbumMetajamId();
          TrackContainerActivity.showNautilusAlbum(localContext1, str, null);
          return;
        }
        Context localContext2 = DocumentMenuHandler.this.mContext;
        long l = DocumentMenuHandler.this.mDoc.getAlbumId();
        TrackContainerActivity.showAlbum(localContext2, l, null, true);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class Delete extends PlayCardMenuHandler.AsyncMenuEntry
  {
    Delete(Resources paramInt, int arg3)
    {
      super(2131296271, i);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.PLAYLIST;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.RADIO;
        if (localType3 != localType4);
      }
      else
      {
        FragmentActivity localFragmentActivity = DocumentMenuHandler.this.mMusicFragment.getFragment().getActivity();
        if (localFragmentActivity == null)
          return;
        DeleteDocumentDialog localDeleteDocumentDialog = new DeleteDocumentDialog();
        Document localDocument = DocumentMenuHandler.this.mDoc;
        Bundle localBundle1 = localDeleteDocumentDialog.createArgs(localFragmentActivity, localDocument);
        FragmentUtils.addFragment(localFragmentActivity, localDeleteDocumentDialog, localBundle1);
        return;
      }
      Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType6 = Document.Type.TRACK;
      if (localType5 == localType6)
      {
        MediaList localMediaList = DocumentMenuHandler.this.mMusicFragment.getFragmentMediaList();
        if ((localMediaList instanceof PlaylistSongList))
        {
          long l1 = ((PlaylistSongList)localMediaList).getPlaylistId();
          long l2 = DocumentMenuHandler.this.mDoc.getIdInParent();
          Uri localUri1 = MusicContent.Playlists.Members.getPlaylistItemUri(l1, l2);
          if (DocumentMenuHandler.this.mContext.getContentResolver().delete(localUri1, null, null) != 0)
          {
            if ((localMediaList instanceof PlayQueueSongList));
            for (int i = 2131230911; ; i = 2131230910)
            {
              Context localContext = DocumentMenuHandler.this.mContext;
              Object[] arrayOfObject = new Object[1];
              String str1 = DocumentMenuHandler.this.mDoc.getTitle();
              arrayOfObject[0] = str1;
              String str2 = localContext.getString(i, arrayOfObject);
              Toast.makeText(DocumentMenuHandler.this.mContext, str2, 0).show();
              return;
            }
          }
          Log.w("DocumentMenuHandler", "Could not remove item from playlist");
          return;
        }
        if (!DocumentMenuHandler.this.getIsAllNautilus())
        {
          Bundle localBundle2 = new Bundle();
          String str3 = DocumentMenuHandler.this.mDoc.getTitle();
          localBundle2.putString("deleteName", str3);
          long l3 = DocumentMenuHandler.this.mDoc.getId();
          localBundle2.putLong("deleteId", l3);
          int j = DeleteConfirmationDialog.DeletionType.SONG.ordinal();
          localBundle2.putInt("deleteType", j);
          String str4 = "deleteHasRemote";
          if (!DocumentMenuHandler.this.getIsSideloaded());
          for (boolean bool1 = true; ; bool1 = false)
          {
            localBundle2.putBoolean(str4, bool1);
            String str5 = DocumentMenuHandler.this.mDoc.getArtistName();
            localBundle2.putString("deleteArtistName", str5);
            boolean bool2 = DocumentMenuHandler.this.mMusicFragment.getFragment().getActivity().showDialog(109, localBundle2);
            return;
          }
        }
        Uri localUri2 = MusicContent.XAudio.getAudioUri(DocumentMenuHandler.this.mDoc.getId());
        int k = DocumentMenuHandler.this.mContext.getContentResolver().delete(localUri2, null, null);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class Buy extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    Buy(Resources arg2)
    {
      super(2131296270, 2131231286);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.TRACK;
        if (localType3 != localType4);
      }
      else
      {
        String str1 = DocumentMenuHandler.this.mDoc.getAlbumMetajamId();
        String str2 = DocumentMenuHandler.this.mDoc.getSongStoreId();
        String str3 = Finsky.getBuyAlbumUri(DocumentMenuHandler.this.mContext, str1, str2).toString();
        Intent localIntent = IntentConstants.getStoreBuyIntent(DocumentMenuHandler.this.mContext, str3);
        DocumentMenuHandler.this.mContext.startActivity(localIntent);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class Share extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    Share(Resources arg2)
    {
      super(2131296269, 2131231285);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.TRACK;
      if (localType1 == localType2)
      {
        Document localDocument = DocumentMenuHandler.this.mDoc;
        Context localContext1 = DocumentMenuHandler.this.mContext;
        SongList localSongList = localDocument.getSongList(localContext1);
        Context localContext2 = DocumentMenuHandler.this.mContext;
        String str1 = localSongList.getName(localContext2);
        String str2 = DocumentMenuHandler.this.mDoc.getSongStoreId();
        GPlusShareActivity.share(DocumentMenuHandler.this.mContext, str2, str1);
        return;
      }
      Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType4 = Document.Type.ALBUM;
      if (localType3 != localType4)
      {
        Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType6 = Document.Type.PLAYLIST;
        if (localType5 != localType6);
      }
      else
      {
        Log.wtf("DocumentMenuHandler", "not implemented");
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class GoToArtist extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    GoToArtist(Resources arg2)
    {
      super(2131296268, 2131231284);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.TRACK;
        if (localType3 != localType4)
        {
          Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
          Document.Type localType6 = Document.Type.RADIO;
          if (localType5 != localType6)
            break label166;
        }
      }
      if (DocumentMenuHandler.this.mDoc.isNautilus())
      {
        Context localContext1 = DocumentMenuHandler.this.mContext;
        String str1 = DocumentMenuHandler.this.mDoc.getArtistMetajamId();
        String str2 = DocumentMenuHandler.this.mDoc.getArtistName();
        ArtistPageActivity.showNautilusArtist(localContext1, str1, str2);
        return;
      }
      Context localContext2 = DocumentMenuHandler.this.mContext;
      long l = DocumentMenuHandler.this.mDoc.getArtistId();
      String str3 = DocumentMenuHandler.this.mDoc.getArtistName();
      ArtistPageActivity.showArtist(localContext2, l, str3, true);
      return;
      label166: Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class RemoveFromMyLibrary extends PlayCardMenuHandler.AsyncMenuEntry
  {
    RemoveFromMyLibrary(Resources arg2)
    {
      super(2131296267, 2131231283);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 == localType2)
      {
        Document localDocument = DocumentMenuHandler.this.mDoc;
        Context localContext = DocumentMenuHandler.this.mContext;
        SongList localSongList = localDocument.getSongList(localContext);
        RemoveFromLibraryButton.removeFromMyLibrary(DocumentMenuHandler.this.mContext, localSongList);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class AddToMyLibrary extends PlayCardMenuHandler.AsyncMenuEntry
  {
    AddToMyLibrary(Resources arg2)
    {
      super(2131296266, 2131231282);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.TRACK;
        if (localType3 != localType4);
      }
      else
      {
        Document localDocument = DocumentMenuHandler.this.mDoc;
        Context localContext = DocumentMenuHandler.this.mContext;
        SongList localSongList = localDocument.getSongList(localContext);
        AddToLibraryButton.addToLibrary(DocumentMenuHandler.this.mContext, localSongList);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class KeepOnDevice extends PlayCardMenuHandler.UIThreadMenuEntry
    implements View.OnClickListener
  {
    final SongList mSongList;

    KeepOnDevice(Resources arg2)
    {
      super(2131296265, 2131231281, localKeepOnViewMedium);
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext2 = DocumentMenuHandler.this.mContext;
      SongList localSongList1 = localDocument.getSongList(localContext2);
      this.mSongList = localSongList1;
      KeepOnView localKeepOnView = getKeepOnView();
      SongList localSongList2 = this.mSongList;
      localKeepOnView.setSongList(localSongList2);
      localKeepOnView.setOnClickListener(this);
    }

    private KeepOnView getKeepOnView()
    {
      return (KeepOnView)this.customView;
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.PLAYLIST;
        if (localType3 != localType4);
      }
      else
      {
        getKeepOnView().onKeepOnViewClicked();
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }

    public void onClick(View paramView)
    {
      onActionSelected();
      if (DocumentMenuHandler.this.mPopupMenu == null)
        return;
      DocumentMenuHandler.this.mPopupMenu.dismiss();
    }
  }

  private class ShopArtist extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    ShopArtist(Resources arg2)
    {
      super(2131296264, 2131231280);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.ARTIST;
        if (localType3 != localType4)
        {
          Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
          Document.Type localType6 = Document.Type.TRACK;
          if (localType5 != localType6)
            break label109;
        }
      }
      Context localContext = DocumentMenuHandler.this.mContext;
      String str = DocumentMenuHandler.this.mDoc.getArtistName();
      Intent localIntent = IntentConstants.getShopForArtistIntent(localContext, str);
      DocumentMenuHandler.this.mContext.startActivity(localIntent);
      return;
      label109: Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class FollowPlaylist extends PlayCardMenuHandler.AsyncMenuEntry
  {
    final SongList mSongList;

    FollowPlaylist(Resources arg2)
    {
      super(2131296273, 2131230900);
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext = DocumentMenuHandler.this.mContext;
      SongList localSongList = localDocument.getSongList(localContext);
      this.mSongList = localSongList;
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.PLAYLIST;
      if ((localType1 == localType2) && (DocumentMenuHandler.this.mDoc.getPlaylistType() == 70))
      {
        Document localDocument = DocumentMenuHandler.this.mDoc;
        Context localContext = DocumentMenuHandler.this.mContext;
        SongList localSongList = localDocument.getSongList(localContext);
        FollowPlaylistButton.followPlaylist(DocumentMenuHandler.this.mContext, localSongList);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class AddToPlaylist extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    AddToPlaylist(Resources arg2)
    {
      super(2131296263, 2131230899);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.ARTIST;
        if (localType3 != localType4)
        {
          Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
          Document.Type localType6 = Document.Type.ALL_SONGS_ARTIST;
          if (localType5 != localType6)
          {
            Document.Type localType7 = DocumentMenuHandler.this.mDoc.getType();
            Document.Type localType8 = Document.Type.PLAYLIST;
            if (localType7 != localType8)
            {
              Document.Type localType9 = DocumentMenuHandler.this.mDoc.getType();
              Document.Type localType10 = Document.Type.GENRE;
              if (localType9 != localType10)
              {
                Document.Type localType11 = DocumentMenuHandler.this.mDoc.getType();
                Document.Type localType12 = Document.Type.ALL_SONGS_GENRE;
                if (localType11 != localType12)
                {
                  Document.Type localType13 = DocumentMenuHandler.this.mDoc.getType();
                  Document.Type localType14 = Document.Type.TRACK;
                  if (localType13 != localType14)
                    break label284;
                }
              }
            }
          }
        }
      }
      FragmentActivity localFragmentActivity = DocumentMenuHandler.this.mMusicFragment.getFragment().getActivity();
      if (localFragmentActivity == null)
        return;
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext = DocumentMenuHandler.this.mContext;
      SongList localSongList = localDocument.getSongList(localContext);
      Document.Type localType15 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType16 = Document.Type.PLAYLIST;
      if (localType15 == localType16);
      for (long l = DocumentMenuHandler.this.mDoc.getId(); ; l = 65535L)
      {
        AddToPlaylistFragment localAddToPlaylistFragment = new AddToPlaylistFragment();
        Bundle localBundle = AddToPlaylistFragment.createArgs(localSongList, l);
        FragmentUtils.addFragment(localFragmentActivity, localAddToPlaylistFragment, localBundle);
        return;
      }
      label284: Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class FindVideo extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    FindVideo(Resources arg2)
    {
      super(2131296277, 2131231279);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.TRACK;
      if (localType1 == localType2)
      {
        Context localContext = DocumentMenuHandler.this.mContext;
        String str1 = DocumentMenuHandler.this.mDoc.getTitle();
        String str2 = DocumentMenuHandler.this.mDoc.getArtistName();
        MusicUtils.startVideoSearchActivity(localContext, str1, str2);
        return;
      }
      Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class AddToQueue extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    AddToQueue(Resources arg2)
    {
      super(2131296262, 2131230762);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.ARTIST;
        if (localType3 != localType4)
        {
          Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
          Document.Type localType6 = Document.Type.ALL_SONGS_ARTIST;
          if (localType5 != localType6)
          {
            Document.Type localType7 = DocumentMenuHandler.this.mDoc.getType();
            Document.Type localType8 = Document.Type.PLAYLIST;
            if (localType7 != localType8)
            {
              Document.Type localType9 = DocumentMenuHandler.this.mDoc.getType();
              Document.Type localType10 = Document.Type.TRACK;
              if (localType9 != localType10)
              {
                Document.Type localType11 = DocumentMenuHandler.this.mDoc.getType();
                Document.Type localType12 = Document.Type.GENRE;
                if (localType11 != localType12)
                {
                  Document.Type localType13 = DocumentMenuHandler.this.mDoc.getType();
                  Document.Type localType14 = Document.Type.ALL_SONGS_GENRE;
                  if (localType13 != localType14)
                    break label191;
                }
              }
            }
          }
        }
      }
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext = DocumentMenuHandler.this.mContext;
      MusicUtils.queue(localDocument.getSongList(localContext));
      return;
      label191: Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class Shuffle extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    Shuffle(Resources arg2)
    {
      super(2131296276, 2131230752);
    }

    public void onActionSelected()
    {
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext = DocumentMenuHandler.this.mContext;
      MusicUtils.shuffleSongs(localDocument.getSongList(localContext));
    }
  }

  private class PlayNext extends PlayCardMenuHandler.UIThreadMenuEntry
  {
    PlayNext(Resources arg2)
    {
      super(2131296261, 2131231278);
    }

    public void onActionSelected()
    {
      Document.Type localType1 = DocumentMenuHandler.this.mDoc.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = DocumentMenuHandler.this.mDoc.getType();
        Document.Type localType4 = Document.Type.ARTIST;
        if (localType3 != localType4)
        {
          Document.Type localType5 = DocumentMenuHandler.this.mDoc.getType();
          Document.Type localType6 = Document.Type.PLAYLIST;
          if (localType5 != localType6)
          {
            Document.Type localType7 = DocumentMenuHandler.this.mDoc.getType();
            Document.Type localType8 = Document.Type.TRACK;
            if (localType7 != localType8)
            {
              Document.Type localType9 = DocumentMenuHandler.this.mDoc.getType();
              Document.Type localType10 = Document.Type.GENRE;
              if (localType9 != localType10)
                break label149;
            }
          }
        }
      }
      if (MusicUtils.sService == null)
        return;
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext = DocumentMenuHandler.this.mContext;
      SongList localSongList = localDocument.getSongList(localContext);
      return;
      label149: Log.wtf("DocumentMenuHandler", "Unexpected doc type");
    }
  }

  private class StartInstantMix extends DocumentMenuHandler.StartRadio
  {
    StartInstantMix(Resources arg2)
    {
      super(localResources, 2131296260, 2131231277);
    }
  }

  private class StartRadio extends PlayCardMenuHandler.AsyncMenuEntry
  {
    StartRadio(Resources arg2)
    {
      super(2131296259, 2131231276);
    }

    StartRadio(Resources paramInt1, int paramInt2, int arg4)
    {
      super(paramInt2, i);
    }

    public void onActionSelected()
    {
      Context localContext1 = DocumentMenuHandler.this.mContext;
      Document localDocument = DocumentMenuHandler.this.mDoc;
      Context localContext2 = DocumentMenuHandler.this.mContext;
      SongList localSongList = localDocument.getSongList(localContext2);
      MusicUtils.playRadio(localContext1, localSongList);
    }
  }

  public static class DocumentContextMenuDelegate
    implements PlayCardView.ContextMenuDelegate
  {
    private final MusicFragment mMusicFragment;

    public DocumentContextMenuDelegate(MusicFragment paramMusicFragment)
    {
      this.mMusicFragment = paramMusicFragment;
    }

    public void onContextMenuPressed(PlayCardView paramPlayCardView, View paramView)
    {
      Document localDocument = (Document)paramPlayCardView.getDocument();
      MusicFragment localMusicFragment = this.mMusicFragment;
      new DocumentMenuHandler(localMusicFragment, localDocument).showPopupMenu(paramView);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.model.DocumentMenuHandler
 * JD-Core Version:    0.6.2
 */