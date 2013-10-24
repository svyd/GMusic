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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import com.google.android.music.medialist.AlbumList;
import com.google.android.music.medialist.ArtistAlbumList;
import com.google.android.music.medialist.ArtistSongList;
import com.google.android.music.medialist.NautilusArtistAlbumList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.Store;
import com.google.android.music.store.TagNormalizer;
import com.google.android.music.ui.cardlib.PlayCardClusterView;
import com.google.android.music.ui.cardlib.PlayCardClusterViewHeader;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardHeap;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.ExploreDocumentClusterBuilder;
import com.google.android.music.ui.cardlib.model.MoreClusterClickHandler;
import com.google.android.music.utils.MusicUtils;
import com.google.android.music.utils.async.AsyncRunner;
import java.util.ArrayList;
import java.util.List;

public class ArtistAlbumsGridFragment extends AlbumGridFragment
{
  private PlayCardClusterView mAllAlbumsCluster;
  private AllSongsView mAllSongs;
  private ArtistHeaderView mArtistHeader;
  private long mArtistId;
  private String mArtistMetajamId;
  private String mArtistName;
  private PlayCardHeap mCardHeap;
  private ClusterLoaderCallbacks mClusterLoaderCallback;
  private AlbumList mLocalAlbumList;
  private PlayCardClusterViewHeader mMyLibraryHeader;
  private PlayCardClusterView mRelatedArtistsCluster;
  private SpinnerHeaderView mSpinner;
  private PlayCardClusterView mTopSongsCluster;

