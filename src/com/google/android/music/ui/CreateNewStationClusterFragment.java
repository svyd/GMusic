package com.google.android.music.ui;

import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.store.MusicContent.Search;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.SearchDocumentBuilder;
import java.util.ArrayList;
import java.util.Iterator;

public class CreateNewStationClusterFragment extends SearchClustersFragment
{
  private final String[] mRadioStationTypes;

  public CreateNewStationClusterFragment()
  {
    ArrayList localArrayList = new ArrayList();
    boolean bool1 = localArrayList.add("bestmatch");
    if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
      boolean bool2 = localArrayList.add("genre");
    boolean bool3 = localArrayList.add("artist");
    boolean bool4 = localArrayList.add("album");
    boolean bool5 = localArrayList.add("track");
    String[] arrayOfString1 = new String[localArrayList.size()];
    String[] arrayOfString2 = (String[])localArrayList.toArray(arrayOfString1);
    this.mRadioStationTypes = arrayOfString2;
  }

  public CreateNewStationClusterFragment(String paramString)
  {
    super(paramString);
    ArrayList localArrayList = new ArrayList();
    boolean bool1 = localArrayList.add("bestmatch");
    if (UIStateManager.getInstance().getPrefs().isNautilusEnabled())
      boolean bool2 = localArrayList.add("genre");
    boolean bool3 = localArrayList.add("artist");
    boolean bool4 = localArrayList.add("album");
    boolean bool5 = localArrayList.add("track");
    String[] arrayOfString1 = new String[localArrayList.size()];
    String[] arrayOfString2 = (String[])localArrayList.toArray(arrayOfString1);
    this.mRadioStationTypes = arrayOfString2;
  }

  protected Cluster createCluster(int paramInt, Cursor paramCursor)
  {
    PlayCardClusterMetadata.CardMetadata localCardMetadata = PlayCardClusterMetadata.CARD_SMALL_WRAPPED;
    ArrayList localArrayList = null;
    Document.Type localType = Document.Type.ALBUM;
    int i = getScreenColumns();
    int j = getResources().getInteger(2131492873);
    String str1 = this.mRadioStationTypes[paramInt];
    String str2;
    if ("bestmatch".equals(str1))
    {
      int k = paramCursor.getColumnIndexOrThrow("searchType");
      if (paramCursor.moveToFirst())
      {
        int m = paramCursor.getInt(k);
        if ((m == 3) || (m == 7))
        {
          localArrayList = SearchDocumentBuilder.buildAlbumDocumentList(paramCursor);
          localType = Document.Type.ALBUM;
        }
        while (true)
        {
          str2 = getResources().getString(2131230854);
          Iterator localIterator = localArrayList.iterator();
          while (localIterator.hasNext())
            ((Document)localIterator.next()).setIsEmulatedRadio(true);
          if ((m == 2) || (m == 1) || (m == 6))
          {
            localArrayList = SearchDocumentBuilder.buildArtistDocumentList(paramCursor);
            localType = Document.Type.ARTIST;
          }
          else if ((m == 5) || (m == 8))
          {
            localArrayList = SearchDocumentBuilder.buildTrackDocumentList(paramCursor);
            localType = Document.Type.TRACK;
          }
          else if (m == 9)
          {
            localArrayList = SearchDocumentBuilder.buildGenreDocumentList(paramCursor, true);
            localType = Document.Type.NAUTILUS_GENRE;
          }
        }
      }
      int n = Log.w("CreateNewStationCluster", "Failed to move the cursor");
    }
    FragmentActivity localFragmentActivity;
    String str6;
    for (Cluster localCluster = null; ; localCluster = new Cluster(localFragmentActivity, localCardMetadata, str2, str6, localArrayList, localType, i, j, null, true))
    {
      return localCluster;
      String str3 = this.mRadioStationTypes[paramInt];
      if ("artist".equals(str3))
      {
        if (UIStateManager.getInstance().getPrefs().isNautilusEnabled());
        for (str2 = getResources().getString(2131231299); ; str2 = getResources().getString(2131231304))
        {
          localArrayList = SearchDocumentBuilder.buildArtistDocumentList(paramCursor);
          localType = Document.Type.ARTIST;
          break;
        }
      }
      String str4 = this.mRadioStationTypes[paramInt];
      if ("album".equals(str4))
      {
        if (UIStateManager.getInstance().getPrefs().isNautilusEnabled());
        for (str2 = getResources().getString(2131231300); ; str2 = getResources().getString(2131231305))
        {
          localArrayList = SearchDocumentBuilder.buildAlbumDocumentList(paramCursor);
          localType = Document.Type.ALBUM;
          break;
        }
      }
      String str5 = this.mRadioStationTypes[paramInt];
      if ("genre".equals(str5))
      {
        str2 = getResources().getString(2131231302);
        localArrayList = SearchDocumentBuilder.buildGenreDocumentList(paramCursor, true);
        localType = Document.Type.NAUTILUS_GENRE;
        break;
      }
      if (UIStateManager.getInstance().getPrefs().isNautilusEnabled());
      for (str2 = getResources().getString(2131231301); ; str2 = getResources().getString(2131231306))
      {
        localArrayList = SearchDocumentBuilder.buildTrackDocumentList(paramCursor);
        localType = Document.Type.TRACK;
        break;
      }
      localFragmentActivity = getActivity();
      str6 = this.mSearchString;
    }
  }

  protected Uri getClusterContentUri(int paramInt)
  {
    String str = this.mRadioStationTypes[paramInt];
    return MusicContent.Search.getSearchUri(this.mSearchString, str);
  }

  protected int getNumberOfClusters()
  {
    return this.mRadioStationTypes.length;
  }

  protected void setupSearchEmptyScreen()
  {
    int i;
    if ((this.mSearchString == null) || (this.mSearchString.length() < 2))
    {
      i = 1;
      if (i == 0)
        break label80;
      setEmptyImageView(2130837710);
      if (!UIStateManager.getInstance().getPrefs().isNautilusEnabled())
        break label65;
      setEmptyScreenText(2131230939);
      setEmptyImageView(2130837737);
    }
    while (true)
    {
      setEmptyScreenPadding(true);
      return;
      i = 0;
      break;
      label65: setEmptyScreenText(2131230940);
      setEmptyImageView(2130837713);
      continue;
      label80: setEmptyImageView(2130837709);
      setEmptyScreenText(2131231371);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.CreateNewStationClusterFragment
 * JD-Core Version:    0.6.2
 */