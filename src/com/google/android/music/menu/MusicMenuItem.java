package com.google.android.music.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;

public abstract class MusicMenuItem
{
  protected boolean mEnabled;
  protected boolean mHasSeparator;
  protected Drawable mIcon;
  protected Intent mIntent;
  protected int mItemId;
  protected MusicMenu mMusicMenu;
  protected int mOrder;
  protected MusicListMenu mSubMenu;
  protected String mTitle;
  protected boolean mVisible;

  public boolean getCheckboxEnabled()
  {
    throw new UnsupportedOperationException();
  }

  public boolean getChecked()
  {
    throw new UnsupportedOperationException();
  }

  public Drawable getIcon()
  {
    return this.mIcon;
  }

  public MusicListMenu getSubMenu()
  {
    return this.mSubMenu;
  }

  public String getTitle()
  {
    return this.mTitle;
  }

  public boolean invoke()
  {
    boolean bool = true;
    onInvoke();
    if (this.mMusicMenu.performItemAction(this));
    while (true)
    {
      return bool;
      if (this.mIntent != null)
        try
        {
          Context localContext = this.mMusicMenu.getContext();
          Intent localIntent = this.mIntent;
          localContext.startActivity(localIntent);
        }
        catch (ActivityNotFoundException localActivityNotFoundException)
        {
          int i = Log.d("MusicMenuItem", "Can't find activity to handle intent; ignoring", localActivityNotFoundException);
        }
      else
        bool = false;
    }
  }

  public boolean isEnabled()
  {
    return this.mEnabled;
  }

  public boolean isVisible()
  {
    return this.mVisible;
  }

  public void onInvoke()
  {
  }

  public void setChecked(boolean paramBoolean)
  {
    throw new UnsupportedOperationException();
  }

  public String toString()
  {
    StringBuilder localStringBuilder1 = new StringBuilder(128);
    String str1 = getClass().getName();
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append(" { ");
    StringBuilder localStringBuilder3 = new StringBuilder().append("id ");
    int i = this.mItemId;
    String str2 = i;
    StringBuilder localStringBuilder4 = localStringBuilder2.append(str2);
    StringBuilder localStringBuilder5 = new StringBuilder().append(", title '");
    String str3 = this.mTitle;
    String str4 = str3 + "'";
    StringBuilder localStringBuilder6 = localStringBuilder4.append(str4);
    StringBuilder localStringBuilder7 = new StringBuilder().append(", order ");
    int j = this.mOrder;
    String str5 = j;
    StringBuilder localStringBuilder8 = localStringBuilder6.append(str5);
    StringBuilder localStringBuilder9 = new StringBuilder().append(", enabled ");
    boolean bool1 = this.mEnabled;
    String str6 = bool1;
    StringBuilder localStringBuilder10 = localStringBuilder8.append(str6);
    StringBuilder localStringBuilder11 = new StringBuilder().append(", visible ");
    boolean bool2 = this.mVisible;
    String str7 = bool2;
    StringBuilder localStringBuilder12 = localStringBuilder10.append(str7);
    StringBuilder localStringBuilder13 = new StringBuilder().append(", separator ");
    boolean bool3 = this.mHasSeparator;
    String str8 = bool3;
    return str8 + " }";
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     com.google.android.music.menu.MusicMenuItem
 * JD-Core Version:    0.6.2
 */