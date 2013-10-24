package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.music.medialist.AllPlaylists;
import com.google.android.music.store.MusicContent.AutoPlaylists;
import com.google.android.music.store.MusicContent.Playlists;
import com.google.android.music.ui.cardlib.PlayCardClusterView;
import com.google.android.music.ui.cardlib.PlayCardClusterViewHeader;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardHeap;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.MoreClusterClickHandler;
import com.google.android.music.utils.MusicUtils;
import java.util.ArrayList;
import java.util.List;

public class PlaylistClustersFragment extends MediaListGridFragment
{
  static final String[] CURSOR_COLUMNS = arrayOfString;
  private PlayCardClusterViewHeader mAllPlaylistsHeader;
  private PlayCardClusterView mAutoPlaylistsCluster;
  private PlayCardHeap mCardHeap;
  private PlayCardClusterMetadata.CardMetadata mCardType;
  private ClusterLoaderCallbacks mClusterLoaderCallback;
  private PlayCardClusterView mRecentPlaylistsCluster;

  static
  {
    String[] arrayOfString = new String[9];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "playlist_name";
    arrayOfString[2] = "playlist_type";
    arrayOfString[3] = "playlist_description";
    arrayOfString[4] = "playlist_owner_name";
    arrayOfString[5] = "playlist_share_token";
    arrayOfString[6] = "playlist_art_url";
    arrayOfString[7] = "playlist_owner_profile_photo_url";
    arrayOfString[8] = "hasLocal";
  }

  public PlaylistClustersFragment()
  {
    ClusterLoaderCallbacks localClusterLoaderCallbacks = new ClusterLoaderCallbacks(null);
    this.mClusterLoaderCallback = localClusterLoaderCallbacks;
    PlayCardHeap localPlayCardHeap = new PlayCardHeap();
    this.mCardHeap = localPlayCardHeap;
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
    this.mCardType = localCardMetadata;
  }

  private PlayCardClusterView buildEmptyCluster(LayoutInflater paramLayoutInflater, ListView paramListView)
  {
    PlayCardClusterView localPlayCardClusterView = (PlayCardClusterView)paramLayoutInflater.inflate(2130968649, paramListView, false);
    PlayCardClusterMetadata localPlayCardClusterMetadata = new PlayCardClusterMetadata(1, 1);
    localPlayCardClusterView.setMetadata(localPlayCardClusterMetadata, null, null);
    localPlayCardClusterView.setVisibility(8);
    localPlayCardClusterView.hideHeader();
    return localPlayCardClusterView;
  }

  private static ArrayList<Document> buildPlaylistDocumentList(Cursor paramCursor, Context paramContext)
  {
    ArrayList localArrayList = new ArrayList();
    int i;
    int j;
    if (paramCursor.moveToFirst())
    {
      i = paramCursor.getCount();
      j = 0;
    }
    while (true)
    {
      if (j < i)
      {
        Document localDocument = populatePlaylistDocument(new Document(), paramCursor, paramContext);
        boolean bool = localArrayList.add(localDocument);
        if (paramCursor.moveToNext());
      }
      else
      {
        return localArrayList;
      }
      j += 1;
    }
  }

  private Cluster getClusterInfo(int paramInt, Cursor paramCursor)
  {
    Document.Type localType = Document.Type.PLAYLIST;
    FragmentActivity localFragmentActivity1 = getActivity();
    ArrayList localArrayList = buildPlaylistDocumentList(paramCursor, localFragmentActivity1);
    int i = getScreenColumns();
    int j = getResources().getInteger(2131492873);
    switch (paramInt)
    {
    default:
      String str1 = "Unexpected type value:" + paramInt;
      throw new IllegalStateException(str1);
    case 1:
    case 0:
    }
    for (String str2 = getResources().getString(2131231265); ; str2 = getResources().getString(2131231264))
    {
      FragmentActivity localFragmentActivity2 = getActivity();
      PlayCardClusterMetadata.CardMetadata localCardMetadata = this.mCardType;
      View.OnClickListener localOnClickListener = null;
      return new Cluster(localFragmentActivity2, localCardMetadata, str2, null, localArrayList, localType, i, j, localOnClickListener);
    }
  }

