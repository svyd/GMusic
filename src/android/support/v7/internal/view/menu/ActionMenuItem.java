package android.support.v7.internal.view.menu;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

public class ActionMenuItem
  implements SupportMenuItem
{
  private final int mCategoryOrder;
  private MenuItem.OnMenuItemClickListener mClickListener;
  private Context mContext;
  private int mFlags = 16;
  private final int mGroup;
  private Drawable mIconDrawable;
  private int mIconResId = 0;
  private final int mId;
  private Intent mIntent;
  private final int mOrdering;
  private char mShortcutAlphabeticChar;
  private char mShortcutNumericChar;
  private CharSequence mTitle;
  private CharSequence mTitleCondensed;

  public ActionMenuItem(Context paramContext, int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence)
  {
    this.mContext = paramContext;
    this.mId = paramInt2;
    this.mGroup = paramInt1;
    this.mCategoryOrder = paramInt3;
    this.mOrdering = paramInt4;
    this.mTitle = paramCharSequence;
  }

  public boolean collapseActionView()
  {
    return false;
  }

  public boolean expandActionView()
  {
    return false;
  }

  public android.view.ActionProvider getActionProvider()
  {
    throw new UnsupportedOperationException();
  }

  public View getActionView()
  {
    return null;
  }

  public char getAlphabeticShortcut()
  {
    return this.mShortcutAlphabeticChar;
  }

  public int getGroupId()
  {
    return this.mGroup;
  }

  public Drawable getIcon()
  {
    return this.mIconDrawable;
  }

  public Intent getIntent()
  {
    return this.mIntent;
  }

  public int getItemId()
  {
    return this.mId;
  }

  public ContextMenu.ContextMenuInfo getMenuInfo()
  {
    return null;
  }

  public char getNumericShortcut()
  {
    return this.mShortcutNumericChar;
  }

  public int getOrder()
  {
    return this.mOrdering;
  }

  public SubMenu getSubMenu()
  {
    return null;
  }

  public android.support.v4.view.ActionProvider getSupportActionProvider()
  {
    return null;
  }

  public CharSequence getTitle()
  {
    return this.mTitle;
  }

  public CharSequence getTitleCondensed()
  {
    return this.mTitleCondensed;
  }

  public boolean hasSubMenu()
  {
    return false;
  }

  public boolean isActionViewExpanded()
  {
    return false;
  }

  public boolean isCheckable()
  {
    if ((this.mFlags & 0x1) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isChecked()
  {
    if ((this.mFlags & 0x2) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isEnabled()
  {
    if ((this.mFlags & 0x10) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isVisible()
  {
    if ((this.mFlags & 0x8) == 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public MenuItem setActionProvider(android.view.ActionProvider paramActionProvider)
  {
    throw new UnsupportedOperationException();
  }

  public SupportMenuItem setActionView(int paramInt)
  {
    throw new UnsupportedOperationException();
  }

  public SupportMenuItem setActionView(View paramView)
  {
    throw new UnsupportedOperationException();
  }

  public MenuItem setAlphabeticShortcut(char paramChar)
  {
    this.mShortcutAlphabeticChar = paramChar;
    return this;
  }

  public MenuItem setCheckable(boolean paramBoolean)
  {
    int i = this.mFlags & 0xFFFFFFFE;
    if (paramBoolean);
    for (int j = 1; ; j = 0)
    {
      int k = j | i;
      this.mFlags = k;
      return this;
    }
  }

  public MenuItem setChecked(boolean paramBoolean)
  {
    int i = this.mFlags & 0xFFFFFFFD;
    if (paramBoolean);
    for (int j = 2; ; j = 0)
    {
      int k = j | i;
      this.mFlags = k;
      return this;
    }
  }

  public MenuItem setEnabled(boolean paramBoolean)
  {
    int i = this.mFlags & 0xFFFFFFEF;
    if (paramBoolean);
    for (int j = 16; ; j = 0)
    {
      int k = j | i;
      this.mFlags = k;
      return this;
    }
  }

  public MenuItem setIcon(int paramInt)
  {
    this.mIconResId = paramInt;
    Drawable localDrawable = this.mContext.getResources().getDrawable(paramInt);
    this.mIconDrawable = localDrawable;
    return this;
  }

  public MenuItem setIcon(Drawable paramDrawable)
  {
    this.mIconDrawable = paramDrawable;
    this.mIconResId = 0;
    return this;
  }

  public MenuItem setIntent(Intent paramIntent)
  {
    this.mIntent = paramIntent;
    return this;
  }

  public MenuItem setNumericShortcut(char paramChar)
  {
    this.mShortcutNumericChar = paramChar;
    return this;
  }

  public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener paramOnActionExpandListener)
  {
    throw new UnsupportedOperationException();
  }

  public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    this.mClickListener = paramOnMenuItemClickListener;
    return this;
  }

  public MenuItem setShortcut(char paramChar1, char paramChar2)
  {
    this.mShortcutNumericChar = paramChar1;
    this.mShortcutAlphabeticChar = paramChar2;
    return this;
  }

  public void setShowAsAction(int paramInt)
  {
  }

  public SupportMenuItem setShowAsActionFlags(int paramInt)
  {
    setShowAsAction(paramInt);
    return this;
  }

  public SupportMenuItem setSupportActionProvider(android.support.v4.view.ActionProvider paramActionProvider)
  {
    throw new UnsupportedOperationException();
  }

  public MenuItem setTitle(int paramInt)
  {
    String str = this.mContext.getResources().getString(paramInt);
    this.mTitle = str;
    return this;
  }

  public MenuItem setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    return this;
  }

  public MenuItem setTitleCondensed(CharSequence paramCharSequence)
  {
    this.mTitleCondensed = paramCharSequence;
    return this;
  }

  public MenuItem setVisible(boolean paramBoolean)
  {
    int i = this.mFlags & 0x8;
    if (paramBoolean);
    for (int j = 0; ; j = 8)
    {
      int k = j | i;
      this.mFlags = k;
      return this;
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.ActionMenuItem
 * JD-Core Version:    0.6.2
 */