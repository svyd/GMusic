package com.google.android.music.ui;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.util.Log;
import com.google.android.music.medialist.NautilusSingleSongList;
import com.google.android.music.medialist.SingleSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.store.MusicContent.Search;
import com.google.android.music.store.ProjectionUtils;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.SearchDocumentBuilder;
import com.google.android.music.utils.MusicUtils;
import java.util.ArrayList;

public class SearchMusicClustersFragment extends SearchClustersFragment
{
  private static final String[] SEARCH_TYPE = arrayOfString;

  static
  {
    String[] arrayOfString = new String[5];
    arrayOfString[0] = "bestmatch";
    arrayOfString[1] = "genre";
    arrayOfString[2] = "artist";
    arrayOfString[3] = "album";
    arrayOfString[4] = "track";
  }

  public SearchMusicClustersFragment()
  {
  }

  public SearchMusicClustersFragment(String paramString)
  {
    super(paramString);
  }

  protected Cluster createCluster(int paramInt, Cursor paramCursor)
  {
    Cluster localCluster = null;
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
    ArrayList localArrayList = null;
    Document.Type localType = Document.Type.ALBUM;
    int i = getScreenColumns();
    int j = getResources().getInteger(2131492873);
    String str1 = SEARCH_TYPE[paramInt];
    int m;
    String str2;
    if ("bestmatch".equals(str1))
    {
      int k = paramCursor.getColumnIndexOrThrow("searchType");
      if (paramCursor.moveToFirst())
      {
        m = paramCursor.getInt(k);
        if ((m == 3) || (m == 7))
        {
          localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
          localArrayList = SearchDocumentBuilder.buildAlbumDocumentList(paramCursor);
          localType = Document.Type.ALBUM;
          str2 = getResources().getString(2131230854);
        }
      }
    }
    while (true)
    {
      FragmentActivity localFragmentActivity = getActivity();
      String str3 = this.mSearchString;
      localCluster = new Cluster(localFragmentActivity, localCardMetadata, str2, str3, localArrayList, localType, i, j, null);
      while (true)
      {
        return localCluster;
        if ((m == 2) || (m == 1) || (m == 6))
        {
          localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_2x1_WRAPPED;
          localArrayList = SearchDocumentBuilder.buildArtistDocumentList(paramCursor);
          localType = Document.Type.ARTIST;
          break;
        }
        if ((m == 5) || (m == 8))
        {
          localCardMetadata = PlayCardClusterMetadata.CARD_ROW;
          i = getResources().getInteger(2131492871);
          localArrayList = SearchDocumentBuilder.buildTrackDocumentList(paramCursor);
          localType = Document.Type.TRACK;
          break;
        }
        if (m != 9)
          break;
        localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
        localArrayList = SearchDocumentBuilder.buildGenreDocumentList(paramCursor, false);
        localType = Document.Type.NAUTILUS_GENRE;
        break;
        int n = Log.w("SearchMusicClusters", "Failed to move the cursor");
      }
      String str4 = SEARCH_TYPE[paramInt];
      if ("artist".equals(str4))
      {
        localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_2x1_WRAPPED;
        str2 = getResources().getString(2131230848);
        localArrayList = SearchDocumentBuilder.buildArtistDocumentList(paramCursor);
        localType = Document.Type.ARTIST;
      }
      else
      {
        String str5 = SEARCH_TYPE[paramInt];
        if ("album".equals(str5))
        {
          localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
          str2 = getResources().getString(2131230849);
          localArrayList = SearchDocumentBuilder.buildAlbumDocumentList(paramCursor);
          localType = Document.Type.ALBUM;
        }
        else
        {
          String str6 = SEARCH_TYPE[paramInt];
          if ("genre".equals(str6))
          {
            localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
            str2 = getResources().getString(2131230850);
            localArrayList = SearchDocumentBuilder.buildGenreDocumentList(paramCursor, false);
            localType = Document.Type.NAUTILUS_GENRE;
          }
          else
          {
            localCardMetadata = PlayCardClusterMetadata.CARD_ROW;
            i = getResources().getInteger(2131492871);
            j = getResources().getInteger(2131492874);
            str2 = getResources().getString(2131230851);
            localArrayList = SearchDocumentBuilder.buildTrackDocumentList(paramCursor);
            localType = Document.Type.TRACK;
          }
        }
      }
    }
  }

  protected Uri getClusterContentUri(int paramInt)
  {
    String str = SEARCH_TYPE[paramInt];
    return MusicContent.Search.getSearchUri(this.mSearchString, str);
  }

  protected int getNumberOfClusters()
  {
    return SEARCH_TYPE.length;
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    long l;
    Object localObject;
    if (this.mPlayFirstSong)
    {
      String[] arrayOfString = SEARCH_TYPE;
      int i = paramLoader.getId();
      if ((arrayOfString[i].equals("track")) && (paramCursor != null) && (paramCursor.moveToFirst()))
      {
        int j = paramCursor.getColumnIndexOrThrow("_id");
        l = paramCursor.getLong(j);
        localObject = null;
        if (!ProjectionUtils.isFauxNautilusId(l))
          break label162;
        int k = paramCursor.getColumnIndexOrThrow("Nid");
        if (paramCursor.isNull(k))
          break label150;
        String str = paramCursor.getString(k);
        localObject = new NautilusSingleSongList(str, "");
      }
    }
    while (true)
    {
      if (localObject != null)
        MusicUtils.playMediaList(getActivity(), (SongList)localObject, -1);
      boolean bool = paramCursor.moveToPosition(-1);
      super.onLoadFinished(paramLoader, paramCursor);
      return;
      label150: int m = Log.e("SearchMusicClusters", "Couldn't get the track metajam id.");
      continue;
      label162: localObject = new SingleSongList(l, "");
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SearchMusicClustersFragment
 * JD-Core Version:    0.6.2
 */