  public ArtistAlbumsGridFragment()
  {
    ClusterLoaderCallbacks localClusterLoaderCallbacks = new ClusterLoaderCallbacks(null);
    this.mClusterLoaderCallback = localClusterLoaderCallbacks;
    this.mLocalAlbumList = null;
    this.mArtistId = 65535L;
    this.mArtistMetajamId = null;
    PlayCardHeap localPlayCardHeap = new PlayCardHeap();
    this.mCardHeap = localPlayCardHeap;
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

  private Cluster getClusterInfo(int paramInt, Cursor paramCursor)
  {
    PlayCardClusterMetadata.CardMetadata localCardMetadata1 = PlayCardClusterMetadata.CARD_SMALL;
    PlayTrackDocumentsClickListener localPlayTrackDocumentsClickListener = null;
    int i = getScreenColumns();
    int j = getResources().getInteger(2131492873);
    String str2;
    PlayCardClusterMetadata.CardMetadata localCardMetadata2;
    ArrayList localArrayList;
    Document.Type localType;
    switch (paramInt)
    {
    default:
      String str1 = "Unexpected type value:" + paramInt;
      throw new IllegalStateException(str1);
    case 1:
      str2 = getResources().getString(2131230867);
      localCardMetadata2 = PlayCardClusterMetadata.CARD_ROW;
      i = getResources().getInteger(2131492871);
      j = getResources().getInteger(2131492874);
      localArrayList = ExploreDocumentClusterBuilder.buildTrackDocumentList(paramCursor);
      localType = Document.Type.TRACK;
      localPlayTrackDocumentsClickListener = new PlayTrackDocumentsClickListener(localArrayList);
    case 0:
    case 2:
    }
    while (true)
    {
      FragmentActivity localFragmentActivity = getActivity();
      return new Cluster(localFragmentActivity, localCardMetadata2, str2, null, localArrayList, localType, i, j, localPlayTrackDocumentsClickListener);
      str2 = getResources().getString(2131230866);
      localCardMetadata2 = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
      localArrayList = ExploreDocumentClusterBuilder.buildAlbumDocumentList(paramCursor);
      localType = Document.Type.ALBUM;
      continue;
      str2 = getResources().getString(2131230868);
      localCardMetadata2 = PlayCardClusterMetadata.CARD_SMALL_2x1_WRAPPED;
      localArrayList = ExploreDocumentClusterBuilder.buildArtistDocumentList(paramCursor);
      localType = Document.Type.ARTIST;
    }
  }

  public static ArtistAlbumsGridFragment newInstance(AlbumList paramAlbumList)
  {
    ArtistAlbumsGridFragment localArtistAlbumsGridFragment = new ArtistAlbumsGridFragment();
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("AlbumList", paramAlbumList);
    localArtistAlbumsGridFragment.setArguments(localBundle);
    return localArtistAlbumsGridFragment;
  }

  private void populateCluster(PlayCardClusterView paramPlayCardClusterView, int paramInt, Cursor paramCursor)
  {
    if (paramCursor.getCount() == 0)
      return;
    ArtistAlbumsGridFragment localArtistAlbumsGridFragment = this;
    int i = paramInt;
    Cursor localCursor = paramCursor;
    Cluster localCluster = localArtistAlbumsGridFragment.getClusterInfo(i, localCursor);
    int j = localCluster.getNbColumns();
    List localList = localCluster.getVisibleContent();
    PlayCardClusterMetadata.CardMetadata localCardMetadata = localCluster.getCardType();
    int k = (localList.size() + j + -1) / j;
    int m = localCardMetadata.getHSpan() * j;
    int n = localCardMetadata.getVSpan() * k;
    PlayCardClusterMetadata localPlayCardClusterMetadata1 = new PlayCardClusterMetadata(m, n);
    int i1 = 0;
    while (true)
    {
      int i2 = localList.size();
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
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    View.OnClickListener localOnClickListener = localCluster.getItemOnClickListener();
    paramPlayCardClusterView.setMetadata(localPlayCardClusterMetadata1, localList, localContextMenuDelegate, localOnClickListener);
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

  public long getArtistId()
  {
    return this.mArtistId;
  }

  public String getArtistMetajamId()
  {
    return this.mArtistMetajamId;
  }

  protected void initEmptyScreen()
  {
  }

  MediaListCursorAdapter newAsyncCursorAdapter(Cursor paramCursor)
  {
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    return new ArtistAlbumsAdapter(this, paramCursor, localContextMenuDelegate);
  }

  MediaListCursorAdapter newLoaderCursorAdapter()
  {
    PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
    return new ArtistAlbumsAdapter(this, localContextMenuDelegate);
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (paramCursor.getCount() > 0)
      this.mMyLibraryHeader.show();
    if ((paramCursor.getCount() > 1) && (!getPreferences().isTabletMusicExperience()))
      this.mAllSongs.show();
    super.onLoadFinished(paramLoader, paramCursor);
  }

  public void onStart()
  {
    if (isClearedFromOnStop())
    {
      this.mArtistHeader.onStart();
      if (this.mAllAlbumsCluster != null)
      {
        this.mAllAlbumsCluster.createContent();
        this.mTopSongsCluster.createContent();
        this.mRelatedArtistsCluster.createContent();
      }
    }
    super.onStart();
  }

  public void onStop()
  {
    super.onStop();
    this.mArtistHeader.onStop();
    if (this.mAllAlbumsCluster == null)
      return;
    this.mAllAlbumsCluster.clearThumbnails();
    this.mTopSongsCluster.clearThumbnails();
    this.mRelatedArtistsCluster.clearThumbnails();
  }

  public void onViewCreated(View paramView, Bundle paramBundle)
  {
    final LayoutInflater localLayoutInflater = getActivity().getLayoutInflater();
    final AlbumList localAlbumList = (AlbumList)getArguments().getParcelable("AlbumList");
    final ListView localListView = getListView();
    localListView.setHeaderDividersEnabled(false);
    localListView.setFooterDividersEnabled(false);
    ArtistHeaderView localArtistHeaderView1 = (ArtistHeaderView)localLayoutInflater.inflate(2130968604, localListView, false);
    this.mArtistHeader = localArtistHeaderView1;
    this.mArtistHeader.setAlbumList(localAlbumList);
    ArtistHeaderView localArtistHeaderView2 = this.mArtistHeader;
    localListView.addHeaderView(localArtistHeaderView2, null, false);
    PlayCardClusterViewHeader localPlayCardClusterViewHeader1 = (PlayCardClusterViewHeader)localLayoutInflater.inflate(2130968650, localListView, false);
    this.mMyLibraryHeader = localPlayCardClusterViewHeader1;
    PlayCardClusterViewHeader localPlayCardClusterViewHeader2 = this.mMyLibraryHeader;
    String str = getResources().getString(2131230841);
    localPlayCardClusterViewHeader2.setContent(str, null, null);
    this.mMyLibraryHeader.hide();
    PlayCardClusterViewHeader localPlayCardClusterViewHeader3 = this.mMyLibraryHeader;
    localListView.addHeaderView(localPlayCardClusterViewHeader3, null, false);
    AllSongsView localAllSongsView1 = (AllSongsView)localLayoutInflater.inflate(2130968603, localListView, false);
    this.mAllSongs = localAllSongsView1;
    this.mAllSongs.hide();
    AllSongsView localAllSongsView2 = this.mAllSongs;
    localListView.addHeaderView(localAllSongsView2, null, false);
    AbsListView.OnScrollListener local1 = new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        ArtistAlbumsGridFragment.this.mArtistHeader.onScroll(paramAnonymousAbsListView, paramAnonymousInt1, paramAnonymousInt2, paramAnonymousInt3);
        View localView = paramAnonymousAbsListView.getChildAt(0);
        ArtistHeaderView localArtistHeaderView = ArtistAlbumsGridFragment.this.mArtistHeader;
        if (localView == localArtistHeaderView)
        {
          float f1 = -localView.getTop();
          int i = ArtistAlbumsGridFragment.this.mArtistHeader.getHeight();
          float f2 = i / 2;
          float f3 = f1 - f2;
          float f4 = i / 2;
          int j = (int)(f3 / f4 * 255.0F);
          if (j > 255)
            j = 255;
          if (j < 0)
            j = 0;
          ArtistAlbumsGridFragment.this.getActionBarController().setActionBarAlpha(j);
          return;
        }
        ArtistAlbumsGridFragment.this.getActionBarController().setActionBarAlpha(255);
      }

      public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt)
      {
      }
    };
    localListView.setOnScrollListener(local1);
    MusicUtils.runAsyncWithCallback(new AsyncRunner()
    {
      public void backgroundTask()
      {
        FragmentActivity localFragmentActivity = ArtistAlbumsGridFragment.this.getActivity();
        if (localFragmentActivity == null)
          return;
        ArtistAlbumsGridFragment localArtistAlbumsGridFragment1 = ArtistAlbumsGridFragment.this;
        String str1 = localAlbumList.getName(localFragmentActivity);
        String str2 = ArtistAlbumsGridFragment.access$202(localArtistAlbumsGridFragment1, str1);
        if ((localAlbumList instanceof NautilusArtistAlbumList))
        {
          NautilusArtistAlbumList localNautilusArtistAlbumList = (NautilusArtistAlbumList)localAlbumList;
          TagNormalizer localTagNormalizer = new TagNormalizer();
          String str3 = ArtistAlbumsGridFragment.this.mArtistName;
          Pair localPair = Store.canonicalizeAndGenerateId(localTagNormalizer, str3);
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment2 = ArtistAlbumsGridFragment.this;
          long l1 = ((Long)localPair.first).longValue();
          String str4 = ArtistAlbumsGridFragment.this.mArtistName;
          ArtistAlbumList localArtistAlbumList = new ArtistAlbumList(l1, str4, true);
          AlbumList localAlbumList1 = ArtistAlbumsGridFragment.access$302(localArtistAlbumsGridFragment2, localArtistAlbumList);
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment3 = ArtistAlbumsGridFragment.this;
          long l2 = ((Long)localPair.first).longValue();
          long l3 = ArtistAlbumsGridFragment.access$402(localArtistAlbumsGridFragment3, l2);
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment4 = ArtistAlbumsGridFragment.this;
          String str5 = localNautilusArtistAlbumList.getNautilusId();
          String str6 = ArtistAlbumsGridFragment.access$502(localArtistAlbumsGridFragment4, str5);
          return;
        }
        if (!(localAlbumList instanceof ArtistAlbumList))
          return;
        ArtistAlbumsGridFragment localArtistAlbumsGridFragment5 = ArtistAlbumsGridFragment.this;
        AlbumList localAlbumList2 = localAlbumList;
        AlbumList localAlbumList3 = ArtistAlbumsGridFragment.access$302(localArtistAlbumsGridFragment5, localAlbumList2);
        ArtistAlbumsGridFragment localArtistAlbumsGridFragment6 = ArtistAlbumsGridFragment.this;
        long l4 = localAlbumList.getArtistId(localFragmentActivity);
        long l5 = ArtistAlbumsGridFragment.access$402(localArtistAlbumsGridFragment6, l4);
        if (ArtistAlbumsGridFragment.this.mArtistId <= 0L)
          return;
        try
        {
          Uri localUri = MusicContent.Artists.getArtistsUri(localAlbumList.getArtistId(localFragmentActivity));
          String[] arrayOfString = new String[1];
          arrayOfString[0] = "ArtistMetajamId";
          localCursor = MusicUtils.query(localFragmentActivity, localUri, arrayOfString, null, null, null);
          if ((localCursor != null) && (localCursor.moveToFirst()))
          {
            ArtistAlbumsGridFragment localArtistAlbumsGridFragment7 = ArtistAlbumsGridFragment.this;
            String str7 = localCursor.getString(0);
            String str8 = ArtistAlbumsGridFragment.access$502(localArtistAlbumsGridFragment7, str7);
          }
          return;
        }
        finally
        {
          Cursor localCursor;
          Store.safeClose(localCursor);
        }
      }

      public void taskCompleted()
      {
        FragmentActivity localFragmentActivity = ArtistAlbumsGridFragment.this.getActivity();
        if (localFragmentActivity == null)
          return;
        if (ArtistAlbumsGridFragment.this.mLocalAlbumList == null)
          throw new IllegalStateException("Local AlbumList was null");
        ActionBarController localActionBarController = ArtistAlbumsGridFragment.this.getActionBarController();
        String str1 = ArtistAlbumsGridFragment.this.mArtistName;
        localActionBarController.setActionBarTitle(str1);
        long l = ArtistAlbumsGridFragment.this.mLocalAlbumList.getArtistId(localFragmentActivity);
        String str2 = ArtistAlbumsGridFragment.this.mArtistName;
        int i = ArtistAlbumsGridFragment.this.getPreferences().getArtistSongsSortOrder();
        ArtistSongList localArtistSongList = new ArtistSongList(l, str2, i, true);
        ArtistAlbumsGridFragment.this.mAllSongs.setSongList(localArtistSongList);
        if ((ArtistAlbumsGridFragment.this.getPreferences().isNautilusEnabled()) && (ArtistAlbumsGridFragment.this.mArtistMetajamId != null))
        {
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment1 = ArtistAlbumsGridFragment.this;
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment2 = ArtistAlbumsGridFragment.this;
          LayoutInflater localLayoutInflater1 = localLayoutInflater;
          ListView localListView1 = localListView;
          PlayCardClusterView localPlayCardClusterView1 = localArtistAlbumsGridFragment2.buildEmptyCluster(localLayoutInflater1, localListView1);
          PlayCardClusterView localPlayCardClusterView2 = ArtistAlbumsGridFragment.access$702(localArtistAlbumsGridFragment1, localPlayCardClusterView1);
          ListView localListView2 = localListView;
          PlayCardClusterView localPlayCardClusterView3 = ArtistAlbumsGridFragment.this.mTopSongsCluster;
          localListView2.addFooterView(localPlayCardClusterView3, null, false);
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment3 = ArtistAlbumsGridFragment.this;
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment4 = ArtistAlbumsGridFragment.this;
          LayoutInflater localLayoutInflater2 = localLayoutInflater;
          ListView localListView3 = localListView;
          PlayCardClusterView localPlayCardClusterView4 = localArtistAlbumsGridFragment4.buildEmptyCluster(localLayoutInflater2, localListView3);
          PlayCardClusterView localPlayCardClusterView5 = ArtistAlbumsGridFragment.access$902(localArtistAlbumsGridFragment3, localPlayCardClusterView4);
          ListView localListView4 = localListView;
          PlayCardClusterView localPlayCardClusterView6 = ArtistAlbumsGridFragment.this.mAllAlbumsCluster;
          localListView4.addFooterView(localPlayCardClusterView6, null, false);
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment5 = ArtistAlbumsGridFragment.this;
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment6 = ArtistAlbumsGridFragment.this;
          LayoutInflater localLayoutInflater3 = localLayoutInflater;
          ListView localListView5 = localListView;
          PlayCardClusterView localPlayCardClusterView7 = localArtistAlbumsGridFragment6.buildEmptyCluster(localLayoutInflater3, localListView5);
          PlayCardClusterView localPlayCardClusterView8 = ArtistAlbumsGridFragment.access$1002(localArtistAlbumsGridFragment5, localPlayCardClusterView7);
          ListView localListView6 = localListView;
          PlayCardClusterView localPlayCardClusterView9 = ArtistAlbumsGridFragment.this.mRelatedArtistsCluster;
          localListView6.addFooterView(localPlayCardClusterView9, null, false);
          ArtistAlbumsGridFragment localArtistAlbumsGridFragment7 = ArtistAlbumsGridFragment.this;
          LayoutInflater localLayoutInflater4 = localLayoutInflater;
          ListView localListView7 = localListView;
          SpinnerHeaderView localSpinnerHeaderView1 = (SpinnerHeaderView)localLayoutInflater4.inflate(2130968684, localListView7, false);
          SpinnerHeaderView localSpinnerHeaderView2 = ArtistAlbumsGridFragment.access$1102(localArtistAlbumsGridFragment7, localSpinnerHeaderView1);
          ListView localListView8 = localListView;
          SpinnerHeaderView localSpinnerHeaderView3 = ArtistAlbumsGridFragment.this.mSpinner;
          localListView8.addFooterView(localSpinnerHeaderView3, null, false);
          LoaderManager localLoaderManager = ArtistAlbumsGridFragment.this.getLoaderManager();
          ArtistAlbumsGridFragment.ClusterLoaderCallbacks localClusterLoaderCallbacks1 = ArtistAlbumsGridFragment.this.mClusterLoaderCallback;
          Loader localLoader1 = localLoaderManager.initLoader(100, null, localClusterLoaderCallbacks1);
          ArtistAlbumsGridFragment.ClusterLoaderCallbacks localClusterLoaderCallbacks2 = ArtistAlbumsGridFragment.this.mClusterLoaderCallback;
          Loader localLoader2 = localLoaderManager.initLoader(101, null, localClusterLoaderCallbacks2);
          ArtistAlbumsGridFragment.ClusterLoaderCallbacks localClusterLoaderCallbacks3 = ArtistAlbumsGridFragment.this.mClusterLoaderCallback;
          Loader localLoader3 = localLoaderManager.initLoader(102, null, localClusterLoaderCallbacks3);
        }
        ArtistAlbumsGridFragment localArtistAlbumsGridFragment8 = ArtistAlbumsGridFragment.this;
        AlbumList localAlbumList = ArtistAlbumsGridFragment.this.mLocalAlbumList;
        localArtistAlbumsGridFragment8.init(localAlbumList);
      }
    });
  }

