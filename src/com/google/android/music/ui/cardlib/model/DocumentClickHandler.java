package com.google.android.music.ui.cardlib.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.music.medialist.NautilusSingleSongList;
import com.google.android.music.medialist.SingleSongList;
import com.google.android.music.medialist.SongList;
import com.google.android.music.preferences.MusicPreferences;
import com.google.android.music.ui.AppNavigation;
import com.google.android.music.ui.ArtistPageActivity;
import com.google.android.music.ui.GenreAlbumGridActivity;
import com.google.android.music.ui.GenresExploreActivity;
import com.google.android.music.ui.SubGenresExploreActivity;
import com.google.android.music.ui.TrackContainerActivity;
import com.google.android.music.ui.UIStateManager;
import com.google.android.music.utils.MusicUtils;
import java.security.InvalidParameterException;

public class DocumentClickHandler
  implements View.OnClickListener
{
  private Context mContext;
  private Document mDoc;

  public DocumentClickHandler(Context paramContext, Document paramDocument)
  {
    this.mContext = paramContext;
    this.mDoc = paramDocument;
  }

  public static void onDocumentClick(Context paramContext, final Document paramDocument)
  {
    int i = 1;
    if (paramDocument.getIsEmulateRadio())
    {
      Document.Type localType1 = paramDocument.getType();
      Document.Type localType2 = Document.Type.ALBUM;
      if (localType1 != localType2)
      {
        Document.Type localType3 = paramDocument.getType();
        Document.Type localType4 = Document.Type.ARTIST;
        if (localType3 != localType4)
        {
          Document.Type localType5 = paramDocument.getType();
          Document.Type localType6 = Document.Type.TRACK;
          if (localType5 != localType6)
            break label74;
        }
      }
      MusicUtils.runAsync(new Runnable()
      {
        public void run()
        {
          Context localContext1 = DocumentClickHandler.this;
          Document localDocument = paramDocument;
          Context localContext2 = DocumentClickHandler.this;
          SongList localSongList = localDocument.getSongList(localContext2);
          MusicUtils.playRadio(localContext1, localSongList);
        }
      });
      return;
      label74: Document.Type localType7 = paramDocument.getType();
      Document.Type localType8 = Document.Type.GENRE;
      if (localType7 != localType8)
      {
        Document.Type localType9 = paramDocument.getType();
        Document.Type localType10 = Document.Type.NAUTILUS_GENRE;
        if (localType9 != localType10);
      }
      else
      {
        MusicUtils.runAsync(new Runnable()
        {
          public void run()
          {
            Context localContext = DocumentClickHandler.this;
            String str1 = paramDocument.getTitle();
            String str2 = paramDocument.getGenreId();
            String str3 = paramDocument.getArtUrl();
            MusicUtils.playGenreRadio(localContext, str1, str2, str3);
          }
        });
        return;
      }
      throw new InvalidParameterException("New radio stations can only be created from artist, albums or tracks");
    }
    Document.Type localType11 = paramDocument.getType();
    Document.Type localType12 = Document.Type.ALBUM;
    if (localType11 == localType12)
    {
      if (paramDocument.isNautilus())
      {
        String str1 = paramDocument.getAlbumMetajamId();
        TrackContainerActivity.showNautilusAlbum(paramContext, str1, paramDocument);
        return;
      }
      long l1 = paramDocument.getAlbumId();
      TrackContainerActivity.showAlbum(paramContext, l1, paramDocument, true);
      return;
    }
    Document.Type localType13 = paramDocument.getType();
    Document.Type localType14 = Document.Type.ARTIST;
    if (localType13 == localType14)
    {
      if (paramDocument.isNautilus())
      {
        String str2 = paramDocument.getArtistMetajamId();
        String str3 = paramDocument.getArtistName();
        ArtistPageActivity.showNautilusArtist(paramContext, str2, str3);
        return;
      }
      long l2 = paramDocument.getArtistId();
      String str4 = paramDocument.getArtistName();
      ArtistPageActivity.showArtist(paramContext, l2, str4, true);
      return;
    }
    Document.Type localType15 = paramDocument.getType();
    Document.Type localType16 = Document.Type.PLAYLIST;
    if (localType15 == localType16)
    {
      if ((paramDocument.getPlaylistType() == 50) || (paramDocument.getPlaylistType() == 60))
      {
        MusicUtils.playMediaList(paramDocument.getSongList(paramContext));
        AppNavigation.openNowPlayingDrawer(paramContext);
        return;
      }
      SongList localSongList1 = paramDocument.getSongList(paramContext);
      TrackContainerActivity.showPlaylist(paramContext, localSongList1, paramDocument);
      return;
    }
    Document.Type localType17 = paramDocument.getType();
    Document.Type localType18 = Document.Type.TRACK;
    if (localType17 == localType18)
    {
      if (paramDocument.isNautilus())
      {
        String str5 = paramDocument.getTrackMetajamId();
        String str6 = paramDocument.getTitle();
        MusicUtils.playMediaList(new NautilusSingleSongList(str5, str6), -1);
        return;
      }
      long l3 = paramDocument.getId();
      String str7 = paramDocument.getTitle();
      MusicUtils.playMediaList(new SingleSongList(l3, str7), -1);
      return;
    }
    Document.Type localType19 = paramDocument.getType();
    Document.Type localType20 = Document.Type.GENRE;
    if (localType19 == localType20)
    {
      long l4 = paramDocument.getId();
      GenreAlbumGridActivity.showAlbumsOfGenre(paramContext, l4, true);
      return;
    }
    Document.Type localType21 = paramDocument.getType();
    Document.Type localType22 = Document.Type.RADIO;
    if (localType21 == localType22)
    {
      long l5 = paramDocument.getId();
      String str8 = paramDocument.getTitle();
      MusicUtils.playRadio(paramContext, l5, str8);
      return;
    }
    Document.Type localType23 = paramDocument.getType();
    Document.Type localType24 = Document.Type.NAUTILUS_GENRE;
    if (localType23 == localType24)
    {
      String str9;
      String str10;
      int j;
      if (paramDocument.isTopLevelGenre())
      {
        str9 = paramDocument.getGenreId();
        str10 = paramDocument.getTitle();
        j = paramDocument.getSubgenreCount();
      }
      String str11;
      String str12;
      String str13;
      for (Intent localIntent1 = GenresExploreActivity.buildStartIntent(paramContext, str9, str10, j, 1); ; localIntent1 = SubGenresExploreActivity.buildStartIntent(paramContext, str11, str12, str13))
      {
        paramContext.startActivity(localIntent1);
        return;
        str11 = paramDocument.getGenreId();
        str12 = paramDocument.getTitle();
        str13 = paramDocument.getParentGenreId();
      }
    }
    Document.Type localType25 = paramDocument.getType();
    Document.Type localType26 = Document.Type.IM_FEELING_LUCKY;
    if (localType25 == localType26)
    {
      UIStateManager localUIStateManager = UIStateManager.getInstance();
      boolean bool1 = localUIStateManager.isDisplayingLocalContent();
      boolean bool2 = localUIStateManager.isStreamingEnabled();
      if ((localUIStateManager.getPrefs().hasStreamingAccount()) && (bool2) && (!bool1));
      while (i != 0)
      {
        MusicUtils.playImFeelingLuckyRadio(paramContext);
        return;
        i = 0;
      }
      MusicUtils.shuffleOnDevice();
      return;
    }
    Document.Type localType27 = paramDocument.getType();
    Document.Type localType28 = Document.Type.ALL_SONGS_ARTIST;
    if (localType27 != localType28)
    {
      Document.Type localType29 = paramDocument.getType();
      Document.Type localType30 = Document.Type.ALL_SONGS_GENRE;
      if (localType29 != localType30);
    }
    else
    {
      SongList localSongList2 = paramDocument.getSongList(paramContext);
      if (localSongList2 == null)
        return;
      Intent localIntent2 = TrackContainerActivity.buildStartIntent(paramContext, localSongList2, null);
      paramContext.startActivity(localIntent2);
      return;
    }
    throw new InvalidParameterException();
  }

  public void onClick(View paramView)
  {
    Context localContext = this.mContext;
    Document localDocument = this.mDoc;
    onDocumentClick(localContext, localDocument);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.model.DocumentClickHandler
 * JD-Core Version:    0.6.2
 */