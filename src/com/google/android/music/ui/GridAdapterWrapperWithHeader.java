package com.google.android.music.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import com.google.android.music.ui.cardlib.PlayCardClusterView;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardHeap;
import com.google.android.music.ui.cardlib.layout.PlayCardView;
import com.google.android.music.ui.cardlib.model.Document;
import com.google.android.music.ui.cardlib.utils.Lists;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class GridAdapterWrapperWithHeader extends BaseAdapter
{
  private static List<PlayCardClusterMetadata> sClusterList = Lists.newArrayList();
  private final PlayCardHeap mCardHeap;
  private final PlayCardClusterMetadata mClusterConfig;
  private final Context mContext;
  private final LayoutInflater mLayoutInflater;
  private final int mNumColumns;
  private final ListAdapter mWrappedAdapter;

  static
  {
    List localList1 = sClusterList;
    PlayCardClusterMetadata localPlayCardClusterMetadata1 = new PlayCardClusterMetadata(4, 2);
    PlayCardClusterMetadata.CardMetadata localCardMetadata1 = PlayCardClusterMetadata.CARD_MEDIUM;
    PlayCardClusterMetadata localPlayCardClusterMetadata2 = localPlayCardClusterMetadata1.addTile(localCardMetadata1, 0, 0);
    localList1.add(0, localPlayCardClusterMetadata2);
    List localList2 = sClusterList;
    PlayCardClusterMetadata localPlayCardClusterMetadata3 = new PlayCardClusterMetadata(6, 3);
    PlayCardClusterMetadata.CardMetadata localCardMetadata2 = PlayCardClusterMetadata.CARD_MEDIUM_16x9;
    PlayCardClusterMetadata localPlayCardClusterMetadata4 = localPlayCardClusterMetadata3.addTile(localCardMetadata2, 0, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata3 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata5 = localPlayCardClusterMetadata4.addTile(localCardMetadata3, 4, 0);
    localList2.add(1, localPlayCardClusterMetadata5);
    List localList3 = sClusterList;
    PlayCardClusterMetadata localPlayCardClusterMetadata6 = new PlayCardClusterMetadata(6, 6);
    PlayCardClusterMetadata.CardMetadata localCardMetadata4 = PlayCardClusterMetadata.CARD_LARGE;
    PlayCardClusterMetadata localPlayCardClusterMetadata7 = localPlayCardClusterMetadata6.addTile(localCardMetadata4, 0, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata5 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata8 = localPlayCardClusterMetadata7.addTile(localCardMetadata5, 4, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata6 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata9 = localPlayCardClusterMetadata8.addTile(localCardMetadata6, 4, 1);
    localList3.add(2, localPlayCardClusterMetadata9);
    List localList4 = sClusterList;
    PlayCardClusterMetadata localPlayCardClusterMetadata10 = new PlayCardClusterMetadata(10, 6);
    PlayCardClusterMetadata.CardMetadata localCardMetadata7 = PlayCardClusterMetadata.CARD_LARGE;
    PlayCardClusterMetadata localPlayCardClusterMetadata11 = localPlayCardClusterMetadata10.addTile(localCardMetadata7, 0, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata8 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata12 = localPlayCardClusterMetadata11.addTile(localCardMetadata8, 4, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata9 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata13 = localPlayCardClusterMetadata12.addTile(localCardMetadata9, 6, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata10 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata14 = localPlayCardClusterMetadata13.addTile(localCardMetadata10, 8, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata11 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata15 = localPlayCardClusterMetadata14.addTile(localCardMetadata11, 4, 1);
    PlayCardClusterMetadata.CardMetadata localCardMetadata12 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata16 = localPlayCardClusterMetadata15.addTile(localCardMetadata12, 6, 1);
    PlayCardClusterMetadata.CardMetadata localCardMetadata13 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata17 = localPlayCardClusterMetadata16.addTile(localCardMetadata13, 8, 1);
    localList4.add(3, localPlayCardClusterMetadata17);
    List localList5 = sClusterList;
    PlayCardClusterMetadata localPlayCardClusterMetadata18 = new PlayCardClusterMetadata(8, 6);
    PlayCardClusterMetadata.CardMetadata localCardMetadata14 = PlayCardClusterMetadata.CARD_LARGE;
    PlayCardClusterMetadata localPlayCardClusterMetadata19 = localPlayCardClusterMetadata18.addTile(localCardMetadata14, 0, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata15 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata20 = localPlayCardClusterMetadata19.addTile(localCardMetadata15, 4, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata16 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata21 = localPlayCardClusterMetadata20.addTile(localCardMetadata16, 6, 0);
    PlayCardClusterMetadata.CardMetadata localCardMetadata17 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata22 = localPlayCardClusterMetadata21.addTile(localCardMetadata17, 4, 1);
    PlayCardClusterMetadata.CardMetadata localCardMetadata18 = PlayCardClusterMetadata.CARD_SMALL;
    PlayCardClusterMetadata localPlayCardClusterMetadata23 = localPlayCardClusterMetadata22.addTile(localCardMetadata18, 6, 1);
    localList5.add(4, localPlayCardClusterMetadata23);
  }

  public GridAdapterWrapperWithHeader(Context paramContext, ListAdapter paramListAdapter, int paramInt1, int paramInt2)
  {
    this.mContext = paramContext;
    this.mWrappedAdapter = paramListAdapter;
    ListAdapter localListAdapter = this.mWrappedAdapter;
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        GridAdapterWrapperWithHeader.this.notifyDataSetChanged();
      }
    };
    localListAdapter.registerDataSetObserver(local1);
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    this.mLayoutInflater = localLayoutInflater;
    PlayCardHeap localPlayCardHeap = new PlayCardHeap();
    this.mCardHeap = localPlayCardHeap;
    this.mNumColumns = paramInt1;
    PlayCardClusterMetadata localPlayCardClusterMetadata = getClusterMetadata(paramInt1, paramInt2);
    this.mClusterConfig = localPlayCardClusterMetadata;
  }

  private PlayCardClusterMetadata.CardMetadata getCardGridMetadata()
  {
    return PlayCardClusterMetadata.CARD_SMALL;
  }

  private PlayCardClusterMetadata getClusterMetadata(int paramInt1, int paramInt2)
  {
    int i = 1;
    PlayCardClusterMetadata localPlayCardClusterMetadata;
    switch (paramInt1)
    {
    default:
      throw new InvalidParameterException("Unsupported cluster configuration");
    case 2:
      localPlayCardClusterMetadata = (PlayCardClusterMetadata)sClusterList.get(0);
    case 3:
    case 4:
    case 5:
    }
    while (true)
    {
      return localPlayCardClusterMetadata;
      List localList = sClusterList;
      if (paramInt2 == 1);
      while (true)
      {
        localPlayCardClusterMetadata = (PlayCardClusterMetadata)localList.get(i);
        break;
        i = 2;
      }
      localPlayCardClusterMetadata = (PlayCardClusterMetadata)sClusterList.get(4);
      continue;
      localPlayCardClusterMetadata = (PlayCardClusterMetadata)sClusterList.get(3);
    }
  }

  private View getHeaderClusterView(View paramView, ViewGroup paramViewGroup, int paramInt)
  {
    int i = this.mClusterConfig.getTileCount();
    int j;
    if (paramInt <= i)
    {
      j = paramInt;
      if (!(paramView instanceof PlayCardClusterView))
        break label150;
    }
    label150: for (PlayCardClusterView localPlayCardClusterView = (PlayCardClusterView)paramView; ; localPlayCardClusterView = (PlayCardClusterView)this.mLayoutInflater.inflate(2130968649, paramViewGroup, false))
    {
      ArrayList localArrayList = new ArrayList();
      localPlayCardClusterView.hideHeader();
      PlayCardClusterMetadata localPlayCardClusterMetadata = this.mClusterConfig;
      localPlayCardClusterView.setMetadata(localPlayCardClusterMetadata, localArrayList, null);
      if (!localPlayCardClusterView.hasCards())
      {
        PlayCardHeap localPlayCardHeap = this.mCardHeap;
        localPlayCardClusterView.inflateContent(localPlayCardHeap);
      }
      int k = 0;
      while (k < j)
      {
        PlayCardView localPlayCardView = localPlayCardClusterView.getCardChild(k);
        Document localDocument = (Document)this.mWrappedAdapter.getView(k, localPlayCardView, paramViewGroup).getTag();
        boolean bool = localArrayList.add(localDocument);
        k += 1;
      }
      j = i;
      break;
    }
    localPlayCardClusterView.createContent();
    return localPlayCardClusterView;
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public int getCount()
  {
    int i = this.mWrappedAdapter.getCount();
    int j = this.mClusterConfig.getTileCount();
    int k;
    if (i == 0)
      k = 0;
    while (true)
    {
      return k;
      if (i <= j)
      {
        k = 1;
      }
      else
      {
        double d1 = i - j;
        double d2 = this.mNumColumns;
        k = (int)Math.ceil(d1 / d2) + 1;
      }
    }
  }

  public Object getItem(int paramInt)
  {
    return null;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    if (paramInt == 0);
    for (int i = 0; ; i = 1)
      return i;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = this.mWrappedAdapter.getCount();
    Object localObject;
    if (i == 0)
      localObject = null;
    int j;
    int i4;
    do
    {
      int k;
      do
      {
        while (true)
        {
          return localObject;
          if (paramInt != 0)
            break;
          localObject = getHeaderClusterView(paramView, paramViewGroup, i);
        }
        if ((paramView != null) && (!(paramView instanceof PlayCardClusterView)))
          break;
        Context localContext = this.mContext;
        localObject = new LinearLayout(localContext);
        ((LinearLayout)localObject).setOrientation(0);
        j = 0;
        k = this.mNumColumns;
      }
      while (j >= k);
      int m = paramInt + -1;
      int n = this.mNumColumns;
      int i1 = m * n + j;
      int i2 = this.mClusterConfig.getTileCount();
      int i3 = i1 + i2;
      View localView1;
      if (i3 < i)
        localView1 = this.mWrappedAdapter.getView(i3, null, (ViewGroup)localObject);
      while (true)
      {
        if ((localView1 instanceof PlayCardView))
        {
          PlayCardView localPlayCardView = (PlayCardView)localView1;
          float f = getCardGridMetadata().getThumbnailAspectRatio();
          localPlayCardView.setThumbnailAspectRatio(f);
        }
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1, 1.0F);
        localView1.setLayoutParams(localLayoutParams);
        ((LinearLayout)localObject).addView(localView1);
        j += 1;
        break;
        localView1 = this.mWrappedAdapter.getView(0, null, (ViewGroup)localObject);
        localView1.setVisibility(4);
      }
      localObject = (ViewGroup)paramView;
      j = 0;
      i4 = ((ViewGroup)localObject).getChildCount();
    }
    while (j >= i4);
    View localView2 = ((ViewGroup)localObject).getChildAt(j);
    int i5 = paramInt + -1;
    int i6 = this.mNumColumns;
    int i7 = i5 * i6 + j;
    int i8 = this.mClusterConfig.getTileCount();
    int i9 = i7 + i8;
    if (i9 < i)
    {
      View localView3 = this.mWrappedAdapter.getView(i9, localView2, (ViewGroup)localObject);
      localView2.setVisibility(0);
    }
    while (true)
    {
      j += 1;
      break;
      localView2.setVisibility(4);
    }
  }

  public int getViewTypeCount()
  {
    return 2;
  }

  public boolean isEnabled(int paramInt)
  {
    return false;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GridAdapterWrapperWithHeader
 * JD-Core Version:    0.6.2
 */