  final class ArtistAlbumsAdapter extends AlbumsAdapter
  {
    protected ArtistAlbumsAdapter(AlbumGridFragment paramCursor, Cursor paramContextMenuDelegate, PlayCardView.ContextMenuDelegate arg4)
    {
      super(paramContextMenuDelegate, localContextMenuDelegate);
      init();
    }

    protected ArtistAlbumsAdapter(AlbumGridFragment paramContextMenuDelegate, PlayCardView.ContextMenuDelegate arg3)
    {
      super(localContextMenuDelegate);
      init();
    }

    public View getFakeView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      Document localDocument = MusicUtils.getDocument(paramView);
      Document.Type localType = Document.Type.ALL_SONGS_ARTIST;
      localDocument.setType(localType);
      long l1 = ArtistAlbumsGridFragment.this.mArtistId;
      localDocument.setId(l1);
      long l2 = ArtistAlbumsGridFragment.this.mArtistId;
      localDocument.setArtistId(l2);
      String str1 = ArtistAlbumsGridFragment.this.mArtistName;
      localDocument.setArtistName(str1);
      String str2 = ArtistAlbumsGridFragment.this.mArtistName;
      localDocument.setTitle(str2);
      String str3 = getContext().getString(2131230751);
      localDocument.setSubTitle(str3);
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      localPlayCardView.setTag(localDocument);
      paramView.setOnClickListener(this);
      return localPlayCardView;
    }

