package android.support.v7.internal.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.ActionProvider.VisibilityListener;
import android.support.v4.view.MenuItemCompat.OnActionExpandListener;
import android.util.Log;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewDebug.CapturedViewProperty;
import android.widget.LinearLayout;

public final class MenuItemImpl
  implements SupportMenuItem
{
  private static String sDeleteShortcutLabel;
  private static String sEnterShortcutLabel;
  private static String sPrependShortcutLabel;
  private static String sSpaceShortcutLabel;
  private android.support.v4.view.ActionProvider mActionProvider;
  private View mActionView;
  private final int mCategoryOrder;
  private MenuItem.OnMenuItemClickListener mClickListener;
  private int mFlags = 16;
  private final int mGroup;
  private Drawable mIconDrawable;
  private int mIconResId = 0;
  private final int mId;
  private Intent mIntent;
  private boolean mIsActionViewExpanded = false;
  private Runnable mItemCallback;
  private MenuBuilder mMenu;
  private ContextMenu.ContextMenuInfo mMenuInfo;
  private MenuItemCompat.OnActionExpandListener mOnActionExpandListener;
  private final int mOrdering;
  private char mShortcutAlphabeticChar;
  private char mShortcutNumericChar;
  private int mShowAsAction = 0;
  private SubMenuBuilder mSubMenu;
  private CharSequence mTitle;
  private CharSequence mTitleCondensed;

  MenuItemImpl(MenuBuilder paramMenuBuilder, int paramInt1, int paramInt2, int paramInt3, int paramInt4, CharSequence paramCharSequence, int paramInt5)
  {
    this.mMenu = paramMenuBuilder;
    this.mId = paramInt2;
    this.mGroup = paramInt1;
    this.mCategoryOrder = paramInt3;
    this.mOrdering = paramInt4;
    this.mTitle = paramCharSequence;
    this.mShowAsAction = paramInt5;
  }

  public void actionFormatChanged()
  {
    this.mMenu.onItemActionRequestChanged(this);
  }

  public boolean collapseActionView()
  {
    boolean bool = false;
    if ((this.mShowAsAction & 0x8) == 0);
    while (true)
    {
      return bool;
      if (this.mActionView == null)
        bool = true;
      else if ((this.mOnActionExpandListener == null) || (this.mOnActionExpandListener.onMenuItemActionCollapse(this)))
        bool = this.mMenu.collapseItemActionView(this);
    }
  }

  public boolean expandActionView()
  {
    boolean bool = false;
    if (((this.mShowAsAction & 0x8) == 0) || (this.mActionView == null));
    while (true)
    {
      return bool;
      if ((this.mOnActionExpandListener == null) || (this.mOnActionExpandListener.onMenuItemActionExpand(this)))
        bool = this.mMenu.expandItemActionView(this);
    }
  }

  public android.view.ActionProvider getActionProvider()
  {
    throw new UnsupportedOperationException("Implementation should use getSupportActionProvider!");
  }

  public View getActionView()
  {
    View localView1;
    if (this.mActionView != null)
      localView1 = this.mActionView;
    while (true)
    {
      return localView1;
      if (this.mActionProvider != null)
      {
        View localView2 = this.mActionProvider.onCreateActionView(this);
        this.mActionView = localView2;
        localView1 = this.mActionView;
      }
      else
      {
        localView1 = null;
      }
    }
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
    Drawable localDrawable;
    if (this.mIconDrawable != null)
      localDrawable = this.mIconDrawable;
    while (true)
    {
      return localDrawable;
      if (this.mIconResId != 0)
      {
        Resources localResources = this.mMenu.getResources();
        int i = this.mIconResId;
        localDrawable = localResources.getDrawable(i);
        this.mIconResId = 0;
        this.mIconDrawable = localDrawable;
      }
      else
      {
        localDrawable = null;
      }
    }
  }

  public Intent getIntent()
  {
    return this.mIntent;
  }

  @ViewDebug.CapturedViewProperty
  public int getItemId()
  {
    return this.mId;
  }

  public ContextMenu.ContextMenuInfo getMenuInfo()
  {
    return this.mMenuInfo;
  }

  public char getNumericShortcut()
  {
    return this.mShortcutNumericChar;
  }

  public int getOrder()
  {
    return this.mCategoryOrder;
  }

  public int getOrdering()
  {
    return this.mOrdering;
  }

  char getShortcut()
  {
    return this.mShortcutAlphabeticChar;
  }

  String getShortcutLabel()
  {
    char c = getShortcut();
    String str1;
    if (c == null)
    {
      str1 = "";
      return str1;
    }
    String str2 = sPrependShortcutLabel;
    StringBuilder localStringBuilder1 = new StringBuilder(str2);
    switch (c)
    {
    default:
      StringBuilder localStringBuilder2 = localStringBuilder1.append(c);
    case '\n':
    case '\b':
    case ' ':
    }
    while (true)
    {
      str1 = localStringBuilder1.toString();
      break;
      String str3 = sEnterShortcutLabel;
      StringBuilder localStringBuilder3 = localStringBuilder1.append(str3);
      continue;
      String str4 = sDeleteShortcutLabel;
      StringBuilder localStringBuilder4 = localStringBuilder1.append(str4);
      continue;
      String str5 = sSpaceShortcutLabel;
      StringBuilder localStringBuilder5 = localStringBuilder1.append(str5);
    }
  }

  public SubMenu getSubMenu()
  {
    return this.mSubMenu;
  }

  public android.support.v4.view.ActionProvider getSupportActionProvider()
  {
    return this.mActionProvider;
  }

  @ViewDebug.CapturedViewProperty
  public CharSequence getTitle()
  {
    return this.mTitle;
  }

  public CharSequence getTitleCondensed()
  {
    if (this.mTitleCondensed != null);
    for (CharSequence localCharSequence = this.mTitleCondensed; ; localCharSequence = this.mTitle)
      return localCharSequence;
  }

  CharSequence getTitleForItemView(MenuView.ItemView paramItemView)
  {
    if ((paramItemView != null) && (paramItemView.prefersCondensedTitle()));
    for (CharSequence localCharSequence = getTitleCondensed(); ; localCharSequence = getTitle())
      return localCharSequence;
  }

  public boolean hasCollapsibleActionView()
  {
    if (((this.mShowAsAction & 0x8) != 0) && (this.mActionView != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean hasSubMenu()
  {
    if (this.mSubMenu != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean invoke()
  {
    boolean bool = true;
    if ((this.mClickListener != null) && (this.mClickListener.onMenuItemClick(this)));
    while (true)
    {
      return bool;
      MenuBuilder localMenuBuilder1 = this.mMenu;
      MenuBuilder localMenuBuilder2 = this.mMenu.getRootMenu();
      if (!localMenuBuilder1.dispatchMenuItemSelected(localMenuBuilder2, this))
        if (this.mItemCallback != null)
          this.mItemCallback.run();
        else if (this.mIntent != null)
          try
          {
            Context localContext = this.mMenu.getContext();
            Intent localIntent = this.mIntent;
            localContext.startActivity(localIntent);
          }
          catch (ActivityNotFoundException localActivityNotFoundException)
          {
            int i = Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", localActivityNotFoundException);
          }
        else if ((this.mActionProvider == null) || (!this.mActionProvider.onPerformDefaultAction()))
          bool = false;
    }
  }

  public boolean isActionButton()
  {
    if ((this.mFlags & 0x20) == 32);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isActionViewExpanded()
  {
    return this.mIsActionViewExpanded;
  }

  public boolean isCheckable()
  {
    int i = 1;
    if ((this.mFlags & 0x1) != i);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public boolean isChecked()
  {
    if ((this.mFlags & 0x2) == 2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isEnabled()
  {
    if ((this.mFlags & 0x10) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isExclusiveCheckable()
  {
    if ((this.mFlags & 0x4) != 0);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isVisible()
  {
    boolean bool = true;
    if ((this.mActionProvider != null) && (this.mActionProvider.overridesItemVisibility()))
      if (((this.mFlags & 0x8) != 0) || (!this.mActionProvider.isVisible()));
    while (true)
    {
      return bool;
      bool = false;
      continue;
      if ((this.mFlags & 0x8) != 0)
        bool = false;
    }
  }

  public boolean requestsActionButton()
  {
    int i = 1;
    if ((this.mShowAsAction & 0x1) != i);
    while (true)
    {
      return i;
      int j = 0;
    }
  }

  public boolean requiresActionButton()
  {
    if ((this.mShowAsAction & 0x2) == 2);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public MenuItem setActionProvider(android.view.ActionProvider paramActionProvider)
  {
    throw new UnsupportedOperationException("Implementation should use setSupportActionProvider!");
  }

  public SupportMenuItem setActionView(int paramInt)
  {
    Context localContext = this.mMenu.getContext();
    LayoutInflater localLayoutInflater = LayoutInflater.from(localContext);
    LinearLayout localLinearLayout = new LinearLayout(localContext);
    View localView = localLayoutInflater.inflate(paramInt, localLinearLayout, false);
    SupportMenuItem localSupportMenuItem = setActionView(localView);
    return this;
  }

  public SupportMenuItem setActionView(View paramView)
  {
    this.mActionView = paramView;
    this.mActionProvider = null;
    if ((paramView != null) && (paramView.getId() == -1) && (this.mId > 0))
    {
      int i = this.mId;
      paramView.setId(i);
    }
    this.mMenu.onItemActionRequestChanged(this);
    return this;
  }

  public void setActionViewExpanded(boolean paramBoolean)
  {
    this.mIsActionViewExpanded = paramBoolean;
    this.mMenu.onItemsChanged(false);
  }

  public MenuItem setAlphabeticShortcut(char paramChar)
  {
    if (this.mShortcutAlphabeticChar != paramChar);
    while (true)
    {
      return this;
      char c = Character.toLowerCase(paramChar);
      this.mShortcutAlphabeticChar = c;
      this.mMenu.onItemsChanged(false);
    }
  }

  public MenuItem setCheckable(boolean paramBoolean)
  {
    int i = this.mFlags;
    int j = this.mFlags & 0xFFFFFFFE;
    if (paramBoolean);
    for (int k = 1; ; k = 0)
    {
      int m = k | j;
      this.mFlags = m;
      int n = this.mFlags;
      if (i != n)
        this.mMenu.onItemsChanged(false);
      return this;
    }
  }

  public MenuItem setChecked(boolean paramBoolean)
  {
    if ((this.mFlags & 0x4) != 0)
      this.mMenu.setExclusiveItemChecked(this);
    while (true)
    {
      return this;
      setCheckedInt(paramBoolean);
    }
  }

  void setCheckedInt(boolean paramBoolean)
  {
    int i = this.mFlags;
    int j = this.mFlags & 0xFFFFFFFD;
    if (paramBoolean);
    for (int k = 2; ; k = 0)
    {
      int m = k | j;
      this.mFlags = m;
      int n = this.mFlags;
      if (i != n)
        return;
      this.mMenu.onItemsChanged(false);
      return;
    }
  }

  public MenuItem setEnabled(boolean paramBoolean)
  {
    int i;
    if (paramBoolean)
      i = this.mFlags | 0x10;
    int j;
    for (this.mFlags = i; ; this.mFlags = j)
    {
      this.mMenu.onItemsChanged(false);
      return this;
      j = this.mFlags & 0xFFFFFFEF;
    }
  }

  public void setExclusiveCheckable(boolean paramBoolean)
  {
    int i = this.mFlags & 0xFFFFFFFB;
    if (paramBoolean);
    for (int j = 4; ; j = 0)
    {
      int k = j | i;
      this.mFlags = k;
      return;
    }
  }

  public MenuItem setIcon(int paramInt)
  {
    this.mIconDrawable = null;
    this.mIconResId = paramInt;
    this.mMenu.onItemsChanged(false);
    return this;
  }

  public MenuItem setIcon(Drawable paramDrawable)
  {
    this.mIconResId = 0;
    this.mIconDrawable = paramDrawable;
    this.mMenu.onItemsChanged(false);
    return this;
  }

  public MenuItem setIntent(Intent paramIntent)
  {
    this.mIntent = paramIntent;
    return this;
  }

  public void setIsActionButton(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      int i = this.mFlags | 0x20;
      this.mFlags = i;
      return;
    }
    int j = this.mFlags & 0xFFFFFFDF;
    this.mFlags = j;
  }

  void setMenuInfo(ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    this.mMenuInfo = paramContextMenuInfo;
  }

  public MenuItem setNumericShortcut(char paramChar)
  {
    if (this.mShortcutNumericChar != paramChar);
    while (true)
    {
      return this;
      this.mShortcutNumericChar = paramChar;
      this.mMenu.onItemsChanged(false);
    }
  }

  public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener paramOnActionExpandListener)
  {
    throw new UnsupportedOperationException("Implementation should use setSupportOnActionExpandListener!");
  }

  public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    this.mClickListener = paramOnMenuItemClickListener;
    return this;
  }

  public MenuItem setShortcut(char paramChar1, char paramChar2)
  {
    this.mShortcutNumericChar = paramChar1;
    char c = Character.toLowerCase(paramChar2);
    this.mShortcutAlphabeticChar = c;
    this.mMenu.onItemsChanged(false);
    return this;
  }

  public void setShowAsAction(int paramInt)
  {
    switch (paramInt & 0x3)
    {
    default:
      throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
    case 0:
    case 1:
    case 2:
    }
    this.mShowAsAction = paramInt;
    this.mMenu.onItemActionRequestChanged(this);
  }

  public SupportMenuItem setShowAsActionFlags(int paramInt)
  {
    setShowAsAction(paramInt);
    return this;
  }

  void setSubMenu(SubMenuBuilder paramSubMenuBuilder)
  {
    this.mSubMenu = paramSubMenuBuilder;
    CharSequence localCharSequence = getTitle();
    SubMenu localSubMenu = paramSubMenuBuilder.setHeaderTitle(localCharSequence);
  }

  public SupportMenuItem setSupportActionProvider(android.support.v4.view.ActionProvider paramActionProvider)
  {
    if (this.mActionProvider == paramActionProvider);
    while (true)
    {
      return this;
      this.mActionView = null;
      if (this.mActionProvider != null)
        this.mActionProvider.setVisibilityListener(null);
      this.mActionProvider = paramActionProvider;
      this.mMenu.onItemsChanged(true);
      if (paramActionProvider != null)
      {
        ActionProvider.VisibilityListener local1 = new ActionProvider.VisibilityListener()
        {
          public void onActionProviderVisibilityChanged(boolean paramAnonymousBoolean)
          {
            MenuBuilder localMenuBuilder = MenuItemImpl.this.mMenu;
            MenuItemImpl localMenuItemImpl = MenuItemImpl.this;
            localMenuBuilder.onItemVisibleChanged(localMenuItemImpl);
          }
        };
        paramActionProvider.setVisibilityListener(local1);
      }
    }
  }

  public MenuItem setTitle(int paramInt)
  {
    String str = this.mMenu.getContext().getString(paramInt);
    return setTitle(str);
  }

  public MenuItem setTitle(CharSequence paramCharSequence)
  {
    this.mTitle = paramCharSequence;
    this.mMenu.onItemsChanged(false);
    if (this.mSubMenu != null)
      SubMenu localSubMenu = this.mSubMenu.setHeaderTitle(paramCharSequence);
    return this;
  }

  public MenuItem setTitleCondensed(CharSequence paramCharSequence)
  {
    this.mTitleCondensed = paramCharSequence;
    if (paramCharSequence == null)
      CharSequence localCharSequence = this.mTitle;
    this.mMenu.onItemsChanged(false);
    return this;
  }

  public MenuItem setVisible(boolean paramBoolean)
  {
    if (setVisibleInt(paramBoolean))
      this.mMenu.onItemVisibleChanged(this);
    return this;
  }

  boolean setVisibleInt(boolean paramBoolean)
  {
    boolean bool = false;
    int i = this.mFlags;
    int j = this.mFlags & 0xFFFFFFF7;
    if (paramBoolean);
    for (int k = 0; ; k = 8)
    {
      int m = k | j;
      this.mFlags = m;
      int n = this.mFlags;
      if (i != n)
        bool = true;
      return bool;
    }
  }

  public boolean shouldShowIcon()
  {
    return this.mMenu.getOptionalIconsVisible();
  }

  boolean shouldShowShortcut()
  {
    if ((this.mMenu.isShortcutsVisible()) && (getShortcut() != null));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean showsTextAsAction()
  {
    if ((this.mShowAsAction & 0x4) == 4);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public String toString()
  {
    return this.mTitle.toString();
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuItemImpl
 * JD-Core Version:    0.6.2
 */