  private void populateCluster(PlayCardClusterView paramPlayCardClusterView, int paramInt, Cursor paramCursor)
  {
    if (paramCursor.getCount() == 0)
      return;
    PlaylistClustersFragment localPlaylistClustersFragment = this;
    int i = paramInt;
    Cursor localCursor = paramCursor;
    Cluster localCluster = localPlaylistClustersFragment.getClusterInfo(i, localCursor);
    int j = localCluster.getNbColumns();
    List localList1 = localCluster.getVisibleContent();
    PlayCardClusterMetadata.CardMetadata localCardMetadata = localCluster.getCardType();
    int k = (localList1.size() + j + -1) / j;
    int m = localCardMetadata.getHSpan() * j;
    int n = localCardMetadata.getVSpan() * k;
    PlayCardClusterMetadata localPlayCardClusterMetadata1 = new PlayCardClusterMetadata(m, n);
    int i1 = 0;
    while (true)
    {
      int i2 = localList1.size();
      if (i1 >= i2)
        break;
      int i3 = i1 / j;
      int i4 = localCardMetadata.getVSpan();
      int i5 = i3 * i4;
      int i6 = i1 % j;
      int i7 = localCardMetadata.getHSpan();
      int i8 = i6 * i7;
      PlayCardClusterMetadata localPlayCardClusterMetadata2 = localPlayCardClusterMetadata1.addTile(localCardMetadata, i8, i5);
      i1 += 1;
    }
    List localList2 = localCluster.getVisibleContent();
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    paramPlayCardClusterView.setMetadata(localPlayCardClusterMetadata1, localList2, localContextMenuDelegate);
    if (!paramPlayCardClusterView.hasCards())
    {
      PlayCardHeap localPlayCardHeap = this.mCardHeap;
      paramPlayCardClusterView.inflateContent(localPlayCardHeap);
    }
    paramPlayCardClusterView.createContent();
    int i9 = paramCursor.getCount();
    int i10 = localPlayCardClusterMetadata1.getTileCount();
    int i11 = i9 - i10;
    Resources localResources;
    Object[] arrayOfObject;
    if (i11 > 0)
    {
      localResources = getResources();
      arrayOfObject = new Object[1];
      Integer localInteger = Integer.valueOf(i11);
      arrayOfObject[0] = localInteger;
    }
    for (String str1 = localResources.getString(2131231303, arrayOfObject); ; str1 = null)
    {
      String str2 = localCluster.getTitle();
      paramPlayCardClusterView.showHeader(str2, null, str1);
      if (str1 != null)
      {
        MoreClusterClickHandler localMoreClusterClickHandler = localCluster.getMoreOnClickListener();
        paramPlayCardClusterView.setMoreClickHandler(localMoreClusterClickHandler);
      }
      paramPlayCardClusterView.setVisibility(0);
      return;
    }
  }

