package com.google.android.music.ui.cardlib.layout;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import com.google.android.music.AsyncAlbumArtImageView;
import com.google.android.music.medialist.SongList;
import com.google.android.music.store.MusicContent.Artists;
import com.google.android.music.store.MusicContent.Genres;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;

public class PlayCardThumbnail extends FrameLayout
{
  private final int mContentPadding;
  private AsyncAlbumArtImageView mThumbnail;
  private int mThumbnailMaxHeight;
  private int mThumbnailMaxWidth;

  public PlayCardThumbnail(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardThumbnail(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int i = paramContext.getResources().getDimensionPixelSize(2131558476);
    this.mContentPadding = i;
    this.mThumbnailMaxWidth = 0;
    this.mThumbnailMaxHeight = 0;
  }

  public void bind(Document paramDocument)
  {
    Document.Type localType = paramDocument.getType();
    if (localType == null)
      throw new IllegalArgumentException("The art mode must be specified");
    boolean bool = paramDocument.getIsEmulateRadio();
    String str1 = paramDocument.getArtUrl();
    long l1 = paramDocument.getId();
    String str2 = paramDocument.getTitle();
    if (bool)
    {
      this.mThumbnail.setExternalArtRadio(str1);
      return;
    }
    if (str1 != null)
    {
      int[] arrayOfInt1 = 1.$SwitchMap$com$google$android$music$ui$cardlib$model$Document$Type;
      int i = localType.ordinal();
      switch (arrayOfInt1[i])
      {
      default:
        this.mThumbnail.setExternalAlbumArt(str1);
        return;
      case 1:
        this.mThumbnail.setExternalArtRadio(str1);
        return;
      case 2:
        AsyncAlbumArtImageView localAsyncAlbumArtImageView1 = this.mThumbnail;
        Uri localUri1 = Uri.parse(str1);
        localAsyncAlbumArtImageView1.setArtistArt(str2, l1, localUri1, false);
        return;
      case 3:
        this.mThumbnail.setSharedPlaylistArt(str1);
        return;
      case 4:
      }
      this.mThumbnail.setSharedPlaylistArt(str1);
      return;
    }
    if ((paramDocument.isNautilus()) && (paramDocument.getPlaylistType() != 70))
    {
      String str3 = "Nautilus item without art url: " + paramDocument;
      int j = Log.w("PlayCardThumbnail", str3);
      return;
    }
    int[] arrayOfInt2 = 1.$SwitchMap$com$google$android$music$ui$cardlib$model$Document$Type;
    int k = localType.ordinal();
    switch (arrayOfInt2[k])
    {
    default:
      this.mThumbnail.showDefaultArtwork();
      return;
    case 2:
      AsyncAlbumArtImageView localAsyncAlbumArtImageView2 = this.mThumbnail;
      Uri localUri2 = MusicContent.Artists.getAlbumsByArtistsUri(l1);
      localAsyncAlbumArtImageView2.setArtistArt(str2, l1, localUri2, true);
      return;
    case 5:
      AsyncAlbumArtImageView localAsyncAlbumArtImageView3 = this.mThumbnail;
      Uri localUri3 = MusicContent.Genres.getAlbumsOfGenreUri(l1);
      localAsyncAlbumArtImageView3.setGenreArt(str2, l1, localUri3);
      return;
    case 6:
      this.mThumbnail.setAlbumId(l1, null, null);
      return;
    case 3:
      AsyncAlbumArtImageView localAsyncAlbumArtImageView4 = this.mThumbnail;
      int m = paramDocument.getPlaylistType();
      localAsyncAlbumArtImageView4.setPlaylistAlbumArt(l1, str2, m);
      return;
    case 1:
      this.mThumbnail.setExternalArtRadio(null);
      return;
    case 7:
      AsyncAlbumArtImageView localAsyncAlbumArtImageView5 = this.mThumbnail;
      long l2 = paramDocument.getAlbumId();
      localAsyncAlbumArtImageView5.setAlbumId(l2, null, null);
      return;
    case 4:
      this.mThumbnail.setSharedPlaylistArt(null);
      return;
    case 8:
      this.mThumbnail.showImFeelingLuckyArtwork();
      return;
    case 9:
    case 10:
    }
    AsyncAlbumArtImageView localAsyncAlbumArtImageView6 = this.mThumbnail;
    Context localContext = getContext();
    SongList localSongList = paramDocument.getSongList(localContext);
    localAsyncAlbumArtImageView6.setArtForSonglist(localSongList);
  }

  public void clear()
  {
    this.mThumbnail.clearArtwork();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    AsyncAlbumArtImageView localAsyncAlbumArtImageView = (AsyncAlbumArtImageView)findViewById(2131296471);
    this.mThumbnail = localAsyncAlbumArtImageView;
  }

  public void setThumbnailMetrics(int paramInt1, int paramInt2)
  {
    this.mThumbnailMaxWidth = paramInt1;
    this.mThumbnailMaxHeight = paramInt2;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.layout.PlayCardThumbnail
 * JD-Core Version:    0.6.2
 */