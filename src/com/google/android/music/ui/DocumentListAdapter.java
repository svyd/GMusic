package com.google.android.music.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.Document.Type;
import com.google.android.music.ui.cardlib.model.DocumentClickHandler;
import java.util.List;

public class DocumentListAdapter extends BaseAdapter
  implements View.OnClickListener
{
  private static PlayCardClusterMetadata.CardMetadata TILE_METADATA = PlayCardClusterMetadata.CARD_SMALL;
  private static PlayCardClusterMetadata.CardMetadata TILE_METADATA_ROW = PlayCardClusterMetadata.CARD_ROW;
  private static PlayCardClusterMetadata.CardMetadata TILE_METADATA_SMALL = PlayCardClusterMetadata.CARD_SMALL_2x1;
  private final PlayCardClusterMetadata.CardMetadata mCardMetadata;
  private final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private final View.OnClickListener mClickListener;
  private final Context mContext;
  private final List<Document> mDocList;

  public DocumentListAdapter(Context paramContext, List<Document> paramList, Document.Type paramType, PlayCardView.ContextMenuDelegate paramContextMenuDelegate, boolean paramBoolean)
  {
  }

  public DocumentListAdapter(Context paramContext, List<Document> paramList, Document.Type paramType, PlayCardView.ContextMenuDelegate paramContextMenuDelegate, boolean paramBoolean, View.OnClickListener paramOnClickListener)
  {
    this.mContext = paramContext;
    this.mDocList = paramList;
    this.mClickListener = paramOnClickListener;
    this.mCardsContextMenuDelegate = paramContextMenuDelegate;
    if (paramBoolean)
    {
      PlayCardClusterMetadata.CardMetadata localCardMetadata1 = TILE_METADATA;
      this.mCardMetadata = localCardMetadata1;
      return;
    }
    int[] arrayOfInt = 1.$SwitchMap$com$google$android$music$ui$cardlib$model$Document$Type;
    int i = paramType.ordinal();
    switch (arrayOfInt[i])
    {
    default:
      PlayCardClusterMetadata.CardMetadata localCardMetadata2 = TILE_METADATA;
      this.mCardMetadata = localCardMetadata2;
      return;
    case 1:
      PlayCardClusterMetadata.CardMetadata localCardMetadata3 = TILE_METADATA_SMALL;
      this.mCardMetadata = localCardMetadata3;
      return;
    case 2:
    }
    PlayCardClusterMetadata.CardMetadata localCardMetadata4 = TILE_METADATA_ROW;
    this.mCardMetadata = localCardMetadata4;
  }

  public int getCount()
  {
    return this.mDocList.size();
  }

  public Object getItem(int paramInt)
  {
    return this.mDocList.get(paramInt);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramView instanceof PlayCardView));
    LayoutInflater localLayoutInflater;
    int i;
    for (PlayCardView localPlayCardView = (PlayCardView)paramView; ; localPlayCardView = (PlayCardView)localLayoutInflater.inflate(i, paramViewGroup, false))
    {
      float f = this.mCardMetadata.getThumbnailAspectRatio();
      localPlayCardView.setThumbnailAspectRatio(f);
      Document localDocument = (Document)this.mDocList.get(paramInt);
      PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      if (this.mClickListener != null)
        this = this.mClickListener;
      localPlayCardView.setOnClickListener(this);
      localPlayCardView.setTag(localDocument);
      return localPlayCardView;
      localLayoutInflater = (LayoutInflater)this.mContext.getSystemService("layout_inflater");
      i = this.mCardMetadata.getLayoutId();
    }
  }

  public void onClick(View paramView)
  {
    if (!(paramView.getTag() instanceof Document))
      return;
    Context localContext = this.mContext;
    Document localDocument = (Document)paramView.getTag();
    DocumentClickHandler.onDocumentClick(localContext, localDocument);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.DocumentListAdapter
 * JD-Core Version:    0.6.2
 */