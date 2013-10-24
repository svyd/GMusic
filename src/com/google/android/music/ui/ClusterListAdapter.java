package com.google.android.music.ui;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.google.android.music.ui.cardlib.PlayCardClusterView;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardClusterMetadata.CardMetadata;
import com.google.android.music.ui.cardlib.layout.PlayCardHeap;
import com.google.android.music.ui.cardlib.layout.PlayCardView.ContextMenuDelegate;
import com.google.android.music.ui.cardlib.model.MoreClusterClickHandler;
import java.util.HashMap;
import java.util.List;

public class ClusterListAdapter extends BaseAdapter
{
  private final PlayCardHeap mCardHeap;
  private final PlayCardView.ContextMenuDelegate mCardsContextMenuDelegate;
  private final ClusterBuilder mClusterBuilder;
  private final HashMap<Integer, Cluster> mClusterInfoMap;
  private final Context mContext;
  private final int mItemTypeCount;
  private final LayoutInflater mLayoutInflater;

  public ClusterListAdapter(Context paramContext, ClusterBuilder paramClusterBuilder, PlayCardView.ContextMenuDelegate paramContextMenuDelegate, int paramInt)
  {
    this.mClusterBuilder = paramClusterBuilder;
    this.mContext = paramContext;
    LayoutInflater localLayoutInflater = LayoutInflater.from(paramContext);
    this.mLayoutInflater = localLayoutInflater;
    PlayCardHeap localPlayCardHeap = new PlayCardHeap();
    this.mCardHeap = localPlayCardHeap;
    HashMap localHashMap = new HashMap();
    this.mClusterInfoMap = localHashMap;
    this.mCardsContextMenuDelegate = paramContextMenuDelegate;
    this.mItemTypeCount = paramInt;
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public int getCount()
  {
    return this.mItemTypeCount;
  }

  public Object getItem(int paramInt)
  {
    HashMap localHashMap = this.mClusterInfoMap;
    Integer localInteger = Integer.valueOf(paramInt);
    return localHashMap.get(localInteger);
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public int getItemViewType(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    HashMap localHashMap = this.mClusterInfoMap;
    Integer localInteger1 = Integer.valueOf(paramInt);
    Cluster localCluster = (Cluster)localHashMap.get(localInteger1);
    Object localObject1;
    if (localCluster == null)
    {
      Context localContext = this.mContext;
      localObject1 = new View(localContext);
      return localObject1;
    }
    if ((paramView != null) && ((paramView instanceof PlayCardClusterView)));
    for (int i = 1; ; i = 0)
    {
      if ((i == 0) || (paramView.getTag() != localCluster))
        break label89;
      localObject1 = paramView;
      break;
    }
    label89: LayoutInflater localLayoutInflater = this.mLayoutInflater;
    int j = 2130968649;
    ViewGroup localViewGroup = paramViewGroup;
    boolean bool = false;
    PlayCardClusterView localPlayCardClusterView = (PlayCardClusterView)localLayoutInflater.inflate(j, localViewGroup, bool);
    int k = localCluster.getNbColumns();
    List localList1 = localCluster.getVisibleContent();
    PlayCardClusterMetadata.CardMetadata localCardMetadata = localCluster.getCardType();
    int m = (localList1.size() + k + -1) / k;
    int n = localCardMetadata.getHSpan() * k;
    int i1 = localCardMetadata.getVSpan() * m;
    int i2 = n;
    int i3 = i1;
    PlayCardClusterMetadata localPlayCardClusterMetadata1 = new PlayCardClusterMetadata(i2, i3);
    int i4 = 0;
    while (true)
    {
      int i5 = localList1.size();
      if (i4 >= i5)
        break;
      int i6 = i4 / k;
      int i7 = localCardMetadata.getVSpan();
      int i8 = i6 * i7;
      int i9 = i4 % k;
      int i10 = localCardMetadata.getHSpan();
      int i11 = i9 * i10;
      int i12 = i8;
      PlayCardClusterMetadata localPlayCardClusterMetadata2 = localPlayCardClusterMetadata1.addTile(localCardMetadata, i11, i12);
      i4 += 1;
    }
    List localList2 = localCluster.getVisibleContent();
    PlayCardView.ContextMenuDelegate localContextMenuDelegate1 = this.mCardsContextMenuDelegate;
    View.OnClickListener localOnClickListener1 = localCluster.getItemOnClickListener();
    List localList3 = localList2;
    PlayCardView.ContextMenuDelegate localContextMenuDelegate2 = localContextMenuDelegate1;
    View.OnClickListener localOnClickListener2 = localOnClickListener1;
    localPlayCardClusterView.setMetadata(localPlayCardClusterMetadata1, localList3, localContextMenuDelegate2, localOnClickListener2);
    if (!localPlayCardClusterView.hasCards())
    {
      PlayCardHeap localPlayCardHeap = this.mCardHeap;
      localPlayCardClusterView.inflateContent(localPlayCardHeap);
    }
    localPlayCardClusterView.createContent();
    Object localObject2 = null;
    int i13 = localCluster.getFullContent().size();
    int i14 = localPlayCardClusterMetadata1.getTileCount();
    int i15 = i13 - i14;
    Resources localResources;
    Object[] arrayOfObject;
    if (i15 > 0)
    {
      localResources = this.mContext.getResources();
      arrayOfObject = new Object[1];
      Integer localInteger2 = Integer.valueOf(i15);
      arrayOfObject[0] = localInteger2;
    }
    for (String str1 = localResources.getString(2131231303, arrayOfObject); ; str1 = null)
    {
      String str2 = localCluster.getTitle();
      Object localObject3 = localObject2;
      localPlayCardClusterView.showHeader(str2, localObject3, str1);
      if (str1 != null)
      {
        MoreClusterClickHandler localMoreClusterClickHandler = localCluster.getMoreOnClickListener();
        localPlayCardClusterView.setMoreClickHandler(localMoreClusterClickHandler);
      }
      localPlayCardClusterView.setTag(localCluster);
      localObject1 = localPlayCardClusterView;
      break;
    }
  }

  public int getViewTypeCount()
  {
    int i = 1;
    if (this.mItemTypeCount < i);
    while (true)
    {
      return i;
      i = this.mItemTypeCount;
    }
  }

  public boolean isEnabled(int paramInt)
  {
    return false;
  }

  public void nullCursor(Loader<Cursor> paramLoader)
  {
    HashMap localHashMap = this.mClusterInfoMap;
    Integer localInteger = Integer.valueOf(paramLoader.getId());
    Object localObject = localHashMap.remove(localInteger);
    notifyDataSetChanged();
  }

  public void swapCursor(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    int i = paramLoader.getId();
    if ((paramCursor != null) && (paramCursor.getCount() > 0))
    {
      HashMap localHashMap1 = this.mClusterInfoMap;
      Integer localInteger1 = Integer.valueOf(i);
      Cluster localCluster = this.mClusterBuilder.buildCluster(i, paramCursor);
      Object localObject1 = localHashMap1.put(localInteger1, localCluster);
    }
    while (true)
    {
      notifyDataSetChanged();
      return;
      HashMap localHashMap2 = this.mClusterInfoMap;
      Integer localInteger2 = Integer.valueOf(i);
      Object localObject2 = localHashMap2.remove(localInteger2);
    }
  }

  static abstract interface ClusterBuilder
  {
    public abstract Cluster buildCluster(int paramInt, Cursor paramCursor);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ClusterListAdapter
 * JD-Core Version:    0.6.2
 */