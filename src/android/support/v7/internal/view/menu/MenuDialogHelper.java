package android.support.v7.internal.view.menu;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v7.appcompat.R.layout;
import android.support.v7.appcompat.R.style;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ListAdapter;

public class MenuDialogHelper
  implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener, DialogInterface.OnKeyListener, MenuPresenter.Callback
{
  private AlertDialog mDialog;
  private MenuBuilder mMenu;
  ListMenuPresenter mPresenter;
  private MenuPresenter.Callback mPresenterCallback;

  public MenuDialogHelper(MenuBuilder paramMenuBuilder)
  {
    this.mMenu = paramMenuBuilder;
  }

  public void dismiss()
  {
    if (this.mDialog == null)
      return;
    this.mDialog.dismiss();
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    MenuBuilder localMenuBuilder = this.mMenu;
    MenuItemImpl localMenuItemImpl = (MenuItemImpl)this.mPresenter.getAdapter().getItem(paramInt);
    boolean bool = localMenuBuilder.performItemAction(localMenuItemImpl, 0);
  }

  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      MenuBuilder localMenuBuilder = this.mMenu;
      if (paramMenuBuilder != localMenuBuilder);
    }
    else
    {
      dismiss();
    }
    if (this.mPresenterCallback == null)
      return;
    this.mPresenterCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    ListMenuPresenter localListMenuPresenter = this.mPresenter;
    MenuBuilder localMenuBuilder = this.mMenu;
    localListMenuPresenter.onCloseMenu(localMenuBuilder, true);
  }

  public boolean onKey(DialogInterface paramDialogInterface, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    Window localWindow;
    View localView;
    KeyEvent.DispatcherState localDispatcherState;
    if ((paramInt == 82) || (paramInt == 4))
      if ((paramKeyEvent.getAction() == 0) && (paramKeyEvent.getRepeatCount() == 0))
      {
        localWindow = this.mDialog.getWindow();
        if (localWindow == null)
          break label155;
        localView = localWindow.getDecorView();
        if (localView == null)
          break label155;
        localDispatcherState = localView.getKeyDispatcherState();
        if (localDispatcherState == null)
          break label155;
        localDispatcherState.startTracking(paramKeyEvent, this);
      }
    while (true)
    {
      return bool;
      if ((paramKeyEvent.getAction() == 1) && (!paramKeyEvent.isCanceled()))
      {
        localWindow = this.mDialog.getWindow();
        if (localWindow != null)
        {
          localView = localWindow.getDecorView();
          if (localView != null)
          {
            localDispatcherState = localView.getKeyDispatcherState();
            if ((localDispatcherState != null) && (localDispatcherState.isTracking(paramKeyEvent)))
            {
              this.mMenu.close(true);
              paramDialogInterface.dismiss();
            }
          }
        }
      }
      else
      {
        label155: bool = this.mMenu.performShortcut(paramInt, paramKeyEvent, 0);
      }
    }
  }

  public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder)
  {
    if (this.mPresenterCallback != null);
    for (boolean bool = this.mPresenterCallback.onOpenSubMenu(paramMenuBuilder); ; bool = false)
      return bool;
  }

  public void show(IBinder paramIBinder)
  {
    MenuBuilder localMenuBuilder1 = this.mMenu;
    Context localContext = localMenuBuilder1.getContext();
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(localContext);
    int i = R.layout.abc_list_menu_item_layout;
    int j = R.style.Theme_AppCompat_CompactMenu_Dialog;
    ListMenuPresenter localListMenuPresenter1 = new ListMenuPresenter(i, j);
    this.mPresenter = localListMenuPresenter1;
    this.mPresenter.setCallback(this);
    MenuBuilder localMenuBuilder2 = this.mMenu;
    ListMenuPresenter localListMenuPresenter2 = this.mPresenter;
    localMenuBuilder2.addMenuPresenter(localListMenuPresenter2);
    ListAdapter localListAdapter = this.mPresenter.getAdapter();
    AlertDialog.Builder localBuilder2 = localBuilder1.setAdapter(localListAdapter, this);
    View localView = localMenuBuilder1.getHeaderView();
    if (localView != null)
      AlertDialog.Builder localBuilder3 = localBuilder1.setCustomTitle(localView);
    while (true)
    {
      AlertDialog.Builder localBuilder4 = localBuilder1.setOnKeyListener(this);
      AlertDialog localAlertDialog = localBuilder1.create();
      this.mDialog = localAlertDialog;
      this.mDialog.setOnDismissListener(this);
      WindowManager.LayoutParams localLayoutParams = this.mDialog.getWindow().getAttributes();
      localLayoutParams.type = 1003;
      if (paramIBinder != null)
        localLayoutParams.token = paramIBinder;
      int k = localLayoutParams.flags | 0x20000;
      localLayoutParams.flags = k;
      this.mDialog.show();
      return;
      Drawable localDrawable = localMenuBuilder1.getHeaderIcon();
      AlertDialog.Builder localBuilder5 = localBuilder1.setIcon(localDrawable);
      CharSequence localCharSequence = localMenuBuilder1.getHeaderTitle();
      AlertDialog.Builder localBuilder6 = localBuilder5.setTitle(localCharSequence);
    }
  }
}

/* Location:           /home/vovs/Downloads/apk_decompile/tmp/code_dec/com.google.android.music-2/classes_dex2jar.jar
 * Qualified Name:     android.support.v7.internal.view.menu.MenuDialogHelper
 * JD-Core Version:    0.6.2
 */