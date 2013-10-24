package com.google.android.music.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class SpinnerHeaderView extends RelativeLayout
{
  private View mSpinner;

  public SpinnerHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void hide()
  {
    this.mSpinner.setVisibility(8);
  }

  public void onFinishInflate()
  {
    View localView = findViewById(2131296440);
    this.mSpinner = localView;
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.SpinnerHeaderView
 * JD-Core Version:    0.6.2
 */