  private static Document populatePlaylistDocument(Document paramDocument, Cursor paramCursor, Context paramContext)
  {
    int i = 1;
    paramDocument.reset();
    Document.Type localType = Document.Type.PLAYLIST;
    paramDocument.setType(localType);
    long l = paramCursor.getLong(0);
    paramDocument.setId(l);
    String str1 = paramCursor.getString(i);
    paramDocument.setPlaylistName(str1);
    int k = paramCursor.getInt(2);
    paramDocument.setPlaylistType(k);
    String str2 = paramCursor.getString(3);
    paramDocument.setDescription(str2);
    String str3 = paramCursor.getString(4);
    paramDocument.setPlaylistOwnerName(str3);
    String str4 = paramCursor.getString(5);
    paramDocument.setPlaylistShareToken(str4);
    String str5 = paramCursor.getString(6);
    paramDocument.setArtUrl(str5);
    String str6 = paramCursor.getString(7);
    paramDocument.setPlaylistOwnerProfilePhotoUrl(str6);
    String str7 = paramDocument.getPlaylistName();
    paramDocument.setTitle(str7);
    int m = paramCursor.getInt(2);
    String str8 = paramCursor.getString(4);
    String str9;
    if (((m == 71) || (m == 70)) && (!TextUtils.isEmpty(str8)))
    {
      Resources localResources = paramContext.getResources();
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = str8;
      str9 = localResources.getString(2131230956, arrayOfObject);
      paramDocument.setSubTitle(str9);
      if (paramCursor.getInt(8) == 0)
        break label253;
    }
    while (true)
    {
      paramDocument.setHasLocal(i);
      return paramDocument;
      str9 = "";
      break;
      label253: int j = 0;
    }
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    setEmptyScreenText(2131231366);
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    return new PlaylistsAdapter(this, paramCursor, null);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    return new PlaylistsAdapter(this, null);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    AllPlaylists localAllPlaylists = new AllPlaylists();
    String[] arrayOfString = CURSOR_COLUMNS;
    init(localAllPlaylists, arrayOfString, false);
    super.onActivityCreated(paramBundle);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (paramCursor.getCount() > 0)
    {
      this.mAllPlaylistsHeader.show();
      super.onLoadFinished(paramLoader, paramCursor);
      if ((!this.mRecentPlaylistsCluster.hasCards()) && (!this.mAutoPlaylistsCluster.hasCards()))
        break label60;
    }
    label60: for (boolean bool = true; ; bool = false)
    {
      setIsCardHeaderShowing(bool);
      return;
      this.mAllPlaylistsHeader.hide();
      break;
    }
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    super.onViewCreated(paramView, paramBundle);
    LayoutInflater localLayoutInflater = getActivity().getLayoutInflater();
    ListView localListView = getListView();
    localListView.setHeaderDividersEnabled(false);
    PlayCardClusterView localPlayCardClusterView1 = buildEmptyCluster(localLayoutInflater, localListView);
    this.mRecentPlaylistsCluster = localPlayCardClusterView1;
    PlayCardClusterView localPlayCardClusterView2 = this.mRecentPlaylistsCluster;
    localListView.addHeaderView(localPlayCardClusterView2, null, false);
    PlayCardClusterView localPlayCardClusterView3 = buildEmptyCluster(localLayoutInflater, localListView);
    this.mAutoPlaylistsCluster = localPlayCardClusterView3;
    PlayCardClusterView localPlayCardClusterView4 = this.mAutoPlaylistsCluster;
    localListView.addHeaderView(localPlayCardClusterView4, null, false);
    PlayCardClusterViewHeader localPlayCardClusterViewHeader1 = (PlayCardClusterViewHeader)localLayoutInflater.inflate(2130968650, localListView, false);
    this.mAllPlaylistsHeader = localPlayCardClusterViewHeader1;
    PlayCardClusterViewHeader localPlayCardClusterViewHeader2 = this.mAllPlaylistsHeader;
    String str = getResources().getString(2131231266);
    localPlayCardClusterViewHeader2.setContent(str, null, null);
    this.mAllPlaylistsHeader.hide();
    PlayCardClusterViewHeader localPlayCardClusterViewHeader3 = this.mAllPlaylistsHeader;
    localListView.addHeaderView(localPlayCardClusterViewHeader3, null, false);
    LoaderManager localLoaderManager = getLoaderManager();
    ClusterLoaderCallbacks localClusterLoaderCallbacks1 = this.mClusterLoaderCallback;
    Loader localLoader1 = localLoaderManager.initLoader(100, null, localClusterLoaderCallbacks1);
    ClusterLoaderCallbacks localClusterLoaderCallbacks2 = this.mClusterLoaderCallback;
    Loader localLoader2 = localLoaderManager.initLoader(101, null, localClusterLoaderCallbacks2);
  }

  private final class PlaylistsAdapter extends MediaListCardAdapter
  {
    private PlaylistsAdapter(PlaylistClustersFragment arg2)
    {
      super(i);
    }

    private PlaylistsAdapter(PlaylistClustersFragment paramCursor, Cursor arg3)
    {
      super(i, localCursor);
    }

    protected void bindViewToLoadingItem(View paramView, Context paramContext)
    {
      if (!(paramView instanceof PlayCardView))
        return;
      ((PlayCardView)paramView).bindLoading();
    }

