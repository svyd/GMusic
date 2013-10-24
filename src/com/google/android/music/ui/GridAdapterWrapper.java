package com.google.android.music.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;

public class GridAdapterWrapper extends BaseAdapter
{
  private final Context mContext;
  private final int mNumColumns;
  private final ListAdapter mWrappedAdapter;

  public GridAdapterWrapper(Context paramContext, ListAdapter paramListAdapter, int paramInt)
  {
    this.mContext = paramContext;
    this.mWrappedAdapter = paramListAdapter;
    ListAdapter localListAdapter = this.mWrappedAdapter;
    DataSetObserver local1 = new DataSetObserver()
    {
      public void onChanged()
      {
        GridAdapterWrapper.this.notifyDataSetChanged();
      }
    };
    localListAdapter.registerDataSetObserver(local1);
    if (paramInt <= 0)
      paramInt = 1;
    this.mNumColumns = paramInt;
  }

  private LinearLayout.LayoutParams getCardLayoutParams()
  {
    return new LinearLayout.LayoutParams(0, -1, 1.0F);
  }

  private View getDummyView()
  {
    Context localContext = this.mContext;
    DummyView localDummyView = new DummyView(localContext);
    localDummyView.setVisibility(4);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, -1, 1.0F);
    localDummyView.setLayoutParams(localLayoutParams);
    return localDummyView;
  }

  public boolean areAllItemsEnabled()
  {
    return false;
  }

  public int getCount()
  {
    double d1 = this.mWrappedAdapter.getCount();
    double d2 = this.mNumColumns;
    return (int)Math.ceil(d1 / d2);
  }

  public Object getItem(int paramInt)
  {
    return null;
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = this.mWrappedAdapter.getCount();
    Object localObject;
    if (i == 0)
      localObject = null;
    int j;
    int n;
    do
    {
      int k;
      do
      {
        return localObject;
        if (paramView != null)
          break;
        Context localContext = this.mContext;
        localObject = new LinearLayout(localContext);
        ((LinearLayout)localObject).setOrientation(0);
        j = 0;
        k = this.mNumColumns;
      }
      while (j >= k);
      int m = this.mNumColumns * paramInt + j;
      View localView1;
      if (m < i)
      {
        localView1 = this.mWrappedAdapter.getView(m, null, (ViewGroup)localObject);
        LinearLayout.LayoutParams localLayoutParams1 = getCardLayoutParams();
        localView1.setLayoutParams(localLayoutParams1);
      }
      while (true)
      {
        ((LinearLayout)localObject).addView(localView1);
        j += 1;
        break;
        localView1 = getDummyView();
      }
      localObject = (ViewGroup)paramView;
      j = 0;
      n = ((ViewGroup)localObject).getChildCount();
    }
    while (j >= n);
    View localView2 = ((ViewGroup)localObject).getChildAt(j);
    int i1 = this.mNumColumns * paramInt + j;
    if (i1 < i)
      if ((localView2 instanceof DummyView))
      {
        ((ViewGroup)localObject).removeViewAt(j);
        View localView3 = this.mWrappedAdapter.getView(i1, null, (ViewGroup)localObject);
        LinearLayout.LayoutParams localLayoutParams2 = getCardLayoutParams();
        localView3.setLayoutParams(localLayoutParams2);
        ((ViewGroup)localObject).addView(localView3, j);
      }
    while (true)
    {
      j += 1;
      break;
      View localView4 = this.mWrappedAdapter.getView(i1, localView2, (ViewGroup)localObject);
      localView2.setVisibility(0);
      continue;
      if (!(localView2 instanceof DummyView))
      {
        ((ViewGroup)localObject).removeViewAt(j);
        View localView5 = getDummyView();
        ((ViewGroup)localObject).addView(localView5);
      }
    }
  }

  public boolean isEnabled(int paramInt)
  {
    return false;
  }

  private static class DummyView extends FrameLayout
  {
    public DummyView(Context paramContext)
    {
      super();
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.GridAdapterWrapper
 * JD-Core Version:    0.6.2
 */