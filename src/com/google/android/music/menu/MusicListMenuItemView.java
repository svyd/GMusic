package com.google.android.music.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicListMenuItemView extends RelativeLayout
{
  private ImageView mLeftIconView;
  private MusicListMenuItem mMenuItem;
  private ImageView mRightIconView;
  private TextView mTitleView;

  public MusicListMenuItemView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, 0);
    setDescendantFocusability(262144);
  }

  public MusicListMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet);
    setDescendantFocusability(262144);
  }

  public void initialize(MusicListMenuItem paramMusicListMenuItem)
  {
    this.mMenuItem = paramMusicListMenuItem;
    setVisibility(0);
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    ImageView localImageView1 = (ImageView)findViewById(2131296425);
    this.mLeftIconView = localImageView1;
    TextView localTextView = (TextView)findViewById(2131296326);
    this.mTitleView = localTextView;
    ImageView localImageView2 = (ImageView)findViewById(2131296426);
    this.mRightIconView = localImageView2;
  }

  public boolean performClick()
  {
    boolean bool = true;
    if (super.performClick());
    while (true)
    {
      return bool;
      if (this.mMenuItem.invoke())
        playSoundEffect(0);
      else
        bool = false;
    }
  }

  public void setIcon(Drawable paramDrawable)
  {
    if ((this.mLeftIconView == null) && (paramDrawable == null))
      return;
    if (paramDrawable != null)
    {
      this.mLeftIconView.setImageDrawable(paramDrawable);
      if (this.mLeftIconView.getVisibility() == 0)
        return;
      this.mLeftIconView.setVisibility(0);
      return;
    }
    this.mLeftIconView.setVisibility(8);
  }

  public void setTitle(CharSequence paramCharSequence)
  {
    if (paramCharSequence != null)
    {
      this.mTitleView.setText(paramCharSequence);
      if (this.mTitleView.getVisibility() == 0)
        return;
      this.mTitleView.setVisibility(0);
      return;
    }
    if (this.mTitleView.getVisibility() == 8)
      return;
    this.mTitleView.setVisibility(8);
  }

  protected void setWidgetImage(int paramInt)
  {
    this.mRightIconView.setImageResource(paramInt);
  }

  protected void setWidgetVisible(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mRightIconView.setVisibility(0);
      return;
    }
    this.mRightIconView.setVisibility(8);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicListMenuItemView
 * JD-Core Version:    0.6.2
 */