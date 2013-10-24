package com.google.android.music.ui.cardlib;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardHeap;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.Document;
import java.util.List;

public class PlayCardClusterView extends ViewGroup
{
  private PlayCardClusterViewContent mContent;
  private PlayCardClusterViewHeader mHeader;
  private final int mHeaderContentOverlap;

  public PlayCardClusterView(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardClusterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int i = paramContext.getResources().getDimensionPixelSize(2131558481);
    this.mHeaderContentOverlap = i;
  }

  public void clearThumbnails()
  {
    this.mContent.clearThumbnails();
  }

  public void createContent()
  {
    this.mContent.createContent();
  }

  public PlayCardView getCardChild(int paramInt)
  {
    return (PlayCardView)this.mContent.getChildAt(paramInt);
  }

  public boolean hasCards()
  {
    int i = this.mContent.getChildCount();
    if ((i > 0) && (this.mContent.getMetadata().getTileCount() != i));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void hideHeader()
  {
    this.mHeader.setVisibility(8);
  }

  public void inflateContent(PlayCardHeap paramPlayCardHeap)
  {
    this.mContent.inflateContent(paramPlayCardHeap);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    PlayCardClusterViewContent localPlayCardClusterViewContent = (PlayCardClusterViewContent)findViewById(2131296464);
    this.mContent = localPlayCardClusterViewContent;
    PlayCardClusterViewHeader localPlayCardClusterViewHeader = (PlayCardClusterViewHeader)findViewById(2131296465);
    this.mHeader = localPlayCardClusterViewHeader;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int i = getWidth();
    int j = getPaddingTop();
    int k = j;
    if (this.mHeader.getVisibility() != 8)
    {
      int m = this.mHeader.getMeasuredHeight();
      PlayCardClusterViewHeader localPlayCardClusterViewHeader = this.mHeader;
      int n = j + m;
      localPlayCardClusterViewHeader.layout(0, j, i, n);
      int i1 = this.mHeaderContentOverlap;
      int i2 = m - i1;
      k += i2;
    }
    PlayCardClusterViewContent localPlayCardClusterViewContent = this.mContent;
    int i3 = getHeight();
    int i4 = getPaddingBottom();
    int i5 = i3 - i4;
    localPlayCardClusterViewContent.layout(0, k, i, i5);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getSize(paramInt1);
    this.mContent.measure(paramInt1, paramInt2);
    int j = this.mContent.getMeasuredHeight();
    int k = getPaddingTop();
    int m = j + k;
    int n = getPaddingBottom();
    int i1 = m + n;
    if (this.mHeader.getVisibility() != 8)
    {
      this.mHeader.measure(paramInt1, 0);
      int i2 = this.mHeader.getMeasuredHeight();
      int i3 = this.mHeaderContentOverlap;
      int i4 = i2 - i3;
      i1 += i4;
    }
    setMeasuredDimension(i, i1);
  }

  public void setMetadata(PlayCardClusterMetadata paramPlayCardClusterMetadata, List<Document> paramList, PlayCardView.ContextMenuDelegate paramContextMenuDelegate)
  {
    this.mContent.setMetadata(paramPlayCardClusterMetadata, paramList, paramContextMenuDelegate, null);
  }

  public void setMetadata(PlayCardClusterMetadata paramPlayCardClusterMetadata, List<Document> paramList, PlayCardView.ContextMenuDelegate paramContextMenuDelegate, View.OnClickListener paramOnClickListener)
  {
    this.mContent.setMetadata(paramPlayCardClusterMetadata, paramList, paramContextMenuDelegate, paramOnClickListener);
  }

  public void setMoreClickHandler(View.OnClickListener paramOnClickListener)
  {
    this.mHeader.setMoreButtonClickHandler(paramOnClickListener);
  }

  public void showHeader(String paramString1, String paramString2, String paramString3)
  {
    this.mHeader.setContent(paramString1, paramString2, paramString3);
    this.mHeader.setVisibility(0);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.PlayCardClusterView
 * JD-Core Version:    0.6.2
 */