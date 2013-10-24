package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.DocumentClickHandler;

public abstract class MediaListCardAdapter extends MediaListCursorAdapter
  implements View.OnClickListener
{
  protected MediaListCardAdapter(MusicFragment paramMusicFragment, int paramInt)
  {
    super(paramMusicFragment, paramInt);
  }

  protected MediaListCardAdapter(MusicFragment paramMusicFragment, int paramInt, Cursor paramCursor)
  {
    super(paramMusicFragment, paramInt, paramCursor);
  }

  public void bindView(View paramView, Context paramContext, Cursor paramCursor)
  {
    super.bindView(paramView, paramContext, paramCursor);
    if (!(paramView instanceof PlayCardView))
      return;
    paramView.setOnClickListener(this);
  }

  public void onClick(View paramView)
  {
    if (!(paramView.getTag() instanceof Document))
      return;
    Context localContext = getContext();
    Document localDocument = (Document)paramView.getTag();
    DocumentClickHandler.onDocumentClick(localContext, localDocument);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MediaListCardAdapter
 * JD-Core Version:    0.6.2
 */