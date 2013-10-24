package com.google.android.music.ui.cardlib;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.music.utils.TypefaceUtil;

public class PlayIconAndTextListAdapter extends ArrayAdapter<IconAndTextListEntry>
{
  Context mContext;
  IconAndTextListEntry[] mData = null;
  int mLayoutResouceId;

  public PlayIconAndTextListAdapter(Context paramContext, int paramInt, IconAndTextListEntry[] paramArrayOfIconAndTextListEntry)
  {
    super(paramContext, paramInt, paramArrayOfIconAndTextListEntry);
    this.mContext = paramContext;
    this.mLayoutResouceId = paramInt;
    this.mData = paramArrayOfIconAndTextListEntry;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    TryNautilusListEntryHolder localTryNautilusListEntryHolder;
    if (paramView == null)
    {
      LayoutInflater localLayoutInflater = ((Activity)this.mContext).getLayoutInflater();
      int i = this.mLayoutResouceId;
      localView = localLayoutInflater.inflate(i, paramViewGroup, false);
      localTryNautilusListEntryHolder = new TryNautilusListEntryHolder(null);
      ImageView localImageView1 = (ImageView)localView.findViewById(2131296494);
      localTryNautilusListEntryHolder.mIconView = localImageView1;
      TextView localTextView1 = (TextView)localView.findViewById(2131296495);
      localTryNautilusListEntryHolder.mTextView = localTextView1;
      TypefaceUtil.setTypeface(localTryNautilusListEntryHolder.mTextView, 1);
      localView.setTag(localTryNautilusListEntryHolder);
    }
    while (true)
    {
      IconAndTextListEntry localIconAndTextListEntry = this.mData[paramInt];
      ImageView localImageView2 = localTryNautilusListEntryHolder.mIconView;
      int j = localIconAndTextListEntry.mIconId;
      localImageView2.setImageResource(j);
      TextView localTextView2 = localTryNautilusListEntryHolder.mTextView;
      int k = localIconAndTextListEntry.mDescriptionId;
      localTextView2.setText(k);
      return localView;
      localTryNautilusListEntryHolder = (TryNautilusListEntryHolder)paramView.getTag();
    }
  }

  private class TryNautilusListEntryHolder
  {
    ImageView mIconView;
    TextView mTextView;

    private TryNautilusListEntryHolder()
    {
    }
  }

  public static class IconAndTextListEntry
  {
    public int mDescriptionId;
    public int mIconId;

    public IconAndTextListEntry(int paramInt1, int paramInt2)
    {
      this.mIconId = paramInt1;
      this.mDescriptionId = paramInt2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.PlayIconAndTextListAdapter
 * JD-Core Version:    0.6.2
 */