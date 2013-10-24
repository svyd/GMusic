package com.google.android.music.ui.cardlib;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayCardClusterViewHeader extends LinearLayout
{
  private View mContent;
  private TextView mMoreView;
  private TextView mTitleMain;
  private TextView mTitleSecondary;

  public PlayCardClusterViewHeader(Context paramContext)
  {
    this(paramContext, null);
  }

  public PlayCardClusterViewHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  public void hide()
  {
    this.mContent.setVisibility(8);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    View localView = findViewById(2131296344);
    this.mContent = localView;
    TextView localTextView1 = (TextView)findViewById(2131296466);
    this.mTitleMain = localTextView1;
    TextView localTextView2 = (TextView)findViewById(2131296467);
    this.mTitleSecondary = localTextView2;
    TextView localTextView3 = (TextView)findViewById(2131296468);
    this.mMoreView = localTextView3;
  }

  public void setContent(String paramString1, String paramString2, String paramString3)
  {
    TextView localTextView1 = this.mTitleMain;
    String str1 = paramString1 + " ";
    localTextView1.setText(str1);
    if (TextUtils.isEmpty(paramString2))
      this.mTitleSecondary.setVisibility(8);
    while (TextUtils.isEmpty(paramString3))
    {
      this.mMoreView.setVisibility(8);
      return;
      TextView localTextView2 = this.mTitleSecondary;
      Spanned localSpanned = Html.fromHtml(paramString2);
      localTextView2.setText(localSpanned);
      this.mTitleSecondary.setVisibility(0);
    }
    TextView localTextView3 = this.mMoreView;
    String str2 = paramString3.toUpperCase();
    localTextView3.setText(str2);
    this.mMoreView.setVisibility(0);
  }

  public void setMoreButtonClickHandler(View.OnClickListener paramOnClickListener)
  {
    boolean bool1 = true;
    setOnClickListener(paramOnClickListener);
    boolean bool2;
    if (paramOnClickListener != null)
    {
      bool2 = true;
      setClickable(bool2);
      if (paramOnClickListener == null)
        break label33;
    }
    while (true)
    {
      setFocusable(bool1);
      return;
      bool2 = false;
      break;
      label33: bool1 = false;
    }
  }

  public void show()
  {
    this.mContent.setVisibility(0);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.ui.cardlib.PlayCardClusterViewHeader
 * JD-Core Version:    0.6.2
 */