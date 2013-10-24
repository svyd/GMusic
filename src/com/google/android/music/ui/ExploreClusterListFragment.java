package com.google.android.music.ui;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentMenuHandler.DocumentContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.ExploreDocumentClusterBuilder;
import com.google.android.music.utils.LabelUtils;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class ExploreClusterListFragment extends BaseClusterListFragment
{
  private static final String[] GROUPS_COLUMNS = arrayOfString;
  protected final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private Cursor mGroupData;
  private Uri mRootUri;

  static
  {
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "description";
    arrayOfString[3] = "size";
    arrayOfString[4] = "groupType";
  }

  public ExploreClusterListFragment()
  {
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
  }

  public ExploreClusterListFragment(Uri paramUri)
  {
    DocumentMenuHandler.DocumentContextMenuDelegate localDocumentContextMenuDelegate = new DocumentMenuHandler.DocumentContextMenuDelegate(this);
    this.mCardsContextMenuDelegate = localDocumentContextMenuDelegate;
    this.mRootUri = paramUri;
  }

  private void moveToGroupOrThrow(int paramInt)
  {
    if (this.mGroupData == null)
      throw new IllegalStateException("No group info");
    if (this.mGroupData.moveToPosition(paramInt))
      return;
    StringBuilder localStringBuilder = new StringBuilder().append("Invalid group requested. Index: ").append(paramInt).append(". Total: ");
    int i = this.mGroupData.getCount();
    String str = i;
    throw new IllegalStateException(str);
  }

  protected Cluster createCluster(int paramInt, Cursor paramCursor)
  {
    moveToGroupOrThrow(paramInt);
    String str1 = this.mGroupData.getString(1);
    int i = this.mGroupData.getInt(4);
    PlayCardClusterMetadata.CardMetadata localCardMetadata1 = PlayCardClusterMetadata.CARD_SMALL;
    PlayTrackDocumentsClickListener localPlayTrackDocumentsClickListener = null;
    int j = getScreenColumns();
    int k = getResources().getInteger(2131492873);
    PlayCardClusterMetadata.CardMetadata localCardMetadata2;
    ArrayList localArrayList;
    Document.Type localType;
    switch (i)
    {
    default:
      String str2 = "Unexpected type value:" + i;
      throw new IllegalStateException(str2);
    case 0:
      localCardMetadata2 = PlayCardClusterMetadata.CARD_ROW;
      j = getResources().getInteger(2131492871);
      k = getResources().getInteger(2131492874);
      localArrayList = ExploreDocumentClusterBuilder.buildTrackDocumentList(paramCursor);
      localType = Document.Type.TRACK;
      localPlayTrackDocumentsClickListener = new PlayTrackDocumentsClickListener(localArrayList);
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      FragmentActivity localFragmentActivity1 = getActivity();
      return new Cluster(localFragmentActivity1, localCardMetadata2, str1, null, localArrayList, localType, j, k, localPlayTrackDocumentsClickListener);
      localCardMetadata2 = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
      localArrayList = ExploreDocumentClusterBuilder.buildAlbumDocumentList(paramCursor);
      localType = Document.Type.ALBUM;
      continue;
      localCardMetadata2 = PlayCardClusterMetadata.CARD_SMALL_2x1_WRAPPED;
      localArrayList = ExploreDocumentClusterBuilder.buildArtistDocumentList(paramCursor);
      localType = Document.Type.ARTIST;
      continue;
      localCardMetadata2 = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
      localArrayList = ExploreDocumentClusterBuilder.buildPlaylistDocumentList(paramCursor);
      Iterator localIterator = localArrayList.iterator();
      if (localIterator.hasNext())
      {
        Document localDocument = (Document)localIterator.next();
        int m = localDocument.getPlaylistType();
        Object[] arrayOfObject;
        if (((m == 71) || (m == 70)) && (!TextUtils.isEmpty(localDocument.getPlaylistOwnerName())))
        {
          arrayOfObject = new Object[1];
          String str3 = localDocument.getPlaylistOwnerName();
          arrayOfObject[0] = str3;
        }
        FragmentActivity localFragmentActivity2;
        MusicPreferences localMusicPreferences;
        for (String str4 = getString(2131230956, arrayOfObject); ; str4 = LabelUtils.getPlaylistSecondaryLabel(localFragmentActivity2, localMusicPreferences, m))
        {
          localDocument.setSubTitle(str4);
          break;
          localFragmentActivity2 = getActivity();
          localMusicPreferences = getPreferences();
        }
      }
      localType = Document.Type.PLAYLIST;
    }
  }

  protected PlayCardView.ContextMenuDelegate getCardsContextMenuDelegate()
  {
    return this.mCardsContextMenuDelegate;
  }

  protected Uri getClusterContentUri(int paramInt)
  {
    moveToGroupOrThrow(paramInt);
    long l = this.mGroupData.getLong(0);
    return getGroupQueryUri(l);
  }

  protected String[] getClusterProjection(int paramInt)
  {
    moveToGroupOrThrow(paramInt);
    String[] arrayOfString;
    switch (this.mGroupData.getInt(4))
    {
    default:
      throw new IllegalStateException("Unexpected type value");
    case 0:
      arrayOfString = ExploreDocumentClusterBuilder.SONG_COLUMNS;
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      return arrayOfString;
      arrayOfString = ExploreDocumentClusterBuilder.ALBUM_COLUMNS;
      continue;
      arrayOfString = ExploreDocumentClusterBuilder.ARTIST_COLUMNS;
      continue;
      arrayOfString = ExploreDocumentClusterBuilder.SHARED_WITH_ME_PLAYLIST_COLUMNS;
    }
  }

  protected abstract Uri getGroupQueryUri(long paramLong);

  protected int getNumberOfClusters()
  {
    if (this.mGroupData == null);
    for (int i = 0; ; i = this.mGroupData.getCount())
      return i;
  }

  public void init(Uri paramUri)
  {
    this.mRootUri = paramUri;
  }

  protected void initEmptyScreen()
  {
    super.initEmptyScreen();
    setEmptyScreenText(2131231365);
    setEmptyImageView(2130837707);
  }

  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    if (this.mRootUri == null)
      throw new IllegalStateException("Search string is not initialized");
    Loader localLoader = getLoaderManager().initLoader(1000, null, this);
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    if (paramInt != 1000);
    FragmentActivity localFragmentActivity;
    Uri localUri;
    String[] arrayOfString1;
    String[] arrayOfString2;
    String str;
    for (Object localObject = super.onCreateLoader(paramInt, paramBundle); ; localObject = new CursorLoader(localFragmentActivity, localUri, arrayOfString1, null, arrayOfString2, str))
    {
      return localObject;
      localFragmentActivity = getActivity();
      localUri = this.mRootUri;
      arrayOfString1 = GROUPS_COLUMNS;
      arrayOfString2 = null;
      str = null;
    }
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    if (paramLoader.getId() != 1000)
    {
      super.onLoadFinished(paramLoader, paramCursor);
      return;
    }
    this.mGroupData = paramCursor;
    startLoading();
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    StringBuilder localStringBuilder = new StringBuilder().append("onLoaderReset is called for loader ");
    int i = paramLoader.getId();
    String str = i;
    int j = Log.i("ExploreClusterListFragment", str);
    if (paramLoader.getId() != 1000)
    {
      super.onLoaderReset(paramLoader);
      return;
    }
    this.mGroupData = null;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ExploreClusterListFragment
 * JD-Core Version:    0.6.2
 */