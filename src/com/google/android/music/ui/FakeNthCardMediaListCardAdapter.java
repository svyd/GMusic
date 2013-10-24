package com.google.android.music.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class FakeNthCardMediaListCardAdapter extends MediaListCardAdapter
{
  private boolean mDisableFake = false;
  private final int mFakeItemIndex;
  private final int mFakeItemLayout;
  private final int mLayoutId;
  private LayoutInflater mLayoutInflater;
  private boolean mShowFakeEvenIfEmpty = false;

  protected FakeNthCardMediaListCardAdapter(MusicFragment paramMusicFragment, int paramInt1, int paramInt2, int paramInt3)
  {
    super(paramMusicFragment, paramInt1);
    this.mFakeItemLayout = paramInt2;
    this.mFakeItemIndex = paramInt3;
    this.mLayoutId = paramInt1;
    LayoutInflater localLayoutInflater = paramMusicFragment.getFragment().getLayoutInflater(null);
    this.mLayoutInflater = localLayoutInflater;
  }

  protected FakeNthCardMediaListCardAdapter(MusicFragment paramMusicFragment, int paramInt1, int paramInt2, int paramInt3, Cursor paramCursor)
  {
    super(paramMusicFragment, paramInt1, paramCursor);
    this.mFakeItemLayout = paramInt2;
    this.mFakeItemIndex = paramInt3;
    this.mLayoutId = paramInt1;
    LayoutInflater localLayoutInflater = paramMusicFragment.getFragment().getLayoutInflater(null);
    this.mLayoutInflater = localLayoutInflater;
  }

  public int getCount()
  {
    int i = super.getCount();
    if ((!this.mDisableFake) && ((i > 0) || (this.mShowFakeEvenIfEmpty)))
      i += 1;
    return i;
  }

  protected Object getFakeItem(int paramInt)
  {
    return null;
  }

  protected long getFakeItemId(int paramInt)
  {
    return -9223372036854775808L;
  }

  public abstract View getFakeView(int paramInt, View paramView, ViewGroup paramViewGroup);

  public Object getItem(int paramInt)
  {
    Object localObject;
    if (this.mDisableFake)
      localObject = super.getItem(paramInt);
    while (true)
    {
      return localObject;
      int i = this.mFakeItemIndex;
      if (paramInt < i)
      {
        localObject = super.getItem(paramInt);
      }
      else if (this.mFakeItemIndex != paramInt)
      {
        localObject = getFakeItem(paramInt);
      }
      else
      {
        int j = paramInt + -1;
        localObject = super.getItem(j);
      }
    }
  }

  public long getItemId(int paramInt)
  {
    long l;
    if (this.mDisableFake)
      l = super.getItemId(paramInt);
    while (true)
    {
      return l;
      int i = this.mFakeItemIndex;
      if (paramInt < i)
      {
        l = super.getItemId(paramInt);
      }
      else if (this.mFakeItemIndex != paramInt)
      {
        l = getFakeItemId(paramInt);
      }
      else
      {
        int j = paramInt + -1;
        l = super.getItemId(j);
      }
    }
  }

  public int getItemViewType(int paramInt)
  {
    int i;
    if (this.mDisableFake)
      i = super.getItemViewType(paramInt);
    while (true)
    {
      return i;
      int j = this.mFakeItemIndex;
      if (paramInt != j)
      {
        int k = this.mFakeItemLayout;
        int m = this.mLayoutId;
        if (k != m)
          i = 1;
      }
      else
      {
        i = 0;
      }
    }
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView;
    if (this.mDisableFake)
      localView = super.getView(paramInt, paramView, paramViewGroup);
    while (true)
    {
      return localView;
      int i = this.mFakeItemIndex;
      int j = this.mFakeItemIndex;
      int k = super.getCount();
      if (j > k)
        i = 0;
      if (paramInt < i)
      {
        localView = super.getView(paramInt, paramView, paramViewGroup);
      }
      else if (i != paramInt)
      {
        if (paramView == null)
        {
          Context localContext = getContext();
          paramView = newFakeView(localContext, paramViewGroup);
        }
        localView = getFakeView(paramInt, paramView, paramViewGroup);
      }
      else
      {
        int m = paramInt + -1;
        localView = super.getView(m, paramView, paramViewGroup);
      }
    }
  }

  public int getViewTypeCount()
  {
    int i = 1;
    if (this.mDisableFake);
    while (true)
    {
      return i;
      int j = this.mFakeItemLayout;
      int k = this.mLayoutId;
      if (j != k)
        i = 2;
    }
  }

  protected View newFakeView(Context paramContext, ViewGroup paramViewGroup)
  {
    LayoutInflater localLayoutInflater = this.mLayoutInflater;
    int i = this.mFakeItemLayout;
    return localLayoutInflater.inflate(i, paramViewGroup, false);
  }

  public void setDisableFake(boolean paramBoolean)
  {
    if (this.mDisableFake != paramBoolean)
      return;
    this.mDisableFake = paramBoolean;
    notifyDataSetChanged();
  }

  public void setShowFakeEvenIfEmpty(boolean paramBoolean)
  {
    boolean bool = this.mShowFakeEvenIfEmpty;
    if (paramBoolean != bool)
      return;
    this.mShowFakeEvenIfEmpty = paramBoolean;
    notifyDataSetChanged();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.FakeNthCardMediaListCardAdapter
 * JD-Core Version:    0.6.2
 */