    protected void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong)
    {
      Document localDocument1 = MusicUtils.getDocument(paramView);
      Document localDocument2 = PlaylistClustersFragment.populatePlaylistDocument(localDocument1, paramCursor, paramContext);
      if (!(paramView instanceof PlayCardView))
        return;
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = PlaylistClustersFragment.this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument1, localContextMenuDelegate);
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = super.getView(paramInt, paramView, paramViewGroup);
      if ((localView instanceof PlayCardView))
      {
        PlayCardView localPlayCardView = (PlayCardView)localView;
        float f = PlaylistClustersFragment.this.mCardType.getThumbnailAspectRatio();
        localPlayCardView.setThumbnailAspectRatio(f);
      }
      return localView;
    }
  }

  private class ClusterLoaderCallbacks
    implements LoaderManager.LoaderCallbacks<Cursor>
  {
    private ClusterLoaderCallbacks()
    {
    }

    public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
    {
      FragmentActivity localFragmentActivity1;
      Uri localUri1;
      String[] arrayOfString1;
      String[] arrayOfString2;
      String str2;
      switch (paramInt)
      {
      default:
        String str1 = "Invalid loader id: " + paramInt;
        throw new IllegalArgumentException(str1);
      case 100:
        localFragmentActivity1 = PlaylistClustersFragment.this.getActivity();
        localUri1 = MusicContent.Playlists.getRecentPlaylistUri(4, false);
        arrayOfString1 = PlaylistClustersFragment.CURSOR_COLUMNS;
        arrayOfString2 = null;
        str2 = null;
      case 101:
      }
      FragmentActivity localFragmentActivity2;
      Uri localUri2;
      String[] arrayOfString3;
      String[] arrayOfString4;
      String str3;
      for (CursorLoader localCursorLoader = new CursorLoader(localFragmentActivity1, localUri1, arrayOfString1, null, arrayOfString2, str2); ; localCursorLoader = new CursorLoader(localFragmentActivity2, localUri2, arrayOfString3, null, arrayOfString4, str3))
      {
        return localCursorLoader;
        localFragmentActivity2 = PlaylistClustersFragment.this.getActivity();
        localUri2 = MusicContent.AutoPlaylists.CONTENT_URI;
        arrayOfString3 = PlaylistClustersFragment.CURSOR_COLUMNS;
        arrayOfString4 = null;
        str3 = null;
      }
    }

    public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
    {
      boolean bool = false;
      if (paramCursor == null)
        return;
      switch (paramLoader.getId())
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Invalid loader id: ");
        int i = paramLoader.getId();
        String str = i;
        throw new IllegalArgumentException(str);
      case 100:
        PlaylistClustersFragment localPlaylistClustersFragment1 = PlaylistClustersFragment.this;
        PlayCardClusterView localPlayCardClusterView1 = PlaylistClustersFragment.this.mRecentPlaylistsCluster;
        localPlaylistClustersFragment1.populateCluster(localPlayCardClusterView1, 0, paramCursor);
      case 101:
      }
      while (true)
      {
        if ((PlaylistClustersFragment.this.mRecentPlaylistsCluster.hasCards()) || (PlaylistClustersFragment.this.mAutoPlaylistsCluster.hasCards()))
          bool = true;
        PlaylistClustersFragment.this.setIsCardHeaderShowing(bool);
        return;
        PlaylistClustersFragment localPlaylistClustersFragment2 = PlaylistClustersFragment.this;
        PlayCardClusterView localPlayCardClusterView2 = PlaylistClustersFragment.this.mAutoPlaylistsCluster;
        localPlaylistClustersFragment2.populateCluster(localPlayCardClusterView2, 1, paramCursor);
      }
    }

    public void onLoaderReset(Loader<Cursor> paramLoader)
    {
      switch (paramLoader.getId())
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Invalid loader id: ");
        int i = paramLoader.getId();
        String str = i;
        throw new IllegalArgumentException(str);
      case 100:
        PlaylistClustersFragment.this.mRecentPlaylistsCluster.setVisibility(4);
        return;
      case 101:
      }
      PlaylistClustersFragment.this.mAutoPlaylistsCluster.setVisibility(4);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.PlaylistClustersFragment
 * JD-Core Version:    0.6.2
 */