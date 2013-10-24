package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.utils.MusicUtils;

public class AlbumsAdapter extends FakeNthCardMediaListCardAdapter
{
  public static final String[] PROJECTION = arrayOfString;
  private static String TAG = "AlbumsAdapter";
  static final PlayCardClusterMetadata.CardMetadata sTileMetadata = PlayCardClusterMetadata.CARD_SMALL;
  protected PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;

  static
  {
    String[] arrayOfString = new String[8];
    arrayOfString[0] = "_id";
    arrayOfString[1] = "album_name";
    arrayOfString[2] = "album_artist";
    arrayOfString[3] = "album_artist_id";
    arrayOfString[4] = "hasLocal";
    arrayOfString[5] = "artworkUrl";
    arrayOfString[6] = "StoreAlbumId";
    arrayOfString[7] = "ArtistMetajamId";
  }

  protected AlbumsAdapter(AlbumGridFragment paramAlbumGridFragment, Cursor paramCursor, PlayCardView.ContextMenuDelegate paramContextMenuDelegate)
  {
  }

  protected AlbumsAdapter(MusicFragment paramMusicFragment, PlayCardView.ContextMenuDelegate paramContextMenuDelegate)
  {
    super(paramMusicFragment, i, j, 0);
    this.mCardsContextMenuDelegate = paramContextMenuDelegate;
    init();
  }

  protected void bindViewToLoadingItem(View paramView, Context paramContext)
  {
    if (!(paramView instanceof PlayCardView))
      return;
    ((PlayCardView)paramView).bindLoading();
  }

  protected void bindViewToMediaListItem(View paramView, Context paramContext, Cursor paramCursor, long paramLong)
  {
    Document localDocument = MusicUtils.getDocument(paramView);
    long l1 = paramCursor.getLong(0);
    localDocument.setId(l1);
    localDocument.setAlbumId(l1);
    String str1 = paramCursor.getString(1);
    String str2 = paramCursor.getString(2);
    long l2 = paramCursor.getLong(3);
    String str3;
    String str4;
    label83: String str5;
    if (paramCursor.isNull(5))
    {
      str3 = null;
      if (!paramCursor.isNull(6))
        break label227;
      str4 = null;
      if (!paramCursor.isNull(7))
        break label240;
      str5 = null;
      label97: if (paramCursor.getInt(4) == 0)
        break label253;
    }
    label227: label240: label253: for (boolean bool = true; ; bool = false)
    {
      localDocument.setTitle(str1);
      localDocument.setSubTitle(str2);
      localDocument.setAlbumName(str1);
      localDocument.setArtistId(l2);
      localDocument.setArtistName(str2);
      localDocument.setArtUrl(str3);
      Document.Type localType = Document.Type.ALBUM;
      localDocument.setType(localType);
      localDocument.setAlbumMetajamId(str4);
      localDocument.setArtistMetajamId(str5);
      localDocument.setHasLocal(bool);
      if (!(paramView instanceof PlayCardView))
        return;
      PlayCardView localPlayCardView = (PlayCardView)paramView;
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      return;
      str3 = paramCursor.getString(5);
      break;
      str4 = paramCursor.getString(6);
      break label83;
      str5 = paramCursor.getString(7);
      break label97;
    }
  }

  public View getFakeView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = Log.wtf(TAG, "Subclass must override this method to support fake view");
    Context localContext = paramView.getContext();
    return new FrameLayout(localContext);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = super.getView(paramInt, paramView, paramViewGroup);
    if ((localView instanceof PlayCardView))
    {
      PlayCardView localPlayCardView = (PlayCardView)localView;
      float f = sTileMetadata.getThumbnailAspectRatio();
      localPlayCardView.setThumbnailAspectRatio(f);
    }
    return localView;
  }

  protected void init()
  {
    setDisableFake(true);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.AlbumsAdapter
 * JD-Core Version:    0.6.2
 */