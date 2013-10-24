package com.google.android.music.ui.cardlib;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.ClusterTileMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardHeap;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.model.DocumentClickHandler;
import java.util.List;

public class PlayCardClusterViewContent extends ViewGroup
  implements View.OnClickListener
{
  private PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private View.OnClickListener mClickListenerOverride;
  private List<Document> mDocs;
  private PlayCardClusterMetadata mMetadata;

  public PlayCardClusterViewContent(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardClusterViewContent(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void clearThumbnails()
  {
    int i = this.mMetadata.getTileCount();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      ((PlayCardView)getChildAt(j)).clearThumbnail();
      j += 1;
    }
  }

  public void createContent()
  {
    int i = this.mMetadata.getTileCount();
    int j = 0;
    if (j >= i)
      return;
    PlayCardView localPlayCardView = (PlayCardView)getChildAt(j);
    Document localDocument;
    if (this.mDocs != null)
      if (this.mDocs != null)
      {
        int k = this.mDocs.size();
        if (j < k)
        {
          localDocument = (Document)this.mDocs.get(j);
          label71: localPlayCardView.setTag(localDocument);
          if (localDocument != null)
            break label99;
          localPlayCardView.bindNoDocument();
        }
      }
    while (true)
    {
      j += 1;
      break;
      localDocument = null;
      break label71;
      label99: PlayCardView.ContextMenuDelegate localContextMenuDelegate = this.mCardsContextMenuDelegate;
      localPlayCardView.bind(localDocument, localContextMenuDelegate);
      if (this.mClickListenerOverride != null);
      for (Object localObject = this.mClickListenerOverride; ; localObject = this)
      {
        localPlayCardView.setOnClickListener((View.OnClickListener)localObject);
        break;
      }
      localPlayCardView.bindFakeData();
    }
  }

  public PlayCardClusterMetadata getMetadata()
  {
    return this.mMetadata;
  }

  public void inflateContent(PlayCardHeap paramPlayCardHeap)
  {
    if (getChildCount() > 0)
      removeAllViews();
    LayoutInflater localLayoutInflater = LayoutInflater.from(getContext());
    int i = 0;
    while (true)
    {
      int j = this.mMetadata.getTileCount();
      if (i >= j)
        return;
      PlayCardClusterMetadata.ClusterTileMetadata localClusterTileMetadata = this.mMetadata.getTileMetadata(i);
      PlayCardClusterMetadata.CardMetadata localCardMetadata = localClusterTileMetadata.getCardMetadata();
      PlayCardView localPlayCardView = paramPlayCardHeap.getCard(localCardMetadata, localLayoutInflater);
      float f = localClusterTileMetadata.getCardMetadata().getThumbnailAspectRatio();
      localPlayCardView.setThumbnailAspectRatio(f);
      addView(localPlayCardView);
      i += 1;
    }
  }

  public void onClick(View paramView)
  {
    if (!(paramView.getTag() instanceof Document))
      return;
    Context localContext = getContext();
    Document localDocument = (Document)paramView.getTag();
    DocumentClickHandler.onDocumentClick(localContext, localDocument);
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getWidth();
    int j = this.mMetadata.getWidth();
    int k = getPaddingLeft();
    int m = i - k;
    int n = getPaddingRight();
    int i1 = (m - n) / j;
    int i2 = getPaddingTop();
    int i3 = getPaddingLeft();
    int i4 = this.mMetadata.getTileCount();
    int i5 = 0;
    while (true)
    {
      if (i5 >= i4)
        return;
      PlayCardClusterMetadata.ClusterTileMetadata localClusterTileMetadata = this.mMetadata.getTileMetadata(i5);
      int i6 = localClusterTileMetadata.getXStart();
      int i7 = localClusterTileMetadata.getYStart();
      View localView = getChildAt(i5);
      int i8 = localView.getMeasuredHeight();
      int i9 = i1 * i6;
      int i10 = i3 + i9;
      int i11 = i8 * i7;
      int i12 = i2 + i11;
      int i13 = localView.getMeasuredWidth() + i10;
      int i14 = localView.getMeasuredHeight() + i12;
      localView.layout(i10, i12, i13, i14);
      i5 += 1;
    }
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    int j = this.mMetadata.getWidth();
    int k = this.mMetadata.getHeight();
    int m = getPaddingLeft();
    int n = i - m;
    int i1 = getPaddingRight();
    int i2 = (n - i1) / j;
    int i3 = 0;
    int i4 = this.mMetadata.getTileCount();
    int i5 = 0;
    while (i5 < i4)
    {
      PlayCardClusterMetadata.ClusterTileMetadata localClusterTileMetadata = this.mMetadata.getTileMetadata(i5);
      int i6 = localClusterTileMetadata.getCardMetadata().getHSpan();
      int i7 = localClusterTileMetadata.getCardMetadata().getVSpan();
      View localView = getChildAt(i5);
      int i8 = View.MeasureSpec.makeMeasureSpec(i2 * i6, 1073741824);
      localView.measure(i8, 0);
      i3 = Math.max(localView.getMeasuredHeight() / i7, i3);
      i5 += 1;
    }
    int i9 = i3 * k;
    setMeasuredDimension(i, i9);
  }

  public void setMetadata(PlayCardClusterMetadata paramPlayCardClusterMetadata, List<Document> paramList, PlayCardView.ContextMenuDelegate paramContextMenuDelegate, View.OnClickListener paramOnClickListener)
  {
    this.mMetadata = paramPlayCardClusterMetadata;
    this.mDocs = paramList;
    this.mCardsContextMenuDelegate = paramContextMenuDelegate;
    this.mClickListenerOverride = paramOnClickListener;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.PlayCardClusterViewContent
 * JD-Core Version:    0.6.2
 */