    protected void init()
    {
      super.init();
      if (!ArtistAlbumsGridFragment.this.getPreferences().isTabletMusicExperience())
        return;
      setDisableFake(false);
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
      CursorLoader localCursorLoader;
      switch (paramInt)
      {
      default:
        String str1 = "Invalid loader id: " + paramInt;
        throw new IllegalArgumentException(str1);
      case 100:
        FragmentActivity localFragmentActivity1 = ArtistAlbumsGridFragment.this.getActivity();
        Uri localUri1 = MusicContent.Artists.getAlbumsByNautilusArtistsUri(ArtistAlbumsGridFragment.this.mArtistMetajamId);
        String[] arrayOfString1 = ExploreDocumentClusterBuilder.ALBUM_COLUMNS;
        String[] arrayOfString2 = null;
        String str2 = null;
        localCursorLoader = new CursorLoader(localFragmentActivity1, localUri1, arrayOfString1, null, arrayOfString2, str2);
      case 101:
      case 102:
      }
      while (true)
      {
        return localCursorLoader;
        FragmentActivity localFragmentActivity2 = ArtistAlbumsGridFragment.this.getActivity();
        Uri localUri2 = MusicContent.Artists.getTopSongsByArtistUri(ArtistAlbumsGridFragment.this.mArtistMetajamId);
        String[] arrayOfString3 = ExploreDocumentClusterBuilder.SONG_COLUMNS;
        String[] arrayOfString4 = null;
        String str3 = null;
        localCursorLoader = new CursorLoader(localFragmentActivity2, localUri2, arrayOfString3, null, arrayOfString4, str3);
        continue;
        FragmentActivity localFragmentActivity3 = ArtistAlbumsGridFragment.this.getActivity();
        Uri localUri3 = MusicContent.Artists.getRelatedArtistsUri(ArtistAlbumsGridFragment.this.mArtistMetajamId);
        String[] arrayOfString5 = ExploreDocumentClusterBuilder.ARTIST_COLUMNS;
        String[] arrayOfString6 = null;
        String str4 = null;
        localCursorLoader = new CursorLoader(localFragmentActivity3, localUri3, arrayOfString5, null, arrayOfString6, str4);
      }
    }

