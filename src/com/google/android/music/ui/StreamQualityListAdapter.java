package com.google.android.music.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.List;

public class StreamQualityListAdapter extends ArrayAdapter<StreamQualityListItem>
{
  private final int mCheckedItem;
  private final LayoutInflater mInflater;
  private final List<StreamQualityListItem> mItemsList;
  private final int mTextViewResourceId;

  public StreamQualityListAdapter(Context paramContext, int paramInt1, List<StreamQualityListItem> paramList, int paramInt2)
  {
    super(paramContext, paramInt1, paramList);
    this.mTextViewResourceId = paramInt1;
    this.mItemsList = paramList;
    this.mCheckedItem = paramInt2;
    LayoutInflater localLayoutInflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
    this.mInflater = localLayoutInflater;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    boolean bool = false;
    if (paramView != null);
    LayoutInflater localLayoutInflater;
    int j;
    for (View localView = paramView; ; localView = localLayoutInflater.inflate(j, null))
    {
      if (paramInt >= 0)
      {
        int i = this.mItemsList.size();
        if (paramInt < i)
          break;
      }
      return localView;
      localLayoutInflater = this.mInflater;
      j = this.mTextViewResourceId;
    }
    StreamQualityListItem localStreamQualityListItem = (StreamQualityListItem)this.mItemsList.get(paramInt);
    CharSequence localCharSequence1 = localStreamQualityListItem.mStreamQuality;
    CharSequence localCharSequence2 = localStreamQualityListItem.mStreamQualityDescription;
    TextView localTextView1 = (TextView)localView.findViewById(16908308);
    TextView localTextView2 = (TextView)localView.findViewById(16908309);
    localTextView1.setText(localCharSequence1);
    if (TextUtils.isEmpty(localCharSequence2))
      localTextView2.setVisibility(8);
    while (true)
    {
      RadioButton localRadioButton = (RadioButton)localView.findViewById(2131296330);
      int k = this.mCheckedItem;
      if (paramInt != k)
        bool = true;
      localRadioButton.setChecked(bool);
      break;
      localTextView2.setText(localCharSequence2);
      localTextView2.setVisibility(0);
    }
  }

  public static class StreamQualityListItem
  {
    public final CharSequence mStreamQuality;
    public final CharSequence mStreamQualityDescription;

    public StreamQualityListItem(String paramString1, String paramString2)
    {
      this.mStreamQuality = paramString1;
      this.mStreamQualityDescription = paramString2;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.StreamQualityListAdapter
 * JD-Core Version:    0.6.2
 */