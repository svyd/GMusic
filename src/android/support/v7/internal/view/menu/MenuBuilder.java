package android.support.v7.internal.view.menu;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R.bool;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyCharacterMap.KeyData;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBuilder
  implements SupportMenu
{
  private static final int[] sCategoryToOrder = { 1, 4, 5, 3, 2, 0 };
  private ArrayList<MenuItemImpl> mActionItems;
  private Callback mCallback;
  private final Context mContext;
  private ContextMenu.ContextMenuInfo mCurrentMenuInfo;
  private int mDefaultShowAsAction = 0;
  private MenuItemImpl mExpandedItem;
  Drawable mHeaderIcon;
  CharSequence mHeaderTitle;
  View mHeaderView;
  private boolean mIsActionItemsStale;
  private boolean mIsClosing = false;
  private boolean mIsVisibleItemsStale;
  private ArrayList<MenuItemImpl> mItems;
  private boolean mItemsChangedWhileDispatchPrevented = false;
  private ArrayList<MenuItemImpl> mNonActionItems;
  private boolean mOptionalIconsVisible = false;
  private CopyOnWriteArrayList<WeakReference<MenuPresenter>> mPresenters;
  private boolean mPreventDispatchingItemsChanged = false;
  private boolean mQwertyMode;
  private final Resources mResources;
  private boolean mShortcutsVisible;
  private ArrayList<MenuItemImpl> mTempShortcutItemList;
  private ArrayList<MenuItemImpl> mVisibleItems;

  public MenuBuilder(Context paramContext)
  {
    ArrayList localArrayList1 = new ArrayList();
    this.mTempShortcutItemList = localArrayList1;
    CopyOnWriteArrayList localCopyOnWriteArrayList = new CopyOnWriteArrayList();
    this.mPresenters = localCopyOnWriteArrayList;
    this.mContext = paramContext;
    Resources localResources = paramContext.getResources();
    this.mResources = localResources;
    ArrayList localArrayList2 = new ArrayList();
    this.mItems = localArrayList2;
    ArrayList localArrayList3 = new ArrayList();
    this.mVisibleItems = localArrayList3;
    this.mIsVisibleItemsStale = true;
    ArrayList localArrayList4 = new ArrayList();
    this.mActionItems = localArrayList4;
    ArrayList localArrayList5 = new ArrayList();
    this.mNonActionItems = localArrayList5;
    this.mIsActionItemsStale = true;
    setShortcutsVisibleInner(true);
  }

  private MenuItem addInternal(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    int i = getOrdering(paramInt3);
    int j = this.mDefaultShowAsAction;
    MenuBuilder localMenuBuilder = this;
    int k = paramInt1;
    int m = paramInt2;
    int n = paramInt3;
    CharSequence localCharSequence = paramCharSequence;
    MenuItemImpl localMenuItemImpl = new MenuItemImpl(localMenuBuilder, k, m, n, i, localCharSequence, j);
    if (this.mCurrentMenuInfo != null)
    {
      ContextMenu.ContextMenuInfo localContextMenuInfo = this.mCurrentMenuInfo;
      localMenuItemImpl.setMenuInfo(localContextMenuInfo);
    }
    ArrayList localArrayList = this.mItems;
    int i1 = findInsertIndex(this.mItems, i);
    localArrayList.add(i1, localMenuItemImpl);
    onItemsChanged(true);
    return localMenuItemImpl;
  }

  private void dispatchPresenterUpdate(boolean paramBoolean)
  {
    if (this.mPresenters.isEmpty())
      return;
    stopDispatchingItemsChanged();
    Iterator localIterator = this.mPresenters.iterator();
    while (localIterator.hasNext())
    {
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if (localMenuPresenter == null)
        boolean bool = this.mPresenters.remove(localWeakReference);
      else
        localMenuPresenter.updateMenuView(paramBoolean);
    }
    startDispatchingItemsChanged();
  }

  private boolean dispatchSubMenuSelected(SubMenuBuilder paramSubMenuBuilder)
  {
    boolean bool1;
    if (this.mPresenters.isEmpty())
      bool1 = false;
    while (true)
    {
      return bool1;
      bool1 = false;
      Iterator localIterator = this.mPresenters.iterator();
      while (localIterator.hasNext())
      {
        WeakReference localWeakReference = (WeakReference)localIterator.next();
        MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
        if (localMenuPresenter == null)
          boolean bool2 = this.mPresenters.remove(localWeakReference);
        else if (!bool1)
          bool1 = localMenuPresenter.onSubMenuSelected(paramSubMenuBuilder);
      }
    }
  }

  private static int findInsertIndex(ArrayList<MenuItemImpl> paramArrayList, int paramInt)
  {
    int i = paramArrayList.size() + -1;
    if (i >= 0)
      if (((MenuItemImpl)paramArrayList.get(i)).getOrdering() > paramInt);
    for (int j = i + 1; ; j = 0)
    {
      return j;
      i += -1;
      break;
    }
  }

  private static int getOrdering(int paramInt)
  {
    int i = (0xFFFF0000 & paramInt) >> 16;
    if (i >= 0)
    {
      int j = sCategoryToOrder.length;
      if (i < j);
    }
    else
    {
      throw new IllegalArgumentException("order does not contain a valid category.");
    }
    int k = sCategoryToOrder[i] << 16;
    int m = 0xFFFF & paramInt;
    return k | m;
  }

  private void removeItemAtInt(int paramInt, boolean paramBoolean)
  {
    if (paramInt < 0)
      return;
    int i = this.mItems.size();
    if (paramInt >= i)
      return;
    Object localObject = this.mItems.remove(paramInt);
    if (!paramBoolean)
      return;
    onItemsChanged(true);
  }

  private void setHeaderInternal(int paramInt1, CharSequence paramCharSequence, int paramInt2, Drawable paramDrawable, View paramView)
  {
    Resources localResources = getResources();
    if (paramView != null)
    {
      this.mHeaderView = paramView;
      this.mHeaderTitle = null;
      this.mHeaderIcon = null;
      onItemsChanged(false);
      return;
    }
    if (paramInt1 > 0)
    {
      CharSequence localCharSequence = localResources.getText(paramInt1);
      this.mHeaderTitle = localCharSequence;
      label51: if (paramInt2 <= 0)
        break label89;
      Drawable localDrawable = localResources.getDrawable(paramInt2);
      this.mHeaderIcon = localDrawable;
    }
    while (true)
    {
      this.mHeaderView = null;
      break;
      if (paramCharSequence == null)
        break label51;
      this.mHeaderTitle = paramCharSequence;
      break label51;
      label89: if (paramDrawable != null)
        this.mHeaderIcon = paramDrawable;
    }
  }

  private void setShortcutsVisibleInner(boolean paramBoolean)
  {
    boolean bool = true;
    if ((paramBoolean) && (this.mResources.getConfiguration().keyboard != 1))
    {
      Resources localResources = this.mResources;
      int i = R.bool.abc_config_showMenuShortcutsWhenKeyboardPresent;
      if (!localResources.getBoolean(i));
    }
    while (true)
    {
      this.mShortcutsVisible = bool;
      return;
      bool = false;
    }
  }

  public MenuItem add(int paramInt)
  {
    String str = this.mResources.getString(paramInt);
    return addInternal(0, 0, 0, str);
  }

  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    String str = this.mResources.getString(paramInt4);
    return addInternal(paramInt1, paramInt2, paramInt3, str);
  }

  public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    return addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
  }

  public MenuItem add(CharSequence paramCharSequence)
  {
    return addInternal(0, 0, 0, paramCharSequence);
  }

  public int addIntentOptions(int paramInt1, int paramInt2, int paramInt3, ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent, int paramInt4, MenuItem[] paramArrayOfMenuItem)
  {
    PackageManager localPackageManager = this.mContext.getPackageManager();
    ComponentName localComponentName1 = paramComponentName;
    Intent[] arrayOfIntent = paramArrayOfIntent;
    Intent localIntent1 = paramIntent;
    List localList = localPackageManager.queryIntentActivityOptions(localComponentName1, arrayOfIntent, localIntent1, 0);
    int i;
    int j;
    label64: ResolveInfo localResolveInfo;
    Intent localIntent2;
    if (localList != null)
    {
      i = localList.size();
      if ((paramInt4 & 0x1) == 0)
        removeGroup(paramInt1);
      j = 0;
      if (j >= i)
        break label264;
      localResolveInfo = (ResolveInfo)localList.get(j);
      localIntent2 = new android/content/Intent;
      if (localResolveInfo.specificIndex >= 0)
        break label247;
    }
    label247: int n;
    for (Intent localIntent3 = paramIntent; ; localIntent3 = paramArrayOfIntent[n])
    {
      localIntent2.<init>(localIntent3);
      String str1 = localResolveInfo.activityInfo.applicationInfo.packageName;
      String str2 = localResolveInfo.activityInfo.name;
      ComponentName localComponentName2 = new ComponentName(str1, str2);
      Intent localIntent4 = localIntent2.setComponent(localComponentName2);
      CharSequence localCharSequence = localResolveInfo.loadLabel(localPackageManager);
      int k = paramInt3;
      MenuItem localMenuItem1 = add(paramInt1, paramInt2, k, localCharSequence);
      Drawable localDrawable = localResolveInfo.loadIcon(localPackageManager);
      MenuItem localMenuItem2 = localMenuItem1.setIcon(localDrawable).setIntent(localIntent2);
      if ((paramArrayOfMenuItem != null) && (localResolveInfo.specificIndex >= 0))
      {
        int m = localResolveInfo.specificIndex;
        paramArrayOfMenuItem[m] = localMenuItem2;
      }
      j += 1;
      break label64;
      i = 0;
      break;
      n = localResolveInfo.specificIndex;
    }
    label264: return i;
  }

  public void addMenuPresenter(MenuPresenter paramMenuPresenter)
  {
    CopyOnWriteArrayList localCopyOnWriteArrayList = this.mPresenters;
    WeakReference localWeakReference = new WeakReference(paramMenuPresenter);
    boolean bool = localCopyOnWriteArrayList.add(localWeakReference);
    Context localContext = this.mContext;
    paramMenuPresenter.initForMenu(localContext, this);
    this.mIsActionItemsStale = true;
  }

  public SubMenu addSubMenu(int paramInt)
  {
    String str = this.mResources.getString(paramInt);
    return addSubMenu(0, 0, 0, str);
  }

  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    String str = this.mResources.getString(paramInt4);
    return addSubMenu(paramInt1, paramInt2, paramInt3, str);
  }

  public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence)
  {
    MenuItemImpl localMenuItemImpl = (MenuItemImpl)addInternal(paramInt1, paramInt2, paramInt3, paramCharSequence);
    Context localContext = this.mContext;
    SubMenuBuilder localSubMenuBuilder = new SubMenuBuilder(localContext, this, localMenuItemImpl);
    localMenuItemImpl.setSubMenu(localSubMenuBuilder);
    return localSubMenuBuilder;
  }

  public SubMenu addSubMenu(CharSequence paramCharSequence)
  {
    return addSubMenu(0, 0, 0, paramCharSequence);
  }

  public void changeMenuMode()
  {
    if (this.mCallback == null)
      return;
    this.mCallback.onMenuModeChange(this);
  }

  public void clear()
  {
    if (this.mExpandedItem != null)
    {
      MenuItemImpl localMenuItemImpl = this.mExpandedItem;
      boolean bool = collapseItemActionView(localMenuItemImpl);
    }
    this.mItems.clear();
    onItemsChanged(true);
  }

  public void clearHeader()
  {
    this.mHeaderIcon = null;
    this.mHeaderTitle = null;
    this.mHeaderView = null;
    onItemsChanged(false);
  }

  public void close()
  {
    close(true);
  }

  final void close(boolean paramBoolean)
  {
    if (this.mIsClosing)
      return;
    this.mIsClosing = true;
    Iterator localIterator = this.mPresenters.iterator();
    while (localIterator.hasNext())
    {
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if (localMenuPresenter == null)
        boolean bool = this.mPresenters.remove(localWeakReference);
      else
        localMenuPresenter.onCloseMenu(this, paramBoolean);
    }
    this.mIsClosing = false;
  }

  public boolean collapseItemActionView(MenuItemImpl paramMenuItemImpl)
  {
    boolean bool1;
    if ((this.mPresenters.isEmpty()) || (this.mExpandedItem != paramMenuItemImpl))
      bool1 = false;
    while (true)
    {
      return bool1;
      bool1 = false;
      stopDispatchingItemsChanged();
      Iterator localIterator = this.mPresenters.iterator();
      MenuPresenter localMenuPresenter;
      do
        while (true)
        {
          if (!localIterator.hasNext())
            break label97;
          WeakReference localWeakReference = (WeakReference)localIterator.next();
          localMenuPresenter = (MenuPresenter)localWeakReference.get();
          if (localMenuPresenter != null)
            break;
          boolean bool2 = this.mPresenters.remove(localWeakReference);
        }
      while (!localMenuPresenter.collapseItemActionView(this, paramMenuItemImpl));
      label97: startDispatchingItemsChanged();
      if (bool1)
        this.mExpandedItem = null;
    }
  }

  boolean dispatchMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem)
  {
    if ((this.mCallback != null) && (this.mCallback.onMenuItemSelected(paramMenuBuilder, paramMenuItem)));
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean expandItemActionView(MenuItemImpl paramMenuItemImpl)
  {
    boolean bool1;
    if (this.mPresenters.isEmpty())
      bool1 = false;
    while (true)
    {
      return bool1;
      bool1 = false;
      stopDispatchingItemsChanged();
      Iterator localIterator = this.mPresenters.iterator();
      MenuPresenter localMenuPresenter;
      do
        while (true)
        {
          if (!localIterator.hasNext())
            break label89;
          WeakReference localWeakReference = (WeakReference)localIterator.next();
          localMenuPresenter = (MenuPresenter)localWeakReference.get();
          if (localMenuPresenter != null)
            break;
          boolean bool2 = this.mPresenters.remove(localWeakReference);
        }
      while (!localMenuPresenter.expandItemActionView(this, paramMenuItemImpl));
      label89: startDispatchingItemsChanged();
      if (bool1)
        this.mExpandedItem = paramMenuItemImpl;
    }
  }

  public int findGroupIndex(int paramInt)
  {
    return findGroupIndex(paramInt, 0);
  }

  public int findGroupIndex(int paramInt1, int paramInt2)
  {
    int i = size();
    if (paramInt2 < 0)
      paramInt2 = 0;
    int j = paramInt2;
    if (j < i)
      if (((MenuItemImpl)this.mItems.get(j)).getGroupId() == paramInt1);
    while (true)
    {
      return j;
      j += 1;
      break;
      j = -1;
    }
  }

  public MenuItem findItem(int paramInt)
  {
    int i = size();
    int j = 0;
    Object localObject;
    if (j < i)
    {
      localObject = (MenuItemImpl)this.mItems.get(j);
      if (((MenuItemImpl)localObject).getItemId() == paramInt);
    }
    while (true)
    {
      return localObject;
      if (((MenuItemImpl)localObject).hasSubMenu())
      {
        MenuItem localMenuItem = ((MenuItemImpl)localObject).getSubMenu().findItem(paramInt);
        if (localMenuItem != null)
          localObject = localMenuItem;
      }
      else
      {
        j += 1;
        break;
        localObject = null;
      }
    }
  }

  public int findItemIndex(int paramInt)
  {
    int i = size();
    int j = 0;
    if (j < i)
      if (((MenuItemImpl)this.mItems.get(j)).getItemId() == paramInt);
    while (true)
    {
      return j;
      j += 1;
      break;
      j = -1;
    }
  }

  MenuItemImpl findItemWithShortcutForKey(int paramInt, KeyEvent paramKeyEvent)
  {
    Object localObject = null;
    ArrayList localArrayList = this.mTempShortcutItemList;
    localArrayList.clear();
    findItemsWithShortcutForKey(localArrayList, paramInt, paramKeyEvent);
    if (localArrayList.isEmpty());
    label200: label207: 
    while (true)
    {
      return localObject;
      int i = paramKeyEvent.getMetaState();
      KeyCharacterMap.KeyData localKeyData = new KeyCharacterMap.KeyData();
      boolean bool1 = paramKeyEvent.getKeyData(localKeyData);
      int j = localArrayList.size();
      if (j == 1)
      {
        localObject = (MenuItemImpl)localArrayList.get(0);
      }
      else
      {
        boolean bool2 = isQwertyMode();
        int k = 0;
        while (true)
        {
          if (k >= j)
            break label207;
          MenuItemImpl localMenuItemImpl = (MenuItemImpl)localArrayList.get(k);
          if (bool2);
          for (int m = localMenuItemImpl.getAlphabeticShortcut(); ; m = localMenuItemImpl.getNumericShortcut())
          {
            int n = localKeyData.meta[0];
            if ((m == n) || ((i & 0x2) != 0))
            {
              int i1 = localKeyData.meta[2];
              if (((m == i1) || ((i & 0x2) == 0)) && ((!bool2) || (m != 8) || (paramInt != 67)))
                break label200;
            }
            localObject = localMenuItemImpl;
            break;
          }
          k += 1;
        }
      }
    }
  }

  void findItemsWithShortcutForKey(List<MenuItemImpl> paramList, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool1 = isQwertyMode();
    int i = paramKeyEvent.getMetaState();
    KeyCharacterMap.KeyData localKeyData = new KeyCharacterMap.KeyData();
    if ((!paramKeyEvent.getKeyData(localKeyData)) && (paramInt != 67))
      return;
    int j = this.mItems.size();
    int k = 0;
    if (k >= j)
      return;
    MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mItems.get(k);
    if (localMenuItemImpl.hasSubMenu())
      ((MenuBuilder)localMenuItemImpl.getSubMenu()).findItemsWithShortcutForKey(paramList, paramInt, paramKeyEvent);
    if (bool1);
    for (int m = localMenuItemImpl.getAlphabeticShortcut(); ; m = localMenuItemImpl.getNumericShortcut())
    {
      if (((i & 0x5) == 0) && (m != null))
      {
        int n = localKeyData.meta[0];
        if (m != n)
        {
          int i1 = localKeyData.meta[2];
          if ((m != i1) && ((!bool1) || (m != 8) || (paramInt != 67)));
        }
        else if (localMenuItemImpl.isEnabled())
        {
          boolean bool2 = paramList.add(localMenuItemImpl);
        }
      }
      k += 1;
      break;
    }
  }

  public void flagActionItems()
  {
    if (!this.mIsActionItemsStale)
      return;
    boolean bool1 = false;
    Iterator localIterator = this.mPresenters.iterator();
    while (localIterator.hasNext())
    {
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if (localMenuPresenter == null)
      {
        boolean bool2 = this.mPresenters.remove(localWeakReference);
      }
      else
      {
        boolean bool3 = localMenuPresenter.flagActionItems();
        bool1 |= bool3;
      }
    }
    if (bool1)
    {
      this.mActionItems.clear();
      this.mNonActionItems.clear();
      ArrayList localArrayList1 = getVisibleItems();
      int i = localArrayList1.size();
      int j = 0;
      if (j < i)
      {
        MenuItemImpl localMenuItemImpl = (MenuItemImpl)localArrayList1.get(j);
        if (localMenuItemImpl.isActionButton())
          boolean bool4 = this.mActionItems.add(localMenuItemImpl);
        while (true)
        {
          j += 1;
          break;
          boolean bool5 = this.mNonActionItems.add(localMenuItemImpl);
        }
      }
    }
    else
    {
      this.mActionItems.clear();
      this.mNonActionItems.clear();
      ArrayList localArrayList2 = this.mNonActionItems;
      ArrayList localArrayList3 = getVisibleItems();
      boolean bool6 = localArrayList2.addAll(localArrayList3);
    }
    this.mIsActionItemsStale = false;
  }

  ArrayList<MenuItemImpl> getActionItems()
  {
    flagActionItems();
    return this.mActionItems;
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public MenuItemImpl getExpandedItem()
  {
    return this.mExpandedItem;
  }

  public Drawable getHeaderIcon()
  {
    return this.mHeaderIcon;
  }

  public CharSequence getHeaderTitle()
  {
    return this.mHeaderTitle;
  }

  public View getHeaderView()
  {
    return this.mHeaderView;
  }

  public MenuItem getItem(int paramInt)
  {
    return (MenuItem)this.mItems.get(paramInt);
  }

  ArrayList<MenuItemImpl> getNonActionItems()
  {
    flagActionItems();
    return this.mNonActionItems;
  }

  boolean getOptionalIconsVisible()
  {
    return this.mOptionalIconsVisible;
  }

  Resources getResources()
  {
    return this.mResources;
  }

  public MenuBuilder getRootMenu()
  {
    return this;
  }

  ArrayList<MenuItemImpl> getVisibleItems()
  {
    if (!this.mIsVisibleItemsStale);
    for (ArrayList localArrayList = this.mVisibleItems; ; localArrayList = this.mVisibleItems)
    {
      return localArrayList;
      this.mVisibleItems.clear();
      int i = this.mItems.size();
      int j = 0;
      while (j < i)
      {
        MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mItems.get(j);
        if (localMenuItemImpl.isVisible())
          boolean bool = this.mVisibleItems.add(localMenuItemImpl);
        j += 1;
      }
      this.mIsVisibleItemsStale = false;
      this.mIsActionItemsStale = true;
    }
  }

  public boolean hasVisibleItems()
  {
    int i = size();
    int j = 0;
    if (j < i)
      if (!((MenuItemImpl)this.mItems.get(j)).isVisible());
    for (boolean bool = true; ; bool = false)
    {
      return bool;
      j += 1;
      break;
    }
  }

  boolean isQwertyMode()
  {
    return this.mQwertyMode;
  }

  public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent)
  {
    if (findItemWithShortcutForKey(paramInt, paramKeyEvent) != null);
    for (boolean bool = true; ; bool = false)
      return bool;
  }

  public boolean isShortcutsVisible()
  {
    return this.mShortcutsVisible;
  }

  void onItemActionRequestChanged(MenuItemImpl paramMenuItemImpl)
  {
    this.mIsActionItemsStale = true;
    onItemsChanged(true);
  }

  void onItemVisibleChanged(MenuItemImpl paramMenuItemImpl)
  {
    this.mIsVisibleItemsStale = true;
    onItemsChanged(true);
  }

  void onItemsChanged(boolean paramBoolean)
  {
    if (!this.mPreventDispatchingItemsChanged)
    {
      if (paramBoolean)
      {
        this.mIsVisibleItemsStale = true;
        this.mIsActionItemsStale = true;
      }
      dispatchPresenterUpdate(paramBoolean);
      return;
    }
    this.mItemsChangedWhileDispatchPrevented = true;
  }

  public boolean performIdentifierAction(int paramInt1, int paramInt2)
  {
    MenuItem localMenuItem = findItem(paramInt1);
    return performItemAction(localMenuItem, paramInt2);
  }

  public boolean performItemAction(MenuItem paramMenuItem, int paramInt)
  {
    MenuItemImpl localMenuItemImpl = (MenuItemImpl)paramMenuItem;
    boolean bool1;
    if ((localMenuItemImpl == null) || (!localMenuItemImpl.isEnabled()))
      bool1 = false;
    while (true)
    {
      return bool1;
      bool1 = localMenuItemImpl.invoke();
      ActionProvider localActionProvider = localMenuItemImpl.getSupportActionProvider();
      if ((localActionProvider != null) && (localActionProvider.hasSubMenu()));
      for (int i = 1; ; i = 0)
      {
        if (!localMenuItemImpl.hasCollapsibleActionView())
          break label89;
        boolean bool2 = localMenuItemImpl.expandActionView();
        bool1 |= bool2;
        if (!bool1)
          break;
        close(true);
        break;
      }
      label89: if ((localMenuItemImpl.hasSubMenu()) || (i != 0))
      {
        close(false);
        if (!localMenuItemImpl.hasSubMenu())
        {
          Context localContext = getContext();
          SubMenuBuilder localSubMenuBuilder1 = new SubMenuBuilder(localContext, this, localMenuItemImpl);
          localMenuItemImpl.setSubMenu(localSubMenuBuilder1);
        }
        SubMenuBuilder localSubMenuBuilder2 = (SubMenuBuilder)localMenuItemImpl.getSubMenu();
        if (i != 0)
          localActionProvider.onPrepareSubMenu(localSubMenuBuilder2);
        boolean bool3 = dispatchSubMenuSelected(localSubMenuBuilder2);
        bool1 |= bool3;
        if (!bool1)
          close(true);
      }
      else if ((paramInt & 0x1) == 0)
      {
        close(true);
      }
    }
  }

  public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2)
  {
    MenuItemImpl localMenuItemImpl = findItemWithShortcutForKey(paramInt1, paramKeyEvent);
    boolean bool = false;
    if (localMenuItemImpl != null)
      bool = performItemAction(localMenuItemImpl, paramInt2);
    if ((paramInt2 & 0x2) != 0)
      close(true);
    return bool;
  }

  public void removeGroup(int paramInt)
  {
    int i = findGroupIndex(paramInt);
    if (i < 0)
      return;
    int j = this.mItems.size() - i;
    int m;
    for (int k = 0; ; k = m)
    {
      m = k + 1;
      if ((k >= j) || (((MenuItemImpl)this.mItems.get(i)).getGroupId() == paramInt))
        break;
      removeItemAtInt(i, false);
    }
    onItemsChanged(true);
  }

  public void removeItem(int paramInt)
  {
    int i = findItemIndex(paramInt);
    removeItemAtInt(i, true);
  }

  public void removeMenuPresenter(MenuPresenter paramMenuPresenter)
  {
    Iterator localIterator = this.mPresenters.iterator();
    while (true)
    {
      if (!localIterator.hasNext())
        return;
      WeakReference localWeakReference = (WeakReference)localIterator.next();
      MenuPresenter localMenuPresenter = (MenuPresenter)localWeakReference.get();
      if ((localMenuPresenter == null) || (localMenuPresenter == paramMenuPresenter))
        boolean bool = this.mPresenters.remove(localWeakReference);
    }
  }

  public void setCallback(Callback paramCallback)
  {
    this.mCallback = paramCallback;
  }

  void setExclusiveItemChecked(MenuItem paramMenuItem)
  {
    int i = paramMenuItem.getGroupId();
    int j = this.mItems.size();
    int k = 0;
    if (k >= j)
      return;
    MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mItems.get(k);
    if ((localMenuItemImpl.getGroupId() == i) || (!localMenuItemImpl.isExclusiveCheckable()));
    while (!localMenuItemImpl.isCheckable())
    {
      k += 1;
      break;
    }
    if (localMenuItemImpl == paramMenuItem);
    for (boolean bool = true; ; bool = false)
    {
      localMenuItemImpl.setCheckedInt(bool);
      break;
    }
  }

  public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    int i = this.mItems.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mItems.get(j);
      if (localMenuItemImpl.getGroupId() != paramInt)
      {
        localMenuItemImpl.setExclusiveCheckable(paramBoolean2);
        MenuItem localMenuItem = localMenuItemImpl.setCheckable(paramBoolean1);
      }
      j += 1;
    }
  }

  public void setGroupEnabled(int paramInt, boolean paramBoolean)
  {
    int i = this.mItems.size();
    int j = 0;
    while (true)
    {
      if (j >= i)
        return;
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mItems.get(j);
      if (localMenuItemImpl.getGroupId() != paramInt)
        MenuItem localMenuItem = localMenuItemImpl.setEnabled(paramBoolean);
      j += 1;
    }
  }

  public void setGroupVisible(int paramInt, boolean paramBoolean)
  {
    int i = this.mItems.size();
    int j = 0;
    int k = 0;
    while (k < i)
    {
      MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mItems.get(k);
      if ((localMenuItemImpl.getGroupId() != paramInt) && (localMenuItemImpl.setVisibleInt(paramBoolean)))
        j = 1;
      k += 1;
    }
    if (j == 0)
      return;
    onItemsChanged(true);
  }

  protected MenuBuilder setHeaderIconInt(Drawable paramDrawable)
  {
    MenuBuilder localMenuBuilder = this;
    int i = 0;
    Drawable localDrawable = paramDrawable;
    View localView = null;
    localMenuBuilder.setHeaderInternal(0, null, i, localDrawable, localView);
    return this;
  }

  protected MenuBuilder setHeaderTitleInt(CharSequence paramCharSequence)
  {
    MenuBuilder localMenuBuilder = this;
    CharSequence localCharSequence = paramCharSequence;
    int i = 0;
    View localView = null;
    localMenuBuilder.setHeaderInternal(0, localCharSequence, i, null, localView);
    return this;
  }

  protected MenuBuilder setHeaderViewInt(View paramView)
  {
    MenuBuilder localMenuBuilder = this;
    int i = 0;
    Drawable localDrawable = null;
    View localView = paramView;
    localMenuBuilder.setHeaderInternal(0, null, i, localDrawable, localView);
    return this;
  }

  public void setQwertyMode(boolean paramBoolean)
  {
    this.mQwertyMode = paramBoolean;
    onItemsChanged(false);
  }

  public int size()
  {
    return this.mItems.size();
  }

  public void startDispatchingItemsChanged()
  {
    this.mPreventDispatchingItemsChanged = false;
    if (!this.mItemsChangedWhileDispatchPrevented)
      return;
    this.mItemsChangedWhileDispatchPrevented = false;
    onItemsChanged(true);
  }

  public void stopDispatchingItemsChanged()
  {
    if (this.mPreventDispatchingItemsChanged)
      return;
    this.mPreventDispatchingItemsChanged = true;
    this.mItemsChangedWhileDispatchPrevented = false;
  }

  public static abstract interface ItemInvoker
  {
    public abstract boolean invokeItem(MenuItemImpl paramMenuItemImpl);
  }

  public static abstract interface Callback
  {
    public abstract boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem);

    public abstract void onMenuModeChange(MenuBuilder paramMenuBuilder);
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuBuilder
 * JD-Core Version:    0.6.2
 */