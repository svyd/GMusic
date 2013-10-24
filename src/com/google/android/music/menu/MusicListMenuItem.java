package com.google.android.music.menu;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MusicListMenuItem extends MusicMenuItem
{
  private boolean mChecked;
  private MusicListMenuItemView mItemView;
  private int mLayoutId;
  private boolean mRequestFocus;
  private boolean mSelected;
  private WidgetMode mWidgetMode;
  private int mWidgetOffDrawable;
  private int mWidgetOnDrawable;

  private void refreshWidget()
  {
    if (this.mItemView == null)
      return;
    WidgetMode localWidgetMode1 = this.mWidgetMode;
    WidgetMode localWidgetMode2 = WidgetMode.WIDGET_NONE;
    if (localWidgetMode1 == localWidgetMode2)
      this.mItemView.setWidgetVisible(false);
    while (true)
    {
      MusicListMenuItemView localMusicListMenuItemView1 = this.mItemView;
      boolean bool1 = this.mSelected;
      localMusicListMenuItemView1.setSelected(bool1);
      if (!this.mRequestFocus)
        return;
      boolean bool2 = this.mItemView.requestFocus();
      this.mRequestFocus = false;
      return;
      MusicListMenuItemView localMusicListMenuItemView2 = this.mItemView;
      int i = getWidgetImage();
      localMusicListMenuItemView2.setWidgetImage(i);
      this.mItemView.setWidgetVisible(true);
    }
  }

  public boolean getCheckboxEnabled()
  {
    WidgetMode localWidgetMode1 = this.mWidgetMode;
    WidgetMode localWidgetMode2 = WidgetMode.WIDGET_CHECKBOX;
    if (localWidgetMode1 == localWidgetMode2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean getChecked()
  {
    return this.mChecked;
  }

  public View getItemView(ViewGroup paramViewGroup)
  {
    if (this.mItemView == null)
    {
      LayoutInflater localLayoutInflater = LayoutInflater.from(this.mMusicMenu.getContext());
      int i = this.mLayoutId;
      MusicListMenuItemView localMusicListMenuItemView1 = (MusicListMenuItemView)localLayoutInflater.inflate(i, paramViewGroup, false);
      this.mItemView = localMusicListMenuItemView1;
      this.mItemView.initialize(this);
      MusicListMenuItemView localMusicListMenuItemView2 = this.mItemView;
      String str = getTitle();
      localMusicListMenuItemView2.setTitle(str);
      MusicListMenuItemView localMusicListMenuItemView3 = this.mItemView;
      Drawable localDrawable = getIcon();
      localMusicListMenuItemView3.setIcon(localDrawable);
      MusicListMenuItemView localMusicListMenuItemView4 = this.mItemView;
      boolean bool = isEnabled();
      localMusicListMenuItemView4.setEnabled(bool);
      refreshWidget();
    }
    return this.mItemView;
  }

  public int getWidgetImage()
  {
    if (getChecked());
    for (int i = this.mWidgetOnDrawable; ; i = this.mWidgetOffDrawable)
      return i;
  }

  public void onInvoke()
  {
    boolean bool = true;
    if (getCheckboxEnabled())
    {
      if (!getChecked());
      while (true)
      {
        setChecked(bool);
        return;
        bool = false;
      }
    }
    if (!this.mMusicMenu.getRadioButtonsEnabled())
      return;
    setChecked(true);
  }

  public void setChecked(boolean paramBoolean)
  {
    setCheckedInternal(paramBoolean);
    this.mMusicMenu.onItemChecked(this, paramBoolean);
  }

  protected void setCheckedInternal(boolean paramBoolean)
  {
    this.mChecked = paramBoolean;
    refreshWidget();
  }

  public static enum WidgetMode
  {
    static
    {
      WIDGET_CHECKBOX = new WidgetMode("WIDGET_CHECKBOX", 2);
      WidgetMode[] arrayOfWidgetMode = new WidgetMode[3];
      WidgetMode localWidgetMode1 = WIDGET_NONE;
      arrayOfWidgetMode[0] = localWidgetMode1;
      WidgetMode localWidgetMode2 = WIDGET_RADIO_BUTTON;
      arrayOfWidgetMode[1] = localWidgetMode2;
      WidgetMode localWidgetMode3 = WIDGET_CHECKBOX;
      arrayOfWidgetMode[2] = localWidgetMode3;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicListMenuItem
 * JD-Core Version:    0.6.2
 */