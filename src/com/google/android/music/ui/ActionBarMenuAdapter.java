package com.google.android.music.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class ActionBarMenuAdapter extends ArrayAdapter<String>
  implements SpinnerAdapter
{
  private String mTitle;

  public ActionBarMenuAdapter(Context paramContext, int paramInt, String[] paramArrayOfString)
  {
    super(paramContext, paramInt, paramArrayOfString);
  }

  private View convert(View paramView, ViewGroup paramViewGroup, int paramInt)
  {
    if (paramView == null)
      paramView = LayoutInflater.from(paramViewGroup.getContext()).inflate(paramInt, paramViewGroup, false);
    return paramView;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = convert(paramView, paramViewGroup, 2130968619);
    TextView localTextView1 = (TextView)localView.findViewById(16908308);
    TextView localTextView2 = (TextView)localView.findViewById(16908309);
    String str1 = this.mTitle;
    localTextView1.setText(str1);
    String str2 = ((String)getItem(paramInt)).toUpperCase();
    localTextView2.setText(str2);
    return localView;
  }

  public void setTitle(String paramString)
  {
    this.mTitle = paramString;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.ActionBarMenuAdapter
 * JD-Core Version:    0.6.2
 */