    public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
    {
      if (paramCursor == null)
        return;
      if (ArtistAlbumsGridFragment.this.mSpinner != null)
        ArtistAlbumsGridFragment.this.mSpinner.hide();
      switch (paramLoader.getId())
      {
      default:
        StringBuilder localStringBuilder = new StringBuilder().append("Invalid loader id: ");
        int i = paramLoader.getId();
        String str = i;
        throw new IllegalArgumentException(str);
      case 100:
        ArtistAlbumsGridFragment localArtistAlbumsGridFragment1 = ArtistAlbumsGridFragment.this;
        PlayCardClusterView localPlayCardClusterView1 = ArtistAlbumsGridFragment.this.mAllAlbumsCluster;
        localArtistAlbumsGridFragment1.populateCluster(localPlayCardClusterView1, 0, paramCursor);
        return;
      case 101:
        ArtistAlbumsGridFragment localArtistAlbumsGridFragment2 = ArtistAlbumsGridFragment.this;
        PlayCardClusterView localPlayCardClusterView2 = ArtistAlbumsGridFragment.this.mTopSongsCluster;
        localArtistAlbumsGridFragment2.populateCluster(localPlayCardClusterView2, 1, paramCursor);
        return;
      case 102:
      }
      ArtistAlbumsGridFragment localArtistAlbumsGridFragment3 = ArtistAlbumsGridFragment.this;
      PlayCardClusterView localPlayCardClusterView3 = ArtistAlbumsGridFragment.this.mRelatedArtistsCluster;
      localArtistAlbumsGridFragment3.populateCluster(localPlayCardClusterView3, 2, paramCursor);
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
        ArtistAlbumsGridFragment.this.mAllAlbumsCluster.setVisibility(4);
        return;
      case 101:
        ArtistAlbumsGridFragment.this.mTopSongsCluster.setVisibility(4);
        return;
      case 102:
      }
      ArtistAlbumsGridFragment.this.mRelatedArtistsCluster.setVisibility(4);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ArtistAlbumsGridFragment
 * JD-Core Version:    0.6.2
 */