package com.google.android.music.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.android.music.log.Log;
import com.google.android.music.utils.ViewUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MultiAdaptersListAdapter extends BaseAdapter
{
  private final int mHeadersLayoutId;
  private final Map<Integer, Integer> mLayoutsToType;
  private boolean mLayoutsToTypeInvalid;
  private final PositionInAdapter mPosHelper;
  private final ArrayList<HeaderAdapter> mSections;

  public MultiAdaptersListAdapter(int paramInt)
  {
    PositionInAdapter localPositionInAdapter = new PositionInAdapter(null);
    this.mPosHelper = localPositionInAdapter;
    this.mHeadersLayoutId = 2130968601;
    HashMap localHashMap = Maps.newHashMap();
    this.mLayoutsToType = localHashMap;
    this.mLayoutsToTypeInvalid = true;
    ArrayList localArrayList = new ArrayList(paramInt);
    this.mSections = localArrayList;
  }

  private Map<Integer, Integer> getLayoutsToType()
  {
    if (this.mLayoutsToTypeInvalid)
    {
      HashSet localHashSet = Sets.newHashSet();
      Iterator localIterator1 = this.mSections.iterator();
      while (localIterator1.hasNext())
        ((HeaderAdapter)localIterator1.next()).addLayoutIds(localHashSet);
      Integer localInteger1 = Integer.valueOf(2130968601);
      boolean bool = localHashSet.add(localInteger1);
      this.mLayoutsToType.clear();
      int i = 0;
      Iterator localIterator2 = localHashSet.iterator();
      while (localIterator2.hasNext())
      {
        Integer localInteger2 = (Integer)localIterator2.next();
        Map localMap = this.mLayoutsToType;
        Integer localInteger3 = Integer.valueOf(i);
        Object localObject = localMap.put(localInteger2, localInteger3);
        i += 1;
      }
      this.mLayoutsToTypeInvalid = false;
    }
    return this.mLayoutsToType;
  }

  private void getPositionInAdapter(int paramInt, PositionInAdapter paramPositionInAdapter)
  {
    paramPositionInAdapter.adapter = null;
    paramPositionInAdapter.position = -1;
    Iterator localIterator = this.mSections.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      HeaderAdapter localHeaderAdapter = (HeaderAdapter)localIterator.next();
      int i = localHeaderAdapter.getCount();
      if (i != 0)
      {
        if (localHeaderAdapter.getHeader() != null)
          paramInt += -1;
        if (paramInt < i)
        {
          paramPositionInAdapter.adapter = localHeaderAdapter;
          paramPositionInAdapter.position = paramInt;
          return;
        }
        paramInt -= i;
      }
    }
  }

  public void addSection(HeaderAdapter paramHeaderAdapter)
  {
    boolean bool = this.mSections.add(paramHeaderAdapter);
    this.mLayoutsToTypeInvalid = true;
  }

  public boolean areAllItemsEnabled()
  {
    return true;
  }

  public int getCount()
  {
    int i = 0;
    Iterator localIterator = this.mSections.iterator();
    while (localIterator.hasNext())
    {
      HeaderAdapter localHeaderAdapter = (HeaderAdapter)localIterator.next();
      int j = localHeaderAdapter.getCount();
      if (j != 0)
      {
        i += j;
        if (localHeaderAdapter.getHeader() != null)
          i += 1;
      }
    }
    return i;
  }

  public Object getItem(int paramInt)
  {
    PositionInAdapter localPositionInAdapter = this.mPosHelper;
    getPositionInAdapter(paramInt, localPositionInAdapter);
    Object localObject;
    if (this.mPosHelper.adapter != null)
      if (this.mPosHelper.position >= 0)
      {
        HeaderAdapter localHeaderAdapter = this.mPosHelper.adapter;
        int i = this.mPosHelper.position;
        localObject = localHeaderAdapter.getItem(i);
      }
    while (true)
    {
      return localObject;
      localObject = this.mPosHelper.adapter.getHeader();
      continue;
      localObject = null;
    }
  }

  public long getItemId(int paramInt)
  {
    return paramInt;
  }

  protected int getItemLayoutId(int paramInt)
  {
    PositionInAdapter localPositionInAdapter = this.mPosHelper;
    getPositionInAdapter(paramInt, localPositionInAdapter);
    HeaderAdapter localHeaderAdapter;
    int i;
    if ((this.mPosHelper.adapter != null) && (this.mPosHelper.position >= 0))
    {
      localHeaderAdapter = this.mPosHelper.adapter;
      i = this.mPosHelper.position;
    }
    for (int j = localHeaderAdapter.getItemLayoutId(i); ; j = 2130968601)
      return j;
  }

  public int getItemViewType(int paramInt)
  {
    int i = getItemLayoutId(paramInt);
    Map localMap = getLayoutsToType();
    Integer localInteger1 = Integer.valueOf(i);
    Integer localInteger2 = (Integer)localMap.get(localInteger1);
    if (localInteger2 != null);
    for (int j = localInteger2.intValue(); ; j = -1)
    {
      return j;
      String str = "Unexpected layoutId: " + i;
      Log.e("MultiAdaptersListAdapter", str);
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    PositionInAdapter localPositionInAdapter = this.mPosHelper;
    getPositionInAdapter(paramInt, localPositionInAdapter);
    Object localObject;
    if (this.mPosHelper.adapter != null)
      if (this.mPosHelper.position >= 0)
      {
        HeaderAdapter localHeaderAdapter = this.mPosHelper.adapter;
        int i = this.mPosHelper.position;
        localObject = localHeaderAdapter.getView(i, paramView, paramViewGroup);
      }
    while (true)
    {
      return localObject;
      localObject = (TextView)ViewUtils.createOrReuseView(paramView, paramViewGroup, 2130968601);
      String str = this.mPosHelper.adapter.getHeader();
      ((TextView)localObject).setText(str);
      continue;
      localObject = null;
    }
  }

  public int getViewTypeCount()
  {
    return getLayoutsToType().size();
  }

  public boolean hasAllAdapters()
  {
    Iterator localIterator = this.mSections.iterator();
    do
      if (!localIterator.hasNext())
        break;
    while ((HeaderAdapter)localIterator.next() != null);
    for (boolean bool = false; ; bool = true)
      return bool;
  }

  public boolean isEnabled(int paramInt)
  {
    if (getItemLayoutId(paramInt) != 2130968601);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public void setSection(int paramInt, HeaderAdapter paramHeaderAdapter)
  {
    Object localObject = this.mSections.set(paramInt, paramHeaderAdapter);
    this.mLayoutsToTypeInvalid = true;
  }

  private static class PositionInAdapter
  {
    MultiAdaptersListAdapter.HeaderAdapter adapter;
    int position;
  }

  public static abstract interface HeaderAdapter extends Adapter
  {
    public abstract void addLayoutIds(Set<Integer> paramSet);

    public abstract String getHeader();

    public abstract int getItemLayoutId(int paramInt);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.MultiAdaptersListAdapter
 * JD-Core Version:    0.6.2
 */